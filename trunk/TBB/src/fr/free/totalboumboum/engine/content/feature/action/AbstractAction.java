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
	public static final String APPEAR = "appear";
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
	public static final String CONSUME = "consume";
	public static final String CRY = "cry";
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
	public static final String DETONATE = "detonate";
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
	public static final String DROP = "drop";
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
	public static final String GATHER = "gather";
	public static final String EXULT = "exult";
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
	public static final String JUMP = "jump";
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
	public static final String LAND = "land";
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
	public static final String MOVEHIGH = "movehigh";
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
	public static final String MOVELOW = "movelow";
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
	public static final String PUNCH = "punch";
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
	public static final String PUSH = "push";
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
	public static final String TRIGGER = "trigger";
// TODO manque l'action d'immobiliser une bombe en d�placement	

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
