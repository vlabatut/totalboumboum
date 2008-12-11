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
	
// **********************************************************
// IA / TOURNOI
// **********************************************************
	
	/*
	 * TODO il faut r�cup�rer toutes les exceptions �ventuellement caus�es par les IA
	 * par exemple en les routant sur une fen�tre d�bug sp�ciale ?
	 * (cf OkanYuksel qui fait un d�passement de la pile)
	 */
	
	/*
	 * TODO il ne faut pas ex�cuter toutes les IA dans un thread pool, mais au contraire les s�parer
	 * sinon, une seule IA bouclant � l'infini va consommer tous les threads dispo dans le treadpool
	 * vont �tre successivement monopolis�s par l'IA
	 */
	
	/*
	 * TODO dans l'API il faut g�n�raliser (tout en simplifiant) les propri�t�s des sprites, plutot que 
	 * de d�finir des fonctions sp�cifiques � chaque fois.
	 */
	
// **********************************************************
// BOMBES
// **********************************************************

	/*
	 * TODO autoriser de poser une derni�re bombe avant de mourir ? (i.e. dans le gesture burning) comme ds XBlast
	 */						
	
	/*
	 * TODO la dur�e d'explosion d'une bombe et la dur�e de burning des sprites doivent �tre 
	 * �gales (?) ou au moins impos�es
	 */
	
	/*
	 * TODO lorsqu'une bombe est touch�e par une flamme, elle bloque la flamme.
	 * ce qu'on voit passer est la flamme r�sultant de l'explosion de la bombe.
	 */

	/*
	 * TODO le temps d'explosion d'une bombe est propre � la fois � la bombe
	 * (capacit�) et au joueur (certains malus la modulent)
	 */
	
	/*
	 * TODO dans l'eventMgr de la bombe, quand elle oscille, il faut g�rer les sprites
	 * qui sont en train de la pousser, et non pas en simple contact.
	 * de plus, il faut que les sprites aient le pouvoir de pousser, sinon ils ne comptent pas.
	 */

// **********************************************************
// COMMANDES
// **********************************************************

	/*
	 * TODO les touches doivent impl�menter (toutes, et non pas seulement les directions)
	 * une option 'stop on release'. par exemple, si je reste appuy� sur
	 * jump, �a doit sauter d�s que c'est possible, et ne s'arr�ter que quand je
	 * rel�che la touche.
	 * en fait non, c int�ressant pour seulement certaines actions
	 */
	
	/*
	 * TODO lors de l'impl�mentation du jeu en r�seau,
	 * une optimisation consiste � ne pas g�n�rer des �vts par le controleur tant
	 * qu'une touche est appuy�e, car �a va faire bcp de choses � transmettre,
	 * le controleur �tant situ� cot� client.
	 * plutot, il faut les g�n�rer dans le controlManager, qui est cot� serveur.
	 * et du cp aussi, il faut �viter d'utiliser ce mode de gestion des �v�nements
	 * pour les autres types d'evts (ie non-controles)
	 */
	
