package fr.free.totalboumboum.engine.content.feature;

import fr.free.totalboumboum.engine.content.sprite.Sprite;

public enum ImageShift
{
	DOWN,BOUNDHEIGHT;
	
	public double getValue(Sprite boundSprite)
	{	double result = 0;
		switch(this)
		{	case DOWN:
				result = 0;
				break;
			case BOUNDHEIGHT:
				result = boundSprite.getBoundHeight();
				break;
		}
		return result;
	}
}
