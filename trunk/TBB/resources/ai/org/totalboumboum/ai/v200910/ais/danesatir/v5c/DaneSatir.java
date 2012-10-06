package org.totalboumboum.ai.v200910.ais.danesatir.v5c;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
 * 
 * @version 5.c
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
@SuppressWarnings("deprecation")
public class DaneSatir extends ArtificialIntelligence
{
	private AiZone zone;
	private AiHero myHero;
	private PathFinder p;
	private BonusCollector bonus;
	private DecisionMaker dec;
	private AiAction result;
	private int moveCount;
	private int waitCount;

	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	@Override
	public AiAction processAction() throws StopRequestException {	
		// avant tout : test d'interruption
		checkInterruption();
		
		// initialize variables
		if(this.myHero == null) {
			init();
			return new AiAction(AiActionName.NONE);
		}
		
		// update variables
		updateEnvironment();
		
		// Make process until last state
		while(dec.getState()!=State.FINISH) {
			checkInterruption();
			this.myHero=this.zone.getOwnHero();
			
			// Our hero is ended?
			if(myHero.hasEnded())
				break;
			
			dec.makeDecision();
			calculAction();
		}

		if (getResult()==null) {
			//GeneralFuncs.printLog(this, "Problem var", VerboseLevel.LOW);
			setResult(new AiAction(AiActionName.NONE));
		}
		return getResult();
	}
	
