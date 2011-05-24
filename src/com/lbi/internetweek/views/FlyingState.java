package com.lbi.internetweek.views;

import com.lbi.internetweek.states.IBirdState;

public class FlyingState implements IBirdState 
{
	Bird		bird;
	
	public FlyingState( Bird b )
	{
		bird = b;
	}
	
	@Override
	public void draw() 
	{	
		
	}

}
