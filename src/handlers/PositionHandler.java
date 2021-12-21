package handlers;

import java.util.ArrayList;

import models.Position;

public class PositionHandler {

	public static PositionHandler handler = null;
	public Position position;
	
	public PositionHandler() {
		position = new Position();
	}
	
	public static PositionHandler getInstance() {
		if(handler == null) {
			handler = new PositionHandler();
		}
		return handler;
	}
	
	public ArrayList<Position> getAllPositions(){
		return position.getAllPositions();
	}
	
	public Position getPosition(String positionID) {
		
		int positionIDInt = 0;
		try {
			positionIDInt = Integer.parseInt(positionID);
		} catch (Exception e) {
			return null;
		}
		
		return position.getPosition(positionIDInt);
	}
}
