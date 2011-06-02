package com.lbi.internetweek.controller;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import com.lbi.internetweek.model.AppProxy;

import processing.core.PApplet;
import processing.core.PConstants;

public class KeyPressedCommand extends SimpleCommand
{
	public void execute(INotification note)
	{	
		switch( (Integer) note.getBody() )
		{
		case PConstants.LEFT:
			PApplet.println("CHANGE TO LEFT");			
			( (AppProxy) this.facade.retrieveProxy(AppProxy.NAME) )
				.setScreen(AppProxy.LEFT_SCREEN);
			break;
			
		case PConstants.RIGHT:
			PApplet.println("CHANGE TO RIGHT");
			( (AppProxy) this.facade.retrieveProxy(AppProxy.NAME) )
				.setScreen(AppProxy.RIGHT_SCREEN);
			break;
		}
	}
}
