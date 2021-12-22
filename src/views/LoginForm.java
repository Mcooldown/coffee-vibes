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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controllers.AuthHandler;
import controllers.EmployeeHandler;
import controllers.PositionHandler;
import controllers.ProductHandler;
import controllers.VoucherHandler;
import models.Employee;
import models.Position;

public class LoginForm implements ActionListener {

	private JFrame frame;
	private JPanel mainPanel, headerPanel, formPanel, btnPanel;
	private JButton btnLogin;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JLabel lblUsername, lblPassword;
	
	public LoginForm() {
		initHeaderPanel();
		initFormPanel();
		initBtnPanel();
		initMainPanel();
		initListeners();
		initFrame();
	}
	
	private void initHeaderPanel() {
		headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		JLabel welcome = new JLabel("Welcome to Coffee Vibes", SwingConstants.CENTER);
		JLabel title = new JLabel("Login", SwingConstants.CENTER);
		headerPanel.add(welcome, BorderLayout.NORTH);
		headerPanel.add(title, BorderLayout.CENTER);
	}
	
	private void initFormPanel() {
		formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
		formPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		
		lblUsername = new JLabel("Username");
		txtUsername = new JTextField();
		lblPassword = new JLabel("Password");
		txtPassword = new JPasswordField();
		
		formPanel.add(lblUsername);
		formPanel.add(txtUsername);
		formPanel.add(lblPassword);
		formPanel.add(txtPassword);
	}
	
	private void initBtnPanel() {
		btnPanel = new JPanel(new GridLayout(1, 1, 10, 10));
		btnPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
		btnLogin = new JButton("LOGIN");
		btnLogin.setBackground(Color.ORANGE);
		
		btnPanel.add(btnLogin);
	}
	
	private void initMainPanel() {
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(formPanel, BorderLayout.CENTER);
		mainPanel.add(btnPanel, BorderLayout.SOUTH);
	}
	
	private void initListeners() {
		btnLogin.addActionListener(this);
	}
	
	private void initFrame() {
		frame = new JFrame("Coffee Vibes - Group 2 - BD01");
		frame.add(mainPanel);
		frame.setSize(500, 225);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void login() {
		
		String username = txtUsername.getText().toString();
		String password = String.valueOf(txtPassword.getPassword());
		
		boolean auth = AuthHandler.getInstance().login(username, password);
		
		if(auth) {
			
			Employee authUser = AuthHandler.getInstance().getCurrentUser();
			Position position = PositionHandler.getInstance().getPosition(String.valueOf(authUser.getPositionID()));
			
			JOptionPane.showMessageDialog(frame, "Login success", "Success", JOptionPane.INFORMATION_MESSAGE);
			
			frame.dispose();
			if(position.getName().equals("HRD") || position.getName().equals("Manager")) {
				EmployeeHandler.getInstance().viewEmployeeManagementForm();
			}else if(position.getName().equals("Product Admin") || position.getName().equals("Barista")) {
				ProductHandler.getInstance().viewProductManagementForm();
			}
		}else {
			JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnLogin) {
			login();
		}
	}

}
