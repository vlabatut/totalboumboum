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

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import fr.free.totalboumboum.data.controls.ControlSettings;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ClassTools;
import fr.free.totalboumboum.tools.ImageTools;

public class ControlsData extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	private UntitledSubPanelTable keysPanel;

	public ControlsData(SplitMenuPanel container, int index)
	{	super(container);

		// title
		String txt = getConfiguration().getLanguage().getText(GuiTools.MENU_OPTIONS_CONTROLS_TITLE)+" "+index;
		setTitle(txt);
	
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
			int lines = 16;
			int cols = head.length;
			int w = (int) getDataWidth();
			int h = (int) getDataHeight();
			keysPanel = new UntitledSubPanelTable(w,h,cols,lines,true);

			// headers
			{	for(int col=0;col<head.length;col++)
				{	BufferedImage image = GuiTools.getIcon(head[col]);
					String tooltip = getConfiguration().getLanguage().getText(head[col]+GuiTools.TOOLTIP);
					JLabel lbl = keysPanel.getLabel(0,col);
					lbl.setText(null);
					lbl.setToolTipText(tooltip);
					int lineHeight = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT);
					double zoom = lineHeight/(double)image.getHeight();
					image = ImageTools.resize(image,zoom,true);
					ImageIcon icon = new ImageIcon(image);
					lbl.setIcon(icon);
				}
			}
			// data
			{	ControlSettings controlSettings = getConfiguration().getGameConfiguration().getControlSettings().get(index);
				for(int line=1;line<actions.length+1;line++)
				{	int col = 0;
					int lineHeight = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT);
					// command
					{	JLabel commandLabel = keysPanel.getLabel(line, col++);
						String name = actions[line-1].toUpperCase().substring(0,1);
						name = name + actions[line-1].toLowerCase().substring(1,actions[line-1].length());
						name = "MenuOptionsControlsLineCommand"+name;
						String text = getConfiguration().getLanguage().getText(name);
						commandLabel.setText(text);
						String tooltip = getConfiguration().getLanguage().getText(name+GuiTools.TOOLTIP);
						commandLabel.setToolTipText(tooltip);
						Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
						commandLabel.setBackground(bg);
//						commandLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE,lineHeight));
					}
					// key
					{	JLabel keyLabel = keysPanel.getLabel(line, col++);
						int key = controlSettings.getKeyFromEvent(actions[line-1]);
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
						keyLabel.setText(text);
						keyLabel.setToolTipText(text);
						Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
						keyLabel.setBackground(bg);
//						keyLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE,lineHeight));
					}
					{	JLabel autoLabel = keysPanel.getLabel(line, col++);
						boolean auto = controlSettings.isAutofire(actions[line-1]);
						String text = Boolean.toString(auto);
						autoLabel.setText(text);
						autoLabel.setToolTipText(text);
						Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
						autoLabel.setBackground(bg);
//						autoLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE,lineHeight));
					}
				}
			}
			//
			setDataPart(keysPanel);
//			updateData();
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
