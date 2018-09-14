package com.lteu.mains;

import java.util.List;

import com.lteu.deliverable.*;
import com.lteu.services.Services;

public class MainClass {

	/**
	 * @param args
	 */
	static List<BaseStation> bts;
	static List<UserEquipment> ue;
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		Services services;// = new Services();
		double avgUserAssoc[] = new double[Params.NUM_BASE_STATIONS];
		double sum1 = 0, sum2 = 0;
		for(int j=0; j<Params.TRIALS; j++){
			services = new Services();
			bts = services.CreateBS(Params.BS_DISRIBUTION);
			ue = services.CreateUE(bts, Params.USER_DISRIBUTION);
			for(int i=0; i<bts.size(); i++){
				avgUserAssoc[i] = avgUserAssoc[i] + bts.get(i).averageSINR(); // taking avg value over all bts
				//sum1 = sum1 + bts.get(j).getUsersAssociated().size();  // for taking a avg value
			}
			//sum2 = sum2 + sum1/bts.size();
		}
		//System.out.println(sum2/Params.TRIALS); // avg value
		for(int j=0; j<Params.NUM_BASE_STATIONS; j++){
			avgUserAssoc[j] = avgUserAssoc[j]/Params.TRIALS;
			System.out.println(avgUserAssoc[j]);
		}
	}
	
	void PrintAvg(String s) {
		
	}
	
	void PrintAll(String s) {
		
	}
}