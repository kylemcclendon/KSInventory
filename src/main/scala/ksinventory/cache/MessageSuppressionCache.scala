package ksinventory.cache

import scala.collection.mutable

object MessageSuppressionCache {
  var playerSuppressMessages: mutable.Map[String, Boolean] = mutable.Map()

  //Getters

  def getPlayerSuppression(playerId: String): Boolean = {
    playerSuppressMessages.getOrElse(playerId, false)
  }

  //Setters

  def setSuppressedPlayers(players: Set[String]): Unit ={
    players.foreach((player) => {
      playerSuppressMessages(player) = true
    })
  }

  def setSuppressedPlayer(player: String): Unit ={
    playerSuppressMessages(player) = true
  }

  //Removers

  def removeSuppressedPlayer(player: String): Unit ={
    playerSuppressMessages(player) = false
  }
}
