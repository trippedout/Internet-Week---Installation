package com.lbi.internetweek.states;

import com.lbi.internetweek.views.Bird;

import de.looksgood.ani.Ani;

public class PerchState extends BirdState
{
	boolean 		birdIsPerched	=	true;
	boolean			birdIsLanding	=	false;
	
	public PerchState( Bird b )
	{
		super(b);
	}
	
	@Override
	public void draw()
	{
		//setting the correct frame of bird animation
		bird.birdTexture 	=	bird.perchedFrames[0];
		
		//set new x and y vals
		if( birdIsPerched )
		{
			bird.x				=	bird.perch.x;
			bird.y				=	bird.perch.y;
		}
		else
		{
			perchBird();
		}
		
		//set scale
		bird.scale			=	0.85f;
	}

	private void perchBird() 
	{
		if( !birdIsLanding )
		{
			birdIsLanding = true;
			
			Ani.to( bird, 1, "x", bird.perch.x, Ani.QUAD_OUT, "onEnd:completePerching" );
			Ani.to( bird, 1, "y", bird.perch.y, Ani.QUAD_OUT );
		}		
	}
	
	public void completePerching( Ani ani )
	{
		birdIsPerched = true;
		birdIsLanding = false;
	}

}
