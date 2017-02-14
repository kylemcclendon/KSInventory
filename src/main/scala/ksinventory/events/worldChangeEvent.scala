package ksinventory.events

import ksinventory.dao.InventoryDao
import ksinventory.services.InventoryService
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.player.PlayerChangedWorldEvent

class worldChangeEvent extends Listener{
  @EventHandler
  def onWorldChange(event: PlayerChangedWorldEvent): Unit ={
    val oldPlayerInventory = event.getPlayer.getInventory.getContents
    val oldPlayerData = (event.getPlayer.getHealth, event.getPlayer.getExp, event.getPlayer.getLevel, event.getPlayer.getFoodLevel, event.getPlayer.getSaturation)

    event.getPlayer.getInventory.clear()
    event.getPlayer.setHealth(20)
    event.getPlayer.setLevel(0)
    event.getPlayer.setExp(0)
    event.getPlayer.setFoodLevel(20)
    event.getPlayer.setSaturation(20)

    val oldWorldId = event.getFrom.getUID
    val newWorldId = event.getPlayer.getWorld.getUID
    val playerId = event.getPlayer.getUniqueId

    //Persist To Cache
    InventoryService.savePlayerInventory(playerId, oldWorldId, oldPlayerInventory.toList)
    InventoryService.savePlayerData(playerId, oldWorldId, oldPlayerData._1.toFloat, oldPlayerData._2, oldPlayerData._3, oldPlayerData._4, oldPlayerData._5)

    //Set New Data
    InventoryService.setPlayerWorldInventory(playerId, newWorldId)
    InventoryService.setPlayerWorldData(playerId, newWorldId)
    InventoryService.setPlayerWorldEndInventory(playerId, newWorldId)
  }
}