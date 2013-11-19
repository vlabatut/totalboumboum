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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.totalboumboum.ai.AiAbstractManager;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.hollow.SuddenDeathEvent;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.consume.SpecificConsume;
import org.totalboumboum.engine.content.feature.event.ControlEvent;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.block.Block;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.game.round.RoundVariables;

/**
 * Classe servant d'interface entre les agents
 * et le jeu.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public abstract class AiManager extends AiAbstractManager<Integer,AiPercepts>
{	/** Flag de débogage */
	private boolean debug = false;
	
    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void finishAi()
	{	//
	}

    /////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** Percept à envoyer à l'IA */
	private AiPercepts percepts = null;
	
	@Override
    public AiPercepts getCurrentPercepts()
    {	return percepts;
    }

	@Override
	public void updatePercepts()
	{	percepts = new AiPercepts();
		
		// compute all the percepts
    	AbstractPlayer player = getPlayer(); 
    	VisibleLoop loop = RoundVariables.loop;
    	Tile[][] matrix = RoundVariables.level.getMatrix();
    	Sprite sprite = player.getSprite();
    	
    	// état du shrink
    	long totalTime = loop.getTotalGameTime();
    	HollowLevel hollowLevel = RoundVariables.loop.getRound().getHollowLevel();
    	List<SuddenDeathEvent> suddenDeathEvents = hollowLevel.getSuddenDeathEvents();
    	percepts.timeBeforeShrink = Long.MAX_VALUE;
    	percepts.nextShrinkPosition = new int[]{0,0};
    	if(!suddenDeathEvents.isEmpty())
    	{	// temps restant avant le shrink
    		SuddenDeathEvent suddenDeathEvent = suddenDeathEvents.get(0);
    		long time = suddenDeathEvent.getTime();
    		percepts.timeBeforeShrink = time - totalTime;
    		// position du prochain bloc shrinké (il peut y en avoir plusieurs, 
    		// mais on ne prend que le premier par simplification)
	    	Collection<List<Sprite>> sprites = suddenDeathEvent.getSprites().values();
	    	if(!sprites.isEmpty())
	    	{	List<Sprite> list = sprites.iterator().next();
	    		if(!list.isEmpty())
	    		{	Sprite s = list.get(0);
	    			Tile t = s.getTile();
	    			percepts.nextShrinkPosition = new int[]{t.getRow(),t.getCol()};
	    		}
	    	}
    	}
    	
    	// position du joueur
    	percepts.ownPosition = new int[2];
 		Tile tile = sprite.getTile();
 		percepts.ownPosition[0] = tile.getCol();
 		percepts.ownPosition[1] = tile.getRow();
        // propriétés du joueur
        {	// bomb range
        	StateAbility ab = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_RANGE);
        	percepts.ownFirePower = (int)ab.getStrength();
	        ab = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_RANGE_MAX);
			if(ab.isActive())
			{	int limit = (int)ab.getStrength();
				if(percepts.ownFirePower>limit)
					percepts.ownFirePower = limit;
			}
        }
        {	// bomb number
        	StateAbility ab = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_NUMBER);
	        ab = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_NUMBER_MAX);
	        percepts.ownBombCount = (int)ab.getStrength();
			if(ab.isActive())
			{	int limit = (int)ab.getStrength();
				if(percepts.ownBombCount>limit)
					percepts.ownBombCount = limit;
			}
			percepts.ownBombCount = percepts.ownBombCount - sprite.getDroppedBombs().size();
        }

        // position relative de l'éventuelle bombe
        percepts.bombPosition = ArtificialIntelligence.AI_DIR_NONE;
        List<Bomb> bombes = tile.getBombs();
        if(bombes.size()>0)
        {	int minX = Integer.MAX_VALUE;
        	int minY = Integer.MAX_VALUE;
        	Iterator<Bomb> i = bombes.iterator();
        	while(i.hasNext())
        	{	Bomb bomb = i.next();
        		int dx = (int)(sprite.getCurrentPosX()-bomb.getCurrentPosX());
        		if(dx<minX)
        			minX = dx;
        		int dy = (int)(sprite.getCurrentPosY()-bomb.getCurrentPosY());
        		if(dy<minY)
        			minY = dy;
        	}
        	// joueur pas parfaitement sur la bombe 
        	if(minX!=0 || minY!=0)
        	{	// même ligne ?
    	    	if(Math.abs(minX)>=Math.abs(minY))
    	    		if(minX>0)
    	    			percepts.bombPosition = ArtificialIntelligence.AI_DIR_LEFT;
    	    		else
    	    			percepts.bombPosition = ArtificialIntelligence.AI_DIR_RIGHT;
    	    	// même colonne ?
    	    	else
    	    		if(minY>0)
    	    			percepts.bombPosition = ArtificialIntelligence.AI_DIR_UP;
    	    		else
    	    			percepts.bombPosition = ArtificialIntelligence.AI_DIR_DOWN;
        	}
        }

    	// matrice de la zone et listes de bombes et de personnages
        percepts.zoneMatrix = new int[matrix[0].length][matrix.length];
        percepts.bombs = new Vector<int[]>();
    	for(int x=0;x<matrix.length;x++)
	    {	for (int y=0;y<matrix[0].length;y++)
	    	{	Tile temp = matrix[x][y];
	    		// bloc vide
	    		percepts.zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_EMPTY;
	    		// mur
	    		if(temp.getBlocks().size()>0)
	    		{	Block b = temp.getBlocks().get(0);
	    			GestureName gesture = b.getCurrentGesture().getName();
	    			if(!(gesture==GestureName.NONE
						|| gesture==GestureName.HIDING
						|| gesture==GestureName.ENDED))
	    			{	SpecificAction action = new SpecificConsume(b);
		    			// mur indestructible
		    			if(sprite.isTargetPreventing(action))
		    				percepts.zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_WALL_HARD;
		    			// mur destructible
		    			else
		    				percepts.zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_WALL_SOFT;
	    			}
	    		}
	    		// bombe
	    		else if(temp.getBombs().size()>0)
	    		{	percepts.zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_BOMB;
	    			Bomb bomb = temp.getBombs().get(0);
	    			int tempBombData[] = {temp.getCol(),temp.getRow(),bomb.getFlameRange()};
	    			percepts.bombs.add(tempBombData);
	    		}
	    		// feu
	    		else if(temp.getFires().size()>0)
	    			percepts.zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_FIRE;
	    		// bonus/malus
	    		else if(temp.getItems().size()>0)
	    		{	List<Item> items = temp.getItems();
	    			Iterator<Item> it = items.iterator();
	    			boolean found = false;
	    			while(!found && it.hasNext())
	    			{	Item item = it.next();
	    				GestureName gesture = item.getCurrentGesture().getName();
	    				if(!(gesture==GestureName.NONE
	    						|| gesture==GestureName.HIDING
	    						|| gesture==GestureName.ENDED))
	    				{	List<AbstractAbility> itemAbilities = item.getItemAbilities();
			    			// bonus de bombe
			    			{	Iterator<AbstractAbility> j = itemAbilities.iterator();
				    			while(j.hasNext() && !found)
				    			{	AbstractAbility a = j.next();
				    				if(a instanceof StateAbility)
				    				{	StateAbility sa = (StateAbility) a;
				    					if(sa.getName().equals(StateAbilityName.HERO_BOMB_NUMBER))
				    					{	found = true;
				    						percepts.zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_ITEM_BOMB;	
				    					}
				    				}
				    			}
			    			}
			    			// bonus de feu
			    			if(!found)
			    			{	Iterator<AbstractAbility> j = itemAbilities.iterator();
				    			while(j.hasNext() && !found)
				    			{	//AbstractAbility a = j.next();
				    				// to avoid blocking situations, any other item is seen as a bomb extra range 
				    				//if(a.getName().equals(StateAbility.BOMB_RANGE))
				    				{	found = true;
				    					percepts.zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_ITEM_FIRE;
				    				}
				    			}
			    			}
			    			if(!found)
			    			{	found = true;
			    				percepts.zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_UNKNOWN;			    			
			    			}
	    				}	    				
	    			}
	    		}
	    	}
	    }
    	
    	// personnages
    	percepts.players = new Vector<int[]>();
    	percepts.playersStates = new Vector<Boolean>();
    	percepts.firePowers = new Vector<Integer>();
    	percepts.bombCounts = new Vector<Integer>();
		List<AbstractPlayer> plyrs = loop.getPlayers();
		Iterator<AbstractPlayer> i = plyrs.iterator();
		while(i.hasNext())
		{	AbstractPlayer tempPlayer = i.next();
			// le joueur représenté par cet objet ne doit pas apparaitre dans cette liste
			if(tempPlayer!=player)
			{	// position
				Tile t = tempPlayer.getSprite().getTile();
				int tempX = t.getCol();
				int tempY = t.getRow();
				// direction
				Direction tempDir = tempPlayer.getSprite().getActualDirection();
				int tempDirAI;
				if(tempDir==Direction.UP)
					tempDirAI = ArtificialIntelligence.AI_DIR_UP;
				else if(tempDir==Direction.DOWN)
					tempDirAI = ArtificialIntelligence.AI_DIR_DOWN;
				else if(tempDir==Direction.LEFT || tempDir==Direction.UPLEFT || tempDir==Direction.DOWNLEFT)
					tempDirAI = ArtificialIntelligence.AI_DIR_LEFT;
				else if(tempDir==Direction.RIGHT || tempDir==Direction.UPRIGHT || tempDir==Direction.DOWNRIGHT)
					tempDirAI = ArtificialIntelligence.AI_DIR_RIGHT;
				else						
					tempDirAI = ArtificialIntelligence.AI_DIR_NONE;
				int tempPlayerData[] = {tempX,tempY,tempDirAI};
				percepts.players.add(tempPlayerData);
				percepts.playersStates.add(!tempPlayer.isOut());
				StateAbility ab = tempPlayer.getSprite().modulateStateAbility(StateAbilityName.HERO_BOMB_RANGE);
				percepts.firePowers.add((int)ab.getStrength());
		        ab = tempPlayer.getSprite().modulateStateAbility(StateAbilityName.HERO_BOMB_NUMBER);
		        percepts.bombCounts.add((int)ab.getStrength());
			}
		}
		
		if(debug)
		{	//zoneMatrix
			System.out.println("zoneMatrix:");
			for(int x=0;x<percepts.zoneMatrix.length;x++)
		    {	for (int y=0;y<percepts.zoneMatrix[0].length;y++)
		    	{	System.out.print(percepts.zoneMatrix[x][y]+" ");		    	
		    	}
		    	System.out.println();
		    }
			//bombs
			System.out.print("bombs:");
			for(int k=0;k<percepts.bombs.size();k++)
				System.out.print("("+percepts.bombs.get(k)[0]+","+percepts.bombs.get(k)[1]+","+percepts.bombs.get(k)[2]+") ");
			System.out.println();
			//players
			System.out.print("players:");
			for(int k=0;k<percepts.players.size();k++)
				System.out.print("("+percepts.players.get(k)[0]+","+percepts.players.get(k)[1]+","+percepts.players.get(k)[2]+") ");
			System.out.println();
			//playersStates
			System.out.print("playersStates:");
			for(int k=0;k<percepts.playersStates.size();k++)
				System.out.print(percepts.playersStates.get(k)+"; ");
			System.out.println();
			// ownPosition
			System.out.println("ownPosition: "+percepts.ownPosition[0]+";"+percepts.ownPosition[1]);
			//timeBeforeShrink
			//nextShrinkPosition
			System.out.println("nextShrinkPosition: "+percepts.nextShrinkPosition[0]+";"+percepts.nextShrinkPosition[1]);
			//bombPosition
			System.out.println("bombPosition: "+percepts.bombPosition);
			//ownFirePower
			System.out.println("ownFirePower: "+percepts.ownFirePower);
			//ownBombCount
			System.out.println("ownBombCount: "+percepts.ownBombCount);
			//firePowers
			//bombCounts
		}
		
		// set the percepts
		((ArtificialIntelligence)getAi()).setPercepts(percepts);
	}

	@Override
	public void finishPercepts()
	{	percepts.finish();
		controlKeys.clear();
	}
	
    /////////////////////////////////////////////////////////////////
	// REACTION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** Simulates control keys */
    private List<Integer> controlKeys = new ArrayList<Integer>();

	@Override
	public List<ControlEvent> convertReaction(Integer value)
	{	List<ControlEvent> result = new ArrayList<ControlEvent>();
		ControlEvent event;
		if(debug)
			System.out.print("action:");
		
		if(value==null)
			value = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		
		switch(value)
		{	case ArtificialIntelligence.AI_ACTION_DO_NOTHING :
				if(debug)
    				System.out.print("none\t");
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_DOWN))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_DOWN);
					event = new ControlEvent(ControlEvent.DOWN,false);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_LEFT))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_LEFT);
					event = new ControlEvent(ControlEvent.LEFT,false);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_RIGHT))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_RIGHT);
					event = new ControlEvent(ControlEvent.RIGHT,false);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_UP))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_UP);
					event = new ControlEvent(ControlEvent.UP,false);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				break;
			case ArtificialIntelligence.AI_ACTION_GO_DOWN :
				if(debug)
    				System.out.print("down\t");
				event = new ControlEvent(ControlEvent.DOWN,true);
				result.add(event);
				if(!controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_DOWN))
				{	controlKeys.add(ArtificialIntelligence.AI_ACTION_GO_DOWN);
					if(debug)
	    				System.out.print("x ");
				}
				else
				{	if(debug)
	    				System.out.print("v ");
				}
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_LEFT))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_LEFT);
					event = new ControlEvent(ControlEvent.LEFT,false);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_RIGHT))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_RIGHT);
					event = new ControlEvent(ControlEvent.RIGHT,false);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_UP))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_UP);
					event = new ControlEvent(ControlEvent.UP,false);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				break;
			case ArtificialIntelligence.AI_ACTION_GO_LEFT :
				if(debug)
    				System.out.print("left\t");
				event = new ControlEvent(ControlEvent.DOWN,false);
				result.add(event);
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_DOWN))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_DOWN);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				if(!controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_LEFT))
				{	controlKeys.add(ArtificialIntelligence.AI_ACTION_GO_LEFT);
					event = new ControlEvent(ControlEvent.LEFT,true);
					result.add(event);
					if(debug)
	    				System.out.print("v ");
				}
				else
				{	if(debug)
	    				System.out.print("x ");
				}
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_RIGHT))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_RIGHT);
					event = new ControlEvent(ControlEvent.RIGHT,false);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_UP))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_UP);
					event = new ControlEvent(ControlEvent.UP,false);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				break;
			case ArtificialIntelligence.AI_ACTION_GO_RIGHT :
				if(debug)
    				System.out.print("right\t");
				event = new ControlEvent(ControlEvent.DOWN,false);
				result.add(event);
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_DOWN))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_DOWN);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_LEFT))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_LEFT);
					event = new ControlEvent(ControlEvent.LEFT,false);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				if(!controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_RIGHT))
				{	controlKeys.add(ArtificialIntelligence.AI_ACTION_GO_RIGHT);
					event = new ControlEvent(ControlEvent.RIGHT,true);
					result.add(event);
					if(debug)
	    				System.out.print("v ");
				}
				else
				{	if(debug)
	    				System.out.print("x ");
				}
				event = new ControlEvent(ControlEvent.UP,false); 
				result.add(event);
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_UP))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_UP);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				break;
			case ArtificialIntelligence.AI_ACTION_GO_UP :
				if(debug)
    				System.out.print("up\t");
				event = new ControlEvent(ControlEvent.DOWN,false);
				result.add(event);
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_DOWN))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_DOWN);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_LEFT))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_LEFT);
					event = new ControlEvent(ControlEvent.LEFT,false);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_RIGHT))
				{	controlKeys.remove((Object)ArtificialIntelligence.AI_ACTION_GO_RIGHT);
					event = new ControlEvent(ControlEvent.RIGHT,false);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				if(!controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_UP))
				{	controlKeys.add(ArtificialIntelligence.AI_ACTION_GO_UP);
					event = new ControlEvent(ControlEvent.UP,true);
					result.add(event);
					if(debug)
	    				System.out.print("v ");
				}
				else
				{	if(debug)
	    				System.out.print("x ");
				}
				break;
			case ArtificialIntelligence.AI_ACTION_PUT_BOMB :
				if(debug)
    				System.out.print("drop");
				event = new ControlEvent(ControlEvent.DROPBOMB,true);
				result.add(event);
				event = new ControlEvent(ControlEvent.DROPBOMB,false);
				result.add(event);
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_DOWN))
				{	event = new ControlEvent(ControlEvent.DOWN,true);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_LEFT))
				{	event = new ControlEvent(ControlEvent.LEFT,true);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_RIGHT))
				{	event = new ControlEvent(ControlEvent.RIGHT,true);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				if(controlKeys.contains(ArtificialIntelligence.AI_ACTION_GO_UP))
				{	event = new ControlEvent(ControlEvent.UP,true);
					result.add(event);
					if(debug)
	    				System.out.print("^ ");
				}
				else
				{	if(debug)
	    				System.out.print("- ");
				}
				break;
			}
			if(debug)
				System.out.println();
		// 
		return result;
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
	@Override
	protected void updateOutput()
	{	// inutile ici
	}
}
