package fr.free.totalboumboum.gui.common;

import javax.swing.JPanel;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
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
	public GuiConfiguration getConfiguration()
	{	return getFrame().getConfiguration();
	}
}
