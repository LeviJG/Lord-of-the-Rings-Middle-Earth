package lotr.common.entity.npc;

import net.minecraft.world.World;

public class LOTREntityUtumnoObsidianWarg extends LOTREntityUtumnoWarg {
	public LOTREntityUtumnoObsidianWarg(World world) {
		super(world);
	}

	@Override
	public void entityInit() {
		super.entityInit();
		setWargType(LOTREntityWarg.WargType.OBSIDIAN);
	}
}
