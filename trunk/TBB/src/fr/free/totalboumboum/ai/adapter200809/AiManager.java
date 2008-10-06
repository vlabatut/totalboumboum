package fr.free.totalboumboum.ai.adapter200809;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import fr.free.totalboumboum.ai.AbstractAiManager;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.player.Player;

/**
 * 
 * Classe servant de traducteur entre le jeu et l'IA :
 * 	- elle traduit les données du jeu en percepts traitables par l'IA (données simplifiées).
 * 	- elle traduit la réponse de l'IA (action) en un évènement compatible avec le jeu.
 * 
 * @author Vincent
 *
 */

public abstract class AiManager extends AbstractAiManager<AiAction>
{
	/**
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
	private Loop loop;
	/** le niveau dans lequel la partie se déroule */
	private Level level;
	
	@Override
	public void init(String instance, Player player)
	{	super.init(instance,player);
		loop = player.getLoop();
		level = loop.getLevel();
		percepts = new AiZone(level,player);
		ArtificialIntelligence ai = ((ArtificialIntelligence)getAi());
		ai.setPercepts(percepts);
	}

	@Override
	public void updatePercepts()
	{	percepts.update();
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
	public ArrayList<ControlEvent> convertReaction(AiAction value)
	{	ArrayList<ControlEvent> result = new ArrayList<ControlEvent>();
		AiActionName name = value.getName();
		Direction direction = value.getDirection();
		ControlEvent event;
		String code;
		switch(name)
		{	case NONE:
				reactionStop(result);
				break;
			case MOVE:
				if(lastMove!=direction)
					reactionStop(result);
				if(direction!=Direction.NONE)
				{	code = ControlEvent.getCodeFromDirection(direction);
					event = new ControlEvent(code,true);
					result.add(event);
					lastMove = direction;
				}
				break;
			case DROP_BOMB :
			{	reactionStop(result);
				event = new ControlEvent(ControlEvent.DROPBOMB,true);
				result.add(event);
				event = new ControlEvent(ControlEvent.DROPBOMB,false);
			}
			case PUNCH :
			{	reactionStop(result);
				event = new ControlEvent(ControlEvent.PUNCHBOMB,true);
				result.add(event);
				event = new ControlEvent(ControlEvent.PUNCHBOMB,false);
			}
		}
		// 
		return result;
	}
	
	/**
	 * active les évènements nécessaires à l'arrêt du personnage.
	 * Utilisé quand l'IA renvoie l'action "ne rien faire"
	 * 
	 * @param result	liste des évènements adaptée à l'action renvoyée par l'IA
	 */
	private void reactionStop(ArrayList<ControlEvent> result)
	{	if(lastMove!=Direction.NONE)
		{	String code = ControlEvent.getCodeFromDirection(lastMove);
			ControlEvent event = new ControlEvent(code,false);
			result.add(event);
			lastMove = Direction.NONE;
		}
	}
}
