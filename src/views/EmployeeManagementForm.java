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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import handlers.EmployeeHandler;
import handlers.PositionHandler;
import models.Employee;
import models.Position;

public class EmployeeManagementForm implements ActionListener {
	
	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu menuEmployees, menuProducts, menuVouchers, menuCart, menuTransactions;
	private JPanel mainPanel, headerPanel, tablePanel, formPanel, centerPanel, btnPanel;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton btnInsert, btnUpdate, btnDelete, btnCancel;
	private JTextField txtEmployeeID, txtPositionID, txtName, txtSalary, txtUsername, txtPassword;
	private JComboBox<Position> listPosition;
	private JLabel lblEmployeeID, lblPositionID, lblName, lblSalary, lblUsername, lblPassword;
	
	public EmployeeManagementForm() {
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

	private void initMenuBar() {
		menuBar = new JMenuBar();
		menuEmployees = new JMenu("Employees");
		menuEmployees.setOpaque(true);
		menuEmployees.setBackground(Color.ORANGE);
		menuProducts = new JMenu("Products");
		menuVouchers = new JMenu("Vouchers");
		menuCart = new JMenu("Cart");
		menuTransactions = new JMenu("Transactions");
		
		menuBar.add(menuEmployees);
		menuBar.add(menuProducts);
		menuBar.add(menuVouchers);
		menuBar.add(menuCart);
		menuBar.add(menuTransactions);
	}
	
	private void initHeaderPanel() {
		headerPanel = new JPanel(new GridLayout(1,2,10,0));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		JLabel title = new JLabel("Employee Management", SwingConstants.CENTER);
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
	
	private String[] getPositionArray() {
		
		ArrayList<Position> positions = PositionHandler.getInstance().getAllPositions();
		String[]positionArray = new String[positions.size()];
		for (int i = 0; i < positions.size(); i++) {
			positionArray[i]= String.valueOf(positions.get(i).getPositionID()) + "-" + positions.get(i).getName();
		}
		return positionArray;
	}
	
	private void initFormPanel() {
		formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
		formPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		
		lblEmployeeID = new JLabel("Employee ID");
		txtEmployeeID = new JTextField();
		
		String[]positionArray = getPositionArray();
		lblPositionID = new JLabel("Position");
		listPosition = new JComboBox(positionArray);

		lblName = new JLabel("Name");
		txtName = new JTextField();
		lblSalary = new JLabel("Salary");
		txtSalary = new JTextField();
		lblUsername = new JLabel("Username");
		txtUsername = new JTextField();
		lblPassword = new JLabel("Password");
		txtPassword = new JTextField();
		
		formPanel.add(lblEmployeeID);
		formPanel.add(txtEmployeeID);
		formPanel.add(lblPositionID);
		formPanel.add(listPosition);
		formPanel.add(lblName);
		formPanel.add(txtName);
		formPanel.add(lblSalary);
		formPanel.add(txtSalary);
		formPanel.add(lblUsername);
		formPanel.add(txtUsername);
		formPanel.add(lblPassword);
		formPanel.add(txtPassword);
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
		btnDelete = new JButton("FIRE");
		btnDelete.setBackground(Color.RED);
		btnCancel = new JButton("CANCEL");
		btnCancel.setBackground(Color.LIGHT_GRAY);
		
		btnPanel.add(btnInsert);
		btnPanel.add(btnCancel);
		btnPanel.add(btnUpdate);
		btnPanel.add(btnDelete);
	}

	private void initMainPanel() {
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(btnPanel, BorderLayout.SOUTH);
	}
	
	private void loadData() {
		String [] columns = {"Employee ID", "Position", "Name", "Salary", "Username", "Password"};
		DefaultTableModel dtm = new DefaultTableModel(columns, 0);
		
		Vector<Object> dataList;
		ArrayList<Employee> employees = EmployeeHandler.getInstance().getAllEmployees();
		
		for (Employee employee : employees) {
			dataList = new Vector<>();
			dataList.add(employee.getEmployeeID());
			Position position = PositionHandler.getInstance().getPosition(String.valueOf(employee.getPositionID()));
			dataList.add(position.getName());
			dataList.add(employee.getName());
			dataList.add(employee.getSalary());
			dataList.add(employee.getUsername());
			dataList.add(employee.getPassword());
			dtm.addRow(dataList);
		}
		
		table.setModel(dtm);
	}
	
	private void initActionListener() {
		btnInsert.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnDelete.addActionListener(this);
		btnCancel.addActionListener(this);
		menuProducts.addActionListener(this);
		menuVouchers.addActionListener(this);
		menuCart.addActionListener(this);
		
		table.addMouseListener(new MouseAdapter() {
		
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				txtEmployeeID.setText(String.valueOf(table.getValueAt(row,0)));
				txtName.setText((String) table.getValueAt(row, 2));
				txtSalary.setText(String.valueOf(table.getValueAt(row, 3)));
				txtUsername.setText((String) table.getValueAt(row, 4));
				txtPassword.setText((String) table.getValueAt(row, 5));
				setInsertView(false);
			}
		});
	}
	
	private void initFrame() {
		frame = new JFrame("Coffee Vibes - Group 2 - BD01");
		frame.setJMenuBar(menuBar);
		frame.add(mainPanel);
		frame.setSize(600, 600);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private String getSelectedPositionID() {
		String selectedPosition = listPosition.getSelectedItem().toString();
		String[] selectedPositionSplit = selectedPosition.split("-", 2);
		String positionID = selectedPositionSplit[0];
		return positionID;
	}
	
	public void insertEmployee() {
		String positionID = getSelectedPositionID();
		String name = txtName.getText().toString();
		String salary = txtSalary.getText().toString();
		String username = txtUsername.getText().toString();
		String password = txtPassword.getText().toString();
		
		boolean inserted = EmployeeHandler.getInstance().insertEmployee(name, positionID, salary, username, password);
		if(inserted) {
			JOptionPane.showMessageDialog(frame, "New Employee Successfully Inserted","Success", JOptionPane.INFORMATION_MESSAGE);
			loadData();
			clearForm();
		}else {
			JOptionPane.showMessageDialog(frame, EmployeeHandler.getInstance().getErrorMessage(),"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void updateEmployee() {
		String employeeID = txtEmployeeID.getText().toString();
		String name = txtName.getText().toString();
		String salary = txtSalary.getText().toString();
		String username = txtUsername.getText().toString();
		String password = txtPassword.getText().toString();
		
		boolean updated = EmployeeHandler.getInstance().updateEmployee(employeeID, name, salary, username, password);
		
		if(updated) {
			JOptionPane.showMessageDialog(frame, "Employee Data successfully updated","Success" ,JOptionPane.INFORMATION_MESSAGE);
			loadData();
			setInsertView(true);
			clearForm();
		}else {
			JOptionPane.showMessageDialog(frame, EmployeeHandler.getInstance().getErrorMessage() ,"Error",JOptionPane.ERROR_MESSAGE);			
		}
	}
	
	private void fireEmployee() {
		String employeeID = txtEmployeeID.getText().toString();
		
		boolean deleted = EmployeeHandler.getInstance().fireEmployee(employeeID);
		
		if(deleted) {
			JOptionPane.showMessageDialog(frame, "Employee successfully fired","Success" ,JOptionPane.INFORMATION_MESSAGE);
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
		
		lblEmployeeID.setVisible(!isInsert);
		txtEmployeeID.setVisible(!isInsert);
		lblPositionID.setVisible(isInsert);
		listPosition.setVisible(isInsert);
	}
	
	private void clearForm() {
		txtEmployeeID.setText("");
		txtName.setText("");
		txtSalary.setText("");
		txtUsername.setText("");
		txtPassword.setText("");
		listPosition.setSelectedIndex(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == btnInsert) {
			insertEmployee();
		}else if(e.getSource() == btnUpdate) {
			int choice = JOptionPane.showConfirmDialog(frame, "Are you sure want to update employee with ID:" + txtEmployeeID.getText().toString() + "?");
			
			if(choice == JOptionPane.YES_OPTION) {
				updateEmployee();
			}
		}else if(e.getSource() == btnDelete) {
			int choice = JOptionPane.showConfirmDialog(frame, "Are you sure want to delete employee with ID:" + txtEmployeeID.getText().toString() + "?");
			
			if(choice == JOptionPane.YES_OPTION) {
				fireEmployee();
			}
		}else if(e.getSource() == btnCancel) {
			setInsertView(true);
			clearForm();
		}else if(e.getSource() == menuProducts) {
			
		}else if(e.getSource() == menuVouchers) {
			
		}else if(e.getSource() == menuCart) {
			
		}else if(e.getSource() == menuTransactions) {
			
		}
		
	}
}