package fr.free.totalboumboum.gui.options.controls;

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

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;

import fr.free.totalboumboum.data.controls.ControlSettings;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ClassTools;

public class ControlsData extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	private UntitledSubPanelTable keysPanel;

	public ControlsData(SplitMenuPanel container, int index)
	{	super(container);

		// title
		{	String text = getConfiguration().getLanguage().getText(GuiTools.MENU_OPTIONS_CONTROLS_TITLE)+" "+index;
			String tooltip = getConfiguration().getLanguage().getText(GuiTools.MENU_OPTIONS_CONTROLS_TITLE+GuiTools.TOOLTIP);
			setTitleText(text,tooltip);
		}
	
		// data
		{	
			String actions[] = 
			{	ControlEvent.UP,
				ControlEvent.RIGHT,
				ControlEvent.DOWN,
				ControlEvent.LEFT,
				ControlEvent.DROPBOMB,
				ControlEvent.PUNCHBOMB
			};
			String head[] = 
			{	GuiTools.MENU_OPTIONS_CONTROLS_HEADER_COMMAND,
				GuiTools.MENU_OPTIONS_CONTROLS_HEADER_KEY,
				GuiTools.MENU_OPTIONS_CONTROLS_HEADER_AUTO
			};
			int lines = 20;
			int cols = head.length;
			int w = getDataWidth();
			int h = getDataHeight();
			keysPanel = new UntitledSubPanelTable(w,h,cols,lines,true);

			// headers
			{	ArrayList<String> keys = new ArrayList<String>();
				ArrayList<Boolean> imageFlags = new ArrayList<Boolean>();
				for(int i=0;i<head.length;i++)
				{	keys.add(head[i]);
					imageFlags.add(true);
				}
				keysPanel.setLineKeysSimple(0, keys, imageFlags);
				//keysPanel.setColumnWidth(0,Integer.MAX_VALUE);
				keysPanel.setSubColumnsWidth(1,Integer.MAX_VALUE);
			}			
			
			// data
			{	ControlSettings controlSettings = getConfiguration().getGameConfiguration().getControlSettings().get(index);
				for(int line=1;line<actions.length+1;line++)
				{	int col = 0;
					// command
					{	String name = actions[line-1].toUpperCase().substring(0,1);
						name = name + actions[line-1].toLowerCase().substring(1,actions[line-1].length());
						String key = "MenuOptionsControlsLineCommand"+name;
						keysPanel.setLabelKey(line,col,key,false);
						col++;
					}
					// key
					{	int key = controlSettings.getKeyFromEvent(actions[line-1]);
						String text = null;
						if(key>=0)
						{	try
							{	Field field = ClassTools.getFieldFromValue(key,KeyEvent.class);
								text = field.getName();
							}
							catch (IllegalArgumentException e)
							{	e.printStackTrace();
							}
							catch (IllegalAccessException e)
							{	e.printStackTrace();
							}
						}
						keysPanel.setLabelText(line,col,text,text);
						col++;
					}
					// autofire
					{	boolean auto = controlSettings.isAutofire(actions[line-1]);
						String key;
						if(auto)
							key = GuiTools.MENU_OPTIONS_CONTROLS_LINE_AUTO_TRUE;
						else
							key = GuiTools.MENU_OPTIONS_CONTROLS_LINE_AUTO_FALSE;
						keysPanel.setLabelKey(line,col,key,true);
						col++;
					}
				}
			}
			
			setDataPart(keysPanel);
		}
	}

	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
	{	// nothing to do here
	}
}
