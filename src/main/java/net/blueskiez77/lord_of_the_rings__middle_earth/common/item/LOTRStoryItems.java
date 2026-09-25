package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Function;
import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCropBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRTrophyType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.InstrumentComponent;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTREffects;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlockTags;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRConkerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRExplodingTermiteEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRMysteryWebEntity;

import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems.*;

/**
 * The LOTR items of tabStory: the named weapons and staves of the legends.
 * Split out of LOTRItems by creative tab; LOTRItems keeps the shared
 * builders and registers this class in order from its init.
 */
public final class LOTRStoryItems {

    // LOTRItemSting: a Lindon dagger with a point of damage on top and Bilbo's
    // seven hundred uses. Its one trick is cutting webs -- the original gave it
    // speed 15 on Ungoliant's web as well as on the vanilla one.
    public static final Item STING = register("sting", LOTRModifiableItem::new,
            dagger(LOTRToolMaterials.LINDON, 1.0f)
                    .component(DataComponents.TOOL, new net.minecraft.world.item.component.Tool(
                            java.util.List.of(net.minecraft.world.item.component.Tool.Rule.minesAndDrops(
                                    net.minecraft.core.HolderSet.direct(
                                            net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(net.minecraft.world.level.block.Blocks.COBWEB),
                                            net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(LOTRDecorationBlocks.WEB_UNGOLIANT)),
                                    15.0f)),
                            1.0f, 2, false)));

    // LOTRItemAnduril: iron, but the Flame of the West all the same -- 9 damage
    // and 1500 uses.
    public static final Item ANDURIL = register("anduril", LOTRModifiableItem::new,
            new Item.Properties().sword(net.minecraft.world.item.ToolMaterial.IRON, 6.0f, -2.4f).durability(1500));

    // LOTRItemRingil: Fingolfin's sword, Lindon steel at 9 damage and 1500 uses.
    public static final Item RINGIL = register("ringil", LOTRModifiableItem::new,
            new Item.Properties().sword(LOTRToolMaterials.LINDON, 5.0f, -2.4f).durability(1500));

    // LOTRItemGandalfStaffGrey: a wooden staff, 4 damage, a thousand uses.
    public static final Item GANDALF_STAFF_GREY = register("gandalf_staff_grey", LOTRModifiableItem::new,
            new Item.Properties().sword(net.minecraft.world.item.ToolMaterial.WOOD, 3.0f, -2.4f).durability(1000));

    // LOTRItemGlamdring: a Gondolin sword, with that material's own numbers.
    public static final Item GLAMDRING = register("glamdring", LOTRModifiableItem::new,
            new Item.Properties().sword(LOTRToolMaterials.GONDOLIN, 3.0f, -2.4f));

    // LOTRItemSauronMace: a Mordor hammer of 8 damage and 1500 uses, and held
    // down it brings the Dark Lord's blow down on everything within twelve
    // blocks -- see the item class.
    public static final Item SAURON_MACE = register("sauron_mace", LOTRSauronMaceItem::new,
            warhammer(LOTRToolMaterials.MORDOR, 4.5f).durability(1500));

    // LOTRItemGandalfStaffWhite: a Lindon-grade staff of 8 damage, and held
    // down it looses the fireball.
    public static final Item GANDALF_STAFF_WHITE = register("gandalf_staff_white",
            LOTRGandalfStaffWhiteItem::new,
            new Item.Properties().sword(LOTRToolMaterials.LINDON, 4.0f, -2.4f).durability(1500));

    private LOTRStoryItems() {
    }

    /** Forces class-load, so the static fields register. Called from LOTRItems.init. */
    static void init() {
    }
}
