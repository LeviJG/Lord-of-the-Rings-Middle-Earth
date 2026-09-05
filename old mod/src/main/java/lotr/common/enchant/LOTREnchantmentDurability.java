package lotr.common.enchant;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class LOTREnchantmentDurability extends LOTREnchantment {
	public float durabilityFactor;

	public LOTREnchantmentDurability(String s, float f) {
		super(s, LOTREnchantmentType.BREAKABLE);
		durabilityFactor = f;
		setValueModifier(durabilityFactor);
	}

	@Override
	public String getDescription(ItemStack itemstack) {
		return StatCollector.translateToLocalFormatted("lotr.enchant.durable.desc", formatMultiplicative(durabilityFactor));
	}

	@Override
	public boolean isBeneficial() {
		return durabilityFactor >= 1.0f;
	}
}
