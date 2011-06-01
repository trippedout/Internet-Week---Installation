package com.lbi.internetweek.utils;

import processing.core.PVector;

public class Vector2D extends PVector 
{
	protected float 	lx = 0, ly = 0, lz = 0;
	public PVector 	velocity;
	
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
	}
	
	public PVector getVelocity()
	{
		return new PVector(this.x - lx, this.y - ly, this.z - lz);
	}

}
