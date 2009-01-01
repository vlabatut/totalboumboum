package fr.free.totalboumboum.engine.content.feature.action;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

public abstract class AbstractAction
{	
	
	public static final String ROLE_ALL = "all";
	public static final String ROLE_NONE = "none";
	public static final String DIRECTION_ALL = "all";
	public static final String CONTACT_ALL = "all";
	public static final String ORIENTATION_ALL = "all";
	public static final String TILE_POSITION_ALL = "all";
	
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
	public static final String APPEAR = "appear";
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
	public static final String CONSUME = "consume";
	public static final String CRY = "cry";
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
	public static final String DETONATE = "detonate";
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
	public static final String DROP = "drop";
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
	public static final String GATHER = "gather";
	public static final String EXULT = "exult";
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
	public static final String JUMP = "jump";
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
	public static final String LAND = "land";
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
	public static final String MOVEHIGH = "movehigh";
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
	public static final String MOVELOW = "movelow";
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
	public static final String PUNCH = "punch";
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
	public static final String PUSH = "push";
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
	public static final String TRIGGER = "trigger";
// TODO manque l'action d'immobiliser une bombe en déplacement	

	/** 
	 * name of this action 
	 */
	protected String name;

	public AbstractAction(String name)
	{	this.name = name;
	}
	
	public String getName()
	{	return name;
	}
	public void setName(String name)
	{	this.name = name;
	}

	protected boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
		}
	}
}
