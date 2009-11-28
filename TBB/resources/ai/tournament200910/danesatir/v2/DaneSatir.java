package tournament200910.danesatir.v2;

import fr.free.totalboumboum.ai.adapter200910.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200910.communication.AiAction;
import fr.free.totalboumboum.ai.adapter200910.communication.AiActionName;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;

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
	public AiAction processAction() throws StopRequestException {	
		// avant tout : test d'interruption
		checkInterruption();
		AiAction result = null;
		if(this.myHero == null)
			init();
		if(myHero.hasEnded())
			return new AiAction(AiActionName.NONE);
		updateEnvironment();
		while(this.finalState == false) {
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
		// avant tout : test d'interruption
		checkInterruption();
		AiAction result = null;
		switch(this.state) {
		case DANGER: searchSafeCase();
			break;
		case GOTO: this.finalState=true;
			PathFinder p = new PathFinder(this,this.currentTile,this.targetTile);
			this.nextTile = p.getNextTile();
			System.out.println("target:"+targetTile+" next->"+nextTile+" current->"+currentTile);
			if (this.nextTile!=null)
				result = new AiAction(AiActionName.MOVE, this.zone.getDirection(this.currentTile, this.nextTile));
			else this.state = State.NOWAYTOGO;
			break;
		case TAKEBONUS: searchNearBonus();
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

	private void searchNearBonus() throws StopRequestException {
		//TODO: make new class BonusCollector
		// avant tout : test d'interruption
		this.checkInterruption();
		AiTile temp = new BonusCollector(this).findNearBonus(currentTile);
		System.out.println("test"+temp);
		if (temp!=null) {
			this.targetTile = temp;
			this.setState(State.GOTO);
		}
		else
			this.setState(State.NONE);
	}

	private void searchSafeCase() throws StopRequestException {
		// avant tout : test d'interruption
		this.checkInterruption();
		this.targetTile = this.timeMatrice.mostSafeCase(currentTile);
		if(targetTile == null)
			this.state = State.NOSAFECASE;
		else
			this.state = State.GOTO;
	}

	private void makeDecision() throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();
		this.state = new DecisionMaker(this).getDecision();
	}

	private void init() throws StopRequestException 	{	
		// avant tout : test d'interruption
		checkInterruption();
		zone = getPercepts();
		myHero = zone.getOwnHero();
		this.moveCount = 0;
	}
	
	private void updateEnvironment() throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();
		if (GeneralFuncs.checkVerboseLevel(5))
			System.out.println("Update Environment fonksiyonuna girdik.");
		this.currentTile = myHero.getTile();
		this.moveCount++;
		this.timeMatrice = new TimeMatrice(this);
		this.finalState = false;
		this.state=State.START;
	}

	public AiHero getOwnHero() {
		return this.myHero;
	}
	
	public TimeMatrice getTimeMatrice() {
		return this.timeMatrice;
	}
	
	public State getState() {
		return this.state;
	}
	
	public void setState(State state) {
		this.state=state;
	}
}
