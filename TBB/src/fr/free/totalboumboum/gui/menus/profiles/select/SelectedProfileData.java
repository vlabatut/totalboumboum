package fr.free.totalboumboum.gui.menus.profiles.select;

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
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileLoader;
import fr.free.totalboumboum.configuration.profile.ProfilesConfiguration;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class SelectedProfileData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;
	
	private static final int LIST_LINE_COUNT = 20;
	private static final int LIST_LINE_PREVIOUS = 0;
	private static final int LIST_LINE_NEXT = LIST_LINE_COUNT-1;

	private static final int VIEW_LINE_NAME = 0;
	private static final int VIEW_LINE_AI_NAME = 1;
	private static final int VIEW_LINE_AI_PACK = 2;
	private static final int VIEW_LINE_HERO_NAME = 3;
	private static final int VIEW_LINE_HERO_PACK = 4;
	private static final int VIEW_LINE_COLOR = 5;

	private static final int LIST_PANEL_INDEX = 0;
	@SuppressWarnings("unused")
	private static final int PREVIEW_PANEL_INDEX = 2;
	
	private ArrayList<UntitledSubPanelTable> listPanels;
	private int currentPage = 0;
	private int selectedRow = -1;
	
	private ProfilesConfiguration profilesConfiguration;
	private SubPanel mainPanel;
	private UntitledSubPanelTable previewPanel;
	private ArrayList<Entry<String,String>> profiles;
	private Profile selectedProfile = null;
	private String selectedProfileFile = null;
	private int leftWidth;
	
	public SelectedProfileData(SplitMenuPanel container)
	{	super(container);

		// title
		setTitleKey(GuiKeys.MENU_PROFILES_LIST_TITLE);
	
		// data
		{	mainPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			mainPanel.setOpaque(false);
			profilesConfiguration = Configuration.getProfilesConfiguration();
			initProfiles();
			
			// list panel
			{	makeListPanels(leftWidth,dataHeight);
				mainPanel.add(listPanels.get(currentPage));
			}
			
			mainPanel.add(Box.createHorizontalGlue());
			
			// preview panel
			{	makePreviewPanel(rightWidth,dataHeight);
				mainPanel.add(previewPanel);
			}
			
			setDataPart(mainPanel);
			
		}
	}
		
	private void initProfiles()
	{	profiles = new ArrayList<Entry<String,String>>(profilesConfiguration.getProfiles().entrySet());
		Collections.sort(profiles,new Comparator<Entry<String,String>>()
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
	
	private void makeListPanels(int width, int height)
	{	int lines = LIST_LINE_COUNT;
		int cols = 1;
		listPanels = new ArrayList<UntitledSubPanelTable>();
		
		for(int panelIndex=0;panelIndex<getPageCount();panelIndex++)
		{	UntitledSubPanelTable listPanel = new UntitledSubPanelTable(width,height,cols,lines,false);
			listPanel.setColSubMaxWidth(0,Integer.MAX_VALUE);
		
			// data
			int line = 1;
			int profileIndex = panelIndex*(LIST_LINE_COUNT-2);
			while(line<LIST_LINE_NEXT && profileIndex<profiles.size())
			{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				Entry<String,String> profile = profiles.get(profileIndex);
				String name = profile.getValue();
				listPanel.setLabelBackground(line,0,bg);
				listPanel.setLabelText(line,0,name,name);
				JLabel label = listPanel.getLabel(line,0);
				label.addMouseListener(this);
				profileIndex++;
				line++;
			}			
			// page up
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_PREVIOUS,0,bg);
				String key = GuiKeys.MENU_PROFILES_LIST_PAGEUP;
				listPanel.setLabelKey(LIST_LINE_PREVIOUS,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_PREVIOUS,0);
				label.addMouseListener(this);
			}
			// page down
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_NEXT,0,bg);
				String key = GuiKeys.MENU_PROFILES_LIST_PAGEDOWN;
				listPanel.setLabelKey(LIST_LINE_NEXT,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_NEXT,0);
				label.addMouseListener(this);
			}
			listPanels.add(listPanel);
		}
	}
	
	private void makePreviewPanel(int width, int height)
	{	int lines = 21;
		int colSubs = 2;
		int colGroups = 1;
		previewPanel = new UntitledSubPanelTable(width,height,colGroups,colSubs,lines,true);
		
		// data
		String keys[] = 
		{	GuiKeys.MENU_PROFILES_PREVIEW_NAME,
			GuiKeys.MENU_PROFILES_PREVIEW_AINAME,
			GuiKeys.MENU_PROFILES_PREVIEW_AIPACK,
			GuiKeys.MENU_PROFILES_PREVIEW_HERONAME,
			GuiKeys.MENU_PROFILES_PREVIEW_HEROPACK,
			GuiKeys.MENU_PROFILES_PREVIEW_COLOR			
		};
		for(int line=0;line<keys.length;line++)
		{	int colSub = 0;
			{	previewPanel.setLabelKey(line,colSub,keys[line],true);
				Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
				previewPanel.setLabelForeground(line,0,fg);
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				previewPanel.setLabelBackground(line,colSub,bg);
				colSub++;
			}
			{	String text = null;
				String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(keys[line]+GuiKeys.TOOLTIP);
				previewPanel.setLabelText(line,colSub,text,tooltip);
				if(line>0)
				{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					previewPanel.setLabelBackground(line,colSub,bg);
				}
				colSub++;
			}
		}
		int maxWidth = width-3*GuiTools.subPanelMargin-previewPanel.getHeaderHeight();
		previewPanel.setColSubMaxWidth(1,maxWidth);
		previewPanel.setColSubPreferredWidth(1,maxWidth);
	}
	
	private void refreshPreview()
	{	String values[] = new String[21];
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
		{	Entry<String,String> entry = profiles.get((selectedRow-1)+currentPage*(LIST_LINE_COUNT-2));
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
			String colorKey = selectedProfile.getSpriteSelectedColor().toString();
			colorKey = colorKey.toUpperCase().substring(0,1)+colorKey.toLowerCase().substring(1,colorKey.length());
			colorKey = GuiKeys.COMMON_COLOR+colorKey;
			values[VIEW_LINE_COLOR] = GuiConfiguration.getMiscConfiguration().getLanguage().getText(colorKey); 
			Color clr = selectedProfile.getSpriteSelectedColor().getColor();
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
		
//		mainPanel.validate();
//		mainPanel.repaint();
	}

	@Override
	public void refresh()
	{	initProfiles();
		makeListPanels(leftWidth,dataHeight);
		refreshList();
		if(selectedRow!=-1)
			listPanels.get(currentPage).setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
		refreshPreview();
	}

	public ProfilesConfiguration getProfilesConfiguration()
	{	return profilesConfiguration;
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
		switch(pos[0])
		{	// previous page
			case LIST_LINE_PREVIOUS:
				if(currentPage>0)
				{	unselectList();
					currentPage--;
					refreshList();
				}
				break;
			// next page
			case LIST_LINE_NEXT:
				if(currentPage<getPageCount()-1)
				{	unselectList();
					currentPage++;
					refreshList();
				}
				break;
			// profile selected
			default:
				unselectList();
				selectedRow = pos[0];
				listPanels.get(currentPage).setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
				refreshPreview();
		}
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	private int getPageCount()
	{	int result = profiles.size()/(LIST_LINE_COUNT-2);
		if(profiles.size()%(LIST_LINE_COUNT-2)>0)
			result++;
		else if(result==0)
			result = 1;
		return result;
	}
	
	public void unselectList()
	{	if(selectedRow!=-1)
		{	listPanels.get(currentPage).setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
			selectedRow = -1;
			refreshPreview();
		}		
	}
	
	private void refreshList()
	{	mainPanel.remove(LIST_PANEL_INDEX);
		mainPanel.add(listPanels.get(currentPage),LIST_PANEL_INDEX);
		mainPanel.validate();
		mainPanel.repaint();
	}
	
	public Profile getSelectedProfile()
	{	return selectedProfile;
	}
	public String getSelectedProfileFile()
	{	return selectedProfileFile;
	}

	public void setSelectedProfile(String fileName)
	{	Iterator<Entry<String,String>> it = profiles.iterator();
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
		{	currentPage = index/(LIST_LINE_COUNT-2);
			refreshList();
			unselectList();
			selectedRow = index%(LIST_LINE_COUNT-2)+1;
			listPanels.get(currentPage).setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
			refreshPreview();
		}
	}
}
