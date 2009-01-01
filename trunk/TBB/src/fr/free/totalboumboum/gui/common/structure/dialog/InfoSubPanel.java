package fr.free.totalboumboum.gui.common.structure.dialog;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class InfoSubPanel extends ModalDialogSubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	
	public InfoSubPanel(int width, int height, String title, String tooltip, ArrayList<String> text)
	{	super(width,height,title,tooltip,text);
		
		setTitleText(title,tooltip);
	
		setText(text);
	}
		
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private JLabel buttonConfirm;
	
	public void setText(ArrayList<String> text)
	{	// sizes
		float fontSize = getTitleFontSize()*GuiTools.FONT_TEXT_RATIO;
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(fontSize);
		int buttonsHeight = (int)(GuiTools.getPixelHeight(fontSize)/GuiTools.FONT_RATIO);
		int textHeight = getDataHeight() - buttonsHeight - GuiTools.subPanelMargin;
		
//		getDataPanel().add(Box.createVerticalGlue());
		
		// message
		{	JTextPane textPane = new JTextPane()
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
			textPane.setOpaque(false);
	
			Dimension dim = new Dimension(getDataWidth(),textHeight);
			textPane.setPreferredSize(dim);
			textPane.setMinimumSize(dim);
			textPane.setMaximumSize(dim);
			textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
	
			// styles
			StyledDocument doc = textPane.getStyledDocument();
			SimpleAttributeSet sa = new SimpleAttributeSet();
			// alignment
			StyleConstants.setAlignment(sa,StyleConstants.ALIGN_LEFT/*JUSTIFIED*/);
			// font size
			StyleConstants.setFontFamily(sa,font.getFamily());
			StyleConstants.setFontSize(sa,font.getSize());
			doc.setCharacterAttributes(0,doc.getLength()+1,sa,true);		
			// color
			Color fg = GuiTools.COLOR_TABLE_REGULAR_FOREGROUND;
			StyleConstants.setForeground(sa,fg);
			// set
	//		doc.setParagraphAttributes(0,doc.getLength()-1,sa,true);		
			doc.setCharacterAttributes(0,doc.getLength()+1,sa,true);		
			// text
			try
			{	doc.remove(0,doc.getLength());
				for(String txt: text)
					doc.insertString(0,txt,sa);
			}
			catch (BadLocationException e)
			{	e.printStackTrace();
			}
			getDataPanel().add(textPane);
		}
		
		getDataPanel().add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
		
		// buttons
		{	// buttons panel
			JPanel buttonsPanel = new JPanel();
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
		{	String code = buttonConfirm.getText();
			fireModalDialogButtonClicked(code);
		}
	}
}
