package lotr.common.world.structure2;

public class LOTRWorldGenHarnedorTavernRuined extends LOTRWorldGenHarnedorTavern {
	public LOTRWorldGenHarnedorTavernRuined(boolean flag) {
		super(flag);
	}

	@Override
	public boolean isRuined() {
		return true;
	}
}
