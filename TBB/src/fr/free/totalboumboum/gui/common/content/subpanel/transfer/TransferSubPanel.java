package fr.free.totalboumboum.gui.common.content.subpanel.transfer;

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
import java.util.ArrayList;

import javax.swing.JLabel;

import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class TransferSubPanel extends UntitledSubPanelTable implements MouseListener
{	private static final long serialVersionUID = 1L;
	
	public TransferSubPanel(int width, int height)
	{	super(width,height,1,1,1,false);
		
		init();
	}
		
	/////////////////////////////////////////////////////////////////
	// CONTENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int lineLeft;
	private int lineRight;
	
	public void init()
	{	// sizes
		int lines = 0;
		int cols = 1;
		if(showLeft)
		{	lineLeft = lines;
			lines++;			
		}
		if(showRight)
		{	lineRight = lines;
			lines++;			
		}
		reinit(cols,lines);

		// icons
		ArrayList<String> keys = new ArrayList<String>();
		if(showLeft)
			keys.add(GuiKeys.COMMON_TRANSFER_LEFT);
		if(showRight)
			keys.add(GuiKeys.COMMON_TRANSFER_RIGHT);

		// enabled
		ArrayList<Boolean> enabled = new ArrayList<Boolean>();
		if(showLeft)
			enabled.add(enabledLeft);
		if(showRight)
			enabled.add(enabledRight);
		
		// content
		for(int line=0;line<keys.size();line++)
		{	// header
			int col = 0;
			{	setLabelKey(line,col,keys.get(line),true);
				Color bg;
				if(enabled.get(line))
					bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				else
					bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				setLabelBackground(line,col,bg);
				JLabel label = getLabel(line,col);
				label.addMouseListener(this);
				col++;
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean showLeft = true;
	private boolean showRight = true;
	private boolean enabledLeft = true;
	private boolean enabledRight = true;

	public void setShowLeft(boolean showLeft)
	{	this.showLeft = showLeft;
		init();
	}
	public void setEnabledLeft(boolean enabled)
	{	enabledLeft = enabled;
		init();
	}

	public void setShowRight(boolean showRight)
	{	this.showRight = showRight;
		init();
	}
	public void setEnabledRight(boolean enabled)
	{	enabledRight = enabled;
		init();
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
	{	
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{	
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{	JLabel label = (JLabel)e.getComponent();
		int[] pos = getLabelPosition(label);
		
		// left
		if(pos[0]==lineLeft && enabledLeft)
		{	fireTransferLeft();			
		}
		// right
		else if(pos[0]==lineRight && enabledRight)
		{	fireTransferRight();			
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<TransferSubPanelListener> listeners = new ArrayList<TransferSubPanelListener>();
	
	public void addListener(TransferSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(TransferSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	private void fireTransferLeft()
	{	for(TransferSubPanelListener listener: listeners)
			listener.transferLeftClicked();
	}

	private void fireTransferRight()
	{	for(TransferSubPanelListener listener: listeners)
			listener.transferRightClicked();
	}
}
