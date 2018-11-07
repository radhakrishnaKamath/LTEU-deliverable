package com.iitm.wcn.wifi.entities;

import java.util.List;

import com.iitm.wcn.wifi.params.Params;

public class UserEquipment {
	private int id;
	private Location loc;
	private AccessPoint associatedAP;
	private Channel ch;
	private double maxSNR = 0;
	private double throughputMultiplier = Params.CH_BANDWIDTH * Params.SIFS / Math.pow(10,6);
	private double throughput=0;
	
	/* Constructor */
	public UserEquipment(int id, Location loc) {
		this.loc = loc;
		this.setId(id);
	}
	
	public AccessPoint searchAP(List<AccessPoint> apList) {
		AccessPoint selectedAP = null;
		double snr;
		double maxPRcvd = -120; //dB
		double pathLoss;
		double distance;
		double powerRcvd;
		double powerTx;
		for(AccessPoint ap: apList) {
			distance = this.distanceTo(ap);
			pathLoss = 2 + 2 * Math.log10(distance / 1000) + 2 * Math.log10(Params.CH_FREQUENCY);	// in dB
			powerTx = 10 * Math.log10(ap.getTxPower());	// in dBm
			powerRcvd = powerTx - pathLoss;	// in dBm
			snr = powerRcvd - Params.NOISE; // in dBm 
			if( powerRcvd > maxPRcvd ) {
				selectedAP = ap;
				maxPRcvd = powerRcvd;
				maxSNR = snr;
			}
		}
		throughputMultiplier = throughputMultiplier * Math.log(1+maxSNR) ;
		return selectedAP;
	}
	
	public double getThroughput() {
		return throughput;
	}
	
	public void updateThroughput(long time) {
		throughput+=throughputMultiplier*time;
	}
	
	
	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public void associateAP(AccessPoint ap) {
		this.associatedAP = ap;
		//this.ch = ap.getChannel();
	}

	public void requestData() {
		
	}
	
	public void senseChannel() {
		
	}
	
	public void requestChannel() {
		
	}
	
	public double distanceTo(AccessPoint ap) {
		return this.loc.distanceTo(ap.getLoc());
	}
	
	public double distanceTo(UserEquipment ue) {
		return this.loc.distanceTo(ue.getLoc());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}