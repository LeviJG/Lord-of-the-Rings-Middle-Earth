package lotr.common.item;

import net.minecraft.item.Item;

public class LOTRItemPolearmLong extends LOTRItemPolearm {
	public LOTRItemPolearmLong(Item.ToolMaterial material) {
		super(material);
	}

	public LOTRItemPolearmLong(LOTRMaterial material) {
		this(material.toToolMaterial());
	}
}
