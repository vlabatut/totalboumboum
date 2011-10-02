package org.totalboumboum.ai.v200910.ais.danesatir.v3;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;

/**
 * 
 * @version 3
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
@SuppressWarnings("deprecation")
public class DecisionMaker {

	private DaneSatir ai;
	private State state;
	private TimeMatrice time;

	public DecisionMaker(DaneSatir ai) throws StopRequestException {
		this.ai=ai;
		this.state=State.NONE;
	}

	public void makeDecision() throws StopRequestException {
		ai.checkInterruption();
		setTime(new TimeMatrice(ai));
		if(getState()==State.DEBUG)
			return;
		if(getState()==State.NOSAFECASE)
			return;
		if(isDanger() && getState()!=State.GOTO) 
			setState(State.DANGER);
		if(getState()==State.START) {
			if(isItemExist())
				setState(State.TAKEBONUS);
			else if (isHiddenItemExist() && isHaveBomb())
				setState(State.EXPLODE);
			else setState(State.NONE);
		}
	}

	private boolean isHiddenItemExist() {
		if(this.ai.getPercepts().getHiddenItemsCount()>0)
			return true;
		return false;
	}

	private boolean isItemExist() {
		if(this.ai.getPercepts().getItems().size()>0)
			return true;
		return false;
	}
	
	private boolean isDanger() {
		double dur = this.time.getTime();
		if(dur!=0)
			return true;
		return false;
	}
	private boolean isHaveBomb() {
		AiHero hero = this.ai.getOwnHero();
		int count=hero.getBombNumber()-hero.getBombCount();
		if (count>0)
			return true;
		return false;
	}
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public TimeMatrice getTime() {
		return time;
	}

	private void setTime(TimeMatrice time) {
		this.time = time;
	}
}
