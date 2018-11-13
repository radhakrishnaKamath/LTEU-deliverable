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
    
	double targetSatis, targetFairness;
	
    public void initLTEU() {
    	for(int i=0;i<6;i++) {
			for(int j=0; j<4; j++) {
				qTable[i][j] = 0;
			}
		}
		state = 0;
		epsilon = 0.03;
		alpha = 0.5;
		gamma = 0.9;
		minAction = 0;
		cLAA = 0;
		cTar = 30 * 1024 * 1024;
		targetSatis = 70;
		targetFairness = 0.5;
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
    	
		return (Math.abs(cTar-cLAA) + accessPoint.cost() + Math.abs(targetSatis - averageSatis()) + accessPoint.satisfaction() 
								   + Math.abs(targetFairness - jainFairness(averageThroughput(), accessPoint.getAvgThroughput())))/5;
	}
    
    
    double jainFairness(double a, double b) {
		return Math.pow(a+b,2)/(2*(a*a + b*b));
	}
    

	public void initCLAA() {
    	this.cLAA = 0;
    }
    
    public int getNextState() {
    	return nextState;
    }
	
    public int nextState(){
		if(cLAA < 1024) {
			return 0;
		} else if(1024 <= cLAA && cLAA < 10240) {
			return 1;
		} else if(10240 <= cLAA && cLAA < 20480) {
			return 2;
		} else if(20480 <= cLAA && cLAA < 30720) {
			return 3;
		} else if(30720 <= cLAA && cLAA < 40960) {
			return 4;
		} else {
			return 5;
		}
	}
    
    public void updateCLAA(double i) {
    	cLAA = cLAA + i;
    }
    
    public void updateCTAR(int i) {
    	cTar = cTar + i;
    }
    
    public double getCLAA() {
    	//System.out.println(cLAA);
    	return cLAA;
    }
	
    public void setNextState(int state) {
    	nextState = state;
    }
    
    public int getCurrState() {
    	return state;
    }
    
    public void minAction() {
		int act = 0;
		for(int j=1; j<4; j++) {
			if(qTable[nextState][act] > qTable[nextState][j]) {
				act = j;
			}
		}
		minAction = act;
	}
    
    public void updateCurrState() {
    	state = nextState;
    }
	
    public void update(int i, int j, double cost) {
		qTable[i][j] = (1-alpha) * qTable[i][j] + alpha * (cost + gamma * qTable[nextState][minAction]);
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
    
    public double averageSatis(){
        double avgsatis = 0;
        for(int i=0; i<usersAssociated.size(); i++){
        	avgsatis = avgsatis + usersAssociated.get(i).getSatisfaction();
        }
        return avgsatis/usersAssociated.size();
    }
}
