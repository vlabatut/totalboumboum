package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.breadthfirstsearch;

import java.util.ArrayList;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.AkbulutKupelioglu;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
public class BreadthFirstSearchNode
{

	public AiTile currentTile;
	private BreadthFirstSearchNode parent;
	private AkbulutKupelioglu monIa;
	private AiHero ownHero;
	int value;
	
	public BreadthFirstSearchNode(AiTile tile, BreadthFirstSearchNode parent, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		this.parent = parent;
		ownHero = monIa.getPercepts().getOwnHero();
		currentTile = tile;
	}

	public AiTile getCurrentTile() throws StopRequestException
	{
		monIa.checkInterruption();
		return currentTile;
	}

	public void setCurrentTile(AiTile currentTile) throws StopRequestException
	{
		monIa.checkInterruption();
		this.currentTile = currentTile;
	}

	public int getValue() throws StopRequestException
	{
		monIa.checkInterruption();
		return value;
	}

	public void setValue(int value) throws StopRequestException
	{
		monIa.checkInterruption();
		this.value = value;
	}
	
	public ArrayList<BreadthFirstSearchNode> getChildren() throws StopRequestException
	{
		monIa.checkInterruption();
		ArrayList<BreadthFirstSearchNode> result = new ArrayList<BreadthFirstSearchNode>();
		if(parent==null)
		{
			for(AiTile tile : currentTile.getNeighbors())
			{
				monIa.checkInterruption();
				if(tile.isCrossableBy(ownHero))
				result.add(new BreadthFirstSearchNode(tile, this, monIa));
			}
		}else
		{
			for(AiTile tile : currentTile.getNeighbors())
			{
				monIa.checkInterruption();
				if(!parent.getCurrentTile().equals(tile)&&tile.isCrossableBy(ownHero))
					result.add(new BreadthFirstSearchNode(tile, this, monIa));
			}
		}
		return result;
	}

	public BreadthFirstSearchNode getParent() throws StopRequestException
	{
		monIa.checkInterruption();
		return parent;
	}

	public void setParent(BreadthFirstSearchNode parent) throws StopRequestException
	{
		monIa.checkInterruption();
		this.parent = parent;
	}


	@Override
	public boolean equals(Object arg0)
	{

		if(arg0 instanceof BreadthFirstSearchNode)
			if(currentTile.equals(((BreadthFirstSearchNode)arg0).currentTile))
				return true;
		return false;
	}
	
	@Override
	public String toString()
	{
		return "Node(position:"+currentTile.toString()+", value:"+value+")";
	}
}
