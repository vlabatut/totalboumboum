 package fr.free.totalboumboum;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.gui.menus.main.MainFrame;
import fr.free.totalboumboum.tools.CalculusTools;


public class Launcher
{	

	public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	new MainFrame();
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
	
// **********************************************************
// IA / TOURNOI
// **********************************************************
	
	/*
	 * TODO il faut récupérer toutes les exceptions éventuellement causées par les IA
	 * par exemple en les routant sur une fenêtre débug spéciale ?
	 * (cf OkanYuksel qui fait un dépassement de la pile)
	 */

	/*
	 * donner carrément aux étudiants des versions light des objets du moteur
	 * > plus pratique pour eux et pour moi, pas d'histoire de code entier, etc.
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
// PROFILS
// **********************************************************
	
	/*
	 * TODO dans un profil, on indique ses couleurs préférées (dans l'ordre) 
	 */
	
	/*
	 * TODO : désolidariser les définitions de commandes des profils, car ce n'est pas viable pour par ex 16 joueurs.
	 * à la place : définir un ensemble de controlSets, qui devront être affectés aux différents joueurs.
	 * du coup, ça va influencer le nbre de joueurs max: les joueurs sans ia qui n'ont pas de commandes ne peuvent pas jouer. 
	 */

	/*
	 * TODO il faut rendre unique l'identifiant des joueurs
	 * ce qui permet de savoir si y a eu un changement de nom quand un tournoi est chargé
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
	
	/*
	 * TODO
	 * pb avec le thème des fleurs : y a une barre d'ombre de 8 pixels de haut
	 * qui s'affiche en haut du niveau (up border)
	 * >>>>en fait c'est partout, et c'est seulement l'ombre des blocs de la ligne d'en bas.
	 * solution : ne pas afficher l'ombre spécifiquement pour les blocs de la dernière ligne
	 */	
	
	/*
	 * TODO améliorer la génération aléatoire de niveau, de manière à ce que ça
	 * soit moins approximatif (utiliser la classe variableTile pour faire le décompte)
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
	 *  - quand des flammes se croisent, il y a des bugs d'affichage (p-ê un pb d'autorisation pour faire entrer une flamme dans la case concernée)
	 */
	
	
	
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
	
	/*
	 * TODO il faut appliquer à trajectoryMgr le même principe (durée forcée)
	 * que pour animeMgr
	 */
	
	/*
	 * TODO couleurs
	 * en fait la couleur dans le profil est une couleur préférée par défaut
	 * mais quand on choisit les joueurs pour un tournoi, ils peuvent changer leur couleur
	 */
	
	/*
	 * TODO tournoi séquence
	 * ça pourrait aussi être un match prédéfini unique, qui se répèterait jusqu'à atteindre une précondition donnée
	 * voire un tirage au sort entre plusieurs matches, comme pour les rounds...
	 */
	
	/*
	 * TODO une fois qu'on a déterminer les nombres de joueurs, y a moyen de gérer les threads de meilleure manière en :
	 * 		- créant un executor au niveau du tournoi
	 * 		- il doit contenir un pool de (nbre d'IA max pvant jouer à la fois)+1(pr loop)
	 * 		- par la suite, au lieu de créer un thread pour chaque ia ou pour le chargement/loop, on en demande un à l'executor
	 * http://java.sun.com/javase/6/docs/api/java/util/concurrent/ExecutorService.html
	 * http://java.sun.com/docs/books/tutorial/essential/concurrency/pools.html
	 * http://java.sun.com/docs/books/tutorial/essential/concurrency/interrupt.html
	 */

	/*
	 * TODO vérifier qu'il n'y a pas d'interférences entre une fin de partie par manque de joueur,
	 * puis la fin temporelle se produit pendant les célébrations
	 */
	
