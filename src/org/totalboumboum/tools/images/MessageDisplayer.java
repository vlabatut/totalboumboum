package org.totalboumboum.tools.images;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import org.totalboumboum.gui.tools.GuiFontTools;

/**
 * Generates the texts displayed right before the start of a round
 * (ready, set, etc.).
 * <br/>
 * Most of this class was adapted from a work by Adam Walker <adam@walkersoftware.net>
 * cf. http://www.walkersoftware.net/articles/text-effects-in-java/
 * 
 * @author Adam Walker
 * @author Vincent Labatut
 */
public class MessageDisplayer
{
	/**
	 * Builds a new image displayer.
	 * 
	 * @param font
	 * 		Font used to draw the text.
	 * @param xc
	 * 		X location of the center of the text.
	 * @param yc
	 * 		Y location of the center of the text.
	 */
	public MessageDisplayer(Font font, int xc, int yc)
	{	setRenderingHints();
		this.font = font;
		this.xc = xc;
		this.yc = yc;
	}
	
	/////////////////////////////////////////////////////////////////
	// LOCATION CENTER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** X location of the center */
	private int xc;
	/** Y location of the center of the text */
	private int yc;

	/////////////////////////////////////////////////////////////////
	// SHAPES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Shape corresponding to the rendered text */
	private Shape shape;
	/** Shape corresponding to the additional outline */
	private Shape outline;

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Color used to draw the text */
	private Color textColor = new Color(175,175,180);
	/** Font used to draw the text */
	private Font font;

	/**
	 * Processes the shapes for the specified text.
	 * The shape will be displayed the next time
	 * the {@link #paint(Graphics) paint} method is called.
	 *  
	 * @param text
	 * 		Text to display.
	 */
	public void updateText(String text)
	{	shape = null;
		outline = null;
		
		Graphics2D g = (Graphics2D)GuiFontTools.getGraphics();
		Stroke bs = new BasicStroke(fatten);
		GlyphVector gv = font.createGlyphVector(g.getFontRenderContext(),text);
		GeneralPath path = new GeneralPath();
		Random random = new Random();
		
		if(jiggle)
		{	for(int i=0;i<gv.getNumGlyphs();i++)
			{	// 1 degrees = 0.0174532925 radians
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
				path.transform(trans);
				path.append(sh,false);
				try
				{	path.transform(trans.createInverse());
				}
				catch(Exception e)
				{	System.out.println("foo");
				}
			}
		}
		else
		{	path.append(gv.getOutline(),false);
		}
		
		if(fatten==0)
		{	shape = path;
		}
		else
		{	// What we need is something like photoshop's expand selection.
			Area area = new Area(path);
			area.add(new Area(bs.createStrokedShape(path)));
			shape = area;
		}
		Stroke stroke = new BasicStroke(2);
		outline = stroke.createStrokedShape(shape);
	}
	
	/**
	 * Changes the text color.
	 * 
	 * @param textColor
	 * 		New color.
	 */
	public void setTextColor(Color textColor)
	{	this.textColor = textColor;
	}

	/**
	 * Draws the pre-processed text shape in the
	 * specified graphics object.
	 * 
	 * @param g
	 * 		Graphics object used for drawing.
	 */
	private void paintText(Graphics2D g)
	{	// shadow
		if(shadowed)
			paintShadow(g,3);		
		// text
		g.setColor(textColor);
		g.fill(shape);
		// glass effect
		if(glass)
			paintGlass(g,textColor);
		// bevel effect
		if(beveled)
			paintBevel(g,inverted);
	}
	
	/////////////////////////////////////////////////////////////////
	// JIGGLE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether letters should be jiggled (slightly rolled) */ 
	private boolean jiggle = true;
	/** Jiggling parameter */
	private int maxTilt = 30;
	
	/**
	 * Enables/disabled the jiggling effet.
	 * 
	 * @param jiggle
	 * 		{@code true} to jiggle the letters.
	 */
	public void setJiggle(boolean jiggle)
	{	this.jiggle = jiggle;
	}
	
	/////////////////////////////////////////////////////////////////
	// SHADOW			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether a shadow should be displayed */
	private boolean shadowed = true;

	/**
	 * Enables/disabled the shadow effet.
	 * 
	 * @param shadowed
	 * 		{@code true} to display a shadow.
	 */
	public void setShadowed(boolean shadowed)
	{	this.shadowed = shadowed;
	}
	
	/**
	 * Paints the shadow of the text.
	 * 
	 * @param g
	 * 		Graphics object used for painting.
	 * @param ds
	 * 		Translation parameter (how far the shadow is).
	 */
	private void paintShadow(Graphics2D g, int ds)
	{	g.translate(ds,ds);
		Rectangle2D r = shape.getBounds2D();
		float x = (float)r.getX();
		float y = (float)r.getY();
		//float w = (float)r.getWidth();
		float h = (float)r.getHeight();
		Color gradientStart = new Color(0, 0, 0,120);
		Color gradientEnd = new Color(0, 0, 0, 40);
		GradientPaint gp = new GradientPaint(x,y,gradientStart,x,y+h, gradientEnd);
		Paint p = g.getPaint();
		g.setPaint(gp);
		g.fill(shape);
		g.translate(-ds,-ds);
		g.setPaint(p);
	}

