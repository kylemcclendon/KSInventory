package ksinventory.cache

import java.util.UUID

import scala.collection.mutable

object PlayerWorldDataCache {
  var playerWorldDataMap: mutable.Map[(UUID, String), (Float, Float, Float, Float, Float)] = mutable.Map()
  var playerDataSaveRetryMap: mutable.Map[UUID, Tuple2[String, Long]] = mutable.Map()
  var playerDataSaveRetryRequests: mutable.Map[UUID, Boolean] = mutable.Map()

  //Contains

  def playerDataContains(playerId: UUID, worldName: String): Boolean ={
    playerWorldDataMap.contains((playerId,worldName))
  }

  //Getters

  def getPlayerData(playerId: UUID, worldName: String): Tuple5[Float, Float, Float, Float, Float]={
    playerWorldDataMap.getOrElse((playerId,worldName),(20,0,0,20,20))
  }

  def getPlayerDataRetry(playerId:UUID): Tuple2[String, Long]={
    playerDataSaveRetryMap.getOrElse(playerId, Tuple2(null, -1))
  }

  def getRetryRequest(playerId: UUID): Boolean ={
    playerDataSaveRetryRequests.getOrElse(playerId, false)
  }

  //Setters

  def setPlayerData(playerId: UUID, worldName: String, health: Float, experience: Float, level: Float, food: Float, saturation: Float): Unit ={
    playerWorldDataMap((playerId,worldName)) = (health, experience,level,food,saturation)
  }

  def setPlayerDataRetry(playerId: UUID, worldName: String): Unit = {
    playerDataSaveRetryMap(playerId) = Tuple2(worldName, System.currentTimeMillis() + 60000)
  }

  def setRetryRequest(playerId: UUID): Unit = {
    playerDataSaveRetryRequests(playerId) = true
  }

  //Removers

  def clearPlayerDataRetry(playerId: UUID): Unit ={
    playerDataSaveRetryMap.remove(playerId)
  }

  def clearRetryRequest(playerId: UUID): Unit ={
    playerDataSaveRetryRequests.remove(playerId)
  }
}
