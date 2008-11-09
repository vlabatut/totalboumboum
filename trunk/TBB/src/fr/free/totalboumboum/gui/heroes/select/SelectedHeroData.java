package fr.free.totalboumboum.gui.heroes.select;

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
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.content.sprite.SpritePreview;
import fr.free.totalboumboum.engine.content.sprite.SpritePreviewLoader;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.Column;
import fr.free.totalboumboum.gui.common.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelColumns;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;

public class SelectedHeroData extends EntitledDataPanel implements MouseListener
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
	private static final int VIEW_LINE_SOURCE = 3;

	private static final int LIST_PANEL_INDEX = 0;
	@SuppressWarnings("unused")
	private static final int PREVIEW_PANEL_INDEX = 2;
	
	@SuppressWarnings("unused")
	private static final int INFOS_PANEL_INDEX = 0;
	@SuppressWarnings("unused")
	private static final int IMAGE_PANEL_INDEX = 2;

	private SubPanel mainPanel;
	private SubPanel previewPanel;
	private UntitledSubPanelTable infosPanel;
	private UntitledSubPanelColumns imagePanel;
	private int listWidth;
	private int listHeight;
	
	private ArrayList<UntitledSubPanelTable> listPackagePanels;
	private int currentPackagePage = 0;
	private int selectedPackageRow = -1;
	private ArrayList<String> heroPackages;
	private ArrayList<ArrayList<String>> heroFolders;

	private ArrayList<UntitledSubPanelTable> listHeroPanels;
	private int currentHeroPage = 0;
	private int selectedHeroRow = -1;
	private ArrayList<SpritePreview> heroPreviews;
		
	private boolean packageMode = true; //false if heroes are displayed 
	
	public SelectedHeroData(SplitMenuPanel container)
	{	super(container);

		// title
		setTitleKey(GuiTools.MENU_HERO_SELECT_TITLE);
	
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

				makeImagePanel(rightWidth,downHeight);
				previewPanel.add(imagePanel);
				
				mainPanel.add(previewPanel);
			}
			
			setDataPart(mainPanel);
		}
	}
		
	/**
	 * builds the list of heroes (main) packages and of heroes folders (or secondary packages).
	 * for each folder, tests if a sprite.xml file is present.
	 */
	private void initPackages()
	{	heroPackages = new ArrayList<String>();
		heroFolders = new ArrayList<ArrayList<String>>();
		
		// packages
		String heroMainName = FileTools.getHeroesPath();
		File heroMainFile = new File(heroMainName);
		FileFilter filter = new FileFilter()
		{	@Override
			public boolean accept(File pathname)
			{	boolean result = pathname.exists() && pathname.isDirectory();
				return result;
			}
		};
		File heroPackageFilesTemp[] = heroMainFile.listFiles(filter);
		ArrayList<File> heroPackageFiles = new ArrayList<File>();
		for(int i=0;i<heroPackageFilesTemp.length;i++)
			heroPackageFiles.add(heroPackageFilesTemp[i]);
		Comparator<File> comparator = new Comparator<File>()
		{	@Override
			public int compare(File o1, File o2)
			{	String n1 = o1.getName();
				String n2 = o2.getName();
				int result = n1.compareTo(n2);				
				return result;
			}			
		};	
		Collections.sort(heroPackageFiles,comparator);

		// Heroes
		Iterator<File> p = heroPackageFiles.iterator();
		while(p.hasNext())
		{	// get the list of folders in this package
			File heroPackageFile = p.next();
			String packageName = heroPackageFile.getName();
			File heroFolderFiles[] = heroPackageFile.listFiles(filter);
			ArrayList<File> heroFiles = new ArrayList<File>();
			for(int i=0;i<heroFolderFiles.length;i++)
				heroFiles.add(heroFolderFiles[i]);
			Collections.sort(heroFiles,comparator);
			ArrayList<String> heroFoldersTemp = new ArrayList<String>();
			Iterator<File> q = heroFiles.iterator();
			while(q.hasNext())
			{	// find the XML file
				File heroFolderFile = q.next();
				String folderName = heroFolderFile.getName();
				File[] content = heroFolderFile.listFiles();
				String xmlFileName = FileTools.FILE_SPRITE+FileTools.EXTENSION_DATA;
				File xmlFile = FileTools.getFile(xmlFileName,content);
				if(xmlFile!=null)
					heroFoldersTemp.add(folderName);
			}
			// add to the list of heroes for this package
			if(heroFoldersTemp.size()>0)
			{	heroFolders.add(heroFoldersTemp);
				heroPackages.add(packageName);
			}
		}
	}
	
	private void initHeroPreviews()
	{	heroPreviews = new ArrayList<SpritePreview>();
		int selectedPackageIndex = currentPackagePage*(LIST_LINE_COUNT-2)+(selectedPackageRow-1);
		String packageName = heroPackages.get(selectedPackageIndex);
		Iterator<String> it = heroFolders.get(selectedPackageIndex).iterator();
		while(it.hasNext())
		{	String folderName = it.next();
			try
			{	SpritePreview heroPreview = SpritePreviewLoader.loadHeroPreview(packageName,folderName);
				heroPreviews.add(heroPreview);
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
	
	private void makePackagesListPanels(int width, int height)
	{	int cols = 1;
		listPackagePanels = new ArrayList<UntitledSubPanelTable>();		
		Iterator<String> it = heroPackages.iterator(); 
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
				String key = GuiTools.MENU_HERO_SELECT_PACKAGE_PAGEUP;
				listPanel.setLabelKey(LIST_LINE_PREVIOUS,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_PREVIOUS,0);
				label.addMouseListener(this);
			}
			// page down
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_NEXT,0,bg);
				String key = GuiTools.MENU_HERO_SELECT_PACKAGE_PAGEDOWN;
				listPanel.setLabelKey(LIST_LINE_NEXT,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_NEXT,0);
				label.addMouseListener(this);
			}
			listPackagePanels.add(listPanel);
		}
	}

	private void makeFoldersListPanels(int width, int height)
	{	int cols = 1;
		listHeroPanels = new ArrayList<UntitledSubPanelTable>();
		Iterator<SpritePreview> it = heroPreviews.iterator(); 
		for(int panelIndex=0;panelIndex<getFoldersPageCount();panelIndex++)
		{	UntitledSubPanelTable listPanel = new UntitledSubPanelTable(width,height,cols,LIST_LINE_COUNT,false);
			listPanel.setSubColumnsMaxWidth(0,Integer.MAX_VALUE);
		
			// data
			int line = LIST_LINE_PARENT+1;
			while(line<LIST_LINE_NEXT && it.hasNext())
			{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				SpritePreview heroPreview = it.next();
				listPanel.setLabelBackground(line,0,bg);
				listPanel.setLabelText(line,0,heroPreview.getFolder(),heroPreview.getFolder());
				JLabel label = listPanel.getLabel(line,0);
				label.addMouseListener(this);
				line++;
			}			
			// page up
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_PREVIOUS,0,bg);
				String key = GuiTools.MENU_HERO_SELECT_CLASS_PAGEUP;
				listPanel.setLabelKey(LIST_LINE_PREVIOUS,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_PREVIOUS,0);
				label.addMouseListener(this);
			}
			// parent
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_PARENT,0,bg);
				String key = GuiTools.MENU_HERO_SELECT_CLASS_PARENT;
				listPanel.setLabelKey(LIST_LINE_PARENT,0,key,false);
				JLabel label = listPanel.getLabel(LIST_LINE_PARENT,0);
				label.addMouseListener(this);
			}
			// page down
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_NEXT,0,bg);
				String key = GuiTools.MENU_HERO_SELECT_CLASS_PAGEDOWN;
				listPanel.setLabelKey(LIST_LINE_NEXT,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_NEXT,0);
				label.addMouseListener(this);
			}
			listHeroPanels.add(listPanel);
		}
	}

	private void makeInfosPanel(int width, int height)
	{	int lines = 10;
		int colSubs = 2;
		int colGroups = 1;
		infosPanel = new UntitledSubPanelTable(width,height,colGroups,colSubs,lines,true);
		
		// data
		String keys[] = 
		{	GuiTools.MENU_HERO_SELECT_PREVIEW_NAME,
			GuiTools.MENU_HERO_SELECT_PREVIEW_PACKAGE,
			GuiTools.MENU_HERO_SELECT_PREVIEW_AUTHOR,
			GuiTools.MENU_HERO_SELECT_PREVIEW_SOURCE
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
	
	private void makeImagePanel(int width, int height)
	{	int cols = 3;
		imagePanel = new UntitledSubPanelColumns(width,height,cols);
		PredefinedColor[] colors = PredefinedColor.values();
		int lines = colors.length/2;
		int margin = GuiTools.subPanelMargin;
		if(colors.length%2 > 0)
			lines++;
		int lineHeight = (height - (lines-1)*margin)/lines;
		int rightWidth = width - 2*lineHeight - 5*margin;

		int col = 0;

		// colors 0
		{	Column cl = imagePanel.getColumn(col);
			cl.setWidth(lineHeight);
			for(int i=1;i<lines;i++)
				cl.addLabel(0);
			for(int line=0;line<lines;line++)
			{	cl.setLabelMinHeight(line,lineHeight);
				cl.setLabelPreferredHeight(line,lineHeight);
				cl.setLabelMaxHeight(line,lineHeight);
//				cl.getLabel(col).addMouseListener(this);
//				cl.setLabelText(line,""+col,""+col);
			}
			Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			cl.setBackgroundColor(bg);
			col++;
		}
		
		// colors 1
		{	Column cl = imagePanel.getColumn(col);
			cl.setWidth(lineHeight);
			for(int i=1;i<lines;i++)
				cl.addLabel(0);
			for(int line=0;line<lines;line++)
			{	cl.setLabelMinHeight(line,lineHeight);
				cl.setLabelPreferredHeight(line,lineHeight);
				cl.setLabelMaxHeight(line,lineHeight);
//				cl.getLabel(col).addMouseListener(this);
//				cl.setLabelText(line,""+col,""+col);
			}
			Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			cl.setBackgroundColor(bg);
			col++;
		}
		
		// image
		{	Column cl = imagePanel.getColumn(col);
			cl.setWidth(rightWidth);
			int line = 0;
			cl.setLabelMinHeight(line,height);
			cl.setLabelPreferredHeight(line,height);
			cl.setLabelMaxHeight(line,height);
			Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			cl.setBackgroundColor(bg);
//			cl.setLabelText(line,""+col,""+col);
			col++;
		}
	}
	
	private void refreshPreview()
	{	String infosValues[] = new String[10];
		BufferedImage image;
		PredefinedColor colorValues[] = PredefinedColor.values();
		boolean colors[] = new boolean[PredefinedColor.values().length];
		// no player selected
		if(packageMode || selectedHeroRow<0)
		{	// notes
			image = null;
			for(int i=0;i<colors.length;i++)
				colors[i] = false;
			// infos
			for(int i=0;i<infosValues.length;i++)
				infosValues[i] = null;
		}
		// one player selected
		else
		{	int index = (selectedHeroRow-2)+currentHeroPage*(LIST_LINE_COUNT-3);
			SpritePreview heroPreview = heroPreviews.get(index);
			// image
			image = heroPreview.getImage();
			for(int i=0;i<colors.length;i++)
				if(heroPreview.hasColor(colorValues[i]))
					colors[i] = true;
			// infos
			infosValues[VIEW_LINE_NAME] = heroPreview.getName();
			infosValues[VIEW_LINE_PACK]= heroPreview.getPack();
			infosValues[VIEW_LINE_AUTHOR] = heroPreview.getAuthor();
			infosValues[VIEW_LINE_SOURCE] = heroPreview.getSource();
		}
		// infos
		for(int line=0;line<infosValues.length;line++)
		{	int colSub = 1;
			String text = infosValues[line];
			String tooltip = text;
			infosPanel.setLabelText(line,colSub,text,tooltip);
		}
		// image
		int line = 0;
		int col = 0;
		int cols = 2;
//		int lines = imagePanel.getColumn(0).getLineCount();
		for(int i=0;i<colors.length;i++)
		{	// colors
			String text = null;
			String tooltip = null;
			Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			if(colors[i])
			{	String colorKey = colorValues[i].toString();
				colorKey = colorKey.toUpperCase().substring(0,1)+colorKey.toLowerCase().substring(1,colorKey.length());
				colorKey = GuiTools.COLOR+colorKey;
				text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(colorKey); 
				tooltip = text;
				Color clr = colorValues[i].getColor();
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
			}
			imagePanel.setLabelBackground(line,col,bg);
			imagePanel.setLabelText(line,col,text,tooltip);
			// index
			col++;
			if(col==cols)
			{	col=0;
				line++;
			}
		}
		JLabel label = imagePanel.getLabel(0,2);
		Dimension prefDim = label.getPreferredSize();
		int imgWidth = (int)(prefDim.width*0.9);
		int imgHeight = (int)(prefDim.height*0.9);
		if(image!=null)
		{	float zoomX = imgWidth/(float)image.getWidth();
			float zoomY = imgHeight/(float)image.getHeight();
			float zoom = Math.min(zoomX,zoomY);
			image = ImageTools.resize(image,zoom,true);
			ImageIcon icon = new ImageIcon(image);
			label.setIcon(icon);
			label.setText(null);
			Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
			label.setBackground(bg);
		}
		else
		{	label.setIcon(null);
			label.setText(null);
			Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			label.setBackground(bg);
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
					initHeroPreviews();
					makeFoldersListPanels(listWidth,listHeight);
					packageMode = false;
					currentHeroPage = 0;
					refreshList();
			}
		}
		// list = ai
		else
		{	int[] pos = listHeroPanels.get(currentHeroPage).getLabelPosition(label);
			switch(pos[0])
			{	// previous page
				case LIST_LINE_PREVIOUS:
					if(currentHeroPage>0)
					{	unselectList();
						currentHeroPage--;
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
					if(currentHeroPage<getFoldersPageCount()-1)
					{	unselectList();
						currentHeroPage++;
						refreshList();
					}
					break;
				// hero selected
				default:
					unselectList();
					selectedHeroRow = pos[0];
					//refreshList();
					listHeroPanels.get(currentHeroPage).setLabelBackground(selectedHeroRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
					refreshPreview();
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	private int getPackagesPageCount()
	{	int result = heroPackages.size()/(LIST_LINE_COUNT-2);
		if(heroPackages.size()%(LIST_LINE_COUNT-2)>0)
			result++;
		else if(result==0)
			result = 1;
		return result;
	}
	
	private int getFoldersPageCount()
	{	int result = heroPreviews.size()/(LIST_LINE_COUNT-2);
		if(heroPreviews.size()%(LIST_LINE_COUNT-2)>0)
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
		// hero displayed
		else if(!packageMode && selectedHeroRow!=-1)
		{	listHeroPanels.get(currentHeroPage).setLabelBackground(selectedHeroRow,0,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
			selectedHeroRow = -1;
			refreshPreview();
		}
	}
	
	private void refreshList()
	{	// packages displayed
		if(packageMode)
		{	mainPanel.remove(LIST_PANEL_INDEX);
			mainPanel.add(listPackagePanels.get(currentPackagePage),LIST_PANEL_INDEX);
		}
		// hero displayed
		else
		{	mainPanel.remove(LIST_PANEL_INDEX);
			mainPanel.add(listHeroPanels.get(currentHeroPage),LIST_PANEL_INDEX);
		}
		// common
		mainPanel.validate();
		mainPanel.repaint();
	}
	
	public SpritePreview getSelectedHeroPreview()
	{	int index = currentHeroPage*(LIST_LINE_COUNT-2)+(selectedHeroRow-2);
		return heroPreviews.get(index);
	}

}
