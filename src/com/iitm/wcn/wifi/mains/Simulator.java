package com.iitm.wcn.wifi.mains;

import java.util.List;

import com.iitm.wcn.wifi.entities.AccessPoint;
import com.iitm.wcn.wifi.entities.UserEquipment;
import com.iitm.wcn.wifi.params.Params;
import com.iitm.wcn.wifi.services.Services;
import com.lteu.deliverable.LteuQlearning;

public class Simulator {
	private static List<AccessPoint> apList;
	private static List<UserEquipment> ueList;
	
	
	public static void main(String[] args) {
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
	}

}
