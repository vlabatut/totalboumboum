package fr.free.totalboumboum.ai.adapter200708;

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
import java.util.Iterator;
import java.util.Vector;

import fr.free.totalboumboum.ai.AbstractAiManager;
import fr.free.totalboumboum.configuration.GameVariables;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbilityName;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.gesture.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.consume.SpecificConsume;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.player.Player;

public abstract class AiManager extends AbstractAiManager<Integer>
{	private boolean debug = false;

	public AiManager(ArtificialIntelligence ai)
    {	super(ai);
		controlKeys = new ArrayList<Integer>();		
	}
	
    /////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finishAi()
	{	
	}

    /////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** percept � envoyer � l'IA : matrice repr�sentant la zone de jeu */
    private int[][] zoneMatrix;;
    /** percept � envoyer � l'IA : liste des bombes */
    private Vector<int[]> bombs;
    /** percept � envoyer � l'IA : liste des personnages */
    private Vector<int[]> players;
    /** percept � envoyer � l'IA : �tats personnages */
    private Vector<Boolean> playersStates;
    /** percept � envoyer � l'IA : temps avant le d�but du shrink */
    private long timeBeforeShrink;
    /** percept � envoyer � l'IA : prochaine position du shrink */
    private int nextShrinkPosition[];
    /** percept � envoyer � l'IA : position du personnage de l'IA */
    private int ownPosition[];
    /** percept � envoyer � l'IA : position relative de la bombe */
    private int bombPosition;
    /** percept � envoyer � l'IA : la portee de la bombe */
    private int ownFirePower;
    /** percept � envoyer � l'IA : le nombre total de bombes */
    private int ownBombCount;
    /** percept � envoyer � l'IA : la portee de la bombe */
    private Vector<Integer> firePowers;
    /** percept � envoyer � l'IA : le nombre total de bombes */
    private Vector<Integer> bombCounts;

	@Override
	public void updatePercepts()
	{	// compute all the percepts
    	Player player = getPlayer(); 
    	Loop loop = GameVariables.loop;
    	Tile[][] matrix = GameVariables.level.getMatrix();
    	Sprite sprite = player.getSprite();
    	
    	// �tat du shrink
//NOTE � compl�ter    	
    	timeBeforeShrink = Long.MAX_VALUE;
    	// position du prochain bloc shrink�
//NOTE � compl�ter    	
    	nextShrinkPosition = new int[2];
    	nextShrinkPosition[0] = 0;
    	nextShrinkPosition[1] = 0;
    	
    	// position du joueur
 		ownPosition = new int[2];
 		Tile tile = sprite.getTile();
        ownPosition[0] = tile.getCol();
        ownPosition[1] = tile.getLine();
        // propri�t�s du joueur
        StateAbility ab = sprite.modulateStateAbility(StateAbilityName.BOMB_RANGE);
		ownFirePower = (int)ab.getStrength();
        ab = sprite.modulateStateAbility(StateAbilityName.BOMB_NUMBER);
		ownBombCount = (int)ab.getStrength() - sprite.getDroppedBombs().size();

        // position relative de l'�ventuelle bombe
        bombPosition = ArtificialIntelligence.AI_DIR_NONE;
        ArrayList<Bomb> bombes = tile.getBombs();
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
        	{	// m�me ligne ?
    	    	if(Math.abs(minX)>=Math.abs(minY))
    	    		if(minX>0)
    	    			bombPosition = ArtificialIntelligence.AI_DIR_LEFT;
    	    		else
    	    			bombPosition = ArtificialIntelligence.AI_DIR_RIGHT;
    	    	// m�me colonne ?
    	    	else
    	    		if(minY>0)
    	    			bombPosition = ArtificialIntelligence.AI_DIR_UP;
    	    		else
    	    			bombPosition = ArtificialIntelligence.AI_DIR_DOWN;
        	}
        }

