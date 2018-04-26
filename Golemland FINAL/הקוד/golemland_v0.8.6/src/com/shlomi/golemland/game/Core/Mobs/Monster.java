package com.shlomi.golemland.game.Core.Mobs;

import com.shlomi.golemland.game.Core.Map;
import com.shlomi.golemland.game.Core.Constants.Tiles;

/**
 * Monster class represent Spider, creeper , zombie and ect. It extends {@link MonsterBehaviorSystem} to randomly change direction and stop.
 * @author Shlomi
 *
 */
public class Monster extends MonsterBehaviorSystem{

	/**
	 * This indicates which 'monster' is this class.
	 */
	private Tiles monsterTile;
	
	/**
	 * New monster object.
	 * @param tile Tiles object, containing texture and monster type.
	 * @param horizontalIndex Horizontal index of location.
	 * @param verticalIndex Vertical index of location.
	 * @param map Map pointer
	 */
	public Monster(Tiles tile, int horizontalIndex, int verticalIndex, Map map) {
		super(tile, horizontalIndex, verticalIndex, map);
		this.monsterTile = tile;
	}
	
	/**
	 * Tiles enum is the type and texture of a monster. Return this monster's tiles enum.
	 * @return The enum Tiles that is this monster
	 */
	public Tiles getMonsterTile() {
		return monsterTile;
	}

}
