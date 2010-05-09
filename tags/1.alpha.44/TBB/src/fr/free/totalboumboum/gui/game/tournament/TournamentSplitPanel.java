package fr.free.totalboumboum.gui.game.tournament;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.gui.game.tournament.description.TournamentDescription;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;

public class TournamentSplitPanel extends SplitMenuPanel
{	
	private BufferedImage image;

	public TournamentSplitPanel(MenuContainer container, MenuPanel parent) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	super(container,parent,BorderLayout.PAGE_END);
	
		// size
		setPreferredSize(getConfiguration().getPanelDimension());
		
		// background
		image = getConfiguration().getBackground();
//		float[] scales = { 0.5f, 0.5f, 0.5f, 1f };
//		float[] offsets = new float[4];
//		RescaleOp rop = new RescaleOp(scales, offsets, null);
//	    image = rop.filter(image, null);
		
		// panels
//		setDataPart(new TournamentDescription(this));
		setMenuPart(new TournamentMenu(this,parent));
	}
	
	@Override
	public void paintComponent(Graphics g)
	{	g.drawImage(image, 0, 0, null);
	}
}
