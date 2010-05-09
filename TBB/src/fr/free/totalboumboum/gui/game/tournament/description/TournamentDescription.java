package fr.free.totalboumboum.gui.game.tournament.description;

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

public abstract class TournamentDescription extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	public TournamentDescription(SplitMenuPanel container)
	{	super(container);
		
		// title
		String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_TOURNAMENT_DESCRIPTION_TITLE);
		setTitle(txt);
	
	}
}
