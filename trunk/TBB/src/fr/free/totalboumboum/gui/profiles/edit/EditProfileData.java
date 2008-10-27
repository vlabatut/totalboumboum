package fr.free.totalboumboum.gui.profiles.edit;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileLoader;
import fr.free.totalboumboum.configuration.profile.ProfilesConfiguration;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.Line;
import fr.free.totalboumboum.gui.common.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelLines;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class EditProfileData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINE_COUNT = 21;

	private static final int LINE_NAME = 0;
	private static final int LINE_AI_NAME = 1;
	private static final int LINE_AI_PACK = 2;
	private static final int LINE_HERO_NAME = 3;
	private static final int LINE_HERO_PACK = 4;
	private static final int LINE_COLOR = 5;

	private Profile profile;
	private UntitledSubPanelLines editPanel;
	
	public EditProfileData(SplitMenuPanel container, Profile profile)
	{	super(container);
		this.profile = profile.copy();
		
		// title
		setTitleKey(GuiTools.MENU_PROFILES_EDIT_TITLE);
	
		// data
		{	int w = getDataWidth();
			int h = getDataHeight();
			editPanel = new UntitledSubPanelLines(w,h,LINE_COUNT,true);
		
			// NAME
			{	Line ln = editPanel.getLine(LINE_NAME);
				ln.addLabel(0);
				ln.addLabel(0);
				int col = 0;
				// icon
				{	ln.setLabelKey(col,GuiTools.MENU_PROFILES_PREVIEW_NAME,true);
					col++;
				}
				// value
				{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					String text = profile.getName();
					String tooltip = text;
					ln.setLabelText(col,text,tooltip);
					col++;
				}
			}
			
			
			
			
			
			mainPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			mainPanel.setOpaque(false);
			profilesConfiguration = Configuration.getProfilesConfiguration().copy();
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
				result = name0.compareTo(name1);
				return result;
			}
		});
	}
	
	public void makeListPanels(int width, int height)
	{	int lines = LIST_LINE_COUNT;
		int cols = 1;
		listPanels = new ArrayList<UntitledSubPanelTable>();
		
		for(int panelIndex=0;panelIndex<getPageCount();panelIndex++)
		{	UntitledSubPanelTable listPanel = new UntitledSubPanelTable(width,height,cols,lines,false);
			listPanel.setSubColumnsMaxWidth(0,Integer.MAX_VALUE);
		
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
				String key = GuiTools.MENU_PROFILES_LIST_PAGEUP;
				listPanel.setLabelKey(LIST_LINE_PREVIOUS,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_PREVIOUS,0);
				label.addMouseListener(this);
			}
			// page down
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_NEXT,0,bg);
				String key = GuiTools.MENU_PROFILES_LIST_PAGEDOWN;
				listPanel.setLabelKey(LIST_LINE_NEXT,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_NEXT,0);
				label.addMouseListener(this);
			}
			listPanels.add(listPanel);
		}
	}
	
	public void makePreviewPanel(int width, int height)
	{	int lines = 21;
		int colSubs = 2;
		int colGroups = 1;
		previewPanel = new UntitledSubPanelTable(width,height,colGroups,colSubs,lines,true);
		
		// data
		String keys[] = 
		{	GuiTools.MENU_PROFILES_PREVIEW_NAME,
			GuiTools.MENU_PROFILES_PREVIEW_AINAME,
			GuiTools.MENU_PROFILES_PREVIEW_AIPACK,
			GuiTools.MENU_PROFILES_PREVIEW_HERONAME,
			GuiTools.MENU_PROFILES_PREVIEW_HEROPACK,
			GuiTools.MENU_PROFILES_PREVIEW_COLOR,			
			GuiTools.MENU_PROFILES_PREVIEW_COLOR,			
			GuiTools.MENU_PROFILES_PREVIEW_COLOR,			
			GuiTools.MENU_PROFILES_PREVIEW_COLOR,			
			GuiTools.MENU_PROFILES_PREVIEW_COLOR,			
			GuiTools.MENU_PROFILES_PREVIEW_COLOR,			
			GuiTools.MENU_PROFILES_PREVIEW_COLOR,			
			GuiTools.MENU_PROFILES_PREVIEW_COLOR,			
			GuiTools.MENU_PROFILES_PREVIEW_COLOR,			
			GuiTools.MENU_PROFILES_PREVIEW_COLOR,			
			GuiTools.MENU_PROFILES_PREVIEW_COLOR,			
			GuiTools.MENU_PROFILES_PREVIEW_COLOR,			
			GuiTools.MENU_PROFILES_PREVIEW_COLOR,			
			GuiTools.MENU_PROFILES_PREVIEW_COLOR,			
			GuiTools.MENU_PROFILES_PREVIEW_COLOR,			
			GuiTools.MENU_PROFILES_PREVIEW_COLOR			
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
				String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(keys[line]+GuiTools.TOOLTIP);
				previewPanel.setLabelText(line,colSub,text,tooltip);
				if(line>0)
				{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					previewPanel.setLabelBackground(line,colSub,bg);
				}
				colSub++;
			}
		}
		int maxWidth = width-3*GuiTools.subPanelMargin-previewPanel.getHeaderHeight();
		previewPanel.setSubColumnsMaxWidth(1,maxWidth);
		previewPanel.setSubColumnsPreferredWidth(1,maxWidth);
	}
	
	private void refreshPreview()
	{	String values[] = new String[21];
		// no player selected
		if(selectedRow<0)
		{	for(int i=0;i<values.length;i++)
				values[i] = null;			
		}
		// one player selected
		else
		{	Entry<String,String> entry = profiles.get((selectedRow-1)+currentPage*(LIST_LINE_COUNT-2));
			Profile profile = null;
			try
			{	profile = ProfileLoader.loadProfile(entry.getKey());
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
			values[VIEW_LINE_NAME] = profile.getName();
			values[VIEW_LINE_AI_NAME]= profile.getAiName();
			values[VIEW_LINE_AI_PACK] = profile.getAiPackname();
			values[VIEW_LINE_HERO_NAME] = profile.getSpriteName();
			values[VIEW_LINE_HERO_PACK] = profile.getSpritePack();
			PredefinedColor[] colors = profile.getSpriteColors();
			for(int i=0;i<colors.length;i++)
			{	if(colors[i]!=null)
					values[VIEW_LINE_COLOR+i] = colors[i].toString();
				else
					values[VIEW_LINE_COLOR+i] = null;
			}
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
/*		
	private void setAdjust()
	{	boolean adjust = engineConfiguration.getAutoFps();
		String key;
		if(adjust)
			key = GuiTools.MENU_OPTIONS_ADVANCED_LINE_ADJUST_ENABLED;
		else
			key = GuiTools.MENU_OPTIONS_ADVANCED_LINE_ADJUST_DISABLED;
		optionsPanel.getLine(LINE_ADJUST).setLabelKey(1,key,true);
	}
	
	private void setFps()
	{	int fps = engineConfiguration.getFps();
		String text = Integer.toString(fps);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiTools.MENU_OPTIONS_ADVANCED_LINE_FPS_TITLE+GuiTools.TOOLTIP); 
		optionsPanel.getLine(LINE_FPS).setLabelText(2,text,tooltip);
	}
	
	private void setGameSpeed()
	{	double speed = engineConfiguration.getSpeedCoeff();
		String text = null;
		int i = 0;
		while(i<speedValues.length && text==null)
		{	if(speedValues[i]==speed)
				text = speedTexts[i];
			else
				i++;
		}
		if(text==null)
		{	NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(2);
			nf.setMinimumFractionDigits(2);
			text = nf.format(speed);
		}
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiTools.MENU_OPTIONS_ADVANCED_LINE_SPEED_TITLE+GuiTools.TOOLTIP); 
		optionsPanel.getLine(LINE_SPEED).setLabelText(2,text,tooltip);
	}
*/	
	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
	{	// nothing to do here
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
	
	private void unselectList()
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
	
	
	
	
	
	
	public Profile getProfile()
	{	return profile;	
	}
}
