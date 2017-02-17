package ksinventory.models

import java.util

import com.datastax.driver.mapping.annotations.{Field, Frozen, UDT}

@UDT(keyspace="minecraft", name="firework_meta")
class FireworkMetaUDT(){
  private var power: Int = -1
  @Frozen
  private var effects: util.List[FireworkEffectUDT] = null

  def this(p: Int, e: util.List[FireworkEffectUDT]){
    this()
    this.power = p
    this.effects = e
  }
}