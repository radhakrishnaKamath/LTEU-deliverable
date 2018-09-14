package com.lteu.services;

import java.util.Comparator;

import com.lteu.deliverable.BaseStation;

public class BaseStationDistance implements Comparable<BaseStationDistance>{
	BaseStation bts;
	double dist;

	public BaseStation getBts() {
		return bts;
	}

	public void setBts(BaseStation bts) {
		this.bts = bts;
	}

	public double getDist() {
		return dist;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}
	
	public BaseStationDistance(BaseStation bts, double dist) {
		super();
		this.bts = bts;
		this.dist = dist;
	}

	public static class Comparators{
		public static Comparator<BaseStationDistance> DIST = new Comparator<BaseStationDistance>() {
			@Override
			public int compare(BaseStationDistance distObj1,
					BaseStationDistance distObj2) {
				// TODO Auto-generated method stub
				if((distObj1.dist - distObj2.dist)==0) {
					return 0;
				} else if((distObj1.dist - distObj2.dist)>0) {
					return 1;
				} else {
					return -1;
				}
				//return (int) (distObj1.dist - distObj2.dist);
			}
	    };
	}

	@Override
	public int compareTo(BaseStationDistance arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}