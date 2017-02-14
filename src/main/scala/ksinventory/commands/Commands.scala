package ksinventory.commands

import net.md_5.bungee.api.ChatColor
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

class Commands extends CommandExecutor{
  var serialized = ""
  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    sender match {
      case player: Player =>
        if (command.getName.equals("serialize")) {
          val serializedItem = player.getInventory.getItemInMainHand.serialize()

          println(serializedItem)
          println(serializedItem.toString)
          serialized = serializedItem.toString
          return true
        }
        else if(command.getName.equals("deserialize")){

        }
      case _ =>
        sender.sendMessage(ChatColor.RED + "Command can only be used by players")
        false
    }
    false
  }
}
