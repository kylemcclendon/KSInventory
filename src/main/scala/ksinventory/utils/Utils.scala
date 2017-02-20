package ksinventory.utils

import java.util

import collection.JavaConverters._
import ksinventory.models.ColorUDT
import org.bukkit.block.banner.{Pattern, PatternType}
import org.bukkit.potion.{PotionData, PotionType}
import org.bukkit.{Color, DyeColor, FireworkEffect}

object Utils {

  var activeRequests = 0

  def getShortenedWorldName(worldName: String): String ={
    if(worldName.contains('_')){
      worldName.substring(0, worldName.indexOf('_'))
    }
    else{
      worldName
    }
  }

  def getColor(r: Int, g: Int, b: Int): Color = {
    Color.fromRGB(r,g,b)
  }

  def getFireworkEffect(ft: String, f: Boolean, t: Boolean, c: util.List[ColorUDT], fc: util.List[ColorUDT]): FireworkEffect ={
    val fireworkColors = c.asScala.map((color) => getColor(color.red,color.green,color.blue)).asJava
    val fireworkFadeColors = fc.asScala.map((color) => getColor(color.red,color.green,color.blue)).asJava
    FireworkEffect.builder().flicker(true).trail(true).withColor(fireworkColors).withFade(fireworkFadeColors).`with`(FireworkEffect.Type.valueOf(ft)).build()
  }

  def getPotionData(potion_type: String, extended: Boolean, upgraded: Boolean): PotionData ={
    val potionType = potion_type match {
      case "AWKWARD" => PotionType.AWKWARD
      case "FIRE_RESISTANCE" => PotionType.FIRE_RESISTANCE
      case "INSTANT_DAMAGE" => PotionType.INSTANT_DAMAGE
      case "INSTANT_HEAL" => PotionType.INSTANT_HEAL
      case "INVISIBILITY" => PotionType.INVISIBILITY
      case "JUMP" => PotionType.JUMP
      case "LUCK" => PotionType.LUCK
      case "MUNDANE" => PotionType.MUNDANE
      case "NIGHT_VISION" => PotionType.NIGHT_VISION
      case "POISON" => PotionType.POISON
      case "REGEN" => PotionType.REGEN
      case "SLOWNESS" => PotionType.SLOWNESS
      case "SPEED" => PotionType.SPEED
      case "STRENGTH" => PotionType.STRENGTH
      case "THICK" => PotionType.THICK
      case "WATER" => PotionType.WATER
      case "WATER_BREATHING" => PotionType.WATER_BREATHING
      case "WEAKNESS" => PotionType.WEAKNESS
      case _ => PotionType.UNCRAFTABLE
    }
    new PotionData(potionType,extended,upgraded)
  }

  def getAsMinecraftPattern(color: String, pattern: String): Pattern ={
    new Pattern(convertToDyeColor(color),PatternType.getByIdentifier(pattern))
  }

  def convertToDyeColor(color: String): DyeColor = {
    color match {
      case "BLACK" => DyeColor.BLACK
      case "BLUE" => DyeColor.BLUE
      case "BROWN" => DyeColor.BROWN
      case "CYAN" => DyeColor.CYAN
      case "GRAY" => DyeColor.GRAY
      case "GREEN" => DyeColor.GREEN
      case "LIGHT_BLUE" => DyeColor.LIGHT_BLUE
      case "LIME" => DyeColor.LIME
      case "MAGENTA" => DyeColor.MAGENTA
      case "ORANGE" => DyeColor.ORANGE
      case "PINK" => DyeColor.PINK
      case "PURPLE" => DyeColor.PURPLE
      case "RED" => DyeColor.RED
      case "SILVER" => DyeColor.SILVER
      case "WHITE" => DyeColor.WHITE
      case "YELLOW" => DyeColor.YELLOW
    }
  }
}
