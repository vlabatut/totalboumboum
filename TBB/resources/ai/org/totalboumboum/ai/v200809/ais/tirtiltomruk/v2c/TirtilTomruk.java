package org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiItem;
import org.totalboumboum.ai.v200809.adapter.AiItemType;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c.astaralgorithm.PathFinder;
import org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c.astaralgorithm.SearchModeEnum;
import org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c.zone.TimedBomb;
import org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c.zone.TimedBombComparator;
import org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c.zone.Zone;
import org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c.zone.ZoneDanger;
import org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c.zone.ZoneEnum;
import org.totalboumboum.engine.content.feature.Direction;

/**
*
* @author Abdullah Tırtıl
* @author Mert Tomruk
*
*/
@SuppressWarnings("deprecation")
public class TirtilTomruk extends ArtificialIntelligence {
	private AiZone zone;
	private AiHero caractere;
	private Vector<AiHero> rivals;
	private AiAction action;
	private AiTile targetDeplacement;
	private AiTile currentTile;
	private AiTile nextTile;
	private AiTile previousTile;
	private LinkedList<AiTile> path;
	private boolean danger = false;
	private Zone zoneAdapted;
	
	private Vector<TimedBomb> timedBombes;
	private boolean dropBombe = false;
	private boolean init = false;
	private long time;
	private boolean isAlone = true;
	private boolean isBlocDest = true;
	private boolean isBonus = false;
	
	public AiAction processAction() throws StopRequestException {
		checkInterruption(); // Appel Obligatoire

		initAI();
		


		// si ownHero est null, c'est que l'IA est morte : inutile de continuer
		if(caractere != null){
		//if (caractere != null && !rivals.isEmpty()) {
			checkDanger();

			if (danger) {// Il y a un danger, il faut se sauver.
			
				pickNextTile();
				action = deplace();
			}
			else
			{
				if(isBonus || isBlocDest){
					if(isAlone)
					{
						
						if(isBonusAccessible())
						{
							pickBonus();
							action = deplace();
						}
						else
						{
							explodeForBonus();
							if (dropBombe)
							{
								action = new AiAction(AiActionName.DROP_BOMB);
								dropBombe = false;
							}
							else
							{
								action = deplace();
							}
						}
					}
					else
					{
						int minDist = Integer.MAX_VALUE;
						Iterator<AiHero> itRivals = rivals.iterator();
						AiHero temp;
						AiHero minDistHero = null;
						while (itRivals.hasNext()) {
							checkInterruption(); // Appel Obligatoire
							temp = itRivals.next();
							PathFinder heroClosePath = new PathFinder(this.zone,temp.getTile(),this,SearchModeEnum.BLOC_DEST_INDEST);
							if(minDist > heroClosePath.getPath().size()&& !heroClosePath.getPath().isEmpty())
							{
								minDist = heroClosePath.getPath().size();
								minDistHero = temp;
							}
						}
						if(minDist >= 6)
						{
							
							if(isBonusAccessible())
							{
								pickBonus();
								action = deplace();
							}
							else
							{
								
								explodeForBonus();
								if (dropBombe)
								{
									action = new AiAction(AiActionName.DROP_BOMB);
									dropBombe = false;
								}
								else
								{
									action = deplace();
								}
							}
						}
						else
						{
							//On est proche d'un rival.
							attackRivalAlpha(minDistHero);
							if(dropBombe)
							{
								action = new AiAction(AiActionName.DROP_BOMB);
								dropBombe = false;
							}
							else
							{
								action = deplace();
							}							
						}
					}
				}
				else
				{/*
					// On peut attaquer.
					System.out.println("ATTACK");
					attack();
					if (dropBombe)
					{
						action = new AiAction(AiActionName.DROP_BOMB);
						dropBombe = false;
					} 
					else
					{
						action = deplace();
					}*/
					
					
					//On est proche d'un rival.
					int minDist = Integer.MAX_VALUE;
					Iterator<AiHero> itRivals = rivals.iterator();
					AiHero temp;
					AiHero minDistHero = null;
					while (itRivals.hasNext()) {
						checkInterruption(); // Appel Obligatoire
						temp = itRivals.next();
						PathFinder heroClosePath = new PathFinder(this.zone,temp.getTile(),this,SearchModeEnum.BLOC_DEST_INDEST);
						if(minDist > heroClosePath.getPath().size() && !heroClosePath.getPath().isEmpty())
						{
							minDist = heroClosePath.getPath().size();
							minDistHero = temp;
						}
					attackRivalAlpha(minDistHero);
					if(dropBombe)
					{
						action = new AiAction(AiActionName.DROP_BOMB);
						dropBombe = false;
					}
					else
					{
						action = deplace();
					}							
				}
			}

		}
		
		
	
		
		/* try {
			 Thread.sleep(100000);
		 } 
		 catch (InterruptedException e) 
		 { 
		 // Auto-generated catch block 
			 e.printStackTrace();
		}*/
		
		
/*
		explodeForBonus();
		System.out.println("TARGET:" + targetDeplacement.toString());
		if (dropBombe)
		{
			action = new AiAction(AiActionName.DROP_BOMB);
			dropBombe = false;
		}
		else
			action = deplace();
*/
	
		
		}
		return action;

		
		 /* Test asd = new Test(getPercepts(),this); try { asd.test(); } catch
		  (InterruptedException e) { //  Auto-generated catch block
		  e.printStackTrace(); } return new AiAction(AiActionName.NONE);
		 */
		
	}
	
