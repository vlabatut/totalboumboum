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
 * @author Eser Devecioglu
 * @author Alev Korkmaz
 *
 */
public class Tree {

	private Vector<Noeud> nodes;
	private Vector<SearchLink> links;
	@SuppressWarnings("unused")
	private Noeud lastNode;
	private Noeud firstNode;
	ArtificialIntelligence ai;
	
	public Tree(Noeud courant, ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.firstNode = courant;
		init();
	}

	public void init() throws StopRequestException {
		ai.checkInterruption();
		nodes = new Vector<Noeud>();
		Noeud initialNode = new Noeud(this.firstNode.getTile(),ai);
		nodes.add(initialNode);
		lastNode = initialNode;
		links = new Vector<SearchLink>();
	}

	public Noeud getRoot() throws StopRequestException {
		ai.checkInterruption();
		return nodes.get(0);
	}

	public void addNoeud(Noeud pere, Noeud fils) throws StopRequestException {
		ai.checkInterruption();
		SearchLink link = new SearchLink(pere, fils,ai);
		addLink(link);
	}

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

	public void addLink(SearchLink link) throws StopRequestException {
		ai.checkInterruption();
		addLinkSynch(link);

	}

	private synchronized void addLinkSynch(SearchLink link) throws StopRequestException {
		ai.checkInterruption();
		Noeud target = link.getTarget();
		links.add(link);
		nodes.add(target);
	}

}
