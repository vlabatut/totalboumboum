package org.totalboumboum.ai.v200910.ais.dereligeckalan.v5;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;

public class SearchLink {
	private Noeud origin;
	// noeud fils
	private Noeud target;

	private DereliGeckalan source;
	/**
	 * Constructeur.
	 * 
	 * @param origin	noeud parent
	 * @param target	noeud fils
	 * @param action	action de transition
	 * @throws StopRequestException 
	 */
	public SearchLink(Noeud origin,Noeud target,DereliGeckalan source) throws StopRequestException
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
		source.checkInterruption();
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
	 * vrai si l'objet pass� en parametre est egale � cet objet
	 * false sinon 
	 * @return result indique l'egalite	
	 */
	public boolean equals(Object object)
	{		boolean result=false;
		if(object == null)
			result = false;
		else if(!(object instanceof SearchLink))
			result = false;
		else
		{	SearchLink temp = (SearchLink) object;
			try {
				result = temp.getOrigin() == getOrigin();
			} catch (StopRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
