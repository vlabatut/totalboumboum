package org.totalboumboum.ai.v201213.ais.erdemtayyar.v2;

import java.util.ArrayList;
import java.util.Collections;

import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;




/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 */
public class MoveHandler extends AiMoveHandler<ErdemTayyar>
{	
	/**
	 * Radius to search for search tiles, with center as our hero's current
	 * tile.
	 */
	private final int SAFE_TILE_SEARCH_RADIUS = 6;
	
	/**
	 * Represents the first element of an array-list etc.
	 */
	private final int FIRST = 0;
	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected MoveHandler(ErdemTayyar ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
	}

	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException
	{	ai.checkInterruption();
				
		return null;
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath updateCurrentPath() throws StopRequestException
	{	ai.checkInterruption();
	return null;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction updateCurrentDirection() throws StopRequestException
	{	ai.checkInterruption();
	Direction result = Direction.NONE;

	if (this.ai.getHs().isHeroInDanger()) {

		// Think twice before setting check radius' value
		result = this.ai.getPs()
				.getNextDirectionOnPath(
						this.ai.getTs().getClosestSafeTile(
								SAFE_TILE_SEARCH_RADIUS));

	} else {
		this.ai.setUtilityMap(this.ai.getUtilityHandler()
				.getUtilitiesByTile());
		AiTile biggestTile = this.ai.getTileWithBiggestUtility();
		this.ai.addToLastUtilityTiles(biggestTile);

		// Here we check if our her is stuck between two utility tiles.
		// If so, he decides to go to a specific one. (Decision maker, break
		// dance breaker)
		if (this.ai.getHs().heroInEndlessLoop()
				&& this.ai.getZone()
						.getBombsByColor(PredefinedColor.ORANGE).isEmpty()) {
			ArrayList<AiTile> a = new ArrayList<AiTile>(
					this.ai.getStuckUtilityTiles());
			Collections.sort(a);
			biggestTile = a.get(FIRST);
		}

		if (biggestTile.equals(this.ai.getHero().getTile())) {
			result = Direction.NONE;
		} else { 
			result = this.ai.getPs().getNextDirectionOnPath(biggestTile);
		}
	}

	// This checks if the result is dangerous. If so, hero stays where he
	// is.
	if (!this.ai.getTs().getCurrentDangerousTiles().contains(this.ai.getHero().getTile())
		&& this.ai.getTs().getCurrentDangerousTiles().contains(this.ai.getHero().getTile().getNeighbor(result)))
		result = Direction.NONE;

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
