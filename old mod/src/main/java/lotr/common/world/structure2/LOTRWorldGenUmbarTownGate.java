package lotr.common.world.structure2;

public class LOTRWorldGenUmbarTownGate extends LOTRWorldGenSouthronTownGate {
	public LOTRWorldGenUmbarTownGate(boolean flag) {
		super(flag);
	}

	@Override
	public boolean isUmbar() {
		return true;
	}
}
