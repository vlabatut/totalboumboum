package fr.free.totalboumboum.gui.game.tournament.statistics;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import fr.free.totalboumboum.gui.common.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.InnerDataPanel;
import fr.free.totalboumboum.gui.common.MenuContainer;
import fr.free.totalboumboum.gui.common.MenuPanel;
import fr.free.totalboumboum.gui.common.SimpleMenuPanel;
import fr.free.totalboumboum.gui.common.SplitMenuPanel;
import fr.free.totalboumboum.gui.menus.options.OptionsMenu;
import fr.free.totalboumboum.gui.menus.tournament.TournamentMain;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class TournamentStatistics extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	public TournamentStatistics(SplitMenuPanel container)
	{	super(container);

		// title
		String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_TOURNAMENT_STATISTICS_TITLE);
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