	/**
	 * Make calculation according to State
	 * @throws StopRequestException
	 */
	private void calculAction() throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();
		/*GeneralFuncs.printLog(this, 
				this.moveCount+": DaneSatir.java:calculAction " +
						"state->"+dec.getState(), 
				VerboseLevel.LOW);*/
		switch(dec.getState()) {
		case DANGER: {
			checkInterruption();
			// find most safe case
			AiTile targetTile = dec.getTime().mostSafeCase();
			
			//GeneralFuncs.printLog(this, "DaneSatir.java: Danger->"+targetTile, VerboseLevel.MED);
			if(targetTile==null) {
				checkInterruption();
				//targetTile = dec.getTime().mostSafeCase();
				dec.setState(State.WAIT);
			}
			
			try {
				if(!p.isTarget(targetTile))
					p.clearPath();
				p.calculPath(targetTile);
			} catch (NoWayException e) {
				//System.out.println("DaneSatir.java:State.DANGER");
				dec.setState(State.WAIT);
			}
			this.dec.setState(State.GOTO);
			break;
		}
		case GOTO:
		case GOTOBOMB: {
			checkInterruption();
			AiTile nextTile = null;
			try {
				nextTile = p.getNextTile();
				
				/*GeneralFuncs.printLog(this,
						"PATH:"+this.p.getPath() + 
						"Target:" + this.p.getTargets() + 
						"Current:" + this.getOwnHero().getTile(), 
						VerboseLevel.MED);*/
				
				Direction d = this.zone.getDirection(this.myHero.getTile(), nextTile);
 
				if(!nextTile.isCrossableBy(myHero)) {
					this.p.clearPath();
					this.dec.setState(State.WAIT);
					break;
				}
				
				//GeneralFuncs.printLog(this, "DaneSatir.java: direction->"+d, VerboseLevel.MED);
				
				while(!TimeMatrice.isSafe(dec.getTime(), nextTile,this)) {
					checkInterruption();
					if(dec.getTime().isSaferThan(nextTile, this.myHero.getTile()))
						break;
					if(dec.getTime().getTime()<Limits.dangerLimit && dec.getTime().getTime()!=0)
						break;
				}
				setResult( new AiAction(AiActionName.MOVE,d));
				
			} catch (PathOverException e) {
				/*GeneralFuncs.printLog(this,
						"GOTO: PATH: "+p.getPath()+" " +
						"Tile: "+this.getOwnHero().getTile(),
						VerboseLevel.LOW);*/
				
				if(p.isArrive()) {
					if(dec.getState() == State.GOTO) {
						// NONE dogru bi state mi?
						dec.setState(State.START);
						//System.out.println("pehpehpeh");
					}
					else if (dec.getState() == State.GOTOBOMB) {
						if(this.dec.canWeEscape(this.getOwnHero().getTile()))
							setResult(new AiAction(AiActionName.DROP_BOMB));
						else dec.setState(State.START);
					}
				}
				else
					try {
						p.clearPath();
						p.calculPath(p.getTargets());
					} catch (NoWayException e1) {
						//System.out.println("FIX");
					}
			} catch (NoWayException e) {
				dec.setState(State.WAIT);
			}
			break;
		}
		case TAKEBONUS: {
			checkInterruption();
			try {
				bonus.findNearestItem();
				
				/*GeneralFuncs.printLog(this, 
						"DaneSatir.java: item-> "+bonus.getNearestItem(), 
						VerboseLevel.MED);*/
				
				dec.setState(State.GOTO);
			} catch (NoWayException e) {
				dec.setState(State.EXPLODE);
			}
			break;
		}
		case EXPLODE: {
			checkInterruption();
			AiTile targetTile = bonus.findWallToExplode();
			if(targetTile==null) {
				dec.setState(State.WAIT);
				break;
			}
			if(!dec.canWeEscape(targetTile)) {
				dec.setState(State.NONE);
				break;
			}
			
			/*GeneralFuncs.printLog(this, 
					"CanWeEscape? -> "+dec.canWeEscape(targetTile)+
					" tile: "+targetTile,
					VerboseLevel.MED);*/

			try {
				p.clearPath();
				p.calculPath(targetTile);
				dec.setState(State.GOTOBOMB);
			} catch (NoWayException e) {
				//System.out.println("DaneSatir.java:EXPLODE:NoWayException");
				dec.setState(State.WAIT);
			}
			break; 
		}
		case DISCOVER_ENEMY: {
			checkInterruption();
			try {
				p.clearPath();
				p.calculPath(getEnemies());
				dec.setState(State.GOTOBOMB);
			} catch (NoWayException e) {
				//System.out.println("Buraya kontrol edip de geldik zaten");
				dec.setState(State.WAIT);
			}
			break;
		}
		case EXPLODE_TO_ENEMY: {
			Astar astar = new Astar(this,myHero,
					new WallCost(this), 
					new BasicHeuristicCalculator(),
					new WallSuccessor(this)
					);
			AiPath path = astar.processShortestPath(myHero.getTile(), getEnemies());
			if (path.isEmpty()) {
				dec.setState(State.WAIT);
				break;
			}
			AiTile target = null;
			for(AiTile i : path.getTiles()) {
				checkInterruption();
				List<AiBlock> blocks=i.getBlocks();
				if (!blocks.isEmpty())
					if(blocks.get(0).isDestructible())
						break;
				target = i;
			}
			
			if(dec.canWeEscape(target) && !p.isTarget(target)) {
				try {
					p.clearPath();
					p.calculPath(target);
					dec.setState(State.GOTOBOMB);
				} catch (NoWayException e) {
					//System.out.println("Daha once baktik buna");
					dec.setState(State.WAIT);
				}
			}
			else
				dec.setState(State.EXPLODE);
			break;
		}
		case WAIT: {
			//setResult(new AiAction(AiActionName.NONE));
			dec.setState(State.START);
			this.myHero=this.getPercepts().getOwnHero();
			this.p.updateTimeMatrice(this.dec.getTime());
			this.waitCount++;
			if(this.waitCount==Limits.turnCount) {
				this.waitCount=0;
				setResult(new AiAction(AiActionName.NONE));
			}
			break;
		}
		case NONE:
			GeneralFuncs.printLog(this, "DaneSatir.java: problem var", VerboseLevel.MED);
			setResult(new AiAction(AiActionName.NONE));			
			break;
		}
	}
	
	/**
	 * Initialize Variables
	 */
	private void init() throws StopRequestException 	{	
		// avant tout : test d'interruption
		checkInterruption();
		zone = getPercepts();
		myHero = zone.getOwnHero();
		this.moveCount = 0;
		this.bonus = new BonusCollector(this);
		this.dec = new DecisionMaker(this);
		this.p = new PathFinder(this,dec.getTime());
	}
	
	/**
	 * Update Variables
	 * @throws StopRequestException
	 */
	private void updateEnvironment() throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();
		this.moveCount++;
		this.setResult(null);
		this.p.updateTimeMatrice(this.dec.getTime());
		Limits.tileDistance = (this.myHero.getTile().getSize()/this.myHero.getWalkingSpeed());
		Limits.dangerLimit = Limits.tileDistance * 5;
		Limits.expandBombTime = Limits.tileDistance * 2;
		if (this.dec.getState()==State.NONE)
			this.dec.setState(State.START);
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public AiHero getOwnHero() throws StopRequestException {
		checkInterruption();
		return this.myHero;
	}
	/**
	 * Getter for PathFinder
	 * @return current PathFinder
	 * @throws StopRequestException 
	 * @see PathFinder 
	 */
	public PathFinder getPathFinder() throws StopRequestException {
		checkInterruption();
		return p;
	}
	
	/**
	 * Getter for Result
	 * @return current result
	 * @throws StopRequestException 
	 * @see AiActionName
	 */
	public AiAction getResult() throws StopRequestException {
		checkInterruption();
		return result;
	}

	/**
	 * 
	 * @param result
	 * @throws StopRequestException
	 */
	public void setResult(AiAction result) throws StopRequestException {
		checkInterruption();
		if(result == null)
			dec.setState(State.START);
		else 
			dec.setState(State.FINISH);
		this.result = result;
	}
	
	/**
	 * Get enemies without us
	 * @return      the image at the specified URL
	 * @throws StopRequestException 
	 */
	public List<AiTile> getEnemies() throws StopRequestException {
		checkInterruption();
		List<AiHero> heroes = this.zone.getHeroes();
		List<AiTile> targets = new ArrayList<AiTile>();
		heroes.remove(myHero);
		for(AiHero i : heroes) {
			checkInterruption();
			targets.add(i.getTile());
		}
		return targets; 
	}
}
