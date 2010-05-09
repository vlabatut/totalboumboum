package fr.free.totalboumboum.gui.menus.tournament;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;

public class TournamentMain extends SplitMenuPanel
{	private static final long serialVersionUID = 1L;

	public TournamentMain(MenuContainer container, MenuPanel parent) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	super(container,parent,BorderLayout.LINE_START);
		// size
		setPreferredSize(getConfiguration().getPanelDimension());		
		// panels
//		setDataPart(new TournamentData(this));
		setMenuPart(new TournamentMenu(this,parent));
	}
}
