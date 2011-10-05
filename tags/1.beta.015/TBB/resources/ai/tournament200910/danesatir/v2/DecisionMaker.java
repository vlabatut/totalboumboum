package tournament200910.danesatir.v2;

import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;

public class DecisionMaker {
	
	private TimeMatrice time;
	private DaneSatir ai;
	
	public DecisionMaker(DaneSatir ai) {
		this.ai=ai;
		this.time=ai.getTimeMatrice();
	}
	
	public boolean onFire(){
		double dur=time.getTime(ai.getOwnHero().getTile());
		//if( dur < Limits.dangerLimit && dur !=0)
		if( dur >0 )
			return true;
		return false;
	}
	public boolean itemExist() {
		return true;
	}
	public State getDecision() throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		if(this.ai.getState() == State.GOTO)
			return State.GOTO;
		if(this.ai.getState() == State.BOMB)
			return State.BOMB;
		if(onFire())
			return State.DANGER;
		if(itemExist())
			return State.TAKEBONUS;
		return State.NONE;
	}
}