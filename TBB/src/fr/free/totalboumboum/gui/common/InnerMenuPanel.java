package fr.free.totalboumboum.gui.common;

import javax.swing.JPanel;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.menus.main.MainFrame;

public abstract class InnerMenuPanel extends JPanel implements ButtonAware
{
	public InnerMenuPanel(SplitMenuPanel container, MenuPanel parent)
	{	this.container = container;
		this.parent = parent;
	}	
	
	public abstract void refresh();

	/////////////////////////////////////////////////////////////////
	// CONTAINER		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected SplitMenuPanel container;
	
	public SplitMenuPanel getContainer()
	{	return container;
	}
	
	public void replaceWith(MenuPanel newPanel)
	{	container.getContainer().setMenuPanel(newPanel);
	}

	/////////////////////////////////////////////////////////////////
	// DATA PART		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ContentPanel dataPart;
	
	public ContentPanel getDataPart()
	{	return dataPart;	
	}
	public void setDataPart(ContentPanel dataPart)
	{	this.dataPart = dataPart;		
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
	
	/////////////////////////////////////////////////////////////////
	// CONFIGURATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public GuiConfiguration getConfiguration()
	{	return getFrame().getConfiguration();
	}
}
