package org.totalboumboum.ai.v201112.ais._simplet;

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

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.engine.content.feature.gesture.modulation.TargetModulation;

/**
 * Cet agent très simple a pour but de montrer comment utiliser l'API.
 * Il n'est pas fait pour être efficace (il suffit de jouer contre lui
 * pour s'en apercevoir). Il est très simple, afin de ne pas vous donner
 * un agent trop efficace que vous copieriez. En fait, en raison de sa
 * simplicité, l'agent n'est pas complètement conforme à ce qui est demandé
 * pour le projet. C'est surtout le cas du gestionnaire de déplacement 
 * {@link MoveHandler} (cf. la documentation de cette classe).
 * <br/>
 * L'agent Simplet change très peu d'objectif, il est capable  de trouver un chemin 
 * pour atteindre son adversaire. Sa stratégie  d'attaque est minimaliste :
 * il se contente de poser une bombe à la fois de manière à géner son 
 * adversaire. Ceci montre bien l'inutilité de cette stratégie : n'importe 
 * quel joueur, même moyen, pourra s'échapper facilement de ce genre de 
 * situation.
 * <br/>
 * Il utilise un gestionnare spécifique {@link TargetModulation} pour 
 * déterminer quel adversaire est sa cible. Il utilise également un
 * autre gestionnaire {@link CommonTools}, qui contient des méthodes
 * générales utilisables par les autres gestionnaires.
 *
 * @author Vincent Labatut
 */
public class Simplet extends ArtificialIntelligence
{
	@Override
	protected void init() throws StopRequestException
	{	checkInterruption();
		
		// à modifier si vous voulez afficher les sorties texte de l'agent
		verbose = false;

		super.init();
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() throws StopRequestException
	{	checkInterruption();
	
	}
	
	@Override
	protected void updatePercepts() throws StopRequestException
	{	checkInterruption();
		
		// on met à jour les gestionnaires non standard,
		// car ils ne seront pas mis à jour par l'algo général
		{	long before = print("    > Entering commonTools.update");
			commonTools.update();
			long after = System.currentTimeMillis();
			long elapsed = after - before;
			print("    < Exiting commonTools.update: duration="+elapsed+" ms");
		}
		{	long before = print("    > Entering targetHandler.update");
			targetHandler.update();
			long after = System.currentTimeMillis();
			long elapsed = after - before;
			print("    < Exiting targetHandler.update: duration="+elapsed+" ms");
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Méthodes communes */
	public CommonTools commonTools;
	/** Gestionnaire chargé de mettre à jour la cible de l'agent (gestionnaire non-standard) */
	protected TargetHandler targetHandler;
	/** Gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** Gestionnaire chargé de calculer les valeurs d'utilité de l'agent */
	protected UtilityHandler utilityHandler;
	/** Gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** Gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;
	
	@Override
	protected void initHandlers() throws StopRequestException
	{	checkInterruption();
		// la sortie texte de chaque gestionnaire peut être désactivée
		// individuellement en modifiant les constantes, ou bien tous 
		// en même temps avec le verbose de cette classe principale
		
		commonTools = new CommonTools(this);
		commonTools.verbose = verbose && true;
		
		targetHandler = new TargetHandler(this);
		targetHandler.verbose = verbose && true;
		
		modeHandler = new ModeHandler(this);
		modeHandler.verbose = verbose && true;

		utilityHandler = new UtilityHandler(this);
		utilityHandler.verbose = verbose && true;

		bombHandler = new BombHandler(this);
		bombHandler.verbose = verbose && true;

		moveHandler = new MoveHandler(this);
		moveHandler.verbose = verbose && true;
	}

	@Override
	protected AiModeHandler<Simplet> getModeHandler() throws StopRequestException
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<Simplet> getUtilityHandler() throws StopRequestException
	{	checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<Simplet> getBombHandler() throws StopRequestException
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<Simplet> getMoveHandler() throws StopRequestException
	{	checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{	checkInterruption();
		
		targetHandler.updateOutput();
		moveHandler.updateOutput();
		utilityHandler.updateOutput();
	}
}
