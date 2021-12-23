package controllers;

import java.sql.Date;
import java.util.ArrayList;

import models.CartItem;
import models.Product;
import models.Transaction;
import models.TransactionItem;
import views.TransactionManagementForm;

public class TransactionHandler {

	private static TransactionHandler handler = null;
	private Transaction transaction;
	private String errorMessage;
	
	public TransactionHandler() {
		transaction = new Transaction();
	}

	public static TransactionHandler getInstance() {
		if(handler == null) {
			handler = new TransactionHandler();
		}
		return handler;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void viewTransactionManagementForm() {
		new TransactionManagementForm();
	}
	
	public boolean insertTransaction(String voucherID, String employeeID, int totalPayment) {
		
//		Update voucher status / delete voucher
		int voucherIDInt = 0;
		if(!voucherID.equals("")) {
			boolean updateVoucherStatus = VoucherHandler.getInstance().updateVoucherStatus(voucherID);
			if(!updateVoucherStatus) {
				errorMessage = "Update voucher status failed";
				return false;
			}
			try {
				voucherIDInt = Integer.parseInt(voucherID);
			} catch (Exception e) {
				errorMessage = "Voucher ID not valid";
				return false;
			}
		}
		
//		Validate employee ID & total payment
		if(employeeID.equals("")) {
			errorMessage = "Employee ID not valid";
			return false;
		}
		int employeeIDInt = 0;
		try {
			employeeIDInt = Integer.parseInt(employeeID);
		} catch (Exception e) {
			errorMessage = "Employee ID not valid";
			return false;
		}
		
		if(totalPayment < 0) {
			errorMessage = "Minimum 0 of total payment";
			return false;
		}

		//		Insert transaction
		long millis=System.currentTimeMillis();
		Date currentDate = new Date(millis);
		Transaction newTransaction = new Transaction(0, currentDate,voucherIDInt, employeeIDInt, totalPayment);
		int newTransactionID = newTransaction.insertTransaction();
		
		ArrayList<CartItem> listItem = CartHandler.getInstance().getCart();
		ArrayList<TransactionItem> listTransactionItem = new ArrayList<>();
		
		for (CartItem item : listItem) {
			
			Product product =  ProductHandler.getInstance().getProduct(String.valueOf(item.getProduct().getProductID()));
//			Update product stock
			ProductHandler.getInstance().updateProductStock(String.valueOf(item.getProduct().getProductID()), product.getStock() - item.getQuantity());
			
//			Add transaction item to list
			TransactionItem newTransactionItem = new TransactionItem(newTransactionID, product.getProductID(), item.getQuantity());
			listTransactionItem.add(newTransactionItem);
		}
		
//		insert list of transaction item
		newTransaction.setListTransactionItem(listTransactionItem);
		newTransaction.insertTransactionItem();
		
//		clear cart
		CartHandler.getInstance().clearCart();
		
		return true;
	}
	
	public ArrayList<Transaction> getAllTransactions (){
		return transaction.getAllTransactions();
	}
	
	public ArrayList<TransactionItem> getTransactionDetail(int transactionID){
		return transaction.getTransactionDetail(transactionID);
	}
}
