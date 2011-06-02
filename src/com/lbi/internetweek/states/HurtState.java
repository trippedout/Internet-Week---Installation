package com.lbi.internetweek.states;

import processing.core.PApplet;
import processing.core.PVector;
import toxi.geom.Vec2D;

import com.lbi.internetweek.view.components.Bird;

public class HurtState extends BirdState
{
	private int 		MULTIPLIER = 35;
	
	public PVector 		startingPosition;
	public PVector 		startingVelocity;

	private boolean 	isParticleSet = false;
	
	private int			life		=	300;

	public HurtState(Bird b)
	{
		super(b);
	}

	@Override
	public void draw()
	{
		if (!isParticleSet)
		{
			//PApplet.println("\tstartingPos & Vel: " + startingPosition + " " + startingVelocity );
			
			life		=	300;
			
			bird.getParticle().x = startingPosition.x;
			bird.getParticle().y = startingPosition.y;
			
			bird.getParticle()
				.clearForce()
				.clearVelocity();
			
			bird.getParticle()
				.addVelocity( new Vec2D( startingVelocity.x * MULTIPLIER, startingVelocity.y * MULTIPLIER ) );
			
			isParticleSet = true;
		}
		
		bird.x = bird.getParticle().x;
		bird.y = bird.getParticle().y;

		Vec2D v = bird.getParticle().getVelocity();
		
		if( bird.x < 50 || bird.x > bird.getParent().width - 100 )
		{
			v.x *= -.8f;			
		}		
		if( bird.y < 50 || bird.y > bird.getParent().height - 200 )
		{
			v.y *= -.8f;
			v.x *= .9f;
		}	

		bird.getParticle()
			.clearVelocity();
		
		bird.getParticle()
			.addVelocity( v );
		
		if( life < 0 )
		{
			bird.setState(bird.flyingState);
		}
		
		life--;		
	}

	@Override
	public void setState(IBirdState state)
	{
		super.setState(state);

		isParticleSet = false;

		if (state instanceof TweetState)
		{
			bird.isTweeting = true;
			bird.state = bird.tweetState;
		} 
		else if (state instanceof FlyingState)
		{
			bird.getBoid().pos = new PVector(bird.x, bird.y);
			bird.getBoid().vel = new PVector(bird.getParent().random(-10,10), bird.getParent().random(-10, 0));
			
			bird.state = bird.flyingState;
		}
	}
}
