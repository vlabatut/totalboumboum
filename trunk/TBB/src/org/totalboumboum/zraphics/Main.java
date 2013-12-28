package org.totalboumboum.zraphics;

/*
 * Main.java
 *
 * Created on June 11, 2005, 10:15 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 * 
 * http://www.walkersoftware.net/articles/text-effects-in-java/
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

/**
 *
 * @author adam
 */
@SuppressWarnings("javadoc")
public class Main {
	private JTextField text;
    private TextDemoPanel shapePanel;
    private JCheckBox bevel;
    private JCheckBox shadow;
    private JCheckBox inverted;
    private JCheckBox jiggle;
    private JSlider fatten;
    private JSlider textSize;
    @SuppressWarnings("rawtypes")
	private JComboBox fontName;
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }
    public void run(){
        JFrame frame = new JFrame("Text Effects Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel(new BorderLayout());
        p.add(shapePanel = new TextDemoPanel());
        shapePanel.setPreferredSize(new Dimension(500,200));
        p.add(makeHeader(), BorderLayout.NORTH);
        frame.getContentPane().add(p);
        frame.pack();
        //frame.setSize(500,250);
        frame.setVisible(true);        
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public JPanel makeHeader(){
        JPanel p = new JPanel(new BorderLayout());
        text = new JTextField("Walker Software");
        p.add(text);
        JButton btn = new JButton("Update");
        btn.addActionListener(new UpdateAction());
        p.add(btn,BorderLayout.EAST);
        JPanel jp = new JPanel();
        jp.add(shadow = new JCheckBox("Shadow", shapePanel.isDrawShadow()));
        jp.add(bevel = new JCheckBox("Bevel", shapePanel.isDrawBevel()));
        jp.add(inverted = new JCheckBox("Inverted", shapePanel.isInverted()));
        jp.add(jiggle = new JCheckBox("Jiggle", shapePanel.isJiggle()));
        shadow.addActionListener(new UpdateAction());
        jiggle.addActionListener(new UpdateAction());
        inverted.addActionListener(new UpdateAction());
        bevel.addActionListener(new UpdateAction());
        jp.add(new JLabel("Fatten"));
        jp.add(fatten = new JSlider(0, 30, (int)(shapePanel.getFatten()*10)));
        fatten.setMajorTickSpacing(10);
        fatten.setMinorTickSpacing(1);
        fatten.setPaintTicks(true);
        JPanel p2 = new JPanel(new GridLayout(2,1));
        p2.add(jp);
        jp = new JPanel();
        String fonts[] =
        GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontName = new JComboBox(fonts);
        fontName.setSelectedItem("Dialog");
        fontName.addActionListener(new UpdateAction());
        jp.add(fontName);
        JButton txtColor = new JButton("Text Color");
        txtColor.addActionListener(new TextColorAction());
        jp.add(txtColor);
        jp.add(new JLabel("text Size"));
        jp.add(textSize = new JSlider(8, 72, shapePanel.getTextSize()));
        textSize.setMinorTickSpacing(1);
        textSize.setMajorTickSpacing(10);
        textSize.setPaintTicks(true);
        textSize.setPaintLabels(true);
        JButton bgColor = new JButton("Background Color");
        bgColor.addActionListener(new BackgroundColorAction());
        jp.add(bgColor);
        p2.add(jp);
        p.add(p2, BorderLayout.SOUTH);
        return p;
    }
    public class UpdateAction implements ActionListener{
            @Override
			public void actionPerformed(ActionEvent evt){
                shapePanel.setDrawShadow(shadow.isSelected());
                shapePanel.setDrawBevel(bevel.isSelected());
                shapePanel.setInverted(inverted.isSelected());
                shapePanel.setJiggle(jiggle.isSelected());
                float f = fatten.getValue() / 10.0f;
                shapePanel.setFatten(f);
                shapePanel.setTextSize(textSize.getValue());
                shapePanel.setFont(new Font((String)fontName.getSelectedItem(), Font.PLAIN, 10));
                shapePanel.setText(text.getText());
            }
        
    }
    public class TextColorAction implements ActionListener{
            @Override
			public void actionPerformed(ActionEvent evt){
                Color c = JColorChooser.showDialog(shapePanel, "Text Color",
                        shapePanel.getTextColor());
                if(c!=null){
                    shapePanel.setTextColor(c);
                }
            }
        
    }
    public class BackgroundColorAction implements ActionListener{
            @Override
			public void actionPerformed(ActionEvent evt){
                Color c = JColorChooser.showDialog(shapePanel, "Backgound Color",
                        shapePanel.getBackground());
                if(c!=null){
                    shapePanel.setBackground(c);
                }
            }
        
    }
    
}
