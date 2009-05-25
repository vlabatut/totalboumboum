package fr.free.totalboumboum.engine.content.feature.action;

public enum ActionName {

	/** 
	 * action d'apparaitre de nulle part (suite à téléport, ou drop) 
	 * INTRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>N/D
	 * 
	 * <p>ABILITY REFUSE
	 *  <br>N/D
	 *  
	 * <p>ABILITY PREVENT
	 * 	<br>paramètre: actor=oui
	 * 	<br>paramètre: target=oui (floor)
	 * 	<br>paramètre: direction=N/D
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=oui
	 * 	<br>paramètre: restriction=SPRITE_TRAVERSE
	 */	
	/** appearing in a tile, coming from nowhere (after a teleport, a drop, at the begining of a round, etc) */
	APPEAR,
	
	/** 
	 * action de faire brûler un autre objet
	 * TRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>paramètre: actor=self
	 * 	<br>paramètre: target=oui (général: toutes les classes)
	 * 	<br>paramètre: direction=N/D
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 * 
	 * <p>ABILITY REFUSE
	 * 	<br>paramètre: actor=oui (fire?)
	 * 	<br>paramètre: target=N/D
	 * 	<br>paramètre: direction=N/D
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>paramètre: actor=oui
	 * 	<br>paramètre: target=oui
	 * 	<br>paramètre: direction=N/D
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 */	
	/** burning another object, usually performed by fire */
	CONSUME,
	
	/** crying for a defeat, at the end of a round. performed by a hero */
	CRY,
	
	/** 
	 * action volontaire de brûler (par exemple explosion timer pr une bombe)
	 * action de créer une explosion, alors que consume = fait de brûler(?)
	 * ou detonate = bruler+explosion alors que consume=etre brulé
	 * INTRANSITIVE
	 *  
	 * <p>ABILITY PERFORM
	 * 	<br>paramètre: actor=self
	 * 	<br>paramètre: target=N/D
	 * 	<br>paramètre: direction=oui (permet de restreindre l'explosion d'une bombe ?)
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 * 
	 * <p>ABILITY REFUSE
	 * 	<br>N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>paramètre: actor=oui
	 * 	<br>paramètre: target=N/D
	 * 	<br>paramètre: direction=N/D
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 */
	/** making an explosion, usually performed by a bomb (triggered bomb, etc) 
	DETONATE,
	/**
	 * action de déposer un objet (une bombe)
	 * TRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>paramètre: actor=self
	 * 	<br>paramètre: target=oui (bomb)
	 * 	<br>paramètre: direction=N/D
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=type de bombe
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 *  
	 * <p>ABILITY REFUSE
	 * 	<br>N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>paramètre: actor=oui (hero)
	 * 	<br>paramètre: target=oui (bomb)
	 * 	<br>paramètre: direction=N/D
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=type de bombe
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 */
	/** puting an object on the ground (dropping a bomb)
	DROP,
	
	/** 
	 * action de récolter (un item)
	 * TRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>paramètre: actor=self
	 * 	<br>paramètre: target=oui (item)
	 * 	<br>paramètre: direction=N/D
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 * 
	 * <p>ABILITY REFUSE (ex: item qui ne peut pas être récolté en permanence)
	 * 	<br>paramètre: actor=oui (hero)
	 * 	<br>paramètre: target=self
	 * 	<br>paramètre: direction=N/D
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 * 
	 * <p>ABILITY PREVENT (ex: un bloc qui empêche par intermitence les heros de récolter l'item)
	 * 	<br>paramètre: actor=oui (hero)
	 * 	<br>paramètre: target=oui (item)
	 * 	<br>paramètre: direction=N/D
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=éventuellement
	 */
	/** picking an object just by walking on it (unlike picking a bomb). (hero gathering an item) */
	GATHER,
	
	/** celebrating a victory at the end of a round */
	EXULT,
	
