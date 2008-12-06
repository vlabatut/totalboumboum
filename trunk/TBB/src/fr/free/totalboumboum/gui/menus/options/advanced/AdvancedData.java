package fr.free.totalboumboum.gui.menus.options.advanced;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;

import javax.swing.JLabel;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.engine.EngineConfiguration;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.Line;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelLines;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class AdvancedData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINE_COUNT = 20;
	private static final int LINE_FPS = 0;
	private static final int LINE_ADJUST = 1;
	private static final int LINE_SPEED = 2;

	private UntitledSubPanelLines optionsPanel;
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
			optionsPanel = new UntitledSubPanelLines(w,h,LINE_COUNT,false);
			int tWidth = (int)(w*0.66);
			
			// data
			{	engineConfiguration = Configuration.getEngineConfiguration().copy();;
				
				// #0 FPS
				{	Line ln = optionsPanel.getLine(LINE_FPS);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMaxWidth(col,tWidth);
						ln.setLabelPreferredWidth(col,tWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_FPS_TITLE,false);
						col++;
					}
					// minus button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_FPS_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
						setFps();
						col++;
					}
					// plus button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_FPS_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #1 AUTO ADJUST
				{	Line ln = optionsPanel.getLine(LINE_ADJUST);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMaxWidth(col,tWidth);
						ln.setLabelPreferredWidth(col,tWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_ADJUST_TITLE,false);
						col++;
					}
					// value
					{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
						setAdjust();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #2 GAME SPEED
				{	Line ln = optionsPanel.getLine(LINE_SPEED);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMaxWidth(col,tWidth);
						ln.setLabelPreferredWidth(col,tWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_SPEED_TITLE,false);
						col++;
					}
					// minus button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_SPEED_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
						setGameSpeed();
						col++;
					}
					// plus button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_ADVANCED_LINE_SPEED_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}

				// EMPTY
				{	for(int line=LINE_SPEED+1;line<LINE_COUNT;line++)
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
	{	JLabel label = (JLabel)e.getComponent();
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
			// border
			case LINE_ADJUST:
				boolean adjust = !engineConfiguration.getAutoFps();
				engineConfiguration.setAutoFps(adjust);
				setAdjust();
				break;
			// smooth graphics
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
		}

	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
}
