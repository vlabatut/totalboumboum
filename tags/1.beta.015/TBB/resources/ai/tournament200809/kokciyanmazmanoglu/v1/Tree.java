package tournament200809.kokciyanmazmanoglu.v1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;


import fr.free.totalboumboum.ai.adapter200809.AiAction;
import fr.free.totalboumboum.ai.adapter200809.AiActionName;
import fr.free.totalboumboum.ai.adapter200809.AiTile;
import fr.free.totalboumboum.ai.adapter200809.StopRequestException;
import fr.free.totalboumboum.engine.content.feature.Direction;


public class Tree {

	KokciyanMazmanoglu km;
	private Vector<Node> nodes;
	private Vector<Link> links;
	private Node currentNode;
	private Node finalNode;
	
	
	public Tree(int line, int col, KokciyanMazmanoglu ai){
		nodes = new Vector<Node>();
		links = new Vector<Link>();
		km = ai;
		setFinalNode(new Node(line,col,km.getFieldMatrix()[line][col],this,-1)); 
		currentNode = convertToNode(ai.getCurrentTile());
	}
	
	
	public Node getRoot() {
		return currentNode;
	}
	

	public boolean containsNode(Node node)
	{	boolean result=false;
		Iterator<Node> i = nodes.iterator();
		while(i.hasNext() && !result){
			Node n = i.next();
			result = ((node.getLine() == n.getLine()) && (node.getCol() == n.getCol()));
		}
		return result;		
	}

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
	
	public  void markVisitedNod(Node node){
		node.setVisited(true);
	}
	
	
	
	public  Iterator<Link> developNode(Node node) throws StopRequestException{
		km.checkInterruption();
		Vector<Link> vLink = new Vector<Link>();
		AiTile tNode = km.getPercepts().getTile(node.getLine(), node.getCol());
		ArrayList<AiTile> arrayTile= km.getClearNeighbors(tNode);
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
			if(!nd.isVisited()){
				Link lk = new Link(node, nd, new AiAction(AiActionName.MOVE,dr));
				if(!vLink.contains(lk))
					addLink(lk, vLink);
			}
		}

		
		
		markVisitedNod(node);
		return vLink.iterator();
	}
	
	public  Node getFinalNode() {
		return finalNode;
	}


	public  void setFinalNode(Node finalNode) {
		this.finalNode = finalNode;
	}
	
	public  boolean isFinalNode(Node node) throws StopRequestException{
		km.checkInterruption();
		Node fNode = getFinalNode();
		return (node.getLine() == fNode.getLine()) && (node.getCol() == fNode.getCol());
	}
	
	public  Node convertToNode(AiTile tile){
		Node nd = null;
		if(tile != null)
			nd = new Node(tile.getLine(),tile.getCol(),km.getFieldMatrix()[tile.getLine()][tile.getCol()],this,0);
		return nd; 
	}
	
	public  void addLink(Link link, Vector<Link> vLink) {
		links.add(link);
		nodes.add(link.getChild());
		vLink.add(link);
	}
	
}
