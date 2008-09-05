package fr.free.totalboumboum.gui.game.tournament.results;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.data.statistics.StatisticMatch;
import fr.free.totalboumboum.data.statistics.StatisticRound;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.ranking.PlayerPoints;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.gui.generic.InnerDataPanel;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SimpleMenuPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;
import fr.free.totalboumboum.gui.menus.options.OptionsMenu;
import fr.free.totalboumboum.gui.menus.tournament.TournamentMain;
import fr.free.totalboumboum.tools.GuiTools;

public class TournamentResults extends InnerDataPanel
{	
	private static final long serialVersionUID = 1L;

	public TournamentResults(SplitMenuPanel container)
	{	super(container);
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		// background
		setBackground(Color.LIGHT_GRAY);		
		//
		JLabel label = new JLabel("Tournament Results");
		Font font = new Font(null,Font.PLAIN,30);
		label.setFont(font);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(label);
	}

	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
	{	// clean
		while(this.getComponentCount()>0)
			remove(0);
		
		// title
		{	JLabel label = new JLabel("Tournament Results");
			Font font = new Font(null,Font.PLAIN,30);
			label.setFont(font);
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			add(label);
		}

		// init
		AbstractTournament tournament = getConfiguration().getTournament();
		ArrayList<Profile> players = tournament.getProfiles();
		ArrayList<StatisticMatch> matches = tournament.getStats().getStatMatches();
		float[] points = tournament.getStats().getPoints();
		String[] texts = new String[players.size()+2];
		String text = "Name";
		for(int i=0;i<matches.size();i++)
			text = text + ".......R"+(i+1);
		text = text + ".......total";
		{	JLabel label = new JLabel(text);
			Font font = new Font(null,Font.PLAIN,20);
			label.setFont(font);
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			add(label);
		}
		
		
		// players
		for(int i=0;i<players.size();i++)
		{	text = players.get(i).getName();
			for(int j=0;j<matches.size();j++)
				text = text + "......."+matches.get(j).getPoints()[i];
			if(tournament.isOver())
				text = text + "......."+points[i];
			else
				text = text + ".......-";
			{	JLabel label = new JLabel(text);
				Font font = new Font(null,Font.PLAIN,20);
				label.setFont(font);
				label.setAlignmentX(Component.CENTER_ALIGNMENT);
				add(label);
			}
		}
		

		
/*		
		// init
		AbstractTournament tournament = getConfiguration().getTournament();
		float[] points = tournament.getStats().getPoints();
		ArrayList<String> players = tournament.getProfiles();
		// sorting players according to points
		TreeSet<PlayerPoints> ranking = new TreeSet<PlayerPoints>();
		for(int i=0;i<points.length;i++)
		{	PlayerPoints pp = new PlayerPoints(players.get(i));
			pp.addPoint(points[i]);
			ranking.add(pp);
		}
		
		// display the ranking
		Iterator<PlayerPoints> i = ranking.descendingIterator();
		while(i.hasNext())
		{	PlayerPoints pp = i.next();
			Iterator<Float> pts = pp.getPoints();
			String text = pts.next()+"."+pp.getPlayer()+" ";
			while(pts.hasNext())
				text = text + pts.next() + " "; 
			JLabel label = new JLabel(text);
			Font font = new Font(null,Font.PLAIN,20);
			label.setFont(font);
			add(label);
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
*/		
	}
}
