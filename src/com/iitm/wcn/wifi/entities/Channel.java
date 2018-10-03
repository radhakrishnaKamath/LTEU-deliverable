package com.iitm.wcn.wifi.entities;

public class Channel {
	private int id;
	private int busyCount;
	private int idleTimer;
	
	public Channel() {
		this.id = 1;
		this.busyCount = 0;
		this.idleTimer = 0;
	}
	
	public Channel(int id) {
		this.id = id;
	}
	
	public boolean isBusy() {
		if( this.busyCount > 0 )
			return true;
		else
			return false;
	}
	
	public void setAsBusy() {
		this.busyCount++;
		this.idleTimer = 0;
	}
	
	public void setAsFree() {
		this.busyCount--;
		this.idleTimer = 0;
	}

	public void resetIdleTimer() {
		this.idleTimer = 0;
	}

	public void updateIdleTimer(int inc) {
		this.idleTimer += inc;
	}

	public int getIdleTimer() {
		return this.idleTimer;
	}
}
