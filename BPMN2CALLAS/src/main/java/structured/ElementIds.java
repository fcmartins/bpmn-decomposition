package structured;

public class ElementIds {

	private static int id = 0;
	
	public static int nextId() {
		return ++id;
	}
}
