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
	
	/*
	 * TODO type de tournoi plus adapt� au r�seau :
	 * des joueurs jouent un match pendant que d'autres attendent 
	 * � la fin du match, les n derniers joueurs sont rel�gu�s dans la salle d'attente
	 * n joueurs qui attendaient sont qualifi�s.
	 * un classement permet de d�terminer le leader provisoire
	 */
	
	/*
	 * TODO inclure dans l'API des fonction d'action pr�d�finies.
	 * par exemple des fonctions pour le d�placement, on indique o� on veut aller et �a d�termine � chaque it�ration
	 * quelle est l'action � effectuer. �tendre � d'autres actions ?
	 * limitation p�dago: on n'utilise plus A*
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
	
	/*
	 * TODO
	 * quand des sprites de plusieurs cases seront d�finis, peut-�tre que les explosions
	 * devront �tre d�finies en tant que sprites (param�trables pour leur taille) de ce type ?
	 * ce qui permettrait de les faire se d�placer.
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
	
	/*
	 * TODO sprite du bad bomberman dans SBM1
	 * �ventuellement les autres persos du monde avec le ring
	 */
	
	/*
	 * TODO
	 * il faut mieux g�rer l'acc�s aux m�thodes, utiliser plus protected.
	 * surtout pour prot�ger les classes qui devraient �tre inaccessibles depuis l'IA
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
// TRAJECTORIES
// **********************************************************
		
	/*
	 * TODO
	 * quand on pose deux bombes en diagonale et qu'on se place dans le cadrant int�rieur d'une des cases libres du m�me carr�
	 * on est bloqu�. ce n'est pas vraiment un pb en fait, plus un feature. mais les non-initi�s peuvent prendre �a pour un bug.
	 * (note : point mentionn� dans le blog)
	 */

