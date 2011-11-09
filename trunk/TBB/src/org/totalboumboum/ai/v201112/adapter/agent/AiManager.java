package org.totalboumboum.ai.v201112.adapter.agent;

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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.AiAbstractManager;
import org.totalboumboum.ai.v201112.adapter.communication.AiAction;
import org.totalboumboum.ai.v201112.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201112.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.internal.AiDataZone;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.event.ControlEvent;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.game.round.RoundVariables;

/**
 * Classe servant de traducteur entre le jeu et l'agent :
 * <br>	- elle traduit les données du jeu en percepts traitables par l'agent (données simplifiées).
 * <br>	- elle traduit la réponse de l'agent (action) en un évènement compatible avec le jeu.
 * 
 * @author Vincent Labatut
 */
public abstract class AiManager extends AiAbstractManager<AiAction>
{	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * Cette méthode doit être appelée par une classe héritant de celle-ci,
	 * et placée dans le package contenant l'agent. 
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer
	 */
	protected AiManager(ArtificialIntelligence ai)
    {	super(ai);
	}

    /////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement le gestionnaire de manière à libérer les ressources 
	 * qu'il occupait.
	 */
    @Override
	public void finishAi()
	{	ArtificialIntelligence ai = ((ArtificialIntelligence)getAi());
		ai.stopRequest();
	}
	
    /////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** l'ensemble des percepts destinés à l'agent */
	private AiDataZone percepts;
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
		percepts = new AiDataZone(level,player);
		ArtificialIntelligence ai = ((ArtificialIntelligence)getAi());
		ai.setZone(percepts);
		output = ai.getOutput();
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
	 * utilisé quand l'agent renvoie l'action "ne rien faire"
	 * 
	 * @param result	
	 * 		liste des évènements adaptée à l'action renvoyée par l'agent
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
	 * @param 
	 * 		result
	 * @param 
	 * 		direction
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
	/** sortie de l'agent */
	private AiOutput output;
	
	/**
	 * tout doit être recopié pour des histoires de synchronisation
	 * (on ne veut pas que l'agent modifie ses sorties pendant que
	 * le moteur du jeu est en train d'y accéder)
	 */
	@Override
	protected void updateOutput()
	{	// tile colors
		{	Color[][] aiMatrix = output.getTileColors();
			Color[][] engineMatrix = getTileColors();
			for(int row=0;row<aiMatrix.length;row++)
				for(int col=0;col<aiMatrix[0].length;col++)
					engineMatrix[row][col] = aiMatrix[row][col];
		}
		
		// tile texts
		{	List<String>[][] aiMatrix = output.getTileTexts();
			List<String>[][] engineMatrix = getTileTexts();
			for(int row=0;row<aiMatrix.length;row++)
				for(int col=0;col<aiMatrix[0].length;col++)
					engineMatrix[row][col] = aiMatrix[row][col];
		}
		
		// paths
		{	List<AiPath> aiPaths = output.getPaths();
			List<List<Tile>> enginePaths = getPaths();
			enginePaths.clear();
			List<Color> aiPathColors = output.getPathColors();
			List<Color> enginePathColors = getPathColors();
			enginePathColors.clear();
			for(int index=0;index<aiPaths.size();index++)
			{	// color
				Color color = aiPathColors.get(index);
				enginePathColors.add(color);
				// path
				AiPath aiPath = aiPaths.get(index);
				List<Tile> path = new ArrayList<Tile>();
				enginePaths.add(path);
				for(AiLocation location: aiPath.getLocations())
				{	// TODO should be adapted so that the pixel coordinates are used instead of the tiles'
					AiTile aiTile = location.getTile();
					int row = aiTile.getRow();
					int col = aiTile.getCol();
					Tile tile = level.getTile(row,col);
					path.add(tile);
				}
			}
		}
	}
}
