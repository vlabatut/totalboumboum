package org.totalboumboum.ai.v200910.ais.aksoytangay.v5_1;

import java.util.ArrayList;

import java.util.Collection;


import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
 * 
 * @version 5.1
 * 
 * @author Cihan Aksoy
 * @author Necmi Murat Tangay
 *
 */
@SuppressWarnings("deprecation")
public class AksoyTangay extends ArtificialIntelligence
{	
	
	private Zone zone = null;
	
	private AiHero ownHero = null;
	
	public AiHero getOwnHero() {
		return ownHero;
	}


	
	public AiTile getOwnHeroTile() {
		return ownHeroTile;
	}



	private AiZone percepts = null;
	
	@SuppressWarnings("unused")
	private State stateMatrix[][] = null;
	
	private EscapeManager escapeManager = null;
	
	private PathManager pathManager = null;
	
	private BonusManager bonusManager = null;
	
	private StrategyManager strategyManager = null;
	
	public AttackManager attackManager = null;
	
	private AttackManager attackManager2 = null;
	
	private boolean keyBomb = true;
	
	private AiTile ownHeroTile;
	
	/** méthode appelée par le moteur du jeu pour obtenir une action d'IA */
	@Override
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
			
		if(ownHero == null)
			init();
		
		AiAction result = new AiAction(AiActionName.NONE);
		Direction direction = Direction.NONE;
			
		ownHeroTile = ownHero.getTile();
		//sysem.out.println("reel tile: "+ownHeroTile);
		
		
		//Sysem.out.println(ownHero.getWalkingSpeed());
		
		
		//algo commence
		if(strategyManager!=null)
		{
			
			if(strategyManager.finishedTTypeEscape())
				if(strategyManager.getBombs().size() == 2)
					if(strategyManager.bombeHasExplosed(strategyManager.getBombs()))
						strategyManager = null;
					else
						direction = Direction.NONE;
				else
					strategyManager = null;
			else
				direction = strategyManager.typeDefiningAndGettingDirection();
			
			result = new AiAction(AiActionName.MOVE, direction);
		}
		else if(escapeManager!=null)
		{
			if(severalDangers(ownHeroTile))
			{	
				strategyManager = new StrategyManager(this, percepts, zone, pathManager, escapeManager);
				direction = strategyManager.typeDefiningAndGettingDirection();
			}
			else
			{
				if(escapeManager.finishedPath())
					escapeManager = null;
				else
					direction = escapeManager.getDirectionToEscape();
			}
			
			result = new AiAction(AiActionName.MOVE, direction);
		}
		else if(isDangerous(ownHeroTile))
		{
			escapeManager = new EscapeManager(this, percepts, pathManager);
			if(severalDangers(ownHeroTile))
			{
				strategyManager = new StrategyManager(this, percepts, zone, pathManager, escapeManager);
				direction = strategyManager.typeDefiningAndGettingDirection();
			}
			else
				direction = escapeManager.getDirectionToEscape();
			////Sysem.out.println("direction : "+ direction.toString());
			
			
			result = new AiAction(AiActionName.MOVE, direction);
		}
		
