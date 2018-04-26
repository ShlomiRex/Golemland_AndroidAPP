package com.shlomi.golemland.game.Core;

import java.util.ArrayList;
import com.shlomi.golemland.R;
import com.shlomi.golemland.Initialization.Settings;

import android.app.Activity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Inventory class handles the inventory.
 * @author Shlomi
 *
 */
public final class Inventory implements Settings.ShopSettings{

	private boolean isNeedToTick = false;
	private final String LOG_TAG = Inventory.class.getSimpleName();
	private ArrayList<Slot> slots;

	/**
	 * Create new inventory.
	 * 
	 * @param slotsArray
	 *            Is array of frame layouts (each one is a slot).
	 * @param isTNTOnMap
	 *            Instance of boolean variable in Map class.
	 */
	public Inventory(ArrayList<FrameLayout> slotsArray) {
		slots = new ArrayList<Slot>();

		//Initialize slots
		for (int i = 0; i < slotsArray.size(); i++)
			slots.add(new Slot(slotsArray.get(i), i));
	}

	/**
	 * When slot is clicked, do stuff. Method is called outside of class.
	 * 
	 * When slot is clicked and it has item, remove it.
	 * 
	 * @param slotID
	 *            ID of the slot. (0 = most left, 4 = most right)
	 * @return Status determine what should we do.<br>
	 * @see Settings.InventoryStatus
	 */
	public int slotClicked(int slotID) {
		Log.d(LOG_TAG, "Clicked on slot: " + slotID);
		Slot slotClicked = slots.get(slotID);
		if (slotClicked.hasItem) {
			Log.d(LOG_TAG, "Slot " + slotID + " has item!");
			
			int slotItem = slotClicked.itemID;
			
			Log.d(LOG_TAG,"Slot item id = " + slotItem);
			
			if (slotItem == ID_TNT) {
				//TNT
				Log.d(LOG_TAG,"Returning status PLACE_TNT: " + Settings.InventoryStatus.PLACE_TNT);
				return Settings.InventoryStatus.PLACE_TNT;
			}
			if(slotItem == ID_POWERUP) {
				//Powerup
				Log.d(LOG_TAG,"Returning status USER_POWERUP: " + Settings.InventoryStatus.USE_POWERUP);
				return Settings.InventoryStatus.USE_POWERUP;
			}
		}
		
		//Slot doesn't have item
		Log.d(LOG_TAG,"Returning status DO_NOTHING");
		return Settings.InventoryStatus.DO_NOTHING;
	}

	/**
	 * Called outside of class.
	 * 
	 * @param slotIndex
	 *            Index of slot.
	 * @return Return the item id that removed from slot.
	 */
	public int removeItemFromSlot(int slotID) {
		// Important to save item id before deleting it.
		int itemID = slots.get(slotID).itemID;
		slots.get(slotID).removeItemFromSlot();
		isNeedToTick = true;
		return itemID;
	}

	/**
	 * Called outside of class.
	 * 
	 * @param itemID
	 *            ID of item.
	 * @return True if adding was success. Else, false. Can fail if no room in slots for a new item.
	 */
	public boolean addItemToInventory(int itemID) {
		Slot freeSlot = null;
		for (Slot tmp : slots) {
			Log.d(LOG_TAG, "Checking to add to slot " + tmp.slotID);

			if (tmp.hasItem == false) {
				freeSlot = tmp;
				Log.d(LOG_TAG, "Found free slot: " + tmp.slotID);
				break;
			} else
				Log.d(LOG_TAG, "Checking another slot...");
		}

		if (freeSlot == null) {
			Log.d(LOG_TAG, "No free slots!");
			return false;
		}

		slots.get(freeSlot.slotID).addItem(itemID);

		isNeedToTick = true;

		Log.d(LOG_TAG, "Adding was success.");
		return true;
	}

	/**
	 * Method called outside of class. Ticks inventory images.
	 * 
	 * @param gameActivity
	 */
	public void tickInventory(Activity gameActivity) {
		if (!isNeedToTick)
			return;
		for (Slot slot : slots)
			slot.updateSlotImage(gameActivity);
		isNeedToTick = false;
		// TODO
		// Log.d(LOG_TAG,"Slots size = ")
	}

	/**
	 * Class slot. Represent a single "Slot Tile" on the screen with position ID, and other item variables.
	 * @author Shlomi
	 *
	 */
	class Slot {
		int itemID;
		boolean hasItem;

		private int slotID;
		private FrameLayout slotFrameLayout;

		/**
		 * @param itemID
		 *            ID of item
		 * @param slotFrameLayout
		 *            The slot framelayout
		 */
		public Slot(FrameLayout slotFrameLayout, int slotID) {
			this.slotID = slotID;
			this.itemID = ID_NO_ITEM;
			this.slotFrameLayout = slotFrameLayout;
			this.hasItem = false;
		}

		/**
		 * Called when player pressed on slot button, so we remove the item image from the butotn.
		 */
		public void removeItemFromSlot() {
			Log.d(LOG_TAG, "Removing item from slot " + slotID);
			itemID = ID_NO_ITEM;
			hasItem = false;
			// Need to tick, in order to show transparent background.
			isNeedToTick = true;
		}

		/**
		 * Add an item to this slot.
		 * @param itemID ItemID to add to this slot.
		 */
		public void addItem(int itemID) {
			Log.d(LOG_TAG, "Slot " + slotID + ": item " + itemID + " added.");
			this.itemID = itemID;
			this.hasItem = true;
		}

		/**
		 * 
		 * @param itemID Item ID to check if has item
		 * @return True, if has item.
		 */
		protected boolean isHasItem(int itemID) {
			if (itemID == ID_NO_ITEM)
				return false;

			return true;
		}

		/**
		 * Called when need to display the image ON the Slot button
		 * @param gameActivity
		 */
		public void updateSlotImage(Activity gameActivity) {
			ImageView imageView = (ImageView) slotFrameLayout
					.findViewById(R.id.itemImageView);

			switch(itemID) {
			case ID_HAT:
				Log.d(LOG_TAG, "Updating slot " + slotID + " as hat");
				imageView.setBackgroundResource(R.drawable.item_hat);
				break;
			case ID_TNT:
				Log.d(LOG_TAG, "Updating slot " + slotID + " as tnt");
				imageView.setBackgroundResource(R.drawable.item_tnt);
				break;
			case ID_POWERUP:
				Log.d(LOG_TAG,"Updating slot " + slotID + " as power up");
				imageView.setBackgroundResource(R.drawable.power_up);
				break;
				
			default:
				// No items in inventory.
				if (isNeedToTick)
					imageView.setBackgroundResource(R.drawable.trans_background);
				break;
			}//switch case
		}

	}// class Slot

}// class Inventory
