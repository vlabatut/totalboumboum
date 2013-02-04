package org.totalboumboum.ai.v201213.ais.cinaryalcin.v4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import org.totalboumboum.ai.v201213.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
 * 
 * 
 *
 * @author Bekir Cınar
 * @author Deniz Yalçın
 */
@SuppressWarnings("deprecation")
public class CinarYalcin extends ArtificialIntelligence
{
	
	/**
	 * our zone
	 */
	AiZone zone;
	/**our hero */
	AiHero ourhero;
	/**our current tile */
	AiTile ourtile;
	
	/**
	 * own zone
	 */
	 Set<AiTile> ownZoneSearch= new TreeSet<AiTile>();
	 
	
	 
		/**
		 * bomb Range
		 */
		ArrayList<AiTile> bombRangeTiles= new ArrayList<AiTile>();

	
	/**
	 * the list of super bonuses
	 */
	ArrayList<AiItemType> SBonus = new ArrayList<AiItemType>();

	/**
	 * the list of extra bonuses
	 */
	ArrayList<AiItemType> Bonus = new ArrayList<AiItemType>();
	
	/**
	 * the list of all wall who is threatened by a or few bomb
	 */
	ArrayList<AiTile> wallWillDestroyedList= new ArrayList<AiTile>();
	
	
	/**
	 * an integer which count maliciouses on our way
	 */
	int MalusCounter=0;

	/**
	 * the list of maluses
	 */
	public ArrayList<AiItemType> Malus = new ArrayList<AiItemType>();

	/**
	 * Instancie la classe principale de l'agent.
	 */
	public CinarYalcin()
	{	// active/désactive la sortie texte
		verbose = false;
		
		// Le list de Bonus
		Bonus.add(AiItemType.EXTRA_BOMB);
		Bonus.add(AiItemType.EXTRA_FLAME);
		Bonus.add(AiItemType.EXTRA_SPEED);
		Bonus.add(AiItemType.RANDOM_EXTRA);
		//Le list de Super Bonus
		SBonus.add(AiItemType.GOLDEN_BOMB);
		SBonus.add(AiItemType.GOLDEN_FLAME);
		SBonus.add(AiItemType.GOLDEN_SPEED);
		//Le list de Malus
		Malus.add(AiItemType.NO_BOMB);
		Malus.add(AiItemType.NO_FLAME);
		Malus.add(AiItemType.NO_SPEED);
		Malus.add(AiItemType.ANTI_BOMB);
		Malus.add(AiItemType.ANTI_FLAME);
		Malus.add(AiItemType.ANTI_SPEED);
		Malus.add(AiItemType.RANDOM_NONE);
		Malus.add(AiItemType.OTHER);
		Malus.add(AiItemType.PUNCH);
			
	}
	
	
	
	/**
	 * 
	 */
	
