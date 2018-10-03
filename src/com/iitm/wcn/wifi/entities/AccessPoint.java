package com.iitm.wcn.wifi.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.iitm.wcn.wifi.params.Params;

public class AccessPoint {
	private int id;
	Random randTime = new Random(Params.TIME_SEED + 3);
	Random randDur = new Random(Params.TIME_SEED + 4);

	private List<UserEquipment> associatedUEList;
	private List<AccessPoint> neighbours;
	private Channel ch;
	private Location loc;
	private int txPower;		// in mW
	private long txStartTime;	// in 10 microsecond slots
	private int backoffTime;
	private boolean backoffStatus;
	private int txDuration;
	private boolean difsWaited;
	
	public int getId() {
		return id;
	}

	public int getBackoffTime() {
		return backoffTime;
	}

	public void setBackoffTime() {
		this.backoffTime *= 2;
	}
	
	public void updateBackoffTime() {
		this.backoffTime *= 2;
	}
	
	public void updateBackoffTime(int time) {
		this.backoffTime = Math.max(this.backoffTime - time, 0);
		
	}

	public List<AccessPoint> getNeighbours(){
		return this.neighbours;
	}
	
	public long getTxStartTime() {
		return txStartTime;
	}

	public void setTxStartTime(long time) {
		this.txStartTime = time;
	}

	public void associateUE(UserEquipment ue) {
		associatedUEList.add(ue);
	}

	public List<UserEquipment> getAssociatedUEList() {
		return associatedUEList;
	}
	
	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	
	public AccessPoint(int id, Location loc, long txStartTime, int txDuration, long seed) {
		associatedUEList = new ArrayList<UserEquipment>();
		neighbours = new ArrayList<AccessPoint>();
		this.loc = loc;
		this.id = id;
		this.txPower = Params.TX_POWER;
		this.txStartTime = txStartTime;
		this.txDuration = txDuration;
		this.backoffTime = Params.T_SLOT;
		this.difsWaited= false;
		this.ch = new Channel();
		this.backoffStatus = false;
		this.randTime = new Random(seed + 1);
		this.randDur = new Random(seed + 2);
	}

	/* add an accesspoint to the neighbours list */
	public void addToNeighbours(AccessPoint ap) {
		if( !this.neighbours.contains(ap) ) {
			this.neighbours.add(ap);
		}
	}
	
	/* set the channel of this accesspoint and it's neighbours as busy */
	public boolean setChannelAsBusy() {
		this.ch.setAsBusy();
		for(AccessPoint ap: neighbours) {
			ap.ch.setAsBusy();
		}
		return true;
	}

	/* set the channel of this accesspoint and it's neighbours as free */
	public boolean setChannelAsFree() {
		this.ch.setAsFree();
		for(AccessPoint ap: neighbours) {
			ap.ch.setAsFree();
		}
		return true;
	}

	/* list the id and location of neighbors 
	 * may be useful!
	 */
	public void printNeighbours() {
		System.out.println("Accesspoint " + this.id + " neighbour list");
		for(AccessPoint ap: neighbours) {
			System.out.println(ap.id + ":" + ap.getLoc());
		}
	}
	
	public void assoicateUE(UserEquipment ue) {
		this.associatedUEList.add(ue);
	}

	public double getTxPower() {
		return this.txPower;
	}

	public Channel getChannel() {
		return ch;
	}
	
	public boolean isChannelBusy() {
		return ch.isBusy();
	}

	public int getTxDuration() {
		return txDuration;
	}

	public void setTxDuration(int txDuration) {
		this.txDuration = txDuration;
	}

	/* Methods to find distances to other equipments */
	public double distanceTo(AccessPoint ap) {
		return this.loc.distanceTo(ap.getLoc());
	}
	
	public double distanceTo(UserEquipment ue) {
		return this.loc.distanceTo(ue.getLoc());
	}

	/* reset all parameters and find a new schedule */
	public void setAsCompleted(long time) {
		this.txStartTime = time + (long)(randTime.nextDouble() * (Params.SIM_DURATION - time) / Params.SIFS) * Params.SIFS;
		this.txDuration = Math.min((int)(Params.SIM_DURATION - txStartTime), (int)(randTime.nextDouble() * Params.MAX_TX_DURATON / Params.SIFS) * Params.SIFS);
		this.backoffTime = Params.T_SLOT;
		this.difsWaited = false;
	}

	public boolean waitedDIFS() {
		return difsWaited;
	}

	public void waitForDIFS() {
		this.difsWaited = true;
	}

	public boolean isInBackoffMode() {
		return backoffStatus;
	}

	public void putInBackoffMode() {
		this.backoffStatus = true;
	}
	
	public void printAssoicatedUEs() {
		System.out.println("Location of AP: " + this.loc);
		for(UserEquipment ue: this.associatedUEList) {
			System.out.println( ue.getId() + ": " + ue.getLoc());
		}
	}
}
