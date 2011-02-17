package org.totalboumboum.gui.common.structure.dialog.inside;

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
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
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
public class InputSubPanel extends ModalDialogSubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	
	public InputSubPanel(int width, int height, String key, List<String> text, String defaultText)
	{	super(width,height);
	
		setTitleKey(key,false);
	
		setContent(text,defaultText);
	}
		
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private MyLabel buttonConfirm;
	private MyLabel buttonCancel;
	private JTextPane inputPane;
	
	public void setContent(List<String> text, String defaultText)
	{	// sizes
		float fontSize = getTitleFontSize()*GuiTools.FONT_TEXT_RATIO;
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(fontSize);
		int buttonsHeight = (int)(GuiTools.getPixelHeight(fontSize)/GuiTools.FONT_RATIO);
		int textHeight = getDataHeight() - 2*buttonsHeight - 2*GuiTools.subPanelMargin;
		
		{	BoxLayout layout = new BoxLayout(getDataPanel(),BoxLayout.PAGE_AXIS); 
			getDataPanel().setLayout(layout);
		}
		
		// message
		{	JPanel textPanel = new JPanel();
			textPanel.setOpaque(false);
			Dimension dim = new Dimension(getDataWidth(),textHeight);
			textPanel.setPreferredSize(dim);
			textPanel.setMinimumSize(dim);
			textPanel.setMaximumSize(dim);
			BoxLayout layout = new BoxLayout(textPanel,BoxLayout.PAGE_AXIS);
			textPanel.setLayout(layout);
			getDataPanel().add(textPanel);
			
			JTextPane textPane = new JTextPane()
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
			textPane.setOpaque(true);
			textPane.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
	
			// styles
			StyledDocument doc = textPane.getStyledDocument();
			SimpleAttributeSet sa = new SimpleAttributeSet();
			// alignment
			StyleConstants.setAlignment(sa,StyleConstants.ALIGN_CENTER);
			// font size
			StyleConstants.setFontFamily(sa,font.getFamily());
			StyleConstants.setFontSize(sa,font.getSize());
			doc.setCharacterAttributes(0,doc.getLength()+1,sa,true);		
			// color
			Color fg = GuiTools.COLOR_TABLE_REGULAR_FOREGROUND;
			StyleConstants.setForeground(sa,fg);
			// set style
			doc.setCharacterAttributes(0,doc.getLength()+1,sa,true);		
			// text
			try
			{	doc.remove(0,doc.getLength());
				for(String txt: text)
					doc.insertString(doc.getLength(),txt+"\n",sa);
			}
			catch (BadLocationException e)
			{	e.printStackTrace();
			}
			doc.setParagraphAttributes(0,doc.getLength()-1,sa,true);		
			textPanel.add(textPane);
		}
		
		getDataPanel().add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));

		// text field
		{	inputPane = new JTextPane()
			{	private static final long serialVersionUID = 1L;
				public void paintComponent(Graphics g)
			    {	Graphics2D g2 = (Graphics2D) g;
		        	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		        	g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		        	super.paintComponent(g2);
			    }			
			};
			inputPane.setEditable(true);
			inputPane.setOpaque(true);
			Dimension dim = new Dimension(getDataWidth(),buttonsHeight);
			inputPane.setPreferredSize(dim);
			inputPane.setMinimumSize(dim);
			inputPane.setMaximumSize(dim);
			inputPane.setAlignmentX(Component.CENTER_ALIGNMENT);
			Color bg = GuiTools.COLOR_TABLE_SELECTED_BACKGROUND;
			inputPane.setBackground(bg);
			Color fg = GuiTools.COLOR_TABLE_REGULAR_FOREGROUND;
			inputPane.setForeground(fg);
			SimpleAttributeSet sa = new SimpleAttributeSet();
			StyleConstants.setAlignment(sa,StyleConstants.ALIGN_CENTER);
			StyleConstants.setFontFamily(sa,font.getFamily());
			StyleConstants.setFontSize(sa,font.getSize());
			StyleConstants.setForeground(sa,fg);
			StyledDocument doc = inputPane.getStyledDocument();
			doc.setParagraphAttributes(0,doc.getLength()+1,sa,true);
			try
			{	doc.remove(0,doc.getLength());
				doc.insertString(doc.getLength(),defaultText,sa);
			}
			catch (BadLocationException e)
			{	e.printStackTrace();
			}
			getDataPanel().add(inputPane);
			inputPane.selectAll();
			inputPane.getCaret().setSelectionVisible(true);
			// request focus to be directly on the text field
			SwingUtilities.invokeLater(new Runnable()
			{	public void run()
				{	inputPane.requestFocusInWindow();
				}
			});
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
			
			// cancel button
			{	String key = GuiKeys.COMMON_DIALOG_CANCEL;			
				buttonCancel = initButton(key,font,buttonsHeight);
				buttonsPanel.add(buttonCancel);
			}

			buttonsPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
			
			// confirm button
			{	String key = GuiKeys.COMMON_DIALOG_CONFIRM;			
				buttonConfirm = initButton(key,font,buttonsHeight);
				buttonsPanel.add(buttonConfirm);
			}
}
		
//		getDataPanel().add(Box.createVerticalGlue());
	}
	
	public String getInput()
	{	String result = inputPane.getText();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// MOUSE LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void mousePressed(MouseEvent e)
	{	Component component = e.getComponent();
		if(component==buttonCancel)
		{	String code = GuiKeys.COMMON_DIALOG_CANCEL;
			fireModalDialogButtonClicked(code);
		}
		else if(component==buttonConfirm)
		{	String code = GuiKeys.COMMON_DIALOG_CONFIRM;
			fireModalDialogButtonClicked(code);
		}
	}
}
