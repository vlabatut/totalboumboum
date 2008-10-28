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
import java.awt.image.BufferedImage;
import java.io.File;
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
import fr.free.totalboumboum.configuration.GameConstants;
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
	
	private static final int LINE_COUNT = 19;

	private static final int LINE_NAME = 0;
	private static final int LINE_AI = 1;
	private static final int LINE_HERO = 2;
	private static final int LINE_COLOR = 3;

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
				ln.addLabel(0);
				int col = 0;
				// icon
				{	ln.setLabelKey(col,GuiTools.MENU_PROFILES_EDIT_NAME,true);
					col++;
				}
				// value
				{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					String text = profile.getName();
					String tooltip = text;
					ln.setLabelText(col,text,tooltip);
					col++;
				}
				// change
				{	ln.setLabelKey(col,GuiTools.MENU_PROFILES_EDIT_NAME_CHANGE,true);
					col++;
				}
			}

			// AI
			{	Line ln = editPanel.getLine(LINE_AI);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				int col = 0;
				// icon
				{	ln.setLabelKey(col,GuiTools.MENU_PROFILES_EDIT_AI,true);
					col++;
				}
				// value
				{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					String text = profile.getAiPackname()+File.separator+profile.getAiName();
					String tooltip = text;
					ln.setLabelText(col,text,tooltip);
					col++;
				}
				// change
				{	ln.setLabelKey(col,GuiTools.MENU_PROFILES_EDIT_AI_CHANGE,true);
					col++;
				}
			}

			// HERO
			{	Line ln = editPanel.getLine(LINE_HERO);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				int col = 0;
				// icon
				{	ln.setLabelKey(col,GuiTools.MENU_PROFILES_EDIT_HERO,true);
					col++;
				}
				// value
				{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					String text = profile.getSpritePack()+File.separator+profile.getSpriteName();
					String tooltip = text;
					ln.setLabelText(col,text,tooltip);
					col++;
				}
				// change
				{	ln.setLabelKey(col,GuiTools.MENU_PROFILES_EDIT_HERO_CHANGE,true);
					col++;
				}
			}
			
			// COLORS
			{	for(int i=0;i<profile.getSpriteColors().length;i++)
				{	int line = LINE_COLOR+i;
					Line ln = editPanel.getLine(line);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// icon
					{	BufferedImage image = GuiTools.getIcon(GuiTools.MENU_PROFILES_EDIT_COLOR);
						String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiTools.MENU_PROFILES_EDIT_COLOR+GuiTools.TOOLTIP);
						ln.setLabelIcon(col,image,tooltip+" "+(i+1));
						col++;
					}
					// value
					{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
						String text;
						PredefinedColor c = profile.getSpriteColors()[i];
						if(c!=null)
							text = c.toString();
						else
							text = null;
						String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiTools.MENU_PROFILES_EDIT_COLOR+GuiTools.TOOLTIP);
						ln.setLabelText(col,text,tooltip);
						col++;
					}
					// previous
					{	ln.setLabelKey(col,GuiTools.MENU_PROFILES_EDIT_COLOR_PREVIOUS,true);
						col++;
					}
					// next
					{	ln.setLabelKey(col,GuiTools.MENU_PROFILES_EDIT_COLOR_NEXT,true);
						col++;
					}
				}
			}
		}
		
		setDataPart(editPanel);
	}
		
	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
	{	// nothing to do here
	}
	
	public Profile getProfile()
	{	return profile;	
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
*/	
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}	
}
