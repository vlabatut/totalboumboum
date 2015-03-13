package org.totalboumboum.ai.v201011.ais.caliskanseven.v6;

import java.awt.Color;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;

/**
 * This is the main class of our AI.
 * 
 * * In this class we : value the matrixes,choose the mode(attacking or
 * collecting mode),make the ai to choose the optimum tile and time to place the
 * bomb,simulate the path that ai can reach.
 * 
 * @author Mustafa Çalışkan
 * @author Cihan Seven
 */
@SuppressWarnings({ "unused", "deprecation" })
public class CaliskanSeven extends ArtificialIntelligence {

	/** */
	AiTile oldtarget = null;
	/** */
	AiPath oldpath = null;
	/** */
	boolean collecte = true;
	/** */
	int targetcounter = 0;
	/** */
	boolean debug = false;

	@Override
	public AiAction processAction() throws StopRequestException {
		checkInterruption();

		Matrix mtx = new Matrix();
		PathFinding pfind = new PathFinding();

		List<AiTile> closed = pfind.listEscapeBomb(this);

		// Calculation of Collect Matrix and Attack Matrix
		double[][] mtx_collecte = mtx.calculate(this, -50, -50, -50, 2, -5, -1,
				1, closed, 0);
		double[][] mtx_attaque = mtx.calculate(this, -50, -50, -50, 0, -1, 5,
				0, closed, 0);

		double[][] mtx_final;

		// Choosing the matrix to use
		try {
			if (pfind.canReachHeros(this)) {
				collecte = false;
				mtx_final = mtx_attaque;
			} else
				mtx_final = mtx_collecte;
		} catch (LimitReachedException e) {
			// 
			//e.printStackTrace();
			mtx_final = mtx_collecte;
		}

		// Movement & Placing the bombs
		AiTile target = pfind.findTarget(this, mtx_final);

		try {
			// Stabilizing the path
			AiPath path = pfind.findPath(this, target, mtx_final);

			if (oldtarget == null) {
				oldtarget = target;
			}
			if (oldpath == null) {
				oldpath = path;
			}

			if (target == oldtarget && (path.compareTo(oldpath) == 0)) {
				target = oldtarget;
				path = oldpath;
			}

			// Place the bomb when reached,if not continue anyway
			if (path.isEmpty() || path.getLength() == 1) {
				AiAction result = null;
				targetcounter++;

				int bombn = this.getPercepts().getOwnHero()
						.getBombNumberCurrent();

				if (pfind.canEscapeBomb(this, closed)
						&& (!collecte || bombn == 0) && targetcounter > 5) {
					result = new AiAction(AiActionName.DROP_BOMB);
					targetcounter = 0;
				} else
					result = new AiAction(AiActionName.NONE);
				return result;
			} else {
				AiAction result = new AiAction(AiActionName.MOVE, this
						.getPercepts().getDirection(path.getTile(0),
								path.getTile(1)));

				oldpath = path;
				oldtarget = target;

				return result;
			}
		} catch (LimitReachedException e) {
			// 
			//e.printStackTrace();
		}

		if (debug) {
			// Showing the values of the matrix in the game play
			for (int l = 0; l < this.getPercepts().getHeight(); ++l) {
				checkInterruption();
				for (int c = 0; c < this.getPercepts().getWidth(); ++c) {
					checkInterruption();
					this.getOutput().setTileText(l, c,
							String.valueOf(Math.round(mtx_final[l][c])));
				}
			}
		}

		AiAction result = new AiAction(AiActionName.NONE);
		return result;
	}

}
