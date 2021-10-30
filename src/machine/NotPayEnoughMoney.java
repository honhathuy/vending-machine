package machine;

public class NotPayEnoughMoney extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotPayEnoughMoney(String msg) {
		super(msg);
	}
}
