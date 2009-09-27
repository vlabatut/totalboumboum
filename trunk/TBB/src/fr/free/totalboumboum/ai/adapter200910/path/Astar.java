package fr.free.totalboumboum.ai.adapter200910.path;

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
import java.util.PriorityQueue;
import java.util.Vector;

import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.HeuristicCalculator;

public class Astar
{	private static boolean debug = false;

	public Astar(CostCalculator costCalculator, HeuristicCalculator heuristicCalculator)
	{	this.costCalculator = costCalculator;
		this.heuristicCalculator = heuristicCalculator;
	}

    /////////////////////////////////////////////////////////////////
	// COST				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private CostCalculator costCalculator = null;
	
	
    /////////////////////////////////////////////////////////////////
	// HEURISTIC		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HeuristicCalculator heuristicCalculator = null;
	
    /////////////////////////////////////////////////////////////////
	// ROOT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** root of the seach tree */
	private AstarNode root = null;
		
    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public AiPath processShortestPath(AiTile startTile, AiTile endTile)
	{	// initialisation
		AiPath result = new AiPath();
		root = new AstarNode(startTile,costCalculator,heuristicCalculator);
		heuristicCalculator.setEndTile(endTile);
		PriorityQueue<AstarNode> queue = new PriorityQueue<AstarNode>(1);
		queue.offer(root);
		boolean done = false;
		AstarNode currentNode;

		// traitement
		do
		{	currentNode = queue.poll();
			currentNode.markVisited();
			if(debug)
				System.out.println("Visité : "+currentNode.toString());
			if(currentNode.getTile()==endTile)
				done = true;
			else
			{	Iterator<SearchLink> i = tree.developNode(currentNode);
				while(i.hasNext())
				{	SearchLink temp = i.next();
					prioFifo.offer(temp.getTarget());
				}
			}
		}
		while(!queue.isEmpty() && !done);
		
		// build solution path
		Vector<SearchLink> path = null;
		try
		{	path = tree.getPath(solution);
			System.out.println(tree.pathToString(path));
		}
		catch (AbsentNodeException e)
		{	e.printStackTrace();
		}
	}
}
