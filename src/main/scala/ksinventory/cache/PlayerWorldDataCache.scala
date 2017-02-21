package ksinventory.cache

import java.util.UUID

import scala.collection.mutable

object PlayerWorldDataCache {
  var playerWorldDataMap: mutable.Map[(UUID, String), (Float, Float, Float, Float, Float)] = mutable.Map()

  def playerDataContains(playerId: UUID, worldName: String): Boolean ={
    playerWorldDataMap.contains((playerId,worldName))
  }

  def getPlayerData(playerId: UUID, worldName: String): Tuple5[Float, Float, Float, Float, Float]={
    playerWorldDataMap.getOrElse((playerId,worldName),(20,0,0,20,20))
  }

  def setPlayerData(playerId: UUID, worldName: String, health: Float, experience: Float, level: Float, food: Float, saturation: Float): Unit ={
    playerWorldDataMap((playerId,worldName)) = (health, experience,level,food,saturation)
  }
}
