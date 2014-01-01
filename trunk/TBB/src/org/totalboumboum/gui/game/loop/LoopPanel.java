package org.totalboumboum.gui.game.loop;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.loop.LoopRenderPanel;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.gui.common.structure.MenuContainer;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.SimpleMenuPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.game.round.RoundSplitPanel;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;

/**
 * This class is used to paint the actual game
 * (sprites and everything, not the menus).
 * 
 * @author Vincent Labatut
 */
public class LoopPanel extends SimpleMenuPanel implements LoopRenderPanel
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a standard loop panel.
	 * 
	 * @param container
	 * 		Swing container for this panel.
	 * @param parent
	 * 		Parent menu item.
	 * @param loop
	 * 		Game engine, which directly draws in this panel.
	 */
	public LoopPanel(MenuContainer container, MenuPanel parent, VisibleLoop loop)
	{	super(container,parent);
		setBackground(Color.BLACK);
    	setIgnoreRepaint(true);

		// background image
		backgroundImage = GuiConfiguration.getMiscConfiguration().getDarkBackground();
		backgroundColor = Configuration.getVideoConfiguration().getBorderColor();

/*
		float[] scales = { 0.5f, 0.5f, 0.5f, 1f };
		float[] offsets = new float[4];
		RescaleOp rop = new RescaleOp(scales, offsets, null);
	    image = rop.filter((BufferedImage)image, null);
*/
	    this.loop = loop;
	    Dimension dim = Configuration.getVideoConfiguration().getPanelDimension();
		setPreferredSize(dim);
		setDoubleBuffered(false);
//		setBackground(Color.RED);
//		setOpaque(false);
		setFocusable(true);
	}

	/////////////////////////////////////////////////////////////////
	// IMAGE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Game engine */
	private VisibleLoop loop;
	/** Color used to fill the screen before drawing the game (in the end: the border color) */
	private Color backgroundColor;
	/** If we want to draw an image instead of a plain (black) background */
	private BufferedImage backgroundImage;
	/**
	 * Various types of buffering I tried:
	 *  <ul>
	 *  	<li>0=normal</li>
	 *  	<li>1=VolatileImage</li>
	 *  	<li>2=BufferStrategy</li>
	 *  </ul>
	 *  Apparently BuffereStrategy doesn't work when the canvas is in a swing container.
	 */
	private int mode = 1;
	/** Image to draw into */
	private Image image;
	/** Buffering strategy (cache stuff) */
	private BufferStrategy bufferStrategy;
	
	/**
	 * Starts displaying the game.
	 */
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
			canvas.createBufferStrategy(2);
			bufferStrategy = canvas.getBufferStrategy();
		}
	}

	/**
	 * Create the appropriate type of image.
	 * 
	 * @return
	 * 		A volatile image.
	 */
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
	public void captureScreen()
	{	// build image copy
		BufferedImage copy = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = copy.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		
		// get current date and time
		Calendar cal = new GregorianCalendar();
		Date currentTime = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
		String currentStr = sdf.format(currentTime);
		
		// set up file name
		String path = FilePaths.getCaptureImagesPath() + File.separator + currentStr + FileNames.EXTENSION_BMP;
		File file = new File(path);
		
		// record image as bmp
		try
		{	ImageIO.write(copy, "bmp", file);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// LOOP RENDER PANEL	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void playerOut(int index)
	{	//System.out.println("the player #"+index+" is out !");		
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
				if(backgroundColor==null)
					g.drawImage(backgroundImage,0,0,null);
				else
				{	g.setColor(backgroundColor);
					Dimension dim = Configuration.getVideoConfiguration().getPanelDimension();
					g.fillRect(0,0,dim.width,dim.height);				
				}
				boolean cs = loop.draw(g);
				g.dispose();

				// possibly make a screen capture
				if(cs)
					captureScreen();
				
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
		{	@Override
			public void run()
			{	//System.out.println("the round is over.");
				parent.refresh();
				if(parent instanceof RoundSplitPanel)
					((RoundSplitPanel)parent).autoAdvance();
				replaceWith(parent);
			}
		});
	}
	
	/////////////////////////////////////////////////////////////////
	// REFRESH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	//
	}

	/////////////////////////////////////////////////////////////////
	// ACTION PERFORMED	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void actionPerformed(ActionEvent arg0)
	{	//
	}

	/////////////////////////////////////////////////////////////////
	// READY-SET-GO			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String getMessageTextGo()
	{	String result = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_MESSAGES_GO);
		return result;
	}

	@Override
	public String getMessageTextReady() 
	{	String result = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_MESSAGES_READY);
		return result;
	}

	@Override
	public String getMessageTextSet()
	{	String result = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_MESSAGES_SET);
		return result;
	}

	@Override
	public Font getMessageFont(double width, double height)
	{	String txts[] = {getMessageTextGo(),getMessageTextReady(),getMessageTextSet()};
		List<String> texts = Arrays.asList(txts);
		Font result = GuiConfiguration.getMiscConfiguration().getFont();
		float fontSize = GuiFontTools.getOptimalFontSize(width*0.9,height*0.9,texts);
		result = result.deriveFont(fontSize);
		return result;
	}
}
