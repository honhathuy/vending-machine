package machine;

public enum Product {
	Coke(10), Pepsi(10), Soda(20);
	
	private int value;
	
	Product (int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
