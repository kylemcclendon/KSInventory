package ksinventory.events

import ksinventory.services.InventoryService
import org.bukkit.Material
import org.bukkit.block.EnderChest
import org.bukkit.event.inventory.{InventoryCloseEvent, InventoryOpenEvent, InventoryType}
import org.bukkit.event.{EventHandler, Listener}

class endChestEvents extends Listener{

  @EventHandler
  def openEnderChest(inventoryOpenEvent: InventoryOpenEvent): Unit ={
    if(inventoryOpenEvent.getInventory.getType.equals(InventoryType.ENDER_CHEST)){
      val inventory = InventoryService.getPlayerWorldEndInventory(inventoryOpenEvent.getPlayer.getUniqueId, inventoryOpenEvent.getPlayer.getWorld.getUID)

      inventoryOpenEvent.getInventory.setContents(inventory.toArray)
    }
  }

  @EventHandler
  def closeEnderChest(inventoryCloseEvent: InventoryCloseEvent): Unit ={
    if(inventoryCloseEvent.getInventory.getType.equals(InventoryType.ENDER_CHEST)){
      val inventory = inventoryCloseEvent.getInventory.getContents

      InventoryService.savePlayerEndInventory(inventoryCloseEvent.getPlayer.getUniqueId, inventoryCloseEvent.getPlayer.getWorld.getUID, inventory.toList)
    }
  }
}