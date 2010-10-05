package org.totalboumboum.gui.menus.options.statistics;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import org.totalboumboum.configuration.statistics.StatisticsConfiguration;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.LinesSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel.Mode;
import org.totalboumboum.gui.common.structure.subpanel.content.Line;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class StatisticsData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINE_COUNT = 20;
	private static final int LINE_INCLUDE_QUICKSTARTS = 0;
	private static final int LINE_INCLUDE_SIMULATIONS = 1;
	private static final int LINE_DEFAULT_RATING = 2;
	private static final int LINE_DEFAULT_RATING_DEVIATION = 3;
	private static final int LINE_DEFAULT_RATING_VOLATILITY = 4;
	private static final int LINE_GAMES_PER_PERIOD = 5;
	private static final int LINE_REINIT = 6;

	private LinesSubPanel optionsPanel;
	private StatisticsConfiguration statisticsConfiguration;
	
	public StatisticsData(SplitMenuPanel container)
	{	super(container);

		// title
		{	setTitleKey(GuiKeys.MENU_OPTIONS_STATISTICS_TITLE);
		}
	
		// data
		{	int w = getDataWidth();
			int h = getDataHeight();
			optionsPanel = new LinesSubPanel(w,h,Mode.BORDER,LINE_COUNT,1,false);
			int titleWidth = (int)(optionsPanel.getDataWidth()*0.66);
			int iconWidth = optionsPanel.getLineHeight();
			
			// data
			{	StatisticsConfiguration original = Configuration.getStatisticsConfiguration();
				original.setReinit(false);
				statisticsConfiguration = original.copy();;
				
				// #0 INCLUDE QUICKSTARTS
				{	Line ln = optionsPanel.getLine(LINE_INCLUDE_QUICKSTARTS);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_STATISTICS_LINE_INCLUDE_QUICKSTART_TITLE,false);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - GuiTools.subPanelMargin;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setIncludeQuickstarts();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #1 INCLUDE SIMULATIONS
				{	Line ln = optionsPanel.getLine(LINE_INCLUDE_SIMULATIONS);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_STATISTICS_LINE_INCLUDE_SIMULATION_TITLE,false);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - GuiTools.subPanelMargin;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setIncludeSimulations();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #2 DEFAULT RATING
				{	Line ln = optionsPanel.getLine(LINE_DEFAULT_RATING);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_TITLE,false);
						col++;
					}
					// minus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setDefaultRating();
						col++;
					}
					// plus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}

				// #3 DEFAULT RATING DEVIATION
				{	Line ln = optionsPanel.getLine(LINE_DEFAULT_RATING_DEVIATION);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_DEVIATION_TITLE,false);
						col++;
					}
					// minus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_DEVIATION_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setDefaultRatingDeviation();
						col++;
					}
					// plus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_DEVIATION_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}

				// #4 DEFAULT RATING VOLATILITY
				{	Line ln = optionsPanel.getLine(LINE_DEFAULT_RATING_VOLATILITY);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_VOLATILITY_TITLE,false);
						col++;
					}
					// minus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_VOLATILITY_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setDefaultRatingVolatility();
						col++;
					}
					// plus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_VOLATILITY_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}

				// #5 GAMES PER PERIOD
				{	Line ln = optionsPanel.getLine(LINE_GAMES_PER_PERIOD);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_GAMES_PER_PERIOD_TITLE,false);
						col++;
					}
					// minus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_GAMES_PER_PERIOD_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setGamesPerPeriod();
						col++;
					}
					// plus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_GAMES_PER_PERIOD_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}

				// #0 REINIT STATS
				{	Line ln = optionsPanel.getLine(LINE_REINIT);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_STATISTICS_LINE_REINIT_TITLE,false);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - GuiTools.subPanelMargin;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setReinit();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// EMPTY
				{	for(int line=LINE_REINIT+1;line<LINE_COUNT;line++)
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
	
	private void setIncludeQuickstarts()
	{	boolean includeQuickstarts = statisticsConfiguration.getIncludeQuickStarts();
		String key;
		if(includeQuickstarts)
			key = GuiKeys.MENU_OPTIONS_STATISTICS_LINE_INCLUDE_QUICKSTART_ENABLED;
		else
			key = GuiKeys.MENU_OPTIONS_STATISTICS_LINE_INCLUDE_QUICKSTART_DISABLED;
		optionsPanel.getLine(LINE_INCLUDE_QUICKSTARTS).setLabelKey(1,key,true);
	}
	
	private void setIncludeSimulations()
	{	boolean includeSimulations = statisticsConfiguration.getIncludeSimulations();
		String key;
		if(includeSimulations)
			key = GuiKeys.MENU_OPTIONS_STATISTICS_LINE_INCLUDE_SIMULATION_ENABLED;
		else
			key = GuiKeys.MENU_OPTIONS_STATISTICS_LINE_INCLUDE_SIMULATION_DISABLED;
		optionsPanel.getLine(LINE_INCLUDE_SIMULATIONS).setLabelKey(1,key,true);
	}
	
	private void setDefaultRating()
	{	int defaultRating = statisticsConfiguration.getDefaultRating();
		String text = Integer.toString(defaultRating);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_TITLE+GuiKeys.TOOLTIP); 
		optionsPanel.getLine(LINE_DEFAULT_RATING).setLabelText(2,text,tooltip);
	}
	
	private void setDefaultRatingDeviation()
	{	int defaultRatingDeviation = statisticsConfiguration.getDefaultRatingDeviation();
		String text = Integer.toString(defaultRatingDeviation);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_DEVIATION_TITLE+GuiKeys.TOOLTIP); 
		optionsPanel.getLine(LINE_DEFAULT_RATING_DEVIATION).setLabelText(2,text,tooltip);
	}
	
	private void setDefaultRatingVolatility()
	{	float defaultRatingVolatility = statisticsConfiguration.getDefaultRatingVolatility();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		String text = nf.format(defaultRatingVolatility);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_VOLATILITY_TITLE+GuiKeys.TOOLTIP); 
		optionsPanel.getLine(LINE_DEFAULT_RATING_VOLATILITY).setLabelText(2,text,tooltip);
	}
	
	private void setGamesPerPeriod()
	{	int gamesPerPeriod = statisticsConfiguration.getGamesPerPeriod();
		String text = Integer.toString(gamesPerPeriod);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_GAMES_PER_PERIOD_TITLE+GuiKeys.TOOLTIP); 
		optionsPanel.getLine(LINE_GAMES_PER_PERIOD).setLabelText(2,text,tooltip);
	}
	
	private void setReinit()
	{	boolean reinit = statisticsConfiguration.getReinit();
		String key;
		if(reinit)
			key = GuiKeys.MENU_OPTIONS_STATISTICS_LINE_REINIT_DONE;
		else
			key = GuiKeys.MENU_OPTIONS_STATISTICS_LINE_REINIT_DO;
		optionsPanel.getLine(LINE_REINIT).setLabelKey(1,key,true);
	}
	
	@Override
	public void refresh()
	{	// nothing to do here
	}

	public StatisticsConfiguration getStatisticsConfiguration()
	{	return statisticsConfiguration;
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
		{	// include quickstarts
			case LINE_INCLUDE_QUICKSTARTS:
				boolean includeQuickstarts = !statisticsConfiguration.getIncludeQuickStarts();
				statisticsConfiguration.setIncludeQuickStarts(includeQuickstarts);
				setIncludeQuickstarts();
				break;
			// include simulations
			case LINE_INCLUDE_SIMULATIONS:
				boolean includeSimulations = !statisticsConfiguration.getIncludeSimulations();
				statisticsConfiguration.setIncludeSimulations(includeSimulations);
				setIncludeSimulations();
				break;
			// default rating
			case LINE_DEFAULT_RATING:
				int defaultRating = statisticsConfiguration.getDefaultRating();
				// minus
				if(pos[1]==1)
				{	if(defaultRating>=10)
						defaultRating = defaultRating - 10;
				}
				// plus
				else //if(pos[1]==3)
				{	if(defaultRating<10000)
						defaultRating = defaultRating + 10;
				}
				// common
				defaultRating = 10*(defaultRating/10);
				statisticsConfiguration.setDefaultRating(defaultRating);
				setDefaultRating();
				break;
			// default rating deviation
			case LINE_DEFAULT_RATING_DEVIATION:
				int defaultRatingDeviation = statisticsConfiguration.getDefaultRatingDeviation();
				// minus
				if(pos[1]==1)
				{	if(defaultRatingDeviation>10)
						defaultRatingDeviation = defaultRatingDeviation - 10;
				}
				// plus
				else //if(pos[1]==3)
				{	if(defaultRatingDeviation<10000)
						defaultRatingDeviation = defaultRatingDeviation + 10;
				}
				// common
				defaultRatingDeviation = 10*(defaultRatingDeviation/10);
				statisticsConfiguration.setDefaultRatingDeviation(defaultRatingDeviation);
				setDefaultRatingDeviation();
				break;
			// default rating volatility
			case LINE_DEFAULT_RATING_VOLATILITY:
				float defaultRatingVolatility = statisticsConfiguration.getDefaultRatingVolatility();
				// minus
				if(pos[1]==1)
				{	if(defaultRatingVolatility>0.01)
						defaultRatingVolatility = defaultRatingVolatility - 0.01f;
				}
				// plus
				else //if(pos[1]==3)
				{	if(defaultRatingVolatility<2)
						defaultRatingVolatility = defaultRatingVolatility + 0.01f;
				}
				// common
				//defaultRatingVolatility = 10*(defaultRatingVolatility/10);
				statisticsConfiguration.setDefaultRatingVolatility(defaultRatingVolatility);
				setDefaultRatingVolatility();
				break;
			// games per period
			case LINE_GAMES_PER_PERIOD:
				int gamesPerPeriod = statisticsConfiguration.getGamesPerPeriod();
				// minus
				if(pos[1]==1)
				{	if(gamesPerPeriod>1)
					gamesPerPeriod = gamesPerPeriod - 1;
				}
				// plus
				else //if(pos[1]==3)
				{	if(gamesPerPeriod<50)
					gamesPerPeriod = gamesPerPeriod + 1;
				}
				// common
				statisticsConfiguration.setGamesPerPeriod(gamesPerPeriod);
				setGamesPerPeriod();
				break;
			// reinit
			case LINE_REINIT:
				boolean reinit = !statisticsConfiguration.getReinit();
				statisticsConfiguration.setReinit(reinit);
				setReinit();
				break;
		}

	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
}
