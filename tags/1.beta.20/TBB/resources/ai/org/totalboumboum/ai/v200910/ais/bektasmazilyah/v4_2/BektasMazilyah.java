package org.totalboumboum.ai.v200910.ais.bektasmazilyah.v4_2;


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
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
 * 
 * @version 4.2
 * 
 * @author Erdem Bektas
 * @author Nedim Mazilyah
 *
 */
public class BektasMazilyah extends ArtificialIntelligence
{	
	private AiZone zone;
	Astar as;

	// le personnage dirigé par cette IA
	private AiHero hero;
//	private AiTile previousTile;
	private AiTile currentTile;
	private AiTile nextTile;
	public AiTile targetTile=null;
	boolean isDanger = false;
	AiAction result = new AiAction(AiActionName.NONE);
	//private long time;
	private boolean dropBomb=false;
	private boolean canGoRival=false;
	private DangerZone dangerZone;
//	private BombMatrice bombMatrice;
	boolean takeBonus = true;
//	private int posx;
//	private int posy;
//	private int count = 0;
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	@Override
	public AiAction processAction() throws StopRequestException
	{	//Appel Obligatoire
		checkInterruption();
		if(hero==null)
			initMonAi();
		dangerZone= new DangerZone(zone,this);
//		bombMatrice= new BombMatrice(this,zone,dangerZone);
		Direction moveDir = Direction.NONE;
		if(hero != null)
		{
			updateLocation(hero.getTile());
			checkDanger();
			if(isDanger)
			{//Il y a un danger, il faut se sauver.
				suivre();
				if(nextTile!=null){
					moveDir = zone.getDirection(currentTile, nextTile);
					}
				else
					moveDir=Direction.NONE;
/**				else {
						goToPlusSecurise();	
						if(nextTile!=null){
							moveDir = zone.getDirection(currentTile, nextTile);
							System.out.println(moveDir.toString());
						}
						else{
							moveDir=Direction.NONE;
						}
						
					}
					*/
				
			}
			else
			{	
				
				mustTakeBonus();
				if(bonusExiste() && takeBonus)
				{
					goToBonus();
					if(nextTile!=null)
					moveDir = zone.getDirection(currentTile, nextTile);
					else
						moveDir=Direction.NONE;
				}
				else 
				{
					//mustTakeBonus();
					canGoToRival();
					if(destructExiste() && !canGoRival)//&& (takeBonus)) // || !canGoRival))
					{
						canGoRival=false;
						destruct();				
						if(nextTile!=null)
						{
							moveDir=zone.getDirection(currentTile, nextTile);
							dropBomb=canDrop();
							if(targetTile==currentTile && dropBomb){
								dropBomb=false;
								result = new AiAction(AiActionName.DROP_BOMB);
								return result;			
							}
						}
						
					}
					else
					/**{
						System.out.println("6");
							canGoToRival();
							if(!canGoRival)
							{
								System.out.println("7");
								destructWallToGoRival();				
								if(nextTile!=null)
								{
									System.out.println("8");
									moveDir=zone.getDirection(currentTile, nextTile);
									dropBomb=kacabilirmi();
									System.out.println("8den sonra kaçar aslýnda:"+dropBomb);
									if(targetTile==currentTile && dropBomb){
										System.out.println("9");
										dropBomb=false;
										result = new AiAction(AiActionName.DROP_BOMB);
										return result;			
									}
								}
							}*/
						
							//else
							{
								dropBomb=canDrop();
							//	rivalvar=rivalVar();
								if(dropBomb && rivalWithMe())
								{
									dropBomb=false;
									//	rivalvar=false;
									result = new AiAction(AiActionName.DROP_BOMB);
									return result;
								}
								else
									if(!rivalWithMe())
									{
										deplace();
										if(nextTile!=null)
											moveDir = zone.getDirection(currentTile, nextTile);									
									}
						
				}}	
			}
			result = new AiAction(AiActionName.MOVE,moveDir);
		}	
		return result;	
	}

