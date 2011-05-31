package com.lbi.internetweek.controller;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import com.lbi.internetweek.view.BirdsMediator;

public class ModeChangeCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		( (BirdsMediator) this.facade.retrieveMediator(BirdsMediator.NAME) )
			.setMode( (Integer) note.getBody() );
	}

}
