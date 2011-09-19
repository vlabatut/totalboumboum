package org.totalboumboum;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.data.configuration.misc.MiscConfiguration;
import org.totalboumboum.gui.frames.NormalFrame;
import org.totalboumboum.gui.frames.QuickFrame;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Launcher
{	
	public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// set thread name
		Thread thread = Thread.currentThread();
		thread.setName("TBB.init");
		
		List<String> argList = Arrays.asList(args); 

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
		// load engine stats
		updateSplash(splash,GuiTools.STARTUP_MESSAGES[GuiTools.STARTUP_STATS]);
		GameStatistics.loadStatistics();
		// startup finished
		updateSplash(splash,GuiTools.STARTUP_MESSAGES[GuiTools.STARTUP_DONE]);
		GameData.quickMode = false;
		
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
		// load engine stats
		System.out.println(GuiTools.STARTUP_MESSAGES[GuiTools.STARTUP_STATS]);
		if(Configuration.getStatisticsConfiguration().getIncludeQuickStarts())
			GameStatistics.loadStatistics();
		// done
		System.out.println(GuiTools.STARTUP_MESSAGES[GuiTools.STARTUP_DONE]);
		GameData.quickMode = true;
		
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
	 * 	- écriture courte du for pour iterator
	 */

	/*
	 * TODO règle pour les loaders :
	 * 	- l'objet à charger est créé dans la première méthode appelée lors du chargement
	 *  - les méthode secondaires reçoivent l'objet créé, et le complètent.
	 *  - quand des objets secondaires sont chargés ind�pendamment, ie dans des méthodes publiques, ils sont eux aussi créés dans la méthode appelée
	 */

	/* TODO mettre tout ce qui appelle du swing dans le thread adapté
 		SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	initGui();	
			}
		});
	 */

	/* TODO quand une classe est modifiée ou créée :
	 * il faut impl�menter la fonction finish qui permet d'effacer toutes les références qu'elle utilisait
	 */
	
	/*
	 * TODO GUI
	 * Split : reçoit les paramètres mais ne les met pas en cache
	 * Menu  : met les originaux en cache, envoie une copie au data
	 * Data  : modifie les paramètres re�us. le menu se charge de les r�cup�rer, les comparer et éventuellement les sauver
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ARTIFICIAL INTELLIGENCE / TOURNAMENT	//////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * TODO dans l'API IA il faut généraliser (tout en simplifiant) les propriétés des sprites, plutot que 
	 * de définir des fonctions spécifiques à chaque fois.
	 * ex : accéder aux propriétés via une fonction générique prenant le nom de la propriété en paramètre
	 */
	
	/*
	 * TODO type de tournoi plus adapté au réseau : hot potatoe
	 * des joueurs jouent un match pendant que d'autres attendent 
	 * à la fin du match, les n derniers joueurs sont relégués dans la salle d'attente
	 * n joueurs qui attendaient sont qualifiés.
	 * un classement permet de déterminer le leader provisoire
	 */
	
	/*
	 * TODO include dans l'API une fonction d'initialisation de l'IA, appelée lors de sa création
	 * faudrait passer en paramètre le niveau et des infos style l'instance, etc.
	 * ça permettrait par exemple, pour une IA qui apprend, de télécharger son fichier de sauvegarde
	 * (dans son package)
	 */
	
	/*
	 * TODO evaluation du projet : pour la qualification, utiliser 
	 * le temps nécessaire pour �liminer un/des adversaire(s) de référence
	 */
	
	/*
	 * TODO
	 * plutot que de développer toute une API pour l'IA, il faudrait r�utiliser les classes du jeu:
	 * 	- faire une copie des donn�es à chaque itération, pour des raisons de synchro
	 *  - on suppose qu'une copie n'est pas modi�e, donc on peut ne la calculer qu'une seule fois pour chaque IA
	 *  - il faut définir des classes d'interface, des wrappers même, permettant aux IA d'accéder seulement aux méthodes autoris�es
	 *  - pour les pb de synchro quand on veut savoir les propriétés du feu qu'une bombe g�n�rerait, il suffit d'en construire un (feu) 
	 *    et de le garder en cache le temps de la partie (en le m�j si besoin de temps en temps)
	 *  >> intérêt : pouvoir utiliser exactement les mêmes fonctions que dans le jeu
	 *  (en fait, ça consiste à faire plus ou moins ce qui est déjà fait, sauf qu'au lieu de recopier les donn�es dans des
	 *  classes différentes, on les copies telles-quelles et on définit des classes wrappers
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// MULTITHREADING	//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO il ne faut pas ex�cuter toutes les IA dans un thread pool, mais au contraire les s�parer
	 * sinon, une seule IA bouclant à l'infini va consommer tous les threads dispo dans le treadpool
	 * vont être successivement monopolis�s par l'IA
	 */
	
	/*
	 * TODO une fois qu'on a détermin� les nombres de joueurs, y a moyen de g�rer les threads de meilleure mani�re en :
	 * 		- créant un executor au niveau du tournoi
	 * 		- il doit contenir un pool de (nbre d'IA max pvant jouer à la fois)+1(pr loop)
	 * 		- par la suite, au lieu de créer un thread pour chaque ia ou pour le chargement/loop, on en demande un à l'executor
	 * http://java.sun.com/javase/6/docs/api/java/util/concurrent/ExecutorService.html
	 * http://java.sun.com/docs/books/tutorial/essential/concurrency/pools.html
	 * http://java.sun.com/docs/books/tutorial/essential/concurrency/interrupt.html
	 * >>> pb: si un thread entre en boucle infinie, il ne sera jamais termin�, donc l'executor devra créer/g�rer un thread de plus
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// BOMBS		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO autoriser de poser une dernière bombe avant de mourir ? (i.e. dans le gesture burning) comme ds XBlast
	 */						
	
	/*
	 * TODO dans l'eventMgr de la bombe, quand elle oscille, il faut g�rer les sprites
	 * qui sont en train de la pousser, et non pas en simple contact.
	 * de plus, il faut que les sprites aient le pouvoir de pousser, sinon ils ne comptent pas.
	 */
	
	/*
	 * TODO
	 * quand des sprites de plusieurs cases seront définis, peut-�tre que les explosions
	 * devront être définies en tant que sprites (param�trables pour leur taille) de ce type ?
	 * ce qui permettrait de les faire se d�placer.
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// CONTROLS		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO lors de l'impl�mentation du jeu en réseau,
	 * une optimisation consiste à ne pas g�n�rer des �vts par le controleur tant
	 * qu'une touche est appuy�e, car ça va faire bcp de choses à transmettre,
	 * le controleur �tant situ� cot� client.
	 * plutot, il faut les g�n�rer dans le controlManager, qui est cot� serveur.
	 * et du cp aussi, il faut �viter d'utiliser ce mode de gestion des �v�nements
	 * pour les autres types d'evts (ie non-controles)
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// EXPLOSION/FIRE	//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	/*
	 * TODO à faire sur le feu (long terme) :
	 * - décomposer l'explosion en :
	 * 		1)explode
	 *		2)stay still
	 *		3)implode
	 * 	pr g�rer le déplacement des flammes à la XBlast
	 * 
	 * ou alternativement :
	 * - décomposer le feu en appearing+standing+disapearing, 
	 * 	ce qui permettrait de le faire durer aussi longtemps qu'on veut
	 */

	/*
	 * TODO explosion:
	 * lorsque la flamme est construite, on teste si chaque case la laisse passer
	 * mais il faut aussi le faire sur la case centrale (origine de l'explosion)
	 * car différents facteurs peuvent limiter l'explosion à une port�e de 0
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
	 * 	- les esp�ces de chars d'assaut prèsent dans certains niveaux (utilisés par les cr�atures)
	 */
	
	/*
	 * TODO définir le shrink comme ça :
	 * - séquence de steps, chacun caract�ris� par :
	 * - un d�lai à laisser �couler apr�s le step pr�c�dent
	 * - la liste des lignes/tiles qui doivent appara�tre, avec le type de bloc devant appara�tre
	 */
	
	/*
	 * TODO pour le d�bug de certains points, il faudra mettre en place un controleur
	 * capable d'enregistrer une séquence de commandes, ce qui permettra de les rejouer
	 * par la suite (style playback) tout en controlant en temps réel un autre perso
	 */
	
	/*
	 * TODO animes/trajectoire : permettre de définir des labels dans la séquence
	 * et de faire des goto et des boucles (?)
	 */
	
	/*
	 * TODO dans l'avenir il serait p-� nécessaire d'utiliser un actionManager,
	 * qui recevrait une action en paramètre, et l'ex�cuterait.
	 * int�r�t : décomposition + utile pour un moteur qui serait seulement un player,
	 * et ne devrait donc pas g�rer les tirages al�atoires, mais seulement l'application
	 * déterministe des actions (le tirage se ferait dans la fonction appelant l'actionMger,
	 * ie généralement : l'eventMgr)
	 */
	
	/*
	 * TODO
	 * il faut mieux g�rer l'acc�s aux méthodes, utiliser plus protected.
	 * surtout pour prot�ger les classes qui devraient être inaccessibles depuis l'IA
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// MULTIPLAYER		//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * TODO
	 * y a un mode de tournoi plus adapté pour le multijoueur, défini chaiplus trop où (mails?)
	 * avec une esp�ce de tournante : n joueurs, matches de k joueurs, les n-k derniers sont remplacés
	 * par ceux qui attendent
	 */
	
	/*
	 * TODO
	 * - pour faciliter la communication lors des parties réseaux :
	 *   syst�mes de bulles pour communiquer. le joueur affecte un message
	 *   à une touche, et quand il appuie dessus, une bulle apparait à cété
	 *   de son joueur pour signifier ce message aux autres joueurs (vraisemblablement
	 *   distants)
	 * - p-� que pour rendre ça plus facile à utiliser, faudrait pr�définir
	 *   un certain nombre de messages et permettre au joueur d'en définir certains custom
	 * - donner aussi la possibilit� de cibler des joueurs en particulier
	 * - exemple d'utilisation :
	 * 		- appuyer sur la touche du joueur cibl�
	 * 		- appuyer sur la touche de message
	 * 		- appuyer sur la touche du joueur mentionn� dans le message
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ITEMS		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * TODO
	 * possibilit� de faire varier le calcul des points
	 * en fonction de la position de départ des joueurs : 
	 * certaines positions sont plus difficiles que d'autres 
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ITEMS		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO g�rer le déplacement des items lors d'une explosion
	 */
	
	/*
	 * TODO
	 * item sp�cial permettant de manger les bombes des autres, comme certains ennemis du jeu original en mode histoire
	 * item sp�cial qui, quand un h�ros meurt, d�clence automatiquement une explosion, éventuellement avec un effet de zone  
	 */
	
	/*
	 *  TODO malus pour les bombes :
	 *  port�e al�atoire : on sait pas à quelle distance ça va p�ter 
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// XML			//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/* 
	 * TODO à faire dans les XML : 
	 * 		- faire respecter au maximum les contraintes avec les sch�mas
	 * 		- ensuite seulement faire respecter les contraintes dans les loaders
	 * 		- dans les loaders, penser à g�rer les valeurs/structures par défaut
	 * exemples :
	 * 		- pour les animes: les noms des gestures doivent être différents entre eux et différents de default
	 * 		- pour les th�mes : les noms des components doivent être différents
	 */	

    /* 
     * TODO pour forcer l'unicit� d'un attribut/�l�ment, voir ici : 
     * http://rangiroa.essi.fr/cours/internet/02-slides-xml/schema.htm
     */		

	// TODO dans schema de Level : contrainte d'unicit� sur les positions des lignes et colonnes    

	/*
	 * TODO dans le fichier de trajectoire, tester que la valeur de dur�e de for�age 
	 * de position n'est pas sup�rieure à la dur�e de la trajectoire
	 * (et il y a surement de nombreux autres test de coh�rence à effectuer)
	 */					

	/*
	 * TODO à faire dans le chargement des fichiers XML
	 * ANIME : 
	 * - v�rifier que si on utilise une ombre, elle a bien été d�clar�e dans le fichier XML
	 * - v�rifier que le mouvement par défaut existe bel et bien
	 * LEVEL :
	 * - matrice vide = que des floors
	 * - v�rifier que quand on fait référence à un bloc par son nom, il existe bien dans le fichier du th�me
	 */

	/*
	 * TODO lorsqu'on définit une animation ou une trajectoire qui est identique à une autre
	 * plutot que de faire un copier-coller, permettre de nommer l'animation d'origine.
	 * bien sur, il faudra v�rifier que cette animation existe, comme pour celle par défaut. 
	 */
	
	/*
	 * TODO dans les fichiers XML, il faut pr�ciser en attribut de la racine la version
	 * du fichier utilisée (i.e. version du sch�ma).
	 */
	
	/*
	 * TODO il faut reprendre les fichiers XML en centralisant au maximum les types
	 * par exemple y en a plein c des références vers des fichiers (attributs file et folder)
	 * ça permettrait de centraliser le code �galement dans le XMLTools
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
	 * dans les modulations third et other, la port�e est limit�e à la case (ou aux cases). 
	 * le contact et surtout le remote ne sont pas encore g�r�s.
	 * id�e pour centraliser le traitement en cas de port�e sans limite:
	 * 	- d�s qu'un sprite change de gesture, ses nouvelles modulations sont analys�es
	 * 	- toutes celles qui sont sans limite de port�e sont stock�es dans un vecteur situ� dans Level 
	 * 		(et toutes celles de l'�tat pr�c�dent sont retir�es de ce même vecteur)
	 * 	- lors de la validation de 3rdMod, ce vecteur est syst�matiquement test� 
	 * 		en plus des sprites situ�s près de l'acteur et de la cible 
	 */
	
	/*
	 * NOTE les modulations sont ordonnées par priorité dans le fichier XML.
	 * dans le cas où plusieurs modulations peuvent être appliqu�es à une action, 
	 * c'est la première définie dans le fichier XML qui est utilisée.
	 * il faut donc l'organiser du plus spécifique au plus général.
	*/

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// THEMES/LEVELS	//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * TODO rajouter des niveaux in�dits en utilisant au maximum les parties inutilisées
	 * des autres th�mes
	 */
	
	/*
	 * TODO dans le level, il faut g�rer la distribution des items :
	 * possibiliter de la paramètrer en fonction du nombre de joueurs ?
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ANIMES	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	/*
	 * TODO lorsque l'anime attendue est cens�e s'arr�ter (pas de répétition)
	 * et que le moteur compte sur un evt de fin pour passer à l'anime suivante,
	 * il y a blocage si l'anime n'est pas définie, et que l'anime par défaut est
	 * répét�e.
	 * exemple : défaut=marcher, et il manque punch -> le bonhomme ne sort jamais de l'�tat punching
	 * 	>>solution : ne jamais compter sur la fin de l'anime, toujours imposer une dur�e à respecter
	 * 	(ce qui permet d'uniformiser le beans pour tous les joueurs)
	 */
	
	/*
	 * TODO il faut s�curiser les animations : s'assurer qu'une anime cens�e
	 * ne pas être r�-init est bien gaul�e. par exemple, pushing doit être gaul�e
	 * comme walking (dans le XML) sinon p-� pb (si pas même dur�e ou autres diff)
	 * > à v�rif
	 */
	
	/*
	 * TODO pour Prognathe, faire une anime de d�but de partie : quand le sprite apparait
	 * id�e : sortir du sol
	 * id�e : le corps est déjà en place, tient la t�te en l'air et la fixe sur ses �paules comme un pilote enfile son casque
	 * 
	 */
	
	/*
	 * TODO animes : permettre de définir plusieurs animes pour 1 gesture,
	 * avec choix al�atoire
	 *  
	 */
	
	/*
	 * TODO animes : il faut définir un syst�me de substitution d'image
	 * de mani�re à ce qu'il soit impossible d'�tre pris en défaut
	 */
	
	/*
	 * TODO introduire une anime/action supplémentaire : en jeu, un joueur
	 * peut provoquer un autre joueur en d�clenchant une animation spécifique.
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
	 * de déplacement (puisque l'ablt à bloquer peut être configur�e suivant la 
	 * direction). donc dans la gestion des collisions sur directions compos�es,
	 * il faut retester avec une requ�te si le fait de retirer une direction provoque
	 * toujours une collision ou pas.
	 */
	
	/*
	 * TODO certaines ablt sont li�es à la trajectoire, par ex : puissance de jump
	 * influe sur la distance du saut.
	 * il faut donc en tenir comtpe dans la trajectoire, en proposant l'utilisation
	 * d'un paramètre externe (au fichier XML)
	 */
	
	/*
	 * TODO quand on calcule l'ablt composite, pour les directions composites :
	 * l'abilit� renvoy�e peut contenir une direction simplifi�e, en fonction 
	 * des autorisations obtenues. 
	 */
	
	/*
	 * TODO pour bien faire, il faudrait lister pour chaque sprite (et chaque configuration)
	 * les actions et les états possibles. les permissions représentent les autorisations
	 * pour appliquer une action. il manque la définition des actions, indiquant dans quel
	 * état un objet se retrouve apr�s avoir effectué/subi/empech� une action donn�e (ce qui 
	 * d�pend éventuellement de l'�tat pr�c�dent l'action). 
	 */
	
	/*
	 * TODO le syst�me de gestion des actions est clairement à am�liorer.
	 * certaines actions comme gather sont automatiques. certaines actions ont un effet de zone 
	 * (pr gather : la case).
	 */
	
	/*
	 * TODO chargement des niveaux
	 * 	- commencer une partie rapide avec un seul niveau
	 * 	- jouer un round
	 * 	- changer les items par défaut pour ce niveau, directement dans le fichier XML
	 * 	- joueur le round suivant: 
	 * 		- la gui affiche la modif
	 *		- mais la hollow level n'a pas été rechargé (mutualisation)
	 *		- et donc la modif n'est pas prise en compte dans l'initialisation des joueurs 
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// STATS				//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * TODO
	 * définir des stats de jeu plus compl�tes :
	 * 		- niveaux les plus utilisés
	 * 		- voire d�tail de tout par niveau
	 * 		- stats temporelles
	 * 		- analyse du jeu du joueur en fonction du temps, performance par niveau
	 * 
	 */
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// IDENTIFICATION		//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO
	 * 
	 * PB: est-ce qu'on autorise la cr�ation d'hote/joueur ind�pendemment du central, ou pas.
	 * >> OUI,mais pas de mdp
	 * 
	 * HOTES
	 * - chaque h�te est identifi� par un UUID (http://www.javapractices.com/topic/TopicAction.do?Id=56)
	 *   qui n'est jamais modifié apr�s
	 * - de plus, l'ensemble de toutes ses adresses MAC sont enregistr�es
	 *   (http://www.kodejava.org/examples/250.html 
	 *    http://www.stratos.me/2008/07/find-mac-address-using-java
	 *    http://www.kodejava.org/examples/250.html)
	 * - cette liste est mise à jour à chaque d�marrage
	 * - si d'un d�marrage à l'autre il n'y a aucune adresse commune, ça veut dire que ce n'est plus la même machine
	 * - on g�n�re alors un nouvel UUID qui �crase le pr�c�dent
	 * - les mots de passe de tous les profils sont r�initialis�s
	 * 
	 * JOUEURS
	 * - chaque joueur est identifi� par l'identifiant de l'hote sur lequel il est créé
	 *   plus un identifiant unique pour cet h�te (on peut prendre un autre UUID)
	 * - ce num�ro ne change jamais, même quand il joue sur un autre hote (ce qui est possible)
	 * - on a un truc du style : joueur@createur
	 * - quand le profil est utilisé sur son hote de cr�ation, pas besoin de mot de passe
	 * - sinon il faut un mot de passe, qu'on a la possibilit� d'enregistrer
	 * >> en fait pas besoin de voir l'hote, avec l'uDDi ça suffit
	 *    chaque joueur doit entrer le mdp quel que soit l'hote (même cr�ateur)
	 *    si le joueur n'est pas enregistr� sur le site, il ne peut pas être prot�g� par un mot de passe
	 *    et de toute fa�on ses stats ne comptent pas
	 * >> int�r�t d'identifier de fa�on unique l'hote ??
	 *    ah oui, pr les stats sur qui joue chez qui...
	 *    
	 * NOTE empecher le même joueur de joueur deux matches en même temps...
	 * >> que les matches via central enregistr�s dans les stats
	 */
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// KNOWN, WAITING BUGS	//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	/*
	 * TODO
	 *	l'ombre des blocs de la dernière ligne est port�e sur les blocs de la première ligne
	 *	c'est normal. pour l'�viter, il faudrait traiter un cas assez particulier... à voir
	 *	>> tentative de r�solution mise en place, à voir
	 */
	
	/*  
	 *  TODO l'item kick ne marche pas
	 */
	
	/*
	 * TODO
	 * quand on pose deux bombes en diagonale et qu'on se place dans le cadrant int�rieur d'une des cases libres du même carr�
	 * on est bloqu�. ce n'est pas vraiment un pb en fait, plus un feature :P . mais les non-initi�s peuvent prendre ça pour un bug.
	 * (note : point mentionn� dans le blog)
	 */
	
	/* 
	 * TODO
	 * impossible de poser une bombe quand on est en train de buter contre un mur en direction upleft (et uniquement cette direction pr NES2) et downleft (pr SBM1)
	 * 		>> ca viendrait de swing ou du clavier (pb mat�riel ou bas niveau)
	 */
	
	/* 
	 * TODO
	 * cas particulier : item apparaissant dans une explosion de bloc, avec un joueur déjà sur le bloc 
	 * (il a passe-muraille et r�sistance au feu) : l'item doit être ramass� d�s qu'il a fini d'apparaitre, en th�orie
	 * ou bien : un h�ros meurt, un item apparait sous les pieds d'un autre joueur immobile : l'item reste sans que le joueur ne se l'approprie
	 * >> en même temps, ça laisse le choix au joueur de ramasser l'item ou pas, c'est pas mal finalement (si c'est un malus par ex.)
	 */
	
	/*
	 * TODO
	 * bug d'affichage dans les notes d'IA, les accents sont affich�s avec la police par défaut
	 * >> pas trouv� la cause de ce beans... (et c'est pas faut d'avoir cherch� !)
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
// CHANGELOG	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* TODO
	 * + beta.021
	 * - Moteur :
	 * - GUI :
	 * - IA :
	 * - Stats :
	 * - Ressources :
	 * 		- th�mes SBM1 arena, circles, hal, power, speed & warp
	 * 		- tous les niveaux des modes histoire et ar�ne
	 * - Divers :
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// BUGS				//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* TODO
	 * 
	 * - bug : j'ai vu l'ia prendre une vie, mourir, relacher la vie, et revenir. 
	 * 	 et la vie �tait fonctionnelle : je l'ai prise, je suis mort, je suis revenu (sans la relacher cette fois)
	 *   >> pas r�ussi à reproduire �a...
	 * 
	 * - la pause d'IA reste même si le joueur est mort, et je ne sais pas pq : au d�bug le moteur n'ex�cute pas le code affichant le texte !
	 * 
	 * - quand on balance une bombe, puis une autre, puis fait p�ter la première et enfin la seconde 
	 *   juste à la fin des flammes de la première, alors l'explosion de la 2nde est circonsrite à la
	 *   case où elle atterrit.
	 *   
	 * - pb de r�initialisation : 
	 * 		- jouer un tournoi single (par ex supercup) en entier
	 * 		- jouer un tournoi de type cup en suivant : il y a un pb sur le MatchPanel, qui semble n'avoir pas été r�init à null
	 * 
	 *  - apparemment, y a un pb avec les GeneralAction, dont certaines définies dans les fichiers XML ne sont pas compatibles 
	 *    avec la classe (en termes de circonstances) mais qui sont quand même chargées et initialis�es normalement
	 *  
	 *  - il semblerait que les directions dans la SpecificAction et dans la GeneralAction correspondante ne soient pas les mêmes...
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// A FAIRE MOTEUR	//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* TODO priorité HAUTE
	 * 
	 * - faut �mettre un �vt de sortie de case à la disparition d'un sprite (mort, masquage, etc)
	 * 
	 * - �tudier le fonctionnement de ended (sprite) pr voir quand retirer le sprite de level/tile
	 * 
	 * - g�rer le shrink
	 * 
	 * - remplacer "� vos marques" "pret" "boum!" par des graphismes pr�calculés
	 * 
	 * - à la fin de la partie, faire disparaitre tout le niveau comme il apparait au d�but
	 * 
	 */
	
	/* TODO priorité BASSE
	 * 
	 * - NBM2 : 
	 * 		- item veste ignifug�e (souffle de l'explosion pas impl�ment�)
	 * 		  (s'apparente à un push sur un joueur adverse, int�ressant pour les instances à venir)
	 * 
	 * - SBM1 : 
	 * 		- le d�lai de retardement ne doit pas être r�initialis� a la suite d'un punch
	 * 		  en fait ça reprend exactement où ça ne �tait au moment ou la bombe touche le sol
	 * 		- le clignotement d'invincibilit� est sens� ralentir quand le temps est presque termin�
	 * 		- item clock : soit rajouter du temps normalement, soit faire un freeze tr�s court?
	 * 		- spawn : faut décomposer en plusieurs abilities : d�lay, proba de spawn, nbre de spawn, etc
	 * 
	 * - �v�nements :
	 * 		- envoyer un evt de changement de gesture (pour indiquer par ex que le sprite devient destructible, etc ?)
	 * 
	 * - Threads :
	 * 		- pouvoir modifier l'UPS pour les IA (pour all�ger le calcul)
	 * 
	 * - Images :
	 * 		- modifier le loader d'image de mani�re à ce qu'une image non-trouv�e soit remplacée par la croix rouge
	 * 
	 * - TournamentsMatches/Rounds :
	 * 		- possibilit� de définir un nom pour tournament/match/round, 
	 * 		  qui sera affiche en titre de prèsentation/stats etc. 
	 * 		  si pas de nom, utilisation d'un nom générique (Round 1 - Prensentation) etc
	 * 		- besoin d'une méthode permettant d'exporter un tournoi/match/round, 
	 * 		  ie de l'écrire enti�rement en local (pas de référence à des composants existants)
	 * 		- dans les fichiers xml, pour les points, plutot que local (vrai ou faux), utiliser round/match/tournament/game (permet de mieux mutualiser les fichiers)
	 * 		- tournoi coupe : 
	 * 			- pouvoir se qualifier depuis n'importe quel leg, pas forcement le pr�c�dent direct (n�cessit� de pr�ciser le num�ro du leg en plus du part, dans le doc XML et la class CupPlayer)
	 * 			- y compris depuis le leg initial (pr�voir une sous-classe pour distinguer initial des autres?)
	 * 
	 * - Items :
	 * 		- définir des noms "human readable" pour les items, histoire de ne pas afficher ces codes internes dans la GUI, en profiter pour introduire une decription, le tout en plusieurs langues. utiliser le code ISO comme paramètre de langue, et l'introduire dans le fichier de langue
	 * 
	 * - Stats : 
	 * 		- nombre de fois qu'un level a été jou�
	 * 
	 * - Points : 
	 * 		- calcul : introduire des variables comme le nombre de joueurs 
	 *  	  (pour définir un bonus pr le joueur qui fait un perfect en survival)
	 * 		- lors du calcul des points, il faut forcer la prèsence d'un classement: 
	 * 		  ça facilite �normêment de traitements en aval
	 *   	  au pire, si le classement est inutile (ex: simple total), on définit un classement-identit� 
	 *   	  (pts utilisés pr le classement <=> pts marqu�s)
	 *
	 * - Limites/Modes de jeu :
	 * 		- limites exprimées de fa�on relative (peindre 75% des cases, �liminer la moiti� des joueurs...)
	 * 		- items: 1 item arr�tant la partie, 1 item faisant diminuer le temps restant (anti-temps)
	 * 		- possibilit� de choisir entre le fait que le round s'arr�te d�s que tout le monde est mort 
	 * 		  sauf 1, ou dernière flamme termin�e
	 * 		- reformater les modes de jeu : pour paint il suffit de définir des bombes sp�ciales qui peignent le sol
	 * 	 	- pour painting, possibilit� de définir quelles cases peuvent être repeintes, 
	 * 		  ce qui permet de poser comme limite un %age de cases repeintes
	 * 
	 * - XML :
	 * 		- voir si on peut mettre à jour le parser XML
	 * 		- définir la liste de gestures pour chaque type de sprite
	 *		- le XML des animes doit avoir soit :
	 * 			- NONE seule
	 *  		- primaries seules
	 *  		- primaries+NONE
	 *  		- primaries+COMPOSITES
	 *  		- primaries+NONE+composite
	 * 		- et les gestures définis doivent au moins contenir les nécessaires, et au plus les autoris�s
	 *  	  il va falloir utiliser XSD 1.1 quand ça sera possible, avec les assertions et associations, cf l'email.
	 * 
	 * - Actions/Abilities/Modulations
	 * 		- HIDING devrait être un gesture définit automatiquement, non ? pas d'image, sensible à rien, seule action autoris�e=apparaitre...
	 *  	- v�rifier qu'avant d'ex�cuter une action, on v�rifie si le sprite concern� (actor) poss�de bien l'ability (avec modulate)
	 * 		- pour chaque gesture, fixer les actions autoris�es 
	 * 		- ça ne me plait pas beaucoup ces actions bidons pour tester les abilities de certains sprites. faut r�fl�chir à un truc plus propre
	 * 		- il faudrait documenter le comportement par défaut du moteur, i.e. pour chaque type de sprite:
	 * 			- qu'est-ce qu'il peut faire comme action? quelles sont les transitions? qu'est-ce qui est interdit ?
	 * 			- ça permettra de savoir ce qui peut être modul� et ce qui ne peut pas l'�tre
	 * 		- un sprite n'est a priori pas un obstacle, tout est g�r� par modulation (y compris pour le feu)
	 * 		- le coup de l'indestructibilit� des items (le fait de r�apparaitre ailleurs) pourrait être �tendue à tous les sprites
	 * 		  (en particulier les joueurs, ça serait un cas sp�cial de r�sistance au feu un peu p�nalisante, utilisable un nombre limit� de fois, par ex !)
	 * 		- ça serait bien que les paramètres num�riques des abilit�s puissent être définies 
	 * 		  au chargement (force, dur�e, utilisation) de fa�on al�atoire, en fonction de certains paramètres. 
	 * 		  on pourrait par ex utiliser une liste de valeur num�riques : 1=déterministe, 2=bornes pour un tirage au sort
	 * 		  utile par exemple pour déterminer le nombre de fois qu'un bloc repousse (plutot que de le fixer dans le fichier XML), 
	 * 		  ou la panne d'une bombe (plutot que de le faire pdt le jeu, et sans utiliser d'ablts sp�ciales)
	 * 
	 * - Niveaux
	 * 		- outil pour d�couper une image (background) en nxm floors
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// A FAIRE GUI		//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* TODO
	 * 
	 * - rajouter un bouton dans les options avanc�es pour vider manuellement le cache m�moire
	 * 
	 * - rajouter une confirmation (popup) quand on sort définitivement d'une partie (icone maison)
	 * 
	 * - 1�re ex�cution
	 * 		- définir un popup d'informations
	 * 		- guider l'utilisateur pas à pas pour créer son profile et commencer sa partie
	 * 
	 * - pour les tournois, le chargement/s�lection du tournoi doit pr�c�der le choix des joueurs
	 * 
	 * - GUI : dans les tables, remplacer les labels par une classe custom qui impl�menterait l'interface basicPanel
	 * 
	 * - profils: 
	 * 		- simplifier, pas besoin de la double représentation default+selected.
	 * 		- r�organiser par rapport aux besoins: joueur pdt le jeu, joueur chargé en dehors du jeu, joueur pas chargé ?
	 * 
	 * - faire le classement lexicographique g�rant les signes diacritiques partout où c'est nécessaire
	 * 
	 * - utiliser les tooltips pour afficher les infos trop longues : calcul de points, nombre à virgule dans la colonne des points (d�cimales cach�es), etc.
	 * 
	 * - pb de dimension de l'image de fond en fonction de la r�solution... (zones pas peintes)
	 * 
	 * - results panel : 
	 * 		- faut afficher explicitement le classement à la fin d'une confrontation
	 * 		- quand il y a trop de rounds dans un match pour que ça rentre à l'�cran, ne pas tout afficher
	 * 		- ça serait bien que les joueurs soient affich�s dans l'ordre relatif aux points de la limite rencontr�e
	 *   	  voire on définit explicitement un ordre d'affichage dans la comp�tition
	 * 		- dans les r�sultats :
	 * 			- afficher par défaut les 4 scores de base
	 * 			- plus les scores utilisés dans les points et/ou les limites
	 * 			- si les limites utilisent des points custom, les afficher aussi
	 * 		- à la fin du round, faire apparaitre les r�sultats par transparence
	 * 
	 * - tournoi : 
	 * 		1) on choisit les paramètres 
	 * 		2) on choisit les joueurs, le jeu restreint leur nombre pr qu'il soit compatible avec le tournoi, 
	 * 		   et restreint aussi les IA pour les mêmes raisons
	 * 
	 * - champ en plus 
	 * 		- dans les profils : le classement du joueur, nbre de rouds gagn�s/nbre de rounds jou�s
	 * 		- dans les persos : % de rounds gagn�s, ou bien nbre de rounds jou�s
	 * 		- tout ça est fait simplement en rajoutant les informations ad�quates dans les classes de stat
	 * 
	 * - lors de la s�lection des commandes :
	 * 		- cliquer sur le bouton d'action fait r�agir quelque chose dans la ligne du joueur correspondant
	 * 		- permet de v�rifier qu'on a pris les bonnes commandes (celles qu'on pensait avoir prises)
	 * 
	 * - il faudrait s�parer les joueurs IA et les joueurs humain dans leur gestion.
	 * 		ca permettrait de s�lectionner directement l'IA, au lieu du joueur, et donc de ne pas avoir à créer plusieurs
	 * 		joueurs avec la même IA quand on veut jouer contre plusieurs versions de la même IA.
	 * 		voire limiter le nombre de joueurs pour une IA à 1 seul, mais s�lectionnable plusieurs fois ?
	 *		 à voir...
	 * 
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// CLEANING PROCEDURE		//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * - source : 
	 * 		- supprimer zgraphics
	 * 		- supprimer tous les commentaires TODO et NOTE
	 * 		- effacer le changelog
	 * 		- virer les classes contenant un main()
	 * - ressources :
	 * 		- supprimer restmp
	 * 		- r�initialiser les logstats
	 * 		- recopier les stats (éventuellement)
	 * 		- recréer les sauvegardes des tournois
	 * - options :
	 * 		- r�initialiser le joueur humain
	 * 		- virer l'enchainement automatique
	 * 
	 * - pour diffusion publique :
	 * 		- fichiers :
	 * 			- recompiler le jeu	
	 * 			- virer les fichiers sources
	 * 			- virer les IA pas finies (source+profils)
	 * 			- virer les versions interm�diaires des IA
	 * 		- options :
	 * 			- simulation des matches AI-only
	 * 		- pr�règler la partie rapide sur : 
	 * 			- les meilleures IA 
	 * 			- des niveaux compatibles
	 * 			- les règles classiques
	 * 
	 *  - pour diffusion projet :
	 * 		- réseau :
	 * 			- bloquer le bouton "réseau" dans le menu principal
	 * 			- bloquer le bouton "réseau" dans le menu de config tournoi/partie rapide
	 * 		- replay :
	 * 			- bloquer le bouton "replay" dans le menu principal
	 * 			- bloquer le bouton "cam�ra" dans les menu tournoi/match/round
	 * 			- bloquer l'action dans les options avanc�es
	 * 		- options :
	 * 			- afficher les exceptions
	 * 		- ressources :
	 * 			- redonner les host id corrects pour les deux �tudiants modifiés pour tester le réseau (5, 13 et 20)
	 *		 	- virer le th�me/perso TBB apr�s avoir vir� les joueurs utilisant les perso et pr�-enregistr�s
	 * 
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// A FAIRE SITE		//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 *	- IA :
	 * 		- utilitaire permettant de controller les checkinterruptions dans les programmes des �tudiants
	 *	- stats :
	 *		- possibilit� d'importer/exporter les stats ind�pendemment de la version des classes
	 *	- GUI : 
	 *  - tournois :
	 *  	- championnat
	 *  - instances :
	 *  
	 *  x) utilitaire pour controler les IA des �tudiants (partage 3/2)
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//EN COURS			//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * - percepts à rajouter
	 * 		- bonus : indiquer pr�cis�ment les types de bonus restants
	 * 		- infos sup sur les adversaires : propriétés des bombes qu'ils peuvent poser, pour pouvoir calculer les blasts
	 * 			>> méthode prenant une position et un joueur en paramètres et calculant le blast et le temps de d�tonation d'une bombe pos�e l� (voire donnant un objet bombe)
	 * 			>> définir une classe bombe virtuelle ? destin�e à ce seul effet ?
	 * 		- la notion de blast peut être impr�cise quand plusieurs bombes sont concern�es
	 * 			>> ça emp�che de calculer des r�action en chaine
	 * 			>> faudrait associer le temps
	 * 			>> ça permettrait d'impl�menter une fonction calculant l'�tat des explosions dans le futur, utilisable à la fois pour les bombes normales et pour les bombes virtuelles
	 * 		- il faut pouvoir être capable de déterminer si un perso est malade, voire contagieux
	 * 		- pour faire un suivi d'une ia à travers plusieurs rounds :
	 * 			- chemin permettant d'écrire dans le package de l'IA, afin de lui permettre de faire des sauvegardes ?
	 * 			- avoir acc�s à l'identifiant de l'IA, et pas seulement à sa couleur
	 * 		- renommer les packages des ia en fr.free.totalboumboum.ai.???? (faudra peut être renommer aussi les classes adapter)
	 * 		- il faut g�rer les chemins au niveau des pixels. les cases, c'est pas assez pr�cis.
	 * - définir une mesure factuelle (reposant sur des faits de jeu) �valuant l'agressivit� des joueurs
	 * 		- cible des bombes: rien, mur ou IA
	 * 		- à pond�rer avec le nombre de bombes pos�es par seconde (par ex)
	 * 
	 * - dans NBM1, on prend l'item quand on passe au centre de l'item, pas quand on entre simplement sur la case
	 * - prochaines �tapes :
	 * 		- league
	 * 		- instance TBB
	 * 		- sons
	 * 		- BM'83
	 * 		- shrink (?)
	 * 		- réseau
	 * 		- revoir GUI (?)
	 * 		- sortir de b�ta
	 * 
	 *  - tournoi league :
	 *  	- finir GUI
	 *  	- finir s�lection des matches
	 *  	- ordonner les matches
	 *  - tournoi coupe : 
	 *  	- possiblit� de définir un tie-break random
	 *  	- possibilit� de définir un tie-break classement stats
	 *  	- possibilit� de définir des matches de classement optionnels (?)
	 *  
	 *  - charger directement le format AI depuis le jeu permettrait de ne pas avoir à g�n�rer 50 images de couleurs différentes
	 *    (ou un autre format vectoriel, SVG p-� ?)
	 *  
	 *  - options pour les stats ?
	 *  	- acc�l�rer la fin des parties où tous les humains ont été �limin�s (p-e un popup ? ou une option : oui-non-demander)
	 *  	- enregistrer l'�volution des stats (sur plusieurs points temporels)
	 *  	- forcer la fin de la p�riode (now)
	 *  - instance TBB
	 *  	- autoriser un burning player à poser une dernière bombe...
	 *  	- bombe sensible aux chocs : en forme d'oeuf
	 *  	- bombe en fer, r�siste aux autres explosions
	 *  	- le feu passe-muraille doit être d'une autre couleur et/ou forme
	 *  	- feu �lectrique : tente d'atteindre un adversaire dans la mesure de sa port�e (prend des virages et tout)
	 *  	- feu d'amour : 
	 *  		- plein de petits coeurs qui contaminent ceux qu'ils touchent. 
	 *  		- les bombes qu'ils posent sont alors aussi des bombes à coeurs. 
	 *  		- elles ne tuent pas, elles contaminent. 
	 *  		- le contaminateur initial est r�sistant
	 *  	- si le feu normal �tait graphiquement un truc carr� avec plein de flam�ches ?
	 *  	- apparition des items : fum�e+l�g�rement sur-�lev�, puis l'item tombe par terre... (comm bug apr�s cache v2)
	 *  
	 *  - enregistrement d'une partie : au lieu d'utiliser data+xml, mettre les donn�es
	 *    xml dans le fichier de donn�es aussi. (au d�but bien entendu)
	 */ 
	
	/**
	 * TODO
	 * dans les fichiers XML animes et trajectoires : pr chaque dir/gesture,
	 * possibilit� (sous forme d'attribut) de redéfinir ou compléter
	 * l'�l�ment déjà défini dans un éventuel sprite abstrait. bien sur,
	 * si pas déjà défini on fait un redefine pr défaut
	 */
	
	/**
	 * TODO
	 * quand on interrompt un round,
	 * les points sont calculés normalement pr les IA
	 * et les joueurs humains sont tous ex-aequo derniers
	 * (voire tout le monde ex-aequo?)
	 */

	/**
	 * TODO faire une appli propre pour g�n�rer les niveaux
	 * mettre une jolie doc en ligne de commande pour ex�cuter tout �a
	 */

	/*
	 * TODO le caract�re local d'un joueur s'oppose à ses commandes
	 * à la fois dans l'interface graphique et dans le moteur
	 */

	/*
	 * lors du chargement des sprites, ne charger que le nécessaire :
	 * 	- blocks/floors
	 * 	- items
	 * 	- fires
	 * 	- heroes gestures (?)
	 */
	
	/**
	 * - certaines abilities ont un effet graphique, comme twinkle ou flat
	 *   faudra penser à un syst�me pour transmettre les changement les concernant
	 */
	
	/**
	 * TODO replay
	 * 	- ajouter des commandes (avance rapide, vitesse, retour arri�re)
	 *  - enregistrer une image au pif en cours de jeu, histoire de l'enregistrer en tant que preview
	 */
	
	/*
	 * TODO interface
	 * remplacer le menu actuel par des icones dont la taille représente l'importance
	 * à gauche de l'icone, mettre le texte actuellement prèsent dans les boutons
	 * il est align� à droite et sa taille est proportionnelle à celle des boutons
	 * 	- options       : engrenages
	 * 	- profils       : une des icones de profil/joueur/etc
	 * 	- stats         : icone stat utilisée en cours de jeu
	 *  - ressources    : icone package
	 *  - tournoi	    : coupe
	 *  - partie rapide : �clair du bouton play
	 *  - charger       : disquette avec fl�che (inverse de sauvegarde)
	 *  - revoir        : une télé ? un oeil ? une pellicule?
	 *  - à propos      : un "?"
	 *  - quitter       : une croix (comme pour fermer une fen�tre) ou l'icone "quitter" du tournoi (?)
	 */
	
	// TODO toutes Loops >> effacer tous les objets inutiles dans finish()
	
	// TODO faut transmettre les abilities/modulations graphiques, elles sont nécessaires
	//	>> faut donc un modulation manager, et un ability manager (?)
	//	>> coute cher... autre solution ?
	// pb pr les IA remote : y a besoin de l'�tat réel du niveau, pas seulement des graphismes
	// solution : mise à jour locale (pas comme replay) et tant pis pr la puissance perdue et la possible d�-synchro
	
	// TODO dans la gui, faudrait g�rer le texte comme les images :
	// le même texte peut servir sur plusieurs �l�ments.
	// ça �conomiserait du traitement et de la place dans les fichiers de traduction,
	// qui seraient plus simples à maintenir.
	// et en plus on pourrait garder des noms d'�l�ments correspondant aux composants GUI,
	// sans se soucier de leur signification réelle (contenu textuel).
	// du cp, les cl�s du texte pourraient être plus explicites (car ind�pendantes)
	
	//TODO tester les confs de tournois/partie rapide sans l'option "utiliser les r�glages pr�c�dents"
	
	//TODO profiles options : reset passwords (automatically performed
	//when the MAC address changes, anyway)
	
	/* TODO la communication [moteur >> gui] devrait se faire par evts, y compris durant le jeu
	 * >> ca permettrait de ne pas raffraichir pour rien lors de l'affichage de toutes les structures du jeu
	 * >> faut définir un syst�me d'�v�nement pour toute classe affich�e directement par un composant graphique
	 * >> ça permetrait de ne pas avoir à recréer les �crans. par ex, dans les options : si qqch est chang�, c'est automatiquement m�j en background
	 * 		- sauf qu'en fait c'est un mvais ex car dans les options ce sont des clones qui sont utilisés
	 *        mais il suffirait de ne pas recréer à chaque fois le panel, plutot de r�utiliser le pr�c�dent en faisant un set et en affectant le listener
	 *      - faudrait du cp faire attention à la gestion de la m�moire : 
	 *      	- les objets finished devraient se d�barasser de leurs listeners
	 *      	- en r�action, les listeners mettent leur référence à null et n'affichent rien >> bon �a
	 *      >> ça va poser des pb de gestion de la m�moire (?)
	 * >>> à faire plus tard...
	 */
	
	/**
	 * utilisation de lock : mettre unlock dans un block finally
	 * 
	 * public class MaClasse
		{	...
			public void maMethode(...)
			{	verrou.lock();
				try
				{	//section critique...
				}
				finally
				{	verrou.unlock();
				}
				...
	 */
	
	/**
	 * NOTE règles générales
	 * 	- une demande d'info aupr�s du serveur (voire client) ne doit pas être
	 *    r�alis�e tant que la GUI n'est pas pr�te à traiter l'�v�nement de lecture associé
	 *  - les méthodes des connexions générales susceptibles d'�tre appelées par les connexions
	 *    individuelles doivent être synchro, afin d'�viter par ex que plusieurs clients
	 *    ne demandent la même t�che en même temps, risquant une interf�rence
	 *    
	 * 
	 * 
	 * 
	 * PROFIL
	 * faut revoir la notion de profil:
	 * 	- connecter directement les stats
	 *  - introduire la distinction entre profil local et distant (fait)
	 *  
	 *  - ne plus permettre qu'un joueur n'appartienne pas au classement glicko2
	 *  
	 *  ca devrait pas être possible d'avoir un profil sans stats
	 *  toutes les stats devraient être centralis�es et chargées quand nécessaire
	 *  y compris pour les rencontres locales (qui peuvent être stock�es le temps d'avoir un acc�s réseau)
	 * 
	 * 
	 * 
	 * REMOTE 
	 * propriété remote des joueurs : comment s'assurer qu'un joueur est bien identifi� ?
	 * >> le central enregistre l'id du dernier hote sur lequel le joueur s'est connect�
	 * 	  si un joueur essaie de se connecter à partir d'un h�te différent, erreur et on demande au joueur de s'identifier
	 * >> mais en fait c'est le central qui controle la v�racit�, c'est lui qui d�tient le dernier hote de connection
	 *    donc on ne peut faire de partie enregistr�e qu'en passant par le central, basta. sinon c'est du hors-piste, de l'amical.
	 * 
	 */ 
	
	/**
	 * à tester :
	 *    - à la fin d'un round, le client ne sort pas de Loop
	 *    - changement effectif d'�tat entre les différents types de browsing pour le client
	 *    		>> besoin de voir ça sur un tournoi complet
	 *    - tester le mode partie rapide
	 *    - le serveur change bien d'�tat à chaque modification de la partie
	 * probl�mes :
	 *    - quand un client s�lectionne des commandes : c'est pas conserv� quand le serveur valide la partie
	 *    - impossible de bouger le perso cété client
	 *    - jeu saccad� cété client même en LAN >> faire de l'�v�nementiel et pas du cosm�tique
	 *    - parfois une exception se produit li�e à GregorianCalendar
	 *    
	 * à faire :
	 *  - la deconnection en cours de partie (et dc de jeu) doit être traitée pour �viter le blocage prolong� du serveur, à toutes les phases de la gui/du jeu
	 *  
	 *  - tout changement d'�tat du client devrait être :
	 *  	- une requ�te envoy�e au serveur
	 *  	- si celui-ci valide, alors les modifs nécessaires sont appliqu�es cot� client
	 *  	>> cf le passage de game selection à players selection
	 *  	>> pas tout changement en fait, par exemple l'exit de ce même état est d�cid� unilat�ralement
	 *  - déterminer les ressources à synchroniser et... le faire rigoureusement
	 *  - penser à tester syst�matiquement si l'�tat du c/s qui reçoit un evt est compatible avec le traitement de cet evt 
	 *  - tout evt transmis à la connection générale par l'indiv doit identifier l'indiv
	 *    car pr client, il y a une diff�rence, qui doit être traitée, entre recevoir un evt donn�
	 *    d'une connection active ou d'une autre connection.
	 *    plus généralement, le traitement d�pend de l'�tat du client pour le serveur correspondant
	 *  - quand la partie commence, faudrait :
	 *  	- fermer toutes les connections inutiles cot� client (i.e. autres serveurs)
	 *  	- p-� mettre les connections concern�es par la partie dans une liste sp�ciale, cété serveur (pr optimiser les temps de transmission)
	 * 
	 * 
	 * 
	 * r�forme GUI
	 * 	- certains pb de gestion de la GUI viennent du fait que des donn�es temporaires sont stock�es dans les classes de la GUI
	 * 	  alors qu'elles seraient mieux control�es et int�gr�es si elles �taient du cété du jeu
	 * 	  exemple: s�lection temporaire de joueurs lors de la configuration d'un joueur. le fait que ces donn�es soient dans la GUI
	 * 	  pose des pb de mise à jour du jeu en réseau, car on est alors oblig� de duppliquer les listes.
	 * 	  >> r�forme à organiser conjointement à la mise en place des �v�nements
	 * 	- GUI: 
	 *  	- dans les cas où on a un panel affichant une liste et d'autres
	 *    	  panels affichant l'�l�ment s�lectionn�, il faudrait que ces derniers �coutent
	 *    	  la liste afin de se mettre à jour automatiquement quand nécessaire.
	 *  	- en fait on peut faire une version simple des souspanels, et une version
	 *    	  qui �coute le panel principal et h�rite de la version simple
	 *  	- pour la configuration des parties, faut une structure g�n�r�e au niveau de ConfigurationXxxx
	 *    	  avec des sous classes pour le jeu en réseau (une pour client, une pour serveur)
	 *    	  tout ça doit impl�menter tout ce qui est déjà impl�ment� au niveau des panels, afin que la GUI
	 *    	  ne fasse plus aucun traitement, mais se contente de transmettre les commandes de l'utilisateur au moteur
	 *  - remarques : 
	 *  	- l'int�gralit� du traitement devrait être effectué cété moteur
	 *    	  la gui ne devrait fonctionner que par requ�te/r�ponses synchrones (pour le local)
	 *    	  ou asynchrones (pour le réseau). 
	 *    	  voire asynchrones pour les deux, avec un syst�me d'�v�nements...
	 *  	- faire un syst�me par d�l�gation permettant d'associer un gestionnaire de listeners/evts
	 *    	  à tout objet du moteur. chaque composant graphique doit simplement impl�menter une interface,
	 *    	  ce qui simplifie le beans. bien sur, chaque objet �metteur doit du cp s'identifier pr chaque evt (source)
	 *    	  et les fonctions d'�coute seront un peu plus longue cot� gui dans le cas de classes �coutant
	 *    	  plusieurs �l�ments du moteur.
	 *    
	 *    
	 *    
	 *  - reconnection process:
	 * 		- client connects and sends a REQUEST_RECONNECTION message, with its id
	 * 		- server checks if the id's
	 * 		- sends back an ANSWER_RECONNECTION message with a boolean showing acceptation or reject
	 *  	- if accepted, the server then sends the necessary updates to the client
	 *  - ça serait bien d'avoir un icone sp�cial (variante de remote) pr indiquer dans les menus qu'un joueur est d�connect�
	 *  - li� à la reconnection : possibilit� de définir des open slots (pr server)
	 *    et par la suite, des joueurs d�sirant prendre la partie en cours peuvent le faire
	 *    (similaire à un joueur d�connect� qui aurait son slot r�serv� et pourrait s'y reconnecter)
	 *  - gestion de la d�/re-connection :
	 * 		- la connection individuelle est conserv�e cété serveur,
	 * 	  	  avec un état DISCONNECTED ou autre
	 * 		- même les threads sont gardés, mais du cp le code doit être modifié
	 * 	  	  pour ne pas tenter d'écrire/lire si la connection est morte
	 * 		- quand le client tente de se reconnecter, si son id correspond à 
	 * 	   	  celle d'une connection DISCONNECTED, alors on se contente de r�initialiser les 
	 * 	  	  streams dans le writer et le reader, et ça roule.
	 *    
	 *    
	 *    
	 *  - IA:
	 *  	- définir la fonction successeur version temporelle
	 *  - à tester :
	 *  	- vitesse renvoy�e par l'API (pour déplacement joueurs)
	 *  	- d�compte des items
	 *  	>> en fait : toutes les modifs depuis la version pr�c�dente
	 *  
	 */
	
	/**
	 * TODO chemins à généraliser
	 * 	- associer un temps avec chaque case (attente) ?
	 *  - structure abstraite, on peut accéder aux cases ou aux pixels
	 *  - le chemin est entier, on passe le perso en paramètre et on nous dit la case suivante (null si pas sur le chemin)
	 *    voire la direction à prendre pour suivre le chemin
	 *  - à voir comment ça peut être représent� derri�re...
	 */
	
	// TODO clignotements de sprites : mettre de la transparence dans les version colori�es
	// TODO utiliser le calcul d'occupation CPU pour limiter les IA trop gourmandes
	// 		utiliser la limite sur yield déjà définie (cf classe config d'ia)
	// TODO calculer un temps d'occupation "normal" (avant le jeu) et s'en servir de r�f pour le temps en cours de jeu
	// TODO arr�ter les threads dont les ia ont été �limin�es du jeu
	// TODO voir quels threads swing prennent toutes les ressources sur les niveaux chargés
	// TODO finir IA
	//	optimisation : poser bombe juste en sortant de la case (pixels) histoire d'�viter de se faire bloquer par le gus qui est devant
	// TODO apparemment les blocs qui tombent lors du shrink sont différents, au moins dans SBM1 & SBM2 
	//	>> à ripper et v�rifier pr les autres versions
	// TODO g�rer le tie-break du dernier tournoi
	// TODO faire disparaitre progressivement les sprites à la fin du burning (niveau d'alpha ? cf items th�me TBB)
	//		>> en fait y a pas de gestion dynamique de l'alpha...
	// TODO utiliser des chemins relatifs à la racine du sprite �viterait toutes les complications li�es au nommage d'images/ombres
	// TODO y a surement un bug pr rezo+simulation (cété client)
	// TODO quand on tente de poser une bombe, le sprite est créé et on tente de voir s'il peut appara�tre
	// 	    question : est-il d'entr�e ins�r� dans le Level ? si oui, y en a surement un paillat au bout d'un moment;
	//		vu qu'en cas d'apparition impossible la bombe n'est pas retir�e du Level (supposition)
	// TODO il est possib que les ombres de Shirobon SBM2 soient à d�caller un peu vers le bas, à voir comment ça rend dans le jeu
	// TODO faire le Dr. Mook de SBM1 (commande le boss du monde 3+transition3>4+boss 5+transition5>6+bosses6&7+fin)
	// TODO voir les items initiaux de chaque zone + la vitesse initiale
	// TODO dans les fichiers xml, g�rer les images de fa�on unique, ce qui permettrait de définir des transformations (flips, rotations)
	//	>> définir des borders comme dans SBM sans avoir à tout g�n�rer à la main (pr blocs sym�triques)
	//	>> temps de chargement plus rapide (en supposant que la transfo est plus rapide que le re-chargement)
	// TODO dans belts, y a un bidule qui passe sur les rails sur les cétés...
	// TODO est-il vraiment nécessaire de g�rer les floors en tant que sprites à part enti�re, quand on peut y mettre plusieurs blocs?
	// TODO réseau:
	//	>> pb quand c'est un client qui clique sur le round en 1er: si s�lection al�atoire, alors le niveau est différent de celui du serveur!
	//	>> en fait, les clients ne devraient pas pouvoir avancer dans le tournoi avant le serveur
	//	>> faut un message indiquant qu'on attend les autres joueurs (et lesquels)
	
	/**
	 * TODO
	 * - corriger bug en cours (�a bloque, faire un debug)
	 * - le message n'est pas affich� cété serveur (surement li�)
	 * - faire le même affichage cété client
	 */
}
