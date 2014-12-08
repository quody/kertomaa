package com.me.mygdxgame;

import java.util.*;
import  com.badlogic.gdx.physics.box2d.World;

public class ProjectileTimer extends TimerTask {
	public void main(gamelogic game, Object remove) {
	      // creating timer task, timer
	      TimerTask tasknew = new ProjectileTimer();
	      Timer timer = new Timer();
	      
	      // scheduling the task at interval
	      timer.schedule(tasknew,0, 100);      
	   }
	   // this method performs the task
	   public void run() {
	      System.out.println("timer working");      
	   }    
}


//todo

//1 create remove methods
//give 2 variables to this timer: world/game whatever is needed and the projectile to be destroyed
//the run will then destroy the object after x ms with those
//note: gamelogic should probably handle activating this timer bcos of special ammo