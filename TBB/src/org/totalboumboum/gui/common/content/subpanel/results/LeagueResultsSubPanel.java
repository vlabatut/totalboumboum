package org.totalboumboum.gui.common.content.subpanel.results;

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
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.List;

import org.totalboumboum.game.profile.Portraits;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.game.tournament.league.LeagueTournament;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.detailed.StatisticTournament;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class LeagueResultsSubPanel extends TableSubPanel
{	private static final long serialVersionUID = 1L;
	private static final int LINES = 16+1;
	private static final int COLS = 0;
	

	private String prefix;

	public LeagueResultsSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,1,1,true);
		
		setLeagueTournament(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// LEAGUE TOURNAMENT	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LeagueTournament leagueTournament;

	public LeagueTournament getLeagueTournament()
	{	return leagueTournament;	
	}
	
	public void setLeagueTournament(LeagueTournament leagueTournament)
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
			if(showConfrontations)
				cols = cols+4;
			if(showTotal)
				cols++;
			if(showPoints) 
				cols++;
			reinit(LINES,cols);
			
			// prefix
			prefix = GuiKeys.COMMON_RESULTS+GuiKeys.TOURNAMENT;
				
			// col widths
			int headerHeight = getHeaderHeight();
			int portraitWidth = headerHeight;
			int nameWidth = headerHeight;
			int scoresWidth[] = {headerHeight,headerHeight,headerHeight,headerHeight,headerHeight};
			int timeWidth = headerHeight;
			int confrontationsWidth[] = {headerHeight,headerHeight,headerHeight,headerHeight};
			int totalWidth = headerHeight;
			
			// headers
			int col = 0;
			{	String headerPrefix = prefix+GuiKeys.HEADER;
				if(showPortrait) 
				{	String key = headerPrefix+GuiKeys.PORTRAIT;
					setLabelKey(0,col,key,true);
					col++;
				}
				if(showName)
				{	String key = headerPrefix+GuiKeys.NAME;
					setLabelKey(0,col,key,true);
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
				if(showConfrontations)
				{	String keys[] = 
					{	headerPrefix+GuiKeys.PLAYED,
						headerPrefix+GuiKeys.WON,
						headerPrefix+GuiKeys.DRAWN,
						headerPrefix+GuiKeys.LOST
					};
					for(int c=0;c<keys.length;c++)
						setLabelKey(0,col+c,keys[c],true);
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
				Color clr = profile.getSpriteColor().getColor();
				// portrait
				if(showPortrait)
				{	BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
					String tooltip = profile.getSpriteName();
					setLabelIcon(line,col,image,tooltip);
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					setLabelBackground(line,col,bg);			
					col++;
				}
				// name
				if(showName)
				{	String text = profile.getName();
					String tooltip = profile.getName();
					setLabelText(line,col,text,tooltip);
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					setLabelBackground(line,col,bg);			
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
						int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
						Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
						setLabelBackground(line,col,bg);
						int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
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
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					setLabelBackground(line,col,bg);			
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>timeWidth)
						timeWidth = temp;
					col++;
				}
				// confrontations
				if(showConfrontations)
				{	String[] confs = 
					{	Integer.toString(stats.getPlayed()[profileIndex]),
						Integer.toString(stats.getWon()[profileIndex]),
						Integer.toString(stats.getDrawn()[profileIndex]),
						Integer.toString(stats.getLost()[profileIndex]),
					};
					for(int j=0;j<confs.length;j++)
					{	String text = confs[j];
						String tooltip = confs[j];
						setLabelText(line,col,text,tooltip);
						int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
						Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
						setLabelBackground(line,col,bg);
						int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
						if(temp>confrontationsWidth[j])
							confrontationsWidth[j] = temp;
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
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					setLabelBackground(line,col,bg);			
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>totalWidth)
						totalWidth = temp;
					col++;
				}
			}
			
			// col widths
			nameWidth = getDataWidth() - (cols-1)*GuiTools.subPanelMargin;
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
			if(showConfrontations)
			{	for(int i=0;i<confrontationsWidth.length;i++)
				{	setColSubMinWidth(col,confrontationsWidth[i]);
					setColSubPrefWidth(col,confrontationsWidth[i]);
					setColSubMaxWidth(col,confrontationsWidth[i]);
					nameWidth = nameWidth - confrontationsWidth[i];
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
	private boolean showPortrait = true;
	private boolean showName = true;
	private boolean showConfrontations = true;
	private boolean showTotal = true;
	private boolean showPoints = true;
	private boolean showScores = true;
	private boolean showTime = true;
	
	public void setShowPortrait(boolean showPortrait)
	{	this.showPortrait = showPortrait;
		setLeagueTournament(leagueTournament);
	}

	public void setShowName(boolean showName)
	{	this.showName = showName;
		setLeagueTournament(leagueTournament);
	}

	public void setShowConfrontations(boolean showConfrontations)
	{	this.showConfrontations = showConfrontations;
		setLeagueTournament(leagueTournament);
	}

	public void setShowTotal(boolean showTotal)
	{	this.showTotal = showTotal;
		setLeagueTournament(leagueTournament);
	}

	public void setShowPoints(boolean showPoints)
	{	this.showPoints = showPoints;
		setLeagueTournament(leagueTournament);
	}

	public void setShowScores(boolean showScores)
	{	this.showScores = showScores;
		setLeagueTournament(leagueTournament);
	}

	public void setShowTime(boolean showTime)
	{	this.showTime = showTime;
		setLeagueTournament(leagueTournament);
	}
}
