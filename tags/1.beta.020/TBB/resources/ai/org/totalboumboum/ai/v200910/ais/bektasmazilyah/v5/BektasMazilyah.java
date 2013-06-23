package org.totalboumboum.ai.v200910.ais.bektasmazilyah.v5;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 5
 * 
 * @author Erdem Bektas
 * @author Nedim Mazilyah
 *
 */
public class BektasMazilyah extends ArtificialIntelligence
{	
	/**Zone du jeu*/
	private AiZone zone;
	
	/** path qu'on va suivre*/
	Astar as;
	
	/** le personnage dirigé par cette IA*/
	private AiHero hero;
	
	/** la case occupée actuellement par le personnage */
	private AiTile currentTile;
	
	/** la case prochaine sur path*/
	private AiTile nextTile;
	
	/** la case sur laquelle on veut aller*/
	public AiTile targetTile=null;
	
	/** pour controller danger*/
	boolean isDanger = false;
	
	/** action qu'on va faire*/
	AiAction result = new AiAction(AiActionName.NONE);
	
	/** condition de deposser une bombe */
	private boolean dropBomb=false;
	
	/**pour controller l'atteindre a une rival */
	private boolean canGoRival=false;
	
	/** on va l'utiliser pour preciser les actions*/
	private DangerZone dangerZone;
	
	/** condition de prendre une bonus*/
	boolean takeBonus = true;
	
	/** Direction de l'action */
	Direction moveDir = Direction.NONE;
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	@Override
	public AiAction processAction() throws StopRequestException
	{	//Appel Obligatoire
		checkInterruption();
		
		// initialisation
		if(hero==null)
			initMonAi();
		//creeation du dangerZone
		dangerZone= new DangerZone(zone,this);
		
		// si ownHero est null, ça veut dire l'IA est morte : inutile de continuer
		if(hero != null)
		{
			updateLocation(hero.getTile());
			//on controlle si'il y a danger
			checkDanger();
			if(isDanger)
			{//Il y a un danger, il faut se sauver.
				// on precise la tile prochaine en utilisant la methode suivre
				suivre();
				if(nextTile!=null){
					moveDir = zone.getDirection(currentTile, nextTile);
					}
				else
					moveDir=Direction.NONE;
			}
			//Il n'y a aucun danger
			else
			{
				// S'il y a une adversaire proche a nous et si on peut deposer une bombe, on la depose 
				dropBomb=canDrop();
				if(isRivalInRange() &&  dropBomb)
				{
					dropBomb=false;
					result = new AiAction(AiActionName.DROP_BOMB);
					return result;
				}
				
				else{
					// Si on n'est pas assez armé et s'ils existent des bonus ouvert, on part pour les prendre
					mustTakeBonus();
					if(bonusExiste() && takeBonus)
					{
						goToBonus();
						if(nextTile!=null)
							moveDir = zone.getDirection(currentTile, nextTile);
					}
					else 
					{
						// On controle s'il y a des adversaires accecibles
						canGoToRival();
						// Si on ne peut pas acceder a une enemie, on commance a detruir des murs
						if(destructExiste() && !canGoRival)
						{
							//on precise la tile prochaine en utilisant la methode detruir 
							detruir();				
							if(nextTile!=null)
							{
								moveDir=zone.getDirection(currentTile, nextTile);
								dropBomb=canDrop();
								//si on est arrivé a targetTile et si on peut deposer des bombes, on depose des bombes pour detruir des murs 
								if(targetTile==currentTile && dropBomb){
									dropBomb=false;
									result = new AiAction(AiActionName.DROP_BOMB);
									return result;			
								}
							}
						
						}
						else
						{
							{
								//si on peut acceder a une ou plusieur adversaires, on precise la tile prochaine pour aller a adversaire 
								goToRival();
								if(nextTile!=null)
									moveDir = zone.getDirection(currentTile, nextTile);									
							}
											
				}
			}
			}}
			// on met à jour la direction renvoyée au moteur du jeu
			result = new AiAction(AiActionName.MOVE,moveDir);
		}	
		return result;	
	}
	
