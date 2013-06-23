package org.totalboumboum.gui.game.save;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.gui.common.content.subpanel.archive.ArchiveMiscSubPanel;
import org.totalboumboum.gui.common.content.subpanel.archive.ArchivePlayersSubPanel;
import org.totalboumboum.gui.common.content.subpanel.file.FolderBrowserSubPanel;
import org.totalboumboum.gui.common.content.subpanel.file.FolderBrowserSubPanelListener;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.BasicPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.stream.file.archive.GameArchive;
import org.totalboumboum.stream.file.archive.GameArchiveLoader;
import org.totalboumboum.tools.files.FileNames;
import org.xml.sax.SAXException;

/**
 * Panel used to save tournament data.
 * 
 * @author Vincent Labatut
 */
public class SaveData extends EntitledDataPanel implements FolderBrowserSubPanelListener
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard panel.
	 * 
	 * @param container
	 * 		Container of this panel.
	 * @param baseFolder
	 * 		Folder
	 */
	public SaveData(SplitMenuPanel container, String baseFolder)
	{	super(container);
		this.baseFolder = baseFolder;

		// title
		setTitleKey(GuiKeys.GAME_SAVE_TITLE);
		
		BasicPanel mainPanel;
		// data
		{	mainPanel = new BasicPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiSizeTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			mainPanel.setOpaque(false);
			
			// list panel
			{	folderPanel = new FolderBrowserSubPanel(leftWidth,dataHeight);
				List<String> targetFiles = new ArrayList<String>();
				targetFiles.add(FileNames.FILE_ARCHIVE+FileNames.EXTENSION_XML);
				targetFiles.add(FileNames.FILE_ARCHIVE+FileNames.EXTENSION_DATA);
				folderPanel.setShowParent(false);
				folderPanel.setFolder(baseFolder,targetFiles);
				folderPanel.addListener(this);
				mainPanel.add(folderPanel);
			}
			
			mainPanel.add(Box.createHorizontalGlue());
			
			// right panel
			{	int miscHeight = (int)((dataHeight - margin)*0.27);
				int playersHeight = dataHeight - miscHeight - margin; 
				
				BasicPanel rightPanel = new BasicPanel(rightWidth,dataHeight);
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
		
	/////////////////////////////////////////////////////////////////
	// PANELS				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Panel displaying the folder browser */
	private FolderBrowserSubPanel folderPanel;
	/** Panel displaying the general information */
	private ArchiveMiscSubPanel miscPanel;
	/** Panel displaying the players */
	private ArchivePlayersSubPanel playersPanel;
	/** Split ratio */
	private static final float SPLIT_RATIO = 0.5f;

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	folderPanel.refresh();
		miscPanel.setGameArchive(selectedArchive);
		playersPanel.setGameArchive(selectedArchive);
	}

	/////////////////////////////////////////////////////////////////
	// SELECTED ARCHIVE		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Selected game save */
	private GameArchive selectedArchive = null;	
	/** Folder */
	private String baseFolder;

	/**
	 * Returns the selected game save.
	 * 
	 * @return
	 * 		Selected game save.
	 */
	public GameArchive getSelectedGameArchive()
	{	return selectedArchive;
	}

	/**
	 * Returns the folder.
	 * 
	 * @return
	 * 		Folder.
	 */
	public String getBaseFolder()
	{	return baseFolder;
	}
	
	/////////////////////////////////////////////////////////////////
	// PACK BROWSER LISTENER		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void folderBrowserParentReached()
	{	// no use here
	}
	
	@Override
	public void folderBrowserSelectionChanged()
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
		fireDataPanelSelectionChange(null);
	}

	@Override
	public void folderBrowserPageChanged()
	{	// useless here
	}
}
