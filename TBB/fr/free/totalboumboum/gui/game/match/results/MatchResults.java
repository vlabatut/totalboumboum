package fr.free.totalboumboum.gui.game.match.results;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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

import fr.free.totalboumboum.data.profile.Portraits;
import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.data.statistics.Score;
import fr.free.totalboumboum.data.statistics.StatisticMatch;
import fr.free.totalboumboum.data.statistics.StatisticRound;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.point.PlayerPoints;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.generic.EntitledDataPanel;
import fr.free.totalboumboum.gui.generic.InnerDataPanel;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SimpleMenuPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;
import fr.free.totalboumboum.gui.generic.TablePanel;
import fr.free.totalboumboum.gui.menus.options.OptionsMenu;
import fr.free.totalboumboum.gui.menus.tournament.TournamentMain;
import fr.free.totalboumboum.gui.tools.SpringUtilities;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.StringTools;

public class MatchResults extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	private TablePanel resultsPanel;
	
	public MatchResults(SplitMenuPanel container)
	{	super(container);

		// title
		String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_TITLE_RESULTS);
		setTitle(txt);
		
		// data
		{	int lines = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_NUMBER)+1;
			int cols = 2+4+2;
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
				String names[] = 
				{	GuiTools.GAME_MATCH_HEADER_NAME,
					GuiTools.GAME_MATCH_HEADER_BOMBS,
					GuiTools.GAME_MATCH_HEADER_ITEMS,
					GuiTools.GAME_MATCH_HEADER_DEATHS,
					GuiTools.GAME_MATCH_HEADER_KILLS,
					GuiTools.GAME_MATCH_HEADER_TOTAL,
					GuiTools.GAME_MATCH_HEADER_POINTS
				};
				for(int i=0;i<names.length;i++)
				{	BufferedImage icon = GuiTools.getIcon(names[i]);
					String tooltip = getConfiguration().getLanguage().getText(names[i]+"Tooltip");
					JLabel lbl = resultsPanel.getLabel(0,1+i);
					lbl.setText(null);
					lbl.setToolTipText(tooltip);
					int lineHeight = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT);
					double zoom = lineHeight/(double)icon.getHeight();
						icon = ImageTools.resize(icon,zoom,true);
					ImageIcon ic = new ImageIcon(icon);
					lbl.setIcon(ic);
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
			JLabel lbl = resultsPanel.getLabel(0,col);
			String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_ROUND)+rounds.size();
			lbl.setText(txt);
		}
		
		// display the ranking
		Iterator<PlayerPoints> i = ranking.descendingIterator();
		int lineHeight = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT);
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
			{	JLabel portraitLabel = resultsPanel.getLabel(line, col++);
				BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
				double zoom = lineHeight/(double)image.getHeight();
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
				String[] scores = 
				{	nf.format(stats.getScores(Score.BOMBS)[pp.getIndex()]),
					nf.format(stats.getScores(Score.ITEMS)[pp.getIndex()]),
					nf.format(stats.getScores(Score.DEATHS)[pp.getIndex()]),
					nf.format(stats.getScores(Score.KILLS)[pp.getIndex()]),
				};
				for(int j=0;j<scores.length;j++)
				{	JLabel pointsLabel = resultsPanel.getLabel(line, col++);
					pointsLabel.setText(scores[j]);
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					pointsLabel.setBackground(bg);
				}
			}			
			// rounds
			Iterator<StatisticRound> r = rounds.iterator();
			while(r.hasNext())
			{	JLabel rndLabel = resultsPanel.getLabel(line, col++);
				StatisticRound statRound = r.next();
				float pts = statRound.getPoints()[pp.getIndex()];
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				String txt = nf.format(pts);
				rndLabel.setText(txt);
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL2;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				rndLabel.setBackground(bg);
			}
			// total
			{	JLabel totalLabel = resultsPanel.getLabel(line, col++);
				float pts = partialPoints[pp.getIndex()];
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				String txt = nf.format(pts);
				totalLabel.setText(txt);
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				totalLabel.setBackground(bg);
			}
			// points
			{	JLabel totalLabel = resultsPanel.getLabel(line, col++);
				String txt = "-";
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				if(match.isOver())	
				{	float pts = points[pp.getIndex()];
					txt = nf.format(pts);
				}
				totalLabel.setText(txt);
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				totalLabel.setBackground(bg);
			}
		}
	}
}
