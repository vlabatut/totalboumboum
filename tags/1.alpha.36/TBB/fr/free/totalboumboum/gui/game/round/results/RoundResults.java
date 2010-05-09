package fr.free.totalboumboum.gui.game.round.results;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.data.profile.Portraits;
import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.data.statistics.StatisticRound;
import fr.free.totalboumboum.game.ranking.PlayerPoints;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.generic.InnerDataPanel;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SimpleMenuPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;
import fr.free.totalboumboum.gui.menus.options.OptionsMenu;
import fr.free.totalboumboum.gui.menus.tournament.TournamentMain;
import fr.free.totalboumboum.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

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
		setBackground(new Color(230,230,230));
		
		add(Box.createVerticalGlue());
		// title
		{	JLabel title = new JLabel("Round Results");
			Font font = getConfiguration().getFont().deriveFont(30f);
			title.setForeground(Color.BLACK);
			title.setFont(font);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			add(title);
		}
		add(Box.createRigidArea(new Dimension(0,25)));
		// results panel
		{	Round round = getConfiguration().getTournament().getCurrentMatch().getCurrentRound();
			int playerNumber = round.getProfiles().size();
			int lines = playerNumber+1;
			int cols = 3;
			GridLayout layout = new GridLayout(lines,cols,10,10);
			resultsPanel = new JPanel(layout);
			Dimension d = getConfiguration().getPanelDimension();
			Dimension dim = new Dimension((int)(d.width*0.9),(int)(d.height*0.7));
			resultsPanel.setPreferredSize(dim);
			resultsPanel.setMaximumSize(dim);
			// labels
			for(int i=0;i<lines;i++)
				for(int j=0;j<cols;j++)
				{	JLabel lbl = new JLabel("N/A");
					Font font = getConfiguration().getFont().deriveFont(20f);
					lbl.setFont(font);
					lbl.setHorizontalAlignment(SwingConstants.CENTER);
					lbl.setBackground(new Color(0,0,0,128));
					lbl.setForeground(Color.BLACK);
					lbl.setOpaque(true);
					resultsPanel.add(lbl);
				}
			// titles
			JLabel lbl;
			lbl = (JLabel)resultsPanel.getComponent(0);
			lbl.setText("Player");
			lbl = (JLabel)resultsPanel.getComponent(1);
			lbl.setText("Name");
			lbl = (JLabel)resultsPanel.getComponent(2);
			lbl.setText("Points");
			updateData();
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
		int k = 3;
		while(i.hasNext())
		{	PlayerPoints pp = i.next();
			Profile profile = players.get(pp.getIndex());
			// color
			Color clr = profile.getSpriteColor().getColor();
			Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),128);
			// portrait
			JLabel portraitLabel = (JLabel)resultsPanel.getComponent(k++);
			BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
			double zoom = portraitLabel.getSize().height/(double)image.getHeight();
if(zoom!=0)
	image = ImageTools.resize(image,zoom,true);
			ImageIcon icon = new ImageIcon(image);
			portraitLabel.setIcon(icon);
			portraitLabel.setBackground(bg);			
			portraitLabel.setText("");
			// name
			JLabel nameLabel = (JLabel)resultsPanel.getComponent(k++);
			nameLabel.setText(pp.getPlayer());
			nameLabel.setBackground(bg);			
			// points
			JLabel pointsLabel = (JLabel)resultsPanel.getComponent(k++);
			pointsLabel.setText(pp.getPoints().next().toString());
			pointsLabel.setBackground(bg);
		}
	}
}
