package fr.free.totalboumboum.gui.menus.explore.ais.select;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.ai.AiPreview;
import fr.free.totalboumboum.ai.AiPreviewLoader;
import fr.free.totalboumboum.gui.common.content.subpanel.ai.AiSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.file.PackBrowserSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.file.PackBrowserSubPanelListener;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.temp.fait.EntitledSubPanelText;
import fr.free.totalboumboum.gui.common.structure.subpanel.temp.fait.SubPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;

public class SelectedAiData extends EntitledDataPanel implements PackBrowserSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;
	
	private AiSubPanel infosPanel;
	private EntitledSubPanelText notesPanel;
	private PackBrowserSubPanel packPanel;
	
	private AiPreview selectedAi;
	
	public SelectedAiData(SplitMenuPanel container)
	{	super(container);

		// title
		setTitleKey(GuiKeys.MENU_RESOURCES_AI_SELECT_TITLE);
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
				String baseFolder = FileTools.getAiPath();
				ArrayList<String> targetFiles = new ArrayList<String>();
				targetFiles.add(FileTools.FILE_AI+FileTools.EXTENSION_XML);
				targetFiles.add(FileTools.FILE_AI_MAIN_CLASS+FileTools.EXTENSION_CLASS);
				packPanel.setFolder(baseFolder,targetFiles);
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
				
				infosPanel = new AiSubPanel(rightWidth,upHeight);
				previewPanel.add(infosPanel);

				previewPanel.add(Box.createVerticalGlue());

				notesPanel = new EntitledSubPanelText(rightWidth,downHeight);
				String key = GuiKeys.MENU_RESOURCES_AI_SELECT_NOTES;
				notesPanel.setTitleKey(key,true);
				float fontSize = notesPanel.getTitleFontSize()/2;
				notesPanel.setFontSize(fontSize);
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
		ArrayList<String> notesValues;
		SubPanel textPanel = notesPanel.getDataPanel();
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
