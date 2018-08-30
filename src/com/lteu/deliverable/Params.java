package com.lteu.deliverable;

public class Params { 
	public static int AREA = 10000;// units: meters
	public static int NUM_BASE_STATIONS = 100; // one BS per 1 sq km
	public static int NUM_USERS = 100; // one User per 1 sq km
	public static int BASE_STATIONS_SEED = 200;
	public static int USER_SEED = 1500;
	public static String USER_DISRIBUTION = "Uniform";
	public static String BS_DISRIBUTION = "Uniform";
	public static double TX_POWER = Math.pow(10,3.215); // units: dbm -- 43dbm -- 43/20 dbm -- 10^3.215 Watt
	public static int NOISE = 84; // units: dbm
}