	private void deplace() throws StopRequestException 
	{
		checkInterruption();
		AiTile tile = null;
		List<AiTile> l = dangerZone.findRivalsTiles();
		List<AiTile> li =sirala(l);
		Iterator<AiTile> it =li.iterator();
	//	System.out.println("it yaratýldý");
		if (it.hasNext())
		{
			checkInterruption();
			AiTile kare=it.next();
			as=new Astar (dangerZone,hero.getCol(),hero.getLine(),kare.getCol(),kare.getLine());		
			if (as.findSecurePath())
			{	
				//System.out.println(kare+"kare");
				Deque<Integer> deque = as.getPath();
				if(!deque.isEmpty()) {
				int tempx = deque.poll(), tempy = deque.poll();
				tile= zone.getTile(tempy,tempx );
				
			}
			if (tile ==hero.getTile()){
			if (!deque.isEmpty()) {
					int  tempx = deque.poll(); int tempy = deque.poll();
					 tile= zone.getTile(tempy,tempx );
					// System.out.println(tile);
			}
		}}
			nextTile=tile;
		}	
}

/**
	private boolean rivalVar() throws StopRequestException
	{
		List<AiHero> list =zone.getHeroes();
		if(list.contains(hero))
			list.remove(hero);
		ArrayList<AiTile> li = takeInRange();
		Iterator<AiHero> it = list.iterator();
		AiTile temp =null;
		boolean result=false;
		while(it.hasNext())
		{
			temp=(it.next()).getTile();
			if(li.contains(temp))
				result=true;
				
		}
 		return result;
	}
*/	
	private void mustTakeBonus() throws StopRequestException {
		checkInterruption();
		if(hero.getBombRange() >3 || hero.getBombNumber()>3)
			takeBonus=false;
		else
			takeBonus=true;
}
	private boolean rivalWithMe() throws StopRequestException
	{
		checkInterruption();
		List<AiHero> list =zone.getHeroes();
		if(list.contains(hero))
			list.remove(hero);
		Iterator<AiHero> it = list.iterator();
		AiTile rival = it.next().getTile();
		return(hero.getTile().equals(rival));
			
		
	}

	private void suivre() throws StopRequestException
	{
		checkInterruption();
		AiTile tile = null;
		List<AiTile> l = dangerZone.findSafeTiles();
	//	System.out.println("en yakýn safe tilelar:"+li);
		List<AiTile> li =sirala(l);
		Iterator<AiTile> it =li.iterator();
	//	System.out.println("it yaratýldý");
		if (it.hasNext())
		{
			checkInterruption();
			AiTile kare=it.next();
			as=new Astar (dangerZone,hero.getCol(),hero.getLine(),kare.getCol(),kare.getLine());		
			if (as.findPath())
			{	
				//System.out.println(kare+"kare");
				Deque<Integer> deque = as.getPath();
				if(!deque.isEmpty()) {
				int tempx = deque.poll(), tempy = deque.poll();
				tile= zone.getTile(tempy,tempx );
				
			}
			if (tile ==hero.getTile()){
			if (!deque.isEmpty()) {
					int  tempx = deque.poll(); int tempy = deque.poll();
					 tile= zone.getTile(tempy,tempx );
					// System.out.println(tile);
			}
		}}
			nextTile=tile;
		}	
	}
	
