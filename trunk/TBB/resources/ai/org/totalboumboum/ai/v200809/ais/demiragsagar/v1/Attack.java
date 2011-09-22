package org.totalboumboum.ai.v200809.ais.demiragsagar.v1;

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
public class Attack {

	int attackZoneWidth;
	int attackZoneHeight;
	int attackBaseX;
	int attackBaseY;
	int midPointX;
	int midPointY;
	boolean olmadikac;
	ArtificialIntelligence ai;
	AiZone zone;

	public Attack(AiZone zone,int width, int height, int x, int y, ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.attackZoneWidth = width;
		this.attackZoneHeight = height;
		this.attackBaseX = x;
		this.attackBaseY = y;
		this.updateMidPoints();
		this.olmadikac=false;
		this.zone=zone;
	}

	public Direction detect(AiTile caseHero) throws StopRequestException {
		ai.checkInterruption();
		return this.zone.getDirection(zone.getTile(this.attackBaseX+this.attackZoneWidth/2,this.attackBaseY+this.attackZoneHeight/2),caseHero);
	}
	
	public void updateNewPoints(AiTile target) throws StopRequestException {
		ai.checkInterruption();
		Direction p;
		if (this.attackZoneHeight < 5 || this.attackZoneWidth < 5)
			this.olmadikac=true;
		// detect position enemy
		p = detect(target);
		// check minsize
		if (p == Direction.UPRIGHT)
			this.attackBaseX = this.midPointX;
		else if (p == Direction.DOWNRIGHT) {
			this.attackBaseX = this.midPointX;
			this.attackBaseY = this.midPointY;
		} else if (p == Direction.DOWNLEFT) {
			this.attackBaseY = this.midPointY;
		}
		this.attackZoneHeight = this.attackZoneHeight / 2;
		this.attackZoneWidth = this.attackZoneWidth / 2;
		if(p == Direction.DOWN || p == Direction.UP || p == Direction.LEFT || p == Direction.RIGHT)
			this.olmadikac=true;
		updateMidPoints();	
	}

	public void updateMidPoints() throws StopRequestException {
		ai.checkInterruption();
		this.midPointX = this.attackBaseX + this.attackZoneWidth / 2;
		this.midPointY = this.attackBaseY + this.attackZoneHeight / 2;
	}
}
