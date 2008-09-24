package fr.free.totalboumboum.gui.game.round.results;

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
import fr.free.totalboumboum.data.statistics.StatisticRound;
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

public class RoundResults extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	private TablePanel resultsPanel;
	
	public RoundResults(SplitMenuPanel container)
	{	super(container);

		// title
		String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_TITLE_RESULTS);
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
				BufferedImage ic = null;
				String icTt = null;
				switch(round.getPlayMode())
				{	case CROWN:
						ic = GuiTools.getIcon(GuiTools.GAME_ROUND_HEADER_CROWNS);
						icTt = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_CROWNS+"Tooltip");
						break;
					case FRAG:
						ic = GuiTools.getIcon(GuiTools.GAME_ROUND_HEADER_FRAGS);
						icTt = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_FRAGS+"Tooltip");
						break;
					case PAINT:
						ic = GuiTools.getIcon(GuiTools.GAME_ROUND_HEADER_PAINTINGS);
						icTt = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_PAINTINGS+"Tooltip");
						break;
					case SURVIVAL:
						ic = GuiTools.getIcon(GuiTools.GAME_ROUND_HEADER_TIME);
						icTt = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_TIME+"Tooltip");
						break;
				}
				BufferedImage[] icons = 
				{	GuiTools.getIcon(GuiTools.GAME_ROUND_HEADER_NAME),
					GuiTools.getIcon(GuiTools.GAME_ROUND_HEADER_BOMBS),
					GuiTools.getIcon(GuiTools.GAME_ROUND_HEADER_ITEMS),
					GuiTools.getIcon(GuiTools.GAME_ROUND_HEADER_DEATHS),
					GuiTools.getIcon(GuiTools.GAME_ROUND_HEADER_KILLS),
					ic,
					GuiTools.getIcon(GuiTools.GAME_ROUND_HEADER_POINTS),
				};
				String[] tooltips = 
				{	getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_NAME+"Tooltip"),
					getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_BOMBS+"Tooltip"),
					getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_ITEMS+"Tooltip"),
					getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_DEATHS+"Tooltip"),
					getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_KILLS+"Tooltip"),
					icTt,
					getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_POINTS+"Tooltip")
				};
				for(int i=0;i<icons.length;i++)
				{	JLabel lbl = resultsPanel.getLabel(0,1+i);
					lbl.setText(null);
					lbl.setToolTipText(tooltips[i]);
					int lineHeight = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT);
					double zoom = lineHeight/(double)icons[i].getHeight();
						icons[i] = ImageTools.resize(icons[i],zoom,true);
					ImageIcon icon = new ImageIcon(icons[i]);
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
					case FRAG:
						sc = nf.format(stats.getScores(Score.KILLS)[pp.getIndex()] - stats.getScores(Score.DEATHS)[pp.getIndex()]);
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
