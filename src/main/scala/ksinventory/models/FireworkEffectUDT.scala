package ksinventory.models

import java.util
import collection.JavaConverters._
import ksinventory.utils.Utils.getColor

import com.datastax.driver.mapping.annotations.{Field, Frozen, UDT}
import org.bukkit.FireworkEffect

@UDT(keyspace="minecraft", name="firework_effect")
case class FireworkEffectUDT(){
  var firework_type: String = null
  var flicker: Boolean = false
  var trail: Boolean = false
  @Frozen
  var colors: util.List[ColorUDT] = null
  @Frozen
  var fade_colors: util.List[ColorUDT] = null

  def this(ft: String, f: Boolean, t: Boolean, c: util.List[ColorUDT], fc: util.List[ColorUDT]){
    this()
    this.firework_type = ft
    this.flicker = f
    this.trail = t
    this.colors = c
    this.fade_colors = fc
  }

//  def getFireworkEffect: FireworkEffect ={
//    val fireworkColors = colors.asScala.map((color) => getColor(color.red,color.green,color.blue)).asJava
//    val fireworkFadeColors = fade_colors.asScala.map((color) => getColor(color.red,color.green,color.blue)).asJava
//    FireworkEffect.builder().flicker(true).trail(true).withColor(fireworkColors).withFade(fireworkFadeColors).`with`(FireworkEffect.Type.valueOf(firework_type)).build()
//    new FireworkEffect(flicker,trail,fireworkColors,fireworkFadeColors,FireworkEffect.Type.valueOf(firework_type))
//  }
}