	/********************************************************************************
	 * la méthode qui precise la tile prochaine pour atteindre a une adversaire
	 * @throws StopRequestException
	 */

	private void goToRival() throws StopRequestException 
	{
		checkInterruption();
		AiTile tile = null;
		List<AiTile> l = dangerZone.findRivalsTiles();
		List<AiTile> li =aligner(l);
		Iterator<AiTile> it =li.iterator();
		if (it.hasNext())
		{
			checkInterruption();
			AiTile kare=it.next();
			as=new Astar (dangerZone,hero.getCol(),hero.getLine(),kare.getCol(),kare.getLine());		
			if (as.findSecurePath())
			{	
				Deque<Integer> deque = as.getPath();
				if(!deque.isEmpty()) {
					int tempx = deque.poll(), tempy = deque.poll();
					tile= zone.getTile(tempy,tempx );
				}
				if (tile ==hero.getTile()){
				if (!deque.isEmpty()) {
						int  tempx = deque.poll(); int tempy = deque.poll();
						 tile= zone.getTile(tempy,tempx );
					}
				}
			}
			nextTile=tile;
		}	
}

	/*******************************************
	 * La méthode qu'on va utiliser pour connaitre s'il ya une adversaire proche a nous
	 * @return si rival est proche a nous
	 * @throws StopRequestException
	 */
	private boolean isRivalInRange() throws StopRequestException
	{
		checkInterruption();
		List<AiHero> list =zone.getRemainingHeroes();
		if(list.contains(hero))
			list.remove(hero);
		List<AiTile> li = preciserRange();
		Iterator<AiHero> it = list.iterator();
		AiTile temp =null;
		boolean result=false;
		while(it.hasNext())
		{
			checkInterruption();
			temp=(it.next()).getTile();
			if(li.contains(temp))
				result=true;
		}
 		return result;
	}
		
	/************************
	 * La methode qu'on va utiliser pour preciser si on doit prendre une bonus
	 * @throws StopRequestException
	 */
	private void mustTakeBonus() throws StopRequestException {
		checkInterruption();
		if(hero.getBombRange() >5 || hero.getBombNumber()>2)
			takeBonus=false;
		else
			takeBonus=true;
}
	

