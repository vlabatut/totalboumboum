package org.totalboumboum.gui.menus.about;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.dialog.inside.ModalDialogSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.tools.files.FileNames;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AboutSubPanel extends ModalDialogSubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	
	public AboutSubPanel(int width, int height)
	{	super(width,height);
	
		String key = GuiKeys.MENU_ABOUT_TITLE;
		String title = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
		setTitleText(title,tooltip);
		
		List<String> text = loadText();
		setContent(text);
	}
		
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private MyLabel buttonConfirm;
	
	public void setContent(List<String> text)
	{	// sizes
		float fontSize = getTitleFontSize()*GuiTools.FONT_TEXT_RATIO;
		int buttonsHeight = (int)(GuiTools.getPixelHeight(fontSize)/GuiTools.FONT_RATIO);
		fontSize = fontSize*0.75f;
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(fontSize);
		int textHeight = getDataHeight() - buttonsHeight - GuiTools.subPanelMargin;
		
		{	BoxLayout layout = new BoxLayout(getDataPanel(),BoxLayout.PAGE_AXIS); 
			getDataPanel().setLayout(layout);
		}
		
		// message
		{	JTextComponent pane = null;
			
			boolean useJTextPane = false;
			// for some reason, using a JTextPane is very slow
			// (at least for the first call). couldn't find why.
			if(useJTextPane)
			{	JTextPane textPane = new JTextPane();
				textPane.setEditable(false);
				textPane.setHighlighter(null);
				textPane.setOpaque(true);
				//textPane.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
					
				// styles
				StyledDocument doc = textPane.getStyledDocument();
				SimpleAttributeSet sa = new SimpleAttributeSet();
				// alignment
				StyleConstants.setAlignment(sa,StyleConstants.ALIGN_JUSTIFIED);
				// font size
				StyleConstants.setFontFamily(sa,"Courier");
				StyleConstants.setFontSize(sa,font.getSize());
				doc.setCharacterAttributes(0,doc.getLength()+1,sa,true);		
				// color
				Color fg = GuiTools.COLOR_TABLE_REGULAR_FOREGROUND;
				StyleConstants.setForeground(sa,fg);
				// set
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
				textPane.setCaretPosition(0);
				pane = textPane;
			}
			// much faster when using a JTextArea instead of the JTextPane
			else
			{	JTextArea textArea = new JTextArea();
				textArea.setEditable(false);
				textArea.setHighlighter(null);
				textArea.setOpaque(true);
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, font.getSize()));
				for(String txt: text)
					textArea.append(txt+"\n");
				textArea.setCaretPosition(0);
				pane = textArea;
			}
			
			JScrollPane textPanel = new JScrollPane(pane);
			textPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			Dimension dim = new Dimension(getDataWidth(),textHeight);
			textPanel.setPreferredSize(dim);
			textPanel.setMinimumSize(dim);
			textPanel.setMaximumSize(dim);
			getDataPanel().add(textPanel);		
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
	
	public List<String> loadText()
	{	List<String> result = new ArrayList<String>();
		String fileName = FileNames.FILE_ABOUT+FileNames.EXTENSION_TEXT;
		File file = new File(fileName);
		try
		{	FileInputStream in = new FileInputStream(file);
			Scanner sc = new Scanner(in);
			while(sc.hasNextLine())
				result.add(sc.nextLine());
			sc.close();
		}
		catch (FileNotFoundException e)
		{	e.printStackTrace();
		}
		return result;
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
