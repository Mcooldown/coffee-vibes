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
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import controllers.AuthHandler;
import controllers.EmployeeHandler;
import controllers.PositionHandler;
import controllers.ProductHandler;
import controllers.TransactionHandler;
import models.Employee;
import models.Position;
import models.Product;
import models.Transaction;
import models.TransactionItem;

public class TransactionManagementForm implements ActionListener {

	private JFrame frame;
	private JMenuBar menuBar;
	private JMenuItem menuTransaction, menuEmployee, menuLogout;
	private JPanel mainPanel, headerPanel, tablePanel, detailPanel, detailTablePanel, centerPanel;
	private JTable tableTransaction ,tableDetail;
	private JScrollPane scrollPaneTransaction, scrollPaneDetail;
	private JLabel lblTransactionID, lblPurchaseDate, lblVoucherID, lblEmployee, lblTotalPrice;
	private Employee authUser;
	private Position authPosition;
	
	public TransactionManagementForm() {
		initAuthPosition();
		initMenuBar();
		initHeaderPanel();
		initTablePanel();
		initDetailPanel();
		initDetailTablePanel();
		initCenterPanel();
		initMainPanel();
		initActionListener();
		initFrame();
	}
	private void initAuthPosition() {
		authUser = AuthHandler.getInstance().getCurrentUser();
		if(authUser == null) AuthHandler.getInstance().logout();
		else {
			authPosition = PositionHandler.getInstance().getPosition(String.valueOf(authUser.getPositionID()));
			if(!authPosition.getName().equals("Manager")) {
				AuthHandler.getInstance().logout();
			}
		}
	}

	private void initMenuBar() {
		menuBar = new JMenuBar();
		menuEmployee = new JMenuItem("Employee");
		menuTransaction = new JMenuItem("Transaction");
		menuTransaction.setBackground(Color.ORANGE);
		menuLogout = new JMenuItem("Logout");
		
		menuBar.add(menuEmployee);
		menuBar.add(menuTransaction);			
		menuBar.add(menuLogout);
	}
	
	private void initHeaderPanel() {
		headerPanel = new JPanel(new GridLayout(1,2,10,0));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		JLabel title = new JLabel("Transaction Management", SwingConstants.CENTER);
		headerPanel.add(title);
	}
	
	private void initTablePanel() {
		tableTransaction = new JTable();
		loadDataTransaction();
		scrollPaneTransaction = new JScrollPane(tableTransaction);
		
		tablePanel = new JPanel(new GridLayout(1, 1, 10, 10));
		tablePanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		tablePanel.add(scrollPaneTransaction);
	}
	
	private void loadDataTransaction() {
		String [] columns = {"Transaction ID","Purchase Date","Voucher ID", "Employee", "Total Price"};
		DefaultTableModel dtm = new DefaultTableModel(columns, 0);
		
		Vector<Object> dataList;
		ArrayList<Transaction> transactions = TransactionHandler.getInstance().getAllTransactions();
		
		for (Transaction transaction : transactions) {
			dataList = new Vector<>();
			dataList.add(transaction.getTransactionID());
			dataList.add(transaction.getPurchaseDate());
			dataList.add(transaction.getVoucherID() == 0 ? "No Voucher" : transaction.getVoucherID());
			
			Employee employee = EmployeeHandler.getInstance().getEmployeeById(transaction.getEmployeeID() + "");
			dataList.add(employee != null ? employee.getName() : "");
			
			dataList.add(transaction.getTotalPrice());
			dtm.addRow(dataList);
		}
		tableTransaction.setModel(dtm);
	}
	
	private void initDetailPanel() {
		detailPanel = new JPanel(new GridLayout(3, 2 ,10,0));
		detailPanel.setBorder(BorderFactory.createEmptyBorder(0,10, 0, 10));
		
		lblTransactionID = new JLabel();
		lblPurchaseDate = new JLabel();
		lblVoucherID = new JLabel();
		lblEmployee = new JLabel();
		lblTotalPrice = new JLabel();
		
		detailPanel.add(lblTransactionID);
		detailPanel.add(lblPurchaseDate);
		detailPanel.add(lblVoucherID);
		detailPanel.add(lblEmployee);
		detailPanel.add(lblTotalPrice);
	}
	
	private void initDetailTablePanel() {
		tableDetail = new JTable();
		scrollPaneDetail = new JScrollPane(tableDetail);
		
		detailTablePanel = new JPanel(new GridLayout(1, 1, 10, 10));
		detailTablePanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		detailTablePanel.add(scrollPaneDetail);
	}
	
	private void initCenterPanel() {
		centerPanel = new JPanel(new GridLayout(2,1,10,10));
		centerPanel.add(detailPanel);
		centerPanel.add(detailTablePanel);
		centerPanel.setVisible(false);
	}

	private void initMainPanel() {
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		
		JPanel subMainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
		subMainPanel.add(tablePanel);
		subMainPanel.add(centerPanel);
		mainPanel.add(subMainPanel);
	}
	
	private void initActionListener() {
		menuEmployee.addActionListener(this);
		menuLogout.addActionListener(this);
		
		tableTransaction.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				int row = tableTransaction.getSelectedRow();
				lblTransactionID.setText("Transaction ID: " +tableTransaction.getValueAt(row,0));
				lblPurchaseDate.setText("Purchase Date: " + tableTransaction.getValueAt(row, 1));
				lblVoucherID.setText("Voucher ID: " + tableTransaction.getValueAt(row,2));
				lblEmployee.setText("Employee: " + tableTransaction.getValueAt(row, 3));
				lblTotalPrice.setText("Total Price: " + tableTransaction.getValueAt(row,4));
				
				loadDataDetailTransaction((int)tableTransaction.getValueAt(row,0));
				centerPanel.setVisible(true);
			}
		});
	}
	
	private void loadDataDetailTransaction(int transactionID) {
		String [] columns = {"Product ID","Product Name","Product Price","Quantity"};
		DefaultTableModel dtm = new DefaultTableModel(columns, 0);
		
		Vector<Object> dataList;
		ArrayList<TransactionItem> transactionItems = TransactionHandler.getInstance().getTransactionDetail(transactionID);
		
		for (TransactionItem transactionItem : transactionItems) {
			dataList = new Vector<>();
			Product product = ProductHandler.getInstance().getProduct(transactionItem.getProductID() + "");
			if(product != null) {
				dataList.add(product.getProductID());
				dataList.add(product.getName());
				dataList.add(product.getPrice());
				dataList.add(transactionItem.getQuantity());
				dtm.addRow(dataList);
			}
		}
		tableDetail.setModel(dtm);
	}
	
	private void initFrame() {
		frame = new JFrame("Coffee Vibes - Group 2 - BD01");
		frame.setJMenuBar(menuBar);
		frame.add(mainPanel);
		frame.setSize(650, 700);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == menuEmployee) {
			frame.dispose();
			EmployeeHandler.getInstance().viewEmployeeManagementForm();
		}else if(e.getSource() == menuLogout) {
			int choice = JOptionPane.showConfirmDialog(frame, "Are you sure want to logout?");
			
			if(choice == JOptionPane.YES_OPTION) {
				frame.dispose();
				AuthHandler.getInstance().logout();
			}
		}
	}

}
