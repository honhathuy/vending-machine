package main;

import machine.*;

public class Main {

	public static void main(String[] args) {
		VendingMachine vm = new VendingMachine();
		
		vm.productInit(10);
		vm.moneyInit(1);
		vm.setPromotionScheme(new ThreeConsecutivePurchases(vm));
		
		vm.runMachine();
	}

}
