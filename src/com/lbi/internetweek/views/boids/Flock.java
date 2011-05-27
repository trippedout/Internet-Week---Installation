package com.lbi.internetweek.views.boids;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Flock 
{
	public PApplet pa;
	
	public Zone zone;
	
	// Keep in zone by looping or bouncing	
	public boolean loopZone;
	
	// Boids list
	public ArrayList<Boid> list;
	
	// Boids try to approach other boids (factor)
	public float cohesion;
	
	// Boids try to keep a minimum distance between others (factor)
	public float avoidance;
	
	// Boids try to move in the same way than other boids (factor)
	public float imitation;
	
	// Boids radius
	public int size = 7;
	
	// Distance for which boids touch things (avoidance, collisions)
	public float aura;
	
	// Distance for which boids sees and responds to its environment
	public float perception;
	
	// Define cruise-speed and stress-speed limit
	public float speedLimit;

	/**
	 * Class constructor.
	 */
	public Flock( PApplet p )
	{
		pa = p;
		zone 		= 	new Zone(this, 30, 20, pa.width+200, pa.height-200);
		loopZone 	= 	false;
		list 		= 	new ArrayList();
		cohesion 	= 	6;
		avoidance 	= 	20;
		imitation 	= 	6;
		size 		=	12;
		aura 		=	3 * size;
		perception 	=	7 * size;
		speedLimit 	=	7;
	}


	/**
	 * Add a boid to flock.
	 */
	public void add(){
		list.add(new Boid(this));
	}
	/**
	 * Add a boid to flock at the defined position.
	 */
	public void add(int x, int y){
		list.add(new Boid(this,x, y));
	}
	/**
	 * Add a boid to flock at the defined position.
	 */
	public void add(PVector p){
		list.add(new Boid(this,p));
	}

	/**
	 * Add a boid to flock outside object list auras or don't create it.
	 *
	 * @param  objs Obj ArrayList : list of objects which areas should be avoided
	 */
	public void addOutside(ArrayList objs)
	{
		/*
		boolean collide = false;
		int it = 0, itMax = 50000;
		PVector pos;
		// do this while random pos is in Obj zone
		do{
			it++;
			pos = boids.zone.randomPos();
			for(int i=0; i<objs.size(); i++)
			{
				Obj o = (Obj) objs.get(i);
				if(PVector.dist(pos, o.pos) < o.aura)
				{
					collide = true;
					break;
				}
			}
		} 
		while(collide && it < itMax);
		if(!collide) list.add(new Boid(pos));
		//*/
	}

	/**
	 * Delete boid from flock.
	 *
	 * @param  i boid index
	 */
	public void del(int i){
		list.remove(i);
	}

	/**
	 * Return the number of boids in flock.
	 */
	public int size(){
		return list.size();
	}

	/**
	 * Return the specified boid.
	 *
	 * @param i boid index
	 * @return Boid
	 */
	public Boid get(int i){
		return (Boid) list.get(i);
	}

	/**
	 * Update each boid position and velocity according to simulation rules.
	 */
	public void update()
	{
		for(int i=0; i<list.size(); i++)
		{
			Boid b = (Boid) list.get(i);

			// basic flock rules
			b.goToCenter();
			b.keepDistance();
			b.matchVelocity();

			// additionnal rules
			b.keepInZone(zone, loopZone);

			// Cruise-speed limitation
			b.limitVelocity(speedLimit);

			// food attraction and eat
			boolean contact;
			
			/*
			for(int j=0; j<foodList.size(); j++)
			{
				Obj f = (Obj) foodList.get(j);
				// medium attraction when perceived
				b.effector(int(f.pos.x), int(f.pos.y), f.aura + boids.perception, 1.2);
				// big repulsion on contact
				b.effector(int(f.pos.x), int(f.pos.y), f.rad + boids.size, -40);
				// if contact
				if(PVector.dist(f.pos, b.pos) < f.rad + boids.size)
				{
					f.rad -= .03;// reduce obj size
					// if size is too small delete it and update control value
					if(f.rad <= 5){
						foodList.remove(j);
						Slider s = (Slider) controlP5.controller("food");
						s.setValue(foodList.size());
					}
				}
			}
			//*/

			/*
			// obstacles boids.avoidance
			for(int j=0; j<obstList.size(); j++)
			{
				Obj o = (Obj) obstList.get(j);
				// small repulsion when perceived (anticipation)
				b.effector(int(o.pos.x), int(o.pos.y), o.aura + boids.perception, -0.2);
				// big repulsion on contact (collision)
				b.effector(int(o.pos.x), int(o.pos.y), o.rad + boids.size, -20);
			}
			//*/

			// mouse repulsion
			//b.effector(mouseX, mouseY, 70, -7);

			// Max-speed limitation
			//b.limitVelocity(speedLimit * 2);

			// finally update position
			b.pos.add(b.vel);
		}
	}

	/**
	 * Draw each boid.
	 */
	public void display()
	{
		for(int i=0; i < list.size(); i++)
		{
			Boid b = (Boid) list.get(i);
			b.display();
		}
	}

	/**
	 * Draw perception distance of boid alpha.
	 */
	public void displayPerception()
	{
		/*
		fill(150, 180, 255, 50);
		stroke(150, 180, 255);
		Boid b = (Boid) boids.get(0);
		ellipse(b.pos.x,  b.pos.y, boids.perception * 2, boids.perception * 2);
		//*/
	}

	/**
	 * Draw aura distance of boid alpha.
	 */
	public void displayAura()
	{
		/*
		fill(255, 180, 150, 50);
		stroke(255, 180, 150);
		Boid b = (Boid) boids.get(0);
		ellipse(b.pos.x,  b.pos.y, boids.aura * 2, boids.aura * 2);
		//*/
	}

}