	/*
	 * TODO
	 * *******************************************************
	 * ************************* FAIT ************************
	 * *******************************************************
	 * + alpha.37
	 * - mise en place d'icones à la place des menus horizontaux
	 * - amélioration de l'affichage des résultats
	 * - icones + plus grandes + play en rouge
	 * - revoir le titre des résultats : plus gros, et carré blanc transparent (comme tableau)
	 * - compléter le layout des résultats
	 * 
	 * + alpha.38
	 * - mettre une barre d'évolution pdt le chargement du niveau
	 * - optimisation du chargement : ne charger qu'une seule fois les caractéristiques des joueurs !
	 * - correction de quelques bugs graphiques dans les thèmes de SBM1
	 * - amélioration graphique des icones/boutons du menu horizontal
	 * - optimisation du chargement : 1 thread au lieu d'une séquence de 2 !
	 * - correction d'un bug sur le control manuel d'un perso
	 * 
	 * + alpha.39
	 * - modification de la fenêtre de résultats du round, de manière à afficher les scores (deaths, etc)
	 * - modication de la fenêtre de résultats, pour gagner de la place : icones dans les en-têtes (à la place du texte) 
	 * - correction d'un bug de synchronisation du gestionnaire de délais
	 * - il FAUT un pointProcessor pour le tournament, pour gérer les égalités
	 * - modification du système de points de manière à pouvoir effectuer un classement en considérant plusieurs paramètres complètement ordonnés
	 * - gérer le partage des points en cas d'égalité
	 * - pb lors de l'affichage du résultat d'un match : le classement des non-gagnants ne respecte pas le total des points...
	 * - quand un héro reçoit l'ordre de pleurer, il ne le fait pas s'il est en train de mourir (en fait faut mettre une vraie action avec permissions et tout)
	 * - correction d'un bug quand l'animation de victoire/défaite ne peut pas être activée : il ne faut pas base la fin de la boucle sur la fin de l'animation, mais sur la fin d'un délai indépendant et équivalent
	 * 
	 *   + alpha.40
	 * - image de fond du menu principal
	 * - blocage des boutons quit et match pendant le chargement du round
	 * - bug corrigé : collision bizarre, les flammes ne peuvent pas se croiser...
	 * - changement du système de touches de controle du jeu (debug) : inversion de F3 et F4 et ESC pour arrêter prématurément une partie
	 * - pb de temps NEGATIF quand j'arrête la partie avec esc, plus bouton encore actif
	 * - pb d'affichage des résultats quand trop de rounds : taille minimale du nom fixée à la taille de l'icone, et tooltip avec le nom complet
	 * - tout petit pb de synchronisation de l'animation de fin de partie avec la fin effective du panel d'animation
	 * - correction du bug qui faisait activer le GC et ramer le jeu au cours du deuxième match.
	 * - la taille minimale du nom dans les résultats est la largeur de l'icone
	 * - mutualisation des éléments de la GUI : tableaux, etc
	 * - réorganisation de la partie language
	 * - correction d'un bug d'affichage dans le raffraichissement des boutons
	 * - assombrir l'image de fond quand y a des tables par dessus
	 * 
	 *   + alpha.41
	 * - correction de quelques bugs d'affichage concernant les thèmes et apparaissant à haute résolution
	 * - gestion des limites plus détaillée, que ce soit pour le tournoi, le match ou le round
	 * - définition des rounds dans des fichiers séparés
	 * - lors du match : clonage de round pour pouvoir les réutiliser (si la limite de confrontation est supérieure au nombre de rounds prévus)
	 * - différenciation des limites de tournoi/match/round : interfaces différentes, types xsd différents
	 * - dans le round, la limite de temps devient une limite normale
	 * - dans le tournoi séquence, on gère l'ordre aléatoire de matches
	 * - dans le match, il faut gérer différemment la limite : 
	 * - les capacités initiales ont été transformées en items initiaux
	 * - les levels contiennent maintenant un certain nombre d'items initiaux, donnés à chaque joueur au début du round
	 * - modification du système de points, de manière à considérer à part un classement auquel on associe les points finaux
	 * - système permettant d'afficher le calcul des points sous forme textuelle (sera notamment utilisé par la GUI)
	 * 
	 * + alpha.42
	 * - mise en place d'un système de previews pour les Levels et Sprites
	 * - correction d'un bug qui affectait le décompte de kills et de deaths au cours d'un round
	 * 
	 * *******************************************************
	 * *********************** A FAIRE ***********************
	 * *******************************************************
	 * - définir les présentations des rounds
	 * - faire deux colonnes pour les items initiaux et les limites du round
	 * - l'emplacement de l'itemset du round doit permettre de contenir des cases carrées sans perte de place
	 * - pb : les items distribués au début du match ne doivent pas être comptés dans les stats !
	 * - redistribution des items lors de la mort d'un joueur (option de round?)
	 * - possibilité de bloquer certains items (on ne les perd pas)
	 * - au moins finir le cycle lors d'une mort, histoire que la différence de timing ne vienne pas juste de l'ordre des joueurs dans la partie 
	 * - possibilité de choisir entre le fait que le match s'arrête dès que tout le monde est mort sauf 1, ou dernière flamme terminée
	 * - feature lié au précédent : gagner plus de points si on finit effetivement le jeu que si on a un time out ou un entre-tuage
	 * - possibilité de donner des noms aux matches et aux rounds
	 * - gérer le shrink
	 * - la bombe en panne ne bouge plus !
	 * - utiliser les tooltips pour afficher les infos trop longues : calcul de points, nombre à virgule dans la colonne des points (décimales cachées), etc.
	 * - un bug apparait parfois : le résultat du match ne correspond pas au classement réel, et les temps affichés non plus
	 * - problème de collisions quand on change la vitesse (surement un problème d'arrondi, ou alors un saut trop grand (en distance), voir si j'avais implémenté la maximalisation du déplacement malgré une éventuelle collision...)
	 * - revoir le système des IA
	 * - redescendre les stats dans loop, et gestion de fin de partie et tout ce qui est en fait directement lié au moteur
	 * - en fait tout le process de points dans les stats est à déplacer dans les rounds/matches, etc 
	 * - à la fin du round, faire apparaitre les résultats par transparence...ça serait la classe ça !
	 * - gérer l'apparition comme une action en soit. si pas possible d'apparaître au début de la partie, faire un atterrissage ?
	 * - problème graphique (contact des ombres, notament) quand on monte en résolution avec certain thèmes
	 * - pb de dimension de l'image de fond en fonction de la résolution... (zones pas peintes)
	 * - results panel : quand il y a trop de rounds dans un match pour que ça rentre à l'écran, ne pas tout afficher
	 * - problème d'affichage : le niveau est coupé en fonction du mode d'affichage, mais pas de façon symétrique (le haut n'est pas coupé, seulement le bas)
	 * - mode plein écran
	 * - rétablir frag comme stat ? non ! 
	 * - vérifier le temps de latence des bombes, ça me parait un peu trop rapide
	 * -------------------------------------------------------------------
	 * - s'occuper de la limite qui fait gagner le joueur qui la franchit : pq pas un simple bonus/malus pour celui qui arrête la partie ?
	 * - limites exprimées de façon relative (peindre 75% des cases...)
	 * - normaliser le calcul des points (discrétisation spéciale ou nombre de points direct), ce qui permettra d'en normaliser la représentation graphique dans la GUI
	 * - pour painting, possibilité de définir quelles cases peuvent être repeinte, ce qui permet de poser comme limite un %age de cases repeintes
	 */
	
	/*
	 * reformater les modes de jeu :
	 * 	- virer les modes de jeu, car ils sont inutiles à part pour paint
	 * 	- pour paint il suffit de définir des bombes spéciales qui peignent le sol
	 *  - pour les autres, tout peut se faire avec le système de limites de match
	 *  - pour survival, il suffit d'utiliser une limite de type last-standing
	 *  - on rajoute également le système de bonus pour une limite donnée
	 *  - intérêt : tout ça permet d'implémenter le bonus à celui qui tue tout le monde en survival, par opposition à une victoire temporelle qui rapporterait donc moins de points
	 *  - LA NOTION DE PLAYMODE EST A SUPPRIMER 
	 */
}
