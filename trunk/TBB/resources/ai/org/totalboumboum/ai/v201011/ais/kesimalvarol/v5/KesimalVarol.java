package org.totalboumboum.ai.v201011.ais.kesimalvarol.v5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

/**
 * Pour indiquer le mode
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 */
@SuppressWarnings("deprecation")
enum Mode {
	NONE,COLLECTE,ATTAQUE
}

/**
 * La classe principal d'IA de groupe gris.
 * 
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 *
 */
@SuppressWarnings("deprecation")
public class KesimalVarol extends ArtificialIntelligence
{	
	boolean verbose=false;
	
	/** Variable pour garder la zone */
	private AiZone zone;
	public AiZone getZone() throws StopRequestException {
		checkInterruption();
		return zone;
	}
	/** Representation modifi√©e de la zone  */
	private GameZoneAnalyzer gZoneAnalyzer;
	/** Repr. de notre agent */
	private AiHero selfHero;
	public AiHero getSelfHero() throws StopRequestException {
		checkInterruption();
		return selfHero;
	}
	/** Repr. du mode de notre agent. */
	private Mode mode;
	public Mode getMode() throws StopRequestException {
		checkInterruption();
		return mode;
	}
	
	/** Objet de MovementController pour gerer nos deplacements. */
	private MovementCommitter mcontrol;
	
	/** Coefficient de tolerance utilisee pour calculer si on peut nous eviter d'une explosion potentielle */
	private double currentToleranceCofficient;
	public double getCurrentToleranceCofficient() throws StopRequestException {
		checkInterruption();
		return currentToleranceCofficient;
	}
	
	/** Renvoie le delai d'explosion d'une bombe imaginaire
	 * 
	 * @return Delai d'explosion
	 * @throws StopRequestException 
	 */
	public double getUsualBombNormalDuration() throws StopRequestException
	{
		checkInterruption();
		AiBomb refer=selfHero.getBombPrototype();
		return refer.getNormalDuration()/1000.0; //2.5 ?
	}
	
	/** Determine si l'IA va poser une bombe */
	private boolean bombReleaseState;
	/** Utilisee pour mieux gerer les delais lors d'execution : Si on ne se trouve pas dans la case actuellement choisi pendant l'iteration precedent,
	 * on ne laissera pas une bombe.
	 */
	private AiTile bombReleaseTile;
	/** On a vu que les bombes ne sont pas posees juste apres l'action, a cause des delai. */
	private AiTile bombShouldBeHere;
	public void willReleaseBomb() throws StopRequestException
	{
		checkInterruption();
		bombReleaseState=true;
		bombReleaseTile=selfHero.getTile();
	}
	/** Pour qu'on reste au mode attaque pour environ 2 secondes. */
	private long remainingTimeToCompleteAttack;
	
	/** Sommes-nous en train de prevoir l'explosion ? */
	private boolean isPredicting;
	public boolean isPredicting() throws StopRequestException {
		checkInterruption();
		return isPredicting;
	}
	/** Utilisee lors de prediction de posage des bombes, s'il existe des bonus interdissant le posage des bombes */
	private AiTile prBonusTile;
	public AiTile getPrBonusTile() throws StopRequestException {
		checkInterruption();
		return prBonusTile;
	}
	public void setPrBonusTile(AiTile prBonusTile) throws StopRequestException {
		checkInterruption();
		this.prBonusTile = prBonusTile;
	}
	
	/** Utilisee pour l'intertie */
	public class _lastPathChosenParameters
	{
		private AiPath lastPathChosen;
		private int cumulativeInterest,currentPosition;
	
		public _lastPathChosenParameters(AiPath p,int cI,int cP) throws StopRequestException
		{
			checkInterruption();
			lastPathChosen=p;
			cumulativeInterest=cI;
			currentPosition=cP;
		}
		public _lastPathChosenParameters(AiPath p,int cI) throws StopRequestException
		{
			checkInterruption();
			lastPathChosen=p;
			cumulativeInterest=cI;
			currentPosition=0;
		}
		public int getCumulativeInterest() throws StopRequestException {
			checkInterruption();
			return cumulativeInterest;
		}
		public void setCumulativeInterest(int cumulativeInterest) throws StopRequestException {
			checkInterruption();
			this.cumulativeInterest = cumulativeInterest;
		}
		public int getCurrentPosition() throws StopRequestException {
			checkInterruption();
			return currentPosition;
		}
		public void setCurrentPosition(int currentPosition) throws StopRequestException {
			checkInterruption();
			this.currentPosition = currentPosition;
		}
		public AiPath getLastPathChosen() throws StopRequestException {
			checkInterruption();
			return lastPathChosen;
		}
		public void setLastPathChosen(AiPath lastPathChosen) throws StopRequestException {
			checkInterruption();
			this.lastPathChosen = lastPathChosen;
		}
	}
	public _lastPathChosenParameters lastPathChosenParameters;
	
