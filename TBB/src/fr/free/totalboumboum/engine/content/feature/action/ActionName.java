package fr.free.totalboumboum.engine.content.feature.action;

public enum ActionName {

	/** 
	 * action d'apparaitre de nulle part (suite � t�l�port, ou drop) 
	 * INTRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>N/D
	 * 
	 * <p>ABILITY REFUSE
	 *  <br>N/D
	 *  
	 * <p>ABILITY PREVENT
	 * 	<br>param�tre: actor=oui
	 * 	<br>param�tre: target=oui (floor)
	 * 	<br>param�tre: direction=N/D
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=oui
	 * 	<br>param�tre: restriction=SPRITE_TRAVERSE
	 */	
	/** appearing in a tile, coming from nowhere (after a teleport, a drop, at the begining of a round, etc) */
	APPEAR,
	
	/** 
	 * action de faire br�ler un autre objet
	 * TRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>param�tre: actor=self
	 * 	<br>param�tre: target=oui (g�n�ral: toutes les classes)
	 * 	<br>param�tre: direction=N/D
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 * 
	 * <p>ABILITY REFUSE
	 * 	<br>param�tre: actor=oui (fire?)
	 * 	<br>param�tre: target=N/D
	 * 	<br>param�tre: direction=N/D
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>param�tre: actor=oui
	 * 	<br>param�tre: target=oui
	 * 	<br>param�tre: direction=N/D
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 */	
	/** burning another object, usually performed by fire */
	CONSUME,
	
	/** crying for a defeat, at the end of a round. performed by a hero */
	CRY,
	
	/** 
	 * action volontaire de br�ler (par exemple explosion timer pr une bombe)
	 * action de cr�er une explosion, alors que consume = fait de br�ler(?)
	 * ou detonate = bruler+explosion alors que consume=etre brul�
	 * INTRANSITIVE
	 *  
	 * <p>ABILITY PERFORM
	 * 	<br>param�tre: actor=self
	 * 	<br>param�tre: target=N/D
	 * 	<br>param�tre: direction=oui (permet de restreindre l'explosion d'une bombe ?)
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 * 
	 * <p>ABILITY REFUSE
	 * 	<br>N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>param�tre: actor=oui
	 * 	<br>param�tre: target=N/D
	 * 	<br>param�tre: direction=N/D
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 */
	/** making an explosion, usually performed by a bomb (triggered bomb, etc) 
	DETONATE,
	/**
	 * action de d�poser un objet (une bombe)
	 * TRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>param�tre: actor=self
	 * 	<br>param�tre: target=oui (bomb)
	 * 	<br>param�tre: direction=N/D
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=type de bombe
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 *  
	 * <p>ABILITY REFUSE
	 * 	<br>N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>param�tre: actor=oui (hero)
	 * 	<br>param�tre: target=oui (bomb)
	 * 	<br>param�tre: direction=N/D
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=type de bombe
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 */
	/** puting an object on the ground (dropping a bomb)
	DROP,
	
	/** 
	 * action de r�colter (un item)
	 * TRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>param�tre: actor=self
	 * 	<br>param�tre: target=oui (item)
	 * 	<br>param�tre: direction=N/D
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 * 
	 * <p>ABILITY REFUSE (ex: item qui ne peut pas �tre r�colt� en permanence)
	 * 	<br>param�tre: actor=oui (hero)
	 * 	<br>param�tre: target=self
	 * 	<br>param�tre: direction=N/D
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 * 
	 * <p>ABILITY PREVENT (ex: un bloc qui emp�che par intermitence les heros de r�colter l'item)
	 * 	<br>param�tre: actor=oui (hero)
	 * 	<br>param�tre: target=oui (item)
	 * 	<br>param�tre: direction=N/D
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=�ventuellement
	 */
	/** picking an object just by walking on it (unlike picking a bomb). (hero gathering an item) */
	GATHER,
	
	/** celebrating a victory at the end of a round */
	EXULT,
	
