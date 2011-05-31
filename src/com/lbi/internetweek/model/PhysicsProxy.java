package com.lbi.internetweek.model;

import org.puremvc.java.patterns.proxy.Proxy;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;
import com.lbi.internetweek.views.BirdsController;

import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;

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
		_physics.setDrag( 0.005f );
		_physics.setWorldBounds( new Rect( 50, 
				50, 
				_app.width-100, 
				_app.height-100
				)
		);

		for( int i = 0; i < AppProxy.NUM_BIRDS; ++i )
		{
			VerletParticle2D p = new VerletParticle2D( 
					new Vec2D( 
							_app.random( _app.width-100 ) + 50,
							_app.random( _app.height-100 ) + 50
							)
			);
			
			_physics.addParticle(p);
		}		
	}
		
	public static final VerletPhysics2D getPhysics()
	{		
		return _physics;
	}

}
