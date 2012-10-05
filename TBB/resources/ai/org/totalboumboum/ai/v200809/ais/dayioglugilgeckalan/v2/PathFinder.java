package org.totalboumboum.ai.v200809.ais.dayioglugilgeckalan.v2;

import java.util.LinkedList;
import java.util.PriorityQueue;

import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Ali Batuhan Dayioğlugil
 * @author Gökhan Geçkalan
 *
 */
@SuppressWarnings("deprecation")
public class PathFinder {

	private LinkedList<Noeud> path;
	private ZoneEnum tab[][];
	private AiZone zone;
	private DayioglugilGeckalan source;
	
	/**
	 * 
	 * @param zone
	 * @param target
	 * @param source
	 * @throws StopRequestException
	 */
	public PathFinder(AiZone zone, AiTile target,DayioglugilGeckalan source) throws StopRequestException
	{
		source.checkInterruption(); //Appel Obligatoire
		this.source = source;
		this.zone = zone;
		Zone AiZoneToZoneConverter = new Zone(zone,source);
		tab = AiZoneToZoneConverter.getZoneArray();
		if(zone.getOwnHero().getTile().equals(target))
		{
			path = new LinkedList<Noeud>();
			path.offer(new Noeud(target.getCol(),target.getLine(),tab[target.getCol()][target.getLine()],source));
		}
		else
		{
			if(!zone.getNeighborTiles(zone.getOwnHero().getTile()).contains(target))
			{		//Target n'est pas a cote de nous
				findPath(tab,zone.getOwnHero().getCol(),zone.getOwnHero().getLine(),new Noeud(target.getCol(),target.getLine(),tab[target.getCol()][target.getLine()],source));
			}
			else
			{
				path = new LinkedList<Noeud>();
				path.offer(new Noeud(target.getCol(),target.getLine(),tab[target.getCol()][target.getLine()],source));
			}
		}
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 */
	public String ToStringPath()
	{
		return path.toString();
	}
	
	/*public LinkedList<Direction> getPath()
	{
		LinkedList <Noeud> pathTemp = (LinkedList <Noeud>)path.clone();
		LinkedList <Direction> resultat = new LinkedList <Direction>();
		Noeud temp1, temp2;
		if(!pathTemp.isEmpty())
			temp1 = pathTemp.poll();
		else
			return resultat;
		if(!pathTemp.isEmpty())
			temp2 = pathTemp.poll();
		else
			return resultat;
		if(pathTemp.isEmpty())
		{//S'il y a un seul mouvement
			if(temp2.getX() - temp1.getX() == 0)
			{//Verticale
				if(temp2.getY() - temp1.getY() == 1)
					resultat.offer(Direction.DOWN);
				else
					resultat.offer(Direction.UP);
			}
			else
			{//Horizontale
				if(temp2.getX() - temp1.getX() == 1)
					resultat.offer(Direction.RIGHT);
				else
					resultat.offer(Direction.LEFT);
			}
			return resultat;
		}
		
		while(!pathTemp.isEmpty())
		{//Il y a d'autres mouvements
			if(temp2.getX()-temp1.getX() == 0)
			{//Est-ce une deplacement verticale?
				if(temp2.getY() - temp1.getX() == 1)
					resultat.offer(Direction.DOWN);
				else
					resultat.offer(Direction.UP);
			}
			else
			{
				if(temp2.getY()-temp1.getY() == 0)
				{//Est-ce une deplacement horizontale?
					if(temp2.getX()-temp1.getX() == 1)
					{
						resultat.offer(Direction.LEFT);
					}
					else
						resultat.offer(Direction.RIGHT);					
				}				
			}
			temp1 = temp2;
			temp2 = pathTemp.poll();
		}
		return resultat;
	} */
	
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
	 */
	private void findPath(ZoneEnum[][] tab,int x,int y,Noeud goal) throws StopRequestException
	{	source.checkInterruption(); //Appel Obligatoire
		//à chaque appel de la fonction,on efface les anciens valeurs.
		path=new LinkedList<Noeud>();
		
		Noeud courant=new Noeud(x,y,tab[x][y],0,source);//case où se trouve ia.
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
				//la case qui est en haut de lui.
				if((temp.getY()-1>=1) && (tab[temp.getX()][temp.getY()-1]!=ZoneEnum.FEU) && (tab[temp.getX()][temp.getY()-1]!=ZoneEnum.BOMBE)&&(tab[temp.getX()][temp.getY()-1]!=ZoneEnum.BLOCDEST)&&(tab[temp.getX()][temp.getY()-1]!=ZoneEnum.BLOCINDEST)) {	
					up=new Noeud(temp.getX(),temp.getY()-1,tab[temp.getX()][temp.getY()-1],temp.getCout()+1,source);
					if(!open.contains(up)  && !closed.contains(up)){
						open.add(up);
						tree.addNoeud(temp,up);
						frange.offer(up);
					}				
				
				}
			//la case qui est en bas de lui.
				if((temp.getY()+1<=zone.getHeight()-1)&& (tab[temp.getX()][temp.getY()+1]!=ZoneEnum.FEU) && (tab[temp.getX()][temp.getY()+1]!=ZoneEnum.BOMBE)&&(tab[temp.getX()][temp.getY()+1]!=ZoneEnum.BLOCDEST)&&(tab[temp.getX()][temp.getY()+1]!=ZoneEnum.BLOCINDEST)) {
					down=new Noeud(temp.getX(),temp.getY()+1,tab[temp.getX()][temp.getY()+1],temp.getCout()+1,source);
					if(!open.contains(down)  && !closed.contains(down)){
						open.add(down);
						tree.addNoeud(temp,down);
						frange.offer(down);
					}	
				 
				 }
			//la case qui est à droite de lui.
				if((temp.getX()+1<=zone.getWidth()-1)&& (tab[temp.getX()+1][temp.getY()]!=ZoneEnum.FEU) && (tab[temp.getX()+1][temp.getY()]!=ZoneEnum.BOMBE)&&(tab[temp.getX()+1][temp.getY()]!=ZoneEnum.BLOCDEST)&&(tab[temp.getX()+1][temp.getY()]!=ZoneEnum.BLOCINDEST)){
					right=new Noeud(temp.getX()+1,temp.getY(),tab[temp.getX()+1][temp.getY()],temp.getCout()+1,source);
					if(!open.contains(right)  && !closed.contains(right)){
						open.add(right);
						tree.addNoeud(temp,right);
						frange.offer(right);
					}	
				}
			//la case qui est en à gauche de lui.
				if((temp.getX()-1>=1)&& (tab[temp.getX()-1][temp.getY()]!=ZoneEnum.FEU)  && (tab[temp.getX()-1][temp.getY()]!=ZoneEnum.BOMBE)&&(tab[temp.getX()-1][temp.getY()]!=ZoneEnum.BLOCDEST)&&(tab[temp.getX()-1][temp.getY()]!=ZoneEnum.BLOCINDEST)) {	
					left=new Noeud(temp.getX()-1,temp.getY(),tab[temp.getX()-1][temp.getY()],temp.getCout()+1,source);	
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
	 * 		?
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unchecked")
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
		return resultat;
	}
	
}
