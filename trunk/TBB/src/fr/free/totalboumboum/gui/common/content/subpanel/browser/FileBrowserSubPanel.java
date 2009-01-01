package fr.free.totalboumboum.gui.common.content.subpanel.browser;

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

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class FileBrowserSubPanel extends SubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;

	public FileBrowserSubPanel(int width, int height)
	{	super(width,height);
		setOpaque(false);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// pages
		setFileNames(null);
	}
	
	/////////////////////////////////////////////////////////////////
	// PAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,String> fileNames;
	ArrayList<Entry<String,String>> names;
	private int lines;
	private int linePrevious;
	private int lineParent;
	private int lineNext;
	private int controlTotalCount;
	private int controlUpCount;
	private int selectedRow;
	private int currentPage = 0;
	private ArrayList<UntitledSubPanelTable> listPanels;
	private int pageCount;	
	
	public HashMap<String,String> getFileNames()
	{	return fileNames;	
	}
	
	public void setFileNames(HashMap<String,String> fileNames)
	{	// init
		this.fileNames = fileNames;
		if(fileNames==null)
			this.fileNames = new HashMap<String, String>();
		listPanels = new ArrayList<UntitledSubPanelTable>();
		currentPage = 0;
		selectedRow = -1; fireFileBrowserSelectionChange();
		
		// size
		lines = 20;
		linePrevious = 0;
		lineParent = 1;
		lineNext = lines-1;
		controlUpCount = 1;
		if(showParent)
			controlUpCount = 2;
		controlTotalCount = controlUpCount+1;
		int cols = 1;
		
		initNames();
		pageCount = getPageCount();
		int textMaxWidth = width - GuiTools.subPanelMargin*2;
		
		for(int panelIndex=0;panelIndex<pageCount;panelIndex++)
		{	UntitledSubPanelTable listPanel = new UntitledSubPanelTable(width,height,cols,lines,false);
			listPanel.setColSubMaxWidth(0,textMaxWidth);
		
			// data
			int line = controlUpCount;
			int nameIndex = panelIndex*(lines-controlTotalCount);
			while(line<lines && nameIndex<names.size())
			{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				Entry<String,String> entry = names.get(nameIndex);
				String name = entry.getValue();
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
				String key = GuiKeys.COMMON_BROWSER_FILE_PAGEUP;
				listPanel.setLabelKey(linePrevious,0,key,true);
				JLabel label = listPanel.getLabel(linePrevious,0);
				label.addMouseListener(this);
			}
			// parent
			if(showParent)
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(lineParent,0,bg);
				String key = GuiKeys.COMMON_BROWSER_FILE_PARENT;
				listPanel.setLabelKey(lineParent,0,key,false);
				JLabel label = listPanel.getLabel(lineParent,0);
				label.addMouseListener(this);
			}
			// page down
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(lineNext,0,bg);
				String key = GuiKeys.COMMON_BROWSER_FILE_PAGEDOWN;
				listPanel.setLabelKey(lineNext,0,key,true);
				JLabel label = listPanel.getLabel(lineNext,0);
				label.addMouseListener(this);
			}
			listPanels.add(listPanel);
		}
		
		refreshList();
	}

	private void initNames()
	{	names = new ArrayList<Entry<String,String>>(fileNames.entrySet());
		Collections.sort(names,new Comparator<Entry<String,String>>()
		{	@Override
			public int compare(Entry<String,String> arg0, Entry<String,String> arg1)
			{	int result;
				String name0 = arg0.getValue();
				String name1 = arg1.getValue();
				Collator collator = Collator.getInstance(Locale.ENGLISH);
				result = collator.compare(name0,name1);
				return result;
			}
		});
	}
	
	private int getPageCount()
	{	int result = names.size()/(lines-controlTotalCount);
		if(names.size()%(lines-controlTotalCount)>0)
			result++;
		else if(result==0)
			result = 1;
		return result;
	}		
	
	public String getSelectedFileName()
	{	String result = null;
		if(selectedRow!=-1)
		{	int selectedIndex = (selectedRow-controlUpCount)+currentPage*(lines-controlTotalCount);
			Entry<String,String> entry = names.get(selectedIndex);
			result = entry.getKey();		
		}
		return result;
	}
	
	public void setSelectedFileName(String fileName)
	{	Iterator<Entry<String,String>> it = names.iterator();
		boolean found = false;
		int index = 0;
		while(it.hasNext() && !found)
		{	Entry<String,String> entry = it.next();
			if(entry.getKey().equalsIgnoreCase(fileName))
				found = true;
			else
				index++;
		}
		if(found)
		{	currentPage = index/(lines-controlTotalCount);
			refreshList();
			int row = index%(lines-controlTotalCount)+controlUpCount;
			selectName(row);
		}
	}
	
	private void selectName(int row)
	{	UntitledSubPanelTable table = listPanels.get(currentPage);
		// unselect the previous selected line
		if(selectedRow!=-1)
			table.setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
		// select the new line
		selectedRow = row;
		if(selectedRow!=-1)
			table.setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
		// update listeners
		fireFileBrowserSelectionChange();
	}

	private void refreshList()
	{	if(this.getComponentCount()>0)
			remove(0);
		add(listPanels.get(currentPage));
		validate();
		repaint();
	}
	
	public void refresh()
	{	String selectedFileName = getSelectedFileName();
		setFileNames(fileNames);
		setSelectedFileName(selectedFileName);
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean showParent = true;
	
	public boolean getShowParent()
	{	return showParent;
	}

	public void setShowParent(boolean showParent)
	{	this.showParent = showParent;
		refresh();
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
			{	selectName(-1);
				currentPage--;
				refreshList();
			}
		}
		// parent
		else if(pos[0]==lineParent && showParent)
		{	selectName(-1);
			refreshList();
			fireFileBrowserParent();
		}
		// next page
		else if(pos[0]==lineNext)
		{	if(currentPage<getPageCount()-1)
			{	selectName(-1);
				currentPage++;
				refreshList();
			}
		}
		// select a name
		else
		{	selectName(pos[0]);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<FileBrowserSubPanelListener> listeners = new ArrayList<FileBrowserSubPanelListener>();
	
	public void addListener(FileBrowserSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(FileBrowserSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	private void fireFileBrowserSelectionChange()
	{	for(FileBrowserSubPanelListener listener: listeners)
			listener.fileBrowserSelectionChanged();
	}

	private void fireFileBrowserParent()
	{	for(FileBrowserSubPanelListener listener: listeners)
			listener.fileBrowserParentReached();
	}
}
