package fr.free.totalboumboum.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.gui.generic.ButtonAware;

public class SwingTools
{	public static final String MENU_BUTTON_HEIGHT = "MENU_BUTTON_HEIGHT";
	public static final String HORIZONTAL_MENU_BUTTON_WIDTH = "HORIZONTAL_MENU_BUTTON_WIDTH";
	public static final String HORIZONTAL_MENU_BUTTON_SPACE = "HORIZONTAL_MENU_BUTTON_SPACE";
	public static final String PRINCIPAL_VERTICAL_MENU_BUTTON_WIDTH = "PRINCIPAL_VERTICAL_MENU_BUTTON_WIDTH";
	public static final String SECONDARY_VERTICAL_MENU_BUTTON_WIDTH = "SECONDARY_VERTICAL_MENU_BUTTON_WIDTH";
	public static final String VERTICAL_MENU_BUTTON_SPACE = "VERTICAL_MENU_BUTTON_SPACE";
	public static final String MENU_BUTTON_FONT_SIZE = "MENU_BUTTON_FONT_SIZE";
	
	private static final HashMap<String,Integer> sizes = new HashMap<String,Integer>();

	public static void init(Configuration configuration, Graphics g)
	{	// init
		Dimension panelDimension = configuration.getPanelDimension();
		int width = panelDimension.width;
		int height = panelDimension.height;
		
		// sizes
		int menuButtonHeight = (int)(height*0.05);
		sizes.put(MENU_BUTTON_HEIGHT,menuButtonHeight);
		sizes.put(HORIZONTAL_MENU_BUTTON_WIDTH,menuButtonHeight);
		sizes.put(HORIZONTAL_MENU_BUTTON_SPACE,(int)(width*0.025));
		sizes.put(PRINCIPAL_VERTICAL_MENU_BUTTON_WIDTH,(int)(width*0.33));
		sizes.put(SECONDARY_VERTICAL_MENU_BUTTON_WIDTH,(int)(width*0.25));
		sizes.put(VERTICAL_MENU_BUTTON_SPACE,(int)(height*0.025));
		
		int menuButtonFontSize = 0;
		int fheight;
		do
		{	menuButtonFontSize++;
			Font font = configuration.getFont().deriveFont((float)menuButtonFontSize);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			fheight = metrics.getHeight();
		}
		while(fheight<menuButtonHeight*0.9);
/*
double pixels;
do
{	menuButtonFontSize++;
	Font font = configuration.getFont().deriveFont(menuButtonFontSize);
	FontRenderContext frc = new FontRenderContext(null, false, false);
	pixels = font.getMaxCharBounds(frc).getHeight();
}
while(pixels<menuButtonHeight*0.9);
 */
		
		sizes.put(MENU_BUTTON_FONT_SIZE, menuButtonFontSize);
	}
	public static int getSize(String key)
	{	int result = -1;
		Integer temp = sizes.get(key);
		if(temp!=null)
			result = temp.intValue();
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void setButtonText(String name, AbstractButton button, Configuration configuration)
	{	// text shown
		String text = configuration.getLanguage().getText(name);
		Font font = configuration.getFont().deriveFont((float)sizes.get(MENU_BUTTON_FONT_SIZE));
		button.setFont(font);
		button.setText(text);
		button.setActionCommand(name);
		// tooltip
		String toolTip = name+"Tooltip";
		text = configuration.getLanguage().getText(toolTip);
		button.setToolTipText(text);
	}		
	public static void initButton(AbstractButton result,String name, int width, ButtonAware panel, Configuration configuration)
	{	// set text
		setButtonText(name,result,configuration);
		// dimension
		Dimension dim = new Dimension(width,sizes.get(MENU_BUTTON_HEIGHT));
		result.setMinimumSize(dim);
		result.setMaximumSize(dim);
		result.setPreferredSize(dim);
		// add to panel
		panel.add(result);
		result.addActionListener(panel);
	}
	public static JButton createPrincipalVerticalMenuButton(String name, ButtonAware panel, Configuration configuration)
	{	int width = sizes.get(PRINCIPAL_VERTICAL_MENU_BUTTON_WIDTH);
		JButton result = new JButton();
		initButton(result,name,width,panel,configuration);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		return result;
	}
	public static JButton createSecondaryVerticalMenuButton(String name, ButtonAware panel, Configuration configuration)
	{	int width = sizes.get(SECONDARY_VERTICAL_MENU_BUTTON_WIDTH);
		JButton result = new JButton();
		initButton(result,name,width,panel,configuration);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		return result;
	}	
	public static JButton createHorizontalMenuButton(String name, ButtonAware panel, Configuration configuration)
	{	int width = sizes.get(HORIZONTAL_MENU_BUTTON_WIDTH);
		JButton result = new JButton();
		initButton(result,name,width,panel,configuration);
		result.setAlignmentY(Component.CENTER_ALIGNMENT);
		return result;
	}
	public static JToggleButton createHorizontalMenuToggleButton(String name, ButtonAware panel, Configuration configuration)
	{	int width = sizes.get(HORIZONTAL_MENU_BUTTON_WIDTH);
		JToggleButton result = new JToggleButton();
		initButton(result,name,width,panel,configuration);
		result.setAlignmentY(Component.CENTER_ALIGNMENT);
		return result;
	}
}
