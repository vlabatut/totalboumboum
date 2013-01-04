package org.totalboumboum.ai.v201213.ais.besnilikangal.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * La class qui contient des methods utiles pour les operations des cases.
 * 
 * @author Doruk Kangal
 * @author Mustafa Besnili
 */
public class TileOperation
{
	/** Notre IA */
	private BesniliKangal ai;
	/** Les cases que notre IA peut atteindre  */
	private Set<AiTile> accessibleTiles;
	/** Les cases que notre IA peut atteidre et qui sont surs (pas de blast ou de flamme) */
	private Set<AiTile> accessibleSafeTiles;
	/** Les cases qui contient le danger */
	private Set<AiTile> dangerousTiles;
	/** Les cases que notre IA peut atteindre et dont il y a des murs destructibles */
	private Map<AiTile, Integer> accessibleDestructibleTiles;
	/** La case dont la valeur d'utilité est maximale */
	private AiTile biggestTile;

	/**
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public TileOperation(BesniliKangal ai) throws StopRequestException
	{
		ai.checkInterruption();
		this.ai = ai;

		accessibleTiles = new HashSet<AiTile>();
		accessibleSafeTiles = new HashSet<AiTile>();
		dangerousTiles = new HashSet<AiTile>();
		accessibleDestructibleTiles = new HashMap<AiTile, Integer>();
	}

	/////////////////////////////////////////////////////////////////
	// UPDATES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * Mettre a jour les cases
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public void updateTiles() throws StopRequestException
	{
		ai.checkInterruption();

		accessibleTiles.clear();
		accessibleSafeTiles.clear();
		dangerousTiles.clear();
		accessibleDestructibleTiles.clear();
		biggestTile = ai.getHero().getTile();

		updateAccessibleTiles( ai.getHero().getTile() );
		updateDangerousTiles();
		updateAccessibleSafeTiles();
		updateAccessibleDestructibleTiles();
		updateBiggestTile();
	}

	/**
	 * Method recursive qui trouve les cases que notre IA peut atteindre.
	 * 
	 * @param sourceTile
	 *            La case de source qu'on commence par, s'il n'est pas
	 *            crossable,on ne produit rien.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	private void updateAccessibleTiles( AiTile sourceTile ) throws StopRequestException
	{
		ai.checkInterruption();
		if ( sourceTile.isCrossableBy( ai.getHero() ) )
		{
			accessibleTiles.add( sourceTile );
			if ( sourceTile.getNeighbor( Direction.UP ).isCrossableBy( ai.getHero() ) && !accessibleTiles.contains( sourceTile.getNeighbor( Direction.UP ) ) )
				updateAccessibleTiles( sourceTile.getNeighbor( Direction.UP ) );
			if ( sourceTile.getNeighbor( Direction.DOWN ).isCrossableBy( ai.getHero() ) && !accessibleTiles.contains( sourceTile.getNeighbor( Direction.DOWN ) ) )
				updateAccessibleTiles( sourceTile.getNeighbor( Direction.DOWN ) );
			if ( sourceTile.getNeighbor( Direction.LEFT ).isCrossableBy( ai.getHero() ) && !accessibleTiles.contains( sourceTile.getNeighbor( Direction.LEFT ) ) )
				updateAccessibleTiles( sourceTile.getNeighbor( Direction.LEFT ) );
			if ( sourceTile.getNeighbor( Direction.RIGHT ).isCrossableBy( ai.getHero() ) && !accessibleTiles.contains( sourceTile.getNeighbor( Direction.RIGHT ) ) )
				updateAccessibleTiles( sourceTile.getNeighbor( Direction.RIGHT ) );
		}
	}

	/**
	 * Mettre a jour les cases dangereuses.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	private void updateDangerousTiles() throws StopRequestException
	{
		ai.checkInterruption();
		for ( AiBomb bomb : ai.getZone().getBombs() )
		{
			ai.checkInterruption();
			for ( AiTile tile : bomb.getBlast() )
			{
				ai.checkInterruption();
				dangerousTiles.add( tile );
			}
		}
		for ( AiFire fire : ai.getZone().getFires() )
		{
			ai.checkInterruption();
			dangerousTiles.add( fire.getTile() );
		}
	}

	/**
	 * Mettre a jour les dont notre IA peut attéindre
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	private void updateAccessibleSafeTiles() throws StopRequestException
	{
		ai.checkInterruption();
		for ( AiTile accessibleTile : accessibleTiles )
		{
			ai.checkInterruption();
			if ( !dangerousTiles.contains( accessibleTile ) )
				accessibleSafeTiles.add( accessibleTile );
		}
	}

	/**
	 * Mettre a jour les cases accessible et destructibles
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	private void updateAccessibleDestructibleTiles() throws StopRequestException
	{
		ai.checkInterruption();
		for ( AiTile safeTile : accessibleSafeTiles )
		{
			ai.checkInterruption();
			int wallCount = 0;
			for ( AiTile neighbor : safeTile.getNeighbors() )
			{
				ai.checkInterruption();
				if ( !neighbor.getBlocks().isEmpty() && neighbor.getBlocks().get( 0 ).isDestructible() )
					wallCount++;
			}
			if ( wallCount >= 0)
				accessibleDestructibleTiles.put( safeTile, wallCount );
		}
	}

	/**
	 * Mettre a jour la case dont la valeur d'utilité est maximal.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	private void updateBiggestTile() throws StopRequestException
	{
		ai.checkInterruption();
		AiTile destinationTile = ai.getHero().getTile();

		Map<AiTile, Float> utilitiesByTile = ai.getUtilityHandler().getUtilitiesByTile();
		Float tileUtility = 0f;
		for ( Entry<AiTile, Float> utilityByTile : utilitiesByTile.entrySet() )
		{
			ai.checkInterruption();
			if ( tileUtility < utilityByTile.getValue() )
			{
				destinationTile = utilityByTile.getKey();
				tileUtility = utilityByTile.getValue();
			}
		}
		biggestTile = destinationTile;
	}

	/////////////////////////////////////////////////////////////////
	// GETTERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * To get the accessible tiles from a given source tile.
	 * 
	 * @return List of all tiles that accessible from a given source tile.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Set<AiTile> getAccessibleTiles() throws StopRequestException
	{
		ai.checkInterruption();
		return new HashSet<AiTile>( accessibleTiles );
	}

	/**
	 * Returns all accessible tiles that within given radius. Uses
	 * "Manhattan Distance" to calculate distances, A* is too slow for this. <br/>
	 * 
	 * @param radius
	 *            Max radius to look.
	 * 
	 * @return List of the tiles within the circle with the center as the given
	 *         tile and the given radius.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Set<AiTile> getAccessibleTilesWithinRadius( int radius ) throws StopRequestException
	{
		ai.checkInterruption();
		Set<AiTile> tilesWithinRadius = new HashSet<AiTile>();
		for ( AiTile accessibleTile : accessibleTiles )
		{
			ai.checkInterruption();
			if ( ai.getZone().getTileDistance( accessibleTile, ai.getHero().getTile() ) <= radius )
				tilesWithinRadius.add( accessibleTile );
		}
		return tilesWithinRadius;
	}

	/**
	 * To get the accessible safe tiles from a given source tile.
	 * 
	 * @return List of all tiles that accessible from a given source tile.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Set<AiTile> getAccessibleSafeTiles() throws StopRequestException
	{
		ai.checkInterruption();
		return new HashSet<AiTile>( accessibleSafeTiles );
	}

	/**
	 * Returns all accessible tiles that within given radius. Uses
	 * "Manhattan Distance" to calculate distances, A* is too slow for this. <br/>
	 * 
	 * @param radius
	 *            Max radius to look.
	 * 
	 * @return List of the tiles within the circle with the center as the given
	 *         tile and the given radius .
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Set<AiTile> getSafeTilesWithinRadius( int radius ) throws StopRequestException
	{
		ai.checkInterruption();
		Set<AiTile> tilesWithinRadius = new HashSet<AiTile>();
		for ( AiTile accessibleTile : accessibleSafeTiles )
		{
			ai.checkInterruption();
			if ( ai.getZone().getTileDistance( accessibleTile, ai.getHero().getTile() ) * accessibleTile.getSize()<= radius )
				tilesWithinRadius.add( accessibleTile );
		}
		return tilesWithinRadius;
	}

	/**
	 * Finds the closest accessible safe tile to this AI's own hero's tile. Uses
	 * "Manhattan Distance" to calculate the distances. <br />
	 * 
	 * @return Closest safe tile to this AI's hero.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiTile getClosestSafeTile() throws StopRequestException
	{
		ai.checkInterruption();
		int distance = Integer.MAX_VALUE;
		AiTile result = ai.getHero().getTile();
		for ( AiTile safeTile : accessibleSafeTiles )
		{
			ai.checkInterruption();
			if ( distance > ai.getZone().getTileDistance( ai.getHero().getTile(), safeTile ) )
			{
				distance = ai.getZone().getTileDistance( ai.getHero().getTile(), safeTile );
				result = safeTile;
			}
		}
		return result;
	}

	/**
	 * To get the accessible dangerous tiles from a given source tile.
	 * 
	 * @return List of all tiles that accessible from a given source tile.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Set<AiTile> getDangerousTiles() throws StopRequestException
	{
		ai.checkInterruption();
		return new HashSet<AiTile>( dangerousTiles );
	}

	/**
	 * @param bomb
	 * 
	 * @return toutes les cases(blast,fire,exploitation chainé)
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Set<AiTile> getAllBlastTiles( AiBomb bomb ) throws StopRequestException
	{
		ai.checkInterruption();
		return getAllBlastTiles( new ArrayList<AiBomb>( Arrays.asList( bomb ) ), 0, new HashSet<AiTile>() );
	}
	
	/**
	 * Une methode recursice qui nous permet de trouver toutes les cases non
	 * sures, on considere ici les cases qui peut etre en danger avec
	 * l'exploitation chainée.
	 * 
	 * @param bombs
	 * @param index
	 * @param blastTiles
	 * 
	 * @return un set des cas qui sont en danger soit par une bombe
	 *         directement,soit par l'exploitation chainée.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	private Set<AiTile> getAllBlastTiles( List<AiBomb> bombs, int index, Set<AiTile> blastTiles ) throws StopRequestException
	{
		ai.checkInterruption();
		if ( index < bombs.size() )
		{
			for ( AiTile blastTile : bombs.get( index ).getBlast() )
			{
				ai.checkInterruption();
				if ( !blastTiles.contains( blastTile ) )
					blastTiles.add( blastTile );
				for ( AiBomb bomb : blastTile.getBombs() )
				{
					ai.checkInterruption();
					if ( !bombs.contains( bomb ) )
						bombs.add( bomb );
				}
			}
			return getAllBlastTiles( bombs, ++index, blastTiles );
		}
		else
			return blastTiles;
	}

	/**
	 * @return une map qui contient les cases accessibles et destructible
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Map<AiTile, Integer> getAccessibleDestructibleTiles() throws StopRequestException
	{
		ai.checkInterruption();
		return new HashMap<AiTile, Integer>( accessibleDestructibleTiles );
	}

	/**
	 * Biggest Tile getter
	 * 
	 * @return la case dont la valeur d'utilité est maximal.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiTile getBiggestTile() throws StopRequestException
	{
		ai.checkInterruption();
		return biggestTile;
	}
}
