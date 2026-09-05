package lotr.common.world.structure2.scan;

public class LOTRScanAlias {
	public String name;
	public Type type;

	public LOTRScanAlias(String s, Type t) {
		name = s;
		type = t;
	}

	public String getFullCode() {
		char c = type.typeCode;
		return c + name + c;
	}

	public enum Type {
		BLOCK('#'), BLOCK_META('~');

		public char typeCode;

		Type(char c) {
			typeCode = c;
		}
	}

}
