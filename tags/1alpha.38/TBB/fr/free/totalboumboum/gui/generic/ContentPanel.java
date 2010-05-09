package fr.free.totalboumboum.gui.generic;

import javax.swing.JPanel;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.gui.menus.main.MainFrame;

public abstract class ContentPanel extends JPanel
{
	protected static final long serialVersionUID = 1L;
	
	public abstract void refresh();

	/////////////////////////////////////////////////////////////////
	// FRAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract MainFrame getFrame();

	/////////////////////////////////////////////////////////////////
	// CONFIGURATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Configuration getConfiguration()
	{	return getFrame().getConfiguration();
	}
}
