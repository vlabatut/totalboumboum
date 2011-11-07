package fr.free.totalboumboum.gui.generic;

import fr.free.totalboumboum.gui.menus.main.MainFrame;


public abstract class MenuPanel extends ContentPanel
{
	
	/////////////////////////////////////////////////////////////////
	// CONTAINER		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected MenuContainer container;
	
	public MenuContainer getContainer()
	{	return container;
	}
	
	public void replaceWith(MenuPanel newPanel)
	{	container.setMenuPanel(newPanel);
	}
	
	/////////////////////////////////////////////////////////////////
	// PARENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected MenuPanel parent;
	
	public MenuPanel getMenuParent()
	{	return parent;
	}

	public void setMenuParent(MenuPanel parent)
	{	this.parent = parent;
	}
	
	/////////////////////////////////////////////////////////////////
	// FRAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public MainFrame getFrame()
	{	return container.getFrame();
	}
}