package com.lbi.internetweek.controller.game;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import processing.core.PVector;

import com.lbi.internetweek.view.PoofMediator;

public class ShowPoofCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		( (PoofMediator) this.facade.retrieveMediator(PoofMediator.NAME) )
			.onShowPoofCommand( (PVector) note.getBody() );
	}
}
