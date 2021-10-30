package machine;

public class SoldOutException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SoldOutException(String msg) {
		super(msg);
	}
}
