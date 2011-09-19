package org.totalboumboum.ai.v200910.ais.aksoytangay.v5_2;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;

import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 5.2
 * 
 * @author Cihan Aksoy
 * @author Necmi Murat Tangay
 *
 */
public class EscapeManager {
	
	private AksoyTangay myAI; 
	
	private PathManager pathManager;
	
	public AiPath path = null;
	
	private AiTile currentTile;
		
	private AiTile lastTile;
	
	private AiZone percepts;
	
	
	
	public EscapeManager(AksoyTangay myAI, AiZone percepts, PathManager pathManager) throws StopRequestException
	{
		myAI.checkInterruption();
		
		this.myAI = myAI;
		this.pathManager = pathManager;
		this.percepts = percepts;
		
		List<AiTile> availibleTiles = pathManager.getAvailibleTilesToEscape();
		path = pathManager.getBestPathToEscape(availibleTiles);
		lastTile = path.getLastTile();
	}
	
	public Direction getDirectionToEscape() throws StopRequestException
	{
		myAI.checkInterruption();
		
		Direction result = Direction.NONE;
		
//		List<AiTile> availibleTiles = pathManager.getAvailibleTilesToEscape();
//		path = pathManager.getBestPathToEscape(availibleTiles);
//		lastTile = path.getLastTile();
		
		
		//bi kere al sonra yol bitene kadar alma
		
		
		AiTile tempTile = null;
		
		if(!finishedPath())
		{
			removeUsedTilesOfPath();
			if(path.isEmpty() || !modifiedPath())
			{
				List<AiTile> availibleTiles = pathManager.getAvailibleTilesToEscape();
				path = pathManager.getBestPathToEscape(availibleTiles);
				lastTile = path.getLastTile();
				
//				boolean res1 = path.isEmpty();
//				boolean res2 = modifiedPath();
//				if(res1&&res2)
//					System.out.println("bloa girdi...isEmpty&modPathIsTrue");
//				else if(res1&&!res2)
//					System.out.println("sadece isEmptyTrue");
//				else
//					System.out.println("sadece modPath true");
					
			}
//			if(myAI.getOwnHero().getLine() == path.getTile(0).getLine() && myAI.getOwnHero().getCol() == path.getTile(0).getCol())
//				path.getTiles().remove(0);
			
			if(path.getLength()>1)
				tempTile = path.getTile(1);
			else if(path.getLength()>0)
				tempTile = path.getTile(0);
			
			result = percepts.getDirection(myAI.getOwnHero(), tempTile);
			
						
			//System.out.println("getDirection.escman" + result.name());
		}
		
		
		return result;
	}
	
	private void removeUsedTilesOfPath() throws StopRequestException
	{	myAI.checkInterruption(); //APPEL OBLIGATOIRE
	
		AiTile curTile = myAI.getOwnHero().getTile();
		
		while(!path.isEmpty() && path.getTile(0)!=curTile)
		{	myAI.checkInterruption(); //APPEL OBLIGATOIRE
			
			path.removeTile(0);
		}
	}
	
	public boolean finishedPath() throws StopRequestException
	{
		myAI.checkInterruption();
		
		currentTile = myAI.getOwnHero().getTile();
		
//		System.out.println("finisH??");
		//System.out.println("last tile and current tile "+lastTile.toString()+" "+currentTile.toString() );
		
		//return (lastTile.equals(currentTile));
		return(lastTile == currentTile);
				
	}
	
	public boolean modifiedPath() throws StopRequestException
	{
		myAI.checkInterruption(); //APPEL OBLIGATOIRE
		
		boolean result = true;
		
		Iterator<AiTile> itrTiles = path.getTiles().iterator();
		AiTile tempTile;
		
		while(itrTiles.hasNext() && result)
		{	myAI.checkInterruption(); //APPEL OBLIGATOIRE
			
			tempTile = itrTiles.next();
			//on regarde si le chemin peut etre encore alle
			result = tempTile.isCrossableBy(myAI.getOwnHero());			
		}
		return result;
	}

	public AiTile getCurrentTile() {
		return myAI.getOwnHero().getTile();
	}
	
	
//	public boolean changedTile() throws StopRequestException
//	{
//		myAI.checkInterruption();
//		
//		AiTile tTile = path.getTile(0);
//		
//		if(myAI.getOwnHero().getTile() == tTile)
//			return false;
//		else
//			return true;
//		
//	}
}
