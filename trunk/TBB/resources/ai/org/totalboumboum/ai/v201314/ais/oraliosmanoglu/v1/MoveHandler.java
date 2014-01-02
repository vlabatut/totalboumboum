package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v1;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TileHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 * Classe gérant le déplacement de l'agent. En particulier, elle implémente la méthode considerMoving de 
 l'algorithme général. Cette méthode fait appel à trois méthodes pour réaliser son traitement : 
 processCurrentDestination(), processCurrentPath() et processCurrentDirection(). Ces trois méthodes 
 doivent être surchargées. 
Ces trois méthodes permettent chacune de mettre à jour une variable : 
	- currentDestination : la case de destination courante ; 
	- currentPath : le chemin courant (pour aller à la destination courante) ; 
	- currentDirection : la direction courante (qui dépend directement du chemin courant). Ces variables 
	 sont notamment utilisées lors du traitement (méthode processCurrentDestination()) et de l'affichage 
	 (méthode updateOutput()).
 */
public class MoveHandler extends AiMoveHandler<Agent>
{	
	/**
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected MoveHandler(Agent ai)
    {	super(ai);
		ai.checkInterruption();
		
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		CostCalculator costCalculator = new TileCostCalculator(ai);
		HeuristicCalculator heuristicCalculator = new TileHeuristicCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		astar = new Astar(ai, ownHero, costCalculator, heuristicCalculator, successorCalculator);
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** A titre d'exemple, je stocke la zone de jeu, car on en a souvent besoin */
	private AiZone zone = null; 
	/** A titre d'exemple, je stocke le sprite controlé par cet agent, car on en a aussi souvent besoin */
	private AiHero ownHero = null; 
	/** Il est nécessaire de stocker l'objet Astar, si on ne veut pas devoir le re-créer à chaque itération */
	private Astar astar = null;
	
	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Calcule l'objectif courant de l'agent, c'est à dire la case dans laquelle il veut aller. 
Ce calcul dépend devrait dépendre au moins des valeurs de préférence déjà calculées, et éventuellement 
 d'autres calculs supplémentaires. 
Le résultat de ce calcul est utilisé pour automatiquement mettre à jour une variable interne, accessible 
 ensuite via la méthode getCurrentDestination().
	 */
	protected AiTile processCurrentDestination()
	{	ai.checkInterruption();
		AiTile result = null;
		
		AiPreferenceHandler<Agent> preferenceHandler = ai.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler.getPreferencesByValue();
		int minPref = Collections.min(preferences.keySet());	// ATTENTION : ici il faudrait tester qu'il y a au moins une valeur dans la map (sinon : NullPointerException !)
		List<AiTile> tiles = preferences.get(minPref);			// on récupère la liste de cases qui ont la meilleure préférence
		result = tiles.get(0);									// on prend la première de la liste (arbitrairement)
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Calcule le chemin courant de l'agent, c'est à dire la séquence de cases à parcourir pour atteindre 
 (directement ou indirectement) la case objectif. 
Ce traitement devrait a priori faire usage des méthodes et classes de l'API permettant de rechercher des 
 chemins. 
Le résultat de ce calcul est utilisé pour automatiquement mettre à jour une variable interne, accessible 
 ensuite via la méthode getCurrentPath(). Pour calculer ce chemin, vous avez, bien entendu, besoin de la 
 valeur renvoyée par getCurrentDestination().
	 */
	protected AiPath processCurrentPath()
	{	ai.checkInterruption();
		AiPath result = null;
		AiLocation startLocation = new AiLocation(ownHero);
		
		
		
		AiTile endTile = getCurrentDestination();		
		try
		{	// on doit OBLIGATOIREMENT exécuter astar dans un bloc try/catch
			result = astar.startProcess(startLocation,endTile);
		}
		catch (LimitReachedException e)
		{
			result = new AiPath();
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Calcule la direction courante suivie par l'agent. 
Ce traitement devrait a priori dépendre du chemin courant, et éventuellement d'autres informations. Si 
 votre méthode renvoie null, alors l'agent interprétera cette valeur comme Direction.NONE et ne se 
 déplacera donc pas. 
Le résultat de ce calcul est utilisé pour automatiquement mettre à jour une variable interne, accessible 
 ensuite via la méthode getCurrentDirection(). Pour calculer ce chemin, vous avez, bien entendu, besoin 
 de la valeur renvoyée par la méthode getCurrentPath(), voire aussi par la méthode 
 getCurrentDestination().
	 */
	protected Direction processCurrentDirection()
	{	ai.checkInterruption();
		Direction result = Direction.NONE;
		
		AiPath path = getCurrentPath();
		if(path==null || path.getLength()<2)		// cas où le chemin est vide, ou bien ne contient que la case courante
			result = Direction.NONE;
		else
		{	AiLocation nextLocation = path.getLocation(1);		// un chemin est une séquence de AiLocation (position en pixel), chaque AiLocation contient la AiTile correspondant à la position.
			AiTile nextTile = nextLocation.getTile();
			AiTile currentTile = ownHero.getTile();
			result = zone.getDirection(currentTile, nextTile);	// ici, j'utilise une méthode de l'API pour calculer la direction pour aller de la 1ère vers la 2nde case
		}
		result=Direction.NONE;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Met à jour les sorties graphiques de l'agent en considérant les données de ce gestionnaire. 
Ici, on représente la case de destination courante en la coloriant, ainsi que le chemin courant, représenté 
 par une ligne. La couleur représente le mode : bleu pour AiMode.COLLECTING et rouge pour 
 AiMode.ATTACKING. 
Cette méthode peut être surchargée si vous voulez afficher les informations différemment, ou d'autres 
 informations. A noter que cette méthode n'est pas appelée automatiquement : elle doit l'être par 
 ArtificialIntelligence.updateOutput() si vous désirez l'utiliser. 
	 */
	public void updateOutput()
	{	ai.checkInterruption();
		
		
		super.updateOutput();
		
		
	}
}
