package org.totalboumboum.ai.v200910.ais.dereligeckalan.v5;


import java.util.LinkedList;
import java.util.PriorityQueue;



import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;

import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;

import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Merih Inal Dereli
 * @author Gökhan Geçkalan
 */
@SuppressWarnings("deprecation")
public class PathFinder {

	/** */
	private LinkedList<Noeud> path;
	/** */
	private ZoneEnum tab[][];
	/** */
	private AiZone zone;
	/** */
	private DereliGeckalan source;
	
	/**
	 * 
	 * @param zone
	 * 		Description manquante !
	 * @param target
	 * 		Description manquante !
	 * @param source
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public PathFinder(AiZone zone, AiTile target,DereliGeckalan source) throws StopRequestException
	{
		source.checkInterruption(); //Appel Obligatoire
		this.source = source;
		this.zone = zone;
		Zone AiZoneToZoneConverter = new Zone(zone,source);
		tab = AiZoneToZoneConverter.getZoneArray();
		LinkedList<AiTile> neigbours = new LinkedList<AiTile>();
		
			
			AiTile downTile = zone.getOwnHero().getTile().getNeighbor(Direction.DOWN);
			neigbours.add(downTile);
			AiTile upTile = zone.getOwnHero().getTile().getNeighbor(Direction.UP);
			neigbours.add(upTile);
			AiTile leftTile = zone.getOwnHero().getTile().getNeighbor(Direction.LEFT);
			neigbours.add(leftTile);
			AiTile rightTile = zone.getOwnHero().getTile().getNeighbor(Direction.RIGHT);
			neigbours.add(rightTile);
		if(zone.getOwnHero().getTile().equals(target))
		{
			path = new LinkedList<Noeud>();
			path.offer(new Noeud(target.getLine(),target.getCol(),tab[target.getLine()][target.getCol()],source));
		}
		else
		{
			
			if(!neigbours.contains(target))
			{		//Target n'est pas a cote de nous
				findPath(tab,zone.getOwnHero().getLine(),zone.getOwnHero().getCol(),new Noeud(target.getLine(),target.getCol(),tab[target.getLine()][target.getCol()],source));
			}
			else
			{
				path = new LinkedList<Noeud>();
				path.offer(new Noeud(target.getLine(),target.getCol(),tab[target.getLine()][target.getCol()],source));
			}
		}
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public String ToStringPath()
	{
		return path.toString();
	}
	
	
	
	/**
	 *Il determine le chemin le plus court au cible.Il utilise l'algorithme de A étoile.
	 *(cf. : http://fr.wikipedia.org/wiki/Algorithme_A*)
	 * @param x
	 *           le coordonné de x de l'ia.
	 * @param y
	 *            le coordonné de y de l'ia.
	 * @param tab le tableau du jeu.
	 * @param goal la case qu'on veut y arriver.
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	private void findPath(ZoneEnum[][] tab,int y,int x,Noeud goal) throws StopRequestException
	{	source.checkInterruption(); //Appel Obligatoire
		//à chaque appel de la fonction,on efface les anciens valeurs.
		path=new LinkedList<Noeud>();
		
		Noeud courant=new Noeud(y,x,tab[y][x],0,source);//case où se trouve ia.
		Tree tree=new Tree(courant,source);//on cree une arbre pour voir le path.
		NoeudAstar comparator=new NoeudAstar(goal);//utilise pour l'algorithme Aetoile
		PriorityQueue<Noeud> frange = new PriorityQueue<Noeud>(1,comparator);//les elements sont inseres en respectant l'ordre du cout et de l'heuristique.
		LinkedList<Noeud> open=new LinkedList<Noeud>();//liste des elements qu'on regarde.
		LinkedList<Noeud> closed=new LinkedList<Noeud>();//liste des elements qu'on a dejà regardé.
		Noeud temp=new Noeud(source);
	
		Noeud solution = null;
		frange.offer(courant);
		open.add(courant);
		//jusqu'a trouver la solution mais attention si la frange est vide,on s'arrete sans trouver la solution.
		while((solution == null) && (!frange.isEmpty())){  
			source.checkInterruption(); //Appel Obligatoire
			temp=frange.poll();//on enleve de la frange
			open.remove(open.indexOf(temp));//on enleve de la liste open.
			closed.add(temp);//on met au liste des elements deja regardés.
			
			if(temp.equals(goal)){
				solution=temp;
			}else{ 	
				Noeud up=null;
				Noeud down=null;
				Noeud right=null;
				Noeud left=null;
				
				AiTile tempTile = zone.getTile(temp.getY(), temp.getX());
				
				AiTile up1 = tempTile.getNeighbor(Direction.UP);
				AiTile down1 = tempTile.getNeighbor(Direction.DOWN);
				AiTile left1 = tempTile.getNeighbor(Direction.LEFT);
				AiTile right1 =tempTile.getNeighbor(Direction.RIGHT);
				//la case qui est en haut de lui
				if((tab[up1.getLine()][up1.getCol()]!=ZoneEnum.FEU) && (tab[up1.getLine()][up1.getCol()]!=ZoneEnum.BOMBE)&&(tab[up1.getLine()][up1.getCol()]!=ZoneEnum.BLOCDEST)&&(tab[up1.getLine()][up1.getCol()]!=ZoneEnum.BLOCINDEST) ) {	
					up=new Noeud(up1.getLine(),up1.getCol(),tab[up1.getLine()][up1.getCol()],temp.getCout()+1,source);
					if(!open.contains(up)  && !closed.contains(up)){
						open.add(up);
						tree.addNoeud(temp,up);
						frange.offer(up);
					}				
				
				}
			//la case qui est en bas de lui.
				if((tab[down1.getLine()][down1.getCol()]!=ZoneEnum.FEU) && (tab[down1.getLine()][down1.getCol()]!=ZoneEnum.BOMBE)&&(tab[down1.getLine()][down1.getCol()]!=ZoneEnum.BLOCDEST)&&(tab[down1.getLine()][down1.getCol()]!=ZoneEnum.BLOCINDEST)  ) {
					down=new Noeud(down1.getLine(),down1.getCol(),tab[down1.getLine()][down1.getCol()],temp.getCout()+1,source);
					if(!open.contains(down)  && !closed.contains(down)){
						open.add(down);
						tree.addNoeud(temp,down);
						frange.offer(down);
					}	
				 
				 }
			//la case qui est à droite de lui.
				if( (tab[right1.getLine()][right1.getCol()]!=ZoneEnum.FEU) && (tab[right1.getLine()][right1.getCol()]!=ZoneEnum.BOMBE)&&(tab[right1.getLine()][right1.getCol()]!=ZoneEnum.BLOCDEST)&&(tab[right1.getLine()][right1.getCol()]!=ZoneEnum.BLOCINDEST)){
					right=new Noeud(right1.getLine(),right1.getCol(),tab[right1.getLine()][right1.getCol()],temp.getCout()+1,source);
					if(!open.contains(right)  && !closed.contains(right)){
						open.add(right);
						tree.addNoeud(temp,right);
						frange.offer(right);
					}	
				}
			//la case qui est en à gauche de lui.
				if( (tab[left1.getLine()][left1.getCol()]!=ZoneEnum.FEU)  && (tab[left1.getLine()][left1.getCol()]!=ZoneEnum.BOMBE)&&(tab[left1.getLine()][left1.getCol()]!=ZoneEnum.BLOCDEST)&&(tab[left1.getLine()][left1.getCol()]!=ZoneEnum.BLOCINDEST)) {	
					left=new Noeud(left1.getLine(),left1.getCol(),tab[left1.getLine()][left1.getCol()],temp.getCout()+1,source);	
					if(!open.contains(left)  && !closed.contains(left)){	
						open.add(left);
						tree.addNoeud(temp,left);
						frange.offer(left);
					}	
				}
			}// fin de l'else
		
			
		}// fin de while

		if(solution!=null)//si on a trouvé la solution sans finir tous les elements de la frange.
			path=tree.getPath(solution);

		frange=null;//on vide la frange.
		tree=null;//on vide l'arbre.
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public LinkedList <AiTile> getPath() throws StopRequestException
	{	source.checkInterruption(); //Appel Obligatoire
		//On doit renverser la file
		LinkedList <AiTile> resultat = new LinkedList <AiTile>();
		LinkedList <Noeud> tempPath = (LinkedList <Noeud>)this.path.clone();
		LinkedList <AiTile> pileTampon = new LinkedList <AiTile>();
		Noeud tempNoeud;
		while(!tempPath.isEmpty())
		{
			source.checkInterruption(); //Appel Obligatoire
			tempNoeud = tempPath.poll();
			pileTampon.push(zone.getTile(tempNoeud.getY(),tempNoeud.getX()));
		}
		while(!pileTampon.isEmpty())
		{
			source.checkInterruption(); //Appel Obligatoire
			resultat.offer(pileTampon.pop());
		}
		if(resultat  == null)
		{
			
		}
		return resultat;
	}
}