	private void pickNextTile() throws StopRequestException {
		checkInterruption(); // Appel Obligatoire
		if (!danger) {
			currentTile = caractere.getTile();
			previousTile = currentTile;
			if (targetDeplacement != caractere.getTile()) {
				PathFinder pathfind = new PathFinder(zone, targetDeplacement,
						this, SearchModeEnum.BLOC_DEST_INDEST_FEU_BOMBE);
				this.path = pathfind.getPath();
			
				if (!path.isEmpty()) {
					nextTile = path.poll();
					if (nextTile.equals(caractere.getTile()))
						nextTile = path.poll();
				}
			}
		} else {
			currentTile = caractere.getTile();
			previousTile = currentTile;
			this.path = getEscapePath();
		
			if (targetDeplacement != caractere.getTile()) {
				if (!path.isEmpty()) {
					AiTile temp = path.poll();
					nextTile = temp;
					if (nextTile.equals(caractere.getTile()))
						nextTile = path.poll();
				}
			}
		}
	}
/*
	private void attack() throws StopRequestException {
		checkInterruption(); // Appel Obligatoire
		int minDist = Integer.MAX_VALUE;
		Iterator<AiHero> itRivals = rivals.iterator();
		AiHero temp;
		AiHero heroToAttack = null;
		while (itRivals.hasNext()) {
			checkInterruption(); // Appel Obligatoire
			temp = itRivals.next();
			if (minDist > Math.abs(temp.getCol() - caractere.getCol())
					+ Math.abs(temp.getLine() - caractere.getLine())) {
				minDist = Math.abs(temp.getCol() - caractere.getCol())
						+ Math.abs(temp.getLine() - caractere.getLine());
				heroToAttack = temp;
			}
		}
		if (minDist >= 4) { // On est tres loin du rival
			// on doit rapprocher
			if(heroToAttack != null)
				this.targetDeplacement = heroToAttack.getTile();
			else
				this.targetDeplacement = caractere.getTile();
		} else { // On peut attaquer
			this.dropBombe = true;
		}
	}
	*/

	private void explodeForBonus() throws StopRequestException
	{checkInterruption(); // Appel Obligatoire
		AiTile temp = bombTileForBonus();
		if(caractere.getTile().equals(temp) && temp != null)
			dropBombe = true;
		else
			if(temp != null)
				this.targetDeplacement = temp;
	}
	
	private void checkDanger() throws StopRequestException {
		checkInterruption(); // Appel obligatoire
		if (!isClear(currentTile) || !isClear(nextTile)) {
			danger = true;
		} else
			danger = false;
	}

	private boolean isClear(AiTile tile) throws StopRequestException {
		checkInterruption(); // Appel obligatoire
		if (tile == null)
			return true;
		ZoneEnum temp = zoneAdapted.getZoneArray()[tile.getCol()][tile
				.getLine()];
		return !(temp == ZoneEnum.FEU || temp == ZoneEnum.FEUPOSSIBLE || temp == ZoneEnum.BOMBE);
	}

	private LinkedList<AiTile> getEscapePath() throws StopRequestException {
		checkInterruption(); // Appel Obligatoire
		Vector<AiTile> escapeTiles = new Vector<AiTile>();
		LinkedList<AiTile> result = new LinkedList<AiTile>();
		LinkedList<AiTile> temp = new LinkedList<AiTile>();
		int i, j;
		int minDistance = Integer.MAX_VALUE;
		double minDanger = Integer.MAX_VALUE;
		AiTile escapeTileTemp;
		for(i = caractere.getCol() - 5; i <= caractere.getCol() + 5; i++) {
			checkInterruption(); // Appel Obligatoire
		//for (i = 0; i < zone.getWidth(); i++) {
		
			for(j = caractere.getLine() - 5; j <= caractere.getLine() + 5; j++) {
				checkInterruption(); // Appel Obligatoire
			//for (j = 0; j <= zone.getHeigh(); j++) {
				
				if (i != caractere.getCol() || j != caractere.getLine()) {
					if (0 < i && i < zone.getWidth() && 0 < j
							&& j < zone.getHeight()) {
						if (zoneAdapted.getZoneArray()[i][j] != ZoneEnum.FEUPOSSIBLE
								&& zoneAdapted.getZoneArray()[i][j] != ZoneEnum.BOMBE
								&& zoneAdapted.getZoneArray()[i][j] != ZoneEnum.FEU
								&& zoneAdapted.getZoneArray()[i][j] != ZoneEnum.BLOCDEST
								&& zoneAdapted.getZoneArray()[i][j] != ZoneEnum.BLOCINDEST)
							escapeTiles.add(zone.getTile(j, i));
					}
				}
			}
		}
		Iterator<AiTile> itEscape = escapeTiles.iterator();
		while (itEscape.hasNext()) {
			checkInterruption(); // Appel Obligatoire
			escapeTileTemp = itEscape.next();
			PathFinder pathFind = new PathFinder(this.zone, escapeTileTemp,
					this, SearchModeEnum.BLOC_DEST_INDEST_FEU_BOMBE);
			temp = pathFind.getPath();
			/*
			 * if(minPath > temp.size() && !temp.isEmpty()) { minPath =
			 * temp.size(); result = temp; targetDeplacement = escapeTileTemp; }
			 */

			if (minDanger * 0.8 + minDistance * 0.2 > this.getPathDanger(temp) * 0.8 + temp.size() * 0.2 && !temp.isEmpty()) {
				minDanger = this.getPathDanger(temp);
				minDistance = temp.size();
				result = temp;
				targetDeplacement = escapeTileTemp;
			}

			/*
			 * System.out.println("Place: (" + caractere.getCol() + "," +
			 * caractere.getLine() + ")"); System.out.println("Path trouve:" +
			 * pathFind.getPath().toString());
			 * System.out.println("Min distance:" + minPath);
			 */

		}
		return result;
	}

