package org.totalboumboum.ai.v201213.ais.oralozugur.v4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201213.adapter.data.AiTile;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201213.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. Cf. la
 * documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<OralOzugur> {
	/**
	 * } Range's bottom limit, used in determining the potentially dangerous
	 * tiles on bomb put.
	 */
	private final int RANGE_BOTTOM_LIMIT = 0;

	/**
	 * Distance's bottom limit, if distance drops to this limit, the fire's edge
	 * is reached in getDangerousTilesOnBombPut().
	 */
	private final int DISTANCE_BOTTOM_LIMIT = 0;
	/** Utility limit for bombing in attack mode */
	private final int BOMBING_ATTACK_ULIMIT = 12;
	/** Represant la case inutile de poser une bombe */
	private static final float ATTACKING_SAFE_TILE = 15;
	/** Le temps dont on besoin pour échapper une explosion chainée */
	private static final long CHAINREACTION_MINIMUM_TIME = 1000;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected BombHandler(OralOzugur ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException {
		ai.checkInterruption();

		AiHero ourHero = ai.getZone().getOwnHero();
		AiTile currentTile = ai.getZone().getOwnHero().getTile();
		Map<AiTile, Float>uMap=this.ai.getUtilityHandler().getUtilitiesByTile();
		float currentTileUtility=0;
		if(!uMap.isEmpty())
		 currentTileUtility= ai.getUtilityHandler()
				.getUtilitiesByTile().get(currentTile);
		
	
		if ((ourHero.getBombNumberMax() - ourHero.getBombNumberCurrent()) != 0
				&& (currentTile.getBombs().isEmpty())) {
			if(this.ai.safe_tile_count==1){//NOWHERE TO GO
				
				
				if(currentTileUtility==17|| currentTileUtility==19)
				{
					for(AiTile tile:ourHero.getTile().getNeighbors())
					{
						this.ai.checkInterruption();
						if(this.ai.customIsCrossableBy(tile)){
						if(this.ai.chainReactionTime(tile)<CHAINREACTION_MINIMUM_TIME)
						{
							return true;
						}
						}
					}
				}
			}
			
			if(this.ai.shield==true){//SHIELDING FROM KILLLER BLAST
				if(this.ai.objective!=null)
				{
					if(currentTile.equals(this.ai.objective))
					{
						this.ai.shield=false;
					return true;
					}
				}
				
			} else {
				
				
				if (canReachSafetyAstar(ourHero) && (!this.ai.shield)) {
					
					
					
					if (!heroMovingInCorridorToEnemy(ourHero)) {
						
						AiTile BiggestTile = ai.getBiggestTile();
						float biggestTileUtility=0;
						if(!uMap.isEmpty())
						biggestTileUtility = ai.getUtilityHandler()
								.getUtilitiesByTile().get(BiggestTile);
						if (ai.getModeHandler().getMode()
								.equals(AiMode.ATTACKING)) {

							

							if ((currentTileUtility == biggestTileUtility)
									&& (currentTileUtility > BOMBING_ATTACK_ULIMIT)
									&& (currentTileUtility != ATTACKING_SAFE_TILE)) {
								return true;
							} else if (this.ai.chainReactionTime(currentTile) > CHAINREACTION_MINIMUM_TIME//CHAINING
									&& (currentTileUtility >= BOMBING_ATTACK_ULIMIT)) {
								return true;
							} else
								return false;

						} else {
							// MODE COLLECTE

							if (ai.getPeutTuerEnnemiofTile(currentTile) == 2) {
								return true;

							} else {
								int nbMurDetrui = ai
										.getNbMurDetruitofTile(currentTile);
								if (currentTile.equals(this.ai.objective)) {
									if (nbMurDetrui > 0) {
										return true;
									} else
										return false;
								} else if (nbMurDetrui > 1) {
									return true;
								}

							}

						}
					}
				}
				
				
			}
		}
		return false;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException {
		ai.checkInterruption();

	}

	// FUNCTIONS
	/**
	 * Checks a hero's danger situation. <br />
	 * (TESTED, WORKS)
	 * 
	 * @param givenHero
	 *            The hero to be checked.
	 * @return If given hero is in danger (in a blast range or in a flame) or
	 *         not.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean isHeroInDanger(AiHero givenHero)
			throws StopRequestException {
		ai.checkInterruption();
		if (this.ai.dangerList.contains(givenHero.getTile()))
			return true;
		return false;
	}

	/**
	 * Controle si on a au moins 2 cases a echapper en securite
	 * 
	 * @param givenHero
	 *            Le personage à tester
	 * @return vrai ssi il existe 2 case à échapper
	 * @throws StopRequestException
	 * 		description manquante !	
	 */
	public boolean canReachSafetyAstar(AiHero givenHero)
			throws StopRequestException {
		ai.checkInterruption();

		ArrayList<AiTile> dangerousTilesOnBombing = getDangerousTilesOnBombPut(
				givenHero.getTile(), givenHero.getBombRange());
		CostCalculator ccalculator = new TimeCostCalculator(this.ai, givenHero);
		HeuristicCalculator hcalculator = new TimeHeuristicCalculator(this.ai,
				givenHero);

		SuccessorCalculator scalculator = new TimePartialSuccessorCalculator(
				this.ai, TimePartialSuccessorCalculator.MODE_NOTREE);
		Astar astar = new Astar(ai, givenHero, ccalculator, hcalculator,
				scalculator);
		AiLocation location = new AiLocation(givenHero);
		AiPath path = new AiPath();

		double explosion_time = this.ai.bombExplosionTime(givenHero.getTile());
		for (AiTile tile : this.ai.accessibleTiles) {
			ai.checkInterruption();
			if (!this.ai.dangerList.contains(tile)
					&& !(dangerousTilesOnBombing.contains(tile))) {
				try {
					path = astar.startProcess(location, tile);

				} catch (LimitReachedException e) {

					return false;
				}

				if (path != null) {
					int distance = astar.getTreeHeight();

					long x = getTotalPauseTime(path.getPauses());
					double limit = givenHero.getWalkingSpeed()
							* (explosion_time / 1000 - x);

					if ((path.getFirstLocation() != null)) {
						if ((distance * tile.getSize()) < limit)
							return true;
					}
				}

			}
		}
		return false;
	}

	/**
	 * 
	 * Function calculating if our hero can reach a safe tile from the moment
	 * given using Dijkstra algorithm for path search
	 * 
	 * @param givenHero
	 *            Hero to calculate
	 * @return true if the hero can reach a safe tile time
	 * @throws StopRequestException
	 * 		description manquante !	
	 */
	public boolean canReachSafetyDijkstra(AiHero givenHero)
			throws StopRequestException {
		ai.checkInterruption();

		ArrayList<AiTile> dangerousTilesOnBombing = getDangerousTilesOnBombPut(
				givenHero.getTile(), givenHero.getBombRange());
		ApproximateCostCalculator costCalculator = new ApproximateCostCalculator(
				ai, givenHero);
		ApproximateSuccessorCalculator successorCalculator = new ApproximateSuccessorCalculator(
				ai);

		Dijkstra dijkstra = new Dijkstra(ai, givenHero, costCalculator,
				successorCalculator);
		AiLocation location = new AiLocation(givenHero);
		Map<AiTile, AiSearchNode> map;

		try {
			map = dijkstra.startProcess(location);
		} catch (LimitReachedException e) {
			return false;
		}

		double explosionTime = this.ai.bombExplosionTime(givenHero.getTile());
		for (AiTile tile : this.ai.accessibleTiles) {
			ai.checkInterruption();
			if (!this.ai.dangerList.contains(tile)
					&& (!dangerousTilesOnBombing.contains(tile))) {
				int x = map.get(tile).getDepth();
				double z = givenHero.getWalkingSpeed();
				double limit = z * (explosionTime - 0.5) / 1000;
				if (x * tile.getSize() < limit) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Calculates the total pause time from a given list of times
	 * 
	 * @param pauses
	 *            List to calculate total pause time
	 * @return The total pause time
	 * @throws StopRequestException
	 * 		description manquante !	
	 */
	public long getTotalPauseTime(List<Long> pauses)
			throws StopRequestException {
		ai.checkInterruption();
		long result = 0;
		if (pauses != null)
			for (int i = 0; i < pauses.size(); i++) {
				ai.checkInterruption();
				result += pauses.get(i);
			}
		return result;

	}

	/**
	 * Retourne un ArrayList qui contient les cases de locations.
	 * 
	 * @param locations
	 * 		description manquante !	
	 * @return ArrayList qui contient les cases de locations.
	 * @throws StopRequestException
	 * 		description manquante !	
	 */
	public ArrayList<AiTile> locationListToTileArrayList(
			List<AiLocation> locations) throws StopRequestException {
		ai.checkInterruption();
		ArrayList<AiTile> tiles = new ArrayList<AiTile>();

		for (AiLocation location : locations) {
			ai.checkInterruption();
			if (location.getTile() != null)
				tiles.add(location.getTile());
		}

		return tiles;

	}

	/**
	 * Checks if the given hero can reach a safe tile if he puts a bomb to his
	 * tile. <br />
	 * (TESTED, WORKS)
	 * 
	 * @param givenHero
	 *            The hero to process.
	 * @return If given hero can access a safe tile or not.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean canReachSafety(AiHero givenHero)
			throws StopRequestException {

		ai.checkInterruption();
		ArrayList<AiTile> dangerousTilesOnBombing = getDangerousTilesOnBombPut(
				givenHero.getTile(), givenHero.getBombRange());
		for (AiTile currentTile : this.ai.accessibleTiles) {
			ai.checkInterruption();
			if (!this.ai.dangerList.contains(currentTile)) {

				if (!(dangerousTilesOnBombing.contains(currentTile))) {

					if (givenHero.getTile().getCol() == currentTile.getCol()) {
						if (Math.abs(givenHero.getPosY()
								- currentTile.getPosY())
								* currentTile.getSize() < this.ai
								.bombExplosionTime(givenHero.getTile())
								* (givenHero.getWalkingSpeed() / 1000)) {
							return true;
						}

					} else if (givenHero.getRow() == currentTile.getRow()) {
						if (Math.abs(givenHero.getPosX()
								- currentTile.getPosX())
								* currentTile.getSize() < this.ai
								.bombExplosionTime(givenHero.getTile())
								* (givenHero.getWalkingSpeed() / 1000)) {
							return true;

						}

					} else
						return true;

				}
			}
		}
		return false;
	}

	/**
	 * Populates a list of tiles which will become dangerous if a bomb is put on
	 * given tile. <br/>
	 * (TESTED, WORKS) <br/>
	 * At the beginning of the game, the ranges of the heroes can be
	 * uninitialized. So, be careful when giving range parameter from a hero's
	 * range. <br/>
	 * If range is 0, this method will return an empty list.
	 * 
	 * @param givenTile
	 *            The tile which will contain the potential bomb.
	 * @param range
	 *            The explosion range of the potential bomb.
	 * @return The list of potentially dangerous tiles on bomb put.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public ArrayList<AiTile> getDangerousTilesOnBombPut(AiTile givenTile,
			int range) throws StopRequestException {
		ai.checkInterruption();
		ArrayList<AiTile> dangerousTilesOnBombPut = new ArrayList<AiTile>();
		if (givenTile.isCrossableBy(ai.getZone().getOwnHero())
				&& (range > RANGE_BOTTOM_LIMIT)) {
			dangerousTilesOnBombPut.add(givenTile);

			AiTile currentTile = givenTile.getNeighbor(Direction.LEFT);
			int distance = range;
			while (currentTile.isCrossableBy(ai.getZone().getOwnHero())
					&& (distance > DISTANCE_BOTTOM_LIMIT)) {
				ai.checkInterruption();
				dangerousTilesOnBombPut.add(currentTile);
				if (!currentTile.getItems().isEmpty())
					break;
				currentTile = currentTile.getNeighbor(Direction.LEFT);
				distance--;
			}

			currentTile = givenTile.getNeighbor(Direction.RIGHT);
			distance = range;
			while (currentTile.isCrossableBy(ai.getZone().getOwnHero())
					&& (distance > DISTANCE_BOTTOM_LIMIT)) {
				ai.checkInterruption();
				dangerousTilesOnBombPut.add(currentTile);
				if (!currentTile.getItems().isEmpty())
					break;
				currentTile = currentTile.getNeighbor(Direction.RIGHT);
				distance--;
			}

			currentTile = givenTile.getNeighbor(Direction.UP);
			distance = range;
			while (currentTile.isCrossableBy(ai.getZone().getOwnHero())
					&& (distance > DISTANCE_BOTTOM_LIMIT)) {
				ai.checkInterruption();
				dangerousTilesOnBombPut.add(currentTile);
				if (!currentTile.getItems().isEmpty())
					break;
				currentTile = currentTile.getNeighbor(Direction.UP);
				distance--;
			}

			currentTile = givenTile.getNeighbor(Direction.DOWN);
			distance = range;
			while (currentTile.isCrossableBy(ai.getZone().getOwnHero())
					&& (distance > DISTANCE_BOTTOM_LIMIT)) {
				ai.checkInterruption();
				dangerousTilesOnBombPut.add(currentTile);
				if (!currentTile.getItems().isEmpty())
					break;
				currentTile = currentTile.getNeighbor(Direction.DOWN);
				distance--;
			}
		}

		return dangerousTilesOnBombPut;
	}

	/**Method pour trouver s'i on deplace vers un adversaire dans un corridor
	 * @param givenHero
	 * 		description manquante !	
	 * @return vrai ssi il existe un ennemi dans notre direction de mouvement et s'il n'existe pas de jonction
	 * @throws StopRequestException
	 * 		description manquante !	
	 */
	public boolean heroMovingInCorridorToEnemy(AiHero givenHero) throws StopRequestException{
		this.ai.checkInterruption();
		if(this.ai.enemy_accessible)
		{
			if(directionConteinsHeros(this.ai.last_direction, givenHero))
			{				
					return HeroIsInCorridor(givenHero);
					
						
					
				
			}
		}

		return false;
	}
	/** Utilisé pour determiner s'il existe un adversaire dans un direction et dans notre portée+2 
	 * @param direction
	 * 		description manquante !	
	 * @param givenHero
	 * 		description manquante !	
	 * @return vrai ssi il existe un ennemi qui assure ces conditions
	 * @throws StopRequestException
	 * 		description manquante !	
	 */
	public boolean directionConteinsHeros(Direction direction,AiHero givenHero) throws StopRequestException
	{
		this.ai.checkInterruption();
		AiTile tile=givenHero.getTile();
		if(direction!=null)
		{
		for(int i=0;i<givenHero.getBombRange()+2;i++)
		{
			this.ai.checkInterruption();
			tile=tile.getNeighbor(direction);
			if(!tile.getHeroes().isEmpty())
				return true;
			if(!tile.getBombs().isEmpty())
				return false;
			if(!tile.getBlocks().isEmpty())
				return false;
		}
		}
		return false;
	}
	/**Method utilisé pour savoir si l'agent donné continue sa mouvement dans corridor sans jonctions.
	 * @param givenHero
	 * 		description manquante !	
	 * @return vrai ssi il assure les condition expliqué au-dessus
	 * @throws StopRequestException
	 * 		description manquante !	
	 */
	private boolean HeroIsInCorridor(AiHero givenHero) throws StopRequestException{
		this.ai.checkInterruption();
		
		AiTile tile=givenHero.getTile();
		Direction[] directions=getPerpendicularDirections(this.ai.last_direction);
		int counter=givenHero.getBombRange()+1;
		if(this.ai.simpleTileDistance(this.ai.nearest_enemy.getTile(), givenHero.getTile())==1)
		{
			return true;
		}
		if(directions[0]!=null || directions[1]!=null){
			tile=tile.getNeighbor(this.ai.last_direction);
		while(counter>0)
		{
			
			this.ai.checkInterruption();
			if(this.ai.customIsCrossableBy(tile.getNeighbor(directions[0])))
			{
				if(tile.getNeighbor(directions[0]).getHeroes().isEmpty())
				return false;
			}
			else if(this.ai.customIsCrossableBy(tile.getNeighbor(directions[1])))
			{
				if(tile.getNeighbor(directions[1]).getHeroes().isEmpty())
				return false;
			}
			tile=tile.getNeighbor(this.ai.last_direction);
			if(!this.ai.customIsCrossableBy(tile))
			{
				return false;
			}
			else if(!tile.getHeroes().isEmpty())
			{
				return true;
			}
			counter--;
			}
			
			
		}
		else return false;
		return true;
	}
	/**
	 * 
	 * @param direction
	 * 		description manquante !	
	 * @return Les direction orthogonal à un direction donné.Donc cette chaine est toujour  longueur de deux.
	 * @throws StopRequestException
	 * 		description manquante !	
	 */
	public Direction[] getPerpendicularDirections(Direction direction) throws StopRequestException{
		this.ai.checkInterruption();
		Direction[] result=new Direction[2];
		result[1]=null;
		result[0]=null;
		if(direction==Direction.UP||direction==Direction.DOWN)
		{
			
			result[0]=Direction.RIGHT;
			result[1]=Direction.LEFT;
		}
		else if(direction==Direction.LEFT|| direction==Direction.RIGHT)
		{
			result[0]=Direction.UP;
			result[1]=Direction.DOWN;
		}
		
		return result;
			
			
		
		
	}
	/**Method pour trouver si un jouer s'est trouve dans quel direction selon un autre jouer.
	 * @param hero
	 * 		description manquante !	
	 * @param enemy
	 * 		description manquante !	
	 * @return Les direction de base donc up,down,left,right. Direction.None s'il ne sont pas dans un direction de base.
	 * @throws StopRequestException
	 * 		description manquante !	
	 */
	public Direction getSimpleDirection(AiHero hero,AiHero enemy)  throws StopRequestException
	{
		this.ai.checkInterruption();
		Direction result=Direction.NONE;
		if(hero.getRow()==enemy.getRow())
		{
			if(hero.getCol()>enemy.getCol())
			{
				result=Direction.LEFT;
			}
			else if(hero.getCol()<enemy.getCol()){
				result=Direction.RIGHT;
			}
				
		}
		else if(hero.getCol()==enemy.getCol())
		{
			if(hero.getRow()>enemy.getRow())
			{
				result=Direction.UP;
			}
			else if(hero.getRow()<enemy.getRow()){
				result=Direction.DOWN;
			}
			
		}
		
		return result;
	}

}
