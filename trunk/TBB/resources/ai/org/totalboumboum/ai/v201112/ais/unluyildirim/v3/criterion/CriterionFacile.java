package org.totalboumboum.ai.v201112.ais.unluyildirim.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v3.UnluYildirim;

/**
 * Cette classe represente la methode processValue qui compte les murs voisins de le tile.
 *  
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
@SuppressWarnings("deprecation")
public class CriterionFacile extends AiUtilityCriterionBoolean
{	/** */
	public static final String NAME = "FACILE";
	
	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public CriterionFacile(UnluYildirim ai) throws StopRequestException
	{	
		super(NAME);
		ai.checkInterruption();
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	protected UnluYildirim ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = true;
	
	
		int number =0 ;
		
        AiHero myhero = ai.getZone().getOwnHero();
		AiTile tile_voisin = null ,tile_hero = null  ;
		AiZone zone = ai.getZone();
		int range = myhero.getBombRange();
		int k=-1 ,l=0;
		boolean result1 = false ;
		int i=1;
		
		
		//System.out.println("zone colon : "+zone.getWidth()+" row : "+zone.getHeight());
		while(i<5 && !result1)
		{
			ai.checkInterruption();
			int j=1;
			while(j<range+1 && !result1)
			{
				ai.checkInterruption();
				if(tile.getRow()+k*j<zone.getHeight() && tile.getCol()+l*j<zone.getWidth() && tile.getRow()+k*j>=0 && tile.getCol()+l*j>=0)
			    {
				    tile_voisin=zone.getTile(tile.getRow()+k*j,tile.getCol()+l*j);
			       // System.out.println("row : "+tile_voisin.getRow()+" col : "+tile_voisin.getCol());
		     	if(!tile_voisin.getHeroes().isEmpty())
			     	{
		     	     	ai.checkInterruption();
			     		tile_hero = tile_voisin;
			     		//System.out.println("true");
			     		result1=true;
				    }
			}
			j++;
			}
			k=k*k;
			l=l*l;
			if(i==2)
				{
				k=0;
				l=-1;
				}
			i++;
		}
		//System.out.println("fin");

		if(tile_hero!=null)
		{List<AiTile> hero_voisin = tile_hero.getNeighbors();
		for(AiTile tile1 : hero_voisin)
		{
			ai.checkInterruption();
			if(!tile1.getBlocks().isEmpty())
				number++;
		} }
	//System.out.println("number : "+number);
		
		
		if(number == 0 || number ==1)
		{
			result = false;
		}
		else
		{
			if(number == 2 || number == 3 )
			{
				result =true;
				
			}
		}
	
		return result;
	}
}
