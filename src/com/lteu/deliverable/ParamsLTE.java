package com.lteu.deliverable;

import java.util.ArrayList;
import java.util.List;

public class ParamsLTE { 
	public static int AREA = 2500;// units: meters
	
	public static int NUM_BASE_STATIONS = 25; // 1 BS per 1 AP
	
	public static int NUM_USERS = NUM_BASE_STATIONS*25; // 60 Users per bts
	
	public static int BASE_STATIONS_SEED = 200;
	
	public static int USER_SEED = 1500;
	
	public static String USER_DISRIBUTION = "Uniform";
	
	public static String BS_DISRIBUTION = "Uniform";
	
	public static double TX_POWER = 23; // units: dBm 
	
	public static int NOISE = -75; // units: db
	
	public static int TRIALS = 1;// no of trials
	
	public static String URL = "https://us1.unwiredlabs.com/v2/process.php";// url of open cell id
	
	public static int NUM_NEAR_BS = 7;// no of nearest BS

	@SuppressWarnings("serial")
	public static List<Double> SINR_CQI = new ArrayList<Double> () {{
		add(0.0);	add(-6.15);	add(-4.37);	add(-2.37);
		add(-0.42);	add(1.53);	add(3.43);	add(5.46);
		add(7.25);	add(9.28);	add(11.11);	add(13.0);
		add(14.9);	add(16.64);	add(18.41);	add(20.54); 	
	}}; //source: https://groups.google.com/d/msg/lte-sim/BWunBUaDCC0/g7NZALmic48J
	
	@SuppressWarnings("serial")
	public static List<Integer> CQI_MCS = new ArrayList<Integer> () {{
		add(0);	add(2);	add(2);	add(2);
		add(2);	add(2);	add(2);	add(4);
		add(4);	add(4);	add(6);	add(6);
		add(6);	add(6);	add(6);	add(6); 	
	}}; //source: < 36.213 Table 7.2.3-1 > http://www.sharetechnote.com/html/Handbook_LTE_CQI.html
	
	public static int[] DATARATE = new int[] {128, 256, 512};
	
	public static int[] USR_DATA_DISTR = new int[] {4, 6, 7};
	
	public static int[] USR_RB_DISTR = new int[] {4, 2, 1};
	
	public static int SUBCARRIERS = 12;
	
	public static int SYMBOLS = 7; //depends on cyclic prefix
	
	public static int RE = SUBCARRIERS * SYMBOLS; //RE in 1 RB
	
	public static int RB = 200; //in one subrame
	
	public static int SUBFRAME_DUR = 1; //in millisec
	
	public static int DUTY_CYCLE = 2000; //in millisec
	
	public static int DUTY_CYCLE_SPLIT = 20000; //in percentage 10% smallest 
	
	public static double TARGET_DATA_REQ = 12*1024;
}
