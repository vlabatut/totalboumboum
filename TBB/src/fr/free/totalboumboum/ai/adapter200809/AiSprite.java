package fr.free.totalboumboum.ai.adapter200809;

import fr.free.totalboumboum.engine.content.sprite.Sprite;

public abstract class AiSprite<T extends Sprite>
{	
	public AiSprite(AiTile tile, T sprite)
	{	this.tile = tile;
		this.sprite = sprite;
		state = new AiState(sprite);
	}
	
	void update()
	{	updateLocation();
		updateState();
		checked = true;
	}

	void finish()
	{	// state
		state.finish();
		state = null;
		// misc
		tile = null;
		sprite = null;
		
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private T sprite;
	private boolean checked;
	
	boolean isSprite(T sprite)
	{	return this.sprite == sprite;
	}
	
	boolean isChecked()
	{	return checked;	
	}
	void uncheck()
	{	checked = false; 
	}

	T getSprite()
	{	return sprite;	
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
	private void updateState()
	{	state.update();
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
	private void updateLocation()
	{	posX = sprite.getCurrentPosX();
		posY = sprite.getCurrentPosY();
		posZ = sprite.getCurrentPosZ();		
	}
}
