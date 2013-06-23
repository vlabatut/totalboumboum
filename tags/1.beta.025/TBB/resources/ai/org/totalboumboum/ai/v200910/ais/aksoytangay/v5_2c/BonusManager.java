package org.totalboumboum.ai.v200910.ais.aksoytangay.v5_2c;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 5.2.c
 * 
 * @author Cihan Aksoy
 * @author Necmi Murat Tangay
 *
 */
@SuppressWarnings("deprecation")
public class BonusManager {

	/** */
	private AksoyTangay myAI; 
	
	/** */
	private PathManager pathManager;
	
	/** */
	private AiPath path;
	
	/** */
	private AiTile currentTile;
	
	/** */
	private AiTile lastTile;
	
	/** */
	private AiZone percepts;
	
//	public List<AiTile> availibleTilesDirect;
//	
//	public List<AiTile> availibleTilesIndirect;
		
	/** */
	private Zone zone;
	
	/**
	 * 
	 * @param myAI
	 * 		Description manquante !
	 * @param percepts
	 * 		Description manquante !
	 * @param pathManager
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public BonusManager(AksoyTangay myAI, AiZone percepts, PathManager pathManager) throws StopRequestException
	{
		myAI.checkInterruption();
		
		this.myAI = myAI;
		this.percepts = percepts;
		this.pathManager = pathManager;
		
		
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Direction getDirectionToCollectBonus() throws StopRequestException
	{
		myAI.checkInterruption();
		
		Direction result = Direction.NONE;
		
		List<AiTile> availibleTilesDirect = pathManager.getAvailibleTilesDirectToCollectBonus();
		List<AiTile> availibleTilesIndirect = pathManager.getAvailibleTilesIndirectToCollectBonus();
		path = pathManager.getBestPathToCollectBonus(availibleTilesDirect, availibleTilesIndirect);
		lastTile = path.getLastTile();
		
		lastTile = path.getLastTile();
		//system.out.println(path.toString());
		AiTile tempTile = null;
		
		if(!finishedPath())
		{
			checkIsOnPath();
			if(path.isEmpty() || modifiedPath())
				path = pathManager.getBestPathToCollectBonus(availibleTilesDirect, availibleTilesIndirect);
			
			if(path.getLength()>1)
				tempTile = path.getTile(1);
			else if(path.getLength()>0)
				tempTile = path.getTile(0);
			result = percepts.getDirection(myAI.getOwnHero(), tempTile);
			
			//system.out.println("getDirection.escman" + result.name());
		}
		
		////destructible olup olmamaya bakp ya bonusu direk al ya da bomba koyup al!!! bu direction da yaplrm???
		
		return result;
		
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void checkIsOnPath() throws StopRequestException
	{	myAI.checkInterruption(); //APPEL OBLIGATOIRE
	
		AiTile currentTile = myAI.getOwnHero().getTile();
		while(!path.isEmpty() && path.getTile(0)!=currentTile)
		{	myAI.checkInterruption(); //APPEL OBLIGATOIRE
			
			path.removeTile(0);
		}
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean finishedPath() throws StopRequestException
	{
		myAI.checkInterruption();
		
		currentTile = myAI.getOwnHero().getTile();
		
		return (lastTile == currentTile);
			
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean modifiedPath() throws StopRequestException
	{
		myAI.checkInterruption(); //APPEL OBLIGATOIRE
		
		boolean result = true;
		
		Iterator<AiTile> it = path.getTiles().iterator();
		AiTile tempTile;
		while(it.hasNext() && result)
		{	myAI.checkInterruption(); //APPEL OBLIGATOIRE
			
			tempTile = it.next();
			result = tempTile.isCrossableBy(myAI.getOwnHero());			
		}
		return result;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean hasExplosed() throws StopRequestException
	{
		myAI.checkInterruption();
		
		AiTile tempTileUp = null;
		AiTile tempTileDown = null;
		AiTile tempTileLeft = null;
		AiTile tempTileRight = null;
		
		if(!pathManager.temp)
		{
			//path = escapeManager.path;
			AiTile tempLastTile = path.getLastTile();
			int x = tempLastTile.getLine();
			int y = tempLastTile.getCol();
			
			
			tempTileUp = percepts.getTile(x, y).getNeighbor(Direction.UP);
			tempTileDown = percepts.getTile(x, y).getNeighbor(Direction.DOWN);
			tempTileLeft = percepts.getTile(x, y).getNeighbor(Direction.LEFT);
			tempTileRight = percepts.getTile(x, y).getNeighbor(Direction.RIGHT);
						
			return ((zone.getMatrix()[tempTileUp.getLine()][tempTileUp.getCol()] != State.DESTRUCTIBLE)&&
					(zone.getMatrix()[tempTileDown.getLine()][tempTileDown.getCol()] != State.DESTRUCTIBLE)&&
					(zone.getMatrix()[tempTileLeft.getLine()][tempTileLeft.getCol()] != State.DESTRUCTIBLE)&&
					(zone.getMatrix()[tempTileRight.getLine()][tempTileRight.getCol()] != State.DESTRUCTIBLE));
		}
		else
			return false;
		
	}
	
//	public boolean lastTileIsDestructible() throws StopRequestException
//	{
//		myAI.checkInterruption();
//		
//		zone = new Zone(percepts, myAI);
//		
//		System.out.println("lastTileis Destructible??");
//				
//		return(zone.getMatrix()[lastTile.getLine()][lastTile.getCol()] == State.DESTRUCTIBLE);
//			
//	}
	
//	public boolean beforeFinishedPath() throws StopRequestException
//	{
//		myAI.checkInterruption();
//		
//		currentTile = myAI.getOwnHero().getTile();
//		
//		List<AiTile> tempTiles = path.getTiles();
//		int sizeTiles = tempTiles.size();
//				
//		System.out.println("beforeFinishedPath\n size tile : "+tempTiles.get(sizeTiles-2).toString());
//		return (currentTile==tempTiles.get(sizeTiles-2));
//		
//	}
}
