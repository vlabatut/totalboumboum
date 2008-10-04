package fr.free.totalboumboum.ai.adapter200809;

import fr.free.totalboumboum.data.profile.PredefinedColor;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;

public class AiHero extends AiSprite<Hero>
{
	public AiHero(AiTile tile, Hero sprite)
	{	super(tile,sprite);
		initColor(sprite);
		initBombParam(sprite);
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMB PARAM		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int bombRange;
	private int bombNumber;
	private int bombCount;
	
	public int getBombRange()
	{	return bombRange;
	}
	public int getBombNumber()
	{	return bombNumber;
	}
	public int getBombCount()
	{	return bombCount;
	}
	
	private void initBombParam(Hero sprite)
	{	// bomb range
		StateAbility ab = sprite.computeCapacity(StateAbility.BOMB_RANGE);
        bombRange = (int)ab.getStrength();
		// max number of simultaneous bombs
    	ab = sprite.computeCapacity(StateAbility.BOMB_NUMBER);
    	bombNumber = (int)ab.getStrength();
        // number of bombs currently dropped
    	bombCount = sprite.getDroppedBombs().size();
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PredefinedColor color;
	
	public PredefinedColor getColor()
	{	return color;	
	}
	private void initColor(Hero sprite)
	{	sprite.getColor();	
	}
}
