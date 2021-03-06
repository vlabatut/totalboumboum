package org.totalboumboum.ai.v200809.ais.demiragsagar.v2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiItem;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.ai.v200809.ais.demiragsagar.v2.AStar;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Doğus Burcu Demirağ
 * @author Zeynep Şagar
 *
 */
@SuppressWarnings("deprecation")
public class DemiragSagar extends ArtificialIntelligence {

	//les variables globales qui sont mise a jour a chaque iteration
	/** */
	private int state;
	/** */
	private Direction d;
	/** */
	private AiZone zone;
	/** */
	private AiHero ownHero;
	/** */
	private AiTile caseCourant;
	/** */
	private AiTile caseTarget;
	/** */
	private boolean debug;
	/** */
	private List<AiTile> caseEnemies;
	/** */
	private List<AiTile> caseBombes;
	/** */
	private List<AiTile> caseItems;
	/** */
	private TimeMatrice timeMatrice;
	/** */
	private long dangerTime;
	/** */
	private int distanceTarget;
	/** */
	private int counter;

	/**
	 * 
	 */
	public DemiragSagar() {
		//l'initialisation des variables globales
		this.state = 0;
		this.d = Direction.NONE;
		this.caseEnemies = new ArrayList<AiTile>();
		this.caseBombes = new ArrayList<AiTile>();
		this.caseItems = new ArrayList<AiTile>();
		this.timeMatrice = null;
		this.debug = false;
		this.dangerTime=60;
		this.distanceTarget=75;
		this.counter=0;
	}

