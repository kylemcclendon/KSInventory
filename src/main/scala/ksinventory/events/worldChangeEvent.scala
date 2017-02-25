package ksinventory.events

import ksinventory.cache.RetryCache
import ksinventory.utils.Utils.getShortenedWorldName
import ksinventory.services.{DataService, EndChestInventoryService, InventoryService}
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.player.PlayerChangedWorldEvent

class worldChangeEvent extends Listener{
  @EventHandler
  def onWorldChange(event: PlayerChangedWorldEvent): Unit ={
    val oldPlayerInventory = event.getPlayer.getInventory.getContents
    val oldPlayerData = (event.getPlayer.getHealth, event.getPlayer.getExp, event.getPlayer.getLevel, event.getPlayer.getFoodLevel, event.getPlayer.getSaturation)
    val oldWorldNameShortened = getShortenedWorldName(event.getFrom.getName)
    val newWorldNameShortened = getShortenedWorldName(event.getPlayer.getWorld.getName)
    val playerId = event.getPlayer.getUniqueId

    //Persist To Cache and DB
    InventoryService.setPlayerInventoryCache(playerId, oldWorldNameShortened, oldPlayerInventory.toList)
    DataService.setPlayerDataCache(playerId, oldWorldNameShortened, oldPlayerData._1.toFloat, oldPlayerData._2, oldPlayerData._3, oldPlayerData._4, oldPlayerData._5)

    InventoryService.persistPlayerInventory(playerId, oldWorldNameShortened)
    DataService.persistPlayerData(playerId, oldWorldNameShortened)

    //Set New Data
    if(!oldWorldNameShortened.equals(newWorldNameShortened)){
      event.getPlayer.getInventory.clear()
      event.getPlayer.setHealth(20)
      event.getPlayer.setLevel(0)
      event.getPlayer.setExp(0)
      event.getPlayer.setFoodLevel(20)
      event.getPlayer.setSaturation(20)

      InventoryService.setPlayerWorldInventory(playerId, newWorldNameShortened)
      DataService.setPlayerWorldData(playerId, newWorldNameShortened)
      EndChestInventoryService.setPlayerWorldEndInventory(playerId, newWorldNameShortened)

      RetryCache.clearRetryRefined(playerId, newWorldNameShortened, "data")
      RetryCache.clearRetryRequestRefined(playerId, newWorldNameShortened, "data")
      RetryCache.clearRetryRefined(playerId, newWorldNameShortened, "inv")
      RetryCache.clearRetryRequestRefined(playerId, newWorldNameShortened, "inv")
    }
  }
}