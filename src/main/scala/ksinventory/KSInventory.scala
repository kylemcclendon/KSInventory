package ksinventory

import ksinventory.commands.Commands
import ksinventory.events.{endChestEvents, playerJoinLeaveEvent, worldChangeEvent}
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
    getLogger.info("KSInventory Disabled")
  }
}
