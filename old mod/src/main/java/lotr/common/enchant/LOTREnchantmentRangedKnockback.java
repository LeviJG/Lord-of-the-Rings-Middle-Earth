package lotr.common.enchant;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class LOTREnchantmentRangedKnockback extends LOTREnchantment {
	public int knockback;

	public LOTREnchantmentRangedKnockback(String s, int i) {
		super(s, LOTREnchantmentType.RANGED_LAUNCHER);
		knockback = i;
		setValueModifier((knockback + 2) / 2.0f);
	}

	@Override
	public String getDescription(ItemStack itemstack) {
		return StatCollector.translateToLocalFormatted("lotr.enchant.rangedKnockback.desc", formatAdditiveInt(knockback));
	}

	@Override
	public boolean isBeneficial() {
		return knockback >= 0;
	}
}
