package fr.free.totalboumboum.gui.game.match.description;

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
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.free.totalboumboum.data.profile.Portraits;
import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.game.round.description.RoundDescription;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class MatchDescription extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.6f;

	public MatchDescription(SplitMenuPanel container)
	{	super(container);
	
		// title
		String key = GuiTools.GAME_MATCH_DESCRIPTION_TITLE;
		setTitleKey(key);
		
		// data
		{	SubPanel infoPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(infoPanel,BoxLayout.LINE_AXIS); 
				infoPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			Match match = getConfiguration().getCurrentMatch();			
			infoPanel.setOpaque(false);
			
			// players panel
			{	JPanel playersPanel = makePlayersPanel(leftWidth,dataHeight);
				infoPanel.add(playersPanel);
			}
			
			infoPanel.add(Box.createHorizontalGlue());
			
			// right panel
			{	JPanel rightPanel = new JPanel();
				{	BoxLayout layout = new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS); 
					rightPanel.setLayout(layout);
				}
				rightPanel.setOpaque(false);
				int upHeight = (dataHeight - margin)/2;
				int downHeight = dataHeight - upHeight - margin;
				
				// points panel
				{	SubPanel pointsPanel = RoundDescription.makePointsPanel(rightWidth,downHeight,match.getPointProcessor(),"Match");
					rightPanel.add(pointsPanel);
				}

				rightPanel.add(Box.createVerticalGlue());
				
				// limit panel
				{	SubPanel limitsPanel = RoundDescription.makeLimitsPanel(rightWidth,downHeight,match.getLimits(),"Match");
					rightPanel.add(limitsPanel);
				}
				infoPanel.add(rightPanel);
			}

			setDataPart(infoPanel);
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
	
	private SubPanel makePlayersPanel(int width, int height)
	{	Match match = getConfiguration().getCurrentMatch();
		int lines = 16+1;
		int cols = 2+1;
		UntitledSubPanelTable playersPanel = new UntitledSubPanelTable(width,height,cols,lines,true);
		// headers
		{	{	JLabel lbl = playersPanel.getLabel(0,0);
				lbl.setOpaque(false);
			}
			String keys[] = 
			{	GuiTools.GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_NAME,
				GuiTools.GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_RANK
			};
			for(int i=1;i<keys.length+1;i++)
				playersPanel.setLabelKey(0,i,keys[i-1],true);
		}
		// empty
		{	playersPanel.setSubColumnsWidth(1,Integer.MAX_VALUE);
		}
		// data
		{	ArrayList<Profile> players = match.getProfiles();
			Iterator<Profile> i = players.iterator();
			int line = 1;
			while(i.hasNext())
			{	int col = 0;
				Profile profile = i.next();
				// color
				Color clr = profile.getSpriteColor().getColor();
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				playersPanel.setLineBackground(line,bg);
				// portrait
				{	BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
					String tooltip = profile.getName();
					playersPanel.setLabelIcon(line,col,image,tooltip);
					col++;
				}
				// name
				{	String text = profile.getName();
					String tooltip = profile.getName();
					playersPanel.setLabelText(line,col,text,tooltip);
					col++;
				}
				// rank
				{	NumberFormat nf = NumberFormat.getInstance();
					nf.setMinimumFractionDigits(0);
					String text = "-";
					String tooltip = "-";
					playersPanel.setLabelText(line,col,text,tooltip);
					col++;
				}
				//
				line++;
			}
		}
		return playersPanel;		
	}
/*	
	private JPanel makeNotesPanel()
	{	// init
		int width = GuiTools.getSize(GuiTools.GAME_DESCRIPTION_PANEL_WIDTH);
		int height = GuiTools.getSize(GuiTools.GAME_DESCRIPTION_PANEL_HEIGHT);
		EntitledSubPanel notesPanel = new EntitledSubPanel(width,height,getConfiguration());
		// title
		String text = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_NOTES);
		String tooltip = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_NOTES+GuiTools.TOOLTIP);
		notesPanel.setTitle(text,tooltip);
		// text panel
		{	JTextPane textPane = new JTextPane()
			{	public void paintComponent(Graphics g)
			    {	Graphics2D g2 = (Graphics2D) g;
		        	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		        	g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		        	super.paintComponent(g2);
			    }			
			};
			textPane.setEditable(false);
			textPane.setHighlighter(null);
//			textPane.putClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY, new Boolean(true));
//			textPane.setSelectedTextColor(null);
//			textPane.setEnabled(false);
//			textPane.setDisabledTextColor(Color.BLACK);

//			textPane.setCaret(new Caret()
//	        {	public void install(JTextComponent c){}
//	            public void deinstall(JTextComponent c){}
//	            public void paint(Graphics g){}
//	            public void addChangeListener(ChangeListener l){}
//	            public void removeChangeListener(ChangeListener l){}
//	            public boolean isVisible(){return false;}
//	            public void setVisible(boolean v){}
//	            public boolean isSelectionVisible(){return false;}
//	            public void setSelectionVisible(boolean v){}
//	            public void setMagicCaretPosition(Point p){}
//	            public Point getMagicCaretPosition(){return new Point(0,0);}
//	            public void setBlinkRate(int rate){}
//	            public int getBlinkRate(){return 10000;}
//	            public int getDot(){return 0;}
//	            public int getMark(){return 0;}
//	            public void setDot(int dot){}
//	            public void moveDot(int dot){}
//	        });
				        
			SimpleAttributeSet sa = new SimpleAttributeSet();
			StyleConstants.setAlignment(sa,StyleConstants.ALIGN_JUSTIFIED);
			Font font = getConfiguration().getFont().deriveFont((float)GuiTools.getSize(GuiTools.GAME_DESCRIPTION_LABEL_TEXT_FONT_SIZE));
			StyleConstants.setFontFamily(sa, font.getFamily());
			StyleConstants.setFontSize(sa, font.getSize());
			StyledDocument doc = textPane.getStyledDocument();
			Match match = getConfiguration().getCurrentMatch();			
			text = "";
			ArrayList<String> list = match.getNotes();
			Iterator<String> i = list.iterator();
			while (i.hasNext())
			{	String temp = i.next();
				text = text + temp + "\n";
			}
			try
			{	doc.insertString(0,text,sa);
			}
			catch (BadLocationException e)
			{	e.printStackTrace();
			}
			doc.setParagraphAttributes(0,doc.getLength()-1,sa,true);
			//
			JComponent dataComp = notesPanel.getDataPanel();
			textPane.setBackground(dataComp.getBackground());
			Dimension dim = dataComp.getPreferredSize();
			textPane.setPreferredSize(dim);
			textPane.setMinimumSize(dim);
			textPane.setMaximumSize(dim);
			textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
			notesPanel.setDataPanel(textPane);
		}
		return notesPanel;		
	}
*/	

}
