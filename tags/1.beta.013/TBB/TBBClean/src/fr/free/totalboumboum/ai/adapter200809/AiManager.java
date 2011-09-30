package fr.free.totalboumboum.ai.adapter200809;

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

import java.util.ArrayList;

import fr.free.totalboumboum.ai.AbstractAiManager;
import fr.free.totalboumboum.configuration.GameVariables;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.player.Player;

/**
 * 
 * Classe servant de traducteur entre le jeu et l'IA :
 * <br>	- elle traduit les donn�es du jeu en percepts traitables par l'IA (donn�es simplifi�es).
 * <br>	- elle traduit la r�ponse de l'IA (action) en un �v�nement compatible avec le jeu.
 * 
 * @author Vincent
 *
 */

public abstract class AiManager extends AbstractAiManager<AiAction>
{	/**
	 * Construit un gestionnaire pour l'IA pass�e en param�tre.
	 * Cette m�thode doit �tre appel�e par une classe h�ritant de celle-ci,
	 * et plac�e dans le package contenant l'IA. 
	 * 
	 * @param ai	l'ia que cette classe doit g�rer
	 */
	protected AiManager(ArtificialIntelligence ai)
    {	super(ai);
	}

    /////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement le gestionnaire de mani�re � lib�rer les ressources 
	 * qu'il occupait.
	 */
	public void finishAi()
	{	ArtificialIntelligence ai = ((ArtificialIntelligence)getAi());
		ai.stopRequest();
	}
	
    /////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** l'ensemble des percepts destin�s � l'IA */
	private AiZone percepts;
	/** le moteur du jeu */
	private Loop loop;
	/** le niveau dans lequel la partie se d�roule */
	private Level level;
	/** date de la derni�re mise � jour des percepts */
	private long lastUpdateTime = 0;
	
	@Override
	public void init(String instance, Player player)
	{	super.init(instance,player);
		loop = GameVariables.loop;
		level = GameVariables.level;
		percepts = new AiZone(level,player);
		ArtificialIntelligence ai = ((ArtificialIntelligence)getAi());
		ai.setPercepts(percepts);
	}

	@Override
	public void updatePercepts()
	{	long elapsedTime = loop.getTotalTime() - lastUpdateTime;
		lastUpdateTime = loop.getTotalTime();
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
	public ArrayList<ControlEvent> convertReaction(AiAction value)
	{	ArrayList<ControlEvent> result = new ArrayList<ControlEvent>();
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
	 * active les �v�nements n�cessaires � l'arr�t du personnage.
	 * Utilis� quand l'IA renvoie l'action "ne rien faire"
	 * 
	 * @param result	liste des �v�nements adapt�e � l'action renvoy�e par l'IA
	 */
	private void reactionStop(ArrayList<ControlEvent> result)
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
	 * active les �v�nements n�cessaires � un changement de direction,
	 * en simulant un joueur humain qui appuierait sur des touches
	 * @param result
	 * @param direction
	 */
	private void updateMove(ArrayList<ControlEvent> result, Direction direction)
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