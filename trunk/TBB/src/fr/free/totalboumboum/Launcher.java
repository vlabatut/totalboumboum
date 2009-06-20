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
	
// **********************************************************
// PERMANENT
// **********************************************************
	/*
	 * TODO PARAMETRES MEMOIRE
	 * -Xms32m -Xmx512m
	 */
	
	/* TODO conseils david :
	 * 	- champ final
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
	
// **********************************************************
// IA / TOURNOI
// **********************************************************
	
	/*
	 * TODO il faut récupérer toutes les exceptions éventuellement causées par les IA
	 * par exemple en les routant sur une fenêtre débug spéciale ?
	 * (cf OkanYuksel qui fait un dépassement de la pile)
	 */
	
	/*
	 * TODO il ne faut pas exécuter toutes les IA dans un thread pool, mais au contraire les séparer
	 * sinon, une seule IA bouclant à l'infini va consommer tous les threads dispo dans le treadpool
	 * vont être successivement monopolisés par l'IA
	 */
	
	/*
	 * TODO dans l'API il faut généraliser (tout en simplifiant) les propriétés des sprites, plutot que 
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
	 * TODO inclure dans l'API des fonction d'action prédéfinies.
	 * par exemple des fonctions pour le déplacement, on indique où on veut aller et ça détermine à chaque itération
	 * quelle est l'action à effectuer. étendre à d'autres actions ?
	 * limitation pédago: on n'utilise plus A*
	 */
	
// **********************************************************
// BOMBES
// **********************************************************

	/*
	 * TODO autoriser de poser une dernière bombe avant de mourir ? (i.e. dans le gesture burning) comme ds XBlast
	 */						
	
	/*
	 * TODO la durée d'explosion d'une bombe et la durée de burning des sprites doivent être 
	 * égales (?) ou au moins imposées
	 */
	
	/*
	 * TODO lorsqu'une bombe est touchée par une flamme, elle bloque la flamme.
	 * ce qu'on voit passer est la flamme résultant de l'explosion de la bombe.
	 */

	/*
	 * TODO le temps d'explosion d'une bombe est propre à la fois à la bombe
	 * (capacité) et au joueur (certains malus la modulent)
	 */
	
	/*
	 * TODO dans l'eventMgr de la bombe, quand elle oscille, il faut gérer les sprites
	 * qui sont en train de la pousser, et non pas en simple contact.
	 * de plus, il faut que les sprites aient le pouvoir de pousser, sinon ils ne comptent pas.
	 */

// **********************************************************
// COMMANDES
// **********************************************************

	/*
	 * TODO les touches doivent implémenter (toutes, et non pas seulement les directions)
	 * une option 'stop on release'. par exemple, si je reste appuyé sur
	 * jump, ça doit sauter dès que c'est possible, et ne s'arrêter que quand je
	 * relâche la touche.
	 * en fait non, c intéressant pour seulement certaines actions
	 */
	
	/*
	 * TODO lors de l'implémentation du jeu en réseau,
	 * une optimisation consiste à ne pas générer des évts par le controleur tant
	 * qu'une touche est appuyée, car ça va faire bcp de choses à transmettre,
	 * le controleur étant situé coté client.
	 * plutot, il faut les générer dans le controlManager, qui est coté serveur.
	 * et du cp aussi, il faut éviter d'utiliser ce mode de gestion des évènements
	 * pour les autres types d'evts (ie non-controles)
	 */
	
// **********************************************************
// FIRE
// **********************************************************
		
	/*
	 * TODO à faire sur le feu (long terme) :
	 * - décomposer l'explosion en :
	 * 		1)explode
	 *		2)stay still
	 *		3)implode
	 * - gérer le déplacement des flammes à la XBlast
	 */

	/*
	 * TODO explosion:
	 * lorsque la flamme est construite, on teste si chaque case la laisse passer
	 * mais il faut aussi le faire sur la case centrale (origine de l'explosion)
	 * car différents facteurs peuvent limiter l'explosion à une portée de 0
	 * (sans pour autant empêcher l'explosion) 
	 */
	
