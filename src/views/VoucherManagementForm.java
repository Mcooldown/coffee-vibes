package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

import handlers.AuthHandler;
import handlers.EmployeeHandler;
import handlers.PositionHandler;
import handlers.ProductHandler;
import handlers.VoucherHandler;
import models.Employee;
import models.Position;
import models.Voucher;

public class VoucherManagementForm implements ActionListener {
	
	private JFrame frame;
	private JMenuBar menuBar;
	private JMenuItem menuEmployees, menuProducts, menuVouchers, menuCart, menuTransactions, menuLogout;
	private JPanel mainPanel, headerPanel, tablePanel, formPanel, centerPanel, btnPanel;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton btnInsert, btnDelete, btnCancel;
	private JTextField txtVoucherID, txtDiscount;
	private JLabel lblVoucherID, lblDiscount;
	private Employee authUser;
	private Position authPosition;
	
	public VoucherManagementForm() {
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
		menuVouchers = new JMenuItem("Vouchers");
		menuVouchers.setOpaque(true);		
		menuVouchers.setBackground(Color.ORANGE);
		menuLogout = new JMenuItem("Logout");
		
		menuBar.add(menuProducts);
		if(authPosition.getName().equals("Product Admin")) {
			menuBar.add(menuVouchers);			
		}
		
		menuBar.add(menuLogout);
	}
	
	private void initHeaderPanel() {
		headerPanel = new JPanel(new GridLayout(1,2,10,0));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		JLabel title = new JLabel("Voucher Management", SwingConstants.CENTER);
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
		formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
		formPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		
		lblVoucherID = new JLabel("Voucher ID");
		txtVoucherID = new JTextField();
		
		lblDiscount = new JLabel("Discount");
		txtDiscount = new JTextField();
		
		formPanel.add(lblVoucherID);
		formPanel.add(txtVoucherID);
		formPanel.add(lblDiscount);
		formPanel.add(txtDiscount);
	}
	
	private void initCenterPanel() {
		centerPanel = new JPanel(new GridLayout(2, 1, 10,10));
		centerPanel.add(tablePanel);
		centerPanel.add(formPanel);
	}
	
	private void initBtnPanel() {
		btnPanel = new JPanel(new GridLayout(1, 5, 10, 10));
		btnPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		btnInsert = new JButton("GENERATE");
		btnInsert.setBackground(Color.ORANGE);
		btnDelete = new JButton("DELETE");
		btnDelete.setBackground(Color.RED);
		btnCancel = new JButton("CANCEL");
		btnCancel.setBackground(Color.LIGHT_GRAY);
		
		btnPanel.add(btnInsert);
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
		String [] columns = {"Voucher ID", "Discount", "Status"};
		DefaultTableModel dtm = new DefaultTableModel(columns, 0);
		
		Vector<Object> dataList;
		ArrayList<Voucher> vouchers = VoucherHandler.getInstance().getAllVouchers();
		
		for (Voucher voucher : vouchers) {
			dataList = new Vector<>();
			dataList.add(voucher.getVoucherID());
			dataList.add(voucher.getDiscount());
			dataList.add(voucher.getStatus());
			dtm.addRow(dataList);
		}
		
		table.setModel(dtm);
	}
	
	private void initActionListener() {
		btnInsert.addActionListener(this);
		btnDelete.addActionListener(this);
		btnCancel.addActionListener(this);
		menuProducts.addActionListener(this);
		menuLogout.addActionListener(this);
		
		table.addMouseListener(new MouseAdapter() {
		
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				txtVoucherID.setText(String.valueOf(table.getValueAt(row,0)));
				txtDiscount.setText((String) table.getValueAt(row, 1));
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
	
	public void generateVoucher() {
		String discount = txtDiscount.getText().toString();
		
		boolean inserted = VoucherHandler.getInstance().insertVoucher(discount);
		if(inserted) {
			JOptionPane.showMessageDialog(frame, "New Voucher successfully generated","Success", JOptionPane.INFORMATION_MESSAGE);
			loadData();
			clearForm();
		}else {
			JOptionPane.showMessageDialog(frame, VoucherHandler.getInstance().getErrorMessage(),"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void deleteVoucher() {
		String voucherID = txtVoucherID.getText().toString();
		
		boolean deleted = VoucherHandler.getInstance().deleteVoucher(voucherID);
		
		if(deleted) {
			JOptionPane.showMessageDialog(frame, "Voucher successfully deleted","Success" ,JOptionPane.INFORMATION_MESSAGE);
			loadData();
			setInsertView(true);
			clearForm();
		}else {
			JOptionPane.showMessageDialog(frame, VoucherHandler.getInstance().getErrorMessage() ,"Error",JOptionPane.ERROR_MESSAGE);			
		}
	}
	
	private void setInsertView(boolean isInsert) {
		btnInsert.setVisible(isInsert);
		btnDelete.setVisible(!isInsert);
		btnCancel.setVisible(!isInsert);
		
		lblVoucherID.setVisible(!isInsert);
		txtVoucherID.setVisible(!isInsert);
	}
	
	private void clearForm() {
		txtVoucherID.setText("");
		txtDiscount.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == btnInsert) {
			generateVoucher();
		}else if(e.getSource() == btnDelete) {
			int choice = JOptionPane.showConfirmDialog(frame, "Are you sure want to delete voucher with ID:" + txtVoucherID.getText().toString() + "?");
			
			if(choice == JOptionPane.YES_OPTION) {
				deleteVoucher();
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
