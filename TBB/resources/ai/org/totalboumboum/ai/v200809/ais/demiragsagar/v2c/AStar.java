package org.totalboumboum.ai.v200809.ais.demiragsagar.v2c;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Doğus Burcu Demirağ
 * @author Zeynep Şagar
 *
 */
@SuppressWarnings("deprecation")
public class AStar {

	AiTile firstTile;
	AiTile lastTile;
	Vector<LienRecherche> links;
	boolean debug;
	ArtificialIntelligence ai;
	
	public AStar(AiTile start, AiTile end, ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.firstTile = start;
		this.lastTile = end;
		this.debug = false;
	}

	public AStar(AiTile start, AiTile end, boolean debug) throws StopRequestException {
		ai.checkInterruption();
		this.firstTile = start;
		this.lastTile = end;
		this.debug = debug;
	}

	public void init() throws StopRequestException {
		ai.checkInterruption();
		links = new Vector<LienRecherche>();
	}

	public void formeLien(Node parent, Node fils) throws StopRequestException {
		ai.checkInterruption();
		LienRecherche link = new LienRecherche(parent, fils,ai);
		links.add(link);
	}

	public Iterator<Node> developNode(AiTile courant) throws StopRequestException {
		ai.checkInterruption();
		Vector<Node> result = new Vector<Node>();
		Node temp;
		Node courantN = new Node(courant);
		//On prend un case et on prend ces voisins qui se trouvent en HAUT,en BAS,a DROITE,a GAUCHE .
		//Ces voisins qui ne contiennent pas de mur ,pas de feu ou pas de bombe vont etre ces fils 
			if (Functions.isClear(courant.getNeighbor(Direction.UP),ai)) {
				temp = new Node(courant.getNeighbor(Direction.UP), lastTile,ai);
				if(this.getParentLink(temp)==null && !temp.memeCoordonnees(new Node(firstTile)))
				{
					result.add(temp);
					formeLien(courantN, temp);
				}
			}
			if (Functions.isClear(courant.getNeighbor(Direction.DOWN),ai)) {
				temp = new Node(courant.getNeighbor(Direction.DOWN), lastTile,ai);
				if(this.getParentLink(temp)==null && !temp.memeCoordonnees(new Node(firstTile)))
				{
					result.add(temp);
					formeLien(courantN, temp);
				}
			}
			if (Functions.isClear(courant.getNeighbor(Direction.LEFT),ai)) {
				temp = new Node(courant.getNeighbor(Direction.LEFT), lastTile,ai);
				if(this.getParentLink(temp)==null && !temp.memeCoordonnees(new Node(firstTile)))
				{
					result.add(temp);
					formeLien(courantN, temp);
				}
			}
			if (Functions.isClear(courant.getNeighbor(Direction.RIGHT),ai)) {
				temp = new Node(courant.getNeighbor(Direction.RIGHT), lastTile,ai);
				if(this.getParentLink(temp)==null && !temp.memeCoordonnees(new Node(firstTile)))
				{
					result.add(temp);
					formeLien(courantN, temp);
				}
			}
		return result.iterator();
	}
	public void formeArbre() throws StopRequestException {
		ai.checkInterruption();
			init();
			if (debug)
				System.out.println("Agac yaratildi");
			Node first;
			Node solution = null;
			NoeudComparator monComp = new NoeudComparator();
			// on a cree une file de priorite
			PriorityQueue<Node> fifo = new PriorityQueue<Node>(1, monComp);
			first=new Node(firstTile);
			fifo.offer(first);
			int iteration = 1;
			while (!fifo.isEmpty() && solution == null) {
				ai.checkInterruption();
				Node node = fifo.poll();
				
				if (debug) System.out.println("Node qui sort du fifo: " + "("	+ node.tile.getCol() + "," + node.tile.getLine() + ")");
				iteration++;
				if (node.memeCoordonnees(new Node(lastTile))) {
					solution = node;
					if (debug) System.out.println("Solution trouve : (" + solution.tile.getCol() + "," + solution.tile.getLine() + ")");
				} else {
					Iterator<Node> i = developNode(node.tile);
					Node parent = null;
					if(!node.memeCoordonnees(first))
					{
						//Si le noeud n'est pas la racine alors on trouve son parent
						parent=this.getParentLink(node).getOrigin();
					}
					while (i.hasNext()) {
						ai.checkInterruption();
						Node temp = i.next();
						if (debug) System.out.println("developNode: " + temp.tile.getCol() + "," + temp.tile.getLine());
						if(!node.memeCoordonnees(first))
						{
							boolean f;
							f=temp.memeCoordonnees(parent);
							//Quand on developpe un noeud ,on ne doit pas prendre son parent comme son fils.
							if(!f)
								fifo.offer(temp);
						}
						else
							fifo.offer(temp);
						
					}
				}
			}

	}
	/*
	 * On trouve le chemin de l'arbre qui est forme par l'alogrithme A*
	 */
	public LinkedList<Node> getPath() throws StopRequestException { 
		ai.checkInterruption();
		LinkedList<Node> path;
		LienRecherche linkParent = getParentLink(new Node(lastTile));
		if(linkParent==null) {
			return null;
		}
		path = new LinkedList<Node>();
		path.push(linkParent.getTarget());
		Node temp;
		while (!linkParent.getOrigin().memeCoordonnees(new Node(firstTile))) {
			ai.checkInterruption();
			temp = linkParent.getOrigin();
			linkParent = getParentLink(temp);
			path.push(linkParent.getTarget());
		}
		return path;
		
	}
	/*
	 * On trouve le parent d'un noeud 
	 */
	public LienRecherche getParentLink(Node child) throws StopRequestException {
		ai.checkInterruption();
		LienRecherche result = null;
		if (!child.memeCoordonnees(new Node(firstTile))) {
			Iterator<LienRecherche> i = links.iterator();
			while (i.hasNext() && result == null) {
				ai.checkInterruption();
				LienRecherche temp = i.next();
				if (temp != null)
					if (child.memeCoordonnees(temp.getTarget()))
						result = temp;
			}
		}
		return result;
	}
	/*
	 * On trouve les fils d'un noeud
	 */
	public List<Node> getChildrenLinks(Node node) throws StopRequestException {
		ai.checkInterruption();
		List<Node> result = new ArrayList<Node>();

		Iterator<LienRecherche> i = links.iterator();
		while (i.hasNext()) {
			ai.checkInterruption();
			LienRecherche temp = i.next();
			if (temp.getOrigin().memeCoordonnees(node))
				result.add(temp.getTarget());
		}
		return result;
	}
	//Il trouve les cases qui n'ont pas de fils
	//ou on peut poser des bombes
	public List<Node> getFils() throws StopRequestException
	{	ai.checkInterruption();
	List<Node> nodes=new ArrayList<Node>();
		Iterator <LienRecherche> it=this.links.iterator();
		while(it.hasNext())
		{	ai.checkInterruption();
			LienRecherche l=it.next();
			if(this.getChildrenLinks(l.getTarget()).size()==0) {
				if(Functions.ChildNodes(l.getTarget().getTile(),ai)==1)
					nodes.add(l.getTarget());
			}
		}
		return nodes;
	}

}