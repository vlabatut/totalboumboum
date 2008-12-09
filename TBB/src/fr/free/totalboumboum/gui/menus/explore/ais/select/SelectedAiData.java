package fr.free.totalboumboum.gui.menus.explore.ais.select;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.ai.AiPreview;
import fr.free.totalboumboum.ai.AiPreviewLoader;
import fr.free.totalboumboum.gui.common.content.subpanel.browser.PackBrowserSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.browser.PackBrowserSubPanelListener;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.EntitledSubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubTextPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;

public class SelectedAiData extends EntitledDataPanel implements PackBrowserSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;
	
	private static final int VIEW_LINE_NAME = 0;
	private static final int VIEW_LINE_PACK = 1;
	private static final int VIEW_LINE_AUTHOR = 2;

	private SubPanel mainPanel;
	private SubPanel previewPanel;
	private UntitledSubPanelTable infosPanel;
	private EntitledSubPanel notesPanel;
	private PackBrowserSubPanel packPanel;
	private int listWidth;
	private int listHeight;
	
	private AiPreview selectedAi;
		
	@SuppressWarnings("unused")
	private int lastAuthorNumber = 1;
	
	public SelectedAiData(SplitMenuPanel container)
	{	super(container);

		// title
		setTitleKey(GuiKeys.MENU_RESOURCES_AI_SELECT_TITLE);
	
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
			{	listWidth = leftWidth;
				listHeight = dataHeight;
				packPanel = new PackBrowserSubPanel(listWidth,listHeight);
				String baseFolder = FileTools.getAiPath();
				ArrayList<String> targetFiles = new ArrayList<String>();
				targetFiles.add(FileTools.FILE_AI+FileTools.EXTENSION_DATA);
				targetFiles.add(FileTools.FILE_AI_MAIN_CLASS+FileTools.EXTENSION_CLASS);
				packPanel.setFolder(baseFolder,targetFiles);
				packPanel.addListener(this);
				mainPanel.add(packPanel);
			}
			
			mainPanel.add(Box.createHorizontalGlue());
			
			// preview panel
			{	previewPanel = new SubPanel(rightWidth,dataHeight);
				{	BoxLayout layout = new BoxLayout(previewPanel,BoxLayout.PAGE_AXIS); 
					previewPanel.setLayout(layout);
				}
				previewPanel.setOpaque(false);
				
				int upHeight = (int)(dataHeight*0.5); 
				int downHeight = dataHeight - upHeight - margin; 
				
				makeInfosPanel(rightWidth,upHeight);
				previewPanel.add(infosPanel);

				previewPanel.add(Box.createVerticalGlue());

				makeNotesPanel(rightWidth,downHeight);
				previewPanel.add(notesPanel);
				
				mainPanel.add(previewPanel);
			}
			
			setDataPart(mainPanel);
		}
	}

	private void makeInfosPanel(int width, int height)
	{	int lines = 10;
		int colSubs = 2;
		int colGroups = 1;
		infosPanel = new UntitledSubPanelTable(width,height,colGroups,colSubs,lines,true);
		
		// data
		String keys[] = 
		{	GuiKeys.MENU_RESOURCES_AI_SELECT_PREVIEW_NAME,
			GuiKeys.MENU_RESOURCES_AI_SELECT_PREVIEW_PACKAGE,
			GuiKeys.MENU_RESOURCES_AI_SELECT_PREVIEW_AUTHOR
		};
		for(int line=0;line<keys.length;line++)
		{	int colSub = 0;
			{	infosPanel.setLabelKey(line,colSub,keys[line],true);
				Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
				infosPanel.setLabelForeground(line,0,fg);
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				infosPanel.setLabelBackground(line,colSub,bg);
				colSub++;
			}
			{	String text = null;
				String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(keys[line]+GuiKeys.TOOLTIP);
				infosPanel.setLabelText(line,colSub,text,tooltip);
				if(line>0)
				{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				infosPanel.setLabelBackground(line,colSub,bg);
				}
				colSub++;
			}
		}
		int maxWidth = width-3*GuiTools.subPanelMargin-infosPanel.getHeaderHeight();
		infosPanel.setColSubMaxWidth(1,maxWidth);
		infosPanel.setColSubPreferredWidth(1,maxWidth);
	}

	//TODO implémenter un EntitledSubPanelText et renommer en UntitledSubPanelText
	private void makeNotesPanel(int width, int height)
	{	notesPanel = new EntitledSubPanel(width,height);
		
		String key = GuiKeys.MENU_RESOURCES_AI_SELECT_PREVIEW_NOTES;
		notesPanel.setTitleKey(key,true);
		
		float fontSize = notesPanel.getTitleFontSize()/2;
		int w = notesPanel.getDataWidth();
		int h = notesPanel.getDataHeight();
		SubTextPanel textPanel = new SubTextPanel(w,h,fontSize);
		notesPanel.setDataPanel(textPanel);
		textPanel.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
	}
	
	private void refreshPreview()
	{	String infosValues[] = new String[10];
		ArrayList<String> notesValues;
		SubTextPanel textPanel = (SubTextPanel)notesPanel.getDataPanel();
		// no player selected
		if(selectedAi==null)
		{	// notes
			notesValues = new ArrayList<String>();
			textPanel.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
			// infos
			for(int i=0;i<infosValues.length;i++)
				infosValues[i] = null;
			for(int i=VIEW_LINE_AUTHOR+1;i<infosValues.length;i++)
			{	infosPanel.setLabelIcon(i,0,null,null);
				Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				infosPanel.setLabelBackground(i,0,bg);	
				infosPanel.setLabelBackground(i,1,bg);	
			}
		}
		// one player selected
		else
		{	// notes
			notesValues = selectedAi.getNotes();
			textPanel.setBackground(GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
			// infos
			infosValues[VIEW_LINE_NAME] = selectedAi.getFolder();
			infosValues[VIEW_LINE_PACK]= selectedAi.getPack();
			ArrayList<String> authors = selectedAi.getAuthors();
			Iterator<String> it = authors.iterator();
			int index = 0;
			while(it.hasNext())
			{	String author = it.next();	
				infosValues[VIEW_LINE_AUTHOR+index] = author;
				index++;
			}
			for(int i=1;i<index;i++)
			{	int line = VIEW_LINE_AUTHOR+i;
				infosPanel.setLabelKey(line,0,GuiKeys.MENU_RESOURCES_AI_SELECT_PREVIEW_AUTHOR,true);
				Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
				infosPanel.setLabelForeground(line,0,fg);
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				infosPanel.setLabelBackground(line,0,bg);
				bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				infosPanel.setLabelBackground(line,1,bg);
			}
			for(int i=VIEW_LINE_AUTHOR+Math.max(index,1);i<infosValues.length;i++)
			{	infosPanel.setLabelIcon(i,0,null,null);
				Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				infosPanel.setLabelBackground(i,0,bg);	
				infosPanel.setLabelBackground(i,1,bg);	
			}
		}
		// infos
		for(int line=0;line<infosValues.length;line++)
		{	int colSub = 1;
			String text = infosValues[line];
			String tooltip = text;
			infosPanel.setLabelText(line,colSub,text,tooltip);
		}
		// notes
		String text = "";
		Iterator<String> i = notesValues.iterator();
		while (i.hasNext())
		{	String temp = i.next();
			text = text + temp + "\n";
		}
		textPanel.setText(text);
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
	public void packBrowserSelectionChange()
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
	}
}
