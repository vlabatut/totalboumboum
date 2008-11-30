package fr.free.totalboumboum.gui.game.tournament.results;

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

import javax.swing.JLabel;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Portraits;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.statistics.Score;
import fr.free.totalboumboum.game.statistics.StatisticMatch;
import fr.free.totalboumboum.game.statistics.StatisticTournament;
import fr.free.totalboumboum.game.tournament.sequence.SequenceTournament;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class SequenceResults extends TournamentResults
{	
	private static final long serialVersionUID = 1L;

	private UntitledSubPanelTable resultsPanel;
	
	public SequenceResults(SplitMenuPanel container)
	{	super(container);
		
		// data
		{	int lines = 16+1;
			int cols = 2+4+2;
			resultsPanel = new UntitledSubPanelTable(dataWidth,dataHeight,cols,lines,true);

			// headers
			{	{	JLabel lbl = resultsPanel.getLabel(0,0);
					lbl.setOpaque(false);
				}
				String[] keys = 
				{	GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_NAME,
					GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_BOMBS,
					GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_ITEMS,
					GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_BOMBEDS,
					GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_BOMBINGS,
					GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_TOTAL,
					GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_POINTS
				};
				for(int col=1;col<keys.length+1;col++)
					resultsPanel.setLabelKey(0,col,keys[col-1],true);
			}
			// data
			{	resultsPanel.setSubColumnsMaxWidth(1,Integer.MAX_VALUE);
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
		SequenceTournament tournament = (SequenceTournament)Configuration.getGameConfiguration().getTournament();
		ArrayList<Profile> players = tournament.getProfiles();
		StatisticTournament stats = tournament.getStats();
		ArrayList<StatisticMatch> matches = stats.getStatisticMatches();
		float[] partialPoints = stats.getTotal();
		float[] points = stats.getPoints();
		
		// sorting players according to points/partial points
		int[] orderedPlayers = tournament.getOrderedPlayers();
		
		// completing missing labels
		int col = 2+4+(matches.size()-1);
		int cols = 2+4+matches.size()+2;
		if(resultsPanel.getColumnCount()<cols)
		{	resultsPanel.addColumn(col);
			String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_MATCH)+matches.size();
			String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_MATCH+GuiKeys.TOOLTIP)+matches.size();
			resultsPanel.setLabelText(0,col,text,tooltip);
		}
		
		// display the ranking
		int line = 0;
		for(int i=0;i<orderedPlayers.length;i++)
		{	// init
			col = 0;
			line++;
			Profile profile = players.get(orderedPlayers[i]);
			// color
			Color clr = profile.getSpriteSelectedColor().getColor();
			// portrait
			{	BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
				String tooltip = profile.getSpriteName();
				resultsPanel.setLabelIcon(line,col,image,tooltip);
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				resultsPanel.setLabelBackground(line,col,bg);			
				col++;
			}
			// name
			{	String text = profile.getName();
				String tooltip = profile.getName();
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
				{	nf.format(stats.getScores(Score.BOMBS)[orderedPlayers[i]]),
					nf.format(stats.getScores(Score.ITEMS)[orderedPlayers[i]]),
					nf.format(stats.getScores(Score.BOMBEDS)[orderedPlayers[i]]),
					nf.format(stats.getScores(Score.BOMBINGS)[orderedPlayers[i]]),
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
			// matches
			Iterator<StatisticMatch> m = matches.iterator();
			while(m.hasNext())
			{	StatisticMatch statMatch = m.next();
				float pts = statMatch.getPoints()[orderedPlayers[i]];
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
			{	float pts = partialPoints[orderedPlayers[i]];
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				String text = nf.format(pts);
				String tooltip = nf.format(pts);
				resultsPanel.setLabelText(line,col,text,tooltip);
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),180);
				resultsPanel.setLabelBackground(line,col,bg);			
				col++;
			}
			// points
			{	String text = "-";
				String tooltip = "-";
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				if(tournament.isOver())	
				{	float pts = points[orderedPlayers[i]];
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
