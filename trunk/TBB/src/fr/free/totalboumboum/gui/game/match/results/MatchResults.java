package fr.free.totalboumboum.gui.game.match.results;

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
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JLabel;

import fr.free.totalboumboum.data.profile.Portraits;
import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.data.statistics.Score;
import fr.free.totalboumboum.data.statistics.StatisticMatch;
import fr.free.totalboumboum.data.statistics.StatisticRound;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.points.PlayerPoints;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class MatchResults extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	private UntitledSubPanelTable resultsPanel;
	
	public MatchResults(SplitMenuPanel container)
	{	super(container);

		// title
		String key = GuiTools.GAME_MATCH_RESULTS_TITLE;
		setTitleKey(key);
		
		// data
		{	int lines = 16+1;
			int cols = 2+4+2;
			resultsPanel = new UntitledSubPanelTable(dataWidth,dataHeight,cols,lines,true);
			
			// headers
			{	{	JLabel lbl = resultsPanel.getLabel(0,0);
					lbl.setOpaque(false);
				}
				String keys[] = 
				{	GuiTools.GAME_MATCH_RESULTS_HEADER_NAME,
					GuiTools.GAME_MATCH_RESULTS_HEADER_BOMBS,
					GuiTools.GAME_MATCH_RESULTS_HEADER_ITEMS,
					GuiTools.GAME_MATCH_RESULTS_HEADER_DEATHS,
					GuiTools.GAME_MATCH_RESULTS_HEADER_KILLS,
					GuiTools.GAME_MATCH_RESULTS_HEADER_TOTAL,
					GuiTools.GAME_MATCH_RESULTS_HEADER_POINTS
				};
				for(int col=1;col<keys.length+1;col++)
					resultsPanel.setLabelKey(0,col,keys[col-1],true);
			}
			// data
			{	resultsPanel.setSubColumnsWidth(1,Integer.MAX_VALUE);
			}
			//
			setDataPart(resultsPanel);
			updateData();
		}
	}

	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
	{	// init
		Match match = getConfiguration().getCurrentMatch();
		ArrayList<Profile> players = match.getProfiles();
		StatisticMatch stats = match.getStats();
		ArrayList<StatisticRound> rounds = stats.getStatRounds();
		float[] partialPoints = stats.getPartialPoints();
		float[] points = stats.getPoints();
		
		// sorting players according to points/partial points
		TreeSet<PlayerPoints> ranking = new TreeSet<PlayerPoints>();
		for(int i=0;i<points.length;i++)
		{	PlayerPoints pp = new PlayerPoints(players.get(i).getName(),i);
			pp.addPoint(points[i]);
			pp.addPoint(partialPoints[i]);
			ranking.add(pp);
		}
		
		// completing missing labels
		int col = 2+4+(rounds.size()-1);
		int cols = 2+4+rounds.size()+2;
		if(resultsPanel.getColumnCount()<cols)
		{	resultsPanel.addColumn(col);
			String text = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_RESULTS_HEADER_ROUND)+rounds.size();
			String tooltip = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_RESULTS_HEADER_ROUND+GuiTools.TOOLTIP)+rounds.size();
			resultsPanel.setLabelText(0,col,text,tooltip);
		}
		
		// display the ranking
		Iterator<PlayerPoints> i = ranking.descendingIterator();
		int line = 0;
		while(i.hasNext())
		{	// init
			col = 0;
			line++;
			PlayerPoints pp = i.next();
			Profile profile = players.get(pp.getIndex());
			// color
			Color clr = players.get(pp.getIndex()).getSpriteColor().getColor();
			// portrait
			{	BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
				String tooltip = pp.getPlayer();
				resultsPanel.setLabelIcon(line,col,image,tooltip);
				JLabel portraitLabel = resultsPanel.getLabel(line, col);
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				portraitLabel.setBackground(bg);
				col++;
			}
			// name
			{	String text = pp.getPlayer();
				String tooltip = pp.getPlayer();
				resultsPanel.setLabelText(line,col,text,tooltip);
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				resultsPanel.setLabelBackground(line,col,bg);			
				col++;
			}
			// scores
			{	NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				String[] scores = 
				{	nf.format(stats.getScores(Score.BOMBS)[pp.getIndex()]),
					nf.format(stats.getScores(Score.ITEMS)[pp.getIndex()]),
					nf.format(stats.getScores(Score.DEATHS)[pp.getIndex()]),
					nf.format(stats.getScores(Score.KILLS)[pp.getIndex()]),
				};
				for(int j=0;j<scores.length;j++)
				{	String text = scores[j];
					String tooltip = scores[j];
					resultsPanel.setLabelText(line,col,text,tooltip);
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					resultsPanel.setLabelBackground(line,col,bg);			
					col++;
				}
			}			
			// rounds
			Iterator<StatisticRound> r = rounds.iterator();
			while(r.hasNext())
			{	StatisticRound statRound = r.next();
				float pts = statRound.getPoints()[pp.getIndex()];
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				String text = nf.format(pts);
				String tooltip = nf.format(pts);
				resultsPanel.setLabelText(line,col,text,tooltip);
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL2;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				resultsPanel.setLabelBackground(line,col,bg);			
				col++;
			}
			// total
			{	float pts = partialPoints[pp.getIndex()];
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				String text = nf.format(pts);
				String tooltip = nf.format(pts);
				resultsPanel.setLabelText(line,col,text,tooltip);
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				resultsPanel.setLabelBackground(line,col,bg);			
				col++;
			}
			// points
			{	String text = "-";
				String tooltip = "-";
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				if(match.isOver())	
				{	float pts = points[pp.getIndex()];
					text = nf.format(pts);
					tooltip = nf.format(pts);					
				}
				resultsPanel.setLabelText(line,col,text,tooltip);
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				resultsPanel.setLabelBackground(line,col,bg);			
				col++;
			}
		}
	}
}
