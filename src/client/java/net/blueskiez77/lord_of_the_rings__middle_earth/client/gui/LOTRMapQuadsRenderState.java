package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.Arrays;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;

import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;
import org.jspecify.annotations.Nullable;

/**
 * Many plain coloured rectangles as one GUI element. Each fill is its own element, and every element
 * added is checked against those beneath it for overlap, so the menu map's hundreds of road dots and
 * waypoints, drawn a fill at a time, cost far more than drawing them. Rectangles are in the GUI's
 * coordinates, as floats, so they can sit on the screen's own pixels.
 */
final class LOTRMapQuadsRenderState implements GuiElementRenderState {

    private static final Matrix3x2fc IDENTITY = new Matrix3x2f();

    private final ScreenRectangle bounds;
    private final @Nullable ScreenRectangle scissorArea;
    private float[] coords = new float[256];
    private int[] colours = new int[64];
    private int count;

    LOTRMapQuadsRenderState(ScreenRectangle bounds, @Nullable ScreenRectangle scissorArea) {
        this.bounds = bounds;
        this.scissorArea = scissorArea;
    }

    void add(float x0, float y0, float x1, float y1, int colour) {
        if (this.count == this.colours.length) {
            this.colours = Arrays.copyOf(this.colours, this.count * 2);
            this.coords = Arrays.copyOf(this.coords, this.count * 8);
        }
        int i = this.count * 4;
        this.coords[i] = x0;
        this.coords[i + 1] = y0;
        this.coords[i + 2] = x1;
        this.coords[i + 3] = y1;
        this.colours[this.count++] = colour;
    }

    boolean isEmpty() {
        return this.count == 0;
    }

    @Override
    public void buildVertices(VertexConsumer consumer) {
        for (int q = 0; q < this.count; ++q) {
            int i = q * 4;
            float x0 = this.coords[i];
            float y0 = this.coords[i + 1];
            float x1 = this.coords[i + 2];
            float y1 = this.coords[i + 3];
            int colour = this.colours[q];
            consumer.addVertexWith2DPose(IDENTITY, x0, y0).setColor(colour);
            consumer.addVertexWith2DPose(IDENTITY, x0, y1).setColor(colour);
            consumer.addVertexWith2DPose(IDENTITY, x1, y1).setColor(colour);
            consumer.addVertexWith2DPose(IDENTITY, x1, y0).setColor(colour);
        }
    }

    @Override
    public RenderPipeline pipeline() {
        return RenderPipelines.GUI;
    }

    @Override
    public TextureSetup textureSetup() {
        return TextureSetup.noTexture();
    }

    @Override
    public @Nullable ScreenRectangle scissorArea() {
        return this.scissorArea;
    }

    @Override
    public ScreenRectangle bounds() {
        return this.bounds;
    }
}