	@Override 
	public AiAction processAction() throws StopRequestException {
		checkInterruption();
		AiAction result = new AiAction(AiActionName.NONE);
		// je recoit les aspects de la zone
		this.zone = getPercepts();
		this.ownHero = this.zone.getOwnHero();
		this.caseCourant = this.ownHero.getTile();
		// les cases ou se trouvent les enemies
		this.caseEnemies = getEnemiesTile();
		if (this.caseEnemies.isEmpty())
			// il n' y a plus d'enemies
			// nous avons gagne
			this.state = -1;
		this.caseBombes = getBombesTile();
		this.caseItems = getItemsTile();
		this.calculeZoneAspect(this.zone);
		if (this.state != 0){
			timeMatrice.updateTimeMatrice(this.caseBombes);
			//this.timeMatrice.printTimeMatrice();
		}
		
		if(this.debug)
			System.out.println("State: "+this.state+" La case Courant est: "+caseCourant.toString());
		switch (this.state) {
		case -1: {
			// quand il ne reste plus d'enemies
			// nous avons gagne
			result = new AiAction(AiActionName.NONE);
			break;
		}
		case 0: {
			/*
			 * quand la classe est juste cree
			 */
			//seulement a l'inisialisation
			//on creer la matrice de temps restant des bombes
			this.timeMatrice=new TimeMatrice(this,this.zone,ownHero.getBombRange());
			this.timeMatrice.updateTimeMatrice(caseBombes);
			this.state=4;
			break;
		}
		case 4: {
			/*
			 * Y a t-il un danger au case courant? Danger = une bombe ou un fire
			 * Si il y a, aller au cas 5 Sinon aller au cas 6
			 */		
			if (this.timeMatrice.getTime(caseCourant) > 0) // S'il ya un danger
				this.state = 5;
			else
				this.state = 6;
			break;
		}
		case 5: {
			/*
			 * Tester si on peut se cacher qquepart
			 */
			if (this.seCacher()) {
				this.state = 500;
			} else {
				if(this.caseTarget==null){
					//sinon penser de nouveau
					this.state=4;
					result=new AiAction(AiActionName.NONE);					
				}
				else{	
				this.state = 500;
				}
			}
			break;
		}
		case 6: {
			/*
			 * Y a t il un item?
			 * Si il existe, aller a 7
			 * Sinon a 8
			 */
			int min=-1;
			AiTile minTile=null;
			if (this.caseItems.size()==0)
				this.state = 8;
		    else {
		    	for(AiTile temp: caseItems)
		    	{
		    		//La distance entre nous et l'item
		    		int valeur=Functions.trouveDistance(this.ownHero.getTile(), temp);	
		    		if(valeur!=-1){
		    			//Il existe un chemin
		    			if(min==-1 || min>valeur)
		    			{	
		    				minTile=temp;
		    				min=valeur;
		    			}
		    		}
		    	}
		    	if(minTile==null){
		    		/*
		    		 * Il existe un item
		    		 * Mais on ne possede pas un chemin
		    		 */
		    		this.state=78;
		    	}
		    			
		    	else {
		    		/*
		    		 * Qui est plus proche?
		    		 * Aller a 7 pour le decider
		    		 */
		    		this.caseTarget = minTile;
		    		this.state = 7;
		    	}
		    }
			break;
		}
		case 7: {
			
			int notreDistance = Functions.trouveDistance(caseCourant,caseTarget);
			if (notreDistance == -1) {
				/*
				 * Nous ne possedont pas un chemin a l'item
				 * On va exploser un mur
				 */
				if(this.debug)
					System.out.println("Duvar patlatilacak");
				this.state = 78;
			}	
			else {
				AiTile closer=Functions.TheCloserTile(this.caseTarget, this.caseEnemies);
				int closerDist=Functions.trouveDistance(closer, this.caseTarget);
				if(this.debug)
					System.out.println("distnce nous :"+notreDistance+", lui:"+closerDist);
				if(notreDistance >= closerDist && closerDist!=-1){
					/*
					 * Si le enemy est plus proche au item
					 * On va essayer de l'empecher
					 */
					this.state = 10;
				}
				else {
					if(!dangerOnTheTrack(this.caseTarget,false)){
						//aller au item
						this.state = 500;
					}
					else
						this.state=4;//penser a nouveau
				}
			}			
			break;
		}		
		case 10: {
			AiTile closer=Functions.TheCloserTile(this.caseTarget, this.caseEnemies);
			if(closer!=null){
				//compare les chemin entre nous-item et enemie-item
				//envoie l'intersection
				this.caseTarget=this.EnemyAtTheGate(closer);
			}
			if(this.caseTarget!=null)
				this.state=501;
			else 
				this.state=4;
			break;
		}		
		case 8: {		
			if (this.FindSoftWallNumber()!=0)
				this.state=78;
			else this.state=4; //rien a faire recommencer
			break;
		}
		case 78:{
			if(Functions.ChildNodes(this.caseCourant)==1) {
				//regarde les cases voisins
				//met une bombe si ==1
				if(supposerBombe(this.caseCourant))
					result=new AiAction(AiActionName.DROP_BOMB);
				this.state=4;//penser a nouveau
				this.caseTarget=null;
				break;
			}
			if(this.caseTarget==null)
				caseTarget=this.caseEnemies.get(0); 
			AStar a=new AStar(this.caseCourant,this.caseTarget);
			a.formeArbre();
			List<Node>fils=a.getFils();
			/*
			 * On explose les murs qui nous empeche
			 * a aller a l'enemie
			 * 
			 */
			if(!fils.isEmpty()) {
				int min=-1;
				for(Node temp:fils)
					if(this.timeMatrice.getTime(temp.getTile())==0) {
						int tempDistance=Functions.trouveDistance(caseCourant, temp.getTile());
						if(min==-1 || min>tempDistance)
							if(this.supposerBombe(temp.getTile())){
								min=tempDistance;							
								this.caseTarget=temp.getTile();
								if(!dangerOnTheTrack(this.caseTarget,true))
									this.state=501;
								else
									this.state=4;
								break;
							}
				}				
			}
			if(this.state==78) {
				if(this.supposerBombe(caseCourant)){
					//rien a faire, laisser une bombe
					result=new AiAction(AiActionName.DROP_BOMB);
					this.state=4;
					break;
				}
				this.state=78;
			}
			break;
		}
		case 501: 
		case 500:
		{
			//Former un chemin selon le target que l'on a choisie
			if(this.caseTarget!=null && !this.caseTarget.getBombs().isEmpty()){
			state=4;
			}
			if (this.caseTarget != null)
				if(this.debug)
					System.out.println("Case Target: "+caseTarget.toString());
			if (!estCaseCible()) {
				counter++;
				AiTile nextCase = null;
				AStar arbre = new AStar(this.caseCourant, this.caseTarget, false);
				arbre.formeArbre();
				// Il n'y a pas de chemin entre la case courant et la case qu'on
				// veut aller
				LinkedList<Node> path = arbre.getPath();
				if (path == null) {
					if(Functions.memeCordonnes(caseCourant, caseTarget))
						//System.out.println("ERREUR: Pas de chemin");
					break;
					}
				else {
					Iterator<Node> it = path.iterator();
					if (it.hasNext())
						nextCase = it.next().tile;
					else {
						this.state=-5;
						//System.out.println("ERREUR: Nous sommes au case cible");
						break;
					}
					if(this.dangerOnTheTrack(this.caseTarget,false)) {
						this.state=4;
						break;
					}
					//System.out.println("counter: "+counter);
					if(counter>this.distanceTarget){
						counter=0;
						this.state=4;
						break;
						}
					if (this.debug)
						System.out.println("nextCase: " + nextCase);
					this.d = getPercepts().getDirection(this.caseCourant,nextCase);
					result = new AiAction(AiActionName.MOVE, this.d);
				}
			} else {
				counter=0;
				if(this.state == 501){
					if(supposerBombe(this.caseCourant)){
						result = new AiAction(AiActionName.DROP_BOMB);
						this.timeMatrice.setDefaultPortee(this.ownHero.getBombRange());
						this.timeMatrice.placerNouvelleBombe(this.caseCourant);
					}
					
				}
				else result = new AiAction(AiActionName.NONE);
				this.state = 4;
				this.caseTarget=null;
			}
			break;
		}
		default:
			if(this.debug)
			System.out.println("Bulunamayan State:"+this.state);
			result = new AiAction(AiActionName.NONE);
			break;
		}
		return result;
	}
	
