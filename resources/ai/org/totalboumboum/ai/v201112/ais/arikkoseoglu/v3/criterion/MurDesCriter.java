package org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3.criterion;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3.ArikKoseoglu;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Furkan Arık
 * @author Aksel Köseoğlu
 */
@SuppressWarnings("deprecation")
public class MurDesCriter extends AiUtilityCriterionInteger
{	/** Nom de ce critère */
	public static final String NAME = "MURDESTRUCTIBLE";
	/** */
	public static final int MUR_LIMIT = 3;
	/**
	 * Crée un nouveau critère binaire.
	 * @param ai 
	 * 		?
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public MurDesCriter(ArikKoseoglu ai) throws StopRequestException
	{	// init nom
		super(NAME,0,MUR_LIMIT);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	protected ArikKoseoglu ai;

	/**
	 * Calcule combien de murs une bombe posée dans
	 * la case passée en paramètre menaçerait.
	 * 
	 * @param center
	 * 		Le centre de l'explosion à envisager.
	 * @return
	 * 		L'ensemble des cases contenant des murs touchés par l'explosion.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
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
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
