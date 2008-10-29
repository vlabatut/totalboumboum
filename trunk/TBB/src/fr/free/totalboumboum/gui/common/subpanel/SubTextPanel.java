package fr.free.totalboumboum.gui.common.subpanel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;

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

public class SubTextPanel extends SubPanel
{	private static final long serialVersionUID = 1L;
	private float fontSize;
	private JTextPane textPane;
	
	public SubTextPanel(int width, int height, float fontSize)
	{	super(width, height);
		this.fontSize = fontSize;
	
		textPane = new JTextPane()
		{	private static final long serialVersionUID = 1L;
	
			public void paintComponent(Graphics g)
		    {	Graphics2D g2 = (Graphics2D) g;
	        	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	        	g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
	        	super.paintComponent(g2);
		    }			
		};
		textPane.setEditable(false);
		textPane.setHighlighter(null);

		Dimension dim = new Dimension(width,height);
		textPane.setPreferredSize(dim);
		textPane.setMinimumSize(dim);
		textPane.setMaximumSize(dim);
		textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		add(textPane);
	}

	/////////////////////////////////////////////////////////////////
	// CONTENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	public void setText(String text)
	{	// init
		SimpleAttributeSet sa = new SimpleAttributeSet();
		StyleConstants.setAlignment(sa,StyleConstants.ALIGN_JUSTIFIED);
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(fontSize);
		StyleConstants.setFontFamily(sa, font.getFamily());
		StyleConstants.setFontSize(sa, font.getSize());
		StyledDocument doc = textPane.getStyledDocument();
		// set text
		try
		{	doc.insertString(0,text,sa);
		}
		catch (BadLocationException e)
		{	e.printStackTrace();
		}
		doc.setParagraphAttributes(0,doc.getLength()-1,sa,true);		
	}
}
