package com.lbi.internetweek.model;

import org.puremvc.java.patterns.proxy.Proxy;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;
import com.lbi.internetweek.views.BirdsController;

import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.behaviors.GravityBehavior;

public class PhysicsProxy extends Proxy
{
	public static final String NAME = "PhysicsProxy";
	
	private Installation					_app;
	
	private static VerletPhysics2D       	_physics;
	private static VerletParticle2D[]    	_particles;
	
	public PhysicsProxy()
	{
		super(NAME);
		
		_app			=	ApplicationFacade.app;
		
		setupPhysics();
	}
	
	private final void setupPhysics()
	{
		_physics = new VerletPhysics2D();
		_physics.setWorldBounds( new Rect( 0, 0, 
				_app.width, 
				_app.height - 180
				)
		);
		_physics.addBehavior( new GravityBehavior( new Vec2D( 0, 0.8f ) ) );
		_physics.setDrag( 0.01f );
		
		for( int i = 0; i < AppProxy.NUM_BIRDS; ++i )
		{
			_physics.addParticle( new VerletParticle2D( new Vec2D() ) );
		}		
	}
		
	public static final VerletPhysics2D getPhysics()
	{		
		return _physics;
	}

}
