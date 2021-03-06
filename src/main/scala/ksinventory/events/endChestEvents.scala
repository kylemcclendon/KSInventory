package ksinventory.events

import ksinventory.cache.{EndChestCache, SaveRetryCache}
import ksinventory.services.EndChestInventoryService
import ksinventory.utils.Utils.getShortenedWorldName
import org.bukkit.{ChatColor, Material}
import org.bukkit.event.inventory.{InventoryCloseEvent, InventoryOpenEvent, InventoryType}
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, Listener}

class endChestEvents extends Listener{

  @EventHandler
  def openEnderChest(inventoryOpenEvent: InventoryOpenEvent): Unit ={
    if(inventoryOpenEvent.getInventory.getType.equals(InventoryType.ENDER_CHEST)){
      val inventory = EndChestInventoryService.getPlayerWorldEndInventory(inventoryOpenEvent.getPlayer.getUniqueId, getShortenedWorldName(inventoryOpenEvent.getPlayer.getWorld.getName))

      SaveRetryCache.clearRetryRefined(inventoryOpenEvent.getPlayer.getUniqueId, getShortenedWorldName(inventoryOpenEvent.getPlayer.getWorld.getName), "end")
      SaveRetryCache.clearRetryRequestRefined(inventoryOpenEvent.getPlayer.getUniqueId, getShortenedWorldName(inventoryOpenEvent.getPlayer.getWorld.getName), "end")

      inventoryOpenEvent.getInventory.setContents(inventory.toArray)
    }
  }

  @EventHandler
  def closeEnderChest(inventoryCloseEvent: InventoryCloseEvent): Unit ={
    if(inventoryCloseEvent.getInventory.getType.equals(InventoryType.ENDER_CHEST)){
      val inventory = inventoryCloseEvent.getInventory.getContents
      val worldName = getShortenedWorldName(inventoryCloseEvent.getPlayer.getWorld.getName)

      EndChestInventoryService.setEndInventoryCache(inventoryCloseEvent.getPlayer.getUniqueId, worldName, inventory.toList)
      EndChestInventoryService.savePlayerEndInventory(inventoryCloseEvent.getPlayer.getUniqueId, worldName)
    }
  }

  @EventHandler
  def attemptOpenEnderChest(playerRightClickEvent: PlayerInteractEvent): Unit ={
    if(playerRightClickEvent.getClickedBlock != null && playerRightClickEvent.getClickedBlock.getType.equals(Material.ENDER_CHEST)){
      if(EndChestCache.getPlayerEndRequest(playerRightClickEvent.getPlayer.getUniqueId, getShortenedWorldName(playerRightClickEvent.getPlayer.getWorld.getName))){
        playerRightClickEvent.getPlayer.sendMessage(ChatColor.RED + "Ender Chest Inventory Not Yet Set For This World. Please Try Again In 5 seconds...")
        playerRightClickEvent.setCancelled(true)
      }
    }
  }
}