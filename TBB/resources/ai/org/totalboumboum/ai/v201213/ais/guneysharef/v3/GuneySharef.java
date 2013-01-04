package org.totalboumboum.ai.v201213.ais.guneysharef.v3;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;



import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.model.partial.AiPartialModel;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Melis Güney
 * @author Seli Sharef
 */
public class GuneySharef extends ArtificialIntelligence
{
	/**
	 * 
	 */
	public HashMap<AiTile, Integer> hashmap;
	
	/**
	 * 
	 */
	public GuneySharef()
	{	// active/désactive la sortie texte
		verbose = false;
	}
	/**
	 * @return the hashmap
	 * @throws StopRequestException
	 */
	public HashMap<AiTile,Integer> getHashMap() throws StopRequestException{
		checkInterruption();
		return hashmap;
	}
	@Override
	protected void initOthers() throws StopRequestException
	{	checkInterruption();
		hashmap=new HashMap<AiTile,Integer>();
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() throws StopRequestException
	{	checkInterruption();
	

	}
	
	@Override
	protected void updatePercepts() throws StopRequestException
	{	checkInterruption();
		

	

	}
	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** gestionnaire chargé de calculer les valeurs d'utilité de l'agent */
	protected UtilityHandler utilityHandler;
	/** gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;
	
	/**
	 * on initialise les handler
	 */
	@Override
	protected void initHandlers() throws StopRequestException
	{	checkInterruption();
		
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		utilityHandler = new UtilityHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
		

		
		
	}

	@Override
	protected ModeHandler getModeHandler() throws StopRequestException
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<GuneySharef> getUtilityHandler() throws StopRequestException
	{	checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<GuneySharef> getBombHandler() throws StopRequestException
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<GuneySharef> getMoveHandler() throws StopRequestException
	{	checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{	checkInterruption();

		
		// ici, par défaut on affiche :
			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les utilités courantes
			utilityHandler.updateOutput();
	
	
	}

	/**
	 * @param tile
	 * @param tile2
	 * @return distance entre les cases
	 * @throws StopRequestException
	 */
	
	public int getDist(AiTile tile, AiTile tile2) throws StopRequestException {
		this.checkInterruption();
		int d= Math.abs(tile.getCol()-tile2.getCol())+Math.abs(tile.getRow()-tile2.getRow());
		
		return d;
	}

	/**
	 * @param t
	 * @return Donne la plus proche case
	 * @throws StopRequestException
	 */
	public Object getClosestTile(AiTile t) throws StopRequestException{
		this.checkInterruption();
		float valeur = 20;
		AiTile tile = null;
		Set<AiTile> liste = this.utilityHandler.selectTiles();
		AiZone z=getZone();
		AiHero h =z.getOwnHero();
		AiTile t2=h.getTile();
		
		if(t==t2)
			liste.remove(t2);
		
		for(AiTile tile2 : liste){
			this.checkInterruption();
			if(getDist(t, tile2) < valeur){
				valeur=getDist(t,tile2);
				tile=tile2;
				
			}
		}
		return tile;
	}
	
	/**
	 * @return la meilleur case
	 * @throws StopRequestException
	 */
	public AiTile getBiggestTile() throws StopRequestException{
		this.checkInterruption();
		float valeur = 0;
		AiTile biggest=null;
		Set<AiTile> liste = this.utilityHandler.selectTiles();

		
		for(AiTile tile : liste){
			this.checkInterruption();
			if(getUtilityHandler().getUtilitiesByTile().get(tile)>valeur)
				valeur=getUtilityHandler().getUtilitiesByTile().get(tile);
			biggest=tile;	
		}
		return biggest;
	}
	
	
	
	/**
	 * @param hero
	 * @param tile
	 * @param simulate
	 * @return les cases qui ne sont pas dangereuses
	 * @throws StopRequestException
	 */
	public List<AiTile> getSafeTiles(AiHero hero, AiTile tile, boolean simulate)
			throws StopRequestException {
		this.checkInterruption();
		AiZone zone = getZone();
		org.totalboumboum.ai.v201213.adapter.model.full.AiSimZone simzone = new org.totalboumboum.ai.v201213.adapter.model.full.AiSimZone(zone);
		ArrayList<AiTile> list = new ArrayList<AiTile>();
		ArrayList<AiTile> safe = new ArrayList<AiTile>();
		ArrayList<AiTile> blastflamelist = new ArrayList<AiTile>();

		list.add(hero.getTile());

		for (org.totalboumboum.ai.v201213.adapter.data.AiBomb b : simzone.getBombs()) {
			this.checkInterruption();

			if (b.getBlast().contains(hero.getTile()))
				continue;

			for (AiTile t : b.getBlast()) {
				this.checkInterruption();
				blastflamelist.add(t);
			}
		}

		for (org.totalboumboum.ai.v201213.adapter.data.AiFire aifire : simzone.getFires()) {
			this.checkInterruption();
			blastflamelist.add(aifire.getTile());
		}
		while (!list.isEmpty()) {
			this.checkInterruption();
			ArrayList<AiTile> list1 = new ArrayList<AiTile>();
			for (AiTile aitile : list) {
				this.checkInterruption();
				for (Direction direction : Direction.getPrimaryValues()) {
					this.checkInterruption();
					AiTile neighbor = aitile.getNeighbor(direction);

					if (neighbor.isCrossableBy(hero)
							&& !safe.contains(neighbor)) {
						if (!blastflamelist.contains(neighbor)) {
							list1.add(neighbor);
						}
					}

				}
				safe.add(aitile);
			}
			for (AiTile aitile : safe) {
				this.checkInterruption();
				if (list.contains(aitile)) {
					list.remove(aitile);
				}
			}
			for (AiTile aitile : list1) {
				this.checkInterruption();
				list.add(aitile);
			}
		}

		if (simulate) {
			org.totalboumboum.ai.v201213.adapter.data.AiBomb b = simzone.createBomb(tile, simzone.getOwnHero());
			safe.removeAll(b.getBlast());
		}

		return safe;
	}
	
	/**
	 * @param aitile
	 * @param direction
	 * @return a boolean. regarde si le mur est detructible
	 * @throws StopRequestException
	 */
	public boolean controlOfDestructibleBlock(AiTile aitile, Direction direction)
			throws StopRequestException {
		this.checkInterruption();
		boolean result = false;
		for (AiBlock currentblock : aitile.getNeighbor(direction).getBlocks()) {
			this.checkInterruption();
			if (currentblock.isDestructible()
					&& aitile.getNeighbor(direction).getItems().isEmpty()) {

				return result = true;
			}
		}

		return result;

	}
	
	/**
	 * @param aitile
	 * @param direction
	 * @param i
	 * @return a boolean. regarde s'il y a un ennemie dans la portée
	 * @throws StopRequestException
	 */
	public boolean getAnEnemyInMyRange(AiTile aitile, Direction direction, int i)
			throws StopRequestException {
		this.checkInterruption();
		boolean result = false;
		AiZone zone = getZone();
		AiHero ourhero = zone.getOwnHero();
	
		int currentrange = ourhero.getBombRange();

		if (!zone.getRemainingOpponents().isEmpty()) {

			while (i < currentrange) {
				this.checkInterruption();
				if (controlOfBlocks(aitile, direction) == true
						&& aitile.getNeighbor(direction).getItems().isEmpty()) {
					if (controlEnemy(aitile, direction) == true) {
						return result = true;
					} else {
						i++;
						return result = getAnEnemyInMyRange(
								aitile.getNeighbor(direction), direction, i);
					}
				}

				else {
					return result = false;
				}
			}
		}
		return result;

	}
	
	/**
	 * @param aitile
	 * @param direction
	 * @return a boolean. regarde si sur la direction il y a un mur non destructible
	 * @throws StopRequestException
	 */
	public boolean controlOfBlocks(AiTile aitile, Direction direction)
			throws StopRequestException {
		this.checkInterruption();
		boolean result = false;

		if (aitile.getNeighbor(direction).getBlocks().isEmpty()) {
			if (aitile.getNeighbor(direction).getBombs().isEmpty()) {
				result = true;

			}
		}
		return result;

	}
	/**
	 * @param aitile
	 * @param direction
	 * @return a boolean ragarde si dans la direction il y a un ennemi
	 * @throws StopRequestException
	 */
	public boolean controlEnemy(AiTile aitile, Direction direction)
			throws StopRequestException {
		this.checkInterruption();

		boolean result = false;

		if (getNearestEnemy().getTile().equals(aitile.getNeighbor(direction))) {
			result = true;
		}
		return result;
	}
	
	/**
	 * @return l'ennemi pret a notre agent
	 * @throws StopRequestException
	 */
	public AiHero getNearestEnemy() throws StopRequestException {
		this.checkInterruption();
		int enemydist = 10000;
		AiHero nearestEnemy = null;
		AiZone zone = getZone();
		AiHero ourhero = zone.getOwnHero();
		AiTile ourtile = ourhero.getTile();
		for (AiHero enemy : zone.getRemainingOpponents()) {
			this.checkInterruption();
			if (enemy.hasEnded())
				continue;

			int dist = this.getDist(enemy.getTile(), ourtile);
			if (dist < enemydist) {
				nearestEnemy = enemy;
				enemydist = dist;
			}
		}
		return nearestEnemy;
	}

	/**
	 * @param tile
	 * @return a boolean regarde s'il y a un danger sur cette case
	 * @throws StopRequestException
	 */
	public Boolean getDanger(AiTile tile) throws StopRequestException {
		this.checkInterruption();
		boolean result = false;
		AiZone zone = getZone();
		AiHero ourhero = zone.getOwnHero();
		AiTile ourtile = ourhero.getTile();
		AiPartialModel apm = new AiPartialModel(zone);
		long crossTile = Math.round(1000 * tile.getSize()
				/ ourhero.getWalkingSpeed());
		long crossTime = crossTile * getDist(ourtile, tile);

		if (!tile.getBombs().isEmpty()) {
			result = true;
			return result;
		}

		if (apm.getExplosion(tile) != null) {
			if (apm.getExplosion(tile).getEnd() < crossTime && crossTime != 0) {
				return result = false;
			}

			else {
				return result = true;

			}
		} else {
			return result = false;
		}

	}
	
	/**
	 * @param tile
	 * @return si on peutbloquer un ennemie
	 * @throws StopRequestException 
	 */
	public Boolean block(AiTile tile) throws StopRequestException{
		this.checkInterruption();
		boolean result=false;
		
		AiZone z=getZone();

		
		if(!z.getRemainingOpponents().isEmpty()){
			if(getSafeTiles(getNearestEnemy(),tile,true).isEmpty()){
				return result=true;
			}
		}
		
		return result;
	}

	/**
	 * @return le mode
	 * 
	 * @throws StopRequestException 
	 */
	public AiMode getMode() throws StopRequestException{
		this.checkInterruption();
		AiMode mode=modeHandler.getMode();
		
		return mode;
	}
}
