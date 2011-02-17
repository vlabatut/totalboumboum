package org.totalboumboum.ai.v201011.ais.caliskanseven.v6;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * This is the Path Finding Class of our AI
 * 
 * In this class,according to our path calculation algorithm,our AI tries to
 * find the most valuable path to walk.
 * 
 * @author Cihan Adil SEVEN & Mustafa CALISKAN
 * 
 */
public class PathFinding {
	/**
	 * 
	 * @param t1
	 *            AiTile Colon
	 * @param t2
	 *            AiTile Line
	 * @return returns the tile which we want to go
	 * @throws StopRequestException
	 */
	public static int tiledist(CaliskanSeven ai, AiTile t1, AiTile t2)
			throws StopRequestException {
		ai.checkInterruption();
		return Math.abs(t1.getCol() - t2.getCol())
				+ Math.abs(t1.getLine() - t2.getLine());
	}

	/**
	 * In this method we check the map if we can reach to a hero at that
	 * moment. We get the lists of heroes and simulate paths to them if they
	 * are reachable. If at least one of them is reachable this methode
	 * returns true otherwise returns false.
	 */
	public boolean canReachHeros(CaliskanSeven ai) throws StopRequestException,
			LimitReachedException {
		
		ai.checkInterruption();
		// Checking if we can reach at least one of the heros
		BasicCostCalculator bCc = new BasicCostCalculator();
		BasicHeuristicCalculator bHc = new BasicHeuristicCalculator();

		Astar astar = new Astar(ai, ai.getPercepts().getOwnHero(), bCc, bHc);

		List<AiHero> heros = ai.getPercepts().getHeroes();

		for (AiHero h : heros) {
			ai.checkInterruption();

			if (h.equals(ai.getPercepts().getOwnHero()))
				continue;

			if (h.hasEnded())
				continue;

			AiPath p = null;
			try {
				p = astar.processShortestPath(ai.getPercepts().getOwnHero()
						.getTile(), h.getTile());
			} catch (LimitReachedException e) {
				ai.checkInterruption();
			}

			if (p == null) {
				continue;
			}

			if (!p.isEmpty())
				return true;
		}

		return false;
	}

	/**
	 * this method returns the path of shortest way with using Astar.
	 */
	public AiPath findPath(CaliskanSeven ai, AiTile target, double[][] matrix)
			throws StopRequestException, LimitReachedException {
		
		ai.checkInterruption();
		Astar astar = new Astar(ai, ai.getPercepts().getOwnHero(),
				new BasicCostCalculator(), new BasicHeuristicCalculator());

		return astar.processShortestPath(ai.getPercepts().getOwnHero()
				.getTile(), target);

	}

	/**
	 * This function returns a true or false value if our way is closed with a
	 * bomb. If there is a bomb in our way, we add the bomb range to the list as
	 * closed tile.
	 * 
	 * @throws StopRequestException
	 */
	public boolean canEscapeBomb(CaliskanSeven ai, List<AiTile> closed)
			throws StopRequestException {

		ai.checkInterruption();
		if (closed.size() > 0)
			return true;
		else
			return false;
	}

