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
import java.util.List;

import javax.swing.SwingConstants;

import org.totalboumboum.game.profile.Portraits;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.detailed.StatisticTournament;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

/**
 * Panel displaying league tournament results.
 * 
 * @author Vincent Labatut
 */
public class HeterogeneousResultsSubPanel extends TableSubPanel
{	/** Class id */
	private static final long serialVersionUID = 1L;
	/** Number of lines */
	private static final int LINES = GameData.STANDARD_TILE_DIMENSION+1;
	/** Number of columns */
	private static final int COLS = 0;
	/** Gui key prefix */
	private static final String PREFIX = GuiKeys.COMMON_RESULTS+GuiKeys.TOURNAMENT;

	/**
	 * Builds a new panel.
	 * 
	 * @param width
	 * 		Width of the panel.
	 * @param height
	 * 		Height of the panel.
	 */
	public HeterogeneousResultsSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,1,1,true);
		
		setTournament(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// LEAGUE TOURNAMENT	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Tournament to be displayed */
	private AbstractTournament leagueTournament;

	/**
	 * Returns the current tournament.
	 * 
	 * @return
	 * 		League tournament.
	 */
	public AbstractTournament getTournament()
	{	return leagueTournament;	
	}
	
	/**
	 * Changes the current tournament.
	 * 
	 * @param leagueTournament
	 * 		New league tournament.
	 */
	public void setTournament(AbstractTournament leagueTournament)
	{	this.leagueTournament = leagueTournament;
		int cols = COLS;
	
		if(leagueTournament!=null)
		{	if(showPortrait) 
				cols++;
			if(showName) 
				cols++;
			if(showScores) 
				cols = cols+5;
			if(showTime) 
				cols++;
			if(showRanks || showConfrontations)
				cols++;
			if(showConfrontations)
				cols = cols+3;
			if(showRanks)
				cols = cols+5;
			if(showTotal)
				cols++;
			if(showPoints) 
				cols++;
			reinit(LINES,cols);
				
			// col widths
			int headerHeight = getHeaderHeight();
			int portraitWidth = headerHeight;
			int nameWidth = headerHeight;
			int scoresWidth[] = {headerHeight,headerHeight,headerHeight,headerHeight,headerHeight};
			int timeWidth = headerHeight;
			int playedWidth = headerHeight;
			int confrontationsWidth[] = {headerHeight,headerHeight,headerHeight};
			int ranksWidth[] = {headerHeight,headerHeight,headerHeight,headerHeight,headerHeight};
			int totalWidth = headerHeight;
			
			// headers
			int col = 0;
			{	String headerPrefix = PREFIX+GuiKeys.HEADER;
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
				if(showConfrontations || showRanks)
				{	String key = headerPrefix+GuiKeys.PLAYED;
					setLabelKey(0,col,key,true);
					col++;
				}
				if(showConfrontations)
				{	String keys[] = 
					{	headerPrefix+GuiKeys.WON,
						headerPrefix+GuiKeys.DRAWN,
						headerPrefix+GuiKeys.LOST
					};
					for(int c=0;c<keys.length;c++)
						setLabelKey(0,col+c,keys[c],true);
					col = col+keys.length;
				}
				if(showRanks)
				{	String keys[] = 
					{	headerPrefix+GuiKeys.RANKS+GuiKeys.FIRST,
						headerPrefix+GuiKeys.RANKS+GuiKeys.SECOND,
						headerPrefix+GuiKeys.RANKS+GuiKeys.THIRD,
						headerPrefix+GuiKeys.RANKS+GuiKeys.FOURTH,
						headerPrefix+GuiKeys.RANKS+GuiKeys.MORE
					};
					for(int c=0;c<keys.length;c++)
						setLabelKey(0,col+c,keys[c],false);
					col = col+keys.length;
				}
				if(showTotal)
				{	String key = headerPrefix+GuiKeys.TOTAL;
					setLabelKey(0,col,key,true);
					col++;
				}
			}
			
			// init
			StatisticTournament stats = leagueTournament.getStats();
			//ArrayList<StatisticBase> confrontationStats = stats.getConfrontationStats();
			List<Profile> players = leagueTournament.getProfiles();
			Ranks orderedPlayers = leagueTournament.getOrderedPlayers();
			List<Profile> absoluteList = orderedPlayers.getAbsoluteOrderList();
			float[] total = stats.getTotal();
			int[] played = stats.getPlayedCounts();
			int[][] confs = stats.getConfrontationCounts();
			int[][] ranks = stats.getRankCounts();
	
			// display the ranking
			col = 0;
			int line = 0;
			for(int i=0;i<total.length;i++)
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
				// played
				if(showConfrontations || showRanks)
				{	String text = Integer.toString(played[profileIndex]);
					String tooltip = text;
					setLabelText(line,col,text,tooltip);
					int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					setLabelBackground(line,col,bg);			
					int temp = GuiFontTools.getPixelWidth(getLineFontSize(),text);
					if(temp>playedWidth)
						playedWidth = temp;
					col++;
				}
				// confrontations
				if(showConfrontations)
				{	String[] vals = 
					{	Integer.toString(confs[profileIndex][0]),
						Integer.toString(confs[profileIndex][1]),
						Integer.toString(confs[profileIndex][2])
					};
					for(int j=0;j<vals.length;j++)
					{	String text = vals[j];
						String tooltip = vals[j];
						setLabelText(line,col,text,tooltip);
						int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
						Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
						setLabelBackground(line,col,bg);
						int temp = GuiFontTools.getPixelWidth(getLineFontSize(),text);
						if(temp>confrontationsWidth[j])
							confrontationsWidth[j] = temp;
						col++;
					}
				}			
				// ranks
				if(showRanks)
				{	String[] vals = 
					{	Integer.toString(ranks[profileIndex][0]),
						Integer.toString(ranks[profileIndex][1]),
						Integer.toString(ranks[profileIndex][2]),
						Integer.toString(ranks[profileIndex][3]),
						Integer.toString(ranks[profileIndex][4])
					};
					for(int j=0;j<vals.length;j++)
					{	String text = vals[j];
						String tooltip = vals[j];
						setLabelText(line,col,text,tooltip);
						int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
						Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
						setLabelBackground(line,col,bg);
						int temp = GuiFontTools.getPixelWidth(getLineFontSize(),text);
						if(temp>ranksWidth[j])
							ranksWidth[j] = temp;
						col++;
					}
				}			
				// total
				if(showTotal)
				{	float pts = total[profileIndex];
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
			}
			
			// col widths
			nameWidth = getDataWidth() - (cols-1)*GuiSizeTools.subPanelMargin;
			col = 0;
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
			if(showConfrontations || showRanks) 
			{	setColSubMinWidth(col,playedWidth);
				setColSubPrefWidth(col,playedWidth);
				setColSubMaxWidth(col,playedWidth);
				nameWidth = nameWidth - playedWidth;
				col++;
			}
			if(showConfrontations)
			{	for(int i=0;i<confrontationsWidth.length;i++)
				{	setColSubMinWidth(col,confrontationsWidth[i]);
					setColSubPrefWidth(col,confrontationsWidth[i]);
					setColSubMaxWidth(col,confrontationsWidth[i]);
					nameWidth = nameWidth - confrontationsWidth[i];
					col++;
				}
			}
			if(showRanks)
			{	for(int i=0;i<ranksWidth.length;i++)
				{	setColSubMinWidth(col,ranksWidth[i]);
					setColSubPrefWidth(col,ranksWidth[i]);
					setColSubMaxWidth(col,ranksWidth[i]);
					nameWidth = nameWidth - ranksWidth[i];
					col++;
				}
			}
			if(showTotal)
			{	setColSubMinWidth(col,totalWidth);
				setColSubPrefWidth(col,totalWidth);
				setColSubMaxWidth(col,totalWidth);
				nameWidth = nameWidth - totalWidth;
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
	/** Whether confrontation numbers should be displayed */
	private boolean showConfrontations = false;
	/** Whether confrontation ranks should be displayed */
	private boolean showRanks = true;
	/** Whether total points should be displayed */
	private boolean showTotal = true;
	/** Whether round points should be displayed */
	private boolean showPoints = true;
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
		setTournament(leagueTournament);
	}

	/**
	 * Enable/disable the displaying of player names.
	 * 
	 * @param showName
	 * 		Whether player names should be displayed.
	 */
	public void setShowName(boolean showName)
	{	this.showName = showName;
		setTournament(leagueTournament);
	}

	/**
	 * Enable/disable the displaying of confrontation numbers
	 * (played, won, drawn, lost).
	 * 
	 * @param showConfrontations
	 * 		Whether confrontation numbers should be displayed.
	 */
	public void setShowConfrontations(boolean showConfrontations)
	{	this.showConfrontations = showConfrontations;
		setTournament(leagueTournament);
	}

	/**
	 * Enable/disable the displaying of confrontation ranks
	 * (played, 1st, 2nd, ...5th or more).
	 * 
	 * @param showRanks
	 * 		Whether confrontation ranks should be displayed.
	 */
	public void setShowRanks(boolean showRanks)
	{	this.showRanks = showRanks;
		setTournament(leagueTournament);
	}

	/**
	 * Enable/disable the displaying of total points.
	 * 
	 * @param showTotal
	 * 		Whether total points should be displayed.
	 */
	public void setShowTotal(boolean showTotal)
	{	this.showTotal = showTotal;
		setTournament(leagueTournament);
	}

	/**
	 * Enable/disable the displaying of round points.
	 * 
	 * @param showPoints
	 * 		Whether total round should be displayed.
	 */
	public void setShowPoints(boolean showPoints)
	{	this.showPoints = showPoints;
		setTournament(leagueTournament);
	}

	/**
	 * Enable/disable the displaying of scores.
	 * 
	 * @param showScores
	 * 		Whether scores should be displayed.
	 */
	public void setShowScores(boolean showScores)
	{	this.showScores = showScores;
		setTournament(leagueTournament);
	}

	/**
	 * Enable/disable the displaying of durations.
	 * 
	 * @param showTime
	 * 		Whether durations should be displayed.
	 */
	public void setShowTime(boolean showTime)
	{	this.showTime = showTime;
		setTournament(leagueTournament);
	}
}
