package fr.free.totalboumboum.engine.content.feature.anime;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.engine.content.feature.ImageShift;


public class AnimeStep
{
	private BufferedImage image;
	private long duration;
	private double xShift;
	private double yShift;
	private BufferedImage shadow;
	private double shadowXShift;
	private double shadowYShift;
	private ImageShift boundYShift;
	
	public AnimeStep()
	{	image = null;
		duration = 0;
		xShift = 0;
		yShift = 0;
		shadow = null;
		shadowXShift = 0;
		shadowYShift = 0;
		boundYShift = ImageShift.DOWN;
	}
	
	public BufferedImage getImage()
	{	return image;
	}
	public void setImage(BufferedImage image)
	{	this.image = image;
	}
	
	public long getDuration()
	{	return duration;
	}
	public void setDuration(long duration)
	{	this.duration = duration;
	}
	
	public AnimeStep copy(ArrayList<BufferedImage> images, ArrayList<BufferedImage> copyImages)
	{	AnimeStep result = new AnimeStep();
		// image
		if(image!=null)
		{	int index = images.indexOf(image);
			BufferedImage copyImg = copyImages.get(index);
			result.setImage(copyImg);
		}
		// duration
		result.setDuration(getDuration());
		// shifts
		result.setXShift(xShift);
		result.setYShift(yShift);
		// shadow
		if(shadow!=null)
		{	int index = images.indexOf(shadow);
			BufferedImage copyImg = copyImages.get(index);
			result.setShadow(copyImg);
		}
		result.setShadowXShift(shadowXShift);
		result.setShadowYShift(shadowYShift);
		// bound
		result.setBoundYShift(boundYShift);
		//
		return result;
	}

	public double getXShift()
	{	return xShift;
	}
	public void setXShift(double xShift)
	{	this.xShift = xShift;
	}

	public double getYShift()
	{	return yShift;
	}
	public void setYShift(double yShift)
	{	this.yShift = yShift;
	}

	public boolean hasShadow()
	{	return shadow != null;
	}
	public void setShadow(BufferedImage shadow)
	{	this.shadow = shadow;
	}
	public BufferedImage getShadow()
	{	return shadow;
	}

	public double getShadowXShift()
	{	return shadowXShift;
	}
	public void setShadowXShift(double shadowXShift)
	{	this.shadowXShift = shadowXShift;
	}

	public double getShadowYShift()
	{	return shadowYShift;
	}
	public void setShadowYShift(double shadowYShift)
	{	this.shadowYShift = shadowYShift;
	}

	public ImageShift getBoundYShift()
	{	return boundYShift;
	}
	public void setBoundYShift(ImageShift boundYShift)
	{	this.boundYShift = boundYShift;
	}

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// misc
			boundYShift = null;
			image = null;
			shadow = null;
		}
	}
}
