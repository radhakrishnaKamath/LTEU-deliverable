package com.lteu.deliverable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lteu.services.BaseStationDistance;

public class UserEquipment {
	
	private int id;
	private Location loc;
	private double dataRequest;
	private List<BaseStationDistance> nearestBaseStation;
	private ArrayList<Double> signalStrength = new ArrayList<Double>();
	private BaseStation associatedBTS;
	private double SINR;
	
	public double getSINR() {
		return SINR;
	}

	public void setSINR(double sINR) {
		SINR = sINR;
	}

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
	
	public List<BaseStationDistance> getNearestBaseStation() {
		return nearestBaseStation;
	}

	public void setNearestBaseStation(ArrayList<BaseStationDistance> nearestBaseStation) {
		this.nearestBaseStation = nearestBaseStation;
	}
	
	public UserEquipment(int id, Location loc, double dataRequest, ArrayList<Double> signalStrength, List<BaseStationDistance> nearestBaseStation) {
		super();
		this.id = id;
		this.loc = loc;
		this.dataRequest = dataRequest;
		this.signalStrength = signalStrength;
		this.nearestBaseStation = nearestBaseStation;
		AddSignalStrength();
	}
	
	public void AddSignalStrength(){
		double[] pathloss = new double[7];
		for(int i=0;i<7;i++){
			double pathlossWatt = Params.TX_POWER - (130 + 60*Math.log(nearestBaseStation.get(i).getDist()));
			pathlossWatt = 10*Math.log(pathlossWatt * 1000);
			pathloss[i] = pathlossWatt;
			//System.out.println("pathlossWatt: " + pathlossWatt);
		}
		double sinri = 0, sinroi = 0, signal;
		for(int i=0; i<7; i++){
			sinri = pathloss[i];
			sinroi = 0;
			for(int j=0; j<7; j++){
				if(j!=i){
					sinroi = sinroi + pathloss[j];
				}
			}
			signal = sinri/(sinroi + Params.NOISE*Params.NOISE);
			signalStrength.add(signal);
			//System.out.println("sinri: " + sinri + " sinroi: " + sinroi + " signals: " + signal);
		}
		SINR = Collections.max(signalStrength);
		
		associatedBTS = nearestBaseStation.get(signalStrength.indexOf(SINR)).getBts();
		associatedBTS.insertUsersAssociated(this);
	}
}
