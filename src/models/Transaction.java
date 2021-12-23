package models;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connect.Connect;

public class Transaction {
	
	private int transactionID;
	private Date purchaseDate;
	private int voucherID;
	private int employeeID;
	private int totalPrice;
	private ArrayList<TransactionItem> listTransactionItem;
	private Connect connect = Connect.getConnection();
	
	public Transaction() {}
	
	public Transaction(int transactionID, Date purchaseDate, int voucherID, int employeeID, int totalPrice) {
		super();
		this.transactionID = transactionID;
		this.purchaseDate = purchaseDate;
		this.voucherID = voucherID;
		this.employeeID = employeeID;
		this.totalPrice = totalPrice;
	}
	
	public int getTransactionID() {
		return transactionID;
	}

	public void setListTransactionItem(ArrayList<TransactionItem> listTransactionItem) {
		this.listTransactionItem = listTransactionItem;
	}

	public void setConnect(Connect connect) {
		this.connect = connect;
	}

	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public int getVoucherID() {
		return voucherID;
	}
	public void setVoucherID(int voucherID) {
		this.voucherID = voucherID;
	}
	public int getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public int insertTransaction() {
		
		String query = "INSERT INTO transactions (purchaseDate, voucherID, employeeID, totalPrice) VALUES (?,?,?,?)";
		PreparedStatement ps = connect.prepareStatement(query);
		
		try {
			ps.setDate(1,purchaseDate);
			ps.setInt(2, voucherID);
			ps.setInt(3, employeeID);
			ps.setInt(4, totalPrice);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public boolean insertTransactionItem() {
		String query = "INSERT INTO transactions (purchaseDate, voucherID, employeeID, totalPrice) VALUES (?,?,?,?)";
		PreparedStatement ps = connect.prepareStatement(query);
		
		try {
			ps.setDate(1,purchaseDate);
			ps.setInt(2, voucherID);
			ps.setInt(3, employeeID);
			ps.setInt(4, totalPrice);
			return ps.executeUpdate() == 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private Transaction mapTransaction(ResultSet rs) {
		try {
			int rsTransactionID = rs.getInt("transactionID");
			Date rsPurchaseDate = rs.getDate("purchaseDate");
			int rsVoucherID = rs.getInt("voucherID");
			int rsEmployeeID = rs.getInt("employeeID");
			int rsTotalPrice = rs.getInt("totalPrice");
			
			return new Transaction(rsTransactionID, rsPurchaseDate, rsVoucherID, rsEmployeeID, rsTotalPrice);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Transaction> getAllTransactions(){
		String query = "SELECT * FROM transactions";
		ResultSet rs =connect.executeQuery(query);
	
		try {
			ArrayList<Transaction> transactions = new ArrayList<>();
			while(rs.next()) {
				Transaction transaction = mapTransaction(rs);
				transactions.add(transaction);
			}
			return transactions;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private TransactionItem mapTransactionItem(ResultSet rs) {
		try {
			int rsTransactionID = rs.getInt("transactionID");
			int rsProductID = rs.getInt("productID");
			int rsQuantity = rs.getInt("quantity");
			
			return new TransactionItem(rsTransactionID, rsProductID, rsQuantity);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<TransactionItem> getTransactionDetail(int transactionID){
		String query = "SELECT * FROM transaction_items WHERE transactionID = " + transactionID;
		ResultSet rs =connect.executeQuery(query);
	
		try {
			listTransactionItem = new ArrayList<>();
			while(rs.next()) {
				TransactionItem transactionItem = mapTransactionItem(rs);
				listTransactionItem.add(transactionItem);
			}
			return listTransactionItem;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
