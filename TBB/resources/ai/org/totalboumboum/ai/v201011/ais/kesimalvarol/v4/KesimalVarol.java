package org.totalboumboum.ai.v201011.ais.kesimalvarol.v4;

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
/**
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 */
@SuppressWarnings("deprecation")
enum Mode {
	NONE,COLLECTE,ATTAQUE
}

@SuppressWarnings("deprecation")
public class KesimalVarol extends ArtificialIntelligence
{	
	boolean verbose=false;//true;
	
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
	
	/** Renvoie le delai d'explosion d'une bombe imaginaire
	 * 
	 * @return Delai d'explosion
	 */
	public double getUsualBombNormalDuration()
	{
		AiBomb refer=selfHero.getBombPrototype();
		return refer.getNormalDuration()/1000.0; //2.5 ?
	}
	
	/** Determine si l'IA va poser une bombe */
	private boolean bombReleaseState;
	public void willReleaseBomb()
	{
		bombReleaseState=true;
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
			lastPathChosenParameters=null;
			if(verbose) System.out.println("Dropped");
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

		/*if(verbose) System.out.print("Speed : ");
		if(verbose) System.out.print(selfHero.getWalkingSpeed());		
		if(verbose) System.out.print(" ");
		if(verbose) System.out.print(zone.getElapsedTime());
		if(verbose) System.out.print("\n");*/
		
		currentToleranceCofficient=200.0*selfHero.getTile().getSize()/selfHero.getWalkingSpeed();
		//if(verbose) System.out.print(currentToleranceCofficient);
		
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
	private void updateTemporarilyIgnoredExplosionCases() throws StopRequestException
	{
		checkInterruption();
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
		isPredicting=true;
		boolean ret=false;
		
		if(gZone.areBonusAdjacent(m))
		{
			if(verbose) System.out.println("Bonuses found");
		}
		else {
			gZone.modifyMatrixWithFutureBomb(m);
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
		return gZone.constructAccesibleCases(m);
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
		gZone.handleWalls(m, modifier);
	}
	
	/**
	 * Determine le mode courant
	 * 
	 */
	private void changeMode() throws StopRequestException
	{
		checkInterruption();
		/*if(selfHero.getBombNumber() <= 2) // || zone.getHiddenItemsCount()+zone.getItems().size() >0)
			mode=Mode.COLLECTE;
		else*/
		if(verbose) System.out.println("Mode change\n"+(selfHero.getBombNumberMax()-selfHero.getBombNumberCurrent())+","+selfHero.getBombRange());
		if((selfHero.getBombNumberMax()-selfHero.getBombNumberCurrent()) >=2 && selfHero.getBombRange() >=3) {
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
