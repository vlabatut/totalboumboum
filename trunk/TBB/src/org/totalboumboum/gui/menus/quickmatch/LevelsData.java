package org.totalboumboum.gui.menus.quickmatch;

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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.game.quickmatch.LevelsSelection;
import org.totalboumboum.configuration.game.quickmatch.QuickMatchConfiguration;
import org.totalboumboum.engine.container.level.preview.LevelPreview;
import org.totalboumboum.engine.container.level.preview.LevelPreviewLoader;
import org.totalboumboum.gui.common.content.subpanel.file.FileBrowserSubPanel;
import org.totalboumboum.gui.common.content.subpanel.file.FileBrowserSubPanelListener;
import org.totalboumboum.gui.common.content.subpanel.file.PackBrowserSubPanel;
import org.totalboumboum.gui.common.content.subpanel.file.PackBrowserSubPanelListener;
import org.totalboumboum.gui.common.content.subpanel.level.LevelSubPanel;
import org.totalboumboum.gui.common.content.subpanel.transfer.TransferSubPanel;
import org.totalboumboum.gui.common.content.subpanel.transfer.TransferSubPanelListener;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.BasicPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.ImageSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel.Mode;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class LevelsData extends EntitledDataPanel implements PackBrowserSubPanelListener, TransferSubPanelListener, FileBrowserSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.30f;
	
	private LevelSubPanel infosPanel;
	private ImageSubPanel imagePanel;
	private FileBrowserSubPanel selectedPanel;
	private PackBrowserSubPanel selectionPanel;
	private TransferSubPanel commandsPanel;
	
	public LevelsData(SplitMenuPanel container)
	{	super(container);
		
		// title
		setTitleKey(GuiKeys.MENU_QUICKMATCH_LEVELS_TITLE);
		
		BasicPanel mainPanel;
		// data
		{	mainPanel = new BasicPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int listWidth = (int)(dataWidth*SPLIT_RATIO); 
			int commandWidth = 2*GuiTools.panelMargin;		
			int commandHeight = 2*(commandWidth-2*GuiTools.subPanelMargin) + 3*GuiTools.subPanelMargin;
			int previewWidth = dataWidth - 2*listWidth - 1*GuiTools.panelMargin - commandWidth - 2*GuiTools.subPanelMargin; 
			mainPanel.setOpaque(false);
			
			// selected levels
			{	selectedPanel = new FileBrowserSubPanel(listWidth,dataHeight);
				selectedPanel.setShowParent(false);
				fileNames = new HashMap<String,String>();	
				selectedPanel.setFileNames(fileNames);
				selectedPanel.addListener(this);
				mainPanel.add(selectedPanel);
			}
			
			mainPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
			
			// commands panel
			{	commandsPanel = new TransferSubPanel(commandWidth,commandHeight);
				commandsPanel.addListener(this);
				mainPanel.add(commandsPanel);
			}
			
			mainPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
			
			// selection panel
			{	selectionPanel = new PackBrowserSubPanel(listWidth,dataHeight);
				String baseFolder = FilePaths.getLevelsPath();
				String targetFile = FileNames.FILE_LEVEL+FileNames.EXTENSION_XML;
				selectionPanel.setFolder(baseFolder,null,targetFile);
				selectionPanel.addListener(this);
				mainPanel.add(selectionPanel);
			}
			
			mainPanel.add(Box.createHorizontalGlue());
			
			// preview panel
			{	BasicPanel previewPanel = new BasicPanel(previewWidth,dataHeight);
				{	BoxLayout layout = new BoxLayout(previewPanel,BoxLayout.PAGE_AXIS); 
					previewPanel.setLayout(layout);
				}
				previewPanel.setOpaque(false);
				
				int upHeight = (int)(dataHeight*0.5); 
				int downHeight = dataHeight - upHeight - GuiTools.panelMargin; 
				
				infosPanel = new LevelSubPanel(previewWidth,upHeight);
				infosPanel.setShowTitle(false);
				infosPanel.setLevelPreview(selectedLevelPreview,10);
				previewPanel.add(infosPanel);
				
				previewPanel.add(Box.createVerticalGlue());
				
				imagePanel = new ImageSubPanel(previewWidth,downHeight,Mode.BORDER);
				previewPanel.add(imagePanel);
				
				mainPanel.add(previewPanel);
			}
			
			setDataPart(mainPanel);
		}
	}
		
	private void refreshPreview()
	{	BufferedImage image;
		// no level selected
		if(selectedLevelPreview==null)
			image = null;
		// one level selected
		else
			image = selectedLevelPreview.getVisualPreview();
		// refresh
		infosPanel.setLevelPreview(selectedLevelPreview,10);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_IMAGE+GuiKeys.TOOLTIP);
		imagePanel.setImage(image,tooltip);
	}
	
	private void refreshCommands()
	{	// right arrow
		if(selectedPanel.getSelectedFileName()!=null)
			commandsPanel.setEnabledRight(true);
		else
			commandsPanel.setEnabledRight(false);
		// left arrow
//		if(selectedLevelPreview!=null 
//			&& selectedLevelPreview.getAllowedPlayerNumbers().contains(playerCount))
//			commandsPanel.setEnabledLeft(true);
//		else
//			commandsPanel.setEnabledLeft(false);		
	}

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	// nothing to do here
	}
	
	/////////////////////////////////////////////////////////////////
	// LEVELS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LevelsSelection levelsSelection;
	private HashMap<String,String> fileNames;
	private LevelPreview selectedLevelPreview = null;
	private int playerCount = 0;
	
	public void setQuickMatchConfiguration(QuickMatchConfiguration quickMatchConfiguration)
	{	// player count
		playerCount = quickMatchConfiguration.getProfilesSelection().getProfileCount();
		// level selection
		levelsSelection = quickMatchConfiguration.getLevelsSelection();
		checkLevelsSelection();
		// filenames
		fileNames = new HashMap<String,String>();	
		numberFileNames();
		selectedPanel.setFileNames(fileNames);
		// panels
		refreshCommands();
	}
	
	private void checkLevelsSelection()
	{	int i=0;
		while(i<levelsSelection.getLevelCount())
		{	String pack = levelsSelection.getPackName(i);
			String folder = levelsSelection.getFolderName(i);
			try
			{	LevelPreview levelPreview = LevelPreviewLoader.loadLevelPreviewOnlyAllowedPlayers(pack,folder);
				if(levelPreview.getAllowedPlayerNumbers().contains(playerCount))
					i++;
				else
					levelsSelection.removeLevel(i);
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
		}
	}
	
	private void numberFileNames()
	{	int l = levelsSelection.getLevelCount();
		int digits = 0;
		while(l!=0)
		{	l = l / 10;
			digits++;
		}
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(digits);
		fileNames.clear();
		for(int i=0;i<levelsSelection.getLevelCount();i++)
		{	String key = Integer.toString(i+1);
			String value = nf.format(i+1)+"."+levelsSelection.getFolderName(i);
			fileNames.put(key,value);
		}
	}
	
	public LevelPreview getSelectedLevelPreview()
	{	return selectedLevelPreview;
	}

	public LevelsSelection getLevelsSelection()
	{	return levelsSelection;
	}

	/////////////////////////////////////////////////////////////////
	// PACK BROWSER LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void packBrowserSelectionChanged()
	{	String pack = selectionPanel.getSelectedPack();
		String folder = selectionPanel.getSelectedName();
		if(pack==null || folder==null)
		{	selectedLevelPreview = null;
		}
		else
		{	// preview
			try
			{	selectedLevelPreview = LevelPreviewLoader.loadLevelPreviewWithoutItemset(pack,folder);
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
		}
		refreshPreview();
		refreshCommands();
	}

	/////////////////////////////////////////////////////////////////
	// TRANSFER LISTENER			/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void transferLeftClicked()
	{	String folderName = selectionPanel.getSelectedName();
		String packName = selectionPanel.getSelectedPack();
		if(folderName!=null && packName!=null)
		{	int index = getTransferIndex();
			if(index==-1)
				index = fileNames.size();
			else
				index++;
			// levels selection
			Set<Integer> allowedPlayers = selectedLevelPreview.getAllowedPlayerNumbers();
			levelsSelection.addLevel(index,packName,folderName,allowedPlayers);
			// file names
			numberFileNames();
			selectedPanel.setFileNames(fileNames);
			String fileName = Integer.toString(index+1);
			selectedPanel.setSelectedFileName(fileName);
			fireDataPanelSelectionChange(null);
		}
	}

	@Override
	public void transferRightClicked()
	{	int index = getTransferIndex();
		if(index>=0)
		{	// levels selection
			levelsSelection.removeLevel(index);
			// file names
			numberFileNames();
			selectedPanel.setFileNames(fileNames);
			if(index>=levelsSelection.getLevelCount())
				index--;
			if(index>=0)
			{	String fileName = Integer.toString(index+1);
				selectedPanel.setSelectedFileName(fileName);
			}
			fireDataPanelSelectionChange(null);
		}
	}
	
	private int getTransferIndex()
	{	int result = -1;
		String idx = selectedPanel.getSelectedFileName();
		if(idx!=null)
			result = Integer.parseInt(idx)-1;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// FILE BROWSER SUBPANEL LISTENER	/////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void fileBrowserParentReached()
	{	
	}

	@Override
	public void fileBrowserSelectionChanged()
	{	refreshCommands();
	}
}
