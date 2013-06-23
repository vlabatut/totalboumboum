package org.totalboumboum.ai.v200910.ais.aksoytangay.v5_2;

import java.util.ArrayList;

import java.util.Collection;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
 * 
 * @version 5.2
 * 
 * @author Cihan Aksoy
 * @author Necmi Murat Tangay
 *
 */
@SuppressWarnings("deprecation")
public class AksoyTangay extends ArtificialIntelligence
{	
	/** */
	private Zone zone = null;
	
	/** */
	private AiHero ownHero = null;
	
	/**
	 * 
	 * @return
	 * 		?
	 */
	public AiHero getOwnHero() {
		return ownHero;
	}


	/**
	 * 
	 * @return
	 * 		?
	 */
	public AiTile getOwnHeroTile() {
		return ownHeroTile;
	}



	/** */
	private AiZone percepts = null;
	
	/** */
	@SuppressWarnings("unused")
	private State stateMatrix[][] = null;
	
	/** */
	private EscapeManager escapeManager = null;
	
	/** */
	private PathManager pathManager = null;
	
	/** */
	private BonusManager bonusManager = null;
	
	/** */
	private StrategyManager strategyManager = null;
	/** */
	public AttackManager attackManager = null;
	
	/** */
	private boolean attackVariable = false;
	
	/** */
	@SuppressWarnings("unused")
	private AttackManager attackManager2 = null;
	
	/** */
	private boolean keyBomb = true;
	
	/** */
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
//		System.out.println("reel tile: "+ownHeroTile);
		
		
//		System.out.println(ownHero.getWalkingSpeed());
		
		
		//algo commence
		if(strategyManager!=null && !attackVariable)
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
		else if(escapeManager!=null && !attackVariable)
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
		else if(isDangerous(ownHeroTile) && !attackVariable)
		{
			escapeManager = new EscapeManager(this, percepts, pathManager);
			if(severalDangers(ownHeroTile))
			{
				strategyManager = new StrategyManager(this, percepts, zone, pathManager, escapeManager);
				direction = strategyManager.typeDefiningAndGettingDirection();
			}
			else
				direction = escapeManager.getDirectionToEscape();
			//system.out.println("direction : "+ direction.toString());
			
			
			result = new AiAction(AiActionName.MOVE, direction);
		}
		
		else if(attackManager!=null)
		{
			if(!attackManager.isTherePath())
				attackManager = null;
			if(!isHerosAccesible())
				attackManager = null;
//			else if(!attackManager.modifiedPath())
//				attackManager = null;
//			else if(attackManager.placeChanged())
//				attackManager = null;
			else if(attackManager.getHeroToAttack().hasEnded())
				attackManager = null;
			else if(attackManager.finishedOnePartOfPath())
			{
				result = new AiAction(AiActionName.DROP_BOMB);
				//System.out.println("bombaloz");
				if(attackManager.finishedPath())
					attackManager = null;
								
			}
			else if(attackManager.finishedPath())
			{
				result = new AiAction(AiActionName.DROP_BOMB);
				
				attackManager = null;
			}
			
			else
			{
				direction = attackManager.gettingAttackDirection();
				if(isDangerous(getOwnHeroTile().getNeighbor(direction)))
					result = new AiAction(AiActionName.NONE);
				else	
					result = new AiAction(AiActionName.MOVE, direction);
			}
		}
		else if(ownHero.getBombNumber()>4 && isHerosAccesible() && !isBonusClose())
		{
			attackManager = new AttackManager(this, percepts, zone, pathManager);
						
			direction = attackManager.gettingAttackDirection();
			attackVariable = true;
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
							else
								result = new AiAction(AiActionName.MOVE, direction);
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
	 * 		Description manquante !
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
	
	/**
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
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
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean isHerosAccesible() throws StopRequestException
	{
		checkInterruption();
		
		boolean result = false;
		
		List<AiHero> heros = percepts.getHeroes();
		Iterator<AiHero> itrHero = heros.iterator();
		AiHero tmpHero = null;
		
		while (itrHero.hasNext()) {
			
			if(tmpHero!=getOwnHero())
			{	
				tmpHero = itrHero.next();
				
				System.out.println("hero tile : "+tmpHero.getTile().toString());
				
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
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
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
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
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
	
	/**
	 * @param tile 
	 * 		Description manquante !
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
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
