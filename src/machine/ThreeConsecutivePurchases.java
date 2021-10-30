package machine;

import java.util.Deque;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ThreeConsecutivePurchases implements PromotionScheme {
	private VendingMachine vm;
	private volatile boolean shouldIncreaseWinRate;
	private int budget;
	
	public ThreeConsecutivePurchases(VendingMachine vm) {
		this.vm = vm;
		this.budget = 0;
		this.shouldIncreaseWinRate = false;
		
		// thread never die
		Timer timer = new Timer( );
		timer.scheduleAtFixedRate(new TimerTask() {
		    @Override
		    public void run() {
		    	if (budget < 50) {
		    		shouldIncreaseWinRate = true;
		    		budget = 0;
		    	}
		    	else {
		    		shouldIncreaseWinRate = false;
		    		budget = 0;
		    	}
		    }    
		}, 86400000, 86400000);
	}
	
	private boolean isWin() {
		Random random = new Random();
		if (shouldIncreaseWinRate) {
			return random.nextInt(2) == 0;
		}
		else {
			return random.nextInt(10) == 0;
		}
	}
	
	private void purchasePromotion(Product product) throws SoldOutException, 
															NotEnoughChangeException, 
															NotPayEnoughMoney, 
															NoInputMoneyException{
		Deque<Product> recentlySelectedProduct = vm.getRecentlySelectedProduct();
		Product temp = recentlySelectedProduct.size() == 3 ? recentlySelectedProduct.getLast() : null;
		boolean areTheSame = true;
		
		if (temp != null) {
			for (Product p : recentlySelectedProduct) {
				if (p != temp) {
					areTheSame = false;
				}
			}
		}
		else {
			areTheSame = false;
		}
		
		vm.selectProduct(product);
		
		if (areTheSame && isWin()) {
			System.out.println("Bravo!!! You get a free" + product);
			budget += product.getValue();
		}
	}

	@Override
	public void runMachine() {
		System.out.println("OPTIONS: ");
		System.out.println("+ : insert money");
		System.out.println("- : refund");
		System.out.println("1: coke");
		System.out.println("2: pepsi");
		System.out.println("3: soda");
		System.out.println("Promotion: If there are three consecutive purchases the same product, "
								+ "	you will have a 10% chance to receive a product for free");
		
		while (true) {
			String opt;
			Scanner sc = new Scanner(System.in);
			opt = sc.nextLine();
			
			try {
				if (opt.equals("+")) {
					System.out.print("Input money (Only support: 10000, 20000, 50000, 100000, 200000): ");
					int inputMoney = sc.nextInt();
					inputMoney /= 1000;
					Denomination d = Denomination.getInstance(inputMoney);
					
					if (d != null) {
						vm.insertMoney(d);
					}
					else {
						System.out.println("This denomination does not exist");
					}
				}
				else if (opt.equals("-")) {
					vm.refund();
				}
				else if (opt.equals("1")) {
					purchasePromotion(Product.Coke);
				}
				else if (opt.equals("2")) {
					purchasePromotion(Product.Pepsi);
				}
				else if (opt.equals("3")) {
					purchasePromotion(Product.Soda);
				}
				else {
					System.out.println("Invalid option");
				}
				
			} catch (UnSupportedDenominationException e) {
				System.out.println(e.getMessage());
			} catch (NoInputMoneyException e) {
				System.out.println(e.getMessage());
			} catch (SoldOutException e) {
				System.out.println(e.getMessage());
			} catch (NotEnoughChangeException e) {
				System.out.println(e.getMessage());
			} catch (NotPayEnoughMoney e) {
				System.out.println(e.getMessage());
				System.out.println("Insert more or refund");
			}
		}
	}

}
