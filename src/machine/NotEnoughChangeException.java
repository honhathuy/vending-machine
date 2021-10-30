package machine;

public class NotEnoughChangeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotEnoughChangeException(String msg) {
		super(msg);
	}
}
