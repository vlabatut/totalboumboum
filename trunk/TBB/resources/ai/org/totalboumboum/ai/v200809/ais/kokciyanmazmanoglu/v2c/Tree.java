package org.totalboumboum.ai.v200809.ais.kokciyanmazmanoglu.v2c;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
*
* @author Nadin Kökciyan
* @author Hikmet Mazmanoğlu
*
*/
@SuppressWarnings("deprecation")
public class Tree {

	/** */
	KokciyanMazmanoglu km;
	/** */
	private Vector<Node> nodes;
	/** */
	private Vector<Link> links;
	/** */
	private Node currentNode;
	/** */
	private Node finalNode;
	
	/**
	 * 
	 * @param line
	 * @param col
	 * @param ai
	 * @throws StopRequestException
	 */
	public Tree(int line, int col, KokciyanMazmanoglu ai) throws StopRequestException{
		ai.checkInterruption();
		nodes = new Vector<Node>();
		links = new Vector<Link>();
		km = ai;
		setFinalNode(new Node(line,col,km.getFieldMatrix()[line][col],this,-1,km)); 
		currentNode = convertToNode(ai.getCurrentTile());
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public Node getRoot() throws StopRequestException {
		km.checkInterruption();
		return currentNode;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean containsNode(Node node) throws StopRequestException
	{	km.checkInterruption();
		boolean result=false;
		Iterator<Node> i = nodes.iterator();
		while(i.hasNext() && !result){
			km.checkInterruption();
			Node n = i.next();
			result = ((node.getLine() == n.getLine()) && (node.getCol() == n.getCol()));
		}
		return result;		
	}

	/**
	 * 
	 * @param line
	 * @param col
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public  Node getNodefromTree(int line, int col) throws StopRequestException
	{	
		km.checkInterruption();
		Node result = null;
		boolean control = false;
		Iterator<Node> i = nodes.iterator();
		while(i.hasNext() && !control){
			km.checkInterruption();
			Node n = i.next();
			control = ((line == n.getLine()) && (col == n.getCol()));
			result = n;
		}
		if(control == false){
			result = null;
		}
		return result;		
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public  Link getParentLink(Node node) throws StopRequestException
	{	
		km.checkInterruption();
		Link result=null;
		if(node != getRoot())
		{	Iterator<Link> it = links.iterator();
			while(it.hasNext() && result==null)
			{	km.checkInterruption();
				Link temp = it.next();
				if(temp.getChild().equals(node)){
					result = temp;
				}
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public  Vector<Link> getPath(Node node) throws StopRequestException
	{
		km.checkInterruption();
		Vector<Link> result = new Vector<Link>();;
		Node n = node;
		for(int i=0;i<node.getDepth();i++){
			km.checkInterruption();
			Link parentLink = getParentLink(n);
			//System.out.println("N "  + parentLink.getParent().getName() + " " + parentLink.getAction().getDirection().name());
			result.add(0,parentLink);
			n=parentLink.getParent();
		}
		
		
		
		
		return result;
	}
	
	/**
	 * 
	 * @param node
	 * @throws StopRequestException
	 */
	public  void markVisitedNod(Node node) throws StopRequestException{
		km.checkInterruption();
		node.setVisited(true);
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public  Iterator<Link> developNode(Node node) throws StopRequestException{
		km.checkInterruption();
		Vector<Link> vLink = new Vector<Link>();
		AiTile tNode = km.getPercepts().getTile(node.getLine(), node.getCol());
		List<AiTile> arrayTile= km.getClearNeighbors(tNode);
		if(arrayTile.size() != 1){
		if(arrayTile.contains(node.convertToTile()))arrayTile.remove(node.convertToTile());
		}
		Iterator<AiTile> iArray = arrayTile.iterator();
		//System.out.println(arrayTile.size());
		while(iArray.hasNext()){
			km.checkInterruption();
			AiTile tl = iArray.next();
			Direction dr =(km.getPercepts().getDirection(tNode, tl));
			
		
			//System.out.println(tl.getCol() + " " + tl.getLine() + " " +  dr.name());
			Node nd = getNodefromTree(tl.getLine(),tl.getCol());
			if(nd == null){
				nd = new Node(tl.getLine(),tl.getCol(),km.getFieldMatrix()[tl.getLine()][tl.getCol()],this,node.getDepth()+1,km);
			}
			if(!nd.isVisited()  && !km.isWall(tl)){
				Link lk = new Link(node, nd, new AiAction(AiActionName.MOVE,dr),km);
				if(!vLink.contains(lk))
					addLink(lk, vLink);
			}
		}

		
		
		markVisitedNod(node);
		return vLink.iterator();
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public  Node getFinalNode() throws StopRequestException {
		km.checkInterruption();
		return finalNode;
	}

	/**
	 * 
	 * @param finalNode
	 * @throws StopRequestException
	 */
	public  void setFinalNode(Node finalNode) throws StopRequestException {
		km.checkInterruption();
		this.finalNode = finalNode;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public  boolean isFinalNode(Node node) throws StopRequestException{
		km.checkInterruption();
		Node fNode = getFinalNode();
		return (node.getLine() == fNode.getLine()) && (node.getCol() == fNode.getCol());
	}
	
	/**
	 * 
	 * @param tile
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public  Node convertToNode(AiTile tile) throws StopRequestException{
		km.checkInterruption();
		Node nd = null;
		if(tile != null)
			nd = new Node(tile.getLine(),tile.getCol(),km.getFieldMatrix()[tile.getLine()][tile.getCol()],this,0,km);
		return nd; 
	}
	
	/**
	 * 
	 * @param link
	 * @param vLink
	 * @throws StopRequestException
	 */
	public  void addLink(Link link, Vector<Link> vLink) throws StopRequestException {
		km.checkInterruption();
		links.add(link);
		nodes.add(link.getChild());
		vLink.add(link);
	}
}
