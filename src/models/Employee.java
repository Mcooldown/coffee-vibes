package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connect.Connect;

public class Employee {
	
	private int employeeID;
	private int positionID;
	private String name;
	private String status;
	private int salary;
	private String username;
	private String password;
	private Connect connect = Connect.getConnection();
	
	public Employee() {}
	
	public Employee(int employeeID, int positionID, String name, String status, int salary, String username,
			String password) {
		super();
		this.employeeID = employeeID;
		this.positionID = positionID;
		this.name = name;
		this.status = status;
		this.salary = salary;
		this.username = username;
		this.password = password;
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	public int getPositionID() {
		return positionID;
	}

	public void setPositionID(int positionID) {
		this.positionID = positionID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean insertEmployee() {
		
		String query = "INSERT INTO employees (name, positionID, salary, status, username, password) VALUES (?,?,?,?,?,?)";
		PreparedStatement ps = connect.prepareStatement(query);
		
		try {
			ps.setString(1, name);
			ps.setInt(2, positionID);
			ps.setInt(3, salary);
			ps.setString(4, status);
			ps.setString(5, username);
			ps.setString(6, password);
			return ps.executeUpdate() == 1;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private Employee map(ResultSet rs) {
		try {
			int rsEmployeeID = rs.getInt("employeeID");
			String rsName = rs.getString("name");
			int rsPositionID = rs.getInt("positionID");
			int rsSalary = rs.getInt("salary");
			String rsStatus = rs.getString("status");
			String rsUsername = rs.getString("username");
			String rsPassword = rs.getString("password");
			
			return new Employee(rsEmployeeID, rsPositionID, rsName, rsStatus, rsSalary, rsUsername, rsPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<Employee> getAllEmployees(){
		String query = "SELECT * FROM employees";
		ResultSet rs =connect.executeQuery(query);
	
		try {
			ArrayList<Employee> employees = new ArrayList<>();
			while(rs.next()) {
				Employee employee = map(rs);
				employees.add(employee);
			}
			return employees;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Employee fetchEmployee(String key, String value) {
		String query = "SELECT * FROM employees WHERE " + key + " = " + value;
		ResultSet rs =connect.executeQuery(query);
	
		try {
			while(rs.next()) {
				Employee employee = map(rs);
				return employee;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Employee getEmployee(String username) {
		return fetchEmployee("username", username);
	}
	
	public Employee getEmployeeById(String employeeID) {
		return fetchEmployee("employeeID", employeeID);
	}
	
	public boolean updateEmployee(int employeeID, String name, int salary, String username, String password) {
		String query = "UPDATE employees SET name = ?, salary = ?, username = ?, password = ? WHERE employeeID = ?";
		PreparedStatement ps = connect.prepareStatement(query);
		
		try {
			ps.setString(1, name);
			ps.setInt(2, salary);
			ps.setString(3, username);
			ps.setString(4, password);
			ps.setInt(5, employeeID);
			return ps.executeUpdate() == 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean fireEmployee(int employeeID) {
		String query = "DELETE FROM employees WHERE employeeID = ?";
		PreparedStatement ps = connect.prepareStatement(query);
		
		try {
			ps.setInt(1, employeeID);
			return ps.executeUpdate() == 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
