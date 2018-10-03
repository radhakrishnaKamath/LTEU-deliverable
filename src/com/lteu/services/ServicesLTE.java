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

import com.lteu.deliverable.ParamsLTE;
import com.lteu.httpconnection.RESTAPIConnect;

public class ServicesLTE{

    Random randX = new Random(ParamsLTE.BASE_STATIONS_SEED);
    Random randY = new Random(ParamsLTE.BASE_STATIONS_SEED+1);

    Random randUserX = new Random(ParamsLTE.USER_SEED);
    Random randUserY = new Random(ParamsLTE.USER_SEED+1);

    List<BaseStation> bsList = new ArrayList<BaseStation>();
    List<UserEquipmentLTE> ueList = new ArrayList<UserEquipmentLTE>();
    List<String> ocidResponseList = new ArrayList<String>();
    
    public List<BaseStation> CreateBS(String distribution){
        if(distribution.equals("Uniform")){
            for(int i=0; i<ParamsLTE.NUM_BASE_STATIONS; i++){
                int x = randX.nextInt(ParamsLTE.AREA);
                int y = randY.nextInt(ParamsLTE.AREA);
                LocationLTE bsLoc = new LocationLTE(x,y);
                ArrayList<UserEquipmentLTE> ue = new ArrayList<UserEquipmentLTE>();
                BaseStation bs = new BaseStation(i, bsLoc, ParamsLTE.TX_POWER, ue);
                bsList.add(bs);
            }
        } else if(distribution.equals("Poisson")){
            PoissonDistribution poisson = new PoissonDistribution (144);
            ParamsLTE.NUM_BASE_STATIONS = poisson.sample();
            bsList = CreateBS("Uniform");
        } else {
        	RESTAPIConnect openCellid = new RESTAPIConnect(ParamsLTE.URL);
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
                        LocationLTE bsLoc = new LocationLTE(x,y);
                        ArrayList<UserEquipmentLTE> ue = new ArrayList<UserEquipmentLTE>();
                        BaseStation bs = new BaseStation(i, bsLoc, ParamsLTE.TX_POWER, ue);
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

    public List<UserEquipmentLTE> CreateUE(List<BaseStation> bsList, String distribution){
        if(distribution.equals("Uniform")){
            for(int i=0; i<ParamsLTE.NUM_USERS; i++){
                int x = randUserX.nextInt(ParamsLTE.AREA);
                int y = randUserY.nextInt(ParamsLTE.AREA);
                LocationLTE ueLoc = new LocationLTE(x,y);
                List<BaseStationDistance> distArr = new ArrayList<BaseStationDistance>();
                for(int j=0; j<ParamsLTE.NUM_BASE_STATIONS; j++){
                    distArr.add(new BaseStationDistance(bsList.get(j), ueLoc.Distance(bsList.get(j).getLocation())));
                    distArr.add(new BaseStationDistance(bsList.get(j), ueLoc.Distance(bsList.get(j).getLocationXChanged())));
                    distArr.add(new BaseStationDistance(bsList.get(j), ueLoc.Distance(bsList.get(j).getLocationYChanged())));
                }
                Collections.sort(distArr, BaseStationDistance.Comparators.DIST);
                List<BaseStationDistance> nearBaseStations = (List<BaseStationDistance>) distArr.subList(0, ParamsLTE.NUM_NEAR_BS);
                ArrayList<Double> signal = new ArrayList<Double>();

                UserEquipmentLTE userequip = new UserEquipmentLTE(i, ueLoc, 0.0, signal, nearBaseStations);
                ueList.add(userequip);
            }
        }
        return ueList;
    }
}
