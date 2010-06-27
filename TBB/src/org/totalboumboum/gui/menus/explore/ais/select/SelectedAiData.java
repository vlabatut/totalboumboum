package org.totalboumboum.gui.menus.explore.ais.select;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.ai.AiPreview;
import org.totalboumboum.ai.AiPreviewLoader;
import org.totalboumboum.gui.common.content.subpanel.ai.AiSubPanel;
import org.totalboumboum.gui.common.content.subpanel.file.PackBrowserSubPanel;
import org.totalboumboum.gui.common.content.subpanel.file.PackBrowserSubPanelListener;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.BasicPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TextSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel.Mode;
import org.totalboumboum.gui.common.structure.subpanel.content.TextContentPanel;
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
public class SelectedAiData extends EntitledDataPanel implements PackBrowserSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;
	
	private AiSubPanel infosPanel;
	private TextSubPanel notesPanel;
	private PackBrowserSubPanel packPanel;
	
	private AiPreview selectedAi;
	
	public SelectedAiData(SplitMenuPanel container)
	{	super(container);

		// title
		setTitleKey(GuiKeys.MENU_RESOURCES_AI_SELECT_TITLE);
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
				String baseFolder = FilePaths.getAisPath();
				String additionalFolder = FileNames.FILE_AIS;
				List<String> targetFiles = new ArrayList<String>();
				targetFiles.add(FileNames.FILE_AI+FileNames.EXTENSION_XML);
				targetFiles.add(FileNames.FILE_AI_MAIN_CLASS+FileNames.EXTENSION_CLASS);
				packPanel.setFolder(baseFolder,additionalFolder,targetFiles);
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
				
				infosPanel = new AiSubPanel(rightWidth,upHeight);
				previewPanel.add(infosPanel);

				previewPanel.add(Box.createVerticalGlue());

				notesPanel = new TextSubPanel(rightWidth,downHeight,Mode.TITLE);
				String key = GuiKeys.MENU_RESOURCES_AI_SELECT_NOTES;
				notesPanel.setTitleKey(key,true);
				float fontSize = notesPanel.getTitleFontSize()/2;
				notesPanel.setFontSize(fontSize);
				TextContentPanel textPanel = notesPanel.getDataPanel();
				textPanel.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
				previewPanel.add(notesPanel);
				
				mainPanel.add(previewPanel);
			}
			
			setDataPart(mainPanel);
		}
	}

	private void refreshPreview()
	{	// infos
		infosPanel.setAiPreview(selectedAi);
		// notes
		List<String> notesValues;
		TextContentPanel textPanel = notesPanel.getDataPanel();
		// no player selected
		if(selectedAi==null)
		{	notesValues = new ArrayList<String>();
			textPanel.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
		}
		// one player selected
		else
		{	notesValues = selectedAi.getNotes();
			textPanel.setBackground(GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
		}
		// refresh
		String text = "";
		Iterator<String> i = notesValues.iterator();
		while (i.hasNext())
		{	String temp = i.next();
			text = text + temp + "\n";
		}
		notesPanel.setText(text);
	}

	/////////////////////////////////////////////////////////////////
	// SELECTED AI					/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public AiPreview getSelectedAiPreview()
	{	return selectedAi;
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
			selectedAi = null;
		else
		{	
			try
			{	selectedAi = AiPreviewLoader.loadAiPreview(pack,folder);
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
		}
		refreshPreview();
		fireDataPanelSelectionChange();
	}
}
