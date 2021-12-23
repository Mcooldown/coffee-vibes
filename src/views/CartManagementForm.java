package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import controllers.VoucherHandler;
import models.CartItem;
import models.Employee;
import models.Position;
import models.Product;

public class CartManagementForm implements ActionListener {

	private JFrame frame;
	private JMenuBar menuBar;
	private JMenuItem menuProducts, menuCart, menuLogout;
	private JPanel mainPanel, headerPanel, tablePanel, formPanel, centerPanel, btnPanel;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton btnDelete, btnCancel, btnCheckout;
	private JTextField txtProductID, txtName,  txtPrice, txtQuantity;
	private JLabel lblProductID, lblName, lblPrice,  lblQuantity;
	private Employee authUser;
	private Position authPosition;
	
	public CartManagementForm() {
		initAuthPosition();
		initMenuBar();
		initHeaderPanel();
		initTablePanel();
		initFormPanel();
		initCenterPanel();
		initBtnPanel();
		initMainPanel();
		initActionListener();
		initFrame();
		setInsertView(true);
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
		menuCart.setOpaque(true);		
		menuCart.setBackground(Color.ORANGE);
		menuLogout = new JMenuItem("Logout");
		
		menuBar.add(menuProducts);			
		menuBar.add(menuCart);
		menuBar.add(menuLogout);
	}
	
	private void initHeaderPanel() {
		headerPanel = new JPanel(new GridLayout(1,2,10,0));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		JLabel title = new JLabel("Cart Management", SwingConstants.CENTER);
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
		formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
		formPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		
		lblProductID = new JLabel("Product ID");
		txtProductID = new JTextField();
		
		lblName = new JLabel("Name");
		txtName = new JTextField();
		lblPrice = new JLabel("Price");
		txtPrice = new JTextField();
		lblQuantity = new JLabel("Quantity");
		txtQuantity = new JTextField();
		
		txtProductID.setEditable(false);
		txtName.setEditable(false);
		txtPrice.setEditable(false);
		txtQuantity.setEditable(false);
		
		formPanel.add(lblProductID);
		formPanel.add(txtProductID);
		formPanel.add(lblName);
		formPanel.add(txtName);
		formPanel.add(lblPrice);
		formPanel.add(txtPrice);
		formPanel.add(lblQuantity);
		formPanel.add(txtQuantity);
	}
	
	private void initCenterPanel() {
		centerPanel = new JPanel(new GridLayout(2, 1, 10,10));
		centerPanel.add(tablePanel);
		centerPanel.add(formPanel);
	}
	
	private void initBtnPanel() {
		btnPanel = new JPanel(new GridLayout(1, 5, 10, 10));
		btnPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		btnDelete = new JButton("DELETE");
		btnDelete.setBackground(Color.RED);
		btnCancel = new JButton("CANCEL");
		btnCancel.setBackground(Color.LIGHT_GRAY);
		btnCheckout = new JButton("CHECKOUT");
		btnCheckout.setBackground(Color.GREEN);
		
		btnPanel.add(btnCheckout);
		btnPanel.add(btnCancel);
		btnPanel.add(btnDelete);
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
		
		for (CartItem cartItem : cartItems) {
			dataList = new Vector<>();
			dataList.add(cartItem.getProduct().getProductID());
			dataList.add(cartItem.getProduct().getName());
			dataList.add(cartItem.getProduct().getPrice());
			dataList.add(cartItem.getQuantity());
			dtm.addRow(dataList);
		}

		table.setModel(dtm);
	}
	
	private void initActionListener() {
		btnDelete.addActionListener(this);
		btnCancel.addActionListener(this);
		btnCheckout.addActionListener(this);
		menuProducts.addActionListener(this);
		menuLogout.addActionListener(this);
		
		table.addMouseListener(new MouseAdapter() {
		
			public void mouseClicked(MouseEvent e) {
				int selectedRow = table.getSelectedRow();
				txtProductID.setText(String.valueOf(table.getValueAt(selectedRow,0)));
				txtName.setText((String) table.getValueAt(selectedRow, 1));
				txtPrice.setText(String.valueOf(table.getValueAt(selectedRow, 2)));
				txtQuantity.setText(String.valueOf(table.getValueAt(selectedRow, 3)));
				setInsertView(false);
			}
		});
	}
	
	private void initFrame() {
		frame = new JFrame("Coffee Vibes - Group 2 - BD01");
		frame.setJMenuBar(menuBar);
		frame.add(mainPanel);
		frame.setSize(650, 600);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void deleteCartItem() {
		String productID = txtProductID.getText().toString();
		
		boolean deleted = CartHandler.getInstance().removeProductFromCart(Integer.parseInt(productID));
		
		if(deleted) {
			JOptionPane.showMessageDialog(frame, "Product successfully deleted","Success" ,JOptionPane.INFORMATION_MESSAGE);
			loadData();
			setInsertView(true);
			clearForm();
		}else {
			JOptionPane.showMessageDialog(frame, EmployeeHandler.getInstance().getErrorMessage() ,"Error",JOptionPane.ERROR_MESSAGE);			
		}
	}
	
	private void setInsertView(boolean isInsert) {
		btnCheckout.setVisible(isInsert);
		btnDelete.setVisible(!isInsert);
		btnCancel.setVisible(!isInsert);
	}
	
	private void clearForm() {
		txtProductID.setText("");
		txtName.setText("");
		txtPrice.setText("");
		txtQuantity.setText("");
	}
	
	private void checkout() {
		if(CartHandler.getInstance().getCart().size() > 0) {
			frame.dispose();
			CartHandler.getInstance().viewCheckOutForm();			
		}else {
			JOptionPane.showMessageDialog(frame, "Cannot checkout. No items in the cart", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == btnCheckout) {
			checkout();
		}else if(e.getSource() == btnDelete) {
			int choice = JOptionPane.showConfirmDialog(frame, "Are you sure want to delete product with ID : "+ txtProductID.getText().toString() + "?");
			
			if(choice == JOptionPane.YES_OPTION) {
				deleteCartItem();
			}
		}else if(e.getSource() == btnCancel) {
			setInsertView(true);
			clearForm();
		}else if(e.getSource() == menuProducts) {
			frame.dispose();
			ProductHandler.getInstance().viewProductManagementForm();
		}else if(e.getSource() == menuLogout) {
			int choice = JOptionPane.showConfirmDialog(frame, "Are you sure want to logout?");
			
			if(choice == JOptionPane.YES_OPTION) {
				frame.dispose();
				AuthHandler.getInstance().logout();
			}
		}
		
	}

}
