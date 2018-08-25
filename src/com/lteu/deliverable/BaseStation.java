package com.lteu.deliverable;

import java.util.ArrayList;
import com.lteu.deliverable.Location;
import com.lteu.deliverable.UserEquipment;


public class BaseStation { 
	private int id;
	private Location location;
	private double txPower;
	private ArrayList<UserEquipment> usersAssociated;
	
	public BaseStation(int id, Location location, double txPower, ArrayList<UserEquipment> usersAssociated) {
		super();
		this.id = id;
		this.location = location;
		this.txPower = txPower;
		this.usersAssociated = usersAssociated;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public double getTxPower() {
		return txPower;
	}

	public void setTxPower(double txPower) {
		this.txPower = txPower;
	}

	public ArrayList<UserEquipment> getUsersAssociated() {
		return usersAssociated;
	}

	public void setUsersAssociated(ArrayList<UserEquipment> usersAssociated) {
		this.usersAssociated = usersAssociated;
	}
	
}