	/** Les endroits d'emplacements qui seront ignorees  */
	private HashMap<AiTile, Long> temporarilyIgnoredEmplacementCases;
	public HashMap<AiTile, Long> getTemporarilyIgnoredCases() throws StopRequestException {
		checkInterruption();
		return temporarilyIgnoredEmplacementCases;
	}
	public void setTemporarilyIgnoredCases(
			HashMap<AiTile, Long> temporarilyIgnoredCases) throws StopRequestException {
		checkInterruption();
		this.temporarilyIgnoredEmplacementCases = temporarilyIgnoredCases;
	}
	
	/** Variable permettant de determiner si nous sommes en danger et n'a pas pu trouver une chemin lors d'iteration precedente. */
	private boolean inDanger;
	/**
	 * Utilisee par MovementCommitter, on indique que nous sommes en danger et on ne peut pas nous diriger vers aucune case.
	 * @throws StopRequestException 
	 */
	public void iAmInDanger(boolean val) throws StopRequestException
	{
		checkInterruption();
		inDanger=val;
	}
	/**
	 * Utilisee par PathSafetyDeterminators, renvoyera si nous sommes deja en danger et on veut nous deplacer n'importe quoi.
	 * @return Si nous etions en danger.
	 * @throws StopRequestException 
	 */
	public boolean isInDangerAndWontMoveAnyway() throws StopRequestException
	{
		checkInterruption();
		return inDanger;
	}
	
	/** On a vu que la premier iteration nous renvoie quelques resultats incompletes (notamment, sur la zone). On utilise cette variable pour ameliorer notre traitement */
	private boolean firstCall;

	@Override
	public void init() throws StopRequestException {
		checkInterruption();
		super.init();
		MovementCommitter.setMonIA(this);
		GameZoneAnalyzer.setMonIA(this);
		SelfCentricResearch.setMonIA(this);
		mode=Mode.NONE;
		mcontrol=new MovementCommitter();
		bombReleaseState=false;
		bombReleaseTile=null;
		bombShouldBeHere=null;
		gZoneAnalyzer=null;
		lastPathChosenParameters=null;
		temporarilyIgnoredEmplacementCases=new HashMap<AiTile, Long>();
		firstCall=true;
	}

	/** 
	 * mÔøΩthode appelÔøΩe par le moteur du jeu pour obtenir une action de votre IA 
	 * @return Methode principal pour renvoyer une action
	 */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		zone=getPercepts();
		selfHero=zone.getOwnHero();
		
		//On a vu que le premier processAction() nous cache quelques informations sur la zone du jeu.
		if(firstCall)
		{
			firstCall=false;
			return new AiAction(AiActionName.NONE);
		}

		if(bombShouldBeHere!=null && bombShouldBeHere.getBombs().size()==1)
		{
			bombShouldBeHere=null;
		}
		
		if(bombReleaseState)
		{
			bombReleaseState=false;
			lastPathChosenParameters=null;
			if(bombReleaseTile==selfHero.getTile())
			{
				if(verbose) System.out.println("Dropped");
				temporarilyIgnoredEmplacementCases.put(bombReleaseTile,Long.valueOf(1250)); //Ignoree pour 1.25 secondes
				bombShouldBeHere=bombReleaseTile;
				if(mode==Mode.ATTAQUE && remainingTimeToCompleteAttack<=0)
				{
					/*
					 * Si on tombera dans le mode collecte et si nous sommes en mode attaque, on voudrait rester dans le mode attaque pour deux secondes.
					 * Sinon, on ne l'utilise pas ; on sera encore dans le mode attaque et on perdera nos chances suivantes.
					 * Pour realiser ceci, on va dire qu'on a encore du temps pour completer notre assaut, interdissant la changement.
					 */
					if(verbose) System.out.println("Fake mode changement.");
					changeMode();
					if(mode==Mode.COLLECTE)
						remainingTimeToCompleteAttack=2000; //On a 2 secondes pour lancer une deuxieme bombe.
					mode=Mode.ATTAQUE;
				}
				return new AiAction(AiActionName.DROP_BOMB);
			}
			else
			{
				if(verbose) System.out.println("Couldn't drop, invoked too late.");
				bombShouldBeHere=null;
			}
			bombReleaseTile=null;
		}
		
