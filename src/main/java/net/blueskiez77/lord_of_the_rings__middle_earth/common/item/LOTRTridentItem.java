package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRThrownTridentEntity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;

/**
 * LOTRItemTrident, on vanilla's TridentItem.
 *
 * <p>DIVERGENCE, at the user's asking, and a large one. The original's trident
 * is a POLEARM that also FISHES: LOTRItemTrident extends LOTRItemPolearm, which
 * is a sword with reach, and holding right click over water reeled a fish in.
 * It could not be thrown at all. This is built on TridentItem instead, so it
 * throws, comes back with Loyalty, spins you through the rain with Riptide and
 * calls lightning with Channeling -- vanilla's trident in every respect but its
 * numbers. The fishing is not ported and would want reimplementing on top.
 *
 * <p>What IS the original's is the damage. LOTRItemPolearm adds nothing to
 * LOTRItemSword, so a trident hits for {@code material damage + 4} like every
 * other blade in the mod -- six on vanilla iron, where a vanilla trident hits
 * for nine. The attack speed is vanilla's, since the swing and the throw are.
 */
public class LOTRTridentItem extends TridentItem implements LOTRModifiable {

    /** TridentItem.createAttributes' own figure. */
    private static final double ATTACK_SPEED = -2.9;

    public LOTRTridentItem(Properties properties) {
        super(properties);
    }

    /**
     * The properties a LOTR trident shares.
     *
     * <p>Everything vanilla's trident needs -- the TOOL component, the
     * durability, a repair item -- with the attack damage worked out the way
     * the rest of the tab is: the value passed is {@code lotrWeaponDamage - 1},
     * because 26.2 shows the total and 1.7.10 showed the modifier.
     *
     * <p>Enchantability is the material's own, as LOTRItemSword had it, rather
     * than the 1 vanilla gives its trident.
     */
    public static Item.Properties properties(ToolMaterial material, float damage) {
        return new Item.Properties()
                .durability(material.durability())
                .repairable(material.repairItems())
                .enchantable(material.enchantmentValue())
                .component(DataComponents.TOOL, TridentItem.createToolProperties())
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.ATTACK_DAMAGE,
                                new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, damage - 1.0,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_SPEED,
                                new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, ATTACK_SPEED,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)
                        .build());
    }

    /*
     * The wind-up animation for a throw: vanilla's own, ItemUseAnimation
     * .TRIDENT, inherited rather than overridden.
     *
     * <p>This used to return BOW, which is what the original's
     * getItemUseAction returns. It was faithful and it was still wrong here,
     * because the two mods do not mean the same thing by it: in 1.7.10 the
     * trident could not be thrown at all -- EnumAction.bow only drew the arm
     * back while you held right click over water to FISH. The throw is this
     * port's addition, at the user's asking, so the animation that belongs
     * with it is the one 26.2 draws a throw with.
     *
     * <p>BOW maps to HumanoidModel.ArmPose.BOW_AND_ARROW in third person,
     * which hauls on an invisible bowstring with both hands. TRIDENT maps to
     * ArmPose.THROW_TRIDENT, which cocks the arm back over the shoulder, and
     * in first person to ItemInHandRenderer's trident case -- translate(-0.5,
     * 0.7, 0.1) and XP(-55), the item raised to throw.
     *
     * <p>THROW_TRIDENT turns the hand nearly upside down, which is why vanilla
     * swaps models while the trident is in use: item/trident_throwing is
     * item/trident_in_hand rolled 180 degrees about Z. Without that roll the
     * item comes out of the raised fist pointing the wrong way -- the
     * "thrown, it is held in the hand backwards" LOTRThrownTridentRenderer's
     * comment records. lotr:item/dunlending_trident_throwing is that same
     * roll applied to the third-person block, selected by a
     * minecraft:using_item condition in assets/lotr/items/dunlending_trident
     * .json exactly as vanilla selects its own two.
     *
     * <p>The throwing model's display transforms are SOLVED, not tuned: each
     * one puts the sprite's shaft -- butt pixel (4,27) to tip pixel (31,0) of
     * dunlending_trident_in_hand.png -- on the line vanilla's TridentModel
     * occupies under item/trident_throwing, midpoint on midpoint, with the
     * texture's face towards the camera. That replicates ItemTransform.apply
     * (left-hand fix included) and the special model's scale(1, -1, -1). A
     * roll on handheld's translation alone got the angle right but left the
     * trident half a block too high and a quarter too far left, because
     * vanilla's model hangs below its origin where a sprite is centred.
     *
     * <p>In hand -- first and third person, never the GUI, the ground or a
     * frame -- the trident is drawn from its 32x32 texture at twice the
     * size, as LOTRRenderLargeItem drew the original's from items/large.
     *
     * <p>ItemUseAnimation.SPEAR, new in 26.2 and the obvious candidate, is not
     * usable: SpearAnimations.firstPersonUse and thirdPersonUseItem both
     * return at once for a stack with no DataComponents.KINETIC_WEAPON, and
     * that component is vanilla's mounted-charge mechanic -- dismounts,
     * knockback and a damage multiplier the original's trident never had.
     */

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity holder,
            EquipmentSlot slot) {
        super.inventoryTick(stack, level, holder, slot);
        rollModifiersOnce(stack, level, holder);
    }

    /**
     * TridentItem.releaseUsing, reimplemented for one line's sake: the trident
     * it spawns has to be {@link LOTRThrownTridentEntity} rather than vanilla's,
     * or the thing in the air is drawn as a vanilla trident. Vanilla builds its
     * ThrownTrident inside a lambda with no seam to replace.
     *
     * <p>RIPTIDE is handed straight back to super. That branch launches the
     * PLAYER and spawns no projectile at all, so there is nothing in it worth
     * duplicating and a good deal worth not getting wrong.
     */
    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity user, int timeLeft) {
        if (!(user instanceof Player player)) {
            return false;
        }
        // THROW_THRESHOLD_TIME: a trident flicked out too fast does nothing.
        if (getUseDuration(stack, user) - timeLeft < TridentItem.THROW_THRESHOLD_TIME) {
            return false;
        }
        if (EnchantmentHelper.getTridentSpinAttackStrength(stack, user) > 0.0f) {
            return super.releaseUsing(stack, level, user, timeLeft);
        }
        if (stack.nextDamageWillBreak()) {
            return false;
        }

        Holder<SoundEvent> sound = EnchantmentHelper
                .pickHighestLevel(stack, EnchantmentEffectComponents.TRIDENT_SOUND)
                .orElse(SoundEvents.TRIDENT_THROW);

        if (level instanceof ServerLevel server) {
            stack.hurtWithoutBreaking(1, player);
            ItemStack thrown = stack.consumeAndReturn(1, player);
            LOTRThrownTridentEntity trident = Projectile.spawnProjectileFromRotation(
                    (lvl, shooter, ammo) -> new LOTRThrownTridentEntity(
                            LOTREntities.THROWN_TRIDENT, lvl, shooter, ammo),
                    server, thrown, player, 0.0f, TridentItem.PROJECTILE_SHOOT_POWER, 1.0f);
            if (player.hasInfiniteMaterials()) {
                trident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
            level.playSound(null, trident, sound.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return true;
    }
}
