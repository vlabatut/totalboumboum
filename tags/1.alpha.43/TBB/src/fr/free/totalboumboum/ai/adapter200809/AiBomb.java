package fr.free.totalboumboum.ai.adapter200809;

import fr.free.totalboumboum.data.profile.PredefinedColor;
import fr.free.totalboumboum.engine.content.feature.GestureConstants;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;

public class AiBomb extends AiSprite
{
	public AiBomb(AiTile tile, Bomb sprite)
	{	super(tile,sprite);
		initType(sprite);
		initRange(sprite);
		initWorking(sprite);
		initColor(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// TYPE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String type;
	
	public String getType()
	{	return type;	
	}
	private void initType(Bomb bomb)
	{	type = bomb.getBombName();		
	}
	
	/////////////////////////////////////////////////////////////////
	// RANGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int range;
	
	public int getRange()
	{	return range;	
	}
	private void initRange(Bomb bomb)
	{	range = bomb.getFlameRange();
	}
	
	/////////////////////////////////////////////////////////////////
	// WORKING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean working;
	
	public boolean isWorking()
	{	return working;	
	}
	private void initWorking(Bomb sprite)
	{	String gesture = sprite.getCurrentGesture();
		if(gesture.equalsIgnoreCase(GestureConstants.OSCILLATING_FAILING)
			|| gesture.equalsIgnoreCase(GestureConstants.STANDING_FAILING))
			working = false;
		else
			working = true;
		
	}

	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PredefinedColor color;
	
	public PredefinedColor getColor()
	{	return color;	
	}
	private void initColor(Bomb sprite)
	{	sprite.getColor();	
	}
}
