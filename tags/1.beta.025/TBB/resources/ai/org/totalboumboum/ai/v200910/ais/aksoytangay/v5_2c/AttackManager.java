package org.totalboumboum.ai.v200910.ais.aksoytangay.v5_2c;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
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
public class AttackManager {

	/** */
	private AksoyTangay myAI;
	
	/** */
	private PathManager pathManager;
	
	/** */
	private AiPath path = null;
	
	/** */
	private AiTile currentTile;
	
	/** */
	private AiTile lastTile;
	/** */
	public AiTile firstTile;
	/** */
	public AiTile secondTile;
	/** */
	public AiTile thirdTile;
	/** */
	public AiTile normalAttackTile = null;
			
	/** */
	private AiZone percepts;
	
	/** */
	private Zone zone;
	/** */
	public List<AiBomb> bombs;
	/** */
	public AiTile whichTileToDropBomb = null;
	/** */
	public AiTile first;
	/** */
	public AiTile second;
	/** */
	public AiTile third;
	/** */
	public AiHero heroToAttack = null;
	/** */
	public AiTile heroToAttackTile = null;
	/** */
	public List<AiTile> heroNeighborTiles;
	/** */
	public AttackType attackType;

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiTile getHeroToAttackTile() throws StopRequestException {
		myAI.checkInterruption();
		return heroToAttackTile;
	}
	/**
	 * 
	 * @param heroToAttackTile
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void setHeroToAttackTile(AiTile heroToAttackTile) throws StopRequestException {
		myAI.checkInterruption();
		this.heroToAttackTile = heroToAttackTile;
	}	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiHero getHeroToAttack() throws StopRequestException {
		myAI.checkInterruption();
		return heroToAttack;
	}
	/**
	 * 
	 * @param heroToAttack
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void setHeroToAttack(AiHero heroToAttack) throws StopRequestException {
		myAI.checkInterruption();
		this.heroToAttack = heroToAttack;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiTile getNormalAttackTile() throws StopRequestException {
		myAI.checkInterruption();
		return normalAttackTile;
	}
	/**
	 * 
	 * @param normalAttackTile
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void setNormalAttackTile(AiTile normalAttackTile) throws StopRequestException {
		myAI.checkInterruption();
		this.normalAttackTile = normalAttackTile;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiTile getWhichTileToDropBomb() throws StopRequestException {
		myAI.checkInterruption();
		return whichTileToDropBomb;
	}
	/**
	 * 
	 * @param whichTileToDropBomb
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void setWhichTileToDropBomb(AiTile whichTileToDropBomb) throws StopRequestException {
		myAI.checkInterruption();
		this.whichTileToDropBomb = whichTileToDropBomb;
	}
	/**
	 * 
	 * @param myAI
	 * 		Description manquante !
	 * @param percepts
	 * 		Description manquante !
	 * @param zone
	 * 		Description manquante !
	 * @param pathManager
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AttackManager(AksoyTangay myAI, AiZone percepts, Zone zone, PathManager pathManager) throws StopRequestException
	{
		myAI.checkInterruption();
		
		this.myAI = myAI;
		this.percepts = percepts;
		this.zone = zone;
		this.pathManager = pathManager;
		
		heroToAttack = getHeroToAttackFonx();
//		System.out.println("saldrcak adam : "+heroToAttack.toString());
	}
	
	/**
	 * 
	 * @param myAI
	 * 		Description manquante !
	 * @param percepts
	 * 		Description manquante !
	 * @param zone
	 * 		Description manquante !
	 * @param pathManager
	 * 		Description manquante !
	 * @param attackType
	 * 		Description manquante !
	 * @param nat
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AttackManager(AksoyTangay myAI, AiZone percepts, Zone zone, PathManager pathManager, AttackType attackType, AiTile nat) throws StopRequestException
	{
		myAI.checkInterruption();
		
		this.myAI = myAI;
		this.percepts = percepts;
		this.zone = zone;
		this.pathManager = pathManager;
		this.attackType = attackType;
		this.normalAttackTile = nat;
					
		if(attackType == AttackType.ENDSHOOT)
		{
			path = pathManager.getShortestPathToOneTile(myAI.getOwnHero(), myAI.getOwnHeroTile(), whichTileToDropBomb);
			//System.out.println(myAI.attackManager.whichTileToDropBomb);
		}
		if(attackType == AttackType.NORMAL)
		{
			path = pathManager.getShortestPathToOneTile(myAI.getOwnHero(), myAI.getOwnHeroTile(), normalAttackTile);
//			System.out.println(path.toString());
		}
		
		if(attackType == AttackType.TWOORTHREEBOMBL)
			path = pathManager.getShortestPathToOneTile(myAI.getOwnHero(), myAI.getOwnHeroTile(), normalAttackTile);
			
		
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiHero getHeroToAttackFonx() throws StopRequestException
	{
		myAI.checkInterruption();
		
		AiHero result = null;
		
		List<AiHero> heros = percepts.getHeroes();
//		System.out.println(heros.toString());
		Iterator<AiHero> itrHero = heros.iterator();
		AiHero tmpHero = null;
			
		//avant chercons un hero proche
		
		while (itrHero.hasNext()) 
		{
			myAI.checkInterruption();
			tmpHero = itrHero.next();
			if(tmpHero!=myAI.getOwnHero())
			{
				
				if(Math.abs(myAI.getOwnHeroTile().getCol() - tmpHero.getTile().getCol()) < 3 || myAI.getOwnHeroTile().getLine() - tmpHero.getTile().getLine() < 3)
				{
					result = tmpHero;
					break;
				}
			}
		}
		
		//s'il n y a un hero proche, on cherche un hero qui a moins bombe
		
		if(result == null)
		{
			int leastNumber = 100;
			int bombeNumber;
			
			while (itrHero.hasNext()) 
			{
				myAI.checkInterruption();
				
				tmpHero = itrHero.next();
				if(tmpHero != myAI.getOwnHero())
				{
					
					bombeNumber = tmpHero.getBombNumber();
					if(leastNumber>bombeNumber)
					{
						leastNumber = bombeNumber;
						result = tmpHero;
					}
					
				}
			
			}
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
	public Direction gettingAttackDirection() throws StopRequestException
	{
		myAI.checkInterruption();
		
		Direction result = Direction.NONE;
		
		
		
//		if(attackType == AttackType.ENDSHOOT)
//			result = endShootDirection(myAI.attackManager.whichTileToDropBomb);
//		if(attackType == AttackType.TWOORTHREEBOMBL)
//			result = lTypeAttackWithTwoOrThreeBombs(myAI.getOwnHeroTile(), normalAttackTile);
//		if(attackType == AttackType.NORMAL)
		result = normalAttack(myAI.getOwnHeroTile(), heroToAttack.getTile());
		System.out.println("direction attack : "+result.toString());
		//si on ne peut pas atteindre a un hero sans exploser de murs
		
		//null olup olmadna bakcaz sonra
		///bonus mant patlatmazsa biz patlatrz mur leri
				
		///retourne la direction
		
		return result;
	}
	
	/**
	 * 
	 * @param whichTileToDropBomb
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Direction endShootDirection(AiTile whichTileToDropBomb) throws StopRequestException
	{
		myAI.checkInterruption();
		
		currentTile = myAI.getOwnHeroTile();
		lastTile = whichTileToDropBomb;
		
		
				
		Direction result = Direction.NONE;
				
		AiTile tempTile = null;
		
		if(!finishedPath())
		{
			removeUsedTilesOfPath();
			if(path.isEmpty() || !modifiedPath())
			{
				path = pathManager.getShortestPathToOneTile(myAI.getOwnHero(), currentTile, lastTile);
			}
//			if(myAI.getOwnHero().getLine() == path.getTile(0).getLine() && myAI.getOwnHero().getCol() == path.getTile(0).getCol())
//				path.getTiles().remove(0);
			
			if(path.getLength()>1)
				tempTile = path.getTile(1);
			else if(path.getLength()>0)
				tempTile = path.getTile(0);
			
			result = percepts.getDirection(myAI.getOwnHero(), tempTile);
		}
		
		return result;
			
	}
	
	/**
	 * 
	 * @param ownTile
	 * 		Description manquante !
	 * @param heroTile
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Direction normalAttack(AiTile ownTile, AiTile heroTile) throws StopRequestException
	{
		myAI.checkInterruption();
		
		Direction result = Direction.NONE;
		
		AiTile normalAttackTile = null;
		
		if(Math.abs(ownTile.getCol()-heroTile.getCol()) < Math.abs(ownTile.getLine()-heroTile.getLine()))
		{
			if(ownTile.getCol() == heroTile.getCol())
			{
				if(myAI.getOwnHero().getBombRange()>Math.abs(ownTile.getLine()-heroTile.getLine()))
				{
					this.firstTile = ownTile;
					this.thirdTile = heroTile;
					this.path = pathManager.getShortestPathToOneTile(myAI.getOwnHero(), firstTile, thirdTile);
					result = dropTwoBomb(first, third);
				}
				else
				{
					if(ownTile.getLine()<heroTile.getLine())
					{
						normalAttackTile = percepts.getTile(ownTile.getLine()+1, ownTile.getCol()); 
						this.path = pathManager.getShortestPathToOneTile(myAI.getOwnHero(), ownTile, normalAttackTile);
						result = goThere(normalAttackTile);
					}
					else
					{
						normalAttackTile = percepts.getTile(ownTile.getLine()-1, ownTile.getCol());
						this.path = pathManager.getShortestPathToOneTile(myAI.getOwnHero(), ownTile, normalAttackTile);
						result = goThere(normalAttackTile);
					}
				}
			}
			else
			{
				if(ownTile.getCol()<heroTile.getCol())
				{
					normalAttackTile = percepts.getTile(ownTile.getLine(), ownTile.getCol()+1);
					this.path = pathManager.getShortestPathToOneTile(myAI.getOwnHero(), ownTile, normalAttackTile);
					result = goThere(normalAttackTile);
				}
				else
				{
					normalAttackTile = percepts.getTile(ownTile.getLine(), ownTile.getCol()-1);
					this.path = pathManager.getShortestPathToOneTile(myAI.getOwnHero(), ownTile, normalAttackTile);
					result = goThere(normalAttackTile);
				}
					
			}
		}
		else
		{
			if(ownTile.getLine() == heroTile.getLine())
			{
				if(myAI.getOwnHero().getBombRange()>Math.abs(ownTile.getCol()-heroTile.getCol()))
				{
					this.firstTile = ownTile;
					this.thirdTile = heroTile;
					this.path = pathManager.getShortestPathToOneTile(myAI.getOwnHero(), firstTile, thirdTile);
					result = dropTwoBomb(first, third);
				}
				else
				{
					if(ownTile.getCol()<heroTile.getCol())
					{
						normalAttackTile = percepts.getTile(ownTile.getLine(), ownTile.getCol()+1);
						this.path = pathManager.getShortestPathToOneTile(myAI.getOwnHero(), ownTile, normalAttackTile);
						result = goThere(normalAttackTile);
					}
						
					else
					{
						normalAttackTile = percepts.getTile(ownTile.getLine(), ownTile.getCol()-1);
						this.path = pathManager.getShortestPathToOneTile(myAI.getOwnHero(), ownTile, normalAttackTile);
						result = goThere(normalAttackTile);
					}
				}
			}
			else
			{
				if(ownTile.getLine()<heroTile.getLine())
				{	
					normalAttackTile = percepts.getTile(ownTile.getLine()+1, ownTile.getCol());
					this.path = pathManager.getShortestPathToOneTile(myAI.getOwnHero(), ownTile, normalAttackTile);
					result = goThere(normalAttackTile);
				}
				else
				{
					normalAttackTile = percepts.getTile(ownTile.getLine()-1, ownTile.getCol());
					this.path = pathManager.getShortestPathToOneTile(myAI.getOwnHero(), ownTile, normalAttackTile);
					result = goThere(normalAttackTile);
				}
			}
		}
		
//		System.out.println("normal atack : "+result.toString());
		
		return result;
		
	}
	
	
	/** pour les  coins
	 * 
	 * @param ownTile
	 * 		Description manquante !
	 * @param heroTile
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Direction lTypeAttackWithTwoOrThreeBombs(AiTile ownTile, AiTile heroTile) throws StopRequestException
	{
		myAI.checkInterruption();
		
		Direction result = Direction.NONE;
		
		int i = 0;
		AiTile tmpTile = ownTile;
				
		
		if(ownTile.getCol() == heroTile.getCol())
		{
			if(zone.getMatrix()[ownTile.getLine()][ownTile.getCol()-1] == State.INDESTRUCTIBLE)
			{
				if(heroTile.getLine()<ownTile.getLine())
				{
					while(zone.getMatrix()[tmpTile.getLine()][tmpTile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmpTile = percepts.getTile(tmpTile.getLine()-1, tmpTile.getCol());
					}
					first = ownTile;
					second = percepts.getTile(ownTile.getLine(), ownTile.getCol()+i);
					third = percepts.getTile(ownTile.getLine()-i, ownTile.getCol()+i);
				}
				else
				{	myAI.checkInterruption();
					while(zone.getMatrix()[tmpTile.getLine()][tmpTile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmpTile = percepts.getTile(tmpTile.getLine()+1, tmpTile.getCol());
					}
					first = ownTile;
					second = percepts.getTile(ownTile.getLine(), ownTile.getCol()+i);
					third = percepts.getTile(ownTile.getLine()+i, ownTile.getCol()+i);
				}
			}
			else if(zone.getMatrix()[ownTile.getLine()][ownTile.getCol()+1] == State.INDESTRUCTIBLE)
			{
				if(heroTile.getLine()<ownTile.getLine())
				{
					while(zone.getMatrix()[tmpTile.getLine()][tmpTile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmpTile = percepts.getTile(tmpTile.getLine()-1, tmpTile.getCol());
					}
					first = ownTile;
					second = percepts.getTile(ownTile.getLine(), ownTile.getCol()-i);
					third = percepts.getTile(ownTile.getLine()-i, ownTile.getCol()-i);
				}
				else
				{
					while(zone.getMatrix()[tmpTile.getLine()][tmpTile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmpTile = percepts.getTile(tmpTile.getLine()+1, tmpTile.getCol());
					}
					first = ownTile;
					second = percepts.getTile(ownTile.getLine(), ownTile.getCol()-i);
					third = percepts.getTile(ownTile.getLine()+i, ownTile.getCol()-i);
				}
			}
		}
		else if(ownTile.getLine() == heroTile.getLine())
		{
			if(zone.getMatrix()[ownTile.getLine()-1][ownTile.getCol()] == State.INDESTRUCTIBLE)
			{
				if(heroTile.getCol()<ownTile.getCol())
				{
					while(zone.getMatrix()[tmpTile.getLine()][tmpTile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmpTile = percepts.getTile(tmpTile.getLine(), tmpTile.getCol()-1);
					}
					first = ownTile;
					second = percepts.getTile(ownTile.getLine()+i, ownTile.getCol());
					third = percepts.getTile(ownTile.getLine()+i, ownTile.getCol()-i);
				}
				else
				{
					while(zone.getMatrix()[tmpTile.getLine()][tmpTile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmpTile = percepts.getTile(tmpTile.getLine(), tmpTile.getCol()+1);
					}
					first = ownTile;
					second = percepts.getTile(ownTile.getLine()+i, ownTile.getCol());
					third = percepts.getTile(ownTile.getLine()+i, ownTile.getCol()+i);
				}
			}
			else if(zone.getMatrix()[ownTile.getLine()+1][ownTile.getCol()] == State.INDESTRUCTIBLE)
			{
				if(heroTile.getCol()<ownTile.getCol())
				{
					while(zone.getMatrix()[tmpTile.getLine()][tmpTile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmpTile = percepts.getTile(tmpTile.getLine(), tmpTile.getCol()-1);
					}
					first = ownTile;
					second = percepts.getTile(ownTile.getLine()-i, ownTile.getCol());
					third = percepts.getTile(ownTile.getLine()-i, ownTile.getCol()-i);
				}
				else
				{
					while(zone.getMatrix()[tmpTile.getLine()][tmpTile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmpTile = percepts.getTile(tmpTile.getLine()+1, tmpTile.getCol()+1);
					}
					first = ownTile;
					second = percepts.getTile(ownTile.getLine()-i, ownTile.getCol());
					third = percepts.getTile(ownTile.getLine()-i, ownTile.getCol()+i);
				}
			}
		}
		if(i != 0)
		{
			int size1 = tilesSizeBetweenTwoTiles(first, second);
			int size2 = tilesSizeBetweenTwoTiles(second, third);
			int size = size1+size2;
				
			if((size+2)*myAI.getOwnHero().getWalkingSpeed() < 2400)
			{
				result = dropThreeBomb(first, second, third);
				lastTile = third;
			}
			else
			{
				result = dropTwoBomb(first, third);
				this.second = third;
				lastTile = this.second;
			}
		}
		
		else if(ownTile.getLine() != heroTile.getLine() && ownTile.getLine() != heroTile.getLine())
		{
			AiTile tmp2Tile = heroTile;
			if(zone.getMatrix()[heroTile.getLine()][heroTile.getCol()-1] == State.INDESTRUCTIBLE)
			{
				if(heroTile.getLine()<ownTile.getLine())
				{
					
					while(zone.getMatrix()[tmp2Tile.getLine()][tmp2Tile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmp2Tile = percepts.getTile(tmp2Tile.getLine()-1, tmp2Tile.getCol());
					}
					first = percepts.getTile(heroTile.getLine()+3-i, heroTile.getCol());
					result = goThere(first);
				}
				else
				{
					while(zone.getMatrix()[tmp2Tile.getLine()][tmp2Tile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmp2Tile = percepts.getTile(tmp2Tile.getLine()-1, tmp2Tile.getCol());
					}
					first = percepts.getTile(heroTile.getLine()-3+i, heroTile.getCol());
					result = goThere(first);
				}
			}
			else if(zone.getMatrix()[heroTile.getLine()][heroTile.getCol()+1] == State.INDESTRUCTIBLE)
			{
				if(heroTile.getLine()<ownTile.getLine())
				{
					while(zone.getMatrix()[tmp2Tile.getLine()][tmp2Tile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmp2Tile = percepts.getTile(tmp2Tile.getLine()-1, tmp2Tile.getCol());
					}
					first = percepts.getTile(heroTile.getLine()+3-i, heroTile.getCol());
					result = goThere(first);
				}
				else
				{
					while(zone.getMatrix()[tmp2Tile.getLine()][tmp2Tile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmp2Tile = percepts.getTile(tmp2Tile.getLine()+1, tmp2Tile.getCol());
					}
					first = percepts.getTile(heroTile.getLine()-3+i, heroTile.getCol());
					result = goThere(first);
				}
			}
			else if(zone.getMatrix()[heroTile.getLine()-1][heroTile.getCol()] == State.INDESTRUCTIBLE)
			{
				if(heroTile.getCol()<ownTile.getCol())
				{
					while(zone.getMatrix()[tmp2Tile.getLine()][tmp2Tile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmp2Tile = percepts.getTile(tmp2Tile.getLine(), tmp2Tile.getCol()-1);
					}
					first = percepts.getTile(heroTile.getLine(), heroTile.getCol()+3-i);
					result = goThere(first);
				}
				else
				{
					while(zone.getMatrix()[tmp2Tile.getLine()][tmp2Tile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmp2Tile = percepts.getTile(tmp2Tile.getLine(), tmp2Tile.getCol()+1);
					}
					first = percepts.getTile(heroTile.getLine(), heroTile.getCol()-3+i);
					result = goThere(first);
				}
			}
			else if(zone.getMatrix()[heroTile.getLine()+1][heroTile.getCol()] == State.INDESTRUCTIBLE)
			{
				if(heroTile.getCol()<ownTile.getCol())
				{
					while(zone.getMatrix()[tmp2Tile.getLine()][tmp2Tile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmp2Tile = percepts.getTile(tmp2Tile.getLine(), tmp2Tile.getCol()-1);
					}
					first = percepts.getTile(heroTile.getLine(), heroTile.getCol()+3-i);
					result = goThere(first);
				}
				else
				{
					while(zone.getMatrix()[tmp2Tile.getLine()][tmp2Tile.getCol()] != State.INDESTRUCTIBLE)
					{	myAI.checkInterruption();
						i++;
						tmp2Tile = percepts.getTile(tmp2Tile.getLine(), tmp2Tile.getCol()+1);
					}
					first = percepts.getTile(heroTile.getLine(), heroTile.getCol()-3+i);
					result = goThere(first);
				}
			}
		}
		
		
		
		
		return result;
		
		
	}
	
	/**
	 * *
	 * @param first
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Direction goThere(AiTile first) throws StopRequestException
	{
		myAI.checkInterruption();
		
		Direction result = Direction.NONE;
			
		currentTile = myAI.getOwnHeroTile();
		lastTile = first;
		
				
		AiTile tempTile = null;
		
		if(!finishedPath())
		{
			removeUsedTilesOfPath();
			if(path.isEmpty() || !modifiedPath())
			{
				path = pathManager.getShortestPathToOneTile(myAI.getOwnHero(), currentTile, lastTile);
			}
//			if(myAI.getOwnHero().getLine() == path.getTile(0).getLine() && myAI.getOwnHero().getCol() == path.getTile(0).getCol())
//				path.getTiles().remove(0);
			
			if(path.getLength()>1)
				tempTile = path.getTile(1);
			else if(path.getLength()>0)
				tempTile = path.getTile(0);
			
			result = percepts.getDirection(myAI.getOwnHero(), tempTile);
		}
		return result;
	}
	
	/**
	 * 
	 * @param first
	 * 		Description manquante !
	 * @param second
	 * 		Description manquante !
	 * @param third
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Direction dropThreeBomb(AiTile first, AiTile second, AiTile third) throws StopRequestException
 	{
		myAI.checkInterruption();
		
		Direction result = Direction.NONE;
		
		if(myAI.getOwnHeroTile() != second)
			result = dropTwoBomb(first, second);
		else
			result = dropTwoBomb(second, third);
		
		return result;
	}
	
	/**
	 * 
	 * @param first
	 * 		Description manquante !
	 * @param third
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Direction dropTwoBomb(AiTile first, AiTile third) throws StopRequestException
	{
		myAI.checkInterruption();
		
		Direction result = Direction.NONE;
		
		if(myAI.getOwnHeroTile() != first)
			this.normalAttackTile = first;
		else
			this.normalAttackTile = third;
		
		
		result = goThere(normalAttackTile);
		
		return result;
	}
	
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AttackType attackTypeDefiner() throws StopRequestException
	{
		myAI.checkInterruption();
		
		AttackType attackType = AttackType.NORMAL;
		normalAttackTile = getHeroToAttackFonx().getTile();
		//eer mur den adama gidemiosak bi hesap yapalm
		//bi de adam ortalardaysa bi saldr dzenleyelim...
		
		return attackType;
	}
	
	/** gerer le chemin    ////////////
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
		
//		System.out.println("finisH??");
		//System.out.println("last tile and current tile "+lastTile.toString()+" "+currentTile.toString() );
		
		//return (lastTile.equals(currentTile));
		return((lastTile == currentTile) || (thirdTile == currentTile)||(currentTile == normalAttackTile));
				
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean finishedOnePartOfPath() throws StopRequestException
	{
		myAI.checkInterruption();
		
		currentTile = myAI.getOwnHero().getTile();
		
		return((currentTile == firstTile)||(currentTile == secondTile)||(currentTile == thirdTile)||(currentTile == normalAttackTile));
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
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void removeUsedTilesOfPath() throws StopRequestException
	{	myAI.checkInterruption(); //APPEL OBLIGATOIRE
	
		AiTile curTile = myAI.getOwnHero().getTile();
		
		
		while(!path.isEmpty() && path.getTile(0)!=curTile)
		{	
			myAI.checkInterruption(); //APPEL OBLIGATOIRE
			
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
	public boolean isTherePath() throws StopRequestException
	{
		myAI.checkInterruption();
		
		return(path!=null);
	}
	
	/**
	 * 
	 * @param startTile
	 * 		Description manquante !
	 * @param finishTile
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	@SuppressWarnings("null")
	public boolean isDangereousPath(AiTile startTile, AiTile finishTile) throws StopRequestException
	{
		myAI.checkInterruption();
		
		boolean result = true;
		
//		int startLine = startTile.getLine();
//		int startCol = startTile.getCol();		
		Collection<AiTile> tiles = null;
		if(startTile.getCol() == finishTile.getCol())
		{
			if(startTile.getCol()<finishTile.getCol())
			{
				AiTile tmpTile = startTile;
				while (tmpTile != finishTile) {
					myAI.checkInterruption();
					tiles.add(tmpTile);
					tmpTile = percepts.getTile(tmpTile.getLine(), tmpTile.getCol()+1);
				}				
			}
			else
			{
				AiTile tmpTile = startTile;
				while (tmpTile != finishTile) {
					myAI.checkInterruption();
					tiles.add(tmpTile);
					tmpTile = percepts.getTile(tmpTile.getLine(), tmpTile.getCol()-1);
				}
			}
		}
		else
		{	if(startTile.getLine()<finishTile.getLine())
			{
				AiTile tmpTile = startTile;
				while (tmpTile != finishTile) {
					myAI.checkInterruption();
					tiles.add(tmpTile);
					tmpTile = percepts.getTile(tmpTile.getLine()+1, tmpTile.getCol());
				}				
			}
			else
			{
				AiTile tmpTile = startTile;
				while (tmpTile != finishTile) {
					myAI.checkInterruption();
					tiles.add(tmpTile);
					tmpTile = percepts.getTile(tmpTile.getLine()-1, tmpTile.getCol());
				}
			}
		}	
		Iterator<AiTile> itrTile = tiles.iterator();
		while (itrTile.hasNext()) {
			myAI.checkInterruption();
			AiTile aiTile = (AiTile) itrTile.next();
			if(myAI.isDangerous(aiTile))
			{
				result = false;
				break;
			}
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
	public boolean isDangerToAttack() throws StopRequestException
	{	myAI.checkInterruption();		
		boolean result = true;		
		if(zone.getMatrix()[heroToAttack.getLine()-1][heroToAttack.getCol()] == State.INDESTRUCTIBLE || zone.getMatrix()[heroToAttack.getLine()+1][heroToAttack.getCol()] == State.INDESTRUCTIBLE)
		{
			if(!isDangerAtLine(heroToAttack.getTile()))
			{
				AiTile tmpTile = heroToAttack.getTile();
				while(zone.getMatrix()[tmpTile.getLine()][tmpTile.getCol()] != State.INDESTRUCTIBLE)
				{	myAI.checkInterruption();
					if(heroToAttack.getCol()<percepts.getWidth()/2)
						tmpTile = percepts.getTile(tmpTile.getLine(), tmpTile.getCol()-1);
					else
						tmpTile = percepts.getTile(tmpTile.getLine(), tmpTile.getCol()+1);
				}
				if(heroToAttack.getCol()<percepts.getWidth()/2)
					tmpTile = percepts.getTile(tmpTile.getLine(), tmpTile.getCol()+1);
				else
					tmpTile = percepts.getTile(tmpTile.getLine(), tmpTile.getCol()-1);
				
				if(!isDangerAtCol(tmpTile))
					result = false;
				
			}
		}
		else
		{
			if(!isDangerAtCol(heroToAttack.getTile()))
			{
				AiTile tmpTile = heroToAttack.getTile();
				while(zone.getMatrix()[tmpTile.getLine()][tmpTile.getCol()] != State.INDESTRUCTIBLE)
				{	myAI.checkInterruption();
					if(heroToAttack.getLine()<percepts.getHeight()/2)
						tmpTile = percepts.getTile(tmpTile.getLine()-1, tmpTile.getCol());
					else
						tmpTile = percepts.getTile(tmpTile.getLine()+1, tmpTile.getCol());
				}
				if(heroToAttack.getCol()<percepts.getWidth()/2)
					tmpTile = percepts.getTile(tmpTile.getLine()+1, tmpTile.getCol());
				else
					tmpTile = percepts.getTile(tmpTile.getLine()-1, tmpTile.getCol());
				
				if(!isDangerAtLine(tmpTile))
					result = false;
				
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean isDangerAtLine(AiTile tile) throws StopRequestException
	{
		myAI.checkInterruption();
		
		boolean result = true;
		
		if(!myAI.isDangerous(tile))
		{
			AiTile tmpTile = tile;
			int i = 0;
			int j = 0;
			while(i<3)
			{	myAI.checkInterruption();
				tmpTile = percepts.getTile(tmpTile.getLine()+1, tmpTile.getCol());
				if(myAI.isDangerous(tmpTile))
				{	myAI.checkInterruption();
					result = true;
					break;
				}
				
				i++;
			}
			while(j<3)
			{	myAI.checkInterruption();
				tmpTile = percepts.getTile(tmpTile.getLine()-1, tmpTile.getCol());
				if(myAI.isDangerous(tmpTile))
				{	myAI.checkInterruption();
					result = true;
					break;
				}
				j++;
			}
			
			if(i == 2 && j == 2)
				result = false;
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean isDangerAtCol(AiTile tile) throws StopRequestException
	{
		myAI.checkInterruption();
		
		boolean result = true;
		
		if(!myAI.isDangerous(tile))
		{
			AiTile tmpTile = tile;
			int i = 0;
			int j = 0;
			while(i<3)
			{	myAI.checkInterruption();
				tmpTile = percepts.getTile(tmpTile.getLine(), tmpTile.getCol()+1);
				if(myAI.isDangerous(tmpTile))
				{	
					result = true;
					break;
				}
				
				i++;
			}
			while(j<3)
			{	myAI.checkInterruption();
				tmpTile = percepts.getTile(tmpTile.getLine(), tmpTile.getCol()-1);
				if(myAI.isDangerous(tmpTile))
				{
					result = true;
					break;
				}
				j++;
			}
			
			if(i == 2 && j == 2)
				result = false;
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param startTile
	 * 		Description manquante !
	 * @param finishTile
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public int tilesSizeBetweenTwoTiles(AiTile startTile, AiTile finishTile) throws StopRequestException
	{
		myAI.checkInterruption();
		
		List<AiTile> tiles = new ArrayList<AiTile>();
		if(startTile.getCol() == finishTile.getCol())
		{
			if(startTile.getCol()<finishTile.getCol())
			{
				AiTile tmpTile = startTile;
				while (tmpTile != finishTile) {
					myAI.checkInterruption();
					tiles.add(tmpTile);
					tmpTile = percepts.getTile(tmpTile.getLine(), tmpTile.getCol()+1);
				}				
			}
			else
			{
				AiTile tmpTile = startTile;
				while (tmpTile != finishTile) {
					myAI.checkInterruption();
					tiles.add(tmpTile);
					tmpTile = percepts.getTile(tmpTile.getLine(), tmpTile.getCol()-1);
				}
			}
		}
		else
		{
			if(startTile.getLine()<finishTile.getLine())
			{
				AiTile tmpTile = startTile;
				while (tmpTile != finishTile) {
					myAI.checkInterruption();
					tiles.add(tmpTile);
					tmpTile = percepts.getTile(tmpTile.getLine()+1, tmpTile.getCol());
				}				
			}
			else
			{
				AiTile tmpTile = startTile;
				while (tmpTile != finishTile) {
					myAI.checkInterruption();
					tiles.add(tmpTile);
					tmpTile = percepts.getTile(tmpTile.getLine()-1, tmpTile.getCol());
				}
			}
		}
		
		return tiles.size();
	}
	
	/**
	 * 
	 * @param ownTile
	 * 		Description manquante !
	 * @param heroTile
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiTile mirrorTile(AiTile ownTile, AiTile heroTile) throws StopRequestException
	{	myAI.checkInterruption();
AttackType attackType = AttackType.NONE;
		
		heroToAttack = getHeroToAttackFonx();
//		System.out.println("heroToAttack : "+heroToAttack.toString());
		heroToAttackTile = heroToAttack.getTile();
		
		//definons une type d'attaque    ////////
		
		//s'il y a trois voisin de hero est mur ou dangereux
		heroNeighborTiles = heroToAttack.getTile().getNeighbors();
		
		int wallSize=0;
		
		
		for(int i=0;i<4;i++)
		{
			myAI.checkInterruption();
			
			if(zone.getMatrix()[heroNeighborTiles.get(i).getLine()][heroNeighborTiles.get(i).getCol()] == State.BOMBE ||
					zone.getMatrix()[heroNeighborTiles.get(i).getLine()][heroNeighborTiles.get(i).getCol()] == State.INDESTRUCTIBLE ||
					zone.getMatrix()[heroNeighborTiles.get(i).getLine()][heroNeighborTiles.get(i).getCol()] == State.DESTRUCTIBLE ||
					zone.getMatrix()[heroNeighborTiles.get(i).getLine()][heroNeighborTiles.get(i).getCol()] == State.DANGER ||
					zone.getMatrix()[heroNeighborTiles.get(i).getLine()][heroNeighborTiles.get(i).getCol()] == State.SEVERALDANGERS)
				wallSize++;
			else
				whichTileToDropBomb = heroNeighborTiles.get(i);
		}
		
		if(wallSize == 3)
			attackType = AttackType.ENDSHOOT;
		else
		{
		//si le ennemi est dans un coin du jeu
			if(attackType == AttackType.NONE)
			{ 
			//allttaki if e qualif 2 den geecek ekilde oyun alan skim kadarsa o kede deildir gibi baka koul konabilir
			//her iki yan destructible olacak!! sûre kat -- 7 tile geme filan gibi cond kat , kendimiz lmeyelim
				if(((Math.abs(heroToAttack.getTile().getCol()-myAI.getOwnHeroTile().getCol()) < myAI.getOwnHero().getBombRange()) ||
						(Math.abs(heroToAttack.getTile().getLine()-myAI.getOwnHeroTile().getLine()) < myAI.getOwnHero().getBombRange())) &&
						((Math.abs(heroToAttack.getTile().getCol()-myAI.getOwnHeroTile().getCol()) <= percepts.getWidth()/3) ||
								(Math.abs(heroToAttack.getTile().getLine()-myAI.getOwnHeroTile().getLine()) <= percepts.getHeight()/3)) &&
								isAtCorner(heroToAttack.getTile()))
					if(!isDangerToAttack())
						attackType = AttackType.TWOORTHREEBOMBL;
		
				 
			}
			else
			{
				normalAttackTile = heroToAttackTile;
				attackType = AttackType.NORMAL;
			}
			
		}
		
		return null;
	}
	
	/** on regard si l'ennemi a change sa position
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean placeChanged() throws StopRequestException
	{
		myAI.checkInterruption();
		
//		System.out.println(heroToAttack.toString());				
		return(heroToAttackTile == heroToAttack.getTile());
		
		
	}
	
	/**
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean isAtCorner(AiTile tile) throws StopRequestException
	{	myAI.checkInterruption();
		boolean result = false;
		
		
		if(zone.getMatrix()[tile.getLine()-1][tile.getCol()] == State.INDESTRUCTIBLE || zone.getMatrix()[tile.getLine()+1][tile.getCol()] == State.INDESTRUCTIBLE)
		{
			for(int i = 1;i<4;i++)
			{	myAI.checkInterruption();
				if(tile.getCol()<percepts.getWidth()/2)
				{
					if(zone.getMatrix()[tile.getLine()][tile.getCol()-i] == State.INDESTRUCTIBLE)
					{
						result = true;
						break;
					}
				}
				else
				{
					if(zone.getMatrix()[tile.getLine()][tile.getCol()+i] == State.INDESTRUCTIBLE)
					{
						result = true;
						break;
					}
				}
			}
		}
		else if(zone.getMatrix()[tile.getLine()][tile.getCol()-1] == State.INDESTRUCTIBLE || zone.getMatrix()[tile.getLine()][tile.getCol()+1] == State.INDESTRUCTIBLE)
		{
			for(int j = 1;j<4;j++)
			{	myAI.checkInterruption();
				if(tile.getLine()<percepts.getHeight()/2)
				{
					if(zone.getMatrix()[tile.getLine()][tile.getCol()-j] == State.INDESTRUCTIBLE)
					{
						result = true;
						break;
					}
				}
				else
				{
					if(zone.getMatrix()[tile.getLine()][tile.getCol()+j] == State.INDESTRUCTIBLE)
					{
						result = true;
						break;
					}
				}
			}
		}
		
		return result;
	}
}