// **********************************************************
// GENERAL
// **********************************************************
	/*
	 * TODO
	 * renommer Bomber tout ce qui concerne Hero ?
	 */
	
	/*
	 * TODO getClass().getResource(IMAGE_DIR + fnm)
	 * permettrait de trouver un path de façon plus propre ? 
	 */    

	/*
	 * TODO dans l'EventManager, est-il vraiment nécessaire de synchroniser la méthode update ?
	 * car on appelle une méthode setGesture dans sprite : quel est le risque d'interblocage réel ?
	 * à généraliser à toutes les méthodes que j'ai synchronisées
	 */

	/* TODO lorsqu'un sprite est ended :
	 * + il faut virer le sprite partout où il est utilisé : gestionnaire de touches, etc. 
	 * (faire une recherche de référence à la classe hero et sprite)
	 * + si c'est un héros il faut faire péter toutes ses bombes et relâcher tous ses items
	 */	

	//TODO le saut ne doit pas être sensible à la vitesse du personnage	-> à voir !

	/*
	 * TODO plutot que d'utiliser des méthodes synchronisées : 
	 * utiliser une file synchronisée par définition ?
	 */

	/*
	 * TODO dans SBM2, il faut gérer :
	 * 	- les boss (dans les autres aussi, notamment SBM5)
	 * 	- les espèces de chars d'assaut présent dans certains niveaux (utilisés par les créatures)
	 */
	
	/*
	 * TODO il est possible que certaines bombes aient des animes différentes pour chaque direction
	 * conséquence : il faut toujours orienter un gesture, même si ça n'a pas vraiment de sens a priori.
	 * ex: bombe missile que l'on pose vers une direction, et qui part en roulant quand elle explose, détruisant le premier obstacle rencontré
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
	 * TODO décomposer, séparer chaque état dans une classe spécifique
	 * qui définit : 
	 * 	- les transitions : comment entre-t-on dans cet état, comment passe-t-on dans un autre état
	 * 	- les réactions : ou plutot les interactions, avec les autres sprites et l'environnement
	 * les managers, en particulier event et request, utiliseraient ces classes.
	 * chaque sprite serait caractérisé par l'utilisation d'un certain nombre de ces classes.
	 */
		
	/*
	 * TODO il faut mettre en place une mémorisation de la dernière action commandée qui n'a pas pu
	 * être exécutée par un sprite, et tenter de l'exécuter à chaque cycle.
	 * exemples : 
	 * 	- remote bombe devant exploser car l'owner est mort, mais ne pouvant pas car bouncing
	 * 	- item devant apparaître car le bloc a été détruit, mais ne pouvant pas car un joueur/bombe occupe la place
	 */
	
	/*
	 * TODO il va être nécessaire de définir un gesture indiquant l'absence d'un sprite relativement au jeu :
	 * 	- item pas encore découvert car le bloc le contenant n'a pas été détruit (équivalent à NONE ?)
	 * 	- item actuellement utilisé par un joueur (prop: USED)
	 */

	/*
	 * TODO les attributs XML respectent la convention java, et non pas XML (avec tirets)
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
	 * TODO sprite du bad bomberman dans SBM1
	 * éventuellement les autres persos du monde avec le ring
	 */
	
	/*
	 * TODO
	 * il faut mieux gérer l'accès aux méthodes, utiliser plus protected.
	 * surtout pour protéger les classes qui devraient être inaccessibles depuis l'IA
	 */

// **********************************************************
// ITEMS
// **********************************************************

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
	 * TODO gérer l'apparition des items à la suite d'une élimination,
	 * comme dans SBM1, avec des volutes de fumée
	 */
	
	/*
	 * TODO il y a aussi à gérer le déplacement des items lors d'une explosion
	 */
	
	/*
	 * TODO gérer une anime d'apparition de l'item
	 * voire aussi de disparition
	 */
	
