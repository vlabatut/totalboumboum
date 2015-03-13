package org.totalboumboum.ai.v200809.ais.devecioglukorkmaz.v2c;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * Représente un arbre de recherche. Les noeuds sont liés avec des SearchLink
 * orientes
 * 
 * @author Eser Devecioğlu
 * @author lev Korkmaz
 *
 */
@SuppressWarnings("deprecation")
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
	/** */
	ArtificialIntelligence ai;
	
	/**
	 * 
	 * @param courant
	 * 		Description manquante !
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Tree(Noeud courant, ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.firstNode = courant;
		init();
	}

	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void init() throws StopRequestException {
		ai.checkInterruption();
		nodes = new Vector<Noeud>();
		Noeud initialNode = new Noeud(this.firstNode.getTile(),ai);
		nodes.add(initialNode);
		lastNode = initialNode;
		links = new Vector<SearchLink>();
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Noeud getRoot() throws StopRequestException {
		ai.checkInterruption();
		return nodes.get(0);
	}

	/**
	 * 
	 * @param pere
	 * 		Description manquante !
	 * @param fils
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void addNoeud(Noeud pere, Noeud fils) throws StopRequestException {
		ai.checkInterruption();
		SearchLink link = new SearchLink(pere, fils,ai);
		addLink(link);
	}

	/**
	 * 
	 * @param node
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public synchronized boolean containsNode(Noeud node) throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;
		Iterator<Noeud> i = nodes.iterator();
		while (i.hasNext() && !result)
		{	ai.checkInterruption();
			result = node == i.next();		
		}
		return result;
	}

	/**
	 * 
	 * @param node
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public SearchLink getParentLink(Noeud node) throws StopRequestException {
		ai.checkInterruption();
		SearchLink result = null;
		if (node != firstNode) {
			Iterator<SearchLink> i = links.iterator();
			while (i.hasNext() && result == null) {
				ai.checkInterruption();
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
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public synchronized Vector<SearchLink> getChildrenLinks(Noeud node) throws StopRequestException {
		ai.checkInterruption();
		
		Vector<SearchLink> result = new Vector<SearchLink>();

		Iterator<SearchLink> i = links.iterator();
		while (i.hasNext()) {
			ai.checkInterruption();
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
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public LinkedList<Noeud> getPath(Noeud node) throws StopRequestException {
		ai.checkInterruption();
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
				ai.checkInterruption();
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
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void addLink(SearchLink link) throws StopRequestException {
		ai.checkInterruption();
		addLinkSynch(link);

	}

	/**
	 * 
	 * @param link
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private synchronized void addLinkSynch(SearchLink link) throws StopRequestException {
		ai.checkInterruption();
		Noeud target = link.getTarget();
		links.add(link);
		nodes.add(target);
	}

}
