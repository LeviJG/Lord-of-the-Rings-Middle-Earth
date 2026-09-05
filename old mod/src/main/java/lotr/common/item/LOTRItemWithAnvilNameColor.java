package lotr.common.item;

import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;

public class LOTRItemWithAnvilNameColor extends Item implements AnvilNameColorProvider {
	public EnumChatFormatting anvilNameColor;

	public LOTRItemWithAnvilNameColor(EnumChatFormatting color) {
		anvilNameColor = color;
	}

	@Override
	public EnumChatFormatting getAnvilNameColor() {
		return anvilNameColor;
	}
}
