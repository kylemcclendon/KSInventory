package ksinventory.services

import java.util.UUID

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import ksinventory.cache.{PlayerWorldDataCache, RetrieveRetryCache, SaveRetryCache}
import ksinventory.dao.PlayerDataDao
import ksinventory.messager.Messager
import ksinventory.utils.Utils
import org.bukkit.Bukkit.getServer

class DataService(playerDataDao: PlayerDataDao) {
  def setPlayerWorldData(playerId: UUID, worldName: String): Unit ={
    if(PlayerWorldDataCache.playerDataContains(playerId,worldName)){
      val player = getServer.getPlayer(playerId)
      val playerData = PlayerWorldDataCache.getPlayerData(playerId,worldName)

      player.setHealth(playerData._1)
      player.setExp(playerData._2)
      player.setLevel(playerData._3.toInt)
      player.setFoodLevel(playerData._4.toInt)
      player.setSaturation(playerData._5)
    }
    else{
      //Get From DB
      val player = getServer.getPlayer(playerId)
      RetrieveRetryCache.clearRetryRefined(playerId, worldName, "data")

      Utils.activeRequests += 1
      val f : Future[(Float, Float, Float, Float, Float)] = Future {
        playerDataDao.getPlayerData(playerId,worldName)
      }

      f onComplete {
        case Success(playerData) =>
          Utils.activeRequests -= 1
          RetrieveRetryCache.clearRetryRequestRefined(playerId, worldName, "data")
          player.setHealth(playerData._1)
          player.setExp(playerData._2)
          player.setLevel(playerData._3.toInt)
          player.setFoodLevel(playerData._4.toInt)
          player.setSaturation(playerData._5)
          setPlayerDataCache(playerId, worldName, playerData._1, playerData._2, playerData._3, playerData._4, playerData._5)
        case Failure(error) =>
          Utils.activeRequests -=1
          RetrieveRetryCache.setRetryRefined(playerId, worldName, "data")
          RetrieveRetryCache.clearRetryRequestRefined(playerId, worldName, "data")
          Messager.messagePlayerFailure(playerId, "Player Data Retrieve Failed! You can retry the retrieve with /retryGet " + worldName + " data")
          println(error)
      }
    }
  }

  def persistPlayerData(playerId: UUID, worldName: String): Unit ={
    val playerData = PlayerWorldDataCache.getPlayerData(playerId, worldName)
    SaveRetryCache.clearRetryRefined(playerId, worldName, "data")

    //Attempt Save To DB
    Utils.activeRequests += 1
    val f: Future[Unit] = Future {
      playerDataDao.savePlayerWorldData(playerId,worldName,playerData._1,playerData._2,playerData._3,playerData._4,playerData._5)
    }

    f onComplete {
      case Success(_) =>
        Utils.activeRequests -= 1
        SaveRetryCache.clearRetryRequestRefined(playerId, worldName, "data")
        Messager.messagePlayerSuccess(playerId, "Player Data Saved. You can quiet this message with /inv quiet")
      case Failure(error) =>
        Utils.activeRequests -= 1
        SaveRetryCache.setRetryRefined(playerId, worldName, "data")
        SaveRetryCache.clearRetryRequestRefined(playerId, worldName, "data")
        Messager.messagePlayerFailure(playerId, "Player Data Save Failed! You can retry the save with /retrySave " + worldName + " data")
        println(error)
    }
  }

  def setPlayerDataCache(playerId: UUID, worldName: String, health: Float, experience: Float, level: Float, food: Float, saturation: Float): Unit ={
    PlayerWorldDataCache.setPlayerData(playerId, worldName, health, experience, level, food, saturation)
  }
}

object DataService extends DataService(PlayerDataDao)

