package machine;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class VendingMachine {
	private int inputMoney;
	private Deque<Product> recentlySelectedProduct; // 3 recent selected product
	private HashMap<Denomination, Integer> cashInMachine; // denomination, amount
	private HashMap<Product, Integer> productInMachine;
	private Denomination[] supportedDeno = {Denomination.TEN, Denomination.TWENTY, Denomination.FIFTY, 
												Denomination.ONE_HUNDRED, Denomination.TWO_HUNDRED};
	private PromotionScheme ps;
	
	
	public VendingMachine() {
		this.inputMoney = 0;
		this.recentlySelectedProduct = new ArrayDeque<Product>();
		this.cashInMachine = new HashMap<Denomination, Integer>();
		this.productInMachine = new HashMap<Product, Integer>();
		this.ps = new Normal(this);
	}
	
	public void productInit(int n) {
		for (Product p : Product.values()) {
			productInMachine.put(p, Integer.valueOf(n));
		}
	}
	
	public void moneyInit(int n) {
		for (Denomination d : Denomination.values()) {
			cashInMachine.put(d, Integer.valueOf(n));
		}
	}
	
	public void setPromotionScheme(PromotionScheme promotionScheme) {
		this.ps = promotionScheme;
	}
	
	public void runMachine() {
		this.ps.runMachine();
	}
	
	public Deque<Product> getRecentlySelectedProduct() {
		return this.recentlySelectedProduct;
	}
	
	private boolean checkDenomination(Denomination money) {
		for (Denomination d : supportedDeno) {
			if (money == d) {
				return true;
			}
		}
		
		return false;
	}
	
	public void insertMoney(Denomination money) throws UnSupportedDenominationException {
		this.inputMoney += money.getValue();
		this.cashInMachine.put(money, cashInMachine.get(money) + 1);
		
		if (!checkDenomination(money)) {
			this.inputMoney -= money.getValue();
			this.cashInMachine.put(money, cashInMachine.get(money) - 1);
			
			System.out.println("Get your money back: " + money.getValue()*1000);
			throw new UnSupportedDenominationException("This Denomination is not supported");			
		}
	}
	
	public void refund() throws NoInputMoneyException, NotEnoughChangeException {
		if (this.inputMoney > 0) {
			List<Denomination> refundMoney = returnChange(inputMoney);
			
			// update cashInMachine
			for (Denomination d : refundMoney) {
				cashInMachine.put(d, cashInMachine.get(d) - 1);
			}
			
			// output
			System.out.print("Get your money back: ");
			for (Denomination d : refundMoney) {
				System.out.print(d.getValue()*1000 + " ");
			}
			System.out.println();

			this.inputMoney = 0;
		}
		else {
			throw new NoInputMoneyException("You haven't inserted money");
		}
	}
	
	private void saveSelectProduct(Product product) {
		if (this.recentlySelectedProduct.size() < 3) {
			this.recentlySelectedProduct.add(product);
		}
		else {
			this.recentlySelectedProduct.removeFirst();
			this.recentlySelectedProduct.add(product);
		}
	}
	
	public void selectProduct(Product selectedProduct) throws SoldOutException, 
																NotEnoughChangeException, 
																NotPayEnoughMoney,
																NoInputMoneyException {
		if (productInMachine.containsKey(selectedProduct) 
			&& productInMachine.get(selectedProduct) > 0) {
			
			int producPrice = selectedProduct.getValue();
			
			if (inputMoney > 0) {
				// need to return the remaining change
				if (inputMoney > producPrice) { 
					int remaining = inputMoney - producPrice;
					
					try {
						List<Denomination> changes = returnChange(remaining);
						productInMachine.put(selectedProduct, productInMachine.get(selectedProduct) - 1);
						saveSelectProduct(selectedProduct);
						
						// update cashInMachine
						for (Denomination d : changes) {
							cashInMachine.put(d, cashInMachine.get(d) - 1);
						}
						
						// return changes
						System.out.println("Your: " + selectedProduct);
						System.out.println("Get back your remaining change: ");
						for (Denomination d : changes) {
							System.out.print(d.getValue()*1000 + " ");
						}
						System.out.println();
						
						inputMoney = 0;
					} catch (NotEnoughChangeException ex) {
						refund();
						throw ex;
					}
				}
				// no return change
				else if (inputMoney == producPrice) { 
					productInMachine.put(selectedProduct, productInMachine.get(selectedProduct) - 1);
					saveSelectProduct(selectedProduct);
					inputMoney = 0;
					System.out.println("Your: " + selectedProduct);
				}
				else {
					throw new NotPayEnoughMoney("You didn't pay enough money. Your input: " 
													+ inputMoney + ", " 
													+ selectedProduct + " price: " + producPrice);
				}
			}
			else {
				throw new NoInputMoneyException("You haven't pay yet");
			}
		}
		else {
			throw new SoldOutException("This product is sold out");
		}
	}
	
	private List<Denomination> returnChange(int n) throws NotEnoughChangeException {
		List<Denomination> changes = new LinkedList<Denomination>();
		HashMap<Denomination, Integer> temp = new HashMap<Denomination, Integer>();
		
		for (Map.Entry<Denomination, Integer> entry : cashInMachine.entrySet()) {
			temp.put(entry.getKey(), entry.getValue());
		}
		
		
		while (n > 0) {
			if (temp.get(Denomination.TWO_HUNDRED) > 0 
					&& n >= Denomination.TWO_HUNDRED.getValue()) {
				n -= Denomination.TWO_HUNDRED.getValue();
				changes.add(Denomination.TWO_HUNDRED);
				temp.put(Denomination.TWO_HUNDRED, temp.get(Denomination.TWO_HUNDRED) - 1);
			}
			else if (temp.get(Denomination.ONE_HUNDRED) > 0 
					&& n >= Denomination.ONE_HUNDRED.getValue()) {
				n -= Denomination.ONE_HUNDRED.getValue();
				changes.add(Denomination.ONE_HUNDRED);
				temp.put(Denomination.ONE_HUNDRED, temp.get(Denomination.ONE_HUNDRED) - 1);
			}
			else if (temp.get(Denomination.FIFTY) > 0 
					&& n >= Denomination.FIFTY.getValue()) {
				n -= Denomination.FIFTY.getValue();
				changes.add(Denomination.FIFTY);
				temp.put(Denomination.FIFTY, temp.get(Denomination.FIFTY) - 1);
			}
			else if (temp.get(Denomination.TWENTY) > 0 
					&& n >= Denomination.TWENTY.getValue()) {
				n -= Denomination.TWENTY.getValue();
				changes.add(Denomination.TWENTY);
				temp.put(Denomination.TWENTY, temp.get(Denomination.TWENTY) - 1);
			}
			else if (temp.get(Denomination.TEN) > 0 
					&& n >= Denomination.TEN.getValue()) {
				n -= Denomination.TEN.getValue();
				changes.add(Denomination.TEN);
				temp.put(Denomination.TEN, temp.get(Denomination.TEN) - 1);
			}
			else {
				throw new NotEnoughChangeException("Not enough change in machine");
			}
		}
		
		return changes;
	}
	
}
