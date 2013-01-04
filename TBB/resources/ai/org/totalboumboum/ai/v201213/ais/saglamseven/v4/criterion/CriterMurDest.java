package org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.SaglamSeven;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe est un simple exemple de 
 * critère entier. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que les bornes
 * du domaine de définition sont spécifiées dans
 * l'appel au constructeur ({@code super(nom,inf,sup)}).
 * 
 * @author Esra Sağlam
 * @author Cihan Adil Seven
 */
public class CriterMurDest extends AiUtilityCriterionInteger<SaglamSeven>
{	/** Nom de ce critère */
	public static final String NAME = "MURDESTRUCTIBLE";
	
	/**
	 * 
	 */
	public static final int MUR_LIMIT=3;
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterMurDest(SaglamSeven ai) throws StopRequestException
	{	super(ai,NAME,1,3);
		ai.checkInterruption();
		this.ai=ai;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/**
 * @param center
 * @return
 * ?
 * @throws StopRequestException
 */
public Set<AiTile> getThreatenedSoftwallTiles(AiTile center) throws StopRequestException
{	ai.checkInterruption();
	// init
	Set<AiTile> result = new TreeSet<AiTile>();
	AiHero ownHero = ai.getZone().getOwnHero();
	int range = ownHero.getBombRange();
	AiFire fire = ownHero.getBombPrototype().getFirePrototype();
	Direction[] direc = {Direction.DOWN,Direction.LEFT,Direction.RIGHT,Direction.UP};
	// on ne teste pas la case de la cible, on la considère comme ok
	
	// par contre, on teste celles situées à porté de bombes
	for(Direction d: direc)
	{	ai.checkInterruption();
		AiTile neighbor = center;
		int i = 1;
		boolean blocked = false;
		while(i<=range && !blocked)
		{	ai.checkInterruption();
			neighbor = neighbor.getNeighbor(d);
			blocked = !neighbor.isCrossableBy(fire);
			List<AiBlock> blocks = neighbor.getBlocks();
			if(!blocks.isEmpty())
			{	AiBlock block = blocks.get(0);
				// si le mur est destructible, on le met dans la liste
				if(block.isDestructible())
					result.add(neighbor);
			}
			i++;
		}
	}
	
	return result;
}
   
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		Set<AiTile> murs = getThreatenedSoftwallTiles(tile);
		if(murs.size()> MUR_LIMIT){
			return  MUR_LIMIT;
		}
		return murs.size();
	}
}
