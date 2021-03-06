package org.totalboumboum.ai.v201112.ais._simplet.v2;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import java.awt.Color;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * L'agent choisit un adversaire et ne le lâche plus
 * jusqu'à ce que l'un des deux soit éliminés.
 * Ici, cette cible est choisie en fonction de son classement général :
 * l'agent choisit le plus faible (en théorie, du moins).
 * 
 * @author Vincent Labatut
 */
@SuppressWarnings("deprecation")
public class TargetHandler extends AiAbstractHandler<Agent>
{	
	/**
	 * Initialise la classe avec l'IA
	 * passée en paramètre.
	 * 
	 * @param ai
	 * 		IA de référence.
	 */
	protected TargetHandler(Agent ai)
	{	super(ai);
		ai.checkInterruption();
		
		zone = ai.getZone();
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** La zone courante */
	protected AiZone zone = null;
	/** L'adversaire constituant la cible actuelle de l'agent */
	protected AiHero target = null;

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * On met à jour la cible si c'est nécessaire,
	 * i.e. : si on est en mode attaque, et si la cible
	 * actuelle est inexistante ou éliminée.
	 */
	protected void update()
	{	ai.checkInterruption();
		
		// on ne change d'adversaire que s'il est null ou déjà éliminé
		boolean change = target==null;
		if(change)
			print("      need to select a target: no target currently selected");
		else
		{	List<AiHero> remaining = zone.getRemainingOpponents();
			if(!remaining.contains(target))
			{	change = true;
				print("      need to select a target: the previous one is dead ("+target+")");
			}
		}
		
		// si on doit changer, on le fait pour l'adversaire le plus mal classé
		if(change)
		{	print("      reviewing remaining players:");
			List<AiHero> remaining = zone.getRemainingOpponents();
			int minRank = Integer.MAX_VALUE;
			AiHero minHero = null;
			for(AiHero hero: remaining)
			{	ai.checkInterruption();	
				
				int rank = zone.getStatsRank(hero);
				print("         +"+hero+" ["+rank+"]");
				if(rank<minRank)
				{	minRank = rank;
					minHero = hero;
				}
			}
			target = minHero;
			print("      new target="+target);
		}
		else
			print("      no need to select a target: keep the same target "+target);
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Permet d'activer/désactiver l'affichage de la cible */
	public boolean outputTarget = true;
	
	/**
	 * Met à jour les sorties graphiques de ce gestionnaire.
	 */
	protected void updateOutput()
	{	ai.checkInterruption();
		
		AiOutput output = ai.getOutput();
		Color color = Color.MAGENTA;
		
		// on représente la cible en magenta
		if(outputTarget && target!=null)
		{	AiTile tile = target.getTile();
			output.addTileColor(tile,color);
		}
	}
}
