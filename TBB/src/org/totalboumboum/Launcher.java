
package org.totalboumboum;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
	 * TODO dans l'API IA il faut g�n�raliser (tout en simplifiant) les propri�t�s des sprites, plutot que 
	 * de d�finir des fonctions sp�cifiques � chaque fois.
	 */
	
	/*
	 * TODO type de tournoi plus adapt� au r�seau : hot potatoe
	 * des joueurs jouent un match pendant que d'autres attendent 
	 * � la fin du match, les n derniers joueurs sont rel�gu�s dans la salle d'attente
	 * n joueurs qui attendaient sont qualifi�s.
	 * un classement permet de d�terminer le leader provisoire
	 */
	
	/*
	 * TODO inclure dans l'API IA des fonctions d'action pr�d�finies.
	 * par exemple des fonctions pour le d�placement, on indique o� on veut aller et �a d�termine � chaque it�ration
	 * quelle est l'action � effectuer. �tendre � d'autres actions ?
	 */
	
	/*
	 * TODO include dans l'API une fonction d'initialisation de l'IA, appel�e lors de sa cr�ation
	 * faudrait passer en param�tre le niveau et des infos style l'instance, etc.
	 * �a permettrait par exemple, pour une IA qui apprend, de t�l�charger son fichier de sauvegarde
	 * (dans son package)
	 */
	
	/*
	 * evaluation du projet : pour la qualification, utiliser ke temps n�cessaire pour �liminer un/des adversaire(s) de r�f�rence
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
// MULTIPLAYER		//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * TODO
	 * y a un mode de tournoi plus adapt� pour le multijoueur, d�fini chaiplus trop o� (mails?)
	 * avec une esp�ce de tournante : n joueurs, matches de k joueurs, les n-k derniers sont remplac�s
	 * par ceux qui attendent
	 */
	
	/*
	 * TODO
	 * - pour faciliter la communication lors des parties r�seaux :
	 *   syst�mes de bulles pour communiquer. le joueur affecte un message
	 *   � une touche, et quand il appuie dessus, une bulle apparait � c�t�
	 *   de son joueur pour signifier ce message aux autres joueurs (vraisemblablement
	 *   distants)
	 * - p-� que pour rendre �a plus facile � utiliser, faudrait pr�d�finir
	 *   un certain nombre de messages et permettre au joueur d'en d�finir certains custom
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
	 * TODO g�rer le d�placement des items lors d'une explosion
	 */
	
	/*
	 * TODO
	 * item sp�cial permettant de manger les bombes des autres, comme certains ennemis du jeu original en mode histoire
	 * item sp�cial qui, quand un h�ros meurt, d�clence automatiquement une explosion, �ventuellement avec un effet de zone  
	 */
	
	/*
	 *  TODO malus pour les bombes :
	 *  port�e al�atoire : on sait pas � quelle distance �a va p�ter 
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
	 * TODO rajouter des niveaux in�dits en utilisant au maximum les parties inutilis�es
	 * des autres th�mes
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
	
	/*
	 * TODO chargement des niveaux
	 * 	- commencer une partie rapide avec un seul niveau
	 * 	- jouer un round
	 * 	- changer les items par d�faut pour ce niveau, directement dans le fichier XML
	 * 	- joueur le round suivant: 
	 * 		- la gui affiche la modif
	 *		- mais la hollow level n'a pas �t� recharg� (mutualisation)
	 *		- et donc la modif n'est pas prise en compte dans l'initialisation des joueurs 
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
	 *   qui n'est jamais modifi� apr�s
	 * - de plus, l'ensemble de toutes ses adresses MAC sont enregistr�es
	 *   (http://www.kodejava.org/examples/250.html 
	 *    http://www.stratos.me/2008/07/find-mac-address-using-java
	 *    http://www.kodejava.org/examples/250.html)
	 * - cette liste est mise � jour � chaque d�marrage
	 * - si d'un d�marrage � l'autre il n'y a aucune adresse commune, �a veut dire que ce n'est plus la m�me machine
	 * - on g�n�re alors un nouvel UUID qui �crase le pr�c�dent
	 * - les mots de passe de tous les profils sont r�initialis�s
	 * 
	 * JOUEURS
	 * - chaque joueur est identifi� par l'identifiant de l'hote sur lequel il est cr��
	 *   plus un identifiant unique pour cet h�te (on peut prendre un autre UUID)
	 * - ce num�ro ne change jamais, m�me quand il joue sur un autre hote (ce qui est possible)
	 * - on a un truc du style : joueur@createur
	 * - quand le profil est utilis� sur son hote de cr�ation, pas besoin de mot de passe
	 * - sinon il faut un mot de passe, qu'on a la possibilit� d'enregistrer
	 * >> en fait pas besoin de voir l'hote, avec l'uDDi �a suffit
	 *    chaque joueur doit entrer le mdp quel que soit l'hote (m�me cr�ateur)
	 *    si le joueur n'est pas enregistr� sur le site, il ne peut pas �tre prot�g� par un mot de passe
	 *    et de toute fa�on ses stats ne comptent pas
	 * >> int�r�t d'identifier de fa�on unique l'hote ??
	 *    ah oui, pr les stats sur qui joue chez qui...
	 *    
	 * NOTE empecher le m�me joueur de joueur deux matches en m�me temps...
	 * >> que les matches via central enregistr�s dans les stats
	 */
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// KNOWN, WAITING BUGS	//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	/*
	 * TODO
	 *	l'ombre des blocs de la derni�re ligne est port�e sur les blocs de la premi�re ligne
	 *	c'est normal. pour l'�viter, il faudrait traiter un cas assez particulier... � voir
	 *	>> tentative de r�solution mise en place, � voir
	 */
	
	/*  
	 *  TODO l'item kick ne marche pas
	 */
	
	/*
	 * TODO
	 * quand on pose deux bombes en diagonale et qu'on se place dans le cadrant int�rieur d'une des cases libres du m�me carr�
	 * on est bloqu�. ce n'est pas vraiment un pb en fait, plus un feature :P . mais les non-initi�s peuvent prendre �a pour un bug.
	 * (note : point mentionn� dans le blog)
	 */
	
	/* 
	 * TODO
	 * impossible de poser une bombe quand on est en train de buter contre un mur en direction upleft (et uniquement cette direction pr NES2) et downleft (pr SBM1)
	 * 		>> ca viendrait de swing ou du clavier (pb mat�riel ou bas niveau)
	 */
	
	/* 
	 * TODO
	 * cas particulier : item apparaissant dans une explosion de bloc, avec un joueur d�j� sur le bloc 
	 * (il a passe-muraille et r�sistance au feu) : l'item doit �tre ramass� d�s qu'il a fini d'apparaitre, en th�orie
	 * ou bien : un h�ros meurt, un item apparait sous les pieds d'un autre joueur immobile : l'item reste sans que le joueur ne se l'approprie
	 * >> en m�me temps, �a laisse le choix au joueur de ramasser l'item ou pas, c'est pas mal finalement (si c'est un malus par ex.)
	 */
	
	/*
	 * TODO
	 * bug d'affichage dans les notes d'IA, les accents sont affich�s avec la police par d�faut
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
// CHANGE LOG	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* TODO
	 * + beta.018
	 * - Moteur :
	 * 		- refonte de l'affichage : tout est maintenant d�l�gu� � un ensemble de classes Display
	 * 		- distinction entre diff�rents types de joueurs (humain, ia, distant) via des classes sp�cifiques
	 * 		- nouvelle classe de Loop pour le replay, meilleure structuration des Loop
	 * 		- nouveaux managers vides, entre autres destin�s aux sprites du mode replay
	 * 		- nouvelle classe Replay, rendant le replay fonctionnel
	 * - GUI :
	 * 		- nouvelle option de replay dans le menu principal
	 * 		- (d�s)activation de l'enregistrement du replay dans les options
	 * 		- m�me chose dans le menu des tournois/parties rapides (en tant que raccourci)
	 * - IA :
	 * - stats :
	 * - Divers :
	 * 		- nettoyage du code par rapport � l'utilisation de ArrayList (remplac� par List partout o� c'�tait possible)
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// BUGS				//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* TODO
	 * 
	 * - bug : j'ai vu l'ia prendre une vie, mourir, relacher la vie, et revenir. 
	 * 	 et la vie �tait fonctionnelle : je l'ai prise, je suis mort, je suis revenu (sans la relacher cette fois)
	 *   >> pas r�ussi � reproduire �a...
	 * 
	 * - la pause d'IA reste m�me si le joueur est mort, et je ne sais pas pq : au d�bug le moteur n'ex�cute pas le code affichant le texte !
	 * 
	 * - quand on balance une bombe, puis une autre, puis fait p�ter la premi�re et enfin la seconde 
	 *   juste � la fin des flammes de la premi�re, alors l'explosion de la 2nde est circonsrite � la
	 *   case o� elle atterrit.
	 *   
	 * - pb de r�initialisation : 
	 * 		- jouer un tournoi single (par ex supercup) en entier
	 * 		- jouer un tournoi de type cup en suivant : il y a un pb sur le MatchPanel, qui semble n'avoir pas �t� r�init � null
	 * 
	 *  - apparemment, y a un pb avec les GeneralAction, dont certaines d�finies dans les fichiers XML ne sont pas compatibles 
	 *    avec la classe (en termes de circonstances) mais qui sont quand m�me charg�es et initialis�es normalement
	 *  
	 *  - il semblerait que les directions dans la SpecificAction et dans la GeneralAction correspondante ne soient pas les m�mes...
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// A FAIRE MOTEUR	//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* TODO PRIORIT� HAUTE
	 * 
	 * - faut �mettre un �vt de sortie de case � la disparition d'un sprite (mort, masquage, etc)
	 * 
	 * - �tudier le fonctionnement de ended (sprite) pr voir quand retirer le sprite de level/tile
	 * 
	 * - g�rer le shrink
	 * 
	 * - remplacer "� vos marques" "pret" "boum!" par des graphismes pr�calcul�s
	 * 
	 * - � la fin de la partie, faire disparaitre tout le niveau comme il apparait au d�but
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
	 * 		- besoin d'une m�thode permettant d'exporter un tournoi/match/round, 
	 * 		  ie de l'�crire enti�rement en local (pas de r�f�rence � des composants existants)
	 * 		- dans les fichiers xml, pour les points, plutot que local (vrai ou faux), utiliser round/match/tournament/game (permet de mieux mutualiser les fichiers)
	 * 		- tournoi coupe : 
	 * 			- pouvoir se qualifier depuis n'importe quel leg, pas forcement le pr�c�dent direct (n�cessit� de pr�ciser le num�ro du leg en plus du part, dans le doc XML et la class CupPlayer)
	 * 			- y compris depuis le leg initial (pr�voir une sous-classe pour distinguer initial des autres?)
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
	 * 		- HIDING devrait �tre un gesture d�finit automatiquement, non ? pas d'image, sensible � rien, seule action autoris�e=apparaitre...
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
	 * - rajouter une confirmation (popup) quand on sort d�finitivement d'une partie (icone maison)
	 * 
	 * - 1�re ex�cution
	 * 		- d�finir un popup d'informations
	 * 		- guider l'utilisateur pas � pas pour cr�er son profile et commencer sa partie
	 * 
	 * - pour les tournois, le chargement/s�lection du tournoi doit pr�c�der le choix des joueurs
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
	 * - tournoi : 
	 * 		1) on choisit les param�tres 
	 * 		2) on choisit les joueurs, le jeu restreint leur nombre pr qu'il soit compatible avec le tournoi, 
	 * 		   et restreint aussi les IA pour les m�mes raisons
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
// CLEANING PROCEDURE		//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * - source : 
	 * 		- supprimer zgraphics
	 * 		- supprimer tous les commentaires TODO et NOTE
	 * 		- effacer le changelog
	 * 		- virer les classes contenant un main()
	 * 		- virer le package d'IA 2010-11
	 * - ressources :
	 * 		- supprimer restmp
	 * 		- r�initialiser les logstats
	 * 		- recopier les stats (�ventuellement)
	 * - options :
	 * 		- r�initialiser le joueur humain
	 * 		- virer l'enchainement automatique
	 * 
	 * - pour diffusion publique:
	 * 		- fichiers :
	 * 			- recompiler le jeu	
	 * 			- virer les fichiers sources
	 * 			- virer les IA pas finies (source+profils)
	 * 			- virer les versions interm�diaires des IA
	 * 		- options :
	 * 			- simulation des matches AI-only
	 * 		- pr�r�gler la partie rapide sur : 
	 * 			- les meilleures IA 
	 * 			- des niveaux compatibles
	 * 			- les r�gles classiques
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
	 * - percepts � rajouter
	 * 		- bonus : indiquer pr�cis�ment les types de bonus restants
	 * 		- infos sup sur les adversaires : propri�t�s des bombes qu'ils peuvent poser, pour pouvoir calculer les blasts
	 * 			>> m�thode prenant une position et un joueur en param�tres et calculant le blast et le temps de d�tonation d'une bombe pos�e l� (voire donnant un objet bombe)
	 * 			>> d�finir une classe bombe virtuelle ? destin�e � ce seul effet ?
	 * 		- la notion de blast peut �tre impr�cise quand plusieurs bombes sont concern�es
	 * 			>> �a emp�che de calculer des r�action en chaine
	 * 			>> faudrait associer le temps
	 * 			>> �a permettrait d'impl�menter une fonction calculant l'�tat des explosions dans le futur, utilisable � la fois pour les bombes normales et pour les bombes virtuelles
	 * 		- il faut pouvoir �tre capable de d�terminer si un perso est malade, voire contagieux
	 * 		- pour faire un suivi d'une ia � travers plusieurs rounds :
	 * 			- chemin permettant d'�crire dans le package de l'IA, afin de lui permettre de faire des sauvegardes ?
	 * 			- avoir acc�s � l'identifiant de l'IA, et pas seulement � sa couleur
	 * 		- renommer les packages des ia en fr.free.totalboumboum.ai.???? (faudra peut �tre renommer aussi les classes adapter)
	 * 		- il faut g�rer les chemins au niveau des pixels. les cases, c'est pas assez pr�cis.
	 * - d�finir une mesure factuelle (reposant sur des faits de jeu) �valuant l'agressivit� des joueurs
	 * 		- cible des bombes: rien, mur ou IA
	 * 		- � pond�rer avec le nombre de bombes pos�es par seconde (par ex)
	 * 
	 * - dans NBM1, on prend l'item quand on passe au centre de l'item, pas quand on entre simplement sur la case
	 * - prochaines �tapes :
	 * 		- league
	 * 		- instance TBB
	 * 		- sons
	 * 		- BM'83
	 * 		- shrink (?)
	 * 		- r�seau
	 * 		- revoir GUI (?)
	 * 		- sortir de b�ta
	 * 
	 *  - tournoi league :
	 *  	- finir GUI
	 *  	- finir s�lection des matches
	 *  	- ordonner les matches
	 *  - tournoi coupe : 
	 *  	- possiblit� de d�finir un tie-break random
	 *  	- possibilit� de d�finir un tie-break classement stats
	 *  	- possibilit� de d�finir des matches de classement optionnels (?)
	 *  
	 *  - charger directement le format AI depuis le jeu permettrait de ne pas avoir � g�n�rer 50 images de couleurs diff�rentes
	 *    (ou un autre format vectoriel, SVG p-� ?)
	 *  
	 *  - options pour les stats ?
	 *  	- acc�l�rer la fin des parties o� tous les humains ont �t� �limin�s (p-e un popup ? ou une option : oui-non-demander)
	 *  	- enregistrer l'�volution des stats (sur plusieurs points temporels)
	 *  	- forcer la fin de la p�riode (now)
	 *  - instance TBB
	 *  	- autoriser un burning player � poser une derni�re bombe...
	 *  	- bombe sensible aux chocs : en forme d'oeuf
	 *  	- bombe en fer, r�siste aux autres explosions
	 *  	- le feu passe-muraille doit �tre d'une autre couleur et/ou forme
	 *  	- feu �lectrique : tente d'atteindre un adversaire dans la mesure de sa port�e (prend des virages et tout)
	 *  	- feu d'amour : 
	 *  		- plein de petits coeurs qui contaminent ceux qu'ils touchent. 
	 *  		- les bombes qu'ils posent sont alors aussi des bombes � coeurs. 
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
	 * possibilit� (sous forme d'attribut) de red�finir ou compl�ter
	 * l'�l�ment d�j� d�fini dans un �ventuel sprite abstrait. bien sur,
	 * si pas d�j� d�fini on fait un redefine pr d�faut
	 */
	
	/**
	 * TODO
	 * quand on interrompt un round,
	 * les points sont calcul�s normalement pr les IA
	 * et les joueurs humains sont tous ex-aequo derniers
	 * (voire tout le monde ex-aequo?)
	 */

	/**
	 * TODO faire une appli propre pour g�n�rer les niveaux
	 * mettre une jolie doc en ligne de commande pour ex�cuter tout �a
	 */

	/*
	 * TODO le caract�re local d'un joueur s'oppose � ses commandes
	 * � la fois dans l'interface graphique et dans le moteur
	 */

	/*
	 * lors du chargement des sprites, ne charger que le n�cessaire :
	 * 	- blocks/floors
	 * 	- items
	 * 	- fires
	 * 	- heroes gestures (?)
	 */
	
	/**
	 * TODO
	 * 	1) d�finir un syst�me d'enregistrement/replay
	 * 	2) �tendre au mode r�seau, la seule diff�rence �tant que dans le preimier
	 * 		cas on lit les evts dans un fichier, alors que dans le deuxi�me on les
	 * 		re�oit � travers le r�seau.
	 * 
	 * - on suppose que chaque joueur poss�de tous les fichiers n�cessaires
	 * - par cons�quent, tout les sprites peuvent �tre charg�s a priori, ainsi que l'aper�u du niveau
	 * - mais la zone est g�n�r�e partiellement al�atoirement, donc il faut la transmettre (sous forme d'�v�nements)
	 * 		>> l'initialisation de la zone (loadTheme) doit diff�rer dans les loops
	 * - composant dans la loop player qui interpr�tera les �v�nements re�us :
	 * 		- trucs individuels >> envoy�s aux sprites concern�s
	 * 		- cr�ation: faite sur place. sprites avec des managers light
	 * 
	 * - certaines abilities ont un effet graphique, comme twinkle ou flat
	 *   faudra penser � un syst�me pour transmettre les changement les concernant
	 */
	
	/**
	 * TODO replay
	 * 	- ajouter des commandes (avance rapide, vitesse, retour arri�re)
	 *  - enregistrer une image au pif en cours de jeu, histoire de l'enregistrer en tant que preview
	 */
	
	/*
	 * TODO interface
	 * remplacer le menu actuel par des icones dont la taille repr�sente l'importance
	 * � gauche de l'icone, mettre le texte actuellement pr�sent dans les boutons
	 * il est align� � droite et sa taille est proportionnelle � celle des boutons
	 * 	- optiosn       : engrenages
	 * 	- profils       : une des icones de profil/joueur/etc
	 * 	- stats         : icone stat utilis�e en cours de jeu
	 *  - ressources    : icone package
	 *  - tournoi	    : coupe
	 *  - partie rapide : �clair du bouton play
	 *  - charger       : disquette avec fl�che (inverse de sauvegarde)
	 *  - revoir        : une t�l� ? un oeil ? une pellicule?
	 *  - � propos      : un "?"
	 *  - quitter       : une croix (comme pour fermer une fen�tre) ou l'icone "quitter" du tournoi (?)
	 */
	
	// TODO toutes Loops >> effacer tous les objets inutiles dans finish()
	
	// TODO faut transmettre les abilities/modulations graphiques, elles sont n�cessaires
	//	>> faut donc un modulation manager, et un ability manager (?)
	//	>> coute cher... autre solution ?
	// pb pr les IA remote : y a besoin de l'�tat r�el du niveau, pas seulement des graphismes
	// solution : mise � jour locale (pas comme replay) et tant pis pr la puissance perdue et la possible d�-synchro
	
	/* TODO
	 * - c�t� serveur :
	 * 		- modification de la boucle pour initialiser et fermer les connexions n�cessaires
	 *        (elles peuvent aussi �tre g�r�es en amont, � voir)
	 *        mais de toute fa�on faut aussi envoyer les donn�es via la connexion (comme pr replay en fait)
	 *      x nouvelle classe h�ritant de Player, control� � distance 
	 *      x nouveau controle associ� � cette classe, lisant les controle dans le flux associ� au joueur
	 *      xle container des remotePlayerControl doit �tre initialis� dans la boucle et lanc� aussi (run)
	 * - c�t� client :
	 * 		x version de la boucle h�ritant de replay mais rajoutant la gestion des controles locaux
	 *  	x version sp�ciale de Control pour que les codes soient envoy�s dans le flux (NetworkControl)
	 *  	x version d'un joueur : comme avant, mais g�n�raliser pour prendre LocalControl ou NetworkControl
	 *  	  le controle utilis� peut �tre d�cid� lors de l'init de la loop, en fonction du type de la loop justement
	 *  	  int�r�t: on garde le joueur humain et l'IA
	 *  	x � la place du RemotePlayersControl, c'est l'�quivalent du Replay qui va g�rer �a et transf�rer le beans aux sprites
	 *  	x m�me dans l'autre sens, faudrait p-� un objet central, histoire de faciliter la synchro lors de l'acc�s au flux...
	 * - Replay :
	 * 		x s�parer in/out
	 * 		x g�n�raliser pour g�rer � la fois les fichiers et les r�seaux
	 * x d�velopper une LocalLoop s�par�e, car certaines fonctionnalit�s de debug ne doivent pas �tre accessibles dans le cadre d'un serveur
	 */
	
	/*
	 * TODO sc�nario de configuration d'un tournoi net
	 * ------------------------------------------------
	 * 
	 * cot� serveur : 
	 * 1) d�finition du tournoi : type, niveaux, etc. (dc invers� par rapport � la version actuelle)
	 * 2) ensuite seulement, s�lection des joueurs : 
	 * 		possibilit� d'ouvrir des slots "remote".
	 * 		possibilit� de verrouiller certains slots (inutilisables)
	 * 3) puis on rend la partie accessible aux joueurs distants (publish)
	 *    apr�s �a, on ne peut plus modifier la partie, sauf en l'annulant et en recommen�ant
	 * 4) puis le serveur attend que les clients se connectent et s'enregistrent pour participer � la partie
	 * 
	 * cot� client : 
	 * 5) �cran de connexion � une partie existante (style : entrer directement l'ip, ou bien choisir dans une liste venant du site).
	 * 6) apr�s connexion, on affiche les propri�t�s du tournoi
	 *    (sans pouvoir les modifier bien sur)
	 *    un "next" am�ne � l'�cran de s�lection des joueurs.
	 * 7) on clique sur les slots libres de mani�re � rajouter des joueurs locaux
	 *    (comme pour une partie locale)
	 * 
	 * cote serveur :
	 * 8) les nouveaux joueurs sont affich�s dans la GUI
	 *    toute modif (ds la liste des joueurs) est r�percut�e � tous les clients connect�s
	 * 9) quand on estime qu'il y a assez de joueurs, on clique next pour commencer la partie
	 * 
	 * cote client :
	 * 10) quand le serveur d�marre la partie, on se retrouve automatiquement dans l'�cran de pr�sentation du tournoi
	 * 
	 * 
	 * 
	 * 
	 * remarques :
	 * 	- la gui de cr�ation de tournoi/partie rapide est la m�me que d'hab, avec une option suppl�mentaire "publish online"
	 * 	  (vaut mieux un �cran de config sp�cifique qu'un simple bouton. �a permettra de g�rer certaines options comme "partie priv�e" ou autre)
	 *  - GUI server pour la s�lection de joueur :
	 *  	- en rouge les joueurs minimaux
	 *  	- en gris (comme maintenant) ceux qu'on peut �ventuellement virer sans que �a emp�che d'utiliser le tournoi/partie
	 *  - cot� client, jouer une partie rapide ou un tournoi c pareil puisqu'une partie rapide est un tournoi !
	 * 
	 * 
	 * 
	 * 
	 * 
	 * en termes de connexion :
	 * 3) partie publi�e : 
	 * 		- cr�ation d'un thread � l'�coute d'un socket
	 *		- quand un client se connecte, il cr�e un objet ClientConnection qui va s'occuper de le g�rer
	 *		  (avec reader/writer dans leur propre thread)
	 *		- la gui enregistre un listener aupr�s de cet objet pour �couter les �v�nements de configuration
	 *		- on �crit le tournoi (ou sa description) dans le flux afin que le client y acc�de
	 * 5) connection � une partie :
	 * 		- cr�ation d'un objet ServerConnection g�rant tout �a avec ses propres threads
	 * 		- la GUI enregistre un listener comme ci-dessus pour les �v�nements de configuration
	 * 6) affichage de la description du tournoi :
	 * 		- on attend de recevoir un �v�nement indiquant que la description du tournoi a �t� lue
	 * 		- puis on l'affiche dans la GUI
	 * 7) clic sur un slot libre :
	 * 		- le client �crit dans le flux le profil du joueur
	 * 		- pour chaque modif possible du profil, un objet ad�quat est ensuite �crit dans le flux
	 * 		- dans les deux cas, la mise � jour de la GUI se fait via la r�ponse du serveur, et non pas localement
	 * 		- NOTE: ceci pose d'ailleurs un probl�me de synchro et de r�activit� de l'interface
	 * 			    on pourrait d�finir une m�thode permettant de lire un objet de fa�on bloqu�e avec un d�lai max
	 * 				la m�thode retourne automatiquement un objet si elle peut le lire, ou bien null ou bout du temps limite
	 * 				ici, �a permettrait de bloquer la GUI le temps que le serveur envoie une confirmation que le beans a �t� trait�
	 * 				alternativement, on pourrait aussi bloquer en utilisant l'attente d'un �v�nement  
	 * 8) nouveau joueur enregistr� 
	 * 		- le serveur lit dans le flux le profil et le rajoute dans la liste des joueurs si c'est possible
	 * 		- chaque modif lue dans le flux entraine un �v�nement que le serveur traite localement (il applique la modif)
	 * 		- puis le serveur envoie � tous les clients une mise � jour de l'�cran des joueurs
	 * 9) d�marrage de la partie cot� serveur
	 * 		- le serveur �crit dans le flux un objet repr�sentant le d�but de la partie
	 * 		  (normalement, les clients ont d�j� toutes les infos n�cessaires : tournoi, joueurs...
	 * 10) d�marrage de la partie cot� client
	 * 		- l'objet appropri� est re�u
	 * 		- validation de l'objet tournoi
	 * 		- affichage du tournoi
	 */
	
	/*
	 * TODO sc�nario de jeu en ligne, une fois que le tournoi/partie a �t� configur�(e)
	 * ------------------------------------------------------------------------------
	 * 
	 * 1) il est n�cessaire que toute action al�atoire soit synchro avec le serveur.
	 * 	  en cons�quence, les clients ne peuvent avancer dans le tournoi que quand ils ont re�u l'objet appropri� du serveur
	 *    cot� client, le bouton "suivant" reste gris� tant que l'objet n'a pas �t� re�u
	 * 2) pour le round c'est diff�rent car tout a �t� charg� avant d'entrer � l'�cran de description du round
	 *    quand un client clique sur "commencer", les sprites sont charg�s et un signal du serveur est attendu
	 *    le serveur envoie le signal quand tous les clients et lui m�me sont pr�ts
	 *    le jeu commence alors
	 * 3) m�me topo pour les stats : faut que le serveur ait fait 'retour' pr que les stats soient calcul�es
	 *    donc tant qu'il ne l'a pas fait, on a du gris� sur le bouton des clients
	 *    ou plutot : les stats ne sont pas mises � jour, tout simplement. mais ils peuvent les visualiser (?)
	 *    
	 * en termes de connexion :
	 * 1) quand on commence un match, le serveur l'�crit dans les connexions
	 *    pareil quand on commence un round
	 * 2) le round lui-m�me, c'est d�j� fait
	 * 3) pour les stats, suffit �galement de les �crire cot� client
  	 */
	
	// TODO dans la gui, faudrait g�rer le texte comme les images :
	// le m�me texte peut servir sur plusieurs �l�ments.
	// �a �conomiserait du traitement et de la place dans les fichiers de traduction,
	// qui seraient plus simples � maintenir.
	// et en plus on pourrait garder des noms d'�l�ments correspondant aux composants GUI,
	// sans se soucier de leur signification r�elle (contenu textuel).
	// du cp, les cl�s du texte pourraient �tre plus explicites (car ind�pendantes)
	
	//TODO tester les confs de tournois/partie rapide sans l'option "utiliser les r�glages pr�c�dents"
	//TODO voir pouquoi c'est si lent dans l'interface quand on r�gle le tournoi
	//TODO rajouter un bouton permettant de bloquer l'inscription/d�sinscription d'un joueur
	//	   � un tournoi online, afin d'en finaliser le casting
	//	   (on peut utiliser le m�me bouton que "publier" en lui donnant 3 �tats)
	// TODO utiliser des string comme message plutot qu'un booleen dans les connection
	//		int�r�t : on peut transmettre des infos plus vari�es tq "start" "players refused" etc.
	
	/*
	 * - penser � virer les connextions cr��es lors de la configuration du tournoi
	 * 	 et pr lesquelles aucun joueur ne s'est enregistr�
	 * - en cas d'annulation d'un tournoi publi�, penser � supprimer toutes les connexions en cours
	 */
	

	//TODO set up the client side now, starting with the connection interface (a modal menu ?)
	
	//TODO profile id is a string and not an int anymore
	//also string in "profile selection" (game config)
	
	//TODO profiles options : reset passwords (automatically performed
	//when the MAC address changes, anyway
	
	//TODO gestion des connections :
	// - un seul objet
	// - faire de la d�lagation vers des composants pour chaque �tape
	// >> int�ret : ne pas avoir besoin de metre en pause les threads, ni d'en cr�er 50 diff�rents
	// - pb: identifier le listener auquel envoyer l'event quand on re�oit un nouvel objet
	// >> suffit de passer par des classes messages : une pr round, une pr match, etc.
	//    et � chaque fois, on envoie uniquement aux listener concern�s en ignorant les autres.
	//    (�a peut m�me se d�l�guer pareil)
	
	
	/* TODO la communication [moteur >> gui] devrait se faire par evts, y compris durant le jeu
	 * >> ca permettrait de ne pas raffraichir pour rien lors de l'affichage de toutes les structures du jeu
	 * >> faut d�finir un syst�me d'�v�nement pour toute classe affich�e directement par un composant graphique
	 * >> �a permetrait de ne pas avoir � recr�er les �crans. par ex, dans les options : si qqch est chang�, c'est automatiquement m�j en background
	 * 		- sauf qu'en fait c'est un mvais ex car dans les options ce sont des clones qui sont utilis�s
	 *        mais il suffirait de ne pas recr�er � chaque fois le panel, plutot de r�utiliser le pr�c�dent en faisant un set et en affectant le listener
	 *      - faudrait du cp faire attention � la gestion de la m�moire : 
	 *      	- les objets finished devraient se d�barasser de leurs listeners
	 *      	- en r�action, les listeners mettent leur r�f�rence � null et n'affichent rien >> bon �a
	 *      >> �a va poser des pb de gestion de la m�moire (?)
	 * >>> � faire plus tard...
	 */
	
	/**
	 * TODO
	 * - ralentissement: p-� inetadress?
	 * - rajouter l'interaction avec l'hote rajout� par l'utilisateur :
	 * 		x donner l'ip dans un popup
	 * 		- retrouver les d�tails sur le net
	 * 		>> pas besoin de plus d'interaction style modifier l'ip (suffit de supprimer/recr�er)
	 * - le serveur doit pouvoir choisir le port
	 */
	
	/**
	 * TODO
	 * - la structure g�rant la connexion (des deux c�t�s) est au m�me niveau que la classe du tournoi,
	 *   i.e. stock�e qqpart dans la config
	 * - la connexion est g�r�e globalement par une structure utilisant la d�l�gation pour impl�menter le traitement des i/o
	 * - chaque d�l�gu� correspond � une �tape : config/tournoi/match/round
	 * - l'�tape correspondant � une info lue est identifi�e par un �v�nement de classe d�di�e,
	 * 	 ce qui permet de l'acheminer vers le d�l�gu� appropri�
	 * - cot� serveur :
	 * 		- la classe est ex�cut�e par un thread ind�pendant, pr�t � r�pondre � toute sollicitation
	 * 		- � chaque demande, un nouveau thread est forked pour g�rer ce client en particulier
	 * 		- si c'est une simple demande de gameinfo, le thread s'arr�te desuite
	 * 		- sinon il doit rester en vie pour la suite du jeu (cas o� le client s'enregistre dans la partie)
	 * 		- en cours de jeu, on a donc 1 thread par joueur, plus des threads occasionnels g�n�r�s pour les autres clients demandant des infos
	 * 		  (� noter qu'en passant par le central, seul celui-ci est susceptible de demander des infos en cours de jeu, ce qu'il ne fait pas
	 *         puisque tout marche par push. o� alors, apr�s un time out sur la partie, pour v�rifier si elle n'a pas crash�.)
	 *      - mais en fait pour chaque joueur, on a 1 thread en input et un en output, donc �a fait deux par joueur
	 *        (pour une simple demande d'info, un seul thread fait les deux, le fait d'attendre est bcp moins grave)
	 *      - en termes de structure, on peut consid�rer :
	 *      	- une classe g�n�rale principale centralisant tout �a
	 *      	  charg�e de cr�er le premier thread g�rant la connexion 
	 *      	- pr chaque �tape, un container secondaire contenu dans cette classe
	 *      	  une esp�ce de multiplexeur/d�multiplexeur
	 *      	- pr chaque client, un gestionnaire contenu dans chaque container 
	 *            dans le cas de l'inscription au tournoi, il s'occuperait de cr�er le deuxi�me thread
	 *      - en fait le coup du multiplexeur/d�multiplexeur n'est valable que quand la partie a commenc� : 
	 *        alors le serveur envoie la meme chose � chaque client.
	 *        mais ce n'est pas le cas pour tout ce qui est config, donc � g�rer diff�remment.
	 * - c�t� client :
	 * 		-  un 
	 */
	
	/**
	 * TODO algo de connection
	 * 	- on n'a besoin de rien lire : la connection commence forc�ment par la demande de gameinfo
	 *  - donc le socket peut �tre directement utilis� pour cr�er une connection de type config
	 *    avec reader/writer
	 *  - plus besoin d'un thread sp�cial pour g�rer les nouvelles connections, puisqu'on en cr�e
	 *    syst�matiquement deux autres pour les i/o
	 *    
	 *    
	 *  - en fait c'est pas la peine de distinguer config/tournoi : suffit
	 *    d'utiliser les instances individuelles pr l'�criture particuli�re
	 *    et l'instance g�n�rale pour l'�criture collective.
	 *    la lecture, elle, est toujours individuelle de toute fa�on.
	 */
	
	// TODO pour r�cup l'@ du serveur c�t� client, utiliser Socket.getInetAddress()
	// bah non, en fait on n'a jamais besoin de faire �a !
	
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
	 * TODO
	 * 
	 * faut vraiment impl�menter les time-outs :
	 * 	- si un client demande les gameinfo � un serveur, mais que celui-ci n'est pas encore pr�t ?
	 *    >> faut alors recommencer au bout d'un temps donn� (d�lai = param config)
	 *    >> cb de fois on recommence (ce nbre = param config)
	 *    
	 * quand on clique sur un serveur pas encore connect� dans la liste directe
	 * �a le met � jour. si d�j� connect�, pas la peine de mettre � jour puisqu'il fait des push
	 * mettre en place un buffer emp�chant l'utilisateur de demander plusieurs mise � jour
	 * 
	 * NOTE r�gles g�n�rales
	 * 	- une demande d'info aupr�s du serveur (voire client) ne doit pas �tre
	 *    r�alis�e tant que la GUI n'est pas pr�te � traiter l'�v�nement de lecture associ�
	 *  - les m�thodes des connexions g�n�rales susceptibles d'�tre appel�es par les connexions
	 *    individuelles doivent �tre synchro, afin d'�viter par ex que plusieurs clients
	 *    ne demande la m�me t�che en m�me temps, risquant une interf�rence
	 *    
	 * pr l'histoire d'utiliser des evts sur les �l�ments du jeu affich�s par la GUI :
	 * �a ne concerne pas la communication entre �l�ments du jeu (sauf exception), qui devrait
	 * �tre r�alis�e de mani�re directe.
	 * 
	 * certains pb de gestion de la GUI viennent du fait que des donn�es temporaires sont stock�es dans les classes de la GUI
	 * alors qu'elles seraient mieux control�es et int�gr�es si elles �taient du c�t� du jeu
	 * exemple: s�lection temporaire de joueurs lors de la configuration d'un joueur. le fait que ces donn�es soient dans la GUI
	 * pose des pb de mise � jour du jeu en r�seau, car on est alors oblig� de duppliquer les listes.
	 * >> r�forme � organiser conjointement � la mise en place des �v�nements
	 * 
	 * faut revoir la notion de profil:
	 * 	- connecter directement les stats
	 *  - introduire la distinction entre profil local et distant (fait)
	 *  
	 *  ca devrait pas �tre possible d'avoir un profil sans stats
	 *  toutes les stats devraient �tre centralis�es et charg�es quand n�cessaire
	 *  y compris pour les rencontres locales (qui peuvent �tre stock�es le temps d'avoir un acc�s r�seau)
	 *  
	 * en fait d�s le d�marrage, un process g�re les tentatives de connection sur le port configur�
	 * de cette mani�re, on peut toujours r�pondre aux tentatives de connection
	 * >> � voir si �a ne prend pas trop de ressources ?
	 * 
	 * propri�t� remote des joueurs : comment s'assurer qu'un joueur est bien identifi� ?
	 * >> le central enregistre l'id du dernier hote sur lequel le joueur s'est connect�
	 * 	  si un joueur essaie de se connecter � partir d'un h�te diff�rent, erreur et on demande au joueur de s'identifier
	 * >> mais en fait c'est le central qui controle la v�racit�, c'est lui qui d�tient le dernier hote de connection
	 *    donc on ne peut faire de partie enregistr�e qu'en passant par le central, basta. sinon c'est du hors-piste, de l'amical.
	 * 
	 * GUI: 
	 *  - dans les cas o� on a un panel affichant une liste et d'autres
	 *    panels affichant l'�l�ment s�lectionn�, il faudrait que ces derniers �coutent
	 *    la liste afin de se mettre � jour automatiquement quand n�cessaire.
	 *  - en fait on peut faire une version simple des souspanels, et une version
	 *    qui �coute le panel principal et h�rite de la version simple
	 *  - pour la configuration des parties, faut une structure g�n�r�e au niveau de ConfigurationXxxx
	 *    avec des sous classes pour le jeu en r�seau (une pour client, une pour serveur)
	 *    tout �a doit impl�menter tout ce qui est d�j� impl�ment� au niveau des panels, afin que la GUI
	 *    ne fasse plus aucun traitement, mais se contente de transmettre les commandes de l'utilisateur au moteur
	 */ 
	
	/**
	 * en cours:
	 *  - re-tester un cp la connection diff�r�e, on dirait que �a marche pas du 1er cp
	 *  - possibilit� de changer l'IP d'un serveur direct existant
	 *    >> � terminer, faut g�rer l'apparition du popup
	 *  - d�connection du serveur pdt s�lection joueurs : 
	 *  		- retour � l'�cran de s�lection des parties
	 *			- mise � jour de gameinfo dans la liste
	 *  - dans les configs de tournois enregistr�es et recharg�es par d�faut, il ne faut garder que les joueurs locaux
	 *  - ne plus permettre qu'un joueur n'appartienne pas au classement glicko2
	 *  - icones: 
	 *  	- pair = rond vide, central = rond plein
	 *  	- revoir les logos pour blocker/d�bloquer les joueurs. en plus �a a l'air invers� dans le jeu...
	 *  
	 */
	
	/**
	 * bugs r�seau
	 * 	- lancer serveur, client, d�connecter client : null exception cot� serveur
	 */
}