// **********************************************************
// FIRE
// **********************************************************
		
	/*
	 * TODO � faire sur le feu (long terme) :
	 * - d�composer l'explosion en :
	 * 		1)explode
	 *		2)stay still
	 *		3)implode
	 * - g�rer le d�placement des flammes � la XBlast
	 */

	/*
	 * TODO explosion:
	 * lorsque la flamme est construite, on teste si chaque case la laisse passer
	 * mais il faut aussi le faire sur la case centrale (origine de l'explosion)
	 * car diff�rents facteurs peuvent limiter l'explosion � une port�e de 0
	 * (sans pour autant emp�cher l'explosion) 
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
	 * permettrait de trouver un path de fa�on plus propre ? 
	 */    

	/*
	 * TODO dans l'EventManager, est-il vraiment n�cessaire de synchroniser la m�thode update ?
	 * car on appelle une m�thode setGesture dans sprite : quel est le risque d'interblocage r�el ?
	 * � g�n�raliser � toutes les m�thodes que j'ai synchronis�es
	 */

	/* TODO lorsqu'un sprite est ended :
	 * + il faut virer le sprite partout o� il est utilis� : gestionnaire de touches, etc. 
	 * (faire une recherche de r�f�rence � la classe hero et sprite)
	 * + si c'est un h�ros il faut faire p�ter toutes ses bombes et rel�cher tous ses items
	 */	

	//TODO le saut ne doit pas �tre sensible � la vitesse du personnage	-> � voir !

	/*
	 * TODO plutot que d'utiliser des m�thodes synchronis�es : 
	 * utiliser une file synchronis�e par d�finition ?
	 */

	/*
	 * TODO dans SBM2, il faut g�rer :
	 * 	- les boss (dans les autres aussi, notamment SBM5)
	 * 	- les esp�ces de chars d'assaut pr�sent dans certains niveaux (utilis�s par les cr�atures)
	 */
	
	/*
	 * TODO il est possible que certaines bombes aient des animes diff�rentes pour chaque direction
	 * cons�quence : il faut toujours orienter un gesture, m�me si �a n'a pas vraiment de sens a priori.
	 * ex: bombe missile que l'on pose vers une direction, et qui part en roulant quand elle explose, d�truisant le premier obstacle rencontr�
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
	 * TODO d�composer, s�parer chaque �tat dans une classe sp�cifique
	 * qui d�finit : 
	 * 	- les transitions : comment entre-t-on dans cet �tat, comment passe-t-on dans un autre �tat
	 * 	- les r�actions : ou plutot les interactions, avec les autres sprites et l'environnement
	 * les managers, en particulier event et request, utiliseraient ces classes.
	 * chaque sprite serait caract�ris� par l'utilisation d'un certain nombre de ces classes.
	 */
		
	/*
	 * TODO il faut mettre en place une m�morisation de la derni�re action command�e qui n'a pas pu
	 * �tre ex�cut�e par un sprite, et tenter de l'ex�cuter � chaque cycle.
	 * exemples : 
	 * 	- remote bombe devant exploser car l'owner est mort, mais ne pouvant pas car bouncing
	 * 	- item devant appara�tre car le bloc a �t� d�truit, mais ne pouvant pas car un joueur/bombe occupe la place
	 */
	
	/*
	 * TODO il va �tre n�cessaire de d�finir un gesture indiquant l'absence d'un sprite relativement au jeu :
	 * 	- item pas encore d�couvert car le bloc le contenant n'a pas �t� d�truit (�quivalent � NONE ?)
	 * 	- item actuellement utilis� par un joueur (prop: USED)
	 */

	/*
	 * TODO les attributs XML respectent la convention java, et non pas XML (avec tirets)
	 */
	
	/*
	 * TODO dans l'avenir il serait p-� n�cessaire d'utiliser un actionManager,
	 * qui recevrait une action en param�tre, et l'ex�cuterait.
	 * int�r�t : d�composition + utile pour un moteur qui serait seulement un player,
	 * et ne devrait donc pas g�rer les tirages al�atoires, mais seulement l'application
	 * d�terministe des actions (le tirage se ferait dans la fonction appelant l'actionMger,
	 * ie g�n�ralement : l'eventMgr)
	 */

