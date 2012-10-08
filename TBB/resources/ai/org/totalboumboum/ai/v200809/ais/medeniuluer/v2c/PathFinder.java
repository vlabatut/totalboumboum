package org.totalboumboum.ai.v200809.ais.medeniuluer.v2c;

import java.util.LinkedList;
import java.util.PriorityQueue;

import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
*
* @author Ekin Medeni
* @author Pınar Uluer
*
*/
@SuppressWarnings("deprecation")
public class PathFinder {
	
	/** */
	private LinkedList<SearchNode> path;
	/** */
	private ZoneEnum tab[][];
	/** */
	private AiZone zone;
	/** */
	private MedeniUluer mu;
	/** */
	private SearchEnum mode;
	
	/**
	 * 
	 * @param zone
	 * @param target
	 * @param mu
	 * @param mode
	 * @throws StopRequestException
	 */
	public PathFinder(AiZone zone, AiTile target, MedeniUluer mu,SearchEnum mode) throws StopRequestException 
	{
		mu.checkInterruption(); // Appel Obligatoire
		this.mu = mu;
		this.zone = zone;
		this.mode = mode;
		Zone AiZoneToZoneConverter = new Zone(zone, mu);
		tab = AiZoneToZoneConverter.getZoneArray();
		if (zone.getOwnHero().getTile().equals(target))
			path = new LinkedList<SearchNode>();
		else {
			if (tab[target.getCol()][target.getLine()] != ZoneEnum.BLOCDESTRUCTIBLE && tab[target.getCol()][target.getLine()] != ZoneEnum.BLOCINDESTRUCTIBLE) 
			{
				if (!zone.getNeighborTiles(zone.getOwnHero().getTile()).contains(target)) 
				{ 
					findPath(tab, zone.getOwnHero().getCol(), zone.getOwnHero().getLine(), new SearchNode(target.getCol(), target.getLine(), tab[target.getCol()][target.getLine()],mu));
				} 
				else 
				{
					path = new LinkedList<SearchNode>();
					path.offer(new SearchNode(target.getCol(), target.getLine(),tab[target.getCol()][target.getLine()], mu));
				}
			} 
			else 
			{
				path = new LinkedList<SearchNode>();
			}
		}
	}

	/**
	 * 
	 * @param zone
	 * @param depart
	 * @param target
	 * @param mu
	 * @param mode
	 * @throws StopRequestException
	 */
	public PathFinder(AiZone zone, AiTile depart, AiTile target,MedeniUluer mu, SearchEnum mode)throws StopRequestException 
	{
		mu.checkInterruption(); // Appel Obligatoire
		this.mu = mu;
		this.zone = zone;
		this.mode = mode;
		Zone AiZoneToZoneConverter = new Zone(zone, mu);
		tab = AiZoneToZoneConverter.getZoneArray();
		if (depart.equals(target))
			path = new LinkedList<SearchNode>();
		else 
		{
			if (tab[target.getCol()][target.getLine()] != ZoneEnum.BLOCDESTRUCTIBLE && tab[target.getCol()][target.getLine()] != ZoneEnum.BLOCINDESTRUCTIBLE) 
			{
				if (!zone.getNeighborTiles(depart).contains(target)) 
				{ 
					findPath(tab, depart.getCol(), depart.getLine(), new SearchNode(target.getCol(), target.getLine(), tab[target.getCol()][target.getLine()], mu));
				} 
				else 
				{
					path = new LinkedList<SearchNode>();
					path.offer(new SearchNode(target.getCol(), target.getLine(),tab[target.getCol()][target.getLine()], mu));
				}
			} 
			else 
			{
				path = new LinkedList<SearchNode>();
			}
		}
	}

