package fr.free.totalboumboum.gui.menus.tournament;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.gui.common.MenuContainer;
import fr.free.totalboumboum.gui.common.MenuPanel;
import fr.free.totalboumboum.gui.common.SplitMenuPanel;

public class TournamentMain extends SplitMenuPanel
{	private static final long serialVersionUID = 1L;

	private BufferedImage image;

	public TournamentMain(MenuContainer container, MenuPanel parent) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	super(container,parent,BorderLayout.LINE_START);
		
		// size
		setPreferredSize(getConfiguration().getPanelDimension());		
		
		// background
		image = getConfiguration().getBackground();
//		float[] scales = { 0.5f, 0.5f, 0.5f, 1f };
//		float[] offsets = new float[4];
//		RescaleOp rop = new RescaleOp(scales, offsets, null);
//	    image = rop.filter(image, null);
		
		// panels
//		setDataPart(new TournamentData(this));
		setMenuPart(new TournamentMenu(this,parent));
	}

	
	@Override
	public void paintComponent(Graphics g)
	{	g.drawImage(image, 0, 0, null);
	}
	
}
