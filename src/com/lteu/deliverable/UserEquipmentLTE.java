package com.lteu.deliverable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.iitm.wcn.wifi.entities.AccessPoint;
import com.iitm.wcn.wifi.entities.Location;
import com.iitm.wcn.wifi.entities.UserEquipment;
import com.iitm.wcn.wifi.params.Params;
import com.lteu.services.BaseStationDistance;

public class UserEquipmentLTE {

    private int id;
    private Location loc;
    private double dataRequest;
    private double dataRec;
    private List<BaseStationDistance> nearestBaseStation;
    private ArrayList<Double> signalStrength = new ArrayList<Double>();
    private BaseStation associatedBTS;
    private double SINR;
    private double satisfaction;
    
    public double getSINR() {
        return SINR;
    }

    public void setSINR(double sINR) {
        SINR = sINR;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public double getDataRequest() {
        return dataRequest;
    }

    public void setDataRequest(double dataRequest) {
        this.dataRequest = dataRequest;
    }

    public ArrayList<Double> getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(ArrayList<Double> signalStrength) {
        this.signalStrength = signalStrength;
    }

    public BaseStation getBts() {
        return associatedBTS;
    }

    public void setBts(BaseStation bts) {
        this.associatedBTS = bts;
    }

    public List<BaseStationDistance> getNearestBaseStation() {
        return nearestBaseStation;
    }

    public void setNearestBaseStation(ArrayList<BaseStationDistance> nearestBaseStation) {
        this.nearestBaseStation = nearestBaseStation;
    }
    
	public double getDataRec() {
		return dataRec;
	}

	public void setDataRec(double dataRec) {
		this.dataRec = dataRec;
	}

	public double getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction() {
		this.satisfaction = dataRec/dataRequest;
	}

    public UserEquipmentLTE(int id, Location loc, double dataRequest, ArrayList<Double> signalStrength, List<BaseStationDistance> nearestBaseStation) {
        super();
        this.id = id;
        this.loc = loc;
        this.dataRequest = dataRequest;
        this.signalStrength = signalStrength;
        this.nearestBaseStation = nearestBaseStation;
        AddSignalStrength();
    }
    
    public UserEquipmentLTE(int id, 
    		Location loc, 
    		double dataRequest, 
    		ArrayList<Double> signalStrength, 
    		List<BaseStationDistance> nearestBaseStation, 
    		BaseStation associatedBTS,
    		AccessPoint accessPoint) {
        super();
        this.id = id;
        this.loc = loc;
        this.dataRequest = dataRequest;
        this.signalStrength = signalStrength;
        this.nearestBaseStation = nearestBaseStation;
        this.associatedBTS = associatedBTS;
        AddSignalStrengthNew(accessPoint);
        GenerateDataReq();
    }

    public void AddSignalStrength(){
        double[] powerRecArr = new double[7];
        for(int i=0;i<ParamsLTE.NUM_NEAR_BS;i++){
            double powerRec = ConvertWattTodBm(ConvertdBmToWatt(ParamsLTE.TX_POWER)/100) - (17.9 + 31.6*Math.log10(nearestBaseStation.get(i).getDist()));
            //System.out.println("power rec: " + powerRec);
            powerRecArr[i] = powerRec;
        }
        double sinri = 0, sinroi = 0, signal;
        for(int i=0; i<ParamsLTE.NUM_NEAR_BS; i++){
            sinri = ConvertdBmToWatt(powerRecArr[i]);
            //System.out.println("sinri: " + sinri);
            sinroi = 0;
            for(int j=0; j<ParamsLTE.NUM_NEAR_BS; j++){
                if(j!=i){
                    sinroi = sinroi + ConvertdBmToWatt(powerRecArr[j]);
                }
            }
            //System.out.println("sinroi: " + sinroi);
            //System.out.println("noise: " + ConvertdBToWatt(Params.NOISE));
            signal = sinri/(sinroi + ConvertdBToWatt(ParamsLTE.NOISE));
            //System.out.println("signal: " + ConvertWattTodBm(signal));
            signalStrength.add(ConvertWattTodBm(signal));
            
        }
        SINR = Collections.max(signalStrength);
        associatedBTS = nearestBaseStation.get(signalStrength.indexOf(SINR)).getBts();
        associatedBTS.insertUsersAssociated(this);
    }

    double pathLoss(double dist) {
    	return 10 + 50*Math.log10(dist);
    }
    
    public void AddSignalStrengthNew(AccessPoint accessPoint){
    	List<Double> distanceAPUsers = new ArrayList<Double>();
    	int apUserCount = accessPoint.getAssociatedUEList().size();
    	
    	double powerRecBTS = ConvertWattTodBm(ConvertdBmToWatt(ParamsLTE.TX_POWER)/100) - pathLoss(nearestBaseStation.get(0).getDist());
    	//System.out.println("pwrRecBTS: " + powerRecBTS);
    	double powerRecAP = ConvertWattTodBm(ConvertdBmToWatt(Params.TX_POWER)/100) - pathLoss(this.getLoc().distanceTo(accessPoint.getLoc()));
    	if(nearestBaseStation.get(0).getBts().getId() == 2) {
    		//System.out.println("BS dist: " + nearestBaseStation.get(0).getDist() + " AP dist: " + this.getLoc().distanceTo(accessPoint.getLoc()));
    		//System.out.println("powerRecBS: " + powerRecBTS + " PowerRecAP: " + powerRecAP);
    	}
    	double[] powerRecArr = new double[apUserCount];
    	
    	int i=0;
    	for(UserEquipment ueAP: accessPoint.getAssociatedUEList()){
    		//System.out.println("actual Users near me: " + accessPoint.getAssociatedUEList().size());
    		distanceAPUsers.add(this.getLoc().distanceTo(ueAP.getLoc()));
        }
		Collections.sort(distanceAPUsers);
		//System.out.println("Users near me: " + distanceAPUsers.size());
    	for(Double dist: distanceAPUsers) {
          double powerRec = ConvertWattTodBm(ConvertdBmToWatt(Params.TX_POWER)/100) - pathLoss(dist);
          powerRecArr[i] = powerRec;
          i++;
    	}
        double sinri = ConvertdBmToWatt(powerRecBTS), sinroi = ConvertdBmToWatt(powerRecAP), signal;
        //System.out.println("sinri: " + sinri + " sinroi" + sinroi);
        for(int j=0; j<apUserCount; j++){
            sinroi = sinroi + ConvertdBmToWatt(powerRecArr[j]);
        }
        signal = sinri/(sinroi + ConvertdBToWatt(ParamsLTE.NOISE));
        SINR = ConvertWattTodBm(signal);
        //System.out.println("SINR: " + SINR);
        associatedBTS.insertUsersAssociated(this);
    }
    
    public double ConvertdBmToWatt(double p){
        return Math.pow(10,(p/10))/1000;
    }
    
    public double ConvertdBToWatt(double p){
        return Math.pow(10,(p/10));
    }
    
    public double ConvertWattTodBm(double p){
        return 10*Math.log10(1000*p);
    }
    
    public void GenerateDataReq() {
    	Random rand = new Random();
    	int perc = rand.nextInt(ParamsLTE.USR_DATA_DISTR[2]+1);
    	if(perc <= ParamsLTE.USR_DATA_DISTR[0]) {
    		dataRequest = ParamsLTE.DATARATE[0];
    	} else if(perc > ParamsLTE.USR_DATA_DISTR[0] && perc <= ParamsLTE.USR_DATA_DISTR[1]) {
    		dataRequest = ParamsLTE.DATARATE[1];
    	} else {
    		dataRequest = ParamsLTE.DATARATE[2];
    	}
    }
}
