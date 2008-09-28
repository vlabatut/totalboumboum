package fr.free.totalboumboum.gui.generic;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class EntitledSubPanel extends JPanel
{	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JComponent data;

	public EntitledSubPanel(int width, int height, GuiConfiguration configuration)
	{	
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);

		// size
		Dimension dim = new Dimension(width,height);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);

		// content
		int margin = GuiTools.getSize(GuiTools.GAME_RESULTS_MARGIN_SIZE);
		int contentWidth = width - 2*margin;
		int titleHeight = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT);
		int dataHeight = height - 3*margin - titleHeight;
		
		add(Box.createVerticalGlue());
		// title
		{	String txt = null;
			title = new JLabel(txt);
			Font font = configuration.getFont().deriveFont((float)GuiTools.getSize(GuiTools.GAME_RESULTS_HEADER_FONT_SIZE));
			title.setFont(font);
			String text = null;
			title.setText(text);
			String tooltip = null;
			title.setToolTipText(tooltip);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			title.setForeground(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
			title.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
			title.setOpaque(true);
			dim = new Dimension(contentWidth,titleHeight);
			title.setPreferredSize(dim);
			title.setMinimumSize(dim);
			title.setMaximumSize(dim);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			add(title);
		}
		//
		add(Box.createVerticalGlue());
		// data
		{	data = new JPanel();
			data.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
			dim = new Dimension(contentWidth,dataHeight);
			data.setPreferredSize(dim);
			data.setMinimumSize(dim);
			data.setMaximumSize(dim);
			add(data);
		}
		add(Box.createVerticalGlue());	
	}
	
	public void setTitle(String text, String tooltip)
	{	title.setText(text);
		title.setToolTipText(tooltip);
	}
	
	public void setDataPanel(JComponent panel)
	{	remove(data);
		data = panel;	
		add(data,3);
	}
	
	public JComponent getDataPanel()
	{	return data;	
	}
	
}
