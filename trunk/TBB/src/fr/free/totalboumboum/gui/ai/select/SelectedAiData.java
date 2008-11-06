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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.ai.AbstractAiManager;
import fr.free.totalboumboum.ai.AiPreview;
import fr.free.totalboumboum.ai.AiPreviewLoader;
import fr.free.totalboumboum.ai.AiPreviewSaver;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.EntitledSubPanel;
import fr.free.totalboumboum.gui.common.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.subpanel.SubTextPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ClassTools;
import fr.free.totalboumboum.tools.FileTools;

public class SelectedAiData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;
	
	private static final int LIST_LINE_COUNT = 20;
	private static final int LIST_LINE_PREVIOUS = 0;
	private static final int LIST_LINE_PARENT = 1;
	private static final int LIST_LINE_NEXT = LIST_LINE_COUNT-1;

	private static final int VIEW_LINE_NAME = 0;
	private static final int VIEW_LINE_PACK = 1;
	private static final int VIEW_LINE_AUTHOR = 2;

	@SuppressWarnings("unused")
	private static final int LIST_PANEL_INDEX = 0;
	@SuppressWarnings("unused")
	private static final int PREVIEW_PANEL_INDEX = 2;
	
	@SuppressWarnings("unused")
	private static final int INFOS_PANEL_INDEX = 0;
	@SuppressWarnings("unused")
	private static final int NOTES_PANEL_INDEX = 2;

	private SubPanel mainPanel;
	private SubPanel previewPanel;
	private UntitledSubPanelTable infosPanel;
	private EntitledSubPanel notesPanel;
	private int listWidth;
	private int listHeight;
	
	private ArrayList<UntitledSubPanelTable> listPackagePanels;
	private int currentPackagePage = 0;
	private int selectedPackageRow = -1;
	private ArrayList<String> aiPackages;
	private ArrayList<ArrayList<String>> aiFolders;

	private ArrayList<UntitledSubPanelTable> listAiPanels;
	private int currentAiPage = 0;
	private int selectedAiRow = -1;
	private ArrayList<AiPreview> aiPreviews;
		
	private boolean packageMode = true; //false if AIs are displayed 
	@SuppressWarnings("unused")
	private int lastAuthorNumber = 1;
	
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
			{	listWidth = leftWidth;
				listHeight = dataHeight;
				makePackagesListPanels(leftWidth,dataHeight);
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
		
	/**
	 * builds the list of ai (main) packages and of ai folders (or secondary packages).
	 * for each folder, tests if an ai.xml file is present. if not, creates this file.
	 */
	private void initPackages()
	{	aiPackages = new ArrayList<String>();
		aiFolders = new ArrayList<ArrayList<String>>();
		
		// packages
		String aiMainName = FileTools.getAiPath();
		File aiMainFile = new File(aiMainName);
		FileFilter filter = new FileFilter()
		{	@Override
			public boolean accept(File pathname)
			{	boolean result = pathname.exists() && pathname.isDirectory();
				return result;
			}
		};
		File aiPackageFilesTemp[] = aiMainFile.listFiles(filter);
		ArrayList<File> aiPackageFiles = new ArrayList<File>();
		for(int i=0;i<aiPackageFilesTemp.length;i++)
			aiPackageFiles.add(aiPackageFilesTemp[i]);
		Comparator<File> comparator = new Comparator<File>()
		{	@Override
			public int compare(File o1, File o2)
			{	String n1 = o1.getName();
				String n2 = o2.getName();
				int result = n1.compareTo(n2);				
				return result;
			}			
		};	
		Collections.sort(aiPackageFiles,comparator);

		// AIs
		Iterator<File> p = aiPackageFiles.iterator();
		while(p.hasNext())
		{	// get the list of folders in this package
			File aiPackageFile = p.next();
			String packageName = aiPackageFile.getName();
			File aiFolderFiles[] = aiPackageFile.listFiles(filter);
			ArrayList<File> aiFiles = new ArrayList<File>();
			for(int i=0;i<aiFolderFiles.length;i++)
				aiFiles.add(aiFolderFiles[i]);
			Collections.sort(aiFiles,comparator);
			ArrayList<String> aiFoldersTemp = new ArrayList<String>();
			Iterator<File> q = aiFiles.iterator();
			while(q.hasNext())
			{	// find the XML file class
				File aiFolderFile = q.next();
				String folderName = aiFolderFile.getName();
				File[] content = aiFolderFile.listFiles();
				String classFileName = FileTools.FILE_AI_MAIN_CLASS+FileTools.EXTENSION_CLASS;
				File classFile = getFile(classFileName,content);
				if(classFile!=null)
				{	String pkgName = packageName+ClassTools.CLASS_SEPARATOR+folderName;
					String classQualifiedName = pkgName+ClassTools.CLASS_SEPARATOR+FileTools.FILE_AI_MAIN_CLASS;
					try
					{	Class<?> tempClass = Class.forName(classQualifiedName);
						if(AbstractAiManager.class.isAssignableFrom(tempClass))
						{	aiFoldersTemp.add(folderName);
							String xmlFileName = FileTools.FILE_AI+FileTools.EXTENSION_DATA;
							File xmlFile = getFile(xmlFileName,content);
							if(xmlFile == null)
							{	AiPreview aiPreview = new AiPreview(packageName,folderName);
								AiPreviewSaver.saveAiPreview(aiPreview);
							}
						}
					}
					catch (ClassNotFoundException e)
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
				}
			}
			// add to the list of AIs for this package
			if(aiFoldersTemp.size()>0)
			{	aiFolders.add(aiFoldersTemp);
				aiPackages.add(packageName);
			}
		}
	}
	
	private File getFile(String fileName, File[] list)
	{	File result = null;
		int i = 0;
		while(i<list.length && result==null)
		{	String fName = list[i].getName();
			if(fName.equalsIgnoreCase(fileName))
				result = list[i];
			else
				i++;				
		}
		return result;
	}
	
	private void initAiPreviews()
	{	aiPreviews = new ArrayList<AiPreview>();
		int selectedPackageIndex = currentPackagePage*(LIST_LINE_COUNT-2)+(selectedPackageRow-1);
		String packageName = aiPackages.get(selectedPackageIndex);
		Iterator<String> it = aiFolders.get(selectedPackageIndex).iterator();
		while(it.hasNext())
		{	String folderName = it.next();
			try
			{	AiPreview aiPreview = AiPreviewLoader.loadAiPreview(packageName,folderName);
				aiPreviews.add(aiPreview);
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
	}
	
	private void makePackagesListPanels(int width, int height)
	{	int cols = 1;
		listPackagePanels = new ArrayList<UntitledSubPanelTable>();		
		Iterator<String> it = aiPackages.iterator(); 
		for(int panelIndex=0;panelIndex<getPackagesPageCount();panelIndex++)
		{	UntitledSubPanelTable listPanel = new UntitledSubPanelTable(width,height,cols,LIST_LINE_COUNT,false);
			listPanel.setSubColumnsMaxWidth(0,Integer.MAX_VALUE);
		
			// data
			int line = LIST_LINE_PREVIOUS+1;
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

	private void makeFoldersListPanels(int width, int height)
	{	int cols = 1;
		listAiPanels = new ArrayList<UntitledSubPanelTable>();
		Iterator<AiPreview> it = aiPreviews.iterator(); 
		for(int panelIndex=0;panelIndex<getFoldersPageCount();panelIndex++)
		{	UntitledSubPanelTable listPanel = new UntitledSubPanelTable(width,height,cols,LIST_LINE_COUNT,false);
			listPanel.setSubColumnsMaxWidth(0,Integer.MAX_VALUE);
		
			// data
			int line = LIST_LINE_PARENT+1;
			while(line<LIST_LINE_NEXT && it.hasNext())
			{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				AiPreview aiPreview = it.next();
				listPanel.setLabelBackground(line,0,bg);
				listPanel.setLabelText(line,0,aiPreview.getFolder(),aiPreview.getFolder());
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
			// parent
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_PARENT,0,bg);
				String key = GuiTools.MENU_AI_SELECT_CLASS_PARENT;
				listPanel.setLabelKey(LIST_LINE_PARENT,0,key,false);
				JLabel label = listPanel.getLabel(LIST_LINE_PARENT,0);
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
		notesPanel.setDataPanel(textPanel);
		Color bg = notesPanel.getDataPanel().getBackground();
		textPanel.setBackground(bg);
		Color fg = notesPanel.getDataPanel().getForeground();
		textPanel.setForeground(fg);
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
	{	String values[] = new String[10];
		// no player selected
		if(packageMode || selectedAiRow<0)
		{	for(int i=0;i<values.length;i++)
				values[i] = null;
			for(int i=VIEW_LINE_AUTHOR+1;i<values.length;i++)
			{	infosPanel.setLabelIcon(i,0,null,null);
				Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				infosPanel.setLabelBackground(i,0,bg);	
				infosPanel.setLabelBackground(i,1,bg);	
			}
		}
		// one player selected
		else
		{	int index = (selectedAiRow-2)+currentAiPage*(LIST_LINE_COUNT-3);
			AiPreview aiPreview = aiPreviews.get(index);
			values[VIEW_LINE_NAME] = aiPreview.getFolder();
			values[VIEW_LINE_PACK]= aiPreview.getPack();
			ArrayList<String> authors = aiPreview.getAuthors();
			Iterator<String> it = authors.iterator();
			index = 0;
			while(it.hasNext())
			{	String author = it.next();	
				values[VIEW_LINE_AUTHOR+index] = author;
				index++;
			}
			for(int i=1;i<index;i++)
			{	int line = VIEW_LINE_AUTHOR+i;
				infosPanel.setLabelKey(line,0,GuiTools.MENU_AI_SELECT_PREVIEW_AUTHOR,true);
				Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
				infosPanel.setLabelForeground(line,0,fg);
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				infosPanel.setLabelBackground(line,0,bg);
				bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				infosPanel.setLabelBackground(line,1,bg);
			}
		}
		// common
		for(int line=0;line<values.length;line++)
		{	int colSub = 1;
			String text = values[line];
			String tooltip = text;
			infosPanel.setLabelText(line,colSub,text,tooltip);
		}
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
	{	JLabel label = (JLabel)e.getComponent();
		// list = packages
		if(packageMode)
		{	int[] pos = listPackagePanels.get(currentPackagePage).getLabelPosition(label);
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
					if(currentPackagePage<getPackagesPageCount()-1)
					{	unselectList();
						currentPackagePage++;
						refreshList();
					}
					break;
				// package selected
				default:
					unselectList();
					selectedPackageRow = pos[0];
					listPackagePanels.get(currentPackagePage).setLabelBackground(selectedPackageRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
					initAiPreviews();
					makeFoldersListPanels(listWidth,listHeight);
					packageMode = false;
					currentAiPage = 0;
					refreshList();
			}
		}
		// list = ai
		else
		{	int[] pos = listAiPanels.get(currentAiPage).getLabelPosition(label);
			switch(pos[0])
			{	// previous page
				case LIST_LINE_PREVIOUS:
					if(currentAiPage>0)
					{	unselectList();
						currentAiPage--;
						refreshList();
					}
					break;
				// go to package
				case LIST_LINE_PARENT:
					unselectList();
					packageMode = true;
					refreshPreview();
					unselectList();
					refreshList();
					break;
				// next page
				case LIST_LINE_NEXT:
					if(currentAiPage<getFoldersPageCount()-1)
					{	unselectList();
						currentAiPage++;
						refreshList();
					}
					break;
				// ai selected
				default:
					unselectList();
					selectedAiRow = pos[0];
					//refreshList();
					listAiPanels.get(currentAiPage).setLabelBackground(selectedAiRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
					refreshPreview();
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	private int getPackagesPageCount()
	{	int result = aiPackages.size()/(LIST_LINE_COUNT-2);
		if(aiPackages.size()%(LIST_LINE_COUNT-2)>0)
			result++;
		else if(result==0)
			result = 1;
		return result;
	}
	
	private int getFoldersPageCount()
	{	int result = aiPreviews.size()/(LIST_LINE_COUNT-2);
		if(aiPreviews.size()%(LIST_LINE_COUNT-2)>0)
			result++;
		else if(result==0)
			result = 1;
		return result;
	}
	
	public void unselectList()
	{	// packages displayed
		if(packageMode && selectedPackageRow!=-1)
		{	listPackagePanels.get(currentPackagePage).setLabelBackground(selectedPackageRow,0,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
			selectedPackageRow = -1;
			//refreshPreview();
		}
		// ai displayed
		else if(!packageMode && selectedAiRow!=-1)
		{	listAiPanels.get(currentAiPage).setLabelBackground(selectedAiRow,0,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
			selectedAiRow = -1;
			refreshPreview();
		}
	}
	
	private void refreshList()
	{	// packages displayed
		if(packageMode)
		{	mainPanel.remove(LIST_PANEL_INDEX);
			mainPanel.add(listPackagePanels.get(currentPackagePage),LIST_PANEL_INDEX);
		}
		// ai displayed
		else
		{	mainPanel.remove(LIST_PANEL_INDEX);
			mainPanel.add(listAiPanels.get(currentAiPage),LIST_PANEL_INDEX);
		}
		// common
		mainPanel.validate();
		mainPanel.repaint();
	}
	
	public AiPreview getSelectedAiPreview()
	{	int index = currentAiPage*(LIST_LINE_COUNT-2)+selectedAiRow;
		return aiPreviews.get(index);
	}

}
