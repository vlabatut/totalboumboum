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
	 * 	- écriture courte du for pour iterator
	 */

	/*
	 * TODO règle pour les loaders :
	 * 	- l'objet à charger est créé dans la première méthode appelée lors du chargement
	 *  - les méthode secondaires reçoivent l'objet créé, et le complètent.
	 *  - quand des objets secondaires sont chargés indépendamment, ie dans des méthodes publiques, ils sont eux aussi créés dans la méthode appelée
	 */

	/* TODO mettre tout ce qui appelle du swing dans le thread adapté
 		SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	initGui();	
			}
		});
	 */

	/* TODO quand une classe est modifiée ou créée :
	 * il faut implémenter la fonction finish qui permet d'effacer toutes les références qu'elle utilisait
	 */
	
	/*
	 * TODO GUI
	 * Split : reçoit les paramètres mais ne les met pas en cache
	 * Menu  : met les originaux en cache, envoie une copie au data
	 * Data  : modifie les paramètres reçus. le menu se charge de les récupérer, les comparer et éventuellement les sauver
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// IA/TOURNAMENT	//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * TODO il faut récupérer toutes les exceptions éventuellement causées par les IA
	 * par exemple en les routant sur une fenêtre débug spéciale ?
	 * (cf OkanYuksel qui fait un dépassement de la pile)
	 */
	
	/*
	 * TODO dans l'API IA il faut généraliser (tout en simplifiant) les propriétés des sprites, plutot que 
	 * de définir des fonctions spécifiques à chaque fois.
	 */
	
	/*
	 * TODO type de tournoi plus adapté au réseau :
	 * des joueurs jouent un match pendant que d'autres attendent 
	 * à la fin du match, les n derniers joueurs sont relégués dans la salle d'attente
	 * n joueurs qui attendaient sont qualifiés.
	 * un classement permet de déterminer le leader provisoire
	 */
	
	/*
	 * TODO inclure dans l'API IA des fonctions d'action prédéfinies.
	 * par exemple des fonctions pour le déplacement, on indique où on veut aller et ça détermine à chaque itération
	 * quelle est l'action à effectuer. étendre à d'autres actions ?
	 * limitation pédago: on n'utilise plus A*
	 */
	
	/*
	 * TODO include dans l'API une fonction d'initialisation de l'IA, appelée lors de sa création
	 * faudrait passer en paramètre le niveau et des infos style l'instance, etc.
	 * ça permettrait par exemple, pour une IA qui apprend, de télécharger son fichier de sauvegarde
	 * (dans son package)
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// MULTITHREADING	//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO il ne faut pas exécuter toutes les IA dans un thread pool, mais au contraire les séparer
	 * sinon, une seule IA bouclant à l'infini va consommer tous les threads dispo dans le treadpool
	 * vont être successivement monopolisés par l'IA
	 */
	
	/*
	 * TODO une fois qu'on a déterminé les nombres de joueurs, y a moyen de gérer les threads de meilleure manière en :
	 * 		- créant un executor au niveau du tournoi
	 * 		- il doit contenir un pool de (nbre d'IA max pvant jouer à la fois)+1(pr loop)
	 * 		- par la suite, au lieu de créer un thread pour chaque ia ou pour le chargement/loop, on en demande un à l'executor
	 * http://java.sun.com/javase/6/docs/api/java/util/concurrent/ExecutorService.html
	 * http://java.sun.com/docs/books/tutorial/essential/concurrency/pools.html
	 * http://java.sun.com/docs/books/tutorial/essential/concurrency/interrupt.html
	 * >>> pb: si un thread entre en boucle infinie, il ne sera jamais terminé, donc l'executor devra créer/gérer un thread de plus
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// BOMBS		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO autoriser de poser une dernière bombe avant de mourir ? (i.e. dans le gesture burning) comme ds XBlast
	 */						
	
	/*
	 * TODO lorsqu'une bombe est touchée par une flamme, elle bloque la flamme.
	 * ce qu'on voit passer est la flamme résultant de l'explosion de la bombe.
	 */

	/*
	 * TODO dans l'eventMgr de la bombe, quand elle oscille, il faut gérer les sprites
	 * qui sont en train de la pousser, et non pas en simple contact.
	 * de plus, il faut que les sprites aient le pouvoir de pousser, sinon ils ne comptent pas.
	 */
	
	/*
	 * TODO
	 * quand des sprites de plusieurs cases seront définis, peut-être que les explosions
	 * devront être définies en tant que sprites (paramétrables pour leur taille) de ce type ?
	 * ce qui permettrait de les faire se déplacer.
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// CONTROLS		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO lors de l'implémentation du jeu en réseau,
	 * une optimisation consiste à ne pas générer des évts par le controleur tant
	 * qu'une touche est appuyée, car ça va faire bcp de choses à transmettre,
	 * le controleur étant situé coté client.
	 * plutot, il faut les générer dans le controlManager, qui est coté serveur.
	 * et du cp aussi, il faut éviter d'utiliser ce mode de gestion des évènements
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
	 * 	pr gérer le déplacement des flammes à la XBlast
	 * 
	 * ou alternativement :
	 * - décomposer le feu en appearing+standing+disapearing, 
	 * 	ce qui permettrait de le faire durer aussi longtemps qu'on veut
	 */

	/*
	 * TODO explosion:
	 * lorsque la flamme est construite, on teste si chaque case la laisse passer
	 * mais il faut aussi le faire sur la case centrale (origine de l'explosion)
	 * car différents facteurs peuvent limiter l'explosion à une portée de 0
	 * (sans pour autant empêcher l'explosion) 
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// GENERAL		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO getClass().getResource(IMAGE_DIR + fnm)
	 * permettrait de trouver un path de façon plus propre ? 
	 */    

	/*
	 * TODO dans SBM2, il faut gérer :
	 * 	- les boss (dans les autres aussi, notamment SBM5)
	 * 	- les espèces de chars d'assaut présent dans certains niveaux (utilisés par les créatures)
	 */
	
	/*
	 * TODO définir le shrink comme ça :
	 * - séquence de steps, chacun caractérisé par :
	 * - un délai à laisser écouler après le step précédent
	 * - la liste des lignes/tiles qui doivent apparaître, avec le type de bloc devant apparaître
	 */
	
	/*
	 * TODO pour le débug de certains points, il faudra mettre en place un controleur
	 * capable d'enregistrer une séquence de commandes, ce qui permettra de les rejouer
	 * par la suite (style playback) tout en controlant en temps réel un autre perso
	 */
	
	/*
	 * TODO animes/trajectoire : permettre de définir des labels dans la séquence
	 * et de faire des goto et des boucles (?)
	 */
	
	/*
	 * TODO dans l'avenir il serait p-ê nécessaire d'utiliser un actionManager,
	 * qui recevrait une action en paramètre, et l'exécuterait.
	 * intérêt : décomposition + utile pour un moteur qui serait seulement un player,
	 * et ne devrait donc pas gérer les tirages aléatoires, mais seulement l'application
	 * déterministe des actions (le tirage se ferait dans la fonction appelant l'actionMger,
	 * ie généralement : l'eventMgr)
	 */
	
	/*
	 * TODO
	 * il faut mieux gérer l'accès aux méthodes, utiliser plus protected.
	 * surtout pour protéger les classes qui devraient être inaccessibles depuis l'IA
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ITEMS		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO gérer le déplacement des items lors d'une explosion
	 */
	
	/*
	 * TODO
	 * item spécial permettant de manger les bombes des autres, comme certains ennemis du jeu original en mode histoire 
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// XML			//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/* 
	 * TODO à faire dans les XML : 
	 * 		- faire respecter au maximum les contraintes avec les schémas
	 * 		- ensuite seulement faire respecter les contraintes dans les loaders
	 * 		- dans les loaders, penser à gérer les valeurs/structures par défaut
	 * exemples :
	 * 		- pour les animes: les noms des gestures doivent être différents entre eux et différents de default
	 * 		- pour les thèmes : les noms des components doivent être différents
	 */	

    /* 
     * TODO pour forcer l'unicité d'un attribut/élément, voir ici : 
     * http://rangiroa.essi.fr/cours/internet/02-slides-xml/schema.htm
     */		

	// TODO dans schema de Level : contrainte d'unicité sur les positions des lignes et colonnes    

	/*
	 * TODO dans le fichier de trajectoire, tester que la valeur de durée de forçage 
	 * de position n'est pas supérieure à la durée de la trajectoire
	 * (et il y a surement de nombreux autres test de cohérence à effectuer)
	 */					

	/*
	 * TODO à faire dans le chargement des fichiers XML
	 * ANIME : 
	 * - vérifier que si on utilise une ombre, elle a bien été déclarée dans le fichier XML
	 * - vérifier que le mouvement par défaut existe bel et bien
	 * LEVEL :
	 * - matrice vide = que des floors
	 * - vérifier que quand on fait référence à un bloc par son nom, il existe bien dans le fichier du thème
	 */

	/*
	 * TODO lorsqu'on définit une animation ou une trajectoire qui est identique à une autre
	 * plutot que de faire un copier-coller, permettre de nommer l'animation d'origine.
	 * bien sur, il faudra vérifier que cette animation existe, comme pour celle par défaut. 
	 */
	
	/*
	 * TODO dans les fichiers XML, il faut préciser en attribut de la racine la version
	 * du fichier utilisée (i.e. version du schéma).
	 */
	
	/*
	 * TODO il faut reprendre les fichiers XML en centralisant au maximum les types
	 * par exemple y en a plein c des références vers des fichiers (attributs file et folder)
	 * ça permettrait de centraliser le code également dans le XMLTools
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
	 * dans les modulations third et other, la portée est limitée à la case (ou aux cases). 
	 * le contact et surtout le remote ne sont pas encore gérés.
	 * idée pour centraliser le traitement en cas de portée sans limite:
	 * 	- dès qu'un sprite change de gesture, ses nouvelles modulations sont analysées
	 * 	- toutes celles qui sont sans limite de portée sont stockées dans un vecteur situé dans Level 
	 * 		(et toutes celles de l'état précédent sont retirées de ce même vecteur)
	 * 	- lors de la validation de 3rdMod, ce vecteur est systématiquement testé 
	 * 		en plus des sprites situés près de l'acteur et de la cible 
	 */
	
	/*
	 * NOTE les modulations sont ordonnées par priorité dans le fichier XML.
	 * dans le cas où plusieurs modulations peuvent être appliquées à une action, 
	 * c'est la première définie dans le fichier XML qui est utilisée.
	 * il faut donc l'organiser du plus spécifique au plus général.
	*/

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// THEMES/LEVELS	//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * TODO dans le thème belt, le small pipe laisse passer les flames 
	 * (sans être détruit et sans protéger ce qui est sous lui)
	 */
	
	/*
	 * TODO rajouter des niveaux inédits en utilisant au maximum les parties inutilisées
	 * des autres thèmes
	 */
	
	/*
	 * TODO rajouter un niveau immeeeeeense (comme dans le gif animé sur le blog)
	 */
	
	/*
	 * TODO dans le level, il faut gérer la distribution des items :
	 * possibiliter de la paramétrer en fonction du nombre de joueurs ?
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ANIMES	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	/*
	 * TODO lorsque l'anime attendue est censée s'arrêter (pas de répétition)
	 * et que le moteur compte sur un evt de fin pour passer à l'anime suivante,
	 * il y a blocage si l'anime n'est pas définie, et que l'anime par défaut est
	 * répétée.
	 * exemple : défaut=marcher, et il manque punch -> le bonhomme ne sort jamais de l'état punching
	 * >>>>solution : ne jamais compter sur la fin de l'anime, toujours imposer une durée à respecter
	 * (ce qui permet d'uniformiser le beans pour tous les joueurs)
	 */
	
	/*
	 * TODO il faut sécuriser les animations : s'assurer qu'une anime censée
	 * ne pas être ré-init est bien gaulée. par exemple, pushing doit être gaulée
	 * comme walking (dans le XML) sinon p-ê pb (si pas même durée ou autres diff)
	 * > à vérif
	 */
	
	/*
	 * TODO pour Prognathe, faire une anime de début de partie : quand le sprite apparait
	 * idée : sortir du sol
	 * idée : le corps est déjà en place, tient la tête en l'air et la fixe sur ses épaules comme un pilote enfile son casque
	 * 
	 */
	
	/*
	 * TODO animes : permettre de définir plusieurs animes pour 1 gesture,
	 * avec choix aléatoire
	 *  
	 */
	
	/*
	 * TODO animes : il faut définir un système de substitution d'image
	 * de manière à ce qu'il soit impossible d'être pris en défaut
	 */
	
	/*
	 * TODO introduire une anime/action supplémentaire : en jeu, un joueur
	 * peut provoquer un autre joueur en déclenchant une animation spécifique.
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ABILITIES	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * TODO y a des trucs qui vont se recouper style :
	 * le bloc doit il empêcher le joueur de poser une bombe
	 * ou bien la bombe d'apparaître ?
	 * >>> un bombe peut apparaître pour autre chose qu'un drop (eg teleport)
	 */

	/*
	 * TODO
	 * le fait qu'il y ait collision ou pas peut changer en fonction de la direction
	 * de déplacement (puisque l'ablt à bloquer peut être configurée suivant la 
	 * direction). donc dans la gestion des collisions sur directions composées,
	 * il faut retester avec une requête si le fait de retirer une direction provoque
	 * toujours une collision ou pas.
	 */
	
	/*
	 * TODO certaines ablt sont liées à la trajectoire, par ex : puissance de jump
	 * influe sur la distance du saut.
	 * il faut donc en tenir comtpe dans la trajectoire, en proposant l'utilisation
	 * d'un paramètre externe (au fichier XML)
	 */
	
	/*
	 * TODO quand on calcule l'ablt composite, pour les directions composites :
	 * l'abilité renvoyée peut contenir une direction simplifiée, en fonction 
	 * des autorisations obtenues. 
	 */
	
	/*
	 * TODO pour bien faire, il faudrait lister pour chaque sprite (et chaque configuration)
	 * les actions et les états possibles. les permissions représentent les autorisations
	 * pour appliquer une action. il manque la définition des actions, indiquant dans quel
	 * état un objet se retrouve après avoir effectué/subi/empeché une action donnée (ce qui 
	 * dépend éventuellement de l'état précédent l'action). 
	 */
	
	/*
	 * TODO le système de gestion des actions est clairement à améliorer.
	 * certaines actions comme gather sont automatiques. certaines actions ont un effet de zone 
	 * (pr gather : la case).
	 */

	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// BUGS CONNUS	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	/*
	 * TODO quand on balance une bombe, puis une autre, puis fait péter la première et enfin la seconde 
	 *  juste à la fin des flammes de la première, alors l'explosion de la 2nde est circonsrite à la
	 *  case où elle atterrit.
	 *  
	 */
	
	/*  
	 *  TODO l'item kick ne marche pas
	 */
	
	/*  
	 *  TODO quand un mur est détruit (définitivement) par une penetration bomb, l'item n'apparait pas car
	 *  la flamme de la bombe dure plus longtemps que l'explosion du mur, et détruit l'item aussitôt qu'il apparait
	 *  >> je pense que ça a été résolu ça, à tester !
	 */
	
	/*
	 * TODO
	 * quand on pose deux bombes en diagonale et qu'on se place dans le cadrant intérieur d'une des cases libres du même carré
	 * on est bloqué. ce n'est pas vraiment un pb en fait, plus un feature :P . mais les non-initiés peuvent prendre ça pour un bug.
	 * (note : point mentionné dans le blog)
	 */
	
	/* TODO
	 * impossible de poser une bombe quand on est en train de buter contre un mur en direction upleft (et uniquement cette direction pr NES2) et downleft (pr SBM1)
	 * 		>> ca viendrait de swing ou du clavier (pb matériel ou bas niveau)
	 */
	
	/* TODO
	 * cas particulier : item apparaissant dans une explosion de bloc, avec un joueur déjà sur le bloc 
	 * (il a passe-muraille et résistance au feu) : l'item doit être ramassé dès qu'il a fini d'apparaitre, en théorie
	 * ou bien : un héros meurt, un item apparait sous les pieds d'un autre joueur immobile : l'item reste sans que le joueur ne se l'approprie
	 * >> en même temps, ça laisse le choix au joueur de ramasser l'item ou pas, c'est pas mal finalement (si c'est un malus par ex.)
	 */
	
	 /* TODO BUGS POTENTIELS (à vérifier)
	  * 
	  * - il semblerait que les directions dans la SpecificAction et dans la GeneralAction correspondante ne soient pas les mêmes...
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
	 * - correction d'un bug lors du chargement des animations mutualisées
	 * - modification d'un thème de NES-BM2 (thème battle, nouveau sol plus lisible, animations d'apparition)
	 * - correction dans les bombes de NES-BM2 (couleur du reflet de la bombe jaune)
	 * - le gesture ENDED n'est plus défini en XML, mais en dur car il est constant : pas d'animation, pas de trajectoire, modulations acteur/target toutes négatives, aucune autre modulation
	 * - pareil pour NONE, sauf que l'action APPEAR est autorisée (en tant qu'acteur)
	 * - introduction des gestures ENTERING et PREPARED, correspondant à APPEARING et STANDING, mais seulement utilisés pour le début du round
	 * - généralisation (aux autres sprites) de l'Ability permettant de contraindre le temps mis par les heros pour apparaitre au début de la partie.
	 * - modification dans le moteur : un sprite dans l'état NONE est quand même associé à une case, ce qui permet de simplifier le traitement des actions. par contre il n'a pas de position en pixels
	 * - correction d'un bug dans la gestion des animations et trajectoires proportionnelles
	 * - correction d'un bug dans la génération des explosions en croix (flamme trop longue)
	 * - définition d'un système permettant au niveau d'apparaître graduellement au début du round
	 * - simplification de l'initialisation des actions afin de ne plus avoir à spécifier une tile (le cas où la tile manquait a été supprimé)
	 * - apparition de messages au début du round (ready-set-go), avec graphismes basiques puis améliorés
	 * - mise en place d'un fond noir dans le loop panel avant que le niveau ne commence à apparaitre (juste avant le chargement)
	 * - possibilité de définir des niveaux contenant des bombes, soit directement sur le sol, soit dans des blocks
	 * - définition des animations d'apparition pour tous les thèmes de SBM1 et NBM2
	 * - définition d'un système de clignotement pour les sprites
	 * - définition d'un système aléatoire de définition des abilités associées à un item (pour les malus et les surprise)
	 * - implémentation de l'inversion des commandes
	 * - implémentation de la gestion des vies, généralisation du moteur pour permettre au joueur de revenir en jeu
	 * - implémentation des modificateurs de vitesse de déplacement au sol pour les héros
	 * - implémentation de la résistance au feu
	 * - nouveau système de maximum pour les StateAbilities (avec une ability supp contenant le suffixe _MAX)
	 * - correction d'un bug concernant la précédence temporelle de certains évènements, en particulier quand deux joueurs brûlent au cours de la même explosion
	 * - implémentation de la constipation bombique
	 * - correction d'un gros bug dans le calcul de distances entre sprites situés dans la classe Level
	 * - possibilité d'avoir plusieurs blocs, floors et items dans une même case (par souci de généralisation)
	 * - système permettant de relacher les items à la mort du joueur
	 * - correction dans le calcul de la direction entre deux sprites (le côté circulaire des niveaux n'était pas pris en compte)
	 * - système de contagion des malus
	 * - système de guérison des malus
	 * - possibilité de faire exploser les bombes posées par un joueur quand celui-ci brûle
	 * - touche END permettant de pauser le jeu
	 * - meilleure gestion de l'exultation/pleur à la fin des manches
	 * - capacité de résistance au feu pour les items (l'item est déplacé au lieu d'être détruit)
	 * - blocs laissant passer les flammes mais pas les joueurs ni les bombes (cf niveau story/custom de SBM1)
	 * - instance SBM1 plus complète (items, niveaux officiels, nouveaux niveaux custom)
	 * - meilleure gestion des items initiaux (effectivement distribués au start)
	 * - correction dans les gestion des blocs spawn
	 * - mise à jour et révision de la strucutre de données de l'API d'IA
	 * - implémentation de A* dans l'API d'IA
	 * - pause individuelle des IA
	 * - affichage de données relatives aux IA : chemins, vision de la zone de jeu, valeurs case par case, etc.
	 * - touche spéciale permettant d'exécuter seulement une itération du moteur
	 * - IA suiveuse : IA de démonstration qui choisit un autre joueur puis le suit
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// BUGS				//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* TODO
	 * 
	 * - deux persos entrant en même temps sur une case contenant un item le ramassent toutes les  deux (testé avec suiveur)
	 * 
	 * - SBM1: malus constipation semble rester même après la fin du clignotement
	 * 
	 * - GUI : les résultats ne s'affichent pas automatiquement quand on revient à l'écran du match
	 * 
	 * - GUI : édition de profil, quand on a fait "nouveau" ça n'active pas le bouton "modifier"
	 * 
	 * - cliquer sur partie rapide, annuler, cliquer sur tournoi, annuler, cliquer sur partie rapide >> bug
	 * 	 peut-être lié : cliquer sur partie rapide, maison, repartie rapide : rien d'affiché
	 * 
	 * - GUI : dans les tooltips du tableau des résultats, il manque les numéros des manches
	 *   
	 * - l'ombre des blocs de la dernière ligne est portée sur les blocs de la première ligne
	 * 
	 * - bug d'affichage dans les notes d'IA, les accents sont affichés avec la police par défaut
	 * 
	 * - bug de collision difficlement reproductible : le joueur va vers le haut, il a l'item pass-walls et il se retrouve 
	 *   téléporté en dehors du niveau (dans le noir) sur NBM2 custom battlezone.
	 *   	- c'est arrivé aussi sur la zone des marais de NBM2. 
	 *   	- ça semble se produire quand on se rapproche des limites (verticales?) du niveau
	 *   	  et on se retrouve téléporté en (0,0)
	 *   	- ça arrive aussi sur le niveau débug : quand l'ia suiveuse passe de l'autre coté, droite>gauche,
	 *   	  le perso se retrouve parfois téléporté en 0,0 aussi
	 *   	- pour le reproduire (à tester) : se déplacer horizontalement sur la dernière ligne du niveau en se faisant suivre par l'IA, 
	 *   	  de droite à gauche, et arrivé au coin bas gauche elle devrait disparaitre (p-ê faut-il être un peu à cheval avec la 1ère ligne)
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// A FAIRE MOTEUR	//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* TODO PRIORITÉ HAUTE
	 * 
	 * - nettoyer tous les tournois, rounds, etc, dans la version clean (et synchro qd nécessaire la version de travail)
	 * 
	 * - programmer l'ia suiveuse
	 * 
	 * - faut émettre un évt de sortie de case à la disparition d'un sprite (mort, masquage, etc)
	 * 
	 * - HIDING devrait être un gesture définit automatiquement, non ? pas d'image, sensible à rien, seule action autorisée=apparaitre...
	 * 
	 * - étudier le fonctionnement de ended (sprite) pr voir quand retirer le sprite de level/tile
	 * 
	 * - gérer le shrink
	 * 
	 * - remplacer "à vos marques" "pret" "boum!" par des graphismes précalculés
	 * 
	 * - à la fin de la partie, faire disparaitre tout le niveau comme il apparait au début
	 * 
	 * - faire un log automatique energistrant toutes les commandes et positions, histoire d'avoir une trace des bugs
	 * 
	 */
	
	/* TODO PRIORITÉ BASSE
	 * 
	 * - NBM2 : 
	 * 		- item veste ignifugée (souffle de l'explosion pas implémenté)
	 * 		  (s'apparente à un push sur un joueur adverse, intéressant pour les instances à venir)
	 * 
	 * - SBM1 : 
	 * 		- le délai de retardement ne doit pas être réinitialisé a la suite d'un punch
	 * 		  en fait ça reprend exactement où ça ne était au moment ou la bombe touche le sol
	 * 		- le clignotement d'invincibilité est sensé ralentir quand le temps est presque terminé
	 * 		- item clock : soit rajouter du temps normalement, soit faire un freeze très court?
	 * 		- spawn : faut décomposer en plusieurs abilities : délay, proba de spawn, nbre de spawn, etc
	 * 
	 * - Évènements :
	 * 		- envoyer un evt de changement de gesture (pour indiquer par ex que le sprite devient destructible, etc ?)
	 * 
	 * - Threads :
	 * 		- pouvoir modifier l'UPS pour les IA (pour alléger le calcul)
	 * 
	 * - Images :
	 * 		- modifier le loader d'image de manière à ce qu'une image non-trouvée soit remplacée par la croix rouge
	 * 
	 * - TournamentsMatches/Rounds :
	 * 		- possibilité de définir un nom pour tournament/match/round, 
	 * 		  qui sera affiche en titre de présentation/stats etc. 
	 * 		  si pas de nom, utilisation d'un nom générique (Round 1 - Prensentation) etc
	 * 		- faire un paramètre dans les rounds qui permet de déterminer s'ils sont compatibles avec le tournoi 2007-08
	 * 		- besoin d'une méthode permettant d'exporter un tournoi/match/round, 
	 * 		  ie de l'écrire entièrement en local (pas de référence à des composants existants)
	 * 
	 * - Items :
	 * 		- définir des noms "human readable" pour les items, histoire de ne pas afficher ces codes internes dans la GUI, en profiter pour introduire une decription, le tout en plusieurs langues. utiliser le code ISO comme paramètre de langue, et l'introduire dans le fichier de langue
	 * 
	 * - Stats : 
	 * 		- nombre de fois qu'un level a été joué
	 * 
	 * - Points : 
	 * 		- calcul : introduire des variables comme le nombre de joueurs 
	 *  	  (pour définir un bonus pr le joueur qui fait un perfect en survival)
	 * 		- lors du calcul des points, il faut forcer la présence d'un classement: 
	 * 		  ça facilite énormément de traitements en aval
	 *   	  au pire, si le classement est inutile (ex: simple total), on définit un classement-identité 
	 *   	  (pts utilisés pr le classement <=> pts marqués)
	 *
	 * - Limites/Modes de jeu :
	 * 		- limites exprimées de façon relative (peindre 75% des cases, éliminer la moitié des joueurs...)
	 * 		- items: 1 item arrêtant la partie, 1 item faisant diminuer le temps restant (anti-temps)
	 * 		- possibilité de choisir entre le fait que le round s'arrête dès que tout le monde est mort 
	 * 		  sauf 1, ou dernière flamme terminée
	 * 		- reformater les modes de jeu : pour paint il suffit de définir des bombes spéciales qui peignent le sol
	 * 	 	- pour painting, possibilité de définir quelles cases peuvent être repeintes, 
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
	 * 		- et les gestures définis doivent au moins contenir les nécessaires, et au plus les autorisés
	 *  	  il va falloir utiliser XSD 1.1 quand ça sera possible, avec les assertions et associations, cf l'email.
	 * 
	 * - Actions/Abilities/Modulations
	 *  	- vérifier qu'avant d'exécuter une action, on vérifie si le sprite concerné (actor) possède bien l'ability (avec modulate)
	 * 		- pour chaque gesture, fixer les actions autorisées 
	 * 		- ça ne me plait pas beaucoup ces actions bidons pour tester les abilities de certains sprites. faut réfléchir à un truc plus propre
	 * 		- il faudrait documenter le comportement par défaut du moteur, i.e. pour chaque type de sprite:
	 * 			- qu'est-ce qu'il peut faire comme action? quelles sont les transitions? qu'est-ce qui est interdit ?
	 * 			- ça permettra de savoir ce qui peut être modulé et ce qui ne peut pas l'être
	 * 		- un sprite n'est a priori pas un obstacle, tout est géré par modulation (y compris pour le feu)
	 * 		- le coup de l'indestructibilité des items (le fait de réapparaitre ailleurs) pourrait être étendue à tous les sprites
	 * 		  (en particulier les joueurs, ça serait un cas spécial de résistance au feu un peu pénalisante, utilisable un nombre limité de fois, par ex !)
	 * 		- ça serait bien que les paramètres numériques des abilités puissent être définies 
	 * 		  au chargement (force, durée, utilisation) de façon aléatoire, en fonction de certains paramètres. 
	 * 		  on pourrait par ex utiliser une liste de valeur numériques : 1=déterministe, 2=bornes pour un tirage au sort
	 * 		  utile par exemple pour déterminer le nombre de fois qu'un bloc repousse (plutot que de le fixer dans le fichier XML), 
	 * 		  ou la panne d'une bombe (plutot que de le faire pdt le jeu, et sans utiliser d'ablts spéciales)
	 * 
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// A FAIRE GUI		//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* TODO
	 * 
	 * - GUI : dans les tables, remplacer les labels par une classe custom qui implémenterait l'interface basicPanel
	 * 
	 * - profils: 
	 * 		- simplifier, pas besoin de la double représentation default+selected.
	 * 		- réorganiser par rapport aux besoins: joueur pdt le jeu, joueur chargé en dehors du jeu, joueur pas chargé ?
	 * 
	 * - faire le classement lexicographique gérant les signes diacritiques partout où c'est nécessaire
	 * 
	 * - utiliser les tooltips pour afficher les infos trop longues : calcul de points, nombre à virgule dans la colonne des points (décimales cachées), etc.
	 * 
	 * - pb de dimension de l'image de fond en fonction de la résolution... (zones pas peintes)
	 * 
	 * - results panel : 
	 * 		- faut afficher explicitement le classement à la fin d'une confrontation
	 * 		- quand il y a trop de rounds dans un match pour que ça rentre à l'écran, ne pas tout afficher
	 * 		- ça serait bien que les joueurs soient affichés dans l'ordre relatif aux points de la limite rencontrée
	 *   	  voire on définit explicitement un ordre d'affichage dans la compétition
	 * 		- dans les résultats :
	 * 			- afficher par défaut les 4 scores de base
	 * 			- plus les scores utilisés dans les points et/ou les limites
	 * 			- si les limites utilisent des points custom, les afficher aussi
	 * 		- à la fin du round, faire apparaitre les résultats par transparence
	 * 
	 * - tournoi : 1) on choisit les paramètres 2) on choisit les joueurs, le jeu restreint leur nombre pr qu'il soit compatible avec le tournoi, et restreint aussi les IA pour les mêmes raisons
	 * 
	 * - champ en plus 
	 * 		- dans les profils : le classement du joueur, nbre de rouds gagnés/nbre de rounds joués
	 * 		- dans les persos : % de rounds gagnés, ou bien nbre de rounds joués
	 * 		- tout ça est fait simplement en rajoutant les informations adéquates dans les classes de stat
	 * 
	 * - lors de la sélection des commandes :
	 * 		- cliquer sur le bouton d'action fait réagir quelque chose dans la ligne du joueur correspondant
	 * 		- permet de vérifier qu'on a pris les bonnes commandes (celles qu'on pensait avoir prises)
	 * 
	 * - il faudrait séparer les joueurs IA et les joueurs humain dans leur gestion.
	 * 		ca permettrait de sélectionner directement l'IA, au lieu du joueur, et donc de ne pas avoir à créer plusieurs
	 * 		joueurs avec la même IA quand on veut jouer contre plusieurs versions de la même IA.
	 * 		voire limiter le nombre de joueurs pour une IA à 1 seul, mais sélectionnable plusieurs fois ?
	 *		 à voir...
	 * 
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// A FAIRE SITE		//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * - possibilité de définir des items indestructibles (sont juste déplacés)
	 * - blocs laissant passer le feu mais pas les joueurs, comme dans le niveau story/custom de SBM1
	 * - parler des nouveaux outils de dev de l'IA : API améliorée, visualisation des couleurs, texte, et chemin
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//EN COURS			//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * - tester la fonction de speed (hero)
	 * - tester la fonction traversable (tile)
	 * - tester la fonction update abilities (hero)
	 * 
	 * - quand l'ia meurt et revient grace à une vie, elle n'a plus le control du personnage
	 * - bug : j'ai vu l'ia prendre une vie, mourir, relacher la vie, et revenir. 
	 * 	 et la vie était foncionnelle : je l'ai prise, je suis mort, je suis revenu (sans la relacher cette fois)
	 * - optimisation de l'API IA:
	 * 		- les IA ont-elles vraiment besoin d'être raffraichies si souvent ? >> non! réduire le taux de raffraichissement
	 * 
	 * - tester si les chemins de l'ia traversent le feu quand elle est invulnérable
	 * - tester si les chemins de l'ia traversent les murs quand elle peut passer à travers
	 * - changement incessant de chemin target alors qu'il n'y a pas d'obstacles
	 * - bug : l'IA essaie parfois de passer à travers les murs (pb dans AiBlock ou AiTile) 
	 */
}
