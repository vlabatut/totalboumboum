package fr.free.totalboumboum.gui.game.loop;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.loop.LoopRenderPanel;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SimpleMenuPanel;


public class LoopPanel extends SimpleMenuPanel implements LoopRenderPanel
{	private static final long serialVersionUID = 1L;
	private Loop loop;
	private Image image;
	
	public LoopPanel(MenuContainer container, MenuPanel parent)
	{	super(container,parent);
/*		
		// image
		image = getConfiguration().getBackground();
		float[] scales = { 0.5f, 0.5f, 0.5f, 1f };
		float[] offsets = new float[4];
		RescaleOp rop = new RescaleOp(scales, offsets, null);
	    image = rop.filter((BufferedImage)image, null);
*/	    
	    loop = getConfiguration().getCurrentRound().getLoop();
	    Dimension dim = getConfiguration().getPanelDimension();
		setPreferredSize(dim);
		setDoubleBuffered(false);
		setBackground(Color.RED);
//		setOpaque(false);
		setFocusable(true);
	}

	public void start()
	{	requestFocus();					
		loop.setPanel(this);
	}

	public Image getBackgroundImage()
	{	
		int width = getPreferredSize().width;
		int height = getPreferredSize().height;
		image = createImage(width,height);
		
		Graphics g = image.getGraphics();
		BufferedImage backgroundImage = getConfiguration().getBackground();
//		float[] scales = { 0.5f, 0.5f, 0.5f, 1f };
//		float[] offsets = new float[4];
//		RescaleOp rop = new RescaleOp(scales, offsets, null);
//		backgroundImage = rop.filter(backgroundImage, null);
		g.drawImage(backgroundImage, 0, 0, null);	
		
		return image;
	}

	@Override
	public void paintScreen()
	{	Graphics g;
		try
		{	g = this.getGraphics();
			if ((g != null) && (image != null))
			{	g.drawImage(image, 0, 0, null);
			}
			// Sync the display on some systems (on Linux, this fixes event queue problems)
			Toolkit.getDefaultToolkit().sync();
			//
			g.dispose();
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}

	@Override
	public void loopOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	//System.out.println("the round is over.");		
				replaceWith(parent);
			}
		});				
	}

	
	
	
	
	
	@Override
	public void refresh()
	{	
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{	
	}

	@Override
	public void playerOut(int index)
	{			
//System.out.println("the player n°"+index+" is out !");		
	}
}
