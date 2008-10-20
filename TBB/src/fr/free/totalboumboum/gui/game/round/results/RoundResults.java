package fr.free.totalboumboum.gui.game.round.results;

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

import fr.free.totalboumboum.configuration.profile.Portraits;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.points.PlayerPoints;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.statistics.Score;
import fr.free.totalboumboum.game.statistics.StatisticRound;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.StringTools;

public class RoundResults extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	private UntitledSubPanelTable resultsPanel;
	
	public RoundResults(SplitMenuPanel container)
	{	super(container);

		// title
		String key = GuiTools.GAME_ROUND_RESULTS_TITLE;
		setTitleKey(key);
		
		// data
		{	int lines = 16+1;
			int cols = 2+5+1;			
			resultsPanel = new UntitledSubPanelTable(dataWidth,dataHeight,cols,lines,true);

			// headers
			{	{	JLabel lbl = resultsPanel.getLabel(0,0);
					lbl.setOpaque(false);
				}				
				Round round = getConfiguration().getCurrentRound();
				String sc = null;
				switch(round.getPlayMode())
				{	case CROWN:
						sc = GuiTools.GAME_ROUND_RESULTS_HEADER_CROWNS;
						break;
					case PAINT:
						sc = GuiTools.GAME_ROUND_RESULTS_HEADER_PAINTINGS;
						break;
					case SURVIVAL:
						sc = GuiTools.GAME_ROUND_RESULTS_HEADER_TIME;
						break;
				}
				String keys[] = 
				{	GuiTools.GAME_ROUND_RESULTS_HEADER_NAME,
					GuiTools.GAME_ROUND_RESULTS_HEADER_BOMBS,
					GuiTools.GAME_ROUND_RESULTS_HEADER_ITEMS,
					GuiTools.GAME_ROUND_RESULTS_HEADER_DEATHS,
					GuiTools.GAME_ROUND_RESULTS_HEADER_KILLS,
					sc,
					GuiTools.GAME_ROUND_RESULTS_HEADER_POINTS
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
		Round round = getConfiguration().getCurrentRound();
		StatisticRound stats = round.getStats();
		ArrayList<Profile> players = round.getProfiles();
		TreeSet<PlayerPoints> ranking = stats.getOrderedPlayers();
		float[] points = stats.getPoints();

		// display the ranking
		Iterator<PlayerPoints> i = ranking.descendingIterator();
		int col = 0;
		int line = 0;
		while(i.hasNext())
		{	// init
			col = 0;
			line++;
			PlayerPoints pp = i.next();
			Profile profile = players.get(pp.getIndex());
			// color
			Color clr = profile.getSpriteColor().getColor();
			// portrait
			{	BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
				String tooltip = pp.getPlayer();
				resultsPanel.setLabelIcon(line,col,image,tooltip);
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				resultsPanel.setLabelBackground(line,col,bg);			
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
				String sc = "";
				switch(round.getPlayMode())
				{	case CROWN:
						sc = nf.format(stats.getScores(Score.CROWNS)[pp.getIndex()]);
						break;
					case PAINT:
						sc = nf.format(stats.getScores(Score.PAINTINGS)[pp.getIndex()]);
						break;
					case SURVIVAL:
						sc = StringTools.formatTimeWithSeconds(stats.getScores(Score.TIME)[pp.getIndex()]);
						break;
				}
				String[] scores = 
				{	nf.format(stats.getScores(Score.BOMBS)[pp.getIndex()]),
					nf.format(stats.getScores(Score.ITEMS)[pp.getIndex()]),
					nf.format(stats.getScores(Score.DEATHS)[pp.getIndex()]),
					nf.format(stats.getScores(Score.KILLS)[pp.getIndex()]),
					sc
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
			// points
			{	double pts = points[pp.getIndex()];
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				String text = nf.format(pts);
				String tooltip = nf.format(pts);
				resultsPanel.setLabelText(line,col,text,tooltip);
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				resultsPanel.setLabelBackground(line,col,bg);			
				col++;;
			}
		}
	}
}
