package org.totalboumboum.ai.v200708.adapter;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import java.io.Serializable;
import java.util.Vector;

/**
 * Représente l'ensemble des informations
 * accessibles à un agent.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public class AiPercepts implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
    /////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice représentant la zone de jeu */
	public int zoneMatrix[][];
	/** liste des bombes */
	public Vector<int[]> bombs;
	/** liste des joueurs */
	public Vector<int[]> players;
	/** liste des états (mort ou vif) des joueurs */
	public Vector<Boolean> playersStates;
	/** position du personnage de l'IA */
	public int[] ownPosition;
	/** temps avant le début du shrink */
	public long timeBeforeShrink;
	/** prochain bloc qui va être shrinké */
	public int nextShrinkPosition[];
	/** position relative de la bombe */
	public int bombPosition;
	/** Puissance des bombes de l'agent */
	public int ownFirePower;
	/** Nombre de bombes de l'agent */
	public int ownBombCount;
	/** Puissance des autres joueurs */
	public Vector<Integer> firePowers;
	/** Nombre de bombes des autres joueurs */
	public Vector<Integer> bombCounts;
	
    /////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Libère de la mémoire les variables
	 * utilisées pour représenter les percepts.
	 */
	public void finish()
	{	bombCounts.clear();
		bombs.clear();
		firePowers.clear();
		nextShrinkPosition = null;
		ownPosition = null;
		players.clear();
		playersStates.clear();
		zoneMatrix = null;
	}
}
