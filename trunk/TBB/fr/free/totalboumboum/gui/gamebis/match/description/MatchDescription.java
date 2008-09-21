package fr.free.totalboumboum.gui.gamebis.match.description;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.security.auth.callback.TextOutputCallback;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import fr.free.totalboumboum.data.profile.Portraits;
import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.data.statistics.Score;
import fr.free.totalboumboum.data.statistics.StatisticRound;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.point.PlayerPoints;
import fr.free.totalboumboum.gui.generic.InnerDataPanel;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SimpleMenuPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;
import fr.free.totalboumboum.gui.menus.options.OptionsMenu;
import fr.free.totalboumboum.gui.menus.tournament.TournamentMain;
import fr.free.totalboumboum.gui.tools.SpringUtilities;
import fr.free.totalboumboum.gui.tools.SwingTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public class MatchDescription extends InnerDataPanel
{	
	private static final long serialVersionUID = 1L;

	public MatchDescription(SplitMenuPanel container)
	{	super(container);
		// layout
		{	BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
			setLayout(layout);
		}
		// size
		int height = SwingTools.getSize(SwingTools.HORIZONTAL_SPLIT_DATA_PANEL_HEIGHT);
		int width = SwingTools.getSize(SwingTools.HORIZONTAL_SPLIT_DATA_PANEL_WIDTH);
		setPreferredSize(new Dimension(width,height));
		// background
//		setBackground(new Color(50,50,50));
		setOpaque(false);
		
		add(Box.createVerticalGlue());

		// title
		{	String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_TITLE_DESCRIPTION);
			JLabel title = new JLabel(txt);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			Font font = getConfiguration().getFont().deriveFont((float)SwingTools.getSize(SwingTools.GAME_TITLE_FONT_SIZE));
			title.setForeground(Color.BLACK);
			title.setBackground(new Color(255,255,255,200));
			title.setOpaque(true);
			title.setFont(font);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			Dimension dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DATA_PANEL_WIDTH),SwingTools.getSize(SwingTools.GAME_DATA_LABEL_TITLE_HEIGHT));
			title.setPreferredSize(dim);
			title.setMinimumSize(dim);
			title.setMaximumSize(dim);
			add(title);
		}

		add(Box.createVerticalGlue());

		// infos panel
		{	Match match = getConfiguration().getTournament().getCurrentMatch();
			JPanel infoPanel = new JPanel();
			{	BoxLayout layout = new BoxLayout(infoPanel,BoxLayout.LINE_AXIS); 
				infoPanel.setLayout(layout);
			}
			Dimension dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DATA_PANEL_WIDTH),SwingTools.getSize(SwingTools.GAME_DATA_PANEL_HEIGHT));
			infoPanel.setPreferredSize(dim);
			infoPanel.setMinimumSize(dim);
			infoPanel.setMaximumSize(dim);
			infoPanel.setOpaque(false);
			//
			// players panel
			{	int playerNumber = match.getProfiles().size();
				playerNumber = 16;
				int lines = playerNumber+1;
				int cols = 2+1;
				SpringLayout layout = new SpringLayout();			
				JPanel playersPanel = new JPanel(layout);
				playersPanel.setBackground(new Color(255,255,255,200));
				dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DESCRIPTION_PANEL_WIDTH),SwingTools.getSize(SwingTools.GAME_DATA_PANEL_HEIGHT));
				playersPanel.setPreferredSize(dim);
				playersPanel.setMinimumSize(dim);
				playersPanel.setMaximumSize(dim);
				// headers
				{	Font font = getConfiguration().getFont().deriveFont((float)SwingTools.getSize(SwingTools.GAME_RESULTS_HEADER_FONT_SIZE));
					{	JLabel lbl = new JLabel(" ");
						lbl.setOpaque(false);
						lbl.setPreferredSize(new Dimension(SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_LINE_HEIGHT),SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
						lbl.setMaximumSize(new Dimension(SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_LINE_HEIGHT),SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
						lbl.setMinimumSize(new Dimension(SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_LINE_HEIGHT),SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
						playersPanel.add(lbl);
					}
					BufferedImage[] icons = 
					{	SwingTools.getIcon(GuiTools.GAME_MATCH_HEADER_NAME),
						SwingTools.getIcon(GuiTools.GAME_MATCH_HEADER_RANK)
					};
					String[] tooltips = 
					{	getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_NAME+"Tooltip"),
						getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_RANK+"Tooltip")
					};
					for(int i=0;i<icons.length;i++)
					{	JLabel lbl = new JLabel(" ");
						lbl.setText("");
						lbl.setToolTipText(tooltips[i]);
						lbl.setHorizontalAlignment(SwingConstants.CENTER);
						lbl.setBackground(new Color(0,0,0,128));
						lbl.setOpaque(true);
						playersPanel.add(lbl);
						int lineHeight = SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_HEADER_HEIGHT);
						double zoom = lineHeight/(double)icons[i].getHeight();
							icons[i] = ImageTools.resize(icons[i],zoom,true);
						ImageIcon icon = new ImageIcon(icons[i]);
						lbl.setIcon(icon);
					}
				}
				// empty
				{	Font font = getConfiguration().getFont().deriveFont((float)SwingTools.getSize(SwingTools.GAME_RESULTS_LINE_FONT_SIZE));
					for(int i=1;i<lines;i++)
					{	// portrait
						{	JLabel lbl = new JLabel(" ");
							lbl.setHorizontalAlignment(SwingConstants.CENTER);
							lbl.setBackground(new Color(0,0,0,20));
							lbl.setOpaque(true);
							playersPanel.add(lbl);
						}
						// name
						{	JLabel lbl = new JLabel(" ");
							lbl.setFont(font);
							lbl.setHorizontalAlignment(SwingConstants.CENTER);
							lbl.setBackground(new Color(0,0,0,20));
							lbl.setForeground(Color.BLACK);
							lbl.setOpaque(true);
							Dimension dimension = new Dimension(Integer.MAX_VALUE,SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_LINE_HEIGHT));
							lbl.setMaximumSize(dimension);
							dimension = new Dimension(SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_HEADER_HEIGHT),SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_LINE_HEIGHT));
							lbl.setMinimumSize(dimension);
							playersPanel.add(lbl);
						}
						// rank
						for(int j=2;j<cols;j++)
						{	JLabel lbl = new JLabel(" ");
							lbl.setFont(font);
							lbl.setHorizontalAlignment(SwingConstants.CENTER);
							lbl.setBackground(new Color(0,0,0,20));
							lbl.setForeground(Color.BLACK);
							lbl.setOpaque(true);
							playersPanel.add(lbl);
						}
					}
				}
				// data
				ArrayList<Profile> players = match.getProfiles();
				Iterator<Profile> i = players.iterator();
				int k = cols;
				int lineHeight = SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_LINE_HEIGHT);
				while(i.hasNext())
				{	Profile profile = i.next();
					// color
					Color clr = profile.getSpriteColor().getColor();
					// portrait
					{	JLabel portraitLabel = (JLabel)playersPanel.getComponent(k++);
						BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
						double zoom = lineHeight/(double)image.getHeight();
						image = ImageTools.resize(image,zoom,true);
						ImageIcon icon = new ImageIcon(image);
						portraitLabel.setIcon(icon);
						Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),120);
						portraitLabel.setBackground(bg);			
						portraitLabel.setText("");
					}
					// name
					{	JLabel nameLabel = (JLabel)playersPanel.getComponent(k++);
						nameLabel.setText(profile.getName());
						nameLabel.setToolTipText(profile.getName());
						Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),120);
						nameLabel.setBackground(bg);
					}
					// scores
					{	NumberFormat nf = NumberFormat.getInstance();
						nf.setMinimumFractionDigits(0);
						JLabel rankLabel = (JLabel)playersPanel.getComponent(k++);
						rankLabel.setText("0");
						Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),100);
						rankLabel.setBackground(bg);
					}			
				}
				//
				int margin = SwingTools.getSize(SwingTools.GAME_RESULTS_MARGIN_SIZE);
				SpringUtilities.makeCompactGrid(playersPanel,lines,cols,margin,margin,margin,margin);
				infoPanel.add(playersPanel);
			}
			infoPanel.add(Box.createHorizontalGlue());
			// text panel
			{	JPanel textPanel = new JPanel();
				{	BoxLayout layout = new BoxLayout(textPanel,BoxLayout.PAGE_AXIS); 
					textPanel.setLayout(layout);
				}
				textPanel.setOpaque(false);
				dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DESCRIPTION_PANEL_WIDTH),SwingTools.getSize(SwingTools.GAME_DATA_PANEL_HEIGHT));
				textPanel.setPreferredSize(dim);
				textPanel.setMinimumSize(dim);
				textPanel.setMaximumSize(dim);
				//
				// rounds panel
				{	JPanel roundsPanel = new JPanel();
					{	BoxLayout layout = new BoxLayout(roundsPanel,BoxLayout.PAGE_AXIS); 
						roundsPanel.setLayout(layout);
					}
					roundsPanel.setBackground(new Color(255,255,255,200));
					dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DESCRIPTION_PANEL_WIDTH),SwingTools.getSize(SwingTools.GAME_DESCRIPTION_PANEL_HEIGHT));
					roundsPanel.setPreferredSize(dim);
					roundsPanel.setMinimumSize(dim);
					roundsPanel.setMaximumSize(dim);
					//
					roundsPanel.add(Box.createVerticalGlue());
					// title
					{	JLabel titleLabel = new JLabel(" ");
						Font font = getConfiguration().getFont().deriveFont((float)SwingTools.getSize(SwingTools.GAME_RESULTS_HEADER_FONT_SIZE));
						titleLabel.setFont(font);
						String text = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_ROUNDS);
						titleLabel.setText(text);
						String tooltip = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_ROUNDS+"Tooltip");
						titleLabel.setToolTipText(tooltip);
						titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
						titleLabel.setForeground(Color.WHITE);
						titleLabel.setBackground(new Color(0,0,0,128));
						titleLabel.setOpaque(true);
						dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DESCRIPTION_LABEL_LINE_WIDTH),SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_HEADER_HEIGHT));
						titleLabel.setPreferredSize(dim);
						titleLabel.setMinimumSize(dim);
						titleLabel.setMaximumSize(dim);
						titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
						roundsPanel.add(titleLabel);
					}
					//
					roundsPanel.add(Box.createVerticalGlue());
					//
					{	JTextPane textPane = new JTextPane();
						textPane.setEditable(false);
						textPane.setHighlighter(null);
//						textPane.setSelectedTextColor(null);
//						textPane.setEnabled(false);
//						textPane.setDisabledTextColor(Color.BLACK);
/*
						textPane.setCaret(new Caret()
				        {	public void install(JTextComponent c){}
				            public void deinstall(JTextComponent c){}
				            public void paint(Graphics g){}
				            public void addChangeListener(ChangeListener l){}
				            public void removeChangeListener(ChangeListener l){}
				            public boolean isVisible(){return false;}
				            public void setVisible(boolean v){}
				            public boolean isSelectionVisible(){return false;}
				            public void setSelectionVisible(boolean v){}
				            public void setMagicCaretPosition(Point p){}
				            public Point getMagicCaretPosition(){return new Point(0,0);}
				            public void setBlinkRate(int rate){}
				            public int getBlinkRate(){return 10000;}
				            public int getDot(){return 0;}
				            public int getMark(){return 0;}
				            public void setDot(int dot){}
				            public void moveDot(int dot){}
				        });
*/				        
						SimpleAttributeSet sa = new SimpleAttributeSet();
						StyleConstants.setAlignment(sa,StyleConstants.ALIGN_JUSTIFIED);
						Font font = getConfiguration().getFont().deriveFont((float)SwingTools.getSize(SwingTools.GAME_DESCRIPTION_LABEL_TEXT_FONT_SIZE));
						StyleConstants.setFontFamily(sa, font.getFamily());
						StyleConstants.setFontSize(sa, font.getSize());
						StyledDocument doc = textPane.getStyledDocument();
						String text = "Here goes a short description of the different rounds in this match. Blablabla blabla blabla blabla blabla blabla. Blablabla blabla blabla blabla blabla blabla. Blablabla blabla blabla blabla blabla blabla. Blablabla blabla blabla blabla blabla blabla. Blablabla blabla blabla blabla blabla blabla. Blablabla blabla blabla blabla blabla blabla. Blablabla blabla blabla blabla blabla blabla. Blablabla blabla blabla blabla blabla blabla.";
						try
						{	doc.insertString(0,text,sa);
						}
						catch (BadLocationException e)
						{	e.printStackTrace();
						}
						doc.setParagraphAttributes(0,doc.getLength()-1,sa,true);
						textPane.setBackground(new Color(0,0,0,20));
						dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DESCRIPTION_LABEL_LINE_WIDTH),SwingTools.getSize(SwingTools.GAME_DESCRIPTION_LABEL_TEXT_HEIGHT));
						textPane.setPreferredSize(dim);
						textPane.setMinimumSize(dim);
						textPane.setMaximumSize(dim);
						textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
						roundsPanel.add(textPane);
					}
					//
					roundsPanel.add(Box.createVerticalGlue());
					textPanel.add(roundsPanel);
				}
				//
				textPanel.add(Box.createVerticalGlue());
				// points panel
				{	JPanel pointsPanel = new JPanel();
					{	BoxLayout layout = new BoxLayout(pointsPanel,BoxLayout.PAGE_AXIS); 
						pointsPanel.setLayout(layout);
					}
					pointsPanel.setBackground(new Color(255,255,255,200));
					dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DESCRIPTION_PANEL_WIDTH),SwingTools.getSize(SwingTools.GAME_DESCRIPTION_PANEL_HEIGHT));
					pointsPanel.setPreferredSize(dim);
					pointsPanel.setMinimumSize(dim);
					pointsPanel.setMaximumSize(dim);
					//
					pointsPanel.add(Box.createVerticalGlue());
					// title
					{	JLabel titleLabel = new JLabel(" ");
						Font font = getConfiguration().getFont().deriveFont((float)SwingTools.getSize(SwingTools.GAME_RESULTS_HEADER_FONT_SIZE));
						titleLabel.setFont(font);
						String text = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_POINTSPROCESS);
						titleLabel.setText(text);
						String tooltip = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_POINTSPROCESS+"Tooltip");
						titleLabel.setToolTipText(tooltip);
						titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
						titleLabel.setForeground(Color.WHITE);
						titleLabel.setBackground(new Color(0,0,0,128));
						titleLabel.setOpaque(true);
						dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DESCRIPTION_LABEL_LINE_WIDTH),SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_HEADER_HEIGHT));
						titleLabel.setPreferredSize(dim);
						titleLabel.setMinimumSize(dim);
						titleLabel.setMaximumSize(dim);
						titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
						pointsPanel.add(titleLabel);
					}
					//
					pointsPanel.add(Box.createVerticalGlue());
					//
					{	JLabel textLabel = new JLabel(" ");
						String text = "sdfgsgdsfgsdfgsdfgsdfgdfgsdfgsdfgsdfgsdfgsdfgsdfgs";
						textLabel.setText(text);
						textLabel.setHorizontalAlignment(SwingConstants.LEFT);
						textLabel.setBackground(new Color(0,0,0,20));
						textLabel.setOpaque(true);
						dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DESCRIPTION_LABEL_LINE_WIDTH),SwingTools.getSize(SwingTools.GAME_DESCRIPTION_LABEL_TEXT_HEIGHT));
						textLabel.setPreferredSize(dim);
						textLabel.setMinimumSize(dim);
						textLabel.setMaximumSize(dim);
						textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
						pointsPanel.add(textLabel);
					}
					//
					pointsPanel.add(Box.createVerticalGlue());
					textPanel.add(pointsPanel);
				}
				//
				textPanel.add(Box.createVerticalGlue());
				// limit panel
				{	JPanel limitPanel = new JPanel();
					{	BoxLayout layout = new BoxLayout(limitPanel,BoxLayout.PAGE_AXIS); 
						limitPanel.setLayout(layout);
					}
					limitPanel.setBackground(new Color(255,255,255,200));
					dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DESCRIPTION_PANEL_WIDTH),SwingTools.getSize(SwingTools.GAME_DESCRIPTION_PANEL_HEIGHT));
					limitPanel.setPreferredSize(dim);
					limitPanel.setMinimumSize(dim);
					limitPanel.setMaximumSize(dim);
					//
					limitPanel.add(Box.createVerticalGlue());
					// title
					{	JLabel titleLabel = new JLabel(" ");
						Font font = getConfiguration().getFont().deriveFont((float)SwingTools.getSize(SwingTools.GAME_RESULTS_HEADER_FONT_SIZE));
						titleLabel.setFont(font);
						String text = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_LIMITS);
						titleLabel.setText(text);
						String tooltip = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_LIMITS+"Tooltip");
						titleLabel.setToolTipText(tooltip);
						titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
						titleLabel.setForeground(Color.WHITE);
						titleLabel.setBackground(new Color(0,0,0,128));
						titleLabel.setOpaque(true);
						dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DESCRIPTION_LABEL_LINE_WIDTH),SwingTools.getSize(SwingTools.GAME_RESULTS_LABEL_HEADER_HEIGHT));
						titleLabel.setPreferredSize(dim);
						titleLabel.setMinimumSize(dim);
						titleLabel.setMaximumSize(dim);
						titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
						limitPanel.add(titleLabel);
					}
					//
					limitPanel.add(Box.createVerticalGlue());
					//
					{	JLabel textLabel = new JLabel(" ");
						String text = "sdfgsgdsfgsdfgsdfgsdfgdfgsdfgsdfgsdfgsdfgsdfgsdfgs";
						textLabel.setText(text);
						textLabel.setHorizontalAlignment(SwingConstants.LEFT);
						textLabel.setBackground(new Color(0,0,0,20));
						textLabel.setOpaque(true);
						dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DESCRIPTION_LABEL_LINE_WIDTH),SwingTools.getSize(SwingTools.GAME_DESCRIPTION_LABEL_TEXT_HEIGHT));
						textLabel.setPreferredSize(dim);
						textLabel.setMinimumSize(dim);
						textLabel.setMaximumSize(dim);
						textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
						limitPanel.add(textLabel);
					}
					//
					limitPanel.add(Box.createVerticalGlue());
					textPanel.add(limitPanel);
				}
				//
				infoPanel.add(textPanel);
			}
			//
			add(infoPanel);
		}

		add(Box.createVerticalGlue());
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
