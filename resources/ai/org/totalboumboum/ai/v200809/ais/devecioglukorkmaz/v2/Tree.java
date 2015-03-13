package org.totalboumboum.ai.v200809.ais.devecioglukorkmaz.v2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

/**
 * Représente un arbre de recherche. Les noeuds sont liés avec des SearchLink
 * orientes
 * 
 * @author Eser Devecioğlu
 * @author lev Korkmaz
 *
 */
public class Tree {

	/** */
	private Vector<Noeud> nodes;
	/** */
	private Vector<SearchLink> links;
	/** */
	@SuppressWarnings("unused")
	private Noeud lastNode;
	/** */
	private Noeud firstNode;

	/**
	 * 
	 * @param courant
	 * 		Description manquante !
	 */
	public Tree(Noeud courant) {
		this.firstNode = courant;
		init();
	}

	/**
	 * 
	 */
	public void init() {
		nodes = new Vector<Noeud>();
		Noeud initialNode = new Noeud(this.firstNode.getTile());
		nodes.add(initialNode);
		lastNode = initialNode;
		links = new Vector<SearchLink>();
	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	public Noeud getRoot() {
		return nodes.get(0);
	}
	
	/**
	 * 
	 * @param pere
	 * 		Description manquante !
	 * @param fils
	 * 		Description manquante !
	 */
	public void addNoeud(Noeud pere, Noeud fils) {
		SearchLink link = new SearchLink(pere, fils);
		addLink(link);
	}

	/**
	 * 
	 * @param node
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public synchronized boolean containsNode(Noeud node) {
		boolean result = false;
		Iterator<Noeud> i = nodes.iterator();
		while (i.hasNext() && !result)
			result = node == i.next();
		return result;
	}

	/**
	 * 
	 * @param node
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
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
	 * 
	 * @param node
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
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
	 * 
	 * @param node
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public LinkedList<Noeud> getPath(Noeud node) {
		LinkedList<Noeud> result = new LinkedList<Noeud>();
		result.add(node);
		Noeud pere = null;
		@SuppressWarnings("unused")
		Noeud fils = null;
		Noeud temp = null;
		if (node != firstNode) {
			SearchLink parentLink = getParentLink(node);
			temp = node;
			while (temp != null) {
				pere = parentLink.getOrigin();
				fils = parentLink.getTarget();
				result.add(pere);
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
	 * 
	 * @param link
	 * 		Description manquante !
	 */
	public void addLink(SearchLink link) {
		addLinkSynch(link);

	}

	/**
	 * 
	 * @param link
	 * 		Description manquante !
	 */
	private synchronized void addLinkSynch(SearchLink link) {
		Noeud target = link.getTarget();
		links.add(link);
		nodes.add(target);
	}

}
