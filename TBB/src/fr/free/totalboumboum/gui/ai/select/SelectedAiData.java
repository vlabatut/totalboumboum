package fr.free.totalboumboum.gui.ai.select;

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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.ai.AiPreview;
import fr.free.totalboumboum.ai.AiPreviewLoader;
import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileLoader;
import fr.free.totalboumboum.configuration.profile.ProfilesConfiguration;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.EntitledSubPanel;
import fr.free.totalboumboum.gui.common.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.subpanel.SubTextPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;

public class SelectedAiData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;
	
	private static final int LIST_LINE_COUNT = 20;
	private static final int LIST_LINE_PREVIOUS = 0;
	private static final int LIST_LINE_NEXT = LIST_LINE_COUNT-1;

	private static final int VIEW_LINE_NAME = 0;
	private static final int VIEW_LINE_PACK = 1;
	private static final int VIEW_LINE_AUTHOR = 2;

	private static final int LIST_PANEL_INDEX = 0;
	@SuppressWarnings("unused")
	private static final int PREVIEW_PANEL_INDEX = 2;
	
	private static final int INFOS_PANEL_INDEX = 0;
	private static final int NOTES_PANEL_INDEX = 2;

	private SubPanel mainPanel;
	private SubPanel previewPanel;
	private UntitledSubPanelTable infosPanel;
	private EntitledSubPanel notesPanel;
	
	private ArrayList<UntitledSubPanelTable> listPackagePanels;
	private int currentPackagePage = 0;
	private int selectedPackageRow = -1;
	private ArrayList<String> aiPacks;
	private ArrayList<ArrayList<File>> aiFiles;

	private ArrayList<UntitledSubPanelTable> listAiPanels;
	private int currentAiPage = 0;
	private int selectedAiRow = -1;
	private ArrayList<AiPreview> aiPreviews;
		
	private boolean packageMode = true; //false if AIs are displayed 
	
	public SelectedAiData(SplitMenuPanel container)
	{	super(container);

		// title
		setTitleKey(GuiTools.MENU_AI_SELECT_TITLE);
	
		// data
		{	mainPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			mainPanel.setOpaque(false);
			initPackages();
			
			// list panel
			{	makePackageListPanels(leftWidth,dataHeight);
				mainPanel.add(listPackagePanels.get(currentPackagePage));
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
		
	private void initPackages()
	{	aiFiles = new ArrayList<ArrayList<File>>();
		aiPacks = new ArrayList<String>();
		
		// packages
		String aiFolder = FileTools.getAiPath();
		File aiF = new File(aiFolder);
		FileFilter filter = new FileFilter()
		{	@Override
			public boolean accept(File pathname)
			{	boolean result = pathname.exists() && pathname.isDirectory();
				return result;
			}
		};
		File aiFs[] = aiF.listFiles(filter);
		ArrayList<File> aiFolders = new ArrayList<File>();
		for(int i=0;i<aiFs.length;i++)
			aiFolders.add(aiFs[i]);
		Comparator<File> comparator = new Comparator<File>()
		{	@Override
			public int compare(File o1, File o2)
			{	String n1 = o1.getName();
				String n2 = o2.getName();
				int result = n1.compareTo(n2);				
				return result;
			}			
		};	
		Collections.sort(aiFolders,comparator);
		
		// AIs
		Iterator<File> p = aiFolders.iterator();
		while(p.hasNext())
		{	// get the list of folders in this package
			File pack = p.next();
			String packName = pack.getName();
			File ai[] = pack.listFiles(filter);
			ArrayList<File> aiPackages = new ArrayList<File>();
			for(int i=0;i<ai.length;i++)
				aiPackages.add(ai[i]);
			Collections.sort(aiPackages,comparator);
			ArrayList<File> fileList = new ArrayList<File>();
			Iterator<File> q = aiPackages.iterator();
			while(q.hasNext())
			{	// find the XML file class
				File aiPack = q.next();
				File[] content = aiPack.listFiles();
				int i = 0;
				File xmlFile = null;
				String xmlFileName = FileTools.FILE_PROPERTIES+FileTools.EXTENSION_DATA;
				while(i<content.length && xmlFile==null)
				{	if(content[i].getName().equalsIgnoreCase(xmlFileName))
						xmlFile = content[i];
					else
						i++;				
				}
				if(xmlFile!=null)
					fileList.add(xmlFile);
			}
			// add to the list of AIs for this package
			if(fileList.size()>0)
			{	aiFiles.add(fileList);
				aiPacks.add(packName);
			}
		}
	}
	
	private void initAiPreviews()
	{	aiPreviews = new ArrayList<AiPreview>();
		int selectedPackageIndex = currentPackagePage*(LIST_LINE_COUNT-2)+selectedPackageRow;
		Iterator<File> it = aiFiles.get(selectedPackageIndex).iterator();
		while(it.hasNext())
		{	File file = it.next();
			AiPreview aiPreview = AiPreviewLoader.loadAiPreview(file.getPath());
			aiPreviews.add(aiPreview);
		}
	}
	
	private void makePackageListPanels(int width, int height)
	{	int cols = 1;
		listPackagePanels = new ArrayList<UntitledSubPanelTable>();
		
		for(int panelIndex=0;panelIndex<getPackagePageCount();panelIndex++)
		{	UntitledSubPanelTable listPanel = new UntitledSubPanelTable(width,height,cols,LIST_LINE_COUNT,false);
			listPanel.setSubColumnsMaxWidth(0,Integer.MAX_VALUE);
		
			// data
			int line = 1;
			Iterator<String> it = aiPacks.iterator(); 
			while(line<LIST_LINE_NEXT && it.hasNext())
			{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				String name = it.next();
				listPanel.setLabelBackground(line,0,bg);
				listPanel.setLabelText(line,0,name,name);
				JLabel label = listPanel.getLabel(line,0);
				label.addMouseListener(this);
				line++;
			}			
			// page up
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_PREVIOUS,0,bg);
				String key = GuiTools.MENU_AI_SELECT_PACKAGE_PAGEUP;
				listPanel.setLabelKey(LIST_LINE_PREVIOUS,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_PREVIOUS,0);
				label.addMouseListener(this);
			}
			// page down
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_NEXT,0,bg);
				String key = GuiTools.MENU_AI_SELECT_PACKAGE_PAGEDOWN;
				listPanel.setLabelKey(LIST_LINE_NEXT,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_NEXT,0);
				label.addMouseListener(this);
			}
			listPackagePanels.add(listPanel);
		}
	}

	private void makeAiListPanels(int width, int height)
	{	int cols = 1;
		listAiPanels = new ArrayList<UntitledSubPanelTable>();
		
		for(int panelIndex=0;panelIndex<getAiPageCount();panelIndex++)
		{	UntitledSubPanelTable listPanel = new UntitledSubPanelTable(width,height,cols,LIST_LINE_COUNT,false);
			listPanel.setSubColumnsMaxWidth(0,Integer.MAX_VALUE);
		
			// data
			int line = 1;
			Iterator<AiPreview> it = aiPreviews.iterator(); 
			while(line<LIST_LINE_NEXT && it.hasNext())
			{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				AiPreview aiPreview = it.next();
				listPanel.setLabelBackground(line,0,bg);
				listPanel.setLabelText(line,0,aiPreview.getName(),aiPreview.getName());
				JLabel label = listPanel.getLabel(line,0);
				label.addMouseListener(this);
				line++;
			}			
			// page up
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_PREVIOUS,0,bg);
				String key = GuiTools.MENU_AI_SELECT_CLASS_PAGEUP;
				listPanel.setLabelKey(LIST_LINE_PREVIOUS,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_PREVIOUS,0);
				label.addMouseListener(this);
			}
			// page down
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_NEXT,0,bg);
				String key = GuiTools.MENU_AI_SELECT_CLASS_PAGEDOWN;
				listPanel.setLabelKey(LIST_LINE_NEXT,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_NEXT,0);
				label.addMouseListener(this);
			}
			listAiPanels.add(listPanel);
		}
	}

	private void makeInfosPanel(int width, int height)
	{	int lines = 10;
		int colSubs = 2;
		int colGroups = 1;
		infosPanel = new UntitledSubPanelTable(width,height,colGroups,colSubs,lines,true);
		
		// data
		String keys[] = 
		{	GuiTools.MENU_AI_SELECT_PREVIEW_NAME,
			GuiTools.MENU_AI_SELECT_PREVIEW_PACKAGE,
			GuiTools.MENU_AI_SELECT_PREVIEW_AUTHOR
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
				String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(keys[line]+GuiTools.TOOLTIP);
				infosPanel.setLabelText(line,colSub,text,tooltip);
				if(line>0)
				{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				infosPanel.setLabelBackground(line,colSub,bg);
				}
				colSub++;
			}
		}
		int maxWidth = width-3*GuiTools.subPanelMargin-infosPanel.getHeaderHeight();
		infosPanel.setSubColumnsMaxWidth(1,maxWidth);
		infosPanel.setSubColumnsPreferredWidth(1,maxWidth);
	}
	
	private void makeNotesPanel(int width, int height)
	{	notesPanel = new EntitledSubPanel(width,height);
		
		String key = GuiTools.MENU_AI_SELECT_PREVIEW_NOTES;
		notesPanel.setTitleKey(key,true);
		
		float fontSize = notesPanel.getTitleFontSize()/3;
		int w = notesPanel.getDataWidth();
		int h = notesPanel.getDataHeight();
		SubTextPanel textPanel = new SubTextPanel(w,h,fontSize);
		Color bg = notesPanel.getDataPanel().getBackground();
		textPanel.setBackground(bg);
		Color fg = notesPanel.getDataPanel().getForeground();
		textPanel.setForeground(fg);
		notesPanel.setDataPanel(textPanel);
	}

