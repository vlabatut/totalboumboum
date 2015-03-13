package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4.criterion;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4.Agent;

/**Dans cette critere, si il y a quelque menace dans une case, donc la valeur de securité va etre false.
 * Sinon, la valeur retourne true. Ici on veut explique le menace. 
 * S'il y a blast de bombe qui peut tuer notre agent apres explosion, alors notre agent est en cas menace.
 * S'il y a une flame de bombe qui peut tuer notre agent directement, alors notre agent ne veut pas entrer a cette case.
 * S'il y a un adversaire "contagious" dans cette case, alors cette case est danger pour notre agent.
 * Donc si il y a menace, la valeur de securite retourne false. Sinon elle retourne true.
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
@SuppressWarnings("deprecation")
public class Securite extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Securite";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Securite(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile){	
		ai.checkInterruption();
		ArrayList<AiTile> danger =new ArrayList<AiTile>();
		danger.addAll(ai.dangereousTiles());
		if(!danger.isEmpty()) {
			if(danger.contains(tile))
				return false;
		}		
		return true;		
	}
}