// **********************************************************
// XML
// **********************************************************
	
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

	/* TODO il faut réformer les fichiers de propriétés :
	 * idée générale : 
	 * 	- elles différent d'un type d'objet à l'autre
	 *  - elles décrivent quels fichiers on doit trouver et où
	 * utilisation :
	 * 	1) charger les propriétés, permettant d'avoir un aperçu
	 *  2) éventuellement charger l'objet lui-même grâce aux propriétés 
	 */

	/*
	 * TODO lorsqu'on définit une animation ou une trajectoire qui est identique à une autre
	 * plutot que de faire un copier-coller, permettre de nommer l'animation d'origine.
	 * bien sur, il faudra vérifier que cette animation existe, comme pour celle par défaut. 
	 */
	
	/*
	 * TODO dans les fichiers XML, il faut préciser en attribut de la racine la version
	 * du fichier utilisée.
	 */
	
	/*
	 * TODO il faut reprendre les fichiers XML en centralisant au maximum les types
	 * par exemple y en a plein c des références vers des fichiers (attributs file et folder)
	 * ça permettrait de centraliser le code également dans le XMLTools
	 */
	
	/*
	 * TODO dans les fichiers XML, dans toutes les références aux XSD, remplacer
	 * les \ par des / : ça semble marcher tout aussi bien (du moins avec l'éditeur
	 * d'éclipse)
	 */

	// **********************************************************
	// TRAJECTORIES
	// **********************************************************
		
	/*
	 * TODO
	 * quand on pose deux bombes en diagonale et qu'on se place dans le cadrant intérieur d'une des cases libres du même carré
	 * on est bloqué. ce n'est pas vraiment un pb en fait, plus un feature. mais les non-initiés peuvent prendre ça pour un bug.
	 * (note : point mentionné dans le blog)
	 */

// **********************************************************
// THEMES/LEVELS
// **********************************************************
	
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
	
// **********************************************************
// ANIMES
// **********************************************************
		
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

