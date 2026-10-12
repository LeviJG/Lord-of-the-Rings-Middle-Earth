package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.function.Predicate;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRStructureTimelapse ("Structure Timelapse", /strTimelapse): a structure from a spawner built
 * block by block, with the interval between each. As the original, the structure's own code runs
 * on a thread of its own and sleeps after each block it sets; here each thing it does to the world
 * is handed to the server's thread, which does it and hands back the answer, so the world is only
 * ever touched there. Builds still running when the server stops are stopped.
 */
public final class LOTRStructureTimelapse {

    private static final List<Thread> allThreads = Collections.synchronizedList(new ArrayList<>());

    private LOTRStructureTimelapse() {
    }

    public static void init() {
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            synchronized (allThreads) {
                for (Thread thread : allThreads) {
                    thread.interrupt();
                }
                allThreads.clear();
            }
        });
    }

    /** start: the build run on its own thread, against the level through the server's thread. */
    public static void start(ServerLevel level, Predicate<WorldGenLevel> build) {
        MinecraftServer server = level.getServer();
        WorldGenLevel view = (WorldGenLevel) Proxy.newProxyInstance(LOTRStructureTimelapse.class.getClassLoader(),
                new Class<?>[]{WorldGenLevel.class}, new View(server, level));
        Thread thread = new Thread(() -> {
            try {
                build.test(view);
            } catch (TimelapseStopped ignored) {
            } catch (RuntimeException e) {
                LOTRMod.LOGGER.error("LOTR structure timelapse failed", e);
            } finally {
                allThreads.remove(Thread.currentThread());
            }
        }, "LOTR structure timelapse");
        thread.setDaemon(true);
        allThreads.add(thread);
        thread.start();
    }

    private static final class TimelapseStopped extends RuntimeException {
        TimelapseStopped() {
            super(null, null, false, false);
        }
    }

    private record View(MinecraftServer server, ServerLevel level) implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            switch (method.getName()) {
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "equals":
                    return proxy == args[0];
                case "toString":
                    return "LOTRStructureTimelapse[" + this.level + "]";
                default:
                    break;
            }
            if (Thread.currentThread().isInterrupted()) {
                throw new TimelapseStopped();
            }
            Object result;
            try {
                result = this.server.submit(() -> {
                    try {
                        return method.invoke(this.level, args);
                    } catch (InvocationTargetException e) {
                        throw new CompletionException(e.getCause());
                    } catch (IllegalAccessException e) {
                        throw new CompletionException(e);
                    }
                }).join();
            } catch (CompletionException e) {
                throw e.getCause() != null ? e.getCause() : e;
            }
            if ("setBlock".equals(method.getName()) && LOTRConfig.strTimelapse) {
                try {
                    Thread.sleep(LOTRConfig.strTimelapseInterval);
                } catch (InterruptedException e) {
                    throw new TimelapseStopped();
                }
            }
            return result;
        }
    }
}
