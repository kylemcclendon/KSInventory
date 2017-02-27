package ksinventory.utils

import org.bukkit.Bukkit
import scala.collection.JavaConverters._

object Utils {
  var activeRequests = 0

  def getShortenedWorldName(worldName: String): String ={
    if(worldName.contains('_')){
      worldName.substring(0, worldName.indexOf('_'))
    }
    else{
      worldName
    }
  }

  def getAllWorldNames: Set[String] = {
    Bukkit.getWorlds.asScala.map((world) => {
      getShortenedWorldName(world.getName)
    }).toSet
  }
}
