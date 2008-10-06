package fr.free.totalboumboum;

public class ChangeLog
{
}

/*
 * + alpha.37
 * - mise en place d'icones � la place des menus horizontaux
 * - am�lioration de l'affichage des r�sultats
 * - icones + plus grandes + play en rouge
 * - revoir le titre des r�sultats : plus gros, et carr� blanc transparent (comme tableau)
 * - compl�ter le layout des r�sultats
 * 
 * + alpha.38
 * - mettre une barre d'�volution pdt le chargement du niveau
 * - optimisation du chargement : ne charger qu'une seule fois les caract�ristiques des joueurs !
 * - correction de quelques bugs graphiques dans les th�mes de SBM1
 * - am�lioration graphique des icones/boutons du menu horizontal
 * - optimisation du chargement : 1 thread au lieu d'une s�quence de 2 !
 * - correction d'un bug sur le control manuel d'un perso
 * 
 * + alpha.39
 * - modification de la fen�tre de r�sultats du round, de mani�re � afficher les scores (deaths, etc)
 * - modication de la fen�tre de r�sultats, pour gagner de la place : icones dans les en-t�tes (� la place du texte) 
 * - correction d'un bug de synchronisation du gestionnaire de d�lais
 * - il FAUT un pointProcessor pour le tournament, pour g�rer les �galit�s
 * - modification du syst�me de points de mani�re � pouvoir effectuer un classement en consid�rant plusieurs param�tres compl�tement ordonn�s
 * - g�rer le partage des points en cas d'�galit�
 * - pb lors de l'affichage du r�sultat d'un match : le classement des non-gagnants ne respecte pas le total des points...
 * - quand un h�ro re�oit l'ordre de pleurer, il ne le fait pas s'il est en train de mourir (en fait faut mettre une vraie action avec permissions et tout)
 * - correction d'un bug quand l'animation de victoire/d�faite ne peut pas �tre activ�e : il ne faut pas base la fin de la boucle sur la fin de l'animation, mais sur la fin d'un d�lai ind�pendant et �quivalent
 * 
 *   + alpha.40
 * - image de fond du menu principal
 * - blocage des boutons quit et match pendant le chargement du round
 * - bug corrig� : collision bizarre, les flammes ne peuvent pas se croiser...
 * - changement du syst�me de touches de controle du jeu (debug) : inversion de F3 et F4 et ESC pour arr�ter pr�matur�ment une partie
 * - pb de temps NEGATIF quand j'arr�te la partie avec esc, plus bouton encore actif
 * - pb d'affichage des r�sultats quand trop de rounds : taille minimale du nom fix�e � la taille de l'icone, et tooltip avec le nom complet
 * - tout petit pb de synchronisation de l'animation de fin de partie avec la fin effective du panel d'animation
 * - correction du bug qui faisait activer le GC et ramer le jeu au cours du deuxi�me match.
 * - la taille minimale du nom dans les r�sultats est la largeur de l'icone
 * - mutualisation des �l�ments de la GUI : tableaux, etc
 * - r�organisation de la partie language
 * - correction d'un bug d'affichage dans le raffraichissement des boutons
 * - assombrir l'image de fond quand y a des tables par dessus
 * 
 *   + alpha.41
 * - correction de quelques bugs d'affichage concernant les th�mes et apparaissant � haute r�solution
 * - gestion des limites plus d�taill�e, que ce soit pour le tournoi, le match ou le round
 * - d�finition des rounds dans des fichiers s�par�s
 * - lors du match : clonage de round pour pouvoir les r�utiliser (si la limite de confrontation est sup�rieure au nombre de rounds pr�vus)
 * - diff�renciation des limites de tournoi/match/round : interfaces diff�rentes, types xsd diff�rents
 * - dans le round, la limite de temps devient une limite normale
 * - dans le tournoi s�quence, on g�re l'ordre al�atoire de matches
 * - dans le match, il faut g�rer diff�remment la limite : 
 * - les capacit�s initiales ont �t� transform�es en items initiaux
 * - les levels contiennent maintenant un certain nombre d'items initiaux, donn�s � chaque joueur au d�but du round
 * - modification du syst�me de points, de mani�re � consid�rer � part un classement auquel on associe les points finaux
 * - syst�me permettant d'afficher le calcul des points sous forme textuelle (sera notamment utilis� par la GUI)
 * 
 * + alpha.42
 * - mise en place d'un syst�me de previews pour les Levels et Sprites
 * - refonte du chargement de niveau pour l'adapter au syst�me de preview
 * - correction d'un bug qui affectait le d�compte de kills et de deaths au cours d'un round
 * - correction d'un probl�me d'affichage (je sais pas trop comment, d'ailleurs...) : le niveau est coup� en fonction du mode d'affichage, mais pas de fa�on sym�trique (le haut n'est pas coup�, seulement le bas)
 * - mise en place du fond d'�cran du jeu � la place des bordures unies lors des parties
 * - correction d'un bug d'optimisation : le format des images n'�tait pas compatible avec l'environnement graphique, ce qui ralentissait le raffraichissement
 * - correction d'un bug : nanoTime est bugg� quand on utilise des syst�mes multicoeurs : j'ai utilis� currentTimeMillis � la place
 * - mise en place de l'acc�l�ration graphique avec l'utilisation d'une VolatileImage pour le panel
 * - mise en place d'un compteur FPS/UPS
 * - correction d'un bug dans le d�compte du temps effectu� dans la boucle principale
 * - correction d'un bug : les items distribu�s au d�but du match �taient compt�s dans les stats
 * - d�finition du paneau de pr�sentation pour les rounds
 * - modification de la police pour y faire appara�tre les caract�res manquants (essentiellement des op�rateurs math�matiques)
 * - correction d'un bug graphique qui faisait appara�tre l'ombre de la derni�re ligne de blocs par dessus la premi�re
 * - correction d'un bug XML dans le th�me superbomberman1.duel
 * 
 * + alpha.43
 * - Modification	: am�lioration du syst�me al�atoire de g�n�ration des niveaux (les distributions doivent �tre contr�l�es car les s�ries g�n�r�es sont trop courtes pour que les distributions soient respect�es)
 * - Modification	: r�organisation du projet Eclipse de mani�re � placer les classes d'ia dans le dossier resources
 * - Modification	: r�vision du syst�me d'adaptateur pour les IA de 2007-08
 * - Modification	: FPS baiss� de 100 � 50, afin d'�conomiser les ressources et d'�viter que �a rame trop sur des petites machines (ou pas acc�l�r�es)
 * - Nouveaut�		: adaptateur sp�cial pour les IA 2008-09
 * - Correction 	: petit bug dans la GUI, les dimensions des niveaux �taient fausses
 * 
 * + alpha.44
 * - Modification	: am�lioration de la structure de donn�es destin�es aux IA 2008-09 et repr�sentant la zone et son contenu
 * - Nouveaut� 		: IA d'exemple pour le package 2008-09
 * - Correction		: bug qui emp�chait un bon d�compte des kills/deaths (diff�rent du premier!)
 * - Nouveaut�		: niveau plus petit pour tester la nouvelle IA
 * - Nouveaut�		: syst�me pour controler que le thread d'une IA ne reste pas bloqu� dans une bloucle infinite (ou autre)
 * - Modification	: plus d'icones dans la GUI notamment pour les points processors
 * - Nouveaut�		: cr�ation d'un splash screen, qui �num�re les �tapes d'initialisation et permet d'avoir un Graphics pour l'init de la GUI avant de faire appara�tre le frame
 * - Modification	: meilleure d�composition du fichier de langue, en plusieurs sections plus lisibles
 * - Modification	: changement d'ic�ne pour les points, les stats et les points custom
 * - Modification	: centrage de la fen�tre principale � la cr�ation
 * 
 * + alpha.45
 * - Nouveaut�		: scripts de lancement/compilation pour Windows et BASH
 * - Correction		: utilisation dans la GUI d'une m�thode qui provoque une exception sous Linux, quand on essaie de convertir une image en niveaux de gris avec BufferedImageOp. M�thode alternative employ�e � la place.
 * - Nouveaut�		: documents relatifs � la licence GPL
 * - Nouveaut�		: fichiers d'information dans la racine du projet  
 * - Nouveaut�		: commentaires complets des classes d'IA
 */