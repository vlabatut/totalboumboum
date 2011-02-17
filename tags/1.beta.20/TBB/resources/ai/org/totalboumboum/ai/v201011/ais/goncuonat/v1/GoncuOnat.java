package org.totalboumboum.ai.v201011.ais.goncuonat.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.totalboumboum.ai.v201011.adapter.data.*;
//import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

@SuppressWarnings("unused")
public class GoncuOnat extends ArtificialIntelligence
{	
	private final int CASE_EMPTY= 2;
	private final int CASE_SOFTWALL = 1;
	private final int CASE_HARDWALL = -1;
	final int CASE_BONUS = 5;
	private final int CASE_DANGER = 5; // danger= bombe,feu, adversaires
	private AiHero ourHero;
	private AiZone zone;
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		AiAction result;
		//ourHero = zone.getOwnHero();
		
		result = new AiAction(AiActionName.NONE);
		return result;
	}
	
	public void MatriceCollecte (int [][] matriceCollecte, AiZone zone)throws StopRequestException
	{
		checkInterruption();
		int height=zone.getHeight();
		int width=zone.getHeight();
		for(int i=0; i<height; i++)
		{	
			checkInterruption();
			for(int j=0; j<width; j++)
			{
				checkInterruption();
				
				matriceCollecte[i][j] = CASE_EMPTY;
			}
		}
		
		for(int line=0;line<height;line++)
		{	checkInterruption(); //APPEL OBLIGATOIRE
			for(int col=0;col<width;col++)
			{	
				checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(line, col);
				List<AiFire> fires = tile.getFires();
				List<AiBomb> bombs = tile.getBombs();
				List<AiItem> item = tile.getItems();
				List<AiBlock> wall = tile.getBlocks();
				
				if(!fires.isEmpty())
				{	matriceCollecte[line][col] = CASE_DANGER;				
				}
				if(!bombs.isEmpty())
				{	matriceCollecte[line][col] = CASE_DANGER;				
				}
				if(((AiBlock) wall).isDestructible())
				{	matriceCollecte[line][col] = CASE_SOFTWALL;				
				}
				if(!((AiBlock) wall).isDestructible())
				{	matriceCollecte[line][col] = CASE_HARDWALL;				
				}
				if(!item.isEmpty())
				{	matriceCollecte[line][col] = CASE_BONUS;
					
				}
			
			}
		}
			
			
	}
/*		
	public void closestBonus(List<AiItem> bonus, AiZone zone ) throws StopRequestException
	{
		checkInterruption();
		int height=zone.getHeight();
		int width=zone.getHeight();
		for(int line=0;line<height;line++)
		{	checkInterruption(); //APPEL OBLIGATOIRE
			for(int col=0;col<width;col++)
			{	
				AiTile tile = zone.getTile(line, col);
				List<AiItem> bonusDistance= tile.getItems();
				int bonuslist = getTileDistance(ourHero.getTile(),tile);
				bonusDistance.add(bonuslist);
			}
		}
	}
	*/
}
