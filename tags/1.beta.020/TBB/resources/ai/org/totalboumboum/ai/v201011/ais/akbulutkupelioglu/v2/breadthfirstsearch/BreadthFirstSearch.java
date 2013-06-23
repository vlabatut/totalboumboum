package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.breadthfirstsearch;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.AkbulutKupelioglu;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.Coordinate;

public class BreadthFirstSearch
{
	
	private AkbulutKupelioglu monIa;
	
	public BreadthFirstSearch(AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
	}
	
	public ArrayList<Coordinate> search(AiTile tile) throws StopRequestException
	{
		monIa.checkInterruption();
		Queue<BreadthFirstSearchNode> queue = new LinkedList<BreadthFirstSearchNode>();
		ArrayList<BreadthFirstSearchNode> processed = new ArrayList<BreadthFirstSearchNode>();
		ArrayList<Coordinate> result = new ArrayList<Coordinate>(); 
		BreadthFirstSearchNode rootNode = new BreadthFirstSearchNode(tile, null, monIa);
		rootNode.setValue(0);
		queue.add(rootNode);
		while(!queue.isEmpty())
		{
			monIa.checkInterruption();
			BreadthFirstSearchNode currentNode = queue.poll();
			ArrayList<BreadthFirstSearchNode> children = currentNode.getChildren();
			for(BreadthFirstSearchNode childNode : children)
			{
				monIa.checkInterruption();
				childNode.setValue(childNode.getParent().getValue()+1);
				if(!queue.contains(childNode)&&!processed.contains(childNode))
					queue.add(childNode);
			}
			processed.add(currentNode);
			result.add(new Coordinate(currentNode.getCurrentTile(), currentNode.getValue(), monIa));
		}
		return result;
		
	}
	
}
