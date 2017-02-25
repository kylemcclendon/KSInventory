package ksinventory.cache

import scala.collection.mutable

object MessageSuppressionCache {
  var playerSuppressMessages: mutable.Map[String, Boolean] = mutable.Map()

  def getPlayerSuppression(playerId: String): Boolean = {
    playerSuppressMessages.getOrElse(playerId, false)
  }

  def setSuppressedPlayers(players: Set[String]): Unit ={
    players.foreach((player) => {
      playerSuppressMessages(player) = true
    })
  }

  def setSuppressedPlayer(player: String): Unit ={
    playerSuppressMessages(player) = true
  }

  def removeSuppressedPlayer(player: String): Unit ={
    playerSuppressMessages(player) = false
  }
}
