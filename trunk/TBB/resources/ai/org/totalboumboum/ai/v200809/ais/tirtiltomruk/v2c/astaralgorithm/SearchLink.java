package org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c.astaralgorithm;

import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c.TirtilTomruk;

/**
 * Représente un lien orienté dans un arbre de recherche.
 * Ce lien est défini par les deux noeuds reliés : l'origine (parent)
 * et la cible (fils) ainsi que par l'action à réaliser pour passer 
 * d'un état à l'autre.
 *
 * @author Abdullah Tırtıl
 * @author Mert Tomruk
 *
 */
public class SearchLink
{	// noeud parent
	private Noeud origin;
	// noeud fils
	private Noeud target;

	private TirtilTomruk source;
	/**
	 * Constructeur.
	 * 
	 * @param origin	noeud parent
	 * @param target	noeud fils
	 * @param action	action de transition
	 * @throws StopRequestException 
	 */
	public SearchLink(Noeud origin,Noeud target,TirtilTomruk source) throws StopRequestException
	{	
		source.checkInterruption(); //Appel Obligatoire
		this.source = source;
		this.origin = origin;
		this.target = target;
		
	}

	/**
	 * Renvoie le noeud parent du lien 
	 * @return	le noeud parent
	 * @throws StopRequestException 
	 * @throws StopRequestException 
	 */
	public Noeud getOrigin() throws StopRequestException
	{   
		source.checkInterruption(); // Appel Obligatoire
		return origin;
	}

	/**
	 * Renvoie le noeud fils du lien
	 * @return	le noeud fils
	 * @throws StopRequestException 
	 */
	public Noeud getTarget() throws StopRequestException
	{	source.checkInterruption(); //Appel Obligatoire
		return target;
	}

	
	

	/**
	 * Renvoie une valeur indiquant l'egalite de l'objet
	 * vrai si l'objet passé en parametre est egale à cet objet
	 * false sinon 
	 * @return result indique l'egalite	
	 */
	public boolean equals(Object object)
	{		boolean result = false;
		if(object == null)
			result = false;
		else if(!(object instanceof SearchLink))
			result = false;
		else
		{	SearchLink temp = (SearchLink) object;
			try {
				result = temp.getOrigin() == getOrigin();
			} catch (StopRequestException e) {
				// 
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
