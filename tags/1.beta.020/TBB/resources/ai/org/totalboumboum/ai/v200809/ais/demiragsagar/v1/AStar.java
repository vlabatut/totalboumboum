package org.totalboumboum.ai.v200809.ais.demiragsagar.v1;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Vector;

import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Dogus Burcu Demirag
 * @author Zeynep Sagar
 *
 */
public class AStar {

	AiTile firstTile;
	AiTile lastTile;
	Vector<LienRecherche> links;
	LinkedList<Node> path;
	boolean debug;
	ArtificialIntelligence ai;
	
	public AStar(AiTile start, AiTile end, ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.firstTile = start;
		this.lastTile = end;
		this.debug = false;
	}

	public AStar(AiTile start, AiTile end, ArtificialIntelligence ai, boolean debug) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.firstTile = start;
		this.lastTile = end;
		this.debug = debug;
	}

	public void init() throws StopRequestException {
		ai.checkInterruption();
		links = new Vector<LienRecherche>();
		// daha sonra new constractor kurulabilir
	}

	public void formeLien(Node parent, Node fils) throws StopRequestException {
		ai.checkInterruption();
		LienRecherche link = new LienRecherche(parent, fils);
		links.add(link);
	}

	public Iterator<Node> developNode(AiTile courant) throws StopRequestException {
		ai.checkInterruption();
		Vector<Node> result = new Vector<Node>();
		Node temp;
		Node courantN = new Node(courant);
		try {
			if (isClear(courant.getNeighbor(Direction.UP))) {
				temp = new Node(courant.getNeighbor(Direction.UP), lastTile);
				result.add(temp);
				formeLien(courantN, temp);
			}

			if (isClear(courant.getNeighbor(Direction.DOWN))) {
				temp = new Node(courant.getNeighbor(Direction.DOWN), lastTile);
				result.add(temp);
				formeLien(courantN, temp);
			}
			if (isClear(courant.getNeighbor(Direction.LEFT))) {
				temp = new Node(courant.getNeighbor(Direction.LEFT), lastTile);
				result.add(temp);
				formeLien(courantN, temp);
			}

			if (isClear(courant.getNeighbor(Direction.RIGHT))) {
				temp = new Node(courant.getNeighbor(Direction.RIGHT), lastTile);
				result.add(temp);
				formeLien(courantN, temp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result.iterator();
	}

	public void formeArbre() throws StopRequestException {
		ai.checkInterruption();
		try {
			init();
			if (debug)
				System.out.println("Agac yaratildi");

			Node solution = null;
			NoeudComparator monComp = new NoeudComparator();
			// on a cree une file de priorité
			PriorityQueue<Node> fifo = new PriorityQueue<Node>(1, monComp);
			fifo.offer(new Node(firstTile));
			int iteration = 1;
			while (!fifo.isEmpty() && solution == null) {
				ai.checkInterruption();
				Node node = fifo.poll();
				if (debug) System.out.println("fifodan cikan node: " + "("	+ node.tile.getCol() + "," + node.tile.getLine() + ")");
				//System.out.println("Visité "+iteration+": "+node.toString());
				iteration++;
				if (node.memeCoordonnees(new Node(lastTile))) {
					solution = node;
					if (debug) System.out.println("Solution trouve : (" + solution.tile.getCol() + "," + solution.tile.getLine() + ")");
				} else {
					Iterator<Node> i = developNode(node.tile);
					while (i.hasNext()) {
						ai.checkInterruption();
						Node temp = i.next();
						if (debug) System.out.println("developNode: " + temp.tile.getCol() + "," + temp.tile.getLine());
						fifo.offer(temp);
					}
				}
			}
			this.path=null;
			getPath();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void getPath() throws StopRequestException { // la liste des NoeudRecherches qui
		ai.checkInterruption();
		LienRecherche linkParent = getParentLink(new Node(lastTile));
		if(linkParent==null) {
			System.out.println("HOP! son node bulunamadi");
			return;
		}
		this.path = new LinkedList<Node>();
		this.path.push(linkParent.getTarget());
		Node temp;
		while (!linkParent.getOrigin().memeCoordonnees(new Node(firstTile))) {
			ai.checkInterruption();
			temp = linkParent.getOrigin();
			linkParent = getParentLink(temp);
			this.path.push(linkParent.getTarget());
		}
	}

	private boolean isClear(AiTile tile) throws StopRequestException{
		ai.checkInterruption();
		boolean result;
		AiBlock block = tile.getBlock();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiFire> fires = tile.getFires();
		result = block == null && bombs.size() == 0 && fires.size() == 0;
		return result;
	}

	public LienRecherche getParentLink(Node node) throws StopRequestException {
		ai.checkInterruption();
		LienRecherche result = null;
		if (!node.memeCoordonnees(new Node(firstTile))) {
			Iterator<LienRecherche> i = links.iterator();
			while (i.hasNext() && result == null) {
				ai.checkInterruption();
				LienRecherche temp = i.next();
				if (temp != null)
					if (node.memeCoordonnees(temp.getTarget()))
						result = temp;
			}
		}
		return result;
	}

	public Vector<LienRecherche> getChildrenLinks(Node node) throws StopRequestException {
		ai.checkInterruption();
		Vector<LienRecherche> result = new Vector<LienRecherche>();

		Iterator<LienRecherche> i = links.iterator();
		while (i.hasNext()) {
			ai.checkInterruption();
			LienRecherche temp = i.next();
			if (temp.getOrigin() == node)
				result.add(temp);
		}
		return result;
	}

}
