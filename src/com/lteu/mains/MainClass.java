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
		Services services = new Services();
		bts = services.CreateBS(Params.BS_DISRIBUTION);
		
		ue = services.CreateUE(bts, Params.USER_DISRIBUTION);
		
/*		System.out.println("BTS X");
  		for(int i=0; i<bts.size(); i++){	
			System.out.println(bts.get(i).getLocation().getX());
		}
		System.out.println("BTS Y");
		for(int i=0; i<bts.size(); i++){
			System.out.println(bts.get(i).getLocation().getY());
		}
		System.out.println("user x");
		for(int i=0; i<ue.size(); i++){
			System.out.println(ue.get(i).getLoc().getX());
		}
		System.out.println("user y");
		for(int i=0; i<ue.size(); i++){
			System.out.println(ue.get(i).getLoc().getY());
		}*/
		
		System.out.println("User BTS association count");
  		for(int i=0; i<bts.size(); i++){	
			System.out.println(bts.get(i).getUsersAssociated().size());
		}
		
  		System.out.println("user SINR");
		for(int i=0; i<ue.size(); i++){
			System.out.println(ue.get(i).getSINR());
		}
		
	}

}
