package org.totalboumboum.ai.v201314.ais.enginserhantipici.v3;


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
import org.totalboumboum.ai.v201314.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class MoveHandler extends AiMoveHandler<Agent>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected MoveHandler(Agent ai)
    {	super(ai);
		ai.checkInterruption();
		setDestructibleWallSearch(false);
		
		// dans cette classe, on aura généralement besoin d'un objet de type Astar.
		// à titre d'exemple, on construit ici un objet Astar très simple (pas forcément très efficace)
		// pour des raisons de rapidité, il est recommandé de créer l'objet Astar une seule fois, 
		// et non pas à chaque itération. Cela permet aussi d'éviter certains problèmes de mémoire.
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		
		CostCalculator costCalculator = new TileCostCalculator(ai);
		HeuristicCalculator heuristicCalculator = new TileHeuristicCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		
		SuccessorCalculator approxScsCalculator = new ApproximateSuccessorCalculator(ai);
		
		astar = new Astar(ai, ownHero, costCalculator, heuristicCalculator, successorCalculator);
		approxAstar = new Astar(ai, ownHero, costCalculator, heuristicCalculator, approxScsCalculator);
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
	//%%%%%%%%%%%
	
	
	/**
	 * Getter for speedAttackFinished
	 * @return boolean variable speedAttackFinished
	 */
	public boolean isSpeedAttackFinished() {
		ai.checkInterruption();
		return speedAttackFinished;
	}
	/**
	 * Setter for boolean variable speedAttackFinished
	 * @param speedAttackFinished boolean variable
	 */
	public void setSpeedAttackFinished(boolean speedAttackFinished) {
		ai.checkInterruption();
		this.speedAttackFinished = speedAttackFinished;
	}
	
	/**
	 * Getter for boolean variable targetAtTheDeadEnd
	 * @return boolean
	 */
	public boolean isTargetAtTheDeadEnd() {
		ai.checkInterruption();
		return targetAtTheDeadEnd;
	}
	/**
	 * Setter for boolean variable targetAtTheDeadEnd
	 * @param targetAtTheDeadEnd boolean value
	 */
	public void setTargetAtTheDeadEnd(boolean targetAtTheDeadEnd) {
		ai.checkInterruption();
		this.targetAtTheDeadEnd = targetAtTheDeadEnd;
	}
	
	
	//%%%%%%%%%%
	/**
	 * A star path algorithm for searching category
	 */
	private Astar approxAstar = null;
	
	
	/**
	 * if there is a possibility of being stucked inside of a tunnel
	 */
	public boolean stuckPossibleInTunnel = false;
	
	/**
	 * to check if a bomb placed for a dead end catergory
	 */
	public static volatile boolean bombPosedForTunnel = false;
	
	/**
	 * if the tile is closed to own hero
	 */
	@SuppressWarnings("unused")
	private boolean tileClosed = false;
	
	/**
	 * to check is destructible wall search is current category
	 */
	public boolean destructibleWallSearch;
	
	/**
	 * to check if speed attack finished
	 */
	public boolean speedAttackFinished;
	
/**
 * if the target in a closed tunnel
 */
	public boolean targetAtTheDeadEnd;
	


	
	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiTile processCurrentDestination()
	{	ai.checkInterruption();
		AiTile result = null;
		
		AiPreferenceHandler<Agent> preferenceHandler = ai.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler.getPreferencesByValue();
		int minPref = Collections.min(preferences.keySet());	// ATTENTION : ici il faudrait tester qu'il y a au moins une valeur dans la map (sinon : NullPointerException !)
		List<AiTile> tiles = preferences.get(minPref);			// on récupère la liste de cases qui ont la meilleure préférence
/*		List<AiTile> atiles = tiles;	
		
		List<AiBomb> bombs = zone.getBombs();
		bombs = new ArrayList<AiBomb>(bombs);
		List<AiTile> blasts = new ArrayList<AiTile>();
		for(AiBomb bomb : bombs){
			ai.checkInterruption();
			blasts.addAll(bomb.getBlast());
		}
		
		for(AiTile tile : atiles){
			ai.checkInterruption();
			if(blasts.contains(tile)){
				tiles.remove(tile);
			}
		}
	*/	
	//	if(tiles.size() == 0) tiles.add(ownHero.getTile());
		
		AiTile destinatedTile = tiles.get(0);
		for(AiTile tile : tiles){
			ai.checkInterruption();
			if(zone.getPixelDistance(ownHero.getPosX(), ownHero.getPosY(), tile) + 0.1 < zone.getPixelDistance(ownHero.getPosX(), ownHero.getPosY(), destinatedTile) ){
				destinatedTile = tile;
			}
		}
/*		
		for(AiTile tile : tiles){
			ai.checkInterruption();
			if(destinatedTile.getPosX()*destinatedTile.getPosY() > tile.getPosX()*tile.getPosY()){
				destinatedTile = tile;
			}
		}
*/
/*		
		for(AiTile tile : tiles){
			ai.checkInterruption();
			if(ai.tileIdentification(tile) < ai.tileIdentification(destinatedTile)){
				destinatedTile = tile;
			}
		}
*/		
		result = destinatedTile;
		
		if (result == null){ //in the case of error of algorithm, instead of having null pointer exception return the tile of own hero 
			result = ai.getZone().getOwnHero().getTile();
		}
		
		if(ai.getZone().getTileDistance(result, ai.getZone().getOwnHero().getTile()) < 2){
			this.tileClosed = true;
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath processCurrentPath()
	{	ai.checkInterruption();
		AiPath result = null;
		AiLocation startLocation = new AiLocation(ownHero);

		AiTile endTile = getCurrentDestination();		// cette case correspond à celle sélectionnée dans la méthode processCurrentDestination
	//	if(!tileClosed){

		if(ai.preferenceHandler.selectedTileType == 6){//category SEARCH
	
			AiLocation fakeLocation = new AiLocation(zone.getOwnHero());
			try
			{	
				result = approxAstar.startProcess(fakeLocation,endTile);
			}
			catch (LimitReachedException e)
			{	//e.printStackTrace();
				result = new AiPath();
			}
		}else if(ai.preferenceHandler.selectedTileType == 8){//SPEED ATTACK	
			result = new AiPath();
			result.addLocation(startLocation);
			AiLocation endLocation = new AiLocation(endTile);
			result.addLocation(endLocation);
		}
		
		
		
	/*	else if(ai.preferenceHandler.selectedTileType == 7){//DEAD END ABS
	
		
			if(stuckPossibleInTunnel || bombPosedForTunnel){
				try
				{	
					result = astar.startProcess(startLocation,ai.tilesInTunnel.get(0));
				}
				catch (LimitReachedException e)
				{	e.printStackTrace();
					result = new AiPath();
				}
			
			}else{
				try
				{	
					result = astar.startProcess(startLocation,endTile);
				}
				catch (LimitReachedException e)
				{	e.printStackTrace();
					result = new AiPath();
				}
			}
			}
			
			
	/*		if(ai.getTilesInTunnel().contains(ownHero.getTile()) && ai.getTilesInTunnel().indexOf(ownHero.getTile()) != 0){
				AiTile target = ai.getTilesInTunnel().get(ai.getTilesInTunnel().indexOf(ownHero.getTile()) - 1);
				for(AiHero enemy : zone.getRemainingOpponents()){
					if(!ai.isATileCompetible(ownHero, enemy, target)){
						stuckPossibleInTunnel = true;
						break;
					}
					//problem var break yap yeni path olustur tunelin basina git
					//degilse astarla devam
				}
			}
		*/	
	/*		if(stuckPossibleInTunnel || bombPosedForTunnel){
				//create a new path from own tile to the beginning of the tunnel
				result = new AiPath();
				result.setLocation(0, startLocation);
				
				int j = 0;
				for(int i = ai.getTilesInTunnel().indexOf(ownHero.getTile()) ; i < 0 ; i-- ){
					AiLocation tempLocation = new AiLocation(ai.getTilesInTunnel().get(i));
					result.setLocation(j, tempLocation);
					j++;
				}
					
			}
		else{
				
				try
				{	
					result = astar.startProcess(startLocation,endTile);
				}
				catch (LimitReachedException e)
				{	e.printStackTrace();
					result = new AiPath();
				}
			}

			}
		*/	
		else{
		
		try
		{
			ai.checkInterruption();
			// on doit OBLIGATOIREMENT exécuter astar dans un bloc try/catch
			result = astar.startProcess(startLocation,endTile);
		}
		catch (LimitReachedException e)
		{	//e.printStackTrace();		// il ne faut PAS afficher la trace (cf. le manuel de l'API, la partie sur les contraintes de programmation)
			// l'exception est levée seulement si astar atteint certaines limites avant de trouver un chemin
			// cela ne veut donc pas dire qu'il y a eu une erreur, ou que le chemin n'existe pas.
			// ça veut seulement dire que ça prend trop de temps/mémoire de trouver ce chemin
			// dans ce cas là, il faudrait, dans ce bloc catch, effectuer un traitement spécial pour résoudre
			// ce problème. Ici, pour l'exemple, on se contente de construire un chemin vide (ce qui n'est pas très malin)
			result = new AiPath();
		}
		}
	/*	if(ai.preferenceHandler.selectedTileType == 7){	
			int diff = ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(),ai.theEnemyInsideTheTunnel.getTile()) - 
					ai.getZone().getOwnHero().getBombRange();
			if(diff == 0){
				for(int i = 0;i < ai.getZone().getOwnHero().getBombRange(); i++)
					result.removeLocation(result.getLastLocation());
				
			}
			
		}*/
		
		
/*		}else{
			result = new AiPath();
			result.setLocation(0, startLocation);
			result.setLocation(1, new AiLocation(endTile));
		}*/
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
//	@SuppressWarnings("static-access")
	@Override
	protected Direction processCurrentDirection()
	{	ai.checkInterruption();
	Direction result = Direction.NONE;
//	List<AiFire> nextFire = new ArrayList<AiFire>();
//	Map<AiTile, List<AiTile>> mapOfDeadEnds = new HashMap<AiTile, List<AiTile>>(ai.getDeadEnds());
	setTargetAtTheDeadEnd(false);
	
	// dans cette méthode, on doit utiliser les calculs précédents (accessibles via
	// getCurrentDestination et getCurrentPath) pour choisir la prochaine direction
	// à suivre pour notre agent. à titre d'exemple, on se content ici de prendre
	// la direction de la case suivante, ce qui n'est pas forcément la meilleure chose à faire.
	// là encore, il s'agit d'un exemple de programmation, et non d'un exemple de conception.
	AiPath path = getCurrentPath();
		if(path==null || path.getLength()<2)		// cas où le chemin est vide, ou bien ne contient que la case courante
			result = Direction.NONE;
	//	else if(stuckPossibleInTunnel || bombPosedForTunnel){
	//		result = zone.getDirection(ownHero.getTile(), ai.getTilesInTunnel().get(0));
	//	}
	else
		{	
		AiLocation nextLocation = path.getLocation(1);		// un chemin est une séquence de AiLocation (position en pixel), chaque AiLocation contient la AiTile correspondant à la position.	
		AiTile nextTile = nextLocation.getTile();
	//	AiTile nextTempTile;
		
/*		if(path.getLocations().size() > 1){
		AiLocation nextTempLocation = path.getLocation(2);	
			nextTempTile = nextTempLocation.getTile();
		}else{
			nextTempTile = nextTile;
		}
*/		
	//	System.out.println("We are in MOVE handler and selected tile type = " + ai.preferenceHandler.selectedTileType);
		if( ai.preferenceHandler.selectedTileType !=1 && ai.DangerDegree(nextTile) == 1  || !nextTile.getFires().isEmpty()){
			
		//	stuckPossibleInTunnel = false;
			result =Direction.NONE;
		
		}else if(!nextTile.getBlocks().isEmpty() && ai.preferenceHandler.selectedTileType == 6 ){//approxAstar
					
				result = Direction.NONE;
					setDestructibleWallSearch(true);
		}
		
		else if(ai.preferenceHandler.selectedTileType == 7){//dead end absolute
			
//			System.out.println("ownhero index in tilesintunnel = " + ai.getTilesInTunnel().indexOf(ownHero.getTile()));
			if(ai.getTilesInTunnel().contains(ownHero.getTile()) && ai.getTilesInTunnel().indexOf(ownHero.getTile()) != 0){
				AiTile target = ai.getTilesInTunnel().get(ai.getTilesInTunnel().indexOf(ownHero.getTile()) - 1);
				for(AiHero enemy : zone.getRemainingOpponents()){
					ai.checkInterruption();
					if(!ai.isATileCompetible(ownHero, enemy, target)){
						stuckPossibleInTunnel = true;
						break;
					}
					//problem var break yap yeni path olustur tunelin basina git
					//degilse astarla devam
				}
			}
			
			//stuckPossibleInTunnel = false;
				AiTile currentTile = ownHero.getTile();
			if(stuckPossibleInTunnel || bombPosedForTunnel){
				result = zone.getDirection(currentTile, ai.getTilesInTunnel().get(0));
			//	stuckPossibleInTunnel = false;
				bombPosedForTunnel = false;
			}else{
		//		stuckPossibleInTunnel = false;
				result = zone.getDirection(currentTile, nextTile);
			}
			
			
		}
	 		
		/*	}else if (ai.preferenceHandler.selectedTileType == 4
					&& nextTempTile.isCrossableBy(ai.getZone().getOwnHero(),false, false, false, true, true, true)){
				result = Direction.NONE;
			}*/
		else{
			
			AiTile currentTile = ownHero.getTile();
			result = zone.getDirection(currentTile, nextTile);
		}
		}
/*		nextFire = nextTile.getFires();
		
		if(!nextFire.isEmpty()){
			result = Direction.NONE;
			}else{
				AiTile currentTile = ownHero.getTile();
				result = zone.getDirection(currentTile, nextTile);	// ici, j'utilise une méthode de l'API pour calculer la direction pour aller de la 1ère vers la 2nde case
			}
		}
*/
return result;
}

	/**
	 * Getter for destructibleWallSearch
	 * @return boolean
	 */
	public boolean isDestructibleWallSearch() {
		ai.checkInterruption();
		return destructibleWallSearch;
	}
	/**
	 * Setter for destructibleWallSearch
	 * @param destructibleWallSearch boolean value
	 */
	public void setDestructibleWallSearch(boolean destructibleWallSearch) {
		ai.checkInterruption();
		this.destructibleWallSearch = destructibleWallSearch;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput()
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();

	}
}
