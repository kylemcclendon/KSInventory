package ksinventory.models

import java.util

import com.datastax.driver.mapping.annotations.{Field, Frozen, UDT}

@UDT(keyspace="minecraft", name="firework_effect")
case class FireworkEffectUDT(){
  private var firework_type: String = null
  private var flicker: Boolean = false
  private var trail: Boolean = false
  @Frozen
  private var colors: util.List[ColorUDT] = null
  @Frozen
  private var fade_colors: util.List[ColorUDT] = null

  def this(ft: String, f: Boolean, t: Boolean, c: util.List[ColorUDT], fc: util.List[ColorUDT]){
    this()
    this.firework_type = ft
    this.flicker = f
    this.trail = t
    this.colors = c
    this.fade_colors = fc
  }
}
