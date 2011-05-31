package com.lbi.internetweek.view.boids;

import processing.core.PVector;

public class Boid 
{
	Flock f;
	public PVector pos;
	public PVector vel;

	/**
	 * Class constructor.
	 */
	public Boid(Flock fl)
	{
		PVector v =  fl.zone.randomPos();
		init( fl, v.x, v.y, fl.pa.random(0,1), fl.pa.random(0,1) );
	}
	/**
	 * Class constructor specifying position of boid.
	 */
	public Boid( Flock fl, int x, int y)
	{
		init( fl, x, y, fl.pa.random(0,1), fl.pa.random(0,1) );
	}
	/**
	 * Class constructor specifying position of boid.
	 */
	public Boid( Flock fl, PVector p)
	{
		init(fl, fl.pa.floor(p.x), fl.pa.floor(p.y), fl.pa.random(0,1), fl.pa.random(0,1));
	}
	
	private void init( Flock fl, float px, float py, float vx, float vy)
	{
		f = fl;
		pos = new PVector(px, py);
		vel = new PVector(vx, vy);
	}

	/**
	 * <p>Boid cohesion : try to approach other boids.</p>
	 * <p>Compute the average direction to the barycenter of the
	 * percepted flock, and add a part of that to velocity.</p>
	 */
	public void goToCenter()
	{
		PVector v = new PVector();
		int nbr = 0;
		for(int i=0; i < f.size(); i++)
		{
			Boid b = (Boid) f.get(i);
			if(b != this && PVector.dist(pos, b.pos) < f.perception)
			{
				// add position of each other percepted boid
				v.add(b.pos);
				nbr++;
			}
		}
		if(nbr > 0)
		{
			v = PVector.sub(PVector.div(v, (float)nbr), pos); // do average
			v.mult(f.cohesion * 0.001f); // set intensity
			vel.add(v); // and add to velocity
		}
	}

	/**
	 * <p>Boid avoidance : try to keep a minimum distance between others.</p>
	 * <p>Compute the average opposite way to the "touched"
	 * flock, and add a part of that to velocity.</p>
	 */
	public void keepDistance()
	{
		PVector u, v = new PVector();
		for(int i=0; i < f.size(); i++)
		{
			Boid b = (Boid)f.get(i);
			float d = PVector.dist(pos, b.pos);
			if(b != this && d < f.aura)
			{
				u = PVector.sub(pos, b.pos);
				u.normalize();
				u.div(d);
				v.add(u);
			}
		}
		v.mult(f.avoidance); // set intensity
		vel.add(v); // and add to velocity
	}

	/**
	 * <p>Boid imitation : try to move in the same way than other boids.</p>
	 * <p>Compute the average velocity of the percepted
	 * flock, and add a part of that to velocity.</p>
	 */
	public void matchVelocity()
	{
		PVector v = new PVector();
		int nbr = 0;
		for(int i=0; i < f.size(); i++)
		{
			Boid b = (Boid) f.get(i);
			if(b != this && PVector.dist(pos, b.pos) < f.perception)
			{
				// add velocity of each other percepted boid
				v.add(b.vel);
				nbr++;
			}
		}
		if(nbr > 0)
		{
			v = PVector.sub(PVector.div(v, nbr), vel); // do average
			v.mult(f.imitation * 0.01f); // set intensity
			vel.add(v); // and add to velocity
		}
	}

	public void limitVelocity(float lim){
		if(vel.mag() > lim)
			vel = PVector.mult(PVector.div(vel, vel.mag()), lim);
	}

	/**
	 * Keep a boid in zone by changing his velocity or position.
	 *
	 * @param  z Zone : zone to keep in
	 * @param  loopZone boolean : loop or bounce on zone sides
	 */
	public void keepInZone(Zone z, boolean loopZone)
	{
		int maxX = z.x + z.w;
		int maxY = z.y + z.h;
		if(!loopZone)
		{
			// When a boid approach a side, an opposite vector is added to velocity (bounce)
			float efficiency = .03f;
			// efficiency ~ 10 : boids immediately rejected
			// efficiency ~ .1  : boids slowly change direction
			PVector v = new PVector();
			if(pos.x <= z.x + f.perception)       v.x = (z.x + f.perception - pos.x) * efficiency;
			else if(pos.x >= maxX - f.perception) v.x = (maxX - f.perception - pos.x) * efficiency;
			if(pos.y <= z.y + f.perception)       v.y = (z.y + f.perception - pos.y) * efficiency;
			else if(pos.y >= maxY - f.perception) v.y = (maxY - f.perception - pos.y) * efficiency;
			vel.add(v);
		}
		else
		{
			// When a boid cross a side, he's placed on the opposite side (teleport)
			if(pos.x <= z.x)       pos.x = maxX;
			else if(pos.x >= maxX) pos.x = z.x;
			if(pos.y <= z.y)       pos.y = maxY;
			else if(pos.y >= maxY) pos.y = z.y;
		}
	}

	/**
	 * Affect boid velocity with an linear point force.
	 *
	 * @param  x x-coordinate of the effector
	 * @param  y y-coordinate of the effector
	 * @param  a aura of the effector (effect distance)
	 * @param  i intensity of the effector. > 0 for attraction, < 0 for repulsion
	 */
	public void effector(int x, int y, float a, float e)
	{
		float d = f.pa.dist(x, y, pos.x, pos.y);
		if(d < a)
		{
			PVector v = new PVector();
			if(pos.x >= x) v.x += (a-d) * e * 0.01;
			else v.x -= (a-d) * e * 0.01;
			if(pos.y >= y) v.y += (a-d) * e * 0.01;
			else v.y -= (a-d) * e * 0.01;
			vel.sub(v);
		}
	}

	/**
	 * Display boid.
	 */
	void display()
	{
		/*
		fill(210, 230, 250);
		stroke(100,150,250);
		ellipse(pos.x, pos.y, boids.size, boids.size);
		float f = boids.size * 0.2; // vector factor
		line(pos.x, pos.y, pos.x + vel.x * f, pos.y + vel.y * f);
		//*/
	}
}
