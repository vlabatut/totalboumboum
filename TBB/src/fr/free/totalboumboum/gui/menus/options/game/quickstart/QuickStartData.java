package fr.free.totalboumboum.gui.menus.options.game.quickstart;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.game.quickstart.QuickStartConfiguration;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileLoader;
import fr.free.totalboumboum.configuration.profile.ProfilesConfiguration;
import fr.free.totalboumboum.configuration.profile.ProfilesSelection;
import fr.free.totalboumboum.gui.common.content.subpanel.players.PlayersSelectionSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.players.PlayersSelectionSubPanelListener;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.BasicPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.TableSubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.SubPanel.Mode;
import fr.free.totalboumboum.gui.menus.options.game.quickstart.hero.SelectHeroSplitPanel;
import fr.free.totalboumboum.gui.menus.options.game.quickstart.profile.SelectProfileSplitPanel;
import fr.free.totalboumboum.gui.menus.options.game.quickstart.round.SelectRoundSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.StringTools;

public class QuickStartData extends EntitledDataPanel implements PlayersSelectionSubPanelListener, MouseListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.06f;
	
	private PlayersSelectionSubPanel playersPanel;
	private TableSubPanel roundPanel;
	private int roundHeight;
	private int playersHeight;

	public QuickStartData(SplitMenuPanel container)
	{	super(container);
		
		// title
		String key = GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_TITLE;
		setTitleKey(key);
		
		BasicPanel mainPanel;
		// data
		{	mainPanel = new BasicPanel(dataWidth,dataHeight);
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
	{	SelectRoundSplitPanel selectRoundPanel = new SelectRoundSplitPanel(container.getContainer(),container,quickStartConfiguration);
		getContainer().replaceWith(selectRoundPanel);
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}

	/////////////////////////////////////////////////////////////////
	// CONFIGURATION				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private QuickStartConfiguration quickStartConfiguration;

	public void setQuickStartConfiguration(QuickStartConfiguration quickStartConfiguration)
	{	this.quickStartConfiguration = quickStartConfiguration;
		try
		{	ProfilesSelection profilesSelection = quickStartConfiguration.getProfilesSelection();
			ArrayList<Profile> profiles = ProfileLoader.loadProfiles(profilesSelection);
			playersPanel.setPlayers(profiles);
			refreshRound();
		}
		catch (IllegalArgumentException e1)
		{	e1.printStackTrace();
		}
		catch (SecurityException e1)
		{	e1.printStackTrace();
		}
		catch (ParserConfigurationException e1)
		{	e1.printStackTrace();
		}
		catch (SAXException e1)
		{	e1.printStackTrace();
		}
		catch (IOException e1)
		{	e1.printStackTrace();
		}
		catch (IllegalAccessException e1)
		{	e1.printStackTrace();
		}
		catch (NoSuchFieldException e1)
		{	e1.printStackTrace();
		}
		catch (ClassNotFoundException e1)
		{	e1.printStackTrace();
		}		
	}
	
	public QuickStartConfiguration getQuickStartConfiguration()
	{	ArrayList<Profile> players = playersPanel.getPlayers();
		ProfilesSelection profilesSelection = ProfilesConfiguration.getSelection(players);
		quickStartConfiguration.setProfilesSelection(profilesSelection);
		return quickStartConfiguration;	
	}

	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void refreshRound()
	{	String roundFile = quickStartConfiguration.getRoundName().toString();
		if(roundFile==null)
		{	roundPanel.setLabelText(0,0,null,null);
			roundPanel.setLabelText(0,1,null,null);
		}
		else
		{	roundPanel.setLabelText(0,0,roundFile.toString(),roundFile.toString());
			TreeSet<Integer> allowedPlayers = quickStartConfiguration.getAllowedPlayers();
			String allowedPlayersStr = StringTools.formatAllowedPlayerNumbers(quickStartConfiguration.getAllowedPlayers());
			roundPanel.setLabelText(0,1,allowedPlayersStr,allowedPlayersStr);
			int playersNumber = playersPanel.getPlayers().size();
			Color bg;
			if(allowedPlayers.contains(playersNumber))
				bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
			else
				bg = GuiTools.COLOR_TABLE_SELECTED_BACKGROUND;
			roundPanel.setLabelBackground(0,1,bg);
		}
		fireDataPanelSelectionChange();
	}

	private TableSubPanel makeRoundPanel(int width, int height)
	{	int cols = 3;
		int lines = 1;
		int margin = GuiTools.subPanelMargin;
		TableSubPanel result = new TableSubPanel(width,height,Mode.BORDER,lines,cols,false);
//		int headerHeight = result.getHeaderHeight();
		int lineHeight = result.getLineHeight();
		int browseWidth = lineHeight;
		int nameWidth = (width - (browseWidth + 4*margin))/2;		
		int allowedPlayersWidth = width - (browseWidth + 4*margin + nameWidth);		
		
		{	int line = 0;
			int col = 0;
			// name
			{	// size
				result.setColSubMinWidth(col,nameWidth);
				result.setColSubPrefWidth(col,nameWidth);
				result.setColSubMaxWidth(col,nameWidth);
				// color
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				result.setLabelBackground(line,col,bg);
				// next
				col++;
			}
			// allowed players
			{	// size
				result.setColSubMinWidth(col,allowedPlayersWidth);
				result.setColSubPrefWidth(col,allowedPlayersWidth);
				result.setColSubMaxWidth(col,allowedPlayersWidth);
				// color
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				result.setLabelBackground(line,col,bg);
				// next
				col++;
			}
			// browse
			{	// size
				result.setColSubMinWidth(col,browseWidth);
				result.setColSubPrefWidth(col,browseWidth);
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
	{	ArrayList<Profile> players = playersPanel.getPlayers();
		SelectProfileSplitPanel selectProfilePanel = new SelectProfileSplitPanel(container.getContainer(),container,index,players);
		getContainer().replaceWith(selectProfilePanel);
	}

	@Override
	public void playerSelectionPlayerRemoved(int index)
	{	refreshRound();
	}

	@Override
	public void playerSelectionProfileSet(int index)
	{	ArrayList<Profile> players = playersPanel.getPlayers();
		SelectProfileSplitPanel selectProfilePanel = new SelectProfileSplitPanel(container.getContainer(),container,index,players);
		getContainer().replaceWith(selectProfilePanel);
	}
}
