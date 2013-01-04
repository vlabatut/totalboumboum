package org.totalboumboum.ai.v201213.ais.cinaryalcin.v2;

import java.util.ArrayList;
import java.util.List;
import org.totalboumboum.ai.v201213.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
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
public class CinarYalcin extends ArtificialIntelligence
{
	
	/** 
	 * our zone*/
	AiZone zone;
	/**our hero */
	AiHero ourhero;
	/**our current tile */
	AiTile ourtile;

	
	/**
	 * the list of super bonuses
	 */
	ArrayList<AiItemType> SBonus = new ArrayList<AiItemType>();

	/**
	 * the list of extra bonuses
	 */
	ArrayList<AiItemType> Bonus = new ArrayList<AiItemType>();

	/**
	 * the list of maluses
	 */
	ArrayList<AiItemType> Malus = new ArrayList<AiItemType>();

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
	 * @return get current dangerous tiles in all zone
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> getCurrentDangerousTiles() throws StopRequestException
	{
		this.checkInterruption();
		zone=getZone();
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();
		for ( AiBomb currentBomb : zone.getBombs() )
		{
			this.checkInterruption();
			dangerousTiles.add( currentBomb.getTile() );
			for ( AiTile currentTile : currentBomb.getBlast() )
			{
				this.checkInterruption();
				dangerousTiles.add( currentTile );
			}
		}
		for ( AiFire currentFire : zone.getFires() )
		{
			this.checkInterruption();
			dangerousTiles.add( currentFire.getTile() );
		}
		return dangerousTiles;
	}
	
	/**
	 * @param aitile 
	 * @return return true if selected tile in danger.if wall or items in this tile it will be destroyer.
	 * @throws StopRequestException  */
	public boolean willBeDestroyed(AiTile aitile) throws StopRequestException {
		this.checkInterruption();

		zone = getZone();
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
	 * @param tilec
	 * @return true if your bomb's fire can reach an enemy
	 * @throws StopRequestException 
	 */
	public boolean possibleDangerForEnemy(AiTile tilec) throws StopRequestException
	{
		this.checkInterruption();
		boolean result = false;
		zone = getZone();
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
	 * @param tile
	 * @param simulate
	 * @return finds all secure tiles for given hero
	 * @throws StopRequestException
	 */
	public List<AiTile> updateSafeTiles(AiHero hero, AiTile tile, boolean simulate)
			throws StopRequestException {
		this.checkInterruption();
		zone = getZone();
		AiSimZone simzone = new AiSimZone(zone);
		ArrayList<AiTile> list = new ArrayList<AiTile>();
		ArrayList<AiTile> safe = new ArrayList<AiTile>();
		ArrayList<AiTile> blastflamelist = new ArrayList<AiTile>();
		
		list.add(hero.getTile());

		for (AiBomb b : simzone.getBombs()) {
			this.checkInterruption();

			if (b.getBlast().contains(hero.getTile()))
				continue;

			for (AiTile t : b.getBlast()) {
				this.checkInterruption();
				blastflamelist.add(t);
			}
		}

		for (AiFire aifire : simzone.getFires()) {
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
		// simulates our bomb and removes blast range from the safe tiles.
		if (simulate) {
			AiBomb b = simzone.createBomb(tile, simzone.getOwnHero());
			safe.removeAll(b.getBlast());
		}
		return safe;
	}
		
	

	/**
	 * @return finds your agent's nearest opponent
	 * @throws StopRequestException
	 */
	public AiHero getNearestEnemy() throws StopRequestException {
		this.checkInterruption();
		int enemydist = 10000;
		AiHero nearestEnemy = null;
		zone = getZone();
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
	 * @param aitile
	 * @param aitile1
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
		zone = getZone();
		ourhero = zone.getOwnHero();
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
				while(tilehero.getNeighbor(Direction.DOWN).isCrossableBy(hero, false, false, false, false, true, true)
						&&i<=bombrange+1&& !tilehero.getNeighbor(Direction.DOWN).equals(mybomb.getTile()))
				{		
					this.checkInterruption();
					tilehero=tilehero.getNeighbor(Direction.DOWN);
					if(result==true){
						if(!tilehero.getNeighbor(Direction.LEFT).isCrossableBy(hero, false, false, false, false, true, true)
								&&!tilehero.getNeighbor(Direction.RIGHT).isCrossableBy(hero, false, false, false, false, true, true)
								&&mybomb.getBlast().contains(tilehero))
							result=true;
						else result=false;
					}
					i++;
				}
				i=0;
				tilehero=hero.getTile();
				while(tilehero.getNeighbor(Direction.UP).isCrossableBy(hero, false, false, false, false, true, true)
						&&i<=bombrange+1 && !tilehero.getNeighbor(Direction.UP).equals(mybomb.getTile()))
				{		
					this.checkInterruption();
					tilehero=tilehero.getNeighbor(Direction.UP);
					if(result==true){
						if(!tilehero.getNeighbor(Direction.LEFT).isCrossableBy(hero, false, false, false, false, true, true)
								&&!tilehero.getNeighbor(Direction.RIGHT).isCrossableBy(hero, false, false, false, false, true, true)
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
						&& i<=bombrange+1 && !tilehero.getNeighbor(Direction.LEFT).equals(mybomb.getTile()))
				{		
					this.checkInterruption();
					tilehero=tilehero.getNeighbor(Direction.LEFT);
					if(result==true){
						if(!tilehero.getNeighbor(Direction.UP).isCrossableBy(hero, false, false, false, false, true, true)
								&&!tilehero.getNeighbor(Direction.DOWN).isCrossableBy(hero, false, false, false, false, true, true)
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
						&& i<=bombrange+1 && !tilehero.getNeighbor(Direction.RIGHT).equals(mybomb.getTile()))
				{		
					this.checkInterruption();
					tilehero=tilehero.getNeighbor(Direction.RIGHT);
					if(result==true){
						if(!tilehero.getNeighbor(Direction.UP).isCrossableBy(hero, false, false, false, false, true, true)
								&&!tilehero.getNeighbor(Direction.DOWN).isCrossableBy(hero, false, false, false, false, true, true)
								&&mybomb.getBlast().contains(tilehero))
							result=true;
						
						else
							result=false;
					}
					i++;
				}
				
			}
		}
		return result;
	}
}
