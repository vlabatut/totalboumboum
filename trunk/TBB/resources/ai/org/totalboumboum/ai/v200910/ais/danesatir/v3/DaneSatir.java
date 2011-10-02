package org.totalboumboum.ai.v200910.ais.danesatir.v3;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
 * 
 * @version 3
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
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	@Override
	public AiAction processAction() throws StopRequestException {	
		// avant tout : test d'interruption
		checkInterruption();
		if(this.myHero == null)
			init();
		if(myHero.hasEnded())
			return new AiAction(AiActionName.NONE);
		updateEnvironment();
		while(dec.getState()!=State.FINISH) {
			checkInterruption();
			dec.makeDecision();
			calculAction();
		}
		if (getResult()==null) {
			GeneralFuncs.printLog(this, "Problem var", VerboseLevel.LOW);
			setResult(new AiAction(AiActionName.NONE));
		}
		return getResult();
	}

	private void calculAction() throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();
		GeneralFuncs.printLog(this, "DaneSatir.java:calculAction state->"+dec.getState(), VerboseLevel.LOW);
		AiTile targetTile=null;
		switch(dec.getState()) {
		case DANGER:
			targetTile = dec.getTime().mostSafeCase();
			GeneralFuncs.printLog(this, "DaneSatir.java: Danger->"+targetTile, VerboseLevel.MED);
			if(targetTile == null) {
				this.dec.setState(State.NOSAFECASE);
				break;
			}
			try {
				p.calculPath(targetTile);
			} catch (NoWayException e) {
				System.out.println("DaneSatir.java:68");
				this.dec.setState(State.DEBUG);
			}
			this.dec.setState(State.GOTO);
			break;
		case GOTO:
			AiTile nextTile=p.getNextTile();
			if(nextTile==null) {
				dec.setState(State.NONE);
				break;
			}
			Direction d = this.zone.getDirection(this.myHero.getTile(), nextTile);
			GeneralFuncs.printLog(this, "DaneSatir.java: direction->"+d, VerboseLevel.MED);
			setResult( new AiAction(AiActionName.MOVE,d));
			break;
		case GOTOBOMB:
			nextTile=p.getNextTile();
			if(nextTile==null) {
				setResult(new AiAction(AiActionName.DROP_BOMB));
				break;
			}
			d = this.zone.getDirection(this.myHero.getTile(), nextTile);
			GeneralFuncs.printLog(this, "DaneSatir.java: direction->"+d, VerboseLevel.MED);
			setResult( new AiAction(AiActionName.MOVE,d));
			break;
		case TAKEBONUS:
			bonus.findNearestItem();
			GeneralFuncs.printLog(this, "DaneSatir.java: item-> "+bonus.getNearestItem(), VerboseLevel.MED);
			dec.setState(State.GOTO);
			break;
		case EXPLODE:
			targetTile = bonus.findWallToExplode();
			try {
				p.clearPath();
				p.calculPath(targetTile);
			} catch (NoWayException e) {
				System.out.println("DaneSatir.java:108");
				dec.setState(State.DEBUG);
			}
			dec.setState(State.GOTOBOMB);
			break;
		case NONE:
			GeneralFuncs.printLog(this, "DaneSatir.java: problem var", VerboseLevel.MED);
			setResult(new AiAction(AiActionName.NONE));
			break;
		case NOSAFECASE:
			setResult(new AiAction(AiActionName.NONE));
			break;
		}
	}

	private void init() throws StopRequestException 	{	
		// avant tout : test d'interruption
		checkInterruption();
		zone = getPercepts();
		myHero = zone.getOwnHero();
		this.moveCount = 0;
		this.p = new PathFinder(this);
		this.bonus = new BonusCollector(this);
		this.dec = new DecisionMaker(this);
	}
	
	private void updateEnvironment() throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();
		this.moveCount++;
		this.setResult(null);
		if (this.dec.getState()==State.NONE || this.dec.getState()==State.FINISH)
			this.dec.setState(State.START);
	}

	public AiHero getOwnHero() {
		return this.myHero;
	}

	public PathFinder getPathFinder() {
		return p;
	}
	
	public AiAction getResult() {
		return result;
	}

	public void setResult(AiAction result) {
		dec.setState(State.FINISH);
		this.result = result;
	}
}
