package org.totalboumboum.zraphics;

/*
 * ShapePanel.java
 *
 * Created on June 11, 2005, 10:16 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 * 
 * http://www.walkersoftware.net/articles/text-effects-in-java/
 */

import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.Random;
/**
 *
 * @author Adam Walker <adam@walkersoftware.net>
 */
@SuppressWarnings("javadoc")
public class TextDemoPanel extends JComponent{
	private static final long serialVersionUID = 1L;
	private Shape shape = null;
    private Shape outline = null;
    private String text = "Walker Software";
    private boolean inverted = false;
    private boolean jiggle = true;
    private boolean drawShadow = true;
    private boolean drawBevel = true;
    private float fatten = 1.3f;
    private Color textColor = new Color(175,175,180);
    private int textSize = 72;//18;
    private boolean glassBG = true;
    private boolean glassText = true;
    private long start = 0;
    private long end = 0;
    private String msg = "";
//    private boolean ringed = true;
    /** Creates a new instance of ShapePanel */
    public TextDemoPanel() {
        setBackground(Color.RED);
        for(int i=0;i<flags.length;i++){
            flags[i] = false;
        }
        flags[0] = true;
        addKeyListener(new KA());
        addMouseListener(new MA());
        
    }
    public class MA extends MouseAdapter{
        @Override
		public void mouseClicked(MouseEvent e) {
            requestFocus();
        }
    }
    public class KA extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent evt){
            char c = evt.getKeyChar();
            int num = c-'0';
            if(num>=0 && num<=9){
                flags[num] = !flags[num];
                msg = "num "+num+" set to "+flags[num];
                repaint();
            }
        }
    }
    public void setText(String s){
        shape = null;
        outline = null;
        text = s;
        repaint();
    }
    boolean flags[] = new boolean[10];
    
    @Override
    public void paintComponent(Graphics go){
        //System.out.println("Go");
        Graphics2D g = (Graphics2D)go;
        RenderingHints hints = new RenderingHints(null);
        int n=0;
        if(flags[n++])
        hints.put(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        if(flags[n++])
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if(flags[n++])
        hints.put(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        if(flags[n++])
        hints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        if(flags[n++])
        hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        if(flags[n++])
        hints.put(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        if(flags[n++])
        hints.put(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_NORMALIZE);
        if(flags[n++])
        hints.put(RenderingHints.KEY_DITHERING,
                RenderingHints.VALUE_DITHER_ENABLE);
        g.setRenderingHints(hints);
        if(shape==null){
            start = System.currentTimeMillis();
            Font font = getFont();
            font = font.deriveFont(Font.BOLD, textSize);
            //new Font(font.getFontName(), Font.BOLD, textSize);
            Stroke bs = new BasicStroke(fatten);
            //g.setStroke(bs);
            GlyphVector gv = font.createGlyphVector(g.getFontRenderContext(),
                    text);
            // 1 degrees = 0.0174532925 radians
            int maxTilt = 20;
            GeneralPath path = new GeneralPath();
            Random random = new Random();
            
            if(jiggle){
                for(int i=0;i<gv.getNumGlyphs();i++){
                    double r = (random.nextInt(2*maxTilt)-maxTilt)*0.0174532925;
                    AffineTransform trans = gv.getGlyphTransform(i);
                    Shape sh = gv.getGlyphOutline(i);
                    Rectangle shr = sh.getBounds();
                    double cx = shr.getCenterX();
                    double cy = shr.getCenterY();
                    // jdk1.5 return null???
                    if(trans==null)
                        trans = new AffineTransform();
                    trans.rotate(r,cx,cy);
                    ////gv.setGlyphTransform(i, trans);
                    ////trans = gv.getGlyphTransform(i);
                    path.transform(trans);
                    path.append(sh,false);
                    try{
                        path.transform(trans.createInverse());
                    }catch(Exception e){System.out.println("foo");}
                }
            }else{
                path.append(gv.getOutline(),false);
            }
            //gv.set
            
            if(fatten==0){
                shape = path;
            }else{
                // What we need is something like photoshop's expand selection.
                
                // The following is very slow...
                Area area = new Area(path);
                area.add(new Area(bs.createStrokedShape(path)));
                shape = area;
                
                // This creates a disconnected outline, which isn't right,
                // but much faster ;)
                /*GeneralPath p2 = new GeneralPath(path);
                p2.append(bs.createStrokedShape(path), false);
                shape = p2;*/
            }
            
            /*if(ringed){
                Area area = null;
                Area comp = new Area();;
                //if(shape instanceof Area)
                //    area = (Area)shape;
                //else
                    area = new Area(shape);
                Stroke empty = new BasicStroke(10);
                comp.add(new Area(shape));
                comp.add(new Area(empty.createStrokedShape(shape)));
                Stroke ring = new BasicStroke(3);
                area.add(new Area(ring.createStrokedShape(comp)));
                shape = area;
            }*/
            Stroke stroke = new BasicStroke(2);
            outline = stroke.createStrokedShape(shape);
            end = System.currentTimeMillis();
            msg = "Build time "+(end-start)+" ms";
        }
        Rectangle r = shape.getBounds();
        //g.clearRect(0,0,getWidth(),getHeight());
        g.setColor(getBackground());
        g.fillRect(0,0,getWidth(),getHeight());
        Rectangle bg = new Rectangle(0,0,getWidth(),getHeight());
        if(glassBG)
            paintGlassEffect(g, bg, getBackground());
        
        
		int x = (getWidth()-r.width)/2;
		int y = (getHeight()-r.height)/2;
		y -= r.y/2;
		g.setColor(Color.BLACK);
		g.translate(x,y);
		// Show bounding box
		//g.draw(r);
		paintText(g);
		g.translate(-x,-y);
		g.setColor(getTextColor());
		g.drawString(msg, 5, getHeight()-5);
        
    }
    
    public static void paintGlassEffect(Graphics2D g, Shape s, Color c){
        Paint oldPaint = g.getPaint();
        Rectangle r = s.getBounds();
        int h = r.height/5;
        Shape oldClip = g.getClip();
        GradientPaint bgp = new GradientPaint(r.x,r.y+h*3,c,
                r.x,r.y+h*5, c.darker());
        g.setClip(r.x,r.y+(h*3),r.width,r.height-(h*3));//h*2);
        g.setPaint(bgp);
        g.fill(s);
        bgp = new GradientPaint(r.x,r.y,c.brighter(),
                r.x,r.y+h*2, c);
        g.setClip(r.x,r.y,r.width,h*2);
        g.setPaint(bgp);
        g.fill(s);
        g.setPaint(oldPaint);
        g.setClip(oldClip);
        
    }
    
    public static void paintDropShadow(Graphics2D g, int ds, Shape s){
        g.translate(ds,ds);
        Rectangle2D r = s.getBounds2D();
        float x = (float)r.getX();
        float y = (float)r.getY();
//		float w = (float)r.getWidth();
        float h = (float)r.getHeight();
        Color gradientStart = new Color(0, 0, 0,120);
        Color gradientEnd = new Color(0, 0, 0, 40);
        GradientPaint gp = new GradientPaint(x,y,gradientStart,
                x,y+h, gradientEnd);
        Paint p = g.getPaint();
        g.setPaint(gp);
        g.fill(s);
        g.translate(-ds,-ds);
        g.setPaint(p);
    }
    public static void paintBevel(Graphics2D g, Shape shape, Shape outline,
            boolean inverted){
        int jitter = 1;
        Rectangle r = shape.getBounds();
        
        Paint dark = new GradientPaint(r.x, r.y, new Color(0,0,0,150),
                r.x, r.y+r.height, new Color(0,0,0,210));
        Paint light = new GradientPaint(r.x, r.y, new Color(255,255,255,230),
                r.x, r.y+r.height, new Color(255,255,255,180));
        Paint oldPaint = g.getPaint();
        // Dark section of bevel
        g.setPaint(inverted?light:dark);
        g.translate(jitter,jitter);
        g.setClip(shape);
        g.translate(-jitter,-jitter);
        g.fill(outline);
        // Light section of Bevel
        g.translate(-jitter,-jitter);
        g.setClip(shape);
        g.translate(jitter,jitter);
        g.setPaint(inverted?dark:light);
        g.fill(outline);
        g.setClip(null);
        g.setPaint(oldPaint);
    }
    public void paintText(Graphics2D g){
        //Drop shadow
        if(drawShadow)
            paintDropShadow(g, 3, shape);
        
        // Main Text
        g.setColor(textColor);
        g.fill(shape);
        if(glassText)
            paintGlassEffect(g, shape, textColor);
        
        if(drawBevel)
            paintBevel(g,shape,outline, inverted);
    }
    
    public boolean isInverted() {
        return inverted;
    }
    
    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }
    
    public boolean isJiggle() {
        return jiggle;
    }
    
    public void setJiggle(boolean jiggle) {
        this.jiggle = jiggle;
    }
    
    public boolean isDrawShadow() {
        return drawShadow;
    }
    
    public void setDrawShadow(boolean drawShadow) {
        this.drawShadow = drawShadow;
    }
    
    public boolean isDrawBevel() {
        return drawBevel;
    }
    
    public void setDrawBevel(boolean drawBevel) {
        this.drawBevel = drawBevel;
    }
    
    public float getFatten() {
        return fatten;
    }
    
    public void setFatten(float fatten) {
        this.fatten = fatten;
    }
    
    public Color getTextColor() {
        return textColor;
    }
    
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        repaint();
    }
    
    public int getTextSize() {
        return textSize;
    }
    
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
    
    public boolean isGlassBG() {
        return glassBG;
    }
    
    public void setGlassBG(boolean glassBG) {
        this.glassBG = glassBG;
    }
    
    public boolean isGlassText() {
        return glassText;
    }
    
    public void setGlassText(boolean glassText) {
        this.glassText = glassText;
    }
    
}
