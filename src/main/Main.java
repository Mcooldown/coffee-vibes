package main;

import handlers.EmployeeHandler;

public class Main {

	public Main() {
		EmployeeHandler.getInstance().viewEmployeeManagementForm();
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
