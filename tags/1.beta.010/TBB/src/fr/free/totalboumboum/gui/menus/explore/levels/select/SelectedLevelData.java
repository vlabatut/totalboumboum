package fr.free.totalboumboum.gui.menus.explore.levels.select;

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

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.level.LevelPreview;
import fr.free.totalboumboum.engine.container.level.LevelPreviewLoader;
import fr.free.totalboumboum.gui.common.content.subpanel.file.PackBrowserSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.file.PackBrowserSubPanelListener;
import fr.free.totalboumboum.gui.common.content.subpanel.level.LevelSubPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelImage;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;

public class SelectedLevelData extends EntitledDataPanel implements PackBrowserSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;
	
	private LevelSubPanel infosPanel;
	private UntitledSubPanelImage imagePanel;
	private PackBrowserSubPanel packPanel;
	
	private LevelPreview selectedLevelPreview = null;
	
	public SelectedLevelData(SplitMenuPanel container)
	{	super(container);

		// title
		setTitleKey(GuiKeys.MENU_RESOURCES_LEVEL_SELECT_TITLE);
	
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
			{	int listWidth = leftWidth;
				int listHeight = dataHeight;
				packPanel = new PackBrowserSubPanel(listWidth,listHeight);
				String baseFolder = FileTools.getLevelsPath();
				String targetFile = FileTools.FILE_LEVEL+FileTools.EXTENSION_XML;
				packPanel.setFolder(baseFolder,targetFile);
				packPanel.addListener(this);
				mainPanel.add(packPanel);
			}
			
			mainPanel.add(Box.createHorizontalGlue());
			
			// preview panel
			{	SubPanel previewPanel = new SubPanel(rightWidth,dataHeight);
				{	BoxLayout layout = new BoxLayout(previewPanel,BoxLayout.PAGE_AXIS); 
					previewPanel.setLayout(layout);
				}
				previewPanel.setOpaque(false);
				
				int upHeight = (int)(dataHeight*0.5); 
				int downHeight = dataHeight - upHeight - margin; 
				
				infosPanel = new LevelSubPanel(rightWidth,upHeight);
				infosPanel.setShowTitle(false);
				infosPanel.setLevelPreview(selectedLevelPreview,10);
				previewPanel.add(infosPanel);

				previewPanel.add(Box.createVerticalGlue());

				imagePanel = new UntitledSubPanelImage(rightWidth,downHeight);
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

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	// nothing to do here
	}
	
	/////////////////////////////////////////////////////////////////
	// SELECTED LEVEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public LevelPreview getSelectedLevelPreview()
	{	return selectedLevelPreview;
	}

	/////////////////////////////////////////////////////////////////
	// PACK BROWSER LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void packBrowserSelectionChanged()
	{	String pack = packPanel.getSelectedPack();
		String folder = packPanel.getSelectedName();
		if(pack==null || folder==null)
			selectedLevelPreview = null;
		else
		{	try
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
	}
}
