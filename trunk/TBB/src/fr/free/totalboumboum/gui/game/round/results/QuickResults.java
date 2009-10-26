package fr.free.totalboumboum.gui.game.round.results;

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
import java.awt.Dimension;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.rank.Ranks;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.SpringUtilities;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.statistics.detailed.Score;
import fr.free.totalboumboum.statistics.detailed.StatisticRound;
import fr.free.totalboumboum.tools.StringTools;
import fr.free.totalboumboum.tools.StringTools.TimeUnit;

public class QuickResults extends JPanel
{	
	private static final long serialVersionUID = 1L;

	private Font headerFont;
	private Font regularFont;
	private int columns = 0;
	private int lines = 0;
	private int tableMargin;
	
	public QuickResults(Dimension dimen, Round round)
	{	super();
		
		// init
		lines = 16+1;
		int cols = 1+5+1;			
		int width = dimen.width;
		int height = dimen.height;
		StatisticRound stats = round.getStats();
		ArrayList<Profile> players = round.getProfiles();
		Ranks orderedPlayers = round.getOrderedPlayers();
		float[] points = stats.getPoints();
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(CENTER_ALIGNMENT);
		setOpaque(false);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
			
		// layout
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
			
		// size
		int margin = (int)(width*0.025);
		tableMargin = (int)(margin*0.2);
		int pWidth = width-2*margin;
		int pHeight = height-2*margin;
		Dimension dim = new Dimension(pWidth,pHeight);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);

		// fonts
//		BufferedImage bi = new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB);
//		Graphics g = bi.getGraphics();
//		GuiTools.setGraphics(g);
		int headerHeight = (int)(1.5*pHeight/(lines+0.5));
		int headerSize = GuiTools.getFontSize(headerHeight);
		headerFont = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(headerSize*0.8f); 
		int lineHeight = (pHeight-headerHeight)/(lines-1);
		int lineSize = GuiTools.getFontSize(lineHeight);
		regularFont = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(lineSize*0.8f); 
		
		// table
		for(int col=0;col<cols;col++)
			addColumn(col);
		
		// headers
		{	String sc = null;
/* TODO à adapter
			switch(round.getPlayMode())
			{	case CROWN:
					sc = "Crowns";
					break;
				case PAINT:
					sc = "Paints";
					break;
				case SURVIVAL:
					sc = "Time";
					break;
			}
*/			
sc = "Time";
			String names[] = 
			{	"Player",
				"Bombs",
				"Items",
				"Bombeds",
				"Bombings",
				sc,
				"Points"
			};
			for(int i=0;i<names.length;i++)
			{	String text = names[i]; 
				JLabel lbl = getLabel(0,i);
				lbl.setText(text);
			}
		}
		// data
		{	int col = 0;
			int line = 0;
			for(int i=0;i<points.length;i++)
			{	// init
				col = 0;
				line++;
				Profile profile = orderedPlayers.getProfileFromAbsoluteRank(i+1);
				int profileIndex = players.indexOf(profile);
				// color
				Color clr = profile.getSpriteColor().getColor();
				// name
				{	JLabel nameLabel = getLabel(line, col++);
					String playerName = profile.getName();
					nameLabel.setText(playerName);
					nameLabel.setToolTipText(playerName);
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					nameLabel.setBackground(bg);
					Dimension dimension = new Dimension(Integer.MAX_VALUE,lineHeight);
					nameLabel.setMaximumSize(dimension);
					dimension = new Dimension(lineHeight,lineHeight);
					nameLabel.setMinimumSize(dimension);
				}
				// scores
				{	NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(2);
					nf.setMinimumFractionDigits(0);
					String sc = "";
/*	TODO à adapter				
					switch(round.getPlayMode())
					{	case CROWN:
							sc = nf.format(stats.getScores(Score.CROWNS)[orderedPlayers[i]]);
							break;
						case PAINT:
							sc = nf.format(stats.getScores(Score.PAINTINGS)[orderedPlayers[i]]);
							break;
						case SURVIVAL:
							sc = StringTools.formatTimeWithSeconds(stats.getScores(Score.TIME)[orderedPlayers[i]]);
							break;
					}
*/					
sc = StringTools.formatTime(stats.getScores(Score.TIME)[profileIndex],TimeUnit.SECOND,TimeUnit.MILLISECOND,false);
					String[] scores = 
					{	nf.format(stats.getScores(Score.BOMBS)[profileIndex]),
						nf.format(stats.getScores(Score.ITEMS)[profileIndex]),
						nf.format(stats.getScores(Score.BOMBEDS)[profileIndex]),
						nf.format(stats.getScores(Score.BOMBINGS)[profileIndex]),
						sc
					};
					for(int j=0;j<scores.length;j++)
					{	JLabel pointsLabel = getLabel(line, col++);
						pointsLabel.setText(scores[j]);
						int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
						Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
						pointsLabel.setBackground(bg);
					}
				}			
				// points
				{	JLabel pointsLabel = getLabel(line, col++);
					double pts = points[profileIndex];
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

	public void addColumn(int index)
	{	columns++;
		int start = 0;
	
		// header
		{	start = 1;
			String txt = null;
			JLabel lbl = new JLabel(txt);
			lbl.setFont(headerFont);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
			lbl.setForeground(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
			lbl.setOpaque(true);
			add(lbl,index);		
		}
		
		// data
		for(int line=0+start;line<lines;line++)
		{	String txt = null;
			JLabel lbl = new JLabel(txt);
			lbl.setFont(regularFont);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
			lbl.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
			lbl.setOpaque(true);
			add(lbl,index+line*columns);
		}
		
		// layout
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		SpringUtilities.makeCompactGrid(this,lines,columns,tableMargin,tableMargin,tableMargin,tableMargin);
	}

	public JLabel getLabel(int line, int col)
	{	JLabel result = (JLabel)getComponent(col+line*columns);;
		return result;
	}
}