	/******************************************************************************
	 * La méthode qu'on va utiliser pour preciser la tile prochaine quand on suit
	 * @throws StopRequestException
	 */
	private void suivre() throws StopRequestException
	{
		checkInterruption();
		AiTile tile = null;
		List<AiTile> l = dangerZone.findSafeTiles();
		List<AiTile> li =aligner(l);
		Iterator<AiTile> it =li.iterator();
		if (it.hasNext())
		{
			checkInterruption();
			AiTile kare=it.next();
			as=new Astar (dangerZone,hero.getCol(),hero.getLine(),kare.getCol(),kare.getLine());		
			if (as.findPath())
			{	
				Deque<Integer> deque = as.getPath();
				if(!deque.isEmpty()) {
				int tempx = deque.poll(), tempy = deque.poll();
				tile= zone.getTile(tempy,tempx );
				
			}
			if (tile ==hero.getTile()){
			if (!deque.isEmpty()) {
					int  tempx = deque.poll(); int tempy = deque.poll();
					 tile= zone.getTile(tempy,tempx );
			}
		}}
			nextTile=tile;
		}	
	}
	
	
	/**********************************************************************************************
	 * La methode qu'on va utiliser pour connaitre si on peut deposer une bombe a ce moment la
	 * @return si on peut deposer une bombe
	 * @throws StopRequestException
	 */
	public boolean canDrop() throws StopRequestException
	{
		checkInterruption();
		List<AiTile> neighbors = preciserRange();
		List<AiTile> result = removeFromList(neighbors);
		Iterator<AiTile> it = result.iterator();
		AiTile temp=null;
		int count=0;
		if(result.size()>0){
			while(it.hasNext())
			{
				checkInterruption();
				temp=it.next();
				int col=temp.getCol();
				int line = temp.getLine();
				Astar a=new Astar (dangerZone,hero.getCol(),hero.getLine(),col,line);
				if ((hero.getCol()!= col || hero.getLine() != line) && a.findSecurePath())
					count++;
			}
		}
		else
			dropBomb=false;
		if(count>=1)
			dropBomb=true;
	//	else
	/**	{
			count=0;
			ArrayList<AiHero> heroes = new ArrayList<AiHero>();
			Iterator<AiHero> itHero = heroes.iterator();
			while(itHero.hasNext()){
				temp=itHero.next().getTile();
				Astar a=new Astar (dangerZone,hero.getCol(),hero.getLine(),temp.getCol(),temp.getLine());
				if ((hero.getCol()!= temp.getCol() || hero.getLine() != temp.getLine()) && !a.findPath())
					count++;
			}
			if(count>0)
				dropBomb=true;
		}*/
	/*	if(count==1)
		{
			count=0;
			temp=hero.getTile();
			AiTile temp1= temp.getNeighbor(Direction.UP);
			if(dangerZone.getValeur(temp1.getCol(),temp1.getLine())== EtatEnum.BLOCDEST || dangerZone.getValeur(temp1.getCol(),temp1.getLine())== EtatEnum.BLOCINDEST)
				count++;
			temp1= temp.getNeighbor(Direction.DOWN);
			if(dangerZone.getValeur(temp1.getCol(),temp1.getLine())== EtatEnum.BLOCDEST || dangerZone.getValeur(temp1.getCol(),temp1.getLine())== EtatEnum.BLOCINDEST)
				count++;
			temp1= temp.getNeighbor(Direction.RIGHT);
			if(dangerZone.getValeur(temp1.getCol(),temp1.getLine())== EtatEnum.BLOCDEST || dangerZone.getValeur(temp1.getCol(),temp1.getLine())== EtatEnum.BLOCINDEST)
				count++;
			temp1= temp.getNeighbor(Direction.LEFT);
			if(dangerZone.getValeur(temp1.getCol(),temp1.getLine())== EtatEnum.BLOCDEST || dangerZone.getValeur(temp1.getCol(),temp1.getLine())== EtatEnum.BLOCINDEST)
				count++;
			if(count>=3)
				dropBomb=true;
		}*/
		return dropBomb;		
	}

	/****************************************************************************************
	 * La methode qu'on va utiliser pour preciser les tiles qui se trouvent dans notre range
	 * @return list des tiles in range
	 * @throws StopRequestException
	 */
	
