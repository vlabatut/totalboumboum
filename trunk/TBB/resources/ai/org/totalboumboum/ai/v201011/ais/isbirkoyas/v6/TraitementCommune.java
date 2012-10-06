package org.totalboumboum.ai.v201011.ais.isbirkoyas.v6;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.ais.isbirkoyas.v6.DeplacementCommune;
import org.totalboumboum.ai.v201011.ais.isbirkoyas.v6.IsbirKoyas;

/**
 * @author Göksu İsbir
 * @author Ela Koyaş
 */
@SuppressWarnings("deprecation")
public class TraitementCommune {

	private DeplacementCommune deplacementCommune = null;
	IsbirKoyas ai = new IsbirKoyas();

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public TraitementCommune(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		this.ai = ai;

	}

	/**
	 * Cette méthode forme une liste des cases cibles que notre IA peut aller
	 * dans le mode attaque. Elle prend deux arguments une matrice de type
	 * double et la zone du jeu. Notre IA peut se déplacer en traversant ces
	 * cases.
	 * 
	 * @param matriceAttaque
	 *            La Matrice attaque
	 * @param gameZone
	 *            la zone du jeu
	 * @return la liste des points finaux.
	 * @throws StopRequestException
	 */
	public List<AiTile> calculeLesPointsFinaux(double[][] matriceAttaque,
			AiZone gameZone) throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE

		List<AiTile> endPoints = new ArrayList<AiTile>();
		int i = gameZone.getHeight();
		int j = gameZone.getWidth();
		for (int ii = 0; ii < i; ii++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int jj = 0; jj < j; jj++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				if (matriceAttaque[ii][jj] > 1
						&& gameZone.getTile(ii, jj).getBlocks().isEmpty()) {
					ai.checkInterruption(); // APPEL OBLIGATOIRE
					endPoints.add(gameZone.getTile(ii, jj));
				}
			}
		}

		List<AiTile> endPoints2 = new ArrayList<AiTile>();

		destinationAttaque(endPoints, endPoints2, matriceAttaque);

		if (ai.print)
			System.out.println("Attaque: EndPoints sans DANGER:" + endPoints2);

		return endPoints2;
	}

	/**
	 * Cette méthode compare les cases cibles entre eux et retourne la case qui
	 * a la valeur plus élevée. S'il y a plus d'une valeur élevée égales alors
	 * IA regarde au distance de ceux cases. Elle prend trois arguments une
	 * matrice de type double et deux listes des cases.
	 * 
	 * @param matrice
	 *            La Matrice Attaque
	 * @param endPoints
	 * @param endPoints2
	 * @throws StopRequestException
	 */
	public void destinationAttaque(List<AiTile> endPoints,
			List<AiTile> endPoints2, double[][] matrice)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		AiTile temp;

		// ordre
		if (endPoints.size() > 1) {
			for (int j = 0; j < endPoints.size(); j++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				for (int i = 0; i < endPoints.size() - 1; i++) {
					ai.checkInterruption(); // APPEL OBLIGATOIRE
					AiTile element = endPoints.get(i);
					AiTile element2 = endPoints.get(i + 1);
					int x = element.getLine();
					int y = element.getCol();
					int x2 = element2.getLine();
					int y2 = element2.getCol();
					double val = matrice[x][y];
					double val2 = matrice[x2][y2];
					if (val < val2) {
						ai.checkInterruption(); // APPEL OBLIGATOIRE
						temp = element;
						endPoints.set(i, element2);
						endPoints.set(i + 1, temp);
					}
				}
			}
		}
		if (ai.print)
			System.out.println("Destination1:" + endPoints);
		if (!endPoints.isEmpty()) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			double max = matrice[endPoints.get(0).getLine()][endPoints.get(0)
					.getCol()];
			for (int i = 0; i < endPoints.size(); i++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				AiTile element = endPoints.get(i);
				int x = element.getLine();
				int y = element.getCol();
				double val = matrice[x][y];
				if (val >= max)
					endPoints2.add(element);
			}
		}

		if (ai.print)
			System.out.println("Destination2:" + endPoints2);
	}

	/**
	 * Cette méthode forme une liste des cases cibles que notre IA peut aller
	 * dans le mode collecte. Elle prend deux arguments, une matrice de type Int
	 * et la zone du jeu. On prend aussi en compte les cases qui sont dans la
	 * portée des bombes.
	 * 
	 * @param matrice
	 *            La Matrice collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @return la liste des points finaux.
	 * @throws StopRequestException
	 */
	public List<AiTile> calculeLesPointsFinaux(int[][] matrice, AiZone gameZone)
			throws StopRequestException {

		ai.checkInterruption();
		// La perception instantanement de l'environnement
		gameZone = ai.getPercepts();
		// la longueur de la matrice
		int width = gameZone.getWidth();
		// la largeur de la matrice
		int height = gameZone.getHeight();
		// la liste ou les points finaux sont tenus
		List<AiTile> endPoints = new ArrayList<AiTile>();
		for (int i = 0; i < height; i++) {
			ai.checkInterruption();
			for (int j = 0; j < width; j++) {
				ai.checkInterruption();
				if (matrice[i][j] > 0) {
					endPoints.add(gameZone.getTile(i, j));
				}
			}
		}
		if (ai.print)
			System.out.println("Collect: EndPoints:" + endPoints);

		List<AiTile> endPoints2 = new ArrayList<AiTile>();

		destinationCollect(endPoints, endPoints2, matrice);
		endPoints = endPoints2;

		if (ai.print)
			System.out.println("Collect: Destination EndPoints:" + endPoints2);
		return endPoints;
	}

	/**
	 * Cette méthode compare les cases cibles entre eux et retourne la case qui
	 * a la valeur plus élevée. S'il y a plus d'une valeur élevée égales alors
	 * IA regarde au distance de ceux cases .Elle prend trois arguments une
	 * matrice de type Int et deux listes des cases.
	 * 
	 * @param matrice
	 *            La Matrice collecte
	 * @param endPoints
	 * @param endPoints2
	 * @throws StopRequestException
	 */
	public void destinationCollect(List<AiTile> endPoints,
			List<AiTile> endPoints2, int[][] matrice)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		AiTile temp;
		for (int j = 0; j < endPoints.size(); j++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int i = 0; i < endPoints.size() - 1; i++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				AiTile element = endPoints.get(i);
				AiTile element2 = endPoints.get(i + 1);
				int x = element.getLine();
				int y = element.getCol();
				int x2 = element2.getLine();
				int y2 = element2.getCol();
				int val = matrice[x][y];
				int val2 = matrice[x2][y2];
				if (val < val2) {
					temp = element;
					endPoints.set(i, element2);
					endPoints.set(i + 1, temp);
				}
			}
		}
		if (!endPoints.isEmpty()) {
			int max = matrice[endPoints.get(0).getLine()][endPoints.get(0)
					.getCol()];
			for (int i = 0; i < endPoints.size(); i++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				AiTile element = endPoints.get(i);
				int x = element.getLine();
				int y = element.getCol();
				int val = matrice[x][y];
				if (val >= max)
					endPoints2.add(element);
			}
		}
	}

	// LE METHODE D'ACCES
	/**
	 * METHODE D'ACCES a la classe DeplacementCommune
	 * 
	 * @throws StopRequestException
	 * @return deplacement commune
	 */
	public DeplacementCommune DeplacementCommune() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE
		return deplacementCommune;
	}
}
