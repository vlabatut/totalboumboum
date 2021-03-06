package org.totalboumboum.ai.v200809.ais.kokciyanmazmanoglu.v2;

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
	 * 		Description manquante !
	 * @param col
	 * 		Description manquante !
	 * @param ai
	 * 		Description manquante !
	 */
	public Tree(int line, int col, KokciyanMazmanoglu ai){
		nodes = new Vector<Node>();
		links = new Vector<Link>();
		km = ai;
		setFinalNode(new Node(line,col,km.getFieldMatrix()[line][col],this,-1)); 
		currentNode = convertToNode(ai.getCurrentTile());
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 */
	public Node getRoot() {
		return currentNode;
	}
	

	/**
	 * 
	 * @param node
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public boolean containsNode(Node node)
	{	boolean result=false;
		Iterator<Node> i = nodes.iterator();
		while(i.hasNext() && !result){
			Node n = i.next();
			result = ((node.getLine() == n.getLine()) && (node.getCol() == n.getCol()));
		}
		return result;		
	}

	/**
	 * 
	 * @param line
	 * 		Description manquante !
	 * @param col
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public  Node getNodefromTree(int line, int col) throws StopRequestException
	{	
		km.checkInterruption();
		Node result = null;
		boolean control = false;
		Iterator<Node> i = nodes.iterator();
		while(i.hasNext() && !control){
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
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public  Link getParentLink(Node node) throws StopRequestException
	{	
		km.checkInterruption();
		Link result=null;
		if(node != getRoot())
		{	Iterator<Link> it = links.iterator();
			while(it.hasNext() && result==null)
			{	Link temp = it.next();
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
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
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
	 * 		Description manquante !
	 */
	public  void markVisitedNod(Node node){
		node.setVisited(true);
	}
	
	
	/**
	 * 
	 * @param node
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
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
				nd = new Node(tl.getLine(),tl.getCol(),km.getFieldMatrix()[tl.getLine()][tl.getCol()],this,node.getDepth()+1);
			}
			if(!nd.isVisited()  && !km.isWall(tl)){
				Link lk = new Link(node, nd, new AiAction(AiActionName.MOVE,dr));
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
	 */
	public  Node getFinalNode() {
		return finalNode;
	}

	/**
	 * 
	 * @param finalNode 
	 * 		Description manquante !
	 */
	public  void setFinalNode(Node finalNode) {
		this.finalNode = finalNode;
	}
	
	/**
	 * 
	 * @param node
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public  boolean isFinalNode(Node node) throws StopRequestException{
		km.checkInterruption();
		Node fNode = getFinalNode();
		return (node.getLine() == fNode.getLine()) && (node.getCol() == fNode.getCol());
	}
	
	/**
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public  Node convertToNode(AiTile tile){
		Node nd = null;
		if(tile != null)
			nd = new Node(tile.getLine(),tile.getCol(),km.getFieldMatrix()[tile.getLine()][tile.getCol()],this,0);
		return nd; 
	}

	/**
	 * 
	 * @param link
	 * 		Description manquante !
	 * @param vLink
	 * 		Description manquante !
	 */
	public  void addLink(Link link, Vector<Link> vLink) {
		links.add(link);
		nodes.add(link.getChild());
		vLink.add(link);
	}
	
}