	private List<AiTile> preciserRange() throws StopRequestException
	{
		checkInterruption();
		AiTile temp=hero.getTile();
		List<AiTile> result = new ArrayList<AiTile>();
		result.add(temp);
		EtatEnum etat = EtatEnum.LIBRE;
		int range = 3;
		int k=0;
		int x=0;
		int y=0;
		boolean up=true;
		boolean down=true;
		boolean	right=true;
		boolean left=true;
		while(k<range && up)
		{
			checkInterruption();
			temp=temp.getNeighbor(Direction.UP);
			x=temp.getCol();
			y=temp.getLine();
			etat=dangerZone.getValeur(x, y);
			if(etat!=EtatEnum.BLOCDEST && etat!=EtatEnum.BLOCINDEST && etat!=EtatEnum.BONUSBOMBE && etat!=EtatEnum.BONUSFEU)
				result.add(temp);
			else 
				up=false;
			k++;
		}
		k=0;
		temp=hero.getTile();
		while(k<range && down)
		{
			checkInterruption();
			temp=temp.getNeighbor(Direction.DOWN);
			x=temp.getCol();
			y=temp.getLine();
			etat=dangerZone.getValeur(x, y);
			if(etat!=EtatEnum.BLOCDEST && etat!=EtatEnum.BLOCINDEST && etat!=EtatEnum.BONUSBOMBE && etat!=EtatEnum.BONUSFEU)
				result.add(temp);
			else 
				down=false;
			k++;
		}
		k=0;
		temp=hero.getTile();
		while(k<range && right)
		{
			checkInterruption();
			temp=temp.getNeighbor(Direction.RIGHT);
			x=temp.getCol();
			y=temp.getLine();
			etat=dangerZone.getValeur(x, y);
			if(etat!=EtatEnum.BLOCDEST && etat!=EtatEnum.BLOCINDEST && etat!=EtatEnum.BONUSBOMBE && etat!=EtatEnum.BONUSFEU)
				result.add(temp);
			else 
				right=false;
			k++;
		}
		k=0;
		temp=hero.getTile();
		while(k<range && left)
		{
			checkInterruption();
			temp=temp.getNeighbor(Direction.LEFT);
			x=temp.getCol();
			y=temp.getLine();
			etat=dangerZone.getValeur(x, y);
			if(etat!=EtatEnum.BLOCDEST && etat!=EtatEnum.BLOCINDEST && etat!=EtatEnum.BONUSBOMBE && etat!=EtatEnum.BONUSFEU)
				result.add(temp);
			else 
				left=false;
			k++;
		}
			
		return result;
	}
		
	/*******************************************************************************
	 * La methode qu'on va utiliser pour faire sortir une liste d'un autre list
	 * @param list
	 * @return list des tiles
	 * @throws StopRequestException
	 */
	
	private List<AiTile> removeFromList(List<AiTile> list) throws StopRequestException
	{
		checkInterruption();
		List<AiTile> result = dangerZone.findSafeTiles();
		Iterator<AiTile> it = list.iterator();
		AiTile temp = null;
		while(it.hasNext())
		{
			checkInterruption();
			temp=it.next();
			if(result.contains(temp))
				result.remove(temp);
		}
		return result;
	}
	
	/******************************************************************************************
	 * La methode qui aligne des elements d'une liste
	 * @param source
	 * @return
	 * @throws StopRequestException
	 */
	public List<AiTile> aligner(List<AiTile> source) throws StopRequestException
	{
		checkInterruption();
		List<AiTile> src = source;
		List<AiTile> target = new ArrayList<AiTile>();
		Iterator<AiTile> it = source.iterator();
		AiTile temp;
		int max=0;
		while(it.hasNext())
		{
			checkInterruption();
			temp=it.next();
			Astar as = new Astar(dangerZone,hero.getCol(),hero.getLine(),temp.getCol(),temp.getLine());
			if(as.findPath())
			{
				Deque<Integer> deq = as.getPath();
				if(deq.size()>=max)
					max=deq.size();
			}
		}
		
		for (int i=0; i<=max; i++)
		{
			checkInterruption();
			Iterator<AiTile> ite = src.iterator();
			while(ite.hasNext())
			{
				checkInterruption();
				temp=ite.next();
				Astar a = new Astar (dangerZone,hero.getCol(),hero.getLine(),temp.getCol(),temp.getLine());
				if(a.findPath())
				{
					Deque<Integer> deq = a.getPath();
					if((deq.size())==i)
						target.add(temp);
				}
			}
			
		}
		return target;
	}

