package org.totalboumboum.gui.common.structure.subpanel.content;

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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiImageTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class TextContentPanel extends ContentPanel
{	private static final long serialVersionUID = 1L;
	
	public TextContentPanel(int width, int height, float fontSize)
	{	super(width, height);
//		setBackground(GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
		
		// for some reason, using a JTextPane is very slow
		// (at least for the first call). couldn't find why.
		if(useJTextPane)
		{	JTextPane pane = new JTextPane()
			{	private static final long serialVersionUID = 1L; 
		
				public void paintComponent(Graphics g)
			    {	Graphics2D g2 = (Graphics2D) g;
		        	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		        	g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		        	super.paintComponent(g2);
			    }			
			};
			pane.setEditable(false);
			pane.setHighlighter(null);
			pane.setOpaque(false);
			pane.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			// text components
			doc = pane.getStyledDocument();
			sa = new SimpleAttributeSet();
			// alignment
			StyleConstants.setAlignment(sa,StyleConstants.ALIGN_LEFT/*JUSTIFIED*/);
			// font size
			setFontSize(fontSize);
			// color
			Color fg = GuiColorTools.COLOR_TABLE_REGULAR_FOREGROUND;
			StyleConstants.setForeground(sa,fg);
			// set
//			doc.setParagraphAttributes(0,doc.getLength()-1,sa,true);		
			doc.setCharacterAttributes(0,doc.getLength()+1,sa,true);
			textPane = pane;
		}
		// much faster when using a JTextArea instead of the JTextPane
		else
		{	JTextArea textArea = new JTextArea()
			{	private static final long serialVersionUID = 1L;
				public void paintComponent(Graphics g)
			    {	Graphics2D g2 = (Graphics2D) g;
		        	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		        	g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		        	super.paintComponent(g2);
			    }			
			};
			textArea.setEditable(false);
			textArea.setHighlighter(null);
			textArea.setOpaque(true);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(fontSize);
			textArea.setFont(font);
			textArea.setBackground(GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
			textArea.setForeground(GuiColorTools.COLOR_TABLE_REGULAR_FOREGROUND);
			textPane = textArea;
		}
	
		add(textPane);
		setDim(width,height);
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void switchDisplay(boolean display)
	{	//
	}	
	
	/////////////////////////////////////////////////////////////////
	// CONTENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	private boolean useJTextPane = false;
	private JTextComponent textPane;
	private StyledDocument doc;
	private SimpleAttributeSet sa;

	public void setText(String text)
	{	if(useJTextPane)
		{	try
			{	doc.remove(0,doc.getLength());
				doc.insertString(0,text,sa);
			}
			catch (BadLocationException e)
			{	e.printStackTrace();
			}
		}
		else
		{	textPane.setText(text);
		}
	}

	/////////////////////////////////////////////////////////////////
	// DIMENSION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void setDim(int width, int height)
	{	super.setDim(width, height);
		Dimension dim = new Dimension(width,height);
		textPane.setPreferredSize(dim);
		textPane.setMinimumSize(dim);
		textPane.setMaximumSize(dim);
	}
	
	/////////////////////////////////////////////////////////////////
	// FONT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private float fontSize;

	public void setFontSize(float fontSize)
	{	this.fontSize = fontSize;
		if(useJTextPane)
		{	Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(fontSize);
			StyleConstants.setFontFamily(sa,font.getFamily());
			StyleConstants.setFontSize(sa,font.getSize());
			doc.setCharacterAttributes(0,doc.getLength()+1,sa,true);
		}
		else
		{	Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(fontSize);
			textPane.setFont(font);
		}
	}
	
	public float getFontSize()
	{	return fontSize;
	}
}
