package fr.free.totalboumboum.ai.adapter200809;

import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.floor.Floor;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;

public abstract class AiSprite
{

	public AiSprite(AiTile tile, Sprite sprite)
	{	this.tile = tile;
		initLocation(sprite);
	}

	public static AiSprite makeSprite(AiTile tile, Sprite sprite)
	{	AiSprite result=null;
		if(sprite instanceof Block)
			result = new AiBlock(tile,(Block)sprite);
		else if(sprite instanceof Bomb)
			result = new AiBomb(tile,(Bomb)sprite);
		else if(sprite instanceof Fire)
			result = new AiFire(tile,(Fire)sprite);
		else if(sprite instanceof Floor)
			result = new AiFloor(tile,(Floor)sprite);
		else if(sprite instanceof Hero)
			result = new AiHero(tile,(Hero)sprite);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AiTile tile;
	
	public AiTile getTile()
	{	return tile;
	}	
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double posX;
	private double posY;
	private double posZ;

	public double getPosX()
	{	return posX;
	}
	public double getPosY()
	{	return posY;
	}
	public double getPosZ()
	{	return posZ;
	}
	private void initLocation(Sprite sprite)
	{	posX = sprite.getCurrentPosX();
		posY = sprite.getCurrentPosY();
		posZ = sprite.getCurrentPosZ();		
	}
	
}
