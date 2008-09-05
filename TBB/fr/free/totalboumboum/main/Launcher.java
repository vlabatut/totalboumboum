 package fr.free.totalboumboum.main;

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
// IA / TOURNOI
// **********************************************************

	/*
	 * TODO idée : pour faciliter le débug de l'IA : possibilité de controler l'IA (les touches
	 * sont prioritaires sur l'ia). dès qu'on relâche le controle, c'est l'ia qui reprend
	 * la direction du sprite.
	 */	
	
// **********************************************************
// BOMBES
// **********************************************************

	/*
	 * TODO a faire sur les bombes :
	 * - pouvoir les lancer
	 * - les autres comportements issus de XBlast ?
	 */
	
	/*
	 * TODO autoriser de poser une dernière bombe avant de mourir ? (i.e. dans le gesture burning) comme ds XBlast
	 */						
	
	/*
	 * TODO la durée d'explosion d'une bombe et la durée de burning des sprites doivent être égales (?)
	 */
	
	/* 
	 * TODO chaque niveau doit choisir un bombpack
	 * il faut définir un système de couleurs (le même que pour les joueurs)
	 * s'il y a incompatibilité (une couleur d'un joueur n'existe pas dans le bombpack du niveau)
	 * alors un bombpack par défault est utilisé, de la même façon si un type de bombe
	 * n'existe pas dans le bombpack (il est remplacé par une bombe du pack par défaut)
	 */
	
	/*
	 * TODO lorsqu'une bombe est touchée par une flamme, elle bloque la flamme.
	 * ce qu'on voit passer est la flamme résultant de l'explosion de la bombe.
	 */
	
	/*
	 * TODO la première initialisation du délai de la bombe doit être déplacé du bombfactory vers le bombsetmanager
	 * (qu'en est il du délai pour le héro ?)
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
	 * TODO
	 * centraliser le chargement des firesets
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
	 * TODO en réalité,la plupart du temps les sprites factories n'ont pas besoin
	 * de cloner les images : seulement pour les changements de couleurs (bonhommes, bombes, explosions (?) 
	 */

	/*
	 * TODO se faire une liste des tests (en jeu) à effectuer
	 */

	/*
	 * TODO possibilité de définir des règles très générales avec l'EventManager,
	 * en utilisant de nombreuses abilities. par la suite, l'utilisateur n'a qu'à
	 * définir les habilités de ses blocs/items pour effectuer la personnalisation,
	 * les règles ne bougent pas elles.
	 * ex: le bloc indestructible a l'habilité TAKE_COMBUSTION, un tapis roulant ne l'a pas, etc.
	 */

	/*
	 * TODO dans l'EventManager, est-il vraiment nécessaire de synchroniser la méthode update ?
	 * car on appelle une méthode setGesture dans sprite : quel est le risque d'interblocage réel ?
	 */

	/* TODO lorsqu'un sprite est ended :
	 * + il faut virer le sprite partout où il est utilisé : gestionnaire de touches, etc. 
	 * (faire une recherche de référence à la classe hero et sprite)
	 * + si c'est un héros il faut faire péter toutes ses bombes et relâcher tous ses items
	 */	

	//TODO le saut ne doit pas être sensible à la vitesse du personnage	-> à voir !

	/*
	 * TODO que se passe-t-il quand un bonhomme entre dans un mur en train d'exploser ?
	 * > dans SBM2 : rien du tout, il ne meurt pas.
	 */		
	
	/*
	 * TODO plutot que d'utiliser des méthodes synchronisées : 
	 * utiliser une file synchronisée par définition ?
	 */

	/*
	 * TODO gestion des évènements
	 * réviser tous les gestionnaires d'évènements à la lumière des derniers aménagements
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
	 * TODO compléter toutes les durés punch/oscillation bombe (avant punch) manquantes
	 * (dans la configuration de SBMx)
	 */
	
	/*
	 * TODO synchroniser les données accédées par la GUI dans le mode débug :
	 * positions des cases(!), etc
	 */
	
	/*
	 * TODO
	 * en fait les différents sprites devraient autoriser appear :
	 * c'est au sprite apparaissant de décider s'il peut ou pas apparaitre
	 * sur une case déjà occupée (solution plus souple) 
	 * >> mais comment puisque l'action d'apparaître s'applique à une case ?
	 */

	/*
	 * TODO PARAMETRES MEMOIRE
	 * -Xms32m -Xmx512m
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
	 * TODO valeur de l'attribut repeat :
	 * 	anime : répéter l'animation quand elle est finie (jusqu'à changement d'action)
	 * 	trajectory : répéter le déplacement quand il est terminé (jusqu'à changement d'action)
	 * 
	 */

    /* 
     * TODO pour forcer l'unicité d'un attribut/élément, voir ici : 
     * http://rangiroa.essi.fr/cours/internet/02-slides-xml/schema.htm
     */		

	// TODO dans schema de Level : contrainte d'unicité sur les positions des lignes et colonnes    

	/*
	 * TODO dans le fichier de trajectoire, tester que la valeur de durée de forçage 
	 * de position n'est pas supérieur à la durée de la trajectoire
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

	/* TODO la String "type" utilisée dans les loaders est en fait inutile. 
	 * il faut réformer ce type, peut être en l'incluant dans les propriétés.	 * 
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
	 * TODO est-il bien nécessaire de faire la distinction entre les blocs (spécial, dur, mou...) ?
	 * réponse : non, il faut généraliser les blocs. mais pour ça, besoin d'avancer plus dans le codage
	 * des spéciaux, histoire de savoir ce qu'il est nécessaire d'implémenter comme fonctionnalités
	 */	

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
	 * TODO certains paramètres peuvent être configurés pour le niveau et pour le thème.
	 * en fait : y a des réglages par défaut, qui peuvent éventuellement être recouverts par les
	 * réglages du thème, pouvant être recouverts par ceux du niveau, pouvant être recouverts
	 * par ceux de l'utilisateur. ça porte sur : la musique, les interactions entre sprites,
	 * etc. même les trajectoires en fait.
	 */
	
	/*
	 * TODO dans le level, il faut gérer la distribution des items :
	 * 	- nombre fixé ou proportion ?
	 *  - dépendant du nombre de joueurs, ou pas ?
	 *  - chaque item est traité indépendemment, ou on peut les grouper (avec une proportion au sein du groupe) ?
	 */
	
	/*
	 * TODO
	 * pb avec le thème des fleurs : y a une barre d'ombre de 8 pixels de haut
	 * qui s'affiche en haut du niveau (up border)
	 */	
	
	/*
	 * TODO quand les blocs vont être unifiés, il faudra permettre
	 * de créer artificiellement des groupes dans XML/dossiers,
	 * sinon c le bordel : soft, hard, border, special, etc...
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
	 * TODO faire une anime de début de partie : quand le sprite apparait
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
	 * TODO le passage de walking à pushing effectue apparemment un reset, 
	 * car l'animation est coupée. à voir
	 */
	
	/*
	 * TODO animes : il faut définir un système de substitution d'image
	 * de manière à ce qu'il soit impossible d'être pris en défaut
	 */

