package org.totalboumboum.gui.common.content.subpanel.results;

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
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingConstants;

import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.profile.Portraits;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.detailed.StatisticBase;
import org.totalboumboum.statistics.detailed.StatisticHolder;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

/**
 * Panel used to display various game results (as a table).
 * All players are concerned by all columns (hence the "homogeneous"
 * in the class name). Otherwise, use the {@link HeterogeneousResultsSubPanel}.
 * 
 * @author Vincent Labatut
 */
public class HomogeneousResultsSubPanel extends TableSubPanel
{	/** Class id */
	private static final long serialVersionUID = 1L;
	/** Number of lines */
	private static final int LINES = GameData.MAX_PROFILES_COUNT+1;
	/** Number of columns */
	private static final int COLS = 0;
	/** Gui key prefix */
	private String prefix;

	/**
	 * Builds a new panel.
	 * 
	 * @param width
	 * 		Width of the panel.
	 * @param height
	 * 		Height of the panel.
	 */
	public HomogeneousResultsSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,1,1,true);
		
		setStatisticHolder(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// STATISTIC HOLDER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Object containing the stats to be displayed */
	private StatisticHolder statisticHolder;

	/**
	 * Returns the object containing the stats.
	 * 
	 * @return
	 * 		Object containing the stats.
	 */
	public StatisticHolder getStatisticHolder()
	{	return statisticHolder;	
	}
	
	/**
	 * Changes the object containing the stats.
	 * 
	 * @param statisticHolder
	 * 		New object containing the stats.
	 */
	public void setStatisticHolder(StatisticHolder statisticHolder)
	{	this.statisticHolder = statisticHolder;
		int cols = COLS;
	
		if(statisticHolder!=null)
		{	if(showRank) 
				cols++;
			if(showPortrait) 
				cols++;
			if(showName) 
				cols++;
			if(showScores) 
				cols = cols+5;
			if(showTime) 
				cols++;
			int confrontationsCount = statisticHolder.getStats().getConfrontationCount();
			if(showConfrontations && !(statisticHolder instanceof Round))
				cols = cols+confrontationsCount;
			if(showTotal && !(statisticHolder instanceof Round))
				cols++;
			if(showPoints) 
				cols++;
			reinit(LINES,cols);
			
			// prefix
			String type = null;
			if(statisticHolder instanceof Match)
				type = GuiKeys.MATCH;
			else if(statisticHolder instanceof Round)
				type = GuiKeys.ROUND;
			else if(statisticHolder instanceof AbstractTournament)
				type = GuiKeys.TOURNAMENT;
			prefix = GuiKeys.COMMON_RESULTS+type;
				
			// col widths
			int headerHeight = getHeaderHeight();
			int portraitWidth = headerHeight;
			int nameWidth = headerHeight;
			int scoresWidth[] = {headerHeight,headerHeight,headerHeight,headerHeight,headerHeight};
			int timeWidth = headerHeight;
			int confrontationsWidth[] = new int[confrontationsCount];
			Arrays.fill(confrontationsWidth,headerHeight);
			int totalWidth = headerHeight;
			int pointsWidth = headerHeight;
			int rankWidth = headerHeight;
			
			// headers
			int col = 0;
			{	String headerPrefix = prefix+GuiKeys.HEADER;
				if(showRank)
				{	String key = headerPrefix+GuiKeys.RANK;
					setLabelKey(0,col,key,true);
					col++;
				}
				if(showPortrait) 
				{	String key = headerPrefix+GuiKeys.PORTRAIT;
					setLabelKey(0,col,key,true);
					col++;
				}
				if(showName)
				{	String key = headerPrefix+GuiKeys.NAME;
					setLabelKey(0,col,key,true);
					setColSubAlignment(col, SwingConstants.LEFT);
					setLabelAlignment(0, col, SwingConstants.CENTER);
					col++;
				}
				if(showScores)
				{	String keys[] = 
					{	headerPrefix+GuiKeys.BOMBS,
						headerPrefix+GuiKeys.ITEMS,
						headerPrefix+GuiKeys.BOMBEDS,
						headerPrefix+GuiKeys.SELF+GuiKeys.BOMBINGS,
						headerPrefix+GuiKeys.BOMBINGS
					};
					for(int c=0;c<keys.length;c++)
						setLabelKey(0,col+c,keys[c],true);
					col = col+keys.length;
				}
				if(showTime)
				{	String key = headerPrefix+GuiKeys.TIME;
					setLabelKey(0,col,key,true);
					col++;
				}
				if(showConfrontations && !(statisticHolder instanceof Round))
				{	String key = headerPrefix+GuiKeys.CONFRONTATION;
					String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
					String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
					for(int c=0;c<confrontationsCount;c++)
					{	String tempText = text+(c+1);
						String tempTooltip = tooltip+" "+(c+1);
						setLabelText(0,col+c,tempText,tempTooltip);
						int temp = GuiFontTools.getPixelWidth(getLineFontSize(),tempText); 
						if(temp>confrontationsWidth[c])
							confrontationsWidth[c] = temp;
					}
					col = col+confrontationsCount;
				}
				if(showTotal && !(statisticHolder instanceof Round))
				{	String key = headerPrefix+GuiKeys.TOTAL;
					setLabelKey(0,col,key,true);
					col++;
				}
				if(showPoints)
				{	String key = headerPrefix+GuiKeys.POINTS;
					setLabelKey(0,col,key,true);
					col++;
				}
			}
			
			// init
			StatisticBase stats = statisticHolder.getStats();
			List<StatisticBase> confrontationStats = stats.getConfrontationStats();
			List<Profile> players = statisticHolder.getProfiles();
			Ranks orderedPlayers = statisticHolder.getOrderedPlayers();
			List<Profile> absoluteList = orderedPlayers.getAbsoluteOrderList();
			float[] points = stats.getPoints();
			float[] partialPoints = stats.getTotal();
	
			// display the ranking
			col = 0;
			int line = 0;
			for(int i=0;i<points.length;i++)
			{	// init
				col = 0;
				line++;
				Profile profile = absoluteList.get(i);
				if(profile==null)
					profile = players.get(i);
				int profileIndex = players.indexOf(profile);
				// color
				PredefinedColor color = profile.getSpriteColor();
				Color clr = color.getColor();
				Color fg = color.getSecondaryColor();
				// rank
				if(showRank)
				{	int rank = orderedPlayers.getRankForProfile(profile);
					String text;
					if(rank>0)
						text = Integer.toString(rank);
					else
						text = "-";
					String tooltip = text;
					setLabelText(line,col,text,tooltip);
					int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					setLabelBackground(line,col,bg);			
					setLabelForeground(line,col,fg);			
					int temp = GuiFontTools.getPixelWidth(getLineFontSize(),text);
					if(temp>rankWidth)
						rankWidth = temp;
					col++;;
				}
				// portrait
				if(showPortrait)
				{	BufferedImage image = profile.getPortraits().getOffgamePortrait(Portraits.OUTGAME_HEAD);
					String tooltip = profile.getSpriteName();
					setLabelIcon(line,col,image,tooltip);
					int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					setLabelBackground(line,col,bg);			
					setLabelForeground(line,col,fg);			
					col++;
				}
				// name
				if(showName)
				{	String text = profile.getName();
					String tooltip = profile.getName();
					setLabelText(line,col,text,tooltip);
					int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					setLabelBackground(line,col,bg);			
					setLabelForeground(line,col,fg);			
					col++;
				}
				// scores
				if(showScores)
				{	NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(2);
					nf.setMinimumFractionDigits(0);
					String[] scores = 
					{	nf.format(stats.getScores(Score.BOMBS)[profileIndex]),
						nf.format(stats.getScores(Score.ITEMS)[profileIndex]),
						nf.format(stats.getScores(Score.BOMBEDS)[profileIndex]),
						nf.format(stats.getScores(Score.SELF_BOMBINGS)[profileIndex]),
						nf.format(stats.getScores(Score.BOMBINGS)[profileIndex]),
					};
					for(int j=0;j<scores.length;j++)
					{	String text = scores[j];
						String tooltip = scores[j];
						setLabelText(line,col,text,tooltip);
						int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
						Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
						setLabelBackground(line,col,bg);
						int temp = GuiFontTools.getPixelWidth(getLineFontSize(),text);
						if(temp>scoresWidth[j])
							scoresWidth[j] = temp;
						col++;
					}
				}			
				// time
				if(showTime)
				{	String text = TimeTools.formatTime(stats.getScores(Score.TIME)[profileIndex],TimeUnit.SECOND,TimeUnit.MILLISECOND,false);
					String tooltip = text;
					setLabelText(line,col,text,tooltip);
					int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					setLabelBackground(line,col,bg);			
					int temp = GuiFontTools.getPixelWidth(getLineFontSize(),text);
					if(temp>timeWidth)
						timeWidth = temp;
					col++;
				}
				// confrontations
				if(showConfrontations && !(statisticHolder instanceof Round))
				{	Iterator<StatisticBase> r = confrontationStats.iterator();
					int cfrt = 0;
					while(r.hasNext())
					{	StatisticBase statB = r.next();
						float pts = statB.getPoints()[profileIndex];
						NumberFormat nf = NumberFormat.getInstance();
						nf.setMaximumFractionDigits(2);
						nf.setMinimumFractionDigits(0);
						String text = nf.format(pts);
						String tooltip = nf.format(pts);
						setLabelText(line,col,text,tooltip);
						int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL2;
						Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
						setLabelBackground(line,col,bg);			
						int temp = GuiFontTools.getPixelWidth(getLineFontSize(),text);
						if(temp>confrontationsWidth[cfrt])
							confrontationsWidth[cfrt] = temp;
						cfrt++;
						col++;
					}
				}
				// total
				if(showTotal && !(statisticHolder instanceof Round))
				{	float pts = partialPoints[profileIndex];
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(2);
					nf.setMinimumFractionDigits(0);
					String text = nf.format(pts);
					String tooltip = nf.format(pts);
					setLabelText(line,col,text,tooltip);
					int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					setLabelBackground(line,col,bg);			
					setLabelForeground(line,col,fg);			
					int temp = GuiFontTools.getPixelWidth(getLineFontSize(),text);
					if(temp>totalWidth)
						totalWidth = temp;
					col++;
				}
				// points
				if(showPoints)
				{	double pts = points[profileIndex];
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(2);
					nf.setMinimumFractionDigits(0);
					String text = nf.format(pts);
					String tooltip = nf.format(pts);
					setLabelText(line,col,text,tooltip);
					int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					setLabelBackground(line,col,bg);			
					setLabelForeground(line,col,fg);			
					int temp = GuiFontTools.getPixelWidth(getLineFontSize(),text);
					if(temp>pointsWidth)
						pointsWidth = temp;
					col++;;
				}
			}
			
			// col widths
			nameWidth = getDataWidth() - (cols-1)*GuiSizeTools.subPanelMargin;
			col = 0;
			if(showRank) 
			{	setColSubMinWidth(col,rankWidth);
				setColSubPrefWidth(col,rankWidth);
				setColSubMaxWidth(col,rankWidth);
				nameWidth = nameWidth - rankWidth;
				col++;
			}
			if(showPortrait) 
			{	setColSubMinWidth(col,portraitWidth);
				setColSubPrefWidth(col,portraitWidth);
				setColSubMaxWidth(col,portraitWidth);
				nameWidth = nameWidth - portraitWidth;
				col++;
			}
			int colName = col;
			if(showName) 
				col++;
			if(showScores) 
			{	for(int i=0;i<scoresWidth.length;i++)
				{	setColSubMinWidth(col,scoresWidth[i]);
					setColSubPrefWidth(col,scoresWidth[i]);
					setColSubMaxWidth(col,scoresWidth[i]);
					nameWidth = nameWidth - scoresWidth[i];
					col++;
				}
			}
			if(showTime) 
			{	setColSubMinWidth(col,timeWidth);
				setColSubPrefWidth(col,timeWidth);
				setColSubMaxWidth(col,timeWidth);
				nameWidth = nameWidth - timeWidth;
				col++;
			}
			if(showConfrontations && !(statisticHolder instanceof Round))
			{	for(int i=0;i<confrontationsWidth.length;i++)
				{	setColSubMinWidth(col,confrontationsWidth[i]);
					setColSubPrefWidth(col,confrontationsWidth[i]);
					setColSubMaxWidth(col,confrontationsWidth[i]);
					nameWidth = nameWidth - confrontationsWidth[i];
					col++;
				}
			}
			if(showTotal && !(statisticHolder instanceof Round))
			{	setColSubMinWidth(col,totalWidth);
				setColSubPrefWidth(col,totalWidth);
				setColSubMaxWidth(col,totalWidth);
				nameWidth = nameWidth - totalWidth;
				col++;
			}
			if(showPoints) 
			{	setColSubMinWidth(col,pointsWidth);
				setColSubPrefWidth(col,pointsWidth);
				setColSubMaxWidth(col,pointsWidth);
				nameWidth = nameWidth - pointsWidth;
				col++;
			}
			if(showName) 
			{	setColSubMinWidth(colName,nameWidth);
				setColSubPrefWidth(colName,nameWidth);
				setColSubMaxWidth(colName,nameWidth);
			}
		}
		else
		{	reinit(1,LINES);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether player pictures should be displayed */
	private boolean showPortrait = true;
	/** Whether player names should be displayed */
	private boolean showName = true;
	/** Whether round numbers should be displayed */
	private boolean showConfrontations = true;
	/** Whether total points should be displayed */
	private boolean showTotal = true;
	/** Whether round points should be displayed */
	private boolean showPoints = true;
	/** Whether player ranks should be displayed */
	private boolean showRank= true;
	/** Whether scores should be displayed */
	private boolean showScores = true;
	/** Whether durations should be displayed */
	private boolean showTime = true;
	
	/**
	 * Enable/disable the displaying of player pictures.
	 * 
	 * @param showPortrait
	 * 		Whether player pictures should be displayed.
	 */
	public void setShowPortrait(boolean showPortrait)
	{	this.showPortrait = showPortrait;
		setStatisticHolder(statisticHolder);
	}

	/**
	 * Enable/disable the displaying of player names.
	 * 
	 * @param showName
	 * 		Whether player names should be displayed.
	 */
	public void setShowName(boolean showName)
	{	this.showName = showName;
		setStatisticHolder(statisticHolder);
	}

	/**
	 * Enable/disable the displaying of round numbers.
	 * 
	 * @param showConfrontations
	 * 		Whether round numbers should be displayed.
	 */
	public void setShowConfrontations(boolean showConfrontations)
	{	this.showConfrontations = showConfrontations;
		setStatisticHolder(statisticHolder);
	}

	/**
	 * Enable/disable the displaying of total points.
	 * 
	 * @param showTotal
	 * 		Whether total points should be displayed.
	 */
	public void setShowTotal(boolean showTotal)
	{	this.showTotal = showTotal;
		setStatisticHolder(statisticHolder);
	}

	/**
	 * Enable/disable the displaying of round points.
	 * 
	 * @param showPoints
	 * 		Whether total round should be displayed.
	 */
	public void setShowPoints(boolean showPoints)
	{	this.showPoints = showPoints;
		setStatisticHolder(statisticHolder);
	}

	/**
	 * Enable/disable the displaying of player ranks.
	 * 
	 * @param showRank
	 * 		Whether player ranks should be displayed.
	 */
	public void setShowRank(boolean showRank)
	{	this.showRank = showRank;
		setStatisticHolder(statisticHolder);
	}

	/**
	 * Enable/disable the displaying of scores.
	 * 
	 * @param showScores
	 * 		Whether scores should be displayed.
	 */
	public void setShowScores(boolean showScores)
	{	this.showScores = showScores;
		setStatisticHolder(statisticHolder);
	}

	/**
	 * Enable/disable the displaying of durations.
	 * 
	 * @param showTime
	 * 		Whether durations should be displayed.
	 */
	public void setShowTime(boolean showTime)
	{	this.showTime = showTime;
		setStatisticHolder(statisticHolder);
	}
}
