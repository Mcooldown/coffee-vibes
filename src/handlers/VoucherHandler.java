package handlers;

import java.util.ArrayList;

import models.Voucher;
import views.VoucherManagementForm;

public class VoucherHandler {

	private static VoucherHandler handler = null;
	private Voucher voucher;
	private String errorMessage; 
	
	public VoucherHandler() {
		voucher = new Voucher();
	}
	
	public static VoucherHandler getInstance() {
		if(handler == null) {
			handler = new VoucherHandler();
		}
		return handler;
	}
	
	public void viewVoucherManagementForm() {
		new VoucherManagementForm();
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public ArrayList<Voucher> getAllVouchers(){
		return voucher.getAllVouchers();
	}
	
	public boolean insertVoucher(String discount) {
		
		if(discount.equals("")) {
			errorMessage = "Discount must be filled";
			return false;
		}else {
			int discountInt = 0;
			try {
				discountInt = Integer.parseInt(discount);
			} catch (Exception e) {
				errorMessage = "Discount must be number";
				return false;
			}
			if(discountInt < 1 || discountInt > 100) {
				errorMessage = "Discount must be between 1 and 100";
				return false;
			}
			
			Voucher newVoucher = new Voucher(0, discountInt, "Active");
			
			boolean inserted = newVoucher.generateVoucher();
			if(!inserted) {
				errorMessage = "Generate voucher failed";
			}
			return inserted;
		}
	}
	
	public Voucher getVoucher(String voucherID) {
		return voucher.getVoucher(voucherID);
	}
	
	public boolean deleteVoucher(String voucherID) {
		if(voucherID.equals("")) {
			errorMessage = "Voucher ID must be filled";
			return false;
		}else {
			Voucher voucher = getVoucher(voucherID);
			
			if(voucher == null) {
				errorMessage = "Voucher ID not exist";
				return false;
			}else {
				boolean deleted = voucher.deleteVoucher(voucher.getVoucherID());
				if(!deleted) {
					errorMessage = "Delete Failed";
				}
				return deleted;
			}
		}
	}
}
