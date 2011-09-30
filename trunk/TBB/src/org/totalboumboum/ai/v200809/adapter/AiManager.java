package org.totalboumboum.ai.v200809.adapter;

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

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.AbstractAiManager;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.event.ControlEvent;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.game.round.RoundVariables;

/**
 * 
 * Classe servant de traducteur entre le jeu et l'IA :
 * <br>	- elle traduit les données du jeu en percepts traitables par l'IA (données simplifiées).
 * <br>	- elle traduit la réponse de l'IA (action) en un évènement compatible avec le jeu.
 * 
 * @author Vincent Labatut
 *
 */

public abstract class AiManager extends AbstractAiManager<AiAction>
{	/**
	 * Construit un gestionnaire pour l'IA passée en paramètre.
	 * Cette méthode doit être appelée par une classe héritant de celle-ci,
	 * et placée dans le package contenant l'IA. 
	 * 
	 * @param ai	l'ia que cette classe doit gérer
	 */
	protected AiManager(ArtificialIntelligence ai)
    {	super(ai);
	}

    /////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement le gestionnaire de manière à libérer les ressources 
	 * qu'il occupait.
	 */
	public void finishAi()
	{	ArtificialIntelligence ai = ((ArtificialIntelligence)getAi());
		ai.stopRequest();
	}
	
    /////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** l'ensemble des percepts destinés à l'IA */
	private AiZone percepts;
	/** le moteur du jeu */
	private VisibleLoop loop;
	/** le niveau dans lequel la partie se déroule */
	private Level level;
	/** date de la dernière mise à jour des percepts */
	private long lastUpdateTime = 0;
	
	@Override
	public void init(String instance, AiPlayer player)
	{	super.init(instance,player);
		loop = RoundVariables.loop;
		level = RoundVariables.level;
		percepts = new AiZone(level,player);
		ArtificialIntelligence ai = ((ArtificialIntelligence)getAi());
		ai.setPercepts(percepts);
	}
	@Override
	public void updatePercepts()
	{	long elapsedTime = loop.getTotalGameTime() - lastUpdateTime;
		lastUpdateTime = loop.getTotalGameTime();
		percepts.update(elapsedTime);
	}
	
	@Override
	public void finishPercepts()
	{	// percepts
		percepts.finish();
		percepts = null;
		ArtificialIntelligence ai = ((ArtificialIntelligence)getAi());
		ai.finish();
		
		// misc
		loop = null;
		level = null;
	}

    /////////////////////////////////////////////////////////////////
	// REACTION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** simule les touches de controle d'un joueur humain */
    private Direction lastMove = Direction.NONE;

	@Override
	public List<ControlEvent> convertReaction(AiAction value)
	{	List<ControlEvent> result = new ArrayList<ControlEvent>();
		AiActionName name = value.getName();
		Direction direction = value.getDirection();
		ControlEvent event;
		switch(name)
		{	case NONE:
				reactionStop(result);
				break;
			case MOVE:
				updateMove(result,direction);
				break;
			case DROP_BOMB :
				reactionStop(result);
				event = new ControlEvent(ControlEvent.DROPBOMB,true);
				result.add(event);
				event = new ControlEvent(ControlEvent.DROPBOMB,false);
				result.add(event);
				break;
			case PUNCH :
				reactionStop(result);
				event = new ControlEvent(ControlEvent.PUNCHBOMB,true);
				result.add(event);
				event = new ControlEvent(ControlEvent.PUNCHBOMB,false);
				result.add(event);
				break;
		}
		// 
		return result;
	}
	
	/**
	 * active les évènements nécessaires à l'arrêt du personnage.
	 * utilisé quand l'IA renvoie l'action "ne rien faire"
	 * 
	 * @param result	liste des évènements adaptée à l'action renvoyée par l'IA
	 */
	private void reactionStop(List<ControlEvent> result)
	{	if(lastMove!=Direction.NONE)
		{	Direction prim[] = lastMove.getPrimaries();
			for(int i=0;i<prim.length;i++)
			{	if(prim[i]!=Direction.NONE)
				{	String code = ControlEvent.getCodeFromPrimaryDirection(prim[i]);
					ControlEvent event = new ControlEvent(code,false);
					result.add(event);
				}
			}
			lastMove = Direction.NONE;
		}
	}
	
	/**
	 * active les évènements nécessaires à un changement de direction,
	 * en simulant un joueur humain qui appuierait sur des touches
	 * @param result
	 * @param direction
	 */
	private void updateMove(List<ControlEvent> result, Direction direction)
	{	// init
		Direction prim1[] = lastMove.getPrimaries();
		Direction prim2[] = direction.getPrimaries();
		String code;
		ControlEvent event;
		// events
		for(int i=0;i<prim1.length;i++)
		{	if(prim1[i]!=prim2[i])
			{	if(prim1[i]!=Direction.NONE)
				{	code = ControlEvent.getCodeFromPrimaryDirection(prim1[i]);
					event = new ControlEvent(code,false);
					result.add(event);
				}
				if(prim2[i]!=Direction.NONE)
				{	code = ControlEvent.getCodeFromPrimaryDirection(prim2[i]);
					event = new ControlEvent(code,true);
					result.add(event);
				}
			}
			else if(prim2[i]!=Direction.NONE)
			{	code = ControlEvent.getCodeFromPrimaryDirection(prim2[i]);
				event = new ControlEvent(code,true);
				result.add(event);
			}
		}
		// new direction
		lastMove = direction;
	}

    /////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput()
	{	// inutile ici
	}
}