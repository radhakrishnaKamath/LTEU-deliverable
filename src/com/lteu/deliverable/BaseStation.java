package com.lteu.deliverable;

import java.util.ArrayList;
import com.lteu.deliverable.LocationLTE;
import com.lteu.deliverable.UserEquipmentLTE;


public class BaseStation {
    private int id;
    private LocationLTE location;
    private double txPower;
    private ArrayList<UserEquipmentLTE> usersAssociated;

    public BaseStation(int id, LocationLTE location, double txPower, ArrayList<UserEquipmentLTE> usersAssociated) {
        super();
        this.id = id;
        this.location = location;
        this.txPower = txPower;
        this.usersAssociated = usersAssociated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocationLTE getLocation() {
        return location;
    }

    public LocationLTE getLocationXChanged() {
        return new LocationLTE(ParamsLTE.AREA - location.getX(), location.getY());
    }

    public LocationLTE getLocationYChanged() {
        return new LocationLTE(location.getX(), ParamsLTE.AREA - location.getY());
    }

    public void setLocation(LocationLTE location) {
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

    public double averageSINR(){
        double avgSINR = 0;
        for(int i=0; i<usersAssociated.size(); i++){
            avgSINR = avgSINR + usersAssociated.get(i).getSINR();
        }
        return avgSINR/usersAssociated.size();
    }
}
