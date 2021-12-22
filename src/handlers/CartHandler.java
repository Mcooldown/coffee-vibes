package handlers;

import java.util.ArrayList;

import models.CartItem;
import models.Product;
import views.AddProductToCartForm;
import views.CartManagementForm;
import views.CheckOutForm;

public class CartHandler {

	private ArrayList<CartItem> listItem;
	private static CartHandler handler = null;
	private String errorMessage;
	
	public CartHandler() {
		listItem = new ArrayList<>();
	}
	
	public static CartHandler getInstance() {
		if(handler == null) {
			handler = new CartHandler();
		}
		return handler;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public boolean addToCart(String productID, String quantity) {
		
		if(quantity.equals("")) {
			errorMessage = "Quantity must be filled";
			return false;
		}else {
			
			int quantityInt = 0;
			try {
				quantityInt = Integer.parseInt(quantity);
			} catch (Exception e) {
				errorMessage = "Quantity must be number";
				return false;
			}
			int productIDInt =0;
			try {				
				productIDInt = Integer.parseInt(productID);
			} catch (Exception e) {
				errorMessage = "Product ID not valid";
				return false;
			}

			Product product = ProductHandler.getInstance().getProduct(String.valueOf(productID));
			
			if(product != null) {
				if(product.getStock() < quantityInt) {
					errorMessage = "Insufficient Stock of Product";
					return false;
				}
				int existIndex = searchCartItem(productIDInt);				
				
				if(existIndex != -1) {
					boolean updateQuantity = updateCartProductQuantity(productIDInt, quantityInt);
				}else {
					CartItem cartItem = new CartItem(getProduct(productIDInt), quantityInt);
					listItem.add(cartItem);
				}
				
				boolean updateStock = updateProductStock(productIDInt, product.getStock()-quantityInt);
				if(updateStock) {
					return true;
				}else {
					errorMessage = "Update stock failed";
					return false;
				}
				
			}else {
				errorMessage = "Product Not found";
				return false;
			}
		}
	}
	
	public void viewAddProductToCartForm(Product product) {
		new AddProductToCartForm(product);
	}
	
	public void viewCheckOutForm() {
		new CheckOutForm();
	}
	
	public void viewCart() {
		new CartManagementForm();
	}

	public ArrayList<CartItem> getCart(){
		return listItem;
	}
	
	public Product getProduct(int productID) {
		return ProductHandler.getInstance().getProduct(String.valueOf(productID));
	}
	
	public boolean updateProductStock(int productID, int stock) {
		
		boolean updated = ProductHandler.getInstance().updateProductStock(String.valueOf(productID), stock);
		if(updated) {
			return true; 
		}else {
			return false;
		}
	}
	
	private int searchCartItem(int productID) {
		for (int i = 0; i < listItem.size(); i++) {
			if(listItem.get(i).getProduct().getProductID().equals(productID)) {
				return i;
			}
		}
		return -1;
	}
	
	private boolean updateCartProductQuantity(int productID, int quantity) {
		
		int index = searchCartItem(productID);
		
		if(index != -1) {
			listItem.get(index).setQuantity(listItem.get(index).getQuantity() + quantity);
			return true;
		}else {
			errorMessage = "Cart Item not found";
			return false;
		}
	}
	
	public boolean removeProductFromCart(int productID) {
		int index = searchCartItem(productID);
		if(index != -1) {
			listItem.remove(index);
			return true;
		}else {
			errorMessage = "Cart Item not found";
			return false;
		}
	}

	public void clearCart() {
		listItem.clear();
	}
}
