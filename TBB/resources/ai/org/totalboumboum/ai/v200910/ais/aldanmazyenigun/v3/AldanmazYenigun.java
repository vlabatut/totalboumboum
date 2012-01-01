package org.totalboumboum.ai.v200910.ais.aldanmazyenigun.v3;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.ais.aldanmazyenigun.v3.AvoidController;
import org.totalboumboum.ai.v200910.ais.aldanmazyenigun.v3.PathController;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 3
 * 
 * @author Cansın Aldanmaz
 * @author Yalçın Yenigün
 *
 */
@SuppressWarnings("deprecation")
public class AldanmazYenigun extends ArtificialIntelligence 
{
	private AiHero ownHero = null;
	
	private AiZone zone = null;
	
	private AiTile caseActuelle = null;
	
	private AvoidController escapeManager = null;
	
	private SafeStateController safeManager = null;
	
	//private AiItem targetItem;
	
	//private AiBlock targetWall = null;
	
    //private AiTile targetPreviousTile;
		
	/** classe chargée de déterminer quelles cases sont sûres */
	private ZoneFormee zoneFormee = null;
	
	private boolean thereIsSafeTile = true;
	
	/** les coordonnées de notre hero*/
	@SuppressWarnings("unused")
	private double x;
	
	@SuppressWarnings({ "unused"})
	private double y;
	
	@SuppressWarnings("unused")
	private PathController targetManager = null;
	

	@Override
	public AiAction processAction() throws StopRequestException {
		
		checkInterruption(); //APPEL OBLIGATOIRE
		//int i,j;
		
		
		if(ownHero == null)
			init();
		/*	
		for(i=0;i<zone.getWidth();i++){
			for(j=0;j<zone.getHeigh();j++){
				//if(zoneFormee.returnMatrix()[i][j] == 100000 || zoneFormee.returnMatrix()[i][j] == 0)
				//	System.out.print("");
				//else{
					System.out.printf("%1.2f ", zoneFormee.returnMatrix()[i][j]);				
			//}
			 
			 
			
					
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
		
		*/
		
		AiAction result = new AiAction(AiActionName.NONE);
		
		updateMatrix();
				
		if(!ownHero.hasEnded())
		{	// on met à jour la position de l'ia dans la zone
			//init();
			
			
			updateLocation();
			
			
			//zoneFormee.updateZone();
			/*
			if(verbose)
				System.out.println(ownHero.getColor()+": ("+currentTile.getLine()+","+currentTile.getCol()+") ("+currentX+","+currentY+")");*/
			Direction moveDir = Direction.NONE;

			
			// si on est en train de fuir : on continue
			if(escapeManager!=null)
			{	if(escapeManager.hasArrived()){
					escapeManager = null;
				}
				else
					moveDir = escapeManager.update();
			}			
			// sinon si on est en danger : on commence à fuir
			else if(!isSafe(caseActuelle))
			{	escapeManager = new AvoidController(this);
				moveDir = escapeManager.update();
			}
			else if(isSafe(caseActuelle))
			{			
				if(safeManager!=null)
				{
					if(safeManager.hasArrived())
						safeManager = null;
					else
						moveDir = safeManager.update();
				}
			//else if(isSafe(caseActuelle)){
				else if(!isBonus(caseActuelle))
				{	
					safeManager = new SafeStateController(this);
					moveDir = safeManager.update();
				}
			}
						
			// on met à jour la direction renvoyée au moteur du jeu
			result = new AiAction(AiActionName.MOVE,moveDir);
		}
		
		return result;
	}
	
