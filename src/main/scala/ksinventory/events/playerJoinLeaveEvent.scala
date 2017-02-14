package ksinventory.events

import ksinventory.services.InventoryService
import org.bukkit.event.player.{PlayerJoinEvent, PlayerQuitEvent}
import org.bukkit.event.{EventHandler, Listener}

class playerJoinLeaveEvent extends Listener{

  @EventHandler
  def onPlayerJoin(playerJoinEvent: PlayerJoinEvent): Unit ={
    //retrieve inventory
    InventoryService.setPlayerWorldInventory(playerJoinEvent.getPlayer.getUniqueId, playerJoinEvent.getPlayer.getWorld.getUID)
    InventoryService.setPlayerWorldEndInventory(playerJoinEvent.getPlayer.getUniqueId, playerJoinEvent.getPlayer.getWorld.getUID)
    InventoryService.setPlayerWorldData(playerJoinEvent.getPlayer.getUniqueId, playerJoinEvent.getPlayer.getWorld.getUID)
  }

  @EventHandler
  def onPlayerLeave(playerQuitEvent: PlayerQuitEvent): Unit ={
    //save inventory
    val player = playerQuitEvent.getPlayer

    InventoryService.savePlayerInventory(player.getUniqueId, player.getWorld.getUID, player.getInventory.getContents.toList)
    InventoryService.savePlayerData(player.getUniqueId, player.getWorld.getUID, player.getHealth.toFloat,player.getExp,player.getLevel,player.getFoodLevel,player.getSaturation)
  }
}
