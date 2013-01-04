package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v1;

import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * @author Tuğçe Gergin
 * @author Seçil Özkanoğlu
 */
public class MoveHandler extends AiMoveHandler<GerginOzkanoglu>
{	
	/**
	 * 
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected MoveHandler(GerginOzkanoglu ai) throws StopRequestException
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
	{	
		ai.checkInterruption();
	    TileCalculation calculate = new TileCalculation(this.ai);
	    return calculate.mostValuableTile();
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath updateCurrentPath() throws StopRequestException
	{	
		ai.checkInterruption();
		PathCalculation pathOperation = new PathCalculation(this.ai);
		AiPath path = pathOperation.bestPath(this.ai.getZone().getOwnHero(), this.updateCurrentDestination());
		return path;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction updateCurrentDirection() throws StopRequestException
	{	ai.checkInterruption();
	AgentOperation agentOp = new AgentOperation(this.ai);
	TileCalculation calculate = new TileCalculation(this.ai);
	AiPath currentPath = this.updateCurrentPath();
	PathCalculation pathCalculation = new PathCalculation(this.ai);
	AiLocation ourLocation = new AiLocation(this.ai.getZone().getOwnHero());
	if(agentOp.agentInDanger())
	{
		// then our agent is in a dangerous tile.
		AiTile safestTile = calculate.closestAndSafestTile(this.ai.getZone().getOwnHero().getTile());
		if(calculate.isDangerous(safestTile))
		{
			return this.ai.getZone().getDirection(ourLocation, currentPath.getLocation(1));
		}
		else
		{
			currentPath = pathCalculation.bestPath(this.ai.getZone().getOwnHero(), safestTile);
			return this.ai.getZone().getDirection(ourLocation,currentPath.getLocation(1));
		}
		
	}
	else
	{
		if(calculate.isThereBomb(this.ai.getZone().getOwnHero().getTile()))
		{
			return this.ai.getZone().getDirection(ourLocation,currentPath.getLocation(1));
		}
		else
		{
			if(calculate.mostValuableTile().equals(this.ai.getZone().getOwnHero().getTile()))
				return Direction.NONE;
			else if(currentPath == null)
				return Direction.NONE;
			else
				return this.ai.getZone().getDirection(ourLocation, currentPath.getLocation(1));
		}
	}
	
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		super.updateOutput();
	}
}
