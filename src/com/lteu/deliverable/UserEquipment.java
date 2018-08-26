package com.lteu.deliverable;

import java.util.ArrayList;

import com.lteu.services.BaseStationDistance;

public class UserEquipment {
	
	private int id;
	private Location loc;
	private double dataRequest;
	private ArrayList<BaseStationDistance> nearestBaseStation;
	private ArrayList<Double> signalStrength = new ArrayList<Double>();
	private BaseStation associatedBTS;
	
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
		return associatedBTS;
	}
	
	public void setBts(BaseStation bts) {
		this.associatedBTS = bts;
	}
	
	public ArrayList<BaseStationDistance> getNearestBaseStation() {
		return nearestBaseStation;
	}

	public void setNearestBaseStation(ArrayList<BaseStationDistance> nearestBaseStation) {
		this.nearestBaseStation = nearestBaseStation;
	}
	
	public UserEquipment(int id, Location loc, double dataRequest, ArrayList<Double> signalStrength, ArrayList<BaseStationDistance> nearestBaseStation) {
		super();
		this.id = id;
		this.loc = loc;
		this.dataRequest = dataRequest;
		this.signalStrength = signalStrength;
		this.nearestBaseStation = nearestBaseStation;
	}

	
}
