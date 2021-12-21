package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connect.Connect;

public class Position {

	private int positionID;
	private String name;
	private Connect connect = Connect.getConnection();
	
	public Position() {}
	
	public Position(int positionID, String name) {
		super();
		this.positionID = positionID;
		this.name = name;
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
	
	private Position map(ResultSet rs) {
		try {
			int rsPositionID = rs.getInt("positionID");
			String rsName = rs.getString("name");
			
			return new Position(rsPositionID, rsName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<Position> getAllPositions(){
		String query = "SELECT * FROM positions";
		ResultSet rs =connect.executeQuery(query);
	
		try {
			ArrayList<Position> positions = new ArrayList<>();
			while(rs.next()) {
				Position position = map(rs);
				positions.add(position);
			}
			return positions;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Position getPosition(int positionID) {
		String query = "SELECT * FROM positions WHERE positionID = " + positionID;
		ResultSet rs = connect.executeQuery(query);
		
		try {
			while(rs.next()) {
				Position position = map(rs);
				return position;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
	}
}
