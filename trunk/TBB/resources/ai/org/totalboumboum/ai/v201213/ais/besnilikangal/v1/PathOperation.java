package org.totalboumboum.ai.v201213.ais.besnilikangal.v1;

import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette class contient des methodes liée aux operation des chemins.
 * 
 * @author Doruk Kangal
 * @author Mustafa Besnili
 */
public class PathOperation
{
	/**
	 * Un cout supplementaire pour un ennemi.
	 */
	private final int ASTAR_COST = 1000;

	/** Notre IA */
	private BesniliKangal ai;

	/**
	 * The AI doivent etre passé
	 * 
	 * @param ai
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public PathOperation( BesniliKangal ai ) throws StopRequestException
	{
		ai.checkInterruption();
		this.ai = ai;
	}

	/**
	 * Methode pour trouver la direction et y suivre la chemin calculé par A* de
	 * la case que notre agent se trouvent et une case pour y aller.
	 * 
	 * @param endTile
	 *            La case de la destination
	 * @return direction à aller
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 * 
	 */
	public Direction getNextDirection( AiTile endTile ) throws StopRequestException
	{
		ai.checkInterruption();
		AiTile currentTile = ai.getHero().getTile();
		return ( currentTile == endTile ) ? Direction.NONE : ai.getZone().getDirection( currentTile, endTile );
	}

	/**
	 * Utilise A* method pour calculer la chemin plus court d'un case dans
	 * laquelle notre IA se trouvent et une case de la destination.
	 * 
	 * @param endTile
	 *            Case de destination.
	 * @return La chemin plus courte
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiPath getShortestPath( AiTile endTile ) throws StopRequestException
	{
		ai.checkInterruption();
		AiHero ourHero = ai.getHero();
		if ( endTile == ourHero.getTile() )
			return null;
		CostCalculator costCalculator = new TimeCostCalculator( ai, ourHero );
		costCalculator.setOpponentCost( ASTAR_COST );
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator( ai, ourHero );
		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator( ai, TimePartialSuccessorCalculator.MODE_NOTREE );
		Astar astarPrecise = new Astar( ai, ourHero, costCalculator, heuristicCalculator, successorCalculator );
		AiLocation loc = new AiLocation( ourHero );
		// Shortest path calculation
		try
		{
			return astarPrecise.startProcess( loc, endTile );
		}
		catch ( LimitReachedException e )
		{
			return null;
		}
	}
}
