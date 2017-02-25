package ksinventory.cache

import java.util.UUID

import scala.collection.mutable

object RetryCache {
  var EndRetryMap: mutable.Map[UUID, (String, Long)] = mutable.Map()
  var EndRetryRequestMap: mutable.Map[UUID, Boolean] = mutable.Map()
  var DataRetryMap: mutable.Map[UUID, (String, Long)] = mutable.Map()
  var DataRetryRequestMap: mutable.Map[UUID, Boolean] = mutable.Map()
  var InventoryRetryMap: mutable.Map[UUID, (String, Long)] = mutable.Map()
  var InventoryRetryRequestMap: mutable.Map[UUID, Boolean] = mutable.Map()

  def getRetry(playerId: UUID, retryType: String): (String, Long) = {
    if(retryType.equals("inv")){
      InventoryRetryMap.getOrElse(playerId, Tuple2(null, -1))
    }
    else if(retryType.equals("data")){
      DataRetryMap.getOrElse(playerId, Tuple2(null, -1))
    }
    else{
      EndRetryMap.getOrElse(playerId, Tuple2(null, -1))
    }
  }

  def getRetryRequest(playerId: UUID, retryType: String): Boolean = {
    if(retryType.equals("inv")){
      InventoryRetryRequestMap.getOrElse(playerId, false)
    }
    else if(retryType.equals("data")){
      DataRetryRequestMap.getOrElse(playerId, false)
    }
    else{
      EndRetryRequestMap.getOrElse(playerId, false)
    }
  }

  def setRetry(playerId: UUID, worldName: String, retryType: String){
    if(retryType.equals("inv")){
      InventoryRetryMap(playerId) = Tuple2(worldName, System.currentTimeMillis()+60000)
    }
    else if(retryType.equals("data")){
      DataRetryMap(playerId) = Tuple2(worldName, System.currentTimeMillis()+60000)
    }
    else{
      EndRetryMap(playerId) = Tuple2(worldName, System.currentTimeMillis()+60000)
    }
  }

  def setRetryRequest(playerId: UUID, retryType: String): Unit ={
    if(retryType.equals("inv")){
      InventoryRetryRequestMap(playerId) = true
    }
    else if(retryType.equals("data")){
      DataRetryRequestMap(playerId) = true
    }
    else{
      EndRetryRequestMap(playerId) = true
    }
  }

  def clearRetry(playerId: UUID, retryType: String): Unit ={
    if(retryType.equals("inv")){
      InventoryRetryMap.remove(playerId)
    }
    else if(retryType.equals("data")){
      DataRetryMap.remove(playerId)
    }
    else{
      EndRetryMap.remove(playerId)
    }
  }

  def clearRetryRequest(playerId: UUID, retryType: String): Unit ={
    if(retryType.equals("inv")){
      InventoryRetryRequestMap.remove(playerId)
    }
    else if(retryType.equals("data")){
      DataRetryRequestMap.remove(playerId)
    }
    else{
      EndRetryRequestMap.remove(playerId)
    }
  }

//  def getEndRetry(playerId: UUID): Tuple2[String, Long] ={
//    playerWorldEndRetryMap.getOrElse(playerId, Tuple2(null, -1))
//  }
//
//  def getRetryRequest(playerId: UUID): Boolean = {
//    playerWorldEndRetryRequests.getOrElse(playerId, false)
//  }
//
//  def setEndRetry(playerId: UUID, worldName: String): Unit ={
//    playerWorldEndRetryMap(playerId) = Tuple2(worldName, System.currentTimeMillis() + 60000)
//  }
//
//  def setRetryRequest(playerId: UUID): Unit ={
//    playerWorldEndRetryRequests(playerId) = true
//  }
//
//  def clearEndRetry(playerId: UUID): Unit ={
//    playerWorldEndRetryMap.remove(playerId)
//  }
//
//  def clearRetryRequest(playerId: UUID): Unit ={
//    playerWorldEndRetryRequests.remove(playerId)
//  }

}
