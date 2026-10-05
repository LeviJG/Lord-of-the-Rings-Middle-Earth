package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRHobbitOvenBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

/**
 * LOTRItemBrandingIron: a hundred uses, mended with iron. Fresh, it is named
 * (its screen, right-clicking the air); named, it is heated at a fire or a
 * burning furnace, forge or hobbit oven; hot, it burns its name into an
 * animal or an NPC that may be renamed, which is kept for good. It cools
 * after every fifth use. Its brand records who branded the creature, which an
 * operator in creative is told by clicking it with a clock. Crafted with
 * iron, a named iron loses its name.
 */
public class LOTRBrandingIronItem extends Item {

    public static final int MAX_NAME_LENGTH = 64;

    /** The creature's "LOTRBrander": who branded it. */
    public static final AttachmentType<UUID> BRANDER = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "brander"),
            builder -> builder.persistent(UUIDUtil.CODEC));

    /** Opens the naming screen; set by the client. */
    public static Runnable openScreen = () -> {
    };

    public LOTRBrandingIronItem(Properties properties) {
        super(properties);
    }

    public static @Nullable String getBrandName(ItemStack stack) {
        String name = stack.get(LOTRDataComponents.BRAND_NAME);
        return StringUtils.isBlank(name) ? null : name;
    }

    public static boolean hasBrandName(ItemStack stack) {
        return getBrandName(stack) != null;
    }

    public static boolean isHeated(ItemStack stack) {
        return stack.has(LOTRDataComponents.HOT_IRON);
    }

    public static void setHeated(ItemStack stack, boolean hot) {
        if (hot) {
            stack.set(LOTRDataComponents.HOT_IRON, Unit.INSTANCE);
        } else {
            stack.remove(LOTRDataComponents.HOT_IRON);
        }
    }

    public static String trimAcceptableBrandName(String name) {
        name = StringUtils.trim(name);
        return name.length() > MAX_NAME_LENGTH ? name.substring(0, MAX_NAME_LENGTH) : name;
    }

    /** getItemStackDisplayName: "Unnamed Branding Iron", or "Branding Iron: Name". */
    @Override
    public Component getName(ItemStack stack) {
        Component name = super.getName(stack);
        String brand = getBrandName(stack);
        return brand != null ? Component.translatable("item.lotr.brandingIron.named", name, brand)
                : Component.translatable("item.lotr.brandingIron.unnamed", name);
    }

    /** onItemRightClick: an unnamed iron opens its naming screen. */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!hasBrandName(player.getItemInHand(hand))) {
            if (level.isClientSide()) {
                openScreen.run();
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    /** onItemUse: a named, cold iron is heated at a burning furnace, forge or oven, or a fire. */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        if (!hasBrandName(stack) || isHeated(stack)) {
            return InteractionResult.PASS;
        }
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (isHotBlock(level.getBlockState(pos))
                || level.getBlockState(pos.relative(context.getClickedFace())).getBlock() instanceof BaseFireBlock) {
            setHeated(stack, true);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static boolean isHotBlock(BlockState state) {
        return (state.getBlock() instanceof AbstractFurnaceBlock || state.getBlock() instanceof LOTRHobbitOvenBlock)
                && state.getValueOrElse(AbstractFurnaceBlock.LIT, false);
    }

    /** itemInteractionForEntity: a hot, named iron brands an animal or a renamable NPC. */
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        String brandName = getBrandName(stack);
        if (!isHeated(stack) || brandName == null || !(entity instanceof Mob mob)) {
            return InteractionResult.PASS;
        }
        boolean acceptable = mob instanceof Animal || mob instanceof LOTRNPCEntity npc && npc.canRenameNPC();
        Component current = mob.getCustomName();
        if (!acceptable || current != null && current.getString().equals(brandName)) {
            return InteractionResult.PASS;
        }
        Level level = mob.level();
        if (!level.isClientSide()) {
            mob.setCustomName(Component.literal(brandName));
            mob.setPersistenceRequired();
            mob.playAmbientSound();
            mob.getJumpControl().jump();
            level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.FIRE_EXTINGUISH, mob.getSoundSource(),
                    0.5f, 2.6f + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.8f);
            int preDamage = stack.getDamageValue();
            stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
            if (preDamage / 5 != stack.getDamageValue() / 5) {
                setHeated(stack, false);
            }
            mob.setAttached(BRANDER, player.getUUID());
        }
        player.swing(hand);
        return InteractionResult.SUCCESS;
    }

    /**
     * LOTRPacketBrandingIron: the name the player gave an unnamed iron in hand.
     */
    public record NamePayload(String brandName) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<NamePayload> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "branding_iron"));
        public static final StreamCodec<RegistryFriendlyByteBuf, NamePayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.stringUtf8(256), NamePayload::brandName, NamePayload::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void init() {
        PayloadTypeRegistry.serverboundPlay().register(NamePayload.TYPE, NamePayload.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(NamePayload.TYPE, (payload, context) -> {
            ItemStack stack = context.player().getMainHandItem();
            String name = trimAcceptableBrandName(payload.brandName());
            if (stack.getItem() instanceof LOTRBrandingIronItem && !StringUtils.isBlank(name) && !hasBrandName(stack)) {
                stack.set(LOTRDataComponents.BRAND_NAME, name);
            }
        });
        // LOTREventHandler.onEntityInteract: an operator in creative with a clock is told who branded a creature.
        UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
            if (player instanceof ServerPlayer serverPlayer && player.getAbilities().instabuild
                    && player.getItemInHand(hand).is(Items.CLOCK) && entity instanceof Mob
                    && !(entity instanceof LOTRNPCEntity npc && npc.hiredNPCInfo.isActive)
                    && serverPlayer.level().getServer().getPlayerList().isOp(serverPlayer.nameAndId())) {
                UUID brander = entity.getAttached(BRANDER);
                if (brander != null) {
                    String name = serverPlayer.level().getServer().services().nameToIdCache().get(brander)
                            .map(NameAndId::name).orElse(brander.toString());
                    player.sendSystemMessage(Component.literal("Entity was branded by " + name)
                            .withStyle(ChatFormatting.GREEN));
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        });
    }
}
