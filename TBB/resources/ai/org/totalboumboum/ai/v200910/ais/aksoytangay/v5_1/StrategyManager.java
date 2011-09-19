package org.totalboumboum.ai.v200910.ais.aksoytangay.v5_1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
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
public class StrategyManager {

	private AksoyTangay myAI;
	
	private PathManager pathManager;
	
	private EscapeManager escapeManager;
	
	private AiPath path;
	
	private AiTile currentTile;
	
	private AiTile lastTile;
	
	AiTile tTypeEscapeTile = null;
		
	private AiZone percepts;
	
	private Zone zone;
	
	List<AiBomb> bombs;
		
	
	public StrategyManager(AksoyTangay myAI, AiZone percepts, Zone zone, PathManager pathManager, EscapeManager escapeManager) throws StopRequestException
	{
		myAI.checkInterruption();
		
		this.myAI = myAI;
		this.percepts = percepts;
		this.zone = zone;
		this.pathManager = pathManager;
		this.escapeManager = escapeManager;
		
		//system.out.println("strategy man init...");
	}
	
	//////////////////////Defans staratejileri            ///////////////////////////////////////////////
	/*
	 * cette fonx retourn vrai si plusieurs bombe nous menace avec le type t, 
	 * sinon(si le type de menace est I) elle renvoi faux
	 */
	
	public Direction typeDefiningAndGettingDirection() throws StopRequestException
	{
		myAI.checkInterruption();
		
		Direction result = Direction.NONE;
		
		bombs = bombsMenaceMe();
		//system.out.println("bombe size!! : "+bombs.size()+" ve onlar unlar : "+bombs.toString());
		
		List<AiTile> tmpTiles = new ArrayList<AiTile>();
		Iterator<AiBomb> itrBomb = bombs.iterator();
		AiBomb tmpBomb;
				
		//s'il y a deux bombes
		if(bombs.size()==2)
		{
			while(itrBomb.hasNext())
			{
				myAI.checkInterruption();
				tmpBomb = itrBomb.next();
				
				tmpTiles.add(tmpBomb.getTile());				
			}
			//system.out.println("tmpTiles size : "+tmpTiles.size()+" ve bu tile lar: "+tmpTiles.toString());
			
			AiTile tTile;
			
			//si les colonnes ou les lignes des deux bombes sont la meme, ça veut dire I ou - type, pas t ou L type.
			if((tmpTiles.get(0).getCol() == tmpTiles.get(1).getCol()) || (tmpTiles.get(0).getLine() == tmpTiles.get(1).getLine()))
			{
				tTile = iTypeEscape(bombs);
				result = percepts.getDirection(myAI.getOwnHero(), tTile);
				//system.out.println("index hatas verilen yerde if blondaym");
			}
			//sinon c'est la menace de t ou L type
			else
			{
				//system.out.println("index hatas verilen yerde else bloundaym");
				
				//si les deux bombes sont loins que nous plus d'1 cas
				if(!escapeManager.getCurrentTile().getNeighbors().contains(tmpTiles.get(0)) && 
						!escapeManager.getCurrentTile().getNeighbors().contains(tmpTiles.get(1)))
				{
					tTile = tTypeEscape(bombs);
					result = percepts.getDirection(myAI.getOwnHero(), tTile);
				}
				//si un des bombes est voisin
				else if((!escapeManager.getCurrentTile().getNeighbors().contains(tmpTiles.get(0)) && 
						escapeManager.getCurrentTile().getNeighbors().contains(tmpTiles.get(1))) ||
						(!escapeManager.getCurrentTile().getNeighbors().contains(tmpTiles.get(1)) && 
						escapeManager.getCurrentTile().getNeighbors().contains(tmpTiles.get(0))))
				{
					tTile = tTypeEscapeWithOneNeighbor();
					result = percepts.getDirection(myAI.getOwnHero(), tTile);
				}
				//si les deux bombes sont voisin
				else
				{
					//si le premier fois, on prend un case de destination
					tTypeEscapeTile = escapeFromOneOfBomb(bombs);
					result = percepts.getDirection(myAI.getOwnHero(), tTypeEscapeTile);
				}
				
			}
		}
		else if(bombs.size()==3 || bombs.size()==4)
		{
			tTypeEscapeTile = escapeFromOneOfBomb(bombs);
			result = percepts.getDirection(myAI.getOwnHero(), tTypeEscapeTile);
		}
		
		
		//system.out.println("typeDefiner : "+result.toString());
		
		return result;
	}
	
	
	
