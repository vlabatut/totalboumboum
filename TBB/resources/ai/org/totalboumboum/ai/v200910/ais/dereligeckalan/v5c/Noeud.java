package org.totalboumboum.ai.v200910.ais.dereligeckalan.v5c;


import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;

public class Noeud {
	

		/** la coordonnee x du noeud */
		private int x;
		/** la coordonnee y du noeud */
		private int y;
		/** la valeur du noeud */
		private ZoneEnum valeur;
		/** le cout du noeud */
		private int cout = 0;

		private DereliGeckalan source;
		/**
		 * Constructeur.
		 * @throws StopRequestException 
		 * @throws org.totalboumboum.ai.v200910.adapter.communication.StopRequestException 
		 */

		public Noeud(DereliGeckalan source) throws StopRequestException{
			super();
			source.checkInterruption(); //Appel Obligatoire
			this.source = source;
			
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
		 * @throws org.totalboumboum.ai.v200910.adapter.communication.StopRequestException 
		 */
		public Noeud(int y, int x, ZoneEnum valeur, int cout,DereliGeckalan source) throws StopRequestException, org.totalboumboum.ai.v200910.adapter.communication.StopRequestException {
			super();
			source.checkInterruption(); //Appel Obligatoire
			this.source = source;
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
		public Noeud(int y, int x, ZoneEnum valeur, DereliGeckalan source) throws StopRequestException {
			super();
			source.checkInterruption(); //Appel Obligatoire
			this.source = source;
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
			source.checkInterruption(); //Appel Obligatoire
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
			source.checkInterruption(); //Appel Obligatoire
			this.x = x;
		}

		/**
		 * Renvoie le coordonne y du noeud
		 * 
		 * @return coordonne y
		 * @throws StopRequestException 
		 */
		public int getY() throws StopRequestException {
			source.checkInterruption(); //Appel Obligatoire
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
			source.checkInterruption(); //Appel Obligatoire
			this.y = y;
		}

		/**
		 * Renvoie la valeur du noeud
		 * 
		 * @return valeur
		 * @throws StopRequestException 
		 */
		public ZoneEnum getValeur() throws StopRequestException {
			source.checkInterruption(); //Appel Obligatoire
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
			source.checkInterruption(); //Appel Obligatoire
			this.valeur = valeur;
		}

		/**
		 * Renvoie le cout du noeud
		 * 
		 * @return cout
		 * @throws StopRequestException 
		 */

		public int getCout() throws StopRequestException {
			source.checkInterruption();
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
			source.checkInterruption();
			this.cout = cout;
		}

		/**
		 * Controle l'�galit� de l'objet à un autre objet
		 * 
		 * @param object
		 *            objet qu'on va comparer
		 * @return true si l'objet est �gal à l'objet pass� en parametre
		 */

		public boolean equals(Object object) {

			Noeud noeud;
			try {
				noeud = new Noeud(source);
			} catch (StopRequestException e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}
			noeud = (Noeud) object;
			try{
			if ((noeud.getX() == x) && (noeud.getY() == y))
				return true;

			else

				return false;
			}
			catch(StopRequestException e)
			{
				return false;
			}
		}

		/**
		 * Calcule l'heuristique du noeud par rapport à un autre noeud pass� en
		 * parametre Il fait le calcul en utilisant la distance du Manhattan * (cf. :
		 * http://fr.wikipedia.org/wiki/Distance_%28math%C3%A9matiques%29) entre le
		 * point de coordonnées du noeud et celui du noeud goal.
		 * 
		 * @param goal
		 *            le noeud par rapport auquel qu'on trouve l'heuristique
		 * @return l'heuristique du noeud par rapport au noeud goal
		 * @throws StopRequestException 
		 */

		public int getHeuristic(Noeud goal) throws StopRequestException {
			source.checkInterruption();
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
