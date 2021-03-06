package org.totalboumboum.ai.v201011.adapter;

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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.ai.AiAbstractManager;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.data.internal.AiDataZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.event.ControlEvent;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.game.round.RoundVariables;
import org.xml.sax.SAXException;

/**
 * 
 * Classe servant de traducteur entre le jeu et l'IA :
 * <br>	- elle traduit les données du jeu en percepts traitables par l'IA (données simplifiées).
 * <br>	- elle traduit la réponse de l'IA (action) en un évènement compatible avec le jeu.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public abstract class AiManager extends AiAbstractManager<AiAction,AiZone>
{	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
 	@Override
	public void init(String instance, AiPlayer player) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, ParserConfigurationException, SAXException, IOException, IllegalArgumentException, URISyntaxException
	{	super.init(instance,player);
	
		loop = RoundVariables.loop;
		level = RoundVariables.level;
		percepts = new AiDataZone(level,player);
		ArtificialIntelligence ai = ((ArtificialIntelligence)getAi());
		ai.setPercepts(percepts);
		output = ai.getOutput();
	}

   @Override
	public void finishAi()
	{	ArtificialIntelligence ai = ((ArtificialIntelligence)getAi());
		ai.stopRequest();
	}
	
    /////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** l'ensemble des percepts destinés à l'IA */
	private AiDataZone percepts;
	/** le moteur du jeu */
	private VisibleLoop loop;
	/** le niveau dans lequel la partie se déroule */
	private Level level;
	/** date de la dernière mise à jour des percepts */
	private long lastUpdateTime = 0;
	
	@Override
	public AiZone getCurrentPercepts()
    {	return percepts;
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
	
		if(value==null)
			value = new AiAction(AiActionName.NONE);
		
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
	 * @param result	
	 * 		liste des évènements adaptée à l'action renvoyée par l'IA
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
	 * 		?	
	 * @param direction
	 * 		?	
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
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initSteps()
	{	// durations
		HashMap<String,LinkedList<Long>> instantDurations = getInstantDurations();
		HashMap<String,Float> averageDurations = getAverageDurations();
		LinkedList<Long> list = new LinkedList<Long>();
		for(int i=0;i<AVERAGE_SCOPE;i++)
			list.add(0l);
		instantDurations.put(TOTAL_DURATION,list);
		averageDurations.put(TOTAL_DURATION,0f);
		
		// colors
		HashMap<String,Color> stepColors = getStepColors();
		stepColors.put(TOTAL_DURATION,Color.DARK_GRAY);
	}
	
	@Override
	public void updateDurations()
	{	// init
		ArtificialIntelligence ai = (ArtificialIntelligence)getAi();
		HashMap<String,LinkedList<Long>> instantDurations = getInstantDurations();
		HashMap<String,Float> averageDurations = getAverageDurations();
		
		// instant durations
		LinkedList<Long> list = instantDurations.get(TOTAL_DURATION);
		list.poll();
		long duration = ai.totalDuration;
		list.offer(duration);

		// average durations
		float average = 0;
		for(long value: list)
			average = average + value;
		average = average / AVERAGE_SCOPE;
		averageDurations.put(TOTAL_DURATION,average);
	}

    /////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** sortie de l'IA */
	private AiOutput output;
	
	/**
	 * tout doit être recopié pour des histoires de synchronisation
	 * (on ne veut pas que l'IA modifie ses sorties pendant que
	 * le moteur du jeu est en train d'y accéder)
	 */
	@Override
	protected void updateOutput()
	{	// tile colors
		{	Color[][] aiMatrix = output.getTileColors();
			List<Color>[][] engineMatrix = getTileColors();
			for(int line=0;line<aiMatrix.length;line++)
				for(int col=0;col<aiMatrix[0].length;col++)
					engineMatrix[line][col] = Arrays.asList(aiMatrix[line][col]);
		}
		
		// tile texts
		{	List<String>[][] aiMatrix = output.getTileTexts();
			List<String>[][] engineMatrix = getTileTexts();
			for(int line=0;line<aiMatrix.length;line++)
				for(int col=0;col<aiMatrix[0].length;col++)
					engineMatrix[line][col] = new ArrayList<String>(aiMatrix[line][col]);
		}
		
		// paths
		{	List<AiPath> aiPaths = output.getPaths();
			List<List<double[]>> enginePaths = getPaths();
			List<List<Long>> engineWaits = getPathWaits();
			enginePaths.clear();
			engineWaits.clear();
			List<Color> aiPathColors = output.getPathColors();
			List<Color> enginePathColors = getPathColors();
			enginePathColors.clear();
			for(int index=0;index<aiPaths.size();index++)
			{	// color
				Color color = aiPathColors.get(index);
				enginePathColors.add(color);
				// waits
				List<Long> waits = new ArrayList<Long>();
				engineWaits.add(waits);
				// path
				AiPath aiPath = aiPaths.get(index);
				List<double[]> path = new ArrayList<double[]>();
				enginePaths.add(path);
				for(AiTile tile: aiPath.getTiles())
				{	// wait
					long wait = 0l;
					waits.add(wait);
					// location
					double x = tile.getPosX();
					double y = tile.getPosY();
					double coord[] = {x,y};
					path.add(coord);
				}
			}
		}
	}
}
