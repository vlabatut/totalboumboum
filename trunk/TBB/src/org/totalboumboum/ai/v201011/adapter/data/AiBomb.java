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
 * représente une bombe du jeu, ie un objet que les joueurs peuvent d�poser
 * pour détruire les murs et �liminer les autre joueurs.
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
	 * 		une représentation du feu g�n�r� par cette bombe
	 */
	public AiFire getFirePrototype();

	/**
	 * renvoie la dur�e de l'explosion de cette bombe.
	 * Cette dur�e comprend l'apparition des flammes,
	 * la dur�e de vie des flammes, et leur disparition.
	 * Cette valeur n'est pas forcément constante, et peut varier d'une bombe à l'autre.
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
	 * indique si l'explosion de la bombe dépend d'un compte à rebours
	 * 
	 * @return	
	 * 		vrai si la bombe dépend d'un compte à rebours
	 */
	public boolean hasCountdownTrigger();
	
	/**
	 * indique si l'explosion de la bombe dépend d'une télécommande
	 * 
	 * @return	
	 * 		vrai si la bombe dépend d'une télécommande
	 */
	public boolean hasRemoteControlTrigger();
	
	/**
	 * indique si l'explosion de la bombe dépend d'un contact avec du feu
	 * 
	 * @return	
	 * 		vrai si la bombe explose au contact du feu
	 */
	public boolean hasExplosionTrigger();
	
	/**
	 * renvoie le d�lai normal avant l'explosion de la bombe.
	 * Ce d�lai ne tient pas compte des pannes éventuelles.
	 * Ce d�lai n'est pas défini pour tous les types de bombes
	 * 
	 * @return	
	 * 		le d�lai normal avant explosion exprimé en millisecondes
	 */
	public long getNormalDuration();

	/**
	 * renvoie la latence de cette bombe, dans le cas où elle peut être d�clench�e par
	 * une explosion. Cette latence représente le temps entre le moment o�
	 * la bombe est touch�e par l'explosion, et le moment où elle commence effectivement
	 * à exploser.
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
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'IA,
	 * elle est destin�e au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plut�t getBlast().
	 * 
	 * @return	
	 * 		vrai si le feu peut traverser les murs
	 */
	public boolean isPenetrating();

	/**
	 * calcule une liste de cases correspondant au souffle de cette bombe,
	 * i.e. toutes les cases qui seront atteinte quand elle va exploser
	 * (y compris la case contenant la bombe elle-même). 
	 * Cette méthode tient compte de murs, items, etc., c'est à dire qu'elle
	 * ne donne que les cases qui seront touch�es si la bombe devait exploser
	 * à l'instant où cette méthode est invoquée. Si un des obstacles à l'explosion
	 * disparait (par exemple si un joueur rammasse un item qui bloquait l'explosion),
	 * alors le souffle peut changer, il faut r�-exécuter cette méthode pour avoir le
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
	 * (pour certains niveaux sp�ciaux où les blocs peuvent g�n�rer des bombes)  
	 * 
	 * @return 
	 * 		un symbole de type PredefinedColor représentant une couleur
	 */
	public PredefinedColor getColor();

	/////////////////////////////////////////////////////////////////
	// OWNER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie le joueur qui a pos� la bombe,
	 * ou bien null si aucun joueur n'a pos� cette bombe 
	 * (pour certains niveaux sp�ciaux où les blocs peuvent g�n�rer des bombes)  
	 * 
	 * @return 
	 * 		le joueur ayant pos� la bombe, ou null si aucun joueur ne l'a pos�e
	 */
	public AiHero getOwner();

	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la vitesse de déplacement au sol de cette bombe,
	 * exprimée en pixel/seconde. il ne s'agit pas de la vitesse 
	 * de déplacement courante, il s'agit de la vitesse de la bombe
	 * quand elle glisse par terre. 
	 * 
	 * @return	
	 * 		la vitesse de déplacement de cette bombe
	 */
	public double getSlidingSpeed();
	
	/////////////////////////////////////////////////////////////////
	// LIFE TIME 		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie le temps écoulé depuis que le compte à rebours de la 
	 * bombe a commencé, exprimé en millisecondes. Bien s�r ceci
	 * n'est valide que pour les bombes à retardement (qui ont un 
	 * compte à rebours).
	 * <b>Attention :</b> certaines actions sp�ciales comme le fait de lancer
	 * la bombe sont susceptibles de r�initialiser le compte à rebours.
	 * 
	 * @return	
	 * 		temps exprimé en ms
	 */
	public long getTime();

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * indique si ce bloc arrête les personnages.
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'IA,
	 * elle est destin�e au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		une valeur AiStopType indiquant si ce bloc arrête les personnages
	 */
	public AiStopType hasStopHeroes();
	
	/**
	 * indique si ce bloc arrête les explosions.
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'IA,
	 * elle est destin�e au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		une valeur AiStopType indiquant si ce bloc arrête le feu
	 */
	public AiStopType hasStopFires();
	
	/**
	 * teste si cette bombe est capable de passer à travers les items
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'IA,
	 * elle est destin�e au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		vrai si la bombe traverse les items
	 */
	public boolean hasThroughItems();
}
