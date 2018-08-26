package com.lteu.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.lteu.deliverable.*;

import com.lteu.deliverable.Params;

public class Services{
	
	Random randX = new Random(Params.BASE_STATIONS_SEED);
	Random randY = new Random(Params.BASE_STATIONS_SEED+1);
	
	Random randUserX = new Random(Params.USER_SEED);
	Random randUserY = new Random(Params.USER_SEED+1);
	
	List<BaseStation> bsList = new ArrayList<BaseStation>();
	List<UserEquipment> ueList = new ArrayList<UserEquipment>();
	
	public List<BaseStation> CreateBS(String distribution){
		if(distribution.equals("Uniform")){
			for(int i=0; i<Params.NUM_BASE_STATIONS; i++){
				int x = randX.nextInt(Params.AREA);
				int y = randY.nextInt(Params.AREA);
				Location bsLoc = new Location(x,y);
				ArrayList<UserEquipment> ue = new ArrayList<UserEquipment>();
				BaseStation bs = new BaseStation(i, bsLoc, Params.TX_POWER, ue);
				bsList.add(bs);
			}
		} else if(distribution.equals("Poisson")){
			/*for(int i=0; i<Params.NUM_BASE_STATIONS; i++){
				int x = randX.nextInt(Params.BASE_STATIONS_SEED);
				int y = randY.nextInt(Params.BASE_STATIONS_SEED+1);
				Location bsLoc = new Location(x,y);
				ArrayList<UserEquipment> ue = new ArrayList<UserEquipment>();
				BaseStation bs = new BaseStation(i, bsLoc, Params.TX_POWER, ue);
				bsList.add(bs);
			}*/
		}
		return bsList;
	}
	
	public List<UserEquipment> CreateUE(String distribution){
		if(distribution.equals("Uniform")){
			for(int i=0; i<Params.NUM_USERS; i++){
				int x = randUserX.nextInt(Params.AREA);
				int y = randUserY.nextInt(Params.AREA);
				Location ueLoc = new Location(x,y);
				ArrayList<BaseStationDistance> distArr = new ArrayList<BaseStationDistance>();
				for(int j=0; j<Params.NUM_BASE_STATIONS; j++){
					distArr.add(new BaseStationDistance(j, ueLoc.Distance(bsList.get(j).getLocation())));
				}
				Collections.sort(distArr, BaseStationDistance.Comparators.DIST);
				ArrayList<BaseStationDistance> nearBaseStations = (ArrayList<BaseStationDistance>) distArr.subList(0, 7);
				ArrayList<Double> signal = new ArrayList<Double>();
				UserEquipment userequip = new UserEquipment(i, ueLoc, 0.0, signal, nearBaseStations);
				ueList.add(userequip);
			}
		}
		return ueList;
	}
}