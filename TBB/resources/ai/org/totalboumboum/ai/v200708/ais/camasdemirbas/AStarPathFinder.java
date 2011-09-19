package org.totalboumboum.ai.v200708.ais.camasdemirbas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.totalboumboum.ai.v200708.ais.camasdemirbas.ManhattanHeuristic;


/**
 * Une impl�mentation de viseur de chemin 
 * qui utilise l'AStar l'algorithme bas� heuristique déterminer un chemin. 
 * 
 * @author Gokhan Camas
 * @author Irem Demirbas
 *
 */
@SuppressWarnings("unchecked")
public class AStarPathFinder implements PathFinder {
	@SuppressWarnings("rawtypes")
	private List closed = new ArrayList();
	private SortedList open = new SortedList();
	
	/** La carte est cherch�e */
	private GameMap map;
	/** La profondeur maximum de recherche que nous voulons accepter avant de renoncer */
	private int maxSearchDistance;
	
	/** La s�rie compl�te de noeuds à travers la carte */
	private Node[][] nodes;
	protected static boolean findPathWithSoftWall;
	private boolean allowDiagMovement;
	/** L'heuristique nous appliquons nous déterminer quels noeuds pour chercher premièrement */
	private AStarHeuristic heuristic;
	
	/**
	 * r�er un viseur de chemin avec l'implicite heuristique - le plus proche pour cibler.
	 * 
	 * @param map La carte être cherch�e
	 * @param maxSearchDistance La profondeur maximum que nous chercherons avant de renoncer
	 * @param allowDiagMovement True si la recherche doit essayer le mouvement de diaganol
	 */
	public AStarPathFinder(GameMap map, int maxSearchDistance, boolean allowDiagMovement,boolean findPathWithSoftWall ) {
		this(map, maxSearchDistance, allowDiagMovement,findPathWithSoftWall, new ManhattanHeuristic(1));
	}

	/**
	 * créer un viseur de chemin 
	 * 
	 * @param heuristic L'heuristique a utilisé pour déterminer l'ordre de recherche de la carte
	 * @param map La carte être cherch�e
	 * @param maxSearchDistance La profondeur maximum que nous chercherons avant de renoncer
	 * @param allowDiagMovement Vrai si la recherche doit essayer le mouvement de diaganol
	 */
	public AStarPathFinder(GameMap map, int maxSearchDistance, 
						   boolean allowDiagMovement, boolean findPathWithSoftWall2, AStarHeuristic heuristic) {
		this.heuristic = heuristic;
		this.map = map;
		this.maxSearchDistance = maxSearchDistance;
		this.allowDiagMovement = allowDiagMovement;
		findPathWithSoftWall = findPathWithSoftWall2;
		
		nodes = new Node[map.getWidthInTiles()][map.getHeightInTiles()];
		for (int x=0;x<map.getWidthInTiles();x++) {
			for (int y=0;y<map.getHeightInTiles();y++) {
				nodes[x][y] = new Node(x,y);
			}
		}
	}
	
	public Path findPath(int sx, int sy, int tx, int ty) {		
		// initial state for A*. The closed group is empty. Only the starting
		// tile is in the open list and it's cost is zero, i.e. we're already there
		nodes[sx][sy].cost = 0;
		nodes[sx][sy].depth = 0;
		closed.clear();
		open.clear();
		open.add(nodes[sx][sy]);
		
		nodes[tx][ty].parent = null;
		
		// while we haven't found the goal and haven't exceeded our max search depth
		int maxDepth = 0;
		while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
			// pull out the first node in our open list, this is determined to 
			// be the most likely to be the next step based on our heuristic
			Node current = getFirstInOpen();
			if (current == nodes[tx][ty]) {
				break;
			}
			
			removeFromOpen(current);
			addToClosed(current);
			
			// search through all the neighbors of the current node evaluating
			// them as next steps
			for (int x=-1;x<2;x++) {
				for (int y=-1;y<2;y++) {
					// not a neighbor, its the current tile
					if ((x == 0) && (y == 0)) {
						continue;
					}
					
					// if we're not allowing diaganol movement then only 
					// one of x or y can be set
					if (!allowDiagMovement) {
						if ((x != 0) && (y != 0)) {
							continue;
						}
					}
					
					// determine the location of the neighbor and evaluate it
					int xp = x + current.x;
					int yp = y + current.y;
					
					if (isValidLocation(sx,sy,xp,yp)) {
						// the cost to get to this node is cost the current plus the movement
						// cost to reach this node. Note that the heursitic value is only used
						// in the sorted open list
						float nextStepCost = current.cost + getMovementCost(current.x, current.y, xp, yp);
						Node neighbor = nodes[xp][yp];
						map.pathFinderVisited(xp, yp);
						
						// if the new cost we've determined for this node is lower than 
						// it has been previously makes sure the node hasn't been discarded. We've
						// determined that there might have been a better path to get to
						// this node so it needs to be re-evaluated
						if (nextStepCost < neighbor.cost) {
							if (inOpenList(neighbor)) {
								removeFromOpen(neighbor);
							}
							if (inClosedList(neighbor)) {
								removeFromClosed(neighbor);
							}
						}
						
						// if the node hasn't already been processed and discarded then
						// reset it's cost to our current cost and add it as a next possible
						// step (i.e. to the open list)
						if (!inOpenList(neighbor) && !(inClosedList(neighbor))) {
							neighbor.cost = nextStepCost;
							neighbor.heuristic = getHeuristicCost(xp, yp, tx, ty);
							maxDepth = Math.max(maxDepth, neighbor.setParent(current));
							addToOpen(neighbor);
						}
					}
				}
			}
		}

