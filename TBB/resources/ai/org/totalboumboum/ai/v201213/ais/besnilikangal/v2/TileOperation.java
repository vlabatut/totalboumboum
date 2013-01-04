package org.totalboumboum.ai.v201213.ais.besnilikangal.v2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
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
	/** Notre hero */
	private AiHero ownHero;
	/** La zone de jeu */
	private AiZone zone;
	/** Les cases que notre IA peut atteindre  */
	private Set<AiTile> accessibleTiles;
	/** Les cases sures */
	private Set<AiTile> safeTiles;
	/** Les cases que notre IA peut atteidre et qui sont surs (pas de blast ou de flamme) */
	private Set<AiTile> accessibleSafeTiles;
	/** Les cases qui contient le danger */
	private Set<AiTile> dangerousTiles;
	/** Les cases que notre IA peut atteindre et dont il y a des murs destructibles */
	private Set<AiTile> destructibleTiles;
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
		this.ownHero = ai.ownHero;
		zone = ai.getZone();
		accessibleTiles = new HashSet<AiTile>();
		safeTiles = new HashSet<AiTile>();
		accessibleSafeTiles = new HashSet<AiTile>();
		dangerousTiles = new HashSet<AiTile>();
		destructibleTiles = new HashSet<AiTile>();
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
	public void update() throws StopRequestException
	{
		ai.checkInterruption();

		accessibleTiles.clear();
		safeTiles.clear();
		accessibleSafeTiles.clear();
		dangerousTiles.clear();
		destructibleTiles.clear();
		biggestTile = ownHero.getTile();

		updateAccessibleTiles( ownHero.getTile() );
		updateDangerousTiles();
		updateSafeTiles();
		updateAccessibleSafeTiles();
		updateDestructibleTiles();
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
		if ( sourceTile.isCrossableBy( ownHero ) && !accessibleTiles.contains( sourceTile ) )
		{
			accessibleTiles.add( sourceTile );
			for ( Direction direction : Direction.getPrimaryValues() )
			{
				ai.checkInterruption();
				updateAccessibleTiles( sourceTile.getNeighbor( direction ) );
			}
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
		for ( AiBomb bomb : zone.getBombs() )
		{
			ai.checkInterruption();
			for ( AiTile tile : bomb.getBlast() )
			{
				ai.checkInterruption();
				dangerousTiles.add( tile );
			}
		}
		for ( AiFire fire : zone.getFires() )
		{
			ai.checkInterruption();
			dangerousTiles.add( fire.getTile() );
		}
		for ( AiItem item : zone.getItems() )
		{
			ai.checkInterruption();
			if ( item.getType().name().startsWith( "NO" ) || item.getType().name().startsWith(  "ANTI" ) )
				dangerousTiles.add( item.getTile() );
		}
		AiSuddenDeathEvent event = zone.getNextSuddenDeathEvent();
		if ( event != null )
		{
			double escapeTime = ownHero.getWalkingSpeed() * ownHero.getTile().getSize() * 3;
			if ( event.getTime() <= escapeTime )
				dangerousTiles.addAll( event.getTiles() );
		}
	}

	/**
	 * Mettre a jour les dont notre IA peut attéindre
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	private void updateSafeTiles() throws StopRequestException
	{
		ai.checkInterruption();
		for ( AiTile tile : zone.getTiles() )
		{
			ai.checkInterruption();
			if ( tile.isCrossableBy( ownHero ) && !dangerousTiles.contains( tile ) /* && playArea.contains( tile ) */ )
				safeTiles.add( tile );
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
	private void updateDestructibleTiles() throws StopRequestException
	{
		ai.checkInterruption();
		for ( AiBlock destructibleBlock : zone.getDestructibleBlocks() )
		{
			ai.checkInterruption();
//			if ( safeTiles.contains( destructibleBlock.getTile() ) )
				destructibleTiles.add( destructibleBlock.getTile() );
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
		AiTile destinationTile = ownHero.getTile();

		Map<AiTile, Float> utilitiesByTile = ai.utilityHandler.getUtilitiesByTile();
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
			if ( zone.getTileDistance( accessibleTile, ownHero.getTile() ) <= radius )
				tilesWithinRadius.add( accessibleTile );
		}
		return tilesWithinRadius;
	}

	/**
	 * Renvoyer les cases qui ne sont pas en danger.C'est-a-dire qu'il n'y aucune bombe qui touche cette case.
	 * 
	 * @return Set<AiTile>
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Set<AiTile> getSafeTiles() throws StopRequestException
	{
		ai.checkInterruption();
		return new HashSet<AiTile>( safeTiles );
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
	 * Retourner les cases de l'ennemi en considerant notre flamme.
	 * 
	 * @param bombRange
	 * 
	 * @return Set<AiTile>
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Set<AiTile> getEnnemyTilesInBombRange( int bombRange ) throws StopRequestException
	{
		ai.checkInterruption();
		Set<AiTile> tilesWithinRadius = new HashSet<AiTile>();
		int range = ownHero.getBombRange();
		for ( AiHero ennemy : ai.getZone().getRemainingOpponents() )
		{
			ai.checkInterruption();
			AiTile neighbor = ennemy.getTile();
			tilesWithinRadius.add( neighbor );
			ai.checkInterruption();
			for ( Direction direction : Direction.getPrimaryValues() )
			{
				ai.checkInterruption();
				int i = 1;
				while ( i <= range )
				{
					ai.checkInterruption();
					neighbor = neighbor.getNeighbor( direction );
					if ( !neighbor.isCrossableBy( ennemy ) )
						break;
					tilesWithinRadius.add( neighbor );
					i++;
				}
			}
		}
		return tilesWithinRadius;
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
			if ( zone.getTileDistance( accessibleTile, ownHero.getTile() ) * accessibleTile.getSize()<= radius )
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
		AiTile result = ownHero.getTile();
		AiTile itemTile = null;
		int radius = (int) ( 2 * ownHero.getWalkingSpeed() * ownHero.getBombDuration() );
		int distance = Integer.MAX_VALUE;
		for ( AiTile safeTile : accessibleSafeTiles )
		{
			ai.checkInterruption();
			int distanceManhattan = zone.getTileDistance( ownHero.getTile(), safeTile );
			if ( distance > distanceManhattan )
			{
				distance = distanceManhattan;
				result = safeTile;
			}
			if ( !safeTile.getItems().isEmpty() && radius > distanceManhattan )
			{
				radius = distanceManhattan;
				itemTile = safeTile;
			}
		}
		return ( itemTile != null ) ? itemTile : result;
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
	public Set<AiTile> getDestructibleTiles() throws StopRequestException
	{
		ai.checkInterruption();
		return new HashSet<AiTile>( destructibleTiles );
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
