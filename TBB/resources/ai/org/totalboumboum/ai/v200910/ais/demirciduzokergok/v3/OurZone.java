package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v3;

import java.util.*;

import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;

public class OurZone {
	// Les constructeurs:
	@SuppressWarnings("unused")
	private AiZone our_zone;
	private AiHero our_bomberman;
	private Collection<AiHero> enemies;
	private Collection<AiBomb> bombs;
	private Collection<AiItem> items;
	private Collection<AiBlock> walls;
	private Collection<AiFire> fires;
	public int width;
	public int height;
	private int pos_x, pos_y;
	private Cases zone_matrix[][];

	public OurZone(AiZone zone) {
		this.our_zone = zone;
		this.enemies = zone.getHeroes();
		this.bombs = zone.getBombs();
		this.items = zone.getItems();
		this.walls = zone.getBlocks();
		this.fires = zone.getFires();
		this.our_bomberman = zone.getOwnHero();
		this.width = zone.getWidth();
		this.height = zone.getHeight();
		// Notre fonction pour remplir la matrice:
		FillTheMatrix();

	}

	private void FillTheMatrix() {
		// initialise avec la case "Safe_Case":
		zone_matrix = new Cases[height][width];
		for (int w = 0; w < width; w++) {

			for (int h = 0; h < height; h++)
				zone_matrix[h][w] = Cases.Safe_Case;
		}

		// placer les murs:
		Iterator<AiBlock> blck_itr =walls.iterator();
		AiBlock block_temp;
		while (blck_itr.hasNext() == true) {

			block_temp = blck_itr.next();

			pos_x = block_temp.getLine();
			pos_y = block_temp.getCol();
			if (block_temp.isDestructible() == true)
				zone_matrix[pos_x][pos_y] = Cases.Wall_D;
			else
				zone_matrix[pos_x][pos_y] = Cases.Wall_ND;
		}
		
		// placer des bombes:
		Iterator<AiBomb> bomb_itr =	bombs.iterator();
		AiBomb bomba2;

		while (bomb_itr.hasNext()) {
			bomba2 = bomb_itr.next();

			pos_x = bomba2.getLine();
			pos_y = bomba2.getCol();
			zone_matrix[pos_x][pos_y] = Cases.Bomb;
		}

		// placer des bonus:
		Iterator<AiItem> bns_itr = items.iterator();
		AiItem item_temp;

		while (bns_itr.hasNext() == true) {

			item_temp = bns_itr.next();

			pos_x = item_temp.getLine();
			pos_y = item_temp.getCol();
			if (zone_matrix[pos_x][pos_y] != Cases.Fire)
				zone_matrix[pos_x][pos_y] = Cases.Bonus;
		}

		//pour les ranges
		AiBomb b;
		Iterator<AiBomb> danger = bombs.iterator();
		while (danger.hasNext()) {
			b = danger.next();			
			
			List <AiTile> Blast=b.getBlast();
			Iterator<AiTile> tile=Blast.iterator();
			AiTile feufutur;
			while (tile.hasNext())
			{
				feufutur=tile.next();
				pos_x=feufutur.getLine();
				pos_y=feufutur.getCol();
				if (zone_matrix[pos_x][pos_y]!=Cases.Wall_D && zone_matrix[pos_x][pos_y]!=Cases.Wall_ND && zone_matrix[pos_x][pos_y]!=Cases.Bomb)
					zone_matrix[pos_x][pos_y]=Cases.Range_Of_Fire;
			}				
		}

		// placer des feux:
		Iterator<AiFire> fire_itr = fires.iterator();
		AiFire fire_temp;
		while (fire_itr.hasNext() == true) {

			fire_temp = fire_itr.next();

			pos_x = fire_temp.getLine();
			pos_y = fire_temp.getCol();
			zone_matrix[pos_x][pos_y] = Cases.Fire;
		}


		// les cases dangeruese
		AiBomb bomba;
		Iterator<AiBomb> danger1 =bombs.iterator();
		while (danger1.hasNext()) {

			bomba = danger1.next();

			pos_x = bomba.getLine();
			pos_y = bomba.getCol();

			for (int count = 1; count <= bomba.getRange(); count++) {
				if (pos_x - count >=0 &&zone_matrix[pos_x - count][pos_y] != Cases.Wall_D
						&& zone_matrix[pos_x - count][pos_y] != Cases.Wall_ND) {
					if (pos_x - count >= 0
							&& (zone_matrix[pos_x - count][pos_y] == Cases.Bomb))
						zone_matrix[pos_x - count][pos_y] = Cases.Danger;
					else if (pos_x - count >= 0
							&& (zone_matrix[pos_x - count][pos_y] == Cases.Bonus))
						zone_matrix[pos_x - count][pos_y] = Cases.Danger;
					else
						break;
				}
			}

			for (int count = 1; count <= bomba.getRange(); count++) {
				if ( pos_y - count >=0 && zone_matrix[pos_x][pos_y - count] != Cases.Wall_D
						&& zone_matrix[pos_x][pos_y - count] != Cases.Wall_ND) {
					if (pos_y - count >= 0
							&& (zone_matrix[pos_x][pos_y - count] == Cases.Bomb))
						zone_matrix[pos_x][pos_y - count] = Cases.Danger;
					else if (pos_y - count >= 0
							&& (zone_matrix[pos_x][pos_y - count] == Cases.Bonus))
						zone_matrix[pos_x][pos_y - count] = Cases.Danger;
					else
						break;
				}
			}

			for (int count = 1; count <= bomba.getRange(); count++) {

				if ( pos_x + count< height && zone_matrix[pos_x + count][pos_y] != Cases.Wall_D
						&& zone_matrix[pos_x + count][pos_y] != Cases.Wall_ND) {
					if (pos_x + count < height
							&& (zone_matrix[pos_x + count][pos_y] == Cases.Bomb))
						zone_matrix[pos_x + count][pos_y] = Cases.Danger;
					else if (pos_x + count < height
							&& (zone_matrix[pos_x + count][pos_y] == Cases.Bonus))
						zone_matrix[pos_x + count][pos_y] = Cases.Danger;
					else
						break;
				}
			}

			for (int count = 1; count <= bomba.getRange(); count++) {
				if ( pos_y + count < width && zone_matrix[pos_x][pos_y + count] != Cases.Wall_D
						&& zone_matrix[pos_x][pos_y + count] != Cases.Wall_ND) {
					if (pos_y + count < width
							&& (zone_matrix[pos_x][pos_y + count] == Cases.Bomb))
						zone_matrix[pos_x][pos_y + count] = Cases.Danger;
					else if (pos_y + count > width
							&& (zone_matrix[pos_x][pos_y + count] == Cases.Bonus))
						zone_matrix[pos_x][pos_y + count] = Cases.Danger;
					else
						break;
				}
			}
		}

		// placer les ennemies:
		Iterator<AiHero> enemie_itr =enemies.iterator();
		AiHero ennemie;

		while (enemie_itr.hasNext()) {
			ennemie = enemie_itr.next();

			pos_x = ennemie.getLine();
			pos_y = ennemie.getCol();
			if (ennemie != our_bomberman) {
				if (zone_matrix[pos_x][pos_y] != Cases.Range_Of_Fire
						&& zone_matrix[pos_x][pos_y] != Cases.Fire &&
						zone_matrix[pos_x][pos_y] != Cases.Danger
						&& zone_matrix[pos_x][pos_y] != Cases.Bomb)
					zone_matrix[pos_x][pos_y] = Cases.Enemie;
			}
		}
	}

	// return to Matrix:
	public Cases[][] returnMatrix() {
		return zone_matrix;
	}

	// s'il n'y a rien de danger
	public boolean CanContinue( int line, int col) {
		boolean resultat = false;
		if (zone_matrix[line][col] != Cases.Fire &&
				zone_matrix[line][col] != Cases.Bomb &&
				zone_matrix[line][col] != Cases.FireAndRange &&
				zone_matrix[line][col] != Cases.Danger &&
				zone_matrix[line][col] != Cases.Wall_D
				&& zone_matrix[line][col] != Cases.Wall_ND)
			resultat =true;
		return resultat;
	}

}