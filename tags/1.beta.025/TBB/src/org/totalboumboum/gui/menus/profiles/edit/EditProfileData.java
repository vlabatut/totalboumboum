package org.totalboumboum.gui.menus.profiles.edit;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.EmptySubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.LinesSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel.Mode;
import org.totalboumboum.gui.common.structure.subpanel.content.EmptyContentPanel;
import org.totalboumboum.gui.common.structure.subpanel.content.Line;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.menus.profiles.ais.SelectedAiSplitPanel;
import org.totalboumboum.gui.menus.profiles.heroes.SelectedHeroSplitPanel;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiImageTools;
import org.totalboumboum.tools.images.ImageTools;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class EditProfileData extends EntitledDataPanel implements MouseListener, DocumentListener
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINE_COUNT = 19;

	private static final int LINE_AI = 0;
	private static final int LINE_HERO = 1;
	private static final int LINE_COLOR = 2;

	private Profile profile;
	private LinesSubPanel editPanel;
	
	public EditProfileData(SplitMenuPanel container, Profile profile)
	{	super(container);
		this.profile = profile.copy();
		
		// title
		setTitleKey(GuiKeys.MENU_PROFILES_EDIT_TITLE);
	
		// data
		{	EmptySubPanel subPanel = (EmptySubPanel)dataPart;
			EmptyContentPanel contentPanel = subPanel.getDataPanel();
			contentPanel.setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);

			int lineHeightEstimation = (subPanel.getDataHeight() - (LINE_COUNT+2)*GuiSizeTools.subPanelMargin) / (LINE_COUNT+1);
			int heightEstimation = (lineHeightEstimation*LINE_COUNT) + (LINE_COUNT+1)*GuiSizeTools.subPanelMargin;
			editPanel = new LinesSubPanel(subPanel.getDataWidth(),heightEstimation,Mode.BORDER,LINE_COUNT,1,false);
			editPanel.setOpaque(false);
			int lineHeight = editPanel.getLineHeight();
			int lineFontSize = editPanel.getLineFontSize();
			int lineWidth = editPanel.getDataWidth();
			int nameHeight = subPanel.getDataHeight() - heightEstimation - GuiSizeTools.subPanelMargin;
			
			// main panel
			{	BoxLayout layout = new BoxLayout(contentPanel,BoxLayout.PAGE_AXIS); 
				contentPanel.setLayout(layout);
				contentPanel.add(Box.createGlue());
			}
			
			// NAME
			{	JPanel namePanel = new JPanel();
				{	BoxLayout layout = new BoxLayout(namePanel,BoxLayout.LINE_AXIS);
					namePanel.setLayout(layout);
					namePanel.setOpaque(false);
					Dimension dim = new Dimension(subPanel.getDataWidth(),nameHeight);
					namePanel.setMinimumSize(dim);
					namePanel.setPreferredSize(dim);
					namePanel.setMaximumSize(dim);		
				}
				namePanel.add(Box.createGlue());
				// icon
				{	MyLabel label = new MyLabel();
					Dimension dim = new Dimension(nameHeight,nameHeight);
					label.setMinimumSize(dim);
					label.setPreferredSize(dim);
					label.setMaximumSize(dim);
					label.setOpaque(true);
					Color bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
					label.setBackground(bg);
					String key = GuiKeys.MENU_PROFILES_EDIT_NAME;
					String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
					label.setToolTipText(tooltip);
					BufferedImage icon = GuiImageTools.getIcon(key);
					double zoom = nameHeight/(double)icon.getHeight();
					icon = ImageTools.getResizedImage(icon,zoom,true);
					ImageIcon icn = new ImageIcon(icon);
					label.setText(null);
					namePanel.add(label);
					label.setIcon(icn);		
				}				
				namePanel.add(Box.createGlue());
				// text pane
				{	Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					Color fg = GuiColorTools.COLOR_TABLE_REGULAR_FOREGROUND;
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
						pane.setEditable(true);
						pane.setOpaque(true);
						pane.setAlignmentX(Component.CENTER_ALIGNMENT);
						sa = new SimpleAttributeSet();
						StyleConstants.setAlignment(sa,StyleConstants.ALIGN_CENTER/*JUSTIFIED*/);
						Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)(lineFontSize));
						StyleConstants.setFontFamily(sa,font.getFamily());
						StyleConstants.setFontSize(sa,font.getSize());
						StyleConstants.setForeground(sa,fg);
						StyledDocument docu = pane.getStyledDocument();
						docu.setParagraphAttributes(0,docu.getLength()+1,sa,true);
						docu.addDocumentListener(this);
						namePanel.add(pane);
						textPane = pane;
						doc = docu;
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
						textArea.setEditable(true);
						textArea.setOpaque(true);
						textArea.setLineWrap(true);
						textArea.setWrapStyleWord(true);
						Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)(lineFontSize));
						textArea.setFont(font);
						textArea.setBackground(GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
						textArea.setForeground(GuiColorTools.COLOR_TABLE_REGULAR_FOREGROUND);
						doc = textArea.getDocument();
						doc.addDocumentListener(this);
						namePanel.add(textArea);
						textPane = textArea;
					}
					
					int nameWidth = lineWidth - nameHeight - GuiSizeTools.subPanelMargin;
					Dimension dim = new Dimension(nameWidth,nameHeight);
					textPane.setPreferredSize(dim);
					textPane.setMinimumSize(dim);
					textPane.setMaximumSize(dim);
					textPane.setBackground(bg);
					textPane.setForeground(fg);
				}
				
				namePanel.add(Box.createGlue());
				contentPanel.add(namePanel);
			}

			int iconWidth = lineHeight;
			// AI
			{	Line ln = editPanel.getLine(LINE_AI);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				int col = 0;
				int textWidth = lineWidth - 3*iconWidth - 4*GuiSizeTools.subPanelMargin;
				int packWidth = textWidth/2;
				int nameWidth = textWidth - packWidth;
				// icon
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
					ln.setLabelKey(col,GuiKeys.MENU_PROFILES_EDIT_AI,true);
					Color bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// pack
				{	ln.setLabelMinWidth(col,packWidth);
					ln.setLabelPrefWidth(col,packWidth);
					ln.setLabelMaxWidth(col,packWidth);
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// name
				{	ln.setLabelMinWidth(col,nameWidth);
					ln.setLabelPrefWidth(col,nameWidth);
					ln.setLabelMaxWidth(col,nameWidth);
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// reset
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
					ln.setLabelKey(col,GuiKeys.MENU_PROFILES_EDIT_AI_RESET,true);
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					MyLabel label = editPanel.getLabel(LINE_AI,col);
					label.addMouseListener(this);
					label.setMouseSensitive(true);
					col++;
				}
				// change
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
					ln.setLabelKey(col,GuiKeys.MENU_PROFILES_EDIT_AI_CHANGE,true);
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					MyLabel label = editPanel.getLabel(LINE_AI,col);
					label.addMouseListener(this);
					label.setMouseSensitive(true);
					col++;
				}
			}

			// HERO
			{	Line ln = editPanel.getLine(LINE_HERO);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				int col = 0;
				int textWidth = lineWidth - 2*iconWidth - 3*GuiSizeTools.subPanelMargin;
				int packWidth = textWidth/2;
				int nameWidth = textWidth - packWidth;
				// icon
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
					ln.setLabelKey(col,GuiKeys.MENU_PROFILES_EDIT_HERO,true);
					Color bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// pack
				{	ln.setLabelMinWidth(col,packWidth);
					ln.setLabelPrefWidth(col,packWidth);
					ln.setLabelMaxWidth(col,packWidth);
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// name
				{	ln.setLabelMinWidth(col,nameWidth);
					ln.setLabelPrefWidth(col,nameWidth);
					ln.setLabelMaxWidth(col,nameWidth);
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// change
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
					ln.setLabelKey(col,GuiKeys.MENU_PROFILES_EDIT_HERO_CHANGE,true);
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					MyLabel label = editPanel.getLabel(LINE_HERO,col);
					label.addMouseListener(this);
					label.setMouseSensitive(true);
					col++;
				}
			}
			
			// COLORS
			{	int line = LINE_COLOR;
				Line ln = editPanel.getLine(line);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				int textWidth = lineWidth - 3*iconWidth - 3*GuiSizeTools.subPanelMargin;
				int col = 0;
				// icon
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
					String key = GuiKeys.MENU_PROFILES_EDIT_COLOR;
					ln.setLabelKey(col,key,true);
					Color bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// value
				{	ln.setLabelMinWidth(col,textWidth);
					ln.setLabelPrefWidth(col,textWidth);
					ln.setLabelMaxWidth(col,textWidth);
					col++;
				}
				// previous
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
					String key = GuiKeys.MENU_PROFILES_EDIT_COLOR_PREVIOUS;
					ln.setLabelKey(col,key,true);
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					MyLabel label = editPanel.getLabel(line,col);
					label.addMouseListener(this);
					label.setMouseSensitive(true);
					col++;
				}
				// next
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
					String key = GuiKeys.MENU_PROFILES_EDIT_COLOR_NEXT;
					ln.setLabelKey(col,key,true);
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					MyLabel label = editPanel.getLabel(line,col);
					label.addMouseListener(this);
					label.setMouseSensitive(true);
					col++;
				}
			}
			
			// EMPTY
			{	for(int line=LINE_COLOR+1;line<LINE_COUNT;line++)
				{	Line ln = editPanel.getLine(line);
					int col = 0;
					int mw = ln.getWidth();
					ln.setLabelMinWidth(col,mw);
					ln.setLabelPrefWidth(col,mw);
					ln.setLabelMaxWidth(col,mw);
					col++;
				}
			}

			contentPanel.add(editPanel);
			refreshData();
		}
		
	}

	@Override
	public void refresh()
	{	refreshData();
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
	{	MyLabel label = (MyLabel)e.getComponent();
		int[] pos = editPanel.getLabelPosition(label);
		switch(pos[0])
		{	// AI
			case LINE_AI:
				if(pos[1]==3)
				{	profile.setAiName(null);
					profile.setAiPackname(null);
					refreshAi();
				}
				else if(pos[1]==4)
				{	SelectedAiSplitPanel aiPanel = new SelectedAiSplitPanel(container.getMenuContainer(),container,profile);
					getMenuContainer().replaceWith(aiPanel);
				}
				break;
			// HERO
			case LINE_HERO:
				SelectedHeroSplitPanel heroPanel = new SelectedHeroSplitPanel(container.getMenuContainer(),container,profile);
				getMenuContainer().replaceWith(heroPanel);
				break;
			// COLOR
			case LINE_COLOR:
				PredefinedColor color = profile.getDefaultSprite().getColor();
				PredefinedColor allColors[] = PredefinedColor.values();
				if(pos[1]==2)
				{	int i = allColors.length-1;
					if(color!=null)
					{	while(i>=0 && allColors[i]!=color)
							i--;
					}
					color = allColors[(i-1+allColors.length)%allColors.length];
				}
				else //if(pos{1==3)
				{	int i = 0;
					if(color!=null)
					{	while(i<allColors.length && allColors[i]!=color)
							i++;
					}
					color = allColors[(i+1)%allColors.length];
				}
				profile.getDefaultSprite().setColor(color);
				refreshColor();
		}	
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}	
	
	private void refreshData()
	{	refreshName();
		refreshAi();
		refreshHero();
		refreshColor();
	}
	
	private void refreshColor()
	{	PredefinedColor color = profile.getDefaultSprite().getColor();
		String text = null;
		String tooltip = null;
		Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
		if(color!=null)
		{	String colorKey = color.toString();
			colorKey = colorKey.toUpperCase().substring(0,1)+colorKey.toLowerCase().substring(1,colorKey.length());
			colorKey = GuiKeys.COMMON_COLOR+colorKey;
			text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(colorKey); 
			tooltip = text;
			Color clr = color.getColor();
			int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
			bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
		}
		editPanel.setLabelText(LINE_COLOR,1,text,tooltip);
		editPanel.setLabelBackground(LINE_COLOR,1,bg);
	}
	
	private void refreshName()
	{	// name
		String text = profile.getName();
		String tooltip = text;
		
		if(useJTextPane)
		{	try
			{	StyledDocument docu = (StyledDocument)doc;
				docu.remove(0,docu.getLength());
				docu.insertString(0,text,sa);
				docu.setParagraphAttributes(0,docu.getLength()+1,sa,true);
			}
			catch (BadLocationException e)
			{	e.printStackTrace();
			}
		}
		else
		{
			textPane.setText(text);
		}
		textPane.setToolTipText(tooltip);		
	}
	
	private void refreshAi()
	{	// init
		Line ln = editPanel.getLine(LINE_AI);
		int col = 1;		
		// pack
		{	String text = profile.getAiPackname();
			String tooltip = text;
			ln.setLabelText(col,text,tooltip);
			col++;
		}		
		// name
		{	String text = profile.getAiName();
			String tooltip = text;
			ln.setLabelText(col,text,tooltip);
			col++;
		}
	}
	
	private void refreshHero()
	{	// init
		Line ln = editPanel.getLine(LINE_HERO);
		int col = 1;		
		// pack
		{	String text = profile.getSpritePack();
			String tooltip = text;
			ln.setLabelText(col,text,tooltip);
			col++;
		}		
		// name
		{	String text = profile.getSpriteName();
			String tooltip = text;
			ln.setLabelText(col,text,tooltip);
			col++;
		}
	}

	/////////////////////////////////////////////////////////////////
	// DOCUMENT LISTENER	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean useJTextPane = false;
	private JTextComponent textPane;
	private Document doc;
	private SimpleAttributeSet sa;

	@Override
	public void changedUpdate(DocumentEvent e)
	{	// not usefull here
	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{	Document document = e.getDocument();
		try
		{	//doc.setParagraphAttributes(0,doc.getLength()+1,sa,true);
			String name = document.getText(0,document.getLength());
			profile.setName(name);
			textPane.setToolTipText(name);
		}
		catch (BadLocationException e1)
		{	e1.printStackTrace();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{	Document document = e.getDocument();
		try
		{	String name = document.getText(0,document.getLength());
			profile.setName(name);
			textPane.setToolTipText(name);
		}
		catch (BadLocationException e1)
		{	e1.printStackTrace();
		}
	}
}
