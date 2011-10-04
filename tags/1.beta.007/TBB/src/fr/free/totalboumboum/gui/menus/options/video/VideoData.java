package fr.free.totalboumboum.gui.menus.options.video;

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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.video.VideoConfiguration;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.Line;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelLines;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class VideoData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINE_COUNT = 20;

	private static final int LINE_PANELDIM = 0;
	private static final int LINE_BORDER = 1;
	private static final int LINE_SMOOTH = 2;

	private UntitledSubPanelLines optionsPanel;
	private VideoConfiguration videoConfiguration;
	
	private int[] resolutionWidths = 
	{	500,
		640,
		800,
		1024,
		1280,
		1600
	};
	private int[] resolutionHeights = 
	{	375,
		480,
		600,
		768,
		1024,
		1200
	};
	
	public VideoData(SplitMenuPanel container)
	{	super(container);

		// title
		{	setTitleKey(GuiKeys.MENU_OPTIONS_VIDEO_TITLE);
		}
	
		// data
		{	int w = getDataWidth();
			int h = getDataHeight();
			optionsPanel = new UntitledSubPanelLines(w,h,LINE_COUNT,false);
			int tWidth = (int)(w*0.66);
			
			// data
			{	videoConfiguration = Configuration.getVideoConfiguration().copy();;
				
				// #0 panel dimension
				{	Line ln = optionsPanel.getLine(LINE_PANELDIM);
//					ln.setBackgroundColor(GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMaxWidth(col,tWidth);
						ln.setLabelPreferredWidth(col,tWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_VIDEO_LINE_PANEL_DIMENSION,false);
						col++;
					}
					// minus button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_VIDEO_LINE_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	
//						int panelWidth = videoConfiguration.getPanelDimension().width;
//						int panelHeight = videoConfiguration.getPanelDimension().height;
//						String text = Integer.toString(panelWidth)+new Character('\u00D7').toString()+Integer.toString(panelHeight);
//						int maxWidth = GuiTools.getPixelWidth(ln.getLineFontSize(),text);
//						for(int i=0;i<resolutionHeights.length;i++)
//						{	String txt = Integer.toString(resolutionWidths[i])+new Character('\u00D7').toString()+Integer.toString(resolutionHeights[i]);
//							int txtWidth = GuiTools.getPixelWidth(ln.getLineFontSize(),txt);
//							if(txtWidth>maxWidth)
//								maxWidth = txtWidth;
//						}
//						ln.setLabelMaxWidth(col,maxWidth);
//						ln.setLabelPreferredWidth(col,maxWidth);
						ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
						setPanelDimension();
						col++;
					}
					// plus button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_VIDEO_LINE_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #1 border color
				{	Line ln = optionsPanel.getLine(LINE_BORDER);
//					ln.setBackgroundColor(GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMaxWidth(col,tWidth);
						ln.setLabelPreferredWidth(col,tWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_VIDEO_LINE_BORDER_COLOR,false);
						col++;
					}
					// value
					{	String text = "Black";
//						int txtWidth = GuiTools.getPixelWidth(ln.getLineFontSize(),text);
						String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_VIDEO_LINE_BORDER_COLOR+GuiKeys.TOOLTIP); 
//						ln.setLabelMaxWidth(col,txtWidth);
						ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
						ln.setLabelText(col,text,tooltip);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #2 smooth graphics
				{	Line ln = optionsPanel.getLine(LINE_SMOOTH);
//					ln.setBackgroundColor(GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMaxWidth(col,tWidth);
						ln.setLabelPreferredWidth(col,tWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_VIDEO_LINE_SMOOTH_GRAPHICS,false);
						col++;
					}
					// value
					{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
						setSmoothGraphics();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}

				// EMPTY
				{	for(int line=LINE_SMOOTH+1;line<LINE_COUNT;line++)
					{	Line ln = optionsPanel.getLine(line);
						int col = 0;
						int mw = ln.getWidth();
						ln.setLabelMinWidth(col,mw);
						ln.setLabelPreferredWidth(col,mw);
						ln.setLabelMaxWidth(col,mw);
						col++;
					}
				}
			}
			
			setDataPart(optionsPanel);
		}
	}
	
	private void setSmoothGraphics()
	{	boolean smooth = videoConfiguration.getSmoothGraphics();
		String key;
		if(smooth)
			key = GuiKeys.MENU_OPTIONS_VIDEO_LINE_ENABLED;
		else
			key = GuiKeys.MENU_OPTIONS_VIDEO_LINE_DISABLED;
		optionsPanel.getLine(LINE_SMOOTH).setLabelKey(1,key,true);
	}
	
	private void setPanelDimension()
	{	int panelWidth = videoConfiguration.getPanelDimension().width;
		int panelHeight = videoConfiguration.getPanelDimension().height;
		String text = Integer.toString(panelWidth)+new Character('\u00D7').toString()+Integer.toString(panelHeight);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_VIDEO_LINE_PANEL_DIMENSION+GuiKeys.TOOLTIP); 
		optionsPanel.getLine(LINE_PANELDIM).setLabelText(2,text,tooltip);
		
	}
	
	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
	{	// nothing to do here
	}

	public VideoConfiguration getVideoConfiguration()
	{	return videoConfiguration;
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
		int[] pos = optionsPanel.getLabelPosition(label);
		switch(pos[0])
		{	// panel dimension
			case LINE_PANELDIM:
				Dimension dim = videoConfiguration.getPanelDimension();
				int index;
				int pw = dim.width;
				// minus
				if(pos[1]==1)
				{	index = 0;
					while(resolutionWidths[index]<pw && index<resolutionWidths.length)
						index++;
					if(index>0)
						index --;
				}
				// plus
				else //if(pos[1]==3)
				{	index = resolutionWidths.length-1;
					while(resolutionWidths[index]>pw && index>=0)
						index--;
					if(index<resolutionWidths.length-1)
						index ++;
				}
				// common
				Dimension newDim = new Dimension(resolutionWidths[index],resolutionHeights[index]);
				videoConfiguration.setPanelDimension(newDim.width,newDim.height);
				setPanelDimension();
				break;
			// border
			case LINE_BORDER:
				break;
			// smooth graphics
			case LINE_SMOOTH:
				boolean smooth = !videoConfiguration.getSmoothGraphics();
				videoConfiguration.setSmoothGraphics(smooth);
				setSmoothGraphics();
				break;
		}

	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
}