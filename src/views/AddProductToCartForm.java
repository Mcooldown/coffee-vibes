package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import handlers.AuthHandler;
import handlers.CartHandler;
import handlers.PositionHandler;
import handlers.ProductHandler;
import models.Employee;
import models.Position;
import models.Product;

public class AddProductToCartForm implements ActionListener {

	private JFrame frame;
	private JMenuBar menuBar;
	private JMenuItem menuProducts, menuCart, menuLogout;
	private JPanel mainPanel, headerPanel, formPanel, btnPanel;
	private JButton btnCancel, btnAddToCart;
	private JTextField txtProductID, txtName, txtDescription, txtPrice, txtStock, txtQuantity;
	private JLabel lblProductID, lblName, lblDescription, lblPrice, lblStock, lblQuantity;
	private Employee authUser;
	private Position authPosition;
	private Product product;
	
	public AddProductToCartForm(Product product) {
		
		this.product = product;
		initAuthPosition();
		initMenuBar();
		initHeaderPanel();
		initFormPanel();
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
			if(!authPosition.getName().equals("Barista")) AuthHandler.getInstance().logout();
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
		JLabel title = new JLabel("Add Product To Cart", SwingConstants.CENTER);
		headerPanel.add(title);
	}
	
	private void initFormPanel() {
		formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
		formPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		
		lblProductID = new JLabel("Product ID");
		txtProductID = new JTextField();
		
		lblName = new JLabel("Name");
		txtName = new JTextField();
		lblDescription = new JLabel("Description");
		txtDescription = new JTextField();
		lblPrice = new JLabel("Price");
		txtPrice = new JTextField();
		lblStock = new JLabel("Stock");
		txtStock = new JTextField();
		lblQuantity = new JLabel("Quantity");
		txtQuantity = new JTextField();
		
		txtProductID.setEditable(false);
		txtProductID.setText(String.valueOf(product.getProductID()));

		txtName.setEditable(false);
		txtName.setText(product.getName());
		
		txtDescription.setEditable(false);
		txtDescription.setText(product.getDescription());
		
		txtPrice.setEditable(false);
		txtPrice.setText(String.valueOf(product.getPrice()));
		
		txtStock.setEditable(false);
		txtStock.setText(String.valueOf(product.getStock()));
		
		formPanel.add(lblProductID);
		formPanel.add(txtProductID);
		formPanel.add(lblName);
		formPanel.add(txtName);
		formPanel.add(lblDescription);
		formPanel.add(txtDescription);
		formPanel.add(lblPrice);
		formPanel.add(txtPrice);
		formPanel.add(lblStock);
		formPanel.add(txtStock);
		formPanel.add(lblQuantity);
		formPanel.add(txtQuantity);
	}
	
	private void initBtnPanel() {
		btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
		btnPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		btnCancel = new JButton("CANCEL");
		btnCancel.setBackground(Color.LIGHT_GRAY);
		btnAddToCart = new JButton("ADD TO CART");
		btnAddToCart.setBackground(Color.GREEN);
		
		btnPanel.add(btnCancel);
		btnPanel.add(btnAddToCart);
	}

	private void initMainPanel() {
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(formPanel, BorderLayout.CENTER);
		mainPanel.add(btnPanel, BorderLayout.SOUTH);
	}
	
	private void initActionListener() {
		btnCancel.addActionListener(this);
		btnAddToCart.addActionListener(this);
		menuCart.addActionListener(this);
		menuLogout.addActionListener(this);
	}
	
	private void initFrame() {
		frame = new JFrame("Coffee Vibes - Group 2 - BD01");
		frame.setJMenuBar(menuBar);
		frame.add(mainPanel);
		frame.setSize(650, 450);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	
	public void addToCart() {
		String quantity = txtQuantity.getText().toString();
		
		boolean added = CartHandler.getInstance().addToCart(String.valueOf(product.getProductID()), quantity);
		if(added) {
			JOptionPane.showMessageDialog(frame, "Product Successfully Added to cart","Success", JOptionPane.INFORMATION_MESSAGE);
			frame.dispose();
			ProductHandler.getInstance().viewProductManagementForm();
		}else {
			JOptionPane.showMessageDialog(frame, CartHandler.getInstance().getErrorMessage(),"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == btnCancel || e.getSource() == menuProducts) {
			frame.dispose();
			ProductHandler.getInstance().viewProductManagementForm();
		}else if(e.getSource() == btnAddToCart) {
			addToCart();
		}else if(e.getSource() == menuCart) {
			frame.dispose();
			CartHandler.getInstance().viewCart();
		}else if(e.getSource() == menuLogout) {
			frame.dispose();
			AuthHandler.getInstance().logout();
		}
		
	}

}
