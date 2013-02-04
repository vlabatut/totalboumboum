package org.totalboumboum.ai.v201213.ais.besnilikangal.v3;

import java.util.Iterator;
import java.util.Set;

import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * 
 * @author Mustafa Besnili
 * @author Doruk Kangal
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<BesniliKangal>
{
	/**
	 * Une destination sure,on utilise dans A* approximation quand on atteind a
	 * cette case, On pose une bombe,puisque celle est la case qui nous bloque.
	 */
	public AiTile safeDestination = null;
	/**
	 * Pour avencer vers l'ennemi,on utilise cette variable.Si elle est true,on
	 * pose une bombe.
	 */
	boolean secondaryBombing = false;
	/**
	 * Il represente la previous destination,on utilise cette variable pour
	 * controler le chemin est changés
	 */
	public AiTile previousDestination = null;
	/** Determine si on a changé la case objective ou pas. */
	private boolean changeDestination = true;
	/** Determine si le chemin qu'on a calculé est fini. */
	private boolean isPathEnded = true;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected MoveHandler( BesniliKangal ai ) throws StopRequestException
	{
		super( ai );
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// DESTINATION 				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException
	{
		ai.checkInterruption();
		AiTile currentDestination = null;
		if ( ai.heroOperation.isHeroInDanger() )
			currentDestination = ai.tileOperation.getClosestSafeTile();
		else
			currentDestination = ai.tileOperation.getBiggestTile();
		changeDestination = ( currentDestination != previousDestination );
		previousDestination = currentDestination;
		return currentDestination;
	}

	/////////////////////////////////////////////////////////////////
	// PATH 					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath updateCurrentPath() throws StopRequestException
	{
		ai.checkInterruption();
		if ( currentPath == null || currentPath.getLength() == 0 || changeDestination || isPathEnded )
		{
			secondaryBombing = false;
			if ( ai.tileOperation.getAccessibleTiles().contains( currentDestination ) )
				return ai.pathOperation.getShortestPathToAccessibleTile( currentDestination );
			else
			{
				AiPath indirectPath = ai.pathOperation.getShortestPathToInaccessibleTile( currentDestination );
				if ( indirectPath != null )
				{
					Iterator<AiLocation> it = indirectPath.getLocations().iterator();
					AiTile blockedTile = null;
					AiTile previousTile = null;
					while ( it.hasNext() && blockedTile == null )
					{
						ai.checkInterruption();
						AiTile tile = it.next().getTile();
						if ( !tile.getBlocks().isEmpty() || ai.itemOperation.getMalusTiles().contains( tile ) )
							blockedTile = previousTile;
						previousTile = tile;
						if ( blockedTile != null && blockedTile.getBombs().isEmpty() )
						{
							safeDestination = blockedTile;
							if ( safeDestination.equals( ai.ownHero.getTile() ) )
								secondaryBombing = true;
							else
								return ai.pathOperation.getShortestPathToAccessibleTile( safeDestination );
						}
					}
					return indirectPath;
				}
			}
		}
		return currentPath;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION 				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction updateCurrentDirection() throws StopRequestException
	{
		ai.checkInterruption();
		Direction result = Direction.NONE;
		AiTile currentTile = ai.ownHero.getTile();
		if ( currentPath != null && currentPath.getLength() > 0 )
		{
			AiTile nextTile = ai.pathOperation.getNextTileOnPath( currentPath, currentTile );
			Set<AiTile> dangerousTiles = ai.tileOperation.getDangerousTiles();
			isPathEnded = ( nextTile == null || !nextTile.isCrossableBy( ai.ownHero ) );
			if ( !isPathEnded )
			{
				if ( dangerousTiles.contains( currentTile ) || !dangerousTiles.contains( nextTile ) )
					result = ai.getZone().getDirection( currentTile, nextTile );
				else if ( !dangerousTiles.contains( currentTile ) && dangerousTiles.contains( nextTile ) )
				{
					boolean canPass = true;
					int i = 2;
					AiTile tempTile = currentTile;
					while ( canPass )
					{
						ai.checkInterruption();
						tempTile = ai.pathOperation.getNextTileOnPath( currentPath, tempTile );
						if ( tempTile != null && tempTile.isCrossableBy( ai.ownHero ) && !dangerousTiles.contains( tempTile ) && !ai.itemOperation.getMalusTiles().contains( tempTile ) )
						{
							canPass = true;
							break;
						}
						else if ( tempTile == null || !tempTile.getBlocks().isEmpty() )
						{
							canPass = false;
							break;
						}
						double passTimeByTile = i * ai.heroOperation.passTimeByTile();
						long minElapsedTime = getMinElapsedTime( tempTile );
						;
						if ( passTimeByTile > minElapsedTime )
							canPass = false;
						i++;
					}
					if ( canPass )
						result = ai.getZone().getDirection( currentTile, nextTile );
				}
			}
		}
		return result;
	}

	/**
	 * Renvoyer le temps restant pour une case donnée et qu'il contient une
	 * bombe.
	 * 
	 * @param givenTile
	 * 
	 * @return long
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	private long getMinElapsedTime( AiTile givenTile ) throws StopRequestException
	{
		ai.checkInterruption();
		long minElapsedTime = Long.MAX_VALUE;
		for ( AiBomb bomb : ai.getZone().getBombs() )
		{
			ai.checkInterruption();
			if ( bomb.getBlast().contains( givenTile ) )
			{
				long timeRemaining = bomb.getNormalDuration() - bomb.getElapsedTime();
				minElapsedTime = ( timeRemaining < minElapsedTime ) ? timeRemaining : minElapsedTime;
			}
		}
		return ( minElapsedTime == Long.MAX_VALUE ) ? Long.MIN_VALUE : minElapsedTime;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT 					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{
		ai.checkInterruption();

		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
	}
}
