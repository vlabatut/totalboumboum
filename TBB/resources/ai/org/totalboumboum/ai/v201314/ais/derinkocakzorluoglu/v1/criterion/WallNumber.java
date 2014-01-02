package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v1.criterion;

//import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v1.Agent;

/**The criteria which defines the wall number which is adjacent with the case that we 
 * are looking at.
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class WallNumber extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "WallNumber";
	/** Domaine de définition  {0,1,2,3,4} */
	/**
	 * On regarde aux cases adjacents qui sont en haut ,en bas
	 * en gauche,et en droite de la case dont on calcule le critère 
	 * "Nombre des murs". On regarde a la nombre des choses bloquant 
	 * ce qui trouve dans ce 4 case.
	 * Si il y n'a pas une bombe , ou un mur dans ces cases la on prend
	 * 0 comme valeur . Si seulement l'un des eux contient une bombe ou un mur 
	 * il va être un . En suivant la même logique ,il peut prendre les valeurs
	 * 2,3,4 aussi. 
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public WallNumber(Agent ai)
	{	super(ai,NAME,0,4);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile)
	{	ai.checkInterruption();
		int result=0;
		
	
		

		List<AiTile> m=tile.getNeighbors();
		
	
		for(int i=0;i<4;i++){
			ai.checkInterruption();
			if(m.get(i).getBlocks().isEmpty())
			{
				result++;
			}
		}
			
	
	
			return result;
		
	}
}
