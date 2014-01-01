package org.totalboumboum.gui.menus.options.ais;

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

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.configuration.ai.AisConfiguration.AutoAdvance;
import org.totalboumboum.configuration.ai.AisConfiguration.TournamentAutoAdvance;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
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
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.xml.sax.SAXException;

/**
 * Table used to the display the AI-related options.
 * 
 * @author Vincent Labatut
 */
public class AisData extends EntitledDataPanel implements MouseListener
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/** Row count */
	private static final int LINE_COUNT = 20;
	/** Row used to display the update-per-second option */
	private static final int LINE_UPS = 0;
	/** Row used to display the auto-advance option */
	private static final int LINE_AUTO_ADVANCE = 1;
	/** Row used to display the auto-advance delay */
	private static final int LINE_AUTO_ADVANCE_DELAY = 2;
	/** Row used to display the tournament auto-advance mode */
	private static final int LINE_TRMNT_AUTO_ADVANCE_MODE = 3;
	/** Row used to display the pack for the tournament auto-advance mode */
	private static final int LINE_TRMNT_AUTO_ADVANCE_PACK = 4;
	/** Row used to display the option hiding all-AI games (simulating them) */
	private static final int LINE_HIDE_ALL_AIS = 5;
	/** Row used to display the option allowing to automatically threaten idle agents */
	private static final int LINE_BOMB_USELESS_AIS = 6;
	/** Row used to display the option allowing to automatically threaten cycling agents */
	private static final int LINE_BOMB_CYCLING_AIS = 7;
	/** Row used to display the option displaying AI exceptions in the console */
	private static final int LINE_DISPLAY_EXCEPTIONS = 8;
	/** Row used to display the option logging AI exceptions in a file */
	private static final int LINE_LOG_EXCEPTIONS = 9;
	/** Row used to display the option allowing to record tournament stats as separate text files */
	private static final int LINE_RECORD_STATS = 10;

	/** Packs available for the tournament auto-advance "pack" mode */
	private List<String> availablePacks;
	/** Panel used to display options */
	private LinesSubPanel optionsPanel;
	/** Associated configuration object */
	private AisConfiguration aisConfiguration;
	
	/**
	 * Creates a new panel to display ai-related options.
	 * 
	 * @param container
	 * 		Container of this panel.
	 */
	public AisData(SplitMenuPanel container)
	{	super(container);

		initPackList();
		
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
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiSizeTools.subPanelMargin - 2*iconWidth;
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
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #1 AUTO ADVANCE
				{	Line ln = optionsPanel.getLine(LINE_AUTO_ADVANCE);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_TITLE,false);
						col++;
					}
					// previous button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_PREVIOUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiSizeTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setAutoAdvance();
						col++;
					}
					// next button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_NEXT,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
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
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiSizeTools.subPanelMargin - 2*iconWidth;
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
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}

				// #3 TOURNAMENT AUTO ADVANCE MODE
				{	Line ln = optionsPanel.getLine(LINE_TRMNT_AUTO_ADVANCE_MODE);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_TOURNAMENT_AUTO_ADVANCE_MODE_TITLE,false);
						col++;
					}
					// previous button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_TOURNAMENT_AUTO_ADVANCE_MODE_PREVIOUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiSizeTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setTournamentAutoAdvanceMode();
						col++;
					}
					// next button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_TOURNAMENT_AUTO_ADVANCE_MODE_NEXT,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #4 TOURNAMENT AUTO ADVANCE PACK
				{	Line ln = optionsPanel.getLine(LINE_TRMNT_AUTO_ADVANCE_PACK);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_TOURNAMENT_AUTO_ADVANCE_PACK_TITLE,false);
						col++;
					}
					// previous button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_TOURNAMENT_AUTO_ADVANCE_PACK_PREVIOUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiSizeTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setTournamentAutoAdvancePack();
						col++;
					}
					// next button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_TOURNAMENT_AUTO_ADVANCE_PACK_NEXT,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #5 HIDE ALL-AIS
				{	Line ln = optionsPanel.getLine(LINE_HIDE_ALL_AIS);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_HIDE_ALL_AIS_TITLE,false);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - GuiSizeTools.subPanelMargin;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setHideAllais();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #6 BOMB USELESS AIS
				{	Line ln = optionsPanel.getLine(LINE_BOMB_USELESS_AIS);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_BOMB_USELESS_AIS_TITLE,false);
						col++;
					}
					// minus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_BOMB_USELESS_AIS_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiSizeTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setBombUselessAis();
						col++;
					}
					// plus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_BOMB_USELESS_AIS_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #7 BOMB CYCLING AIS
				{	Line ln = optionsPanel.getLine(LINE_BOMB_CYCLING_AIS);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_BOMB_CYCLING_AIS_TITLE,false);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - GuiSizeTools.subPanelMargin;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setBombCyclingAis();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #8 DISPLAY EXCEPTIONS
				{	Line ln = optionsPanel.getLine(LINE_DISPLAY_EXCEPTIONS);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_DISPLAY_EXCEPTIONS_TITLE,false);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - GuiSizeTools.subPanelMargin;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setDisplayExceptions();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #9 LOG EXCEPTIONS
				{	Line ln = optionsPanel.getLine(LINE_LOG_EXCEPTIONS);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_LOG_EXCEPTIONS_TITLE,false);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - GuiSizeTools.subPanelMargin;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setLogExceptions();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #10 RECORD STATS
				{	Line ln = optionsPanel.getLine(LINE_RECORD_STATS);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_AIS_LINE_RECORD_STATISTICS_TITLE,false);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - GuiSizeTools.subPanelMargin;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setRecordStats();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// EMPTY
				{	for(int line=LINE_RECORD_STATS+1;line<LINE_COUNT;line++)
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
	
	/**
	 * Init the AI pack list depending on available files and folders.
	 */
	private void initPackList()
	{	TreeSet<String> temp = new TreeSet<String>();
		RankingService rankingService = GameStatistics.getRankingService();
		Set<String> playerIds = rankingService.getPlayers();
		Iterator<String> it = playerIds.iterator();
		while(it.hasNext())
		{	String playerId = it.next();
			try
			{	Profile profile = ProfileLoader.loadProfile(playerId);
				String pack = profile.getAiPackname();
				if(pack!=null)
					temp.add(pack);
			}
			catch (IllegalArgumentException e)
			{	e.printStackTrace();
			}
			catch (SecurityException e)
			{	e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{	e.printStackTrace();
			}
			catch (NoSuchFieldException e)
			{	e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{	e.printStackTrace();
			}
			catch (ParserConfigurationException e)
			{	e.printStackTrace();
			}
			catch (SAXException e)
			{	e.printStackTrace();
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
		}
		
		NavigableSet<String> temp2 = temp.descendingSet();
		availablePacks = new ArrayList<String>(temp2);
	}
	
	/**
	 * Updates the auto-advance option.
	 */
	private void setAutoAdvance()
	{	AutoAdvance autoAdvance = aisConfiguration.getAutoAdvance();
		String aaStr = autoAdvance.toString();
		String key = GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE+aaStr.toUpperCase().substring(0,1)+aaStr.toLowerCase().substring(1,aaStr.length());
		optionsPanel.getLine(LINE_AUTO_ADVANCE).setLabelKey(2,key,false);
	}
	
	/**
	 * Updates the auto-advance delay.
	 */
	private void setAutoAdvanceDelay()
	{	long speed = aisConfiguration.getAutoAdvanceDelay();
		String text = Long.toString(speed/1000);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_TITLE+GuiKeys.TOOLTIP); 
		optionsPanel.getLine(LINE_AUTO_ADVANCE_DELAY).setLabelText(2,text,tooltip);
	}
	
	/**
	 * Updates the tournament auto-advance mode.
	 */
	private void setTournamentAutoAdvanceMode()
	{	TournamentAutoAdvance tournamentAutoAdvance = aisConfiguration.getTournamentAutoAdvanceMode();
		String taaStr = tournamentAutoAdvance.toString();
		String key = GuiKeys.MENU_OPTIONS_AIS_LINE_TOURNAMENT_AUTO_ADVANCE_MODE+taaStr.toUpperCase().substring(0,1)+taaStr.toLowerCase().substring(1,taaStr.length());
		optionsPanel.getLine(LINE_TRMNT_AUTO_ADVANCE_MODE).setLabelKey(2,key,false);
	}
	
	/**
	 * Updates the tournament auto-advance pack.
	 */
	private void setTournamentAutoAdvancePack()
	{	String pack = aisConfiguration.getTournamentAutoAdvancePack();
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_AIS_LINE_TOURNAMENT_AUTO_ADVANCE_PACK_TITLE+GuiKeys.TOOLTIP);
		optionsPanel.getLine(LINE_TRMNT_AUTO_ADVANCE_PACK).setLabelText(2,pack,tooltip);
	}
	
	/**
	 * Updates the UPS option.
	 */
	private void setUps()
	{	int ups = aisConfiguration.getAiUps();
		String text = Integer.toString(ups);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_AIS_LINE_UPS_TITLE+GuiKeys.TOOLTIP); 
		optionsPanel.getLine(LINE_UPS).setLabelText(2,text,tooltip);
	}
	
	/**
	 * Updates the options controlling the display of all-ai rounds.
	 */
	private void setHideAllais()
	{	boolean hideAllais = aisConfiguration.getHideAllAis();
		String key;
		if(hideAllais)
			key = GuiKeys.MENU_OPTIONS_AIS_LINE_HIDE_ALL_AIS_ENABLED;
		else
			key = GuiKeys.MENU_OPTIONS_AIS_LINE_HIDE_ALL_AIS_DISABLED;
		optionsPanel.getLine(LINE_HIDE_ALL_AIS).setLabelKey(1,key,true);
	}
	
	/**
	 * Updates the option controlling the threatening of idle agents.
	 */
	private void setBombUselessAis()
	{	long buais = aisConfiguration.getBombUselessAis();
		String text,tooltip;
		if(buais<=0)
		{	text = new Character('\u221E').toString();
			tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_AIS_LINE_BOMB_USELESS_AIS_DISABLED+GuiKeys.TOOLTIP);			
		}
		else
		{	text = Long.toString(buais);
			tooltip = text+" ms";
		}
		optionsPanel.getLine(LINE_BOMB_USELESS_AIS).setLabelText(2,text,tooltip);
	}
	
	/**
	 * Updates the option controlling the threatening of cycling agents.
	 */
	private void setBombCyclingAis()
	{	boolean bcais = aisConfiguration.getBombCyclingAis();
		String key;
		if(bcais)
			key = GuiKeys.MENU_OPTIONS_AIS_LINE_BOMB_CYCLING_AIS_ENABLED;
		else
			key = GuiKeys.MENU_OPTIONS_AIS_LINE_BOMB_CYCLING_AIS_DISABLED;
		optionsPanel.getLine(LINE_BOMB_CYCLING_AIS).setLabelKey(1,key,true);
	}
	
	/**
	 * 	Updates the option displaying AI exceptions in the console.
	 */
	private void setDisplayExceptions()
	{	boolean displayExceptions = aisConfiguration.getDisplayExceptions();
		String key;
		if(displayExceptions)
			key = GuiKeys.MENU_OPTIONS_AIS_LINE_DISPLAY_EXCEPTIONS_ENABLED;
		else
			key = GuiKeys.MENU_OPTIONS_AIS_LINE_DISPLAY_EXCEPTIONS_DISABLED;
		optionsPanel.getLine(LINE_DISPLAY_EXCEPTIONS).setLabelKey(1,key,true);
	}
	
	/** 
	 * Updates the option logging AI exceptions in a file.
	 */
	private void setLogExceptions()
	{	boolean logExceptions = aisConfiguration.getLogExceptions();
		String key;
		if(logExceptions)
			key = GuiKeys.MENU_OPTIONS_AIS_LINE_LOG_EXCEPTIONS_ENABLED;
		else
			key = GuiKeys.MENU_OPTIONS_AIS_LINE_LOG_EXCEPTIONS_DISABLED;
		optionsPanel.getLine(LINE_LOG_EXCEPTIONS).setLabelKey(1,key,true);
	}
	
	/** 
	 * Updates the option allowing to record tournament stats as separate text files
	 */
	private void setRecordStats()
	{	boolean recordStats = aisConfiguration.getRecordStats();
		String key;
		if(recordStats)
			key = GuiKeys.MENU_OPTIONS_AIS_LINE_RECORD_STATISTICS_ENABLED;
		else
			key = GuiKeys.MENU_OPTIONS_AIS_LINE_RECORD_STATISTICS_DISABLED;
		optionsPanel.getLine(LINE_RECORD_STATS).setLabelKey(1,key,true);
	}
	
	@Override
	public void refresh()
	{	// nothing to do here
	}

	/**
	 * Returns the configuration object associated to this panel.
	 * 
	 * @return
	 * 		Ai-related configuration object.
	 */
	public AisConfiguration getAisConfiguration()
	{	return aisConfiguration;
	}	
	
	/////////////////////////////////////////////////////////////////
	// MOUSE LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void mouseClicked(MouseEvent e)
	{	//
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{	//
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{	//
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
				AutoAdvance autoAdvance = aisConfiguration.getAutoAdvance();
				// previous
				if(pos[1]==1)
				{	autoAdvance = autoAdvance.getPrevious();
				}
				// next
				else //if(pos[1]==3)
				{	autoAdvance = autoAdvance.getNext();
				}
				// common
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
			// tournament auto advance mode
			case LINE_TRMNT_AUTO_ADVANCE_MODE:
				TournamentAutoAdvance tournamentAutoAdvance = aisConfiguration.getTournamentAutoAdvanceMode();
				// previous
				if(pos[1]==1)
				{	tournamentAutoAdvance = tournamentAutoAdvance.getPrevious();
				}
				// next
				else //if(pos[1]==3)
				{	tournamentAutoAdvance = tournamentAutoAdvance.getNext();
				}
				// common
				aisConfiguration.setTournamentAutoAdvanceMode(tournamentAutoAdvance);
				setTournamentAutoAdvanceMode();
				break;
			// tournament auto advance pack
			case LINE_TRMNT_AUTO_ADVANCE_PACK:
				String pack = aisConfiguration.getTournamentAutoAdvancePack();
				int index = availablePacks.indexOf(pack);
				// previous
				if(pos[1]==1)
				{	index = (index+1)%availablePacks.size();
				}
				// next
				else //if(pos[1]==3)
				{	index = (index-1+availablePacks.size())%availablePacks.size();
				}
				// common
				pack = availablePacks.get(index);
				aisConfiguration.setTournamentAutoAdvancePack(pack);
				setTournamentAutoAdvancePack();
				break;
			// hide all-ais
			case LINE_HIDE_ALL_AIS:
				boolean hideAllais = !aisConfiguration.getHideAllAis();
				aisConfiguration.setHideAllAis(hideAllais);
				setHideAllais();
				break;
			// BOMB USELESS AIS
			case LINE_BOMB_USELESS_AIS:
				long buais = aisConfiguration.getBombUselessAis();
				// minus
				if(pos[1]==1)
				{	if(buais>=1000)
						buais = buais - 1000;
				}
				// plus
				else //if(pos[1]==3)
				{	buais = buais + 1000;
				}
				// common
				buais = 1000*(buais/1000);
				aisConfiguration.setBombUselessAis(buais);
				setBombUselessAis();
				break;
			// display exceptions
			case LINE_BOMB_CYCLING_AIS:
				boolean bcais = !aisConfiguration.getBombCyclingAis();
				aisConfiguration.setBombCyclingAis(bcais);
				setBombCyclingAis();
				break;
			// display exceptions
			case LINE_DISPLAY_EXCEPTIONS:
				boolean displayExceptions = !aisConfiguration.getDisplayExceptions();
				aisConfiguration.setDisplayExceptions(displayExceptions);
				setDisplayExceptions();
				break;
			// log exceptions
			case LINE_LOG_EXCEPTIONS:
				boolean logExceptions = !aisConfiguration.getLogExceptions();
				aisConfiguration.setLogExceptions(logExceptions);
				setLogExceptions();
				break;
			// record stats
			case LINE_RECORD_STATS:
				boolean recordStats = !aisConfiguration.getRecordStats();
				aisConfiguration.setRecordStats(recordStats);
				setRecordStats();
				break;
		}

	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	//
	}
}