	/** 
	 * action de sauter en l'air (hors d�placement sur le plan, qui est li� � movehigh)
	 * INTRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>param�tre: actor=self
	 * 	<br>param�tre: target=N/D
	 * 	<br>param�tre: direction=N/D
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 * 
	 * <p>ABILITY REFUSE
	 * 	<br>N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>param�tre: actor=oui (hero)
	 * 	<br>param�tre: target=N/D
	 * 	<br>param�tre: direction=N/D
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 */
	/** begining an aerial move (hero jumping)
	JUMP,
	
	/** 
	 * action d'atterrir (hors d�placement sur le plan, qui est li� � movehigh)
	 * INTRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 *	<br>N/D 
	 *
	 * <p>ABILITY REFUSE
	 * 	<br>N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>param�tre: actor=oui (hero,bombe)
	 * 	<br>param�tre: target=N/D
	 * 	<br>param�tre: direction=N/D
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=oui
	 * 	<br>param�tre: restriction=SPRITE_TRAVERSE
	 */
	/** finishing an aerial move (hero or bomb landing on the ground) */
	LAND,
	
	/** 
	 * action de se d�placer en l'air
	 * INTRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>N/D
	 * 
	 * <p>ABILITY REFUSE
	 * 	<br>N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>param�tre: actor=oui (hero,bomb)
	 * 	<br>param�tre: target=N/D
	 * 	<br>param�tre: direction=oui
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=oui
	 * 	<br>param�tre: restriction=SPRITE_TRAVERSE
	 */
	/** in-air moving (in the plane) */ 
	MOVEHIGH,
	
	/** 
	 * action de se d�placer sur le sol
	 * INTRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>N/D
	 * 
	 * <p>ABILITY REFUSE
	 * 	<br>N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>param�tre: actor=oui (hero, bomb)
	 * 	<br>param�tre: target=N/D
	 * 	<br>param�tre: direction=oui
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=oui
	 * 	<br>param�tre: restriction=SPRITE_TRAVERSE
	 */
	/** on ground (normal) move (hero walking, bomb sliding, etc) */
	MOVELOW,
	
	/** 
	 * action de balancer un autre objet
	 * TRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>param�tre: actor=self
	 * 	<br>param�tre: target=oui (bomb)
	 * 	<br>param�tre: direction=oui
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 * 
	 * <p>ABILITY REFUSE
	 * 	<br>param�tre: actor=oui (hero)
	 * 	<br>param�tre: target=self
	 * 	<br>param�tre: direction=oui
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>param�tre: actor=oui (hero)
	 * 	<br>param�tre: target=oui (bomb)
	 * 	<br>param�tre: direction=oui
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 */
	/** hitting a bomb (or hero) to send it in the air */
	PUNCH,
	
	/** 
	 * action de pousser un autre objet (peut �tre un kick)
	 * TRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>param�tre: actor=self
	 * 	<br>param�tre: target=oui (bomb)
	 * 	<br>param�tre: direction=oui
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 * 
	 * <p>ABILITY REFUSE
	 * 	<br>param�tre: actor=oui (hero)
	 * 	<br>param�tre: target=self
	 * 	<br>param�tre: direction=oui
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>param�tre: actor=oui (hero)
	 * 	<br>param�tre: target=oui (bomb)
	 * 	<br>param�tre: direction=oui
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 */
	/** pushing an object (hero, bomb, wall...) in order to make it move on the ground */
	PUSH,

	/** 
	 * action d'activer l'explosion d'une remote bomb
	 * TRANSITIVE
	 * 
	 * <p>ABILITY PERFORM
	 * 	<br>param�tre: actor=self
	 * 	<br>param�tre: target=oui (bombe)
	 * 	<br>param�tre: direction=N/D
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 * 
	 * <p>ABILITY REFUSE (g�n�ralement pas utilis�, mais possible)
	 * 	<br>N/D
	 * 
	 * <p>ABILITY PREVENT
	 * 	<br>param�tre: actor=oui (hero)
	 * 	<br>param�tre: target=all
	 * 	<br>param�tre: direction=N/D
	 * 	<br>param�tre: strength=bool
	 * 	<br>param�tre: kind=N/D
	 * 	<br>param�tre: scope=N/D
	 * 	<br>param�tre: restriction=N/D
	 */
	/** asking for a remote bomb to explode (usually performed by a hero) */
	TRIGGER
	;

}
