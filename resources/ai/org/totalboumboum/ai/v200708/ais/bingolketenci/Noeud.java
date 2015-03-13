package org.totalboumboum.ai.v200708.ais.bingolketenci;


/**
 * Représente un noeud dans un arbre de recherche.
 * Chaque noeud est defini avec les coordonnees x et y
 * chaque noeud possede aussi une valeur entiere 
 * et un cout.
 * 
 * @author Gizem Bingöl
 * @author Utku Görkem Kentenci
 *
 */
public class Noeud {

	/** la coordonnee x du noeud */
	private int x;
	/** la coordonnee y du noeud */
	private int y;
	/** la valeur du noeud */
	private int valeur;
	/** le cout du noeud */
	private int cout = 0;

	/**
	 * Constructeur.
	 */

	public Noeud() {
		super();
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
	 */
	public Noeud(int x, int y, int valeur, int cout) {
		super();
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
	 */
	public Noeud(int x, int y, int valeur) {
		super();
		this.x = x;
		this.y = y;
		this.valeur = valeur;
	}

	/**
	 * Renvoie le coordonne x du noeud
	 * 
	 * @return coordonne x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Change le coordonne x du noeud
	 * 
	 * @param x
	 *            coordonne x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Renvoie le coordonne y du noeud
	 * 
	 * @return coordonne y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Change le coordonne y du noeud
	 * 
	 * @param y
	 *            coordonne y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Renvoie la valeur du noeud
	 * 
	 * @return valeur
	 */
	public int getValeur() {
		return valeur;
	}

	/**
	 * Change la valeur du noeud
	 * 
	 * @param valeur
	 *            valeur du noeud
	 */
	public void setValeur(int valeur) {
		this.valeur = valeur;
	}

	/**
	 * Renvoie le cout du noeud
	 * 
	 * @return cout
	 */

	public int getCout() {
		return cout;
	}

	/**
	 * Change le cout du noeud
	 * 
	 * @param cout
	 *            cout du noeud
	 */
	public void setCout(int cout) {
		this.cout = cout;
	}

	/**
	 * Controle l'égalité de l'objet à un autre objet
	 * 
	 * @param object
	 *            objet qu'on va comparer
	 * @return true si l'objet est égal à l'objet passé en parametre
	 */
	@Override
	public boolean equals(Object object) {

		Noeud noeud = new Noeud();
		noeud = (Noeud) object;
		if ((noeud.getX() == x) && (noeud.getY() == y))
			return true;

		else

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
	 */

	public int getHeuristic(Noeud goal) {

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

	@Override
	public String toString() {
		String resultat;
		resultat = x + "  " + y + "  " + valeur;
		return resultat;

	}

}
