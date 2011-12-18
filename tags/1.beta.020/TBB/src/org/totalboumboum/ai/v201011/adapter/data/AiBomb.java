package org.totalboumboum.ai.v201011.adapter.data;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import java.util.List;

import org.totalboumboum.tools.images.PredefinedColor;

/**
 * repr�sente une bombe du jeu, ie un objet que les joueurs peuvent d�poser
 * pour d�truire les murs et �liminer les autre joueurs.
 * 
 * @author Vincent Labatut
 *
 */
public interface AiBomb extends AiSprite
{	
	/////////////////////////////////////////////////////////////////
	// FIRE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie un exemple de feu que cette bombe peut g�n�rer
	 * 
	 * @return	
	 * 		une repr�sentation du feu g�n�r� par cette bombe
	 */
	public AiFire getFirePrototype();

	/**
	 * renvoie la dur�e de l'explosion de cette bombe.
	 * Cette dur�e comprend l'apparition des flammes,
	 * la dur�e de vie des flammes, et leur disparition.
	 * Cette valeur n'est pas forc�ment constante, et peut varier d'une bombe � l'autre.
	 * 
	 * @return	
	 * 		la dur�e de l'explosion
	 */
	public long getExplosionDuration();

	/////////////////////////////////////////////////////////////////
	// FUSE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la probabilit� que la bombe tombe en panne quand elle devrait exploser
	 * 
	 * @return	
	 * 		une mesure de probabilit�
	 */
	public float getFailureProbability();
	
	/**
	 * indique si l'explosion de la bombe d�pend d'un compte � rebours
	 * 
	 * @return	
	 * 		vrai si la bombe d�pend d'un compte � rebours
	 */
	public boolean hasCountdownTrigger();
	
	/**
	 * indique si l'explosion de la bombe d�pend d'une t�l�commande
	 * 
	 * @return	
	 * 		vrai si la bombe d�pend d'une t�l�commande
	 */
	public boolean hasRemoteControlTrigger();
	
	/**
	 * indique si l'explosion de la bombe d�pend d'un contact avec du feu
	 * 
	 * @return	
	 * 		vrai si la bombe explose au contact du feu
	 */
	public boolean hasExplosionTrigger();
	
	/**
	 * renvoie le d�lai normal avant l'explosion de la bombe.
	 * Ce d�lai ne tient pas compte des pannes �ventuelles.
	 * Ce d�lai n'est pas d�fini pour tous les types de bombes
	 * 
	 * @return	
	 * 		le d�lai normal avant explosion exprim� en millisecondes
	 */
	public long getNormalDuration();

	/**
	 * renvoie la latence de cette bombe, dans le cas o� elle peut �tre d�clench�e par
	 * une explosion. Cette latence repr�sente le temps entre le moment o�
	 * la bombe est touch�e par l'explosion, et le moment o� elle commence effectivement
	 * � exploser.
	 * 
	 * @return	
	 * 		la latence de la bombe pour une d�tonation d�clench�e par une autre explosion
	 */
	public long getLatencyDuration();
	
	/////////////////////////////////////////////////////////////////
	// RANGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la port�e de la bombe
	 * (ie. le nombre de cases occup�es par sa flamme)
	 * 
	 * @return	
	 * 		port�e de la bombe
	 */
	public int getRange();
	
	/**
	 * indique si le feu �mis par la bombe peut traverser les murs
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plut�t getBlast().
	 * 
	 * @return	
	 * 		vrai si le feu peut traverser les murs
	 */
	public boolean isPenetrating();

	/**
	 * calcule une liste de cases correspondant au souffle de cette bombe,
	 * i.e. toutes les cases qui seront atteinte quand elle va exploser
	 * (y compris la case contenant la bombe elle-m�me). 
	 * Cette m�thode tient compte de murs, items, etc., c'est � dire qu'elle
	 * ne donne que les cases qui seront touch�es si la bombe devait exploser
	 * � l'instant o� cette m�thode est invoqu�e. Si un des obstacles � l'explosion
	 * disparait (par exemple si un joueur rammasse un item qui bloquait l'explosion),
	 * alors le souffle peut changer, il faut r�-ex�cuter cette m�thode pour avoir le
	 * nouveau souffle de la bombe dans ce nouvel environnement.
	 * 
	 * @return	
	 * 		une liste de cases correspondant aux cases qui seront touch�es par la flamme de cette bombe 
	 */
	public List<AiTile> getBlast();

	/////////////////////////////////////////////////////////////////
	// WORKING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * indique si cette bombe fonctionne normalement (ie si elle n'est pas tomb�e en panne)
	 * 
	 * @return	
	 * 		vrai si cette bombe marche, faux si elle est en panne
	 */
	public boolean isWorking();

	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la couleur de cette bombe.
	 * Cette couleur est null si aucun joueur n'a pos� la bombe 
	 * (pour certains niveaux sp�ciaux o� les blocs peuvent g�n�rer des bombes)  
	 * 
	 * @return 
	 * 		un symbole de type PredefinedColor repr�sentant une couleur
	 */
	public PredefinedColor getColor();

	/////////////////////////////////////////////////////////////////
	// OWNER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie le joueur qui a pos� la bombe,
	 * ou bien null si aucun joueur n'a pos� cette bombe 
	 * (pour certains niveaux sp�ciaux o� les blocs peuvent g�n�rer des bombes)  
	 * 
	 * @return 
	 * 		le joueur ayant pos� la bombe, ou null si aucun joueur ne l'a pos�e
	 */
	public AiHero getOwner();

	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la vitesse de d�placement au sol de cette bombe,
	 * exprim�e en pixel/seconde. il ne s'agit pas de la vitesse 
	 * de d�placement courante, il s'agit de la vitesse de la bombe
	 * quand elle glisse par terre. 
	 * 
	 * @return	
	 * 		la vitesse de d�placement de cette bombe
	 */
	public double getSlidingSpeed();
	
	/////////////////////////////////////////////////////////////////
	// LIFE TIME 		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie le temps �coul� depuis que le compte � rebours de la 
	 * bombe a commenc�, exprim� en millisecondes. Bien s�r ceci
	 * n'est valide que pour les bombes � retardement (qui ont un 
	 * compte � rebours).
	 * <b>Attention :</b> certaines actions sp�ciales comme le fait de lancer
	 * la bombe sont susceptibles de r�initialiser le compte � rebours.
	 * 
	 * @return	
	 * 		temps exprim� en ms
	 */
	public long getTime();

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * indique si ce bloc arr�te les personnages.
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		une valeur AiStopType indiquant si ce bloc arr�te les personnages
	 */
	public AiStopType hasStopHeroes();
	
	/**
	 * indique si ce bloc arr�te les explosions.
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		une valeur AiStopType indiquant si ce bloc arr�te le feu
	 */
	public AiStopType hasStopFires();
	
	/**
	 * teste si cette bombe est capable de passer � travers les items
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		vrai si la bombe traverse les items
	 */
	public boolean hasThroughItems();
}