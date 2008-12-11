package fr.free.totalboumboum;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.SplashScreen;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.menus.main.MainFrame;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.XmlTools;

public class Launcher
{	
	public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	
		// splashscreen
		SplashScreen splash = SplashScreen.getSplashScreen();
		
		// init
		updateSplash(splash,"[Loading XML schemas]");
		XmlTools.init();
		updateSplash(splash,"[Loading configuration]");
		Configuration.loadConfiguration();
		updateSplash(splash,"[Loading GUI]");
		GuiConfiguration.loadConfiguration();
		updateSplash(splash,"[Initializing GUI]");
		GuiTools.init();
		ToolTipManager.sharedInstance().setInitialDelay(200);
		updateSplash(splash,"[Done]");
		
		// create GUI
		SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	try
				{	new MainFrame();
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
		    g.drawString("Total Boum Boum version "+GameConstants.VERSION,70,90);
		    g.drawString(new Character('\u00A9').toString()+" 2008 Vincent Labatut",70,100);
		    g.drawString("Licensed under the GPL v2",70,110);
			g.setColor(GuiTools.COLOR_SPLASHSCREEN_TEXT);
	        g.drawString(msg,70,315);
	        splash.update();
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

// **********************************************************
// JOUEURS
// **********************************************************

	/*
	 * TODO
	 * il faut faire un système de manière à pouvoir faire varier le nombre
	 *  de joueurs dans un tournoi sans pour autant tout reconfigurer.
	 *  ça peut s'estimer automatiquement : 
	 *  	- pour série/simple : 
	 *  		- parmi tous les matches, quel est le level acceptant le moins de joueurs
	 *  		- ->il s'agit du nombre de joueurs max du tournoi
	 *  	- pour coupe : 
	 *  		- on définit un match (possiblement différent) par niveau de progression
	 *  		- donc ce nombre, pour un niveau, indique combien de matches seront nécessaires, et comment répartir les joueurs
	 *  		- (il n'y a pas de limite théorique au nombre de joueurs)
	 *  		- le concepteur peut configurer certains paramètres, comme le nombre min/max de joueurs pour chaque niveau de la compète
	 *  	- pour championnat :
	 *  		- a priori, il est possible de tout goupiller parfaitement quelles que soient les contraintes de nombre de joueurs
	 *  
	 */
	
	/*
	 * TODO au chargement du tournoi, nécessaire de déterminer ce nombre de joueurs max
	 * de même pour match et round
	 */	
	
// **********************************************************
// PERSOS
// **********************************************************

	/* TODO
	 * 
	 * un sprite de perso doit être défini dans toutes les 16 couleurs possibles
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
	 *  - problème de collision : quand on vient de poser une bombe, on est bloqué dans certaines
	 *  	directions. hypothèse: il faudrait peut être redéfinir l'action movelow/high pour la rendre transitive
	 *  	ce qui permettrait de controler ce cas de figure dans le XML
	 *  	>> contourné avec une truc pas très propre, à résoudre sérieusement
	 *  - quand on balance une bombe, puis une autre, puis fait péter la première et enfin la seconde 
	 *  	juste à la fin des flammes de la première, alors l'explosion de la 2nde est circonsrite à la
	 *  	case où elle atterrit.
	 *  - l'item kick ne marche pas
	 *  - quand un mur est détruit (définitivement) par une penetration bomb, l'item n'apparait pas car
	 *  la flamme de la bombe dure plus longtemps que l'explosion du mur, et empêche l'item d'apparaître
	 */
	
	
	
	/*
	 * TODO il faut appliquer à trajectoryMgr le même principe (durée forcée)
	 * que pour animeMgr
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
	 * + beta.009
	 * - modification : mutualisation des classes codant les éléments de la GUI
	 * - nouveauté : mode plein écran
	 * 
	 * *******************************************************
	 * *********************** A FAIRE ***********************
	 * *******************************************************
	 * 
	 * - ça serait bien que les joueurs soient affichés dans l'ordre relatif aux points de la limite rencontrée
	 *   voire on définit explicitement un ordre d'affichage dans la compétition
	 * 
	 * - faire le classement lexicographique gérant les signes diacritiques partout où c'est nécessaire
	 * 
	 * - parties en cours :
	 * 		- une seule partie en même temps
	 * 		- cliquer sur exit termine effectivement la partie en cours
	 * 		- utiliser les flèches par contre, permet d'en sortir avec possibilité d'y revenir, l'autre type de rencontre est bloqué (quickmatch/tournament)
	 * 		- ou alors le fait de commencer une nouvelle partie provoque l'arrêt de l'ancienne (avec interrogation de l'utilisateur)
	 * 		+ plus simplement : gérer deux configurations séparées pour le tournoi et le quickmatch
	 * 
	 * - modifier le loader d'image de manière à ce qu'une image non-trouvée soit remplacée par la croix rouge
	 * 
	 * - calculer en dur les largeurs limites de toutes les colonnes utilisées dans toutes les tables
	 * 
	 * - utiliser les tooltips pour afficher les infos trop longues : calcul de points, nombre à virgule dans la colonne des points (décimales cachées), etc.
	 * - pb de dimension de l'image de fond en fonction de la résolution... (zones pas peintes)
	 * - results panel : quand il y a trop de rounds dans un match pour que ça rentre à l'écran, ne pas tout afficher
	 * - possibilité de donner des noms aux matches et aux rounds
	 * 
	 * - bug de déplacement quand on bouge en diagonale en posant plein de bombes dans un espace dégagé, on se retrouve téléporté sur une case voisine
	 * - bug d'animation/collision : push ne se déclenche pas
	 * - bug d'animation : quand le bonhomme va à droite et qu'on appuie sur gauche, y a une espèce de passage droite-gauche très rapide au niveau de l'animation, insuportable
	 * 
	 * - faire un paramètre dans les rounds qui permet de déterminer s'ils sont compatibles avec le tournoi 2007-08
	 * - tournoi : 1) on choisit les paramètres 2) on choisit les joueurs, le jeu restreint leur nombre pr qu'il soit compatible avec le tournoi, et restreint aussi les IA pour les mêmes raisons
	 * 
	 * - besoin d'une méthode permettant d'exporter un tournoi/match/round, ie de l'écrire entièrement en local (pas de référence à des composants existants)
	 * 
	 * - dans les résultats :
	 * 		- afficher par défaut les 4 scores de bases
	 * 		- plus les scores utilisés dans les points et/ou les limites
	 * 		- si les limites utilisent des points custom, les afficher aussi
	 * 
	 * - bug d'affichage dans les notes d'IA, les accents sont affichés avec la police par défaut
	 * 
	 * - stats : nombre de fois qu'un level a été joué
	 * - champ en plus dans les profils : le classement du joueur, nbre de rouds gagnés/nbre de rounds joués
	 * - dans les persos : % de rounds gagnés, ou bien nbre de rounds joués
	 * 
	 * - lors de la sélection des commandes :
	 * 		- cliquer sur le bouton d'action fait réagir quelque chose dans la ligne du joueur correspondant
	 * 		- permet de vérifier qu'on a pris les bonnes commandes (celles qu'on pensait avoir prises)
	 * 
	 * -------------------------------------------------------------------
	 * - calcul de points : introduire des variables comme le nombre de joueurs (pour définir un bonus pr le joueur qui fait un perfect en survival)
	 * - gérer le shrink
	 * - mode plein écran
	 * - dans les autorisations, gérer l'apparition comme une action en soit. si pas possible d'apparaître au début de la partie, faire un atterrissage ?
	 * - à la fin du round, faire apparaitre les résultats par transparence...ça serait la classe ça !
	 * 
	 * - pour painting, possibilité de définir quelles cases peuvent être repeinte, ce qui permet de poser comme limite un %age de cases repeintes
	 * - définir des noms "human readable" pour les items, histoire de ne pas afficher ces codes internes dans la GUI, en profiter pour introduire une decription, le tout en plusieurs langues. utiliser le code ISO comme paramètre de langue, et l'introduire dans le fichier de langue
	 * - possibilité de définir un nom pour tournament/match/round, qui sera affiche en titre de présentation/stats etc. si pas de nom, utilisation d'un nom générique (Round 1 - Prensentation) etc
	 * - faire un chargement ad hoc des matches, rounds, etc ? fusionner du coup HollowLevel et LevelPreview ? (voir si les objets de ces deux classes sont créés au même moment)
	 * - ergonomie : faire le chargement du round dès qu'on clique sur "next" dans le match, et attendre ensuite que le joueur valide le début du match !
	 * 
	 * - redistribution des items lors de la mort d'un joueur (option de round?)
	 * - possibilité de bloquer certains items (on ne les perd pas lorsqu'on meurt)
	 * 
	 * LIMITES & MODES de jeu :
	 * - limites exprimées de façon relative (peindre 75% des cases...)
	 * - items: 1 item arrêtant la partie, 1 item faisant diminuer le temps restant (anti-temps)
	 * - au moins finir le cycle lors d'une mort, histoire que la différence de timing ne vienne pas juste de l'ordre des joueurs dans la partie 
	 * - possibilité de choisir entre le fait que le round s'arrête dès que tout le monde est mort sauf 1, ou dernière flamme terminée
	 * - feature lié au précédent : gagner plus de points si on finit effetivement le jeu que si on a un time out ou un entre-tuage
	 * reformater les modes de jeu :
	 * 	- pour paint il suffit de définir des bombes spéciales qui peignent le sol
	 */
	
	
	/*
	 * partie rapide :
	 * 	- le match est défini ad hoc, de façon simplifiée
	 * 		- on choisit directement des niveaux et pas des rounds
	 * 		- mêmes points pour tous les rounds
	 * 		- mêmes limites aussi 
	 *  - on peut configurer le match dans une certaine mesure
	 *  	- choix des joueurs
	 *  	- choix des NIVEAUX + ordre aléatoire ou pas
	 *  	- choix des limites pour match et rounds
	 *  - choix limité de limite, voire configuration simplifiée :
	 *  	- temps limite
	 *  	- ? s'inspirer des BM existants
	 *  	- emplacement de départ aléatoire
	 */
}