	/***********************************************************************
	 ****La methode qui precise la tile prochaine pour aller a une bonus****
	 **** @throws StopRequestException**************************************
	 */
	private void goToBonus() throws StopRequestException
	{		
		checkInterruption();
		AiTile tile = null;
		List<AiTile> l = dangerZone.findBonusTiles();
		List<AiTile> li = aligner(l);
		Iterator<AiTile> it =li.iterator();
		if (it.hasNext())
		{
			AiTile kare=it.next();
			{
				as=new Astar (dangerZone,hero.getCol(),hero.getLine(),kare.getCol(),kare.getLine());
		
				if (as.findSecurePath())
				{	
					Deque<Integer> deque = as.getPath();
					if(!deque.isEmpty()) {
					int tempx = deque.poll(), tempy = deque.poll();
					tile= zone.getTile(tempy,tempx );					
				}
				if (tile == hero.getTile()){
					if (!deque.isEmpty()) {
						int  tempx = deque.poll(); int tempy = deque.poll();
						tile= zone.getTile(tempy,tempx );
					}
				}
				}
			}
				nextTile=tile;
		}
	}
	
	/*****************************************************************
	 * La methode qui precise la tile prochaine pour aller a une bonus
	 * @throws StopRequestException 
	 */
	private void detruir() throws StopRequestException
	{
		
		checkInterruption();
		List<AiTile> a= dangerZone.findDesctructibleTiles();
		List<AiTile> b= findClearNeighbors(a);
		List<AiTile> dst= dangerZone.findTilesForDestruct(b);
		List<AiTile> dest=aligner(dst);
		AiTile tile = null;
		AiTile kare=null;
		Iterator<AiTile> it =dest.iterator();
		if (it.hasNext())
		{
			if(!hero.getTile().equals(targetTile))
				kare=it.next();
			else 
				kare=null;
			if(kare!=null){
			as=new Astar (dangerZone,hero.getCol(),hero.getLine(),kare.getCol(),kare.getLine());
			if (as.findSecurePath())
			{	
				Deque<Integer> deque = as.getPath();
				findTargetTile(deque);
				Deque<Integer> deq = as.getPath();
				if(!deq.isEmpty()) {
				int tempx = deq.poll(), tempy = deq.poll();
				tile= zone.getTile(tempy,tempx );
			}
			if (tile ==hero.getTile()){
			if (!deq.isEmpty()) {
					int  tempx = deq.poll(); int tempy = deq.poll();
					 tile= zone.getTile(tempy,tempx );
			}}}
				nextTile=tile;
		}}
		else
			nextTile=currentTile;
	}

	
	/********************************************************
	 * La methode pour trouver les voisins claires d'une list  
	 * @param list
	 * @return list
	 * @throws StopRequestException
	 */
	public List<AiTile> findClearNeighbors(List<AiTile> list) throws StopRequestException
	{
		checkInterruption();
		List<AiTile> li = list;
		List<AiTile> clearNeighbors = new ArrayList<AiTile>();
		Iterator<AiTile> it =li.iterator();
		while(it.hasNext())
		{
			checkInterruption();
			AiTile t = it.next();
			
			{
				AiTile r = t.getNeighbor(Direction.RIGHT);
				if (isClear(r.getCol(),r.getLine()) && !clearNeighbors.contains(r))
					clearNeighbors.add(r);
				AiTile l = t.getNeighbor(Direction.LEFT);
				if (isClear(l.getCol(),l.getLine()) && !clearNeighbors.contains(l))
					clearNeighbors.add(l);
				AiTile u = t.getNeighbor(Direction.UP );
				if (isClear(u.getCol(),u.getLine()) && !clearNeighbors.contains(u))
					clearNeighbors.add(u);
				AiTile d = t.getNeighbor(Direction.DOWN);
				if (isClear(d.getCol(),d.getLine()) && !clearNeighbors.contains(d))
					clearNeighbors.add(d);
			}			
		}
		return clearNeighbors;
	}
	
