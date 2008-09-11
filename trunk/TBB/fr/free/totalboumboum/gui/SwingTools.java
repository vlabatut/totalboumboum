package fr.free.totalboumboum.gui;

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
	public static final String GAME_RESULTS_LABEL_LINE_HEIGHT = "GAME_RESULTS_LABEL_LINE_HEIGHT";
	public static final String GAME_RESULTS_LABEL_HEADER_HEIGHT = "GAME_RESULTS_LABEL_HEADER_HEIGHT";
	public static final String GAME_RESULTS_LINE_FONT_SIZE = "GAME_RESULTS_LINE_FONT_SIZE";
	public static final String GAME_RESULTS_HEADER_FONT_SIZE = "GAME_RESULTS_HEADER_FONT_SIZE";
	public static final String GAME_RESULTS_MARGIN_SIZE = "GAME_RESULTS_MARGIN_SIZE";
	public static final String GAME_RESULTS_LABEL_NAME_WIDTH = "GAME_RESULTS_LABEL_NAME_WIDTH";
		
	// sizes
	private static final HashMap<String,Integer> sizes = new HashMap<String,Integer>();
	
	// tournament/match/result
	public static final String GAME_ICON_INFO = "GAME_ICON_INFO";
	public static final String GAME_ICON_LEFT = "GAME_ICON_LEFT";
	public static final String GAME_ICON_PLAY = "GAME_ICON_PLAY";
	public static final String GAME_ICON_QUIT = "GAME_ICON_QUIT";
	public static final String GAME_ICON_RESULTS = "GAME_ICON_RESULTS";
	public static final String GAME_ICON_RIGHT = "GAME_ICON_RIGHT";
	public static final String GAME_ICON_STATS = "GAME_ICON_STATS";
	// icons
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
		String folder = FileTools.getIconsPath()+File.separator;
		{
			try
			{	image = ImageTools.loadImage(folder+"description.png",null);
			}
			catch (IOException e)
			{	image = absent;
			}
			icons.put(GuiTools.TOURNAMENT_BUTTON_DESCRIPTION,image);
			icons.put(GuiTools.MATCH_BUTTON_DESCRIPTION,image);
			icons.put(GuiTools.ROUND_BUTTON_DESCRIPTION,image);
		}
		{	try
			{	image = ImageTools.loadImage(folder+"left_blue.png",null);
			}
			catch (IOException e)
			{	image = absent;
			}
			icons.put(GuiTools.TOURNAMENT_BUTTON_MENU,image);
			icons.put(GuiTools.MATCH_BUTTON_CURRENT_TOURNAMENT,image);
			icons.put(GuiTools.ROUND_BUTTON_CURRENT_MATCH,image);
		}
		{	try
			{	image = ImageTools.loadImage(folder+"left_red.png",null);
			}
			catch (IOException e)
			{	image = absent;
			}
			icons.put(GuiTools.TOURNAMENT_BUTTON_FINISH,image);
			icons.put(GuiTools.MATCH_BUTTON_FINISH,image);
			icons.put(GuiTools.ROUND_BUTTON_FINISH,image);
		}
		{	try
			{	image = ImageTools.loadImage(folder+"play.png",null);
			}
			catch (IOException e)
			{	image = absent;
			}
			icons.put(GuiTools.ROUND_BUTTON_PLAY,image);
		}
		{	try
			{	image = ImageTools.loadImage(folder+"home.png",null);
			}
			catch (IOException e)
			{	image = absent;
			}
			icons.put(GuiTools.TOURNAMENT_BUTTON_QUIT,image);
			icons.put(GuiTools.MATCH_BUTTON_QUIT,image);
			icons.put(GuiTools.ROUND_BUTTON_QUIT,image);
		}
		{	try
			{	image = ImageTools.loadImage(folder+"results.png",null);
			}
			catch (IOException e)
			{	image = absent;
			}
			icons.put(GuiTools.TOURNAMENT_BUTTON_RESULTS,image);
			icons.put(GuiTools.MATCH_BUTTON_RESULTS,image);
			icons.put(GuiTools.ROUND_BUTTON_RESULTS,image);
		}
		{	try
			{	image = ImageTools.loadImage(folder+"right_blue.png",null);
			}
			catch (IOException e)
			{	image = absent;
			}
			try
			{	image = ImageTools.loadImage(folder+"right_red.png",null);
			}
			catch (IOException e)
			{	image = absent;
			}
			icons.put(GuiTools.TOURNAMENT_BUTTON_CURRENT_MATCH,image);
			icons.put(GuiTools.TOURNAMENT_BUTTON_NEXT_MATCH,image);
			icons.put(GuiTools.MATCH_BUTTON_CURRENT_ROUND,image);
			icons.put(GuiTools.MATCH_BUTTON_NEXT_ROUND,image);
		}
		{	try
			{	image = ImageTools.loadImage(folder+"stats.png",null);
			}
			catch (IOException e)
			{	image = absent;
			}
			icons.put(GuiTools.TOURNAMENT_BUTTON_STATISTICS,image);
			icons.put(GuiTools.MATCH_BUTTON_STATISTICS,image);
			icons.put(GuiTools.ROUND_BUTTON_STATISTICS,image);
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
	{	BufferedImage result;;
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void setButtonContent(String name, AbstractButton button, Configuration configuration)
	{	// content
		if(icons.containsKey(name))
		{	//icon
			BufferedImage icon = getIcon(name);
			double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
			icon = ImageTools.resize(icon,zoom*0.9,true);
			ImageIcon ii = new ImageIcon(icon);
			button.setIcon(ii);
//			button.setBorderPainted(false);
//			button.setBorder(null);
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
