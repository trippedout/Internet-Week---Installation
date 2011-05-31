package com.lbi.internetweek;

import org.puremvc.java.patterns.facade.Facade;
import org.puremvc.java.patterns.observer.Notification;

import com.lbi.internetweek.controller.KeyPressedCommand;
import com.lbi.internetweek.controller.MainDrawCommand;
import com.lbi.internetweek.controller.StartupCommand;
import com.lbi.internetweek.controller.TwitterUpdateCommand;
import com.lbi.internetweek.controller.MainUpdateCommand;

public class ApplicationFacade extends Facade
{
	public static final String STARTUP 				=	"startup";
	public static final String UPDATE				=	"update";
	public static final String DRAW 				=	"draw";
	public static final String KEY_PRESSED 			=	"key_pressed";
	public static final String TWITTER_UPDATED		=	"twitter_updated";
	
	private static ApplicationFacade instance = null;
	
	public static Installation app;
	
	public static ApplicationFacade getInst()
	{
		if( instance == null )
		{
			instance = new ApplicationFacade();
		}
		
		return instance;
	}
	
	protected void initializeController()
	{
		super.initializeController();
		
		registerCommand( STARTUP, new StartupCommand() );
		registerCommand( DRAW, new MainDrawCommand() );
		registerCommand( UPDATE, new MainUpdateCommand() );
		registerCommand( KEY_PRESSED, new KeyPressedCommand() );
		registerCommand( TWITTER_UPDATED, new TwitterUpdateCommand() );
	}

	public void startup(Installation $app)
	{
		ApplicationFacade.app = $app;
		notifyObservers( new Notification(STARTUP, null, null) );
	}

	public void update()
	{
		notifyObservers( new Notification(UPDATE, null, null) );
	}
	
	public void draw()
	{
		notifyObservers( new Notification(DRAW, null, null) );		
	}

	public void keyPressed(int code)
	{
		notifyObservers( new Notification(KEY_PRESSED, code) );
	}

}
