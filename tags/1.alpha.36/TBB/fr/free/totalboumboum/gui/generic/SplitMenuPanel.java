package fr.free.totalboumboum.gui.generic;

import java.awt.BorderLayout;

public abstract class SplitMenuPanel extends MenuPanel implements MenuContainer
{
	protected String menuLocation;
	
	public SplitMenuPanel(MenuContainer container, MenuPanel parent, String menuLocation)
	{	// fields
		this.container = container;
		this.parent = parent;
		this.menuLocation = menuLocation;
		// layout
		BorderLayout layout = new BorderLayout(); 
		setLayout(layout);
	}	
	
	public void refresh()
	{	dataPart.refresh();
		menuPart.refresh();
	}

	/////////////////////////////////////////////////////////////////
	// MENU PART		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected InnerMenuPanel menuPart;
	
	public void setMenuPart(InnerMenuPanel menuPart)
	{	if(this.menuPart!=null)
			remove(this.menuPart);
		this.menuPart = menuPart;
		this.menuPart.refresh();
		add(menuPart, menuLocation);
		validate();
		repaint();		
	}

	/////////////////////////////////////////////////////////////////
	// DATA PART		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ContentPanel dataPart;

	public void setDataPart(ContentPanel dataPart)
	{	if(this.dataPart!=null)
			remove(this.dataPart);
		this.dataPart = dataPart;
		this.dataPart.refresh();
		add(dataPart, BorderLayout.CENTER);
		validate();
		repaint();		
	}
	
	/////////////////////////////////////////////////////////////////
	// MENU PANEL		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setMenuPanel(MenuPanel newPanel)
	{	setDataPart(newPanel);
	}
}