	public boolean canDrop() throws StopRequestException
	{
		checkInterruption();
		List<AiTile> neighbors = takeInRange();
//		System.out.println("iþaretlenecek yerler:" + neighbors);
		List<AiTile> result = removeFromList(neighbors);
//		System.out.println("listeden çýkýnca:" + result);
		Iterator<AiTile> it = result.iterator();
		AiTile temp=null;
		int count=0;
		if(result.size()!=0){
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
		if(count>1)
			dropBomb=true;
		else
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
		if(count==1)
		{
			count=0;
			System.out.println("count 1");
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
		}
		return dropBomb;		
	}

	private List<AiTile> takeInRange() throws StopRequestException
	{
		checkInterruption();
		AiTile temp=hero.getTile();
		List<AiTile> result = new ArrayList<AiTile>();
		result.add(temp);
		EtatEnum etat = EtatEnum.LIBRE;
		int range = 1;
		int k=0;
		int x=0;
		int y=0;
		boolean up=true;
		boolean down=true;
		boolean	right=true;
		boolean left=true;
		while(k<range+1 && up)
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
		while(k<range+1 && down)
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
		while(k<range+1 && right)
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
		while(k<range+1 && left)
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
		
	private List<AiTile> removeFromList(List<AiTile> list) throws StopRequestException
	{
		checkInterruption();
		List<AiTile> result = dangerZone.findSafeTiles();
		Iterator<AiTile> it = list.iterator();
		AiTile temp = null;
		while(it.hasNext())
		{
			temp=it.next();
			if(result.contains(temp))
				result.remove(temp);
		}
		return result;
	}
	
	public List<AiTile> sirala(List<AiTile> source) throws StopRequestException
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
	
	private void goToBonus() throws StopRequestException
	{		
		checkInterruption();
		AiTile tile = null;
		List<AiTile> l = dangerZone.findBonusTiles();
	//	System.out.println("bonuslar:"+l);
		List<AiTile> li = sirala(l);
	//	System.out.println("bonuslarýn sýralýsý:"+li);
		Iterator<AiTile> it =li.iterator();
		//System.out.println("it yaratýldý");
		if (it.hasNext())
		{
			checkInterruption();
			AiTile kare=it.next();
		//	if(!isDanger(kare.getCol(),kare.getLine()))
			{
				as=new Astar (dangerZone,hero.getCol(),hero.getLine(),kare.getCol(),kare.getLine());
		
				if (as.findSecurePath())
				{	
					//System.out.println(kare+"kare");
					Deque<Integer> deque = as.getPath();
		//			System.out.println(deque);
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
	
	/**
	private void goToPlusSecurise() throws StopRequestException
	{
		checkInterruption();
		AiTile tile = null;
		ArrayList<AiTile> li = bombMatrice.findPlusSafeTiles();
		Iterator<AiTile> it =li.iterator();
	//	System.out.println("it yaratýldý");
		if (it.hasNext())
		{
			checkInterruption();
			AiTile kare=it.next();
			as=new Astar (dangerZone,hero.getCol(),hero.getLine(),kare.getCol(),kare.getLine());
		
			if (as.findPath())
			{	
				//System.out.println(kare+"kare");
				Deque<Integer> deque = as.getPath();
				if(!deque.isEmpty()) {
				int tempx = deque.poll(), tempy = deque.poll();
				tile= zone.getTile(tempy,tempx );
			}
			if (tile ==hero.getTile()){
			if (!deque.isEmpty()) {
					int  tempx = deque.poll(); int tempy = deque.poll();
					tile= zone.getTile(tempy,tempx );
					// System.out.println(tile);
			}}}
		}
		nextTile=tile;		
	}
*/	
	
	private void destruct() throws StopRequestException
	{
		
		checkInterruption();
		List<AiTile> a= dangerZone.findDesctructibleTiles();
	//	System.out.println("dest olanlar:"+a);
		List<AiTile> b= findClearNeighbors(a);
	//	System.out.println("serbest olanlar:"+b);
		List<AiTile> dst= dangerZone.findTilesForDestruct(b);
	//	System.out.println("gidilesi olanlar:"+dst);
		List<AiTile> dest=sirala(dst);
	//	System.out.println("gidilesi sýrali"+dest);
		AiTile tile = null;
		AiTile kare=null;
		Iterator<AiTile> it =dest.iterator();
		if (it.hasNext())
		{
			checkInterruption();
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
			
//			System.out.println("targetTile:"+targetTile);
//			System.out.println("nextTile:"+nextTile);}
		}}
		else
			nextTile=currentTile;
	}
		
	
	public void destructWallToGoRival() throws StopRequestException
	{
		checkInterruption();
		AiTile temp=null;
		List<AiTile> tiles = new ArrayList<AiTile>();
		List<AiHero> heroes = zone.getHeroes();
//		System.out.println("düþman burda:"+heroes);
		if(heroes.contains(hero))
			heroes.remove(hero);
		Iterator<AiHero> itHero = heroes.iterator();
		while(itHero.hasNext())
		{
			temp=itHero.next().getTile();
			Astar a = new Astar(dangerZone,hero.getCol(),hero.getLine(),temp.getCol(),temp.getLine());
			if(a.findPathSurWall())
			{
				Deque<Integer> deque = a.getPath();
				tiles=pathTiles(deque);
//				System.out.println("yol bu::::::::::"+tiles);
			}
		}
		
		List<AiTile> dest = new ArrayList<AiTile>();
		Iterator<AiTile> itTile = tiles.iterator();
		while(itTile.hasNext())
		{
			temp=itTile.next();
	//		System.out.println("bu tile için: bakýyo" +temp);
			if(dangerZone.getValeur(temp.getCol(), temp.getLine())== EtatEnum.BLOCDEST)
				dest.add(temp);
		}
		
//		System.out.println("dest bu çýktý:"+dest);
		
		dest=findClearNeighbors(dest);
//		System.out.println("komþularý bu:"+dest);
		List<AiTile> dst= dangerZone.findTilesForDestruct(dest);
		dest=sirala(dst);
//		System.out.println("þuralara býrakmaya çalýþýyorum:"+dest);
		temp=null;
		AiTile kare=null;
		Iterator<AiTile> it =dest.iterator();
		if (it.hasNext())
		{
			checkInterruption();
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
				temp= zone.getTile(tempy,tempx );
			}
			if (temp ==hero.getTile()){
			if (!deq.isEmpty()) {
					int  tempx = deq.poll(); int tempy = deq.poll();
					 temp= zone.getTile(tempy,tempx );
			}}}
				nextTile=temp;
			
//			System.out.println("targetTile:"+targetTile);
//			System.out.println("nextTile:"+nextTile);}
		}}
		else
			nextTile=currentTile;
		
	}
	
	public List<AiTile> findClearNeighbors(List<AiTile> list) throws StopRequestException
	{
		List<AiTile> li = list;
		List<AiTile> clearNeighbors = new ArrayList<AiTile>();
		Iterator<AiTile> it =li.iterator();
		while(it.hasNext())
		{
			checkInterruption();
			AiTile t = it.next();
			
			{
				AiTile r = t.getNeighbor(Direction.RIGHT);
				if (isClear(r.getCol(),r.getLine()))
					clearNeighbors.add(r);
				AiTile l = t.getNeighbor(Direction.LEFT);
				if (isClear(l.getCol(),l.getLine()))
					clearNeighbors.add(l);
				AiTile u = t.getNeighbor(Direction.UP);
				if (isClear(u.getCol(),u.getLine()))
					clearNeighbors.add(u);
				AiTile d = t.getNeighbor(Direction.DOWN);
				if (isClear(d.getCol(),d.getLine()))
					clearNeighbors.add(d);
			}			
		}
		return clearNeighbors;
	}
	
	private void canGoToRival() throws StopRequestException
	{
		checkInterruption();
		AiTile temp=null;
		List<AiHero> heroes = zone.getHeroes();
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
	
	public void findTargetTile(Deque<Integer> deq) throws StopRequestException
	{
		checkInterruption();
		int tempx;
		int tempy;
		while(!deq.isEmpty()){
			 tempx=deq.poll();
			 tempy=deq.poll();
			 targetTile=zone.getTile(tempy, tempx);
		}
	}
	
	public AiTile getCurrentTile() throws StopRequestException {
		checkInterruption();
		return currentTile;
	}

	private void initMonAi() throws StopRequestException{
		//APPEL OBLIGATOIRE
		checkInterruption();
		//On initialise les instances
		zone = getPercepts();
		hero = zone.getOwnHero();
		isDanger=false;
		currentTile = hero.getTile();
		//System.out.println("initialise edildi");
	}
	
	void checkDanger() throws StopRequestException{
		checkInterruption(); //Appel obligatoire
		if(isClear(currentTile.getCol(),currentTile.getLine()))
			isDanger = false;		
		else
			isDanger = true;
	}
	
	public boolean isClear(int x,int y) throws StopRequestException {
		
		checkInterruption();
		EtatEnum etat= dangerZone.getValeur(x, y);
		return (etat==EtatEnum.LIBRE || etat==EtatEnum.BONUSBOMBE || etat==EtatEnum.BONUSFEU || etat==EtatEnum.HERO || etat==EtatEnum.RIVAL);
	}
	
	public boolean isLibre(int x,int y) throws StopRequestException
	{
		checkInterruption();
		EtatEnum etat=dangerZone.getValeur(x, y);
		return(etat==EtatEnum.LIBRE || etat==EtatEnum.HERO || etat==EtatEnum.RIVAL);
	}
	
	public boolean isDanger(int x, int y) throws StopRequestException
	{
		checkInterruption();
		EtatEnum etat = dangerZone.getValeur(x, y);
		return(etat== EtatEnum.DANGER || etat==EtatEnum.BOMBE || etat == EtatEnum.FEU );
	}
	
	public boolean isBonus(int x, int y) throws StopRequestException{
		
		checkInterruption();
		EtatEnum etat = dangerZone.getValeur(x, y);
		return (etat == EtatEnum.BONUSBOMBE || etat==EtatEnum.BONUSFEU);
	}
	
	public boolean isDesctructible(int x, int y) throws StopRequestException{
		
		checkInterruption();
		EtatEnum etat = dangerZone.getValeur(x, y);
		return (etat == EtatEnum.BLOCDEST);
	}
	
	public boolean isRival(int x, int y) throws StopRequestException{
		
		checkInterruption();
		EtatEnum etat = dangerZone.getValeur(x, y);
		return (etat == EtatEnum.RIVAL);
	}

	private void updateLocation(AiTile currentTile) throws StopRequestException
	{
		checkInterruption();
		this.currentTile=currentTile;
	}
				
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
	
	public DangerZone getDangerZone() throws StopRequestException {
		checkInterruption();
		return dangerZone;
	}

	//renvoie la zone de jeu
	public AiZone getZone() throws StopRequestException{
		checkInterruption();//Appel Obligatoire
		return zone;
	}
	
	public AiHero getOwnHero() throws StopRequestException {
		checkInterruption();
		return hero;
	}
}