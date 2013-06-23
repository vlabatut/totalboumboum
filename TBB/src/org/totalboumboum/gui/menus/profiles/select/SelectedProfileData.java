package org.totalboumboum.gui.menus.profiles.select;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.profiles.ProfilesConfiguration;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.gui.common.content.subpanel.file.FileBrowserSubPanel;
import org.totalboumboum.gui.common.content.subpanel.file.FileBrowserSubPanelListener;
import org.totalboumboum.gui.common.content.subpanel.profile.ProfileSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.BasicPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class SelectedProfileData extends EntitledDataPanel implements FileBrowserSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;
	
	public SelectedProfileData(SplitMenuPanel container)
	{	super(container);

		// title
		setTitleKey(GuiKeys.MENU_PROFILES_SELECT_TITLE);
		
		BasicPanel mainPanel;
		// data
		{	mainPanel = new BasicPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			mainPanel.setOpaque(false);
			profilesConfiguration = Configuration.getProfilesConfiguration();
			
			// list panel
			{	filePanel = new FileBrowserSubPanel(leftWidth,dataHeight);
				filePanel.setShowParent(false);
				HashMap<String,String> ids = profilesConfiguration.getProfiles();
				HashMap<String,String> fileNames = new HashMap<String, String>();
				for(Entry<String,String> entry: ids.entrySet())
				{	String key = entry.getKey();
					String value = entry.getValue();
					fileNames.put(key,value);
				}
				filePanel.setFileNames(fileNames);
				filePanel.addListener(this);
				mainPanel.add(filePanel);
			}
			
			mainPanel.add(Box.createHorizontalGlue());
			
			// preview panel
			{	miscPanel = new ProfileSubPanel(rightWidth,dataHeight);
				mainPanel.add(miscPanel);
			}
			
			setDataPart(mainPanel);
			
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// PANELS				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ProfilesConfiguration profilesConfiguration;
	private FileBrowserSubPanel filePanel;
	private ProfileSubPanel miscPanel;
	
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
	private Profile selectedProfile = null;
	
	public Profile getSelectedProfile()
	{	return selectedProfile;
	}

	public String getSelectedProfileId()
	{	String result = filePanel.getSelectedFileName();
		//Integer result = Integer.parseInt(fileName);
		return result;
	}
	
	public void setSelectedProfile(String id)
	{	String fileName = id/*.toString()*/;
		filePanel.setSelectedFileName(fileName);
	}

	public String getReplacementProfileId()
	{	//Integer result = null;
		String result = filePanel.getReplacementFileName();
//		if(fileName!=null)
//			result = Integer.parseInt(fileName);
		return result;	
	}
	
	/////////////////////////////////////////////////////////////////
	// FILE BROWSER LISTENER		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void fileBrowserParentReached()
	{	// useless here
	}

	@Override
	public void fileBrowserSelectionChanged()
	{	String file = filePanel.getSelectedFileName();
		if(file==null)
			selectedProfile = null;
		else
		{	try
			{	//int id = Integer.parseInt(file);
				selectedProfile = ProfileLoader.loadProfile(file);			
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
		miscPanel.setProfile(selectedProfile);
		fireDataPanelSelectionChange(null);
	}
}
