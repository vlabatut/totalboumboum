package org.totalboumboum.ai.v201011.ais.kesimalvarol.v6;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * La classe qui sert a modifier la zone du jeu, particulierement pour poser des
 * bombes, en modifiant la zone comme on avait posee une bombe. On pourrait bien
 * utiliser AiModel, mais on avait déja implementé celle-ci et notre IA n'a pas
 * vraiment besoin de toutes ses fonctionnalités servies.
 * 
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 */
@SuppressWarnings("deprecation")
public class GameZoneFuturePredictions {
	/** classe principale de l'IA, permet d'accÔøΩder ÔøΩ checkInterruption() */
	private static KesimalVarol monIA;

	/**
	 * 
	 * @param monIA
	 *            description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public static void setMonIA(KesimalVarol monIA) throws StopRequestException {
		monIA.checkInterruption();
		GameZoneFuturePredictions.monIA = monIA;
	}

	/**
	 * Pour prevoir la matrice ayant une bombe
	 * 
	 * @param m
	 *            description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public static void modifyMatrixWithFutureBomb(Matrix m)
			throws StopRequestException {
		monIA.checkInterruption();
		AiTile bombloc = monIA.getSelfHero().getTile();
		modifyMatrixWithFutureBomb(m, bombloc);
	}

	/**
	 * Pour prevoir la matrice ayant une bombe
	 * 
	 * @param m
	 *            La matrice qui sera modifiee
	 * @param bombloc
	 *            description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public static void modifyMatrixWithFutureBomb(Matrix m, AiTile bombloc)
			throws StopRequestException {
		monIA.checkInterruption();
		int bombrange = monIA.getSelfHero().getBombRange();
		AiZone zone = monIA.getZone();

		double bombval = -200;
		int i = bombloc.getLine(), j = bombloc.getCol();
		if (bombval > m.representation[i][j])
			bombval = m.representation[i][j];

		if (monIA.verbose)
			System.out.println(i + "," + j);
		if (monIA.verbose)
			System.out.println(bombrange);

		for (int iC = i - 1; iC >= i - bombrange; iC--) {
			monIA.checkInterruption();
			if (iC < monIA.getZone().getHeight() && iC >= 0) {
				if (monIA.verbose)
					System.out.println(zone.getTile(iC, j));
				try {
					if (zone.getTile(iC, j).getBlocks().size() == 0) {
						if (monIA.verbose)
							System.out.println("M1 : " + zone.getTile(iC, j));
						if (m.representation[iC][j] > bombval)
							m.representation[iC][j] = bombval;
						if (monIA.verbose)
							System.out.println(m.representation[iC][j]);
					} else
						break;
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
		for (int jC = j - 1; jC >= j - bombrange; jC--) {
			monIA.checkInterruption();
			if (jC < monIA.getZone().getWidth() && jC >= 0) {
				if (monIA.verbose)
					System.out.println(zone.getTile(i, jC));
				try {
					if (zone.getTile(i, jC).getBlocks().size() == 0) {
						if (monIA.verbose)
							System.out.println("M2 : " + zone.getTile(i, jC));
						if (m.representation[i][jC] > bombval)
							m.representation[i][jC] = bombval;
						if (monIA.verbose)
							System.out.println(m.representation[i][jC]);
					} else
						break;
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
		for (int iC = i; iC <= i + bombrange; iC++) {
			monIA.checkInterruption();
			if (iC < monIA.getZone().getHeight() && iC >= 0) {
				try {
					if (zone.getTile(iC, j).getBlocks().size() == 0) {
						if (monIA.verbose)
							System.out.println("M3 : " + zone.getTile(iC, j));
						if (m.representation[iC][j] > bombval)
							m.representation[iC][j] = bombval;
						if (monIA.verbose)
							System.out.println(m.representation[iC][j]);
					} else
						break;
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
		for (int jC = j; jC <= j + bombrange; jC++) {
			monIA.checkInterruption();
			if (jC < monIA.getZone().getWidth() && jC >= 0) {
				try {
					if (zone.getTile(i, jC).getBlocks().size() == 0) {
						if (monIA.verbose)
							System.out.println("M4 : " + zone.getTile(i, jC));
						if (m.representation[i][jC] > bombval)
							m.representation[i][jC] = bombval;
						if (monIA.verbose)
							System.out.println(m.representation[i][jC]);
					} else
						break;
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
		if (monIA.verbose)
			System.out.println("Bombval set to " + bombval);
		// m.representation[i][j]=-300;

	}

	/**
	 * 
	 * @param m
	 *            description manquante !
	 * @return description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public static boolean areBonusAdjacentAndNotTargetingEnemies(Matrix m)
			throws StopRequestException {
		monIA.checkInterruption();
		return areBonusAdjacentAndNotTargetingEnemies(m, monIA.getSelfHero()
				.getTile());
	}

	/**
	 * Pour ne pas detruire les bonus si elles sont a cote d'un mur destructible
	 * 
	 * @param m
	 *            Matrice necessaire pour les calculs
	 * @param currentLocation
	 *            description manquante !
	 * @return Oui s'il y a des bonus en proximite.
	 * @throws StopRequestException
	 *             description manquante !
	 * */
	public static boolean areBonusAdjacentAndNotTargetingEnemies(Matrix m,
			AiTile currentLocation) throws StopRequestException {
		monIA.checkInterruption();
		int i = currentLocation.getLine(), j = currentLocation.getCol(), bombrange = monIA
				.getSelfHero().getBombRange();
		if (m.regionEmplacementImportanceMatrix[i][j].importance >= 2)
			m.representation[i][j] -= (m.regionEmplacementImportanceMatrix[i][j].importance - 1) * 40;

		// Si celle-ci est pour detruire les adversaires, on va laisser la
		// bombe.
		if (m.regionEmplacementImportanceMatrix[i][j].forEnemy)
			return false;

		// Sinon, on voudrait ramasser les bonus avant leur attaquer.
		AiZone zone = monIA.getZone();
		for (int iC = i - 1; iC > i - bombrange; iC--) {
			monIA.checkInterruption();
			try {
				if (zone.getTile(iC, j).getBlocks().size() == 0) {
					if (zone.getTile(iC, j).getItems().size() > 0) {
						monIA.setPrBonusTile(zone.getTile(iC, j));
						return true;
					}
				} else
					break;
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			} catch (NullPointerException e) {
				continue;
			}
		}
		for (int iC = i; iC < i + bombrange; iC++) {
			monIA.checkInterruption();
			try {
				if (zone.getTile(iC, j).getBlocks().size() == 0) {
					if (zone.getTile(iC, j).getItems().size() > 0) {
						monIA.setPrBonusTile(zone.getTile(iC, j));
						return true;
					}
				} else
					break;
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			} catch (NullPointerException e) {
				continue;
			}
		}
		for (int jC = j - 1; jC > j - bombrange; jC--) {
			monIA.checkInterruption();
			try {
				if (zone.getTile(i, jC).getBlocks().size() == 0) {
					if (zone.getTile(i, jC).getItems().size() > 0) {
						monIA.setPrBonusTile(zone.getTile(i, jC));
						return true;
					}
				} else
					break;
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			} catch (NullPointerException e) {
				continue;
			}
		}
		for (int jC = j; jC < j + bombrange; jC++) {
			monIA.checkInterruption();
			try {
				if (zone.getTile(i, jC).getBlocks().size() == 0) {
					if (zone.getTile(i, jC).getItems().size() > 0) {
						monIA.setPrBonusTile(zone.getTile(i, jC));
						return true;
					}
				} else
					break;
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			} catch (NullPointerException e) {
				continue;
			}
		}
		return false;
	}
}
