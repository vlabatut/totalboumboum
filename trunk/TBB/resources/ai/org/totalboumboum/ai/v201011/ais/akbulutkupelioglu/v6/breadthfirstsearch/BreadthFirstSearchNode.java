package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.breadthfirstsearch;

import java.util.ArrayList;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.AkbulutKupelioglu;

/**
 * Represents a node in a breadth-first search.
 * 
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 *
 */
@SuppressWarnings("deprecation")
public class BreadthFirstSearchNode
{

	/** The tile to which this node corresponds. */
	public AiTile currentTile;
	/** */
	private BreadthFirstSearchNode parent;
	/** */
	private AkbulutKupelioglu monIa;
	/** */
	private AiHero ownHero;
	/** */
	int value;
	
	/**
	 * Creates a new node.
	 * 
	 * @param tile The tile to which this node corresponds. 
	 * @param parent The parent node.
	 * @param ia AkbulutKupelioglu using this object.
	 * @throws StopRequestException
	 */
	public BreadthFirstSearchNode(AiTile tile, BreadthFirstSearchNode parent, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		this.parent = parent;
		ownHero = monIa.getPercepts().getOwnHero();
		currentTile = tile;
	}

	/**
	 * Gets the tile to which this node corresponds.
	 * @return The tile to which this node corresponds.
	 * @throws StopRequestException
	 */
	public AiTile getCurrentTile() throws StopRequestException
	{
		monIa.checkInterruption();
		return currentTile;
	}

	/**Sets the tile to which this node corresponds.
	 * @param currentTile The tile to be set.
	 * @throws StopRequestException
	 */
	public void setCurrentTile(AiTile currentTile) throws StopRequestException
	{
		monIa.checkInterruption();
		this.currentTile = currentTile;
	}

	/**
	 * Gets the value associated with this node.
	 * @return The associated value.
	 * @throws StopRequestException
	 */
	public int getValue() throws StopRequestException
	{
		monIa.checkInterruption();
		return value;
	}

	/**
	 * Sets the value associated to this node.
	 * @param value The value to be associated.
	 * @throws StopRequestException
	 */
	public void setValue(int value) throws StopRequestException
	{
		monIa.checkInterruption();
		this.value = value;
	}
	
	/**
	 * Gets the children of this node. The children are determined by the ability of a hero crossing
	 * the tile to which this node corresponds. The returned list may be empty.
	 * @return A list containing the children of this node.
	 * @throws StopRequestException
	 */
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

	/**
	 * Gets the parent node of this node.
	 * @return The parent node.
	 * @throws StopRequestException
	 */
	public BreadthFirstSearchNode getParent() throws StopRequestException
	{
		monIa.checkInterruption();
		return parent;
	}

	/**
	 * Sets the parent node of this node.
	 * @param parent The parent node.
	 * @throws StopRequestException
	 */
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
