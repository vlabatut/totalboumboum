package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Our bomb handler class.
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class BombHandler extends AiBombHandler<AlcarKayaYildirim>
{	
	/** */
	public AiTile startTile=ai.getZone().getOwnHero().getTile();
	/** */
	protected AiZone zone;
	/** */
	protected AiHero ownHero;
	/** */
	protected AiTile ownTile;
	/** */
	private double currentSpeed = 0;
	/** */
	public static final int BLOCK = 15;
	/** Minumun and maximum utility values of a tile in range of an enemy (with danger=true) */
	public static final int MIN_TILE_IN_RANGE = 10;
	/** */
	public static final int MAX_TILE_IN_RANGE = 13;
	/** Min and max utility values of the other cases when an enemy is in our range. */
	public static final int MIN_TILE_RANGE = 6;
	/** */ 
	public static final int MAX_TILE_RANGE = 9;
	/** int straight refers to the minimum value of utility of the case "Tile Close to destructible wall close to enemy". */
	public static final int STRAIGHT = 30;

	/** Utility values of a tile close to an enemy , when we don't have an enemy  in our selected tiles. */
	public static final int MIN_TILE_CLOSE_ENEMY = 2;
	/** */
	public static final int MAX_TILE_CLOSE_ENEMY = 29;
	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	
	/**
	 * @param ai 
	 * @throws StopRequestException  */

	
	protected BombHandler(AlcarKayaYildirim ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
   	
	}

	
	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	
	/**
	 * @param tile
	 * @return result
	 * @throws StopRequestException
	 */
	public boolean isTileThreatened(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();	
		
		currentSpeed=ownHero.getWalkingSpeed();
		long crossTime = 0;
		
		
		crossTime = Math.round(1000*tile.getSize()/currentSpeed);
		
		
		boolean result = false; 
		List<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> it = bombs.iterator();
		while(!result && it.hasNext())
		{	ai.checkInterruption();	
			
			AiBomb bomb = it.next();
			long timeRemaining = bomb.getNormalDuration() - bomb.getElapsedTime();
			if(!bomb.hasCountdownTrigger() || timeRemaining>crossTime)
			{	List<AiTile> blast = bomb.getBlast();
				result = blast.contains(tile);
			}
		}
		return result;
	}
	
	
    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
	
	boolean result = false;
	zone = ai.getZone();
	ownHero=zone.getOwnHero();
	ownTile = ownHero.getTile();
	AiTile blockedTile=null;
	AiBomb ownBombPt = ownHero.getBombPrototype();
	boolean bombAbsence = ownTile.isCrossableBy(ownBombPt);
	
	boolean herocontrol = true;
	if (!this.ai.getZone().getRemainingOpponents().isEmpty()) {
		for (AiHero aihero : this.ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();
			if (this.ai.utilityHandler.selectTiles().contains(
					aihero.getTile())) {
				herocontrol = false;
				break;
			}

		}
	}
	
	

	if(ai.moveHandler.getCurrentPath()!=null)
	{
		AiPath yol = ai.moveHandler.getCurrentPath();
		Iterator<AiLocation> it = yol.getLocations().iterator();

		AiTile previousTile = null;
		while(it.hasNext() && blockedTile==null)
		{	
			ai.checkInterruption();

			AiLocation location = it.next();
			AiTile tile = location.getTile();
			List<AiBlock> blocks = tile.getBlocks();

			if(!blocks.isEmpty())
				blockedTile = previousTile;
				previousTile = tile;
		}
	}

	//MODE COLLECTE
	
	if(ai.modeHandler.getMode()==AiMode.COLLECTING)
	{
		
		if(bombAbsence)	
		{
			AiTile currentDestination = ai.moveHandler.getCurrentDestination();

			boolean bombPrimaryDestination = ownTile.equals(currentDestination);

			boolean isSafeTileControl=false;
			List<AiTile> blast = ownBombPt.getBlast();
			isSafeTileControl= blast.contains(ai.moveHandler.safeTileControl);

			
			if(ai.moveHandler.safeTile==null)
			{
				if(ai.moveHandler.safeTileControl==null)
				{
					if(bombPrimaryDestination && ownTile!=startTile && ai.moveHandler.getSafeTile())
					{
						result=true;
					}

					if(blockedTile==ownTile && ai.moveHandler.getSafeTile())
					{
						result=true;
					}
						
				}else
				{
					if(zone.getBombsByColor(PredefinedColor.BLUE).isEmpty())
					{

						isSafeTileControl=false;
				
					}
				if(bombPrimaryDestination && ownTile!=startTile && !isSafeTileControl)
					{
						result=true;
					}

				if(blockedTile==ownTile && !isSafeTileControl)
				{
					result=true;
				}
				}
			}else if(isTileThreatened(ai.moveHandler.safeTile))
				{
					if(bombPrimaryDestination && startTile!=ownTile && ownTile!=ai.moveHandler.safeTile && !isSafeTileControl)
					{
						result=true;
					}

					if(blockedTile==ownTile && !isSafeTileControl)
					{
						result=true;
					}
						
				
				}	

		}
	}
	
	/*
	 * 
	 * MODE ATTAQUE
	 * 
	 */
	
	
	else if(ai.modeHandler.getMode()==AiMode.ATTACKING)
	{
		if ((ownHero.getBombNumberCurrent() < ownHero.getBombNumberMax())
				&& ownTile.getBombs().isEmpty()) {
			if (this.ai.getSafeTiles(ownHero, null, true).size() > 0) {

				// List containing tiles which can block the enemy
				ArrayList<AiTile> list = new ArrayList<AiTile>();
				Map<AiTile, Float> hashmap;
				hashmap = this.ai.getUtilityHandler().getUtilitiesByTile();
				for (AiTile currentTile : hashmap.keySet()) {
					this.ai.checkInterruption();

					if (herocontrol == false) {

						if (this.ai.getUtilityHandler()
								.getUtilitiesByTile().get(currentTile) == BLOCK) {
							list.add(currentTile);
						}

						if (!list.isEmpty() && list.contains(ownTile)) {
							return result = true;
						}
					}
				}
				for (Direction direction : Direction.getPrimaryValues()) {
					ai.checkInterruption();

					if (ai.getAnEnemyInMyRange(ownTile, direction, 0) == true) {

						// List1 contains tiles in range of an enemy which
						// will cause danger to him.
						// List2 contains the other tiles in mode attack.
						ArrayList<AiTile> list1 = new ArrayList<AiTile>();
						ArrayList<AiTile> list2 = new ArrayList<AiTile>();
						Map<AiTile, Float> hashmap1;
						hashmap1 = this.ai.getUtilityHandler()
								.getUtilitiesByTile();

						for (AiTile currentTile : hashmap1.keySet()) {
							this.ai.checkInterruption();
							if (this.ai.getUtilityHandler()
									.getUtilitiesByTile().get(currentTile) >= MIN_TILE_IN_RANGE
									&& this.ai.getUtilityHandler()
											.getUtilitiesByTile()
											.get(currentTile) <= MAX_TILE_IN_RANGE) {

								list1.add(currentTile);

							}
							if (this.ai.getUtilityHandler()
									.getUtilitiesByTile().get(currentTile) >= MIN_TILE_RANGE
									&& this.ai.getUtilityHandler()
											.getUtilitiesByTile()
											.get(currentTile) <= MAX_TILE_RANGE) {
								list2.add(currentTile);
							}

						}
						if (list.isEmpty() && !list1.isEmpty()
								&& list1.contains(ownTile)) {
							return result = true;
						}

						if (list1.isEmpty() && !list2.isEmpty()
								&& list2.contains(ownTile)) {
							return result = true;
						}

					}
				}

				for (Direction direction : Direction.getPrimaryValues()) {
					ai.checkInterruption();
					if (herocontrol == true
							&& this.ai.controlOfDestructibleBlock(ownTile,
									direction) == true) {
						// List2 contains tiles on the same line with the
						// nearest enemy (Straight())
						// List3 contains tiles with a certain distance to
						// the nearest enemy.
						ArrayList<AiTile> list2 = new ArrayList<AiTile>();
						ArrayList<AiTile> list3 = new ArrayList<AiTile>();
						Map<AiTile, Float> hashmap2;
						hashmap2 = this.ai.getUtilityHandler()
								.getUtilitiesByTile();

						for (AiTile currentTile : hashmap2.keySet()) {
							this.ai.checkInterruption();
							if (this.ai.getUtilityHandler()
									.getUtilitiesByTile().get(currentTile) >= STRAIGHT) {
								list2.add(currentTile);

							}
							if ((this.ai.getUtilityHandler()
									.getUtilitiesByTile().get(currentTile) >= MIN_TILE_CLOSE_ENEMY
									&& this.ai.getUtilityHandler()
											.getUtilitiesByTile()
											.get(currentTile) <= MAX_TILE_CLOSE_ENEMY && this.ai
									.getUtilityHandler()
									.getUtilitiesByTile().get(currentTile) != 4)) {
								list3.add(currentTile);
							}
						}
						if (!list2.isEmpty() && list2.contains(ownTile)) {

							return result = true;
						}
						if (list2.isEmpty() && !list3.isEmpty()
								&& list3.contains(ownTile)) {

							return result = true;
						}
					}
				}
				return result = false;
			} else {
				return result = false;

			}
		} else {
			return result = false;

		}
	}
	else{
		result = false;
	}
		
		
	
		return result;
	}


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
	}
}