    	// matrice de la zone et listes de bombes et de personnages
 		zoneMatrix = new int[matrix[0].length][matrix.length];
    	bombs = new Vector<int[]>();
    	for(int x=0;x<matrix.length;x++)
	    {	for (int y=0;y<matrix[0].length;y++)
	    	{	Tile temp = matrix[x][y];
	    		// bloc vide
    			zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_EMPTY;
	    		// mur
	    		if(temp.getBlock()!=null)
	    		{	Block b = temp.getBlock();
	    			SpecificAction action = new SpecificConsume(b);
	    			// mur indestructible
	    			if(sprite.isTargetPreventing(action))
	    				zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_WALL_HARD;
	    			// mur destructible
	    			else
	    				zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_WALL_SOFT;
	    		}
	    		// bombe
	    		else if(temp.getBombs().size()>0)
	    		{	zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_BOMB;
	    			Bomb bomb = temp.getBombs().get(0);
	    			int tempBombData[] = {temp.getCol(),temp.getLine(),bomb.getFlameRange()};
	    			bombs.add(tempBombData);
	    		}
	    		// feu
	    		else if(temp.getFires().size()>0)
	    			zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_FIRE;
	    		// bonus/malus
	    		else if(temp.getItem()!=null)
	    		{	ArrayList<AbstractAbility> itemAbilities = temp.getItem().getItemAbilities();
	    			boolean found = false;
    				// bonus de bombe
	    			{	Iterator<AbstractAbility> j = itemAbilities.iterator();
		    			while(j.hasNext() && !found)
		    			{	AbstractAbility a = j.next();
		    				if(a instanceof StateAbility)
		    				{	StateAbility sa = (StateAbility) a;
		    					if(sa.getName().equals(StateAbilityName.BOMB_NUMBER))
		    					{	found = true;
		    						zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_ITEM_BOMB;	
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
			    				zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_ITEM_FIRE;
		    				}
		    			}
	    			}
	    			if(!found)
	    				zoneMatrix[y][x] = ArtificialIntelligence.AI_BLOCK_UNKNOWN;
	    		}
	    	}
	    }
    	
    	// personnages
    	players = new Vector<int[]>();
    	playersStates = new Vector<Boolean>();
    	firePowers = new Vector<Integer>();
    	bombCounts = new Vector<Integer>();
		ArrayList<Player> plyrs = loop.getPlayers();
		Iterator<Player> i = plyrs.iterator();
		while(i.hasNext())
		{	Player tempPlayer = i.next();
			// le joueur repr�sent� par cet objet ne doit pas apparaitre dans cette liste
			if(tempPlayer!=player)
			{	// position
				Tile t = tempPlayer.getSprite().getTile();
				int tempX = t.getCol();
				int tempY = t.getLine();
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
				players.add(tempPlayerData);
				playersStates.add(!tempPlayer.getSprite().isEnded());
		        ab = tempPlayer.getSprite().modulateStateAbility(StateAbilityName.BOMB_RANGE);
		        firePowers.add((int)ab.getStrength());
		        ab = tempPlayer.getSprite().modulateStateAbility(StateAbilityName.BOMB_NUMBER);
		        bombCounts.add((int)ab.getStrength());
			}
		}
		
		if(debug)
		{	//zoneMatrix
			System.out.println("zoneMatrix:");
			for(int x=0;x<zoneMatrix.length;x++)
		    {	for (int y=0;y<zoneMatrix[0].length;y++)
		    	{	System.out.print(zoneMatrix[x][y]+" ");		    	
		    	}
		    	System.out.println();
		    }
			//bombs
			System.out.print("bombs:");
			for(int k=0;k<bombs.size();k++)
				System.out.print("("+bombs.get(k)[0]+","+bombs.get(k)[1]+","+bombs.get(k)[2]+") ");
			System.out.println();
			//players
			System.out.print("players:");
			for(int k=0;k<players.size();k++)
				System.out.print("("+players.get(k)[0]+","+players.get(k)[1]+","+players.get(k)[2]+") ");
			System.out.println();
			//playersStates
			System.out.print("playersStates:");
			for(int k=0;k<playersStates.size();k++)
				System.out.print(playersStates.get(k)+"; ");
			System.out.println();
			// ownPosition
			System.out.println("ownPosition: "+ownPosition[0]+";"+ownPosition[1]);
			//timeBeforeShrink
			//nextShrinkPosition
			System.out.println("nextShrinkPosition: "+nextShrinkPosition[0]+";"+nextShrinkPosition[1]);
			//bombPosition
			System.out.println("bombPosition: "+bombPosition);
			//ownFirePower
			System.out.println("ownFirePower: "+ownFirePower);
			//ownBombCount
			System.out.println("ownBombCount: "+ownBombCount);
			//firePowers
			//bombCounts
		}
		
		// set the percepts
		((ArtificialIntelligence)getAi()).setPercepts(zoneMatrix, bombs, players, playersStates, 
			ownPosition, timeBeforeShrink, nextShrinkPosition, bombPosition, 
			ownFirePower, ownBombCount, firePowers, bombCounts);
	}

	@Override
	public void finishPercepts()
	{	bombCounts.clear();
		bombs.clear();
		controlKeys.clear();
		firePowers.clear();
		nextShrinkPosition = null;
		ownPosition = null;
		players.clear();
		playersStates.clear();
		zoneMatrix = null;
	}
	
    /////////////////////////////////////////////////////////////////
	// REACTION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** simulates control keys */
    private ArrayList<Integer> controlKeys;

	@Override
	public ArrayList<ControlEvent> convertReaction(Integer value)
	{	ArrayList<ControlEvent> result = new ArrayList<ControlEvent>();
		ControlEvent event;
		if(debug)
			System.out.print("action:");
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
}
