/*
 * Most of the code was taken from the plugin: https://github.com/AddstarMC/Prism-Bukkit
 * Thanks to the developers!
 */
package com.github.radiant.ezclans.logs;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;

public class InventoryListener implements Listener {
    /**
     * Handle inventory transfers.
     *
     * @param event InventoryDragEvent
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryDrag(final InventoryDragEvent event) {
        // Store some info
        final Player player = (Player) event.getWhoClicked();
        final Inventory inv = event.getInventory();
        
        ClanMember cm = Clans.getMember(player.getUniqueId());
        
        if (cm == null) {
        	return;
        }
        
        if (cm.getClan().getStorage() != inv) {
        	return;
        }

        final Map<Integer, ItemStack> newItems = event.getNewItems();
        for (final Entry<Integer, ItemStack> entry : newItems.entrySet()) {

            int rawSlot = entry.getKey();

            // Top inventory
            if (rawSlot < event.getInventory().getSize()) {
                ItemStack stack = event.getView().getItem(rawSlot);
                int slotViewAmount = (stack == null)
                        ? 0 : stack.getAmount();
                int amount = entry.getValue().getAmount() - slotViewAmount;
                EzLogs.logStorage(player, entry.getValue().getType(), amount, "INSERT");
                //RecordingQueue.addToQueue(ActionFactory.createItemStack(INSERT, entry.getValue(), amount,rawSlot, null, containerLoc, player));
            }
        }
    }

    /**
     * Handle inventory transfers.
     *
     * @param event InventoryClickEvent
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(final InventoryClickEvent event) {
        int slot = event.getRawSlot(); //this is the unique slot number for the view.
        // Specifically slot -999, or out of the window
        if (slot < 0) {
            return;
        }
        
        final Player player = (Player) event.getWhoClicked();
        final Inventory inv = event.getInventory();
        
        ClanMember cm = Clans.getMember(player.getUniqueId());
        
        if (cm == null) {
        	return;
        }
        
        if (cm.getClan().getStorage() != inv) {
        	return;
        }
        
        boolean isTopInv = slot < event.getInventory().getSize();

        ItemStack heldItem = event.getCursor();
        ItemStack slotItem = event.getCurrentItem();

        switch (event.getClick()) {
            // IGNORE BOTTOM
            case LEFT:
                if (isTopInv) {
                    if (heldItem == null || heldItem.getType() == Material.AIR) {
                        if (slotItem != null && slotItem.getType() != Material.AIR) {
                        	EzLogs.logStorage(player, slotItem.getType(), slotItem.getAmount(), "REMOVE");
                            //RecordingQueue.addToQueue(ActionFactory.createItemStack(REMOVE, slotItem, slotItem.getAmount(), slot, null, containerLoc, player));
                        }
                    } else {
                        int amount = 0;
                        int maxStack = heldItem.getMaxStackSize();
                        if (slotItem == null || ((slotItem != null && slotItem.getType() == Material.AIR) && heldItem.getAmount() <= maxStack)) {
                            amount = heldItem.getAmount();
                        }
                        if (slotItem != null && slotItem.getType().equals(heldItem.getType())) {
                            int slotQty = slotItem.getAmount();
                            amount = Math.min(maxStack - slotQty,heldItem.getAmount());
                        }
                        if (amount > 0) {
                        	EzLogs.logStorage(player, heldItem.getType(), amount, "INSERT");
                            //RecordingQueue.addToQueue(ActionFactory.createItemStack(INSERT, heldItem, amount, slot,null, containerLoc, player));

                        }
                        if (slotItem != null && slotItem.getType() != Material.AIR && !slotItem.getType().equals(heldItem.getType())) {
                            // its a switch.
                        	EzLogs.logStorage(player, heldItem.getType(), heldItem.getAmount(), "INSERT");
                            //RecordingQueue.addToQueue(ActionFactory.createItemStack(INSERT,heldItem,heldItem.getAmount(),slot,null,containerLoc,player));
                        	EzLogs.logStorage(player, slotItem.getType(), slotItem.getAmount(), "REMOVE");
                            //RecordingQueue.addToQueue(ActionFactory.createItemStack(REMOVE,slotItem,slotItem.getAmount(),slot,null,containerLoc,player));
                        }
                    }
                }
                break;

            case RIGHT:
                if (isTopInv) {
                    if (heldItem == null || heldItem.getType() == Material.AIR) {
                        if (slotItem != null && slotItem.getType() != Material.AIR) {
                        	EzLogs.logStorage(player, slotItem.getType(), (slotItem.getAmount() + 1) / 2, "REMOVE");
                            //RecordingQueue.addToQueue(ActionFactory.createItemStack(REMOVE, slotItem,(slotItem.getAmount() + 1) / 2, slot, null, containerLoc, player));

                        }
                    } else {
                        if (slotItem == null || ((slotItem != null && slotItem.getType() == Material.AIR) || (slotItem != null && slotItem.equals(heldItem)))
                                && slotItem.getAmount() < slotItem.getType().getMaxStackSize()) {
                        	EzLogs.logStorage(player, slotItem.getType(), 1, "INSERT");
                            //RecordingQueue.addToQueue(ActionFactory.createItemStack(INSERT, slotItem, 1, slot, null,containerLoc, player));

                        }
                    }
                }
                break;

            case NUMBER_KEY:
                if (isTopInv) {
                    ItemStack swapItem = player.getInventory().getItem(event.getHotbarButton());

                    if (slotItem != null && slotItem.getType() != Material.AIR) {
                    	EzLogs.logStorage(player, slotItem.getType(), slotItem.getAmount(), "REMOVE");
                        //RecordingQueue.addToQueue(ActionFactory.createItemStack(REMOVE, slotItem, slotItem.getAmount(),slot, null, containerLoc, player));

                    }

                    if (swapItem != null && swapItem.getType() != Material.AIR) {
                    	EzLogs.logStorage(player, swapItem.getType(), swapItem.getAmount(), "INSERT");
                        //RecordingQueue.addToQueue(ActionFactory.createItemStack(INSERT, swapItem, swapItem.getAmount(), slot, null, containerLoc, player));

                    }
                }
                break;

            // HALF 'N HALF
            case DOUBLE_CLICK: {
                int amount = (heldItem == null) ? 0 :
                        heldItem.getType().getMaxStackSize() - heldItem.getAmount();

                ItemStack[] contents = event.getInventory().getStorageContents();
                int length = contents.length;

                for (int i = 0; i < length; ++i) {
                    ItemStack is = contents[i];

                    int size = 0;
                    if (is != null && (is.getType() != Material.AIR || is.equals(heldItem))) {
                        size += is.getAmount();
                    }
                    amount = recordDeductTransfer("REMOVE",size,amount,heldItem,player);
                    if (amount <= 0) {
                        break;
                    }
                }
                break;
            }

            // CROSS INVENTORY EVENTS
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
                if (isTopInv) {
                    if (slotItem != null && slotItem.getType() != Material.AIR) {
                        int stackSize = slotItem.getType().getMaxStackSize();
                        int remaining = slotItem.getAmount();

                        for (ItemStack is : event.getView().getBottomInventory().getStorageContents()) {
                            //noinspection ConstantConditions  Until intellij sorts it checks
                            if (is == null || is.getType() == Material.AIR) {
                                remaining -= stackSize;
                            } else if (is.isSimilar(slotItem)) {
                                remaining -= (stackSize - Math.min(is.getAmount(), stackSize));
                            }

                            if (remaining <= 0) {
                                remaining = 0;
                                break;
                            }
                        }
                        EzLogs.logStorage(player, slotItem.getType(), slotItem.getAmount() - remaining, "REMOVE");
                        //RecordingQueue.addToQueue(ActionFactory.createItemStack(REMOVE, slotItem,slotItem.getAmount() - remaining, slot, null, containerLoc, player));

                    }
                } else {
                    int stackSize = slotItem.getType().getMaxStackSize();
                    int amount = slotItem.getAmount();

                    ItemStack[] contents = event.getInventory().getStorageContents();
                    int length = contents.length;

                    // Fill item stacks first
                    for (int i = 0; i < length; ++i) {
                        ItemStack is = contents[i];

                        if (slotItem.isSimilar(is)) {
                            amount = recordDeductTransfer("INSERT",stackSize - is.getAmount(),amount,slotItem,player);
                            if (amount <= 0) {
                                break;
                            }
                        }
                    }

                    // Fill empty slots
                    if (amount > 0) {
                        for (int i = 0; i < length; ++i) {
                            ItemStack is = contents[i];

                            if (is == null || is.getType() == Material.AIR) {
                                amount = recordDeductTransfer("INSERT",stackSize,amount,slotItem, player);
                                if (amount <= 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
                break;

            // DROPS
            case DROP:
                if (slotItem != null && slotItem.getType() != Material.AIR && slotItem.getAmount() > 0) {
                	EzLogs.logStorage(player, slotItem.getType(), 1, "REMOVE");
                    //RecordingQueue.addToQueue(ActionFactory.createItemStack(REMOVE, slotItem, 1, slot, null, containerLoc, player));

                }
                break;

            case CONTROL_DROP:
                if (slotItem != null && slotItem.getType() != Material.AIR && slotItem.getAmount() > 0) {
                	EzLogs.logStorage(player, slotItem.getType(), slotItem.getAmount(), "REMOVE");
                    //RecordingQueue.addToQueue(ActionFactory.createItemStack(REMOVE, slotItem, slotItem.getAmount(),slot, null, containerLoc, player));

                }
                break;

            case WINDOW_BORDER_LEFT:
                // Drop stack on cursor
            case WINDOW_BORDER_RIGHT:
                // Drop 1 on cursor

            default:
                // What the hell did you do
        }
    }

    private int recordDeductTransfer(String act, int size, int amount, ItemStack heldItem, Player player) {
        int transferred = Math.min(size, amount);
        int newAmount = amount - transferred;
        if (transferred > 0) {
        	EzLogs.logStorage(player, heldItem.getType(), transferred, act);
            //RecordingQueue.addToQueue(ActionFactory.createItemStack(act, heldItem, transferred, slotLocation, null, containerLoc, player));

        }
        return newAmount;
    }

}
