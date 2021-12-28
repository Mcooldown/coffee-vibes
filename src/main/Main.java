package main;

import controllers.AuthHandler;

public class Main {

	public Main() {
		AuthHandler.getInstance().viewLoginForm();
	}
	
	public static void main(String[] args) {
		new Main();
	}
}