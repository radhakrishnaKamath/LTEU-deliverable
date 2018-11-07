package com.lteu.mains;

import java.util.List;

import com.lteu.deliverable.*;
import com.lteu.services.ServicesLTE;

import com.iitm.wcn.wifi.entities.AccessPoint;
import com.iitm.wcn.wifi.entities.UserEquipment;
import com.iitm.wcn.wifi.params.Params;
import com.iitm.wcn.wifi.services.Services;
import com.lteu.deliverable.LteuQlearning;

public class MainClass {

	/**
	 * @param args
	 */
	static List<BaseStation> bts;
	static List<UserEquipmentLTE> ue;
	private static List<AccessPoint> apList;
	private static List<UserEquipment> ueList;
	
	public static void main(String[] args) {
		
		LteuQlearning lteu = new LteuQlearning();
		Services services = new Services();
		ServicesLTE servicesLTE  = new ServicesLTE();
		/* Initialization of WiFi simulation environment */
		apList = services.createAPs();
		ueList = services.createUsers(apList);
		/* Association of users to APs */
		services.associateUsersToAPs(ueList, apList);
//		for(AccessPoint ap: apList) {
//			System.out.println(ap.getAssociatedUEList().size());
//		}
		/* Initialization of LTE simulation environment */
		bts = servicesLTE.CreateBS(apList);
		ue = servicesLTE.CreateUE(bts, apList);
		
		double avgSINR[] = new double[ParamsLTE.NUM_BASE_STATIONS];
		//double avgUserAssoc[] = new double[ParamsLTE.NUM_BASE_STATIONS];
		for(int j=0; j<ParamsLTE.TRIALS; j++){
			for(int i=0; i<ParamsLTE.NUM_BASE_STATIONS; i++){
				avgSINR[i] = avgSINR[i] + bts.get(i).averageSINR(); // taking avg value over all bts
			}
		}
		for(int j=0; j<ParamsLTE.NUM_BASE_STATIONS; j++){
			avgSINR[j] = avgSINR[j]/ParamsLTE.TRIALS;
		}
		/* end of initialization */
		/* simulation */
		/* simulation runs in steps of SIFS, because SIFS is the smallest unit */
 		
		for(BaseStation bs:bts) {
			AccessPoint ap = bs.getAccessPoint();
			bs.initLTEU();
			int timeLTEU = bs.LTEUTimeSlot();
			double cost;
			boolean initFlag = true;
			for(long time = 0; time < Params.SIM_DURATION;) {
				for(int slotPercent=1; slotPercent<=5; slotPercent++, time +=Params.SIFS) {
					if(slotPercent <= timeLTEU) {
						int userDataRateReq[] = new int [] {0, 0, 0};
						int totalData = 0;
						
						if(initFlag) {
							bs.initCLAA();
							initFlag = false;
						}
						ap.setChannelAsBusy();
						int timeLTE = Params.SIFS * ParamsLTE.SUBFRAME_DUR;
						int totalRB = ParamsLTE.RB * timeLTE;
						if(ap.getTxStartTime() == 
								time) {
							ap.setTxStartTime(time + ap.getBackoffTime());
							ap.putInBackoffMode();
			                ap.updateBackoffTime();
						} else if(ap.isInBackoffMode()) {
							/* if the channel is busy then pause(increment) the backoff timer */
							if(ap.isChannelBusy()) {
								ap.setTxStartTime(ap.getTxStartTime() + Params.SIFS);
								/* reset the channel idletimer */
								ap.getChannel().resetIdleTimer();
							}
							else {
								/* if the channel is idle for DIFS then resume(stop incrementing) the backoff timer */
								ap.getChannel().updateIdleTimer(Params.SIFS);
								if( ap.getChannel().getIdleTimer() < Params.DIFS ) {
									ap.setTxStartTime( ap.getTxStartTime() + Params.SIFS );
								}
							}
						}
						
						for(UserEquipmentLTE ue: bs.getUsersAssociated()) {
							double sinr = 0.0;
							for(int s=0;s<ParamsLTE.SINR_CQI.size()-1;s++) {
								if(ParamsLTE.SINR_CQI.get(s) == ue.getSINR()) {
									sinr = ParamsLTE.SINR_CQI.get(s);
									break;
								} else if(ParamsLTE.SINR_CQI.get(0) > ue.getSINR()) {
									sinr = ParamsLTE.SINR_CQI.get(0);
									break;
								} else if (ParamsLTE.SINR_CQI.get(s) < ue.getSINR() && ParamsLTE.SINR_CQI.get(s+1) > ue.getSINR()) {
									sinr = ParamsLTE.SINR_CQI.get(s);
									break;
								} else if(ParamsLTE.SINR_CQI.get(ParamsLTE.SINR_CQI.size()-1) > ue.getSINR()) {
									sinr = ParamsLTE.SINR_CQI.get(ParamsLTE.SINR_CQI.size()-1);
									break;
								}
							}
							totalData = ParamsLTE.CQI_MCS.get(ParamsLTE.SINR_CQI.indexOf(sinr)) * ParamsLTE.RE; 
							//change needed
							if(ue.getDataRequest() == ParamsLTE.DATARATE[2]) {
								userDataRateReq[2]++;
							} else if(ue.getDataRequest() == ParamsLTE.DATARATE[1]) {
								userDataRateReq[1]++;
							} else {
								userDataRateReq[0]++;
							}
						}
						
						int totalReq = ParamsLTE.DATARATE[0] * userDataRateReq[0] + 
									ParamsLTE.DATARATE[1] * userDataRateReq[1] + 
									ParamsLTE.DATARATE[2] * userDataRateReq[2];
						
						int totalDataAvail = totalData * totalRB / 1024;
						
						for(UserEquipmentLTE ue: bs.getUsersAssociated()) {
							
							if(ue.getDataRequest() == ParamsLTE.DATARATE[2]) {
								double data = ParamsLTE.DATARATE[2]*totalDataAvail / totalReq;
								ue.setDataRec(data);
	  							bs.updateCLAA(totalData / (5 * Params.SIFS)); //(8 * 1024 * 1024));
							} else if(ue.getDataRequest() == ParamsLTE.DATARATE[1]) {
								double data = ParamsLTE.DATARATE[1]*totalDataAvail / totalReq;
								ue.setDataRec(data);
	  							bs.updateCLAA(totalData / (5 * Params.SIFS)); //(8 * 1024 * 1024));
							} else {
								double data = ParamsLTE.DATARATE[0]*totalDataAvail / totalReq;
								ue.setDataRec(data);
								bs.updateCLAA(totalData / (5 * Params.SIFS)); //(8 * 1024 * 1024));
								
							}
							ue.setSatisfaction();							
						}
						
						if(slotPercent == timeLTEU) {
							
							cost = bs.calculateCost();
							//System.out.println(bs.getCLAA(timeLTE * Params.SIFS));
							bs.setNextState(bs.nextState());
							bs.minAction();
							bs.update(bs.getCurrState(), timeLTEU, cost);
							bs.updateCurrState();
							//System.out.println("id: " + bs.getId() + " cLAA: " + bs.getCLAA());
						}
					} else {
						initFlag = true;
						ap.setChannelAsFree();
						/* if this AP is scheduled to start at this time */
						if(ap.getTxStartTime() == time) {
							// System.out.println(ap.getId() + " is scheduled at " + time);
							/* check whether the channel is busy */
							if(ap.waitedDIFS() == false) {
								/* if channel not busy wait for DIFS time */
								ap.setTxStartTime( time + Params.DIFS);
								ap.waitForDIFS();						
							} else {
								/* lock the channel */
								//System.out.println("AP " + ap.getId() + " started at time " + time);
								ap.setChannelAsBusy();
								/* send data */
				            }
				        }
						/* otherwise check whether the station is in backoff mode */
						else if(ap.isInBackoffMode()) {
							/* if the channel is busy then pause(increment) the backoff timer */
							if(ap.isChannelBusy()) {
								ap.setTxStartTime(ap.getTxStartTime() + Params.SIFS);
								/* reset the channel idletimer */
								ap.getChannel().resetIdleTimer();
							} else {
								/* if the channel is idle for DIFS then resume(stop incrementing) the backoff timer */
						   		ap.getChannel().updateIdleTimer(Params.SIFS);
								if( ap.getChannel().getIdleTimer() < Params.DIFS ) {
									ap.setTxStartTime( ap.getTxStartTime() + Params.SIFS );
								}
							}
						}
						
						
						/* set the channel free after the data transmission is completed */
				        if( ap.getTxStartTime() + ap.getTxDuration() + Params.SIFS == time) {
				        	//System.out.println("AP " + ap.getId() + " is completed at " + time);
				        	System.out.println("ap: " + ap.getId() + " will call ue.updateThroughput");
				        	for(UserEquipment ue :ap.getAssociatedUEList()) {
				        		ue.updateThroughput(ap.getTxDuration());
				        	}
				        	ap.setAsCompleted(time);
				        	ap.setChannelAsFree();
				        	// new schedule
				    		// services.printAPSchedule(apList);
				        }
				        if(slotPercent==5 && ap.getTxStartTime() + ap.getTxDuration() + Params.SIFS < time) {
			        		System.out.println("ap: " + ap.getId() + " will call ue.updateThroughput");
				        	//System.out.println("AP " + ap.getId() + " is completed at " + time);
				        	for(UserEquipment ue :ap.getAssociatedUEList()) {
				        		ue.updateThroughput(time - ap.getTxStartTime());
				        	}
				        	
				        	ap.setRemaining(time, ap.getTxDuration() - time + ap.getTxStartTime());
				        	ap.setChannelAsFree();
				        	// new schedule
				    		// services.printAPSchedule(apList);
				        }
					}
				}
			}
			//ap.getAvgThroughput();
			//System.out.println("Avg thruput: " + bs.averageThroughput() + " avg satis: " + bs.averageSatis() + " wifi throughput: " + ap.getAvgThroughput());
			System.out.println("id: " + ap.getId() + " wifi throughput: " + ap.getAvgThroughput());
}
//		double a = btsThroughput();
//		double b = wifiThroughput();
//		
//		System.out.println("Avg thruput: " + a + " wifi throughput: " + b + " user satisfaction: " + btsSatisfaction() + " jain fairness: " + jainFairness(a, b));
		//System.out.println(" wifi throughput: " + b);
		//------------- lte start ---------------
		// = new Services();
		
		//double sum1 = 0, sum2 = 0;
		//for(int j=0; j<ParamsLTE.TRIALS; j++){
			//for(int i=0; i<bts.size(); i++){
				//avgSINR[i] = avgSINR[i] + bts.get(i).averageSINR(); // taking avg value over all bts
				//avgUserAssoc[i] = avgUserAssoc[i] + bts.get(i).getUsersAssociated().size(); // taking avg value over all bts
				//sum1 = sum1 + bts.get(j).getUsersAssociated().size();  // for taking a avg value
			//}
//			for(int k=0; k < bts.get(0).getUsersAssociated().size(); k++) {
//				System.out.println(bts.get(0).getUsersAssociated().get(k).getSINR());
//			}
			//sum2 = sum2 + sum1/bts.size();
		//}
		//System.out.println(sum2/Params.TRIALS); // avg value
		//for(int j=0; j<ParamsLTE.NUM_BASE_STATIONS; j++){
		//	avgSINR[j] = avgSINR[j]/ParamsLTE.TRIALS;
			//avgUserAssoc[j] = avgUserAssoc[j]/ParamsLTE.TRIALS;
			//System.out.println(avgUserAssoc[j]);
		//} //avgUserAssoc1[j] + " " +
		
		//------------- lte finish ---------------
		
		//------------- wifi start ---------------
		
		
		//Params.SIM_DURATION = 2000;
		//System.out.println("simulation duration" + Params.SIM_DURATION);
		
		
		
		/* Find neighbours of APs */
		//services.findNeighbours(apList);
		
		//------------- wifi finish ---------------
		
	}
	
	static double jainFairness(double a, double b) {
		return Math.pow(a+b,2)/(2*(a*a + b*b));
	}
	
	static double wifiThroughput() {
		double thrput = 0;
		int count=0;
		for(AccessPoint ap:apList) {
			if(ap.getAvgThroughput()<5) {
				thrput = thrput + ap.getAvgThroughput();
				count++;
			}
			
		}
		return thrput/count;
	}
	
	static double btsThroughput() {
		double thrput = 0;
		int count=0;
		for(BaseStation bs:bts) {
			if(bs.averageThroughput()!=0) {
				thrput = thrput + bs.averageThroughput();
				count++;
			}
			
		}
		return thrput/count;
	}
	
	static double btsSatisfaction() {
		double satis = 0;
		int count=0;
		for(BaseStation bs:bts) {
			if(bs.averageSatis()!=0) {
				satis = satis + bs.averageSatis();
				count++;
			}
		}
		return satis/count;
	}
}