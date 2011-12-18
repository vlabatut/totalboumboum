package fr.free.totalboumboum.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.gui.generic.ButtonAware;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public class SwingTools
{	// menus
	public static final String MENU_HORIZONTAL_BUTTON_HEIGHT = "MENU_HORIZONTAL_BUTTON_HEIGHT";
	public static final String MENU_VERTICAL_BUTTON_HEIGHT = "MENU_VERTICAL_BUTTON_HEIGHT";
	public static final String MENU_ALL_BUTTON_FONT_SIZE = "MENU_ALL_BUTTON_FONT_SIZE";
	public static final String MENU_HORIZONTAL_BUTTON_WIDTH = "MENU_HORIZONTAL_BUTTON_WIDTH";
	public static final String MENU_VERTICAL_PRIMARY_BUTTON_WIDTH = "MENU_VERTICAL_PRIMARY_BUTTON_WIDTH";
	public static final String MENU_VERTICAL_SECONDARY_BUTTON_WIDTH = "MENU_VERTICAL_SECONDARY_BUTTON_WIDTH";
	public static final String MENU_VERTICAL_BUTTON_SPACE = "MENU_VERTICAL_BUTTON_SPACE";
	public static final String MENU_HORIZONTAL_BUTTON_SPACE = "MENU_HORIZONTAL_BUTTON_SPACE";
	// splits
	public static final String HORIZONTAL_SPLIT_MENU_PANEL_HEIGHT = "HORIZONTAL_SPLIT_MENU_PANEL_HEIGHT";
	public static final String HORIZONTAL_SPLIT_MENU_PANEL_WIDTH = "HORIZONTAL_SPLIT_MENU_PANEL_WIDTH";
	public static final String HORIZONTAL_SPLIT_DATA_PANEL_HEIGHT = "HORIZONTAL_SPLIT_DATA_PANEL_HEIGHT";
	public static final String HORIZONTAL_SPLIT_DATA_PANEL_WIDTH = "HORIZONTAL_SPLIT_DATA_PANEL_WIDTH";
	public static final String VERTICAL_SPLIT_MENU_PANEL_HEIGHT = "VERTICAL_SPLIT_MENU_PANEL_HEIGHT";
	public static final String VERTICAL_SPLIT_MENU_PANEL_WIDTH = "VERTICAL_SPLIT_MENU_PANEL_WIDTH";
	public static final String VERTICAL_SPLIT_DATA_PANEL_HEIGHT = "VERTICAL_SPLIT_DATA_PANEL_HEIGHT";
	public static final String VERTICAL_SPLIT_DATA_PANEL_WIDTH = "VERTICAL_SPLIT_DATA_PANEL_WIDTH";
	// game
	public static final String GAME_DATA_PANEL_HEIGHT = "GAME_DATA_PANEL_HEIGHT";
	public static final String GAME_DATA_PANEL_WIDTH = "GAME_DATA_PANEL_WIDTH";
	public static final String GAME_DATA_LABEL_TITLE_HEIGHT = "GAME_DATA_LABEL_TITLE_HEIGHT";
	public static final String GAME_DATA_MARGIN_SIZE = "GAME_DATA_MARGIN_SIZE";
	public static final String GAME_TITLE_FONT_SIZE = "GAME_TITLE_FONT_SIZE";
	public static final String GAME_PROGRESSBAR_FONT_SIZE = "GAME_PROGRESSBAR_FONT_SIZE";
	public static final String GAME_RESULTS_LABEL_LINE_HEIGHT = "GAME_RESULTS_LABEL_LINE_HEIGHT";
	public static final String GAME_RESULTS_LABEL_HEADER_HEIGHT = "GAME_RESULTS_LABEL_HEADER_HEIGHT";
	public static final String GAME_RESULTS_LINE_FONT_SIZE = "GAME_RESULTS_LINE_FONT_SIZE";
	public static final String GAME_RESULTS_HEADER_FONT_SIZE = "GAME_RESULTS_HEADER_FONT_SIZE";
	public static final String GAME_RESULTS_MARGIN_SIZE = "GAME_RESULTS_MARGIN_SIZE";
	public static final String GAME_RESULTS_LABEL_NAME_WIDTH = "GAME_RESULTS_LABEL_NAME_WIDTH";
		
	// sizes
	private static final HashMap<String,Integer> sizes = new HashMap<String,Integer>();
	
	// icons
	public static final String ICON_NORMAL = "_normal";
	public static final String ICON_NORMAL_SELECTED = "_normal_selected";
	public static final String ICON_DISABLED = "_disabled";
	public static final String ICON_DISABLED_SELECTED = "_disabled_selected";
	public static final String ICON_ROLLOVER = "_rollover";
	public static final String ICON_ROLLOVER_SELECTED = "_rollover_selected";
	public static final String ICON_PRESSED = "_pressed";
	private static final HashMap<String,BufferedImage> icons = new HashMap<String,BufferedImage>();
	

	public static void init(Configuration configuration, Graphics g)
	{	// init
		Dimension panelDimension = configuration.getPanelDimension();
		int width = panelDimension.width;
		int height = panelDimension.height;
		
		// buttons
		int verticalMenuButtonHeight = (int)(height*0.05);
		sizes.put(MENU_VERTICAL_BUTTON_HEIGHT,verticalMenuButtonHeight);
		int horizontalMenuButtonHeight = (int)(height*0.07);
		sizes.put(MENU_HORIZONTAL_BUTTON_HEIGHT,horizontalMenuButtonHeight);
		int horizontalMenuButtonWidth = horizontalMenuButtonHeight;
		sizes.put(MENU_HORIZONTAL_BUTTON_WIDTH,horizontalMenuButtonWidth);
		sizes.put(MENU_HORIZONTAL_BUTTON_SPACE,(int)(width*0.025));
		int gameProgressbarFontSize = getFontSize(horizontalMenuButtonHeight*0.6, configuration, g); 
		sizes.put(GAME_PROGRESSBAR_FONT_SIZE,gameProgressbarFontSize);
		sizes.put(MENU_VERTICAL_PRIMARY_BUTTON_WIDTH,(int)(width*0.33));
		int secondaryVerticalMenuButtonWIdth = (int)(width*0.25);
		sizes.put(MENU_VERTICAL_SECONDARY_BUTTON_WIDTH,secondaryVerticalMenuButtonWIdth);
		sizes.put(MENU_VERTICAL_BUTTON_SPACE,(int)(height*0.025));
		
		// menu panels
		int horizontalSplitMenuPanelHeight = horizontalMenuButtonHeight; 
		sizes.put(HORIZONTAL_SPLIT_MENU_PANEL_HEIGHT,horizontalSplitMenuPanelHeight);
		sizes.put(HORIZONTAL_SPLIT_MENU_PANEL_WIDTH,width);
		int horizontalSplitDataPanelHeight = height-horizontalSplitMenuPanelHeight; 
		sizes.put(HORIZONTAL_SPLIT_DATA_PANEL_HEIGHT,horizontalSplitDataPanelHeight);
		int horizontalSplitDataPanelWidth = width; 
		sizes.put(HORIZONTAL_SPLIT_DATA_PANEL_WIDTH,horizontalSplitDataPanelHeight);
		sizes.put(VERTICAL_SPLIT_MENU_PANEL_HEIGHT,height);
		int verticalSplitMenuPanelWidth = secondaryVerticalMenuButtonWIdth;
		sizes.put(VERTICAL_SPLIT_MENU_PANEL_WIDTH,verticalSplitMenuPanelWidth);
		sizes.put(VERTICAL_SPLIT_DATA_PANEL_HEIGHT,height);
		sizes.put(VERTICAL_SPLIT_DATA_PANEL_WIDTH,width-verticalSplitMenuPanelWidth);
		// font
		int menuButtonFontSize = getFontSize(verticalMenuButtonHeight*0.9,configuration,g);
		sizes.put(MENU_ALL_BUTTON_FONT_SIZE, menuButtonFontSize);
		
		// font
		int gameTitleFontSize = (int)(menuButtonFontSize*1.3);
		sizes.put(GAME_TITLE_FONT_SIZE, gameTitleFontSize);
		// labels
		int gameLabelTitleHeight;
		{	Font font = configuration.getFont().deriveFont((float)gameTitleFontSize);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			gameLabelTitleHeight = (int)(metrics.getHeight()*1.2);
		}
		sizes.put(GAME_DATA_LABEL_TITLE_HEIGHT, gameLabelTitleHeight);
		// game panel
		int gameDataMarginSize = (int)(width*0.025);
		sizes.put(GAME_DATA_MARGIN_SIZE,gameDataMarginSize);
		int gameDataPanelHeight = horizontalSplitDataPanelHeight-3*gameDataMarginSize-gameLabelTitleHeight;
		sizes.put(GAME_DATA_PANEL_HEIGHT,gameDataPanelHeight);
		int gameDataPanelWidth = horizontalSplitDataPanelWidth-2*gameDataMarginSize;
		sizes.put(GAME_DATA_PANEL_WIDTH,gameDataPanelWidth);
		int gameResultsMarginSize = (int)(height*0.005);
		sizes.put(GAME_RESULTS_MARGIN_SIZE,gameResultsMarginSize);
		int gameResultsLabelLineHeight = (gameDataPanelHeight-16*gameResultsMarginSize)/17;
		sizes.put(GAME_RESULTS_LABEL_LINE_HEIGHT,gameResultsLabelLineHeight);
		int gameResultLineFontSize = getFontSize(gameResultsLabelLineHeight*0.9, configuration, g);
		sizes.put(GAME_RESULTS_LINE_FONT_SIZE,gameResultLineFontSize);
		int gameResultLabelNameWidth = (int)(gameDataPanelWidth/3);
		sizes.put(GAME_RESULTS_LABEL_NAME_WIDTH,gameResultLabelNameWidth);
		int gameResultsLabelHeaderHeight = gameDataPanelHeight-16*gameResultsMarginSize-16*gameResultsLabelLineHeight;
		sizes.put(GAME_RESULTS_LABEL_HEADER_HEIGHT,gameResultsLabelHeaderHeight);
		int gameResultHeaderFontSize = getFontSize(gameResultsLabelHeaderHeight*0.9, configuration, g);
		sizes.put(GAME_RESULTS_HEADER_FONT_SIZE,gameResultHeaderFontSize);
		
		
		// icons
		BufferedImage absent = ImageTools.getAbsentImage(64,64);
		BufferedImage image;
		String[] buttonStates = {ICON_NORMAL,ICON_NORMAL_SELECTED,
				ICON_DISABLED,ICON_DISABLED_SELECTED,
				ICON_ROLLOVER,ICON_ROLLOVER_SELECTED,
				ICON_PRESSED};
		
		String folder = FileTools.getIconsPath()+File.separator;
		{	String name = "description";
			for(int i=0;i<buttonStates.length;i++)
			{	image = loadIcon(folder+name+buttonStates[i]+".png",absent);
				icons.put(GuiTools.TOURNAMENT_BUTTON_DESCRIPTION+buttonStates[i],image);
				icons.put(GuiTools.MATCH_BUTTON_DESCRIPTION+buttonStates[i],image);
				icons.put(GuiTools.ROUND_BUTTON_DESCRIPTION+buttonStates[i],image);
			}
		}
		{	String name = "left_blue";
			for(int i=0;i<buttonStates.length;i++)
			{	image = loadIcon(folder+name+buttonStates[i]+".png",absent);
				icons.put(GuiTools.TOURNAMENT_BUTTON_MENU+buttonStates[i],image);
				icons.put(GuiTools.MATCH_BUTTON_CURRENT_TOURNAMENT+buttonStates[i],image);
				icons.put(GuiTools.ROUND_BUTTON_CURRENT_MATCH+buttonStates[i],image);
			}
		}
		{	String name = "left_red";
			for(int i=0;i<buttonStates.length;i++)
			{	image = loadIcon(folder+name+buttonStates[i]+".png",absent);
				icons.put(GuiTools.TOURNAMENT_BUTTON_FINISH+buttonStates[i],image);
				icons.put(GuiTools.MATCH_BUTTON_FINISH+buttonStates[i],image);
				icons.put(GuiTools.ROUND_BUTTON_FINISH+buttonStates[i],image);
			}
		}
		{	String name = "play";
			for(int i=0;i<buttonStates.length;i++)
			{	image = loadIcon(folder+name+buttonStates[i]+".png",absent);
				icons.put(GuiTools.ROUND_BUTTON_PLAY+buttonStates[i],image);
			}
		}
		{	String name = "home";
			for(int i=0;i<buttonStates.length;i++)
			{	image = loadIcon(folder+name+buttonStates[i]+".png",absent);
				icons.put(GuiTools.TOURNAMENT_BUTTON_QUIT+buttonStates[i],image);
				icons.put(GuiTools.MATCH_BUTTON_QUIT+buttonStates[i],image);
				icons.put(GuiTools.ROUND_BUTTON_QUIT+buttonStates[i],image);
			}
		}
		{	String name = "results";
			for(int i=0;i<buttonStates.length;i++)
			{	image = loadIcon(folder+name+buttonStates[i]+".png",absent);
				icons.put(GuiTools.TOURNAMENT_BUTTON_RESULTS+buttonStates[i],image);
				icons.put(GuiTools.MATCH_BUTTON_RESULTS+buttonStates[i],image);
				icons.put(GuiTools.ROUND_BUTTON_RESULTS+buttonStates[i],image);
			}
		}
		{	String name = "right_blue";
			for(int i=0;i<buttonStates.length;i++)
			{	image = loadIcon(folder+name+buttonStates[i]+".png",absent);
			}
		}
		{	String name = "right_red";
			for(int i=0;i<buttonStates.length;i++)
			{	image = loadIcon(folder+name+buttonStates[i]+".png",absent);
				icons.put(GuiTools.TOURNAMENT_BUTTON_CURRENT_MATCH+buttonStates[i],image);
				icons.put(GuiTools.TOURNAMENT_BUTTON_NEXT_MATCH+buttonStates[i],image);
				icons.put(GuiTools.MATCH_BUTTON_CURRENT_ROUND+buttonStates[i],image);
				icons.put(GuiTools.MATCH_BUTTON_NEXT_ROUND+buttonStates[i],image);
			}
		}
		{	String name = "stats";
			for(int i=0;i<buttonStates.length;i++)
			{	image = loadIcon(folder+name+buttonStates[i]+".png",absent);
				icons.put(GuiTools.TOURNAMENT_BUTTON_STATISTICS+buttonStates[i],image);
				icons.put(GuiTools.MATCH_BUTTON_STATISTICS+buttonStates[i],image);
				icons.put(GuiTools.ROUND_BUTTON_STATISTICS+buttonStates[i],image);
			}
		}		
		
	}
	public static int getSize(String key)
	{	int result = -1;
		Integer temp = sizes.get(key);
		if(temp!=null)
			result = temp.intValue();
		return result;
	}
	public static BufferedImage getIcon(String key)
	{	BufferedImage result;
		BufferedImage temp = icons.get(key);
		if(temp==null)
			result = ImageTools.getAbsentImage(64,64);
		else
			result = temp;
		return result;
	}
	public static int getFontSize(double limit, Configuration configuration, Graphics g)
	{	int result = 0;
		int fheight;
		do
		{	result++;
			Font font = configuration.getFont().deriveFont((float)result);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			fheight = metrics.getHeight();
		}
		while(fheight<limit);
		return result;
	}
	public static BufferedImage loadIcon(String path, BufferedImage absent)
	{	BufferedImage image;
		try
		{	image = ImageTools.loadImage(path,null);
		}
		catch (IOException e)
		{	image = absent;
		}
		return image;	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	public static void setButtonContent(String name, AbstractButton button, Configuration configuration)
	{	// content
		if(icons.containsKey(name+ICON_NORMAL))
		{	// normal icon
			{	BufferedImage icon = getIcon(name+ICON_NORMAL);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*0.9,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setIcon(ii);
			}
			// disabled icon
			{	BufferedImage icon = getIcon(name+ICON_DISABLED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*0.9,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setDisabledIcon(ii);
			}
			// pressed icon
			{	BufferedImage icon = getIcon(name+ICON_PRESSED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*0.9,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setPressedIcon(ii);
			}
			// selected icon
			{	BufferedImage icon = getIcon(name+ICON_NORMAL_SELECTED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*0.9,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setSelectedIcon(ii);
			}
			// disabled selected icon
			{	BufferedImage icon = getIcon(name+ICON_DISABLED_SELECTED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*0.9,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setDisabledSelectedIcon(ii);
			}
			// rollover icon
			{	BufferedImage icon = getIcon(name+ICON_ROLLOVER);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*0.9,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setRolloverEnabled(true);
				button.setRolloverIcon(ii);
			}
			// rollover selected icon
			{	BufferedImage icon = getIcon(name+ICON_ROLLOVER_SELECTED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*0.9,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setRolloverSelectedIcon(ii);
			}
			// 
			button.setBorderPainted(false);
			button.setBorder(null);
			button.setMargin(null);
//			button.setBackground(new Color(0,0,0,0));
	        button.setContentAreaFilled(false);
			
		}
		else
		{	// text 
			String text = configuration.getLanguage().getText(name);
			Font font = configuration.getFont().deriveFont((float)sizes.get(MENU_ALL_BUTTON_FONT_SIZE));
			button.setFont(font);
			button.setText(text);
		}		
		// action command
		button.setActionCommand(name);
		// tooltip
		String toolTip = name+"Tooltip";
		String text = configuration.getLanguage().getText(toolTip);
		button.setToolTipText(text);
	}		
	public static void initButton(AbstractButton result,String name, int width, int height, ButtonAware panel, Configuration configuration)
	{	// dimension
		Dimension dim = new Dimension(width,height);
		result.setMinimumSize(dim);
		result.setMaximumSize(dim);
		result.setPreferredSize(dim);
		// set text
		setButtonContent(name,result,configuration);
		// add to panel
		panel.add(result);
		result.addActionListener(panel);
	}
	public static JButton createPrincipalVerticalMenuButton(String name, ButtonAware panel, Configuration configuration)
	{	int width = sizes.get(MENU_VERTICAL_PRIMARY_BUTTON_WIDTH);
		int height = sizes.get(MENU_VERTICAL_BUTTON_HEIGHT);
		JButton result = new JButton();
		initButton(result,name,width,height,panel,configuration);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		return result;
	}
	public static JButton createSecondaryVerticalMenuButton(String name, ButtonAware panel, Configuration configuration)
	{	int width = sizes.get(MENU_VERTICAL_SECONDARY_BUTTON_WIDTH);
		int height = sizes.get(MENU_VERTICAL_BUTTON_HEIGHT);
		JButton result = new JButton();
		initButton(result,name,width,height,panel,configuration);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		return result;
	}	
	public static JButton createHorizontalMenuButton(String name, ButtonAware panel, Configuration configuration)
	{	int width = sizes.get(MENU_HORIZONTAL_BUTTON_WIDTH);
		int height = sizes.get(MENU_HORIZONTAL_BUTTON_HEIGHT);
		JButton result = new JButton();
		initButton(result,name,width,height,panel,configuration);
		result.setAlignmentY(Component.CENTER_ALIGNMENT);
		return result;
	}
	public static JToggleButton createHorizontalMenuToggleButton(String name, ButtonAware panel, Configuration configuration)
	{	int width = sizes.get(MENU_HORIZONTAL_BUTTON_WIDTH);
		int height = sizes.get(MENU_HORIZONTAL_BUTTON_HEIGHT);
		JToggleButton result = new JToggleButton();
		initButton(result,name,width,height,panel,configuration);
		result.setAlignmentY(Component.CENTER_ALIGNMENT);
		return result;
	}
}