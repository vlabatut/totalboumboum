package org.totalboumboum.gui.menus.options.advanced;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.text.NumberFormat;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.engine.EngineConfiguration;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.LinesSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel.Mode;
import org.totalboumboum.gui.common.structure.subpanel.content.Line;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiImageTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AdvancedData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINE_COUNT = 20;
	private static final int LINE_FPS = 0;
	private static final int LINE_ADJUST = 1;
	private static final int LINE_SPEED = 2;
	private static final int LINE_LOG_CONTROLS = 3;
	private static final int LINE_CACHE = 4;
	private static final int LINE_CACHE_LIMIT = 5;
	private static final int LINE_RECORD_GAMES = 6;

	private LinesSubPanel optionsPanel;
	private EngineConfiguration engineConfiguration;
	
	private String[] speedTexts = 
	{	"/4",
		"/3",
		"/2",
		new Character('\u00D7').toString()+"1",
		new Character('\u00D7').toString()+"2",
		new Character('\u00D7').toString()+"3",
		new Character('\u00D7').toString()+"4"
	};
	private double[] speedValues = 
	{	0.25,
		0.33,
		0.5,
		1,
		2,
		3,
		4
	};
	
	public AdvancedData(SplitMenuPanel container)
	{	super(container);

		// title
		{	setTitleKey(GuiKeys.MENU_OPTIONS_ADVANCED_TITLE);
		}
	
		// data
		{	int w = getDataWidth();
			int h = getDataHeight();
			optionsPanel = new LinesSubPanel(w,h,Mode.BORDER,LINE_COUNT,1,false);
			int titleWidth = (int)(optionsPanel.getDataWidth()*0.66);
			int iconWidth = optionsPanel.getLineHeight();
			
			// data
			{	engineConfiguration = Configuration.getEngineConfiguration().copy();;
				
				// #0 FPS
				{	Line ln = optionsPanel.getLine(LINE_FPS);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_FPS_TITLE,false);
						col++;
					}
					// minus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_FPS_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiSizeTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setFps();
						col++;
					}
					// plus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_FPS_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #1 AUTO ADJUST
				{	Line ln = optionsPanel.getLine(LINE_ADJUST);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_ADJUST_TITLE,false);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - GuiSizeTools.subPanelMargin;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setAdjust();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #2 GAME SPEED
				{	Line ln = optionsPanel.getLine(LINE_SPEED);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_SPEED_TITLE,false);
						col++;
					}
					// minus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_SPEED_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiSizeTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setGameSpeed();
						col++;
					}
					// plus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_SPEED_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #3 LOG CONTROLS
				{	Line ln = optionsPanel.getLine(LINE_LOG_CONTROLS);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_LOG_CONTROLS_TITLE,false);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - GuiSizeTools.subPanelMargin;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setLogControls();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #4 CACHE
				{	Line ln = optionsPanel.getLine(LINE_CACHE);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_CACHE_TITLE,false);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - GuiSizeTools.subPanelMargin;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setCache();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #5 CACHE LIMIT
				{	Line ln = optionsPanel.getLine(LINE_CACHE_LIMIT);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_CACHE_LIMIT_TITLE,false);
						col++;
					}
					// minus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_CACHE_LIMIT_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiSizeTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setCacheLimit();
						col++;
					}
					// plus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_CACHE_LIMIT_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #6 RECORD GAMES
				{	Line ln = optionsPanel.getLine(LINE_RECORD_GAMES);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_RECORD_GAMES_TITLE,false);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - GuiSizeTools.subPanelMargin;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setRecordGames();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// EMPTY
				{	for(int line=LINE_RECORD_GAMES+1;line<LINE_COUNT;line++)
					{	Line ln = optionsPanel.getLine(line);
						int col = 0;
						int maxWidth = ln.getWidth();
						ln.setLabelMinWidth(col,maxWidth);
						ln.setLabelPrefWidth(col,maxWidth);
						ln.setLabelMaxWidth(col,maxWidth);
						col++;
					}
				}
			}
			
			setDataPart(optionsPanel);
		}
	}
	
	private void setAdjust()
	{	boolean adjust = engineConfiguration.getAutoFps();
		String key;
		if(adjust)
			key = GuiKeys.MENU_OPTIONS_ADVANCED_LINE_ADJUST_ENABLED;
		else
			key = GuiKeys.MENU_OPTIONS_ADVANCED_LINE_ADJUST_DISABLED;
		optionsPanel.getLine(LINE_ADJUST).setLabelKey(1,key,true);
	}
	
	private void setFps()
	{	int fps = engineConfiguration.getFps();
		String text = Integer.toString(fps);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_ADVANCED_LINE_FPS_TITLE+GuiKeys.TOOLTIP); 
		optionsPanel.getLine(LINE_FPS).setLabelText(2,text,tooltip);
	}
	
	private void setGameSpeed()
	{	double speed = engineConfiguration.getSpeedCoeff();
		String text = null;
		int i = 0;
		while(i<speedValues.length && text==null)
		{	if(speedValues[i]==speed)
				text = speedTexts[i];
			else
				i++;
		}
		if(text==null)
		{	NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(2);
			nf.setMinimumFractionDigits(2);
			text = nf.format(speed);
		}
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_ADVANCED_LINE_SPEED_TITLE+GuiKeys.TOOLTIP); 
		optionsPanel.getLine(LINE_SPEED).setLabelText(2,text,tooltip);
	}
	
	private void setLogControls()
	{	boolean logControls = engineConfiguration.getLogControls();
		String key;
		if(logControls)
			key = GuiKeys.MENU_OPTIONS_ADVANCED_LINE_LOG_CONTROLS_ENABLED;
		else
			key = GuiKeys.MENU_OPTIONS_ADVANCED_LINE_LOG_CONTROLS_DISABLED;
		optionsPanel.getLine(LINE_LOG_CONTROLS).setLabelKey(1,key,true);
	}
	
	private void setCache()
	{	boolean cache = engineConfiguration.isSpriteMemoryCached();
		String key;
		if(cache)
			key = GuiKeys.MENU_OPTIONS_ADVANCED_LINE_CACHE_ENABLED;
		else
			key = GuiKeys.MENU_OPTIONS_ADVANCED_LINE_CACHE_DISABLED;
		optionsPanel.getLine(LINE_CACHE).setLabelKey(1,key,true);
	}
	
	private void setCacheLimit()
	{	long limit = engineConfiguration.getSpriteCacheLimit();
		limit = limit/1024/1024;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);
		nf.setMinimumFractionDigits(0);
		String text = nf.format(limit);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_ADVANCED_LINE_CACHE_LIMIT_TITLE+GuiKeys.TOOLTIP); 
		optionsPanel.getLine(LINE_CACHE_LIMIT).setLabelText(2,text,tooltip);
	}

	private void setRecordGames()
	{	boolean recordGames = engineConfiguration.isRecordRounds();
		String key;
		if(recordGames)
			key = GuiKeys.MENU_OPTIONS_ADVANCED_LINE_RECORD_GAMES_ENABLED;
		else
			key = GuiKeys.MENU_OPTIONS_ADVANCED_LINE_RECORD_GAMES_DISABLED;
		optionsPanel.getLine(LINE_RECORD_GAMES).setLabelKey(1,key,true);
	}
	
	@Override
	public void refresh()
	{	// nothing to do here
	}

	public EngineConfiguration getEngineConfiguration()
	{	return engineConfiguration;
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
	{	MyLabel label = (MyLabel)e.getComponent();
		int[] pos = optionsPanel.getLabelPosition(label);
		switch(pos[0])
		{	// panel dimension
			case LINE_FPS:
				int fps = engineConfiguration.getFps();
				// minus
				if(pos[1]==1)
				{	if(fps>=35)
						fps = fps-5;
				}
				// plus
				else //if(pos[1]==3)
				{	if(fps<=95)
						fps = fps + 5;
				}
				// common
				fps = 5*(fps/5);
				engineConfiguration.setFps(fps);
				setFps();
				break;
			// adjust FPS
			case LINE_ADJUST:
				boolean adjust = !engineConfiguration.getAutoFps();
				engineConfiguration.setAutoFps(adjust);
				setAdjust();
				break;
			// speed
			case LINE_SPEED:
				double speed = engineConfiguration.getSpeedCoeff();
				int index;
				// minus
				if(pos[1]==1)
				{	index = 0;
					while(speedValues[index]<speed && index<speedValues.length)
						index++;
					if(index>0)
						index --;
				}
				// plus
				else //if(pos[1]==3)
				{	index = speedValues.length-1;
					while(speedValues[index]>speed && index>=0)
						index--;
					if(index<speedValues.length-1)
						index ++;
				}
				// common
				engineConfiguration.setSpeedCoeff(speedValues[index]);
				setGameSpeed();
				break;
			// log controls
			case LINE_LOG_CONTROLS:
				boolean logControls = !engineConfiguration.getLogControls();
				engineConfiguration.setLogControls(logControls);
				setLogControls();
				break;
			// cache
			case LINE_CACHE:
				boolean cache = !engineConfiguration.isSpriteMemoryCached();
				engineConfiguration.setSpriteMemoryCached(cache);
				setCache();
				break;
			// cache limit
			case LINE_CACHE_LIMIT:
				long limit = engineConfiguration.getSpriteCacheLimit();
				// minus
				if(pos[1]==1)
				{	if(limit>=16*1024*1024)
						limit = limit-16*1024*1024;
				}
				// plus
				else //if(pos[1]==3)
				{	limit = limit + 16*1024*1024;				
				}
				// common
				engineConfiguration.setSpriteCacheLimit(limit);
				setCacheLimit();
				break;			
			// record games
			case LINE_RECORD_GAMES:
				boolean recordGames = !engineConfiguration.isRecordRounds();
				engineConfiguration.setRecordRounds(recordGames);
				setRecordGames();
				break;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
}
