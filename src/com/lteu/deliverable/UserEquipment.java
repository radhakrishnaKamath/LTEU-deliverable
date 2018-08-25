package com.lteu.deliverable;

import java.util.ArrayList;

public class UserEquipment {
	
	private int id;
	private Location loc;
	private double dataRequest;
	private ArrayList<Double> signalStrength = new ArrayList<Double>();
	private BaseStation bts;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Location getLoc() {
		return loc;
	}
	
	public void setLoc(Location loc) {
		this.loc = loc;
	}
	
	public double getDataRequest() {
		return dataRequest;
	}
	
	public void setDataRequest(double dataRequest) {
		this.dataRequest = dataRequest;
	}
	
	public ArrayList<Double> getSignalStrength() {
		return signalStrength;
	}
	
	public void setSignalStrength(ArrayList<Double> signalStrength) {
		this.signalStrength = signalStrength;
	}
	
	public BaseStation getBts() {
		return bts;
	}
	
	public void setBts(BaseStation bts) {
		this.bts = bts;
	}
	
	public UserEquipment(int id, Location loc, double dataRequest, ArrayList<Double> signalStrength) {
		super();
		this.id = id;
		this.loc = loc;
		this.dataRequest = dataRequest;
		this.signalStrength = signalStrength;
	}
}
