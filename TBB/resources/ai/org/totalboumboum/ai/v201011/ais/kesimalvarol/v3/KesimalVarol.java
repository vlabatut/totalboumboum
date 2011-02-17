package org.totalboumboum.ai.v201011.ais.kesimalvarol.v3;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

/**
 * L'IA de groupe gris.
 * Implementation des 
 * Ali Baran Kesimal
 * Işıl Varol
 * 
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 *
 */

/**
 * Pour indiquer le mode
 */
enum Mode {
	NONE,COLLECTE,ATTAQUE
}

public class KesimalVarol extends ArtificialIntelligence
{	
	/** Variable pour garder la zone */
	private AiZone zone;
	public AiZone getZone() {
		return zone;
	}
	/** Representation modifi√©e de la zone  */
	private GameZone gZone;
	/** Repr. de notre agent */
	private AiHero selfHero;
	public AiHero getSelfHero() {
		return selfHero;
	}
	/** Repr. du mode de notre agent. */
	private Mode mode;
	public Mode getMode() {
		return mode;
	}
	
	/** Objet de MovementController pour gerer nos deplacements. */
	private MovementController mcontrol;
	
	/** Coefficient de tolerance utilisee pour calculer si on peut nous eviter d'une explosion potentielle */
	private double currentToleranceCofficient;
	public double getCurrentToleranceCofficient() {
		return currentToleranceCofficient;
	}
	
	/** Determine si l'IA va poser une bombe */
	private boolean bombReleaseState;
	public void willReleaseBomb()
	{
		bombReleaseState=true;
	}
	
	//w/ aipath ?
	/** La cible precedente */
	private AiTile lastTarget;
	/** L'action precedente */
	private AiAction lastAction;
	/** La valeur cumulative pour la cible precedente */
	public int lastTargetCumulativeInterest;
	public AiTile getLastTarget() {
		return lastTarget;
	}

	public void setLastTarget(AiTile lastTarget) {
		this.lastTarget = lastTarget;
	}
	public AiAction getLastAction() {
		return lastAction;
	}

	public void setLastAction(AiAction lastAction) {
		this.lastAction = lastAction;
	}
	
	/** Sommes-nous en train de prevoir l'explosion ? */
	private boolean isPredicting;
	public boolean isPredicting() {
		return isPredicting;
	}
	/** Utilisee lors de prediction de posage des bombes, s'il existe des bombes interdissant le posage des bombes */
	private AiTile prBonusTile;
	public AiTile getPrBonusTile() {
		return prBonusTile;
	}
	public void setPrBonusTile(AiTile prBonusTile) {
		this.prBonusTile = prBonusTile;
	}
	
	/** Utilisee pour l'intertie */
	public class _lastPathChosenParameters
	{
		private AiPath lastPathChosen;
		private int cumulativeInterest,currentPosition;
	
		public _lastPathChosenParameters(AiPath p,int cI,int cP)
		{
			lastPathChosen=p;
			cumulativeInterest=cI;
			currentPosition=cP;
		}
		public _lastPathChosenParameters(AiPath p,int cI)
		{
			lastPathChosen=p;
			cumulativeInterest=cI;
			currentPosition=0;
		}
		public int getCumulativeInterest() {
			return cumulativeInterest;
		}
		public void setCumulativeInterest(int cumulativeInterest) {
			this.cumulativeInterest = cumulativeInterest;
		}
		public int getCurrentPosition() {
			return currentPosition;
		}
		public void setCurrentPosition(int currentPosition) {
			this.currentPosition = currentPosition;
		}
		public AiPath getLastPathChosen() {
			return lastPathChosen;
		}
		public void setLastPathChosen(AiPath lastPathChosen) {
			this.lastPathChosen = lastPathChosen;
		}
	}
	public _lastPathChosenParameters lastPathChosenParameters;
	
	/** Les endroits d'emplacements qui seront ignorees  */
	private HashMap<AiTile, Long> temporarilyIgnoredCases;
	public HashMap<AiTile, Long> getTemporarilyIgnoredCases() {
		return temporarilyIgnoredCases;
	}
	public void setTemporarilyIgnoredCases(
			HashMap<AiTile, Long> temporarilyIgnoredCases) {
		this.temporarilyIgnoredCases = temporarilyIgnoredCases;
	}

	@Override
	public void init() throws StopRequestException {
		super.init();
		MovementController.setMonIA(this);
		GameZone.setMonIA(this);
		mode=Mode.NONE;
		mcontrol=new MovementController();
		bombReleaseState=false;
		lastAction=null;
		lastTarget=null;
		gZone=null;
		lastPathChosenParameters=null;
		temporarilyIgnoredCases=new HashMap<AiTile, Long>();
	}

	/** Cstr pour initializer les variables des autres classes. */
	public KesimalVarol()
	{
		super();
	}

	/** 
	 * mÔøΩthode appelÔøΩe par le moteur du jeu pour obtenir une action de votre IA 
	 * @return Methode principal pour renvoyer une action
	 */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		if(bombReleaseState)
		{
			bombReleaseState=false;
			lastAction=null;
			lastTarget=null;
			lastPathChosenParameters=null;
			//!!
			temporarilyIgnoredCases.put(selfHero.getTile(),Long.valueOf(2000)); //2 sn iptal et.
			return new AiAction(AiActionName.DROP_BOMB);
		}
		
		prBonusTile=null;
		
		zone=getPercepts();
		selfHero=zone.getOwnHero();
		
		updateTemporarilyIgnoredExplosionCases();
		updatePathIfChangedCase();
		
		isPredicting=false;
		
