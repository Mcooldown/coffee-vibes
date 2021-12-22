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
import models.Employee;
import models.Position;
import models.Product;

public class ProductManagementForm implements ActionListener {

	private JFrame frame;
	private JMenuBar menuBar;
	private JMenuItem menuProducts, menuCart,  menuVouchers, menuLogout;
	private JPanel mainPanel, headerPanel, tablePanel, formPanel, centerPanel, btnPanel;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton btnInsert, btnUpdate, btnDelete, btnCancel, btnAddToCart;
	private JTextField txtProductID, txtName, txtDescription, txtPrice, txtStock;
	private JLabel lblProductID, lblName, lblDescription, lblPrice, lblStock;
	private Employee authUser;
	private Position authPosition;
	
	public ProductManagementForm() {
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
		}
	}

	private void initMenuBar() {
		menuBar = new JMenuBar();
		menuProducts = new JMenuItem("Products");
		menuProducts.setOpaque(true);		
		menuProducts.setBackground(Color.ORANGE);
		menuVouchers = new JMenuItem("Vouchers");
		menuCart = new JMenuItem("Cart");					
		menuLogout = new JMenuItem("Logout");
		
		menuBar.add(menuProducts);			
		if(authPosition.getName().equals("Product Admin")) {
			menuBar.add(menuVouchers);			
		}
		if(authPosition.getName().equals("Barista")) {
			menuBar.add(menuCart);
		}
		
		menuBar.add(menuLogout);
	}
	
	private void initHeaderPanel() {
		headerPanel = new JPanel(new GridLayout(1,2,10,0));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		JLabel title = new JLabel("Product Management", SwingConstants.CENTER);
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
		lblDescription = new JLabel("Description");
		txtDescription = new JTextField();
		lblPrice = new JLabel("Price");
		txtPrice = new JTextField();
		lblStock = new JLabel("Stock");
		txtStock = new JTextField();
		
		if(authPosition.getName().equals("Barista")) {
			txtProductID.setEditable(false);
			txtName.setEditable(false);
			txtDescription.setEditable(false);
			txtPrice.setEditable(false);
			txtStock.setEditable(false);
		}
		
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
	}
	
	private void initCenterPanel() {
		centerPanel = new JPanel(new GridLayout(2, 1, 10,10));
		centerPanel.add(tablePanel);
		centerPanel.add(formPanel);
	}
	
	private void initBtnPanel() {
		btnPanel = new JPanel(new GridLayout(1, 5, 10, 10));
		btnPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		btnInsert = new JButton("INSERT");
		btnInsert.setBackground(Color.ORANGE);
		btnUpdate = new JButton("UPDATE");
		btnUpdate.setBackground(Color.YELLOW);
		btnDelete = new JButton("DELETE");
		btnDelete.setBackground(Color.RED);
		btnCancel = new JButton("CANCEL");
		btnCancel.setBackground(Color.LIGHT_GRAY);
		btnAddToCart = new JButton("ADD TO CART");
		btnAddToCart.setBackground(Color.GREEN);
		

		if(authPosition.getName().equals("Product Admin")) {
			btnPanel.add(btnInsert);
			btnPanel.add(btnUpdate);
			btnPanel.add(btnDelete);			
		}
		btnPanel.add(btnCancel);

		if(authPosition.getName().equals("Barista")) {
			btnPanel.add(btnAddToCart);			
		}
	}

	private void initMainPanel() {
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(btnPanel, BorderLayout.SOUTH);
	}
	
	private void loadData() {
		String [] columns = {"Product ID", "Name", "Description","Price", "Stock"};
		DefaultTableModel dtm = new DefaultTableModel(columns, 0);
		
		Vector<Object> dataList;
		ArrayList<Product> products = ProductHandler.getInstance().getAllProducts();
		
		for (Product product : products) {
			dataList = new Vector<>();
			dataList.add(product.getProductID());
			dataList.add(product.getName());
			dataList.add(product.getDescription());
			dataList.add(product.getPrice());
			dataList.add(product.getStock());
			dtm.addRow(dataList);
		}

		table.setModel(dtm);
	}
	
	private void initActionListener() {
		btnInsert.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnDelete.addActionListener(this);
		btnCancel.addActionListener(this);
		btnAddToCart.addActionListener(this);
		menuVouchers.addActionListener(this);
		menuCart.addActionListener(this);
		menuLogout.addActionListener(this);
		
		table.addMouseListener(new MouseAdapter() {
		
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				txtProductID.setText(String.valueOf(table.getValueAt(row,0)));
				txtName.setText((String) table.getValueAt(row, 1));
				txtDescription.setText((String) table.getValueAt(row, 2));
				txtPrice.setText(String.valueOf(table.getValueAt(row, 3)));
				txtStock.setText(String.valueOf(table.getValueAt(row, 4)));
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
	
	
	public void insertProduct() {
		String name = txtName.getText().toString();
		String description = txtDescription.getText().toString();
		String price = txtPrice.getText().toString();
		String stock = txtStock.getText().toString();
		
		boolean inserted = ProductHandler.getInstance().insertProduct(name, description, stock, price);
		if(inserted) {
			JOptionPane.showMessageDialog(frame, "New Product Successfully Inserted","Success", JOptionPane.INFORMATION_MESSAGE);
			loadData();
			clearForm();
		}else {
			JOptionPane.showMessageDialog(frame, ProductHandler.getInstance().getErrorMessage(),"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void updateEmployee() {
		String productID = txtProductID.getText().toString();
		String name = txtName.getText().toString();
		String description = txtDescription.getText().toString();
		String price = txtPrice.getText().toString();
		
		boolean updated = ProductHandler.getInstance().updateProduct(productID, name, description, price);
		
		if(updated) {
			JOptionPane.showMessageDialog(frame, "Product Detail Data successfully updated","Success" ,JOptionPane.INFORMATION_MESSAGE);
			loadData();
			setInsertView(true);
			clearForm();
		}else {
			JOptionPane.showMessageDialog(frame, EmployeeHandler.getInstance().getErrorMessage() ,"Error",JOptionPane.ERROR_MESSAGE);			
		}
	}
	
	private void deleteProduct() {
		String productID = txtProductID.getText().toString();
		
		boolean deleted = ProductHandler.getInstance().deleteProduct(productID);
		
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
		btnInsert.setVisible(isInsert);
		btnUpdate.setVisible(!isInsert);
		btnDelete.setVisible(!isInsert);
		btnCancel.setVisible(!isInsert);
		btnAddToCart.setVisible(!isInsert);
		
		lblProductID.setVisible(!isInsert);
		txtProductID.setVisible(!isInsert);
		txtStock.setEditable(isInsert);
		
		if(authPosition.getName().equals("Barista")) {
			txtStock.setEditable(false);
		}else {
			txtStock.setEditable(isInsert);
		}
	}
	
	private void clearForm() {
		txtProductID.setText("");
		txtName.setText("");
		txtDescription.setText("");
		txtStock.setText("");
		txtPrice.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == btnInsert) {
			insertProduct();
		}else if(e.getSource() == btnUpdate) {
			int choice = JOptionPane.showConfirmDialog(frame, "Are you sure want to update product with ID:" + txtProductID.getText().toString() + "?");
			
			if(choice == JOptionPane.YES_OPTION) {
				updateEmployee();
			}
		}else if(e.getSource() == btnDelete) {
			int choice = JOptionPane.showConfirmDialog(frame, "Are you sure want to delete product with ID:" + txtProductID.getText().toString() + "?");
			
			if(choice == JOptionPane.YES_OPTION) {
				deleteProduct();
			}
		}else if(e.getSource() == btnCancel) {
			setInsertView(true);
			clearForm();
		}else if(e.getSource() == btnAddToCart) {
			try {
				int productID = Integer.parseInt(txtProductID.getText().toString());
				Product product = CartHandler.getInstance().getProduct(productID);
				
				if(product != null) {
					frame.dispose();
					CartHandler.getInstance().viewAddProductToCartForm(product);									
				}else {
					JOptionPane.showMessageDialog(frame, "Product ID not valid", "Error", JOptionPane.ERROR_MESSAGE);					
				}
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(frame, "Product ID not valid", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}else if(e.getSource() == menuVouchers) {
			frame.dispose();
			VoucherHandler.getInstance().viewVoucherManagementForm();
		}else if(e.getSource() == menuCart) {
			frame.dispose();
			CartHandler.getInstance().viewCart();
		}else if(e.getSource() == menuLogout) {
			int choice = JOptionPane.showConfirmDialog(frame, "Are you sure want to logout?");
			
			if(choice == JOptionPane.YES_OPTION) {
				frame.dispose();
				AuthHandler.getInstance().logout();
			}
		}
		
	}

}