	@Override
	protected void initOthers() throws StopRequestException
	{	checkInterruption();
		
		
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
	protected AiModeHandler<CinarYalcin> getModeHandler() throws StopRequestException
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<CinarYalcin> getUtilityHandler() throws StopRequestException
	{	checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<CinarYalcin> getBombHandler() throws StopRequestException
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<CinarYalcin> getMoveHandler() throws StopRequestException
	{	checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{	checkInterruption();
			
			// les chemins et destinations courants
			moveHandler.updateOutput();
			checkInterruption();
			// les utilités courantes
			utilityHandler.updateOutput();
	}
	
	

	/**
	 * @return get current mode
	 * @throws StopRequestException 
	 */
	public AiMode getMode() throws StopRequestException
	{
		checkInterruption();
		AiMode mode=modeHandler.getMode();
		return mode;
	}
	
	/**
	 * sudden death will appear in our time limit
 	 * @param time
	 * @return true if sudden death event is close
	 * @throws StopRequestException 
	 */
	public boolean isSuddenDeathClose(long time) throws StopRequestException {
		this.checkInterruption();
		boolean resultat=false;
		zone=getZone();
		
		if(zone.getNextSuddenDeathEvent()!=null)
		{
			AiSuddenDeathEvent suddendeath= zone.getNextSuddenDeathEvent();
			if( suddendeath.getTime()<time)
			{
				resultat = true;
			}
		}
		
		return resultat;	
	}
	
	
	/**
	 * @return an ArrayList<AiTile> which get current dangerous tiles in all zone
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> getCurrentDangerousTiles() throws StopRequestException
	{
		this.checkInterruption();
		zone=getZone();
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();
		
		for ( AiFire currentFire : zone.getFires() )
		{
			this.checkInterruption();
			if(!dangerousTiles.contains(currentFire.getTile()))
				dangerousTiles.add( currentFire.getTile() );
		}
		
		if(isSuddenDeathClose(6000))
		{
			for(AiTile tilesudden:zone.getNextSuddenDeathEvent().getTiles())
			{
				this.checkInterruption();
				if(!dangerousTiles.contains(tilesudden))
					dangerousTiles.add(tilesudden);
			}
		}
		
		if(!zone.getBombs().isEmpty())
		{
			for(AiBomb currentbomb: zone.getBombs())
			{
				this.checkInterruption();
				if(!dangerousTiles.contains(currentbomb.getTile()))
					dangerousTiles.add(currentbomb.getTile());
				for(AiTile currenttile:currentbomb.getBlast())
				{
					this.checkInterruption();
					if(!dangerousTiles.contains(currenttile))
						dangerousTiles.add(currenttile);		
				}
			}
		}
		for(AiHero aiHero : zone.getRemainingOpponents())
		{
			this.checkInterruption();
			if(aiHero.isContagious())
			{
				if(!dangerousTiles.contains(aiHero.getTile()))
				dangerousTiles.add(aiHero.getTile());
			}
		}
		return dangerousTiles;
	}
	
	
	/**
	 * @param hero 
	 * @return List of tile that given hero can reach
	 * @throws StopRequestException
	 */
	public Set<AiTile> getTilesReachable(AiHero hero) throws StopRequestException
	{	this.checkInterruption();
		AiHero ourhero = hero;
		AiTile ourtile = ourhero.getTile();
		Set<AiTile> result = new TreeSet<AiTile>();
		AiTile aitile = ourtile;	
		Queue<AiTile> qe = new LinkedList<AiTile>();
		qe.add(aitile);
		while (!qe.isEmpty()) {
			this.checkInterruption();
			aitile = qe.poll();
			for (Direction direction : Direction.getPrimaryValues()) {
				this.checkInterruption();
				if (aitile.getNeighbor(direction).getBlocks().isEmpty()
						&& !qe.contains(aitile.getNeighbor(direction))
						&& !result.contains(aitile.getNeighbor(direction))
						&& !aitile.getNeighbor(direction).equals(ourtile)) {
					qe.add(aitile.getNeighbor(direction));
				}
			}
			if (!qe.isEmpty()) {

				aitile = qe.peek();

				result.add(aitile);

			} 
			else { break;}	
		}		
		result.add(ourtile);	
		return result;
		}
	
	/**
	 * @param hero
	 * @return List of tile that given hero can reach but it consideres bomb as a block
	 * @throws StopRequestException
	 */
	public Set<AiTile> getTilesReachableWithBombs(AiHero hero) throws StopRequestException
	{	this.checkInterruption();
		AiHero ourhero = hero;
		AiTile ourtile = ourhero.getTile();
		Set<AiTile> result = new TreeSet<AiTile>();
		AiTile aitile = ourtile;	
		Queue<AiTile> qe = new LinkedList<AiTile>();
		qe.add(aitile);
		while (!qe.isEmpty()) {
			this.checkInterruption();
			aitile = qe.poll();
			for (Direction direction : Direction.getPrimaryValues()) {
				this.checkInterruption();
				if (aitile.getNeighbor(direction).getBombs().isEmpty()
						&& aitile.getNeighbor(direction).getBlocks().isEmpty()
						&& !qe.contains(aitile.getNeighbor(direction))
						&& !result.contains(aitile.getNeighbor(direction))
						&& !aitile.getNeighbor(direction).equals(ourtile)) {
					qe.add(aitile.getNeighbor(direction));
				}
			}
			if (!qe.isEmpty()) {

				aitile = qe.peek();

				result.add(aitile);

			} 
			else { break;}	
		}		
		result.add(ourtile);	
		return result;
		}
	
	
	/**
	 * @return List of tiles in my hero's bomb range
	 * @throws StopRequestException 
	 */
	public ArrayList<AiTile> bombRangeTiles() throws StopRequestException
	{
		this.checkInterruption();
		zone=getZone();
		AiHero ourHero=zone.getOwnHero();
		AiTile ourTile=ourHero.getTile();
		AiTile bakilanTile=ourTile;
		boolean kontrol = false;
		
		ArrayList<AiTile> resultat=new ArrayList<AiTile>();
		resultat.add(bakilanTile);
		for (Direction direction : Direction.getPrimaryValues()) {
			this.checkInterruption();
			for(int i=0;((i<ourHero.getBombRange())&&(kontrol==false));i++){
				this.checkInterruption();
				bakilanTile=bakilanTile.getNeighbor(direction);
				resultat.add(bakilanTile);
				if(bakilanTile.getBlocks().size()>0){
					kontrol=true;
				}			
			}
			kontrol=false;
			bakilanTile=ourTile;	
		}
		return resultat;
	}
	
	
	/**
	 * @param x an integer who defines bomb range
	 * @return check if there is an enemy in our x range
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> bombRangeTiles2(int x) throws StopRequestException
	{
		this.checkInterruption();
		zone=getZone();
		AiHero ourHero=zone.getOwnHero();
		AiTile ourTile=ourHero.getTile();
		AiTile bakilanTile=ourTile;
		boolean kontrol = false;
		
		ArrayList<AiTile> resultat=new ArrayList<AiTile>();
		resultat.add(bakilanTile);
		if(x>ourHero.getBombRange()){
			x=ourHero.getBombRange();
		}
		
		for (Direction direction : Direction.getPrimaryValues()) {
			this.checkInterruption();
			for(int i=0;((i<x)&&(kontrol==false));i++){
				this.checkInterruption();
				bakilanTile=bakilanTile.getNeighbor(direction);
				resultat.add(bakilanTile);
				if((bakilanTile.getBlocks().size()>0)){
					kontrol=true;
				}			
			}
			kontrol=false;
			bakilanTile=ourTile;	
		}
		
		return resultat;
	}
	
	/**
	 * @return number of block in our bomb range
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> numberOfBlock() throws StopRequestException {
		this.checkInterruption();
		ArrayList<AiTile> tiles=bombRangeTiles;
		ArrayList<AiTile> resultat=new ArrayList<AiTile>();
		
		for (AiTile aiTile : tiles) {
			this.checkInterruption();
			if(aiTile.getBlocks().size()>0){
				for (AiBlock block : aiTile.getBlocks()) {
					this.checkInterruption();
					if(block.isDestructible()){
						resultat.add(aiTile);
					}
				}
			}
		}	
		return resultat;
	}
	
	
	
	/**
	 * @return number of item in our hero's bomb range
	 * @throws StopRequestException
	 */
	public int numberOfItems() throws StopRequestException {
		this.checkInterruption();
		ArrayList<AiTile> tiles=bombRangeTiles;
		int number=0;
		
		for (AiTile aiTile : tiles) {
			this.checkInterruption();
			if(aiTile.getItems().size()>0){
			for (AiItem aiItem : aiTile.getItems()) {
				this.checkInterruption();
				if(Bonus.contains(aiItem.getType())||SBonus.contains(aiItem.getType())){
					number++;
				}
				else if(Malus.contains(aiItem.getType())){
					MalusCounter++;
				}
			}
		}	
	}
		return number;
	}
	
	/**
	 * 
	 * @return list of all wall who is threatened by a bomb
	 * @throws StopRequestException 
	 */
	public ArrayList<AiTile> wallWillBeDestroyed() throws StopRequestException
	{
		this.checkInterruption();
		ArrayList<AiTile> resultat = new ArrayList<AiTile>();
		boolean kontrol =false;
		zone=getZone();
		
		for (AiBomb a : zone.getBombs()) {
			this.checkInterruption();
			AiTile bakilan = a.getTile();
			for (Direction direction : Direction.getPrimaryValues()) {
				this.checkInterruption();
				
				for(int i=0;(i<a.getRange()&& kontrol==false);i++)
				{
					this.checkInterruption();
					if(bakilan.getBlocks().size()>0){
						if(!resultat.contains(bakilan))
							resultat.add(bakilan);
						kontrol=true;
					}
					bakilan =bakilan.getNeighbor(direction);
				}
				bakilan=a.getTile();
				kontrol=false;
			}			
		}
		return resultat;
	}
	
	/** 
	 * It uses a global variable 'ownZoneSearch' which contains reachable tiles and is defined before.
	 * @return true if there is an enemy reachable in our hero's zone
	 * @throws StopRequestException 
	 */
	public boolean anyEnemieInOurZone() throws StopRequestException {
		this.checkInterruption();
		boolean resultat = false;
		zone=getZone();
		for (AiHero hero : zone.getRemainingOpponents()) {
			this.checkInterruption();
			if(resultat==false){
			if(ownZoneSearch.contains(hero.getTile()))
				resultat=true;
			}
			
		}
		
		return resultat;
	}
	
	/**
	 * @param aitile 
	 * @return return true if selected tile in danger.if wall or items in this tile it will be destroyer.
	 * @throws StopRequestException  */
	public boolean willBeDestroyed(AiTile aitile) throws StopRequestException {
		this.checkInterruption();
		zone=getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		AiTile	tileBakilan = ourtile;
		
		boolean result = false;
		boolean controlBomb = false;
		int i=0;
		for(Direction direction: Direction.getPrimaryValues())
		{
			checkInterruption();
			while(!(tileBakilan.getNeighbor(direction).getBlocks().size()==0)&&(result==false)&&(controlBomb==false))
			{
				checkInterruption();
				tileBakilan= tileBakilan.getNeighbor(direction);
				i++;
				if(tileBakilan.getBombs().size()>0)
				{
					controlBomb=true;
					for(AiBomb bomb:tileBakilan.getBombs())
					{
						checkInterruption();
						if(bomb.getRange()>=i)
						{
							result=true;
						}
					}
				}
			}
			tileBakilan=ourtile;
			controlBomb=false;
			i=0;
		}
		
		
		return result;
	}	
	
	/**
	 * @param tilec a tile which you drop your bomb on. if parameter is left null,
	 *  it will take your hero's tile as a parameter
	 * @return true if your bomb's fire can reach an enemy.
	 * @throws StopRequestException 
	 */
	public boolean possibleDangerForEnemy(AiTile tilec) throws StopRequestException
	{
		this.checkInterruption();
		boolean result = false;
		zone=getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		AiSimZone simzone=new AiSimZone(zone);
		AiBomb mybomb=simzone.createBomb(tilec,simzone.getOwnHero());
		for(AiHero hero:simzone.getRemainingOpponents())
		{		
			this.checkInterruption();
			if(mybomb.getBlast().contains(hero.getTile()))
			result=true;	
		}
		return result;
	}
	

	
	
	/**
	 * @param hero
	 * @param tile when simulation process, bomb is dropped on that tile.
	 * @param simulate boolean if a bombing simulation is wanted, must given true.
	 * @param simulateAllHero all heros will pose a bomb and you will simulate it
	 * @return finds all secure tiles for given hero
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> updateSafeTiles(AiHero hero, AiTile tile, boolean simulate,boolean simulateAllHero)
			throws StopRequestException {
		this.checkInterruption();
		zone=getZone();
		AiSimZone simzone = new AiSimZone(zone);
		ArrayList<AiTile> currentDangerousTiles= getCurrentDangerousTiles();
		
		this.checkInterruption();
		AiHero ourhero = hero;
		AiTile ourtile = ourhero.getTile();
		ArrayList<AiTile> result = new ArrayList<AiTile>();
		AiTile aitile = ourtile;	
		Queue<AiTile> qe = new LinkedList<AiTile>();
		qe.add(aitile);
		while (!qe.isEmpty()) {
			this.checkInterruption();
			aitile = qe.poll();
			for (Direction direction : Direction.getPrimaryValues()) {
				this.checkInterruption();
				if (aitile.getNeighbor(direction).getBombs().isEmpty()
						&& aitile.getNeighbor(direction).getBlocks().isEmpty()
						&& !qe.contains(aitile.getNeighbor(direction))
						&& !result.contains(aitile.getNeighbor(direction))
						&& !aitile.getNeighbor(direction).equals(ourtile)) {
					qe.add(aitile.getNeighbor(direction));
				}
			}
			if (!qe.isEmpty()) {

				aitile = qe.peek();

				result.add(aitile);

			} 
			else { break;}	
		}		
		result.add(ourtile);
		if(!currentDangerousTiles.isEmpty())
		{
			for(AiTile tiled:currentDangerousTiles)
			{
				this.checkInterruption();
				if(result.contains(tiled))
					result.remove(tiled);
			}
		}		
		
		// simulates our bomb and removes blast range from the safe tiles.
		if (simulate) {
			AiBomb b = simzone.createBomb(simzone.getOwnHero().getTile(), simzone.getOwnHero());
			if(result.contains(b.getTile()))
				result.remove(b.getTile());
			for(AiTile tileb:b.getBlast())
			{
				this.checkInterruption();
				if(result.contains(tileb))
				result.remove(tileb);
			}
			
		}
		if(simulateAllHero)
		{
			for (AiHero aiHero : simzone.getRemainingOpponents()) {		
				this.checkInterruption();
				AiBomb c =simzone.createBomb(simzone.getHeroByColor(aiHero.getColor()).getTile(),simzone.getHeroByColor(aiHero.getColor()));
				if(result.contains(c.getTile()))
					result.remove(c.getTile());
				for(AiTile tileb:c.getBlast())
				{
					this.checkInterruption();
					if(result.contains(tileb))
					result.remove(tileb);
				}
			}
		}		
			
		return result;
	}
	

	/**
	 * @param bomb
	 * @return time to explode of processed bomb
	 * @throws StopRequestException
	 */
	public long processBomb(AiBomb bomb) throws StopRequestException
	{
		this.checkInterruption();
		List<AiBomb> bombList=zone.getBombs();
		long timetobomb=bomb.getNormalDuration()-bomb.getElapsedTime();
		if(!bombList.isEmpty())
		{
			for(AiBomb bomba:bombList)
			{
				this.checkInterruption();
				if(bomba.getBlast().contains(bomb.getTile()))
				{
					long timebomba=bomba.getNormalDuration()-bomba.getElapsedTime();
					if(timebomba<timetobomb)
						timetobomb=timebomba;
				}
			}
		}
		
		return timetobomb;	
	}
		
	

	/**
	 * @return finds your agent's nearest opponent
	 * @throws StopRequestException
	 */
	public AiHero getNearestEnemy() throws StopRequestException {
		this.checkInterruption();
		int enemydist = 10000;
		AiHero nearestEnemy = null;
		zone=getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
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
	 * @param aitile AiTile
	 * @param aitile1 AiTile
	 * @return get distance between aitile and aitile1 with Manhattan algorithm.
	 * @throws StopRequestException
	 */
	public int getDist(AiTile aitile, AiTile aitile1)
			throws StopRequestException {
		this.checkInterruption();
		int distance = Math.abs(aitile.getCol() - aitile1.getCol())
				+ Math.abs(aitile.getRow() - aitile1.getRow());
		return distance;
	}
	
	/**
	 * @param tile
	 * @return true if your bomb in given tile can block an enemy
	 * @throws StopRequestException
	 */
	public Boolean isBlockingEnemy(AiTile tile) throws StopRequestException {
		this.checkInterruption();
		boolean result = true;
		zone=getZone();
		ourhero = zone.getOwnHero();
		ArrayList<AiTile> currentDangerousTiles= getCurrentDangerousTiles();
		int bombrange=ourhero.getBombRange();
		int i=0;
		ourtile = ourhero.getTile();
		AiSimZone simzone = new AiSimZone(zone);
		AiBomb mybomb=simzone.createBomb(tile, simzone.getOwnHero());
		if (!simzone.getRemainingOpponents().isEmpty()) {
			for(AiHero hero:simzone.getRemainingOpponents())
			{
				this.checkInterruption();
				AiTile tilehero=hero.getTile();
				i=0;
				if(mybomb.getBlast().contains(tilehero)){
				while(tilehero.getNeighbor(Direction.DOWN).isCrossableBy(hero, false, false, false, false, true, true)
						&&i<=bombrange&& !tilehero.getNeighbor(Direction.DOWN).equals(mybomb.getTile()))
				{		
					this.checkInterruption();
					tilehero=tilehero.getNeighbor(Direction.DOWN);
					if(result==true){
						if((!tilehero.getNeighbor(Direction.LEFT).isCrossableBy(hero, false, false, false, false, true, true)
								||currentDangerousTiles.contains(tilehero.getNeighbor(Direction.LEFT)))
								&&(!tilehero.getNeighbor(Direction.RIGHT).isCrossableBy(hero, false, false, false, false, true, true)
										||currentDangerousTiles.contains(tilehero.getNeighbor(Direction.RIGHT)))
								&&mybomb.getBlast().contains(tilehero))
							result=true;
						else result=false;
					}
					i++;
				}
				i=0;
				tilehero=hero.getTile();
				while(tilehero.getNeighbor(Direction.UP).isCrossableBy(hero, false, false, false, false, true, true)
						&&i<=bombrange && !tilehero.getNeighbor(Direction.UP).equals(mybomb.getTile()))
				{		
					this.checkInterruption();
					tilehero=tilehero.getNeighbor(Direction.UP);
					if(result==true){
						if((!tilehero.getNeighbor(Direction.LEFT).isCrossableBy(hero, false, false, false, false, true, true)
								||currentDangerousTiles.contains(tilehero.getNeighbor(Direction.LEFT)))
								&&(!tilehero.getNeighbor(Direction.RIGHT).isCrossableBy(hero, false, false, false, false, true, true)
										||currentDangerousTiles.contains(tilehero.getNeighbor(Direction.RIGHT)))
								&&mybomb.getBlast().contains(tilehero))
							result=true;
						else
							result=false;					
					}
					i++;
				}
				i=0;
				tilehero=hero.getTile();
				while(tilehero.getNeighbor(Direction.LEFT).isCrossableBy(hero, false, false, false, false, true, true)
						&& i<=bombrange && !tilehero.getNeighbor(Direction.LEFT).equals(mybomb.getTile()))
				{		
					this.checkInterruption();
					tilehero=tilehero.getNeighbor(Direction.LEFT);
					if(result==true){
						if((!tilehero.getNeighbor(Direction.UP).isCrossableBy(hero, false, false, false, false, true, true)
								||currentDangerousTiles.contains(tilehero.getNeighbor(Direction.UP)))
								&&(!tilehero.getNeighbor(Direction.DOWN).isCrossableBy(hero, false, false, false, false, true, true)
										||currentDangerousTiles.contains(tilehero.getNeighbor(Direction.DOWN)))
								&&mybomb.getBlast().contains(tilehero))
							result=true;
						else
							result=false;
					}
					i++;
				}
				i=0;
				tilehero=hero.getTile();
				while(tilehero.getNeighbor(Direction.RIGHT).isCrossableBy(hero, false, false, false, false, true, true)
						&& i<=bombrange && !tilehero.getNeighbor(Direction.RIGHT).equals(mybomb.getTile()))
				{		
					this.checkInterruption();
					tilehero=tilehero.getNeighbor(Direction.RIGHT);
					if(result==true){
						if((!tilehero.getNeighbor(Direction.UP).isCrossableBy(hero, false, false, false, false, true, true)
								||currentDangerousTiles.contains(tilehero.getNeighbor(Direction.UP)))
								&&(!tilehero.getNeighbor(Direction.DOWN).isCrossableBy(hero, false, false, false, false, true, true)
										||currentDangerousTiles.contains(tilehero.getNeighbor(Direction.DOWN)))
								&&mybomb.getBlast().contains(tilehero))
							result=true;
						
						else
							result=false;
					}
					i++;
				}
				}
				else result=false;
			}
		}
		return result;
	}
	/**
	 * @param arrList
	 * @return true if given list contains null element
	 * 
	 * @throws StopRequestException 
	 */
	public Boolean containsNull(ArrayList<AiTile> arrList) throws StopRequestException
	{	checkInterruption();
		boolean result=true;
	    if(arrList != null)
	    {
	        for(AiTile tile : arrList)
	        {	checkInterruption();
	        
	        	if(tile != null) 
	        		result= false;
	        }
	    }

	    return result;
	}
}