	/**
	 * Prend l'intersection entre le chmein du enemie-item
	 * et de nous-item
	 * @param enemy 
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public AiTile EnemyAtTheGate(AiTile enemy)
	{
		try {
			checkInterruption();
		} catch (StopRequestException e) {
			//e.printStackTrace();
		}
		AiTile result=null;
		AStar arbre1=new AStar(this.ownHero.getTile(),this.caseTarget);
		arbre1.formeArbre();
		//		Il n'y a pas de chemin entre la case courant et la case qu'on veut aller
		if(arbre1.getPath()==null) 
			return null;
		else {
			if(Functions.memeCordonnes(caseTarget,enemy))
				if(debug)
					System.out.println("enemy ile ayni case");
			AStar arbre2 = new AStar(enemy,this.caseTarget);
			arbre2.formeArbre();
			if(arbre2.getPath()==null)
				return null;
			Node tempNode1 = new Node(this.caseTarget);
			Node tempNode2 = new Node(this.caseTarget);
			while(tempNode1.memeCoordonnees(tempNode2)&& tempNode1!=null && tempNode2!=null ) {
				result=tempNode1.getTile();
				if(!tempNode1.memeCoordonnees(new Node(arbre1.firstTile)) && !tempNode2.memeCoordonnees(new Node(arbre2.firstTile))){
				tempNode1=arbre1.getParentLink(tempNode1).getOrigin();
				tempNode2=arbre2.getParentLink(tempNode2).getOrigin();
				}
				else
					break;
			}
			
		}
		return result;
	}
	/**
	 * On suppose de mettre une bombe
	 * On regarde si on sera en danger
	 * @param temp 
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * 
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public boolean supposerBombe(AiTile temp) throws StopRequestException{
		checkInterruption();
		
		int i,j;
		boolean resultat=false;
//		long nouveauTime [][]= new long[17][15];
		long nouveauTime [][]= new long[zone.getHeight()][zone.getWidth()]; //adjustment
//		for (j = 0; j < 15; j++)
		for (i = 0; i < zone.getHeight(); i++) //adjustment
//			for (i = 0; i < 17; i++)
			for (j = 0; j < zone.getWidth(); j++) //adjustment
			{
				nouveauTime[i][j]=this.timeMatrice.getTime(i,j);
			}
		this.timeMatrice.placerNouvelleBombe(temp);
		if(this.seCacher(true))
			resultat=true;
//		for (j = 0; j < 15; j++)
		for (i = 0; i < zone.getHeight(); i++) //adjustment
//			for (i = 0; i < 17; i++)
			for (j = 0; j < zone.getWidth(); j++) //adjustment
			{
				this.timeMatrice.putTime(i,j,nouveauTime[i][j]);
			}
		return resultat;
	}
	/**
	 * Mettre a jour les tiles des enemies
	 * @return
	 * 		Description manquante !
	 */
	public List<AiTile> getEnemiesTile() {
		try {
			checkInterruption();
		} catch (StopRequestException e) {
			//e.printStackTrace();
		}

		List<AiTile> monItera = new ArrayList<AiTile>();

		for (AiHero i : this.zone.getHeroes()) {
			if (this.debug)
				System.out.println("La couleur de l'enemy: " + i.getColor());
			try {
				checkInterruption();
			} catch (StopRequestException e) {
				//e.printStackTrace();
			}
			if (i.getColor() != this.ownHero.getColor())
				if (i != null)
					monItera.add(i.getTile());
		}
		return monItera;
	}

	/**
	 * Mettre a jour les tiles des bombes
	 * @return
	 * 		?
	 */
	public List<AiTile> getBombesTile() {
		List<AiTile> b = new ArrayList<AiTile>();
		try {
			checkInterruption();
		} catch (StopRequestException e) {
			//e.printStackTrace();
		}
		if (this.zone.getBombs() != null)
			for (AiBomb i : this.zone.getBombs()) {
				try {
					checkInterruption();
				} catch (StopRequestException e) {
					//e.printStackTrace();
				}
				if(!i.getTile().getBombs().isEmpty())
					b.add(i.getTile());
			}
		return b;
	}

