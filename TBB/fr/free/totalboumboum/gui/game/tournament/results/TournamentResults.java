package fr.free.totalboumboum.gui.game.tournament.results;

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
import fr.free.totalboumboum.data.statistics.StatisticTournament;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.point.PlayerPoints;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.game.tournament.sequence.SequenceTournament;
import fr.free.totalboumboum.gui.generic.InnerDataPanel;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SimpleMenuPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;
import fr.free.totalboumboum.gui.menus.options.OptionsMenu;
import fr.free.totalboumboum.gui.menus.tournament.TournamentMain;
import fr.free.totalboumboum.gui.tools.SpringUtilities;
import fr.free.totalboumboum.gui.tools.SwingTools;
import fr.free.totalboumboum.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public class TournamentResults extends InnerDataPanel
{	
	private static final long serialVersionUID = 1L;

	private JPanel resultsPanel;
	
	public TournamentResults(SplitMenuPanel container)
	{	super(container);
		// layout
		{	BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
			setLayout(layout);
		}
		// size
		int height = SwingTools.getSize(SwingTools.HORIZONTAL_SPLIT_DATA_PANEL_HEIGHT);
		int width = SwingTools.getSize(SwingTools.HORIZONTAL_SPLIT_DATA_PANEL_WIDTH);
		setPreferredSize(new Dimension(width,height));
		// background
		setBackground(new Color(50,50,50));
		
		add(Box.createVerticalGlue());
		// title
		{	String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_TOURNAMENT_TITLE_RESULTS);
			JLabel title = new JLabel(txt);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			Font font = getConfiguration().getFont().deriveFont((float)SwingTools.getSize(SwingTools.GAME_TITLE_FONT_SIZE));
			title.setForeground(Color.BLACK);
			title.setBackground(new Color(255,255,255,128));
			title.setOpaque(true);
			title.setFont(font);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			add(title);
			Dimension dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DATA_PANEL_WIDTH),SwingTools.getSize(SwingTools.GAME_DATA_LABEL_TITLE_HEIGHT));
			title.setPreferredSize(dim);
			title.setMinimumSize(dim);
			title.setMaximumSize(dim);
		}
		
		add(Box.createVerticalGlue());
		
		// results panel
		{	AbstractTournament tournament = getConfiguration().getTournament();
			int playerNumber = tournament.getProfiles().size();
			playerNumber = 16;
			int lines = playerNumber+1;
			int cols = 2+4+2;
			SpringLayout layout = new SpringLayout();			
			resultsPanel = new JPanel(layout);
			resultsPanel.setBackground(new Color(255,255,255,128));
			Dimension dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DATA_PANEL_WIDTH),SwingTools.getSize(SwingTools.GAME_DATA_PANEL_HEIGHT));
			resultsPanel.setPreferredSize(dim);
			resultsPanel.setMinimumSize(dim);
			resultsPanel.setMaximumSize(dim);
			// headers
			{	Font font = getConfiguration().getFont().deriveFont((float)SwingTools.getSize(SwingTools.GAME_RESULTS_HEADER_FONT_SIZE));
				{	JLabel lbl = new JLabel(" ");
					lbl.setOpaque(false);
					lbl.setPreferredSize(new Dimension(SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_LINE_HEIGHT),SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
					lbl.setMaximumSize(new Dimension(SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_LINE_HEIGHT),SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
					lbl.setMinimumSize(new Dimension(SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_LINE_HEIGHT),SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
					resultsPanel.add(lbl);
				}
/*				
				{	String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_TOURNAMENT_HEADER_NAME);
					JLabel lbl = new JLabel(txt);
					lbl.setFont(font);
					lbl.setHorizontalAlignment(SwingConstants.CENTER);
					lbl.setBackground(new Color(0,0,0,128));
					lbl.setForeground(Color.BLACK);
					lbl.setOpaque(true);
					resultsPanel.add(lbl);
				}
				{	String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_TOURNAMENT_HEADER_TOTAL);
					JLabel lbl = new JLabel(txt);
					lbl.setFont(font);
					lbl.setHorizontalAlignment(SwingConstants.CENTER);
					lbl.setBackground(new Color(0,0,0,128));
					lbl.setForeground(Color.BLACK);
					lbl.setOpaque(true);
					resultsPanel.add(lbl);
				}
*/				
				BufferedImage[] icons = 
				{	SwingTools.getIcon(GuiTools.GAME_TOURNAMENT_HEADER_NAME),
					SwingTools.getIcon(GuiTools.GAME_TOURNAMENT_HEADER_BOMBS),
					SwingTools.getIcon(GuiTools.GAME_TOURNAMENT_HEADER_ITEMS),
					SwingTools.getIcon(GuiTools.GAME_TOURNAMENT_HEADER_DEATHS),
					SwingTools.getIcon(GuiTools.GAME_TOURNAMENT_HEADER_KILLS),
					SwingTools.getIcon(GuiTools.GAME_TOURNAMENT_HEADER_TOTAL),
					SwingTools.getIcon(GuiTools.GAME_TOURNAMENT_HEADER_POINTS)
				};
				String[] tooltips = 
				{	getConfiguration().getLanguage().getText(GuiTools.GAME_TOURNAMENT_HEADER_NAME+"Tooltip"),
					getConfiguration().getLanguage().getText(GuiTools.GAME_TOURNAMENT_HEADER_BOMBS+"Tooltip"),
					getConfiguration().getLanguage().getText(GuiTools.GAME_TOURNAMENT_HEADER_ITEMS+"Tooltip"),
					getConfiguration().getLanguage().getText(GuiTools.GAME_TOURNAMENT_HEADER_DEATHS+"Tooltip"),
					getConfiguration().getLanguage().getText(GuiTools.GAME_TOURNAMENT_HEADER_KILLS+"Tooltip"),
					getConfiguration().getLanguage().getText(GuiTools.GAME_TOURNAMENT_HEADER_TOTAL+"Tooltip"),
					getConfiguration().getLanguage().getText(GuiTools.GAME_TOURNAMENT_HEADER_POINTS+"Tooltip")
				};
				for(int i=0;i<icons.length;i++)
				{	JLabel lbl = new JLabel(" ");
					lbl.setText("");
					lbl.setToolTipText(tooltips[i]);
					lbl.setHorizontalAlignment(SwingConstants.CENTER);
					lbl.setBackground(new Color(0,0,0,128));
					lbl.setOpaque(true);
					resultsPanel.add(lbl);
					int lineHeight = SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_HEADER_HEIGHT);
					double zoom = lineHeight/(double)icons[i].getHeight();
						icons[i] = ImageTools.resize(icons[i],zoom,true);
					ImageIcon icon = new ImageIcon(icons[i]);
					lbl.setIcon(icon);
				}
			}
			// data
			{	Font font = getConfiguration().getFont().deriveFont((float)SwingTools.getSize(SwingTools.GAME_RESULTS_LINE_FONT_SIZE));
				for(int i=1;i<lines;i++)
				{	// portrait
					{	JLabel lbl = new JLabel(" ");
						lbl.setHorizontalAlignment(SwingConstants.CENTER);
						lbl.setBackground(new Color(0,0,0,20));
						lbl.setOpaque(true);
						resultsPanel.add(lbl);
					}
					// name
					{	JLabel lbl = new JLabel(" ");
						lbl.setFont(font);
						lbl.setHorizontalAlignment(SwingConstants.CENTER);
						lbl.setBackground(new Color(0,0,0,20));
						lbl.setForeground(Color.BLACK);
						lbl.setOpaque(true);
						Dimension dimension = new Dimension(Integer.MAX_VALUE,SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_LINE_HEIGHT));
						lbl.setMaximumSize(dimension);
						dimension = new Dimension(SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_HEADER_HEIGHT),SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_LINE_HEIGHT));
						lbl.setMinimumSize(dimension);
						resultsPanel.add(lbl);
					}
					// total
					for(int j=2;j<cols;j++)
					{	JLabel lbl = new JLabel(" ");
						lbl.setFont(font);
						lbl.setHorizontalAlignment(SwingConstants.CENTER);
						lbl.setBackground(new Color(0,0,0,20));
						lbl.setForeground(Color.BLACK);
						lbl.setOpaque(true);
						resultsPanel.add(lbl);
					}
				}
			}
			//
			updateData();
			int margin = SwingTools.getSize(SwingTools.GAME_RESULTS_MARGIN_SIZE);
			SpringUtilities.makeCompactGrid(resultsPanel,lines,cols,margin,margin,margin,margin);
			add(resultsPanel);
		}

		add(Box.createVerticalGlue());
	}

	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
	{	// init
		SequenceTournament tournament = (SequenceTournament)getConfiguration().getTournament();
		ArrayList<Profile> players = tournament.getProfiles();
		StatisticTournament stats = tournament.getStats();
		ArrayList<StatisticMatch> matches = stats.getStatMatches();
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
		int lines = 16+1/*players.size()+1*/;
		int cols = 2+4+matches.size()+2;
		if(resultsPanel.getComponentCount()<lines*cols)
		{	int colNbr = 2+4+matches.size()-1;
			// header
			{	String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_TOURNAMENT_HEADER_MATCH)+(matches.size());
				JLabel lbl = new JLabel(txt);
				txt = getConfiguration().getLanguage().getText(GuiTools.GAME_TOURNAMENT_HEADER_MATCH+"Tooltip")+" "+matches.size();
				lbl.setToolTipText(txt);
				Font font = getConfiguration().getFont().deriveFont((float)SwingTools.getSize(SwingTools.GAME_RESULTS_HEADER_FONT_SIZE));
				lbl.setFont(font);
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setBackground(new Color(0,0,0,128));
				lbl.setForeground(Color.WHITE);
				lbl.setOpaque(true);
				resultsPanel.add(lbl,colNbr);
			}
			// data
			for(int line=1;line<17;line++)
			{	JLabel lbl = new JLabel(" ");
				Font font = getConfiguration().getFont().deriveFont((float)SwingTools.getSize(SwingTools.GAME_RESULTS_LINE_FONT_SIZE));
				lbl.setFont(font);
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setBackground(new Color(0,0,0,20));
				lbl.setForeground(Color.BLACK);
				lbl.setOpaque(true);
				resultsPanel.add(lbl,line*cols+colNbr);
			}
			// layout
			SpringLayout layout = new SpringLayout();
			resultsPanel.setLayout(layout);
			int margin = SwingTools.getSize(SwingTools.GAME_RESULTS_MARGIN_SIZE);
			SpringUtilities.makeCompactGrid(resultsPanel,lines,cols,margin,margin,margin,margin);
		}
		
		// display the ranking
		Iterator<PlayerPoints> i = ranking.descendingIterator();
		int k = cols;
		int lineHeight = SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_LINE_HEIGHT);
		while(i.hasNext())
		{	PlayerPoints pp = i.next();
			Profile profile = players.get(pp.getIndex());
			// color
			Color clr = players.get(pp.getIndex()).getSpriteColor().getColor();
			// portrait
			{	JLabel portraitLabel = (JLabel)resultsPanel.getComponent(k++);
				BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
				double zoom = lineHeight/(double)image.getHeight();
				image = ImageTools.resize(image,zoom,true);
				ImageIcon icon = new ImageIcon(image);
				portraitLabel.setIcon(icon);
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),120);
				portraitLabel.setBackground(bg);			
				portraitLabel.setText("");
			}
			// name
			{	JLabel nameLabel = (JLabel)resultsPanel.getComponent(k++);
				nameLabel.setText(pp.getPlayer());
				nameLabel.setToolTipText(pp.getPlayer());
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),120);
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
				{	JLabel pointsLabel = (JLabel)resultsPanel.getComponent(k++);
					pointsLabel.setText(scores[j]);
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),100);
					pointsLabel.setBackground(bg);
				}
			}			
			// matches
			Iterator<StatisticMatch> m = matches.iterator();
			while(m.hasNext())
			{	JLabel matchLabel = (JLabel)resultsPanel.getComponent(k++);
				StatisticMatch statMatch = m.next();
				float pts = statMatch.getPoints()[pp.getIndex()];
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				String txt = nf.format(pts);
				matchLabel.setText(txt);
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),140);
				matchLabel.setBackground(bg);
			}
			// total
			{	JLabel totalLabel = (JLabel)resultsPanel.getComponent(k++);
				float pts = partialPoints[pp.getIndex()];
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				String txt = nf.format(pts);
				totalLabel.setText(txt);
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),180);
				totalLabel.setBackground(bg);
			}
			// points
			{	JLabel totalLabel = (JLabel)resultsPanel.getComponent(k++);
				String txt = "-";
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				if(tournament.isOver())	
				{	float pts = points[pp.getIndex()];
					txt = nf.format(pts);
				}
				totalLabel.setText(txt);
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),210);
				totalLabel.setBackground(bg);
			}
		}
	}
}