	/**
	 * This function returns a list of all the possible tiles that are safe
	 * in case we drop a bomb.
	 * 
	 * In this method we create two lists (open and closed) and we add tiles to
	 * open list or closed list according to the situation of that moment.The
	 * open list is our way to escape from danger.
	 */
	public List<AiTile> listEscapeBomb(CaliskanSeven ai)
			throws StopRequestException {

		ai.checkInterruption();
		AiTile tile = ai.getPercepts().getOwnHero().getTile();
		AiHero hero = ai.getPercepts().getOwnHero();
		ArrayList<AiTile> open = new ArrayList<AiTile>();
		ArrayList<AiTile> closed = new ArrayList<AiTile>();
		List<AiTile> blist = Util.getBlastList(ai, false);
		open.add(tile);

		while (!open.isEmpty()) {
			ai.checkInterruption();
			ArrayList<AiTile> toopen = new ArrayList<AiTile>();
			for (AiTile t : open) {
				ai.checkInterruption();
				for (Direction direction : Direction.getPrimaryValues()) {
					ai.checkInterruption();
					AiTile neighbor = t.getNeighbor(direction);
					if (neighbor.isCrossableBy(hero)
							&& !closed.contains(neighbor)
							&& !blist.contains(neighbor))
						toopen.add(neighbor);
				}
				closed.add(t);
			}
			for (AiTile t : closed) {
				ai.checkInterruption();
				if (open.contains(t)) {
					open.remove(t);
				}
			}
			for (AiTile t : toopen) {
				ai.checkInterruption();
				open.add(t);
			}
		}

		int bombrange = hero.getBombRange();
		int h_line = hero.getLine();
		int h_col = hero.getCol();
		int maxline = ai.getPercepts().getHeight() - 1;
		int maxcol = ai.getPercepts().getWidth() - 1;
		for (int a = 0; a <= bombrange; a++) {
			ai.checkInterruption();
			AiTile t1 = ai.getPercepts().getTile(Math.min(h_line + a, maxline),
					h_col);
			if (closed.contains(t1))
				closed.remove(t1);

			AiTile t2 = ai.getPercepts()
					.getTile(Math.max(h_line - a, 0), h_col);
			if (closed.contains(t2))
				closed.remove(t2);

			AiTile t3 = ai.getPercepts().getTile(h_line,
					Math.min(h_col + a, maxcol));
			if (closed.contains(t3))
				closed.remove(t3);

			AiTile t4 = ai.getPercepts()
					.getTile(h_line, Math.max(h_col - a, 0));
			if (closed.contains(t4))
				closed.remove(t4);
		}

		return closed;

	}

	/**
	 * This function finds a target tile using the  matrix given.
	 * 
	 * @param ai
	 * @param matrix
	 * @return
	 * @throws StopRequestException
	 */
	public AiTile findTarget(CaliskanSeven ai, double matrix[][])
			throws StopRequestException {
		
		ai.checkInterruption();
		AiTile tile = ai.getPercepts().getOwnHero().getTile();
		AiHero hero = ai.getPercepts().getOwnHero();
		ArrayList<AiTile> open = new ArrayList<AiTile>();
		ArrayList<AiTile> closed = new ArrayList<AiTile>();
		List<AiTile> blist = Util.getBlastList(ai, true);
		open.add(tile);

		while (!open.isEmpty()) {
			ai.checkInterruption();
			ArrayList<AiTile> toopen = new ArrayList<AiTile>();
			for (AiTile t : open) {
				ai.checkInterruption();
				for (Direction direction : Direction.getPrimaryValues()) {
					ai.checkInterruption();
					AiTile neighbor = t.getNeighbor(direction);
					if (neighbor.isCrossableBy(hero)
							&& !closed.contains(neighbor)
							&& !blist.contains(neighbor))
						toopen.add(neighbor);
				}
				closed.add(t);
			}
			for (AiTile t : closed) {
				ai.checkInterruption();
				if (open.contains(t)) {
					open.remove(t);
				}
			}
			for (AiTile t : toopen) {
				ai.checkInterruption();
				open.add(t);
			}
		}

		// Finding the most valuable tile in the list
		double mp = matrix[closed.get(0).getLine()][closed.get(0).getCol()];
		int ml = closed.get(0).getLine();
		int mc = closed.get(0).getCol();
		for (AiTile t : closed) {
			ai.checkInterruption();
			if (mp < matrix[t.getLine()][t.getCol()]) {
				mp = matrix[t.getLine()][t.getCol()];
				ml = t.getLine();
				mc = t.getCol();
			}
		}

		tile = ai.getPercepts().getTile(ml, mc);

		return tile;

	}
}
