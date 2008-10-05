package fr.free.totalboumboum.gui.common;

import fr.free.totalboumboum.gui.menus.main.MainFrame;

public abstract class InnerDataPanel extends ContentPanel
{	
	private static final long serialVersionUID = 1L;
	
	public InnerDataPanel(SplitMenuPanel container)
	{	this.container = container;
	}	

	public abstract void updateData();
	
	/////////////////////////////////////////////////////////////////
	// CONTAINER		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected SplitMenuPanel container;
	
	public SplitMenuPanel getContainer()
	{	return container;
	}

	/////////////////////////////////////////////////////////////////
	// FRAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public MainFrame getFrame()
	{	return container.getFrame();
	}
}