		prBonusTile=null;
		updateTemporarilyIgnoredExplosionCases();
		updateAttackCompletionPermit();
		updatePathIfChangedCase();
		isPredicting=false;
		
		changeMode();
		
		currentToleranceCofficient=200.0*selfHero.getTile().getSize()/selfHero.getWalkingSpeed();
		
		gZoneAnalyzer=new GameZoneAnalyzer(gZoneAnalyzer);
		Matrix m=gZoneAnalyzer.constructInterestMatrix();
		if(bombShouldBeHere!=null)
		{
			GameZoneFuturePredictions.modifyMatrixWithFutureBomb(m);
			bombShouldBeHere=null;
		}
		
		AiAction result = mcontrol.commitMovement(m);
		
		colorizePath(m,mcontrol);
		return result; 
		//return new AiAction(AiActionName.NONE);
	}
	
	/**
	 * Mettre a jour les endroits d'emplacement qui etaient ignores
	 */
	private void updateTemporarilyIgnoredExplosionCases() throws StopRequestException
	{
		checkInterruption();
		ArrayList<AiTile> keysToBeRemoved = new ArrayList<AiTile>();
	    for(Map.Entry<AiTile,Long> e : temporarilyIgnoredEmplacementCases.entrySet())
	    {
	    	checkInterruption();
	    	long val=e.getValue()-zone.getElapsedTime();
	    	if(val<=0)
	    		keysToBeRemoved.add(e.getKey());
	    	else
	    		temporarilyIgnoredEmplacementCases.put(e.getKey(), val);

	    }
	    for(AiTile key : keysToBeRemoved)
	    {
	    	checkInterruption();
	    	temporarilyIgnoredEmplacementCases.remove(key);
	    }
	}
	
	private void updateAttackCompletionPermit() throws StopRequestException
	{
		checkInterruption();
		if(remainingTimeToCompleteAttack>0)
			remainingTimeToCompleteAttack-=zone.getElapsedTime();
	}
	
	/**
	 * Utilisee par MovementController, pour changer le chemin courant
	 * 
	 * @param p Le chemin
	 * @param cI Importance cumulative (pas utilisee maintenant)
	 */
	public void requestNewPath(AiPath p,int cI) throws StopRequestException
	{
		checkInterruption();
		lastPathChosenParameters=new _lastPathChosenParameters(p, cI);
	}
	
	/**
	 * On mettra a jour le chemin courant a chaque iteration
	 */
	private void updatePathIfChangedCase() throws StopRequestException
	{
		checkInterruption();
		if(lastPathChosenParameters!=null) {
			if(verbose) System.out.println("Path update\n"+lastPathChosenParameters.getCurrentPosition()+" out of "+(lastPathChosenParameters.getLastPathChosen().getLength()-1));
			if(lastPathChosenParameters.getLastPathChosen().getLength()-1==lastPathChosenParameters.getCurrentPosition())
			{
				lastPathChosenParameters=null;
				if(verbose) System.out.println("Arrived on case");
			}
			else if(lastPathChosenParameters.getLastPathChosen().getTile(lastPathChosenParameters.getCurrentPosition())!=selfHero.getTile())
			{
				if(lastPathChosenParameters.getLastPathChosen().getTile(lastPathChosenParameters.getCurrentPosition()+1)==selfHero.getTile())
				{
					//lastPathChosenParameters.setCurrentPosition(lastPathChosenParameters.getCurrentPosition()+1);
					lastPathChosenParameters.lastPathChosen.removeTile(0);
				}
				else {
					lastPathChosenParameters=null;
					if(verbose) System.out.println("ABRUPTLY Arrived on case");
				}
			}
		}
	}
	
	/**
	 * Determine si on peut poser de bombe
	 * 
	 * @param m La matrice d'interet sur lequel notre calcul sera basee
	 * @return La decision a faire
	 */
	public boolean isBombDropPermitted(Matrix m) throws StopRequestException
	{
		checkInterruption();
		
		if(verbose) System.out.println("Bomb count on case is :"+selfHero.getTile().getBombs().size());
		if(selfHero.getBombRange()==0 || selfHero.getTile().getBombs().size()>0 || m.representation[selfHero.getTile().getLine()][selfHero.getTile().getCol()]<=-275)
		{
			if(verbose) System.out.println(selfHero);
			//if(verbose) System.out.println(selfHero.getBombRange() + " " + selfHero.getTile().getBombs().size() + " " + m.representation[selfHero.getTile().getLine()][selfHero.getTile().getCol()]);
			return false;
		}
		
		m.makeBackup();
		if(verbose) System.out.println("Made matrix backup");
		isPredicting=true;
		boolean ret=false;
		
		if(mode==Mode.COLLECTE && GameZoneFuturePredictions.areBonusAdjacentAndNotTargetingEnemies(m))
		{
			if(verbose) System.out.println("Bonuses found");
		}
		else {
			GameZoneFuturePredictions.modifyMatrixWithFutureBomb(m);
			AiAction res=mcontrol.commitMovementForFutureBomb(m);
			if(res.getName()==AiActionName.NONE)
			{
				if(verbose) System.out.println("Nondrop");
			}
			else {
				if(verbose) System.out.println("Drop permitted");
				ret=true;
			}
		}
		
		if(verbose) System.out.println("Restored matrix backup");
		m.restoreBackup();
		isPredicting=false;
		
		if(!ret)
		{
			temporarilyIgnoredEmplacementCases.put(selfHero.getTile(),Long.valueOf(500)); //Ignoree pour 1.25 secondes
		}
		return ret;
	}
	
	/**
	 * Si on va laisser une bombe, la case prefereable sera deja determinee grace a cette methode
	 * @param targ
	 */
	public void requestSpecialBombEvasiveTarget(AiTile targ) throws StopRequestException
	{
		checkInterruption();
		mcontrol.setBombSpecialRequest(targ);
	}
	
	/**
	 * Determine la meilleure case pour nous deplacer si A* nous a rendu aucune
	 * 
	 * @param m La matrice d'interet sur lequel notre calcul sera basee
	 * @return La case le plus preferable si les 4 premiers chemins au A* ne marchent pas
	 */
	public AiTile getReachablePreferableLocation(Matrix m) throws StopRequestException
	{
		checkInterruption();
		return SelfCentricResearch.getPreferableAccesibleCase(m);
	}

	/**
	 * Utilisee par mode attaque, si on a besoin de detruire les murs.
	 * 
	 * @param m La matrice d'interet sur lequel notre calcul sera basee
	 * @param modifier Valeur a effectuer
	 */
	public void requestWallEffects(Matrix m,int modifier) throws StopRequestException
	{
		checkInterruption();
		gZoneAnalyzer.handleWalls(m, modifier);
	}
	
	/**
	 * Determine le mode courant
	 */
	private void changeMode() throws StopRequestException
	{
		checkInterruption();
		if(verbose) System.out.println("Mode change\n"+(selfHero.getBombNumberMax()-selfHero.getBombNumberCurrent())+","+selfHero.getBombRange());
		
		//Si on est en train de commetre un assaut, on ne change pas notre mode pour assurer qu'on lance une attaque mieux orientee vers detruire l'adversaire.
		if(mode==Mode.ATTAQUE && remainingTimeToCompleteAttack>0)
			return;
			
		if(selfHero.getBombNumberMax()-selfHero.getBombNumberCurrent()+selfHero.getBombRange()>6 || (selfHero.getBombNumberMax()-selfHero.getBombNumberCurrent()) >=2 && selfHero.getBombRange() >=3) {
			if(mode==Mode.COLLECTE) {
				if(verbose) System.out.println("C->A");
			}
			mode=Mode.ATTAQUE;
		}
		else {
			if(mode==Mode.ATTAQUE) {
				if(verbose) System.out.println("A->C");
			}
			mode=Mode.COLLECTE;
		}
		if(verbose) System.out.println("Now in "+mode);
	}
	
	//---------------------
	// Methodes a utiser lors de debogage
	//---------------------
	/**
	 * Utilisee lors de debogage, pour montrer les couleurs et textes
	 * @param m Matrice a utiliser
	 * @param mc MovementController a utiliser
	 * 
	 */
	private void colorizePath(Matrix m,MovementCommitter mc) throws StopRequestException
	{
		checkInterruption();
		m.colorizeMapAccordingly();
		mc.drawPathOnScreen();
	}
}
