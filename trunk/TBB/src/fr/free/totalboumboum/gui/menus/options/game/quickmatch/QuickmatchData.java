package fr.free.totalboumboum.gui.menus.options.game.quickmatch;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.gui.common.content.subpanel.players.PlayersSelectionSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.players.PlayersSelectionSubPanelListener;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.menus.options.game.quickstart.round.SelectRoundSplitPanel;
import fr.free.totalboumboum.gui.menus.quickmatch.players.hero.SelectHeroSplitPanel;
import fr.free.totalboumboum.gui.menus.quickmatch.players.profile.SelectProfileSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class QuickmatchData extends EntitledDataPanel implements PlayersSelectionSubPanelListener, MouseListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.06f;
	
	private PlayersSelectionSubPanel playersPanel;
	private UntitledSubPanelTable roundPanel;
	private SubPanel mainPanel;
	private int roundHeight;
	private int playersHeight;

	public QuickmatchData(SplitMenuPanel container)
	{	super(container);
		
		// title
		String key = GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_TITLE;
		setTitleKey(key);
		
		// data
		{	mainPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			roundHeight = (int)(dataHeight*SPLIT_RATIO); 
			playersHeight = dataHeight - roundHeight - margin;
			mainPanel.setOpaque(false);
			
			// round panel
			{	roundPanel = makeRoundPanel(dataWidth,roundHeight);
				mainPanel.add(roundPanel);
			}

			mainPanel.add(Box.createVerticalGlue());
			
			// players panel
			{	playersPanel = new PlayersSelectionSubPanel(dataWidth,playersHeight);
				playersPanel.addListener(this);
				mainPanel.add(playersPanel);
			}
		}

		setDataPart(mainPanel);
	}

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	playersPanel.refresh();
		refreshRound();
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
	{	
//		JLabel label = (JLabel)e.getComponent();
//		int[] pos = playersPanel.getLabelPosition(label);
		// round
		SelectRoundSplitPanel selectRoundPanel = new SelectRoundSplitPanel(container.getContainer(),container,roundFile);
		getContainer().replaceWith(selectRoundPanel);
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<Profile> players;

	public void setSelectedProfiles(ArrayList<Profile> selectedProfiles)
	{	players = selectedProfiles;
		playersPanel.setPlayers(players);
	}
	
	public ArrayList<Profile> getSelectedProfiles()
	{	return players;	
	}

	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StringBuffer roundFile;
	
	public void setRound(String roundFolder)
	{	roundFile = new StringBuffer(roundFolder);
		refreshRound();
	}
	
	public String getSelectedRound()
	{	return roundFile.toString();	
	}
	
	private void refreshRound()
	{	if(roundFile!=null)
			roundPanel.setLabelText(0,0,roundFile.toString(),roundFile.toString());
		else
			roundPanel.setLabelText(0,0,null,null);
	}

	private UntitledSubPanelTable makeRoundPanel(int width, int height)
	{	int cols = 2;
		int lines = 1;
		int margin = GuiTools.subPanelMargin;
		UntitledSubPanelTable result = new UntitledSubPanelTable(width,height,cols,lines,false);
		@SuppressWarnings("unused")
		int headerHeight = result.getHeaderHeight();
		int lineHeight = result.getLineHeight();
		int browseWidth = lineHeight;
		int fileWidth = width - (browseWidth + 3*margin);		
		
		{	int line = 0;
			int col = 0;
			// name
			{	// size
				result.setColSubMinWidth(col,fileWidth);
				result.setColSubPreferredWidth(col,fileWidth);
				result.setColSubMaxWidth(col,fileWidth);
				// color
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				result.setLabelBackground(line,col,bg);
				// next
				col++;
			}
			// browse
			{	// size
				result.setColSubMinWidth(col,browseWidth);
				result.setColSubPreferredWidth(col,browseWidth);
				result.setColSubMaxWidth(col,browseWidth);
				// icon
				String key = GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_ROUND_BROWSE;
				result.setLabelKey(line,col,key,true);
				// color
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				result.setLabelBackground(line,col,bg);
				// listener
				JLabel lbl = result.getLabel(line,col);
				lbl.addMouseListener(this);
				// next
				col++;
			}
		}
		
		return result;	
	}

	/////////////////////////////////////////////////////////////////
	// PLAYER SELECTION LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void playerSelectionHeroSet(int index)
	{	Profile profile = playersPanel.getPlayer(index);
		SelectHeroSplitPanel selectHeroPanel = new SelectHeroSplitPanel(container.getContainer(),container,profile);
		getContainer().replaceWith(selectHeroPanel);	
	}

	@Override
	public void playerSelectionPlayerAdded(int index)
	{	SelectProfileSplitPanel selectProfilePanel = new SelectProfileSplitPanel(container.getContainer(),container,index,players);
		getContainer().replaceWith(selectProfilePanel);
	}

	@Override
	public void playerSelectionProfileSet(int index)
	{	SelectProfileSplitPanel selectProfilePanel = new SelectProfileSplitPanel(container.getContainer(),container,index,players);
		getContainer().replaceWith(selectProfilePanel);
	}
}
