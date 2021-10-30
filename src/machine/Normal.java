package machine;

import java.util.Scanner;

public class Normal implements PromotionScheme {
	private VendingMachine vm;
	
	public Normal(VendingMachine vm) {
		this.vm = vm;
	}

	@Override
	public void runMachine() {
		System.out.println("OPTIONS: ");
		System.out.println("+ : insert money");
		System.out.println("- : refund");
		System.out.println("1: coke");
		System.out.println("2: pepsi");
		System.out.println("3: soda");
		
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
					vm.selectProduct(Product.Coke);
				}
				else if (opt.equals("2")) {
					vm.selectProduct(Product.Pepsi);
				}
				else if (opt.equals("3")) {
					vm.selectProduct(Product.Soda);
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
