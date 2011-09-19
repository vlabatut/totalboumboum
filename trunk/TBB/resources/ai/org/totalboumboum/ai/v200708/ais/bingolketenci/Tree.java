package org.totalboumboum.ai.v200708.ais.bingolketenci;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

/**
 * Repr�sente un arbre de recherche. Les noeuds sont li�s avec des SearchLink
 * orientes
 * 
 * @author Gizem Bingol
 * @author Utku Gorkem Kentenci
 *
 */
public class Tree {

	// ensemble des noeuds composant l'arbre
	private Vector<Noeud> nodes;
	// ensemble des liens composant l'arbre
	private Vector<SearchLink> links;

	// dernier noeud trait� lors de la recherche
	@SuppressWarnings("unused")
	private Noeud lastNode;
	private Noeud firstNode;

	public Tree(Noeud courant) {
		this.firstNode = courant;
		init();
	}

	/**
	 * Initialise l'arbre (� faire avant de commencer une recherche)
	 */
	public void init() {
		nodes = new Vector<Noeud>();
		Noeud initialNode = new Noeud(this.firstNode.getX(), this.firstNode
				.getY(), 0);
		nodes.add(initialNode);
		lastNode = initialNode;
		links = new Vector<SearchLink>();

	}

	/**
	 * Renvoie la racine de l'arbre
	 * 
	 * @return la racine de l'arbre de recherche
	 */
	public Noeud getRoot() {
		return nodes.get(0);
	}

	/**
	 * Ajoute un noeud à l'arbre
	 * 
	 * @param pere
	 *            le pere du noeud qu'on va ajouter
	 * @param fils
	 *            le noeud qu'on ajoute
	 */
	public void addNoeud(Noeud pere, Noeud fils) { // on cree un lien entre le
													// pere et le fils
		SearchLink link = new SearchLink(pere, fils);
		// on ajoute le lien à l'arbre,on appelle la methode addLink qui
		// lui-meme ajoute le noeud
		addLink(link);

	}

	/**
	 * D�termine si l'arbre contient le noeud pass� en param�tre.
	 * 
	 * @param node
	 *            le noeud à rechercher
	 * @return vrai si l'arbre contient le noeud
	 */
	public synchronized boolean containsNode(Noeud node) {
		boolean result = false;
		Iterator<Noeud> i = nodes.iterator();
		while (i.hasNext() && !result)
			result = node == i.next();
		return result;
	}

	/**
	 * Renvoie le lien liant le noeud pass� en param�tre à son p�re.
	 * 
	 * @param node
	 *            le noeud à traiter
	 * @return un lien contenant le noeud en position target
	 * @throws AbsentNodeException
	 */
	public SearchLink getParentLink(Noeud node) {
		SearchLink result = null;
		if (node != firstNode) {
			Iterator<SearchLink> i = links.iterator();
			while (i.hasNext() && result == null) {
				SearchLink temp = i.next();
				if (temp != null)
					if (temp.getTarget() == node)
						result = temp;
			}
		}
		return result;
	}

	/**
	 * Renvoie tous les liens partant du noeud pass� en param�tre.
	 * 
	 * @param node
	 *            le noeud à traiter
	 * @return tous les liens où ce noeud est en position origin
	 * @throws AbsentNodeException
	 */
	public synchronized Vector<SearchLink> getChildrenLinks(Noeud node) {
		Vector<SearchLink> result = new Vector<SearchLink>();

		Iterator<SearchLink> i = links.iterator();
		while (i.hasNext()) {
			SearchLink temp = i.next();
			if (temp.getOrigin() == node)
				result.add(temp);
		}
		return result;
	}

	/**
	 * Renvoie une s�quence de noeuds repr�sentant un chemin allant de la racine
	 * au noeud pass� en param�tre.
	 * 
	 * @param node
	 *            le noeud à traiter
	 * @return un vecteur de noeuds repr�sentant le chemin depuis la racine
	 */
	public LinkedList<Noeud> getPath(Noeud node) { // la liste des noeuds qui
													// se trouve sur le path
		LinkedList<Noeud> result = new LinkedList<Noeud>();
		// on ajoute d'abord le noeud courant
		result.add(node);
		Noeud pere = null;
		@SuppressWarnings("unused")
		Noeud fils = null;
		Noeud temp = null;
		if (node != firstNode)// si le noeud est different de la racine de
								// l'arbre
		{
			SearchLink parentLink = getParentLink(node);
			temp = node;
			while (temp != null)// on fait un boucle jusqu'a arriver au noeud
								// racie de l'arbre
			{
				pere = parentLink.getOrigin();
				fils = parentLink.getTarget();
				result.add(pere);// on ajoute chaque fois le pere
				parentLink = getParentLink(pere);
				if (parentLink != null)
					temp = parentLink.getOrigin();
				else
					temp = null;

			}
		}
		return result;
	}

	/**
	 * crée un nouveau lien dans l'arbre. Le noeud origin doit �tre prèsent dans
	 * le graphe. Le noeud target doit �tre absent de l'arbre. Si le noeud
	 * origin n'appartient pas au graphe. Si le noeud target appartient d�j� au
	 * graphe.
	 * 
	 * @param link
	 *            le lien à rajouter dans l'arbre
	 */
	public void addLink(SearchLink link) {
		addLinkSynch(link);

	}

	/**
	 * méthode utilisée par addLink pour des op�rations devant �tre effectuées
	 * en mode synchronis�.
	 * 
	 * @param link
	 *            le lien à ajouter
	 */
	private synchronized void addLinkSynch(SearchLink link) {
		Noeud target = link.getTarget();
		links.add(link);
		nodes.add(target);
	}

}