	private AiAction deplace() throws StopRequestException {
		checkInterruption(); // Appel Obligatoire
		if (caractere != null) { // on met à jour la position de l'ia dans la
									// zone
			currentTile = caractere.getTile();

			// premier appel : on initialise l'IA
			if (nextTile == null) {
				nextTile = currentTile;
				previousTile = currentTile;
			}

			// arrivé à destination : on choisit une nouvelle destination
			if (currentTile == nextTile)
				pickNextTile();
			// au cas ou quelqu'un prendrait le Contrôle manuel du personnage
			else if (previousTile != currentTile) {
				previousTile = currentTile;
				pickNextTile();
			}

			// on calcule la direction à prendre
			Direction direction = getPercepts().getDirection(currentTile,
					nextTile);

			AiAction result = new AiAction(AiActionName.NONE);
			// on calcule l'action
			if (direction != Direction.NONE)
				result = new AiAction(AiActionName.MOVE, direction);
			/*
			 * DEGISTIRDIM
			 * 
			 * DIKKAT DIKKAT DIKKAT
			 * 
			 * DEGISTIRDIM
			 */
			ZoneDanger zoneD = new ZoneDanger(this.zone,this);
			if((zoneD.getZoneArray()[caractere.getTile().getNeighbor(result.getDirection()).getCol()][caractere.getTile().getNeighbor(result.getDirection()).getLine()]
			                                                                                          ==ZoneEnum.FEUPOSSIBLE_TRESDANGEREUX
			         || zoneD.getZoneArray()[caractere.getTile().getNeighbor(result.getDirection()).getCol()][caractere.getTile().getNeighbor(result.getDirection()).getLine()]
						                                                                                          ==ZoneEnum.FEU)
					&& zoneD.getZoneArray()[caractere.getTile().getCol()][caractere.getTile().getLine()]!=ZoneEnum.FEUPOSSIBLE_TRESDANGEREUX)
			{
				result = new AiAction(AiActionName.NONE);
			}
			return result;
		} else
			return new AiAction(AiActionName.NONE);
	}

	private void initAI() throws StopRequestException {
		checkInterruption(); // Appel Obligatoire
		if (!init) {
			// On initialise les instances
			zone = getPercepts();
			time = time + zone.getElapsedTime();
			refreshTimedBombes();
			caractere = zone.getOwnHero();
			action = new AiAction(AiActionName.NONE);
			rivals = new Vector<AiHero>();
			zoneAdapted = new Zone(zone, this);
		
			danger = false;
			dropBombe = false;
			path = new LinkedList<AiTile>();
			if (currentTile == null)
				currentTile = caractere.getTile();
			if (nextTile == null)
				nextTile = currentTile;
			if (previousTile == null)
				previousTile = currentTile;
			if (targetDeplacement == null)
				targetDeplacement = currentTile;

			path = new LinkedList<AiTile>();
			isBonus = isBonusAccessible();
			isBlocDest = isThereBlocDest();
			Collection<AiHero> tempHeroCol = zone.getHeroes();
			Iterator<AiHero> itTempHero = tempHeroCol.iterator();
			while (itTempHero.hasNext()) {
				checkInterruption(); // Appel Obligatoire
				AiHero tempHero = itTempHero.next();
				if (!tempHero.equals(caractere))
					rivals.add(tempHero);
			}
			PathFinder pathFindAlone;
			isAlone = true;
			Iterator<AiHero> itRivals = rivals.iterator();
			while (isAlone && itRivals.hasNext()) {
				checkInterruption(); // Appel Obligatoire
				AiHero tempRival = itRivals.next();
				pathFindAlone = new PathFinder(this.zone, tempRival.getTile(),
						this, SearchModeEnum.BLOC_DEST_INDEST);
				LinkedList<AiTile> pathRival = pathFindAlone.getPath();
				if (!pathRival.isEmpty()
						|| tempRival.getTile().equals(caractere.getTile()))
					isAlone = false;
			}
			init = true;
		} else {
			// On initialise les instances
			zone = getPercepts();
			time = time + zone.getElapsedTime();
			refreshTimedBombes();
			caractere = zone.getOwnHero();
			action = new AiAction(AiActionName.NONE);
			rivals = new Vector<AiHero>();
			zoneAdapted = new Zone(zone, this);
	
			danger = false;
			dropBombe = false;
			path = new LinkedList<AiTile>();
			isBonus = isBonusAccessible();
			isBlocDest = isThereBlocDest();
			Collection<AiHero> tempHeroCol = zone.getHeroes();
			Iterator<AiHero> itTempHero = tempHeroCol.iterator();
			while (itTempHero.hasNext()) {
				checkInterruption(); // Appel Obligatoire
				AiHero tempHero = itTempHero.next();
				if (!tempHero.equals(caractere))
					rivals.add(tempHero);
			}
			PathFinder pathFindAlone;
			isAlone = true;
			Iterator<AiHero> itRivals = rivals.iterator();
			while (isAlone && itRivals.hasNext()) {
				checkInterruption(); // Appel Obligatoire
				AiHero tempRival = itRivals.next();
				pathFindAlone = new PathFinder(this.zone, tempRival.getTile(),
						this, SearchModeEnum.BLOC_DEST_INDEST);
				LinkedList<AiTile> pathRival = pathFindAlone.getPath();
				if (!pathRival.isEmpty()
						|| tempRival.getTile().equals(caractere.getTile()))
					isAlone = false;
			}
		}
		// System.out.print(zoneAdapted.toString());
		// System.out.println("--------------------------------");
		
		// System.out.print(this.timedBombes + "\n");
	}

