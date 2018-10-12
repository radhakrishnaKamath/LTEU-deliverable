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

import com.iitm.wcn.wifi.entities.AccessPoint;
import com.iitm.wcn.wifi.entities.Location;
import com.iitm.wcn.wifi.entities.UserEquipment;
import com.iitm.wcn.wifi.params.Params;
import com.lteu.deliverable.*;
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
                Location bsLoc = new Location(x,y);
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
                        Location bsLoc = new Location(x,y);
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
    
    public List<BaseStation> CreateBS(List<AccessPoint> apList){
    	for(int i=0; i<5; i++){
    		for(int j=0; j<5; j++) {
    			int x = i*100 + randX.nextInt(100);
                int y = j*100 + randY.nextInt(100);
                Location bsLoc = new Location(x,y);
                //System.out.println("loc: x: " + bsLoc.getX() + " y: " + bsLoc.getY());
                ArrayList<UserEquipmentLTE> ue = new ArrayList<UserEquipmentLTE>();
                BaseStation bs = new BaseStation((5*i)+j, bsLoc, ParamsLTE.TX_POWER, ue, apList.get(5*i+j));
                bsList.add(bs);
    		}
        }
        return bsList;
    }

    public List<UserEquipmentLTE> CreateUE(List<BaseStation> bsList, String distribution){
        if(distribution.equals("Uniform")){
            for(int i=0; i<ParamsLTE.NUM_USERS; i++){
                int x = randUserX.nextInt(ParamsLTE.AREA);
                int y = randUserY.nextInt(ParamsLTE.AREA);
                Location ueLoc = new Location(x,y);
                List<BaseStationDistance> distArr = new ArrayList<BaseStationDistance>();
                for(int j=0; j<ParamsLTE.NUM_BASE_STATIONS; j++){
                    distArr.add(new BaseStationDistance(bsList.get(j), ueLoc.distanceTo(bsList.get(j).getLocation())));
                    distArr.add(new BaseStationDistance(bsList.get(j), ueLoc.distanceTo(bsList.get(j).getLocationXChanged())));
                    distArr.add(new BaseStationDistance(bsList.get(j), ueLoc.distanceTo(bsList.get(j).getLocationYChanged())));
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

    public List<UserEquipmentLTE> CreateUE(List<BaseStation> bsList, List<AccessPoint> apList){
    	Random randR = new Random(Params.USER_SEED);
		Random randTheta = new Random(Params.USER_SEED + 1);
		double theta;
		int r;
		
    	for(int i=0; i<ParamsLTE.NUM_USERS; i++){
    		BaseStation bs = bsList.get(i%ParamsLTE.NUM_BASE_STATIONS);
    		AccessPoint accessPoint = apList.get(i%Params.NUM_APS);
    		Location bsLoc = bs.getLocation();
    		theta = (randTheta.nextInt(360)) * Math.PI / 180;
			r = randR.nextInt(50);
            Location ueLoc = new Location(bsLoc.getX() + (int)Math.floor(r * Math.cos(theta)), bsLoc.getY() + (int)Math.floor(r * Math.sin(theta)));
            if(ueLoc.getX() == bsLoc.getX()  && ueLoc.getY() == bsLoc.getY()) {
				i--;
				continue;
			}
            //System.out.println("loc: x: " + ueLoc.getX() + " y: " + ueLoc.getY());
            List<BaseStationDistance> distArr = new ArrayList<BaseStationDistance>();
            distArr.add(new BaseStationDistance(bs, ueLoc.distanceTo(bs.getLocation())));
            List<BaseStationDistance> nearBaseStations = (List<BaseStationDistance>) distArr;
            ArrayList<Double> signal = new ArrayList<Double>();
            UserEquipmentLTE userequip = new UserEquipmentLTE(i, ueLoc, 0.0, signal, nearBaseStations, bs, accessPoint);
            ueList.add(userequip);
        }
        return ueList;
    }
}
