package fr.free.totalboumboum.gui.game.loop;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.awt.image.VolatileImage;
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
	private BufferedImage backgroundImage;
	// 0=normal 1=VolatileImage 2=BufferStrategy
	// apparently BuffereStrategy doesn't work when the canvas is in a swing container
	private int mode = 1;
	private Image image;
	private BufferStrategy bufferStrategy;
	
	public LoopPanel(MenuContainer container, MenuPanel parent)
	{	super(container,parent);

		// background image
		backgroundImage = getConfiguration().getBackground();	
/*
		float[] scales = { 0.5f, 0.5f, 0.5f, 1f };
		float[] offsets = new float[4];
		RescaleOp rop = new RescaleOp(scales, offsets, null);
	    image = rop.filter((BufferedImage)image, null);
*/
	    loop = getConfiguration().getCurrentRound().getLoop();
	    Dimension dim = getConfiguration().getPanelDimension();
		setPreferredSize(dim);
		setDoubleBuffered(false);
//		setBackground(Color.RED);
//		setOpaque(false);
		setFocusable(true);
	}

	public void start()
	{	requestFocus();					
		loop.setPanel(this);
		if(mode==0)
		{	int width = getPreferredSize().width;
			int height = getPreferredSize().height;
			image = createImage(width, height);
		}
		else if(mode==1)
			image = createVolatileImage();
		else if(mode==2)
		{	Canvas canvas = new Canvas();
			canvas.setPreferredSize(getPreferredSize());
			add(canvas);
Graphics g = canvas.getGraphics();
g.drawOval(50,50,50,50);
g.dispose();
			canvas.createBufferStrategy(2);
			bufferStrategy = canvas.getBufferStrategy();
		}
	}

	public VolatileImage createVolatileImage()
	{	int width = getPreferredSize().width;
		int height = getPreferredSize().height;
		GraphicsConfiguration gc = getGraphicsConfiguration();
		VolatileImage result = gc.createCompatibleVolatileImage(width,height,Transparency.OPAQUE);
		int valid = result.validate(gc);
		if (valid == VolatileImage.IMAGE_INCOMPATIBLE)
			result = gc.createCompatibleVolatileImage(width,height,Transparency.OPAQUE);
		return result;
	}

	@Override
	public void paintScreen()
	{	try
		{	boolean again;
			do
			{	Graphics g = null;
				// buffered image
				if(mode==0)
					g = image.getGraphics();
				// volatile image : check validation
				else if(mode==1)
				{	GraphicsConfiguration gc = getGraphicsConfiguration();
					int valid = ((VolatileImage)image).validate(gc);
					if(valid == VolatileImage.IMAGE_INCOMPATIBLE)
						image = createVolatileImage();
					g = image.getGraphics();
				}
				// buffer strategy
				else if(mode==2)
					g = bufferStrategy.getDrawGraphics();

				// draw stuff in the buffer
				g.drawImage(backgroundImage,0,0,null);
				loop.drawLevel(g);
				g.dispose();

				// copy the buffer on the panel
				if(mode==0 ||mode==1)
				{	Graphics gp = getGraphics();
					gp.drawImage(image, 0, 0, null);
					gp.dispose();
				}
				else if(mode==2)
					bufferStrategy.show();
				//Tell the System to do the drawing now
				Toolkit.getDefaultToolkit().sync();

				// while condition
				if(mode==1)
					again = ((VolatileImage)image).contentsLost();
				else
					again = false;			
			}
			while(again);
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
//System.out.println("the player n�"+index+" is out !");		
	}
}