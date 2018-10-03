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
		//------------- lte start ---------------
		ServicesLTE servicesLTE;// = new Services();
		double avgUserAssoc[] = new double[ParamsLTE.NUM_BASE_STATIONS];
		double avgUserAssoc1[] = new double[ParamsLTE.NUM_BASE_STATIONS];
		double sum1 = 0, sum2 = 0;
		for(int j=0; j<ParamsLTE.TRIALS; j++){
			servicesLTE = new ServicesLTE();
			bts = servicesLTE.CreateBS(ParamsLTE.BS_DISRIBUTION);
			ue = servicesLTE.CreateUE(bts, ParamsLTE.USER_DISRIBUTION);
			for(int i=0; i<bts.size(); i++){
				avgUserAssoc[i] = avgUserAssoc[i] + bts.get(i).averageSINR(); // taking avg value over all bts
				avgUserAssoc1[i] = avgUserAssoc1[i] + bts.get(i).getUsersAssociated().size(); // taking avg value over all bts
				//sum1 = sum1 + bts.get(j).getUsersAssociated().size();  // for taking a avg value
			}
//			for(int k=0; k < bts.get(0).getUsersAssociated().size(); k++) {
//				System.out.println(bts.get(0).getUsersAssociated().get(k).getSINR());
//			}
			//sum2 = sum2 + sum1/bts.size();
		}
		//System.out.println(sum2/Params.TRIALS); // avg value
		for(int j=0; j<ParamsLTE.NUM_BASE_STATIONS; j++){
			avgUserAssoc[j] = avgUserAssoc[j]/ParamsLTE.TRIALS;
			avgUserAssoc1[j] = avgUserAssoc1[j]/ParamsLTE.TRIALS;
			System.out.println(avgUserAssoc[j]);
		} //avgUserAssoc1[j] + " " +
		
		//------------- lte finish ---------------
		
		//------------- wifi start ---------------
		
		LteuQlearning lteu = new LteuQlearning();
		Services services = new Services();
		//Params.SIM_DURATION = 2000;
		System.out.println("simulation duration" + Params.SIM_DURATION);
		
		/* Initialization of simulation environment */
		apList = services.createAPs();
		//services.printAPLocations(apList);
		
		ueList = services.createUsers(apList);
		//services.printUELocations(ueList);
		
		/* Find neighbours of APs */
		services.findNeighbours(apList);
		
		/*for(AccessPoint ap: apList ) {
			ap.printNeighbours();
		}*/
		
		// services.printAPSchedule(apList);
		
		/* Association of users to APs */
		services.associateUsersToAPs(ueList, apList);
	
		services.printUEAssociations(apList);
		
		/* end of initialization */
		
		/* simulation */
		/* simulation runs in steps of SIFS, because SIFS is the smallest unit */
		for( long time = 0; time < Params.SIM_DURATION; time += 5*Params.SIFS ) {
			//System.out.println("Timeslot " + time);
			int slotCutout = lteu.LTEUTimeSlot();
			for(int slotPercent=1; slotPercent<=5; slotPercent++) {
				if(slotPercent > slotCutout) {
					for(AccessPoint ap: apList ) {
						/* if this AP is scheduled to start at this time */
						if( ap.getTxStartTime() == time ) {
							// System.out.println(ap.getId() + " is scheduled at " + time);
							/* check whether the channel is busy */
							if( ap.isChannelBusy() ) {
								/* if busy backoff for some time */
								ap.setTxStartTime( time + ap.getBackoffTime() );
								ap.putInBackoffMode();
				                ap.updateBackoffTime();
							}
							else if( ap.waitedDIFS() == false ) {
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
		
		//------------- wifi finish ---------------
		
	}
}