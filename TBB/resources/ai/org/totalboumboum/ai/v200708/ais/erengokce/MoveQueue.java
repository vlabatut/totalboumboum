package org.totalboumboum.ai.v200708.ais.erengokce;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

/**
 * 
 * @author Can Eren
 * @author Mustafa Mert Gökçe
 *
 */
public class MoveQueue {

	Vector<Node> way;
	//les graphes pour aEtoile
	Vector<Node> open;
	Vector<Node> closed;
	//les actions
	Vector<Integer> actions;
	//les coordonnes du noeud racine pour ne pas s'eloigner, optimiser l'algorithme
	int xorg,yorg;
	
	public MoveQueue(int x,int y)
	{
		this.open=new Vector<Node>();
		this.closed=new Vector<Node>();
		this.way=new Vector<Node>();
		this.actions=new Vector<Integer>();
		xorg=x;
		yorg=y;
	}
	
	public Vector<Node> getWay()
	{
		return way;
	}
	
	public Vector<Integer> getActions()
	{
		return actions;
	}
	
	
	public void getActionsFromWay()
	{
		if(!way.isEmpty())
		{
		Node n=way.remove(0);
		while(n.father!=null)
		{
			if(n.father.x!=n.x)
			{
				//si on accede son pere par un mouvement a droite
				if(n.father.x-n.x==1)
				actions.add(3);
				//si on accede son pere par un mouvement a gauche
				if(n.x-n.father.x==1)
				actions.add(4);
			}
			else
			{
				if(n.father.y!=n.y)
				{
					//si on accede son pere par un mouvement en bas
					if(n.father.y-n.y==1)
						actions.add(1);
					//si on accede son pere par un mouvement en haut
					if(n.y-n.father.y==1)
						actions.add(2);
				}
			}
			//iteration
			n=n.father;
		}
		}
	}
	
	//l'algorithme aEtoile utilise pour s'echapper du bombe ou du shrink
	public synchronized void aStar(Node root,int bombx,int bomby,int power,int[][] zoneMatrix) throws ExecutionException
	{
		//si noeud est deja sur open "graphe ouvert"
		boolean already=false;
		{
			Iterator<Node> i=open.iterator();
			while(i.hasNext())
			{
				Node j=i.next();
				if(root.x==j.x&&root.y==j.y)
					already=true;
			}
			//si il y est pas on le rajoute
			if(!already)
		{
				open.add(root);
				already=false;
		}
		//	on met le premier element sur ferme
		Node n=open.remove(0);
		closed.add(n);
		//si la bombe est dangereux pour le noeud en operation
		if(((bombx==n.x)&&((Math.abs(bomby-n.y))<=power))||((bomby==n.y)&&((Math.abs(bombx-n.x))<=power)))
		{
			//l'arbre de recherche
			Tree tr=new Tree(n);
			//si les fils sont ni dans ouvert ni dans ferme
			if((!open.contains(tr.fils1))&&(!closed.contains(tr.fils1)))
			{	
				if(((xorg-tr.fils1.x)<=3)&&((yorg-tr.fils1.y)<=3))
				{
					//les noeuds appartient-ils a l'environnement?
					if(tr.fils1.x<16&&tr.fils1.y<14)
					{
						//la route est ouvert (pas d'obstacle)
				if(zoneMatrix[tr.fils1.x][tr.fils1.y]==0)
					{
					//pas de point secure
					if(way.isEmpty())
					{
						//le noeud est il dans ouvert
						i=open.iterator();
						while(i.hasNext())
						{
							Node j=i.next();
							if(tr.fils1.x==j.x&&tr.fils1.y==j.y)
								already=true;
						}
						if(!already)
						{
							open.add(tr.fils1);
							already=false;
							//appel recursive
							aStar(tr.fils1,bombx,bomby,power,zoneMatrix);
						}
					}
					}
					}
					
				}
			}
			//meme chose pour le fils1
			if((!open.contains(tr.fils2))&&(!closed.contains(tr.fils2)))
			{	
				if(((xorg-tr.fils2.x)<=3)&&((yorg-tr.fils2.y)<=3))
				{
				if(zoneMatrix[tr.fils2.x][tr.fils2.y]==0)
				{	
					if(tr.fils2.x<16&&tr.fils2.y<14)
					{
						
						if(way.isEmpty())
						{
							i=open.iterator();
							while(i.hasNext())
							{
								Node j=i.next();
								if(tr.fils2.x==j.x&&tr.fils2.y==j.y)
									already=true;
							}
							if(!already)
							{
								open.add(tr.fils2);
								already=false;
								aStar(tr.fils2,bombx,bomby,power,zoneMatrix);
							}
						}
				}}}
			}
			//meme chose pour le fils1
			if((!open.contains(tr.fils3))&&(!closed.contains(tr.fils3)))
			{
				if(((xorg-tr.fils3.x)<=3)&&((yorg-tr.fils3.y)<=3))
				{
				if(zoneMatrix[tr.fils3.x][tr.fils3.y]==0)
				{
					if(tr.fils3.x<16&&tr.fils3.y<14)
					{
						if(way.isEmpty())
						{
							i=open.iterator();
							while(i.hasNext())
							{
								Node j=i.next();
								if(tr.fils3.x==j.x&&tr.fils3.y==j.y)
									already=true;
							}
							if(!already)
							{
								open.add(tr.fils3);
								already=false;
								aStar(tr.fils3,bombx,bomby,power,zoneMatrix);
							}
						}
				}}}
			}
			//meme chose pour le fils1
			if((!open.contains(tr.fils4))&&(!closed.contains(tr.fils4)))
			{
				if(((xorg-tr.fils4.x)<=3)&&((yorg-tr.fils4.y)<=3))
				{
				if(zoneMatrix[tr.fils4.x][tr.fils4.y]==0)
				{
					if(tr.fils4.x<16&&tr.fils4.y<14)
					{
						
						if(way.isEmpty())
						{
							i=open.iterator();
							while(i.hasNext())
							{
								Node j=i.next();
								if(tr.fils4.x==j.x&&tr.fils4.y==j.y)
									already=true;
							}
							if(!already)
							{
								open.add(tr.fils4);
								already=false;
								aStar(tr.fils4,bombx,bomby,power,zoneMatrix);
							}
						}
				}}}
			}
		}
		//si le point est secure il est notre route a obtenir
		else
		{
			way.add(n);		
		}
		}
		
	}
	
}
