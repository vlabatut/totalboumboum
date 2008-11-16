package fr.free.totalboumboum.gui.game.tournament.description;

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
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Portraits;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.tournament.sequence.SequenceTournament;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.game.round.description.RoundDescription;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class SequenceDescription extends TournamentDescription 
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.6f;

	public SequenceDescription(SplitMenuPanel container)
	{	super(container);
		
		// data
		{	SubPanel infoPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(infoPanel,BoxLayout.LINE_AXIS); 
				infoPanel.setLayout(layout);
			}

			int margin = GuiTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			infoPanel.setOpaque(false);
			SequenceTournament tournament = (SequenceTournament)Configuration.getGameConfiguration().getTournament();
			
			// players panel
			{	JPanel playersPanel = makePlayersPanel(leftWidth,dataHeight);
				infoPanel.add(playersPanel);
			}
			
			infoPanel.add(Box.createHorizontalGlue());
			
			// right panel
			{	JPanel rightPanel = new JPanel();
				{	BoxLayout layout = new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS); 
					rightPanel.setLayout(layout);
				}
				rightPanel.setOpaque(false);
				Dimension dim = new Dimension(rightWidth,dataHeight);
				rightPanel.setPreferredSize(dim);
				rightPanel.setMinimumSize(dim);
				rightPanel.setMaximumSize(dim);
				int upHeight = (dataHeight - margin)/2;
				int downHeight = dataHeight - upHeight - margin;
				
				// points panel
				{	SubPanel pointsPanel = RoundDescription.makePointsPanel(rightWidth,downHeight,tournament.getPointProcessor(),"Tournament");
					rightPanel.add(pointsPanel);
				}

				rightPanel.add(Box.createVerticalGlue());
				
				// limit panel
				{	SubPanel limitsPanel = RoundDescription.makeLimitsPanel(rightWidth,downHeight,tournament.getLimits(),"Tournament");
					rightPanel.add(limitsPanel);
				}
				infoPanel.add(rightPanel);
			}

			setDataPart(infoPanel);
		}
	}

	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
	{	// nothing to do here
	}

	private SubPanel makePlayersPanel(int width, int height)
	{	SequenceTournament tournament = (SequenceTournament)Configuration.getGameConfiguration().getTournament();
		int lines = 16+1;
		int cols = 3+1;
		UntitledSubPanelTable playersPanel = new UntitledSubPanelTable(width,height,cols,lines,true);
		// headers
		{	{	JLabel lbl = playersPanel.getLabel(0,0);
				lbl.setOpaque(false);
			}
			String keys[] = 
			{	GuiKeys.GAME_TOURNAMENT_DESCRIPTION_PLAYERS_HEADER_PROFILE,
				GuiKeys.GAME_TOURNAMENT_DESCRIPTION_PLAYERS_HEADER_NAME,
				GuiKeys.GAME_TOURNAMENT_DESCRIPTION_PLAYERS_HEADER_RANK
			};
			for(int i=1;i<keys.length+1;i++)
				playersPanel.setLabelKey(0,i,keys[i-1],true);
		}
		// empty
		{	playersPanel.setSubColumnsMaxWidth(2,Integer.MAX_VALUE);
		}
		// data
		{	ArrayList<Profile> players = tournament.getProfiles();
			Iterator<Profile> i = players.iterator();
			int line = 1;
			while(i.hasNext())
			{	int col = 0;
				Profile profile = i.next();
				// color
				Color clr = profile.getSpriteColor().getColor();
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				playersPanel.setLineBackground(line,bg);
				// portrait
				{	BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
					String tooltip = profile.getName();
					playersPanel.setLabelIcon(line,col,image,tooltip);
					col++;
				}
				// profile type
				{	String aiName = profile.getAiName();
					String key;
					if(aiName==null)
						key = GuiKeys.GAME_TOURNAMENT_DESCRIPTION_PLAYERS_DATA_HUMAN;
					else
						key = GuiKeys.GAME_TOURNAMENT_DESCRIPTION_PLAYERS_DATA_COMPUTER;
					playersPanel.setLabelKey(line,col,key,true);
					col++;
				}
				// name
				{	String text = profile.getName();
					String tooltip = profile.getName();
					playersPanel.setLabelText(line,col,text,tooltip);
					col++;
				}
				// rank
				{	NumberFormat nf = NumberFormat.getInstance();
					nf.setMinimumFractionDigits(0);
					String text = "-";
					String tooltip = "-";
					playersPanel.setLabelText(line,col,text,tooltip);
					col++;
				}
				//
				line++;
			}
		}
		return playersPanel;		
	}
}
