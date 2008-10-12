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
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import fr.free.totalboumboum.data.profile.Portraits;
import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.data.statistics.Score;
import fr.free.totalboumboum.data.statistics.StatisticRound;
import fr.free.totalboumboum.game.points.PlayerPoints;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.common.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.TablePanel;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.StringTools;

public class RoundResults extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	private TablePanel resultsPanel;
	
	public RoundResults(SplitMenuPanel container, int w, int h)
	{	super(container,w,h);

		// title
		String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_RESULTS_TITLE);
		setTitle(txt);
		
		// data
		{	int lines = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_NUMBER)+1;
			int cols = 2+5+1;			
			int width = GuiTools.getSize(GuiTools.GAME_DATA_PANEL_WIDTH);
			int height = GuiTools.getSize(GuiTools.GAME_DATA_PANEL_HEIGHT); 
			resultsPanel = new TablePanel(width,height,cols,lines,true,getConfiguration());

			// headers
			{	{	JLabel lbl = resultsPanel.getLabel(0,0);
					lbl.setText(null);
					lbl.setPreferredSize(new Dimension(GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT),GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
					lbl.setMaximumSize(new Dimension(GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT),GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
					lbl.setMinimumSize(new Dimension(GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT),GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
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
				String names[] = 
				{	GuiTools.GAME_ROUND_RESULTS_HEADER_NAME,
					GuiTools.GAME_ROUND_RESULTS_HEADER_BOMBS,
					GuiTools.GAME_ROUND_RESULTS_HEADER_ITEMS,
					GuiTools.GAME_ROUND_RESULTS_HEADER_DEATHS,
					GuiTools.GAME_ROUND_RESULTS_HEADER_KILLS,
					sc,
					GuiTools.GAME_ROUND_RESULTS_HEADER_POINTS
				};
				for(int i=0;i<names.length;i++)
				{	BufferedImage image = GuiTools.getIcon(names[i]); 
					String tooltip = getConfiguration().getLanguage().getText(names[i]+GuiTools.TOOLTIP); 
					JLabel lbl = resultsPanel.getLabel(0,1+i);
					lbl.setText(null);
					lbl.setToolTipText(tooltip);
					int lineHeight = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT);
					double zoom = lineHeight/(double)image.getHeight();
					image = ImageTools.resize(image,zoom,true);
					ImageIcon icon = new ImageIcon(image);
					lbl.setIcon(icon);
				}
			}
			// data
			{	for(int i=1;i<lines;i++)
				{	// name
					{	JLabel lbl = resultsPanel.getLabel(i,1);
						Dimension dimension = new Dimension(Integer.MAX_VALUE,GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT));
						lbl.setMaximumSize(dimension);
						dimension = new Dimension(GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT),GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT));
						lbl.setMinimumSize(dimension);
					}
				}
			}
			//
			setDataPanel(resultsPanel);
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
		int lineHeight = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT);
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
			{	JLabel portraitLabel = resultsPanel.getLabel(line, col++);
				BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
				double zoom = lineHeight/(double)image.getHeight();
//				double zoom = portraitLabel.getSize().height/(double)image.getHeight();
//				if(zoom!=0)
					image = ImageTools.resize(image,zoom,true);
				ImageIcon icon = new ImageIcon(image);
				portraitLabel.setIcon(icon);
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				portraitLabel.setBackground(bg);			
				portraitLabel.setText("");
			}
			// name
			{	JLabel nameLabel = resultsPanel.getLabel(line, col++);
				nameLabel.setText(pp.getPlayer());
				nameLabel.setToolTipText(pp.getPlayer());
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				nameLabel.setBackground(bg);
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
				{	JLabel pointsLabel = resultsPanel.getLabel(line, col++);
					pointsLabel.setText(scores[j]);
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					pointsLabel.setBackground(bg);
				}
			}			
			// points
			{	JLabel pointsLabel = resultsPanel.getLabel(line, col++);
				double pts = points[pp.getIndex()];
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				String txt = nf.format(pts);
				pointsLabel.setText(txt);
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				pointsLabel.setBackground(bg);
			}
		}
	}
}
