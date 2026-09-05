package lotr.common.enchant;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class LOTREnchantmentRangedDamage extends LOTREnchantment {
	public float damageFactor;

	public LOTREnchantmentRangedDamage(String s, float damage) {
		super(s, LOTREnchantmentType.RANGED_LAUNCHER);
		damageFactor = damage;
		if (damageFactor > 1.0f) {
			setValueModifier(damageFactor * 2.0f);
		} else {
			setValueModifier(damageFactor);
		}
	}

	@Override
	public String getDescription(ItemStack itemstack) {
		return StatCollector.translateToLocalFormatted("lotr.enchant.rangedDamage.desc", formatMultiplicative(damageFactor));
	}

	@Override
	public boolean isBeneficial() {
		return damageFactor >= 1.0f;
	}
}
