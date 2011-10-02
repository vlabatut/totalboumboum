package org.totalboumboum.ai.v200910.ais.aksoytangay.v5_1;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 5.1
 * 
 * @author Cihan Aksoy
 * @author Necmi Murat Tangay
 *
 */
@SuppressWarnings("deprecation")
public class BonusManager {

	private AksoyTangay myAI; 
	
	private PathManager pathManager;
	
	private AiPath path;
	
	private AiTile currentTile;
	
	private AiTile lastTile;
	
	private AiZone percepts;
		
	@SuppressWarnings("unused")
	private Zone zone;
	
	public BonusManager(AksoyTangay myAI, AiZone percepts, PathManager pathManager) throws StopRequestException
	{
		myAI.checkInterruption();
		
		this.myAI = myAI;
		this.percepts = percepts;
		this.pathManager = pathManager;
	}
	
	public Direction getDirectionToCollectBonus() throws StopRequestException
	{
		myAI.checkInterruption();
		
		Direction result = Direction.NONE;
		List<AiTile> availibleTilesDirect = pathManager.getAvailibleTilesDirectToCollectBonus();
		List<AiTile> availibleTilesIndirect = pathManager.getAvailibleTilesIndirectToCollectBonus();
		path = pathManager.getBestPathToCollectBonus(availibleTilesDirect, availibleTilesIndirect);
		
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
	
	private void checkIsOnPath() throws StopRequestException
	{	myAI.checkInterruption(); //APPEL OBLIGATOIRE
	
		AiTile currentTile = myAI.getOwnHero().getTile();
		while(!path.isEmpty() && path.getTile(0)!=currentTile)
		{	myAI.checkInterruption(); //APPEL OBLIGATOIRE
			
			path.removeTile(0);
		}
	}
	
	public boolean finishedPath() throws StopRequestException
	{
		myAI.checkInterruption();
		
		currentTile = myAI.getOwnHero().getTile();
		
		return (lastTile == currentTile);
			
	}
	
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