// **********************************************************
// ABILITIES
// **********************************************************
	
	/*
	 * TODO dans le fichier XML, possibilité d'indiquer quelles
	 * abilités apparaissent/disparaissent lors du passage à un état donné.
	 * les abilités actuellement définies correspondent à un défaut.
	 * >> ça évite de devoir les lister en dur dans l'eventManager
	 */	
	
	/*
	 * TODO les abilities d'un hero dépendent du type de jeu (SBM1, etc) et non
	 * pas du sprite. par contre, pr les autres sprites, ça dépend du niveau/sprite lui-même
	 * donc faut séparer le fichier d'abilities pour les héros
	 */
	
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
	 * TODO item malus : pb de la représentation graphique : tête de mort pour tous, ou design perso ? 
	 * - soit on doit faire un choix (ie pas les deux pr une config donnée)
	 * - soit on peut définir un seul item avec différents états pour effet caché/montré
	 * - soit on définit carrément plusieurs items différents : même effet mais apparences différentes
	 */
	
	/*
	 * TODO explosion:
	 * lorsque la flamme est construite, on teste si chaque case la laisse passer
	 * mais il faut aussi le faire sur la case centrale (origine de l'explosion)
	 * car différents facteurs peuvent limiter l'explosion à une portée de 0
	 * (sans pour autant empêcher l'explosion) 
	 */
	
	/*
	 * TODO quand on calcule l'ablt composite, pour les directions composites :
	 * l'abilité renvoyée peut contenir une direction simplifiée, en fonction 
	 * des autorisations obtenues. 
	 */
	
	/*
	 * TODO le temps d'explosion d'une bombe est propre à la fois à la bombe
	 * (capacité) et au joueur (certains malus la modulent)
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
	 * TODO dans l'eventMgr de la bombe, quand elle oscille, il faut gérer les sprites
	 * qui sont en train de la pousser, et non pas en simple contact.
	 * de plus, il faut que les sprites aient le pouvoir de pousser, sinon ils ne comptent pas.
	 */
	
	/*
	 * TODO pour l'affichage, dans l'idéal il faudrait définir un système de priorités
	 * implémenté par une ablt spéciale posée pour chaque sprite, car il n'est pas
	 * certain que chaque représentation graphique (par ex pr les items) soit équivalente
	 * à l'original en termes de superposition des couches.
	 */
	
	/*
	 * TODO le système de gestion des actions est clairement à améliorer.
	 * certaines actions comme gather sont automatiques. certaines actions ont un effet de zone 
	 * (pr gather : la case).
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
	 * TODO à faire avant d'implémenter l'interface
	 * un level doit permettre de définir : (en plus de ce qui est déjà défini)
	 * 	- nombre de joueurs, position des joueurs
	 *  - nombre/proportion des différents items
	 *  	- un item dans la grille peut être défini ou tiré au sort
	 *  	- idée : définir des items aléatoires, chacun avec sa propre distribution de proba
	 *  - définition des shrinks
	 *  - les ablts et items affectés par défaut aux joueurs
	 */
	
	/* TODO conseils david :
	 * 	- champ final
	 * 	- écriture courte du for pour iterator
	 */
	
	/*
	 * TODO les attributs XML respectent la convention java, et non pas XML (avec tirets)
	 */
	
	/*
	 * TODO règle pour les loaders :
	 * 	- l'objet à charger est créé dans la première méthode appelée lors du chargement
	 *  - les méthode secondaires reçoivent l'objet créé, et le complètent.
	 *  - quand des objets secondaires sont chargés indépendamment, ie dans des méthodes publiques, ils sont eux aussi créés dans la méthode appelée
	 */
	
	/*
	 * TODO améliorer la génération aléatoire de niveau, de manière à ce que ça
	 * soit moins approximatif (utiliser la classe variableTile pour faire le décompte)
	 */
	
	/*
	 * TODO dans un profil, on indique ses couleurs préférées (dans l'ordre) 
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
	 * TODO AAAA au chargement du tournoi, nécessaire de déterminer ce nombre de joueurs max
	 * de même pour match et round
	 */
	
	/*
	 * TODO mettre les IA dans un dossier situé dans les ressources
	 * (modifier le projet pour pouvoir compiler ces classes-là)
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
	 * TODO : désolidariser les définitions de commandes des profils, car ce n'est pas viable pour par ex 16 joueurs.
	 * à la place : définir un ensemble de controlSets, qui devront être affectés aux différents joueurs.
	 * du coup, ça va influencer le nbre de joueurs max: les joueurs sans ia qui n'ont pas de commandes ne peuvent pas jouer. 
	 */
	
	/*
	 * TODO dans le menu des tournaments, il ne faut pas confondre :
	 * 	- charger/continuer une partie déjà commencée
	 * 	- créer une nouvelle partie et la commencer
	 */
	
	/*
	 * TODO il faut récupérer toutes les exceptions éventuellement causées par les IA
	 * par exemple en les routant sur une fenêtre débug spéciale ?
	 * (cf OkanYuksel qui fait un dépassement de la pile)
	 */
	
	/*
	 * TODO voir le problème de collisions quand on passe en 8x
	 * p-e un problème d'arrondi ?
	 * ou alors un saut trop grand (en distance), voir si j'avais implémenté la maximalisation du déplacement
	 * malgré une éventuelle collision... 
	 */
	
	/* TODO REMARQUE PERMANENTE
	 * mettre tout ce qui appelle du swing dans le thread adapté
	 	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	initGui();	
			}
		});
	 */

	/* TODO REMARQUE PERMANENTE
	 * quand une classe est modifiée ou créée, il faut implémenter la fonction finish
	 * qui permet d'effacer toutes les références qu'elle utilisait
	 */

	/*
	 * TODO il faut rendre unique l'identifiant des joueurs
	 * ce qui permet de savoir si y a eu un changement de nom quand un tournoi est chargé
	 */
	
	/*
	 * TODO il faut appliquer à trajectoryMgr le même principe (durée forcée)
	 * que pour animeMgr
	 */
	
	/*
	 * TODO pb lors de l'affichage du résultat d'un match :
	 * le classement des non-gagnants ne respecte pas le total des points...
	 */
	
	/*
	 * A FAIRE :
	 * 1) mettre un peu plus en forme les résultats
	 * 2) définir les présentations des rounds
	 * 3) introduire l'animation d'apparition des persos au début de la partie
	 * 4) passer à un système gérant plusieurs pointProcesseurs
	 */
}
