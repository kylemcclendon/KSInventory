package ksinventory.cache

import java.util.UUID

import scala.collection.mutable

object SaveRetryCache {
  var saveRetryMap: mutable.Map[(UUID, String, String), Long] = mutable.Map()
  var saveRetryRequestMap: mutable.Map[(UUID, String, String), Boolean] = mutable.Map()

  def getRetryRefined(playerId: UUID, worldName: String, retryType: String): Long = {
    saveRetryMap.getOrElse((playerId, worldName, retryType), -1)
  }

  def getRetryRequestRefined(playerId: UUID, worldName: String, retryType: String): Boolean = {
    saveRetryRequestMap.getOrElse((playerId,worldName,retryType), false)
  }

  def setRetryRefined(playerId: UUID, worldName: String, retryType: String): Unit ={
    saveRetryMap((playerId,worldName,retryType)) = System.currentTimeMillis() + 60000
  }

  def setRetryRequestRefined(playerId: UUID, worldName: String, retryType: String): Unit ={
    saveRetryRequestMap((playerId,worldName,retryType)) = true
  }

  def clearRetryRefined(playerId: UUID, worldName: String, retryType: String): Unit ={
    saveRetryMap.remove((playerId,worldName,retryType))
  }

  def clearRetryRequestRefined(playerId: UUID, worldName: String, retryType: String): Unit ={
    saveRetryRequestMap.remove((playerId,worldName,retryType))
  }
}
