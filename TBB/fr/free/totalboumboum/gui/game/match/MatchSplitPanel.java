package fr.free.totalboumboum.gui.game.match;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import fr.free.totalboumboum.gui.game.tournament.description.TournamentDescription;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;

public class MatchSplitPanel extends SplitMenuPanel
{	
	private BufferedImage image;

	public MatchSplitPanel(MenuContainer container, MenuPanel parent)
	{	super(container,parent,BorderLayout.PAGE_END);
		// size
		setPreferredSize(getConfiguration().getPanelDimension());
		// background
		image = getConfiguration().getBackground();
		// panels
		setMenuPart(new MatchMenu(this,parent));
	}
	
	@Override
	public void paintComponent(Graphics g)
	{	g.drawImage(image, 0, 0, null);
	}
}