	/*
	private void updateTarget() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		
		if(targetItem==null || targetItem.hasEnded())
		{	chooseBonusWay();
			if(targetItem!=null)
			{	AiTile targetCurrentTile = targetItem.getTile();
				targetManager.setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile; 
			}
		}
		/*
		else if(targetWall==null && targetWall.hasEnded())
		{	
			chooseWallWay();
			if(targetWall!=null)
			{	AiTile targetCurrentTile = targetWall.getTile();
				targetManager.setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile;
			}
		}
		
		else
		{
			
			AiTile targetCurrentTile = targetItem.getTile();
			if(targetCurrentTile==caseActuelle)
			{	double targetX = targetItem.getPosX();
				double targetY = targetItem.getPosY();
				targetManager.setDestination(targetX,targetY);				
			}
			else if(targetCurrentTile!=targetPreviousTile)
			{	targetManager.setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile;				
			}
			
		}
	}
	 */
	
	private void init() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE

		zone = getPercepts();
		ownHero = zone.getOwnHero();
	
		updateLocation();

		targetManager = new PathController(this,caseActuelle);
	}
	
	private void updateMatrix() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE

	    zone = getPercepts();		
		zoneFormee = new ZoneFormee(zone);
		updateLocation();
	}
		
	private void updateLocation() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
				
		caseActuelle = ownHero.getTile();
		x = ownHero.getPosX();
		y = ownHero.getPosY();				
	}
	
	public double getDangerLevel(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return zoneFormee.getDangerLevel(tile);		
	}
	
	public List<AiTile> findSafeTiles(AiTile origin) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		List<AiTile> result = new ArrayList<AiTile>();
		for(int line=0;line<zone.getHeight();line++)
		{	checkInterruption(); //APPEL OBLIGATOIRE
			for(int col=0;col<zone.getWidth();col++)
			{	checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(line,col);
				int x = tile.getCol();
				int y = tile.getLine();
				
				if(zoneFormee.isSafe(x, y))
					result.add(tile);
				/*
				if(result.isEmpty()){
					//this.thereIsSafeTile = false;
					if(zoneFormee.isThereSafeTiles(x, y))
						result.add(tile);
				}
				*/
			}
		}		
		return result;
				
	}
	
	public boolean getThereIsSafeTile(){
		return this.thereIsSafeTile;
	}
	

	public List<AiTile> findItemsTiles(AiTile origin) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		List<AiTile> result = new ArrayList<AiTile>();
		for(int line=0;line<zone.getHeight();line++)
		{	checkInterruption(); //APPEL OBLIGATOIRE
			for(int col=0;col<zone.getWidth();col++)
			{	checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(line,col);
				int x = tile.getCol();
				int y = tile.getLine();
				
				if(zoneFormee.isBonus(x,y))
					result.add(tile);
				
				//ce sont pour les murs mais ça ne marche pas
				/*
				else if(result.isEmpty()){
					if(zoneFormee.isWall(x, y))
					{
						if(zoneFormee.isSafe(x-1,y)){
							tile = zone.getTile(x-1, y);
							result.add(tile);
						}
						if(zoneFormee.isSafe(x,y-1)){
							tile = zone.getTile(x, y-1);
							result.add(tile);

						}
						if(zoneFormee.isSafe(x,y+1)){
							tile = zone.getTile(x, y+1);
							result.add(tile);

						}
						if(zoneFormee.isSafe(x+1,y)){
							tile = zone.getTile(x+1, y);
							result.add(tile);

						}					
					}
				}
				*/
			}
		}		
		return result;
	}
	
	public AiHero getOwnHero() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return ownHero;
	}
	
	public AiZone getZone() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return zone;
	}
	
	public AiTile getActualTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return caseActuelle;
	}
	
	public ZoneFormee getZoneFormee() throws StopRequestException
	{
		checkInterruption(); //APPEL OBLIGATOIRE
		return zoneFormee;
	}
	
	private boolean isSafe(AiTile tile){
		boolean result;
		
		result = zoneFormee.isSafe(tile.getCol(),tile.getLine());
		return result;
	}
	
	private boolean isBonus(AiTile tile){
		boolean result;
		
		result = zoneFormee.isBonus(tile.getCol(),tile.getLine());
		return result;
	}
	
	/*
	private boolean isWall(AiTile tile){
		boolean result;
		
		result = zoneFormee.isWall(tile.getCol(),tile.getLine());
		return result;
	}
	*/
	

		
}
