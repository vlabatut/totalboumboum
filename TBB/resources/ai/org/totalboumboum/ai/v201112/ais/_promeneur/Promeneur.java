package org.totalboumboum.ai.v201112.ais._promeneur;

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

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**
 * Cette classe implémente un agent très faible,
 * qui se contente de se promener aléatoirement dans la zone
 * de jeu. Il ne se déplace pas complètement au hasard,
 * car il n'entre pas sur une case qui contient du feu.
 * Par contre, il n'anticipe pas du tout les explosions
 * à venir.
 * 
 * @author Vincent Labatut
 * @version 2 - version adaptée à l'API 2011-2012
 */
public class Promeneur extends ArtificialIntelligence 
{
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la zone de jeu */ 
	private AiZone zone = null;
	/** le personnage contrôlé par l'agent*/
	private AiHero ownHero = null;
	
	@Override
	protected void initPercepts() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		zone = getZone();
		ownHero = zone.getOwnHero();
	}
	
	@Override
	public void updatePercepts() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		// on met à jour la position de l'ia dans la zone
		moveHandler.currentTile = ownHero.getTile();
	}
	
	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** gestionnaire de mode (bidon) */
	protected AiModeHandler<Promeneur> modeHandler;
	/** gestionnaire d'utilité (bidon) */
	protected AiUtilityHandler<Promeneur> utilityHandler;
	/** gestionnaire de bombage (bidon) */
	protected AiBombHandler<Promeneur> bombHandler;
	/** gestionnaire de déplacement (réel) */
	protected MoveHandler moveHandler;
	
	@Override
	protected void initHandlers() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		modeHandler = new AiModeHandler<Promeneur>(this)
		{	@Override
			protected boolean isCollectPossible() throws StopRequestException
			{	return true;
			}
			
			@Override
			protected boolean hasEnoughItems() throws StopRequestException
			{	return false;
			}
		};
		
		utilityHandler = new AiUtilityHandler<Promeneur>(this)
		{	@Override
			protected Set<AiTile> selectTiles() throws StopRequestException
			{	return new TreeSet<AiTile>();
			}

			@Override
			protected void initCriteria() throws StopRequestException
			{	
			}

			@Override
			protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
			{	return null;
			}
		};
		
		bombHandler = new AiBombHandler<Promeneur>(this)
		{	@Override
			protected boolean considerBombing() throws StopRequestException
			{	return false;
			}
		};
		
		moveHandler = new MoveHandler(this);
	}

	@Override
	protected AiModeHandler<Promeneur> getModeHandler() throws StopRequestException
	{	return modeHandler;
	}

	@Override
	protected AiUtilityHandler<Promeneur> getUtilityHandler() throws StopRequestException
	{	return utilityHandler;
	}

	@Override
	protected AiBombHandler<Promeneur> getBombHandler() throws StopRequestException
	{	return bombHandler;
	}

	@Override
	protected AiMoveHandler<Promeneur> getMoveHandler() throws StopRequestException
	{	return moveHandler;
	}
}
