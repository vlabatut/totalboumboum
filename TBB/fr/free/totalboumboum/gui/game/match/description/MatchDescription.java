package fr.free.totalboumboum.gui.game.match.description;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import fr.free.totalboumboum.gui.SwingTools;
import fr.free.totalboumboum.gui.generic.InnerDataPanel;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SimpleMenuPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;
import fr.free.totalboumboum.gui.menus.options.OptionsMenu;
import fr.free.totalboumboum.gui.menus.tournament.TournamentMain;
import fr.free.totalboumboum.tools.GuiTools;

public class MatchDescription extends InnerDataPanel
{	
	private static final long serialVersionUID = 1L;

	public MatchDescription(SplitMenuPanel container)
	{	super(container);
		// background
		setBackground(Color.CYAN);
		// size
		int height = SwingTools.getSize(SwingTools.HORIZONTAL_SPLIT_DATA_PANEL_HEIGHT);
		int width = SwingTools.getSize(SwingTools.HORIZONTAL_SPLIT_DATA_PANEL_WIDTH);
		setPreferredSize(new Dimension(width,height));
		//
		add(new JLabel("Match Description"));
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
