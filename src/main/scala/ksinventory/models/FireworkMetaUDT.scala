package ksinventory.models

import java.util

import com.datastax.driver.mapping.annotations.{Frozen, UDT}

@UDT(keyspace="minecraft", name="firework_meta")
class FireworkMetaUDT(){
  var power: Int = -1
  @Frozen
  var effects: util.List[FireworkEffectUDT] = _

  def this(p: Int, e: util.List[FireworkEffectUDT]){
    this()
    this.power = p
    this.effects = e
  }
}