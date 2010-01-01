package tournament200910.danesatir.v1;

import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;

public class DecisionMaker {
	
	private TimeMatrice time;
	@SuppressWarnings("unused")
	private AiZone zone;
	private AiHero myHero;
	
	public DecisionMaker(AiZone zone,AiHero myHero,TimeMatrice time) {
		this.time=time;
		this.zone=zone;
		this.myHero=myHero;
	}
	
	public boolean onFire(){
		if(time.getTime(myHero.getTile())>0)
			return true;
		return false;
	}
	
	public State getDecision() {
		if(onFire())
			return State.DANGER;
		return State.NONE;
	}
}
