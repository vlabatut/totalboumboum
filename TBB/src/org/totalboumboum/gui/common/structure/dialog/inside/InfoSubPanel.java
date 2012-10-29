package org.totalboumboum.gui.common.structure.dialog.inside;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class InfoSubPanel extends ModalDialogSubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	
	public InfoSubPanel(int width, int height, String key, List<String> text)
	{	super(width,height);
	
		setTitleKey(key,false);
	
		setContent(text);
	}
		
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private MyLabel buttonConfirm;
	
	public void setContent(List<String> text)
	{	// sizes
		float fontSize = getTitleFontSize()*GuiTools.FONT_TEXT_RATIO;
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(fontSize);
		int buttonsHeight = (int)(GuiTools.getPixelHeight(fontSize)/GuiTools.FONT_RATIO);
		int textHeight = getDataHeight() - buttonsHeight - GuiTools.subPanelMargin;
		
		{	BoxLayout layout = new BoxLayout(getDataPanel(),BoxLayout.PAGE_AXIS); 
			getDataPanel().setLayout(layout);
		}
		
		// message
		{	// note: a JTextPane was used before, but it was taking ages to appear (the first time)
			JPanel textPanel = new JPanel();
			textPanel.setOpaque(false);
			Dimension dim = new Dimension(getDataWidth(),textHeight);
			textPanel.setPreferredSize(dim);
			textPanel.setMinimumSize(dim);
			textPanel.setMaximumSize(dim);
			BoxLayout layout = new BoxLayout(textPanel,BoxLayout.PAGE_AXIS);
			textPanel.setLayout(layout);
			getDataPanel().add(textPanel);
			
			JTextArea textArea = new JTextArea()
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
			textArea.setFont(font);
			textArea.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
			textArea.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
			for(String txt: text)
				textArea.append(txt+"\n");
			textPanel.add(textArea);
		}
		
		getDataPanel().add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
		
		// buttons
		{	// buttons panel
			JPanel buttonsPanel = new JPanel();
			buttonsPanel.setOpaque(false);
			Dimension dim = new Dimension(getDataWidth(),buttonsHeight);
			buttonsPanel.setMinimumSize(dim);
			buttonsPanel.setPreferredSize(dim);
			buttonsPanel.setMaximumSize(dim);
			BoxLayout layout = new BoxLayout(buttonsPanel,BoxLayout.LINE_AXIS);
			buttonsPanel.setLayout(layout);
			getDataPanel().add(buttonsPanel);
			
			// confirm button
			String key = GuiKeys.COMMON_DIALOG_CONFIRM;			
			buttonConfirm = initButton(key,font,buttonsHeight);
			buttonsPanel.add(buttonConfirm);
		}
		
//		getDataPanel().add(Box.createVerticalGlue());
	}
	
	/////////////////////////////////////////////////////////////////
	// MOUSE LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void mousePressed(MouseEvent e)
	{	Component component = e.getComponent();
		if(component==buttonConfirm)
		{	String code = GuiKeys.COMMON_DIALOG_CONFIRM;
			fireModalDialogButtonClicked(code);
		}
	}
}
