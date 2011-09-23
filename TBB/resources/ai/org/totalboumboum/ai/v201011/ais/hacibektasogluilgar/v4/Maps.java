package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v4;

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
import org.totalboumboum.ai.v201011.adapter.data.AiItemType;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;


/**
 * @author Engin Hacıbektaşoğlu
 * @author Elif Nurdan İlgar
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
	static long bombExplosionVitesse=700;
	AiPath chemin=new AiPath();
	
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
	 * @param matrice
	 * @throws StopRequestException
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
	
	/**
	 * 
	 * @param matrice
	 * @throws StopRequestException
	 */
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
	

	/**
	 * 
	 * @param matrice
	 * @throws StopRequestException
	 */
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

			bombExplosionVitesse=bomb.getExplosionDuration();
			List<AiTile> inScopeTiles = bomb.getBlast();
			for (int i = 0; i < inScopeTiles.size(); i++) {
				notreIA.checkInterruption();
				/*
				 *  on controle la duree de la bomb pour pouvoir comprendre si
				 *  c'est bien de passer par ce tile ou non   
				 */
			

						
				if(bomb.getTime()<bomb.getNormalDuration()/2
						&& matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()]!=Etats.FLAMMABLE_ROUGE)
				matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()] = Etats.FLAMMABLE_VERT;
				else
					matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()] = Etats.FLAMMABLE_ROUGE;
			}
			
			matrice[x][y]=Etats.BOMB;

		}
	}
	
	/**
	 * 
	 * @param matrice
	 * @throws StopRequestException
	 */
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
	/**
	 * 
	 * @param matrice
	 * @throws StopRequestException
	 */
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
	/**
	 * 
	 * 
	 * @param matrice
	 * @throws StopRequestException
	 */
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
	
	/**
	 * on fait mis à jour de tous les matrices calculé pour avoir total matrice du zone
	 * 
	 * @param matrice
	 * @throws StopRequestException
	 */
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
						|| matriceCollecte[x][j]==Etats.VIDE_AU_MUR_DESTRUCTIBLE
						|| matriceCollecte[x][j]==Etats.ADVERSAIRE))
				{

					if(division)
					{
						// on calcule le point d'une telle tile comme (point/distance_entre_l'héro_et_la_case
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
								matriceCollecte[x][j]=Etats.VIDE_AU_MUR_DESTRUCTIBLE;
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
			
			for(int a=1;a<porteeDuBomb+1;a++)
			{
				notreIA.checkInterruption();
				
				d=c*a;
				int y=j-d;
				if(y<0) y+=height;
				else if(y>=height) y-=height;
				if((matriceCollecte[i][y]==Etats.VIDE 
						|| matriceCollecte[i][y]==Etats.VIDE_PRIORITAIRE
						|| matriceCollecte[i][y]==Etats.VIDE_AU_MUR_DESTRUCTIBLE
						|| matriceCollecte[i][y]==Etats.ADVERSAIRE))
				{
					if(division)
					{
						// on calcule le point d'une telle tile comme (point/distance_entre_l'héro_et_la_case
						int distance=zone.getTileDistance(ourHero.getLine(),ourHero.getCol(), i, y);
						if(distance==0) distance=1;
						matriceTotal[i][y]+=note/distance;
					}
					// pour l'utilisation d'imaginaire bomb
					else
						matriceTotal[i][y]=note;
						
					
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
								matriceCollecte[i][y]=Etats.VIDE_AU_MUR_DESTRUCTIBLE;
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
	 * cette methode controle le chemin pour voir s'il y en a des tiles dangeraux, 
	 * s'il les trouve alors il va nous dire "réfléchissez encore une fois avant y aller"
	 * 
	 * @param pathCible le chemin à controller
	 * @return la resultat du control (true ou false)
	 * @throws StopRequestException
	 */
	private boolean pathCibleControl(AiPath pathCible) throws StopRequestException
	{
		notreIA.checkInterruption();
		boolean control=true;
		
		for(int i=0;i<pathCible.getLength();i++)
		{
			notreIA.checkInterruption();
			
			int line=pathCible.getTile(i).getLine();
			int col=pathCible.getTile(i).getCol();
			if(matriceTotal[line][col]<0)
			{
				control=false;
			}
		}
		
		return control;
		
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
				if(matriceCollecte[heroLine][heroColonne]==Etats.VIDE_AU_MUR_DESTRUCTIBLE)
				{
					if(ourHero.getBombNumberMax()-ourHero.getBombNumberCurrent()>1)
					{

						double[][] matriceImaginaire=  matricePourImaginaireBomb(heroLine, heroColonne,  matriceTotal);
						Path path=new Path(notreIA,ourHero, adversairesActuelles);
						
						int longueur=(int) ( bombExplosionVitesse/ourHero.getWalkingSpeed());
						
						// on calcule tous les tiles qui sont accesible dans le cas de poser un bomb
						
						path.plusProcheAccesible(heroLine, heroColonne, matriceCollecte, matriceImaginaire, longueur,chemin);
					
						boolean control=path.voisineControl(matriceTotal);

						if(control)	
						{
							if(chemin.getLength()>0)
								result= new AiAction(AiActionName.DROP_BOMB);
							else matriceTotal[ourHero.getLine()][ourHero.getCol()]=0;
						}

					}
				}
			}
		}		
		return result;	
	}
	
	/**
	 * cette methode calcule le matrice collecte
	 * 
	 * @param matrice
	 * @return une action
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
				
				// on donne le couleur vert aux tiles inaccesible en mode collecte par notre héro
				if( matriceTotal[i][j]==-100) output.setTileColor(i, j,case_inaccesible);

			}	
		}
		
		result=decisionBombPourCollecte( matriceTotal,chemin);
				
		Path path=new Path(notreIA, ourHero, adversairesActuelles);

		if(result==null)
		{
			AiPath pathCible=path.trouverCible(ourHero.getLine(), ourHero.getCol(),matriceTotal);
			
			if(matriceTotal[ourHero.getLine()][ourHero.getCol()]>=0 && !pathCibleControl(pathCible))
			{
				pathCible=new AiPath();
				pathCible.addTile(ourHero.getTile());
			}
			
			if(pathCible!=null && pathCible.getLength()>0)
				result=path.nouvelleAction(pathCible, ourHero);
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
			if( matriceCollecte[heroLine][heroColonne]==Etats.VIDE_PRIORITAIRE ||
					matriceCollecte[heroLine][heroColonne]==Etats.VIDE_AU_MUR_DESTRUCTIBLE
//					|| 	matriceCollecte[heroLine][heroColonne]==Etats.MUR_DESTRUCTIBLE
					|| 	matriceCollecte[heroLine][heroColonne]==Etats.ADVERSAIRE)
			{
									
					double[][] matriceImaginaire=  matricePourImaginaireBomb(heroLine, heroColonne,  matriceTotal);
					Path path=new Path(notreIA,ourHero, adversairesActuelles);					
					int longueur=(int) ( bombExplosionVitesse/ourHero.getWalkingSpeed());
					
					path.plusProcheAccesible(heroLine, heroColonne, matriceCollecte, matriceImaginaire, longueur,chemin);
					
					
					boolean control= path.voisineControl(matriceTotal);
					
					if(control && matriceTotal[ourHero.getLine()][ourHero.getCol()]>=10)	
					{
						if(chemin.getLength()>0)
							result= new AiAction(AiActionName.DROP_BOMB);
						else matriceTotal[ourHero.getLine()][ourHero.getCol()]=0;
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
				
				// on donne le couleur rouge aux tiles inaccesible par notre héro
				if( matriceTotal[i][j]==-100) output.setTileColor(i, j,case_inaccesible);
			}	
		}


	
		Path path=new Path(notreIA, ourHero, adversairesActuelles);
			
		result=decisionBombPourAttaque( matriceTotal,chemin);

		
		if(result==null)
		{	
			AiPath pathCible=path.trouverCible(ourHero.getLine(), ourHero.getCol(),matriceTotal);	
			
			if(matriceTotal[ourHero.getLine()][ourHero.getCol()]>=0 && !pathCibleControl(pathCible))
			{
				pathCible=new AiPath();
				pathCible.addTile(ourHero.getTile());
			}
			
			
			if(pathCible!=null && pathCible.getLength()>0)
			{
				result=path.nouvelleAction(pathCible, ourHero);
			}
		}

		return result;

	}

	
	/**
	 * on choisi le mode d'apres les criteres
	 * 
	 * @param matrice
	 * @return
	 * @throws StopRequestException
	 * @throws LimitReachedException 
	 */
	public AiAction choisirLeMode(Etats matrice[][]) throws StopRequestException, LimitReachedException
	{
		AiAction result;
		notreIA.checkInterruption();
		int maxNbBonus=0;
		
		for(int x=0;x<nbAdversaire;x++)
		{
			notreIA.checkInterruption();
			 
			int nbBonusDeAdversaire=adversairesActuelles[x].getBombNumberMax();
			if(nbBonusDeAdversaire>maxNbBonus) maxNbBonus=nbBonusDeAdversaire;
		}
		
		if(ourHero.getBombNumberMax()<maxNbBonus)
		{
			if(nbBonus>0)
			{
				int nbFlammeExtra=0;
				int nbBonusExtra=0;
				for(int i=0;i<nbBonus;i++)
				{
					notreIA.checkInterruption();
					if(bonuses[i].getType().equals(AiItemType.EXTRA_FLAME))
					{
						if(ourHero.getBombRange()<9)
						{
							nbFlammeExtra++;
						}
					}
					else if(bonuses[i].getType().equals(AiItemType.EXTRA_BOMB))
					{
						if(ourHero.getBombNumberMax()<9)
						{
							nbBonusExtra++;
						}
					}
				}
				if(nbFlammeExtra>0 || nbBonusExtra>0)
				{
					result=calculeMatriceCollecte(matrice);
				}
				else result=calculeMatriceAttaque(matrice);
			}
			else if(nbMurDestructible>0)
			{
				result=calculeMatriceCollecte(matrice);
			}else 
				result=calculeMatriceAttaque(matrice);
		}
		else result=calculeMatriceAttaque(matrice);

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

			}
		}
	}

	
}
