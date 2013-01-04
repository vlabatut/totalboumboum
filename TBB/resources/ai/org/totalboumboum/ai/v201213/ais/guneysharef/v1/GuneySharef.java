package org.totalboumboum.ai.v201213.ais.guneysharef.v1;




import java.util.Set;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;

/**
 * @author Melis Güney
 * @author Seli Sharef
 */
public class GuneySharef extends ArtificialIntelligence
{
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public GuneySharef()
	{	// active/désactive la sortie texte
		verbose = false;
	}
	
	@Override
	protected void initOthers() throws StopRequestException
	{	checkInterruption();
		
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() throws StopRequestException
	{	checkInterruption();
	

	}
	
	@Override
	protected void updatePercepts() throws StopRequestException
	{	checkInterruption();
		

	

	}
	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** gestionnaire chargé de calculer les valeurs d'utilité de l'agent */
	protected UtilityHandler utilityHandler;
	/** gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;
	
	/**
	 * on initialise les handler
	 */
	@Override
	protected void initHandlers() throws StopRequestException
	{	checkInterruption();
		
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		utilityHandler = new UtilityHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
		

		
		
	}

	@Override
	protected ModeHandler getModeHandler() throws StopRequestException
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<GuneySharef> getUtilityHandler() throws StopRequestException
	{	checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<GuneySharef> getBombHandler() throws StopRequestException
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<GuneySharef> getMoveHandler() throws StopRequestException
	{	checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{	checkInterruption();

		
		// ici, par défaut on affiche :
			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les utilités courantes
			utilityHandler.updateOutput();
	
	
	}

	/**
	 * @param tile
	 * @param tile2
	 * @return distance entre les cases
	 * @throws StopRequestException
	 */
	
	public int getDist(AiTile tile, AiTile tile2) throws StopRequestException {
		this.checkInterruption();
		int d= Math.abs(tile.getCol()-tile2.getCol())+Math.abs(tile.getRow()-tile2.getRow());
		
		return d;
	}

	/**
	 * @param t
	 * @return Donne la plus proche case
	 * @throws StopRequestException
	 */
	public Object getClosestTile(AiTile t) throws StopRequestException{
		this.checkInterruption();
		float valeur = 20;
		AiTile tile = null;
		Set<AiTile> liste = this.utilityHandler.selectTiles();
		AiZone z=getZone();
		AiHero h =z.getOwnHero();
		AiTile t2=h.getTile();
		
		if(t==t2)
			liste.remove(t2);
		
		for(AiTile tile2 : liste){
			this.checkInterruption();
			if(getDist(t, tile2) < valeur){
				valeur=getDist(t,tile2);
				tile=tile2;
				
			}
		}
		return tile;
	}
	
	/**
	 * @return trouve la meilleur case
	 * @throws StopRequestException
	 */
	public AiTile getBiggestTile() throws StopRequestException{
		this.checkInterruption();
		float valeur = 0;
		AiTile biggest=null;
		Set<AiTile> liste = this.utilityHandler.selectTiles();

		
		for(AiTile tile : liste){
			this.checkInterruption();
			if(getUtilityHandler().getUtilitiesByTile().get(tile)>valeur)
				valeur=getUtilityHandler().getUtilitiesByTile().get(tile);
			biggest=tile;	
		}
		return biggest;
	}


}
