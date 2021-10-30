package machine;

public class NoInputMoneyException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoInputMoneyException(String msg) {
		super(msg);
	}
}
