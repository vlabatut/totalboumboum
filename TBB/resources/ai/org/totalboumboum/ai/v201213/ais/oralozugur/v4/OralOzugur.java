package org.totalboumboum.ai.v201213.ais.oralozugur.v4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.model.full.AiSimBomb;
import org.totalboumboum.ai.v201213.adapter.model.full.AiSimZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe principale de l'agent. Cf. la
 * documentation de {@link ArtificialIntelligence} pour plus de détails.
 * 
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
public class OralOzugur extends ArtificialIntelligence {
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public AiTile objective = null;
	/**Le variable pour pose une bombe pourque une autre bombe ne puisse pas nous tuer */
	public boolean shield=false;
	

	/** Liste de tile accessible */
	public ArrayList<AiTile> accessibleTiles;
	/**Indique que le tile suivant à une malus contagious. */
	//public boolean contagiousTile;
//	public boolean contagiousBombed;
	public boolean safety;
	/**
	 * Liste tile qui sont concernée par mort subite.
	 */
	public ArrayList<AiTile> suddenDeathTiles;
	/**Represant l'ennemi plus proche 
	 * null s'in n'existe pas dans le case accessible */
	public AiHero nearest_enemy;
	/** Il vaut vrai s'il y a un adversaire accessible */
	public boolean enemy_accessible;
	/** Constant utiliser pour initialisation dans chainReactionTime */
	private static final int BOMB_EXPLOSION_CHECK = 100000;
	/**le dernier direction de l'agent */
	public Direction last_direction;
	/**Nombre de case sure*/
	public int safe_tile_count;
	
	/**
	 * On garde ça pour diminuer les operations.
	 */
	public float objective_utility=0;

	/**
	 * getter pour accessibleTiles
	 * 
	 * @return liste de tiles accessible
	 * @throws StopRequestException
	 * */
	public ArrayList<AiTile> getAccessibleTiles() throws StopRequestException {
		checkInterruption();
		return accessibleTiles;
	}

	/**
	 * Utiliser pour determiner le rayon de zone de piege
	 * 
	 */
	private static final int AMBUSH_LIMIT = 4;
	/**
	 * Le temps de faire le control pour mort subite
	 */
	private static final long SUDDEN_DEATH_CONTROL_TIME = 3500;
	/**
	 * Liste de tiles dangereuse
	 */
	public ArrayList<AiTile> dangerList;
	
	/**On l'utilise pour savoir quelle case est plus proche a un ennemi inaccessible*/
	public int best_Wall_Determinator;
	
	/**
	 * Utilisé pour initialiser et mettre au jour best_Wall_determinator;
	 */
	public static final int BEST_WALL_INITIATOR=1000;
	
	/**
	 * La seule case qui va retourner la critere bestwall vrai
	 */
	public AiTile bestWallTile;
	
	/**
	 * Constructeur de la classe OralOzugur
	 */
	public OralOzugur() {
		// active/désactive la sortie texte
		verbose = false;
	}

	@Override
	protected void initOthers() throws StopRequestException {
		checkInterruption();
		this.dangerList = new ArrayList<AiTile>();
		this.suddenDeathTiles = new ArrayList<AiTile>();
		this.enemy_accessible = false;
		this.nearest_enemy = null;
		this.best_Wall_Determinator=BEST_WALL_INITIATOR;
		this.safe_tile_count=0;
		this.last_direction=null;
		
		
//		this.contagiousTile=false;
//		this.contagiousBombed=false;

	}

	// ///////////////////////////////////////////////////////////////
	// PERCEPTS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() throws StopRequestException {
		checkInterruption();