		// since we've got an empty open list or we've run out of search 
		// there was no path. Just return null
		if (nodes[tx][ty].parent == null) {
			return null;
		}
		
		// At this point we've definitely found a path so we can uses the parent
		// references of the nodes to find out way from the target location back
		// to the start recording the nodes on the way.
		Path path = new Path();
		Node target = nodes[tx][ty];
		while (target != nodes[sx][sy]) {
			path.prependStep(target.x, target.y);
			target = target.parent;
		}
		path.prependStep(sx,sy);
		
		// thats it, we have our path 
		return path;
	}

	/**
	 * Obtenir le premier �l�ment de la liste ouverte. 
	 * Ceci est le suivant être cherch�. 
	 * 
	 * @return e premier �l�ment dans la liste ouverte
	 */
	protected Node getFirstInOpen() {
		return (Node) open.first();
	}
	
	/**
	 * Ajouter un noeud à la liste ouverte
	 * 
	 * @param node Le noeud être ajout� à la liste ouverte
	 */
	protected void addToOpen(Node node) {
		open.add(node);
	}
	
	/**
	 * Le contr�le si un noeud est dans la liste ouverte
	 * 
	 * @param node 
	 * @return True si le noeud donn� est dans la liste ouverte
	 */
	protected boolean inOpenList(Node node) {
		return open.contains(node);
	}
	
	/**
	 * Enlever un noeud de la liste ouverte
	 * 
	 * @param node Le noeud pour enlever de la liste ouverte
	 */
	protected void removeFromOpen(Node node) {
		open.remove(node);
	}
	
	/**
	 * Ajouter un noeud à la liste ferm�e
	 * 
	 * @param node Le noeud pour ajouter à la liste ferm�e
	 */
	protected void addToClosed(Node node) {
		closed.add(node);
	}
	
	/**
	 * Le contr�le si le noeud fourni est dans la liste ferm�e
	 * 
	 * @param node 
	 * @return True si le noeud spécifié est dans la liste ferm�e
	 */
	protected boolean inClosedList(Node node) {
		return closed.contains(node);
	}
	
	/**
	 * Enlever un noeud de la liste ferm�e
	 * 
	 * @param node Le noeud pour enlever de la liste ferm�e
	 */
	protected void removeFromClosed(Node node) {
		closed.remove(node);
	}
	
	protected boolean isValidLocation(int sx, int sy, int x, int y) {
		boolean invalid = (x < 0) || (y < 0) || (x >= map.getWidthInTiles()) || (y >= map.getHeightInTiles());
		
		if ((!invalid) && ((sx != x) || (sy != y))) {
			invalid = map.blocked(x, y);
		}
		
		return !invalid;
	}
	
	/**
	 * Obtenir le co�t pour se d�placer par un emplacement donn�
	 * 
	 */
	public float getMovementCost(int sx, int sy, int tx, int ty) {
		return map.getCost(sx, sy, tx, ty);
	}

	/**
	 * Obtenir le co�t heuristique pour l'emplacement donn�. 
	 * Ceci détermine dans lequel commande les emplacements sont trait�s. 
	 * 
	 */
	public float getHeuristicCost(int x, int y, int tx, int ty) {
		return heuristic.getCost(map,x, y, tx, ty);
	}
	
	/**
	 * Une liste tri�e simple
	 *
	 * @author Gokhan Camas -- Irem Demirbas
	 */
	private class SortedList {
		/** La liste d'�l�ments */
		@SuppressWarnings("rawtypes")
		private List list = new ArrayList();
		
		/**
		 * Rapporter le premier �l�ment de la liste
		 *  
		 * @return Le premier �l�ment de la liste
		 */
		public Object first() {
			return list.get(0);
		}
		
		/**
		 * Vider la liste
		 */
		public void clear() {
			list.clear();
		}
		
		/**
		 * Ajouter un �l�ment à la liste - les causes triant
		 * 
		 * @param o L'�l�ment pour ajouter
		 */
		public void add(Object o) {
			list.add(o);
			Collections.sort(list);
		}
		
		/**
		 * Enlever un �l�ment de la liste
		 * 
		 * @param o L'�l�ment pour enlever
		 */
		public void remove(Object o) {
			list.remove(o);
		}
	
		/**
		 * Obtenir le nombre d'�l�ments dans la liste
		 * 
		 * @return Le nombre d'�l�ment dans la liste
 		 */
		public int size() {
			return list.size();
		}
		
		/**
		 * Le contr�le si un �l�ment est dans la liste
		 * 
		 * @param o L'�l�ment pour chercher
		 * @return True si l'�l�ment est dans la liste
		 */
		public boolean contains(Object o) {
			return list.contains(o);
		}
	}
	
	/**
	 * Un noeud seul dans le graphique de recherche
	 */
	
	@SuppressWarnings("rawtypes")
	private class Node implements Comparable {
		private int x;
		private int y;
		private float cost;
		private Node parent;
		private float heuristic;
		private int depth;
		
		/**
		 * créer un nouveau noeud
		 * 
		 */
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int setParent(Node parent) {
			depth = parent.depth + 1;
			this.parent = parent;
			
			return depth;
		}
		
		public int compareTo(Object other) {
			Node o = (Node) other;
			
			float f = heuristic + cost;
			float of = o.heuristic + o.cost;
			
			if (f < of) {
				return -1;
			} else if (f > of) {
				return 1;
			} else {
				return 0;
			}
		}
	}
}
