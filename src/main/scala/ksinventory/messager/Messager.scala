package ksinventory.messager

import java.util.UUID

import ksinventory.cache.MessageSuppressionCache
import org.bukkit.{Bukkit, ChatColor}

object Messager {
  def messagePlayerSuccess(playerId: UUID, message: String): Unit ={
    if(!MessageSuppressionCache.getPlayerSuppression(playerId.toString)){
      val p = Bukkit.getServer.getPlayer(playerId)
      if(p != null && p.isOnline){
        p.sendMessage(ChatColor.ITALIC.toString + ChatColor.GRAY.toString + message)
      }
    }
  }

  def messagePlayerFailure(playerId: UUID, message: String): Unit ={
    val p = Bukkit.getServer.getPlayer(playerId)
    if(p != null && p.isOnline){
      p.sendMessage(ChatColor.RED + message)
    }
  }
}