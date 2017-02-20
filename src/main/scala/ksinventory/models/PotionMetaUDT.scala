package ksinventory.models

import com.datastax.driver.mapping.annotations.{Field, UDT}
import org.bukkit.potion.{PotionData, PotionType}

@UDT(keyspace = "minecraft", name="potion_meta")
class PotionMetaUDT(){
  var potion_type: String = null
  var upgraded: Boolean = false
  var extended: Boolean = false

  def this(pt: String, u: Boolean, e: Boolean){
    this()
    this.potion_type = pt
    this.upgraded = u
    this.extended = e
  }

//  def getPotionData: PotionData ={
//    val potionType = this.potion_type match {
//      case "AWKWARD" => PotionType.AWKWARD
//      case "FIRE_RESISTANCE" => PotionType.FIRE_RESISTANCE
//      case "INSTANT_DAMAGE" => PotionType.INSTANT_DAMAGE
//      case "INSTANT_HEAL" => PotionType.INSTANT_HEAL
//      case "INVISIBILITY" => PotionType.INVISIBILITY
//      case "JUMP" => PotionType.JUMP
//      case "LUCK" => PotionType.LUCK
//      case "MUNDANE" => PotionType.MUNDANE
//      case "NIGHT_VISION" => PotionType.NIGHT_VISION
//      case "POISON" => PotionType.POISON
//      case "REGEN" => PotionType.REGEN
//      case "SLOWNESS" => PotionType.SLOWNESS
//      case "SPEED" => PotionType.SPEED
//      case "STRENGTH" => PotionType.STRENGTH
//      case "THICK" => PotionType.THICK
//      case "WATER" => PotionType.WATER
//      case "WATER_BREATHING" => PotionType.WATER_BREATHING
//      case "WEAKNESS" => PotionType.WEAKNESS
//      case _ => PotionType.UNCRAFTABLE
//    }
//    new PotionData(potionType,this.extended,this.upgraded)
//  }
}
