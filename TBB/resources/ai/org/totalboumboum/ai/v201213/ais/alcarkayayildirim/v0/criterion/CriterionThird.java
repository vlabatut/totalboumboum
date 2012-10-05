package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v0.criterion;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionString;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v0.AlcarKayaYildirim;

/**
 * Cette classe représente est un simple exemple de 
 * critère chaîne de caractères. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que le domaine
 * de définition est spécifiés dans l'appel au constructeur 
 * ({@code super(nom,inf,sup)}).
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class CriterionThird extends AiUtilityCriterionString
{	/** Nom de ce critère */
	public static final String NAME = "THIRD";
	/** Valeur du domaine de définition */
	public static final String VALUE1 = "une valeur";
	/** Valeur du domaine de définition */
	public static final String VALUE2 = "une autre valeur";
	/** Valeur du domaine de définition */
	public static final String VALUE3 = "encore une autre";
	/** Valeur du domaine de définition */
	public static final String VALUE4 = "et puis une autre";
	/** Valeur du domaine de définition */
	public static final String VALUE5 = "et enfin une dernière";
	/** Domaine de définition */
	public static final Set<String> DOMAIN = new TreeSet<String>(Arrays.asList
	(	VALUE1,
		VALUE2,
		VALUE3,
		VALUE4,
		VALUE5
	));
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionThird(AlcarKayaYildirim ai) throws StopRequestException
	{	// init nom + bornes du domaine de définition
		super(NAME,DOMAIN);
		
		// init agent
		this.ai = ai;
	}

    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** L'agent associé au traitement */ 
	protected AlcarKayaYildirim ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String processValue(AiTile tile) throws StopRequestException
	{	String result = VALUE3;
		
		// TODO à compléter par le traitement approprié
		
		return result;
	}
}
