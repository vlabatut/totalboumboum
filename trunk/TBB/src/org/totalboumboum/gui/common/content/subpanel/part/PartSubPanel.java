package org.totalboumboum.gui.common.content.subpanel.part;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import java.util.List;

import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.game.tournament.cup.CupLeg;
import org.totalboumboum.game.tournament.cup.CupPart;
import org.totalboumboum.game.tournament.cup.CupPlayer;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.LinesSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.content.Line;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class PartSubPanel extends LinesSubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	
	public PartSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.TITLE,1,1,false);
		
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
		reinit(lines,1);
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
				ln.setLabelPrefWidth(col,buttonWidth);
				ln.setLabelText(col,null,null);
				ln.getLabel(col).setOpaque(false);
				col++;
			}
			// player
			{	ln.setLabelMaxWidth(col,nameWidth);
				ln.setLabelPrefWidth(col,nameWidth);
				ln.setLabelKey(col,GuiKeys.COMMON_PART_PLAYER,true);
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				ln.setLabelBackground(col,bg);				
				col++;
			}
			// rank
			{	ln.setLabelMaxWidth(col,rankWidth);
				ln.setLabelPrefWidth(col,rankWidth);
				ln.setLabelKey(col,GuiKeys.COMMON_PART_RANK,true);
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				ln.setLabelBackground(col,bg);
				col++;
			}
			// empty
			{	ln.setLabelMaxWidth(col,buttonWidth);
				ln.setLabelPrefWidth(col,buttonWidth);
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
					ln.setLabelPrefWidth(col,buttonWidth);
					ln.setLabelKey(col,GuiKeys.COMMON_PART_BEFORE,true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					ln.setLabelBackground(col,bg);
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				// player
				{	ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelPrefWidth(col,nameWidth);
					setPlayerName(line);
					col++;
				}
				// rank
				{	ln.setLabelMaxWidth(col,rankWidth);
					ln.setLabelPrefWidth(col,rankWidth);
					setPlayerRank(line);
					col++;
				}
				// after button
				{	ln.setLabelMaxWidth(col,buttonWidth);
					ln.setLabelPrefWidth(col,buttonWidth);
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
			
		Profile profile = getProfileForLine(line);
		// profile known
		if(profile!=null)
		{	Color clr = profile.getSpriteColor().getColor();
			int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
			bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
			text = profile.getName();
			tooltip = text;
		}
		// profile unknown yet
		else
		{	bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
			String key = GuiKeys.COMMON_PART_UNDECIDED;
			text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
			CupLeg previousLeg = part.getLeg().getPreviousLeg();
			if(previousLeg==null)
				tooltip = null;
			else
			{	CupPlayer player = part.getPlayers().get(line-1);
				int previousPartNumber = player.getPrevPart();
				int previousRank = player.getPrevRank();
				String partName = previousLeg.getPart(previousPartNumber).getName();
				tooltip = partName+":"+previousRank;						
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
	private Profile getProfileForLine(int line)
	{	Ranks ranks = part.getOrderedPlayers();
		List<Profile> absoluteList = ranks.getAbsoluteOrderList();
		Profile result = null;
		if(line<=absoluteList.size())
			result = absoluteList.get(line-1);
		else if(ranks.size()==0)
			result = part.getProfileForIndex(line-1);
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

		Profile profile = getProfileForLine(line);		
		// color
		if(profile!=null)
		{	Color clr = profile.getSpriteColor().getColor();
			int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
			bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);					
		}
		else
			bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;

		// rank
		Ranks ranks = part.getOrderedPlayers();
		int rk = ranks.getRankForProfile(profile);
		if(rk==-1)
		{	text = "-";
			tooltip = null;
		}
		else
		{	text = Integer.toString(rk);
			tooltip = text;
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
	private List<PartSubPanelListener> listeners = new ArrayList<PartSubPanelListener>();
	
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
	{	MyLabel label = (MyLabel)e.getComponent();
		int[] pos = getLabelPosition(label);
		if(pos==null)
			fireTitleClicked(part);
		else
		{	Profile profile = getProfileForLine(pos[0]);
			switch(pos[1])
			{	// before button
				case COL_BEFORE:
					{	CupLeg leg = part.getLeg();
						CupLeg previousLeg = leg.getPreviousLeg();
						// doesn't work for the first leg 
						if(previousLeg!=null)
						{	CupPlayer p = part.getPlayerForProfile(profile);
							int partNumber = p.getPrevPart();
							CupPart previousPart = previousLeg.getPart(partNumber);
							fireBeforeClicked(previousPart);
						}
					}
					break;
				// after button
				case COL_AFTER:
					{	Ranks ranks = part.getOrderedPlayers();
						List<Profile> pr = ranks.getProfilesFromRank(pos[0]);
						if(pr!=null && pr.size()==1)
						{	CupPart nextPart = part.getNextPartForRank(pos[0]);
							if(nextPart!=null)
								firePartAfterClicked(nextPart);
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
