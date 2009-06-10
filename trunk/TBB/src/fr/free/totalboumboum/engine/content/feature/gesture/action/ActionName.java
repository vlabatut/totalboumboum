package fr.free.totalboumboum.engine.content.feature.gesture.action;

import fr.free.totalboumboum.engine.content.feature.gesture.action.appear.GeneralAppear;

public enum ActionName
{
	/** appearing in a tile, coming from nowhere (after a teleport, a drop, at the begining of a round, etc) */
	APPEAR,
	
	/** burning another object, usually performed by fire */
	CONSUME,
	
	/** crying for a defeat, at the end of a round. always performed by a hero */
	CRY,
	
	/** making an explosion, usually performed by a bomb (triggered bomb, etc) */ 
	DETONATE,
	
	/** puting an object on the ground (dropping a bomb) */
	DROP,
	
	/** celebrating a victory at the end of a round. always performed by a hero */
	EXULT,
	
	/** picking an object just by walking on it (unlike picking a bomb). (hero gathering an item) */
	GATHER,
	
	/** begining an aerial move (hero jumping) */
	JUMP,
	
	/** finishing an aerial move (hero or bomb landing on the ground) */
	LAND,
	
	/** in-air moving (in the plane) */ 
	MOVEHIGH,
	
	/** on ground (normal) move (hero walking, bomb sliding, etc) */
	MOVELOW,
	
	/** hitting a bomb (or hero) to send it in the air */
	PUNCH,
	
	/** pushing an object (hero, bomb, wall...) in order to make it move on the ground */
	PUSH,

	/** asking for a remote bomb to explode (usually performed by a hero) */
	TRIGGER;
	
	/**
	 * returns an empty GeneralAction corresponding to the specified name
	 * @return
	 */
	public GeneralAction createGeneralAction()
	{	GeneralAction result = null;
		if(this==APPEAR)
			result = new GeneralAppear();
		else if(this==CONSUME)
			result = new GeneralAppear();
		else if(this==CRY)
			result = new GeneralAppear();
		else if(this==DETONATE)
			result = new GeneralAppear();
		else if(this==DROP)
			result = new GeneralAppear();
		else if(this==EXULT)
			result = new GeneralAppear();
		else if(this==GATHER)
			result = new GeneralAppear();
		else if(this==JUMP)
			result = new GeneralAppear();
		else if(this==LAND)
			result = new GeneralAppear();
		else if(this==MOVEHIGH)
			result = new GeneralAppear();
		else if(this==MOVELOW)
			result = new GeneralAppear();
		else if(this==PUNCH)
			result = new GeneralAppear();
		else if(this==PUSH)
			result = new GeneralAppear();
		else if(this==TRIGGER)
			result = new GeneralAppear();
		return result;
	}
}