	/**
	 * Mettre a jour les tiles des items
	 * @return
	 * 		?
	 */
	public List<AiTile> getItemsTile() {
		List<AiTile> p = new ArrayList<AiTile>();
		try {
			checkInterruption();
		} catch (StopRequestException e) {
			//e.printStackTrace();
		}
		if (this.zone.getItems() != null)
			for (AiItem i : this.zone.getItems()) {
				try {
					checkInterruption();
				} catch (StopRequestException e) {
					//e.printStackTrace();
				}
				if(i.getTile().getItem()!=null)
					p.add(i.getTile());
			}
		return p;
	}

	/**
	 * 
	 * @param zone
	 * 		Description manquante !
	 */
	public void calculeZoneAspect(AiZone zone) {
		try {
			checkInterruption();
		} catch (StopRequestException e) {
			//e.printStackTrace();
		}
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean seCacher(){
		return seCacher(false);
	}
	/**
	 * Chercher une case pour se cacher
	 * @param poserBombe 
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public boolean seCacher(boolean poserBombe) {
		try {
			checkInterruption();
		} catch (StopRequestException e) {
			//e.printStackTrace();
		}
		AiTile petit = null;
		boolean fuir = false;
		int min = 10000, temp, i, j;
//		for (j = 0; j < 15; j++)
		for (i = 0; i < zone.getHeight(); i++) //adjustment
//			for (i = 0; i < 17; i++)
			for (j = 0; j < zone.getWidth(); j++) //adjustment
				if(!Functions.memeCordonnes(zone.getTile(i,j), this.caseCourant))
					if ( this.timeMatrice.getTime(i,j)==0) {//bulacakkkk
						temp = Functions.trouveDistance(caseCourant,this.zone.getTile(i, j));
						if (temp > 0 && min > temp) 
							if(!dangerOnTheTrack(this.zone.getTile(i, j),poserBombe)) {
								min = temp;
								petit = this.zone.getTile(i, j);
								fuir = true;
							}
					}
		this.caseTarget = petit;
		return fuir;
	}
	/**
	 * Regarde si il existe un danger sur le chemin chosit
	 * @param target 
	 * 		Description manquante !
	 * @param placerBombe 
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public boolean dangerOnTheTrack(AiTile target,boolean placerBombe) {
		try {
			checkInterruption();
		} catch (StopRequestException e) {
			//e.printStackTrace();
		}
		boolean flag=false;
		AStar arbre=new AStar(this.caseCourant,target);
		arbre.formeArbre();
		LinkedList<Node> path=arbre.getPath();
		if(path==null)
			return true;
		int dangerSize=path.size();
		if(placerBombe)
			dangerSize=dangerSize+5;
		for(Node tempNode:path) {
			if(this.timeMatrice.getTime(tempNode.getTile()) < dangerSize*(this.dangerTime*3) && this.timeMatrice.getTime(tempNode.getTile())!=0)
				flag=true;
		}
		return flag;
	}

	/**
	 * Regarde si nous sommes arrive a la case cible
	 * @return
	 * 		Description manquante !
	 */
	public boolean estCaseCible() {
		try {
			checkInterruption();
		} catch (StopRequestException e) {
			//e.printStackTrace();
		}
		// fonction verifiant si l'ia est arrive a la case cible
		boolean resultat = false;
		if (this.ownHero.getTile().getCol() == this.caseTarget.getCol()	&& this.ownHero.getTile().getLine() == this.caseTarget.getLine())
			resultat = true;
		return resultat;

	}

	/**
	 * Compte les nombres des SoftWalls restant
	 * @return
	 * 		Description manquante !
	 */
	public int FindSoftWallNumber() {
		try {
			checkInterruption();
		} catch (StopRequestException e) {
			//e.printStackTrace();
		}
		int mat[][] = this.timeMatrice.getBombMatrice(this.zone);
		int sommeHardWall = 0;
//		for (j = 0; j < 15; j++)
		for (int i = 0; i < zone.getHeight(); i++) //adjustment
//			for (i = 0; i < 17; i++)
			for (int j = 0; j < zone.getWidth(); j++) //adjustment
//				if (mat[j][i] == -1) {
				if (mat[i][j] == -1) { //adjustment
//					AiTile temp = this.zone.getTile(i, j);
					AiTile temp = this.zone.getTile(i, j); //adjustment
					if (!temp.getBlock().isDestructible())
						sommeHardWall++;
				}
		return sommeHardWall;
	}
}
