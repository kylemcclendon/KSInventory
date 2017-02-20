package ksinventory.models

import com.datastax.driver.mapping.annotations.{Field, UDT}
import org.bukkit.DyeColor
import org.bukkit.block.banner.{Pattern, PatternType}

@UDT(keyspace = "minecraft", name = "pattern")
class PatternUDT(){
  var color: String = null
  var pattern: String = null

  def this(c: String, p: String){
    this()
    this.color = c
    this.pattern = p
  }
}