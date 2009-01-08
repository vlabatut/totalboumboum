package fr.free.totalboumboum.gui.common.content.subpanel.part;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JLabel;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.tournament.cup.CupLeg;
import fr.free.totalboumboum.game.tournament.cup.CupPart;
import fr.free.totalboumboum.game.tournament.cup.CupPlayer;
import fr.free.totalboumboum.game.tournament.cup.CupTournament;
import fr.free.totalboumboum.gui.common.structure.subpanel.EntitledSubPanelLines;
import fr.free.totalboumboum.gui.common.structure.subpanel.Line;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class PartSubPanel extends EntitledSubPanelLines implements MouseListener
{	private static final long serialVersionUID = 1L;
	
	public PartSubPanel(int width, int height)
	{	super(width,height,1);
		
		setPart(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// PART				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static final int COL_BEFORE = 0;
	private static final int COL_PLAYER = 1;
	private static final int COL_RANK = 2;
	private static final int COL_AFTER = 3;
	private CupPart part;

	public CupPart getPart()
	{	return part;	
	}
	
	public void setPart(CupPart part)
	{	this.part = part;
		
		// sizes
		int lines;
		if(part==null)
			lines = 1+4;
		else
			lines = 1+part.getPlayers().size();
		setNewTable(lines);
		int buttonWidth = getLineHeight();
		int nameWidth = (int)((getWidth() - (2*buttonWidth + 5*GuiTools.subPanelMargin))*0.75);
		int rankWidth = getWidth() - (2*buttonWidth + nameWidth + 5*GuiTools.subPanelMargin);
	
		int line = 0;
		// header
		{	Line ln = getLine(line);
			for(int i=0;i<3;i++)
				ln.addLabel(0);
			ln.setForegroundColor(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
			int col = 0;
			// empty
			{	ln.setLabelMaxWidth(col,buttonWidth);
				ln.setLabelPreferredWidth(col,buttonWidth);
				ln.setLabelText(col,null,null);
				ln.getLabel(col).setOpaque(false);
				col++;
			}
			// player
			{	ln.setLabelMaxWidth(col,nameWidth);
				ln.setLabelPreferredWidth(col,nameWidth);
				ln.setLabelKey(col,GuiKeys.COMMON_PART_PLAYER,true);
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				ln.setLabelBackground(col,bg);				
				col++;
			}
			// rank
			{	ln.setLabelMaxWidth(col,rankWidth);
				ln.setLabelPreferredWidth(col,rankWidth);
				ln.setLabelKey(col,GuiKeys.COMMON_PART_RANK,true);
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				ln.setLabelBackground(col,bg);
				col++;
			}
			// empty
			{	ln.setLabelMaxWidth(col,buttonWidth);
				ln.setLabelPreferredWidth(col,buttonWidth);
				ln.setLabelText(col,null,null);
				ln.getLabel(col).setOpaque(false);
				col++;
			}
		}

		if(part!=null)
		{	// title
			String title = part.getName();
			setTitleText(title,title);
			getTitleLabel().addMouseListener(this);
						
			line++;
			// players
			for(int k=0;k<part.getPlayers().size();k++)
			{	Line ln = getLine(line);
				for(int i=0;i<3;i++)
					ln.addLabel(0);
				ln.setForegroundColor(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
				int col = 0;
				// before button
				{	ln.setLabelMaxWidth(col,buttonWidth);
					ln.setLabelPreferredWidth(col,buttonWidth);
					ln.setLabelKey(col,GuiKeys.COMMON_PART_BEFORE,true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					ln.setLabelBackground(col,bg);
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				// player
				{	ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelPreferredWidth(col,nameWidth);
					setPlayerName(line);
					col++;
				}
				// rank
				{	ln.setLabelMaxWidth(col,rankWidth);
					ln.setLabelPreferredWidth(col,rankWidth);
					setPlayerRank(line);
					col++;
				}
				// after button
				{	ln.setLabelMaxWidth(col,buttonWidth);
					ln.setLabelPreferredWidth(col,buttonWidth);
					ln.setLabelKey(col,GuiKeys.COMMON_PART_AFTER,true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					ln.setLabelBackground(col,bg);
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				line++;
			}
		}		
		else
		{	// title
			setTitleText("N/A","N/A");
			
			// colors
			setLabelBackground(line,1,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
			setLabelBackground(line,2,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
			for(int i=0;i<5;i++)
			{	setLabelBackground(line,2,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
				setLabelBackground(line,2,GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
				setLabelBackground(line,2,GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
				setLabelBackground(line,2,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
			}
		}
	}
	/**
	 * if there is no profiles, then the players are undetermined.
	 * if there is no rankings, then they're ordered according to CupPlayer,
	 * else they're ordered according to the rankings 
	 * @param line
	 */
	private void setPlayerName(int line)
	{	String text;
		String tooltip;
		Color bg;
				
		ArrayList<Profile> profiles = part.getProfiles();
		int index = getPlayerForLine(line);
		// profile should be known
		if(profiles.size()>0)
		{	// profile known
			if(index!=-1 && profiles.size()>index)
			{	Profile profile = profiles.get(index);
				Color clr = profile.getSpriteColor().getColor();
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				text = profile.getName();
				tooltip = text;
			}
			// not enough profiles
			else
			{	bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				text = "-";
				tooltip = null;
			}			
		}
		// profile not known yet
		else
		{	// maybe the part from the previous leg is over
			CupLeg leg = part.getLeg();
			int previousLegNumber = leg.getNumber()-1;
			CupPlayer p = part.getPlayers().get(index);
			int previousPartNumber = p.getPart();
			int previousRank = p.getRank();
			CupTournament tournament = part.getTournament();
			Profile profile = null;
			if(previousLegNumber>=0)
			{	CupPart previousPart = tournament.getLeg(previousLegNumber).getPart(previousPartNumber);
				HashMap<Integer,ArrayList<Integer>> rkgs = previousPart.getRankings();
				ArrayList<Integer> tie = rkgs.get(previousRank);
				if(tie!=null && tie.size()==1)
					profile = previousPart.getProfileForRank(previousRank);
			}
			if(profile!=null)
			{	Color clr = profile.getSpriteColor().getColor();
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				text = profile.getName();
				tooltip = text;
			}
			// or maybe not
			else
			{	bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				String key = GuiKeys.COMMON_PART_UNDECIDED;
				text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
				if(previousLegNumber<0)
					tooltip = null;
				else
				{	CupLeg previousLeg = tournament.getLegs().get(previousLegNumber);
					String partName = previousLeg.getPart(previousPartNumber).getName();
					tooltip = partName+":"+previousRank;						
				}
			}
		}

		// set content
		setLabelText(line,COL_PLAYER,text,tooltip);
		setLabelBackground(line,COL_PLAYER,bg);
	}
	
	/**
	 * if there's a ranking, it's used to determine which player goes on which line.
	 * if not, the CupPlayer order is used instead.
	 * @param line
	 * @return
	 */
	private int getPlayerForLine(int line)
	{	int result = part.getIndexForRank(line);
		if(result==-1)
			result = line - 1;
		return result;
	}
	
	/**
	 * if there is no rank, then it is undetermined.
	 * else, put the rank according to the profile.
	 * @param line
	 */
	private void setPlayerRank(int line)
	{	String text;
		String tooltip;
		Color bg;

		ArrayList<Profile> profiles = part.getProfiles();
		HashMap<Integer,ArrayList<Integer>> rankings = part.getRankings();
		int index = getPlayerForLine(line);
		
		// color
		if(profiles.size()>index)
		{	Profile profile = profiles.get(index);
			Color clr = profile.getSpriteColor().getColor();
			int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
			bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);					
		}
		else
			bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;

		// rank
		if(rankings.size()>0)
		{	Iterator<Entry<Integer,ArrayList<Integer>>> it = rankings.entrySet().iterator();
			int rk = -1;
			while(rk<0 && it.hasNext())
			{	Entry<Integer,ArrayList<Integer>> entry = it.next();
				ArrayList<Integer> list = entry.getValue();
				if(list.contains(index))
					rk = entry.getKey();
			}
			if(rk==-1)
			{	text = "-";
				tooltip = null;
			}
			else
			{	text = Integer.toString(rk);
				tooltip = text;
			}
		}
		else
		{	text = "-";
			tooltip = null;
		}

		// set content
		setLabelText(line,COL_RANK,text,tooltip);
		setLabelBackground(line,COL_RANK,bg);
	}
	
	public void refresh()
	{	int lim = part.getPlayers().size();
		for(int line=1;line<=lim;line++)
		{	setPlayerName(line);
			setPlayerRank(line);
		}
	}
	
	public void setSelected(boolean select)
	{	Color hBg,dBg;
		if(select)
		{	hBg = GuiTools.COLOR_TABLE_SELECTED_DARK_BACKGROUND;
			dBg = GuiTools.COLOR_TABLE_SELECTED_PALE_BACKGROUND;
		}
		else
		{	hBg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
			dBg = GuiTools.COLOR_COMMON_BACKGROUND;
		}
		setTitleBackground(hBg);
		setLineBackground(0,hBg);
		setBackground(dBg);
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<PartSubPanelListener> listeners = new ArrayList<PartSubPanelListener>();
	
	public void addListener(PartSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(PartSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	private void firePartAfterClicked(CupPart part)
	{	for(PartSubPanelListener listener: listeners)
			listener.partAfterClicked(part);
	}

	private void fireBeforeClicked(CupPart part)
	{	for(PartSubPanelListener listener: listeners)
			listener.partBeforeClicked(part);
	}

	private void fireTitleClicked(CupPart part)
	{	for(PartSubPanelListener listener: listeners)
			listener.partTitleClicked(part);
	}

	/////////////////////////////////////////////////////////////////
	// MOUSE LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void mouseClicked(MouseEvent e)
	{	
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{	
	}

	@Override
	public void mouseExited(MouseEvent e)
	{	
	}

	@Override
	public void mousePressed(MouseEvent e)
	{	JLabel label = (JLabel)e.getComponent();
		int[] pos = getLabelPosition(label);
		if(pos==null)
			fireTitleClicked(part);
		else
		{	int index = getPlayerForLine(pos[0]);
			switch(pos[1])
			{	// before button
				case COL_BEFORE:
					{	CupLeg leg = part.getLeg();
						int previousLegNumber = leg.getNumber()-1;
						CupPlayer p = part.getPlayers().get(index);
						int partNumber = p.getPart();
						CupTournament tournament = part.getTournament();
						// doesn't work for the first leg 
						if(previousLegNumber>=0)
						{	CupLeg previousLeg = tournament.getLegs().get(previousLegNumber);
							CupPart previousPart = previousLeg.getPart(partNumber);
							fireBeforeClicked(previousPart);
						}
					}
					break;
				// after button
				case COL_AFTER:
					{	HashMap<Integer,ArrayList<Integer>> rankings = part.getRankings();
						// works only if there are rankings
						if(rankings.size()>0)
						{	// look fot this player's ranking
							Iterator<Entry<Integer,ArrayList<Integer>>> it = rankings.entrySet().iterator();
							int rk = -1;
							while(rk<0 && it.hasNext())
							{	Entry<Integer,ArrayList<Integer>> entry = it.next();
								ArrayList<Integer> list = entry.getValue();
								if(list.contains(index))
									rk = entry.getKey();							
							}
							// works only if the player isn't tied to others
							if(rk>=0 && rankings.get(rk).size()==1)
							{	int partNumber = part.getNumber();
								int legNumber = part.getLeg().getNumber();
								int nextLegNumber = legNumber+1;
								CupTournament tournament = part.getTournament();
								// works only if it wasn't the last leg
								if(tournament.getLegs().size()>nextLegNumber)
								{	CupLeg nextLeg = tournament.getLeg(nextLegNumber);
									CupPart nextPart = null;
									ArrayList<CupPart> parts = nextLeg.getParts();
									Iterator<CupPart> iter = parts.iterator();
									while(iter.hasNext() && nextPart==null)
									{	CupPart part = iter.next();
										Iterator<CupPlayer> itp = part.getPlayers().iterator();
										while(itp.hasNext() && nextPart==null)
										{	CupPlayer plyr = itp.next();
											if(plyr.getPart()==partNumber && plyr.getRank()==rk)
												nextPart = part;										
										}
									}
									// works only if there's a part using the player
									if(nextPart!=null)
										firePartAfterClicked(nextPart);
								}
							}
						}
					}
					break;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
}
