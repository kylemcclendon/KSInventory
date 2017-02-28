package ksinventory.cache

import java.util.UUID

import scala.collection.mutable

object RetrieveRetryCache {
  var retrieveRetryMap: mutable.Map[(UUID, String, String), Long] = mutable.Map()
  var retrieveRetryRequestMap: mutable.Map[(UUID, String, String), Boolean] = mutable.Map()

  def getRetryRefined(playerId: UUID, worldName: String, retryType: String): Long = {
    retrieveRetryMap.getOrElse((playerId, worldName, retryType), -1)
  }

  def getRetryRequestRefined(playerId: UUID, worldName: String, retryType: String): Boolean = {
    retrieveRetryRequestMap.getOrElse((playerId,worldName,retryType), false)
  }

  def setRetryRefined(playerId: UUID, worldName: String, retryType: String): Unit ={
    retrieveRetryMap((playerId,worldName,retryType)) = System.currentTimeMillis() + 60000
  }

  def setRetryRequestRefined(playerId: UUID, worldName: String, retryType: String): Unit ={
    retrieveRetryRequestMap((playerId,worldName,retryType)) = true
  }

  def clearRetryRefined(playerId: UUID, worldName: String, retryType: String): Unit ={
    retrieveRetryMap.remove((playerId,worldName,retryType))
  }

  def clearRetryRequestRefined(playerId: UUID, worldName: String, retryType: String): Unit ={
    retrieveRetryRequestMap.remove((playerId,worldName,retryType))
  }
}
