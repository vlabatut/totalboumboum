package org.totalboumboum.ai.v201314.ais.cetinozel.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.cetinozel.v1.Agent;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Hakan Çetin
 * @author Yiğit Özel
 */
@SuppressWarnings("deprecation")
public class Distance extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Distance";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Distance(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile)
	{	
		ai.checkInterruption();
		boolean result = false;
		AiHero hero;
		List<AiBlock> walls;

		AiZone zone = tile.getZone();
		hero=zone.getOwnHero();
		int range=hero.getBombRange();
		int colum=tile.getCol();
		int row=tile.getRow();
		AiTile tilee;
		List<AiHero> heros;
		heros=tile.getZone().getRemainingOpponents();
		for(int c=0;c<=range;c++)
		{		
				
				if((c+row)<zone.getHeight())
				{	
					tilee=zone.getTile(c+row, colum);
					walls=zone.getTile(c+row,colum).getBlocks();
					if(!walls.isEmpty())
						break;
					if(!heros.isEmpty())
					{
					for(int x=0;x<heros.size();x++)
					{
						if(zone.getTileDistance(heros.get(x).getTile(),tilee)<2)
						{	
							result=true;
							break;
						}
					}
					}
					if(result==true)	break;

				}
		}
		for(int c=0;c<=range;c++)
		{		
			
			if((-c+row)>0)
			{	
				tilee=zone.getTile(-c+row, colum);
				walls=zone.getTile(-c+row,colum).getBlocks();
				if(!walls.isEmpty())
					break;
				if(!heros.isEmpty())
				{
				for(int x=0;x<heros.size();x++)
				{
					if(zone.getTileDistance(heros.get(x).getTile(),tilee)<2)
					{	
						result=true;
						break;
					}
				}
				}
				if(result==true)	break;

			}
		}
		for(int d=0;d<=range;d++)
		{		
			
			if((d+colum)<zone.getWidth())
			{	
				tilee=zone.getTile(row, colum+d);
				walls=zone.getTile(row,colum+d).getBlocks();
				if(!walls.isEmpty())
					break;
				if(!heros.isEmpty())
				{
				for(int x=0;x<heros.size();x++)
				{
					if(zone.getTileDistance(heros.get(x).getTile(),tilee)<2)
					{	
						result=true;
						break;
					}
				}
				}
				if(result==true)	break;

			}
		}
		for(int d=0;d<=range;d++)
		{		
			
			if((-d+colum)>0)
			{	
				tilee=zone.getTile(row, colum-d);
				walls=zone.getTile(row,colum-d).getBlocks();
				if(!walls.isEmpty())
					break;
				if(!heros.isEmpty())
				{
				for(int x=0;x<heros.size();x++)
				{
					if(zone.getTileDistance(heros.get(x).getTile(),tilee)<2)
					{	
						result=true;
						break;
					}
				}
				}
				if(result==true)	break;

			}
		}
	
		return result;
	}
}
