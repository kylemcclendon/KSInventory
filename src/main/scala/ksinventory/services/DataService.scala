package ksinventory.services

import java.util.UUID

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import ksinventory.cache.PlayerWorldInventoryCache
import ksinventory.dao.PlayerDataDao
import ksinventory.messager.Messager
import ksinventory.utils.Utils
import org.bukkit.Bukkit.getServer

class DataService(playerDataDao: PlayerDataDao) {
  def setPlayerWorldData(playerId: UUID, worldName: String): Unit ={
    if(PlayerWorldInventoryCache.playerDataContains(playerId,worldName)){
      val player = getServer.getPlayer(playerId)
      val playerData = PlayerWorldInventoryCache.getPlayerData(playerId,worldName)

      player.setHealth(playerData._1)
      player.setExp(playerData._2)
      player.setLevel(playerData._3.toInt)
      player.setFoodLevel(playerData._4.toInt)
      player.setSaturation(playerData._5)
    }
    else{
      //Get From DB
      val player = getServer.getPlayer(playerId)

      Utils.activeRequests += 1
      val f : Future[Tuple5[Float, Float, Float, Float, Float]] = Future {
        playerDataDao.getPlayerData(playerId,worldName)
      }

      f onComplete {
        case Success(playerData) =>
          Utils.activeRequests -= 1
          player.setHealth(playerData._1)
          player.setExp(playerData._2)
          player.setLevel(playerData._3.toInt)
          player.setFoodLevel(playerData._4.toInt)
          player.setSaturation(playerData._5)
          setPlayerDataCache(playerId, worldName, playerData._1, playerData._2, playerData._3, playerData._4, playerData._5)
        case Failure(error) =>
          Utils.activeRequests -=1
          println(error)
      }
    }
  }

  def savePlayerData(playerId: UUID, worldName: String, health: Float, experience: Float, level: Float, food: Float, saturation: Float): Unit ={
    setPlayerDataCache(playerId,worldName,health,experience,level,food,saturation)

    //Attempt Save To DB
    Utils.activeRequests += 1
    val f: Future[Unit] = Future {
      playerDataDao.savePlayerWorldData(playerId,worldName,health,experience,level,food,saturation)
    }

    f onComplete {
      case Success(success) =>
        Utils.activeRequests -= 1
        Messager.messagePlayerSuccess(playerId, "Player Data Successfully Saved. You can silence this message with /inv quiet")
      case Failure(error) =>
        Utils.activeRequests -= 1
        Messager.messagePlayerFailure(playerId, "Player Data Save Failed! You can retry the save with /inv retry")
        println(error)
    }
  }

  def setPlayerDataCache(playerId: UUID, worldName: String, health: Float, experience: Float, level: Float, food: Float, saturation: Float): Unit ={
    PlayerWorldInventoryCache.setPlayerData(playerId, worldName, health, experience, level, food, saturation)
  }
}

object DataService extends DataService(PlayerDataDao)