// **********************************************************
// JOUEURS
// **********************************************************

	/*
	 * TODO
	 * il faut faire un syst�me de mani�re � pouvoir faire varier le nombre
	 *  de joueurs dans un tournoi sans pour autant tout reconfigurer.
	 *  �a peut s'estimer automatiquement : 
	 *  	- pour s�rie/simple : 
	 *  		- parmi tous les matches, quel est le level acceptant le moins de joueurs
	 *  		- ->il s'agit du nombre de joueurs max du tournoi
	 *  	- pour coupe : 
	 *  		- on d�finit un match (possiblement diff�rent) par niveau de progression
	 *  		- donc ce nombre, pour un niveau, indique combien de matches seront n�cessaires, et comment r�partir les joueurs
	 *  		- (il n'y a pas de limite th�orique au nombre de joueurs)
	 *  		- le concepteur peut configurer certains param�tres, comme le nombre min/max de joueurs pour chaque niveau de la comp�te
	 *  	- pour championnat :
	 *  		- a priori, il est possible de tout goupiller parfaitement quelles que soient les contraintes de nombre de joueurs
	 *  
	 */
	
	/*
	 * TODO au chargement du tournoi, n�cessaire de d�terminer ce nombre de joueurs max
	 * de m�me pour match et round
	 */	
	
// **********************************************************
// PERSOS
// **********************************************************

	/* TODO
	 * 
	 * un sprite de perso doit �tre d�fini dans toutes les 16 couleurs possibles
	 */
	
// **********************************************************
// ITEMS
// **********************************************************

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
	 * TODO g�rer l'apparition des items � la suite d'une �limination,
	 * comme dans SBM1, avec des volutes de fum�e
	 */
	
	/*
	 * TODO il y a aussi � g�rer le d�placement des items lors d'une explosion
	 */
	
	/*
	 * TODO g�rer une anime d'apparition de l'item
	 * voire aussi de disparition
	 */
	
// **********************************************************
// XML
// **********************************************************
	
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

	/* TODO il faut r�former les fichiers de propri�t�s :
	 * id�e g�n�rale : 
	 * 	- elles diff�rent d'un type d'objet � l'autre
	 *  - elles d�crivent quels fichiers on doit trouver et o�
	 * utilisation :
	 * 	1) charger les propri�t�s, permettant d'avoir un aper�u
	 *  2) �ventuellement charger l'objet lui-m�me gr�ce aux propri�t�s 
	 */

	/*
	 * TODO lorsqu'on d�finit une animation ou une trajectoire qui est identique � une autre
	 * plutot que de faire un copier-coller, permettre de nommer l'animation d'origine.
	 * bien sur, il faudra v�rifier que cette animation existe, comme pour celle par d�faut. 
	 */
	
	/*
	 * TODO dans les fichiers XML, il faut pr�ciser en attribut de la racine la version
	 * du fichier utilis�e.
	 */
	
	/*
	 * TODO il faut reprendre les fichiers XML en centralisant au maximum les types
	 * par exemple y en a plein c des r�f�rences vers des fichiers (attributs file et folder)
	 * �a permettrait de centraliser le code �galement dans le XMLTools
	 */
	
	/*
	 * TODO dans les fichiers XML, dans toutes les r�f�rences aux XSD, remplacer
	 * les \ par des / : �a semble marcher tout aussi bien (du moins avec l'�diteur
	 * d'�clipse)
	 */
	
// **********************************************************
// THEMES/LEVELS
// **********************************************************
	
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
	
// **********************************************************
// ANIMES
// **********************************************************
		
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

