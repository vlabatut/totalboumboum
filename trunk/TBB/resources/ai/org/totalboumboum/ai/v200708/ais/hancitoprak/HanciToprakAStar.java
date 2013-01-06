package org.totalboumboum.ai.v200708.ais.hancitoprak;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Aslıhan Hanci
 * @author Emine Canan Toprak
 *
 */
public class HanciToprakAStar {

	/** The set of nodes that have been searched through */
	private List<HanciToprakNode> closed = new ArrayList<HanciToprakNode>();
	/** The set of nodes that we do not yet consider fully searched */
	private HanciToprakSortedList open = new HanciToprakSortedList();
	/** */
	public List<HanciToprakNode> path = new ArrayList<HanciToprakNode>();
	/** */
	private HanciToprakNode[][] nodes;

	/** */
	HanciToprakNode debut;
	/** */
	HanciToprakNode fin;
	/** */
	private int[][] pos = {
			{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },// 0 //ceci est un
			// exemple
			// d'utilsation
			{ 2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2 },// 1
			{ 2, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 2 },// 2
			{ 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2 },// 3
			{ 2, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 2 },// 4
			{ 2, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2 },// 2
			{ 2, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 2 },// 6
			{ 2, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2 },// 7
			{ 2, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 2 },// 8
			{ 2, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 2 },// 9
			{ 2, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 2 },// 10
			{ 2, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 2 },// 11
			{ 2, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 2 },// 12
			{ 2, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 2 },// 13
			{ 2, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 2 },
			{ 2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2 },
			{ 2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2 } };// 14

