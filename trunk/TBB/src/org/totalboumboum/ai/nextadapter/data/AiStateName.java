package org.totalboumboum.ai.nextadapter.data;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import org.totalboumboum.engine.content.feature.gesture.GestureName;

/**
 * repr�sente un nom associ� � un �tat, c'est � dire : l'action
 * effectu�e par le sprite concern�, ou bien l'action qu'il subit.
 * Ces actions sont diff�rentes des 'gestes' utilis�s dans le jeu,
 * car elles sont plus simples : une de ces actions peut correspondre � 
 * plusieurs gestes diff�rents. Par exemple, ce STANDING-ci
 * peut correspondre � un joueur qui marche (WALKING), ou bien � une bombe 
 * en train de glisser (SLIDING), ou bien � un joueur en train de pousser 
 * une bombe (PUSHING), etc.
 * 
 * @author Vincent
 *
 */

public enum AiStateName
{
	// equivalent gesture: BURNING
	/** le sprite est en train de br�ler */
	BURNING,
	// equivalent gesture: ENDED
	/** le sprite n'est plus en jeu */
	ENDED,
	// equivalent gesture: BOUNCING, JUMPING, LANDING, PUNCHED
	/** le sprite est en l'air (en train de sauter ou de rebondir sur les murs) */
	FLYING,
	// equivalent gesture: APPEARING, CRYING, ENTERING, EXULTING, OSCILLATING, OSCILLATING_FAILING, PREPARED, PUNCHING, STANDING, STANDING_FAILING, WAITING
	/** le sprite ne fait rien ou bien r�alise une action qui ne n�cessite pas de d�placement */ 
	STANDING,
	// equivalent gesture: PUSHING, SLIDING, SLIDING_FAILING, WALKING
	/** le sprite se d�place sur le sol */
	MOVING;
	
	// unused gestures: NONE, HIDING
	
	/**
	 * convertit une chaine de caract�res correspondant � un geste du jeu
	 * en un objet correspondant � une action telle qu'elle est per�ue par l'IA.
	 * 
	 * @param gesture	geste � convertir
	 * @return	le symbole de l'action correspondante 
	 */
	public static AiStateName makeNameFromGesture(GestureName gesture)
	{	AiStateName result = null;
		if(gesture==GestureName.BURNING)
			result = AiStateName.BURNING;
		else if(gesture==GestureName.ENDED)
			result = AiStateName.ENDED;
		else if(gesture==GestureName.BOUNCING
				|| gesture==GestureName.JUMPING
				|| gesture==GestureName.LANDING
				|| gesture==GestureName.PUNCHED)
			result = AiStateName.FLYING;
		else if(gesture==GestureName.APPEARING
				|| gesture==GestureName.CRYING
				|| gesture==GestureName.ENTERING
				|| gesture==GestureName.EXULTING
				|| gesture==GestureName.OSCILLATING
				|| gesture==GestureName.OSCILLATING_FAILING
				|| gesture==GestureName.PREPARED
				|| gesture==GestureName.PUNCHING
				|| gesture==GestureName.STANDING
				|| gesture==GestureName.STANDING_FAILING
				|| gesture==GestureName.WAITING)
			result = AiStateName.STANDING;
		else if(gesture==GestureName.PUSHING
				|| gesture==GestureName.SLIDING
				|| gesture==GestureName.SLIDING_FAILING
				|| gesture==GestureName.WALKING)
			result = AiStateName.MOVING;		
		return result;
	}
}
