package org.totalboumboum.ai.v201213.ais.kartturgut.v4.criterion;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.kartturgut.v4.KartTurgut;

/**
 * @author Yunus Kart
 * @author Siyabend Turgut
 */
@SuppressWarnings("deprecation")
public class Securite extends AiUtilityCriterionBoolean<KartTurgut>
{	/** */
	public static final String	NAME	= "Securite";
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Securite( KartTurgut ai ) throws StopRequestException
	{	super( ai,NAME );
		ai.checkInterruption();
	}

    @Override
	public Boolean processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		boolean resultat = false;

		List<AiBomb> bombes = this.ai.getZone().getBombs();
		Iterator<AiBomb> Bombs = bombes.iterator();
		
		AiBomb bombe;
		while(Bombs.hasNext() && !resultat){
			ai.checkInterruption();
			bombe = Bombs.next();
			if(bombe.getCol() == tile.getCol() && Math.abs(bombe.getCol() - tile.getCol()) < bombe.getRange()+1)
				resultat = true;
			if(bombe.getRow() == tile.getRow() && Math.abs(bombe.getRow() - tile.getRow()) < bombe.getRange()+1)
				resultat = true;
		}
		if(!tile.getFires().isEmpty())
			resultat = true;
		return resultat;
	}
}
