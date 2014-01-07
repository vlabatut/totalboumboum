package org.totalboumboum.ai.v201314.ais.cetinozel.v1.criterion;


import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
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
public class DeclencherUneAutreBombe extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DeclencherUneAutreBombe";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public DeclencherUneAutreBombe(Agent ai)
	{	super(ai,NAME, 0, 2);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile)
	{	
		ai.checkInterruption();
		int result=0;
		AiHero hero;
		List<AiBlock> walls;
		List<AiBomb> bombs;
		AiZone area = tile.getZone();
		hero=area.getOwnHero();
		int rangelimit=hero.getBombRangeLimit();
		int colum=tile.getCol();
		int row=tile.getRow();
		for(int c=0;c<=rangelimit;c++)
		{		
				if((c+row)<area.getHeight()-2)
				{	
					bombs=area.getTile(c+row,colum).getBombs();
					walls=area.getTile(c+row,colum).getBlocks();
					if(!walls.isEmpty())
						break;
					if(!bombs.isEmpty())
					{	
						
						if(bombs.get(0).getRange()>c)	
							result++;
							
					}	

				}
		}
		for(int c=0;c<=rangelimit;c++)
		{	
			if((-c+row)>1)
			{
				bombs=area.getTile(-c+row,colum).getBombs();
				walls=area.getTile(-c+row,colum).getBlocks();
				if(!walls.isEmpty())
					break;
				if(!bombs.isEmpty())
				{	
					if(bombs.get(0).getRange()>c)	
							result++;
					
				}
			}
		}
		for(int d=0;d<=rangelimit;d++)
		{		
				if((d+colum)<area.getWidth()-2)
				{	
					bombs=area.getTile(row,d+colum).getBombs();
					walls=area.getTile(row,colum+d).getBlocks();
					if(!walls.isEmpty())
						break;
					if(!bombs.isEmpty())
					{
						if(bombs.get(0).getRange()>d)	
							result++;
					}
				}
		}
		for(int d=0;d<=rangelimit;d++)
		{	
			if((-d+colum)>1)
			{
				bombs=area.getTile(row,colum-d).getBombs();
				walls=area.getTile(row,colum-d).getBlocks();
				if(!walls.isEmpty())
					break;
				if(!bombs.isEmpty())
				{
				if(bombs.get(0).getRange()>d)	
						result++;
				}
			}
		}
		if(result>2)	 result=2;
		return result;
}
}

