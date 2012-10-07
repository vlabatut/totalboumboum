package org.totalboumboum.ai.v200809.ais.medeniuluer.v2;

import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.ai.v200809.ais.medeniuluer.v2.MedeniUluer;

/**
 * Représente un lien orienté dans un arbre de recherche.
 * Ce lien est défini par les deux noeuds reliés : l'origine (parent)
 * et la cible (fils) ainsi que par l'action à réaliser pour passer 
 * d'un état à l'autre.
 *
 * @author Ekin Medeni
 * @author Pınar Uluer
 *
 */
@SuppressWarnings("deprecation")
public class SearchLink
{	/** noeud parent */
	private SearchNode origin;
	/** noeud fils */
	private SearchNode target;

	/** */
	private MedeniUluer mu;
	
	/**
	 * Constructeur.
	 * 
	 * @param origin	noeud parent
	 * @param target	noeud fils
	 * @param mu	action de transition
	 * @throws StopRequestException 
	 */
	public SearchLink(SearchNode origin,SearchNode target,MedeniUluer mu) throws StopRequestException
	{	
		mu.checkInterruption(); //Appel Obligatoire
		this.mu = mu;
		this.origin = origin;
		this.target = target;
		
	}

	/**
	 * Renvoie le noeud parent du lien 
	 * @return	le noeud parent
	 */
	public SearchNode getOrigin()
	{   
		return origin;
	}

	/**
	 * Renvoie le noeud fils du lien
	 * @return	le noeud fils
	 * @throws StopRequestException 
	 */
	public SearchNode getTarget() throws StopRequestException
	{	mu.checkInterruption(); //Appel Obligatoire
		return target;
	}	

	/**
	 * Renvoie une valeur indiquant l'egalite de l'objet
	 * vrai si l'objet passé en parametre est egale à cet objet
	 * false sinon 
	 * @return result indique l'egalite	
	 */
	public boolean equals(Object object)
	{		boolean result;
		if(object == null)
			result = false;
		else if(!(object instanceof SearchLink))
			result = false;
		else
		{	SearchLink temp = (SearchLink) object;
			result = temp.getOrigin() == getOrigin();
		}
		return result;
	}
	
}
