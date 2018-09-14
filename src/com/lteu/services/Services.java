package com.lteu.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


import org.apache.commons.math3.distribution.PoissonDistribution;
import org.json.JSONException;
import org.json.JSONObject;

import com.lteu.deliverable.*;

import com.lteu.deliverable.Params;
import com.lteu.httpconnection.RESTAPIConnect;

public class Services{

    Random randX = new Random(Params.BASE_STATIONS_SEED);
    Random randY = new Random(Params.BASE_STATIONS_SEED+1);

    Random randUserX = new Random(Params.USER_SEED);
    Random randUserY = new Random(Params.USER_SEED+1);

    List<BaseStation> bsList = new ArrayList<BaseStation>();
    List<UserEquipment> ueList = new ArrayList<UserEquipment>();
    List<String> ocidResponseList = new ArrayList<String>();
    
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
            PoissonDistribution poisson = new PoissonDistribution (144);
            Params.NUM_BASE_STATIONS = poisson.sample();
            bsList = CreateBS("Uniform");
        } else {
        	RESTAPIConnect openCellid = new RESTAPIConnect(Params.URL);
        	try {
        		int i=2, j=0;
        		String res;
        		while(ocidResponseList.size() != 144) {
        			if(j%60==0) {
        				i++;
        			}
        			j++;
        			res = openCellid.GetOpenCellID(i+"",j+"");
        			JSONObject jo = new JSONObject(res);
        			if(jo.get("status").equals("ok")){
        				double x = jo.getDouble("lat");
                        double y = jo.getDouble("lon");
                        Location bsLoc = new Location(x,y);
                        ArrayList<UserEquipment> ue = new ArrayList<UserEquipment>();
                        BaseStation bs = new BaseStation(i, bsLoc, Params.TX_POWER, ue);
                        bsList.add(bs);
        			}
        		}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return bsList;
    }

    public List<UserEquipment> CreateUE(List<BaseStation> bsList, String distribution){
        if(distribution.equals("Uniform")){
            for(int i=0; i<Params.NUM_USERS; i++){
                int x = randUserX.nextInt(Params.AREA);
                int y = randUserY.nextInt(Params.AREA);
                Location ueLoc = new Location(x,y);
                List<BaseStationDistance> distArr = new ArrayList<BaseStationDistance>();
                for(int j=0; j<Params.NUM_BASE_STATIONS; j++){
                    distArr.add(new BaseStationDistance(bsList.get(j), ueLoc.Distance(bsList.get(j).getLocation())));
                    distArr.add(new BaseStationDistance(bsList.get(j), ueLoc.Distance(bsList.get(j).getLocationXChanged())));
                    distArr.add(new BaseStationDistance(bsList.get(j), ueLoc.Distance(bsList.get(j).getLocationYChanged())));
                }
                Collections.sort(distArr, BaseStationDistance.Comparators.DIST);
                List<BaseStationDistance> nearBaseStations = (List<BaseStationDistance>) distArr.subList(0, 7);
                ArrayList<Double> signal = new ArrayList<Double>();

                UserEquipment userequip = new UserEquipment(i, ueLoc, 0.0, signal, nearBaseStations);
                ueList.add(userequip);
            }
        }
        return ueList;
    }
}
