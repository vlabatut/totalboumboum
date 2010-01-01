package org.totalboumboum.ai.v200910.ais.dereligeckalan.v4_2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;

public class Tree {
	private Vector<Noeud> nodes;
	// ensemble des liens composant l'arbre
	private Vector<SearchLink> links;

	// dernier noeud traité lors de la recherche
	@SuppressWarnings("unused")
	private Noeud lastNode;
	private Noeud firstNode;
	private DereliGeckalan source;
	
	public Tree(Noeud courant, DereliGeckalan source) throws StopRequestException {
		source.checkInterruption(); //Appel Obligatoire
		this.source = source;
		this.firstNode = courant;
		init();
}
	public void init() throws StopRequestException {
		source.checkInterruption(); //Appel Obligatoire
		nodes = new Vector<Noeud>();
		Noeud initialNode = new Noeud(this.firstNode.getX(), this.firstNode
				.getY(), ZoneEnum.CARACTERE, 0,source);
		nodes.add(initialNode);
		lastNode = initialNode;
		links = new Vector<SearchLink>();
	}

	/**
	 * Renvoie la racine de l'arbre
	 * 
	 * @return la racine de l'arbre de recherche
	 * @throws StopRequestException 
	 */
	public Noeud getRoot() throws StopRequestException {
		source.checkInterruption(); //Appel Obligatoire
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
	 */
	public void addNoeud(Noeud pere, Noeud fils) throws StopRequestException { // on cree un lien entre le
													// pere et le fils
		source.checkInterruption(); //Appel Obligatoire
		SearchLink link = new SearchLink(pere, fils,source);
		// on ajoute le lien à l'arbre,on appelle la methode addLink qui
		// lui-meme ajoute le noeud
		addLink(link);

	}

	/**
	 * Détermine si l'arbre contient le noeud passé en paramètre.
	 * 
	 * @param node
	 *            le noeud à rechercher
	 * @return vrai si l'arbre contient le noeud
	 * @throws StopRequestException 
	 */
	public synchronized boolean containsNode(Noeud node) throws StopRequestException {
		source.checkInterruption(); //Appel Obligatoire
		boolean result = false;
		Iterator<Noeud> i = nodes.iterator();
		while (i.hasNext() && !result)
		{
			source.checkInterruption(); //Appel Obligatoire
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
	 * @throws AbsentNodeException
	 */
	public SearchLink getParentLink(Noeud node) throws StopRequestException {
		source.checkInterruption(); //Appel Obligatoire
		SearchLink result = null;
		if (node != firstNode) {
			Iterator<SearchLink> i = links.iterator();
			while (i.hasNext() && result == null) {
				source.checkInterruption(); //Appel Obligatoire
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
	 * @throws AbsentNodeException
	 */
	public synchronized Vector<SearchLink> getChildrenLinks(Noeud node) throws StopRequestException {
		source.checkInterruption(); //Appel Obligatoire
		Vector<SearchLink> result = new Vector<SearchLink>();
		
		Iterator<SearchLink> i = links.iterator();
		while (i.hasNext()) {
			source.checkInterruption(); //Appel Obligatoire
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
	 */
	public LinkedList<Noeud> getPath(Noeud node) throws StopRequestException { // la liste des noeuds qui
													// se trouve sur le path
		source.checkInterruption(); //Appel Obligatoire
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
				source.checkInterruption(); //Appel Obligatoire
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
	 * Crée un nouveau lien dans l'arbre. Le noeud origin doit être présent dans
	 * le graphe. Le noeud target doit être absent de l'arbre. Si le noeud
	 * origin n'appartient pas au graphe. Si le noeud target appartient déjà au
	 * graphe.
	 * 
	 * @param link
	 *            le lien à rajouter dans l'arbre
	 * @throws StopRequestException 
	 */
	public void addLink(SearchLink link) throws StopRequestException {
		source.checkInterruption(); //Appel Obligatoire
		addLinkSynch(link);

	}

	/**
	 * Méthode utilisée par addLink pour des opérations devant être effectuées
	 * en mode synchronisé.
	 * 
	 * @param link
	 *            le lien à ajouter
	 * @throws StopRequestException 
	 */
	private synchronized void addLinkSynch(SearchLink link) throws StopRequestException {
		source.checkInterruption(); //Appel Obligatoire
		Noeud target = link.getTarget();
		links.add(link);
		nodes.add(target);
	}

}
