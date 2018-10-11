package com.lteu.deliverable;

import java.util.Random;

public class LteuQlearning {
	double qTable [][] = new double [6][4];
	double cTar, r, epsilon, alpha, gamma, ci, cLAA, cost;
	int state, action, minAction, nextState;
	Random rand = new Random();
	
	public LteuQlearning() {
		for(int i=0;i<6;i++) {
			for(int j=0; j<4; j++) {
				qTable[i][j] = 0;
			}
		}
		state = 0;
		epsilon = rand.nextDouble();
	}
	
	public int LTEUTimeSlot () {
		r = rand.nextDouble();
		if(r<epsilon) {
			int action = rand.nextInt(4);
		} else {
			action = minAction;
		}
		return action;
	}
	
	double calculateCost(){
		return Math.abs(cTar-cLAA);
	}
	
	int nextState(){
		if(cLAA < 1) {
			return 0;
		} else if(1 <= cLAA && cLAA < 10) {
			return 1;
		} else if(10 <= cLAA && cLAA < 20) {
			return 2;
		} else if(20 <= cLAA && cLAA < 30) {
			return 3;
		} else if(30 <= cLAA && cLAA < 40) {
			return 4;
		} else {
			return 5;
		}
	}
	
	int minAction(double qTable[][], int i) {
		int act = 0;
		for(int j=1; j<4; j++) {
			if(qTable[i][act] > qTable[i][j]) {
				act = j;
			}
		}
		return act;
	}
	
	void update(double qTable[][], int i, int j, double cost, double currCost, double nextCost, double alpha, double gamma) {
		qTable[i][j] = (1-alpha) * currCost + alpha * (cost + gamma * nextCost);
	}
}
