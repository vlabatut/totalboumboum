package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v1;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;

/**
 * 
 * @author Osman Demirci
 * @author Mustafa Göktuğ Düzok
 * @author Hatice Esra Ergök
 * 
 */
@SuppressWarnings("deprecation")
public class Zone_IA {	
	
	@SuppressWarnings("unused")
	private AiZone zone_IA;
	private Collection<AiHero> ennemies;
	private AiHero bomberman;
	private Collection<AiBomb> bombes;
	private Collection<AiBlock> murs;
	@SuppressWarnings("unused")
	private Collection<AiItem> items;	
	@SuppressWarnings("unused")
	private Collection<AiFire> feu;	
	public int width;
	public int height;
	@SuppressWarnings("unused")
	private int x_IA, y_IA;
	private Cas matrice[][];
	
	private DemirciDuzokErgok source;
	
	public Zone_IA(AiZone zone_IA, DemirciDuzokErgok source) throws StopRequestException {
		
		//APPEL OBLIGATOIRE
		source.checkInterruption();
		this.source=source;
		
		this.zone_IA = zone_IA;
		this.bomberman = zone_IA.getOwnHero();
		this.ennemies = zone_IA.getHeroes();
		this.bombes = zone_IA.getBombs();
		this.murs = zone_IA.getBlocks();
		this.items = zone_IA.getItems();
		this.feu = zone_IA.getFires();
		this.width = zone_IA.getWidth();
		this.height = zone_IA.getHeight();	
		
		former_Matrice();
	}
	
	public void former_Matrice() throws StopRequestException
	{
		//APPEL OBLIGATOIRE
		source.checkInterruption();
		matrice = new Cas[width][height];
		int i, j;
		// On initial la matrice de la zone
		for (i = 0; i < width; i++) {			
			source.checkInterruption();//APPEL OBLIGATOIRE
			for (j = 0; j < height; j++) {
				source.checkInterruption();//APPEL OBLIGATOIRE				
				matrice[i][j] = Cas.LIBRE;
			}
		}
		
		//On met nos ennemies
		Iterator<AiHero> itEnnemies= ennemies.iterator();
		while(itEnnemies.hasNext())
		{
			source.checkInterruption();
			AiHero caractere=itEnnemies.next();
			if(!caractere.equals(bomberman))
			{
				matrice[caractere.getCol()][caractere.getLine()]=Cas.ENNEMIE;
			}
		}
		
		//On met les murs
		Iterator<AiBlock> itMurs=murs.iterator();
		while(itMurs.hasNext())
		{
			source.checkInterruption();
			AiBlock block=itMurs.next();
			if(block.isDestructible())
				matrice[block.getCol()][block.getLine()]=Cas.DESTRUCTIBLE_MUR;
			else
				matrice[block.getCol()][block.getLine()]=Cas.INDESTRUCTIBLE_MUR;
		}
		
		//On met les bombes
		Iterator<AiBomb> itBomb=bombes.iterator();
		while(itBomb.hasNext())
		{
			
		}
	}

}
