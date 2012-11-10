package org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v3.criterion;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v3.CaliskanGeckalanSeven;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe est un simple exemple de 
 * critère entier. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que les bornes
 * du domaine de définition sont spécifiées dans
 * l'appel au constructeur ({@code super(nom,inf,sup)}).
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 * @author Cihan Seven
 */
@SuppressWarnings("deprecation")
public class PertinentMurs extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "PERTINENTMURS";
	
	/**
	 * Crée un nouveau critère entier.
	 * @param ai 
	 * 		?
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public PertinentMurs(CaliskanGeckalanSeven ai) throws StopRequestException
	{	// init nom + bornes du domaine de définition
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;

	}

    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	protected CaliskanGeckalanSeven ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result =false;
		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();
		int range = ownHero.getBombRange();
		int k = 0;
		boolean up= false;
		boolean down= false;
		boolean right= false;
		boolean left= false;

		while(k<range && !up)
		{
			ai.checkInterruption();
			AiTile  tempTile = tile.getNeighbor(Direction.UP);
			Collection<AiBlock> block = tempTile.getBlocks(); 
			Iterator<AiBlock> it = block.iterator();
			if(it.hasNext()) {
				ai.checkInterruption();
				if(it.next().isDestructible())
					result= true;
				up = true;
			}
			k++;
		}
		k=0;
		while(k<range && !down)
		{
			ai.checkInterruption();
			AiTile  tempTile = tile.getNeighbor(Direction.DOWN);
			Collection<AiBlock> block = tempTile.getBlocks(); 
			Iterator<AiBlock> it = block.iterator();
			if(it.hasNext()) {
				ai.checkInterruption();
				if(it.next().isDestructible())
					result = true;
				down = true;
			}
			k++;
		}
		k=0;
		while(k<range && !left)
		{
			ai.checkInterruption();
			AiTile  tempTile = tile.getNeighbor(Direction.LEFT);
			Collection<AiBlock> block = tempTile.getBlocks(); 
			Iterator<AiBlock> it = block.iterator();
			if(it.hasNext()) {
				ai.checkInterruption();
				if(it.next().isDestructible())
					result = true;
				left = true;
			}
			k++;
		}
		
		k=0;
		while(k<range && !right)
		{
			ai.checkInterruption();
			AiTile  tempTile = tile.getNeighbor(Direction.RIGHT);
			Collection<AiBlock> block = tempTile.getBlocks(); 
			Iterator<AiBlock> it = block.iterator();
			if(it.hasNext()) {
				ai.checkInterruption();
				if(it.next().isDestructible())
					result = true;
				right = true;
			}
			k++;
		}
		k=0;
		return result;
	}
	
	
	
}
