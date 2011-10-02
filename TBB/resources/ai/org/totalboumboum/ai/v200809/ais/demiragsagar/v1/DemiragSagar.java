package org.totalboumboum.ai.v200809.ais.demiragsagar.v1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Doğus Burcu Demirağ
 * @author Zeynep Şagar
 *
 */
@SuppressWarnings("deprecation")
public class DemiragSagar extends ArtificialIntelligence {
	Direction d;
	AiZone zone;
	AiHero ownHero;
	AiTile caseCourant;
	AiTile caseHedef;
	List<AiTile> bombes;
	int baseX = 0;
	int baseY;
	int zoneWidth;
	int zoneHeight;
	Attack monAttack;
	double[][] matrice;
	Escape esc;
	List<AiTile> interTiles;
	boolean estIntermediaire;
	boolean debug;
	boolean olmadikacilk;

	public DemiragSagar() {
		this.d = Direction.NONE;
		this.estIntermediaire = false;
		this.caseHedef = null;		
		this.interTiles = new ArrayList<AiTile>();
		this.bombes = new ArrayList<AiTile>();
		this.monAttack = null;
		this.debug=false;
		this.olmadikacilk=false;
	}

	@Override
	public AiAction processAction() throws StopRequestException {
		checkInterruption();
		AiAction result = new AiAction(AiActionName.NONE);

		// zone details
		this.zone = getPercepts();
		if(this.monAttack==null) this.monAttack = new Attack(zone,9, 9, 4, 3, this);
		getBombes();
		
		if (baseX == 0)
			calculeZoneAspect(zone);

		// les heros
		ownHero = zone.getOwnHero();
		caseCourant = ownHero.getTile();
		AiTile caseEnemi = this.getEnemyTile();
		if(caseEnemi==null)
			return new AiAction(AiActionName.NONE);

		if (d == Direction.NONE) {
			AiTile target = null;
			// nous navons pas de direction
			if (caseCourant.getCol() == monAttack.midPointX && caseCourant.getLine() == monAttack.midPointY) {
				// on a divise la zone par 4, nous sommes au milieu
				if(debug) System.out.println("point milieu");
				bombes.clear();
				bombes.add(zone.getTile(caseCourant.getLine(), caseCourant.getCol()));
				monAttack.updateNewPoints(caseEnemi);
				bombes.add(zone.getTile(monAttack.midPointY,monAttack.midPointX));
				esc = new Escape(bombes);
				this.interTiles = esc.getIntersection(zone);
				bombes.clear();
				estIntermediaire = true;
				return new AiAction(AiActionName.DROP_BOMB);
			} else if (bombes.isEmpty() && monAttack.olmadikac && zone.getFires().isEmpty()) {
				if(debug) System.out.println("kacmayi birak");
				monAttack.olmadikac = false;
				//sacma
				olmadikacilk=false;
				this.estIntermediaire = false;
				monAttack=null;
				return new AiAction(AiActionName.DROP_BOMB);
			} else if (monAttack.olmadikac) {
				if(debug) System.out.println("kac");
				//estIntermediaire = false;
				//si getEscape n'a pas un effet un sur ou on est ,il faut faire retourner ou on est
				this.getBombes();
				esc = new Escape(bombes);
				target = esc.getEscape(caseCourant, zone);
				if(olmadikacilk==false) {
					olmadikacilk=true;
					return new AiAction(AiActionName.NONE);
				}
				//printTile(target);
			} else if (estIntermediaire) {
				if(debug) System.out.println("kesisim'e bomba koy");
				// avancer a l'intersection
				target = interTiles.get(0);
				// l'intersection des effets des bombes que nous avons depose
				// regarder tous les intersections
				if (ownHero.getCol() == interTiles.get(0).getCol() && ownHero.getLine() == interTiles.get(0).getLine()) {
					if(debug) System.out.println("kesisim'e gelmissin la");
					// nous sommes a l'intersection
					estIntermediaire = false;
					return new AiAction(AiActionName.DROP_BOMB);
				}
			} else {  //if (estIntermediaire == false) 
				if(debug) System.out.println("hic bisi yok orta noktaya ilerle");
				// aller au milieu de la zone partage
				target = this.zone.getTile(monAttack.midPointY,	monAttack.midPointX);
			}
				
			//System.out.println("bee:"+target.getCol()+" "+target.getLine());
			if(caseCourant.getLine()==target.getLine() && caseCourant.getCol()==target.getCol())
				return new AiAction(AiActionName.NONE);
			AStar arbre;
			arbre = new AStar(caseCourant, target, this, false);
			arbre.formeArbre();
			if(arbre.path==null) {
				printTile(target);
				return new AiAction(AiActionName.NONE);
			}
			Iterator<Node> it = arbre.path.iterator();
			if (it.hasNext())
				caseHedef = it.next().tile;
			else
				// System.out.println("pas de chemin");
				caseHedef = caseCourant;
			//System.out.println("caseHedef: "+caseHedef.getCol()+","+caseHedef.getLine());
			d = getPercepts().getDirection(caseCourant, caseHedef);
			// System.out.println(d.toString());
			result = new AiAction(AiActionName.MOVE, d);
			// moving finished

		} else if (!estCaseCible())
			// on avance jusqu'a la case cible
			result = new AiAction(AiActionName.MOVE, d);
		return result;
	}

	public void calculeZoneAspect(AiZone zone) {
		try {
			checkInterruption();
		} catch (StopRequestException e) {
			e.printStackTrace();
		}
		this.zoneHeight = 9;
		this.zoneWidth = 9;
		this.baseX = 4;
		this.baseY = 3;
	}

	public boolean estCaseCible() {
		try {
			checkInterruption();
		} catch (StopRequestException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		// fonction verifiant si l'ia est arrive a la case cible
		boolean resultat = false;

		if (caseHedef == null)
			caseHedef = caseCourant.getNeighbor(d);
		if (ownHero.getTile().equals(caseHedef)) {
			d = Direction.NONE;
			caseHedef = null;
			resultat = true;
		}
		return resultat;

	}

	public AiTile getEnemyTile() {
		try {
			checkInterruption();
		} catch (StopRequestException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		AiHero enemy = null;
		for (AiHero i : this.zone.getHeroes()){
			try {
				checkInterruption();
			} catch (StopRequestException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
			if (i.getColor() != this.ownHero.getColor())
				enemy = i;
		}
		if (enemy != null)
			return enemy.getTile();
		return null;
	}

	public void getBombes() {
		try {
			checkInterruption();
		} catch (StopRequestException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		bombes.clear();
		if (this.zone.getBombs() != null)
			for (AiBomb i : this.zone.getBombs()){
				try {
					checkInterruption();
				} catch (StopRequestException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
				bombes.add(i.getTile());
			}
	}
	public void printTile(AiTile t) {
		System.out.println("[]"+t.getLine()+" "+t.getCol());
	}
}