	/**
	 * 
	 * @param zone
	 * @param simulatedOrAdaptedZone
	 * @param depart
	 * @param target
	 * @param mu
	 * @param mode
	 * @throws StopRequestException
	 */
	public PathFinder(AiZone zone, ZoneEnum[][] simulatedOrAdaptedZone,
			AiTile depart, AiTile target, MedeniUluer mu,
			SearchEnum mode) throws StopRequestException {
		mu.checkInterruption(); // Appel Obligatoire
		this.mu = mu;
		this.zone = zone;
		this.tab = simulatedOrAdaptedZone;
		this.mode = mode;
		if (depart.equals(target))
			path = new LinkedList<SearchNode>();
		else 
		{
			int col = target.getCol();
			int line = target.getLine();
			if (mode == SearchEnum.BOMB_SIMULATION) 
			{
				if (tab[col][line] != ZoneEnum.BLOCDESTRUCTIBLE
						&& tab[col][line] != ZoneEnum.BLOCINDESTRUCTIBLE
						&& tab[col][line] != ZoneEnum.BLOC_EXPLOSE_SIMULE
						&& tab[col][line] != ZoneEnum.EXPLOSION_SIMULE)
				{					
					if (!zone.getNeighborTiles(depart).contains(target)) 
					{ 
						findPath(tab, depart.getCol(), depart.getLine(),new SearchNode(target.getCol(), target.getLine(),tab[target.getCol()][target.getLine()],mu));
					} 
					else 
					{
						path = new LinkedList<SearchNode>();
						path.offer(new SearchNode(target.getCol(), target.getLine(), tab[target.getCol()][target.getLine()], mu));
					}
				} 
				else 
				{
					path = new LinkedList<SearchNode>();
				}
			} 
			else 
			{
				if (tab[target.getCol()][target.getLine()] != ZoneEnum.BLOCDESTRUCTIBLE && tab[target.getCol()][target.getLine()] != ZoneEnum.BLOCINDESTRUCTIBLE) 
				{
					if (!zone.getNeighborTiles(depart).contains(target)) 
					{ 
						findPath(tab, depart.getCol(), depart.getLine(),new SearchNode(target.getCol(), target.getLine(),tab[target.getCol()][target.getLine()],mu));
					} 
					else 
					{
						path = new LinkedList<SearchNode>();
						path.offer(new SearchNode(target.getCol(), target.getLine(), tab[target.getCol()][target.getLine()], mu));
					}
				} 
				else 
				{
					path = new LinkedList<SearchNode>();
				}
			}
		}
	}

	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public String toStringPath() throws StopRequestException {
		mu.checkInterruption();
		return path.toString();
	}

	
	/**
	 *Il determine le chemin le plus court au cible.Il utilise l'algorithme de
	 * A étoile. (cf. : http://fr.wikipedia.org/wiki/Algorithme_A*)
	 * 
	 * @param x
	 *            le coordonné de x de l'ia.
	 * @param y
	 *            le coordonné de y de l'ia.
	 * @param tab
	 *            le tableau du jeu.
	 * @param goal
	 *            la case qu'on veut y arriver.
	 * @throws StopRequestException
	 */
	private void findPath(ZoneEnum[][] tab, int x, int y, SearchNode goal)
			throws StopRequestException {
		mu.checkInterruption(); // Appel Obligatoire
		
		path = new LinkedList<SearchNode>();

		SearchNode courant = new SearchNode(x, y, tab[x][y], 0, mu);
		SearchTree tree = new SearchTree(courant, mu);
		SearchNodeComp comp = new SearchNodeComp(goal,mu);
		
		//A etoile;
		PriorityQueue<SearchNode> frange = new PriorityQueue<SearchNode>(1, comp);
		
		LinkedList<SearchNode> open = new LinkedList<SearchNode>();
		LinkedList<SearchNode> closed = new LinkedList<SearchNode>();
		SearchNode temp = new SearchNode(mu);

		SearchNode solution = null;
		frange.offer(courant);
		open.add(courant);
		
		while ((solution == null) && (!frange.isEmpty())) 
		{
			mu.checkInterruption(); // Appel Obligatoire
			temp = frange.poll();
			open.remove(open.indexOf(temp));
			closed.add(temp);

			if (temp.equals(goal)) 
			{
				solution = temp;
			} 
			else 
			{
				SearchNode up = null;
				SearchNode down = null;
				SearchNode right = null;
				SearchNode left = null;
				
				if (getConditions(temp, mode, Direction.UP)) 
				{
					up = new SearchNode(temp.getX(), temp.getY() - 1, tab[temp.getX()][temp.getY() - 1], temp.getCout() + 1,mu);
					if (!open.contains(up) && !closed.contains(up))
					{
						open.add(up);
						tree.addSearchNode(temp, up);
						frange.offer(up);
					}
				}
				
				if (getConditions(temp, mode, Direction.DOWN)) 
				{
					down = new SearchNode(temp.getX(), temp.getY() + 1, tab[temp.getX()][temp.getY() + 1], temp.getCout() + 1,mu);
					if (!open.contains(down) && !closed.contains(down)) 
					{
						open.add(down);
						tree.addSearchNode(temp, down);
						frange.offer(down);
					}				
				}
				
				if (getConditions(temp, mode, Direction.RIGHT)) 
				{
					right = new SearchNode(temp.getX() + 1, temp.getY(), tab[temp.getX() + 1][temp.getY()], temp.getCout() + 1,mu);
					if (!open.contains(right) && !closed.contains(right)) 
					{
						open.add(right);
						tree.addSearchNode(temp, right);
						frange.offer(right);
					}
				}
				
				if (getConditions(temp, mode, Direction.LEFT)) 
				{
					left = new SearchNode(temp.getX() - 1, temp.getY(), tab[temp.getX() - 1][temp.getY()], temp.getCout() + 1,mu);
					if (!open.contains(left) && !closed.contains(left)) 
					{
						open.add(left);
						tree.addSearchNode(temp, left);
						frange.offer(left);
					}
				}
			}
		}
		
		if (solution != null)
			path = tree.getPath(solution);

		frange = null;
		tree = null;
	}

	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<AiTile> getPath() throws StopRequestException {
		mu.checkInterruption(); // Appel Obligatoire
		// On doit renverser la file
		
		LinkedList<AiTile> resultat = new LinkedList<AiTile>();
		LinkedList<SearchNode> tempPath = (LinkedList<SearchNode>) this.path.clone();
		LinkedList<AiTile> pileTampon = new LinkedList<AiTile>();
		SearchNode tempSearchNode;
		
		while (!tempPath.isEmpty()) 
		{
			mu.checkInterruption(); // Appel Obligatoire
			tempSearchNode = tempPath.poll();
			pileTampon.push(zone.getTile(tempSearchNode.getY(), tempSearchNode.getX()));
		}
		
		while (!pileTampon.isEmpty()) 
		{
			mu.checkInterruption(); // Appel Obligatoire
			resultat.offer(pileTampon.pop());
		}
		return resultat;
	}

