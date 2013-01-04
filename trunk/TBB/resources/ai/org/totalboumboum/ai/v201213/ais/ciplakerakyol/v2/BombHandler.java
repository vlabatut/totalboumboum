package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;

/**
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class BombHandler extends AiBombHandler<CiplakErakyol>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected BombHandler(CiplakErakyol ai) throws StopRequestException
    {	
		super(ai);
    	ai.checkInterruption();
		verbose = true;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{
		ai.checkInterruption();

		AiHero ownHero = ai.getZone().getOwnHero();
		// ATTACK MODE
		if (this.ai.getModeHandler().getMode().equals(AiMode.ATTACKING))
		{
			if (ownHero.getTile().equals( ai.getTileWithBiggestUtility()))
			{
				// on vérifie si on peut au moins poser une bombe
				int bombNumberCurrent = ownHero.getBombNumberCurrent();
				int bombNumberMax = ownHero.getBombNumberMax();
				if (bombNumberCurrent < bombNumberMax)
				{
					if (canReachSafety())
					{
						AiHero ennemy = getAdversaireAccessible();
						if (ennemy != null)
						{
							List<AiTile> blastTiles = ownHero.getBombPrototype().getBlast();
							if (blastTiles.contains(ennemy.getTile()))
								return true;
						}
					}
				}
			}
		}
		else if (this.ai.getModeHandler().getMode().equals(AiMode.COLLECTING))
		{
			if ( !ownHero.getTile().getBombs().isEmpty() )
			{
				int bombNumberCurrent = ownHero.getBombNumberCurrent();
				int bombNumberMax = ownHero.getBombNumberMax();
				if (bombNumberCurrent < bombNumberMax)
				{
					if (canReachSafety())
					{
						AiTile currentTile = ownHero.getTile();
						for ( AiTile neighbor : currentTile.getNeighbors() )
						{
							List<AiBlock> blocks = neighbor.getBlocks();
							if ( !blocks.isEmpty() && blocks.get(0).isDestructible() )
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{	
		ai.checkInterruption();
	}

	/**
	 * Checks if the given hero can reach a safe tile if he puts a bomb to his
	 * tile. <br />
	 * (TESTED, WORKS)
	 * 
	 * @return If given hero can access a safe tile or not.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean canReachSafety( ) throws StopRequestException
	{
		ai.checkInterruption();
		AiHero ownHero = ai.getZone().getOwnHero();
		double walkingSpeed = ownHero.getWalkingSpeed();
		int distance = (int) (walkingSpeed*ownHero.getBombDuration());
		
		List<AiTile> tilesWithinRadius = getAccessibleTilesWithinRadius(ownHero.getTile(), distance);
		tilesWithinRadius.removeAll( ownHero.getBombPrototype().getBlast() );
		return ( tilesWithinRadius.size() > 0 );
	}

	/**
	 * Returns all accessible tiles that within given radius. Uses
	 * "Manhattan Distance" to calculate distances, A* is too slow for this. <br />
	 * (TESTED, WORKS)
	 * 
	 * @param givenTile
	 *            The center tile.
	 * @param radius
	 *            Max radius to look.
	 * @return List of the tiles within the circle with the center as the given
	 *         tile and the given radius .
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public ArrayList<AiTile> getAccessibleTilesWithinRadius( AiTile givenTile, int radius ) throws StopRequestException
	{
		ai.checkInterruption();
		ArrayList<AiTile> tilesWithinRadius = new ArrayList<AiTile>();
		for ( AiTile currentTile : ai.getZone().getTiles() )
		{
			ai.checkInterruption();
			if ( this.ai.getZone().getTileDistance( currentTile, givenTile ) <= radius ) tilesWithinRadius.add( currentTile );
		}
		return tilesWithinRadius;
	}

	/**
	 * 
	 * @return heros which are accessible
	 * @throws StopRequestException 
	 */
	private AiHero getAdversaireAccessible() throws StopRequestException
	{
		ArrayList<AiTile> accessibleTiles = ai.getAccessibleTiles( ai.getZone().getOwnHero().getTile() );
		
		for ( AiHero ennemy : ai.getZone().getRemainingOpponents() )
		{
			if ( accessibleTiles.contains( ennemy.getTile() ) )
				return ennemy;
		}
		return null;
	}
}
