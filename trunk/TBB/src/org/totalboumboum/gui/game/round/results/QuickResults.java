package org.totalboumboum.gui.game.round.results;

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
import java.awt.Dimension;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.gui.tools.SpringUtilities;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

/**
 * 
 * @author Vincent Labatut
 *
 */
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
		List<Profile> players = round.getProfiles();
		Ranks orderedPlayers = round.getOrderedPlayers();
		float[] points = stats.getPoints();
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(CENTER_ALIGNMENT);
		setOpaque(false);
		
		// background
		setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);
			
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
		int headerSize = GuiFontTools.getFontSize(headerHeight);
		headerFont = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(headerSize*0.8f); 
		int lineHeight = (pHeight-headerHeight)/(lines-1);
		int lineSize = GuiFontTools.getFontSize(lineHeight);
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
				"Self-bombings",
				"Bombings",
				sc,
				"Points"
			};
			for(int i=0;i<names.length;i++)
			{	String text = names[i]; 
				MyLabel lbl = getLabel(0,i);
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
				List<Profile> absoluteList = orderedPlayers.getAbsoluteOrderList();
				Profile profile = absoluteList.get(i);
				int profileIndex = players.indexOf(profile);
				// color
				Color clr = profile.getSpriteColor().getColor();
				// name
				{	MyLabel nameLabel = getLabel(line, col++);
					String playerName = profile.getName();
					nameLabel.setText(playerName);
					nameLabel.setToolTipText(playerName);
					int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
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
sc = TimeTools.formatTime(stats.getScores(Score.TIME)[profileIndex],TimeUnit.SECOND,TimeUnit.MILLISECOND,false);
					String[] scores = 
					{	nf.format(stats.getScores(Score.BOMBS)[profileIndex]),
						nf.format(stats.getScores(Score.ITEMS)[profileIndex]),
						nf.format(stats.getScores(Score.BOMBEDS)[profileIndex]),
						nf.format(stats.getScores(Score.SELF_BOMBINGS)[profileIndex]),
						nf.format(stats.getScores(Score.BOMBINGS)[profileIndex]),
						sc
					};
					for(int j=0;j<scores.length;j++)
					{	MyLabel pointsLabel = getLabel(line, col++);
						pointsLabel.setText(scores[j]);
						int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
						Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
						pointsLabel.setBackground(bg);
					}
				}			
				// points
				{	MyLabel pointsLabel = getLabel(line, col++);
					double pts = points[profileIndex];
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(2);
					nf.setMinimumFractionDigits(0);
					String txt = nf.format(pts);
					pointsLabel.setText(txt);
					int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
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
			MyLabel lbl = new MyLabel(txt);
			lbl.setFont(headerFont);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setBackground(GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND);
			lbl.setForeground(GuiColorTools.COLOR_TABLE_HEADER_FOREGROUND);
			lbl.setOpaque(true);
			add(lbl,index);		
		}
		
		// data
		for(int line=0+start;line<lines;line++)
		{	String txt = null;
			MyLabel lbl = new MyLabel(txt);
			lbl.setFont(regularFont);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setBackground(GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
			lbl.setForeground(GuiColorTools.COLOR_TABLE_REGULAR_FOREGROUND);
			lbl.setOpaque(true);
			add(lbl,index+line*columns);
		}
		
		// layout
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		SpringUtilities.makeCompactGrid(this,lines,columns,tableMargin,tableMargin,tableMargin,tableMargin);
	}

	public MyLabel getLabel(int line, int col)
	{	MyLabel result = (MyLabel)getComponent(col+line*columns);;
		return result;
	}
}
