package fr.free.totalboumboum.gui.game.tournament.statistics;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import fr.free.totalboumboum.gui.generic.EntitledDataPanel;
import fr.free.totalboumboum.gui.generic.InnerDataPanel;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SimpleMenuPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;
import fr.free.totalboumboum.gui.menus.options.OptionsMenu;
import fr.free.totalboumboum.gui.menus.tournament.TournamentMain;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class TournamentStatistics extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	public TournamentStatistics(SplitMenuPanel container)
	{	super(container);

		// title
		String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_TOURNAMENT_TITLE_STATISTICS);
		setTitle(txt);
	
	}

	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
	{	// nothing to do here
	}
}
