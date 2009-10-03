package fr.free.totalboumboum;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.SplashScreen;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.data.configuration.misc.MiscConfiguration;
import fr.free.totalboumboum.gui.frames.NormalFrame;
import fr.free.totalboumboum.gui.frames.QuickFrame;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.XmlTools;

public class Launcher
{	
	public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	List<String> argList = Arrays.asList(args); 
		if(argList.contains(GuiTools.OPTIONS[GuiTools.OPTION_HELP]))
			displayHelp();
		else
		{	forceWindow = argList.contains(GuiTools.OPTIONS[GuiTools.OPTION_WINDOW]);
			if(argList.contains(GuiTools.OPTIONS[GuiTools.OPTION_QUICK]))
				quickLaunch();
			else
				normalLaunch();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static void displayHelp()
	{	System.out.println(GuiTools.OPTION_HELP_MESSAGE);
		for(int i=0;i<GuiTools.OPTIONS.length;i++)
			System.out.println("    ->"+GuiTools.OPTIONS[i]+": "+GuiTools.OPTIONS_HELP[i]);
	}
	
	/////////////////////////////////////////////////////////////////
	// NORMAL LAUNCH	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static void normalLaunch() throws SAXException, ParserConfigurationException, IllegalArgumentException, SecurityException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// splashscreen
		SplashScreen splash = SplashScreen.getSplashScreen();
		
		// load XML schemas
		updateSplash(splash,GuiTools.STARTUP_MESSAGES[GuiTools.STARTUP_XML]);
		XmlTools.init();
		// load configuration
		updateSplash(splash,GuiTools.STARTUP_MESSAGES[GuiTools.STARTUP_CONFIG]);
		Configuration.loadConfiguration();
		// load GUI configuration
		updateSplash(splash,GuiTools.STARTUP_MESSAGES[GuiTools.STARTUP_GUI]);
		GuiConfiguration.loadConfiguration();
		resolutionSelection();
		// initalize GUI
		updateSplash(splash,GuiTools.STARTUP_MESSAGES[GuiTools.STARTUP_INIT]);
		GuiTools.init();
		// startup finished
		updateSplash(splash,GuiTools.STARTUP_MESSAGES[GuiTools.STARTUP_DONE]);
		
		// create GUI
		SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	try
				{	NormalFrame normalFrame = new NormalFrame();
					if(fullScreen)
						normalFrame.makeVisible(device,newMode);
					else
						normalFrame.makeVisible();
				}
				catch (IllegalArgumentException e)
				{	e.printStackTrace();
				}
				catch (SecurityException e)
				{	e.printStackTrace();
				}
				catch (ParserConfigurationException e)
				{	e.printStackTrace();
				}
				catch (SAXException e)
				{	e.printStackTrace();
				}
				catch (IOException e)
				{	e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{	e.printStackTrace();
				}
				catch (NoSuchFieldException e)
				{	e.printStackTrace();
				}
				catch (ClassNotFoundException e)
				{	e.printStackTrace();
				}
			}
		});		
	}
	
	private static void updateSplash(SplashScreen splash, String msg)
	{	if(splash!=null)
		{	Graphics2D g = (Graphics2D)splash.createGraphics();
			Rectangle size = splash.getBounds();
			g.setComposite(AlphaComposite.Clear);
			g.fillRect(0,0,size.width,size.height);
			g.setPaintMode();
			g.setFont(new Font("Arial",Font.PLAIN,10));
			g.setColor(new Color(0,0,0,100));
			for(int i=0;i<GuiTools.STARTUP_LEGAL.length;i++)
				g.drawString(GuiTools.STARTUP_LEGAL[i],70,90+i*10);
			g.setColor(GuiTools.COLOR_SPLASHSCREEN_TEXT);
	        g.drawString(msg,70,315);
	        splash.update();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// QUICK LAUNCH		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static void quickLaunch() throws SAXException, ParserConfigurationException, IllegalArgumentException, SecurityException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// legal
		for(int i=0;i<GuiTools.STARTUP_LEGAL.length;i++)
			System.out.println(GuiTools.STARTUP_LEGAL[i]);
		System.out.println("--------------------------------");
		// load XML schemas
		System.out.println(GuiTools.STARTUP_MESSAGES[GuiTools.STARTUP_XML]);
		XmlTools.init();
		// load configuration 
		System.out.println(GuiTools.STARTUP_MESSAGES[GuiTools.STARTUP_CONFIG]);
		Configuration.loadConfiguration();
		// load GUI configuration
		System.out.println(GuiTools.STARTUP_MESSAGES[GuiTools.STARTUP_GUI]);
		MiscConfiguration miscConfig = new MiscConfiguration();
		miscConfig.setFont(null,new Font("Arial",Font.PLAIN,10));
		GuiConfiguration.setMiscConfiguration(miscConfig);
		resolutionSelection();
		// initalize GUI
		System.out.println(GuiTools.STARTUP_MESSAGES[GuiTools.STARTUP_INIT]);
		GuiTools.quickInit();
		// done
		System.out.println(GuiTools.STARTUP_MESSAGES[GuiTools.STARTUP_DONE]);
		
		// create GUI
		SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	try
				{	QuickFrame quickFrame = new QuickFrame();
					if(fullScreen)
						quickFrame.makeVisible(device,newMode);
					else
						quickFrame.makeVisible();
					quickFrame.begin();
				}
				catch (IllegalArgumentException e)
				{	e.printStackTrace();
				}
				catch (SecurityException e)
				{	e.printStackTrace();
				}
				catch (ParserConfigurationException e)
				{	e.printStackTrace();
				}
				catch (SAXException e)
				{	e.printStackTrace();
				}
				catch (IOException e)
				{	e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{	e.printStackTrace();
				}
				catch (NoSuchFieldException e)
				{	e.printStackTrace();
				}
				catch (ClassNotFoundException e)
				{	e.printStackTrace();
				}
			}
		});
	}
	
	/////////////////////////////////////////////////////////////////
	// RESOLUTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static boolean forceWindow;
	private static GraphicsDevice device;
	private static DisplayMode newMode;
	private static boolean fullScreen;

	private static void resolutionSelection()
	{	fullScreen = false;
		// graphic conf
		GraphicsEnvironment graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = graphEnv.getDefaultScreenDevice();

		// full screen size
		if(!forceWindow && Configuration.getVideoConfiguration().getFullScreen())
		{	if(device.isFullScreenSupported())
			{	fullScreen = true;
				Dimension contentDimension = Configuration.getVideoConfiguration().getPanelDimension();
				final int width = contentDimension.width;
				final int height = contentDimension.height;
				DisplayMode currentMode = device.getDisplayMode();
				final int bitDepth;
				if(currentMode.getBitDepth()>16)
					bitDepth = 16;
				else
					bitDepth = currentMode.getBitDepth();
				final int refreshRate = currentMode.getRefreshRate();
				DisplayMode modes[] = device.getDisplayModes();
				List<DisplayMode> modeList = Arrays.asList(modes);
				Collections.sort(modeList,new Comparator<DisplayMode>()
				{	@Override
					public int compare(DisplayMode arg0, DisplayMode arg1)
					{	int result;
						// width
						{	int w0 = arg0.getWidth();
							int w1 = arg1.getWidth();
							result = Math.abs(w0-width)-Math.abs(w1-width);
						}
						// height
						if(result==0)
						{	int h0 = arg0.getHeight();
							int h1 = arg1.getHeight();
							result = Math.abs(h0-height)-Math.abs(h1-height);
						}
						// refresh rate
						if(result==0)
						{	int rr0 = arg0.getRefreshRate();
							int rr1 = arg1.getRefreshRate();
							result = Math.abs(rr0-refreshRate)-Math.abs(rr1-refreshRate);
						}
						// bit depth
						if(result==0)
						{	int bd0 = arg0.getBitDepth();
							int bd1 = arg1.getBitDepth();
							result = Math.abs(bd0-bitDepth)-Math.abs(bd1-bitDepth);
						}
						return result;
					}
				});
				newMode = modeList.get(0);
				contentDimension.setSize(newMode.getWidth(),newMode.getHeight());
			}
		}		
	}
	
	
	
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	
	
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// PERMANENT	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * TODO PARAMETRES MEMOIRE
	 * -Xms32m -Xmx512m
	 */
	
	/* TODO conseils david :
	 * 	- champ final pr forcer l'initialisation
	 * 	- �criture courte du for pour iterator
	 */

	/*
	 * TODO r�gle pour les loaders :
	 * 	- l'objet � charger est cr�� dans la premi�re m�thode appel�e lors du chargement
	 *  - les m�thode secondaires re�oivent l'objet cr��, et le compl�tent.
	 *  - quand des objets secondaires sont charg�s ind�pendamment, ie dans des m�thodes publiques, ils sont eux aussi cr��s dans la m�thode appel�e
	 */

	/* TODO mettre tout ce qui appelle du swing dans le thread adapt�
 		SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	initGui();	
			}
		});
	 */

	/* TODO quand une classe est modifi�e ou cr��e :
	 * il faut impl�menter la fonction finish qui permet d'effacer toutes les r�f�rences qu'elle utilisait
	 */
	
	/*
	 * TODO GUI
	 * Split : re�oit les param�tres mais ne les met pas en cache
	 * Menu  : met les originaux en cache, envoie une copie au data
	 * Data  : modifie les param�tres re�us. le menu se charge de les r�cup�rer, les comparer et �ventuellement les sauver
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// IA/TOURNAMENT	//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * TODO il faut r�cup�rer toutes les exceptions �ventuellement caus�es par les IA
	 * par exemple en les routant sur une fen�tre d�bug sp�ciale ?
	 * (cf OkanYuksel qui fait un d�passement de la pile)
	 */
	
	/*
	 * TODO dans l'API IA il faut g�n�raliser (tout en simplifiant) les propri�t�s des sprites, plutot que 
	 * de d�finir des fonctions sp�cifiques � chaque fois.
	 */
	
	/*
	 * TODO type de tournoi plus adapt� au r�seau :
	 * des joueurs jouent un match pendant que d'autres attendent 
	 * � la fin du match, les n derniers joueurs sont rel�gu�s dans la salle d'attente
	 * n joueurs qui attendaient sont qualifi�s.
	 * un classement permet de d�terminer le leader provisoire
	 */
	
	/*
	 * TODO inclure dans l'API IA des fonctions d'action pr�d�finies.
	 * par exemple des fonctions pour le d�placement, on indique o� on veut aller et �a d�termine � chaque it�ration
	 * quelle est l'action � effectuer. �tendre � d'autres actions ?
	 * limitation p�dago: on n'utilise plus A*
	 */
	
	/*
	 * TODO include dans l'API une fonction d'initialisation de l'IA, appel�e lors de sa cr�ation
	 * faudrait passer en param�tre le niveau et des infos style l'instance, etc.
	 * �a permettrait par exemple, pour une IA qui apprend, de t�l�charger son fichier de sauvegarde
	 * (dans son package)
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// MULTITHREADING	//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO il ne faut pas ex�cuter toutes les IA dans un thread pool, mais au contraire les s�parer
	 * sinon, une seule IA bouclant � l'infini va consommer tous les threads dispo dans le treadpool
	 * vont �tre successivement monopolis�s par l'IA
	 */
	
	/*
	 * TODO une fois qu'on a d�termin� les nombres de joueurs, y a moyen de g�rer les threads de meilleure mani�re en :
	 * 		- cr�ant un executor au niveau du tournoi
	 * 		- il doit contenir un pool de (nbre d'IA max pvant jouer � la fois)+1(pr loop)
	 * 		- par la suite, au lieu de cr�er un thread pour chaque ia ou pour le chargement/loop, on en demande un � l'executor
	 * http://java.sun.com/javase/6/docs/api/java/util/concurrent/ExecutorService.html
	 * http://java.sun.com/docs/books/tutorial/essential/concurrency/pools.html
	 * http://java.sun.com/docs/books/tutorial/essential/concurrency/interrupt.html
	 * >>> pb: si un thread entre en boucle infinie, il ne sera jamais termin�, donc l'executor devra cr�er/g�rer un thread de plus
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// BOMBS		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO autoriser de poser une derni�re bombe avant de mourir ? (i.e. dans le gesture burning) comme ds XBlast
	 */						
	
	/*
	 * TODO lorsqu'une bombe est touch�e par une flamme, elle bloque la flamme.
	 * ce qu'on voit passer est la flamme r�sultant de l'explosion de la bombe.
	 */

	/*
	 * TODO dans l'eventMgr de la bombe, quand elle oscille, il faut g�rer les sprites
	 * qui sont en train de la pousser, et non pas en simple contact.
	 * de plus, il faut que les sprites aient le pouvoir de pousser, sinon ils ne comptent pas.
	 */
	
	/*
	 * TODO
	 * quand des sprites de plusieurs cases seront d�finis, peut-�tre que les explosions
	 * devront �tre d�finies en tant que sprites (param�trables pour leur taille) de ce type ?
	 * ce qui permettrait de les faire se d�placer.
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// CONTROLS		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO lors de l'impl�mentation du jeu en r�seau,
	 * une optimisation consiste � ne pas g�n�rer des �vts par le controleur tant
	 * qu'une touche est appuy�e, car �a va faire bcp de choses � transmettre,
	 * le controleur �tant situ� cot� client.
	 * plutot, il faut les g�n�rer dans le controlManager, qui est cot� serveur.
	 * et du cp aussi, il faut �viter d'utiliser ce mode de gestion des �v�nements
	 * pour les autres types d'evts (ie non-controles)
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// EXPLOSION/FIRE	//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	/*
	 * TODO � faire sur le feu (long terme) :
	 * - d�composer l'explosion en :
	 * 		1)explode
	 *		2)stay still
	 *		3)implode
	 * 	pr g�rer le d�placement des flammes � la XBlast
	 * 
	 * ou alternativement :
	 * - d�composer le feu en appearing+standing+disapearing, 
	 * 	ce qui permettrait de le faire durer aussi longtemps qu'on veut
	 */

	/*
	 * TODO explosion:
	 * lorsque la flamme est construite, on teste si chaque case la laisse passer
	 * mais il faut aussi le faire sur la case centrale (origine de l'explosion)
	 * car diff�rents facteurs peuvent limiter l'explosion � une port�e de 0
	 * (sans pour autant emp�cher l'explosion) 
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// GENERAL		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO getClass().getResource(IMAGE_DIR + fnm)
	 * permettrait de trouver un path de fa�on plus propre ? 
	 */    

	/*
	 * TODO dans SBM2, il faut g�rer :
	 * 	- les boss (dans les autres aussi, notamment SBM5)
	 * 	- les esp�ces de chars d'assaut pr�sent dans certains niveaux (utilis�s par les cr�atures)
	 */
	
	/*
	 * TODO d�finir le shrink comme �a :
	 * - s�quence de steps, chacun caract�ris� par :
	 * - un d�lai � laisser �couler apr�s le step pr�c�dent
	 * - la liste des lignes/tiles qui doivent appara�tre, avec le type de bloc devant appara�tre
	 */
	
	/*
	 * TODO pour le d�bug de certains points, il faudra mettre en place un controleur
	 * capable d'enregistrer une s�quence de commandes, ce qui permettra de les rejouer
	 * par la suite (style playback) tout en controlant en temps r�el un autre perso
	 */
	
	/*
	 * TODO animes/trajectoire : permettre de d�finir des labels dans la s�quence
	 * et de faire des goto et des boucles (?)
	 */
	
	/*
	 * TODO dans l'avenir il serait p-� n�cessaire d'utiliser un actionManager,
	 * qui recevrait une action en param�tre, et l'ex�cuterait.
	 * int�r�t : d�composition + utile pour un moteur qui serait seulement un player,
	 * et ne devrait donc pas g�rer les tirages al�atoires, mais seulement l'application
	 * d�terministe des actions (le tirage se ferait dans la fonction appelant l'actionMger,
	 * ie g�n�ralement : l'eventMgr)
	 */
	
	/*
	 * TODO
	 * il faut mieux g�rer l'acc�s aux m�thodes, utiliser plus protected.
	 * surtout pour prot�ger les classes qui devraient �tre inaccessibles depuis l'IA
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ITEMS		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO g�rer le d�placement des items lors d'une explosion
	 */
	
	/*
	 * TODO
	 * item sp�cial permettant de manger les bombes des autres, comme certains ennemis du jeu original en mode histoire 
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// XML			//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/* 
	 * TODO � faire dans les XML : 
	 * 		- faire respecter au maximum les contraintes avec les sch�mas
	 * 		- ensuite seulement faire respecter les contraintes dans les loaders
	 * 		- dans les loaders, penser � g�rer les valeurs/structures par d�faut
	 * exemples :
	 * 		- pour les animes: les noms des gestures doivent �tre diff�rents entre eux et diff�rents de default
	 * 		- pour les th�mes : les noms des components doivent �tre diff�rents
	 */	

    /* 
     * TODO pour forcer l'unicit� d'un attribut/�l�ment, voir ici : 
     * http://rangiroa.essi.fr/cours/internet/02-slides-xml/schema.htm
     */		

	// TODO dans schema de Level : contrainte d'unicit� sur les positions des lignes et colonnes    

	/*
	 * TODO dans le fichier de trajectoire, tester que la valeur de dur�e de for�age 
	 * de position n'est pas sup�rieure � la dur�e de la trajectoire
	 * (et il y a surement de nombreux autres test de coh�rence � effectuer)
	 */					

	/*
	 * TODO � faire dans le chargement des fichiers XML
	 * ANIME : 
	 * - v�rifier que si on utilise une ombre, elle a bien �t� d�clar�e dans le fichier XML
	 * - v�rifier que le mouvement par d�faut existe bel et bien
	 * LEVEL :
	 * - matrice vide = que des floors
	 * - v�rifier que quand on fait r�f�rence � un bloc par son nom, il existe bien dans le fichier du th�me
	 */

	/*
	 * TODO lorsqu'on d�finit une animation ou une trajectoire qui est identique � une autre
	 * plutot que de faire un copier-coller, permettre de nommer l'animation d'origine.
	 * bien sur, il faudra v�rifier que cette animation existe, comme pour celle par d�faut. 
	 */
	
	/*
	 * TODO dans les fichiers XML, il faut pr�ciser en attribut de la racine la version
	 * du fichier utilis�e (i.e. version du sch�ma).
	 */
	
	/*
	 * TODO il faut reprendre les fichiers XML en centralisant au maximum les types
	 * par exemple y en a plein c des r�f�rences vers des fichiers (attributs file et folder)
	 * �a permettrait de centraliser le code �galement dans le XMLTools
	 */
	
	/*
	 * TODO les attributs doivent XML respecter la convention java, et non pas XML (avec tirets)
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// TRAJECTORIES		//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// MODULATIONS	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* 
	 * TODO
	 * dans les modulations third et other, la port�e est limit�e � la case (ou aux cases). 
	 * le contact et surtout le remote ne sont pas encore g�r�s.
	 * id�e pour centraliser le traitement en cas de port�e sans limite:
	 * 	- d�s qu'un sprite change de gesture, ses nouvelles modulations sont analys�es
	 * 	- toutes celles qui sont sans limite de port�e sont stock�es dans un vecteur situ� dans Level 
	 * 		(et toutes celles de l'�tat pr�c�dent sont retir�es de ce m�me vecteur)
	 * 	- lors de la validation de 3rdMod, ce vecteur est syst�matiquement test� 
	 * 		en plus des sprites situ�s pr�s de l'acteur et de la cible 
	 */
	
	/*
	 * NOTE les modulations sont ordonn�es par priorit� dans le fichier XML.
	 * dans le cas o� plusieurs modulations peuvent �tre appliqu�es � une action, 
	 * c'est la premi�re d�finie dans le fichier XML qui est utilis�e.
	 * il faut donc l'organiser du plus sp�cifique au plus g�n�ral.
	*/

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// THEMES/LEVELS	//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * TODO dans le th�me belt, le small pipe laisse passer les flames 
	 * (sans �tre d�truit et sans prot�ger ce qui est sous lui)
	 */
	
	/*
	 * TODO rajouter des niveaux in�dits en utilisant au maximum les parties inutilis�es
	 * des autres th�mes
	 */
	
	/*
	 * TODO rajouter un niveau immeeeeeense (comme dans le gif anim� sur le blog)
	 */
	
	/*
	 * TODO dans le level, il faut g�rer la distribution des items :
	 * possibiliter de la param�trer en fonction du nombre de joueurs ?
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ANIMES	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	/*
	 * TODO lorsque l'anime attendue est cens�e s'arr�ter (pas de r�p�tition)
	 * et que le moteur compte sur un evt de fin pour passer � l'anime suivante,
	 * il y a blocage si l'anime n'est pas d�finie, et que l'anime par d�faut est
	 * r�p�t�e.
	 * exemple : d�faut=marcher, et il manque punch -> le bonhomme ne sort jamais de l'�tat punching
	 * >>>>solution : ne jamais compter sur la fin de l'anime, toujours imposer une dur�e � respecter
	 * (ce qui permet d'uniformiser le beans pour tous les joueurs)
	 */
	
	/*
	 * TODO il faut s�curiser les animations : s'assurer qu'une anime cens�e
	 * ne pas �tre r�-init est bien gaul�e. par exemple, pushing doit �tre gaul�e
	 * comme walking (dans le XML) sinon p-� pb (si pas m�me dur�e ou autres diff)
	 * > � v�rif
	 */
	
	/*
	 * TODO pour Prognathe, faire une anime de d�but de partie : quand le sprite apparait
	 * id�e : sortir du sol
	 * id�e : le corps est d�j� en place, tient la t�te en l'air et la fixe sur ses �paules comme un pilote enfile son casque
	 * 
	 */
	
	/*
	 * TODO animes : permettre de d�finir plusieurs animes pour 1 gesture,
	 * avec choix al�atoire
	 *  
	 */
	
	/*
	 * TODO animes : il faut d�finir un syst�me de substitution d'image
	 * de mani�re � ce qu'il soit impossible d'�tre pris en d�faut
	 */
	
	/*
	 * TODO introduire une anime/action suppl�mentaire : en jeu, un joueur
	 * peut provoquer un autre joueur en d�clenchant une animation sp�cifique.
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ABILITIES	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * TODO y a des trucs qui vont se recouper style :
	 * le bloc doit il emp�cher le joueur de poser une bombe
	 * ou bien la bombe d'appara�tre ?
	 * >>> un bombe peut appara�tre pour autre chose qu'un drop (eg teleport)
	 */

	/*
	 * TODO
	 * le fait qu'il y ait collision ou pas peut changer en fonction de la direction
	 * de d�placement (puisque l'ablt � bloquer peut �tre configur�e suivant la 
	 * direction). donc dans la gestion des collisions sur directions compos�es,
	 * il faut retester avec une requ�te si le fait de retirer une direction provoque
	 * toujours une collision ou pas.
	 */
	
	/*
	 * TODO certaines ablt sont li�es � la trajectoire, par ex : puissance de jump
	 * influe sur la distance du saut.
	 * il faut donc en tenir comtpe dans la trajectoire, en proposant l'utilisation
	 * d'un param�tre externe (au fichier XML)
	 */
	
	/*
	 * TODO quand on calcule l'ablt composite, pour les directions composites :
	 * l'abilit� renvoy�e peut contenir une direction simplifi�e, en fonction 
	 * des autorisations obtenues. 
	 */
	
	/*
	 * TODO pour bien faire, il faudrait lister pour chaque sprite (et chaque configuration)
	 * les actions et les �tats possibles. les permissions repr�sentent les autorisations
	 * pour appliquer une action. il manque la d�finition des actions, indiquant dans quel
	 * �tat un objet se retrouve apr�s avoir effectu�/subi/empech� une action donn�e (ce qui 
	 * d�pend �ventuellement de l'�tat pr�c�dent l'action). 
	 */
	
	/*
	 * TODO le syst�me de gestion des actions est clairement � am�liorer.
	 * certaines actions comme gather sont automatiques. certaines actions ont un effet de zone 
	 * (pr gather : la case).
	 */

	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// BUGS CONNUS	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	/*
	 * TODO quand on balance une bombe, puis une autre, puis fait p�ter la premi�re et enfin la seconde 
	 *  juste � la fin des flammes de la premi�re, alors l'explosion de la 2nde est circonsrite � la
	 *  case o� elle atterrit.
	 *  
	 */
	
	/*  
	 *  TODO l'item kick ne marche pas
	 */
	
	/*  
	 *  TODO quand un mur est d�truit (d�finitivement) par une penetration bomb, l'item n'apparait pas car
	 *  la flamme de la bombe dure plus longtemps que l'explosion du mur, et d�truit l'item aussit�t qu'il apparait
	 *  >> je pense que �a a �t� r�solu �a, � tester !
	 */
	
	/*
	 * TODO
	 * quand on pose deux bombes en diagonale et qu'on se place dans le cadrant int�rieur d'une des cases libres du m�me carr�
	 * on est bloqu�. ce n'est pas vraiment un pb en fait, plus un feature :P . mais les non-initi�s peuvent prendre �a pour un bug.
	 * (note : point mentionn� dans le blog)
	 */
	
	/* TODO
	 * impossible de poser une bombe quand on est en train de buter contre un mur en direction upleft (et uniquement cette direction pr NES2) et downleft (pr SBM1)
	 * 		>> ca viendrait de swing ou du clavier (pb mat�riel ou bas niveau)
	 */
	
	/* TODO
	 * cas particulier : item apparaissant dans une explosion de bloc, avec un joueur d�j� sur le bloc 
	 * (il a passe-muraille et r�sistance au feu) : l'item doit �tre ramass� d�s qu'il a fini d'apparaitre, en th�orie
	 * ou bien : un h�ros meurt, un item apparait sous les pieds d'un autre joueur immobile : l'item reste sans que le joueur ne se l'approprie
	 * >> en m�me temps, �a laisse le choix au joueur de ramasser l'item ou pas, c'est pas mal finalement (si c'est un malus par ex.)
	 */
	
	 /* TODO BUGS POTENTIELS (� v�rifier)
	  * 
	  * - il semblerait que les directions dans la SpecificAction et dans la GeneralAction correspondante ne soient pas les m�mes...
	  * 
	  */
	
	
	
	
	
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// CHANGE LOG	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* TODO
	 * + beta.013
	 * - correction d'un bug lors du chargement des animations des bombes
	 * - correction d'un bug lors du chargement des animations mutualis�es
	 * - modification d'un th�me de NES-BM2 (th�me battle, nouveau sol plus lisible, animations d'apparition)
	 * - correction dans les bombes de NES-BM2 (couleur du reflet de la bombe jaune)
	 * - le gesture ENDED n'est plus d�fini en XML, mais en dur car il est constant : pas d'animation, pas de trajectoire, modulations acteur/target toutes n�gatives, aucune autre modulation
	 * - pareil pour NONE, sauf que l'action APPEAR est autoris�e (en tant qu'acteur)
	 * - introduction des gestures ENTERING et PREPARED, correspondant � APPEARING et STANDING, mais seulement utilis�s pour le d�but du round
	 * - g�n�ralisation (aux autres sprites) de l'Ability permettant de contraindre le temps mis par les heros pour apparaitre au d�but de la partie.
	 * - modification dans le moteur : un sprite dans l'�tat NONE est quand m�me associ� � une case, ce qui permet de simplifier le traitement des actions. par contre il n'a pas de position en pixels
	 * - correction d'un bug dans la gestion des animations et trajectoires proportionnelles
	 * - correction d'un bug dans la g�n�ration des explosions en croix (flamme trop longue)
	 * - d�finition d'un syst�me permettant au niveau d'appara�tre graduellement au d�but du round
	 * - simplification de l'initialisation des actions afin de ne plus avoir � sp�cifier une tile (le cas o� la tile manquait a �t� supprim�)
	 * - apparition de messages au d�but du round (ready-set-go), avec graphismes basiques puis am�lior�s
	 * - mise en place d'un fond noir dans le loop panel avant que le niveau ne commence � apparaitre (juste avant le chargement)
	 * - possibilit� de d�finir des niveaux contenant des bombes, soit directement sur le sol, soit dans des blocks
	 * - d�finition des animations d'apparition pour tous les th�mes de SBM1 et NBM2
	 * - d�finition d'un syst�me de clignotement pour les sprites
	 * - d�finition d'un syst�me al�atoire de d�finition des abilit�s associ�es � un item (pour les malus et les surprise)
	 * - impl�mentation de l'inversion des commandes
	 * - impl�mentation de la gestion des vies, g�n�ralisation du moteur pour permettre au joueur de revenir en jeu
	 * - impl�mentation des modificateurs de vitesse de d�placement au sol pour les h�ros
	 * - impl�mentation de la r�sistance au feu
	 * - nouveau syst�me de maximum pour les StateAbilities (avec une ability supp contenant le suffixe _MAX)
	 * - correction d'un bug concernant la pr�c�dence temporelle de certains �v�nements, en particulier quand deux joueurs br�lent au cours de la m�me explosion
	 * - impl�mentation de la constipation bombique
	 * - correction d'un gros bug dans le calcul de distances entre sprites situ�s dans la classe Level
	 * - possibilit� d'avoir plusieurs blocs, floors et items dans une m�me case (par souci de g�n�ralisation)
	 * - syst�me permettant de relacher les items � la mort du joueur
	 * - correction dans le calcul de la direction entre deux sprites (le c�t� circulaire des niveaux n'�tait pas pris en compte)
	 * - syst�me de contagion des malus
	 * - syst�me de gu�rison des malus
	 * - possibilit� de faire exploser les bombes pos�es par un joueur quand celui-ci br�le
	 * - touche END permettant de pauser le jeu
	 * - meilleure gestion de l'exultation/pleur � la fin des manches
	 * - capacit� de r�sistance au feu pour les items (l'item est d�plac� au lieu d'�tre d�truit)
	 * - blocs laissant passer les flammes mais pas les joueurs ni les bombes (cf niveau story/custom de SBM1)
	 * - instance SBM1 plus compl�te (items, niveaux officiels, nouveaux niveaux custom)
	 * - meilleure gestion des items initiaux (effectivement distribu�s au start)
	 * - correction dans les gestion des blocs spawn
	 * - mise � jour et r�vision de la strucutre de donn�es de l'API d'IA
	 * - impl�mentation de A* dans l'API d'IA
	 * - pause individuelle des IA
	 * - affichage de donn�es relatives aux IA : chemins, vision de la zone de jeu, valeurs case par case, etc.
	 * - touche sp�ciale permettant d'ex�cuter seulement une it�ration du moteur
	 * - IA suiveuse : IA de d�monstration qui choisit un autre joueur puis le suit
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// BUGS				//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* TODO
	 * 
	 * - deux persos entrant en m�me temps sur une case contenant un item le ramassent toutes les  deux (test� avec suiveur)
	 * 
	 * - SBM1: malus constipation semble rester m�me apr�s la fin du clignotement
	 * 
	 * - GUI : les r�sultats ne s'affichent pas automatiquement quand on revient � l'�cran du match
	 * 
	 * - GUI : �dition de profil, quand on a fait "nouveau" �a n'active pas le bouton "modifier"
	 * 
	 * - cliquer sur partie rapide, annuler, cliquer sur tournoi, annuler, cliquer sur partie rapide >> bug
	 * 	 peut-�tre li� : cliquer sur partie rapide, maison, repartie rapide : rien d'affich�
	 * 
	 * - GUI : dans les tooltips du tableau des r�sultats, il manque les num�ros des manches
	 *   
	 * - l'ombre des blocs de la derni�re ligne est port�e sur les blocs de la premi�re ligne
	 * 
	 * - bug d'affichage dans les notes d'IA, les accents sont affich�s avec la police par d�faut
	 * 
	 * - bug de collision difficlement reproductible : le joueur va vers le haut, il a l'item pass-walls et il se retrouve 
	 *   t�l�port� en dehors du niveau (dans le noir) sur NBM2 custom battlezone.
	 *   	- c'est arriv� aussi sur la zone des marais de NBM2. 
	 *   	- �a semble se produire quand on se rapproche des limites (verticales?) du niveau
	 *   	  et on se retrouve t�l�port� en (0,0)
	 *   	- �a arrive aussi sur le niveau d�bug : quand l'ia suiveuse passe de l'autre cot�, droite>gauche,
	 *   	  le perso se retrouve parfois t�l�port� en 0,0 aussi
	 *   	- pour le reproduire (� tester) : se d�placer horizontalement sur la derni�re ligne du niveau en se faisant suivre par l'IA, 
	 *   	  de droite � gauche, et arriv� au coin bas gauche elle devrait disparaitre (p-� faut-il �tre un peu � cheval avec la 1�re ligne)
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// A FAIRE MOTEUR	//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* TODO PRIORIT� HAUTE
	 * 
	 * - nettoyer tous les tournois, rounds, etc, dans la version clean (et synchro qd n�cessaire la version de travail)
	 * 
	 * - programmer l'ia suiveuse
	 * 
	 * - faut �mettre un �vt de sortie de case � la disparition d'un sprite (mort, masquage, etc)
	 * 
	 * - HIDING devrait �tre un gesture d�finit automatiquement, non ? pas d'image, sensible � rien, seule action autoris�e=apparaitre...
	 * 
	 * - �tudier le fonctionnement de ended (sprite) pr voir quand retirer le sprite de level/tile
	 * 
	 * - g�rer le shrink
	 * 
	 * - remplacer "� vos marques" "pret" "boum!" par des graphismes pr�calcul�s
	 * 
	 * - � la fin de la partie, faire disparaitre tout le niveau comme il apparait au d�but
	 * 
	 * - faire un log automatique energistrant toutes les commandes et positions, histoire d'avoir une trace des bugs
	 * 
	 */
	
	/* TODO PRIORIT� BASSE
	 * 
	 * - NBM2 : 
	 * 		- item veste ignifug�e (souffle de l'explosion pas impl�ment�)
	 * 		  (s'apparente � un push sur un joueur adverse, int�ressant pour les instances � venir)
	 * 
	 * - SBM1 : 
	 * 		- le d�lai de retardement ne doit pas �tre r�initialis� a la suite d'un punch
	 * 		  en fait �a reprend exactement o� �a ne �tait au moment ou la bombe touche le sol
	 * 		- le clignotement d'invincibilit� est sens� ralentir quand le temps est presque termin�
	 * 		- item clock : soit rajouter du temps normalement, soit faire un freeze tr�s court?
	 * 		- spawn : faut d�composer en plusieurs abilities : d�lay, proba de spawn, nbre de spawn, etc
	 * 
	 * - �v�nements :
	 * 		- envoyer un evt de changement de gesture (pour indiquer par ex que le sprite devient destructible, etc ?)
	 * 
	 * - Threads :
	 * 		- pouvoir modifier l'UPS pour les IA (pour all�ger le calcul)
	 * 
	 * - Images :
	 * 		- modifier le loader d'image de mani�re � ce qu'une image non-trouv�e soit remplac�e par la croix rouge
	 * 
	 * - TournamentsMatches/Rounds :
	 * 		- possibilit� de d�finir un nom pour tournament/match/round, 
	 * 		  qui sera affiche en titre de pr�sentation/stats etc. 
	 * 		  si pas de nom, utilisation d'un nom g�n�rique (Round 1 - Prensentation) etc
	 * 		- faire un param�tre dans les rounds qui permet de d�terminer s'ils sont compatibles avec le tournoi 2007-08
	 * 		- besoin d'une m�thode permettant d'exporter un tournoi/match/round, 
	 * 		  ie de l'�crire enti�rement en local (pas de r�f�rence � des composants existants)
	 * 
	 * - Items :
	 * 		- d�finir des noms "human readable" pour les items, histoire de ne pas afficher ces codes internes dans la GUI, en profiter pour introduire une decription, le tout en plusieurs langues. utiliser le code ISO comme param�tre de langue, et l'introduire dans le fichier de langue
	 * 
	 * - Stats : 
	 * 		- nombre de fois qu'un level a �t� jou�
	 * 
	 * - Points : 
	 * 		- calcul : introduire des variables comme le nombre de joueurs 
	 *  	  (pour d�finir un bonus pr le joueur qui fait un perfect en survival)
	 * 		- lors du calcul des points, il faut forcer la pr�sence d'un classement: 
	 * 		  �a facilite �norm�ment de traitements en aval
	 *   	  au pire, si le classement est inutile (ex: simple total), on d�finit un classement-identit� 
	 *   	  (pts utilis�s pr le classement <=> pts marqu�s)
	 *
	 * - Limites/Modes de jeu :
	 * 		- limites exprim�es de fa�on relative (peindre 75% des cases, �liminer la moiti� des joueurs...)
	 * 		- items: 1 item arr�tant la partie, 1 item faisant diminuer le temps restant (anti-temps)
	 * 		- possibilit� de choisir entre le fait que le round s'arr�te d�s que tout le monde est mort 
	 * 		  sauf 1, ou derni�re flamme termin�e
	 * 		- reformater les modes de jeu : pour paint il suffit de d�finir des bombes sp�ciales qui peignent le sol
	 * 	 	- pour painting, possibilit� de d�finir quelles cases peuvent �tre repeintes, 
	 * 		  ce qui permet de poser comme limite un %age de cases repeintes
	 * 
	 * - XML :
	 * 		- voir si on peut mettre � jour le parser XML
	 * 		- d�finir la liste de gestures pour chaque type de sprite
	 *		- le XML des animes doit avoir soit :
	 * 			- NONE seule
	 *  		- primaries seules
	 *  		- primaries+NONE
	 *  		- primaries+COMPOSITES
	 *  		- primaries+NONE+composite
	 * 		- et les gestures d�finis doivent au moins contenir les n�cessaires, et au plus les autoris�s
	 *  	  il va falloir utiliser XSD 1.1 quand �a sera possible, avec les assertions et associations, cf l'email.
	 * 
	 * - Actions/Abilities/Modulations
	 *  	- v�rifier qu'avant d'ex�cuter une action, on v�rifie si le sprite concern� (actor) poss�de bien l'ability (avec modulate)
	 * 		- pour chaque gesture, fixer les actions autoris�es 
	 * 		- �a ne me plait pas beaucoup ces actions bidons pour tester les abilities de certains sprites. faut r�fl�chir � un truc plus propre
	 * 		- il faudrait documenter le comportement par d�faut du moteur, i.e. pour chaque type de sprite:
	 * 			- qu'est-ce qu'il peut faire comme action? quelles sont les transitions? qu'est-ce qui est interdit ?
	 * 			- �a permettra de savoir ce qui peut �tre modul� et ce qui ne peut pas l'�tre
	 * 		- un sprite n'est a priori pas un obstacle, tout est g�r� par modulation (y compris pour le feu)
	 * 		- le coup de l'indestructibilit� des items (le fait de r�apparaitre ailleurs) pourrait �tre �tendue � tous les sprites
	 * 		  (en particulier les joueurs, �a serait un cas sp�cial de r�sistance au feu un peu p�nalisante, utilisable un nombre limit� de fois, par ex !)
	 * 		- �a serait bien que les param�tres num�riques des abilit�s puissent �tre d�finies 
	 * 		  au chargement (force, dur�e, utilisation) de fa�on al�atoire, en fonction de certains param�tres. 
	 * 		  on pourrait par ex utiliser une liste de valeur num�riques : 1=d�terministe, 2=bornes pour un tirage au sort
	 * 		  utile par exemple pour d�terminer le nombre de fois qu'un bloc repousse (plutot que de le fixer dans le fichier XML), 
	 * 		  ou la panne d'une bombe (plutot que de le faire pdt le jeu, et sans utiliser d'ablts sp�ciales)
	 * 
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// A FAIRE GUI		//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* TODO
	 * 
	 * - GUI : dans les tables, remplacer les labels par une classe custom qui impl�menterait l'interface basicPanel
	 * 
	 * - profils: 
	 * 		- simplifier, pas besoin de la double repr�sentation default+selected.
	 * 		- r�organiser par rapport aux besoins: joueur pdt le jeu, joueur charg� en dehors du jeu, joueur pas charg� ?
	 * 
	 * - faire le classement lexicographique g�rant les signes diacritiques partout o� c'est n�cessaire
	 * 
	 * - utiliser les tooltips pour afficher les infos trop longues : calcul de points, nombre � virgule dans la colonne des points (d�cimales cach�es), etc.
	 * 
	 * - pb de dimension de l'image de fond en fonction de la r�solution... (zones pas peintes)
	 * 
	 * - results panel : 
	 * 		- faut afficher explicitement le classement � la fin d'une confrontation
	 * 		- quand il y a trop de rounds dans un match pour que �a rentre � l'�cran, ne pas tout afficher
	 * 		- �a serait bien que les joueurs soient affich�s dans l'ordre relatif aux points de la limite rencontr�e
	 *   	  voire on d�finit explicitement un ordre d'affichage dans la comp�tition
	 * 		- dans les r�sultats :
	 * 			- afficher par d�faut les 4 scores de base
	 * 			- plus les scores utilis�s dans les points et/ou les limites
	 * 			- si les limites utilisent des points custom, les afficher aussi
	 * 		- � la fin du round, faire apparaitre les r�sultats par transparence
	 * 
	 * - tournoi : 1) on choisit les param�tres 2) on choisit les joueurs, le jeu restreint leur nombre pr qu'il soit compatible avec le tournoi, et restreint aussi les IA pour les m�mes raisons
	 * 
	 * - champ en plus 
	 * 		- dans les profils : le classement du joueur, nbre de rouds gagn�s/nbre de rounds jou�s
	 * 		- dans les persos : % de rounds gagn�s, ou bien nbre de rounds jou�s
	 * 		- tout �a est fait simplement en rajoutant les informations ad�quates dans les classes de stat
	 * 
	 * - lors de la s�lection des commandes :
	 * 		- cliquer sur le bouton d'action fait r�agir quelque chose dans la ligne du joueur correspondant
	 * 		- permet de v�rifier qu'on a pris les bonnes commandes (celles qu'on pensait avoir prises)
	 * 
	 * - il faudrait s�parer les joueurs IA et les joueurs humain dans leur gestion.
	 * 		ca permettrait de s�lectionner directement l'IA, au lieu du joueur, et donc de ne pas avoir � cr�er plusieurs
	 * 		joueurs avec la m�me IA quand on veut jouer contre plusieurs versions de la m�me IA.
	 * 		voire limiter le nombre de joueurs pour une IA � 1 seul, mais s�lectionnable plusieurs fois ?
	 *		 � voir...
	 * 
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// A FAIRE SITE		//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * - possibilit� de d�finir des items indestructibles (sont juste d�plac�s)
	 * - blocs laissant passer le feu mais pas les joueurs, comme dans le niveau story/custom de SBM1
	 * - parler des nouveaux outils de dev de l'IA : API am�lior�e, visualisation des couleurs, texte, et chemin
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//EN COURS			//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * - tester la fonction de speed (hero)
	 * - tester la fonction traversable (tile)
	 * - tester la fonction update abilities (hero)
	 * 
	 * - quand l'ia meurt et revient grace � une vie, elle n'a plus le control du personnage
	 * - bug : j'ai vu l'ia prendre une vie, mourir, relacher la vie, et revenir. 
	 * 	 et la vie �tait foncionnelle : je l'ai prise, je suis mort, je suis revenu (sans la relacher cette fois)
	 * - optimisation de l'API IA:
	 * 		- les IA ont-elles vraiment besoin d'�tre raffraichies si souvent ? >> non! r�duire le taux de raffraichissement
	 * 
	 * - tester si les chemins de l'ia traversent le feu quand elle est invuln�rable
	 * - tester si les chemins de l'ia traversent les murs quand elle peut passer � travers
	 * - changement incessant de chemin target alors qu'il n'y a pas d'obstacles
	 * - bug : l'IA essaie parfois de passer � travers les murs (pb dans AiBlock ou AiTile) 
	 */
}
