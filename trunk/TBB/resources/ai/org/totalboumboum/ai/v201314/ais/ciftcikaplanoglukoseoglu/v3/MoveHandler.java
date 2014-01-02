package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3.criterion.Distance;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3.criterion.ExitNumber;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3.criterion.Threat;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TileHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * Classe gérant le déplacement de l'agent. Cf. la documentation de
 * {@link AiMoveHandler} pour plus de détails.
 * 
 * 
 * 
 * @author Özkan Çiftçi
 * @author Akın Kaplanoğlu
 * @author Erol Köseoğlu
 */
public class MoveHandler extends AiMoveHandler<Agent> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected MoveHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = true;

		// dans cette classe, on aura généralement besoin d'un objet de type
		// Astar.
		// à titre d'exemple, on construit ici un objet Astar très simple (pas
		// forcément très efficace)
		// pour des raisons de rapidité, il est recommandé de créer l'objet
		// Astar une seule fois,
		// et non pas à chaque itération. Cela permet aussi d'éviter certains
		// problèmes de mémoire.

		zone = ai.getZone();
		ownHero = zone.getOwnHero();

		CostCalculator costCalculator = new TileCostCalculator(ai);
		HeuristicCalculator heuristicCalculator = new TileHeuristicCalculator(
				ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(
				ai);
		costCalculator.setOpponentCost(50000);
		costCalculator.setMalusCost(50000);
		astar = new Astar(ai, ownHero, costCalculator, heuristicCalculator,
				successorCalculator);

		CostCalculator costCalculator1 = new ApproximateCostCalculator(ai,
				ownHero);
		HeuristicCalculator heuristicCalculator1 = new NoHeuristicCalculator(ai);
		SuccessorCalculator successorCalculator1 = new ApproximateSuccessorCalculator(
				ai);

		costCalculator1.setMalusCost(50000);
		;

		astarApproximation = new Astar(ai, ownHero, costCalculator1,
				heuristicCalculator1, successorCalculator1);
	}

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** A titre d'exemple, je stocke la zone de jeu, car on en a souvent besoin */
	private AiZone zone = ai.getZone();
	/**
	 * A titre d'exemple, je stocke le sprite controlé par cet agent, car on en
	 * a aussi souvent besoin
	 */
	private AiHero ownHero = zone.getOwnHero();
	/**
	 * Il est nécessaire de stocker l'objet Astar, si on ne veut pas devoir le
	 * re-créer à chaque itération
	 */
	private Astar astar = null;

	/**
	 * la valeur booléene qui va nous permettre de poser une bombe pour pouvoir
	 * déblayer le chemin des murs destructibles quand l'adversaire est
	 * innacessible.
	 */
	public boolean futiletorun;
	/**
	 * un nouvel objet Astar.
	 */
	private Astar astarApproximation = null;

	/**
	 * les cases qui vont nous permettre a notre agent de fuir au cas ou il est
	 * sur une case tres menacée.
	 */
	public ArrayList<AiTile> escape;
	/**
	 * L'objet qui nous permet de prendre la valeur du critere threat.
	 */
	Threat th = (Threat) ai.preferenceHandler.getCriterion("THREAT");
	
	/**
	 * L'objet qui nous permet de prendre la valeur du critere distance.
	 */
	Distance d = (Distance) ai.preferenceHandler.getCriterion("DISTANCE");
	
	/**
	 * L'objet qui nous permet de prendre la valeur du critere exit_number.
	 */

	ExitNumber en = (ExitNumber) ai.preferenceHandler
			.getCriterion("EXIT_NUMBER");
	
	/**
	 * L'objet qui nous permet de prendre le map des preferences.
	 */

	Map<AiTile, Integer> preferencese;

	// ///////////////////////////////////////////////////////////////
	// DESTINATION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiTile processCurrentDestination() {
		ai.checkInterruption();
		AiTile result = null;
		zone = ai.getZone();
		

		AiPreferenceHandler<Agent> preferenceHandler = ai
				.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler
				.getPreferencesByValue();
		preferencese = preferenceHandler.getPreferencesByTile();
		int minPref = Collections.min(preferences.keySet());

		List<AiTile> tiles = preferences.get(minPref); // on récupère la liste
														// de cases qui ont la
														// meilleure préférence
		AiTile herotile = zone.getOwnHero().getTile();

		// si la case de destination a la valeur minimale, on garde la case de
		// destination.
		if (preferencese.get(getCurrentDestination()) == minPref) {
			result = getCurrentDestination();

		} else {
			// sinon on prend la case qui a la valeur minimale.
			result = tiles.get(0);
		}

		escape=ai.preferenceHandler.escape;
		ArrayList<AiTile> escape1 = ai.preferenceHandler.escape1;
	



		// si on est sur une case tres menacée
		if (th.fetchValue(herotile) == 2) {

			// on prend les cases a distance proche et non menacée. S'il n'y a
			// aucune
			// case non-menacée proche, on prend une case un peu menacée proche.

			if (!escape.isEmpty()) {
				if (escape.contains(getCurrentDestination()))
					result = getCurrentDestination();
				else
					result = escape.get(0);
			} else if (!escape1.isEmpty()) {
				if (escape1.contains(getCurrentDestination()))
					result = getCurrentDestination();
				else
					result = escape1.get(0);
			}
		}
		
		/*List<AiItem> items=zone.getItems();
		int k=50;
		AiItem it=null;
		if(!items.isEmpty()){
		if(!ai.preferenceHandler.accessibleTiles.contains(ai.preferenceHandler.adversaire)){
			if(th.fetchValue(herotile)==0){
				for(AiItem i:items){
					if(zone.getTileDistance(i.getTile(), ownHero.getTile())<k){
						k=zone.getTileDistance(i.getTile(), ownHero.getTile());
						it=i;
					}
				}
				if(it!=null){
					if(it.getType().isBonus()){
						result=it.getTile();
					}}
				else{
					result=getCurrentDestination();
				}
			}
		}}
		else{
			result=getCurrentDestination();
		}
		*/
		
		
		

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// PATH /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiPath processCurrentPath() {
		ai.checkInterruption();
		AiPath result = null;
		AiLocation startLocation = new AiLocation(ownHero);

		// pour l'exemple, voici la manière la plus simple d'utiliser A*
		// là encore, le but est d'illustrer l'utilisation de l'API, et non pas
		// la conception d'un agent
		AiTile endTile = getCurrentDestination(); // cette case correspond à
													// celle sélectionnée dans
													// la méthode
													// processCurrentDestination

		try { // on doit OBLIGATOIREMENT exécuter astar dans un bloc try/catch
				// si l'adversaire est accessible.
			if (ai.preferenceHandler.flag == false)
				result = astar.startProcess(startLocation, endTile);
			else
				result = astarApproximation
						.startProcess(startLocation, endTile);

		} catch (LimitReachedException e) {
			// pour comprendre si on est dans catch
			print("YALLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLAAAAAAAAAAAAAAAAAAHhh");
			
			// e.printStackTrace(); // il ne faut PAS afficher la trace (cf. le
			// manuel de l'API, la partie sur les contraintes de programmation)
			// l'exception est levée seulement si astar atteint certaines
			// limites avant de trouver un chemin
			// cela ne veut donc pas dire qu'il y a eu une erreur, ou que le
			// chemin n'existe pas.
			// ça veut seulement dire que ça prend trop de temps/mémoire de
			// trouver ce chemin
			// dans ce cas là, il faudrait, dans ce bloc catch, effectuer un
			// traitement spécial pour résoudre
			// ce problème. Ici, pour l'exemple, on se contente de construire un
			// chemin vide (ce qui n'est pas très malin)
			try {
				result = astar.startProcess(startLocation, escape.get(0));
			} catch (LimitReachedException e1) {
				print("kel");
				
			}

		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// DIRECTION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Direction processCurrentDirection() {
		ai.checkInterruption();
		Direction result = Direction.NONE;

		// dans cette méthode, on doit utiliser les calculs précédents
		// (accessibles via
		// getCurrentDestination et getCurrentPath) pour choisir la prochaine
		// direction
		// à suivre pour notre agent. à titre d'exemple, on se content ici de
		// prendre
		// la direction de la case suivante, ce qui n'est pas forcément la
		// meilleure chose à faire.
		// là encore, il s'agit d'un exemple de programmation, et non d'un
		// exemple de conception.
		AiPath path = getCurrentPath();
		if (path == null || path.getLength() < 2) // cas où le chemin est vide,
													// ou bien ne contient que
													// la case courante
			result = Direction.NONE;
		else {
			AiLocation nextLocation = path.getLocation(1);
			AiTile nextTile = nextLocation.getTile();
			AiHero adversaire = ai.preferenceHandler.adversaire;
			AiTile currentTile = ownHero.getTile();
			futiletorun = false;
			List<AiItem> malus = nextTile.getItems();

			result = zone.getDirection(currentTile, nextTile);
 
		
				
			if (getCurrentPath().getLength() > 2
					&& en.fetchValue(getCurrentPath().getLocation(1).getTile()) < 2
			 		&& zone.getPixelDistance(adversaire.getPosX(),
			 				adversaire.getPosY(),
							getCurrentPath().getLocation(2).getTile()) < zone
							.getPixelDistance(ownHero.getPosX(), ownHero
									.getPosY(), getCurrentPath().getLocation(2)
									.getTile())
					&& !adversaire.getTile().equals(currentTile)
					&& !currentTile.getBombs().isEmpty()&& adversaire.getBombNumberMax() != adversaire
							.getBombNumberCurrent()&& th.fetchValue(currentTile)!=2) {
				
					List<AiTile> dont = currentTile.getNeighbors();
					ArrayList<AiTile> dont1 = new ArrayList<AiTile>();
					dont1.addAll(dont);

					dont1.remove(getCurrentPath().getLocation(1).getTile());
					int a = 33;
					if (dont1.contains(getCurrentDestination())) {
						result = getCurrentDirection();

					} else
						{
						for (AiTile t : dont1) {
							ai.checkInterruption();
							if (preferencese.get(t) < a
									&& t.isCrossableBy(ownHero)&& th.fetchValue(t)<2 && t.getFires().isEmpty()) {
								
								
								
								a = preferencese.get(t);
								
								result = zone.getDirection(currentTile, t); 
							
							}
							
						}
						
				}}
			

			
				
			if (ai.preferenceHandler.flag1) {
				if (!malus.isEmpty())
					if (!malus.get(0).getType().isBonus()) {

						List<AiTile> neighbors = currentTile.getNeighbors();
						for (AiTile t : neighbors) {
							ai.checkInterruption();
							if (t.isCrossableBy(ownHero) && !t.equals(nextTile)
									&& en.fetchValue(t) != 0)
								result = zone.getDirection(currentTile, t);
						}

					}
			} else if (ai.preferenceHandler.flag)
			{if(ai.getZone().getOwnHero().getBombNumberCurrent()>0)
				result=Direction.NONE;
				
				if (!nextTile.getBlocks().isEmpty())
					futiletorun = true;
			if (!malus.isEmpty())
				if (!malus.get(0).getType().isBonus())
					futiletorun = true;
			}
			if ((th.fetchValue(currentTile) < 2 && th.fetchValue(nextTile) == 2)|| !nextTile.getFires().isEmpty())

						result = Direction.NONE;
		}

		// si la prochaine case est tres menacée et notre case ne l'est pas.
					
		
		
		
		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() {
		ai.checkInterruption();

		// ici on se contente de faire le traitement par défaut
		super.updateOutput();

	}
}
