package org.totalboumboum;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
	 * sous Linux, pour eviter les problemes avec le garbage collector :
	 * -XX:-UseConcMarkSweepGC
	 */
	
	/* TODO conseils david :
	 * 	- champ final pr forcer l'initialisation
	 * 	- écriture courte du for pour iterator
	 */

	/*
	 * TODO règle pour les loaders :
	 * 	- l'objet à charger est créé dans la première méthode appelée lors du chargement
	 *  - les méthode secondaires reçoivent l'objet créé, et le complètent.
	 *  - quand des objets secondaires sont chargés indépendamment, ie dans des méthodes publiques, 
	 *    ils sont eux aussi créés dans la méthode appelée
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
	 * TODO éléments de la GUI
	 * Split : reçoit les paramètres mais ne les met pas en cache
	 * Menu  : met les originaux en cache, envoie une copie au data
	 * Data  : modifie les paramètres reçus. le menu se charge de les récupérer, les comparer et éventuellement les sauver
	 */
	
	/*
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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ARTIFICIAL INTELLIGENCE / TOURNAMENT	/ INTELLIGENCE ARTIFICIELLE / TOURNOI	//////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * TODO dans l'API IA il faut généraliser (tout en simplifiant) les propriétés des sprites, plutot que 
	 * de définir des fonctions spécifiques à chaque fois.
	 * ex : accéder aux propriétés via une fonction générique prenant le nom de la propriété en paramètre
	 */
	
	/*
	 * TODO include dans l'API une fonction d'initialisation de l'IA, appelée lors de sa création
	 * faudrait passer en paramètre le niveau et des infos style l'instance, etc.
	 * ça permettrait par exemple, pour une IA qui apprend, de télécharger son fichier de sauvegarde
	 * (dans son package)
	 */
	
	/*
	 * TODO evaluation du projet : pour la qualification, utiliser 
	 * le temps nécessaire pour éliminer un/des adversaire(s) de référence
	 */
	
	/*
	 * TODO
	 * plutot que de développer toute une API pour l'IA, il faudrait réutiliser les classes du jeu:
	 * 	- faire une copie des données à chaque itération, pour des raisons de synchro
	 *  - on suppose qu'une copie n'est pas modifiée, donc on peut ne la calculer qu'une seule fois pour chaque IA
	 *  - il faut définir des classes d'interface, des wrappers même, permettant aux IA d'accéder seulement aux méthodes autorisées
	 *  - pour les pb de synchro quand on veut savoir les propriétés du feu qu'une bombe générerait, il suffit d'en construire un (feu) 
	 *    et de le garder en cache le temps de la partie (en le màj si besoin de temps en temps)
	 *  >> intérêt : pouvoir utiliser exactement les mêmes fonctions que dans le jeu
	 *  (en fait, ça consiste à faire plus ou moins ce qui est déjà fait, sauf qu'au lieu de recopier les données dans des
	 *  classes différentes, on les copies telles-quelles et on définit des classes wrappers
	 */

	/*
	 * TODO
	 * quand on interrompt un round,
	 * les points sont calculés normalement pr les IA
	 * et les joueurs humains sont tous ex-aequo derniers
	 * (voire tout le monde ex-aequo?)
	 */
	
	/*
	 * TODO 
	 *  - utiliser le calcul d'occupation CPU pour limiter les IA trop gourmandes
	 *  - utiliser la limite sur yield déjà définie (cf classe config d'ia)
	 *  - calculer un temps d'occupation "normal" (avant le jeu) et s'en servir de réf pour le temps en cours de jeu
	 */
	
	/*
	 * TODO arrêter les threads dont les ia ont été éliminées du jeu
	 */
	
	/*
	 * TODO bombe menaçant les agents
	 * poser une bombe de plus en plus forte, au lieu de faire du hasard
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// MULTITHREADING / MULTIPROCESSUS	//////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO il ne faut pas exécuter toutes les IA dans un thread pool, mais au contraire les séparer
	 * sinon, une seule IA bouclant à l'infini va monopoliser tous les threads dispo dans le treadpool
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
// BOMBS / BOMBES	//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO autoriser de poser une dernière bombe avant de mourir ? (i.e. dans le gesture burning) comme ds XBlast
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
// CONTROLS / COMMANDES		//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// EXPLOSION /  FIRE / FEU	//////////////////////////////////////////////////////////////////////////////////////////////////
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
// ORIGINAL VERSIONS RIP	//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO
	 * il est possib que les ombres de Shirobon SBM2 soient à décaler un peu vers le bas, 
	 * à voir comment ça rend dans le jeu
	 */
	
	/*
	 * TODO faire le Dr. Mook de SBM1 (commande le boss du monde 3+transition3>4+boss 5+transition5>6+bosses6&7+fin)
	 */
	
	/*
	 * TODO apparemment les blocs qui tombent lors du shrink sont différents, au moins dans SBM1 & SBM2 
	 *	>> à ripper et vérifier pr les autres versions
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// GENERAL / GÉNÉRAL	//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO getClass().getResource(IMAGE_DIR + fnm)
	 * permettrait de trouver un path de façon plus propre ? 
	 */    

	/*
	 * TODO dans SBM2, il faut gérer :
	 * 	- les boss (dans les autres aussi, notamment SBM5)
	 * 	- les espèces de chars d'assaut présents dans certains niveaux (utilisés par les créatures)
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
	 * TODO
	 * il faut mieux gérer l'accès aux méthodes, utiliser plus protected.
	 * surtout pour protéger les classes qui devraient être inaccessibles depuis l'IA
	 */
	
	/*
	 * TODO
	 * est-il vraiment nécessaire de gérer les floors en tant que sprites à part entière, quand on peut y mettre plusieurs blocs?
	 * on pourrait carrément supprimer ce type de sprite et afficher l'image de base, sans test.
	 * les sols de type ralentissement ou accélération peuvent être inclus sous forme de blocks spéciaux.
	 */
	
	/*
	 * TODO quand on tente de poser une bombe, le sprite est créé et on tente de voir s'il peut apparaître
	 * question : est-il automatiquement inséré dans le Level à la création ? 
	 * >> si oui, y en a surement un paillat au bout d'un moment;
	 * 	  vu qu'en cas d'apparition impossible la bombe n'est pas retirée du Level (supposition)
	 */
	
	/*
	 * TODO lors du chargement des sprites, ne charger que le nécessaire :
	 * 	- blocks/floors
	 * 	- items
	 * 	- fires
	 * 	- heroes gestures (?)
	 */
	
	/*
	 * TODO replay
	 * 	- ajouter des commandes (avance rapide, vitesse, retour arrière)
	 *  - enregistrer une image au pif en cours de jeu, histoire de l'enregistrer en tant que preview
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// GUI	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/* TODO la communication [moteur >> gui] devrait se faire par evts, y compris durant le jeu
	 * >> ca permettrait de ne pas raffraichir pour rien lors de l'affichage de toutes les structures du jeu
	 * >> faut définir un système d'évènement pour toute classe affichée directement par un composant graphique
	 * >> ça permetrait de ne pas avoir à recréer les écrans. par ex, dans les options : si qqch est changé, c'est automatiquement màj en background
	 * 		- sauf qu'en fait c'est un mvais ex car dans les options ce sont des clones qui sont utilisés
	 *        mais il suffirait de ne pas recréer à chaque fois le panel, plutot de réutiliser le précédent en faisant un set et en affectant le listener
	 *      - faudrait du cp faire attention à la gestion de la mémoire : 
	 *      	- les objets finished devraient se débarasser de leurs listeners
	 *      	- en réaction, les listeners mettent leur référence à null et n'affichent rien >> bon ça
	 *      >> ça va poser des pb de gestion de la mémoire (?)
	 * >>> à faire plus tard...
	 */
	
	/*
	 * TODO réforme GUI
	 * 	- certains pb de gestion de la GUI viennent du fait que des données temporaires sont stockées dans les classes de la GUI
	 * 	  alors qu'elles seraient mieux controlées et intégrées si elles étaient du côté du jeu
	 * 	  exemple: sélection temporaire de joueurs lors de la configuration d'un joueur. le fait que ces données soient dans la GUI
	 * 	  pose des pb de mise à jour du jeu en réseau, car on est alors obligé de duppliquer les listes.
	 * 	  >> réforme à organiser conjointement à la mise en place des évènements
	 * 	- GUI: 
	 *  	- dans les cas où on a un panel affichant une liste et d'autres
	 *    	  panels affichant l'élément sélectionné, il faudrait que ces derniers écoutent
	 *    	  la liste afin de se mettre à jour automatiquement quand nécessaire.
	 *  	- en fait on peut faire une version simple des souspanels, et une version
	 *    	  qui écoute le panel principal et hérite de la version simple
	 *  	- pour la configuration des parties, faut une structure générée au niveau de ConfigurationXxxx
	 *    	  avec des sous classes pour le jeu en réseau (une pour client, une pour serveur)
	 *    	  tout ça doit implémenter tout ce qui est déjà implémenté au niveau des panels, afin que la GUI
	 *    	  ne fasse plus aucun traitement, mais se contente de transmettre les commandes de l'utilisateur au moteur
	 *  - remarques : 
	 *  	- l'intégralité du traitement devrait être effectué côté moteur
	 *    	  la gui ne devrait fonctionner que par requête/réponses synchrones (pour le local)
	 *    	  ou asynchrones (pour le réseau). 
	 *    	  voire asynchrones pour les deux, avec un système d'évènements...
	 *  	- faire un système par délégation permettant d'associer un gestionnaire de listeners/evts
	 *    	  à tout objet du moteur. chaque composant graphique doit simplement implémenter une interface,
	 *    	  ce qui simplifie le beans. bien sur, chaque objet émetteur doit du cp s'identifier pr chaque evt (source)
	 *    	  et les fonctions d'écoute seront un peu plus longue coté gui dans le cas de classes écoutant
	 *    	  plusieurs éléments du moteur.
	 */

	/* 
	 * TODO règles générales
	 * 	- une demande d'info auprès du serveur (voire client) ne doit pas être
	 *    réalisée tant que la GUI n'est pas prête à traiter l'évènement de lecture associé
	 *  - les méthodes des connexions générales susceptibles d'être appelées par les connexions
	 *    individuelles doivent être synchro, afin d'éviter par ex que plusieurs clients
	 *    ne demandent la même tâche en même temps, risquant une interférence
	 */
	
	/* TODO dans la gui, faudrait gérer le texte comme les images :
	 * le même texte peut servir sur plusieurs éléments.
	 * ça économiserait du traitement et de la place dans les fichiers de traduction,
	 * qui seraient plus simples à maintenir.
	 * et en plus on pourrait garder des noms d'éléments correspondant aux composants GUI,
	 * sans se soucier de leur signification réelle (contenu textuel).
	 * du cp, les clés du texte pourraient être plus explicites (car indépendantes)
	 */ 
	
	/*
	 * TODO renouvellement du menu principal
	 * remplacer le menu actuel par des icones dont la taille représente l'importance
	 * à gauche de l'icone, mettre le texte actuellement prèsent dans les boutons
	 * il est aligné à droite et sa taille est proportionnelle à celle des boutons
	 * 	- options       : engrenages
	 * 	- profils       : une des icones de profil/joueur/etc
	 * 	- stats         : icone stat utilisée en cours de jeu
	 *  - ressources    : icone package
	 *  - tournoi	    : coupe
	 *  - partie rapide : éclair du bouton play
	 *  - charger       : disquette avec flâche (inverse de sauvegarde)
	 *  - revoir        : une télé ? un oeil ? une pellicule?
	 *  - à propos      : un "?"
	 *  - quitter       : une croix (comme pour fermer une fenêtre) ou l'icone "quitter" du tournoi (?)
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// NETWORK - RÉSEAU		//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	/*
	 * TODO à tester :
	 *    - à la fin d'un round, le client ne sort pas de Loop
	 *    - changement effectif d'état entre les différents types de browsing pour le client
	 *    		>> besoin de voir ça sur un tournoi complet
	 *    - tester le mode partie rapide
	 *    - le serveur change bien d'état à chaque modification de la partie
	 */
	
	/* TODO problèmes :
	 *    - quand un client sélectionne des commandes : c'est pas conservé quand le serveur valide la partie
	 *    - impossible de bouger le perso côté client
	 *    - jeu saccadû côté client même en LAN >> faire de l'évènementiel et pas du cosmétique
	 *    - parfois une exception se produit liée à GregorianCalendar
	 */
	
	/*    
	 * TODO à faire :
	 *  - la deconnection en cours de partie (et dc de jeu) doit être traitée pour éviter le blocage prolongé du serveur, à toutes les phases de la gui/du jeu
	 *  
	 *  - tout changement d'état du client devrait être :
	 *  	- une requête envoyée au serveur
	 *  	- si celui-ci valide, alors les modifs nécessaires sont appliquées coté client
	 *  	>> cf le passage de game selection à players selection
	 *  	>> pas tout changement en fait, par exemple l'exit de ce même état est décidé unilatéralement
	 *  - déterminer les ressources à synchroniser et... le faire rigoureusement
	 *  - penser à tester systématiquement si l'état du c/s qui reçoit un evt est compatible avec le traitement de cet evt 
	 *  - tout evt transmis à la connection générale par l'indiv doit identifier l'indiv
	 *    car pr client, il y a une différence, qui doit être traitée, entre recevoir un evt donné
	 *    d'une connection active ou d'une autre connection.
	 *    plus généralement, le traitement dépend de l'état du client pour le serveur correspondant
	 *  - quand la partie commence, faudrait :
	 *  	- fermer toutes les connections inutiles coté client (i.e. autres serveurs)
	 *  	- p-ê mettre les connections concernées par la partie dans une liste spéciale, côté serveur (pr optimiser les temps de transmission)
	 */
	
	/* 
	 * TODO
	 *  - reconnection process:
	 * 		- client connects and sends a REQUEST_RECONNECTION message, with its id
	 * 		- server checks if the id's
	 * 		- sends back an ANSWER_RECONNECTION message with a boolean showing acceptation or reject
	 *  	- if accepted, the server then sends the necessary updates to the client
	 *  - ça serait bien d'avoir un icone spécial (variante de remote) pr indiquer dans les menus qu'un joueur est déconnecté
	 *  - lié à la reconnection : possibilité de définir des open slots (pr server)
	 *    et par la suite, des joueurs désirant prendre la partie en cours peuvent le faire
	 *    (similaire à un joueur déconnecté qui aurait son slot réservé et pourrait s'y reconnecter)
	 *  - gestion de la dé/re-connection :
	 * 		- la connection individuelle est conservée côté serveur,
	 * 	  	  avec un état DISCONNECTED ou autre
	 * 		- même les threads sont gardés, mais du cp le code doit être modifié
	 * 	  	  pour ne pas tenter d'écrire/lire si la connection est morte
	 * 		- quand le client tente de se reconnecter, si son id correspond à 
	 * 	   	  celle d'une connection DISCONNECTED, alors on se contente de réinitialiser les 
	 * 	  	  streams dans le writer et le reader, et ça roule.
	 */
	
	/* 
	 * TODO réseau:
	 * - pb quand c'est un client qui clique sur le round en 1er: si sélection aléatoire, 
	 * 		alors le niveau est différent de celui du serveur!
	 * - en fait, les clients ne devraient pas pouvoir avancer dans le tournoi avant le serveur
	 * - faut un message indiquant qu'on attend les autres joueurs (et lesquels)
	 */
	
	/* 
	 * TODO y a surement un bug pr rezo+simulation (côté client)
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
	
	/* TODO faut transmettre les abilities/modulations graphiques, elles sont nécessaires
	 *	>> faut donc un modulation manager, et un ability manager (?)
	 *	>> coute cher... autre solution ?
	 * pb pr les IA remote : y a besoin de l'état réel du niveau, pas seulement des graphismes
	 * solution : mise à jour locale (pas comme replay) et tant pis pr la puissance perdue et la possible dé-synchro
	 */
	
	/*
	 * TODO certaines abilities ont un effet graphique, comme twinkle ou flat
	 *    > faudra penser à un système pour transmettre les changement les concernant via le réseau
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// MULTIPLAYER / MULTIJOUEUR	//////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * TODO
	 * y a un mode de tournoi plus adapté pour le multijoueur, défini chaiplus trop où (mails?)
	 * avec une espèce de tournante : n joueurs, matches de k joueurs, les n-k derniers sont remplacés
	 * par ceux qui attendent
	 * ----------
	 * TODO type de tournoi plus adapté au réseau : hot potatoe
	 * des joueurs jouent un match pendant que d'autres attendent 
	 * à la fin du match, les n derniers joueurs sont relégués dans la salle d'attente
	 * n joueurs qui attendaient sont qualifiés.
	 * un classement permet de déterminer le leader provisoire
	 */
	
	
	/*
	 * TODO
	 * - pour faciliter la communication lors des parties réseaux :
	 *   systèmes de bulles pour communiquer. le joueur affecte un message
	 *   à une touche, et quand il appuie dessus, une bulle apparait à cété
	 *   de son joueur pour signifier ce message aux autres joueurs (vraisemblablement
	 *   distants)
	 * - p-ê que pour rendre ça plus facile à utiliser, faudrait prédéfinir
	 *   un certain nombre de messages et permettre au joueur d'en définir certains custom
	 * - donner aussi la possibilité de cibler des joueurs en particulier
	 * - exemple d'utilisation :
	 * 		- appuyer sur la touche du joueur ciblé
	 * 		- appuyer sur la touche de message
	 * 		- appuyer sur la touche du joueur mentionné dans le message
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// POINTS		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * TODO
	 * possibilité de faire varier le calcul des points
	 * en fonction de la position de départ des joueurs : 
	 * certaines positions sont plus difficiles que d'autres 
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ITEMS		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO gérer le déplacement des items lors d'une explosion
	 */
	
	/*
	 * TODO
	 * - item spécial permettant de manger les bombes des autres, comme certains ennemis du jeu original en mode histoire
	 * - item spécial qui, quand un héros meurt, déclenche automatiquement une explosion, éventuellement avec un effet de zone  
	 */
	
	/*
	 *  TODO malus pour les bombes :
	 *  - portée aléatoire : on sait pas à quelle distance ça va péter
	 *  - portée nulle : permet quand même de bloquer les explosions des autres, donc aspect défensif important 
	 */
	
	/*
	 * TODO grâce au système de modulation:
	 * 	 - mur ne se laissant traverser que par des joueurs ayant au moins 5 bombes
	 *   - condition sur d'autres abilities? 
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
	 * TODO les attributs XML doivent respecter la convention java, et non pas XML (avec tirets)
	 */
	
	/*
	 * TODO
	 * dans les fichiers XML animes et trajectoires : pr chaque dir/gesture,
	 * possibilité (sous forme d'attribut) de redéfinir ou compléter
	 * l'élément déjà défini dans un éventuel sprite abstrait. bien sur,
	 * si pas déjà défini on fait un redefine pr défaut
	 */
	
	/*
	 *  TODO dans les fichiers xml, gérer les images de façon unique, ce qui permettrait de définir des transformations (flips, rotations)
	 *	>> définir des borders comme dans SBM sans avoir à tout générer à la main (pr blocs symétriques)
	 *	>> temps de chargement plus rapide (en supposant que la transfo est plus rapide que le re-chargement)
	 */
	
	/*
	 * TODO utiliser des chemins relatifs à la racine du sprite éviterait 
	 * toutes les complications liées au nommage d'images/ombres
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// TRAJECTORIES / TRAJECTOIRES	//////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// MODULATIONS	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* 
	 * TODO
	 * dans les modulations third et other, la portée est limitée à la case (ou aux cases). 
	 * le contact et surtout le remote ne sont pas encore gérés.
	 * idée pour centraliser le traitement en cas de portée sans limite:
	 * 	- Dès qu'un sprite change de gesture, ses nouvelles modulations sont analysées
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
// THEMES / LEVELS / NIVEAUX	//////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * TODO rajouter des niveaux inédits en utilisant au maximum les parties inutilisées
	 * des autres thèmes
	 */
	
	/*
	 * TODO dans le level, il faut gérer la distribution des items :
	 * possibiliter de la paramètrer en fonction du nombre de joueurs ?
	 */
	
	/*
	 *  TODO dans belts, y a un bidule qui passe sur les rails sur les côtés...
	 */
	
	/*
	 * TODO faire une appli propre pour générer les niveaux
	 * mettre une jolie doc en ligne de commande pour exécuter tout ça 
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ANIMES / ANIMATIONS	//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	/*
	 * TODO lorsque l'anime attendue est censée s'arrêter (pas de répétition)
	 * et que le moteur compte sur un evt de fin pour passer à l'anime suivante,
	 * il y a blocage si l'anime n'est pas définie, et que l'anime par défaut est
	 * répétée.
	 * exemple : défaut=marcher, et il manque punch -> le bonhomme ne sort jamais de l'état punching
	 * 	>>solution : ne jamais compter sur la fin de l'anime, toujours imposer une durée à respecter
	 * 	(ce qui permet d'uniformiser le beans pour tous les joueurs)
	 */
	
	/*
	 * TODO il faut sécuriser les animations : s'assurer qu'une anime censée
	 * ne pas être ré-init est bien gaulée. par exemple, pushing doit être gaulée
	 * comme walking (dans le XML) sinon p-ê pb (si pas même durée ou autres diff)
	 * > à vérif
	 */
	
	/*
	 * TODO permettre de définir plusieurs animes pour 1 gesture,
	 * avec choix aléatoire
	 *  
	 */
	
	/*
	 * TODO il faut définir un système de substitution d'image
	 * de manière à ce qu'il soit impossible d'être pris en défaut
	 */
	
	/*
	 * TODO introduire une anime/action supplémentaire : en jeu, un joueur
	 * peut provoquer un autre joueur en déclenchant une animation spécifique.
	 */

	/*
	 * TODO faire disparaitre progressivement les sprites à la fin du burning (niveau d'alpha ? cf items thème TBB)
	 * 	>> implémenter une gestion dynamique de l'alpha, depuis le fichier XML
	 *  >> cohérent avec l'histoire de transformations (flip et tout ça) à définir aussi dans XML
	 */
	
	/*
	 * TODO clignotements de sprites : mettre de la transparence dans les version coloriées
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ABILITIES / CAPACITÉS	//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * TODO y a des trucs qui vont se recouper style :
	 * le bloc doit-il empêcher le joueur de poser une bombe
	 * ou bien la bombe d'apparaître ?
	 * >>> un bombe peut apparaître pour autre chose qu'un drop (ex : téléport)
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
	
	/*
	 * TODO chargement des niveaux
	 * 	- commencer une partie rapide avec un seul niveau
	 * 	- jouer un round
	 * 	- changer les items par défaut pour ce niveau, directement dans le fichier XML
	 * 	- jouer le round suivant: 
	 * 		- la GUI affiche la modif
	 *		- mais le hollow level n'a pas été rechargé (mutualisation)
	 *		- et donc la modif n'est pas prise en compte dans l'initialisation des joueurs 
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// STATS / STATISTIQUES / STATISTICS	//////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * TODO
	 * définir des stats de jeu plus complètes :
	 * 		- niveaux les plus utilisés
	 * 		- voire détail de tout par niveau
	 * 		- stats temporelles
	 * 		- analyse du jeu du joueur en fonction du temps, performance par niveau
	 */

	/*
	 * TODO
	 * utiliser une BD pour stocker toutes les données du jeu,
	 * y compris la configuration (?)
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// PROFILES / IDENTIFICATION / PROFILS	//////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TODO PB: est-ce qu'on autorise la création d'hote/joueur indépendemment du central, ou pas.
	 * >> OUI,mais pas de mdp
	 * 
	 * HOTES
	 * - chaque hôte est identifié par un UUID (http://www.javapractices.com/topic/TopicAction.do?Id=56)
	 *   qui n'est jamais modifié après
	 * - de plus, l'ensemble de toutes ses adresses MAC sont enregistrées
	 *   (http://www.kodejava.org/examples/250.html 
	 *    http://www.stratos.me/2008/07/find-mac-address-using-java
	 *    http://www.kodejava.org/examples/250.html)
	 * - cette liste est mise à jour à chaque démarrage
	 * - si d'un démarrage à l'autre il n'y a aucune adresse commune, ça veut dire que ce n'est plus la même machine
	 * - on génére alors un nouvel UUID qui écrase le précédent
	 * - les mots de passe de tous les profils sont réinitialisés
	 * 
	 * JOUEURS
	 * - chaque joueur est identifié par l'identifiant de l'hote sur lequel il est créé
	 *   plus un identifiant unique pour cet hôte (on peut prendre un autre UUID)
	 * - ce numéro ne change jamais, même quand il joue sur un autre hote (ce qui est possible)
	 * - on a un truc du style : joueur@createur
	 * - quand le profil est utilisé sur son hote de création, pas besoin de mot de passe
	 * - sinon il faut un mot de passe, qu'on a la possibilité d'enregistrer
	 * >> en fait pas besoin de voir l'hote, avec l'uDDi ça suffit
	 *    chaque joueur doit entrer le mdp quel que soit l'hote (même créateur)
	 *    si le joueur n'est pas enregistré sur le site, il ne peut pas être protégé par un mot de passe
	 *    et de toute façon ses stats ne comptent pas
	 * >> intérêt d'identifier de façon unique l'hote ??
	 *    ah oui, pr les stats sur qui joue chez qui...
	 *    
	 * NOTE empecher le même joueur de jouer deux matches en même temps...
	 * >> seulement les matches via central sont enregistrés dans les stats
	 */

	/*
	 * TODO PROFIL
	 * faut revoir la notion de profil:
	 * 	- connecter directement les stats
	 *  x introduire la distinction entre profil local et distant (fait)
	 *  
	 *  - ne plus permettre qu'un joueur n'appartienne pas au classement glicko2
	 *  
	 *  ca devrait pas être possible d'avoir un profil sans stats
	 *  toutes les stats devraient être centralisées et chargées quand nécessaire
	 *  y compris pour les rencontres locales (qui peuvent être stockées le temps d'avoir un accès réseau)
	 */
	
	/*
	 * TODO profiles options : reset passwords (automatically performed
	 * when the MAC address changes, anyway)
	 */

	/* 
	 * TODO REMOTE 
	 * propriété remote des joueurs : comment s'assurer qu'un joueur est bien identifié ?
	 * >> le central enregistre l'id du dernier hote sur lequel le joueur s'est connecté
	 * 	  si un joueur essaie de se connecter à partir d'un hôte différent, erreur et on demande au joueur de s'identifier
	 * >> mais en fait c'est le central qui controle la véracité, c'est lui qui détient le dernier hote de connection
	 *    donc on ne peut faire de partie enregistrée qu'en passant par le central, basta. sinon c'est du hors-piste, de l'amical.
	 * 
	 */ 
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// KNOWN, WAITING BUGS	/ BUGS EN ATTENTE	//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	/*
	 * TODO
	 *	l'ombre des blocs de la dernière ligne est portée sur les blocs de la première ligne
	 *	c'est normal. pour l'éviter, il faudrait traiter un cas assez particulier...
	 *	>> à voir
	 */
	
	/*  
	 *  TODO l'item kick ne marche pas
	 */
	
	/*
	 * TODO
	 * quand on pose deux bombes en diagonale et qu'on se place dans le cadrant intérieur d'une des cases libres du même carré
	 * on est bloqué. ce n'est pas vraiment un pb en fait, plus un feature :P . mais les non-initiés peuvent prendre ça pour un bug.
	 * (note : point mentionné dans le blog)
	 */
	
	/* 
	 * TODO
	 * impossible de poser une bombe quand on est en train de buter contre un mur en direction upleft (et uniquement cette direction pr NES2) et downleft (pr SBM1)
	 * 		>> ca viendrait de swing ou du clavier (pb matériel ou bas niveau)
	 */
	
	/* 
	 * TODO
	 * cas particulier : item apparaissant dans une explosion de bloc, avec un joueur déjà sur le bloc 
	 * (il a passe-muraille et résistance au feu) : l'item doit être ramassé dès qu'il a fini d'apparaitre, en théorie
	 * ou bien : un héros meurt, un item apparait sous les pieds d'un autre joueur immobile : l'item reste sans que le joueur ne se l'approprie
	 * >> en même temps, ça laisse le choix au joueur de ramasser l'item ou pas, c'est pas mal finalement (si c'est un malus par ex.)
	 */
	
	/*
	 * TODO
	 * bug d'affichage dans les notes d'IA, les accents sont affichés avec la police par défaut
	 * >> pas trouvé la cause de ce beans... (et c'est pas faut d'avoir cherché !)
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
// BUGS				//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* TODO
	 * 
	 * - bug : j'ai vu l'ia prendre une vie, mourir, relacher la vie, et revenir. 
	 * 	 et la vie était fonctionnelle : je l'ai prise, je suis mort, je suis revenu (sans la relacher cette fois)
	 *   >> pas réussi à reproduire ça...
	 * 
	 * - la pause d'IA reste même si le joueur est mort, et je ne sais pas pq : au débug le moteur n'exécute pas le code affichant le texte !
	 * 
	 * - quand on balance une bombe, puis une autre, puis fait péter la première et enfin la seconde 
	 *   juste à la fin des flammes de la première, alors l'explosion de la 2nde est circonsrite à la
	 *   case où elle atterrit.
	 *   
	 * - pb de réinitialisation : 
	 * 		- jouer un tournoi single (par ex supercup) en entier
	 * 		- jouer un tournoi de type cup en suivant : il y a un pb sur le MatchPanel, qui semble n'avoir pas été réinit à null
	 * 
	 *  - apparemment, y a un pb avec les GeneralAction, dont certaines définies dans les fichiers XML ne sont pas compatibles 
	 *    avec la classe (en termes de circonstances) mais qui sont quand même chargées et initialisées normalement
	 *  
	 *  - il semblerait que les directions dans la SpecificAction et dans la GeneralAction correspondante ne soient pas les mêmes...
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// TO DO ENGINE / A FAIRE MOTEUR	//////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* TODO priorité HAUTE
	 * 
	 * - faut émettre un évt de sortie de case à la disparition d'un sprite (mort, masquage, etc)
	 * 
	 * - étudier le fonctionnement de ended (sprite) pr voir quand retirer le sprite de level/tile
	 * 
	 * - gérer le shrink
	 * 
	 * - remplacer "à vos marques" "pret" "boum!" par des graphismes précalculés
	 * 
	 * - à la fin de la partie, faire disparaitre tout le niveau comme il apparait au début
	 * 
	 */
	
	/* TODO priorité BASSE
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
	 * 		- spawn : faut décomposer en plusieurs abilities : délai, proba de spawn, nbre de spawn, etc
	 * 
	 * - évènements :
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
	 * 		  qui sera affiche en titre de prèsentation/stats etc. 
	 * 		  si pas de nom, utilisation d'un nom générique (Round 1 - Prensentation) etc
	 * 		- besoin d'une méthode permettant d'exporter un tournoi/match/round, 
	 * 		  ie de l'écrire entièrement en local (pas de référence à des composants existants)
	 * 		- dans les fichiers xml, pour les points, plutot que local (vrai ou faux), utiliser round/match/tournament/game (permet de mieux mutualiser les fichiers)
	 * 		- tournoi coupe : 
	 * 			- pouvoir se qualifier depuis n'importe quel leg, pas forcement le précédent direct (nécessité de préciser le numéro du leg en plus du part, dans le doc XML et la class CupPlayer)
	 * 			- y compris depuis le leg initial (prévoir une sous-classe pour distinguer initial des autres?)
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
	 * 		- lors du calcul des points, il faut forcer la prèsence d'un classement: 
	 * 		  ça facilite énormément de traitements en aval
	 *   	  au pire, si le classement est inutile (ex: simple total), on définit un classement-identité 
	 *   	  (pts utilisés pr le classement <=> pts marqués)
	 *
	 * - Limites/Modes de jeu :
	 * 		- limites exprimées de façon relative (peindre 75% des cases, éliminer la moitié des joueurs...)
	 * 		- items: 1 item arrêtant la partie, 1 item faisant diminuer le temps restant (anti-temps)
	 * 		- possibilité de choisir entre le fait que le round s'arrête Dès que tout le monde est mort 
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
	 * 		- HIDING devrait être un gesture définit automatiquement, non ? pas d'image, sensible à rien, seule action autorisée=apparaitre...
	 *  	- vérifier qu'avant d'exécuter une action, on vérifie si le sprite concerné (actor) posséde bien l'ability (avec modulate)
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
	 * - Niveaux
	 * 		- outil pour découper une image (background) en nxm floors
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// TO DO GUI / A FAIRE GUI	//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* TODO
	 * 
	 * - rajouter un bouton dans les options avancées pour vider manuellement le cache mémoire
	 * 
	 * - rajouter une confirmation (popup) quand on sort définitivement d'une partie (icone maison)
	 * 
	 * - 1ère exécution
	 * 		- définir un popup d'informations
	 * 		- guider l'utilisateur pas à pas pour créer son profile et commencer sa partie
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
	 * - dans les tables, il faudrait avoir la possibilité de développer/cacher des colonnes ou lignes en cliquant dessus
	 *   ça permettrait d'afficher des stats plus détaillées seulement au besoin, ou pour le calcul des points d'un match, etc.
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// CLEANING PROCEDURE / PROCÉDURE DE NETTOYAGE	//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * - source :
	 * 		- éventuellement mettre à jour la version 
	 * 		- supprimer zgraphics
	 * 		- supprimer tous les commentaires TODO et NOTE
	 * 		- effacer le changelog
	 * 		- virer les classes contenant un main()
	 * - ressources :
	 * 		- supprimer restmp
	 * 		- supprimer les agents de test
	 * 		- réinitialiser les logstats
	 * 		- recopier les stats (éventuellement)
	 * 		- recréer les sauvegardes des tournois
	 * - options :
	 * 		- virer l'enchainement automatique
	 * 		- virer le bombage des IA inactives
	 * - profils :
	 * 		- supprimer les profils de test
	 * 		- réinitialiser les stats des joueurs humains ou autres
	 * 
	 * - pour diffusion publique :
	 * 		- fichiers :
	 * 			- recompiler le jeu	
	 * 			- virer les fichiers sources
	 * 			- virer les IA pas finies (source+profils)
	 * 			- virer les versions intermédiaires des IA
	 * 		- options :
	 * 			- simulation des matches AI-only
	 * 		- prérégler la partie rapide sur : 
	 * 			- les meilleures IA 
	 * 			- des niveaux compatibles
	 * 			- les règles classiques
	 * 
	 *  - pour diffusion projet :
	 * 		- réseau :
	 * 			- bloquer le bouton "réseau" dans le menu principal
	 * 			  org.totalboumboum.gui.menus.main.MainMenu.buttonNetworkGame
	 * 			- bloquer le bouton "réseau" dans le menu de config tournoi/partie rapide
	 * 			  org.totalboumboum.gui.menus.tournament.TournamenuMenu.buttonPublish
	 * 			  org.totalboumboum.gui.menus.quickmatch.QuickMatchMenu.buttonPublish
	 * 			  
	 * 		- replay :
	 * 			- bloquer le bouton "replay" dans le menu principal
	 * 			  org.totalboumboum.gui.menus.main.MainMenu.buttonReplay
	 * 			- bloquer le bouton "caméra" dans les menu tournoi/match/round
	 * 			  org.totalboumboum.gui.game.tournament.TournamentMenu.buttonRecord
	 * 			  org.totalboumboum.gui.game.tournament.MatchMenu.buttonRecord
	 * 			  org.totalboumboum.gui.game.tournament.RoundMenu.buttonRecord
	 * 			- bloquer l'action dans les options avancées
	 * 			  org.totalboumboum.gui.menus.options.advanced.AdvancedData ligne 'case LINE_RECORD_GAMES'
	 * 		- options :
	 * 			- afficher les exceptions
	 * 			- prérégler la partie rapide 
	 * 		- ressources :
	 * 			- redonner les host id corrects pour les étudiants modifiés pour tester le réseau
	 * 				- 0d3dfd83-6632-423f-b63e-d7e5743ad998
	 * 				- 26db3658-cf0e-4650-96f4-55080b4720f8
	 * 				- 553c0fb4-5418-4e33-b244-d5f399ae40f2
	 *		 	- virer le thème/perso/niveaux TBB après avoir viré les joueurs utilisant les perso et pré-enregistrès
	 * 
	 * promeneur :	af92d497-7975-4dcf-b05d-89e706c3eec3
	 * simplet : 	227b2e4d-bd7b-4153-962b-699bc909e5e1
	 * suiveur :	cc207601-8afe-4c67-94d9-17cdc9bea77d
	 */
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// CURRENT STUFF / EN COURS		//////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * - percepts à rajouter
	 * 		- il faut pouvoir être capable de déterminer si un perso est malade, voire contagieux
	 * 		- pour faire un suivi d'une ia à travers plusieurs rounds :
	 * 			- chemin permettant d'écrire dans le package de l'IA, afin de lui permettre de faire des sauvegardes ?
	 * 			- avoir accès à l'identifiant de l'IA, et pas seulement à sa couleur
	 * 
	 * - définir une mesure factuelle (reposant sur des faits de jeu) évaluant l'agressivité des joueurs
	 * 		- cible des bombes: rien, mur ou IA
	 * 		- à pondérer avec le nombre de bombes posées par seconde (par ex)
	 * 
	 * - dans NBM1, on prend l'item quand on passe au centre de l'item, pas quand on entre simplement sur la case
	 * 
	 * - prochaines étapes :
	 * 		- league
	 * 		- instance TBB
	 * 		- sons
	 * 		- BM'83
	 * 		- shrink (?)
	 * 		- réseau
	 * 		- revoir GUI (?)
	 * 		- sortir de bêta
	 * 
	 *  - tournoi league :
	 *  	- finir GUI
	 *  	- finir sélection des matches
	 *  	- ordonner les matches
	 *  - tournoi coupe : 
	 *  	- possibilité de définir un tie-break random
	 *  	- possibilité de définir un tie-break classement stats
	 *  	- possibilité de définir des matches de classement optionnels (?)
	 *  
	 *  - charger directement le format AI depuis le jeu permettrait de ne pas avoir à générer 50 images de couleurs différentes
	 *    (ou un autre format vectoriel, SVG p-ê ?)
	 *  
	 *  - options pour les stats ?
	 *  	- accélérer la fin des parties où tous les humains ont été éliminés (p-e un popup ? et/ou une option : oui-non-demander)
	 *  	- enregistrer l'évolution des stats (sur plusieurs points temporels)
	 *  	- forcer la fin de la période (now)
	 *  
	 *  - instance TBB
	 *  	- autoriser un burning player à poser une dernière bombe...
	 *  	- bombe sensible aux chocs : en forme d'oeuf
	 *  	- bombe en fer, résiste aux autres explosions
	 *  	- le feu passe-muraille doit être d'une autre couleur et/ou forme
	 *  	- feu électrique : tente d'atteindre un adversaire dans la mesure de sa portée (prend des virages et tout)
	 *  	- feu d'amour : 
	 *  		- plein de petits coeurs qui contaminent ceux qu'ils touchent. 
	 *  		- les bombes qu'ils posent sont alors aussi des bombes à coeurs. 
	 *  		- elles ne tuent pas, elles contaminent. 
	 *  		- le contaminateur initial est résistant (les coeurs sont de sa couleur)
	 *  	- si le feu normal était graphiquement un truc carré avec plein de flamèches ?
	 *  	- apparition des items : fumée+légèrement surélevé, puis l'item tombe par terre... (comm bug après cache v2)
	 *  
	 *  - enregistrement d'une partie : au lieu d'utiliser data+xml, mettre les données
	 *    xml dans le fichier de données aussi. (au début bien entendu)
	 *    
	 * - voir quels threads swing prennent toutes les ressources sur les niveaux très chargés
	 */ 
	
	


	
	
	
	
	// TODO toutes Loops >> effacer tous les objets inutiles dans finish()
	
	
	
	//TODO tester les confs de tournois/partie rapide sans l'option "utiliser les réglages précédents"
	
	
	/**
	 * TODO mort subite
	 * 
	 * - màj modèle :
	 * 		- prise en compte du shrink dans le modèle
	 * 		- màj des tests des modèles de l'API
	 * 
	 * - réformer tous les thèmes des instances déjà traitées
	 * 		- de manière à mutualiser au max
	 * 		- épurer en virant les items pas standard
	 * - adapter les niveaux existants pour qu'il y ait la mort subite originale
	 * 
	 * - à tester :
	 * 
	 * - bugs :
	 * 
	 * 
	 * TODO nouveaux items
	 * - à faire :
	 * 		- full model (AiFullModel.pickItem):
	 * 			- gérer les changement de vitesse d'un joueur (aussi dans AiSimHero.updateWalkingSpeed)
	 * 			- gérer l'aspect temporel de certains malus
	 * 			- gérer aussi leur aspect contagieux
	 * 		- ressources
	 * 			- màj les noms des items: no-xxxx, anti-xxxx, extra-xxxx, golden-xxxx
	 *  
	 *  
	 *  - adapter simplet pour être sûr que tout est ok
	 *  - noter les modifs dans le changelog
	 *  - GUI: on peut activer / désactiver la mort subite (mais pas la configurer)
	 */
	
}
