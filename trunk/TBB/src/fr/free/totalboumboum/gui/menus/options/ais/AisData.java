package fr.free.totalboumboum.gui.menus.options.ais;

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

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.ai.AisConfiguration;
import fr.free.totalboumboum.gui.common.content.MyLabel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.container.LinesSubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.container.SubPanel.Mode;
import fr.free.totalboumboum.gui.common.structure.subpanel.content.Line;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class AisData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINE_COUNT = 20;
	private static final int LINE_UPS = 0;
	private static final int LINE_AUTO_ADVANCE = 1;
	private static final int LINE_AUTO_ADVANCE_DELAY = 2;
	private static final int LINE_HIDE_ALLAIS = 3;

	private LinesSubPanel optionsPanel;
	private AisConfiguration aisConfiguration;
	
	public AisData(SplitMenuPanel container)
	{	super(container);

		// title
		{	setTitleKey(GuiKeys.MENU_OPTIONS_AIS_TITLE);
		}
	
		// data
		{	int w = getDataWidth();
			int h = getDataHeight();
			optionsPanel = new LinesSubPanel(w,h,Mode.BORDER,LINE_COUNT,1,false);
			int titleWidth = (int)(optionsPanel.getDataWidth()*0.66);
			int iconWidth = optionsPanel.getLineHeight();
			
			// data
			{	aisConfiguration = Configuration.getAisConfiguration().copy();;
				
				// #0 UPS
				{	Line ln = optionsPanel.getLine(LINE_UPS);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_UPS_TITLE,false);
						col++;
					}
					// minus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_UPS_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setUps();
						col++;
					}
					// plus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_UPS_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #1 AUTO ADVANCE
				{	Line ln = optionsPanel.getLine(LINE_AUTO_ADVANCE);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_TITLE,false);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - GuiTools.subPanelMargin;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setAutoAdvance();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #2 AUTO ADVANCE DELAY
				{	Line ln = optionsPanel.getLine(LINE_AUTO_ADVANCE_DELAY);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_DELAY_TITLE,false);
						col++;
					}
					// minus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_DELAY_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setAutoAdvanceDelay();
						col++;
					}
					// plus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_DELAY_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}

				// #3 HIDE ALL-AIS
				{	Line ln = optionsPanel.getLine(LINE_HIDE_ALLAIS);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_HIDE_ALLAIS_TITLE,false);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - GuiTools.subPanelMargin;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setHideAllais();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// EMPTY
				{	for(int line=LINE_HIDE_ALLAIS+1;line<LINE_COUNT;line++)
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
	
	private void setAutoAdvance()
	{	boolean autoAdvance = aisConfiguration.getAutoAdvance();
		String key;
		if(autoAdvance)
			key = GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_ENABLED;
		else
			key = GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_DISABLED;
		optionsPanel.getLine(LINE_AUTO_ADVANCE).setLabelKey(1,key,true);
	}
	
	private void setAutoAdvanceDelay()
	{	long speed = aisConfiguration.getAutoAdvanceDelay();
		String text = Long.toString(speed/1000);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_TITLE+GuiKeys.TOOLTIP); 
		optionsPanel.getLine(LINE_AUTO_ADVANCE_DELAY).setLabelText(2,text,tooltip);
	}
	
	private void setUps()
	{	int ups = aisConfiguration.getAiUps();
		String text = Integer.toString(ups);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_AIS_LINE_UPS_TITLE+GuiKeys.TOOLTIP); 
		optionsPanel.getLine(LINE_UPS).setLabelText(2,text,tooltip);
	}
	
	private void setHideAllais()
	{	boolean hideAllais = aisConfiguration.getHideAllAis();
		String key;
		if(hideAllais)
			key = GuiKeys.MENU_OPTIONS_AIS_LINE_HIDE_ALLAIS_ENABLED;
		else
			key = GuiKeys.MENU_OPTIONS_AIS_LINE_HIDE_ALLAIS_DISABLED;
		optionsPanel.getLine(LINE_HIDE_ALLAIS).setLabelKey(1,key,true);
	}
	
	@Override
	public void refresh()
	{	// nothing to do here
	}

	public AisConfiguration getAisConfiguration()
	{	return aisConfiguration;
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
		{	// UPS
			case LINE_UPS:
				int ups = aisConfiguration.getAiUps();
				// minus
				if(pos[1]==1)
				{	if(ups>5)
						ups = ups-5;
				}
				// plus
				else //if(pos[1]==3)
				{	if(ups<=95)
						ups = ups + 5;
				}
				// common
				ups = 5*(ups/5);
				aisConfiguration.setAiUps(ups);
				setUps();
				break;
			// auto advance
			case LINE_AUTO_ADVANCE:
				boolean autoAdvance = !aisConfiguration.getAutoAdvance();
				aisConfiguration.setAutoAdvance(autoAdvance);
				setAutoAdvance();
				break;
			// auto advance delay
			case LINE_AUTO_ADVANCE_DELAY:
				long autoAdvanceDelay = aisConfiguration.getAutoAdvanceDelay();
				// minus
				if(pos[1]==1)
				{	if(autoAdvanceDelay>1000)
						autoAdvanceDelay = autoAdvanceDelay-1000;
				}
				// plus
				else //if(pos[1]==3)
				{	//if(ups<=95)
						autoAdvanceDelay = autoAdvanceDelay + 1000;
				}
				// common
				autoAdvanceDelay = 1000*(autoAdvanceDelay/1000);
				aisConfiguration.setAutoAdvanceDelay(autoAdvanceDelay);
				setAutoAdvanceDelay();
				break;
			// hide all-ais
			case LINE_HIDE_ALLAIS:
				boolean hideAllais = !aisConfiguration.getHideAllAis();
				aisConfiguration.setHideAllAis(hideAllais);
				setHideAllais();
				break;
		}

	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
}
