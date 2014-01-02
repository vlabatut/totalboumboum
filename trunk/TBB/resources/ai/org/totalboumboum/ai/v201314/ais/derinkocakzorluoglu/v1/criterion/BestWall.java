

package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v1.criterion;

//import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
//import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
//import org.totalboumboum.ai.v201314.adapter.data.AiFire;
//import org.totalboumboum.ai.v201314.adapter.data.AiHero;
//import org.totalboumboum.ai.v201314.adapter.data.AiHero;
//import org.totalboumboum.ai.v201314.adapter.data.AiSprite;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
//import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v1.Agent;
/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class BestWall extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "BestWall";
	
	/**
	 * Crée un nouveau critère binaire.
	 * On cherche la case qui est la plus proche a un ennemi inaccessible et
	 * qui est à côté des mur destructibles. Il existe toujours un MurMeilleur.
	 * Si deux ou plusieurs cases sont à la même distance à l’ennemi on prend
	 * une par hasard.
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public BestWall(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile)
	{	ai.checkInterruption();
	

	/* 
	 * ON est en train de construire cette critere . Il ne marche pas encore et c'est 
	 * pourquoi on ne l'a pas mis .
	 * 
	 */
	
	
           return tile.getFires().isEmpty();
           }
	}
