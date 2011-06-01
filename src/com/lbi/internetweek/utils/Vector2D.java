package com.lbi.internetweek.utils;

import processing.core.PVector;

public class Vector2D extends PVector 
{
	protected float 	lx = 0, ly = 0, lz = 0;
	public PVector 		velocity = new PVector();
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void set( float nx, float ny, float nz )
	{
		lx = this.x;
		ly = this.y;
		lz = this.z;
		
		this.x = nx;
		this.y = ny;
		this.z = nz;
				
		velocity.x = nx - lx;
		velocity.y = ny - ly;
		velocity.z = nz - lz;
	}
	
	public PVector getVelocity()
	{
		return velocity;
	}
	
	/**
	 * Simple function to find the magnitude of the velocity, used in MPH tracking.
	 * @return
	 */
	
	public float vmag()
	{
		return velocity.mag();
	}

}
