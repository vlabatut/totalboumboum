package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v2;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;


/**
 * @author Elif Nurdan �LGAR 
 * @author Engin Hac�bekta�o�lu
 *
 */


public class Maps 
{
	private HacibektasogluIlgar notreIA;
	AiZone zone;
	Etats matriceCollecte[][];
	double matriceTotal[][];
	int height;
	int width;
	AiHero ourHero;
	int nbAdversaire = 0;
	AiHero adversairesActuelles[];
	int nbBonus=0;
	int nbMurDestructible=0;
	AiItem bonuses[];
	static long bombExplotionVites;
	static  AiPath chemin=new AiPath();
	
	/**
	 * 
	 * @param notreIA
	 * @param zone
	 * @throws StopRequestException 
	 */
	public Maps(HacibektasogluIlgar hi) throws StopRequestException
	{
		
		
		notreIA=hi;
		notreIA.checkInterruption();
		zone=notreIA.zone;
		height=zone.getHeight();
		width=zone.getWidth();
		ourHero= zone.getOwnHero();
	
		matriceCollecte=new Etats[height][width];
		matriceTotal= new double[height][width];
		adversairesActuelles = new AiHero[10];
		bonuses = new AiItem[zone.getItems().size()+zone.getHiddenItemsCount()];
		
	}
	
	/**
	 * on initialise la matrice en lui pensent comme tous ses tiles sont vide 
	 */
	public void initialiserMatrices(Etats matrice[][]) throws StopRequestException 
	{
		notreIA.checkInterruption();

		for(int i=0;i<height;i++)
		{
			notreIA.checkInterruption();
			for(int j=0;j< width;j++)
			{
				notreIA.checkInterruption();	
				matrice[i][j]=Etats.VIDE;
				matriceTotal[i][j]=0;
			}
		}
	}
	
	public void calculeMurs(Etats matrice[][])throws StopRequestException 
	{
		notreIA.checkInterruption();
		
		Collection<AiBlock> murs=zone.getBlocks();
		Iterator<AiBlock> iterMur=murs.iterator();
		
		while(iterMur.hasNext())
		{
			notreIA.checkInterruption();
			AiBlock mur = iterMur.next();
			if(mur.isDestructible())
			{
				matrice[mur.getLine()][mur.getCol()]=Etats.MUR_DESTRUCTIBLE;
				nbMurDestructible++;
			}
			else matrice[mur.getLine()][mur.getCol()]=Etats.MUR_NON_DESTRUCTIBLE;
			
		}
	
	}
	


	public void calculeBombs(Etats matrice[][]) throws StopRequestException
	{
		notreIA.checkInterruption();
		Collection<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> iterBombs = bombs.iterator();
		
		while(iterBombs.hasNext())
		{
			notreIA.checkInterruption();
			AiBomb bomb = iterBombs.next();
			int x=bomb.getLine();
			int y=bomb.getCol();

			bombExplotionVites=bomb.getExplosionDuration();
			List<AiTile> inScopeTiles = bomb.getBlast();
			for (int i = 0; i < inScopeTiles.size(); i++) {
				notreIA.checkInterruption();
				/*
				 *  on controle la duree de la bomb pour pouvoir comprendre si
				 *  c'est bien de passer par ce tile ou non   
				 */
			
				if(bomb.getTime()<bomb.getNormalDuration()/2)
				matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()] = Etats.FLAMMABLE_VERT;
				else if(bomb.getTime()<bomb.getNormalDuration())
				matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()] = Etats.FLAMMABLE_ROUGE;
			}
			
