package machine;

public enum Denomination {
	ONE(1), TWO(2), FIVE(5), TEN(10), TWENTY(20), FIFTY(50), 
	ONE_HUNDRED(100), TWO_HUNDRED(200), FIVE_HUNDRED(500);
	
	private int value;
	
	private Denomination(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public boolean isValid(int n) {
		for (Denomination d : Denomination.values()) {
			if (d.getValue() == n) {
				return true;
			}
		}
		return false;
	}
	
	public static Denomination getInstance(int value) {
		for (Denomination d : Denomination.values()) {
			if (d.getValue() == value) {
				return d;
			}
		}
		
		return null;
	}
}
