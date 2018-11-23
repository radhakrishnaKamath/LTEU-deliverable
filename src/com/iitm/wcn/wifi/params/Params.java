package com.iitm.wcn.wifi.params;

public class Params { 
	public static int AP_SEED = 3000;			// random number generation
	public static int USER_SEED = 4000;			// random number generation
	public static int TIME_SEED = 6000;

	public static int AREA = 500;				// units: side in meters, 500m x 500m area
	public static int NUM_APS = 25; 			// one AP per 100m x 100m meter area

	public static int USERS_PER_AP = 10; 		// 20 users per AP

	public static String USER_DISTRIBUTION = "Uniform";
	public static String AP_DISRIBUTION = "Uniform";
	
	public static int TX_POWER = 23; 			// milliWatt or 10*log(P/1mW)dBm
	public static int NOISE = -90; 				// dBm
	public static int CH_FREQUENCY = 2400;		// MHz
	public static int CH_BANDWIDTH = 20;		// Mega Hertz
	public static int AP_RANGE = 50;			// radius in meters
	
	public static long SIM_DURATION = 68000000; //60*60*1000*1000L;	// microseconds //1 hr 24* is removed
	public static int T_SLOT = 20;				// 20 micro second
	public static int SIFS = 10;				// 10 micro second, short inter frame spacing
	public static int DIFS = SIFS + 2 * T_SLOT;	// DCF inter frame spacing in micro seconds
	public static int CW_MIN = 15;				// contention window size
	public static int CW_MAX = 1023;			// contention window size
	public static int MAX_TX_DURATON = 300;		// no of slots
	
	public static double TARGET_DATA_REQ = 1.529164;		// data request
	
	public static int NO_OF_AP = 2;		// no of APs associated with the BTS
}