	/////////////////////////////////////////////////////////////////
	// BEVEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether the bevel effect should be applied */
	private boolean beveled = true;
	/** Whether the bevel effect should be inverted */
	private boolean inverted = false;
	
	/**
	 * Disables/Enables the bevel effect.
	 * 
	 * @param beveled
	 * 		{@code true} to enable the bevel effect.
	 */
	public void setBeveled(boolean beveled)
	{	this.beveled = beveled;
	}
	
	/**
	 * Disables/Enables bevel effect inversion.
	 * 
	 * @param inverted
	 * 		{@code true} to invert the bevel effect.
	 */
	public void setInverted(boolean inverted)
	{	this.inverted = inverted;
	}

	/**
	 * Paints the bevel effect in the specified graphics.
	 * 
	 * @param g
	 * 		Graphics used for painting.
	 * @param inverted
	 * 		Whether the effect should be inverted.
	 */
	private void paintBevel(Graphics2D g, boolean inverted)
	{	int jitter = 1;
		Rectangle r = shape.getBounds();
		//
		Paint dark = new GradientPaint(r.x, r.y, new Color(0,0,0,150),r.x, r.y+r.height, new Color(0,0,0,210));
		Paint light = new GradientPaint(r.x, r.y, new Color(255,255,255,230),r.x, r.y+r.height, new Color(255,255,255,180));
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

	/////////////////////////////////////////////////////////////////
	// FATTEN			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Fatten parameter */
	private float fatten = 1f;

	/**
	 * Sets the fatten parameter, i.e. some kind of additional
	 * outline drawn in top of the text.
	 * 
	 * @param fatten
	 * 		Thickness of the outline (0 for no effect at all).
	 */
	public void setFatten(float fatten)
	{	this.fatten = fatten;
	}

	/////////////////////////////////////////////////////////////////
	// GLASS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether a glass effect should be applied to the text */
	private boolean glass = true;

	/**
	 * Enables/disables the glass effect.
	 * 
	 * @param glass
	 * 		{@code true} to enable the glass effect.
	 */
	public void setGlass(boolean glass)
	{	this.glass = glass;
	}

	/**
	 * Paints the glass effect using the specified color.
	 * 
	 * @param g
	 * 		Graphics object used for drawing.
	 * @param c
	 * 		Color used for the glass effect.
	 */
	private void paintGlass(Graphics2D g, Color c)
	{	Paint oldPaint = g.getPaint();
		Rectangle r = shape.getBounds();
		int h = r.height/5;
		Shape oldClip = g.getClip();
		GradientPaint bgp = new GradientPaint(r.x,r.y+h*3,c,r.x,r.y+h*5,c.darker());
		g.setClip(r.x,r.y+(h*3),r.width,r.height-(h*3));//h*2);
		g.setPaint(bgp);
		g.fill(shape);
		bgp = new GradientPaint(r.x,r.y,c.brighter(),r.x,r.y+h*2,c);
		g.setClip(r.x,r.y,r.width,h*2);
		g.setPaint(bgp);
		g.fill(shape);
		g.setPaint(oldPaint);
		g.setClip(oldClip);
	}
	
	/////////////////////////////////////////////////////////////////
	// RENDERING HINTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether anti-alias should be used */
	private boolean flagAntiAlias = true;
	/** Quality of the rendering */
	private RenderingPolicy flagRenderingPolicy = RenderingPolicy.QUALITY;
	/** General rendering parameters */
	private RenderingHints renderingHints = null;
   
	/**
	 * Initializes the rendering parameters.
	 */
	private void setRenderingHints()
	{	renderingHints = new RenderingHints(null);
		// anti-alias
		if(flagAntiAlias)
		{	renderingHints.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
		// rendering quality
		if(flagRenderingPolicy==RenderingPolicy.QUALITY)
		{	renderingHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			renderingHints.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			renderingHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		}
		else if(flagRenderingPolicy==RenderingPolicy.SPEED)
		{	renderingHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
			renderingHints.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
			renderingHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		}
		// misc
		renderingHints.put(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		renderingHints.put(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
		renderingHints.put(RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_ENABLE);
	}
	
	/////////////////////////////////////////////////////////////////
	// PAINT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Paints the rendered text using the current parameters.
	 * 
	 * @param go
	 * 		Graphics object used for painting.
	 */
	public void paint(Graphics go)
	{	Graphics2D g = (Graphics2D)go;
		g.setRenderingHints(renderingHints);
	
		Rectangle r = shape.getBounds();
		int x = xc - r.width/2;
		int y = yc + r.height/2;

		g.setColor(Color.BLACK);
		g.translate(x,y);
		// Show bounding box
		paintText(g);
		g.translate(-x,-y);
	}
	
	/////////////////////////////////////////////////////////////////
	// RENDERING POLICY		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Rendering approach.
	 * 
	 * @author Vincent
	 */
	private enum RenderingPolicy
	{	/** Default approach */
		DEFAULT,
		/** Favor graphical quality */
		QUALITY,
		/** Favor fast rendering */
		SPEED;
	}
}