// **********************************************************
// ABILITIES
// **********************************************************
	
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
	 * TODO dans les permissions, faut-il passer les forces à 0 et sans framing ?
	 * de plus, ne faudrait-il pas inclure les interdictions sous la forme de modulations
	 * avec framing et valeur négative ou nulle ? 
	 */

	/*
	 * TODO le système de gestion des actions est clairement à améliorer.
	 * certaines actions comme gather sont automatiques. certaines actions ont un effet de zone 
	 * (pr gather : la case).
	 */

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/*
	 * TODO BUGS EN COURS
	 *  - quand on balance une bombe, puis une autre, puis fait péter la première et enfin la seconde 
	 *  	juste à la fin des flammes de la première, alors l'explosion de la 2nde est circonsrite à la
	 *  	case où elle atterrit.
	 *  - l'item kick ne marche pas
	 *  - quand un mur est détruit (définitivement) par une penetration bomb, l'item n'apparait pas car
	 *  la flamme de la bombe dure plus longtemps que l'explosion du mur, et détruit l'item aussitôt qu'il apparait
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
	
	/*
	 * TODO
	 * 
	 * + beta.012
	 * - nouveau système de gestion des collisions dans le trajectory manager
	 * - correction du bug d'animation/collision : push ne se déclenche pas
	 * - correction du bug d'animation : quand le bonhomme va à droite et qu'on appuie sur gauche, y a une espèce de passage droite-gauche très rapide au niveau de l'animation, insuportable
	 * - mise à niveau du gestionnaire de trajectoire
	 *   
	 * - bug d'affichage quand les flammes se croisent au cours de plusieurs explosions: la dernière n'est pas affichée
	 * 
	 * permissions >> modulations
	 * 
	 * - lors du calcul des points, il faut forcer la présence d'un classement: ça facilite énormément de traitements en aval
	 *   au pire, si le classement est inutile (ex: simple total), on définit un classement-identité (pts utilisés pr le classement <=> pts marqués)
	 * - ça serait bien que les joueurs soient affichés dans l'ordre relatif aux points de la limite rencontrée
	 *   voire on définit explicitement un ordre d'affichage dans la compétition
	 * - faut afficher explicitement le classement à la fin d'une confrontation
	 * - dans les résultats :
	 * 		- afficher par défaut les 4 scores de base
	 * 		- plus les scores utilisés dans les points et/ou les limites
	 * 		- si les limites utilisent des points custom, les afficher aussi
	 * *******************************************************
	 * *********************** A FAIRE ***********************
	 * *******************************************************
	 * 
	 * - GUI : dans les tables, remplacer les labels par une classe custom qui implémenterait l'interface basicPanel
	 * 
	 * - pouvoir modifier l'UPS pour les IA (pour alléger le calcul)
	 * 
	 * - profils: simplifier, pas besoin de la double représentation default+selected.
	 * - réorganiser par rapport aux besoins: joueur pdt le jeu, joueur chargé en dehors du jeu, joueur pas chargé ?
	 * 
	 * - faire le classement lexicographique gérant les signes diacritiques partout où c'est nécessaire
	 * 
	 * - modifier le loader d'image de manière à ce qu'une image non-trouvée soit remplacée par la croix rouge
	 * 
	 * - utiliser les tooltips pour afficher les infos trop longues : calcul de points, nombre à virgule dans la colonne des points (décimales cachées), etc.
	 * - pb de dimension de l'image de fond en fonction de la résolution... (zones pas peintes)
	 * - results panel : quand il y a trop de rounds dans un match pour que ça rentre à l'écran, ne pas tout afficher
	 * - possibilité de donner des noms aux matches et aux rounds
	 * 
	 * - faire un paramètre dans les rounds qui permet de déterminer s'ils sont compatibles avec le tournoi 2007-08
	 * - tournoi : 1) on choisit les paramètres 2) on choisit les joueurs, le jeu restreint leur nombre pr qu'il soit compatible avec le tournoi, et restreint aussi les IA pour les mêmes raisons
	 * 
	 * - besoin d'une méthode permettant d'exporter un tournoi/match/round, ie de l'écrire entièrement en local (pas de référence à des composants existants)
	 * 
	 * - bug d'affichage dans les notes d'IA, les accents sont affichés avec la police par défaut
	 * 
	 * - stats : nombre de fois qu'un level a été joué
	 * - champ en plus dans les profils : le classement du joueur, nbre de rouds gagnés/nbre de rounds joués
	 * - dans les persos : % de rounds gagnés, ou bien nbre de rounds joués
	 * - tout ça est fait simplement en rajoutant les informations adéquates dans les classes de stat
	 * 
	 * - lors de la sélection des commandes :
	 * 		- cliquer sur le bouton d'action fait réagir quelque chose dans la ligne du joueur correspondant
	 * 		- permet de vérifier qu'on a pris les bonnes commandes (celles qu'on pensait avoir prises)
	 * 
	 * -------------------------------------------------------------------
	 * - calcul de points : introduire des variables comme le nombre de joueurs (pour définir un bonus pr le joueur qui fait un perfect en survival)
	 * - gérer le shrink
	 * - dans les autorisations, gérer l'apparition comme une action en soit. si pas possible d'apparaître au début de la partie, faire un atterrissage ?
	 * - à la fin du round, faire apparaitre les résultats par transparence
	 * 
	 * - pour painting, possibilité de définir quelles cases peuvent être repeintes, ce qui permet de poser comme limite un %age de cases repeintes
	 * - définir des noms "human readable" pour les items, histoire de ne pas afficher ces codes internes dans la GUI, en profiter pour introduire une decription, le tout en plusieurs langues. utiliser le code ISO comme paramètre de langue, et l'introduire dans le fichier de langue
	 * - possibilité de définir un nom pour tournament/match/round, qui sera affiche en titre de présentation/stats etc. si pas de nom, utilisation d'un nom générique (Round 1 - Prensentation) etc
	 * - faire un chargement ad hoc des matches, rounds, etc ? fusionner du coup HollowLevel et LevelPreview ? (voir si les objets de ces deux classes sont créés au même moment)
	 * 
	 * - redistribution des items lors de la mort d'un joueur (option de round?)
	 * - possibilité de bloquer certains items (on ne les perd pas lorsqu'on meurt)
	 * 
	 * LIMITES & MODES de jeu :
	 * - limites exprimées de façon relative (peindre 75% des cases, éliminer la moitié des joueurs...)
	 * - items: 1 item arrêtant la partie, 1 item faisant diminuer le temps restant (anti-temps)
	 * - possibilité de choisir entre le fait que le round s'arrête dès que tout le monde est mort sauf 1, ou dernière flamme terminée
	 * reformater les modes de jeu :
	 * 	- pour paint il suffit de définir des bombes spéciales qui peignent le sol
	 */
	
	
	
	
	
	
	
	
	/*
	 * TODO LAST
	 * 
	 * - voir si on peut mettre à jour le parser XML
	 * - définir la liste de gesture pour chaque type de sprite
	 * - pour chaque gesture, fixer les actions autorisées 
	 * - adapter tous les loaders au fait que les constantes exprimées en XML sont maintenant en majuscules, adapter aussi les fichiers xml
	 * 
	 * - forcément des trucs à changer sur les adapteurs d'IA, car ils attendent des permissions et pas des modulations. donc null (i.e. pas de modulation) n'a plus la même signification.
	 * 
	 * 
	 * y a manifestement une différence entre GeneralAction et les actions utilisées dans les Ability et Modulation
	 * dans ces dernières, on se place relativement au sprite, qui est actor target ou third.
	 * surement qqchose à faire de ce coté là
	 * 
	 * faut il utiliser getAbility pour récupérer les StateAbility, ou bien faut-il module ? (a priori: moduler)
	 * 
	 * réelle question avec les modulations :
	 * 	- il y a eu confusion sur les modulations d'état, qui étaient jusqu'alors interprétées comme des auto-modulation,
	 * alors qu'en réalité il s'agit de modulation sur des tiers. non ? 
	 * ou même les deux, par ex: un bloc qui est bas pr un gesture, et qui devient un obstacle pour un autre gesture.
	 * ou une bombe qui n'a pas toujours la même puissance.
	 * p-ê faut il donc définir deux types de StateModulation.
	 * de plus, la zone d'influence est importante : où le tiers doit il être situé pour intervenir?
	 * d'ailleurs c'est à rajouter aussi dans les modulations d'action.
	 * et ça pose un pb d'optimisation: pour l'instant on utilise un modèle pull, ce qui est OK dans l'hypothèse d'une modulation
	 * exclusivement locale (deux cases max: actor+target), mais si on passe en global ça va couter cher.
	 * d'où l'intérêt de p-ê passer à un modèle push, mais ça veut dire pas mal d'adaptation à gérer.
	 * il faudrait implémenter le système au niveau de la gestion des contacts
	 * et un évènement de collision/changement de case provoquerait un changement dans les modulations concernant le sprite
	 * p-ê qu'on peut mettre ce coté l'aspect global (pas de contact ni de case partagée) pour l'instant, et continuer avec le système
	 * déjà utilisé avec les acions (à savoir: scanner la case courante, voire les sprites en contact (note: la liste en est connue à chaque instant)
	 * idée pour centraliser le traitement en cas de portée sans limite:
	 * 	- dès qu'un sprite change de gesture, ses nouvelles modulations sont analysées
	 * 	- toutes celles qui sont sans limite de portée sont stockées dans un vecteur situé dans Level (et toutes celles de l'état précédent sont retirées de ce même vecteur)
	 * 	- lors de la validation de 3rdMod, ce vecteur est systématiquement testé en plus des sprites situés près de l'acteur et de la cible 
	 * 
	 * - il faut mutualiser tous les fichiers de description de sprites communs (style tous les blocs durs)
	 * 	- chargement plus rapide
	 * 	- moins d'occupation mémoire
	 * 	- fichiers moins chiants à gérer (moins de répétitions)
	 * - il faut mener une réflexion sur ce qu'il est vraiment nécessaire de cloner et ce qui peut être partagé, ce qui permettrait d'optimiser un peu plus l'utilisation de la mémoire
	 * 		- les actions générales utilisées pour les abilities doivent être copiées, car elles sont définies en termes de SELF et doivent donc être adapté au sprite concerné (de toute façon, ça c'est du spécifique à un sprite. pas la peine de copier, en fait)
	 * 
	 * - l'adaptation des fichiers de description de sprite XML à la mutualisation de ability et autres est simple: il suffit de remettre les éléments qui indiquaient où se situaient ces descriptions.
	 * faudrait tester si on peut utiliser des paths dans les includes XML sans perdre la portabilité.
	 * ou plus simplement, utiliser des mots clés comme "general" "specific"
	 * - l'autre aspect du problème est l'adaptation des classes, en particulier celles de chargement. les sprites susceptibles d'avoir des fichiers en commun doivent être chargés depuis la même classe.
	 * donc il va falloir intervenir sur la classe située entre thème et sprite (pour blocs, bombes, items...)
	 * pour les héros ça me semble inutile de se prendre la tête sur l'optimisation des animes, vu qu'elles ne sont chargées qu'une seule fois par joueur (à la différence des types de blocs, qui sont parfois nombreux)
	 */
}
