package org.totalboumboum.engine.content.feature.action;

import java.io.Serializable;

import org.totalboumboum.engine.content.feature.action.appear.GeneralAppear;
import org.totalboumboum.engine.content.feature.action.consume.GeneralConsume;
import org.totalboumboum.engine.content.feature.action.cry.GeneralCry;
import org.totalboumboum.engine.content.feature.action.detonate.GeneralDetonate;
import org.totalboumboum.engine.content.feature.action.drop.GeneralDrop;
import org.totalboumboum.engine.content.feature.action.exult.GeneralExult;
import org.totalboumboum.engine.content.feature.action.gather.GeneralGather;
import org.totalboumboum.engine.content.feature.action.jump.GeneralJump;
import org.totalboumboum.engine.content.feature.action.land.GeneralLand;
import org.totalboumboum.engine.content.feature.action.movehigh.GeneralMoveHigh;
import org.totalboumboum.engine.content.feature.action.movelow.GeneralMoveLow;
import org.totalboumboum.engine.content.feature.action.punch.GeneralPunch;
import org.totalboumboum.engine.content.feature.action.push.GeneralPush;
import org.totalboumboum.engine.content.feature.action.release.GeneralRelease;
import org.totalboumboum.engine.content.feature.action.transmit.GeneralTransmit;
import org.totalboumboum.engine.content.feature.action.trigger.GeneralTrigger;

/**
 * 
 * @author Vincent Labatut
 *
 */
public enum ActionName implements Serializable
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

	/** hero releasing an item (after he died, for instance) */
	RELEASE,
	
	/** an item is transmited from one owner to another one (generally from one hero to another one) */
	TRANSMIT,
	
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
			result = new GeneralConsume();
		else if(this==CRY)
			result = new GeneralCry();
		else if(this==DETONATE)
			result = new GeneralDetonate();
		else if(this==DROP)
			result = new GeneralDrop();
		else if(this==EXULT)
			result = new GeneralExult();
		else if(this==GATHER)
			result = new GeneralGather();
		else if(this==JUMP)
			result = new GeneralJump();
		else if(this==LAND)
			result = new GeneralLand();
		else if(this==MOVEHIGH)
			result = new GeneralMoveHigh();
		else if(this==MOVELOW)
			result = new GeneralMoveLow();
		else if(this==PUNCH)
			result = new GeneralPunch();
		else if(this==PUSH)
			result = new GeneralPush();
		else if(this==RELEASE)
			result = new GeneralRelease();
		else if(this==TRANSMIT)
			result = new GeneralTransmit();
		else if(this==TRIGGER)
			result = new GeneralTrigger();
		return result;
	}
}
