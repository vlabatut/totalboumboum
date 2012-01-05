package org.totalboumboum.ai.v201112.ais.gungorkavus.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v1.GungorKavus;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Eyüp Burak Güngör
 * @author Umit Kavus
 */
public class ItemVisibleMenace extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Menace";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * Au cas où le moteur demande la terminaison de l'agent.
	 */
	public ItemVisibleMenace(GungorKavus ai) throws StopRequestException
	{	// init nom
		super(NAME);

		// init agent
		this.ai = ai;
	}

	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected GungorKavus ai;

	/////////////////////////////////////////////////////////////////
	// PROCESS	 /////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	boolean result = false;
	AiZone zone = ai.getZone();
	List<AiBomb> bombL = zone.getBombs();

	for(int i=0;i<bombL.size();i++){
		ai.checkInterruption();
		if(bombL.get(i).getRow()==tile.getRow()){
			if(bombL.get(i).getCol()>tile.getCol()){
				if(tile.getCol()>=(bombL.get(i).getCol()-bombL.get(i).getRange())){
					for(int a = 1;a<bombL.get(i).getCol()-tile.getCol();a++){
						ai.checkInterruption();
						if(zone.getTile(tile.getRow(), tile.getCol()+a).getBlocks().size()==0&&zone.getTile(tile.getRow(), tile.getCol()+a).getHeroes().size()==0&&zone.getTile(tile.getRow(), tile.getCol()+a).getItems().size()==0){
							result = true;
						}
					}	
				}
			}else if(bombL.get(i).getCol()<tile.getCol()){
				if(tile.getCol()<=(bombL.get(i).getCol()+bombL.get(i).getRange())){
					for(int a = 1;a<tile.getCol()-bombL.get(i).getCol();a++){
						ai.checkInterruption();
						if(zone.getTile(tile.getRow(), tile.getCol()-a).getBlocks().size()==0&&zone.getTile(tile.getRow(), tile.getCol()-a).getHeroes().size()==0&&zone.getTile(tile.getRow(), tile.getCol()-a).getItems().size()==0){
							result = true;
						}
					}
				}
			}
		}

		if(bombL.get(i).getCol()==tile.getCol()){
			if(bombL.get(i).getRow()>tile.getRow()){
				if(tile.getRow()>=(bombL.get(i).getRow()-bombL.get(i).getRange())){
					for(int a = 1;a<bombL.get(i).getRow()-tile.getRow();a++){
						ai.checkInterruption();
						if(zone.getTile(tile.getRow()+a, tile.getCol()).getBlocks().size()==0&&zone.getTile(tile.getRow()+a, tile.getCol()).getHeroes().size()==0&&zone.getTile(tile.getRow()+a, tile.getCol()).getItems().size()==0){
							result = true;	
						}
					}
				}
			}else if(bombL.get(i).getRow()<tile.getRow()){
				if(tile.getRow()<=(bombL.get(i).getRow()+bombL.get(i).getRange())){
					for(int a = 1;a<tile.getRow()-bombL.get(i).getRow();a++){
						ai.checkInterruption();
						if(zone.getTile(tile.getRow()-a, tile.getCol()).getBlocks().size()==0&&zone.getTile(tile.getRow()-a, tile.getCol()).getHeroes().size()==0&&zone.getTile(tile.getRow()-a, tile.getCol()).getItems().size()==0){
							result = true;	
						}
					}
				}
			}
		}

	}

	return result;
	}
}