	/** 
	 * action de sauter en l'air (hors déplacement sur le plan, qui est lié à movehigh)
	 * INTRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>paramètre: actor=self
	 * 	<br>paramètre: target=N/D
	 * 	<br>paramètre: direction=N/D
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 * 
	 * <p>ABILITY REFUSE
	 * 	<br>N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>paramètre: actor=oui (hero)
	 * 	<br>paramètre: target=N/D
	 * 	<br>paramètre: direction=N/D
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 */
	/** begining an aerial move (hero jumping)
	JUMP,
	
	/** 
	 * action d'atterrir (hors déplacement sur le plan, qui est lié à movehigh)
	 * INTRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 *	<br>N/D 
	 *
	 * <p>ABILITY REFUSE
	 * 	<br>N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>paramètre: actor=oui (hero,bombe)
	 * 	<br>paramètre: target=N/D
	 * 	<br>paramètre: direction=N/D
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=oui
	 * 	<br>paramètre: restriction=SPRITE_TRAVERSE
	 */
	/** finishing an aerial move (hero or bomb landing on the ground) */
	LAND,
	
	/** 
	 * action de se déplacer en l'air
	 * INTRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>N/D
	 * 
	 * <p>ABILITY REFUSE
	 * 	<br>N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>paramètre: actor=oui (hero,bomb)
	 * 	<br>paramètre: target=N/D
	 * 	<br>paramètre: direction=oui
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=oui
	 * 	<br>paramètre: restriction=SPRITE_TRAVERSE
	 */
	/** in-air moving (in the plane) */ 
	MOVEHIGH,
	
	/** 
	 * action de se déplacer sur le sol
	 * INTRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>N/D
	 * 
	 * <p>ABILITY REFUSE
	 * 	<br>N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>paramètre: actor=oui (hero, bomb)
	 * 	<br>paramètre: target=N/D
	 * 	<br>paramètre: direction=oui
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=oui
	 * 	<br>paramètre: restriction=SPRITE_TRAVERSE
	 */
	/** on ground (normal) move (hero walking, bomb sliding, etc) */
	MOVELOW,
	
	/** 
	 * action de balancer un autre objet
	 * TRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>paramètre: actor=self
	 * 	<br>paramètre: target=oui (bomb)
	 * 	<br>paramètre: direction=oui
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 * 
	 * <p>ABILITY REFUSE
	 * 	<br>paramètre: actor=oui (hero)
	 * 	<br>paramètre: target=self
	 * 	<br>paramètre: direction=oui
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>paramètre: actor=oui (hero)
	 * 	<br>paramètre: target=oui (bomb)
	 * 	<br>paramètre: direction=oui
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 */
	/** hitting a bomb (or hero) to send it in the air */
	PUNCH,
	
	/** 
	 * action de pousser un autre objet (peut être un kick)
	 * TRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>paramètre: actor=self
	 * 	<br>paramètre: target=oui (bomb)
	 * 	<br>paramètre: direction=oui
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 * 
	 * <p>ABILITY REFUSE
	 * 	<br>paramètre: actor=oui (hero)
	 * 	<br>paramètre: target=self
	 * 	<br>paramètre: direction=oui
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>paramètre: actor=oui (hero)
	 * 	<br>paramètre: target=oui (bomb)
	 * 	<br>paramètre: direction=oui
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 */
	/** pushing an object (hero, bomb, wall...) in order to make it move on the ground */
	PUSH,

	/** 
	 * action d'activer l'explosion d'une remote bomb
	 * TRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>paramètre: actor=self
	 * 	<br>paramètre: target=oui (bombe)
	 * 	<br>paramètre: direction=N/D
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 * 
	 * <p>ABILITY REFUSE (généralement pas utilisé, mais possible)
	 * 	<br>N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>paramètre: actor=oui (hero)
	 * 	<br>paramètre: target=all
	 * 	<br>paramètre: direction=N/D
	 * 	<br>paramètre: strength=bool
	 * 	<br>paramètre: kind=N/D
	 * 	<br>paramètre: scope=N/D
	 * 	<br>paramètre: restriction=N/D
	 */
	/** asking for a remote bomb to explode (usually performed by a hero) */
	TRIGGER
	;

}
