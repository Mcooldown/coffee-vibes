package handlers;

import java.util.ArrayList;

import models.Product;
import views.ProductManagementForm;

public class ProductHandler {
	
	private static ProductHandler handler = null;
	private Product product;
	private String errorMessage;
	
	public ProductHandler() {
		product = new Product();
	}
	
	public static ProductHandler getInstance() {
		if(handler == null) {
			handler = new ProductHandler();
		}
		return handler;
	}
	
	public void viewProductManagementForm() {
		new ProductManagementForm();
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	private boolean validateProduct(String name, String description, String price) {
		if(name.equals("")) {
			errorMessage = "Name must be filled";
			return false;
		}else {
			if(description.equals("")) {
				errorMessage = "Description must be filled";
				return false;
			}
			int priceTemp = 0;
			try {
				priceTemp = Integer.parseInt(price);
			} catch (Exception e) {
				errorMessage = "Price must be numeric";
				return false;
			}
			if(priceTemp < 1) {
				errorMessage = "Price cannot be less than 1";
				return false;
			}
			

			return true;
		}
	}
	
	public boolean insertProduct(String name, String description, String stock, String price) {
		
		boolean passValidate = validateProduct(name, description, price);
		
		if(passValidate) {
			
			int stockTemp = 0;
			try {
				stockTemp = Integer.parseInt(stock);
			} catch (Exception e) {
				errorMessage = "stockTemp must be numeric";
				return false;
			}
			if(stockTemp < 1) {
				errorMessage = "Stock cannot be less than 0";
				return false;
			}
			
			Product newProduct = new Product(0, name, description, Integer.parseInt(price), Integer.parseInt(stock));
			
			boolean inserted = newProduct.insertNewProduct();
			if(!inserted) {
				errorMessage = "Insert failed";
			}
			return inserted;
		}else {
			return false;
		}
	}
	
	public ArrayList<Product>getAllProducts(){
		return product.getAllProducts();
	}
	
	public Product getProduct(String productId) {
		return product.getProduct(productId);
	}
	
	public boolean updateProduct(String productID, String name, String description, String price) {
		if(productID.equals("")) {
			errorMessage = "Product ID must be filled";
			return false;
		}else {
			Product product = getProduct(productID);
			
			if(product == null) {
				errorMessage = "Product ID not exist";
				return false;
			}else {
				boolean passValidate = validateProduct(name, description, price);
				
				if(passValidate) {
					boolean updated = product.updateProduct(Integer.parseInt(productID), name, description, Integer.parseInt(price));
					if(!updated) {
						errorMessage = "Update failed";
					}
					return updated;
				}else {
					return false;
				}
			}
		}
	}
	
	public boolean deleteProduct(String productID) {
		
		if(productID.equals("")) {
			errorMessage = "Product ID must be filled";
			return false;
		}else {
			Product product = getProduct(productID);
			
			if(product == null) {
				errorMessage = "Product ID not exist";
				return false;
			}else {
				boolean deleted = product.deleteProduct(Integer.parseInt(productID));
				if(!deleted) {
					errorMessage = "Delete Failed";
				}
				return deleted;
			}
		}
	}


}
