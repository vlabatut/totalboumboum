package org.totalboumboum.ai.v201213.ais.besnilikangal.v3;

import java.util.HashSet;
import java.util.Set;

import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;

/**
 * La class qui contient des methods utiles pour les operations des items.
 * 
 * @author Doruk Kangal
 * @author Mustafa Besnili
 */
@SuppressWarnings("deprecation")
public class ItemOperation
{
	/** Notre IA*/
	private BesniliKangal ai;
	/** Le nombre de bombe qu'on a ramassé.Celle qui est initial soustrait celle qu'on a collecté */
	public int extraBombCount;
	/**Les cases qu'il contient un bonus*/
	private Set<AiTile> goodItemTiles;
	/**Les cases qu'il contient un malus*/
	private Set<AiTile> malusTiles;

	/**
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public ItemOperation( BesniliKangal ai ) throws StopRequestException
	{
		ai.checkInterruption();
		this.ai = ai;
		goodItemTiles = new HashSet<AiTile>();
		malusTiles = new HashSet<AiTile>();
	}

	/////////////////////////////////////////////////////////////////
	// UPDATES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Mettre a jour les cases qu'ils contient malus ou bonus.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public void update() throws StopRequestException
	{
		ai.checkInterruption();

		goodItemTiles.clear();
		malusTiles.clear();

		updateItemTiles();
	}

	/**
	 * Mettre a jour les cases qu'ils contient malus ou bonus,
	 * s'il y en a,ajoute dans goodItemTiles et malusItemTiles.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public void updateItemTiles() throws StopRequestException
	{
		ai.checkInterruption();
		Set<AiTile> safeTiles = ai.tileOperation.getSafeTiles();
		for ( AiItem item : ai.getZone().getItems() )
		{
			ai.checkInterruption();
			AiTile itemTile = item.getTile();
			if ( safeTiles.contains( itemTile ) )
			{
				if ( isGoodItem( item ) )
					goodItemTiles.add( itemTile );
				else
					malusTiles.add( itemTile );
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// GETTERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Getter goodItemTiles
	 *
	 * @return Set<AiTile>
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Set<AiTile> getGoodItemTiles() throws StopRequestException
	{
		ai.checkInterruption();
		return goodItemTiles;
	}

	/** Getter malusTiles
	 * 
	 * @return Set<AiTile>
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Set<AiTile> getMalusTiles() throws StopRequestException
	{
		ai.checkInterruption();
		return malusTiles;
	}

	/**
	 * Determine pour un item donné, si cet item est utile pour nous.
	 * 
	 * @param item
	 * 
	 * @return boolean
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean isGoodItem( AiItem item ) throws StopRequestException
	{
		ai.checkInterruption();
		String itemName = item.getType().name();
		return ( !item.isContagious() && ( itemName.contains( "GOLD" ) || itemName.contains( "EXTRA" ) ) && ( !itemName.contains( "ANTI" ) && !itemName.contains( "NON" ) ) );
	}
}
