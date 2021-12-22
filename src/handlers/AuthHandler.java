package handlers;

import models.Employee;
import views.LoginForm;

public class AuthHandler {

	private static AuthHandler handler = null;
	private Employee authUser, employee;
	private String errorMessage;
	
	public AuthHandler() {
		employee = new Employee();
	}
	
	public static AuthHandler getInstance() {
		if(handler == null) {
			handler = new AuthHandler();
		}
		return handler;
	}
	
	public void viewLoginForm() {
		new LoginForm();
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public Employee getCurrentUser() {
		return authUser;
	}

	public boolean login(String username, String password) {

		Employee existUser = employee.getEmployee(username);
		
		if(existUser != null) {
			if(existUser.getPassword().equals(password)) {
				authUser = new Employee(
						existUser.getEmployeeID(), 
						existUser.getPositionID(),
						existUser.getName(),
						existUser.getStatus(),
						existUser.getSalary(),
						existUser.getUsername(),
						existUser.getPassword());
				return true;
			}else {
				errorMessage = "Invalid username or password";
				return false;
			}
		}else {
			errorMessage = "Invalid username or password";
			return false;
		}
	}
	
	public void logout() {
		authUser = null;
		new LoginForm();
	}
}