		changeMode();

		/*System.out.print("Speed : ");
		System.out.print(selfHero.getWalkingSpeed());		
		System.out.print(" ");
		System.out.print(zone.getElapsedTime());
		System.out.print("\n");*/
		
		currentToleranceCofficient=200.0*selfHero.getTile().getSize()/selfHero.getWalkingSpeed();
		//System.out.print(currentToleranceCofficient);
		
		gZone=new GameZone(gZone);
		Matrix m=gZone.constructInterestMatrix();
		
		AiAction result = mcontrol.commitMovement(m);
		
		//AiAction result = new AiAction(AiActionName.NONE);
		//uneMethode();
		
		colorizePath(m,mcontrol);
		return result; 
		//return new AiAction(AiActionName.NONE);
	}
	
	/**
	 * Mettre a jour les endroits d'emplacement qui etaient ignores
	 */
	private void updateTemporarilyIgnoredExplosionCases()
	{
		ArrayList<AiTile> keysToBeRemoved = new ArrayList<AiTile>();
	    for(Map.Entry<AiTile,Long> e : temporarilyIgnoredCases.entrySet())
	    {
	    	long val=e.getValue()-zone.getElapsedTime();
	    	if(val<=0)
	    		keysToBeRemoved.add(e.getKey());
	    	else
	    		temporarilyIgnoredCases.put(e.getKey(), val);

	    }
	    for(AiTile key : keysToBeRemoved)
	    {
	    	temporarilyIgnoredCases.remove(key);
	    }
	}
	
	/**
	 * Utilisee par MovementController, pour changer le chemin courant
	 * 
	 * @param p Le chemin
	 * @param cI Importance cumulative (pas utilisee maintenant)
	 */
	public void requestNewPath(AiPath p,int cI)
	{
		lastPathChosenParameters=new _lastPathChosenParameters(p, cI);
	}
	
	/**
	 * On mettra a jour le chemin courant a chaque iteration
	 */
	private void updatePathIfChangedCase() throws StopRequestException
	{
		checkInterruption();
		if(lastPathChosenParameters!=null) {
			//!!//!!
			if(lastPathChosenParameters.getLastPathChosen().getLength()-1==lastPathChosenParameters.getCurrentPosition())
			{
				lastPathChosenParameters=null;
				//!!//!!
			}
			else if(lastPathChosenParameters.getLastPathChosen().getTile(lastPathChosenParameters.getCurrentPosition())!=selfHero.getTile())
			{
				if(lastPathChosenParameters.getLastPathChosen().getTile(lastPathChosenParameters.getCurrentPosition()+1)==selfHero.getTile())
				{
					lastPathChosenParameters.setCurrentPosition(lastPathChosenParameters.getCurrentPosition()+1);
				}
				else {
					lastPathChosenParameters=null;
					//!!//!!
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
		
		//!!//!!
		if(selfHero.getBombRange()==0 || selfHero.getTile().getBombs().size()>0 || m.representation[selfHero.getTile().getLine()][selfHero.getTile().getCol()]<=-275)
		{
			//!!//!!
			////!!
			return false;
		}
		
		m.makeBackup();
		isPredicting=true;
		boolean ret=false;
		
		if(gZone.areBonusAdjacent(m))
		{
			//!!//!!
		}
		else {
			gZone.modifyMatrixWithFutureBomb(m);
			AiAction res=mcontrol.commitMovementForFutureBomb(m);
			if(res.getName()==AiActionName.NONE)
			{
				//!!//!!
			}
			else {
				//!!//!!
				ret=true;
			}
		}
		
		m.restoreBackup();
		isPredicting=false;
		
		if(!ret)
		{
			temporarilyIgnoredCases.put(selfHero.getTile(),Long.valueOf(2000)); //2 sn iptal et.
		}
		return ret;
	}
	
	/**
	 * Si on va laisser une bombe, la case prefereable sera deja determinee grace a cette methode
	 * @param targ
	 */
	public void requestSpecialBombEvasiveTarget(AiTile targ)
	{
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
		return gZone.constructAccesibleCases(m);
	}
	/*
	public AiTile getReachableSafestLocation(Matrix m) throws StopRequestException
	{
		return gZone.constructNearestSafeCases(m);
	}*/
	/**
	 * Utilisee par mode attaque, si on a besoin de detruire les murs.
	 * 
	 * @param m La matrice d'interet sur lequel notre calcul sera basee
	 * @param modifier Valeur a effectuer
	 */
	public void requestWallEffects(Matrix m,int modifier) throws StopRequestException
	{
		gZone.handleWalls(m, modifier);
	}
	
	/**
	 * Determine le mode courant
	 * 
	 */
	private void changeMode() throws StopRequestException
	{
		/*if(selfHero.getBombNumber() <= 2) // || zone.getHiddenItemsCount()+zone.getItems().size() >0)
			mode=Mode.COLLECTE;
		else*/
		//!!//!!
		if((selfHero.getBombNumberMax()-selfHero.getBombNumberCurrent()) >=2 && selfHero.getBombRange() >=3) {
			if(mode==Mode.COLLECTE) {
				//!!//!!
			}
			mode=Mode.ATTAQUE;
		}
		else {
			if(mode==Mode.ATTAQUE) {
				//!!//!!
			}
			mode=Mode.COLLECTE;
		}
		//!!
		//mode=Mode.ATTAQUE;
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
	private void colorizePath(Matrix m,MovementController mc) throws StopRequestException
	{
		checkInterruption();
		m.colorizeMapAccordingly();
		mc.drawPathOnScreen();
	}
}
