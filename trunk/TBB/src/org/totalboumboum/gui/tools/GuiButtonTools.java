package org.totalboumboum.gui.tools;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import org.totalboumboum.gui.common.structure.ButtonAware;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.tools.images.ImageTools;

/**
 * Sets of fields and methods related
 * to button management in the GUI.
 * 
 * @author Vincent Labatut
 */
public class GuiButtonTools
{	
	/**
	 * Automatically defines the content 
	 * of a button: image or text.
	 * 
	 * @param name
	 * 		Key used to set the button content.
	 * @param button
	 * 		Button to set.
	 */
	public static void setButtonContent(String name, AbstractButton button)
	{	// content
		if(GuiTools.icons.containsKey(name+GuiTools.ICON_NORMAL))
		{	// normal icon
			{	BufferedImage icon = GuiTools.getIcon(name+GuiTools.ICON_NORMAL);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.getResizedImage(icon,zoom*GuiSizeTools.BUTTON_ICON_MARGIN_RATIO,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setIcon(ii);
			}
			// disabled icon
			{	BufferedImage icon = GuiTools.getIcon(name+GuiTools.ICON_DISABLED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.getResizedImage(icon,zoom*GuiSizeTools.BUTTON_ICON_MARGIN_RATIO,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setDisabledIcon(ii);
			}
			// pressed icon
			{	BufferedImage icon = GuiTools.getIcon(name+GuiTools.ICON_PRESSED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.getResizedImage(icon,zoom*GuiSizeTools.BUTTON_ICON_MARGIN_RATIO,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setPressedIcon(ii);
			}
			// selected icon
			{	BufferedImage icon = GuiTools.getIcon(name+GuiTools.ICON_NORMAL_SELECTED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.getResizedImage(icon,zoom*GuiSizeTools.BUTTON_ICON_MARGIN_RATIO,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setSelectedIcon(ii);
			}
			// disabled selected icon
			{	BufferedImage icon = GuiTools.getIcon(name+GuiTools.ICON_DISABLED_SELECTED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.getResizedImage(icon,zoom*GuiSizeTools.BUTTON_ICON_MARGIN_RATIO,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setDisabledSelectedIcon(ii);
			}
			// rollover icon
			{	BufferedImage icon = GuiTools.getIcon(name+GuiTools.ICON_ROLLOVER);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.getResizedImage(icon,zoom*GuiSizeTools.BUTTON_ICON_MARGIN_RATIO,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setRolloverEnabled(true);
				button.setRolloverIcon(ii);
			}
			// rollover selected icon
			{	BufferedImage icon = GuiTools.getIcon(name+GuiTools.ICON_ROLLOVER_SELECTED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.getResizedImage(icon,zoom*GuiSizeTools.BUTTON_ICON_MARGIN_RATIO,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setRolloverSelectedIcon(ii);
			}
			// 
			button.setBorderPainted(false);
			button.setBorder(null);
			button.setMargin(null);
	        button.setContentAreaFilled(false);
	        button.setFocusPainted(false);
		}
		else
		{	// text 
			String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(name);
			button.setText(text);
		}		
		// action command
		button.setActionCommand(name);
		// tooltip
		String tooltipKey = name+GuiKeys.TOOLTIP;
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(tooltipKey);
		button.setToolTipText(tooltip);
	}		
	
	/**
	 * Automatically initializes a 
	 * button and its content.
	 * 
	 * @param result
	 * 		Button to initialize.
	 * @param name
	 * 		Key to use.
	 * @param width
	 * 		Width of the button.
	 * @param height
	 * 		Height of the button.
	 * @param panel
	 * 		Container of the button.
	 */
	private static void initButton(AbstractButton result, String name, int width, int height, ButtonAware panel)
	{	// dimension
		Dimension dim = new Dimension(width,height);
		result.setMinimumSize(dim);
		result.setMaximumSize(dim);
		result.setPreferredSize(dim);
		// set text
		setButtonContent(name,result);
		// add to panel
		if(panel!=null)
		{	panel.add(result);
			result.addActionListener(panel);
		}
	}
	
	/**
	 * Creates and returns a regular button.
	 * 
	 * @param name
	 * 		Key to use for initialization.
	 * @param width
	 * 		Width of the button.
	 * @param height
	 * 		Height of the button.
	 * @param fontSize
	 * 		Font size, if some text must be inserted.
	 * @param panel
	 * 		Container of the button.
	 * @return
	 * 		The generated regular button.
	 */
	public static JButton createButton(String name, int width, int height, int fontSize, ButtonAware panel)
	{	JButton result = new JButton();
		initButton(result,name,width,height,panel);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.setAlignmentY(Component.CENTER_ALIGNMENT);
		// font
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)fontSize);
		result.setFont(font);
		//
		return result;
	}

	/**
	 * Creates and returns a toggle button.
	 * 
	 * @param name
	 * 		Key to use for initialization.
	 * @param width
	 * 		Width of the button.
	 * @param height
	 * 		Height of the button.
	 * @param fontSize
	 * 		Font size, if some text must be inserted.
	 * @param panel
	 * 		Container of the button.
	 * @return
	 * 		The generated toggle button.
	 */
	public static JToggleButton createToggleButton(String name, int width, int height, int fontSize, ButtonAware panel)
	{	JToggleButton result = new JToggleButton();
		initButton(result,name,width,height,panel);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.setAlignmentY(Component.CENTER_ALIGNMENT);
		// font
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)fontSize);
		result.setFont(font);
		//
		return result;
	}
}