// **********************************************************
// MODULATIONS
// **********************************************************
	/* 
	 * TODO
	 * dans les modulations third et other, la port�e est limit�e � la case (ou aux cases). le contact et surtout le remote ne sont pas encore g�r�s.
	 * id�e pour centraliser le traitement en cas de port�e sans limite:
	 * 	- d�s qu'un sprite change de gesture, ses nouvelles modulations sont analys�es
	 * 	- toutes celles qui sont sans limite de port�e sont stock�es dans un vecteur situ� dans Level (et toutes celles de l'�tat pr�c�dent sont retir�es de ce m�me vecteur)
	 * 	- lors de la validation de 3rdMod, ce vecteur est syst�matiquement test� en plus des sprites situ�s pr�s de l'acteur et de la cible 
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
	
	/*
	 * TODO introduire une anime/action suppl�mentaire : en jeu, un joueur
	 * peut provoquer un autre joueur en d�clenchant une animation sp�cifique.
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
	 *  - quand on balance une bombe, puis une autre, puis fait p�ter la premi�re et enfin la seconde 
	 *  	juste � la fin des flammes de la premi�re, alors l'explosion de la 2nde est circonsrite � la
	 *  	case o� elle atterrit.
	 *  - l'item kick ne marche pas
	 *  - quand un mur est d�truit (d�finitivement) par une penetration bomb, l'item n'apparait pas car
	 *  la flamme de la bombe dure plus longtemps que l'explosion du mur, et d�truit l'item aussit�t qu'il apparait
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
	 * 
	 * 
	 * - bug d'affichage quand les flammes se croisent au cours de plusieurs explosions: la derni�re n'est pas affich�e
	 * - sbm1 : le d�lai de retardement ne doit pas �tre r�initialis� a la suite d'un punch
	 * en fait �a reprend exactement o� �a ne �tait au moment ou la bombe touche le sol
	 * - il y a visiblement un bug dans la GUI quand on commence une partie rapide puis revient au menu principal et en recommence une autre : la pr�c�dente ne semble pas compl�tement r�initialis�e
	 *  >> � voir : p-� simplement une fausse manip. faire : choisir un seul niveau, commencer, recommencer avec un niveau de plus et voir si le nouveau niveau apparait bien dans la nouvelle partie
	 * 
	 * - lors du calcul des points, il faut forcer la pr�sence d'un classement: �a facilite �norm�ment de traitements en aval
	 *   au pire, si le classement est inutile (ex: simple total), on d�finit un classement-identit� (pts utilis�s pr le classement <=> pts marqu�s)
	 * - �a serait bien que les joueurs soient affich�s dans l'ordre relatif aux points de la limite rencontr�e
	 *   voire on d�finit explicitement un ordre d'affichage dans la comp�tition
	 * - faut afficher explicitement le classement � la fin d'une confrontation
	 * - dans les r�sultats :
	 * 		- afficher par d�faut les 4 scores de base
	 * 		- plus les scores utilis�s dans les points et/ou les limites
	 * 		- si les limites utilisent des points custom, les afficher aussi
	 * 
	 * *******************************************************
	 * *********************** A FAIRE ***********************
	 * *******************************************************
	 * 
	 * - GUI : dans les tables, remplacer les labels par une classe custom qui impl�menterait l'interface basicPanel
	 * 
	 * - pouvoir modifier l'UPS pour les IA (pour all�ger le calcul)
	 * 
	 * - profils: simplifier, pas besoin de la double repr�sentation default+selected.
	 * - r�organiser par rapport aux besoins: joueur pdt le jeu, joueur charg� en dehors du jeu, joueur pas charg� ?
	 * 
	 * - faire le classement lexicographique g�rant les signes diacritiques partout o� c'est n�cessaire
	 * 
	 * - modifier le loader d'image de mani�re � ce qu'une image non-trouv�e soit remplac�e par la croix rouge
	 * 
	 * - utiliser les tooltips pour afficher les infos trop longues : calcul de points, nombre � virgule dans la colonne des points (d�cimales cach�es), etc.
	 * - pb de dimension de l'image de fond en fonction de la r�solution... (zones pas peintes)
	 * - results panel : quand il y a trop de rounds dans un match pour que �a rentre � l'�cran, ne pas tout afficher
	 * - possibilit� de donner des noms aux matches et aux rounds
	 * 
	 * - faire un param�tre dans les rounds qui permet de d�terminer s'ils sont compatibles avec le tournoi 2007-08
	 * - tournoi : 1) on choisit les param�tres 2) on choisit les joueurs, le jeu restreint leur nombre pr qu'il soit compatible avec le tournoi, et restreint aussi les IA pour les m�mes raisons
	 * 
	 * - besoin d'une m�thode permettant d'exporter un tournoi/match/round, ie de l'�crire enti�rement en local (pas de r�f�rence � des composants existants)
	 * 
	 * - bug d'affichage dans les notes d'IA, les accents sont affich�s avec la police par d�faut
	 * 
	 * - stats : nombre de fois qu'un level a �t� jou�
	 * - champ en plus dans les profils : le classement du joueur, nbre de rouds gagn�s/nbre de rounds jou�s
	 * - dans les persos : % de rounds gagn�s, ou bien nbre de rounds jou�s
	 * - tout �a est fait simplement en rajoutant les informations ad�quates dans les classes de stat
	 * 
	 * - lors de la s�lection des commandes :
	 * 		- cliquer sur le bouton d'action fait r�agir quelque chose dans la ligne du joueur correspondant
	 * 		- permet de v�rifier qu'on a pris les bonnes commandes (celles qu'on pensait avoir prises)
	 * 
	 * - il faudrait s�parer les joueurs IA et les joueurs humain dans leur gestion.
	 * ca permettrait de s�lectionner directement l'IA, au lieu du joueur, et donc de ne pas avoir � cr�er plusieurs
	 * joueurs avec la m�me IA quand on veut jouer contre plusieurs versions de la m�me IA.
	 * voire limiter le nombre de joueurs pour une IA � 1 seul, mais s�lectionnable plusieurs fois ?
	 * � voir...
	 * 
	 * - SBM1 : copier le rythme du clignotement sur SBM1 (pr invincibilit�)
	 * 			remarque: le clignotement est sens� ralentir quand le temps est presque termin�
	 * 
	 * -------------------------------------------------------------------
	 * - calcul de points : introduire des variables comme le nombre de joueurs (pour d�finir un bonus pr le joueur qui fait un perfect en survival)
	 * - g�rer le shrink
	 * - dans les autorisations, g�rer l'apparition comme une action en soit. si pas possible d'appara�tre au d�but de la partie, faire un atterrissage ?
	 * - � la fin du round, faire apparaitre les r�sultats par transparence
	 * 
	 * - pour painting, possibilit� de d�finir quelles cases peuvent �tre repeintes, ce qui permet de poser comme limite un %age de cases repeintes
	 * - d�finir des noms "human readable" pour les items, histoire de ne pas afficher ces codes internes dans la GUI, en profiter pour introduire une decription, le tout en plusieurs langues. utiliser le code ISO comme param�tre de langue, et l'introduire dans le fichier de langue
	 * - possibilit� de d�finir un nom pour tournament/match/round, qui sera affiche en titre de pr�sentation/stats etc. si pas de nom, utilisation d'un nom g�n�rique (Round 1 - Prensentation) etc
	 * - faire un chargement ad hoc des matches, rounds, etc ? fusionner du coup HollowLevel et LevelPreview ? (voir si les objets de ces deux classes sont cr��s au m�me moment)
	 * 
	 * - redistribution des items lors de la mort d'un joueur (option de round?)
	 * - possibilit� de bloquer certains items (on ne les perd pas lorsqu'on meurt)
	 * 
	 * LIMITES & MODES de jeu :
	 * - limites exprim�es de fa�on relative (peindre 75% des cases, �liminer la moiti� des joueurs...)
	 * - items: 1 item arr�tant la partie, 1 item faisant diminuer le temps restant (anti-temps)
	 * - possibilit� de choisir entre le fait que le round s'arr�te d�s que tout le monde est mort sauf 1, ou derni�re flamme termin�e
	 * reformater les modes de jeu :
	 * 	- pour paint il suffit de d�finir des bombes sp�ciales qui peignent le sol
	 */
	
	
	
	
	
	
	
	
	/*
	 * TODO au lieu d'utiliser une fonction set sprite qui met � jour les roles des acteurs et targets
	 * dans les modulations, il faut effectuer cette mise � jour au chargement des modulations, puisqu'� cet
	 * instant on connait d�j� le role de sprite concern�.
	 * 
	 * - voir si on peut mettre � jour le parser XML
	 * - d�finir la liste de gesture pour chaque type de sprite
	 * - pour chaque gesture, fixer les actions autoris�es 
	 * - adapter tous les loaders au fait que les constantes exprim�es en XML sont maintenant en majuscules, adapter aussi les fichiers xml
	 * 
	 * y a manifestement une diff�rence entre GeneralAction et les actions utilis�es dans les Ability et Modulation
	 * dans ces derni�res, on se place relativement au sprite, qui est actor target ou third.
	 * surement qqchose � faire de ce cot� l�
	 * 
	 * il faut tr�s certainement aussi mutualiser les firesets, puisque les flammes sont les m�mes pour tout le monde.
	 * et aussi les explosions, pour les m�mes raisons.
	 * 
	 * il faut aussi typer les noms d'action et de gesture, car tous les sprites n'ont pas acc�s � tous.
	 * difficile vu qu'on ne peut pas sousclasser les types enum
	 * 
	 * �a ne me plait pas beaucoup ces actions bidons pour tester les abilities de certains sprites. faut r�fl�chir � un truc plus propre
	 * 
	 * le fait d'impl�menter l'ex�cution d'une action dans la SpecificAction correspondante pose des probl�mes car la r�alisation
	 * de l'action d�pend g�n�ralement d'un contexte situ� dans l'event manager ou un autre manager.
	 * il faudrait donc plutot utiliser des m�thodes localis�es dans ces managers.
	 * �a implique que les SpecificAction ne sont pas � sp�cialiser pour chaque role 
	 * 
	 * tester le syst�me qui compl�te les animations automatiquement quand elles sont manquantes dans le fichier XML.
	 * 
	 * il faudrait documenter le comportement par d�faut du moteur, i.e. pour chaque type de sprite:
	 * qu'est-ce qu'il peut faire comme action? quelles sont les transitions? qu'est-ce qui est interdit ?
	 * �a permettra de savoir ce qui peut �tre modul� et ce qui ne peut pas l'�tre
	 * 		- un sprite n'est a priori pas un obstacle, tout est g�r� par modulation (y compris pour le feu)
	 * 
	 *  >> pr la mutualisation de certains aspects des sprites, suffit de rendre explicite
	 *  dans le fichier sprite.xml la localisation des composants (animes, traj, etc) avec
	 *  la possibilit� de les foutre en amont. apriori, seulement valable pr bombes, items et themes
	 *  >>>plus simple: possibilit� de r�utiliser un autre sprite, en (re)d�finissant certains de ses composants
	 * 	pb:
	 * 		- ordre de chargement des sprites. les modeles doivent �tre charg�s en premier. seule m�thode pour �a : scanner tous les sprites dans le dossier (> p-� long)
	 * 		  ou alors faire un chargement r�cursif (si un sprite demand� n'a pas encore �t� charg�, on le fait desuite >> pose de pb de r�organisation hi�rarchique du chargement)
	 * 		- red�finition des �l�ments d�j� d�finis (?)
	 *  >>>> d�finir les sprites r�utilisables � part, comme des sprites abstraits bien distincts des autres
	 *  	- pour bombset : 1) charger tous les composants communs
	 *  					 2) pour les animes, charger seulement les infos n�cessaires
	 *  					 3) �crire dans bombset (ou la structure plus g�n�rale) une fonction d'instanciation permettant de charger la couleur voulue
	 * 
	 * NOTE: les modulations sont ordonn�es par priorit� dans le fichier XML.
	 * dans le cas o� plusieurs modulations peuvent �tre appliqu�es � une action, 
	 * c'est la premi�re d�finie dans le fichier XML qui est utilis�e.
	 * il faut donc l'organiser du plus sp�cifique au plus g�n�ral.
	 * 
	 * le XML des animes doit avoir soit :
	 * 	- NONE seule
	 *  - primaries seules
	 *  - primaries+NONE
	 *  - primaries+COMPOSITES
	 *  - primaries+NONE+composite
	 * et les gestures d�finis doivent au moins contenir les n�cessaires, et au plus les autoris�s
	 *  il va falloir utiliser XSD 1.1 quand �a sera possible, avec les assertions et associations, cf l'email.
	 *  
	 *  NOTE gestion des collisions
	 *  normalement, le syst�me de modulation doit permettre de parfaitement g�rer le d�placement dans les bombes.
	 *  a voir s'il n'est pas possible d'utiliser �a pour simplifier la gestion actuelle des collisions.
	 *  rque: comme on teste les sprites sur des positions virtuelles, qu'ils n'occupent pas encore,
	 *  il faudra construire les actions � tester manuellement (tous les param�tres de type contact, orientation, etc) dans le trajectoryManager
	 *  
	 *  DROP ne devrait pas �tre transitif, mais automatiquement s'appliquer � une bombe.
	 *  �a permettrait de bien s�parer DROP et APPEAR dans l'action (la premi�re pour le sprite posant
	 *  la bombe et la seconde pour la bombe elle m�me) et en m�me temps �a permet
	 *  de ne pas avoir � cr�er la bombe avant de la poser, ce qui parait anti-pratique et pas logique du tout.
	 *  >> connexe: v�rifier qu'avant d'ex�cuter une action, on v�rifie si le sprite concern� (actor) poss�de bien l'ability
	 *  
	 *  faudra mettre en place un syst�me de remplacement (croix rouge?) pr quand une anime n�cessaire n'est pas trouv�e
	 */
	
	/*TODO BUGS
	 * - il semblerait que la direction dans la SpecificAction et dans la GeneralAction correspondante ne soit pas la m�me...
	 * - impossible de poser une bombe quand on est en train de buter contre un mur en direction upleft (et uniquement cette direction pr NES2) et downleft (pr SBM1)
	 * 	>> viendrait de swing ou du clavier (pb mat�riel)
	 * 
	 */
	
	/*TODO instance NES BM2
	 * - item veste ignifug�e (souffle de l'explosion pas impl�ment�)
	 */
	
	/*TODO
	 * - site : parler de la possibilit� d'inclure des bombes dans le niveau, de base (pas vu dans BM officiel, mais tr�s utilis� dans X-Blast)
	 */
	
	// - rajouter la restitution des items ramass�s � la mort du perso
	// - annuler le malus : l'item donne une abilit� sp�cifique "anti-malus"
	// - l'�ventuel malus (bonus aussi?) en cours doit �tre annul� lorsqu'on renait
	//		pour ces trois points: faut associer une dur�e/utilisation � l'item (en plus des ablt) ce qui permettra de le r�initialiser si n�cessaire
	//			>> sauf que toutes les abilities n'ont pas forc�ment la m�me dur�e dans l'item. faut donc un truc au niveau des abilit�s
	//			>> p-� qu'il faut aussi une abilit� "restart when dropped"
	// - emp�cher d'appuyer sur esc quand �a exulte d�j� (p-� en testant la modulation sur exultation?)
}