	public List<AiBomb> bombsMenaceMe() throws StopRequestException
	{
		myAI.checkInterruption();
		
		List<AiBomb> result = new ArrayList<AiBomb>();
		Collection<AiBomb> bombs = zone.getBombs();
		List<AiTile> tiles;
		
		Iterator<AiBomb> itrBomb = bombs.iterator();
		AiBomb tmpBomb = null;
		
		while(itrBomb.hasNext())
		{
			myAI.checkInterruption();
			tmpBomb = itrBomb.next();
			//system.out.println("tmpBomb : "+ tmpBomb.toString());
			tiles = tmpBomb.getBlast();
			//system.out.println("tiles : "+tiles.toString());
			Iterator<AiTile> itrTile = tiles.iterator();
			AiTile tmpTile;
			while(itrTile.hasNext())
			{
				myAI.checkInterruption();
				tmpTile = itrTile.next();
				//system.out.print("tmpTile : "+tmpTile.toString());
				
//				if(tmpTile == escapeManager.getCurrentTile())
//				{
//					//system.out.println("bombay ekliyoruz!!!!!!!!");
//					System.out.println("current tile : "+escapeManager.getCurrentTile());
//					result.add(tmpBomb);
//				}
				
				if(tmpTile == myAI.getOwnHeroTile())
				{
					//system.out.println("bombay ekliyoruz!!!!!!!!");
					//system.out.println("current tile : "+myAI.getOwnHeroTile().toString());
					result.add(tmpBomb);
				}
					
			}
		}
		
		//system.out.println("bombs menaceme : "+result.toString());
		
		return result;
		
	}
	
	
	public AiTile iTypeEscape(List<AiBomb> bombs) throws StopRequestException
	{
		myAI.checkInterruption();
				
		//si on est dans la meme colonne avec les deux bombes
		if(bombs.get(0).getCol() == escapeManager.getCurrentTile().getCol())
		{
			if(zone.getMatrix()[escapeManager.getCurrentTile().getLine()][escapeManager.getCurrentTile().getCol()+1] != State.SEVERALDANGERS ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()][escapeManager.getCurrentTile().getCol()+1] != State.BOMBE ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()][escapeManager.getCurrentTile().getCol()+1] != State.INDESTRUCTIBLE ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()][escapeManager.getCurrentTile().getCol()+1] != State.DESTRUCTIBLE)
				tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()+1);
			else
				tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()-1);
		}
		//si on est dans la meme ligne avec les deux bombes
		else
		{
			if(zone.getMatrix()[escapeManager.getCurrentTile().getLine()+1][escapeManager.getCurrentTile().getCol()] != State.SEVERALDANGERS ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()+1][escapeManager.getCurrentTile().getCol()] != State.BOMBE ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()+1][escapeManager.getCurrentTile().getCol()] != State.INDESTRUCTIBLE ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()+1][escapeManager.getCurrentTile().getCol()] != State.DESTRUCTIBLE)
				tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine()+1, escapeManager.getCurrentTile().getCol());
			else
				tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine()-1, escapeManager.getCurrentTile().getCol());
		}
		
		return tTypeEscapeTile;		
	}
	
	public AiTile tTypeEscape(List<AiBomb> bombs) throws StopRequestException
	{
		myAI.checkInterruption();
				
		//on considere le temps d'explosion des bombes ici
		
		
		
		
		
		//si bombe1 exploit apres
		if((bombs.get(0).getNormalDuration()-bombs.get(0).getTime()) < (bombs.get(1).getNormalDuration()-bombs.get(1).getTime()))
		{
			//bombe1 est dans le meme colonne ou le meme ligne avec moi?
			if(bombs.get(1).getCol() == escapeManager.getCurrentTile().getCol())
			{
				if(bombs.get(1).getLine() < escapeManager.getCurrentTile().getLine())
					tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine()-1, escapeManager.getCurrentTile().getCol());
				else
					tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine()+1, escapeManager.getCurrentTile().getCol());
				
			}
			else
			{
				if(bombs.get(1).getCol() < escapeManager.getCurrentTile().getCol())
					tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()-1);
				else
					tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()+1);
			}
		}
		//si bombe0 exploit apres
		else
		{
			//bombe0 est dans le meme colonne ou le meme ligne avec moi?
			if(bombs.get(0).getCol() == escapeManager.getCurrentTile().getCol())
			{
				if(bombs.get(0).getLine() < escapeManager.getCurrentTile().getLine())
					tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine()-1, escapeManager.getCurrentTile().getCol());
				else
					tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine()+1, escapeManager.getCurrentTile().getCol());
				
			}
			else
			{
				if(bombs.get(0).getCol() < escapeManager.getCurrentTile().getCol())
					tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()-1);
				else
					tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()+1);
			}
		}
		
		//system.out.println("tTypeEscapeTile fonx : "+tTypeEscapeTile.toString());
		
		return tTypeEscapeTile;
	}
	
	
	public AiTile tTypeEscapeWithOneNeighbor() throws StopRequestException
	{
		myAI.checkInterruption();
				
		//on considere le temps d'explosion des bombes ici
		
		
		
		List<AiBomb> bombs = bombsMenaceMe();
		
		AiBomb neighborBomb = null;
		AiBomb otherBomb = null;
		
		if(escapeManager.getCurrentTile().getNeighbors().contains(bombs.get(0)))
		{
			neighborBomb = bombs.get(0);
			otherBomb = bombs.get(1);
		}
		else
		{
			neighborBomb = bombs.get(1);
			otherBomb = bombs.get(0);
		}
		
		//si le bomb voisin exploit avant c'est ok..
		if((neighborBomb.getNormalDuration()-neighborBomb.getTime()) < (otherBomb.getNormalDuration()-otherBomb.getTime()))
		{
			//bombe1 est dans le meme colonne ou le meme ligne avec moi?
			if(otherBomb.getCol() == escapeManager.getCurrentTile().getCol())
			{
				if(otherBomb.getLine() < escapeManager.getCurrentTile().getLine())
					tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine()-1, escapeManager.getCurrentTile().getCol());
				else
					tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine()+1, escapeManager.getCurrentTile().getCol());
				
			}
			else
			{
				if(otherBomb.getCol() < escapeManager.getCurrentTile().getCol())
					tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()-1);
				else
					tTypeEscapeTile = percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()+1);
			}
		}
		//sinon on s'enfuit normallement
		else
		{
			tTypeEscapeTile = escapeFromOneOfBomb(bombs);
		}
		
		//system.out.println("tTypeEscapeTile fonx : "+tTypeEscapeTile.toString());
		
		return tTypeEscapeTile;
	}
	
	public AiTile escapeFromOneOfBomb(List<AiBomb> bombs) throws StopRequestException
	{
		myAI.checkInterruption();
		
		AiTile result = null;
		
		List<AiBomb> sameColumnBombs = new ArrayList<AiBomb>();
		List<AiBomb> sameLineBombs = new ArrayList<AiBomb>();
		
		if(bombs.size() == 2)
		{
			if(escapeManager.getCurrentTile().getCol() == bombs.get(0).getCol())
			{
				sameColumnBombs.add(bombs.get(0));
				sameLineBombs.add(bombs.get(1));
			}
			else
			{
				sameColumnBombs.add(bombs.get(1));
				sameLineBombs.add(bombs.get(0));
			}
			
			//si deux bombes sont voisin avec nous, on va s'enfuir a un case qui n'a pas de plusieur danger
			//avant on regarde le voisin de colonne 
			if(sameColumnBombs.get(0).getLine()<escapeManager.getCurrentTile().getLine())
			{
				//si le bomb est plus haut que nous
				if(!myAI.severalDangers(percepts.getTile(escapeManager.getCurrentTile().getLine()+1, escapeManager.getCurrentTile().getCol())))
					result = percepts.getTile(escapeManager.getCurrentTile().getLine()+1, escapeManager.getCurrentTile().getCol());
				else if(sameLineBombs.get(0).getCol()<escapeManager.getCurrentTile().getCol())
				{
					if(!myAI.severalDangers(percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()+1)))
						result = percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()+1);
				}
				else if(sameLineBombs.get(0).getCol()>escapeManager.getCurrentTile().getCol())
				{
					if(!myAI.severalDangers(percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()-1)))
						result = percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()-1);
				}	
					
			}
			else
			{
				//si le bomb est plus haut que nous
				if(!myAI.severalDangers(percepts.getTile(escapeManager.getCurrentTile().getLine()-1, escapeManager.getCurrentTile().getCol())))
					result = percepts.getTile(escapeManager.getCurrentTile().getLine()-1, escapeManager.getCurrentTile().getCol());
				else if(sameLineBombs.get(0).getCol()<escapeManager.getCurrentTile().getCol())
				{
					if(!myAI.severalDangers(percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()+1)))
						result = percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()+1);
				}
				else if(sameLineBombs.get(0).getCol()>escapeManager.getCurrentTile().getCol())
				{
					if(!myAI.severalDangers(percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()-1)))
						result = percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()-1);
				}	
					
			}
		}
		else if(bombs.size() == 3)
		{
			if(escapeManager.getCurrentTile().getCol() == bombs.get(0).getCol())
				sameColumnBombs.add(bombs.get(0));
			else if(escapeManager.getCurrentTile().getCol() == bombs.get(1).getCol())
				sameColumnBombs.add(bombs.get(1));
			else if(escapeManager.getCurrentTile().getCol() == bombs.get(2).getCol())
				sameColumnBombs.add(bombs.get(2));
			
			if(escapeManager.getCurrentTile().getLine() == bombs.get(0).getLine())
				sameColumnBombs.add(bombs.get(0));
			else if(escapeManager.getCurrentTile().getLine() == bombs.get(1).getLine())
				sameColumnBombs.add(bombs.get(1));
			else if(escapeManager.getCurrentTile().getLine() == bombs.get(2).getLine())
				sameColumnBombs.add(bombs.get(2));
			
			//on regard ou dans quelle coordonne il existe un bombe (colonne ou ligne)
			if(sameColumnBombs.size() == 1)
			{
				if(sameColumnBombs.get(0).getCol()<escapeManager.getCurrentTile().getCol())
					result = percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()+1);
				else
					result = percepts.getTile(escapeManager.getCurrentTile().getLine(), escapeManager.getCurrentTile().getCol()-1);
			}
			else
			{
				if(sameLineBombs.get(0).getLine()<escapeManager.getCurrentTile().getLine())
					result = percepts.getTile(escapeManager.getCurrentTile().getLine()+1, escapeManager.getCurrentTile().getCol());
				else
					result = percepts.getTile(escapeManager.getCurrentTile().getLine()-1, escapeManager.getCurrentTile().getCol());
			}
		}
		//s'il ya quatre bombes autour de nous
		else
		{
			if(zone.getMatrix()[escapeManager.getCurrentTile().getLine()+1][escapeManager.getCurrentTile().getCol()+1] != State.SEVERALDANGERS ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()+1][escapeManager.getCurrentTile().getCol()+1] != State.INDESTRUCTIBLE ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()+1][escapeManager.getCurrentTile().getCol()+1] != State.DESTRUCTIBLE ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()+1][escapeManager.getCurrentTile().getCol()+1] != State.BOMBE)
				result = percepts.getTile(escapeManager.getCurrentTile().getLine()+1, escapeManager.getCurrentTile().getCol()+1);
			else if(zone.getMatrix()[escapeManager.getCurrentTile().getLine()-1][escapeManager.getCurrentTile().getCol()+1] != State.SEVERALDANGERS ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()-1][escapeManager.getCurrentTile().getCol()+1] != State.INDESTRUCTIBLE ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()-1][escapeManager.getCurrentTile().getCol()+1] != State.DESTRUCTIBLE ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()-1][escapeManager.getCurrentTile().getCol()+1] != State.BOMBE)
				result = percepts.getTile(escapeManager.getCurrentTile().getLine()-1, escapeManager.getCurrentTile().getCol()+1);
			else if(zone.getMatrix()[escapeManager.getCurrentTile().getLine()+1][escapeManager.getCurrentTile().getCol()-1] != State.SEVERALDANGERS ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()+1][escapeManager.getCurrentTile().getCol()-1] != State.INDESTRUCTIBLE ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()+1][escapeManager.getCurrentTile().getCol()-1] != State.DESTRUCTIBLE ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()+1][escapeManager.getCurrentTile().getCol()-1] != State.BOMBE)
				result = percepts.getTile(escapeManager.getCurrentTile().getLine()+1, escapeManager.getCurrentTile().getCol()-1);
			else if(zone.getMatrix()[escapeManager.getCurrentTile().getLine()-1][escapeManager.getCurrentTile().getCol()-1] != State.SEVERALDANGERS ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()-1][escapeManager.getCurrentTile().getCol()-1] != State.INDESTRUCTIBLE ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()-1][escapeManager.getCurrentTile().getCol()-1] != State.DESTRUCTIBLE ||
					zone.getMatrix()[escapeManager.getCurrentTile().getLine()-1][escapeManager.getCurrentTile().getCol()-1] != State.BOMBE)
				result = percepts.getTile(escapeManager.getCurrentTile().getLine()-1, escapeManager.getCurrentTile().getCol()-1);
			
		}
		
		return result;
	}
	
	
	public boolean bombeHasExplosed(List<AiBomb> bombs) throws StopRequestException
	{
		myAI.checkInterruption();
	
		//on attend pour explosion d'un bombe dont on s'enfuit
		return (bombs.get(0).hasEnded() || bombs.get(1).hasEnded());
		
	}
	
	public List<AiBomb> getBombs() {
		return bombs;
	}
	
	
	//////////////////////BONUSSSSSS staratejileri            ///////////////////////////////////////////////
	public boolean hasExplosed() throws StopRequestException
	{
		myAI.checkInterruption();
		
		AiTile tempTileUp = null;
		AiTile tempTileDown = null;
		AiTile tempTileLeft = null;
		AiTile tempTileRight = null;
		
		if(!pathManager.temp)
		{
			path = escapeManager.path;
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
	
	
	
	///////////////////////ATTACK stra       ///////////////////////////////////////////////////
	
	
	
	
	
	//////////////////////GENERALLLLL            ///////////////////////////////////////////////
	
	

	public boolean finishedTTypeEscape() throws StopRequestException
	{
		myAI.checkInterruption();
						
		currentTile = myAI.getOwnHero().getTile();
		//system.out.println("finishedTTypeEscape fonx.");
				
		return (tTypeEscapeTile == currentTile);
	}
	
	public boolean finishedPath() throws StopRequestException
	{
		myAI.checkInterruption();
		
		currentTile = myAI.getOwnHero().getTile();
		
		return (lastTile == currentTile);
			
	}
	
	
}
