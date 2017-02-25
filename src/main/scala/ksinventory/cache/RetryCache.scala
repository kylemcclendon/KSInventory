package ksinventory.cache

import java.util.UUID

import scala.collection.mutable

object RetryCache {
  var retryMap: mutable.Map[(UUID, String, String), Long] = mutable.Map()
  var retryRequestMap: mutable.Map[(UUID, String, String), Boolean] = mutable.Map()

  def getRetryRefined(playerId: UUID, worldName: String, retryType: String): Long = {
    retryMap.getOrElse((playerId, worldName, retryType), -1)
  }

  def getRetryRequestRefined(playerId: UUID, worldName: String, retryType: String): Boolean = {
    retryRequestMap.getOrElse((playerId,worldName,retryType), false)
  }

  def setRetryRefined(playerId: UUID, worldName: String, retryType: String): Unit ={
    retryMap((playerId,worldName,retryType)) = System.currentTimeMillis() + 60000
  }

  def setRetryRequestRefined(playerId: UUID, worldName: String, retryType: String): Unit ={
    retryRequestMap((playerId,worldName,retryType)) = true
  }

  def clearRetryRefined(playerId: UUID, worldName: String, retryType: String): Unit ={
    retryMap.remove((playerId,worldName,retryType))
  }

  def clearRetryRequestRefined(playerId: UUID, worldName: String, retryType: String): Unit ={
    retryRequestMap.remove((playerId,worldName,retryType))
  }
}
