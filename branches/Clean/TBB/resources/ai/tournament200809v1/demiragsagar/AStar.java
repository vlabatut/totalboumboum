package tournament200809v1.demiragsagar;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Vector;

import fr.free.totalboumboum.ai.adapter200809.AiBlock;
import fr.free.totalboumboum.ai.adapter200809.AiBomb;
import fr.free.totalboumboum.ai.adapter200809.AiFire;
import fr.free.totalboumboum.ai.adapter200809.AiTile;
import fr.free.totalboumboum.engine.content.feature.Direction;

public class AStar {

	AiTile firstTile;
	AiTile lastTile;
	Vector<LienRecherche> links;
	LinkedList<Node> path;
	boolean debug;

	public AStar(AiTile start, AiTile end) {
		this.firstTile = start;
		this.lastTile = end;
		this.debug = false;
	}

	public AStar(AiTile start, AiTile end, boolean debug) {
		this.firstTile = start;
		this.lastTile = end;
		this.debug = debug;
	}

	public void init() {
		links = new Vector<LienRecherche>();
		// 
	}

	public void formeLien(Node parent, Node fils) {
		LienRecherche link = new LienRecherche(parent, fils);
		links.add(link);
	}

	public Iterator<Node> developNode(AiTile courant) {
		Vector<Node> result = new Vector<Node>();
		Node temp;
		Node courantN = new Node(courant);
		try {
			if (isClear(courant.getNeighbour(Direction.UP))) {
				temp = new Node(courant.getNeighbour(Direction.UP), lastTile);
				result.add(temp);
				formeLien(courantN, temp);
			}

			if (isClear(courant.getNeighbour(Direction.DOWN))) {
				temp = new Node(courant.getNeighbour(Direction.DOWN), lastTile);
				result.add(temp);
				formeLien(courantN, temp);
			}
			if (isClear(courant.getNeighbour(Direction.LEFT))) {
				temp = new Node(courant.getNeighbour(Direction.LEFT), lastTile);
				result.add(temp);
				formeLien(courantN, temp);
			}

			if (isClear(courant.getNeighbour(Direction.RIGHT))) {
				temp = new Node(courant.getNeighbour(Direction.RIGHT), lastTile);
				result.add(temp);
				formeLien(courantN, temp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result.iterator();
	}

	public void formeArbre() {
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

	public void getPath() { // la liste des NoeudRecherches qui
		LienRecherche linkParent = getParentLink(new Node(lastTile));
		if(linkParent==null) {
			System.out.println("HOP! son node bulunamadi");
			return;
		}
		this.path = new LinkedList<Node>();
		this.path.push(linkParent.getTarget());
		Node temp;
		while (!linkParent.getOrigin().memeCoordonnees(new Node(firstTile))) {
			temp = linkParent.getOrigin();
			linkParent = getParentLink(temp);
			this.path.push(linkParent.getTarget());
		}
	}

	private boolean isClear(AiTile tile){
		boolean result;
		AiBlock block = tile.getBlock();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiFire> fires = tile.getFires();
		result = block == null && bombs.size() == 0 && fires.size() == 0;
		return result;
	}

	public LienRecherche getParentLink(Node node) {
		LienRecherche result = null;
		if (!node.memeCoordonnees(new Node(firstTile))) {
			Iterator<LienRecherche> i = links.iterator();
			while (i.hasNext() && result == null) {
				LienRecherche temp = i.next();
				if (temp != null)
					if (node.memeCoordonnees(temp.getTarget()))
						result = temp;
			}
		}
		return result;
	}

	public Vector<LienRecherche> getChildrenLinks(Node node) {
		Vector<LienRecherche> result = new Vector<LienRecherche>();

		Iterator<LienRecherche> i = links.iterator();
		while (i.hasNext()) {
			LienRecherche temp = i.next();
			if (temp.getOrigin() == node)
				result.add(temp);
		}
		return result;
	}

}