		else if(attackManager2!=null)
		{
			if(!attackManager2.isTherePath())
			{
				attackManager2 = null;
				attackManager = null;
			}
			else if(!attackManager2.modifiedPath())
			{
				attackManager2 = null;
				attackManager = null;
			}
			else if(attackManager2.placeChanged())
			{
				attackManager2 = null;
				attackManager = null;
			}
			else if(attackManager2.getHeroToAttack().hasEnded())
			{
				attackManager2 = null;
				attackManager = null;
			}
			else if(attackManager2.finishedOnePartOfPath())
			{
				result = new AiAction(AiActionName.DROP_BOMB);
				//System.out.println("bombaloz");
				if(attackManager2.finishedPath())
				{
					attackManager2 = null;
					attackManager = null;
				}
				
			}
			else if(attackManager2.finishedPath())
			{
				result = new AiAction(AiActionName.DROP_BOMB);
				attackManager2 = null;
				attackManager = null;
			}
			
			else
			{
				direction = attackManager2.gettingAttackDirection();
				if(isDangerous(getOwnHeroTile().getNeighbor(direction)))
					result = new AiAction(AiActionName.NONE);
				else	
					result = new AiAction(AiActionName.MOVE, direction);
			}
		}
		else if(ownHero.getBombNumber()>3 && isHerosAccesible() && !isBonusClose())
		{
			attackManager = new AttackManager(this, percepts, zone, pathManager);
			AttackType attackType = attackManager.attackTypeDefiner();
			//Sysem.out.println("at type : "+attackType.toString());
			 
			attackManager2 = new AttackManager(this, percepts, zone, pathManager, attackType);
				
			if(attackManager.heroToAttack != null)
				attackManager2.setHeroToAttack(attackManager.heroToAttack);
			if(attackManager.heroToAttackTile != null)
				attackManager2.setHeroToAttackTile(attackManager.heroToAttackTile);
			if(attackManager.normalAttackTile != null)
				attackManager2.setNormalAttackTile(attackManager.normalAttackTile);
			if(attackManager.whichTileToDropBomb != null)
				attackManager2.setWhichTileToDropBomb(attackManager.whichTileToDropBomb);
				
			//Sysem.out.println(attackType.toString());
			direction = attackManager2.gettingAttackDirection();
			result = new AiAction(AiActionName.MOVE, direction);
			
		}
		else
		{
			direction = bonusManager.getDirectionToCollectBonus();
			
			if(pathManager.temp == false)
			{
				//system.out.println("ilk");
				 
				if(keyBomb|| strategyManager.hasExplosed())
				{
					//system.out.println("iki");
					keyBomb = true;
					if(bonusManager.finishedPath())
					{
						//system.out.println("!!");
						result = new AiAction(AiActionName.DROP_BOMB);
						//keyBomb = false;
					}
				}
				else if(!bonusManager.finishedPath())
					result = new AiAction(AiActionName.MOVE, direction);
			}
			else if(!bonusManager.finishedPath() || pathManager.temp == true)
				result = new AiAction(AiActionName.MOVE, direction);
			
			
			if(isDangerous(getOwnHeroTile().getNeighbor(direction)))
				result = new AiAction(AiActionName.NONE);
		}
		
			
			
		
			
				
		return result;
		
	}
	
		
	/** methode qui teste si le case est en danger ou pas
	 * 
	 *  @param tile : AiTile
	 *  
	 *  @return la situation de danger de case
	 *  
	 *  @throws StopRequestException
	 */
	public boolean isDangerous(AiTile tile) throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		if(tile==null)
			return false;
		
		stateMatrix = zone.updateMatrix();
		State state = zone.getMatrix()[tile.getLine()][tile.getCol()];
		/*
		System.out.println("isdangereous.akstan");
		if(state==State.SURE)
			System.out.println("--sure--");
		else if(state==State.DANGER)
			System.out.println("--danger--" + stateMatrix[tile.getCol()][tile.getLine()]);
		*/
		if(state==State.DANGER || state == State.SEVERALDANGERS)
		{
			//system.out.println("////NOW IN DANGER////////");
			return true;
		}
		
		else
			return false;
		
	}
	
	public boolean severalDangers(AiTile tile) throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		if(tile==null)
			return false;
		
		stateMatrix = zone.updateMatrix();
		State state = zone.getMatrix()[tile.getLine()][tile.getCol()];
		/*
		System.out.println("isdangereous.akstan");
		if(state==State.SURE)
			System.out.println("--sure--");
		else if(state==State.DANGER)
			System.out.println("--danger--" + stateMatrix[tile.getCol()][tile.getLine()]);
		*/
		if(state==State.SEVERALDANGERS)
		{
			//system.out.println("////NOW IN severalDANGERs////////");
			return true;
		}
		
		else
			return false;
		
	}
	
	public boolean isHerosAccesible() throws StopRequestException
	{
		checkInterruption();
		
		boolean result = false;
		
		List<AiHero> heros = percepts.getHeroes();
		Iterator<AiHero> itrHero = heros.iterator();
		AiHero tmpHero = itrHero.next();
		
		while (itrHero.hasNext()) {
			
			if(tmpHero!=getOwnHero())
			{	
				tmpHero = itrHero.next();
				
				//Sysem.out.println("hero tile : "+itrHero.next().getTile().toString());
				
				AiPath tmpPath = pathManager.getShortestPathToOneTile(getOwnHero(), getOwnHeroTile(), tmpHero.getTile());
				if(tmpPath != null)
				{
					result = true;
					break;
				}
			}
			
				
		}
		
		
		return result;		
	}
	
	public boolean isBonusClose() throws StopRequestException
	{
		checkInterruption();
		
		boolean result = false;
		
		
		Collection<AiItem> items = zone.getItems();
		Iterator<AiItem> itrItems = items.iterator();
		
		while (itrItems.hasNext()) {
			AiItem aiItem = (AiItem) itrItems.next();
			
			AiPath tmpPath = pathManager.getShortestPathToOneTile(getOwnHero(), getOwnHeroTile(), aiItem.getTile());
			if(tmpPath !=null)
				if(tmpPath.getLength()<5)
					result = true;
			
		}
		
		return result;
	}
	
	private void init() throws StopRequestException
	{
		checkInterruption();
		
		percepts = getPercepts();
		ownHero = percepts.getOwnHero();
		zone = new Zone(percepts, this);
		stateMatrix = zone.getMatrix();
		
		pathManager = new PathManager(this, percepts);
		//escapeManager = new EscapeManager(this,percepts, pathManager);
		bonusManager = new BonusManager(this, percepts, pathManager);
		
		
		//System.out.println("init.akstan");
	}
	
	//silinecek!!
	
	@SuppressWarnings("unused")
	private List<AiTile> getClearNeighbors(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases autour de la case de référence
		Collection<AiTile> neighbors = getPercepts().getTile(tile.getLine(), tile.getCol()).getNeighbors();
		// on garde les cases sans bloc ni bombe ni feu
		List<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiTile> it = neighbors.iterator();
		while(it.hasNext())
		{	checkInterruption(); //APPEL OBLIGATOIRE
		
			AiTile t = it.next();
			if(isDangerous(t))
				result.add(t);
		}
		return result;
	}
	
	
}
