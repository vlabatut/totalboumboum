package fr.free.totalboumboum.ai.adapter200809;

import fr.free.totalboumboum.engine.content.sprite.Sprite;

public abstract class AiSprite
{
	public AiSprite(AiTile tile, Sprite sprite)
	{	this.tile = tile;
		initLocation(sprite);
		initState(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** �tat dans lequel se trouve ce sprite */
	private AiState state;

	/** 
	 * renvoie l'�tat dans lequel se trouve ce sprite
	 * (ie: quelle action il est en train d'effectuer ou de subir)
	 */
	public AiState getState()
	{	return state;
	}
	/** 
	 * initialise l'�tat dans lequel se trouve ce sprite
	 */
	private void initState(Sprite sprite)
	{	state = new AiState(sprite);
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** repr�sentation de la case contenant ce sprite */
	private AiTile tile;
	
	/** 
	 * renvoie la repr�sentation de la case contenant ce sprite 
	 */
	public AiTile getTile()
	{	return tile;
	}
	/** 
	 * renvoie le num�ro de la ligne contenant ce sprite 
	 */
	public int getLine()
	{	return tile.getLine();	
	}
	/** 
	 * renvoie le num�ro de la colonne contenant ce sprite
	 */
	public int getCol()
	{	return tile.getCol();	
	}
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abscisse de ce sprite en pixels */
	private double posX;
	/** ordonn�e de ce sprite en pixels */
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
	 * renvoie l'ordonn�e de ce sprite en pixels 
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
