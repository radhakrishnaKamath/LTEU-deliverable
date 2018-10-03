package com.lteu.deliverable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lteu.services.BaseStationDistance;

public class UserEquipmentLTE {

    private int id;
    private LocationLTE loc;
    private double dataRequest;
    private List<BaseStationDistance> nearestBaseStation;
    private ArrayList<Double> signalStrength = new ArrayList<Double>();
    private BaseStation associatedBTS;
    private double SINR;

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

    public LocationLTE getLoc() {
        return loc;
    }

    public void setLoc(LocationLTE loc) {
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

    public UserEquipmentLTE(int id, LocationLTE loc, double dataRequest, ArrayList<Double> signalStrength, List<BaseStationDistance> nearestBaseStation) {
        super();
        this.id = id;
        this.loc = loc;
        this.dataRequest = dataRequest;
        this.signalStrength = signalStrength;
        this.nearestBaseStation = nearestBaseStation;
        AddSignalStrength();
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

    public double ConvertdBmToWatt(double p){
        return Math.pow(10,(p/10))/1000;
    }
    
    public double ConvertdBToWatt(double p){
        return Math.pow(10,(p/10));
    }
    
    public double ConvertWattTodBm(double p){
        return 10*Math.log10(1000*p);
    }
}
