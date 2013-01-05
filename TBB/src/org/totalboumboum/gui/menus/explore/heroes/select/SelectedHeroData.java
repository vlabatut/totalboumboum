package org.totalboumboum.gui.menus.explore.heroes.select;

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

import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.content.sprite.SpritePreview;
import org.totalboumboum.engine.content.sprite.SpritePreviewLoader;
import org.totalboumboum.gui.common.content.subpanel.file.PackBrowserSubPanel;
import org.totalboumboum.gui.common.content.subpanel.file.PackBrowserSubPanelListener;
import org.totalboumboum.gui.common.content.subpanel.sprite.SpriteImageSubPanel;
import org.totalboumboum.gui.common.content.subpanel.sprite.SpriteInfoSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.BasicPanel;
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
public class SelectedHeroData extends EntitledDataPanel implements PackBrowserSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;
	
	private SpriteInfoSubPanel infosPanel;
	private SpriteImageSubPanel imagePanel;
	private PackBrowserSubPanel packPanel;

	private SpritePreview selectedSprite;
		
	public SelectedHeroData(SplitMenuPanel container)
	{	super(container);

		// title
		setTitleKey(GuiKeys.MENU_RESOURCES_HERO_TITLE);
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
			{	int listWidth = leftWidth;
				int listHeight = dataHeight;
				packPanel = new PackBrowserSubPanel(listWidth,listHeight);
				String baseFolder = FilePaths.getHeroesPath();
				String targetFile = FileNames.FILE_SPRITE+FileNames.EXTENSION_XML;
				packPanel.setFolder(baseFolder,null,targetFile);
				packPanel.addListener(this);
				mainPanel.add(packPanel);
			}
			
			mainPanel.add(Box.createHorizontalGlue());
			
			// preview panel
			{	BasicPanel previewPanel = new BasicPanel(rightWidth,dataHeight);
				{	BoxLayout layout = new BoxLayout(previewPanel,BoxLayout.PAGE_AXIS); 
					previewPanel.setLayout(layout);
				}
				previewPanel.setOpaque(false);
				
				int upHeight = (int)(dataHeight*0.5); 
				int downHeight = dataHeight - upHeight - margin; 
				
				infosPanel = new SpriteInfoSubPanel(rightWidth,upHeight);
				previewPanel.add(infosPanel);

				previewPanel.add(Box.createVerticalGlue());

				imagePanel = new SpriteImageSubPanel(rightWidth,downHeight);
				previewPanel.add(imagePanel);
				
				mainPanel.add(previewPanel);
			}
			
			setDataPart(mainPanel);
		}
	}

	/////////////////////////////////////////////////////////////////
	// SELECTED HERO				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public SpritePreview getSelectedHeroPreview()
	{	return selectedSprite;
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	// nothing to do here
	}
	
	/////////////////////////////////////////////////////////////////
	// PACK BROWSER LISTENER		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void packBrowserSelectionChanged()
	{	String pack = packPanel.getSelectedPack();
		String folder = packPanel.getSelectedName();
		if(pack==null || folder==null)
			selectedSprite = null;
		else
		{	try
			{	selectedSprite = SpritePreviewLoader.loadHeroPreview(pack,folder);
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
		// refresh
		infosPanel.setSpritePreview(selectedSprite);
		imagePanel.setSpritePreview(selectedSprite);
		fireDataPanelSelectionChange(null);
	}
}
