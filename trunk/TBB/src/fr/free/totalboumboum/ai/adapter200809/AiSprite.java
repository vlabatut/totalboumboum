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
		initState(sprite);
	}

	/** 
	 * construit et renvoie une représentation du sprite passé en paramètre 
	 */
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
	// STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** état dans lequel se trouve ce sprite */
	private AiState state;
	
	/** 
	 * renvoie l'état dans lequel se trouve ce sprite
	 * (ie: quelle action il est en train d'effectuer ou de subir)
	 */
	public AiState getState()
	{	return state;
	}
	/** 
	 * initialise l'état dans lequel se trouve ce sprite
	 */
	private void initState(Sprite sprite)
	{	state = new AiState(sprite);
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** représentation de la case contenant ce sprite */
	private AiTile tile;
	
	/** 
	 * renvoie la représentation de la case contenant ce sprite 
	 */
	public AiTile getTile()
	{	return tile;
	}
	/** 
	 * renvoie le numéro de la ligne contenant ce sprite 
	 */
	public int getLine()
	{	return tile.getLine();	
	}
	/** 
	 * renvoie le numéro de la colonne contenant ce sprite
	 */
	public int getCol()
	{	return tile.getCol();	
	}
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abscisse de ce sprite en pixels */
	private double posX;
	/** ordonnée de ce sprite en pixels */
	private double posY;
	/** altitude de ce sprite en pixels */
	private double posZ;

	/** 
	 * renvoie l'abscisse de ce sprite en pixels 
	 */
	public double getPosX()
	{	return posX;
	}
	/** 
	 * renvoie l'ordonnée de ce sprite en pixels 
	 */
	public double getPosY()
	{	return posY;
	}
	/** 
	 * renvoie l'altitude de ce sprite en pixels 
	 */
	public double getPosZ()
	{	return posZ;
	}
	/** 
	 * initialise les positions de ce sprite en pixels 
	 */
	private void initLocation(Sprite sprite)
	{	posX = sprite.getCurrentPosX();
		posY = sprite.getCurrentPosY();
		posZ = sprite.getCurrentPosZ();		
	}
}