	/*********************************************************************************
	 * La methode qu'on va utiliser pour connaitre si on peut atteindre une adversaire
	 * @throws StopRequestException
	 */
	private void canGoToRival() throws StopRequestException
	{
		checkInterruption();
		AiTile temp=null;
		List<AiHero> heroes = zone.getRemainingHeroes();
		Iterator<AiHero> it =heroes.iterator();
		while(it.hasNext())
		{
			checkInterruption();
			temp=it.next().getTile();
			Astar a = new Astar(dangerZone,getOwnHero().getCol(),getOwnHero().getLine(),temp.getCol(),temp.getLine());
			if ((hero.getCol()!= temp.getCol() || hero.getLine() != temp.getLine()) && a.findSecurePath())
				canGoRival=true;
		}
	}
	
	/*******************************************************
	 * La methode sert a trouver une liste tile d'une chemin
	 * @param deq
	 * @return list
	 * @throws StopRequestException
	 */
	public List<AiTile> pathTiles(Deque<Integer> deq) throws StopRequestException
	{
		checkInterruption();
		AiTile tile;
		List<AiTile> tilesOfPath = new ArrayList<AiTile>();
		int tempx;
		int tempy;
		while(!deq.isEmpty()) {
			checkInterruption();
			tempx = deq.poll();
			tempy = deq.poll();
			tile= zone.getTile(tempy,tempx );
			if(tile!=hero.getTile())
				tilesOfPath.add(tile);
		}
		return tilesOfPath;
	}
	
	/******************************************
	 * La methode sert a trouver la tile target
	 * @param deq
	 * @throws StopRequestException
	 */
	public void findTargetTile(Deque<Integer> deq) throws StopRequestException
	{
		checkInterruption();
		int tempx;
		int tempy;
		while(!deq.isEmpty()){
			checkInterruption();
			 tempx=deq.poll();
			 tempy=deq.poll();
			 targetTile=zone.getTile(tempy, tempx);
		}
	}
	
	/**
	 * renvoie la case courante
	 */
	public AiTile getCurrentTile() throws StopRequestException {
		checkInterruption();
		return currentTile;
	}

	/************************************************
	 * La methode sert a initialiser notre algorithme
	 * @throws StopRequestException
	 */
	private void initMonAi() throws StopRequestException{
		//APPEL OBLIGATOIRE
		checkInterruption();
		//On initialise les instances
		zone = getPercepts();
		hero = zone.getOwnHero();
		isDanger=false;
		currentTile = hero.getTile();
	}
	
	/************************************************
	 * La methode sert a controler s'il y a un danger
	 * @throws StopRequestException
	 */
	void checkDanger() throws StopRequestException{
		checkInterruption(); //Appel obligatoire
		if(isClear(currentTile.getCol(),currentTile.getLine()))
			isDanger = false;		
		else
			isDanger = true;
	}
	
	/***********************************************
	 * La methode qui controle si la tile est claire
	 * @param x
	 * @param y
	 * @return si la tile est claire
	 * @throws StopRequestException
	 */
	public boolean isClear(int x,int y) throws StopRequestException {
		
		checkInterruption();
		EtatEnum etat= dangerZone.getValeur(x, y);
		return (etat==EtatEnum.LIBRE || etat==EtatEnum.BONUSBOMBE || etat==EtatEnum.BONUSFEU || etat==EtatEnum.HERO || etat==EtatEnum.RIVAL);
	}
	
	/*************************************************
	 * La methode sert a controler si la tile est vide
	 * @param x
	 * @param y
	 * @return si la tile est libre
	 * @throws StopRequestException
	 */
	public boolean isLibre(int x,int y) throws StopRequestException
	{
		checkInterruption();
		EtatEnum etat=dangerZone.getValeur(x, y);
		return(etat==EtatEnum.LIBRE || etat==EtatEnum.HERO || etat==EtatEnum.RIVAL);
	}
	
	/************************************************************************
	 * La methode sert a controler si la tile se trouve dans la range danger
	 * @param x
	 * @param y
	 * @return si la tile est en danger
	 * @throws StopRequestException
	 */
	public boolean isDanger(int x, int y) throws StopRequestException
	{
		checkInterruption();
		EtatEnum etat = dangerZone.getValeur(x, y);
		return(etat== EtatEnum.DANGER || etat==EtatEnum.BOMBE || etat == EtatEnum.FEU );
	}
	
