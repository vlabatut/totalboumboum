package org.totalboumboum.ai.v201314.ais.cetinozel.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
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
public class Duree extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Duree";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Duree(Agent ai)
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
		boolean result = true;
		


		AiZone zone = tile.getZone();



		AiHero hero;
		List<AiBlock> murs;
		List<AiBomb> bombs;

		hero=zone.getOwnHero();
		int rangelimit=hero.getBombRangeLimit();
		int colum=tile.getCol();
		int line=tile.getRow();
		long timeBeforeExpo;
		long timeAfterExpo;
		for(int c=0;c<=rangelimit;c++)
		{		
				if((c+line)<zone.getHeight()-2)
				{	
					bombs=zone.getTile(c+line,colum).getBombs();
					murs=zone.getTile(c+line,colum).getBlocks();
					if(!murs.isEmpty())
						break;
					if(!bombs.isEmpty())
					{	
						if(bombs.get(0).getRange()>=c)	
						{	
							
							timeBeforeExpo=bombs.get(0).getNormalDuration()-bombs.get(0).getElapsedTime();
							timeAfterExpo=timeBeforeExpo+bombs.get(0).getBurningDuration();
							double temp=1000*(zone.getTileDistance(tile,zone.getTile(c+line, colum))*AiTile.getSize())/hero.getWalkingSpeed();
							if(temp>-1000+ timeBeforeExpo && temp <1000+timeAfterExpo)
							{
								result=false;
							}
						}
					}

				}
		}
		if(result)
		{
		for(int c=0;c<=rangelimit;c++)
		{	
			if((-c+line)>1)
			{
				bombs=zone.getTile(-c+line,colum).getBombs();
				murs=zone.getTile(-c+line,colum).getBlocks();
				if(!murs.isEmpty())
					break;
				if(!bombs.isEmpty())
				{	
					if(bombs.get(0).getRange()>=c)	
					{	
						
						timeBeforeExpo=bombs.get(0).getNormalDuration()-bombs.get(0).getElapsedTime();
						timeAfterExpo=timeBeforeExpo+bombs.get(0).getBurningDuration();
						double temp=1000*(zone.getTileDistance(tile,zone.getTile(-c+line, colum))*AiTile.getSize())/hero.getWalkingSpeed();
						if(temp>-1000+ timeBeforeExpo && temp <1000+timeAfterExpo)
						{
							result=false;
						}
					}
					
				}
			}
		}
		}
		if(result)
		{
		for(int d=0;d<=rangelimit;d++)
		{		
				if((d+colum)<zone.getWidth()-2)
				{	
					bombs=zone.getTile(line,d+colum).getBombs();
					murs=zone.getTile(line,colum+d).getBlocks();
					if(!murs.isEmpty())
						break;
					if(!bombs.isEmpty())
					{
						if(bombs.get(0).getRange()>=d)	
						{	
							
							timeBeforeExpo=bombs.get(0).getNormalDuration()-bombs.get(0).getElapsedTime();
							timeAfterExpo=timeBeforeExpo+bombs.get(0).getBurningDuration();
							double temp=1000*(zone.getTileDistance(tile,zone.getTile(line, d+colum))*AiTile.getSize())/hero.getWalkingSpeed();
							if(temp>-1000 + timeBeforeExpo && temp <1000+timeAfterExpo)
							{
								result=false;
							}
						}
					}
				}
		}
		}
		if(result)
		{
		for(int d=0;d<=rangelimit;d++)
		{	
			if((-d+colum)>1)
			{
				bombs=zone.getTile(line,-d+colum).getBombs();
				murs=zone.getTile(line,-d+colum).getBlocks();
				if(!murs.isEmpty())
					break;
				if(!bombs.isEmpty())
				{
				if(bombs.get(0).getRange()>=d)	
				{	
					
					timeBeforeExpo=bombs.get(0).getNormalDuration()-bombs.get(0).getElapsedTime();
					timeAfterExpo=timeBeforeExpo+bombs.get(0).getBurningDuration();
					double temp=1000*(zone.getTileDistance(tile,zone.getTile(line, colum-d))*AiTile.getSize())/hero.getWalkingSpeed();
					if(temp>-1000 + timeBeforeExpo && temp <1000+timeAfterExpo)
					{
						result=false;
					}
				}
				}
			}
		}
		}


		
		return result;
	}
}
