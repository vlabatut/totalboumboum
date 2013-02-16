package org.totalboumboum.ai.v201213.adapter.agent;

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

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.ai.AiAbstractManager;
import org.totalboumboum.ai.v201213.adapter.communication.AiAction;
import org.totalboumboum.ai.v201213.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201213.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.internal.AiDataZone;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.event.ControlEvent;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.game.round.RoundVariables;
import org.xml.sax.SAXException;

/**
 * Classe servant de traducteur entre le jeu et l'agent :
 * <ul>
 * 		<li>Elle traduit les données du jeu en percepts 
 * 			traitables par l'agent (données simplifiées).</li>
 * 		<li>Elle traduit la réponse de l'agent (action) 
 * 			en un évènement compatible avec le jeu.</li>
 * </ul>
 * Elle n'est pas destinée à être modifiée par le
 * concepteur d'un agent (vous). Elle est réservée au moteur.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public abstract class AiManager extends AiAbstractManager<AiAction>
{	
	/////////////////////////////////////////////////////////////////
	// AI				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Termine proprement le gestionnaire de manière à libérer les ressources 
	 * qu'il occupait.
	 */
    @Override
	public final void finishAi()
	{	ArtificialIntelligence ai = ((ArtificialIntelligence)getAi());
		ai.stopRequest();
	}
	
    /////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** L'ensemble des percepts destinés à l'agent */
	private AiDataZone percepts;
	/** Le moteur du jeu */
	private VisibleLoop loop;
	/** Le niveau dans lequel la partie se déroule */
	private Level level;
	/** Date de la dernière mise à jour des percepts */
	private long lastUpdateTime = 0;
	
	@Override
	public final void init(String instance, AiPlayer player) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, ParserConfigurationException, SAXException, IOException
	{	super.init(instance,player);
	
		loop = RoundVariables.loop;
		level = RoundVariables.level;
		percepts = new AiDataZone(level,player);
		ArtificialIntelligence ai = ((ArtificialIntelligence)getAi());
		ai.setZone(percepts);
		output = ai.getOutput();
	}

	@Override
	public final void updatePercepts()
	{	long elapsedTime = loop.getTotalGameTime() - lastUpdateTime;
		lastUpdateTime = loop.getTotalGameTime();
		percepts.update(elapsedTime);
	}
	
	@Override
	public final void finishPercepts()
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
    /** Simule les touches de controle d'un joueur humain */
    private Direction lastMove = Direction.NONE;

	@Override
	public final List<ControlEvent> convertReaction(AiAction value)
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
	 * Active les évènements nécessaires à l'arrêt du personnage.
	 * Utilisé quand l'agent renvoie l'action "ne rien faire"
	 * 
	 * @param result	
	 * 		Liste des évènements adaptée à l'action renvoyée par l'agent.
	 */
	private final void reactionStop(List<ControlEvent> result)
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
	 * Active les évènements nécessaires à un changement de direction,
	 * en simulant un joueur humain qui appuierait sur des touches.
	 * 
	 * @param result
	 * 		Évènements de contrôle.
	 * @param direction
	 * 		Direction de déplacement.
	 */
	private final void updateMove(List<ControlEvent> result, Direction direction)
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
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected final void initSteps()
	{	ArtificialIntelligence ai = ((ArtificialIntelligence)getAi());
		
		// names
		List<String> stepNames = getStepNames();
		stepNames.addAll(ai.stepNames);
		
		// colors
		HashMap<String,Color> stepColors = getStepColors();
		List<Color> colorList = Arrays.asList(Color.BLUE,Color.CYAN,Color.GRAY,Color.GREEN,Color.MAGENTA,Color.ORANGE,Color.PINK,Color.RED,Color.WHITE,Color.YELLOW);
		Iterator<Color> it = colorList.iterator();
		for(String stepName: stepNames)
		{	if(!it.hasNext())
				it = colorList.iterator();
			Color color = it.next();;
			stepColors.put(stepName,color);
		}
		stepColors.put(TOTAL_DURATION,Color.DARK_GRAY);

		// durations
		HashMap<String,LinkedList<Long>> instantDurations = getInstantDurations();
		HashMap<String,Float> averageDurations = getAverageDurations();
		List<String> stepNames2 = new ArrayList<String>(stepNames);
		stepNames2.add(TOTAL_DURATION);
		for(String stepName: stepNames2)
		{	LinkedList<Long> list = new LinkedList<Long>();
			for(int i=0;i<AVERAGE_SCOPE;i++)
				list.add(0l);
			instantDurations.put(stepName,list);
			averageDurations.put(stepName,0f);
		}
	}
	
	/**
	 * Met à jour la map contenant
	 * les temps destinés au moteur.
	 */
	@Override
	public final void updateDurations()
	{	// init
		ArtificialIntelligence ai = ((ArtificialIntelligence)getAi());
		Map<String,LinkedList<Long>> instantDurations = getInstantDurations();
		Map<String,Float> averageDurations = getAverageDurations();
		List<String> stepNames = new ArrayList<String>(getStepNames());
		stepNames.add(0,TOTAL_DURATION);
		Map<String,Long> agentDurations = ai.getStepDurations();
		
		// update
		averageDurations.clear();
		for(String stepName: stepNames)
		{	// instant durations
			LinkedList<Long> list = instantDurations.get(stepName);
			list.poll();
			Long duration;
			if(stepName.equals(TOTAL_DURATION))
				duration = ai.getTotalDuration();
			else
			{	duration = agentDurations.get(stepName);
				if(duration==null)
					duration = 0l;
			}
			list.offer(duration);

			// average durations
			float average = 0;
			for(long value: list)
				average = average + value;
			average = average / AVERAGE_SCOPE;
			averageDurations.put(stepName,average);
		}
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Sortie de l'agent */
	private AiOutput output;
	
	/**
	 * Tout doit être recopié pour des histoires de synchronisation
	 * (on ne veut pas que l'agent modifie ses sorties pendant que
	 * le moteur du jeu est en train d'y accéder).
	 */
	@Override
	protected final void updateOutput()
	{	// tile colors
		{	List<Color>[][] aiMatrix = output.getTileColors();
			List<Color>[][] engineMatrix = getTileColors();
			for(int row=0;row<aiMatrix.length;row++)
				for(int col=0;col<aiMatrix[0].length;col++)
					engineMatrix[row][col] = new ArrayList<Color>(aiMatrix[row][col]);
		}
		
		// tile texts
		{	List<String>[][] aiMatrix = output.getTileTexts();
			List<String>[][] engineMatrix = getTileTexts();
			for(int row=0;row<aiMatrix.length;row++)
				for(int col=0;col<aiMatrix[0].length;col++)
					engineMatrix[row][col] = new ArrayList<String>(aiMatrix[row][col]);
		}
		
		// paths
		{	Map<AiPath,Color> aiPaths = output.getPaths();
			List<List<Tile>> enginePaths = getPaths();
			enginePaths.clear();
			List<Color> enginePathColors = getPathColors();
			enginePathColors.clear();
			for(Entry<AiPath, Color> entry: aiPaths.entrySet())
			{	// color
				Color color = entry.getValue();
				enginePathColors.add(color);
				// path
				AiPath aiPath = entry.getKey();
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
