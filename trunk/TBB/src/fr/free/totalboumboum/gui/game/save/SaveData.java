package fr.free.totalboumboum.gui.game.save;

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

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.archive.GameArchive;
import fr.free.totalboumboum.game.archive.GameArchiveLoader;
import fr.free.totalboumboum.gui.common.content.subpanel.archive.ArchiveMiscSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.archive.ArchivePlayersSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.browser.FolderBrowserSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.browser.FolderBrowserSubPanelListener;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;

public class SaveData extends EntitledDataPanel implements FolderBrowserSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;

	private FolderBrowserSubPanel folderPanel;
	private ArchiveMiscSubPanel miscPanel;
	private ArchivePlayersSubPanel playersPanel;

	private String baseFolder;
	
	public SaveData(SplitMenuPanel container, String baseFolder)
	{	super(container);
		this.baseFolder = baseFolder;

		// title
		setTitleKey(GuiKeys.GAME_SAVE_TITLE);
		
		SubPanel mainPanel;
		// data
		{	mainPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			mainPanel.setOpaque(false);
			
			// list panel
			{	folderPanel = new FolderBrowserSubPanel(leftWidth,dataHeight);
				ArrayList<String> targetFiles = new ArrayList<String>();
				targetFiles.add(FileTools.FILE_ARCHIVE+FileTools.EXTENSION_XML);
				targetFiles.add(FileTools.FILE_ARCHIVE+FileTools.EXTENSION_DATA);
				folderPanel.setShowParent(false);
				folderPanel.setFolder(baseFolder,targetFiles);
				folderPanel.addListener(this);
				mainPanel.add(folderPanel);
			}
			
			mainPanel.add(Box.createHorizontalGlue());
			
			// right panel
			{	int miscHeight = (int)((dataHeight - margin)*0.27);
				int playersHeight = dataHeight - miscHeight - margin; 
				
				SubPanel rightPanel = new SubPanel(rightWidth,dataHeight);
				rightPanel.setOpaque(false);
				mainPanel.add(rightPanel);
				{	BoxLayout layout = new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS); 
					rightPanel.setLayout(layout);
				}
				
				rightPanel.add(Box.createVerticalGlue());

				{	miscPanel = new ArchiveMiscSubPanel(rightWidth,miscHeight,5);
					rightPanel.add(miscPanel);
				}

				rightPanel.add(Box.createRigidArea(new Dimension(margin,margin)));

				{	playersPanel = new ArchivePlayersSubPanel(rightWidth,playersHeight);
					rightPanel.add(playersPanel);
				}

				rightPanel.add(Box.createVerticalGlue());
			}
			
			setDataPart(mainPanel);
			
		}
	}
		
	@Override
	public void refresh()
	{	folderPanel.refresh();
		miscPanel.setGameArchive(selectedArchive);
		playersPanel.setGameArchive(selectedArchive);
	}

	/////////////////////////////////////////////////////////////////
	// SELECTED ARCHIVE		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GameArchive selectedArchive = null;	

	public GameArchive getSelectedGameArchive()
	{	return selectedArchive;
	}
	
	/////////////////////////////////////////////////////////////////
	// PACK BROWSER LISTENER		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void packBrowserParent()
	{	// no use here
	}
	
	@Override
	public void packBrowserSelectionChange()
	{	String folder = folderPanel.getSelectedName();
		if(folder==null)
			selectedArchive = null;
		else
		{	try
			{	selectedArchive = GameArchiveLoader.loadGameArchive(folder);
			}
			catch (IllegalArgumentException e)
			{	e.printStackTrace();
			}
			catch (SecurityException e)
			{	e.printStackTrace();
			}
			catch (ParserConfigurationException e)
			{	e.printStackTrace();
			}
			catch (SAXException e)
			{	e.printStackTrace();
			}
			catch (IOException e)
			{	e.printStackTrace();
			} 
			catch (ClassNotFoundException e)
			{	e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{	e.printStackTrace();
			}
			catch (NoSuchFieldException e)
			{	e.printStackTrace();
			}
		}
		miscPanel.setGameArchive(selectedArchive);
		playersPanel.setGameArchive(selectedArchive);
		fireDataPanelSelectionChange();
	}
}
