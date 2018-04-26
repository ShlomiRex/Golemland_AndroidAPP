package com.shlomi.golemland.game.Core.Mobs;

import java.util.Random;
import com.shlomi.golemland.game.Core.Map;
import com.shlomi.golemland.game.Core.Constants.Tiles;

/**
 * This is special class for identifying mob that walks randomly, stops
 * randomly, and ect. It extends Mob.<br>
 * Monster should extend this class.
 */
public abstract class MonsterBehaviorSystem extends Mob {

	private Random rnd;

	public MonsterBehaviorSystem(Tiles tile, int horizontalIndex, int verticalIndex, Map map) {
		super(tile, horizontalIndex, verticalIndex, map);
		rnd = new Random();
	}

	@Override
	public void tick() {
		// Here we do randomly stops
		// Every tick, there is a chance that a creeper will stop moving. It
		// depends on CREEPER_CHANSE_TO_WAIT value.

		// If it is true, then we stop, we don't 'tick' the creeper
		if (rnd.nextInt(TickSettings.MONSTER_CHANCE_TO_WAIT) == 0)
			return;

		// Else we tick the creeper to walk
		super.tick();
	}

}
