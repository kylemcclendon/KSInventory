package ksinventory.models

import java.util
import com.datastax.driver.mapping.annotations.{Frozen, UDT}

@UDT(keyspace="minecraft", name="firework_effect")
case class FireworkEffectUDT(){
  var firework_type: String = _
  var flicker: Boolean = false
  var trail: Boolean = false
  @Frozen
  var colors: util.List[ColorUDT] = _
  @Frozen
  var fade_colors: util.List[ColorUDT] = _

  def this(ft: String, f: Boolean, t: Boolean, c: util.List[ColorUDT], fc: util.List[ColorUDT]){
    this()
    this.firework_type = ft
    this.flicker = f
    this.trail = t
    this.colors = c
    this.fade_colors = fc
  }
}
