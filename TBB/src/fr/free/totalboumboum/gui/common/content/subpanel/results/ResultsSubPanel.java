package fr.free.totalboumboum.gui.common.content.subpanel.results;

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
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.configuration.profile.Portraits;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.rank.Ranks;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.statistics.Score;
import fr.free.totalboumboum.game.statistics.StatisticBase;
import fr.free.totalboumboum.game.statistics.StatisticHolder;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.gui.common.structure.subpanel.temp.fait.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.StringTools;

public class ResultsSubPanel extends UntitledSubPanelTable
{	private static final long serialVersionUID = 1L;
	

	private String prefix;

	public ResultsSubPanel(int width, int height)
	{	super(width,height,1,1,1,true);
		
		setStatisticHolder(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// STATISTIC HOLDER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StatisticHolder statisticHolder;

	public StatisticHolder getStatisticHolder()
	{	return statisticHolder;	
	}
	
	public void setStatisticHolder(StatisticHolder statisticHolder)
	{	this.statisticHolder = statisticHolder;
		
		// sizes
		int lines = 16+1;
		int cols = 0;
		
		if(statisticHolder!=null)
		{	if(showPortrait) 
				cols++;
			if(showName) 
				cols++;
			if(showScores) 
				cols = cols+4;
			if(showTime) 
				cols++;
			int confrontationsCount = statisticHolder.getStats().getConfrontationCount();
			if(showConfrontations && !(statisticHolder instanceof Round))
				cols = cols+confrontationsCount;
			if(showTotal && !(statisticHolder instanceof Round))
				cols++;
			if(showPoints) 
				cols++;
			reinit(cols,lines);
			
			// prefix
			String type = null;
			if(statisticHolder instanceof Match)
				type = GuiKeys.MATCH;
			else if(statisticHolder instanceof Round)
				type = GuiKeys.ROUND;
			else if(statisticHolder instanceof AbstractTournament)
				type = GuiKeys.TOURNAMENT;
			prefix = GuiKeys.COMMON_RESULTS+type;
				
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
						setLabelText(0,col+c,text+(c+1),tooltip);
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
			
			// data
			{	setColSubMaxWidth(1,Integer.MAX_VALUE);
				int w = getHeaderHeight();
				setColSubPreferredWidth(1,w);
			}
	
			StatisticBase stats = statisticHolder.getStats();
			ArrayList<StatisticBase> confrontationStats = stats.getConfrontationStats();
			ArrayList<Profile> players = statisticHolder.getProfiles();
			Ranks orderedPlayers = statisticHolder.getOrderedPlayers();
			float[] points = stats.getPoints();
			float[] partialPoints = stats.getTotal();
	
			// display the ranking
			col = 0;
			int line = 0;
			for(int i=0;i<points.length;i++)
			{	// init
				col = 0;
				line++;
				Profile profile = orderedPlayers.getProfileFromAbsoluteRank(i+1);
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
						nf.format(stats.getScores(Score.BOMBINGS)[profileIndex]),
					};
					for(int j=0;j<scores.length;j++)
					{	String text = scores[j];
						String tooltip = scores[j];
						setLabelText(line,col,text,tooltip);
						int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
						Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
						setLabelBackground(line,col,bg);			
						col++;
					}
				}			
				// time
				if(showTime)
				{	String text = StringTools.formatTimeWithSeconds(stats.getScores(Score.TIME)[profileIndex]);
					String tooltip = text;
					setLabelText(line,col,text,tooltip);
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					setLabelBackground(line,col,bg);			
					col++;
				}
				// confrontations
				if(showConfrontations && !(statisticHolder instanceof Round))
				{	Iterator<StatisticBase> r = confrontationStats.iterator();
					while(r.hasNext())
					{	StatisticBase statB = r.next();
						float pts = statB.getPoints()[profileIndex];
						NumberFormat nf = NumberFormat.getInstance();
						nf.setMaximumFractionDigits(2);
						nf.setMinimumFractionDigits(0);
						String text = nf.format(pts);
						String tooltip = nf.format(pts);
						setLabelText(line,col,text,tooltip);
						int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL2;
						Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
						setLabelBackground(line,col,bg);			
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
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					setLabelBackground(line,col,bg);			
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
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					setLabelBackground(line,col,bg);			
					col++;;
				}
			}	
		}
		else
		{	reinit(1,lines);
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
		setStatisticHolder(statisticHolder);
	}

	public void setShowName(boolean showName)
	{	this.showName = showName;
		setStatisticHolder(statisticHolder);
	}

	public void setShowConfrontations(boolean showConfrontations)
	{	this.showConfrontations = showConfrontations;
		setStatisticHolder(statisticHolder);
	}

	public void setShowTotal(boolean showTotal)
	{	this.showTotal = showTotal;
		setStatisticHolder(statisticHolder);
	}

	public void setShowPoints(boolean showPoints)
	{	this.showPoints = showPoints;
		setStatisticHolder(statisticHolder);
	}

	public void setShowScores(boolean showScores)
	{	this.showScores = showScores;
		setStatisticHolder(statisticHolder);
	}

	public void setShowTime(boolean showTime)
	{	this.showTime = showTime;
		setStatisticHolder(statisticHolder);
	}
}