// **********************************************************
// ABILITIES
// **********************************************************
	
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
	 * TODO dans les permissions, faut-il passer les forces � 0 et sans framing ?
	 * de plus, ne faudrait-il pas inclure les interdictions sous la forme de modulations
	 * avec framing et valeur n�gative ou nulle ? 
	 */

	/*
	 * TODO le syst�me de gestion des actions est clairement � am�liorer.
	 * certaines actions comme gather sont automatiques. certaines actions ont un effet de zone 
	 * (pr gather : la case).
	 */

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/*
	 * TODO BUGS EN COURS
	 *  - probl�me de collision : quand on vient de poser une bombe, on est bloqu� dans certaines
	 *  	directions. hypoth�se: il faudrait peut �tre red�finir l'action movelow/high pour la rendre transitive
	 *  	ce qui permettrait de controler ce cas de figure dans le XML
	 *  	>> contourn� avec une truc pas tr�s propre, � r�soudre s�rieusement
	 *  - quand on balance une bombe, puis une autre, puis fait p�ter la premi�re et enfin la seconde 
	 *  	juste � la fin des flammes de la premi�re, alors l'explosion de la 2nde est circonsrite � la
	 *  	case o� elle atterrit.
	 *  - l'item kick ne marche pas
	 *  - quand un mur est d�truit (d�finitivement) par une penetration bomb, l'item n'apparait pas car
	 *  la flamme de la bombe dure plus longtemps que l'explosion du mur, et emp�che l'item d'appara�tre
	 */
	
	
	
	/*
	 * TODO il faut appliquer � trajectoryMgr le m�me principe (dur�e forc�e)
	 * que pour animeMgr
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
	
	/*
	 * TODO
	 * 
	 * + beta.009
	 * - modification : mutualisation des classes codant les �l�ments de la GUI
	 * - nouveaut� : mode plein �cran
	 * 
	 * *******************************************************
	 * *********************** A FAIRE ***********************
	 * *******************************************************
	 * 
	 * - �a serait bien que les joueurs soient affich�s dans l'ordre relatif aux points de la limite rencontr�e
	 *   voire on d�finit explicitement un ordre d'affichage dans la comp�tition
	 * 
	 * - faire le classement lexicographique g�rant les signes diacritiques partout o� c'est n�cessaire
	 * 
	 * - parties en cours :
	 * 		- une seule partie en m�me temps
	 * 		- cliquer sur exit termine effectivement la partie en cours
	 * 		- utiliser les fl�ches par contre, permet d'en sortir avec possibilit� d'y revenir, l'autre type de rencontre est bloqu� (quickmatch/tournament)
	 * 		- ou alors le fait de commencer une nouvelle partie provoque l'arr�t de l'ancienne (avec interrogation de l'utilisateur)
	 * 		+ plus simplement : g�rer deux configurations s�par�es pour le tournoi et le quickmatch
	 * 
	 * - modifier le loader d'image de mani�re � ce qu'une image non-trouv�e soit remplac�e par la croix rouge
	 * 
	 * - calculer en dur les largeurs limites de toutes les colonnes utilis�es dans toutes les tables
	 * 
	 * - utiliser les tooltips pour afficher les infos trop longues : calcul de points, nombre � virgule dans la colonne des points (d�cimales cach�es), etc.
	 * - pb de dimension de l'image de fond en fonction de la r�solution... (zones pas peintes)
	 * - results panel : quand il y a trop de rounds dans un match pour que �a rentre � l'�cran, ne pas tout afficher
	 * - possibilit� de donner des noms aux matches et aux rounds
	 * 
	 * - bug de d�placement quand on bouge en diagonale en posant plein de bombes dans un espace d�gag�, on se retrouve t�l�port� sur une case voisine
	 * - bug d'animation/collision : push ne se d�clenche pas
	 * - bug d'animation : quand le bonhomme va � droite et qu'on appuie sur gauche, y a une esp�ce de passage droite-gauche tr�s rapide au niveau de l'animation, insuportable
	 * 
	 * - faire un param�tre dans les rounds qui permet de d�terminer s'ils sont compatibles avec le tournoi 2007-08
	 * - tournoi : 1) on choisit les param�tres 2) on choisit les joueurs, le jeu restreint leur nombre pr qu'il soit compatible avec le tournoi, et restreint aussi les IA pour les m�mes raisons
	 * 
	 * - besoin d'une m�thode permettant d'exporter un tournoi/match/round, ie de l'�crire enti�rement en local (pas de r�f�rence � des composants existants)
	 * 
	 * - dans les r�sultats :
	 * 		- afficher par d�faut les 4 scores de bases
	 * 		- plus les scores utilis�s dans les points et/ou les limites
	 * 		- si les limites utilisent des points custom, les afficher aussi
	 * 
	 * - bug d'affichage dans les notes d'IA, les accents sont affich�s avec la police par d�faut
	 * 
	 * - stats : nombre de fois qu'un level a �t� jou�
	 * - champ en plus dans les profils : le classement du joueur, nbre de rouds gagn�s/nbre de rounds jou�s
	 * - dans les persos : % de rounds gagn�s, ou bien nbre de rounds jou�s
	 * 
	 * - lors de la s�lection des commandes :
	 * 		- cliquer sur le bouton d'action fait r�agir quelque chose dans la ligne du joueur correspondant
	 * 		- permet de v�rifier qu'on a pris les bonnes commandes (celles qu'on pensait avoir prises)
	 * 
	 * -------------------------------------------------------------------
	 * - calcul de points : introduire des variables comme le nombre de joueurs (pour d�finir un bonus pr le joueur qui fait un perfect en survival)
	 * - g�rer le shrink
	 * - mode plein �cran
	 * - dans les autorisations, g�rer l'apparition comme une action en soit. si pas possible d'appara�tre au d�but de la partie, faire un atterrissage ?
	 * - � la fin du round, faire apparaitre les r�sultats par transparence...�a serait la classe �a !
	 * 
	 * - pour painting, possibilit� de d�finir quelles cases peuvent �tre repeinte, ce qui permet de poser comme limite un %age de cases repeintes
	 * - d�finir des noms "human readable" pour les items, histoire de ne pas afficher ces codes internes dans la GUI, en profiter pour introduire une decription, le tout en plusieurs langues. utiliser le code ISO comme param�tre de langue, et l'introduire dans le fichier de langue
	 * - possibilit� de d�finir un nom pour tournament/match/round, qui sera affiche en titre de pr�sentation/stats etc. si pas de nom, utilisation d'un nom g�n�rique (Round 1 - Prensentation) etc
	 * - faire un chargement ad hoc des matches, rounds, etc ? fusionner du coup HollowLevel et LevelPreview ? (voir si les objets de ces deux classes sont cr��s au m�me moment)
	 * - ergonomie : faire le chargement du round d�s qu'on clique sur "next" dans le match, et attendre ensuite que le joueur valide le d�but du match !
	 * 
	 * - redistribution des items lors de la mort d'un joueur (option de round?)
	 * - possibilit� de bloquer certains items (on ne les perd pas lorsqu'on meurt)
	 * 
	 * LIMITES & MODES de jeu :
	 * - limites exprim�es de fa�on relative (peindre 75% des cases...)
	 * - items: 1 item arr�tant la partie, 1 item faisant diminuer le temps restant (anti-temps)
	 * - au moins finir le cycle lors d'une mort, histoire que la diff�rence de timing ne vienne pas juste de l'ordre des joueurs dans la partie 
	 * - possibilit� de choisir entre le fait que le round s'arr�te d�s que tout le monde est mort sauf 1, ou derni�re flamme termin�e
	 * - feature li� au pr�c�dent : gagner plus de points si on finit effetivement le jeu que si on a un time out ou un entre-tuage
	 * reformater les modes de jeu :
	 * 	- pour paint il suffit de d�finir des bombes sp�ciales qui peignent le sol
	 */
	
	
	/*
	 * partie rapide :
	 * 	- le match est d�fini ad hoc, de fa�on simplifi�e
	 * 		- on choisit directement des niveaux et pas des rounds
	 * 		- m�mes points pour tous les rounds
	 * 		- m�mes limites aussi 
	 *  - on peut configurer le match dans une certaine mesure
	 *  	- choix des joueurs
	 *  	- choix des NIVEAUX + ordre al�atoire ou pas
	 *  	- choix des limites pour match et rounds
	 *  - choix limit� de limite, voire configuration simplifi�e :
	 *  	- temps limite
	 *  	- ? s'inspirer des BM existants
	 *  	- emplacement de d�part al�atoire
	 */
}