	private void refreshTimedBombes() throws StopRequestException {
		checkInterruption(); // Appel Obligatoire
		Collection<AiBomb> bombes = this.zone.getBombs();
		Iterator<AiBomb> itBombes = bombes.iterator();
		Vector<TimedBomb> newTimedBombes = new Vector<TimedBomb>();
		while (itBombes.hasNext()) {
			checkInterruption(); // Appel Obligatoire
			AiBomb tempBomb = itBombes.next();
			TimedBomb tempTimedBomb = new TimedBomb(this.zone, tempBomb, time,
					time,this);
			if (!this.timedBombes.contains(tempTimedBomb)) {
				newTimedBombes.add(tempTimedBomb);
			} else {
				timedBombes.get(timedBombes.indexOf(tempTimedBomb)).setTime(
						time);
				newTimedBombes.add(timedBombes.get(timedBombes
						.indexOf(tempTimedBomb)));
			}
		}
		TimedBombComparator temp = new TimedBombComparator();
		Collections.sort(newTimedBombes, temp);
		this.timedBombes = newTimedBombes;
	}

	@SuppressWarnings("unchecked")
	public double getPathDanger(LinkedList<AiTile> path)
			throws StopRequestException {
		checkInterruption(); // Appel Obligatoire
		int i = 2;
		boolean continueWhile = true;
		double danger = 0;
		LinkedList<AiTile> temp = (LinkedList<AiTile>) path.clone();
		ZoneDanger zoneDangerCreate = new ZoneDanger(this.zone, this);
		ZoneEnum[][] zoneDanger = zoneDangerCreate.getZoneArray();

		AiTile tempTile1;
		if(!temp.isEmpty())
		{
			tempTile1 = temp.poll();
			if(zoneDanger[tempTile1.getCol()][tempTile1.getLine()] == ZoneEnum.FEUPOSSIBLE_TRESDANGEREUX)
				switch (zoneDanger[tempTile1.getCol()][tempTile1.getLine()]) {
				case FEUPOSSIBLE_TRESDANGEREUX:
					danger = Integer.MAX_VALUE;
					continueWhile = false;
					break;
				case FEUPOSSIBLE_DANGEREUX:
					danger += -(2 / 5) * 1 + 12;
					break;
				case FEUPOSSIBLE_PEUDANGEREUX:
					danger += -(1 / 5) * 1 + 10;
					break;
				case FEUPOSSIBLE_PASDANGEREUX:
					danger += -(1 / 10) * 1 + 9;
					break;
				default:
					break;
				}
		}
		while (!temp.isEmpty() && continueWhile) {
			checkInterruption(); // Appel Obligatoire
			AiTile tempTile = temp.poll();
			switch (zoneDanger[tempTile.getCol()][tempTile.getLine()]) {
			case FEUPOSSIBLE_TRESDANGEREUX:
				danger += -1 * i + 13;
				break;
			case FEUPOSSIBLE_DANGEREUX:
				danger += -(2 / 5) * i + 12;
				break;
			case FEUPOSSIBLE_PEUDANGEREUX:
				danger += -(1 / 5) * i + 10;
				break;
			case FEUPOSSIBLE_PASDANGEREUX:
				danger += -(1 / 10) * i + 9;
				break;
			default:
				break;
			}
			i++;
		}
		return danger;
	}
	

