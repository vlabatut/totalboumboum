package org.totalboumboum.ai.v201213.ais.cinaryalcin.v1;

import java.util.ArrayList;
import java.util.Map;


import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Bekir Cınar
 * @author Deniz Yalçın
 */
public class BombHandler extends AiBombHandler<CinarYalcin>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected BombHandler(CinarYalcin ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
   	
    	// TODO à compléter
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
	
		// TODO à compléter
	
	AiMode ownMode = ai.getModeHandler().getMode();
	AiHero ownHero = ai.getZone().getOwnHero();
	AiTile ownTile = ownHero.getTile();
	boolean resultat = false;

	if(ownMode == AiMode.COLLECTING )
	{
	if((ownHero.getBombNumberCurrent()>0)&&(ownTile.getBombs().isEmpty()))
	{ if(ai.getSafeTiles(ownHero,ownTile,true).size()>1)//Burası sorun yaratabilir.
		{
		
			for(Direction direction : Direction.getPrimaryValues())
			{
				ai.checkInterruption();
				if(ai.controlOfBlocks(ownTile, direction)==true)
				{
					resultat = true;
					
				}
			}	
		}else resultat=false;
	}else resultat=false;
	}
	
	else // Collecte de değilse ataktadır yorumu yaptım sorun çıkarabilir.
	{
		if((ownHero.getBombNumberCurrent()>0)&&(ownTile.getBombs().isEmpty()))
			{
				if(ai.getSafeTiles(ownHero, ownTile, true).size()>1)//buraya da dikkat
				{
					ArrayList<AiTile> liste = new ArrayList<AiTile>();
					Map<AiTile, Float> hashmap ;
					hashmap = ai.getUtilityHandler().getUtilitiesByTile();
					
					for(AiTile deneme : hashmap.keySet())
					{
						if (this.ai.getUtilityHandler().getUtilitiesByTile().get(deneme) == 4) // adamın bize uzaklığı bunu değişkene atayabiliriz.
							liste.add(deneme);
						if(!liste.isEmpty())
							resultat=true;						
					}
					for(Direction direction : Direction.getPrimaryValues())
					{
						if (ai.getAnEnemyInMyRange(ownTile, direction, 0) == true)
							{
						ai.checkInterruption();
						ArrayList<AiTile> liste1 = new ArrayList<AiTile>();
					
						Map<AiTile,Float> hashmap1;
						hashmap1 = this.ai.getUtilityHandler().getUtilitiesByTile();
						for (AiTile currentTile : hashmap1.keySet()) 
						{
							if ((ai.getUtilityHandler()	.getUtilitiesByTile().get(currentTile)>=2)&&(ai.getUtilityHandler().getUtilitiesByTile().get(currentTile)<=ownHero.getBombRange()))
								//if te tanımlanmış sayılar var onlara bakmak lazım
							{
								liste1.add(currentTile);
							}
							if (liste.isEmpty() && !liste1.isEmpty())//Burayı kontrol etmek gerekecek
								resultat = true;
						}
						}
					}
				
				}
				else resultat=false;
			}
			else resultat=false;
		}
		
		return resultat;
	}


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// TODO à compléter, si vous voulez afficher quelque chose
	}
}