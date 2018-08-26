package com.lteu.services;

import java.util.Comparator;

public class BaseStationDistance implements Comparable<BaseStationDistance>{
	int index;
	double dist;
	public BaseStationDistance(int index, double dist) {
		super();
		this.index = index;
		this.dist = dist;
	}
	
	public static class Comparators{
		public static Comparator<BaseStationDistance> DIST = new Comparator<BaseStationDistance>() {
			@Override
			public int compare(BaseStationDistance distObj1,
					BaseStationDistance distObj2) {
				// TODO Auto-generated method stub
				return (int) (distObj1.dist - distObj2.dist);
			}
	    };
	}

	@Override
	public int compareTo(BaseStationDistance arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}