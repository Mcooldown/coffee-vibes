package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connect.Connect;

public class Voucher {

	private int voucherID;
	private int discount;
	private String status;
	private Connect connect = Connect.getConnection();
	
	public Voucher() {}
	
	public Voucher(int voucherID, int discount, String status) {
		super();
		this.voucherID = voucherID;
		this.discount = discount;
		this.status = status;
	}
	public int getVoucherID() {
		return voucherID;
	}
	public void setVoucherID(int voucherID) {
		this.voucherID = voucherID;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public boolean generateVoucher() {
		String query = "INSERT INTO vouchers (discount, status) VALUES (?,?)";
		PreparedStatement ps = connect.prepareStatement(query);
		
		try {
			ps.setInt(1, discount);
			ps.setString(2, status);
			return ps.executeUpdate() == 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private Voucher map(ResultSet rs) {
		try {
			int rsVoucherID = rs.getInt("voucherID");
			int rsDiscount = rs.getInt("discount");
			String rsStatus = rs.getString("status");
			
			return new Voucher(rsVoucherID, rsDiscount, rsStatus);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Voucher> getAllVouchers(){
		String query = "SELECT * FROM vouchers";
		ResultSet rs =connect.executeQuery(query);
	
		try {
			ArrayList<Voucher> vouchers = new ArrayList<>();
			while(rs.next()) {
				Voucher voucher = map(rs);
				vouchers.add(voucher);
			}
			return vouchers;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Voucher getVoucher(String voucherID) {
		String query = "SELECT * FROM vouchers WHERE voucherID = " + voucherID;
		ResultSet rs = connect.executeQuery(query);
		
		try {
			while(rs.next()) {
				Voucher voucher = map(rs);
				return voucher;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
	}
	
	public boolean executeUpdate(String query) {
		PreparedStatement ps = connect.prepareStatement(query);
		
		try {
			ps.setInt(1, voucherID);
			return ps.executeUpdate() == 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateVoucherStatus(int voucherID) {
		String query = "UPDATE vouchers SET status = 'Used' WHERE voucherID = ?";
		return executeUpdate(query);
	}
	
	public boolean deleteVoucher(int voucherID) {
		String query = "DELETE FROM vouchers WHERE voucherID = ?";
		return executeUpdate(query);
	}
}
