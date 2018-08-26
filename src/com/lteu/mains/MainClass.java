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
		
		ue = services.CreateUE(Params.USER_DISRIBUTION);
		
/*		for(int i=0; i<bts.size(); i++){
			System.out.println(bts.get(i).getLocation().getX());
		}
		System.out.println("asgddjasdj");
		for(int i=0; i<bts.size(); i++){
			System.out.println(bts.get(i).getLocation().getY());
		}
		System.out.println("user loc");
		for(int i=0; i<ue.size(); i++){
			System.out.println(ue.get(i).getLoc().getX());
		}
		System.out.println("random text");
		for(int i=0; i<ue.size(); i++){
			System.out.println(ue.get(i).getLoc().getY());
		}*/
	}

}
