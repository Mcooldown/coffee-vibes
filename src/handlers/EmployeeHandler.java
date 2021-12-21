package handlers;

import java.util.ArrayList;

import models.Employee;
import models.Position;
import views.EmployeeManagementForm;

public class EmployeeHandler {

	public static EmployeeHandler handler = null;
	public Employee employee;
	private String errorMessage;
	
	public EmployeeHandler() {
		employee = new Employee();
	}
	
	public static EmployeeHandler getInstance() {
		if(handler == null) {
			handler = new EmployeeHandler();
		}
		return handler;
	}

	public void viewEmployeeManagementForm() {
		new EmployeeManagementForm();
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	private boolean validateEmployee(String name, String salary, String username, String password) {
		if(name.equals("")) {
			errorMessage = "Name must be filled";
			return false;
		}else {
			if(salary.equals("")) {
				errorMessage = "Salary must be filled";
				return false;
			}
			int salaryInt = 0;
			try {
				salaryInt = Integer.parseInt(salary);
			} catch (Exception e) {
				errorMessage = "Salary must be number";
				return false;
			}
			if(salaryInt <= 0) {
				errorMessage = "Salary must be greater than 0";
				return false;
			}
			
			if(username.equals("")) {
				errorMessage = "Username must be filled";
				return false;
			}else if(password.equals("")) {
				errorMessage = "Password must be filled";
				return false;
			}
			return true;
		}
	}
	
	public boolean insertEmployee(String name, String positionID, String salary, String username, String password) {
		
		if(positionID.equals("")) {
			errorMessage = "Position ID must be filled";
			return false;	
		}else {
			Position position = PositionHandler.getInstance().getPosition(positionID);
			if(position.equals("")) {
				errorMessage = "Position ID not exist";
				return false;
			}else {
				
				boolean passValidate = validateEmployee(name, salary, username, password);
				
				if(passValidate) {
					Employee newEmployee = new Employee(0, position.getPositionID(), name, "Active", Integer.parseInt(salary), username, password);
					
					boolean inserted = newEmployee.insertEmployee();
					if(!inserted) {
						errorMessage = "Insert failed";
					}
					return inserted;
				}else {
					return false;
				}
			}
		}
	}
	
	public ArrayList<Employee> getAllEmployees(){
		return employee.getAllEmployees();
	}
	
	public Employee getEmployee(String username) {
		return employee.getEmployee(username);
	}
	
	public Employee getEmployeeById(String employeeID) {
		return employee.getEmployeeById(employeeID);
	}
	
	public boolean updateEmployee(String employeeID, String name, String salary, String username, String password) {
		
		if(employeeID.equals("")) {
			errorMessage = "Employee ID must be filled";
			return false;
		}else {
			Employee employee = getEmployeeById(employeeID);
			
			if(employee == null) {
				errorMessage = "Employee ID not exist";
				return false;
			}else {
				boolean passValidate = validateEmployee(name, salary, username, password);
				
				if(passValidate) {
					boolean updated = employee.updateEmployee(Integer.parseInt(employeeID), name, Integer.parseInt(salary), username, password);
					if(!updated) {
						errorMessage = "Update failed";
					}
					return updated;
				}else {
					return false;
				}
			}
		}
	}
	
	public boolean fireEmployee(String employeeID) {
		
		if(employeeID.equals("")) {
			errorMessage = "Employee ID must be filled";
			return false;
		}else {
			Employee employee = getEmployeeById(employeeID);
			
			if(employee == null) {
				errorMessage = "Employee ID not exist";
				return false;
			}else {
				boolean fired = employee.fireEmployee(employee.getEmployeeID());
				if(!fired) {
					errorMessage = "Fire Failed";
				}
				return fired;
			}
		}
	}
}
