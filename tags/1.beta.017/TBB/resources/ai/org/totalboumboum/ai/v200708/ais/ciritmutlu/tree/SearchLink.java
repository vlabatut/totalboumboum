package org.totalboumboum.ai.v200708.ais.ciritmutlu.tree;

import org.totalboumboum.ai.v200708.ais.ciritmutlu.problem.Action;


/**
 * Represente un lien oriente dans un arbre de recherche.
 * Ce lien est defini par les deux noeuds relies : l'origine (parent)
 * et la cible (fils) ainsi que par l'action e realiser pour passer 
 * d'un etat e l'autre.
 */
public class SearchLink
{	// noeud parent
	private SearchNode origin;
	// noeud fils
	private SearchNode target;
	// action de transition
	private Action action;

	/**
	 * Constructeur.
	 * 
	 * @param origin	noeud parent
	 * @param target	noeud fils
	 * @param action	action de transition
	 */
	public SearchLink(SearchNode origin,SearchNode target, Action action)
	{	this.origin = origin;
		this.target = target;
		this.action = action;
	}

	/**
	 * Renvoie le noeud parent du lien 
	 * @return	le noeud parent
	 */
	public SearchNode getOrigin()
	{	return origin;
	}

	/**
	 * Renvoie le noeud fils du lien
	 * @return	le noeud fils
	 */
	public SearchNode getTarget()
	{	return target;
	}

	/**
	 * Renvoie l'action de transition
	 * @return	l'action associee au lien
	 */
	public Action getAction()
	{	return action;
	}

	public boolean equals(Object object)
	{	boolean result;
		if(object == null)
			result = false;
		else if(!(object instanceof SearchLink))
			result = false;
		else
		{	SearchLink temp = (SearchLink) object;
			result = temp.getOrigin() == getOrigin()
				&& temp.getTarget() == getTarget();
		}
		return result;
	}
}
