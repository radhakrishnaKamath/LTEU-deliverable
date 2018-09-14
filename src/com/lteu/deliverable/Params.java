package com.lteu.deliverable;

public class Params { 
	public static int AREA = 6000;// units: meters
	public static int NUM_BASE_STATIONS = 4*(AREA/1000)*(AREA/1000); // four BS per 1 sq km
	public static int NUM_USERS = NUM_BASE_STATIONS*400; // 400 Users per bts
	public static int BASE_STATIONS_SEED = 200;
	public static int USER_SEED = 1500;
	public static String USER_DISRIBUTION = "Uniform";
	public static String BS_DISRIBUTION = "Uniform";
	public static double TX_POWER = 43; //Math.pow(10,3.215) watt // units: dbm -- 43dbm -- 43/20 dbm -- 10^3.215 Watt
	public static int NOISE = 84; // units: dbm
	public static int TRIALS = 50;// no of trials
	public static String URL = "https://us1.unwiredlabs.com/v2/process.php";// url of open cell id
}