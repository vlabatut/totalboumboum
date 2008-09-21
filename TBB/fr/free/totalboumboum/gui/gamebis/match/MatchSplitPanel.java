package fr.free.totalboumboum.gui.gamebis.match;

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
		try 
		{	String imagePath = FileTools.getImagesPath()+File.separator+"background.jpg";
			image = ImageTools.loadImage(imagePath,null);
			double zoomY = getPreferredSize().getHeight()/(double)image.getHeight();
			double zoomX = getPreferredSize().getWidth()/(double)image.getWidth();
			double zoom = Math.max(zoomX,zoomY);
			image = ImageTools.resize(image,zoom,true);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		// panels
		setMenuPart(new MatchMenu(this,parent));
	}

	
	@Override
	public void paintComponent(Graphics g)
	{	g.drawImage(image, 0, 0, null);
	}
}
