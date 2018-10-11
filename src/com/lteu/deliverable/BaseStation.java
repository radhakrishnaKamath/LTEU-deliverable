package com.lteu.deliverable;

import java.util.ArrayList;
import java.util.Random;

import com.iitm.wcn.wifi.entities.AccessPoint;
import com.iitm.wcn.wifi.entities.Location;
import com.lteu.deliverable.LocationLTE;
import com.lteu.deliverable.UserEquipmentLTE;


public class BaseStation {
    private int id;
    private Location location;
    private double txPower;
    private ArrayList<UserEquipmentLTE> usersAssociated;
    private AccessPoint accessPoint;
    
    /* LTEU */
    
    double qTable [][] = new double [6][4];
	double cTar, r, epsilon, alpha, gamma, ci, cLAA, cost;
	int state, action, minAction, nextState;
	Random rand = new Random();
    
    public void initLTEU() {
    	for(int i=0;i<6;i++) {
			for(int j=0; j<4; j++) {
				qTable[i][j] = 0;
			}
		}
		state = 0;
		epsilon = rand.nextDouble();
		alpha = rand.nextDouble();
		minAction = 0;
		cLAA = 0;
		cTar = 0;
    }
    
    public int LTEUTimeSlot () {
		r = rand.nextDouble();
		if(r<epsilon) {
			int action = rand.nextInt(4);
		} else {
			action = minAction;
		}
		return action + 1;
	}
	
    public double calculateCost(){
		return Math.abs(cTar-cLAA);
	}
	
    public int nextState(){
		if(cLAA < 1) {
			return 0;
		} else if(1 <= cLAA && cLAA < 10) {
			return 1;
		} else if(10 <= cLAA && cLAA < 20) {
			return 2;
		} else if(20 <= cLAA && cLAA < 30) {
			return 3;
		} else if(30 <= cLAA && cLAA < 40) {
			return 4;
		} else {
			return 5;
		}
	}
    
    public void updateCLAA(int i) {
    	cLAA = cLAA + i;
    }
    
    public void updateCTAR(int i) {
    	cTar = cTar + i;
    }
    
    public double getCLAA(double time) {
    	return cLAA/time;
    }
	
    public void setNextState(int state) {
    	nextState = state;
    }
    
    public int minAction(double qTable[][], int i) {
		int act = 0;
		for(int j=1; j<4; j++) {
			if(qTable[i][act] > qTable[i][j]) {
				act = j;
			}
		}
		return act;
	}
	
    public void update(double qTable[][], int i, int j, double cost, double currCost, double nextCost, double alpha, double gamma) {
		qTable[i][j] = (1-alpha) * currCost + alpha * (cost + gamma * nextCost);
	}
	
    /* LTEU  END*/

    public BaseStation(int id, Location bsLoc, double txPower, ArrayList<UserEquipmentLTE> usersAssociated) {
        super();
        this.id = id;
        this.location = bsLoc;
        this.txPower = txPower;
        this.usersAssociated = usersAssociated;
    }
    
    public BaseStation(int id, Location bsLoc, double txPower, ArrayList<UserEquipmentLTE> usersAssociated, AccessPoint accessPoint) {
        super();
        this.id = id;
        this.location = bsLoc;
        this.txPower = txPower;
        this.usersAssociated = usersAssociated;
        this.setAccessPoint(accessPoint);
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

    public Location getLocationXChanged() {
        return new Location(ParamsLTE.AREA - location.getX(), location.getY());
    }

    public Location getLocationYChanged() {
        return new Location(location.getX(), ParamsLTE.AREA - location.getY());
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

    public ArrayList<UserEquipmentLTE> getUsersAssociated() {
        return usersAssociated;
    }

    public void setUsersAssociated(ArrayList<UserEquipmentLTE> usersAssociated) {
        this.usersAssociated = usersAssociated;
    }

    public void insertUsersAssociated(UserEquipmentLTE usersAssociated) {
        this.usersAssociated.add(usersAssociated);
    }
    
    public AccessPoint getAccessPoint() {
		return accessPoint;
	}

	public void setAccessPoint(AccessPoint accessPoint) {
		this.accessPoint = accessPoint;
	}

    public double averageSINR(){
        double avgSINR = 0;
        for(int i=0; i<usersAssociated.size(); i++){
            avgSINR = avgSINR + usersAssociated.get(i).getSINR();
        }
        return avgSINR/usersAssociated.size();
    }
    
    public double averageThroughput(){
        double avgthrput = 0;
        for(int i=0; i<usersAssociated.size(); i++){
        	avgthrput = avgthrput + usersAssociated.get(i).getDataRec();
        }
        return avgthrput/usersAssociated.size();
    }
}
