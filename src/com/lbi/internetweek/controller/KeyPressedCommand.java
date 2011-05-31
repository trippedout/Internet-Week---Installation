package com.lbi.internetweek.controller;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import processing.core.PConstants;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;

public class KeyPressedCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		Installation app = ApplicationFacade.app;
		
		switch( (Integer) note.getBody() )
		{
		case PConstants.UP:
			app.println("UP KEY PRESSED");
			break;
			
		case PConstants.DOWN:
			app.println("DOWN KEY PRESSED");
			break;
		}
	}
}
