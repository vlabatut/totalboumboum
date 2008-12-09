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
import java.io.IOException;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileLoader;
import fr.free.totalboumboum.configuration.profile.ProfilesConfiguration;
import fr.free.totalboumboum.gui.common.content.subpanel.browser.FileBrowserSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.browser.FileBrowserSubPanelListener;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class SelectedProfileData extends EntitledDataPanel implements FileBrowserSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;
	
	private static final int VIEW_LINE_NAME = 0;
	private static final int VIEW_LINE_AI_NAME = 1;
	private static final int VIEW_LINE_AI_PACK = 2;
	private static final int VIEW_LINE_HERO_NAME = 3;
	private static final int VIEW_LINE_HERO_PACK = 4;
	private static final int VIEW_LINE_COLOR = 5;

	
	private ProfilesConfiguration profilesConfiguration;
	private FileBrowserSubPanel filePanel;
	private SubPanel mainPanel;
	private UntitledSubPanelTable previewPanel;
	private Profile selectedProfile = null;
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
			
			// list panel
			{	filePanel = new FileBrowserSubPanel(leftWidth,dataHeight);
				filePanel.setShowParent(false);
				HashMap<String,String> fileNames = profilesConfiguration.getProfiles();
				filePanel.setFolder(fileNames);
				filePanel.addListener(this);
				mainPanel.add(filePanel);
			}
			
			mainPanel.add(Box.createHorizontalGlue());
			
			// preview panel
			{	makePreviewPanel(rightWidth,dataHeight);
				mainPanel.add(previewPanel);
			}
			
			setDataPart(mainPanel);
			
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
		if(selectedProfile==null)
		{	for(int i=0;i<values.length;i++)
				values[i] = null;	
			Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
			previewPanel.setLabelBackground(VIEW_LINE_COLOR,1,bg);
		}
		// one player selected
		else
		{	values[VIEW_LINE_NAME] = selectedProfile.getName();
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
	}

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	filePanel.refresh();
	}

	/////////////////////////////////////////////////////////////////
	// PROFILES						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Profile getSelectedProfile()
	{	return selectedProfile;
	}

	public String getSelectedProfileFile()
	{	return filePanel.getSelectedFileName();
	}
	
	public void setSelectedProfile(String fileName)
	{	filePanel.setSelectedFileName(fileName);
	}

	/////////////////////////////////////////////////////////////////
	// FILE BROWSER LISTENER		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void fileBrowserParent()
	{	
	}

	@Override
	public void fileBrowserSelectionChange()
	{	String file = filePanel.getSelectedFileName();
		if(file==null)
			selectedProfile = null;
		else
		{	try
			{	selectedProfile = ProfileLoader.loadProfile(file);			
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
		}
		refreshPreview();
	}
}
