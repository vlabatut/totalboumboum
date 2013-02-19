package org.totalboumboum.ai.v201213.ais.besnilikangal.v3;

import java.util.Iterator;

import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;

/**
 * Cette class contient des methodes liée aux operation des chemins.
 * 
 * @author Doruk Kangal
 * @author Mustafa Besnili
 */
@SuppressWarnings("deprecation")
public class PathOperation
{
	/** Notre IA */
	private BesniliKangal ai;
	/** Le personnage contrôlé par l'agent */
	private AiHero ownHero;
	/** L'objet A* utilisé pour le calcul des chemins directs */
	private Astar astarPrecise = null;
	/** L'objet A* utilisé pour le calcul des chemins indirects */
	private Astar astarApproximation = null;
	/** Un cout supplementaire pour un ennemi */
	private final static int ASTAR_COST = 5000;
	/** Un cout supplementaire pour un malus */
	private final static int MALUS_COST = 5000 ;

	/**
	 * The AI doivent etre passé
	 * 
	 * @param ai
	 * 		?	
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public PathOperation( BesniliKangal ai ) throws StopRequestException
	{
		ai.checkInterruption();
		ownHero = ai.ownHero;

		// astar précis
		{
			CostCalculator costCalculator = new TimeCostCalculator( ai, ownHero );
			costCalculator.setOpponentCost( ASTAR_COST );
			costCalculator.setMalusCost( MALUS_COST );
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator( ai, ownHero );
			SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator( ai, TimePartialSuccessorCalculator.MODE_NOTREE );
			astarPrecise = new Astar( ai, ownHero, costCalculator, heuristicCalculator, successorCalculator );
		}
		// astar approximation
		{
			CostCalculator costCalculator = new ApproximateCostCalculator( ai, ownHero );
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator( ai, ownHero );
			SuccessorCalculator successorCalculator = new ApproximateSuccessorCalculator( ai );
			astarApproximation = new Astar( ai, ownHero, costCalculator, heuristicCalculator, successorCalculator );
		}
		this.ai = ai;
	}

	/**
	 * Utilise A* method pour calculer la chemin plus court d'un case dans
	 * laquelle notre IA se trouvent et une case de la destination.
	 * 
	 * @param targetTile
	 *            Case de destination.
	 * @return La chemin plus courte
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiPath getShortestPathToAccessibleTile( AiTile targetTile ) throws StopRequestException
	{
		ai.checkInterruption();
		if ( targetTile != ownHero.getTile() )
		{
			AiLocation startLocation = new AiLocation( ownHero );
			try
			{
				return astarPrecise.startProcess( startLocation, targetTile );
			}
			catch ( LimitReachedException e )
			{
				// e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Trouve une chemin qui n'est pas accessible par notre IA.
	 * 
	 * @param targetTile
	 * 		?	
	 * @return AiPath s'il en a 
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiPath getShortestPathToInaccessibleTile( AiTile targetTile ) throws StopRequestException
	{
		ai.checkInterruption();
		if ( targetTile != ownHero.getTile() )
		{
			AiLocation startLocation = new AiLocation( ownHero );
			try
			{
				return astarApproximation.startProcess( startLocation, targetTile );
			}
			catch ( LimitReachedException e )
			{
				// e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Renvoyer la prochaine case dans notre chemin
	 * 
	 * @param currentPath
	 * 		information manquante !?	
	 * @param givenTile
	 * 		information manquante !?	
	 * @return AiTile
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiTile getNextTileOnPath( AiPath currentPath, AiTile givenTile ) throws StopRequestException
	{
		ai.checkInterruption();
		Iterator<AiLocation> itr = currentPath.getLocations().iterator();
		boolean isCurrentTile = false;
		while ( itr.hasNext() && !isCurrentTile )
		{
			ai.checkInterruption();
			isCurrentTile = ( givenTile == itr.next().getTile() );
		}
		return ( itr.hasNext() ) ? itr.next().getTile() : null;
	}

	/**
	 * Pour une case donnée,determine si notre chemin contient cette case.
	 * 
	 * @param currentTile
	 * 		information manquante !?	
	 * @return boolean
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean isCurrentPathContainsCurrentTile( AiTile currentTile ) throws StopRequestException
	{
		ai.checkInterruption();
		AiPath currentPath = ai.getMoveHandler().getCurrentPath();
		if ( currentPath != null )
		{
			for ( AiLocation location : currentPath.getLocations() )
			{
				ai.checkInterruption();
				if ( location.getTile() == currentTile )
					return true;
			}
		}
		return false;
	}
}
