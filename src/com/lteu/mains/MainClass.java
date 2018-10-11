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
			//System.out.println(avgSINR[j]);
		}
		/* end of initialization */
		/* simulation */
		/* simulation runs in steps of SIFS, because SIFS is the smallest unit */
//		for( long time = 0; time < Params.SIM_DURATION; time += 5*Params.SIFS ) {
//			//System.out.println("Timeslot " + time);
//			int slotCutout = lteu.LTEUTimeSlot();
//			for(int slotPercent=1; slotPercent<=5; slotPercent++) {
//				if(slotPercent <= slotCutout) {
//					int timeLTE = Params.SIFS * ParamsLTE.SUBFRAME_DUR;
//					int totalRB = ParamsLTE.RB * timeLTE;
//					//int totalRE = ParamsLTE.RE * totalRB;
//					
//					for(BaseStation bs: bts) {
//						for(UserEquipmentLTE ue: bs.getUsersAssociated()) {
//							double sinr = 0.0;
//							for(int s=0;s<ParamsLTE.SINR_CQI.size()-1;s++) {
//								if(ParamsLTE.SINR_CQI.get(s) == ue.getSINR()) {
//									sinr = ParamsLTE.SINR_CQI.get(s);
//									break;
//								} else if(ParamsLTE.SINR_CQI.get(0) > ue.getSINR()) {
//									sinr = ParamsLTE.SINR_CQI.get(0);
//									break;
//								} else if (ParamsLTE.SINR_CQI.get(s) < ue.getSINR() && ParamsLTE.SINR_CQI.get(s+1) > ue.getSINR()) {
//									sinr = ParamsLTE.SINR_CQI.get(s);
//									break;
//								} else if(ParamsLTE.SINR_CQI.get(ParamsLTE.SINR_CQI.size()-1) > ue.getSINR()) {
//									sinr = ParamsLTE.SINR_CQI.get(ParamsLTE.SINR_CQI.size()-1);
//									break;
//								}
//							}
//							int totalData = ParamsLTE.CQI_MCS.get(ParamsLTE.SINR_CQI.indexOf(sinr)) * ParamsLTE.RE;
//							if(ue.getDataRequest() == 512) {
//								ue.setDataRec(totalData);
//								totalRB = totalRB - 1;
//							} else if(ue.getDataRequest() == 256) {
//								ue.setDataRec(2 * totalData);
//								totalRB = totalRB - 2;
//							} else {
//								ue.setDataRec(4 * totalData);
//								totalRB = totalRB - 4;
//							}
//						}
//					} 
//					//System.out.println("RBS left: " + totalRB);
//				} else if(slotPercent > slotCutout) {
//					for(AccessPoint ap: apList ) {
//						/* if this AP is scheduled to start at this time */
//						if( ap.getTxStartTime() == time ) {
//							// System.out.println(ap.getId() + " is scheduled at " + time);
//							/* check whether the channel is busy */
//							if(ap.isChannelBusy()) {
//								/* if busy backoff for some time */
//								ap.setTxStartTime(time + ap.getBackoffTime());
//								ap.putInBackoffMode();
//				                ap.updateBackoffTime();
//							}
//							else if(ap.waitedDIFS() == false) {
//								/* if channel not busy wait for DIFS time */
//								ap.setTxStartTime( time + Params.DIFS);
//								ap.waitForDIFS();						
//							}
//							else {
//								/* lock the channel */
//								System.out.println("AP " + ap.getId() + " started at time " + time);
//								ap.setChannelAsBusy();
//								/* send data */
//				            }
//				        }
//						/* otherwise check whether the station is in backoff mode */
//						else if( ap.isInBackoffMode() ) {
//							/* if the channel is busy then pause(increment) the backoff timer */
//							if( ap.isChannelBusy() ) {
//								ap.setTxStartTime( ap.getTxStartTime() + Params.SIFS );
//								/* reset the channel idletimer */
//								ap.getChannel().resetIdleTimer();
//							}
//							else {
//								/* if the channel is idle for DIFS then resume(stop incrementing) the backoff timer */
//								ap.getChannel().updateIdleTimer(Params.SIFS);
//								if( ap.getChannel().getIdleTimer() < Params.DIFS ) {
//									ap.setTxStartTime( ap.getTxStartTime() + Params.SIFS );
//								}
//							}
//						}
//						
//						
//						/* set the channel free after the data transmission is completed */
//				        if( ap.getTxStartTime() + ap.getTxDuration() + Params.SIFS == time ) {
//				        	System.out.println("AP " + ap.getId() + " is completed at " + time);
//				        	ap.setAsCompleted(time);
//				        	ap.setChannelAsFree();
//				        	
//				        	// new schedule
//				    		// services.printAPSchedule(apList);
//				        }
//			        }
//				}
//			}    
//		} 
		
		
		for(BaseStation bs:bts) {
			
			bs.initLTEU();
			int timeLTEU = bs.LTEUTimeSlot();
			double cost;
			for(long time = 0; time < Params.SIM_DURATION; time += 5*Params.SIFS) {
				for(int slotPercent=1; slotPercent<=5; slotPercent = slotPercent + timeLTEU) {
					if(slotPercent <= timeLTEU) {
						int timeLTE = Params.SIFS * ParamsLTE.SUBFRAME_DUR;
						int totalRB = ParamsLTE.RB * timeLTE;
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
							int totalData = ParamsLTE.CQI_MCS.get(ParamsLTE.SINR_CQI.indexOf(sinr)) * ParamsLTE.RE;
							if(ue.getDataRequest() == 512) {
								ue.setDataRec(totalData * timeLTE);
								bs.updateCLAA(totalData * timeLTE);
								bs.updateCTAR(512);
								totalRB = totalRB - 1*timeLTE;
							} else if(ue.getDataRequest() == 256) {
								ue.setDataRec(2 * timeLTE * totalData);
								bs.updateCLAA(2 * timeLTE * totalData);
								bs.updateCTAR(256);
								totalRB = totalRB - 2*timeLTE;
							} else {
								ue.setDataRec(4 * timeLTE * totalData);
								bs.updateCLAA(4 * timeLTE * totalData);
								bs.updateCTAR(128);
								totalRB = totalRB - 4*timeLTE;
							}
							cost = bs.calculateCost();
							System.out.println(bs.getCLAA(timeLTE * Params.SIFS));
							//bs.setNextState(bs.nextState());
						}
					} else {
						AccessPoint ap = bs.getAccessPoint();
						/* if this AP is scheduled to start at this time */
						if( ap.getTxStartTime() == time ) {
							// System.out.println(ap.getId() + " is scheduled at " + time);
							/* check whether the channel is busy */
							if(ap.isChannelBusy()) {
								/* if busy backoff for some time */
								ap.setTxStartTime(time + ap.getBackoffTime());
								ap.putInBackoffMode();
				                ap.updateBackoffTime();
							}
							else if(ap.waitedDIFS() == false) {
								/* if channel not busy wait for DIFS time */
								ap.setTxStartTime( time + Params.DIFS);
								ap.waitForDIFS();						
							}
							else {
								/* lock the channel */
								System.out.println("AP " + ap.getId() + " started at time " + time);
								ap.setChannelAsBusy();
								/* send data */
				            }
				        }
						/* otherwise check whether the station is in backoff mode */
						else if( ap.isInBackoffMode() ) {
							/* if the channel is busy then pause(increment) the backoff timer */
							if( ap.isChannelBusy() ) {
								ap.setTxStartTime( ap.getTxStartTime() + Params.SIFS );
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
						
						
						/* set the channel free after the data transmission is completed */
				        if( ap.getTxStartTime() + ap.getTxDuration() + Params.SIFS == time ) {
				        	System.out.println("AP " + ap.getId() + " is completed at " + time);
				        	ap.setAsCompleted(time);
				        	ap.setChannelAsFree();
				        	
				        	// new schedule
				    		// services.printAPSchedule(apList);
				        }
					}
				}
			}
		}
		
		
		
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
}