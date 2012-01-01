package org.totalboumboum.gui.menus.replay.select;

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

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.gui.common.content.subpanel.file.FolderBrowserSubPanel;
import org.totalboumboum.gui.common.content.subpanel.file.FolderBrowserSubPanelListener;
import org.totalboumboum.gui.common.content.subpanel.replay.ReplayMiscSubPanel;
import org.totalboumboum.gui.common.content.subpanel.replay.ReplayPlayersSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.BasicPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.stream.file.replay.FileClientStream;
import org.totalboumboum.stream.file.replay.ReplayLoader;
import org.totalboumboum.tools.files.FileNames;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class SelectedReplayData extends EntitledDataPanel implements FolderBrowserSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;

	@SuppressWarnings("unused")
	private String baseFolder;
	
	public SelectedReplayData(SplitMenuPanel container, String baseFolder)
	{	super(container);
		this.baseFolder = baseFolder;

		// title
		setTitleKey(GuiKeys.MENU_REPLAY_LOAD_TITLE);
		
		BasicPanel mainPanel;
		// data
		{	mainPanel = new BasicPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			mainPanel.setOpaque(false);
			
			// list panel
			{	folderPanel = new FolderBrowserSubPanel(leftWidth,dataHeight);
				List<String> targetFiles = new ArrayList<String>();
				targetFiles.add(FileNames.FILE_REPLAY+FileNames.EXTENSION_XML);
				targetFiles.add(FileNames.FILE_REPLAY+FileNames.EXTENSION_DATA);
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

				{	miscPanel = new ReplayMiscSubPanel(rightWidth,miscHeight,5);
					rightPanel.add(miscPanel);
				}

				rightPanel.add(Box.createRigidArea(new Dimension(margin,margin)));

				{	playersPanel = new ReplayPlayersSubPanel(rightWidth,playersHeight);
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
	private FolderBrowserSubPanel folderPanel;
	private ReplayMiscSubPanel miscPanel;
	private ReplayPlayersSubPanel playersPanel;

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	folderPanel.refresh();
		miscPanel.setReplay(selectedReplay);
		playersPanel.setReplay(selectedReplay);
	}

	/////////////////////////////////////////////////////////////////
	// SELECTED ARCHIVE		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private FileClientStream selectedReplay = null;	

	public FileClientStream getSelectedReplay()
	{	return selectedReplay;
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
			selectedReplay = null;
		else
		{	try
			{	selectedReplay = ReplayLoader.loadReplay(folder);
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
		miscPanel.setReplay(selectedReplay);
		playersPanel.setReplay(selectedReplay);
		fireDataPanelSelectionChange(null);
	}

	@Override
	public void folderBrowserPageChanged()
	{	// useless here
	}
}