	/********************************************************
	 * La methode sert a definir si la tile contient une bonus
	 * @param x
	 * @param y
	 * @return si la tile contient une bonus
	 * @throws StopRequestException
	 */
	public boolean isBonus(int x, int y) throws StopRequestException{
		
		checkInterruption();
		EtatEnum etat = dangerZone.getValeur(x, y);
		return (etat == EtatEnum.BONUSBOMBE || etat==EtatEnum.BONUSFEU);
	}
	
	/****************
	 * La methode sert a controler si la tile est destructible
	 * @param x
	 * @param y
	 * @return si la tile est destructible
	 * @throws StopRequestException
	 */
	public boolean isDesctructible(int x, int y) throws StopRequestException{
		
		checkInterruption();
		EtatEnum etat = dangerZone.getValeur(x, y);
		return (etat == EtatEnum.BLOCDEST);
	}
	
	/****************************************************************
	 * La methode sert a controler si la tile contient une adversaire
	 * @param x
	 * @param y
	 * @return
	 * @throws StopRequestException
	 */
	public boolean isRival(int x, int y) throws StopRequestException{
		
		checkInterruption();
		EtatEnum etat = dangerZone.getValeur(x, y);
		return (etat == EtatEnum.RIVAL);
	}

	/********************************************************
	 * La methode sert a mettre a jour la place de hero
	 * @param currentTile
	 * @throws StopRequestException
	 */
	private void updateLocation(AiTile currentTile) throws StopRequestException
	{
		checkInterruption();
		this.currentTile=currentTile;
	}
				
	/******************************************************
	 * la methode sert a preciser s'il y a des bonus ouvert
	 * @return s'il y a des bonus ouvert
	 * @throws StopRequestException
	 */
	public boolean bonusExiste () throws StopRequestException{
		checkInterruption();
		List<AiTile> bonusList=dangerZone.findBonusTiles();
		Iterator<AiTile> it = bonusList.iterator();
		AiTile temp;
		int count=0;
		while(it.hasNext())
		{
			checkInterruption();
			temp=it.next();
			Astar as = new Astar(dangerZone,hero.getCol(),hero.getLine(),temp.getCol(),temp.getLine());
			if(as.findSecurePath())
				count++;
		}
		return (count>0);
	}
	
	/*********************************************************
	 * La methode sert a preciser s'il y a des murs a detruir
	 * @return s'il existe des murs a detruir
	 * @throws StopRequestException
	 */
	public boolean destructExiste () throws StopRequestException{
		checkInterruption();
		List<AiTile> dstList=dangerZone.findDesctructibleTiles();
		Iterator<AiTile> it = dstList.iterator();
		AiTile temp;
		int count=0;
		while(it.hasNext())
		{
			checkInterruption();
			temp=it.next();
			Astar as = new Astar(dangerZone,hero.getCol(),hero.getLine(),temp.getCol(),temp.getLine());
			if(as.findSecurePath());
				count++;
		}
		return (count>0);
	}
	
	/****
	 * La methode qui renvoi la dangerzone
	 * @return
	 * @throws StopRequestException
	 */
	public DangerZone getDangerZone() throws StopRequestException {
		checkInterruption();
		return dangerZone;
	}

	/***********************************
	 * La methode qui renvoi zone de jeu
	 * @return zone
	 * @throws StopRequestException
	 */
	public AiZone getZone() throws StopRequestException{
		checkInterruption();//Appel Obligatoire
		return zone;
	}
	
	/***********************************
	 * La methode qui renvoie notre hero
	 * @return
	 * @throws StopRequestException
	 */
	public AiHero getOwnHero() throws StopRequestException {
		checkInterruption();
		return hero;
	}
}