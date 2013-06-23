package org.totalboumboum.ai.v200809.ais.medeniuluer.v2c;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * Représente un arbre de recherche. Les noeuds sont liés avec des SearchLink
 * orientes
 *
 * @author Ekin Medeni
 * @author Pınar Uluer
 *
 */
@SuppressWarnings("deprecation")
public class SearchTree {

	/** ensemble des noeuds dans l'arbre */
	private Vector<SearchNode> nodes;
	/** ensemble des liens dans l'arbre */
	private Vector<SearchLink> links;

	/** dernier noeud traité lors de la recherche */
	@SuppressWarnings("unused")
	private SearchNode lastNode;
	/** */
	private SearchNode firstNode;
	/** */
	private MedeniUluer mu;
	
	/**
	 * 
	 * @param courant
	 * 		Description manquante !
	 * @param mu
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public SearchTree(SearchNode courant,MedeniUluer mu ) throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		this.mu = mu;
		this.firstNode = courant;
		init();
	}

	/**
	 * Initialise l'arbre
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public void init() throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		nodes = new Vector<SearchNode>();
		SearchNode initialNode = new SearchNode(this.firstNode.getX(), this.firstNode.getY(), ZoneEnum.CARACTERE, 0,mu);
		nodes.add(initialNode);
		lastNode = initialNode;
		links = new Vector<SearchLink>();
	}

	/**
	 * Renvoie la racine de l'arbre
	 * 
	 * @return la racine de l'arbre de recherche
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public SearchNode getRoot() throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		return nodes.get(0);
	}

	/**
	 * Ajoute un noeud à l'arbre
	 * 
	 * @param pere
	 *            le pere du noeud qu'on va ajouter
	 * @param fils
	 *            le noeud qu'on ajoute
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public void addSearchNode(SearchNode pere, SearchNode fils) throws StopRequestException { // on cree un lien entre le
													// pere et le fils
		mu.checkInterruption(); //Appel Obligatoire
		SearchLink link = new SearchLink(pere, fils,mu);
		// on ajoute le lien à l'arbre,on appelle la methode addLink qui
		// lui-meme ajoute le noeud
		addLink(link);

	}

	/**
	 * détermine si l'arbre contient le noeud passé en paramètre.
	 * 
	 * @param node
	 *            le noeud à rechercher
	 * @return vrai si l'arbre contient le noeud
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public synchronized boolean containsNode(SearchNode node) throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		boolean result = false;
		Iterator<SearchNode> i = nodes.iterator();
		while (i.hasNext() && !result)
		{
			mu.checkInterruption(); //Appel Obligatoire
			result = node == i.next();
		}		
		return result;
	}

	/**
	 * Renvoie le lien liant le noeud passé en paramètre à son père.
	 * 
	 * @param node
	 *            le noeud à traiter
	 * @return un lien contenant le noeud en position target
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public SearchLink getParentLink(SearchNode node) throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		SearchLink result = null;
		if (node != firstNode) {
			Iterator<SearchLink> i = links.iterator();
			while (i.hasNext() && result == null) {
				mu.checkInterruption(); //Appel Obligatoire
				SearchLink temp = i.next();
				if (temp != null)
					if (temp.getTarget() == node)
						result = temp;
			}
		}
		return result;
	}

	/**
	 * Renvoie tous les liens partant du noeud passé en paramètre.
	 * 
	 * @param node
	 *            le noeud à traiter
	 * @return tous les liens où ce noeud est en position origin
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public synchronized Vector<SearchLink> getChildrenLinks(SearchNode node) throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		Vector<SearchLink> result = new Vector<SearchLink>();
		
		Iterator<SearchLink> i = links.iterator();
		while (i.hasNext()) {
			mu.checkInterruption(); //Appel Obligatoire
			SearchLink temp = i.next();
			if (temp.getOrigin() == node)
				result.add(temp);
		}
		return result;
	}

	/**
	 * Renvoie une séquence de noeuds représentant un chemin allant de la racine
	 * au noeud passé en paramètre.
	 * 
	 * @param node
	 *            le noeud à traiter
	 * @return un vecteur de noeuds représentant le chemin depuis la racine
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public LinkedList<SearchNode> getPath(SearchNode node) throws StopRequestException { // la liste des noeuds qui
													// se trouve sur le path
		mu.checkInterruption(); //Appel Obligatoire
		LinkedList<SearchNode> result = new LinkedList<SearchNode>();
		// on ajoute d'abord le noeud courant
		result.add(node);
		SearchNode pere = null;
		@SuppressWarnings("unused")
		SearchNode fils = null;
		SearchNode temp = null;
		if (node != firstNode)// si le noeud est different de la racine de
								// l'arbre
		{
			SearchLink parentLink = getParentLink(node);
			temp = node;
			while (temp != null)// on fait un boucle jusqu'a arriver au noeud
								// racie de l'arbre
			{
				mu.checkInterruption(); //Appel Obligatoire
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
	 * crée un nouveau lien dans l'arbre. Le noeud origin doit être prèsent dans
	 * le graphe. Le noeud target doit être absent de l'arbre. Si le noeud
	 * origin n'appartient pas au graphe. Si le noeud target appartient déjà au
	 * graphe.
	 * 
	 * @param link
	 *            le lien à rajouter dans l'arbre
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public void addLink(SearchLink link) throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		addLinkSynch(link);

	}

	/**
	 * méthode utilisée par addLink pour des opérations devant être effectuées
	 * en mode synchronisé.
	 * 
	 * @param link
	 *            le lien à ajouter
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	private synchronized void addLinkSynch(SearchLink link) throws StopRequestException {
		mu.checkInterruption(); //Appel Obligatoire
		SearchNode target = link.getTarget();
		links.add(link);
		nodes.add(target);
	}

}