			matrice[x][y]=Etats.BOMB;

		}
	}
	
	public void calculeFeu(Etats matrice[][]) throws StopRequestException
	{
		notreIA.checkInterruption();
		Collection<AiFire> feux= zone.getFires();
		Iterator<AiFire> iterFeux = feux.iterator();
		
		while(iterFeux.hasNext())
		{
			notreIA.checkInterruption();
			AiFire feu=iterFeux.next();
			matrice[feu.getLine()][feu.getCol()]=Etats.FEU;
			
		}
		
		
	}
	
	public void calculeBonus (Etats matrice[][]) throws StopRequestException
	{
		notreIA.checkInterruption();
		
		Collection<AiItem> items = zone.getItems();
		Iterator<AiItem> iterItems = items.iterator();
		
		while (iterItems.hasNext()) {
			notreIA.checkInterruption();
			
			AiItem item = iterItems.next();
			bonuses[nbBonus]=item;
			nbBonus++;
			int x=item.getLine();
			int y=item.getCol();

			matrice[x][y] = Etats.BONUS;
		}
	}
	
	public void calculeAdversaire(Etats matrice[][]) throws StopRequestException
	{
		notreIA.checkInterruption();
		
		Collection<AiHero> adversaires = zone.getRemainingHeroes();
		Iterator<AiHero> iterAdversaires = adversaires.iterator();
		
		while(iterAdversaires.hasNext())
		{
			notreIA.checkInterruption();
			AiHero adversaire = iterAdversaires.next();
			if(adversaire!=ourHero)
				{
					matrice[adversaire.getLine()][adversaire.getCol()]=Etats.ADVERSAIRE;
					adversairesActuelles[nbAdversaire]=adversaire;
				
					nbAdversaire++;
				}
		}
	}
	
	public void misAJourEtatsMatrice(Etats matrice[][]) throws StopRequestException
	{
		notreIA.checkInterruption();
		
		initialiserMatrices(matrice);
		calculeMurs(matrice);
		calculeBonus(matrice);
		calculeAdversaire(matrice);
		calculeBombs(matrice);
		calculeFeu(matrice);
	}
	/**
	 * on va utiliser cette methode pour calculer les points des cases autour d'un mur 
	 * desctructible ou bien autour d'une adversaire   
	 *
	 * @param matriceTotal 
	 * @param i
	 * @param j
	 * @param note
	 * @param division si le valeur changes par distance "true" sinon "false"
	 * @throws StopRequestException
	 */
	public void calculeDapresPortee(double matriceTotal[][], int i,int j,int note,boolean division) throws StopRequestException
	{
		notreIA.checkInterruption();
		
		int porteeDuBomb=ourHero.getBombRange();
		int c=1,d;
		
		// calcule pour les tiles verticales
		for(int b=0;b<2;b++)
		{
			notreIA.checkInterruption();
			
			for(int a=1;a<porteeDuBomb+1;a++)
			{
				notreIA.checkInterruption();
				
				d=c*a;
				int x=i-d;
				if(x<0) x+=height;
				else if(x>=height) x-=height;
				if((matriceCollecte[x][j]==Etats.VIDE 
						|| matriceCollecte[x][j]==Etats.VIDE_PRIORITAIRE
						|| matriceCollecte[x][j]==Etats.VIDE_ET_MUR_DESTRUCTIBLE
						|| matriceCollecte[x][j]==Etats.ADVERSAIRE))
				{
					if(division)
					{
						// on calcule le point d'une telle tile comme (point/distance_entre_l'h�ro_et_la_case
						int distance=zone.getTileDistance(ourHero.getLine(),ourHero.getCol(), x, j);
						if(distance==0) distance=1;
						matriceTotal[x][j]+=note/distance;
					}
					// pour l'utilisation d'imaginaire bomb
					else matriceTotal[x][j]=note;
					
					Color mur= new Color(0, 191, 255);
					Color ennemi= new Color(236,53,232); 
					AiOutput output = notreIA.getOutput();
					if(note==20 || note==100) 
					{
						output.setTileColor(x, j,ennemi);
						if(matriceCollecte[x][j]!=Etats.ADVERSAIRE)
							matriceCollecte[x][j]=Etats.VIDE_PRIORITAIRE;
					}
					else 
						if(note==10)
						{
							output.setTileColor(x, j,mur);
							if(matriceCollecte[x][j]!=Etats.ADVERSAIRE)
								matriceCollecte[x][j]=Etats.VIDE_ET_MUR_DESTRUCTIBLE;
						}
				}
				else break;
			}
			c=-1;
		}		

		// calcule pour les tiles horizontales
		c=1;
		for(int b=0;b<2;b++)
		{
			notreIA.checkInterruption();
			
			for(int a=1;a<porteeDuBomb;a++)
			{
				notreIA.checkInterruption();
				
				d=c*a;
				int y=j-d;
				if(y<0) y+=height;
				else if(y>=height) y-=height;
				if((matriceCollecte[i][y]==Etats.VIDE 
						|| matriceCollecte[i][y]==Etats.VIDE_PRIORITAIRE
						|| matriceCollecte[i][y]==Etats.VIDE_ET_MUR_DESTRUCTIBLE
						|| matriceCollecte[i][y]==Etats.ADVERSAIRE))
				{
					if(division)
					{
						// on calcule le point d'une telle tile comme (point/distance_entre_l'h�ro_et_la_case
						int distance=zone.getTileDistance(ourHero.getLine(),ourHero.getCol(), i, y);
						if(distance==0) distance=1;
						matriceTotal[i][y]+=note/distance;
					}
					// pour l'utilisation d'imaginaire bomb
					else matriceTotal[i][y]=note;
					
					Color mur= new Color(0, 191, 255);
					Color ennemi= new Color(236,53,232); 
					AiOutput output = notreIA.getOutput();
					if(note==20 || note==100) 
					{
						output.setTileColor(i, y,ennemi);
						if(matriceCollecte[i][y]!=Etats.ADVERSAIRE)
							matriceCollecte[i][y]=Etats.VIDE_PRIORITAIRE;
					}
					else 
						if(note==10)
						{
							output.setTileColor(i, y,mur);
							if(matriceCollecte[i][y]!=Etats.ADVERSAIRE)
								matriceCollecte[i][y]=Etats.VIDE_ET_MUR_DESTRUCTIBLE;
						}
				}
				else break;
			}
			c=-1;
		}		

	}

	/**
	 * cette methode calcule nouvelle matrice d'apres un bomb non-reel qu'on veut y mettre en position(line,col)
	 * @param line
	 * @param col
	 * @param matriceTotal matrice reel qui est deja calcule pour jeu
	 * @return matrice imaginaire
	 * @throws StopRequestException
	 */
	public double[][] matricePourImaginaireBomb(int line,int col, double[][] matriceTotal) throws StopRequestException
	{
		double[][] matriceImaginaire= new double[height][width];
		notreIA.checkInterruption();
		for(int i=0;i<height;i++)
		{
			notreIA.checkInterruption();
			for(int j=0;j<width;j++)
			{
				notreIA.checkInterruption();
				matriceImaginaire[i][j]=matriceTotal[i][j];
			}
		}
		
		calculeDapresPortee(matriceImaginaire,line,col,-10,false);
		matriceImaginaire[line][col]=-10;
		
		return matriceImaginaire;
	}
	/**
	 * Dans le mode collecte on calcule s'il est necessaire de poser une bomb ou pas
	 * 
	 * @param matrice
	 * @return
	 * @throws StopRequestException
	 * @throws LimitReachedException 
	 */
	private AiAction decisionBombPourCollecte(double matrice[][],	AiPath chemin) throws StopRequestException, LimitReachedException
	{
		notreIA.checkInterruption();
		
		AiAction result=null;

		int heroLine = ourHero.getLine();
		int heroColonne = ourHero.getCol();


		if(matriceTotal[heroLine][heroColonne]>0)
		{

			if(zone.getHiddenItemsCount()>0)
			{
				if(matriceCollecte[heroLine][heroColonne]==Etats.VIDE_ET_MUR_DESTRUCTIBLE)
				{
					
//					int adversaireAccesible=nbAdversaire;
//					for(int i=0;i<nbAdversaire;i++)
//					{
//						notreIA.checkInterruption();
//						int pathSize=0;
//						Astar path=initialisationAstar();					
//						if(!TileEstAccesible(heroLine, heroColonne, matriceTotal, adversairesActuelles[i].getTile(), pathSize,path))  
//							adversaireAccesible--;
//					}
//					
//					if(adversaireAccesible==0 || ourHero.getBombNumber()-ourHero.getBombCount()>1)
					if(ourHero.getBombNumberMax()-ourHero.getBombNumberCurrent()>1)
					{

						double[][] matriceImaginaire=  matricePourImaginaireBomb(heroLine, heroColonne,  matriceTotal);
						Path path=new Path(notreIA,ourHero, adversairesActuelles);
						
						int longueur=(int) ( bombExplotionVites/ourHero.getWalkingSpeed());
						
						// on calcule tous les tiles qui sont accesible dans le cas de poser un bomb
						path.plusProcheAccesible(heroLine, heroColonne, matriceImaginaire, longueur+2,chemin);

						Color tileAccesible= new Color(255, 255, 255);
						AiOutput output=notreIA.getOutput();
						
						for(int a=0;a<chemin.getLength();a++)
						{
							output.setTileColor(chemin.getTile(a), tileAccesible);
						}
						
						if(chemin.getLength()>0)
						{
							result= new AiAction(AiActionName.DROP_BOMB);
						}

					}
				}
			}
		}		
		return result;	
	}
	
	/**
	 * 
	 * @param matrice
	 * @return 
	 * @throws StopRequestException
	 * @throws LimitReachedException 
	 */
	public AiAction calculeMatriceCollecte(Etats matrice[][]) throws StopRequestException, LimitReachedException
	{
		notreIA.checkInterruption();
	
		AiAction result=null;
		
		for(int i=0;i< height;i++)
		{
			notreIA.checkInterruption();
			
			for(int j=0;j< width;j++)
			{
				notreIA.checkInterruption();
				
				// on donne couleurs aux tiles flammable vert, flammable rouge et inaccesible
				Color flammable_vert= new Color(64,225,68);
				Color flammable_rouge= new Color(251,11,47); 
				Color case_inaccesible=new Color(0,255,0);
				
				AiOutput output = notreIA.getOutput();
				
				switch (matrice[i][j]) {
				case VIDE:
					 matriceTotal[i][j]+=0;
					break;
				case MUR_DESTRUCTIBLE:
					 matriceTotal[i][j]=-100;
					 calculeDapresPortee( matriceTotal,i, j, 10,true);
					break;
				case MUR_NON_DESTRUCTIBLE:
					 matriceTotal[i][j]=-100;
					break;
				case BOMB:
					 matriceTotal[i][j]=-100;
					break;
				case ADVERSAIRE_BOMB:
					 matriceTotal[i][j]=-100;
					break;
				case FLAMMABLE_VERT:
					 matriceTotal[i][j]+=-50;
					output.setTileColor(i, j,flammable_vert);
					break;
				case FLAMMABLE_ROUGE:
					 matriceTotal[i][j]=-100;
					output.setTileColor(i, j,flammable_rouge);
					break;
				case FEU:
					 matriceTotal[i][j]=-100;
					break;
				case ADVERSAIRE:
					int distance=zone.getTileDistance(ourHero.getLine(),ourHero.getCol(), i, j);
					if(distance==0) distance=1;
					 matriceTotal[i][j]+=20/distance;
					 calculeDapresPortee( matriceTotal,i, j, 20,true);
					break;					
				case BONUS:
					/*
					 * on calcule tous les distances entre les heros et les bonus
					 * a la fin on calcule la matrice totale comme 
					 *   matriceTotal[i][j]+=100*(minDistance/distance1);
					 */
					int distance1 = zone.getTileDistance(ourHero.getLine(), ourHero.getCol(), i, j);
					int distance2[]= new int[ nbAdversaire];
					int minDistance=100;
					for(int x=0;x< nbAdversaire;x++)
					{
						notreIA.checkInterruption();
						distance2[x]=zone.getTileDistance( adversairesActuelles[x].getLine(),  adversairesActuelles[x].getCol(), i, j);
						if(distance2[x]<minDistance) minDistance=distance2[x];
					}
					 matriceTotal[i][j]+=100*((double)minDistance/distance1);
					break;
				default:
					break;
				}
				
				// on donne le couleur vert aux tiles inaccesible en mode collecte par notre h�ro
				if( matriceTotal[i][j]==-100) output.setTileColor(i, j,case_inaccesible);

			}	
		}

		result=decisionBombPourCollecte( matriceTotal,chemin);
		
		Path path=new Path(notreIA, ourHero, adversairesActuelles);
		
		
		System.out.println("heeyyo chemin"+chemin.toString()+"longeur"+chemin.getLength());

		if(matriceTotal[ourHero.getLine()][ourHero.getCol()]<0)
		{
			
			AiTile tileSafe=null;
			if(chemin.getLength()>0)
			{
				CostCalculator cost=new BasicCostCalculator();
				HeuristicCalculator heuristique=new BasicHeuristicCalculator();
				Astar astar=new Astar(notreIA, ourHero, cost, heuristique);
				
				for(int i=0;i<chemin.getLength();i++)
				{
					if(matriceTotal[chemin.getTile(i).getLine()][chemin.getTile(i).getCol()]>=0)
					{
						tileSafe=chemin.getTile(i);
					}
				}
				if(tileSafe!=null)
				{
					System.out.println("heyy tile safe");
					AiPath aipath=astar.processShortestPath(ourHero.getTile(), tileSafe);
					result=path.newAction(aipath, ourHero);
				}			
			}
		}
		if(result==null)
		{
			AiPath pathCible=path.trouverCible(ourHero.getLine(), ourHero.getCol(),matriceTotal);
			result=path.newAction(pathCible, ourHero);
		}
		
		return result;
	}
	/**
	 * pour le mode attaque on calcule s'il est necessaire de poser une bomb ou pas
	 * 
	 * @param matrice
	 * @return
	 * @throws StopRequestException
	 * @throws LimitReachedException 
	 */
	private AiAction decisionBombPourAttaque(double matrice[][],AiPath chemin) throws StopRequestException, LimitReachedException
	{
		notreIA.checkInterruption();
		AiAction result=null;

		int heroLine = ourHero.getLine();
		int heroColonne = ourHero.getCol();
		
		if( matriceTotal[heroLine][heroColonne]>0)
		{
			if( matriceCollecte[heroLine][heroColonne]==Etats.VIDE_PRIORITAIRE || matriceCollecte[heroLine][heroColonne]==Etats.VIDE_ET_MUR_DESTRUCTIBLE )
			{
				@SuppressWarnings("unused")
				int bombRestant=ourHero.getBombNumberMax()-ourHero.getBombNumberCurrent();
				
						
//				if((bombRestant>=0 &&  matriceTotal[heroColonne][heroLine]>25)
//						|| (bombRestant>=2 &&  matriceTotal[heroColonne][heroLine]>35)
//						|| (bombRestant>=1 &&  matriceTotal[heroColonne][heroLine]>50)
//						)
				{
					
					double[][] matriceImaginaire=  matricePourImaginaireBomb(heroLine, heroColonne,  matriceTotal);
					Path path=new Path(notreIA,ourHero, adversairesActuelles);
					
					int longueur=(int) ( bombExplotionVites/ourHero.getWalkingSpeed());
					
					path.plusProcheAccesible(heroLine, heroColonne, matriceImaginaire, longueur+2,chemin);

					Color tileAccesible= new Color(255, 255, 255);
					AiOutput output=notreIA.getOutput();
					
					for(int a=0;a<chemin.getLength();a++)
					{
						output.setTileColor(chemin.getTile(a), tileAccesible);
					}
					
					
					if(chemin.getLength()>0)			
					{
						result= new AiAction(AiActionName.DROP_BOMB);
					}
					
				}
			}
		}

		return result;	
	}	



	public AiAction calculeMatriceAttaque(Etats matrice[][]) throws StopRequestException, LimitReachedException
	{
		notreIA.checkInterruption();
	
		AiAction result = null;
		
		for(int i=0;i< height;i++)
		{
			notreIA.checkInterruption();
			
			for(int j=0;j< width;j++)
			{
				notreIA.checkInterruption();
				
				// on donne couleurs aux tiles flammable vert, flammable rouge et inaccesible
				Color flammable_vert= new Color(64,225,68);
				Color flammable_rouge= new Color(251,11,47); 
				Color case_inaccesible=new Color(255,0,0);
				
				AiOutput output = notreIA.getOutput();
				
				switch (matrice[i][j]) {
				case VIDE:
					 matriceTotal[i][j]+=0;
					break;
				case MUR_DESTRUCTIBLE:
					 matriceTotal[i][j]=-100;
					 calculeDapresPortee( matriceTotal,i, j, 10,true);
					break;
				case MUR_NON_DESTRUCTIBLE:
					 matriceTotal[i][j]=-100;
					break;
				case BOMB:
					 matriceTotal[i][j]=-100;
					break;
				case ADVERSAIRE_BOMB:
					 matriceTotal[i][j]=-100;
					break;
				case FLAMMABLE_VERT:
					 matriceTotal[i][j]+=-50;
					output.setTileColor(i, j,flammable_vert);
					break;
				case FLAMMABLE_ROUGE:
					 matriceTotal[i][j]=-100;
					output.setTileColor(i, j,flammable_rouge);
					break;
				case FEU:
					 matriceTotal[i][j]=-100;
					break;
				case ADVERSAIRE:
					int distance=zone.getTileDistance(ourHero.getLine(),ourHero.getCol(), i, j);
					if(distance==0) distance=1;
					 matriceTotal[i][j]+=100/distance;
					 calculeDapresPortee( matriceTotal,i, j, 100,true);
					break;					
				case BONUS:
					/*
					 * on calcule tous les distances entre les heros et les bonus
					 * a la fin on calcule la matrice totale comme 
					 *   matriceTotal[i][j]+=100*(minDistance/distance1);
					 */
					int distance1 = zone.getTileDistance(ourHero.getLine(), ourHero.getCol(), i, j);
					int distance2[]= new int[ nbAdversaire];
					int minDistance=100;
					for(int x=0;x< nbAdversaire;x++)
					{
						notreIA.checkInterruption();
						distance2[x]=zone.getTileDistance( adversairesActuelles[x].getLine(),  adversairesActuelles[x].getCol(), i, j);
						if(distance2[x]<minDistance) minDistance=distance2[x];
					}
					 matriceTotal[i][j]+=20*((double)minDistance/distance1);
					break;
				default:
					break;
				}
				
				// on donne le couleur rouge aux tiles inaccesible par notre h�ro
				if( matriceTotal[i][j]==-100) output.setTileColor(i, j,case_inaccesible);
			}	
		}
		
		Path path=new Path(notreIA, ourHero, adversairesActuelles);
		
		result=decisionBombPourAttaque( matriceTotal,chemin);
		
		if(matriceTotal[ourHero.getLine()][ourHero.getCol()]<0)
		{
			
			AiTile tileSafe=null;
			if(chemin.getLength()>0)
			{
				CostCalculator cost=new BasicCostCalculator();
				HeuristicCalculator heuristique=new BasicHeuristicCalculator();
				Astar astar=new Astar(notreIA, ourHero, cost, heuristique);
				
				for(int i=0;i<chemin.getLength();i++)
				{
					if(matriceTotal[chemin.getTile(i).getLine()][chemin.getTile(i).getCol()]>=0)
					{
						System.out.println("heyy tile safe");
						tileSafe=chemin.getTile(i);
					}
				}
				if(tileSafe!=null)
				{
					AiPath aipath=astar.processShortestPath(ourHero.getTile(), tileSafe);
					result=path.newAction(aipath, ourHero);
				}			
			}

		}

		if(result==null)
		{
			
			AiPath pathCible=path.trouverCible(ourHero.getLine(), ourHero.getCol(),matriceTotal);
		    result=path.newAction(pathCible, ourHero);
		}

		return result;

	}

	
	/**
	 * 
	 * @param matrice
	 * @return
	 * @throws StopRequestException
	 * @throws LimitReachedException 
	 */
	public AiAction choisirLeMode(Etats matrice[][]) throws StopRequestException, LimitReachedException
	{
		AiAction result = null;
		notreIA.checkInterruption();

		
		
		int distance1[] = new int[ nbBonus];
		int distance2[]= new int[ nbAdversaire];
		int minDistance1=100,minDistance2=100;
		
		for(int x=0;x< nbBonus;x++)
		{
			notreIA.checkInterruption();
			
			distance1[x]=zone.getTileDistance( bonuses[x].getLine(),  bonuses[x].getCol(), ourHero.getLine(), ourHero.getCol());
			if(distance1[x]<minDistance1) minDistance1=distance1[x];
		}
		
		for(int x=0;x< nbAdversaire;x++)
		{
			notreIA.checkInterruption();
			
			distance2[x]=zone.getTileDistance( adversairesActuelles[x].getLine(),  adversairesActuelles[x].getCol(), ourHero.getLine(), ourHero.getCol());
			if(distance2[x]<minDistance2) minDistance2=distance2[x];
		}
		
		if( nbBonus>0)
		{
			if(minDistance1>minDistance2)
			{
				result= calculeMatriceAttaque(matrice);
			}
			else result= calculeMatriceCollecte(matrice);
		}
		else 
			if( nbMurDestructible>0)
			{
				int maxNbBonus=0;
				int nbBonusDesAdversaires[]= new int[ nbAdversaire];
				for(int x=0;x< nbAdversaire;x++)
				{
					notreIA.checkInterruption();
					nbBonusDesAdversaires[x]= adversairesActuelles[x].getBombNumberMax()+ adversairesActuelles[x].getBombRange();
					if(nbBonusDesAdversaires[x]>maxNbBonus) maxNbBonus=nbBonusDesAdversaires[x];
				}
				int monNbBonus=ourHero.getBombNumberMax()+ourHero.getBombRange();
				
				if(monNbBonus<maxNbBonus) 
					result= calculeMatriceCollecte(matrice);
				else 
					result= calculeMatriceAttaque(matrice);
			}
			else result= calculeMatriceAttaque(matrice);
		return result;
	}

	
	
	/**
	 * methode affichante la matriceTotale en console et en zone de jeu à la fois
	 * 
	 * @param matriceTotal
	 * @throws StopRequestException
	 */
	public void afficherMatrice(double matriceTotal[][]) throws StopRequestException
	{
		notreIA.checkInterruption();
		
		for(int i=0;i<height;i++)
		{
			notreIA.checkInterruption();
			
			for(int j=0;j<width;j++)
			{
				notreIA.checkInterruption();
				AiOutput output = notreIA.getOutput();
				output.setTileText(i, j, ""+matriceTotal[i][j]);
				
				//System.out.print(matriceTotal[i][j]+"  ");

			}
			
			//System.out.println("");
		}
		//System.out.println("");

	}

	
}
