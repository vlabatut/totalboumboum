package tournament200910.danesatir.v1;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;

/**
 * >> ce texte est à remplacer par votre propre description de votre IA
 * 
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
 */
public class DaneSatir extends ArtificialIntelligence
{
	private AiZone zone;
	private AiHero myHero;
	private AiTile currentTile;
	private AiTile nextTile;
	private AiTile targetTile;
	private State state;
	private boolean finalState;
	private int moveCount;
	private TimeMatrice timeMatrice;
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		AiAction result = null;
		if(this.myHero == null)
			init();
		updateEnvironment();
		while(!this.finalState) {
			makeDecision();
			result = calculAction();
		}
		if (result == null) {
			if(GeneralFuncs.checkVerboseLevel(7))
				System.out.println("Problem var");
			result = new AiAction(AiActionName.NONE);
		}
		return result;
	}
	
	private AiAction calculAction() throws StopRequestException {
		// TODO Auto-generated method stub
		checkInterruption();
		AiAction result = null;
		switch(this.state) {
		case DANGER: searchSafeCase();
			break;
		case GOTO: this.finalState = true;
			findShortestPath();
			result = new AiAction(AiActionName.MOVE, this.zone.getDirection(this.currentTile, this.nextTile));
			break;
		case NOSAFECASE: this.finalState = true;
			System.out.println("Problem var");
			break;
		case NONE:
			//System.out.println("Problem var");
			this.finalState = true;
			break;
		}
		
		return result;
	}

	private void findShortestPath() throws StopRequestException {
		// TODO Auto-generated method stub
		this.checkInterruption();
		AiPath path = new PathFinder(this,this.currentTile,this.targetTile).getPath();
		if(GeneralFuncs.tileCompare(path.getTile(0),this.currentTile))
			path.removeTile(this.currentTile);
		this.nextTile = path.getTile(0);
		System.out.println(currentTile+"->"+nextTile);
	}

	private void searchSafeCase() throws StopRequestException {
		// TODO Auto-generated method stub
		this.checkInterruption();
		targetTile = this.timeMatrice.mostSafeCase(currentTile);
		if(targetTile == null)
			this.state = State.NOSAFECASE;
		else
			this.state = State.GOTO;
	}

	private void makeDecision() throws StopRequestException {
		// TODO Auto-generated method stub
		checkInterruption();
		this.state = new DecisionMaker(this.zone,this.myHero,this.timeMatrice).getDecision();
	}

	private void init() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		zone = getPercepts();
		myHero = zone.getOwnHero();
		this.moveCount = 0;
	}
	
	private void updateEnvironment() throws StopRequestException {
		checkInterruption();
		if (GeneralFuncs.checkVerboseLevel(5))
			System.out.println("Update Environment fonksiyonuna girdik.");
		this.currentTile = myHero.getTile();
		this.moveCount++;
		this.timeMatrice = new TimeMatrice(this);
		this.finalState = false;
	}

	public AiHero getOwnHero() {
		// TODO Auto-generated method stub
		return this.myHero;
	}
}
