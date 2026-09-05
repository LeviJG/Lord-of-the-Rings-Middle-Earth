package lotr.common.item;

import net.minecraft.item.Item;

public class LOTRItemPike extends LOTRItemPolearmLong {
	public LOTRItemPike(Item.ToolMaterial material) {
		super(material);
	}

	public LOTRItemPike(LOTRMaterial material) {
		this(material.toToolMaterial());
	}
}
