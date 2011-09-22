package org.totalboumboum.ai.v200809.ais.tirtiltomruk.v1;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.ai.v200809.ais.tirtiltomruk.v1.astaralgorithm.PathFinder;
import org.totalboumboum.ai.v200809.ais.tirtiltomruk.v1.zone.Zone;
import org.totalboumboum.ai.v200809.ais.tirtiltomruk.v1.zone.ZoneEnum;
import org.totalboumboum.engine.content.feature.Direction;

/**
*
* @author Abdullah Tırtıl
* @author Mert Tomruk
*
*/
public class TirtilTomruk extends ArtificialIntelligence
{
	private AiZone zone;
	private AiHero caractere;
	private Vector<AiHero> rivals;
	private AiAction action;
	private AiTile targetDeplacement;
	private AiTile currentTile;
	private AiTile nextTile;
	private AiTile previousTile;
	private LinkedList <AiTile> path;
	private boolean danger = false;
	private Zone zoneAdapted;
	private boolean dropBombe = false;
	private boolean init = false;
	
	
	public AiAction processAction() throws StopRequestException
	{	checkInterruption(); //Appel Obligatoire
	
//		System.out.println("---------------------***********-------------");
//		System.out.println("---------------------*Iteration*-------------");
//		System.out.println("---------------------***********-------------");
		initAI();	
		
		// si ownHero est null, c'est que l'IA est morte : inutile de continuer
		if(caractere != null && !rivals.isEmpty())
		{
			checkDanger();
			
			if(danger)
			{//Il y a un danger, il faut se sauver.
//				System.out.println("DANGER");
				pickNextTile();
				if(isClear(caractere.getTile()))
				{
					action = new AiAction(AiActionName.NONE);
				}
				else
				{
					action = deplace();
				}
			}
			else
			{//On peut attaquer.
//				System.out.println("ATTACK");
				attack();
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
		return action;
	}
	
	private void pickNextTile() throws StopRequestException
	{	checkInterruption(); //Appel Obligatoire
		if (!danger) 
		{
			currentTile = caractere.getTile();
			previousTile = currentTile;
			if (targetDeplacement != caractere.getTile()) {
				PathFinder pathfind = new PathFinder(zone, targetDeplacement,this);
				this.path = pathfind.getPath();

				if (!path.isEmpty()) {
					nextTile = path.poll();
					if (nextTile.equals(caractere.getTile()))
						nextTile = path.poll();
				}
			}
		}
		else
		{
			currentTile = caractere.getTile();
			previousTile = currentTile;
			this.path = getEscapePath();
			if (targetDeplacement != caractere.getTile()) {
				if (!path.isEmpty()) {
					nextTile = path.poll();
					if (nextTile.equals(caractere.getTile()))
						nextTile = path.poll();
				}
			}
		}
	}
	
	private void attack() throws StopRequestException
	{	checkInterruption(); //Appel Obligatoire
		int minDist = 5000;
		Iterator <AiHero> itRivals = rivals.iterator();
		AiHero temp;
		AiHero heroToAttack = null;
		while(itRivals.hasNext())
		{
			checkInterruption(); //Appel Obligatoire
			temp = itRivals.next();			
			if(minDist > Math.abs(temp.getCol() - caractere.getCol()) + Math.abs(temp.getLine() - caractere.getLine()))
			{
				minDist = Math.abs(temp.getCol() - caractere.getCol()) + Math.abs(temp.getLine() - caractere.getLine());
				heroToAttack = temp;
			}
		}
		if(minDist >= 4)
		{	//On est tres loin du rival
			//on doit rapprocher
			this.targetDeplacement = heroToAttack.getTile();
		}
		else
		{	//On peut attaquer
			this.dropBombe = true;
		}
	}
	
	private void checkDanger() throws StopRequestException
	{	checkInterruption(); //Appel obligatoire
		if(!isClear(currentTile) || !isClear(nextTile))
		{
			danger = true;
		}		
		else
			danger = false;
	}
	
	private boolean isClear(AiTile tile) throws StopRequestException
	{	checkInterruption(); //Appel obligatoire
		if(tile == null)
			return true;
		ZoneEnum temp = zoneAdapted.getZoneArray()[tile.getCol()][tile.getLine()];
		return !(temp == ZoneEnum.FEU || temp == ZoneEnum.FEUPOSSIBLE || temp == ZoneEnum.BOMBE);
	}
	
	private LinkedList <AiTile> getEscapePath() throws StopRequestException
	{	checkInterruption(); //Appel Obligatoire
		Vector <AiTile> escapeTiles = new Vector <AiTile>();
		LinkedList <AiTile> result = new LinkedList <AiTile>();
		LinkedList <AiTile> temp = new LinkedList <AiTile>();
		int i,j;
		int minPath = 5000;
		AiTile escapeTileTemp;
		for(i = caractere.getCol() - 2; i <= caractere.getCol() + 2; i++)
		{	checkInterruption(); //Appel Obligatoire
			for(j = caractere.getLine() - 2; j <= caractere.getLine() + 2; j++)
			{	checkInterruption(); //Appel Obligatoire
				if(i != caractere.getCol() || j != caractere.getLine()){
					if(0 < i && i < zone.getWidth() && 0 < j && j < zone.getHeight())
					{
						if(zoneAdapted.getZoneArray()[i][j] != ZoneEnum.FEUPOSSIBLE && zoneAdapted.getZoneArray()[i][j] != ZoneEnum.BOMBE && zoneAdapted.getZoneArray()[i][j] != ZoneEnum.FEU && zoneAdapted.getZoneArray()[i][j] != ZoneEnum.BLOCDEST && zoneAdapted.getZoneArray()[i][j] != ZoneEnum.BLOCINDEST)
							escapeTiles.add(zone.getTile(j, i));
					}
				}
			}
		}
		Iterator <AiTile> itEscape = escapeTiles.iterator();
		while(itEscape.hasNext())
		{	checkInterruption(); //Appel Obligatoire
			escapeTileTemp = itEscape.next();
			PathFinder pathFind = new PathFinder(this.zone,escapeTileTemp,this);
//			System.out.println("Paths essaye:" + escapeTileTemp.toString());
			temp = pathFind.getPath();
//			System.out.println(temp.toString());
			if(minPath > temp.size() && !temp.isEmpty())
			{			
				minPath = temp.size();
				result = temp;
				targetDeplacement = escapeTileTemp;
			}
			/*System.out.println("Place: (" + caractere.getCol() + "," + caractere.getLine() + ")");
			System.out.println("Path trouve:" + pathFind.getPath().toString());
			System.out.println("Min distance:" + minPath);
			*/
		}
//		System.out.println("Place: (" + caractere.getCol() + "," + caractere.getLine() + ")");
//		System.out.println("Path trouve:" + result.toString());
//		System.out.println("Min distance:" + minPath);
		return result;
	}

	private AiAction deplace() throws StopRequestException
	{	checkInterruption(); //Appel Obligatoire
		if(caractere!=null)
		{	// on met à jour la position de l'ia dans la zone
			currentTile = caractere.getTile();
			
			// premier appel : on initialise l'IA
			if(nextTile == null)
			{
				nextTile = currentTile;
				previousTile = currentTile;
			}
			
			// arrivé à destination : on choisit une nouvelle destination
			if(currentTile==nextTile)
				pickNextTile();
			// au cas ou quelqu'un prendrait le Contrôle manuel du personnage
			else if(previousTile!=currentTile)
			{	previousTile = currentTile;
				pickNextTile();			
			}
						
			// on calcule la direction à prendre
			Direction direction = getPercepts().getDirection(currentTile,nextTile);
			
			AiAction result = new AiAction(AiActionName.NONE);
			// on calcule l'action
			if(direction!=Direction.NONE)
				result = new AiAction(AiActionName.MOVE,direction);			
			return result;
		}
		else
			return new AiAction(AiActionName.NONE);
	}

	private void initAI() throws StopRequestException
	{	checkInterruption(); //Appel Obligatoire
		if(!init)
		{
			//On initialise les instances
			zone = getPercepts();
			caractere = zone.getOwnHero();		
			action = new AiAction(AiActionName.NONE);
			rivals = new Vector<AiHero>();
			zoneAdapted = new Zone(zone,this);
			danger = false;
			dropBombe = false;
			if(currentTile == null)
				currentTile = caractere.getTile();
			if(nextTile == null)
				nextTile = currentTile;
			if(previousTile == null)
				previousTile = currentTile;
			if(targetDeplacement == null)
				targetDeplacement = currentTile;
				
			path = new LinkedList<AiTile>();
			Collection <AiHero> tempHeroCol = zone.getHeroes();
			Iterator <AiHero> itTempHero = tempHeroCol.iterator();
			while(itTempHero.hasNext())
			{	checkInterruption(); //Appel Obligatoire
				AiHero tempHero = itTempHero.next();
				if(!tempHero.equals(caractere))
					rivals.add(tempHero);
			}
			init = true;
		}
		else
		{
			//On initialise les instances
			zone = getPercepts();
			caractere = zone.getOwnHero();		
			action = new AiAction(AiActionName.NONE);
			rivals = new Vector<AiHero>();
			zoneAdapted = new Zone(zone,this);
			danger = false;
			dropBombe = false;
			path = new LinkedList<AiTile>();
			Collection <AiHero> tempHeroCol = zone.getHeroes();
			Iterator <AiHero> itTempHero = tempHeroCol.iterator();
			while(itTempHero.hasNext())
			{	checkInterruption(); //Appel Obligatoire
				AiHero tempHero = itTempHero.next();
				if(!tempHero.equals(caractere))
					rivals.add(tempHero);
			}
		}
//		System.out.print(zoneAdapted.toString());
	}
}
