package fr.free.totalboumboum.gui.game.match;

import java.awt.BorderLayout;

import fr.free.totalboumboum.gui.game.tournament.description.TournamentDescription;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;

public class MatchSplitPanel extends SplitMenuPanel
{	
	public MatchSplitPanel(MenuContainer container, MenuPanel parent)
	{	super(container,parent,BorderLayout.PAGE_END);
		// size
		setPreferredSize(getConfiguration().getPanelDimension());
		// panels
		setMenuPart(new MatchMenu(this,parent));
	}

}
