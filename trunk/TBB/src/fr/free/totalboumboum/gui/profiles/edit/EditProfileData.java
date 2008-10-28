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

import javax.swing.JLabel;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.Line;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelLines;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class EditProfileData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINE_COUNT = 20;

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
			editPanel = new UntitledSubPanelLines(w,h,LINE_COUNT,false);
		
			// NAME
			{	Line ln = editPanel.getLine(LINE_NAME);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				int col = 0;
				// icon
				{	ln.setLabelKey(col,GuiTools.MENU_PROFILES_EDIT_NAME,true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// value
				{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					String text = profile.getName();
					String tooltip = text;
					ln.setLabelText(col,text,tooltip);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// change
				{	ln.setLabelKey(col,GuiTools.MENU_PROFILES_EDIT_NAME_CHANGE,true);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
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
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// value
				{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					String text = profile.getAiPackname()+File.separator+profile.getAiName();
					String tooltip = text;
					ln.setLabelText(col,text,tooltip);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// change
				{	ln.setLabelKey(col,GuiTools.MENU_PROFILES_EDIT_AI_CHANGE,true);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
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
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// value
				{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					String text = profile.getSpritePack()+File.separator+profile.getSpriteName();
					String tooltip = text;
					ln.setLabelText(col,text,tooltip);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// change
				{	ln.setLabelKey(col,GuiTools.MENU_PROFILES_EDIT_HERO_CHANGE,true);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
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
						Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
						ln.setLabelBackground(col,bg);
						col++;
					}
					// value
					{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
						refreshColor(i);
						col++;
					}
					// previous
					{	ln.setLabelKey(col,GuiTools.MENU_PROFILES_EDIT_COLOR_PREVIOUS,true);
						Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
						ln.setLabelBackground(col,bg);
						JLabel label = editPanel.getLabel(line,col);
						label.addMouseListener(this);
						col++;
					}
					// next
					{	ln.setLabelKey(col,GuiTools.MENU_PROFILES_EDIT_COLOR_NEXT,true);
						Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
						ln.setLabelBackground(col,bg);
						JLabel label = editPanel.getLabel(line,col);
						label.addMouseListener(this);
						col++;
					}
				}
			}
		}
		
		setDataPart(editPanel);
	}

	private void refreshColor(int colorIndex)
	{	int line = LINE_COLOR+colorIndex;
		PredefinedColor color = profile.getSpriteColors()[colorIndex];
		String text = null;
		String tooltip = null;
		Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
		if(color!=null)
		{	text = color.toString();
			tooltip = text;
			Color clr = color.getColor();
			int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
			bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
		}
		editPanel.setLabelText(line,1,text,tooltip);
		editPanel.setLabelBackground(line,1,bg);
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
	{	JLabel label = (JLabel)e.getComponent();
		int[] pos = editPanel.getLabelPosition(label);
		switch(pos[0])
		{	// NAME
			case LINE_NAME:
				break;
			// AI
			case LINE_AI:
				break;
			// HERO
			case LINE_HERO:
				break;
			// COLOR
			default:
				int colorIndex = pos[0]-LINE_COLOR;
				PredefinedColor colors[] = profile.getSpriteColors();
				PredefinedColor color = colors[colorIndex];
				PredefinedColor allColors[] = PredefinedColor.values();
				boolean colorSet = false;
				if(pos[1]==2)
				{	int i = allColors.length-1;
					if(color!=null)
					{	while(i>=0 && allColors[i]!=color)
							i--;
					}
					while(!colorSet)
					{	if(i==-1)
						{	color = null;
							colorSet = true;
						}
						else
						{	color = allColors[i];
							int j = allColors.length-1;
							while(j>=0 && colors[j]!=color)
								j--;
							if(j==-1)
								colorSet = true;
							else
								i--;
						}	
					}
				}
				else //if(pos{1==3)
				{	int i = 0;
					if(color!=null)
					{	while(i<allColors.length && allColors[i]!=color)
							i++;
					}
					while(!colorSet)
					{	if(i==allColors.length)
						{	color = null;
							colorSet = true;
						}
						else
						{	color = allColors[i];
							int j = 0;
							while(j<colors.length && colors[j]!=color)
								j++;
							if(j==colors.length)
								colorSet = true;
							else
								i++;
						}	
					}
				}
				profile.getSpriteColors()[colorIndex] = color;
				refreshColor(colorIndex);
		}	
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}	
}
