package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v4;

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
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<AlcarKayaYildirim>
{	
	/** */
	public AiTile startTile = ai.getZone().getOwnHero().getTile();
	/** */
	protected AiZone zone = ai.getZone();
	/** */
	protected AiHero ownHero = zone.getOwnHero();
	/** */
	protected AiTile ownTile = ownHero.getTile();
	/** */

	private double currentSpeed = 0;
	/** Minumun and maximum utility values of a tile in range of an enemy */
	public static final int MIN_TILE_IN_RANGE = 14;
	/** */
	public static final int MAX_TILE_IN_RANGE = 16;
	/** Min and max utility values of the other cases when an enemy is in our range. */
	public static final int MIN_TILE_RANGE = 10;
	/** */ 
	public static final int MAX_TILE_RANGE = 13;
	/** Utility values of a tile close to an enemy , when we don't have an enemy  in our selected tiles. */
	public static final int MIN_TILE_AVANCE_ENEMY = 6;
	/** */
	public static final int MAX_TILE_AVANCE_ENEMY = 8;
	
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
	
	List<AiHero> opponentL = zone.getRemainingOpponents();
	List<AiTile> tileL = ownHero.getBombPrototype().getBlast();
	List<AiBomb> bombL = zone.getBombs();
	AiTile nextTile = ai.moveHandler.nextTile;
	boolean result = false;
	zone = ai.getZone();
	ownHero=zone.getOwnHero();
	ownTile = ownHero.getTile();
	AiTile blockedTile=null;
	AiBomb ownBombPt = ownHero.getBombPrototype();
	boolean bombAbsence = ownTile.isCrossableBy(ownBombPt);
	
	boolean herocontrol = true;
	// We control if we have enemies in the list of tiles returned by SelectTiles.
	if (!this.ai.getZone().getRemainingOpponents().isEmpty()) {
		for (AiHero aihero : this.ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();
			if (this.ai.utilityHandler.selectTiles().contains(aihero.getTile()) && !aihero.equals(ownHero)) {
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
					if (ai.moveHandler.safeTileControl == null) {
						if (bombPrimaryDestination && ownTile != startTile && this.ai.canReachSafety() && ai.moveHandler.getSafeTile()) { // && ai.moveHandler.getSafeTile()
							result = true;
						}

						if (blockedTile == ownTile && this.ai.canReachSafety() && ai.moveHandler.getSafeTile()) { // && ai.moveHandler.getSafeTile()
							result = true;
						}

					} else {
						if (zone.getBombsByColor(PredefinedColor.BLUE).isEmpty()) {
							isSafeTileControl = false;
						}
						if (bombPrimaryDestination && ownTile != startTile && !isSafeTileControl && this.ai.canReachSafety()) {
							result = true;
						}

						if (blockedTile == ownTile && !isSafeTileControl && this.ai.canReachSafety()) {
							result = true;
						}
					}
			}
			else if (isTileThreatened(ai.moveHandler.safeTile)) {
						if (bombPrimaryDestination && startTile != ownTile && ownTile != ai.moveHandler.safeTile
								&& !isSafeTileControl && this.ai.canReachSafety()) {
							result = true;
						}
	
						if (blockedTile == ownTile && !isSafeTileControl && this.ai.canReachSafety()) {
							result = true;
						}
	
					}

		}
		else
			result = false;
	}
	
	// MODE ATTAQUE
	else if(ai.modeHandler.getMode()==AiMode.ATTACKING)
	{
		boolean bombPrimaryDestination = ownTile.equals(ai.moveHandler.getCurrentDestination());
		boolean isSafeTileKont2=false;
		List<AiTile> blast = ownBombPt.getBlast();
		isSafeTileKont2= blast.contains(ai.moveHandler.safeTileControl);
		
	    if ((ownHero.getBombNumberCurrent() < ownHero.getBombNumberMax()) && ownTile.getBombs().isEmpty() && this.ai.canReachSafety() && !this.ai.isHeroInDanger()) {
			if (this.ai.getSafeTiles(ownHero, null, true).size() > 0) {
				//PORTEE DE L'ENNEMI
				if (herocontrol==false && this.ai.isHeroInRange()) {
						// List1 contains tiles in range of an enemy which will cause danger to him.
						// List2 contains the other tiles in mode attack.
						ArrayList<AiTile> list1 = new ArrayList<AiTile>();
						ArrayList<AiTile> list2 = new ArrayList<AiTile>();
						Map<AiTile, Float> hashmap1;
						hashmap1 = this.ai.getUtilityHandler().getUtilitiesByTile();

						for (AiTile currentTile : hashmap1.keySet()) {
							this.ai.checkInterruption();
							if (this.ai.getUtilityHandler().getUtilitiesByTile().get(currentTile) >= MIN_TILE_IN_RANGE
									&& this.ai.getUtilityHandler().getUtilitiesByTile().get(currentTile) <= MAX_TILE_IN_RANGE) {

								list1.add(currentTile);

							}
							if (this.ai.getUtilityHandler().getUtilitiesByTile().get(currentTile) >= MIN_TILE_RANGE
									&& this.ai.getUtilityHandler().getUtilitiesByTile().get(currentTile) <= MAX_TILE_RANGE) {
								list2.add(currentTile);
							}

						}
						if (!list1.isEmpty() && list1.contains(ownTile) && this.ai.canReachSafety()) {
							return result = true;
						}

						if (list1.isEmpty() && !list2.isEmpty() && list2.contains(ownTile) && this.ai.canReachSafety()) {
							return result = true;
						}

					}

				// ADVERSAIRE INACCESSİBLE
				for (Direction direction : Direction.getPrimaryValues()) {
					ai.checkInterruption();
					if (herocontrol == true && this.ai.controlOfDestructibleBlock(ownTile,direction) == true) {		
						// List3 contains tiles with a certain distance to the nearest enemy.
						ArrayList<AiTile> list3 = new ArrayList<AiTile>();
						Map<AiTile, Float> hashmap2;
						hashmap2 = this.ai.getUtilityHandler().getUtilitiesByTile();

						for (AiTile currentTile : hashmap2.keySet()) {
							this.ai.checkInterruption();
							if ((this.ai.getUtilityHandler().getUtilitiesByTile().get(currentTile) >= MIN_TILE_AVANCE_ENEMY
									&& this.ai.getUtilityHandler().getUtilitiesByTile().get(currentTile) <= MAX_TILE_AVANCE_ENEMY)){
								list3.add(currentTile);
							}
						}
								
						if (!list3.isEmpty() && list3.contains(ownTile) && this.ai.canReachSafety()) {
							return result = true;
						}
					}
				}
			
				for(int i = 0;i<opponentL.size();i++)
				{
					ai.checkInterruption();					

					if(tileL.contains(opponentL.get(i).getTile()))
					{
						if(bombL.isEmpty())
						{
							if(!isSafeTileKont2)
								result=true;
						}
						else
						{
							for(int j = 0;j<bombL.size();j++)
							{
								ai.checkInterruption();
								if(!bombL.get(j).getBlast().contains(ownHero.getTile())&&!bombL.get(j).getBlast().contains(nextTile))
								{
									if(!isSafeTileKont2)
										result = true;
								}
							}
						}
					}
				}
				
				if(ai.moveHandler.safeTile==null)
				{
					if(ai.moveHandler.safeTileControl!=null)
					{
						if(bombPrimaryDestination && !isSafeTileKont2)
							result = true;
						
						if(blockedTile==ownTile && ai.moveHandler.getSafeTile())
							result=true;
					}else
					{
						
						if(bombPrimaryDestination && ai.moveHandler.getSafeTile() && ownTile!=startTile)
							result=true;
						if(blockedTile==ownTile && ai.moveHandler.getSafeTile())
							result=true;
					}
						
				}
				if(ai.moveHandler.safeTile!=null)
				{
					if(isTileThreatened(ai.moveHandler.safeTile))
					{
						if(blockedTile!=null)
						{
						if(blockedTile.equals(ownTile) && !isSafeTileKont2)
							result=true;
						}
					}
				}
				return result;
			} else {
				return result = false;
			}
		} else {
			
			for(int i = 0;i<opponentL.size();i++)
			{
				ai.checkInterruption();
				

				if(tileL.contains(opponentL.get(i).getTile()))
				{
					if(bombL.isEmpty())
					{
						if(!isSafeTileKont2)
							result=true;
					}
					else
					{
						for(int j = 0;j<bombL.size();j++)
						{
							ai.checkInterruption();
							if(!bombL.get(j).getBlast().contains(ownHero.getTile())&&!bombL.get(j).getBlast().contains(nextTile))
							{
								if(!isSafeTileKont2)
									result = true;
							}
						}
					}
				}
			}
			
			if(ai.moveHandler.safeTile==null)
			{
				if(ai.moveHandler.safeTileControl!=null)
				{
					if(bombPrimaryDestination && !isSafeTileKont2)
						result = true;
					
					if(blockedTile==ownTile && ai.moveHandler.getSafeTile())
						result=true;
				}else
				{
					
					if(bombPrimaryDestination && ai.moveHandler.getSafeTile() && ownTile!=startTile)
						result=true;
					if(blockedTile==ownTile && ai.moveHandler.getSafeTile())
						result=true;
				}
					
			}
			if(ai.moveHandler.safeTile!=null)
			{
				if(isTileThreatened(ai.moveHandler.safeTile))
				{
					if(blockedTile!=null)
					{
					if(blockedTile.equals(ownTile) && !isSafeTileKont2)
						result=true;
					}
				}
			}
		
			return result;

		}
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
