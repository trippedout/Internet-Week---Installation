package com.lbi.internetweek.view.components;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;
import com.lbi.internetweek.model.AppProxy;
import com.lbi.internetweek.view.GUIMediator;

public class GUIView
{
	private Installation		_pa;
	private GUIMediator			_mediator;
	private AppProxy 			_appProxy;
	

	public GUIView(GUIMediator guiMediator)
	{
		_pa			=	ApplicationFacade.app;
		_mediator	=	guiMediator;
	}
	
	public GUIView(GUIMediator guiMediator, AppProxy appProxy)
	{
		_pa			=	ApplicationFacade.app;
		_mediator	=	guiMediator;
		_appProxy	=	appProxy;
	}
	
	public void updateScore( int newScore )
	{
		
	}
	
}