		this.accessibleTiles = new ArrayList<AiTile>();
		this.suddenDeathTiles = getSuddenDeathTiles();
		
	}

	@Override
	protected void updatePercepts() throws StopRequestException {
		checkInterruption();

		this.suddenDeathTiles.clear();
		this.suddenDeathTiles = getSuddenDeathTiles();

		this.accessibleTiles.clear();

		getAccessibleTilesFrom(this.getZone().getOwnHero().getTile());
		
	
		
		

		this.dangerList.clear();
		this.dangerList = getCurrentDangerousTiles();

		this.enemy_accessible = isEnemiesAccessible();

		this.nearest_enemy = getNearestEnemy();
		
		this.safe_tile_count=0;
		this.bestWallTile = getBestWallIfAny();
		//this.best_Wall_Determinator=BEST_WALL_INITIATOR;
	}

	// ///////////////////////////////////////////////////////////////
	// HANDLERS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** gestionnaire chargé de calculer les valeurs d'utilité de l'agent */
	protected UtilityHandler utilityHandler;
	/** gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;

	@Override
	protected void initHandlers() throws StopRequestException {
		checkInterruption();

		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		utilityHandler = new UtilityHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);

	}

	@Override
	protected AiModeHandler<OralOzugur> getModeHandler()
			throws StopRequestException {
		checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<OralOzugur> getUtilityHandler()
			throws StopRequestException {	
		checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<OralOzugur> getBombHandler()
			throws StopRequestException {
		checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<OralOzugur> getMoveHandler()
			throws StopRequestException {
		checkInterruption();
		return moveHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException {
		checkInterruption();

		// ici, par défaut on affiche :
		// les chemins et destinations courants
		moveHandler.updateOutput();
		// les utilités courantes
		utilityHandler.updateOutput();

		// cf. la Javadoc dans ArtificialIntelligence pour une description de la
		// méthode
	}

	// OUT METHODS
	/**
	 * Le recherhe de la case ou l'utilité est maximal
	 * 
	 * 
	 * 
	 * @return AiTile -> case qui a l'utilité maximal
	 * @throws StopRequestException
	 */
	public AiTile getBiggestTile() throws StopRequestException {
		this.checkInterruption();
		Map<AiTile, Float> hashmap;
		hashmap = getUtilityHandler().getUtilitiesByTile();

		List<AiTile> biggestTiles = new ArrayList<AiTile>();

		AiTile biggestTile = null;
		float value = -10;
		int distance = 1000;
		if (!hashmap.isEmpty()) {
			for (AiTile currentTile : hashmap.keySet()) {
				this.checkInterruption();

				if (hashmap.get(currentTile) > value) {

					value = hashmap.get(currentTile);
					biggestTile = currentTile;
					biggestTiles.clear();
					biggestTiles.add(currentTile);
					distance = getZone().getTileDistance(
							getZone().getOwnHero().getTile(), currentTile);
				} else if (hashmap.get(currentTile) == value) {
					int distance2 = getZone().getTileDistance(
							getZone().getOwnHero().getTile(), currentTile);
					if (distance > distance2) {
						biggestTile = currentTile;
						biggestTiles.clear();
						biggestTiles.add(currentTile);
						distance = getZone().getTileDistance(
								getZone().getOwnHero().getTile(), currentTile);
					} else if (distance == distance2)
						biggestTiles.add(currentTile);
				}
			}
			biggestTile = biggestTiles.get((int) (Math.random() * (biggestTiles
					.size() - 1)));
		}
		return biggestTile;

	}

	/**
	 * Recursive method to fill a list of accessible tiles.
	 * 
	 * @param sourceTile
	 *            The tile to start looking from. If not crossable, list will
	 *            not be populated.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	private void fillAccessibleTilesBy(AiTile sourceTile)
			throws StopRequestException {
		checkInterruption();
		AiHero hero=getZone().getOwnHero();
		if ( sourceTile.isCrossableBy(hero ) )
		{
			this.accessibleTiles.add( sourceTile );
			if ( sourceTile.getNeighbor( Direction.UP ).isCrossableBy( hero ) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.UP ) ) )
				fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.UP ) );
			if ( sourceTile.getNeighbor( Direction.DOWN ).isCrossableBy( hero ) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.DOWN ) ) )
				fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.DOWN ) );
			if ( sourceTile.getNeighbor( Direction.LEFT ).isCrossableBy( hero ) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.LEFT ) ) ) 
				fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.LEFT ) );
			if ( sourceTile.getNeighbor( Direction.RIGHT ).isCrossableBy( hero ) && !this.accessibleTiles.contains( sourceTile.getNeighbor( Direction.RIGHT ) ) )
				fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.RIGHT ) );
		}
	}
	
	/**
	 * Outil pour connaître si une case contient des murs.
	 * 
	 * @param sourceTile la case qu'on veut savoir si elle contient des murs.
	 * @return vrai s'il n'existe pas mur dans la case.
	 * @throws StopRequestException 
	 */
	public boolean isTileContainsWalls(AiTile sourceTile) throws StopRequestException
	{
		this.checkInterruption();
		boolean result=false;
		if(!sourceTile.getBlocks().isEmpty())
			result=true;
		return result;
	}

	/**
	 * To get the accessible tiles from a given source tile. (TESTED, WORKS)
	 * 
	 * @param sourceTile
	 *            Tile to start looking from.
	 * @return List of all tiles that accessible from a given source tile.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public ArrayList<AiTile> getAccessibleTilesFrom(AiTile sourceTile)
			throws StopRequestException {
		this.checkInterruption();

		fillAccessibleTilesBy(sourceTile);

		return this.accessibleTiles;
	}

	/**
	 * To get destructed tiles from a given source tile.
	 * 
	 * @param sourceTile
	 *            Tile to start looking from.
	 * @return value of NbMurDetruit of the given tile
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public int getNbMurDetruitofTile(AiTile sourceTile)
			throws StopRequestException {
		this.checkInterruption();
		int result = 0;
		AiZone zone = this.getZone();
		AiTile tile_up = sourceTile.getNeighbor(Direction.UP);
		AiTile tile_down = sourceTile.getNeighbor(Direction.DOWN);
		AiTile tile_left = sourceTile.getNeighbor(Direction.LEFT);
		AiTile tile_right = sourceTile.getNeighbor(Direction.RIGHT);
		int i = 1, bomb_range = zone.getOwnHero().getBombRange();
		// obstacles sont pour terminer les recherce de murs quand on se
		// rencontre des murs.
		boolean[] obstacle = { true, true, true, true, true };

		while (obstacle[4] && (i <= bomb_range)) {
			this.checkInterruption();

			List<AiBlock> blocks;

			if (obstacle[0]) {
				blocks = tile_up.getBlocks();
				if (!tile_up.getItems().isEmpty())
					obstacle[0] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						this.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[0] = false;
					}
				}

				tile_up = tile_up.getNeighbor(Direction.UP);
			}
			if (obstacle[1]) {
				blocks = tile_down.getBlocks();
				if (!tile_down.getItems().isEmpty())
					obstacle[1] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						this.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[1] = false;
					}
				}
				tile_down = tile_down.getNeighbor(Direction.DOWN);
			}
			if (obstacle[2]) {
				blocks = tile_left.getBlocks();
				if (!tile_left.getItems().isEmpty())
					obstacle[2] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						this.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[2] = false;
					}
				}
				tile_left = tile_left.getNeighbor(Direction.LEFT);
			}
			if (obstacle[3]) {
				blocks = tile_right.getBlocks();
				if (!tile_right.getItems().isEmpty())
					obstacle[3] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						this.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[3] = false;
					}
				}
				tile_right = tile_right.getNeighbor(Direction.RIGHT);
			}
			if ((!obstacle[0]) && (!obstacle[1])
					&& (!obstacle[2] && (!obstacle[3])))
				obstacle[4] = false;

			i++;

		}
		if (result > 4)
			result = 4;
		return result;
	}

	/**
	 * To get the PeutTuerEnnemi value from a given source tile.
	 * 
	 * @param sourceTile
	 *            Tile to start looking from.
	 * @return PeutTuerEnnemi value of the tile
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public int getPeutTuerEnnemiofTile(AiTile sourceTile)
			throws StopRequestException {
		this.checkInterruption();
		int result = 0;

		
		if (!getZone().getRemainingHeroes().isEmpty()) {
			if (isInBlastSight(sourceTile,nearest_enemy))
				return 2;

			if(nearest_enemy!=null){
			for (AiTile neighbor : sourceTile.getNeighbors()) {
				this.checkInterruption();
				if (neighbor.getBlocks().isEmpty()) {
					if(neighbor!=null){
					if((simpleTileDistance(sourceTile,
							nearest_enemy.getTile()) <= AMBUSH_LIMIT)){
					if (isInBlastSight(neighbor, nearest_enemy))
						return 1;
					}
				}
				}
			}
			}
		}
		return result;	
	}

	/**
	 * Simulation pour voir si on peut tuer un adversaire en posant une bombe
	 * 
	 * 
	 * @param tile
	 *            Le tile on considere poser la bombe
	 * @param enemy
	 *            L'adversaire qu'on veut eliminer
	 * 
	 * @return vrai si on peut.
	 * @throws StopRequestException
	 */
	public boolean isSimInBlastSight(AiTile tile, AiHero enemy)// v2
			throws StopRequestException {
		checkInterruption();
		AiZone zone = this.getZone();
		AiSimZone simzone = new AiSimZone(zone);
		AiSimBomb simbomb = simzone.createBomb(tile, simzone.getOwnHero());
		List<AiTile> indanger_tiles = simbomb.getBlast();

		if (indanger_tiles.contains(enemy.getTile()))
			return true;

		return false;

	}

	/**
	 * Control si on peut tuer un adversaire en posant une bombe dans cette tile
	 * 
	 * @param tile
	 * @return vrai si on peut tuer un ennemi en posant une bombe dans ce tile
	 * @throws StopRequestException
	 */
	public boolean isInBlastSight(AiTile tile) throws StopRequestException {
		checkInterruption();
		this.checkInterruption();

		if (this.getZone().getRemainingHeroes() != null) {
			AiZone zone = this.getZone();
			AiHero hero = zone.getOwnHero();
			if (!tile.getHeroes().isEmpty()) {
				for (AiHero currentHero : tile.getHeroes()) {
					this.checkInterruption();
					if (currentHero.getId() != hero.getId())
						return true;
				}
			}
			AiTile tile_up = tile.getNeighbor(Direction.UP);
			AiTile tile_down = tile.getNeighbor(Direction.DOWN);
			AiTile tile_left = tile.getNeighbor(Direction.LEFT);
			AiTile tile_right = tile.getNeighbor(Direction.RIGHT);
			int i = 1, bomb_range = hero.getBombRange();
			// obstacles sont pour terminer les recherce d'ennemi quand on se
			// rencontre des murs.
			boolean[] obstacle = { true, true, true, true, true };

			while (obstacle[4] && (i <= bomb_range)) {
				this.checkInterruption();
				if (obstacle[0]) {

					if (!tile_up.getHeroes().isEmpty()
							&& (!tile_up.getHeroes().contains(hero)))
						return true;

					else if (!tile_up.isCrossableBy(hero))
						obstacle[0] = false;

					else if (!tile_up.getItems().isEmpty())
						obstacle[0] = false;

					tile_up = tile_up.getNeighbor(Direction.UP);
				}
				if (obstacle[1]) {

					if (!tile_down.getHeroes().isEmpty()
							&& (!tile_down.getHeroes().contains(hero)))
						return true;

					else if (!tile_down.isCrossableBy(hero))
						obstacle[1] = false;

					else if (!tile_down.getItems().isEmpty())
						obstacle[1] = false;
					tile_down = tile_down.getNeighbor(Direction.DOWN);
				}
				if (obstacle[2]) {

					if (!tile_left.getHeroes().isEmpty()
							&& (!tile_left.getHeroes().contains(hero)))
						return true;

					else if (!tile_left.isCrossableBy(hero))
						obstacle[2] = false;

					else if (!tile_left.getItems().isEmpty())
						obstacle[2] = false;
					tile_left = tile_left.getNeighbor(Direction.LEFT);
				}
				if (obstacle[3]) {

					if (!tile_right.getHeroes().isEmpty()
							&& (!tile_right.getHeroes().contains(hero)))
						return true;

					else if (!tile_right.isCrossableBy(hero))
						obstacle[3] = false;

					else if (!tile_right.getItems().isEmpty())
						obstacle[3] = false;
					tile_right = tile_right.getNeighbor(Direction.RIGHT);
				}
				if ((!obstacle[0]) && (!obstacle[1])
						&& (!obstacle[2] && (!obstacle[3])))
					obstacle[4] = false;

				i++;

			}

		}
		return false;

	}

	/**
	 * Control si on peut tuer un adversaire particulière en posant une bombe
	 * dans cette tile
	 * 
	 * @param tile
	 * 
	 * @param enemy
	 *            l'ennemi qu'on veut eliminer
	 * @return vrai si l'adversaire est dans notre portée
	 * @throws StopRequestException
	 */
	public boolean isInBlastSight(AiTile tile, AiHero enemy)
			throws StopRequestException {
		checkInterruption();
		this.checkInterruption();

		if (enemy != null) {
			AiZone zone = this.getZone();
			AiHero hero = zone.getOwnHero();
			
			
			
			
			AiTile tile_up = tile.getNeighbor(Direction.UP);
			AiTile tile_down = tile.getNeighbor(Direction.DOWN);
			AiTile tile_left = tile.getNeighbor(Direction.LEFT);
			AiTile tile_right = tile.getNeighbor(Direction.RIGHT);
			int i = 1, bomb_range = hero.getBombRange();
			// obstacles sont pour terminer les recherce de murs quand on se
			// rencontre des murs.
			boolean[] obstacle = { true, true, true, true, true };
			
			while (obstacle[4] && (i <= bomb_range)) {// kendi tilelımıza asla
														// true dondurmez
				this.checkInterruption();
				if (obstacle[0]) {

					if (tile_up.equals(enemy.getTile()))
						return true;

					else if (!tile_up.isCrossableBy(hero))
						obstacle[0] = false;

					else if (!tile_up.getItems().isEmpty())
						obstacle[0] = false;

					tile_up = tile_up.getNeighbor(Direction.UP);
				}
				if (obstacle[1]) {

					if (tile_down.equals(enemy.getTile()))
						return true;

					else if (!tile_down.isCrossableBy(hero))
						obstacle[1] = false;

					else if (!tile_down.getItems().isEmpty())
						obstacle[1] = false;
					tile_down = tile_down.getNeighbor(Direction.DOWN);
				}
				if (obstacle[2]) {

					if (tile_left.equals(enemy.getTile()))
						return true;

					else if (!tile_left.isCrossableBy(hero))
						obstacle[2] = false;

					else if (!tile_left.getItems().isEmpty())
						obstacle[2] = false;
					tile_left = tile_left.getNeighbor(Direction.LEFT);
				}
				if (obstacle[3]) {

					if (tile_right.equals(enemy.getTile()))
						return true;

					else if (!tile_right.isCrossableBy(hero))
						obstacle[3] = false;

					else if (!tile_right.getItems().isEmpty())
						obstacle[3] = false;
					tile_right = tile_right.getNeighbor(Direction.RIGHT);
				}
				if ((!obstacle[0]) && (!obstacle[1])
						&& (!obstacle[2] && (!obstacle[3])))
					obstacle[4] = false;

				i++;

			}
		}
		return false;

	}

	/**
	 * fonction pour trouver l'ennemi plus proche
	 * 
	 * @return AiHero de l'ennemi plus proche
	 * @throws StopRequestException
	 */
	public AiHero getNearestEnemy() throws StopRequestException {
		checkInterruption();
		AiZone zone = this.getZone();
		AiHero hero = zone.getOwnHero();
		AiHero target = null;
		
		int distance = 5000;
		int temp_distance=0;
		
		if (enemy_accessible) {
			for (AiHero enemy : zone.getRemainingOpponents()) {
				this.checkInterruption();
				if (((temp_distance=simpleTileDistance(hero.getTile(), enemy.getTile())) < distance)
						&& (this.accessibleTiles.contains(enemy.getTile())) && (enemy.getId() != getZone().getOwnHero().getId())){
					distance=temp_distance;
					target = enemy;
				}
			}
		}
		else
		{
			for (AiHero enemy : zone.getRemainingOpponents()) {
				this.checkInterruption();
				if (((temp_distance=simpleTileDistance(hero.getTile(), enemy.getTile())) < distance) && (enemy.getId() != getZone().getOwnHero().getId())){
					distance=temp_distance;
					best_Wall_Determinator = BEST_WALL_INITIATOR;
					target = enemy;
				}
			}
			
		}
		return target;
	}
	
	/**
	 * 
	 * Une methode simple pour determiner distance de deux cases
	 * 
	 * @param tile1
	 * @param tile2
	 * @return la distance entre deux cases
	 * @throws StopRequestException
	 */
	public int simpleTileDistance(AiTile tile1,AiTile tile2) throws StopRequestException
	{
		checkInterruption();
		return Math.abs(tile1.getCol()-tile2.getCol())+Math.abs(tile1.getRow()-tile2.getRow());
	}

	/**
	 * Controle si un ennemi est accesible ou pas
	 * 
	 * @return vrai, s'il y a un adversaire accesible dans la zone
	 * @throws StopRequestException
	 */
	public boolean isEnemiesAccessible() throws StopRequestException {
		checkInterruption();
		for (AiHero hero : getZone().getRemainingOpponents()) {
			checkInterruption();
			if (accessibleTiles.contains(hero.getTile())
					&& (hero.getId() != getZone().getOwnHero().getId()))
				return true;
		}
		return false;

	}

	/**
	 * Le recherhe s'il existe un item existe et visible dans la zone
	 * 
	 * @param itemtype
	 *            le type d'item
	 * @return vrai/faux
	 * @throws StopRequestException
	 */
	public boolean zoneHasItem(AiItemType itemtype) throws StopRequestException {
		checkInterruption();
		for (AiItem item : this.getZone().getItems()) {
			checkInterruption();
			if (item.getType().equals(itemtype))
				return true;
		}
		return false;
	}

	/**
	 * Control s'il y a un item dans les cases accessible
	 * 
	 * @return vrai s'il y a un item accessible
	 * @throws StopRequestException
	 */
	public boolean accessiblesHaveBonus() throws StopRequestException {
		checkInterruption();
		for (AiTile tile : accessibleTiles) {
			checkInterruption();

			for (AiItem item : tile.getItems()) {
				checkInterruption();
				if (isBonus(item))
					return true;
			}
		}

		return false;
	}

	/**
	 * Control si l'item est bonus
	 * 
	 * @param item
	 * @return vrai si c'est un bonus
	 * @throws StopRequestException
	 */

	public boolean isBonus(AiItem item) throws StopRequestException {
		checkInterruption();
		AiItemType type = item.getType();
		if (type.equals(AiItemType.EXTRA_BOMB)
				|| type.equals(AiItemType.EXTRA_FLAME)
				|| type.equals(AiItemType.EXTRA_SPEED)
				|| type.equals(AiItemType.GOLDEN_BOMB)
				|| type.equals(AiItemType.GOLDEN_FLAME)
				|| type.equals(AiItemType.GOLDEN_SPEED)
				|| type.equals(AiItemType.RANDOM_EXTRA))
			return true;
		return false;
	}

	/**
	 * Populates a list of dangerous tiles of this AI's zone. <br />
	 * (TESTED, WORKS)
	 * 
	 * @return List of the dangerous tiles.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public ArrayList<AiTile> getCurrentDangerousTiles()
			throws StopRequestException {
		checkInterruption();
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();
		for (AiBomb currentBomb : getZone().getBombs()) {
			checkInterruption();
			dangerousTiles.add(currentBomb.getTile());
			for (AiTile currentTile : currentBomb.getBlast()) {
				checkInterruption();
				dangerousTiles.add(currentTile);
			}
		}
		for (AiFire currentFire : getZone().getFires()) {
			checkInterruption();
			dangerousTiles.add(currentFire.getTile());
		}
		return dangerousTiles;
	}

	/**
	 * Recherhe de route pour echapper apres avoir posée une bombe
	 * 
	 * 
	 * @return nombre de route d'échappement simple
	 * @throws StopRequestException
	 */

	

	public int possible_escaping_directions() throws StopRequestException {
		checkInterruption();
		AiHero hero = getZone().getOwnHero();
		AiTile tile = hero.getTile();

		AiTile tile_up = tile.getNeighbor(Direction.UP);
		AiTile tile_down = tile.getNeighbor(Direction.DOWN);
		AiTile tile_left = tile.getNeighbor(Direction.LEFT);
		AiTile tile_right = tile.getNeighbor(Direction.RIGHT);
		int up = 0, down = 0, left = 0, right = 0;
		int range = hero.getBombRange();
		int counter = 0;

		boolean[] advance_direction = { true, true, true, true, true };
		while (advance_direction[4]) {
			checkInterruption();
			counter++;

			if (advance_direction[0]) {
				if (dangerList.contains(tile_up)) {
					advance_direction[0] = false;
				} else {
					if (tile_up.getNeighbor(Direction.LEFT).isCrossableBy(hero)) {

						up++;
					}

					else if (tile_up.getNeighbor(Direction.RIGHT)
							.isCrossableBy(hero)
							&& !dangerList.contains(tile
									.getNeighbor(Direction.LEFT))) {
						advance_direction[0] = false;
						up = 1;
					}
					tile_up = tile_up.getNeighbor(Direction.UP);
					if (!tile_up.isCrossableBy(hero))
						advance_direction[0] = false;

					if (counter == range)
						up = 1;
				}

			}

			if (advance_direction[1]) {
				if (dangerList.contains(tile_down)) {
					advance_direction[2] = false;
				} else {
					if (tile_down.getNeighbor(Direction.LEFT).isCrossableBy(
							hero)) {

						down++;
					}

					else if (tile_down.getNeighbor(Direction.RIGHT)
							.isCrossableBy(hero)
							&& !dangerList.contains(tile
									.getNeighbor(Direction.LEFT))) {
						advance_direction[1] = false;
						down = 1;
					}
					tile_down = tile_down.getNeighbor(Direction.DOWN);
					if (!tile_down.isCrossableBy(hero))
						advance_direction[1] = false;
					if (counter == range)
						down = 1;
				}

			}
			if (advance_direction[2]) {
				if (dangerList.contains(tile_left)) {
					advance_direction[2] = false;
				} else {
					if (tile_left.getNeighbor(Direction.UP).isCrossableBy(hero)) {

						left++;
					}

					else if (tile_left.getNeighbor(Direction.DOWN)
							.isCrossableBy(hero)) {
						advance_direction[2] = false;
						left = 1;
					}
					tile_left = tile_left.getNeighbor(Direction.LEFT);
					if (!tile_left.isCrossableBy(hero))
						advance_direction[2] = false;
				}
				if (counter == range)
					left = 1;

			}
			if (advance_direction[3]) {
				if (dangerList.contains(tile_right)) {
					advance_direction[3] = false;
				} else {
					if (tile_right.getNeighbor(Direction.UP)
							.isCrossableBy(hero)) {

						right++;
					}

					else if (tile_right.getNeighbor(Direction.DOWN)
							.isCrossableBy(hero)
							&& !dangerList.contains(tile
									.getNeighbor(Direction.DOWN))) {
						advance_direction[3] = false;
						right = 1;
					}
					tile_right = tile_right.getNeighbor(Direction.RIGHT);
					if (!tile_right.isCrossableBy(hero))
						advance_direction[3] = false;
				}
				if (counter == range)
					right = 1;

			}
			if (!advance_direction[0] && !advance_direction[1]
					&& !advance_direction[2] && !advance_direction[3]) {
				advance_direction[4] = false;
			}
		}

		return up + down + left + right;
	}

	/**
	 * 
	 * Renvoie une liste de tile qui sont concernée de mort
	 *         subite
	 * 
	 * @return ArrayListe: Les cases concernee de mort subite
	 * 
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> getSuddenDeathTiles() throws StopRequestException {
		checkInterruption();
		ArrayList<AiTile> result = new ArrayList<AiTile>();

		if (!this.getZone().getAllSuddenDeathEvents().isEmpty()) {
			for (AiSuddenDeathEvent suddenDeath : this.getZone()
					.getAllSuddenDeathEvents()) {
				checkInterruption();
				if (suddenDeath.getTime() < this.getZone().getTotalTime()
						+ SUDDEN_DEATH_CONTROL_TIME) {

					for (AiTile tile : suddenDeath.getTiles()) {

						checkInterruption();
						result.add(tile);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Renvoie le temps exacte si on pose une bombe dans une case donnée
	 * 
	 * @param tile
	 *            Le case qu'on consider poser une bombe
	 * @return ms(long)
	 * @throws StopRequestException
	 */
	public long bombExplosionTime(AiTile tile) throws StopRequestException {
		checkInterruption();
		long result = BOMB_EXPLOSION_CHECK;
		if (dangerList.contains(tile)) {
			result= chainReactionTime(tile);
			}

		 else {
			result = getZone().getOwnHero().getBombDuration();
		}
		return result;
	}

	/**
	 * 
	 * Calcule le temps qui va rester pour faire un chainage
	 * 
	 * @param tile
	 *            Celle qu'on veut savoir s'il est possible de créer une
	 *            explosion chainée
	 * @return Le temps qui nous restera s'il est le chainage est posible, sinon
	 *         0
	 * @throws StopRequestException
	 */
	public long chainReactionTime(AiTile tile) throws StopRequestException {
		checkInterruption();
		long result = BOMB_EXPLOSION_CHECK;
		Map<AiBomb, Long>bomb_time=getZone().getDelaysByBombs();
		if (dangerList.contains(tile)) {
			if (!bomb_time.isEmpty()) {
				for (AiBomb bomb : getZone().getBombs()) {
					checkInterruption();
					if (bomb.getBlast().contains(tile)) {
					long explosion_time =bomb_time.get(bomb)
									- bomb.getElapsedTime();
							if(explosion_time<result)
								result=explosion_time;			
			}
				}
			
			}
		}
		 else
			result = 0;

		return result;
	}
	


	/**
	 * Cherche la case qui est la plus proche a un ennemi inaccesible
	 * 
	 * @return la seule bestwall dans le map
	 * @throws StopRequestException 
	 */
	private AiTile getBestWallIfAny() throws StopRequestException{
		checkInterruption();
		AiTile BestWallCase = null;
		
		
		for (AiTile tile : accessibleTiles) {
			checkInterruption();
			if(nearest_enemy!=null){
				for(AiTile tileNeighbor : tile.getNeighbors())
				{
					checkInterruption();
					for(AiBlock tileBlock: tileNeighbor.getBlocks())
					{
						checkInterruption();

						if(tileBlock.isDestructible())
						{
							for(AiTile neighbor : tileNeighbor.getNeighbors())
							{
								checkInterruption();
								if(!getAccessibleTiles().contains(neighbor))
								{
									if(simpleTileDistance(tile,this.nearest_enemy.getTile())<best_Wall_Determinator)
									{
									best_Wall_Determinator=this.simpleTileDistance(tile,nearest_enemy.getTile());
									BestWallCase=tile;
									}
									if(simpleTileDistance(tile,this.nearest_enemy.getTile())==best_Wall_Determinator)
									{
										if (BestWallCase != null) {
											if (tile.getRow() > BestWallCase.getRow()) {
												BestWallCase = tile;
											}
											else if ((tile.getRow() == BestWallCase.getRow()) && (tile.getCol() > BestWallCase.getCol())) {
												BestWallCase = tile;
											}
										}
									}
								}
							}
						}
							
					}
				}
				}
		}
		if(BestWallCase!=null)
		{
		for(AiBomb bomb: getZone().getBombs())
		{
			checkInterruption();
			if(accessibleTiles.contains(bomb.getTile().getNeighbor(Direction.UP))||accessibleTiles.contains(bomb.getTile().getNeighbor(Direction.DOWN))
					||accessibleTiles.contains(bomb.getTile().getNeighbor(Direction.LEFT))
					||accessibleTiles.contains(bomb.getTile().getNeighbor(Direction.RIGHT)))
					{
					if(simpleTileDistance(bomb.getTile(),this.nearest_enemy.getTile())<simpleTileDistance(this.nearest_enemy.getTile(), BestWallCase))
						BestWallCase=bomb.getTile();
					}
		}
		}
		
		return BestWallCase;
	}
	
	/**Renvoie vrai si item est un contigious.
	 * @param item 
	 * @return vrai ssi il est contigious
	 * @throws StopRequestException
	 */
	public boolean isContagiousBonus(AiItem item) throws StopRequestException
	{
		checkInterruption();
			if(item.getType()==AiItemType.NO_BOMB || item.getType()==AiItemType.NO_FLAME || item.getType()==AiItemType.NO_SPEED)
			{
				return true;
			}
		
		return false;
	}
	/** Le control si la bombe donnée est accessible.
	 * @param bomb
	 * @return vrai/faux
	 * @throws StopRequestException
	 */
	public boolean isbombInAccessible(AiBomb bomb) throws StopRequestException
	{
		checkInterruption();
		for(AiTile tile: bomb.getTile().getNeighbors())
		{
			checkInterruption();
			if(this.accessibleTiles.contains(tile))
			{
				return true;
			}
		}
		return false;
	}
	/**Method serve à trouver une case peut nous proteger si on y pose une bombe. 
	 * @param bomb le bomb on essaye d'échapper
	 * @param givenHero
	 * @return le tile on doit poser une bomber pour échapper. Null s'il n'existe pas.
	 * @throws StopRequestException
	 */
	public AiTile findShieldingTile(AiBomb bomb,AiHero  givenHero) throws StopRequestException
	{
		checkInterruption();
		AiTile result=null;
		int distance=100;
		for(AiTile tile: bomb.getTile().getNeighbors())
		{
			checkInterruption();
			if(this.accessibleTiles.contains(tile))
			{
			if(simpleTileDistance(tile, givenHero.getTile())<distance)
			{
				distance=simpleTileDistance(tile, givenHero.getTile());
				result=tile;
			}
			}
		}
		return result;
	}
	/**Presque pareil celui de AiZone mais ici on seulement compte les mur, les bombes et les flames.
	 * @param tile
	 * @return vrai si on peut se situe dans cette case.
	 * @throws StopRequestException
	 */
	public boolean customIsCrossableBy(AiTile tile) throws StopRequestException
	{
		checkInterruption();
		if(!tile.getBlocks().isEmpty())
			return false;
		if(!tile.getBombs().isEmpty())
			return false;
		if(!tile.getFires().isEmpty())
			return false;
		return true;
	}
	


}
