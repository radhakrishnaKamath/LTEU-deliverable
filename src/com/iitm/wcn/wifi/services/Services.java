package com.iitm.wcn.wifi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.iitm.wcn.wifi.entities.AccessPoint;
import com.iitm.wcn.wifi.entities.Location;
import com.iitm.wcn.wifi.entities.UserEquipment;
import com.iitm.wcn.wifi.params.Params;
import com.lteu.deliverable.BaseStation;
import com.lteu.deliverable.ParamsLTE;
import com.lteu.deliverable.UserEquipmentLTE;

public class Services {

	public static List<AccessPoint> createAPs() {
		List<AccessPoint> apList = new ArrayList<AccessPoint>();
		AccessPoint ap;
		Location loc;
		Random randX = new Random(Params.AP_SEED);
		Random randY = new Random(Params.AP_SEED + 1);
		Random randTime = new Random(Params.TIME_SEED + 1);
		Random randDur = new Random(Params.TIME_SEED + 2);
		Random seed = new Random(Params.TIME_SEED + 3);
		long txStartTime;
		int txDuration;
		
		for( int i = 0; i < Params.NUM_APS; ++i) {
			/* generate a random starting time for this AP */
			txStartTime = (long)(randTime.nextDouble() * (Params.SIM_DURATION / Params.SIFS)) * Params.SIFS;
			/* generate a random duration for data transfer */
			txDuration = Math.min((int)(Params.SIM_DURATION - txStartTime), (int)(randTime.nextDouble() * Params.MAX_TX_DURATON / Params.SIFS) * Params.SIFS);
			loc = new Location( randX.nextInt(Params.AREA),	randY.nextInt(Params.AREA));
			
			ap = new AccessPoint(i, loc,txStartTime, txDuration, seed.nextLong());
			apList.add(ap);
		}
		return apList;
	}
	
	public static List<AccessPoint> createAPsNew() {
		List<AccessPoint> apList = new ArrayList<AccessPoint>();
		AccessPoint ap;
		Location apLoc;
		int id;
		Random randX = new Random(Params.AP_SEED);
		Random randY = new Random(Params.AP_SEED + 1);
		Random randTime = new Random(Params.TIME_SEED + 1);
		Random randDur = new Random(Params.TIME_SEED + 2);
		Random seed = new Random(Params.TIME_SEED + 3);
		long txStartTime;
		int txDuration;
		for(int i=0; i<5; i++){
    		for(int j=0; j<5*Params.NO_OF_AP; j+=Params.NO_OF_AP) {
    			for(int k=0; k<Params.NO_OF_AP; k++) {
    				/* generate a random starting time for this AP */
        			txStartTime = (long)(randTime.nextDouble() * (Params.SIM_DURATION / Params.SIFS)) * Params.SIFS;
        			/* generate a random duration for data transfer */
        			txDuration = Math.min((int)(Params.SIM_DURATION - txStartTime), (int)(randTime.nextDouble() * Params.MAX_TX_DURATON / Params.SIFS) * Params.SIFS);
        			txDuration = 10* txDuration;
        			id = (5*i*Params.NO_OF_AP)+j+k;
        			int x = i*100 + randX.nextInt(100);
                    int y = j*100/Params.NO_OF_AP + randY.nextInt(100);
                    apLoc = new Location(x,y);
                    
                    ArrayList<UserEquipmentLTE> ue = new ArrayList<UserEquipmentLTE>();
                    ap = new AccessPoint((5*i*Params.NO_OF_AP)+j+k, apLoc, txStartTime, txDuration, seed.nextLong());
                    apList.add(ap);
    			}
    		}
        }
        return apList;
	}

	public static List<UserEquipment> createUsers(List<AccessPoint> apList) {
		List<UserEquipment> ueList = new ArrayList<UserEquipment>();
		UserEquipment ue;
		Location loc;
		Location apLoc;
		Random randR = new Random(Params.USER_SEED);
		Random randTheta = new Random(Params.USER_SEED + 1);
		double theta;
		int r;
			
		for( AccessPoint ap: apList ) {
			apLoc = ap.getLoc();
			for( int j = 0; j < Params.USERS_PER_AP; ++j) {
				theta = (randTheta.nextInt(360)) * Math.PI / 180;
				r = randR.nextInt(Params.AP_RANGE);
				/* x = r * cos(theta)
				 * y = r * sin(theta)
				 */
				loc = new Location( apLoc.getX() + (int)Math.floor(r * Math.cos(theta)), apLoc.getY() + (int)Math.floor(r * Math.sin(theta)));
				
				if( loc.getX() < 0 || loc.getX() > Params.AREA || loc.getY() < 0  || loc.getY() > Params.AREA) {
					j--;
					continue;
				}
				if(loc.getX() == apLoc.getX() && loc.getY() == apLoc.getY()) {
					j--;
					continue;
				}
				ue = new UserEquipment(ap.getId() * 100 + j, loc);
				ueList.add(ue);
			}
		}
		return ueList;
	}
	
	public static void printAPLocations(List<AccessPoint> apList) {
		System.out.println("Locatons of access points");
		for(AccessPoint ap : apList) {
			System.out.println(ap.getLoc());
		}
	}
	
	public void printUELocations(List<UserEquipment> ueList) {
		System.out.println("Locatons of users");
		for(UserEquipment ue : ueList) {
			System.out.println(ue.getLoc());
		}
	}

	/*
	 * find neighbours for all APs in the given list 
	*/
	public void findNeighbours(List<AccessPoint> apList) {
		for( AccessPoint apSrc: apList ) {
			for( AccessPoint apDst: apList ) {
				/* if within transmission range, add ap to neighbours list */
				if(!apSrc.equals(apDst)) {
					if(apSrc.distanceTo(apDst) < Params.AP_RANGE) {
						apSrc.addToNeighbours(apDst);
						apDst.addToNeighbours(apSrc);
					}
				}
			}	
		}
	}

	
	public void associateUsersToAPs(List<UserEquipment> ueList, List<AccessPoint> apList) {
		AccessPoint ap;
		for( UserEquipment ue: ueList ) {
			ap = ue.searchAP(apList);
			ue.associateAP(ap);
			ap.associateUE(ue);
		}
		
	}

	/* might be useful */
	public void printAPSchedule(List<AccessPoint> apList) {
		for(AccessPoint ap: apList ) {
			System.out.println(ap.getId() + ":" + ap.getTxStartTime() + " for " + ap.getTxDuration());
		}
	}
	
	public void printUEAssociations(List<AccessPoint> apList) {
		for(AccessPoint ap: apList ) {
			ap.printAssoicatedUEs();
		}
	}
}
