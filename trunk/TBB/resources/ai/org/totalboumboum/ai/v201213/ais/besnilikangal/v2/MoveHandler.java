package org.totalboumboum.ai.v201213.ais.besnilikangal.v2;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
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
public class MoveHandler extends AiMoveHandler<BesniliKangal>
{
	/** */
	boolean secondaryBombing = false ;
	/** */
	public AiTile safeDestination =null;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected MoveHandler(BesniliKangal ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException
	{
		ai.checkInterruption();
		if ( ai.heroOperation.isHeroInDanger() )
			return ai.tileOperation.getClosestSafeTile();
		else
			return ai.tileOperation.getBiggestTile();
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath updateCurrentPath() throws StopRequestException
	{
		ai.checkInterruption();
		secondaryBombing = false;
		if ( ai.tileOperation.getAccessibleTiles().contains( currentDestination ) )
			return ai.pathOperation.getShortestPathToAccessibleTile( currentDestination );
		else
		{
			AiPath indirectPath = ai.pathOperation.getShortestPathToInaccessibleTile( currentDestination );
			Iterator<AiLocation> it = indirectPath.getLocations().iterator();
			AiTile blockedTile = null;
			AiTile previousTile = null;
			while ( it.hasNext() && blockedTile == null )
			{
				ai.checkInterruption();
				// la case est bloquante si elle contient un bloc (forcément destructible)
				AiLocation location = it.next();
				AiTile tile = location.getTile();
				print( "Current tile :" + tile.toString() );
				List<AiBlock> blocks = tile.getBlocks();
				// alors on prend celle d'avant
				if ( !blocks.isEmpty() )
					blockedTile = previousTile;
				previousTile = tile;
				if ( blockedTile != null && blockedTile.getBombs().isEmpty() )
				{
					safeDestination = blockedTile;
					if ( safeDestination.equals( ai.ownHero.getTile() ) )
						secondaryBombing = true;
					else
					{
						currentPath = ai.pathOperation.getShortestPathToAccessibleTile( safeDestination );
						return currentPath;
					}
				}
			}
			return indirectPath;
		}
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction updateCurrentDirection() throws StopRequestException
	{
		ai.checkInterruption();
		Direction result = Direction.NONE;
		if ( currentPath != null && currentPath.getLength() >= 2 && currentPath.getFirstPause() <= 0 )
		{
			AiTile currentTile = ai.ownHero.getTile();
			AiTile nextTile = currentPath.getLocation( 1 ).getTile();
			Set<AiTile> dangerousTiles = ai.tileOperation.getDangerousTiles();
			if ( dangerousTiles.contains( currentTile ) || !dangerousTiles.contains( nextTile ) )
				result = ( currentTile == nextTile ) ? Direction.NONE : ai.getZone().getDirection( currentTile, nextTile );
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
	}
}
