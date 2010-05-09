package fr.free.totalboumboum.gui.common.content;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.image.ImageTools;

public class MyLabel extends JLabel implements MouseListener, MouseMotionListener
{	private static final long serialVersionUID = 1L;

	public MyLabel()
	{	super();
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public MyLabel(String text)
	{	super(text);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean hiddenFlag = false;

	public void switchDisplay(boolean display)
	{	if(display)
		{	hiddenFlag = true;
			if(mouseSensitive)
				switchBackground(false);
		}
	}	
	
	/////////////////////////////////////////////////////////////////
	// CONTENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * set a label's icon or text. for an icon, the label's PREFERRED SIZE
	 * MUST BE SET before calling this method.
	 * @param label
	 * @param key
	 * @param imageFlag
	 */
	public void setKey(String key, boolean imageFlag)
	{	String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
		// is there an available icon ?
		if(imageFlag)
		{	BufferedImage icon = GuiTools.getIcon(key);
			setIcon(icon,tooltip);		
		}
		// if not : use text
		else
		{	String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
			setText(text,tooltip);
		}		
	}
	
	public void setIcon(BufferedImage icon, String tooltip)
	{	ImageIcon icn = null;
		if(icon!=null)
		{	int h = getPreferredSize().height;
			double zoom = h/(double)icon.getHeight();
			icon = ImageTools.resize(icon,zoom,true);
			icn = new ImageIcon(icon);
		}
		setText(null);
		setIcon(icn);
		setToolTipText(tooltip);
	}
	
	public void setText(String text, String tooltip)
	{	setIcon(null);
		setText(text);
		setToolTipText(tooltip);
	}

	/////////////////////////////////////////////////////////////////
	// BACKGROUND		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Color originalBackground;
	private Color darkerBackground;
	private boolean mouseSensitive = false;
	private boolean darkSwitch = false;
	
	public void setMouseSensitive(boolean mouseSensitive)
	{	this.mouseSensitive = mouseSensitive;
	}
	
	@Override
	public void setBackground(Color background)
	{	originalBackground = background;
		darkerBackground = GuiTools.changeColorAlpha(background,GuiTools.ALPHA_DARKER_CHANGE);
		switchBackground(darkSwitch);
	}
	
	public void switchBackground(boolean darker)
	{	Color bg;
		if(darker)
			bg = darkerBackground;
		else
			bg = originalBackground;
		darkSwitch = darker;
		super.setBackground(bg);		
	}
	
	/////////////////////////////////////////////////////////////////
	// MOUSE LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void mouseClicked(MouseEvent e)
	{	
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{	if(mouseSensitive)
			switchBackground(true);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{	if(mouseSensitive)
			switchBackground(false);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{	
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}

	/////////////////////////////////////////////////////////////////
	// MOUSE MOTION LISTENER	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void mouseDragged(MouseEvent e)
	{	
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{	if(hiddenFlag)
		{	hiddenFlag = false;
			if(mouseSensitive)
				switchBackground(true);
		}
	}
}