/*	
			text = "";
			ArrayList<String> list = match.getNotes();
			Iterator<String> i = list.iterator();
			while (i.hasNext())
			{	String temp = i.next();
				text = text + temp + "\n";
			}
			try
			{	doc.insertString(0,text,sa);
			}
			catch (BadLocationException e)
			{	e.printStackTrace();
			}
			doc.setParagraphAttributes(0,doc.getLength()-1,sa,true);
*/
	
	private void refreshPreview()
	{	
/*		
		String values[] = new String[21];
		// no player selected
		if(selectedRow<0)
		{	for(int i=0;i<values.length;i++)
				values[i] = null;	
			Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
			previewPanel.setLabelBackground(VIEW_LINE_COLOR,1,bg);
			selectedProfile = null;
			selectedProfileFile = null;
		}
		// one player selected
		else
		{	Entry<String,String> entry = profiles.get((selectedRow-1)+currentPackagePage*(LIST_LINE_COUNT-2));
			try
			{	selectedProfileFile = entry.getKey();
				selectedProfile = ProfileLoader.loadProfile(selectedProfileFile);			
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
			catch (IllegalAccessException e)
			{	e.printStackTrace();
			}
			catch (NoSuchFieldException e)
			{	e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{	e.printStackTrace();
			}
			values[VIEW_LINE_NAME] = selectedProfile.getName();
			values[VIEW_LINE_AI_NAME]= selectedProfile.getAiName();
			values[VIEW_LINE_AI_PACK] = selectedProfile.getAiPackname();
			values[VIEW_LINE_HERO_NAME] = selectedProfile.getSpriteName();
			values[VIEW_LINE_HERO_PACK] = selectedProfile.getSpritePack();
			values[VIEW_LINE_COLOR] = selectedProfile.getSpriteColor().toString();
			Color clr = selectedProfile.getSpriteColor().getColor();
			int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
			Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
			previewPanel.setLabelBackground(VIEW_LINE_COLOR,1,bg);
		}
		// common
		for(int line=0;line<values.length;line++)
		{	int colSub = 1;
			String text = values[line];
			String tooltip = text;
			previewPanel.setLabelText(line,colSub,text,tooltip);
		}
*/		
	}

	@Override
	public void refresh()
	{	refreshPreview();
	}

	@Override
	public void updateData()
	{	// nothing to do here
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
	{	
/*		
		JLabel label = (JLabel)e.getComponent();
		int[] pos = listPanels.get(currentPackagePage).getLabelPosition(label);
		switch(pos[0])
		{	// previous page
			case LIST_LINE_PREVIOUS:
				if(currentPackagePage>0)
				{	unselectList();
					currentPackagePage--;
					refreshList();
				}
				break;
			// next page
			case LIST_LINE_NEXT:
				if(currentPackagePage<getPageCount()-1)
				{	unselectList();
					currentPackagePage++;
					refreshList();
				}
				break;
			// profile selected
			default:
				unselectList();
				selectedRow = pos[0];
				listPanels.get(currentPackagePage).setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
				refreshPreview();
		}
		
*/		
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	private int getPackagePageCount()
	{	int result = aiPacks.size()/(LIST_LINE_COUNT-2);
		if(aiPacks.size()%(LIST_LINE_COUNT-2)>0)
			result++;
		else if(result==0)
			result = 1;
		return result;
	}
	
	private int getAiPageCount()
	{	int result = aiPreviews.size()/(LIST_LINE_COUNT-2);
		if(aiPreviews.size()%(LIST_LINE_COUNT-2)>0)
			result++;
		else if(result==0)
			result = 1;
		return result;
	}
	
	public void unselectList()
	{	
/*		
		if(selectedRow!=-1)
		{	listPanels.get(currentPackagePage).setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
			selectedRow = -1;
			refreshPreview();
		}
*/		
	}
	
	private void refreshList()
	{	
/*		
		mainPanel.remove(LIST_PANEL_INDEX);
		mainPanel.add(listPanels.get(currentPackagePage),LIST_PANEL_INDEX);
		mainPanel.validate();
		mainPanel.repaint();
*/		
	}
	
	public AiPreview getSelectedAiPreview()
	{	int index = currentAiPage*(LIST_LINE_COUNT-2)+selectedAiRow;
		return aiPreviews.get(index);
	}

}
