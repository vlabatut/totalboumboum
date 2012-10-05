package org.totalboumboum.ai.v200809.ais.medeniuluer.v2c;

import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * Représente un noeud dans un arbre de recherche.
 *Chaque noeud est defini avec les coordonnees x et y
 *chaque noeud possede aussi une valeur entiere 
 *et un cout.
 *
 * @author Ekin Medeni
 * @author Pınar Uluer
 *
 */
@SuppressWarnings("deprecation")
public class SearchNode {

	/** la coordonnee x du noeud */
	private int x;
	/** la coordonnee y du noeud */
	private int y;
	/** la valeur du noeud */
	private ZoneEnum valeur;
	/** le cout du noeud */
	private int cout = 0;

	private MedeniUluer mu;
	
	/**
	 * Constructeur.
	 * @param mu 
	 * @throws StopRequestException 
	 */
	public SearchNode(MedeniUluer mu) throws StopRequestException {
		super();
		mu.checkInterruption(); //Appel Obligatoire
		this.mu = mu;
	}

	/**
	 * Constructeur.
	 * 
	 * @param x
	 *            coordonne x du noeud
	 * @param y
	 *            coordonne y du noeud
	 * @param valeur
	 *            valeur du noeud
	 * @param cout
	 *            cout du noeud
	 * @throws StopRequestException 
	 */
	public SearchNode(int x, int y, ZoneEnum valeur, int cout) throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		this.x = x;
		this.y = y;
		this.valeur = valeur;
		this.cout = cout;
	}
	
	/**
	 * Constructeur.
	 * 
	 * @param x
	 *            coordonne x du noeud
	 * @param y
	 *            coordonne y du noeud
	 * @param valeur
	 *            valeur du noeud
	 * @param mu 
	 * @throws StopRequestException 
	 */
	public SearchNode(int x, int y, ZoneEnum valeur, MedeniUluer mu) throws StopRequestException {
		super();
		mu.checkInterruption();
		this.mu = mu;
		this.x = x;
		this.y = y;
		this.valeur = valeur;
	}
	
	/**
	 * Constructeur.
	 * 
	 * @param x
	 *            coordonne x du noeud
	 * @param y
	 *            coordonne y du noeud
	 * @param valeur
	 *            valeur du noeud
	 * @param cout
	 *            cout du noeud
	 * @param mu 
	 * @throws StopRequestException 
	 */
	public SearchNode(int x, int y, ZoneEnum valeur, int cout,MedeniUluer mu) throws StopRequestException {
		super();
		mu.checkInterruption();
		this.mu = mu;
		this.x = x;
		this.y = y;
		this.valeur = valeur;
		this.cout = cout;
		
		
	}

	/**
	 * Constructeur.
	 * 
	 * @param x
	 *            coordonne x du noeud
	 * @param y
	 *            coordonne y du noeud
	 * @param valeur
	 *            valeur du noeud
	 * @throws StopRequestException 
	 */
	public SearchNode(int x, int y, ZoneEnum valeur) throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		this.x = x;
		this.y = y;
		this.valeur = valeur;
	}

	/**
	 * Renvoie le coordonne x du noeud
	 * 
	 * @return coordonne x
	 * @throws StopRequestException 
	 */
	public int getX() throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		return x;
	}

	/**
	 * Change le coordonne x du noeud
	 * 
	 * @param x
	 *            coordonne x
	 * @throws StopRequestException 
	 */
	public void setX(int x) throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		this.x = x;
	}

	/**
	 * Renvoie le coordonne y du noeud
	 * 
	 * @return coordonne y
	 * @throws StopRequestException 
	 */
	public int getY() throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		return y;
	}

	/**
	 * Change le coordonne y du noeud
	 * 
	 * @param y
	 *            coordonne y
	 * @throws StopRequestException 
	 */
	public void setY(int y) throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		this.y = y;
	}

	/**
	 * Renvoie la valeur du noeud
	 * 
	 * @return valeur
	 * @throws StopRequestException 
	 */
	public ZoneEnum getValeur() throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		return valeur;
	}

	/**
	 * Change la valeur du noeud
	 * 
	 * @param valeur
	 *            valeur du noeud
	 * @throws StopRequestException 
	 */
	public void setValeur(ZoneEnum valeur) throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		this.valeur = valeur;
	}

	/**
	 * Renvoie le cout du noeud
	 * 
	 * @return cout
	 * @throws StopRequestException 
	 */

	public int getCout() throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		return cout;
	}

	/**
	 * Change le cout du noeud
	 * 
	 * @param cout
	 *            cout du noeud
	 * @throws StopRequestException 
	 */
	public void setCout(int cout) throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		this.cout = cout;
	}

	/**
	 * Controle l'égalité de l'objet à un autre objet
	 * 
	 * @param object
	 *            objet qu'on va comparer
	 * @return true si l'objet est égal à l'objet passé en parametre
	 */

	public boolean equals(Object object) {

		SearchNode node;
		try {
			node = new SearchNode(mu);
		} catch (StopRequestException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
		node = (SearchNode) object;
		try {
			if ((node.getX() == x) && (node.getY() == y))
				return true;

			else

				return false;
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Calcule l'heuristique du noeud par rapport à un autre noeud passé en
	 * parametre Il fait le calcul en utilisant la distance du Manhattan * (cf. :
	 * http://fr.wikipedia.org/wiki/Distance_%28math%C3%A9matiques%29) entre le
	 * point de coordonnées du noeud et celui du noeud goal.
	 * 
	 * @param goal
	 *            le noeud par rapport auquel qu'on trouve l'heuristique
	 * @return l'heuristique du noeud par rapport au noeud goal
	 * @throws StopRequestException 
	 */

	public int getHeuristic(SearchNode goal) throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire

		int result = 0;
		//somme des differences des coordonnees x et de y des noeuds
		result = result + Math.abs(x - goal.getX());
		result = result + Math.abs(y - goal.getY());
		return result;

	}

	/**
	 * Renvoie une representation textuelle du noeud
	 * 
	 * @return string les coordonnees du noeud et sa valeur sour forme de texte
	 */

	public String toString() {
		String resultat;
		resultat = x + "  " + y + "  " + valeur;
		return resultat;

	}

}
