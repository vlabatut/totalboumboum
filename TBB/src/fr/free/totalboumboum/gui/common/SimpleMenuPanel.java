package fr.free.totalboumboum.gui.common;

public abstract class SimpleMenuPanel extends MenuPanel implements ButtonAware
{
	public SimpleMenuPanel(MenuContainer container, MenuPanel parent)
	{	this.container = container;
		this.parent = parent;
	}	
}
