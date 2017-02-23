package ksinventory.events

import ksinventory.cache.MessageSuppressionCache
import ksinventory.services.{DataService, EndChestInventoryService, InventoryService}
import ksinventory.utils.Utils.getShortenedWorldName
import org.bukkit.ChatColor
import org.bukkit.event.player.{PlayerJoinEvent, PlayerKickEvent, PlayerQuitEvent}
import org.bukkit.event.{EventHandler, Listener}

class playerJoinLeaveEvent extends Listener{

  @EventHandler
  def onPlayerJoin(playerJoinEvent: PlayerJoinEvent): Unit ={
    if(MessageSuppressionCache.getPlayerSuppression(playerJoinEvent.getPlayer.getUniqueId.toString)){
      playerJoinEvent.getPlayer.sendMessage(ChatColor.GRAY + "Inventory messages are suppressed for you. Turn them on with /inv verbose if you want to start getting inventory messages...")
    }
    InventoryService.setPlayerWorldInventory(playerJoinEvent.getPlayer.getUniqueId, getShortenedWorldName(playerJoinEvent.getPlayer.getWorld.getName))
    EndChestInventoryService.setPlayerWorldEndInventory(playerJoinEvent.getPlayer.getUniqueId, getShortenedWorldName(playerJoinEvent.getPlayer.getWorld.getName))
    DataService.setPlayerWorldData(playerJoinEvent.getPlayer.getUniqueId, getShortenedWorldName(playerJoinEvent.getPlayer.getWorld.getName))
  }

  @EventHandler
  def onPlayerLeave(playerQuitEvent: PlayerQuitEvent): Unit ={
    val player = playerQuitEvent.getPlayer

    InventoryService.savePlayerInventory(player.getUniqueId, getShortenedWorldName(player.getWorld.getName), player.getInventory.getContents.toList)
    DataService.savePlayerData(player.getUniqueId, getShortenedWorldName(player.getWorld.getName), player.getHealth.toFloat,player.getExp,player.getLevel,player.getFoodLevel,player.getSaturation)
  }

  @EventHandler
  def onPlayerKick(playerKickEvent: PlayerKickEvent): Unit ={
    val player = playerKickEvent.getPlayer

    InventoryService.savePlayerInventory(player.getUniqueId, getShortenedWorldName(player.getWorld.getName), player.getInventory.getContents.toList)
    DataService.savePlayerData(player.getUniqueId, getShortenedWorldName(player.getWorld.getName), player.getHealth.toFloat,player.getExp,player.getLevel,player.getFoodLevel,player.getSaturation)
  }
}
