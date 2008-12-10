package fr.free.totalboumboum.gui.common.content.subpanel.browser;

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
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class PackBrowserSubPanel extends SubPanel implements MouseListener, FolderBrowserSubPanelListener
{	private static final long serialVersionUID = 1L;

	public PackBrowserSubPanel(int width, int height)
	{	super(width,height);
		setOpaque(false);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// pages
		setFolder(null,new ArrayList<String>());
	}
	
	/////////////////////////////////////////////////////////////////
	// PAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String baseFolder;
	private ArrayList<String> targetFiles;
	private int lines;
	private int linePrevious;
	private int lineNext;
	private int controlTotalCount;
	private int controlUpCount;
	private int currentPage = 0;
	private ArrayList<UntitledSubPanelTable> listPanels;
	private ArrayList<String> names;
	private int pageCount;
	private FolderBrowserSubPanel filePanel;
	private String selectedName;
	
	public String getBaseFolder()
	{	return baseFolder;	
	}
	
	public ArrayList<String> getTargetFiles()
	{	return targetFiles;	
	}
	
	public void setFolder(String baseFolder, String targetFile)
	{	ArrayList<String> targetFiles = new ArrayList<String>();
		targetFiles.add(targetFile);
		setFolder(baseFolder,targetFiles);
	}
	
	public void setFolder(String baseFolder, ArrayList<String> targetFiles)
	{	// init
		this.baseFolder = baseFolder;
		this.targetFiles = targetFiles;
		listPanels = new ArrayList<UntitledSubPanelTable>();
		currentPage = 0;
		filePanel = null;
		selectedName = null;
		
		// size
		lines = 20;
		linePrevious = 0;
		lineNext = lines-1;
		controlUpCount = 1;
		controlTotalCount = controlUpCount+1;
		int cols = 1;
		int textMaxWidth = width - GuiTools.subPanelMargin*2;
		
		initNames();
		pageCount = getPageCount();
		
		for(int panelIndex=0;panelIndex<pageCount;panelIndex++)
		{	UntitledSubPanelTable listPanel = new UntitledSubPanelTable(width,height,cols,lines,false);
			listPanel.setColSubMaxWidth(0,textMaxWidth);
		
			// data
			int line = controlUpCount;
			int nameIndex = panelIndex*(lines-controlTotalCount);
			while(line<lines && nameIndex<names.size())
			{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				String name = names.get(nameIndex);
				listPanel.setLabelBackground(line,0,bg);
				listPanel.setLabelText(line,0,name,name);
				JLabel label = listPanel.getLabel(line,0);
				label.addMouseListener(this);
				nameIndex++;
				line++;
			}			
			// page up
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(linePrevious,0,bg);
				String key = GuiKeys.COMMON_BROWSER_PACK_PAGEUP;
				listPanel.setLabelKey(linePrevious,0,key,true);
				JLabel label = listPanel.getLabel(linePrevious,0);
				label.addMouseListener(this);
			}
			// page down
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(lineNext,0,bg);
				String key = GuiKeys.COMMON_BROWSER_PACK_PAGEDOWN;
				listPanel.setLabelKey(lineNext,0,key,true);
				JLabel label = listPanel.getLabel(lineNext,0);
				label.addMouseListener(this);
			}
			listPanels.add(listPanel);
		}
		
		refreshList();
	}

	private void initNames()
	{	names = new ArrayList<String>();
		if(baseFolder!=null && targetFiles.size()>0)
		{	File fileBaseFolder = new File(baseFolder);
			FileFilter filter = new FileFilter()
			{	@Override
				public boolean accept(File pathname)
				{	boolean result = pathname.exists() && pathname.isDirectory();
					return result;
				}
			};
			List<File> fileFolders = Arrays.asList(fileBaseFolder.listFiles(filter));
			Collections.sort(fileFolders,new Comparator<File>()
			{	@Override
				public int compare(File arg0, File arg1)
				{	int result;
					String name0 = arg0.getName();
					String name1 = arg1.getName();
					Collator collator = Collator.getInstance(Locale.ENGLISH);
					result = collator.compare(name0,name1);
					return result;
				}
			});
			for(File f:fileFolders)
			{	File[] folders = f.listFiles();
				int i = 0;
				boolean foundAll = false;
				while(i<folders.length && !foundAll)
				{	if(folders[i].isDirectory())
					{	List<File> files = Arrays.asList(folders[i].listFiles());
						boolean found = true;
						Iterator<String> it = targetFiles.iterator();
						while(it.hasNext() && found)
						{	String targetFile = it.next();
							File testFile = new File(folders[i].getPath()+File.separator+targetFile);
							found = files.contains(testFile);
						}
						if(found)
							foundAll = true;
					}
					i++;
				}
				if(foundAll)
					names.add(f.getName());
			}
		}
	}
	
	private int getPageCount()
	{	int result = names.size()/(lines-controlTotalCount);
		if(names.size()%(lines-controlTotalCount)>0)
			result++;
		else if(result==0)
			result = 1;
		return result;
	}		
	
	public String getSelectedName()
	{	String result = null;
		if(filePanel!=null)
			result = filePanel.getSelectedName();		
		return result;
	}
	
	public String getSelectedPack()
	{	String result = selectedName;
		return result;
	}
	
	private void selectPack(int row)
	{	if(row==-1)
		{	
//			if(filePanel!=null)
//				filePanel.removeListener(this);
			filePanel = null;
			selectedName = null;
		}
		else
		{	filePanel = new FolderBrowserSubPanel(width,height);
			int selectedIndex = (row-controlUpCount)+currentPage*(lines-controlTotalCount);
			selectedName = names.get(selectedIndex);
			String bFolder = baseFolder+File.separator+selectedName;
			filePanel.setFolder(bFolder,targetFiles);
			filePanel.addListener(this);
		}
		refreshList();
		// update listeners
//		firePackBrowserSelectionChange();
	}

	private void refreshList()
	{	if(this.getComponentCount()>0)
			remove(0);
		if(filePanel == null)
			add(listPanels.get(currentPage));
		else
			add(filePanel);
		validate();
		repaint();
	}
	
	public void refresh()
	{	setFolder(baseFolder,targetFiles);		
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
		int[] pos = listPanels.get(currentPage).getLabelPosition(label);
		
		// previous page
		if(pos[0]==linePrevious)
		{	if(currentPage>0)
			{	currentPage--;
				refreshList();
			}
		}
		// next page
		else if(pos[0]==lineNext)
		{	if(currentPage<getPageCount()-1)
			{	currentPage++;
				refreshList();
			}
		}
		// select a name
		else
		{	selectPack(pos[0]);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<PackBrowserSubPanelListener> listeners = new ArrayList<PackBrowserSubPanelListener>();
	
	public void addListener(PackBrowserSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(PackBrowserSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	private void firePackBrowserSelectionChange()
	{	for(PackBrowserSubPanelListener listener: listeners)
			listener.packBrowserSelectionChange();
	}

	/////////////////////////////////////////////////////////////////
	// FILE BROWSER LISTENER		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void folderBrowserSelectionChange()
	{	firePackBrowserSelectionChange();
	}

	@Override
	public void folderBrowserParent()
	{	selectPack(-1);
	}
}
