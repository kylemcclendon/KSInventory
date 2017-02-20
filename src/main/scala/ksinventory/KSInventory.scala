package ksinventory

import ksinventory.commands.Commands
import ksinventory.database.CassandraDbConnector
import ksinventory.events.{endChestEvents, playerJoinLeaveEvent, worldChangeEvent}
import ksinventory.utils.Utils
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class KSInventory extends JavaPlugin{
  override def onEnable(): Unit = {
    val pm = getServer.getPluginManager
    val commands = new Commands()
    pm.registerEvents(new worldChangeEvent(), this)
    pm.registerEvents(new playerJoinLeaveEvent(), this)
    pm.registerEvents(new endChestEvents(),this)

    getCommand("serialize").setExecutor(commands)
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
    CassandraDbConnector.closeSession
    getLogger.info("Database connection shut down. KSInventory Disabled")
  }
}
