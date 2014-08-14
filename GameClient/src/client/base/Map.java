package client.base;

import java.util.Arrays;

public class Map {

	private int mapheight;
	private int mapWidth;
	private int[][] tiles = new int[0][0];

	Map(int sizeX, int sizeY) {
		this.mapheight = sizeY;
		this.mapWidth = sizeX;
		this.tiles = new int[sizeX][sizeY];
	}

	public int getMapheight() {
		return mapheight;
	}

	public void setMapheight(int mapheight) {
		this.mapheight = mapheight;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public void setMapWidth(int mapWidth) {
		this.mapWidth = mapWidth;
	}

	public int[][] getTiles() {
		return tiles;
	}

	public void setTiles(int[][] tiles) {
		this.tiles = tiles;
	}

	@Override
	public String toString() {
		return "Map [mapheight=" + mapheight + ", mapWidth=" + mapWidth
				+ ", tiles=" + Arrays.toString(tiles) + "]";
	}

}
