package org.totalboumboum.ai.v201011.ais.kayayerinde.v3;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Önder Kaya
 * @author Nezaket Yerinde
 */
@SuppressWarnings("deprecation")
public class GetSafe {

	private double nFree = 0;
	private double nDestructible = 50;
	private double nBlock = 100;
	private KayaYerinde ai;
	private AiHero ownHero;
	private AiZone zone;

	public GetSafe(KayaYerinde ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.zone = ai.getPercepts();
		this.ownHero = this.zone.getOwnHero();
	}

	public Direction destinationDanger(Matrice mat) throws StopRequestException {
		ai.checkInterruption();
		Direction dir = Direction.NONE;
		AiTile bombTile = mat.getBombTile(ownHero);
		if (bombTile != null) {
			if (bombTile.getCol() == this.ownHero.getCol()
					&& bombTile.getLine() == this.ownHero.getLine())
				dir = Direction.DOWNLEFT;
			else if (bombTile.getCol() == this.ownHero.getCol()) {
				if (bombTile.getLine() < this.ownHero.getLine())
					dir = Direction.UP;
				else
					dir = Direction.DOWN;
			} else if (bombTile.getLine() == this.ownHero.getLine()) {
				if (bombTile.getCol() < this.ownHero.getCol())
					dir = Direction.LEFT;
				else
					dir = Direction.RIGHT;
			}
		}
		return dir;
	}

	public Direction getRescueDirection(Matrice m) throws StopRequestException {
		ai.checkInterruption();
		boolean up = true;
		boolean down = true;
		boolean left = true;
		boolean right = true;
		Direction dir = destinationDanger(m), result = null;
		int i = 0, j = 1;
		double[][] mat = m.getMatrice();
		if (dir != Direction.NONE) {
			if (dir == Direction.UP) {
				if (mat[ownHero.getLine()][ownHero.getCol() + 1] == nFree)
					result = Direction.RIGHT;
				else if (mat[ownHero.getLine()][ownHero.getCol() - 1] == nFree)
					result = Direction.LEFT;
				else
					result = Direction.DOWN;
			}

			else if (dir == Direction.DOWN) {
				if (mat[ownHero.getLine()][ownHero.getCol() + 1] == nFree)
					result = Direction.RIGHT;
				else if (mat[ownHero.getLine()][ownHero.getCol() - 1] == nFree)
					result = Direction.LEFT;
				else
					result = Direction.UP;
			}

			else if (dir == Direction.LEFT) {
				if (mat[ownHero.getLine() - 1][ownHero.getCol()] == nFree)
					result = Direction.UP;
				else if (mat[ownHero.getLine() + 1][ownHero.getCol()] == nFree)
					result = Direction.DOWN;
				else
					result = Direction.RIGHT;
			}

			else if (dir == Direction.RIGHT) {
				if (mat[ownHero.getLine() - 1][ownHero.getCol()] == nFree)
					result = Direction.UP;
				else if (mat[ownHero.getLine() + 1][ownHero.getCol()] == nFree)
					result = Direction.DOWN;
				else
					result = Direction.LEFT;
			}

			else {
				while (i == 0 && (up || down || right || left)) {
					ai.checkInterruption();
					if (down) {
						if (mat[ownHero.getLine() + j][ownHero.getCol() + 1] == nFree || mat[ownHero.getLine()+ j][ownHero.getCol() - 1] == nFree) {
							if (mat[ownHero.getLine() + j][ownHero.getCol()] == nDestructible || mat[ownHero.getLine() + j][ownHero.getCol()] == nBlock)
								down = false;
							else {
								result = Direction.DOWN;
								i = 1;
							}
						}
					}

					else if (up) {
						if ((mat[ownHero.getLine() - j][ownHero.getCol() + 1] == nFree || mat[ownHero.getLine()- j][ownHero.getCol() - 1] == nFree)) {
							if (mat[ownHero.getLine() - j][ownHero.getCol()] == nDestructible || mat[ownHero.getLine() - j][ownHero.getCol()] == nBlock)
								up = false;
							else {
								result = Direction.UP;
								i = 1;
							}
						}
					}

					else if (right) {
						if (mat[ownHero.getLine() - 1][ownHero.getCol() + j] == nFree || mat[ownHero.getLine() + 1][ownHero.getCol() + j] == nFree) {
							if (mat[ownHero.getLine()][ownHero.getCol() + j] == nDestructible || mat[ownHero.getLine()][ownHero.getCol()+ j] == nBlock)
								right = false;
							else {
								result = Direction.RIGHT;
								i = 1;
							}
						}
					}

					else if (left) {
						if (mat[ownHero.getLine() - 1][ownHero.getCol() - j] == nFree || mat[ownHero.getLine() + 1][ownHero.getCol() - j] == nFree) {
							if (mat[ownHero.getLine()][ownHero.getCol() - j] == nDestructible || mat[ownHero.getLine()][ownHero.getCol()- j] == nBlock)
								left = false;
							else {
								result = Direction.LEFT;
								i = 1;
							}
						}
					}
					j++;
				}
			}
		}

		return result;
	}

}