	/**
	 * 
	 * @param debut
	 * @param fin
	 */
	public HanciToprakAStar(HanciToprakNode debut, HanciToprakNode fin) {

		this.debut = debut;
		this.fin = fin;

		nodes = new HanciToprakNode[posDimX()][posDimY()];
		for (int x = 0; x < posDimX(); x++) {
			for (int y = 0; y < posDimY(); y++) {
				nodes[x][y] = new HanciToprakNode(x, y);
			}
		}

	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	protected int posDimX() {
		return pos.length;
	}

	/**
	 * Renvoie la hauteur de la zone de jeu
	 * 
	 * @return la hauteur de la matrice
	 */
	protected int posDimY() {
		return pos[0].length;
	}

	/**
	 * 
	 * @param debut
	 */
	public void findPath(HanciToprakNode debut) {

		// int xc, yc;
		// open.clear();
		// closed.clear();
		// HanciTprkNode courante = debut;
		// HanciTprkNode memoire = null;
		// debut.setCost(0);
		//
		// while (!inClosedList(fin)) {
		//
		// if (!inClosedList(courante)) {
		// closed.add(courante);
		//
		// xc = courante.getX();
		// yc = courante.getY();
		//
		// if ((yc >= 2) && (pos[xc][yc - 1] != 1))
		// ajoutOuverte(courante, (new HanciTprkNode(xc, yc - 1)));// ////
		// if ((yc < 14 - 1) && (pos[xc][yc + 1] != 1))
		// ajoutOuverte(courante, (new HanciTprkNode(xc, yc + 1)));// ////
		// if (xc < 16 - 1) {
		// if ((pos[xc + 1][yc] != 1))
		// ajoutOuverte(courante, (new HanciTprkNode(xc + 1, yc)));// /////
		// if ((yc >= 2)
		// && (pos[xc + 1][yc - 1] != 1)
		// && !((pos[xc][yc - 1] == 1) || (pos[xc + 1][yc] == 1)))
		// ajoutOuverte(courante, (new HanciTprkNode(xc + 1,
		// yc - 1)));
		// if ((yc < 14 - 1)
		// && (pos[xc + 1][yc + 1] != 1)
		// && !((pos[xc + 1][yc] == 1) || (pos[xc][yc + 1] == 1)))
		// ajoutOuverte(courante, (new HanciTprkNode(xc + 1,
		// yc + 1)));
		// }
		// if (xc >= 2) {
		// if ((pos[xc - 1][yc] != 1))
		// ajoutOuverte(courante, (new HanciTprkNode(xc - 1, yc)));
		// if ((yc >= 2)
		// && (pos[xc - 1][yc - 1] != 1)
		// && !((pos[xc - 1][yc] == 1) || (pos[xc][yc - 1] == 1)))
		// ajoutOuverte(courante, (new HanciTprkNode(xc - 1,
		// yc - 1)));
		// if ((yc < 14 - 1)
		// && (pos[xc - 1][yc + 1] != 1)
		// && !((pos[xc - 1][yc] == 1) || (pos[xc][yc + 1] == 1)))
		// ajoutOuverte(courante, (new HanciTprkNode(xc - 1,
		// yc + 1)));
		// }
		//
		// memoire = courante;
		//
		// }
		//
		// open.remove(courante);
		//
		// if (open.size() == 0) {
		// if (Math.abs(memoire.getX() - fin.getX()) >= 1
		// && Math.abs(memoire.getY() - fin.getY()) >= 1)// pour
		// // permettre
		// // les
		// // sauts
		// // des
		// // coins
		// // des
		// // murs
		// // remplacer
		// // >=
		// // par >
		// // et &&
		// // par
		// // ||
		// {
		// System.out
		// .println("Il n'y a pas de chemin entre ces deux point !!!");
		// break;
		// } else
		// break;
		// }
		//
		// courante=getMinF();
		//
		// }
		//
		// fin.setParent(memoire);

//		System.out.println("findPathteyim ");
		int xc;
		int yc;
		open.clear();
		closed.clear();

		HanciToprakNode courante = debut;
		HanciToprakNode temp_node = null;

//		System.out.println("debut: " + debut.getX() + debut.getY());
//		System.out.println("fin: " + fin.getX() + fin.getY());

		courante.setCost(0);
		// courante.setParent(null);
		open.add(courante);

//		System.out.println("opensize: " + open.size());
		while (!inClosedList(fin)) {
//			System.out.println("whilea dondum");
//			System.out.println("opensize: " + open.size());
			if (!inClosedList(courante)) {
				closed.add(courante);
//				System.out.println("!inClosedList(courante):" + courante.getX()
//						+ courante.getY());

				xc = courante.getX();
				yc = courante.getY();

				if ((yc >= 2) && (pos[xc][yc - 1] != 2)) {
					System.out
							.println("(yc >= 2) && (pos[xc][yc - 1] != 2) courente:"
									+ courante
									+ "(new HanciTprkNode(xc, yc - 1))"
									+ xc
									+ (yc - 1));
					ajoutOuverte(courante, (new HanciToprakNode(xc, yc - 1)));

				}
				if ((yc < posDimY() - 1) && (pos[xc][yc + 1] != 2)) {
					System.out
							.println("(yc < posDimY() - 1) && (pos[xc][yc + 1] != 2)) courente:"
									+ courante
									+ "(new HanciTprkNode(xc, yc + 1))"
									+ xc
									+ (yc + 1));
					ajoutOuverte(courante, (new HanciToprakNode(xc, yc + 1)));
				}

				if (xc < posDimX() - 1) {

					if ((pos[xc + 1][yc] != 2))
						ajoutOuverte(courante, (new HanciToprakNode(xc + 1, yc)));
					if ((yc >= 2)
							&& (pos[xc + 1][yc - 1] != 2)
							&& !((pos[xc][yc - 1] == 2) || (pos[xc + 1][yc] == 2)))
						ajoutOuverte(courante, (new HanciToprakNode(xc + 1,
								yc - 1)));
					if ((yc < posDimY() - 1)
							&& (pos[xc + 1][yc + 1] != 2)
							&& !((pos[xc + 1][yc] == 2) || (pos[xc][yc + 1] == 2)))
						ajoutOuverte(courante, (new HanciToprakNode(xc + 1,
								yc + 1)));
				}
				if (xc >= 2) {
					if ((pos[xc - 1][yc] != 2))
						ajoutOuverte(courante, (new HanciToprakNode(xc - 1, yc)));
					if ((yc >= 2)
							&& (pos[xc - 1][yc - 1] != 2)
							&& !((pos[xc - 1][yc] == 2) || (pos[xc][yc - 1] == 2)))
						ajoutOuverte(courante, (new HanciToprakNode(xc - 1,
								yc - 1)));
					if ((yc < posDimY() - 1)
							&& (pos[xc - 1][yc + 1] != 2)
							&& !((pos[xc - 1][yc] == 2) || (pos[xc][yc + 1] == 2)))
						ajoutOuverte(courante, (new HanciToprakNode(xc - 1,
								yc + 1)));

				}
//				System.out.println("temp_node = courante");
				temp_node = courante;

			}
			open.remove(courante);

			if (open.size() == 0) {
				if (Math.abs(temp_node.getX() - fin.getX()) >= 1
						&& Math.abs(temp_node.getY() - fin.getY()) >= 1) {
					System.out
							.println("Il n'y a pas de chemin entre ces deux point !!!");
					break;
				} else
					break;
			}

			// System.out.println("courante = (HanciTprkNode) open.first()");
			courante = (HanciToprakNode) open.first();
		}

		fin.setParent(temp_node);

//		System.out.println("findPatht bitti ");
	}

	/**
	 * 
	 * @param courante
	 * @param adjacente
	 */
	public void ajoutOuverte(HanciToprakNode courante, HanciToprakNode adjacente) {
//		System.out.println("ajoutouvertteyim");
		int g = courante.getCost()
				+ ((adjacente.getX() == courante.getX() || adjacente.getY() == courante
						.getY()) ? 10 : 15);
//		System.out.println("gsi:" + g);
		int h = (Math.abs(adjacente.getX() - fin.getX()) + Math.abs(adjacente
				.getY()
				- fin.getY()));
//		System.out.println("hsi:" + h);
		int f = g + h;
//		System.out.println("fsi:" + f);
		// for(int i=0;i<open.size();i++)
		// System.out.println(open.get(i).getX()+open.get(i).getY());
		if (inOpenList(adjacente)) {
//			System.out.println("inOpenList(adjacente)");
			if (adjacente.getF() > f) {
				adjacente.setCost(g);
				adjacente.setF(f);
				adjacente.setParent(courante);
			}
		} else if (!isInList(adjacente, closed)) {
			System.out
					.println("inOpenList(adjacente) diil ve !inClosedList(adjacente)");
			adjacente.setCost(g);
			adjacente.setHeuristic(h);
			adjacente.setF(f);
			adjacente.setParent(courante);
			open.add(adjacente);

//			System.out.println("ajoutouvertte sunu ekledim: "
//					+ adjacente.getX() + " " + adjacente.getY());
		}

	}

	/**
	 * 
	 */
	public void Path() {

		HanciToprakNode current = fin;
		while (current.getParent() != null) {
			path.add(current);
			current = current.getParent();
		}

	}

	/**
	 * 
	 * @param debut
	 * @param fin
	 * @param matrix
	 */
	public void initialiser(HanciToprakNode debut, HanciToprakNode fin,
			int[][] matrix) {
//		System.out.println("initialisenin icindeyim");
//		System.out.println();

		this.debut = debut;
		this.fin = fin;
		for (int i1 = 0; i1 < matrix[0].length; i1++) {
			for (int i2 = 0; i2 < matrix.length; i2++) {

				// if(matrix[i2][i1]==0)
				// System.out.print("000 ");
				// else
//				System.out.print(matrix[i2][i1] + " ");
			}
//			System.out.println();
		}
//		System.out.println("bizim matris");

		for (int i1 = 0; i1 < pos[0].length; i1++) {
			for (int i2 = 0; i2 < pos.length; i2++) {

				// if(matrix[i2][i1]==0)
				// System.out.print("000 ");
				// else
//				System.out.print(pos[i2][i1] + " ");
			}
//			System.out.println();
		}
//		System.out.println("obur matris");

//		for (int i = 0; i < 17; i++) {
		for (int i = 0; i < pos.length; i++) { // adjustment
//			for (int j = 0; j < 15; j++) { // adjustment
			for (int j = 0; j < pos[0].length; j++) {
				pos[i][j] = matrix[i][j];
				nodes[i][j].setCost(matrix[i][j]);

			}
		}
//		System.out.println("initialisenin icinde forlarti bitrdim");

		findPath(debut);
		Path();
	}

	/**
	 * Check if a node is in the open list
	 * 
	 * @param node
	 *            The node to check for
	 * @return True if the node given is in the open list
	 */
	public boolean inOpenList(HanciToprakNode node) {
		return open.contains(node);
	}

	/**
	 * Check if the node supplied is in the closed list
	 * 
	 * @param node
	 *            The node to search for
	 * @return True if the node specified is in the closed list
	 */
	public boolean inClosedList(HanciToprakNode node) {
		return closed.contains(node);
	}

	/**
	 * 
	 * @param courante
	 * @param list
	 * @return
	 * 		? 
	 */
	public boolean isInList(HanciToprakNode courante,
			List<HanciToprakNode> list) {
		Iterator<HanciToprakNode> ito = list.iterator();
		while (ito.hasNext()) {
			if (ito.next().equals(courante))
				return true;
		}
		return false;
	}

	// HanciTprkNode getMinF() {
	// HanciTprkNode min = null;
	// min = open.get(0);
	// Iterator<HanciTprkNode> fIt = open.iterator();
	//
	//		while (fIt.hasNext()) {
	//			min = compareF(min, fIt.next());
	//		}
	//		return min;
	//
	//	} 
	
	/**
	 * @param cF1 
	 * @param cF2 
	 * @return 
	 * 		?
	 */
	HanciToprakNode compareF(HanciToprakNode cF1, HanciToprakNode cF2) {
		if (cF1.getF() < cF2.getF())
			return cF1;
		return cF2;
	}

}
