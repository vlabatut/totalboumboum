package fr.free.totalboumboum.gui.game.loop;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

import javax.swing.SwingUtilities;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.loop.LoopRenderPanel;
import fr.free.totalboumboum.gui.common.structure.MenuContainer;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.SimpleMenuPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;

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
    	setIgnoreRepaint(true);

		// background image
		backgroundImage = GuiConfiguration.getMiscConfiguration().getDarkBackground();	
/*
		float[] scales = { 0.5f, 0.5f, 0.5f, 1f };
		float[] offsets = new float[4];
		RescaleOp rop = new RescaleOp(scales, offsets, null);
	    image = rop.filter((BufferedImage)image, null);
*/
	    loop = Configuration.getGameConfiguration().getTournament().getCurrentMatch().getCurrentRound().getLoop();
	    Dimension dim = Configuration.getVideoConfiguration().getPanelDimension();
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
//System.out.println("the player n°"+index+" is out !");		
	}
}
