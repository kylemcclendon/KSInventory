package ksinventory

import java.io.File

import collection.JavaConverters._
import ksinventory.cache.MessageSuppressionCache
import ksinventory.commands.Commands
import ksinventory.database.CassandraDbConnector
import ksinventory.events.{endChestEvents, playerJoinLeaveEvent, worldChangeEvent}
import ksinventory.utils.Utils
import org.bukkit.plugin.java.JavaPlugin

class KSInventory extends JavaPlugin{
  override def onEnable(): Unit = {
    val pm = getServer.getPluginManager
    val commands = new Commands(this)
    pm.registerEvents(new worldChangeEvent(), this)
    pm.registerEvents(new playerJoinLeaveEvent(), this)
    pm.registerEvents(new endChestEvents(),this)

    getCommand("inv").setExecutor(commands)
    createConfig()
    MessageSuppressionCache.setSuppressedPlayers(getConfig.getKeys(false).asScala.toSet)
    getLogger.info("Enabling KSInventory")
    getLogger.info("KSInventory Enabled")
  }

  override def onDisable(): Unit = {
    getLogger.info("Disabling KSInventory")
    while(Utils.activeRequests > 0) {
      getLogger.info(Utils.activeRequests + " active database requests still open, waiting 5 seconds for them to resolve...")
      Thread.sleep(5000)
    }

    getLogger.info("All database requests resolved. Shutting down database connection...")
    CassandraDbConnector.closeSession()
    getLogger.info("Database connection shut down. KSInventory Disabled")
  }

  private def createConfig() {
    try {
      if (!getDataFolder.exists()) {
        getDataFolder.mkdirs()
      }
      val file = new File(getDataFolder, "config.yml")
      if (!file.exists()) {
        getLogger.info("Config.yml not found, creating!")
      } else {
        getLogger.info("Config.yml found, loading!")
      }
    } catch {
      case e: Exception => e.printStackTrace()

    }

  }
}