	/**
	 * 
	 * @param noeud
	 * @param mode
	 * @param direction
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean getConditions(SearchNode noeud, SearchEnum mode,Direction direction) throws StopRequestException 
	{
		mu.checkInterruption();
		switch (direction) {
		case UP:
			switch (mode) {
			case BLOC_DEST_INDEST:
				return (noeud.getY() - 1 >= 1)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.BLOCINDESTRUCTIBLE);
			case BLOC_DEST_INDEST_FEU_BOMBE:
				return (noeud.getY() - 1 >= 1)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.FEU)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.BOMBE)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.BLOCINDESTRUCTIBLE);
			case BLOC_DEST_INDEST_FEU_BOMBE_FEU_POSSIBLE:
				return (noeud.getY() - 1 >= 1)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.FEU)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.BOMBE)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.BLOCINDESTRUCTIBLE)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.FEUPOSSIBLE);
			case BOMB_SIMULATION:
				return (noeud.getY() - 1 >= 1)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.BLOC_EXPLOSE_SIMULE)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.BLOCINDESTRUCTIBLE)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.FEUPOSSIBLE)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.FEU)
						&& (tab[noeud.getX()][noeud.getY() - 1] != ZoneEnum.BOMBE);

			default:
				return false;
			}
		case DOWN:
			switch (mode) {
			case BLOC_DEST_INDEST:
				return (noeud.getY() + 1 <= zone.getHeight())
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.BLOCINDESTRUCTIBLE);
			case BLOC_DEST_INDEST_FEU_BOMBE:
				return (noeud.getY() + 1 <= zone.getHeight())
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.FEU)
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.BOMBE)
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.BLOCINDESTRUCTIBLE);
			case BLOC_DEST_INDEST_FEU_BOMBE_FEU_POSSIBLE:
				return (noeud.getY() + 1 <= zone.getHeight())
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.FEU)
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.BOMBE)
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.BLOCINDESTRUCTIBLE)
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.FEUPOSSIBLE);
			case BOMB_SIMULATION:
				return (noeud.getY() + 1 <= zone.getHeight())
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.BLOC_EXPLOSE_SIMULE)
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.BLOCINDESTRUCTIBLE)
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.FEUPOSSIBLE)
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.FEU)
						&& (tab[noeud.getX()][noeud.getY() + 1] != ZoneEnum.BOMBE);
			default:
				return false;
			}
		case RIGHT:
			switch (mode) {
			case BLOC_DEST_INDEST:
				return (noeud.getX() + 1 <= zone.getWidth())
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.BLOCINDESTRUCTIBLE);
			case BLOC_DEST_INDEST_FEU_BOMBE:
				return (noeud.getX() + 1 <= zone.getWidth())
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.FEU)
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.BOMBE)
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.BLOCINDESTRUCTIBLE);
			case BLOC_DEST_INDEST_FEU_BOMBE_FEU_POSSIBLE:
				return (noeud.getX() + 1 <= zone.getWidth())
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.FEU)
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.BOMBE)
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.BLOCINDESTRUCTIBLE)
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.FEUPOSSIBLE);
			case BOMB_SIMULATION:
				return (noeud.getX() + 1 <= zone.getWidth())
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.BLOC_EXPLOSE_SIMULE)
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.BLOCINDESTRUCTIBLE)
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.FEUPOSSIBLE)
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.FEU)
						&& (tab[noeud.getX() + 1][noeud.getY()] != ZoneEnum.BOMBE);
			default:
				return false;
			}
		case LEFT:
			switch (mode) {
			case BLOC_DEST_INDEST:
				return (noeud.getX() - 1 >= 1)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.BLOCINDESTRUCTIBLE);
			case BLOC_DEST_INDEST_FEU_BOMBE:
				return (noeud.getX() - 1 >= 1)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.FEU)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.BOMBE)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.BLOCINDESTRUCTIBLE);
			case BLOC_DEST_INDEST_FEU_BOMBE_FEU_POSSIBLE:
				return (noeud.getX() - 1 >= 1)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.FEU)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.BOMBE)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.BLOCINDESTRUCTIBLE)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.FEUPOSSIBLE);
			case BOMB_SIMULATION:
				return (noeud.getX() - 1 >= 1)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.BLOC_EXPLOSE_SIMULE)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.BLOCINDESTRUCTIBLE)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.FEUPOSSIBLE)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.FEU)
						&& (tab[noeud.getX() - 1][noeud.getY()] != ZoneEnum.BOMBE);
			default:
				return false;
			}
		default:
			return false;
		}
	}

}



