package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import controllers.AuthHandler;
import controllers.CartHandler;
import controllers.EmployeeHandler;
import controllers.PositionHandler;
import controllers.ProductHandler;
import controllers.TransactionHandler;
import controllers.VoucherHandler;
import models.CartItem;
import models.Employee;
import models.Position;
import models.Product;
import models.Voucher;

public class CheckOutForm implements ActionListener {

	private JFrame frame;
	private JMenuBar menuBar;
	private JMenuItem menuProducts, menuCart, menuLogout;
	private JPanel mainPanel, headerPanel, tablePanel, formPanel, centerPanel, btnPanel;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton btnVoucher, btnConfirm;
	private JTextField txtVoucherID;
	private JLabel lblVoucherID, lblTotalPrice;
	private Employee authUser;
	private Position authPosition;
	private int totalPrice;
	
	public CheckOutForm() {
		initAuthPosition();
		initMenuBar();
		initHeaderPanel();
		initFormPanel();
		initTablePanel();
		initCenterPanel();
		initBtnPanel();
		initMainPanel();
		initActionListener();
		initFrame();
	}
	private void initAuthPosition() {
		authUser = AuthHandler.getInstance().getCurrentUser();
		if(authUser == null) AuthHandler.getInstance().logout();
		else {
			authPosition = PositionHandler.getInstance().getPosition(String.valueOf(authUser.getPositionID()));
			if(!authPosition.getName().equals("Barista")) {
				AuthHandler.getInstance().logout();
			}
		}
	}

	private void initMenuBar() {
		menuBar = new JMenuBar();
		menuProducts = new JMenuItem("Products");
		menuCart = new JMenuItem("Cart");
		menuLogout = new JMenuItem("Logout");
		
		menuBar.add(menuProducts);			
		menuBar.add(menuCart);
		menuBar.add(menuLogout);
	}
	
	private void initHeaderPanel() {
		headerPanel = new JPanel(new GridLayout(1,2,10,0));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		JLabel title = new JLabel("Checkout", SwingConstants.CENTER);
		headerPanel.add(title);
	}
	
	private void initTablePanel() {
		table = new JTable();
		loadData();
		scrollPane = new JScrollPane(table);
		
		tablePanel = new JPanel(new GridLayout(1, 1, 10, 10));
		tablePanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		tablePanel.add(scrollPane);
	}
	
	
	private void initFormPanel() {
		formPanel = new JPanel(new BorderLayout(10, 20));
		formPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		
		lblVoucherID = new JLabel("Voucher ID");
		txtVoucherID = new JTextField();
		btnVoucher = new JButton("Use Voucher");
		lblTotalPrice = new JLabel("Total Price: ");
		
		formPanel.add(lblVoucherID, BorderLayout.LINE_START);
		formPanel.add(txtVoucherID, BorderLayout.CENTER);
		formPanel.add(btnVoucher, BorderLayout.LINE_END);
		formPanel.add(lblTotalPrice, BorderLayout.SOUTH);
	}
	
	private void initCenterPanel() {
		centerPanel = new JPanel(new GridLayout(2,1,10,10));
		centerPanel.add(tablePanel);
		centerPanel.add(formPanel);
	}
	
	private void initBtnPanel() {
		btnPanel = new JPanel(new GridLayout(1, 1, 10, 10));
		btnPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		btnConfirm = new JButton("CONFIRM");
		btnConfirm.setBackground(Color.GREEN);
		
		btnPanel.add(btnConfirm);
	}

	private void initMainPanel() {
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(btnPanel, BorderLayout.SOUTH);
	}
	
	private void loadData() {
		String [] columns = {"Product ID","Product Name","Product Price", "Quantity"};
		DefaultTableModel dtm = new DefaultTableModel(columns, 0);
		
		Vector<Object> dataList;
		ArrayList<CartItem> cartItems = CartHandler.getInstance().getCart();
		
		totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			dataList = new Vector<>();
			dataList.add(cartItem.getProduct().getProductID());
			dataList.add(cartItem.getProduct().getName());
			dataList.add(cartItem.getProduct().getPrice());
			dataList.add(cartItem.getQuantity());
			dtm.addRow(dataList);
			totalPrice += (cartItem.getProduct().getPrice()*cartItem.getQuantity());
		}
		lblTotalPrice.setText("Total Price: " +totalPrice);
		table.setModel(dtm);
	}
	
	private void initActionListener() {
		btnConfirm.addActionListener(this);
		btnVoucher.addActionListener(this);
		menuProducts.addActionListener(this);
		menuLogout.addActionListener(this);
	}
	
	private void initFrame() {
		frame = new JFrame("Coffee Vibes - Group 2 - BD01");
		frame.setJMenuBar(menuBar);
		frame.add(mainPanel);
		frame.setSize(650, 400);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void confirm() {
		String voucherID = txtVoucherID.getText().toString();
		String employeeID = String.valueOf(authUser.getEmployeeID());

		boolean checkoutSuccess = TransactionHandler.getInstance().insertTransaction(voucherID, employeeID, totalPrice);
		if(checkoutSuccess) {
			JOptionPane.showMessageDialog(frame,"Checkout success", "Success", JOptionPane.INFORMATION_MESSAGE);
			frame.dispose();
			ProductHandler.getInstance().viewProductManagementForm();
		}else {
			JOptionPane.showMessageDialog(frame, TransactionHandler.getInstance().getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void useVoucher() {
		
		String voucherID = txtVoucherID.getText().toString();
		Voucher voucher = VoucherHandler.getInstance().getVoucher(voucherID);
		
		if(voucher != null) {
			JOptionPane.showMessageDialog(frame, "Voucher Applied", "Success", JOptionPane.INFORMATION_MESSAGE);
			txtVoucherID.setEditable(false);
			btnVoucher.setEnabled(false);
			totalPrice -= ((totalPrice*voucher.getDiscount())/100);
			lblTotalPrice.setText("Total Price: " + totalPrice);
		}else {
			JOptionPane.showMessageDialog(frame, "Voucher ID not valid", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == menuProducts) {
			frame.dispose();
			ProductHandler.getInstance().viewProductManagementForm();
		}else if(e.getSource() == menuLogout) {
			int choice = JOptionPane.showConfirmDialog(frame, "Are you sure want to logout?");
			
			if(choice == JOptionPane.YES_OPTION) {
				frame.dispose();
				AuthHandler.getInstance().logout();
			}
		}else if(e.getSource() == btnConfirm) {
			int choice = JOptionPane.showConfirmDialog(frame, "Are you sure want to confirm this transaction?");
			
			if(choice == JOptionPane.YES_OPTION) {
				confirm();
			}
		}else if(e.getSource() == btnVoucher) {
			useVoucher();
		}
		
	}

}
