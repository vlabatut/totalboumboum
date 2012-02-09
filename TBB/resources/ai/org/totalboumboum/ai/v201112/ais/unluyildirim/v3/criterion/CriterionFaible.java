package org.totalboumboum.ai.v201112.ais.unluyildirim.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v3.UnluYildirim;

/**
 * Cette classe represente une method qui renvoie la valeur true si le tile possede le plus faible adversaire
 * 
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
@SuppressWarnings("deprecation")
public class CriterionFaible extends AiUtilityCriterionBoolean
{	
	public static final String NAME = "FAIBLE";
	
	public CriterionFaible(UnluYildirim ai) throws StopRequestException
	{	
		super(NAME);
		ai.checkInterruption();
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected UnluYildirim ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;
	
		AiZone zone ;
		AiHero myhero ;
		zone = ai.getZone();
		myhero=  zone.getOwnHero();
		Boolean result1 = false ;
		int range = myhero.getBombRange();
		AiTile tile_voisin;
		AiTile tile_hero = null ;
		int k=-1 ,l=0;
		int i=1;
		
		while(i<5 && !result1)//Il regarde tous les tiles voisins du tile pour trouver l'adversaire
		{
			ai.checkInterruption();
			int j=1;
			while(j<range+1 && !result1)
			{
				ai.checkInterruption();
				if(tile.getRow()+k*j<zone.getHeight() && tile.getCol()+l*j<zone.getWidth() && tile.getRow()+k*j>=0 && tile.getCol()+l*j>=0)
			    {
					
					tile_voisin=zone.getTile(tile.getRow()+k*j,tile.getCol()+l*j);
			     
		     	if(!tile_voisin.getHeroes().isEmpty())
			     	{
			     		tile_hero = tile_voisin;
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
		
		//Si le tile est proche de l'adversaire plus faible dans la zone , il retourne true
		if(tile_hero!=null)
			{if(tile_hero.getHeroes().get(0).getRoundRank()<=myhero.getRoundRank())
				{
				//System.out.println("plus faible que notre hero");
				result= true;
				return result ;
				}
		
			}
		return result;
	}
}
