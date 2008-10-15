package fr.free.totalboumboum.gui.quicklaunch;

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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.profile.Portraits;
import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.data.statistics.Score;
import fr.free.totalboumboum.data.statistics.StatisticRound;
import fr.free.totalboumboum.game.points.PlayerPoints;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.common.MenuContainer;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.panel.data.InnerDataPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.SimpleMenuPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.menus.tournament.TournamentSplitPanel;
import fr.free.totalboumboum.gui.options.OptionsMenu;
import fr.free.totalboumboum.gui.tools.SpringUtilities;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.StringTools;

public class QuickResults extends JPanel
{	
	private static final long serialVersionUID = 1L;

	private Configuration configuration;
	private Font headerFont;
	private Font regularFont;
	private int columns = 0;
	private int lines = 0;
	private int tableMargin;
	
	public QuickResults(Dimension dimen, Configuration configuration)
	{	super();
		
		// init
		lines = 16+1;
		int cols = 1+5+1;			
		int width = dimen.width;
		int height = dimen.height;
		Round round = configuration.getTournament().getCurrentMatch().getCurrentRound();
		StatisticRound stats = round.getStats();
		ArrayList<Profile> players = round.getProfiles();
		TreeSet<PlayerPoints> ranking = stats.getOrderedPlayers();
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
		BufferedImage bi = new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		GuiTools.setGraphics(g);
		GuiTools.setGameFont(new Font("Arial",Font.PLAIN,10));
		int headerHeight = (int)(1.5*pHeight/17.5);
		int headerSize = GuiTools.getFontSize(headerHeight);
		headerFont = new Font("Arial",Font.PLAIN,headerSize); 
		int lineHeight = (pHeight-headerHeight)/16;
		int lineSize = GuiTools.getFontSize(lineHeight);
		regularFont = new Font("Arial",Font.PLAIN,lineSize); 
		
		// table
		for(int col=0;col<cols;col++)
			addColumn(col);
		
		// headers
		{	String sc = null;
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
			String names[] = 
			{	"Player",
				"Bombs",
				"Items",
				"Deaths",
				"Kills",
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
		{	Iterator<PlayerPoints> i = ranking.descendingIterator();
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
				// name
				{	JLabel nameLabel = getLabel(line, col++);
					nameLabel.setText(pp.getPlayer());
					nameLabel.setToolTipText(pp.getPlayer());
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
					{	JLabel pointsLabel = getLabel(line, col++);
						pointsLabel.setText(scores[j]);
						int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
						Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
						pointsLabel.setBackground(bg);
					}
				}			
				// points
				{	JLabel pointsLabel = getLabel(line, col++);
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
