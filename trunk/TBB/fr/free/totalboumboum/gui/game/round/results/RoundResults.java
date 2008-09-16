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
import fr.free.totalboumboum.gui.SpringUtilities;
import fr.free.totalboumboum.gui.SwingTools;
import fr.free.totalboumboum.gui.generic.InnerDataPanel;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SimpleMenuPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;
import fr.free.totalboumboum.gui.menus.options.OptionsMenu;
import fr.free.totalboumboum.gui.menus.tournament.TournamentMain;
import fr.free.totalboumboum.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.StringTools;

public class RoundResults extends InnerDataPanel
{	
	private static final long serialVersionUID = 1L;

	private JPanel resultsPanel;
	
	public RoundResults(SplitMenuPanel container)
	{	super(container);
		// layout
		{	BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
			setLayout(layout);
		}
		// background
		setBackground(new Color(50,50,50));
		// size
		int height = SwingTools.getSize(SwingTools.HORIZONTAL_SPLIT_DATA_PANEL_HEIGHT);
		int width = SwingTools.getSize(SwingTools.HORIZONTAL_SPLIT_DATA_PANEL_WIDTH);
		setPreferredSize(new Dimension(width,height));
		
		add(Box.createVerticalGlue());
		
		// title
		{	String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_TITLE_RESULTS);
			JLabel title = new JLabel(txt);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			Font font = getConfiguration().getFont().deriveFont((float)SwingTools.getSize(SwingTools.GAME_TITLE_FONT_SIZE));
			title.setFont(font);
			title.setForeground(Color.BLACK);
			title.setBackground(new Color(255,255,255,128));
			title.setOpaque(true);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			Dimension dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DATA_PANEL_WIDTH),SwingTools.getSize(SwingTools.GAME_DATA_LABEL_TITLE_HEIGHT));
			title.setPreferredSize(dim);
			title.setMinimumSize(dim);
			title.setMaximumSize(dim);
			add(title);
		}
		
		add(Box.createVerticalGlue());
		
		// results panel
		{	Round round = getConfiguration().getTournament().getCurrentMatch().getCurrentRound();
			int playerNumber = round.getProfiles().size();
			playerNumber = 16;
			int lines = playerNumber+1;
			int cols = 2+5+1;			
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
				String hd = "";
				switch(round.getPlayMode())
				{	case CROWN:
						hd = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_CROWNS);
						break;
					case FRAG:
						hd = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_FRAGS);
						break;
					case PAINT:
						hd = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_PAINTINGS);
						break;
					case SURVIVAL:
						hd = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_TIME);
						break;
				}
				String[] headers = 
				{	getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_NAME),
					getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_BOMBS),
					getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_ITEMS),
					getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_DEATHS),
					getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_KILLS),
					hd,
					getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_POINTS),
				};
				for(int i=0;i<headers.length;i++)
				{	String txt = headers[i];
					JLabel lbl = new JLabel(txt);
					lbl.setFont(font);
					lbl.setHorizontalAlignment(SwingConstants.CENTER);
					lbl.setBackground(new Color(0,0,0,128));
					lbl.setForeground(Color.BLACK);
					lbl.setOpaque(true);
					resultsPanel.add(lbl);
				}
*/				
				BufferedImage ic = null;
				String icTt = "";
				switch(round.getPlayMode())
				{	case CROWN:
						ic = SwingTools.getIcon(GuiTools.GAME_ROUND_HEADER_CROWNS);
						icTt = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_CROWNS+"Tooltip");
						break;
					case FRAG:
						ic = SwingTools.getIcon(GuiTools.GAME_ROUND_HEADER_FRAGS);
						icTt = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_FRAGS+"Tooltip");
						break;
					case PAINT:
						ic = SwingTools.getIcon(GuiTools.GAME_ROUND_HEADER_PAINTINGS);
						icTt = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_PAINTINGS+"Tooltip");
						break;
					case SURVIVAL:
						ic = SwingTools.getIcon(GuiTools.GAME_ROUND_HEADER_TIME);
						icTt = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_TIME+"Tooltip");
						break;
				}
				BufferedImage[] icons = 
				{	SwingTools.getIcon(GuiTools.GAME_ROUND_HEADER_NAME),
					SwingTools.getIcon(GuiTools.GAME_ROUND_HEADER_BOMBS),
					SwingTools.getIcon(GuiTools.GAME_ROUND_HEADER_ITEMS),
					SwingTools.getIcon(GuiTools.GAME_ROUND_HEADER_DEATHS),
					SwingTools.getIcon(GuiTools.GAME_ROUND_HEADER_KILLS),
					ic,
					SwingTools.getIcon(GuiTools.GAME_ROUND_HEADER_POINTS),
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
						resultsPanel.add(lbl);
					}
					// scores/points
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
		Round round = getConfiguration().getTournament().getCurrentMatch().getCurrentRound();
		StatisticRound stats = round.getStats();
		ArrayList<Profile> players = round.getProfiles();
		TreeSet<PlayerPoints> ranking = stats.getOrderedPlayers();
		
		// display the ranking
		Iterator<PlayerPoints> i = ranking.descendingIterator();
		int k = 2+5+1;
		int lineHeight = SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_LINE_HEIGHT);
		while(i.hasNext())
		{	PlayerPoints pp = i.next();
			Profile profile = players.get(pp.getIndex());
			// color
			Color clr = profile.getSpriteColor().getColor();
			// portrait
			{	JLabel portraitLabel = (JLabel)resultsPanel.getComponent(k++);
				BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
				double zoom = lineHeight/(double)image.getHeight();
//				double zoom = portraitLabel.getSize().height/(double)image.getHeight();
//				if(zoom!=0)
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
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),120);
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
				{	JLabel pointsLabel = (JLabel)resultsPanel.getComponent(k++);
					pointsLabel.setText(scores[j]);
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),120);
					pointsLabel.setBackground(bg);
				}
			}			
			// points
			{	JLabel pointsLabel = (JLabel)resultsPanel.getComponent(k++);
				double pts = pp.getPoints().next();
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				String txt = nf.format(pts);
				pointsLabel.setText(txt);
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),180);
				pointsLabel.setBackground(bg);
			}
		}
	}
}
