package org.totalboumboum.ai.v201011.ais.ozdokerozen.v6;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * @author Gizem Lara Özdöker
 * @author Sercan Özen
 */
@SuppressWarnings("deprecation")
public class OutilsMur {

	TileControleur tileControleur;
	ArtificialIntelligence abc;
	AiZone gameZone;
	AiHero notreHero;
	List<AiTile> tilesPossible=new ArrayList<AiTile>();
	
	int MURDESTRUCTIBLE=5;
	int MURINDESTRUCTIBLE=0;
	int FIRE=-2;
	int SECURE=1;
	int BONUS=10;
	int SCOPE=-3;
	
	/**
	 * 
	 * @param ai
	 * @param matrice
	 * @throws StopRequestException
	 */
	public OutilsMur(OzdokerOzen ai,int matrice[][]) throws StopRequestException {
		ai.checkInterruption();
		// initiliasation
		tileControleur=new TileControleur(ai, matrice);
		abc=ai;
		gameZone=abc.getPercepts();
		notreHero=gameZone.getOwnHero();
		List<AiTile> scopes=new ArrayList<AiTile>();
		tileControleur.tilePossibleArriveAvecRisk(notreHero.getTile(), tilesPossible, scopes, matrice);
	}

	/**
	 * Car on ne peut pas cree une path avec astar pour les murs, methode retourne le voisin qu'on peut arriver des murs
	 * @param mur
	 * @param tilesPossible
	 * @return tilde de cible pour detruire le mur
	 * @throws StopRequestException 
	 */
	public AiTile cibleAstar(AiTile mur, List<AiTile> tilesPossible) throws StopRequestException{
		abc.checkInterruption();
		AiTile voisinDeMur=null;
		if(mur!=null){
			List<AiTile> voisins=mur.getNeighbors();
			for(int i=0;i<voisins.size();i++){
				abc.checkInterruption();
				if(tilesPossible.contains(voisins.get(i))){
					voisinDeMur=voisins.get(i);
				}
			}
		}
		return voisinDeMur;
	}
}
