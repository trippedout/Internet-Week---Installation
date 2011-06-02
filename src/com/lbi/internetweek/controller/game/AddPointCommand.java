package com.lbi.internetweek.controller.game;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import com.lbi.internetweek.model.GameProxy;

public class AddPointCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		( (GameProxy) this.facade.retrieveProxy(GameProxy.NAME) )
			.addPointToScore();
	}

}