	/*
	 * Null dndrebilir ha.
	 */
	public AiTile bombTileForBonus() throws StopRequestException {
		checkInterruption(); // Appel Obligatoire
	/*
		
		Vector<AiTile> blocksDestructibles = new Vector<AiTile>();
		
		for (int i = 0; i < zone.getWidth(); i++) {
			for (int j = 0; j < zone.getHeigh(); j++) {
				if (zone.getTile(j, i).getBlock() != null && zone.getTile(j, i).getBlock().isDestructible())
					blocksDestructibles.add(zone.getTile(j, i));
			}
		}
		
		if (blocksDestructibles.isEmpty())
			return null;
		else
		{
			Vector<AiTile> tilesAccessibles = new Vector<AiTile>();
			tilesAccessibles.add(zone.getOwnHero().getTile());
			// Les tiles accessibles:
			for (int i = 0; i < zone.getHeigh(); i++) 
			{
				for (int j = 0; j < zone.getWidth(); j++) 
				{
					PathFinder pathFind = new PathFinder(this.zone, this.zone.getTile(i, j), this,SearchModeEnum.BLOC_DEST_INDEST);
					if (!pathFind.getPath().isEmpty())
						tilesAccessibles.add(zone.getTile(i, j));
				}
			}
			
			
			//Iterator<AiTile> itTilesAccessibles = tilesAccessibles.iterator();
			
				int maxBlocks = 0;
				int minDistance = Integer.MAX_VALUE;
				AiTile maxBlocksTile = null;
				for(int k = 0; k < tilesAccessibles.size(); k++)
				{
					AiTile temp = tilesAccessibles.get(k);
					//AiTile temp = itTilesAccessibles.next();
					for (int i = temp.getCol() - 3; i < temp.getCol() + 3; i++) 
					{						
						for (int j = temp.getLine() - 3; j < temp.getLine() + 3; j++) 
						{
							if (i >= 0 && i < zone.getWidth() && j >= 0	&& j < zone.getHeigh())
							{
								Zone tempZone = new Zone(this.zone, this);
								ZoneEnum[][] zoneArray = tempZone.simulateBomb(temp);
								
								if (tempZone.getLastSimulatedBombExplodes() != -1) {
									PathFinder pathFindEscape = new PathFinder(	this.zone, zoneArray, temp,	this.zone.getTile(j, i), this,SearchModeEnum.BOMB_SIMULATION);
									//System.out.println("Temp:" + temp.toString() + "Dest:" + this.zone.getTile(j, i).toString());
									LinkedList<AiTile> foundPath = pathFindEscape.getPath();
									
									if (!foundPath.isEmpty() && foundPath != null) 
									{
										if ((maxBlocks * 0.6) - (minDistance * 0.4) < (tempZone.getLastSimulatedBombExplodes() * 0.6) - (foundPath.size() * 0.4))
										//if(maxBlocks < tempZone.getLastSimulatedBombExplodes())
										{
											maxBlocks = tempZone.getLastSimulatedBombExplodes();
											minDistance = foundPath.size();
											maxBlocksTile = temp;
										}
									}
								}
							}
						}		
					}
				}
			System.out.println("BUNU PATLATIRSAM INANILMAZIM:"+ maxBlocksTile.toString());
			return maxBlocksTile;
			}			
		
		*/
		
		double maxBlocks = 0;
		double minDistance = Integer.MAX_VALUE;
		AiTile maxBlocksTile = null;
		
		Zone tempZone = new Zone(this.zone, this);
		for(int i = caractere.getCol() - 3; i <= caractere.getCol() + 3; i++) {
			checkInterruption(); // Appel Obligatoire
			for(int j = caractere.getLine() - 3; j <= caractere.getLine() + 3; j++) {
				checkInterruption(); // Appel Obligatoire
				if (i > 0 && i < zone.getWidth() && j > 0	&& j < zone.getHeight())
				{
					
					AiTile temp = this.zone.getTile(j, i);
					PathFinder isAccessible = new PathFinder(this.zone,tempZone.getZoneArray(),caractere.getTile() ,this.zone.getTile(j, i), this,SearchModeEnum.BLOC_DEST_INDEST);
					LinkedList<AiTile> pathToTile = isAccessible.getPath();
					if (!(pathToTile.isEmpty() && pathToTile != null) || caractere.getTile().equals(zone.getTile(j, i)))
					{
						//System.out.println("ACCESSIBLE KARE:" + this.zone.getTile(j,i));
						//System.out.println("YOL: " + isAccessible.getPath());
						ZoneEnum[][] zoneArraySimulated = tempZone.simulateBomb(temp);
						
						if (tempZone.getLastSimulatedBombExplodes() != -1)
						{
							for(int k = i - 3; k <= i + 3;k++)
							{	checkInterruption();
								for(int l = j - 3; l <= j + 3; l++)
								{	checkInterruption(); // Appel Obligatoire
									if(k > 0 && k < zone.getWidth() && l > 0	&& l < zone.getHeight()){
										PathFinder pathFindEscape = new PathFinder(this.zone,zoneArraySimulated,this.zone.getTile(j,i),this.zone.getTile(l,k),this,SearchModeEnum.BOMB_SIMULATION);							
										LinkedList<AiTile> foundPathEscape = pathFindEscape.getPath();
										if (!foundPathEscape.isEmpty() && foundPathEscape != null) 
										{
											//System.out.println("BOMBA KONABILIR KARE:" + this.zone.getTile(j,i));
											
											if ((maxBlocks * 0.4) - (minDistance * 0.6) < (tempZone.getLastSimulatedBombExplodes() * 0.4) - (pathToTile.size() * 0.6))
											//if(maxBlocks < tempZone.getLastSimulatedBombExplodes())
											{
												maxBlocks = tempZone.getLastSimulatedBombExplodes();
												minDistance = pathToTile.size();
												maxBlocksTile = temp;
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

		return maxBlocksTile;
	}
	
	
	public boolean isThereBlocDest() throws StopRequestException
	{
		checkInterruption(); // Appel Obligatoire
		Vector<AiTile> blocksDestructibles = new Vector<AiTile>();
		
		for (int i = 0; i < zone.getWidth(); i++) {
			checkInterruption(); // Appel Obligatoire
			for (int j = 0; j < zone.getHeight(); j++) {
				checkInterruption(); // Appel Obligatoire
				if (zone.getTile(j, i).getBlock() != null && zone.getTile(j, i).getBlock().isDestructible())
					blocksDestructibles.add(zone.getTile(j, i));
			}
		}
		return !blocksDestructibles.isEmpty();
	}

	
	public Vector<TimedBomb> getTimedBombes() throws StopRequestException {
		checkInterruption(); // Appel Obligatoire
		return timedBombes;
	}

	
	public long getTime() throws StopRequestException {
		checkInterruption(); // Appel Obligatoire
		return time;
	}

	
	public boolean isBonusAccessible() throws StopRequestException
	{
		checkInterruption(); // Appel Obligatoire
		Vector<AiTile> bonus = new Vector<AiTile>();

		Iterator<AiItem> itBonus = this.zone.getItems().iterator();
		while(itBonus.hasNext())
		{	checkInterruption();
			AiItem temp = itBonus.next();
			if(temp.getType() == AiItemType.EXTRA_BOMB || temp.getType() == AiItemType.EXTRA_FLAME) 
				bonus.add(temp.getTile());
		}
		
		Iterator<AiTile> itBonus2 = bonus.iterator();
		while(itBonus2.hasNext())
		{checkInterruption(); // Appel Obligatoire
			AiTile temp = itBonus2.next();
			PathFinder pathFind = new PathFinder(this.zone,temp,this,SearchModeEnum.BLOC_DEST_INDEST);
			if(!pathFind.getPath().isEmpty())
			{
				return true;
			}
		}
		return false;
	}
		
	
	public LinkedList<AiTile> getBonusPath() throws StopRequestException
	{
		checkInterruption(); // Appel Obligatoire
		Vector<AiTile> bonus = new Vector<AiTile>();

		Iterator<AiItem> itBonus = this.zone.getItems().iterator();
		while(itBonus.hasNext())
		{	checkInterruption();
			AiItem temp = itBonus.next();
			if(temp.getType() == AiItemType.EXTRA_BOMB || temp.getType() == AiItemType.EXTRA_FLAME) 
				bonus.add(temp.getTile());
		}
		
		Iterator<AiTile> itBonus2 = bonus.iterator();
		int minDist = Integer.MAX_VALUE;
		LinkedList<AiTile> minDistBonusPath = new LinkedList<AiTile>();
		while(itBonus2.hasNext())
		{checkInterruption(); // Appel Obligatoire
			AiTile temp = itBonus2.next();
			PathFinder pathFind = new PathFinder(this.zone,temp,this,SearchModeEnum.BLOC_DEST_INDEST);
			if(!pathFind.getPath().isEmpty())
			{
				if(pathFind.getPath().size() < minDist)
				{
					minDistBonusPath = pathFind.getPath();
					minDist = pathFind.getPath().size();					
				}
			}
		}
		return minDistBonusPath;
	}

	
	public void pickBonus() throws StopRequestException
	{
		
		checkInterruption(); // Appel Obligatoire
		Vector<AiTile> bonus = new Vector<AiTile>();

		Iterator<AiItem> itBonus = this.zone.getItems().iterator();
		while(itBonus.hasNext())
		{checkInterruption(); // Appel Obligatoire
			AiItem temp = itBonus.next();
			if(temp.getType() == AiItemType.EXTRA_BOMB || temp.getType() == AiItemType.EXTRA_FLAME) 
				bonus.add(temp.getTile());
		}
		
		Iterator<AiTile> itBonus2 = bonus.iterator();
		int minDist = Integer.MAX_VALUE;
		//LinkedList<AiTile> minDistBonusPath = new LinkedList<AiTile>();
		AiTile minDistBonus = caractere.getTile();
		while(itBonus2.hasNext())
		{checkInterruption(); // Appel Obligatoire
			AiTile temp = itBonus2.next();
			PathFinder pathFind = new PathFinder(this.zone,temp,this,SearchModeEnum.BLOC_DEST_INDEST);
			if(!pathFind.getPath().isEmpty())
			{
				if(pathFind.getPath().size() < minDist)
				{
					//minDistBonusPath = pathFind.getPath();
					minDist = pathFind.getPath().size();
					minDistBonus = temp;
				}
			}
		}
		this.targetDeplacement = minDistBonus;
	}

	public int getTileDanger(AiTile tile) throws StopRequestException
	{checkInterruption(); // Appel Obligatoire
		checkInterruption(); // Appel Obligatoire
		ZoneEnum[][] zoneDangerTemp = zoneAdapted.simulateBomb(tile);
		int minDist = Integer.MAX_VALUE;
		for(int i = tile.getCol() - 2; i < tile.getCol() + 2; i++)
		{checkInterruption(); // Appel Obligatoire
			for(int j = tile.getLine() - 2; j < tile.getLine() + 2; j++)
			{checkInterruption(); // Appel Obligatoire
				if(tile.getCol() != i && tile.getLine() != j && i > 1 && j > 1 && i < zone.getWidth() && j < zone.getHeight())
				{
					PathFinder pathFind = new PathFinder(zone,zoneDangerTemp,tile,zone.getTile(j,i),this,SearchModeEnum.BOMB_SIMULATION);
					if(pathFind.getPath().size() < minDist && !pathFind.getPath().isEmpty())
						minDist = pathFind.getPath().size();
				}
			}
		}
		return minDist;
	}		
		
	
	public AiTile attackRivalBombTile(AiHero hero) throws StopRequestException
	{checkInterruption(); // Appel Obligatoire
		int maxPath = 0;
		int minDistCaractere = Integer.MAX_VALUE;
		AiTile maxTile = null;
	

		for(int i = hero.getTile().getCol() - 6; i <= hero.getTile().getCol() + 6; i++)
		{
			checkInterruption(); // Appel Obligatoire
			for(int j = hero.getTile().getLine() - 6; j <= hero.getTile().getLine() + 6; j++)
			{
				checkInterruption(); // Appel Obligatoire
				//Pour une carre qui entoure le rival
				if(j > 1 && i > 1 && j < zone.getHeight()-1 && i < zone.getWidth()-1 && !zone.getTile(j,i).equals(hero.getTile()) && zoneAdapted.getZoneArray()[i][j]==ZoneEnum.LIBRE
						&& Math.abs(hero.getTile().getCol()-i)>1 && Math.abs(hero.getTile().getLine()-j)>1)
				{
					PathFinder pathFinder = new PathFinder(this.zone,zone.getTile(j,i),this,SearchModeEnum.BLOC_DEST_INDEST);
					if(pathFinder.getPath().size() < 6 && pathFinder.getPath()!=null && !pathFinder.getPath().isEmpty())
					{
						int distCaractere = pathFinder.getPath().size();
						
						ZoneEnum[][] zoneDanger = zoneAdapted.simulateBomb(zone.getTile(j,i));
						boolean pathCaractereFound = false;
						for(int k = i - 2; k < i + 2 && !pathCaractereFound;k++)
						{
							checkInterruption(); // Appel Obligatoire
							for(int l = j - 2; l < j + 2 && !pathCaractereFound;l++)
							{
								checkInterruption(); // Appel Obligatoire
								if(l > 1 && k > 1 && l < zone.getHeight() && k < zone.getWidth())
								{
									PathFinder pathCaractereFinder = new PathFinder(this.zone,zoneDanger,zone.getTile(j,i),zone.getTile(l, k),this,SearchModeEnum.BOMB_SIMULATION);
									if(pathCaractereFinder.getPath().size() <= 6 && !pathCaractereFinder.getPath().isEmpty())
										pathCaractereFound = true;
								}
							}
						}
						boolean continueFor = true;
						if(pathCaractereFound){
							for(int m = hero.getTile().getCol() - 3; m < hero.getTile().getCol() + 3 && continueFor;m++)
							{
								checkInterruption(); // Appel Obligatoire
								for(int n = hero.getTile().getLine() - 3; n < hero.getTile().getLine() + 3 && continueFor;n++)
								{	
									checkInterruption(); // Appel Obligatoire
									
									if(n > 1 && m > 1 && n < zone.getHeight() -1  && m < zone.getWidth() - 1)
									{
				
										PathFinder pathRivalFinder = new PathFinder(this.zone,zoneDanger,hero.getTile(),zone.getTile(n,m),this,SearchModeEnum.BOMB_SIMULATION);
										if(((pathRivalFinder.getPath().size() * 0.05 - minDistCaractere * 0.95 < maxPath*0.05 - distCaractere * 0.95)  || (pathRivalFinder.getPath().isEmpty() && distCaractere <= 2)) && zoneAdapted.getZoneArray()[m][n] != ZoneEnum.BLOCDEST && zoneAdapted.getZoneArray()[m][n] != ZoneEnum.BLOCINDEST )
										{
											if(pathRivalFinder.getPath().isEmpty())
											{
												continueFor = false;
												maxTile = this.zone.getTile(n,m);
											}
											else
											{	
												maxPath = pathRivalFinder.getPath().size();
												maxTile = this.zone.getTile(n,m);
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
	
		return maxTile;
	}

	public void attackRival(AiHero hero) throws StopRequestException
	{
		checkInterruption(); // Appel Obligatoire
		
		AiTile bombTile = attackRivalBombTile(hero);
		if(bombTile != null)
		{
			if(!caractere.getTile().equals(bombTile))
			{
				targetDeplacement = bombTile;
			}
			else
			{
				this.dropBombe = true;
			}			
		}		
	}

	public void attackRivalBeta(AiHero hero) throws StopRequestException
	{
		
		checkInterruption(); // Appel Obligatoire
		int minDist = Integer.MAX_VALUE;
		AiTile maxTile = null;
		for(int i = caractere.getCol() - 6; i < caractere.getCol() + 6; i++)
		{
			checkInterruption(); // Appel Obligatoire
			for(int j = caractere.getLine() - 6; j < caractere.getCol() + 6; j++)
			{
				checkInterruption(); // Appel Obligatoire
				if(i > 0 && j > 0 && i < zone.getWidth() -1 && j < zone.getHeight() - 1 && zoneAdapted.getZoneArray()[i][j]!=ZoneEnum.BLOCDEST && zoneAdapted.getZoneArray()[i][j]!=ZoneEnum.BLOCINDEST)
				{
					ZoneEnum simulZone[][] = zoneAdapted.simulateBomb(zone.getTile(j,i));
					PathFinder escapePath = new PathFinder(this.zone,simulZone,caractere.getTile(),zone.getTile(j,i),this,SearchModeEnum.BOMB_SIMULATION);
					//if((!escapePath.getPath().isEmpty() && escapePath.getPath().size() < 6) || caractere.getTile().equals(zone.getTile(j,i)))
					if(!escapePath.getPath().isEmpty() && escapePath.getPath().size() < 6)
					{
						int count = 0;
						for(int k = hero.getCol()-2;k<hero.getCol()+2;k++)
						{
							checkInterruption(); // Appel Obligatoire
							for(int l = hero.getLine()-2;l<hero.getLine()+2;l++)
							{
								checkInterruption(); // Appel Obligatoire
								if(l > 0 && k > 0 && k < zone.getWidth() -1 && l < zone.getHeight() - 1 )
								{
									if(simulZone[k][l]==ZoneEnum.EXPLOSION_SIMULE)
										count++;
								}
							}
						}
						if(count > 1)
						{
							PathFinder pathFind = new PathFinder(this.zone,zone.getTile(j,i),this,SearchModeEnum.BLOC_DEST_INDEST);
							if(minDist > pathFind.getPath().size())
							{
								minDist = pathFind.getPath().size();
								maxTile = zone.getTile(j,i);
							}
						}
					}
				}				
			}
		}
		
		if(maxTile != null)
		{
			if(caractere.getTile().equals(maxTile))
				dropBombe = true;
			else
				targetDeplacement = maxTile;
		}
		else
		{
			targetDeplacement = hero.getTile();
		}
	}
	
	
	public void attackRivalAlpha(AiHero hero) throws StopRequestException
	{	checkInterruption();
		
		AiTile bombTile = null;
		int maxTile = 0;
		Vector<AiTile> safeTiles = new Vector<AiTile>();
		checkInterruption(); // Appel Obligatoire
		for(int i = caractere.getCol() - 1; i <= caractere.getCol() + 1; i++)
		{
			checkInterruption(); // Appel Obligatoire
			for(int j = caractere.getLine() - 1; j <= caractere.getLine() + 1; j++)
			{
			
				checkInterruption(); // Appel Obligatoire
				//Pour un carre de 3x3 qui entoure le caractere
				if(i > 1 && j > 1 && i < zone.getWidth() - 1 && j < zone.getHeight() - 1 && zoneAdapted.getZoneArray()[i][j] != ZoneEnum.BLOCDEST && zoneAdapted.getZoneArray()[i][j] != ZoneEnum.BLOCINDEST){
				
				//Est-ce qu'on peut se sauver?
				boolean escapeFound = false;
				ZoneEnum[][] zoneDanger = zoneAdapted.simulateBomb(zone.getTile(j,i));
				for(int k = i - 3; k <= i + 3; k++)
				{	checkInterruption();
					for(int l = j - 3; l <= j + 3;l++)
					{	checkInterruption();
						
						if(k > 1 && l > 1 && k < zone.getWidth() - 1 && l < zone.getHeight() - 1){
						PathFinder pathFind = new PathFinder(zone,zoneDanger,zone.getTile(j,i),zone.getTile(l,k),this,SearchModeEnum.BOMB_SIMULATION);
						if(pathFind.getPath().size() < 6 && !pathFind.getPath().isEmpty())
						{
							escapeFound = true;
							safeTiles.add(zone.getTile(l,k));
						}							
						}
					}
				}
				
				if(escapeFound)
				{					
					//On peut se sauver.
					//Est-ce qu'il peut se sauver
					int minPath = Integer.MAX_VALUE;
					for(int m = hero.getTile().getCol() - 3; m <= hero.getTile().getCol() + 3; m++)
					{	checkInterruption();
						for(int n = hero.getTile().getLine() - 3; n <= hero.getTile().getLine() + 3; n++)
						{	checkInterruption();
						
							if(m > 1 && n > 1 && m < zone.getWidth() - 1 && n < zone.getHeight() -1){
							PathFinder pathFindRival = new PathFinder(zone,zoneDanger,hero.getTile(),zone.getTile(n,m),this,SearchModeEnum.BOMB_SIMULATION);
							if(pathFindRival.getPath().size() < minPath || pathFindRival.getPath().isEmpty())
							{
								if(!hero.getTile().equals(zone.getTile(n,m)))
								{
									minPath = Integer.MAX_VALUE;
								}
								else
								{
									minPath = pathFindRival.getPath().size();
								}
							}
						}}
					}
					if(maxTile < minPath)
					{
						bombTile = zone.getTile(j,i);
					}
				}	
			
			}
		}}
		
		if(bombTile != null)
		{
			
			if(caractere.getTile().equals(bombTile))
				dropBombe = true;
			else
				targetDeplacement = bombTile;
		}
		else
		{
			
			if(caractere.getTile().equals(hero.getTile()))
				dropBombe = true;
			else
				targetDeplacement = hero.getTile();
		}
	}
	
}
