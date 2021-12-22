package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connect.Connect;

public class Product {

	private Integer productID;
	private String name;
	private String description;
	private Integer price;
	private Integer stock;
	private Connect connect = Connect.getConnection();
	
	public Product() {}
	
	public Product(Integer productID, String name, String description, Integer price, Integer stock) {
		super();
		this.productID = productID;
		this.name = name;
		this.description = description;
		this.price = price;
		this.stock = stock;
	}

	public Integer getProductID() {
		return productID;
	}

	public void setProductID(Integer productID) {
		this.productID = productID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}
	
	
	public boolean insertNewProduct() {
		
		String query = "INSERT INTO products (name, description, price, stock) VALUES (?,?,?,?)";
		PreparedStatement ps = connect.prepareStatement(query);
		
		try {
			ps.setString(1, name);
			ps.setString(2, description);
			ps.setInt(3, price);
			ps.setInt(4, stock);
			return ps.executeUpdate() == 1;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<Product> getAllProducts(){
		String query = "SELECT * FROM products";
		ResultSet rs =connect.executeQuery(query);
	
		try {
			ArrayList<Product> products = new ArrayList<>();
			while(rs.next()) {
				Product product = map(rs);
				products.add(product);
			}
			return products;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Product map(ResultSet rs) {
		try {
			int rsProductId = rs.getInt("productID");
			String rsName = rs.getString("name");
			String rsDescription = rs.getString("description");
			int rsPrice = rs.getInt("price");
			int rsStock = rs.getInt("stock");
		
			return new Product(rsProductId, rsName, rsDescription, rsPrice, rsStock);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Product getProduct(String productID) {
		String query = "SELECT * FROM products WHERE productID  = " + productID;
		ResultSet rs =connect.executeQuery(query);
	
		try {
			while(rs.next()) {
				Product product = map(rs);
				return product;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean updateProduct() {
		String query = "UPDATE products SET name = ?, description = ?, price = ?, stock = ? WHERE productID = ?";
		PreparedStatement ps = connect.prepareStatement(query);
		
		try {
			ps.setString(1, name);
			ps.setString(2, description);
			ps.setInt(3, price);
			ps.setInt(4, stock);
			ps.setInt(5, productID);
			return ps.executeUpdate() == 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteProduct() {
		String query = "DELETE FROM products WHERE productID = ?";
		PreparedStatement ps = connect.prepareStatement(query);
		
		try {
			ps.setInt(1, productID);
			return ps.executeUpdate() == 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
