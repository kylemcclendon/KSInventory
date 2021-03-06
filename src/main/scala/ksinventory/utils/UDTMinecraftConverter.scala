package ksinventory.utils

import java.util

import scala.collection.JavaConverters._
import ksinventory.models.ColorUDT
import org.bukkit.{Color, DyeColor, FireworkEffect}
import org.bukkit.block.banner.{Pattern, PatternType}
import org.bukkit.entity.EntityType
import org.bukkit.potion.{PotionData, PotionType}

object UDTMinecraftConverter {
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

  def getEntityType(entity: String): EntityType = {
    entity match {
      case "BAT" => EntityType.BAT
      case "BLAZE" => EntityType.BLAZE
      case "CAVE_SPIDER" => EntityType.CAVE_SPIDER
      case "CHICKEN" => EntityType.CHICKEN
      case "COW" => EntityType.COW
      case "CREEPER" => EntityType.CREEPER
      case "DONKEY" => EntityType.DONKEY
      case "ELDER_GUARDIAN" => EntityType.ELDER_GUARDIAN
      case "ENDER_DRAGON" => EntityType.ENDER_DRAGON
      case "ENDERMAN" => EntityType.ENDERMAN
      case "ENDERMITE" => EntityType.ENDERMITE
      case "EVOKER" => EntityType.EVOKER
      case "GHAST" => EntityType.GHAST
      case "GIANT" => EntityType.GIANT
      case "GUARDIAN" => EntityType.GUARDIAN
      case "HORSE" => EntityType.HORSE
      case "HUSK" => EntityType.HUSK
      case "IRON_GOLEM" => EntityType.IRON_GOLEM
      case "LLAMA" => EntityType.LLAMA
      case "MAGMA_CUBE" => EntityType.MAGMA_CUBE
      case "MULE" => EntityType.MULE
      case "MUSHROOM_COW" => EntityType.MUSHROOM_COW
      case "OCELOT" => EntityType.OCELOT
      case "PIG" => EntityType.PIG
      case "PIG_ZOMBIE" => EntityType.PIG_ZOMBIE
      case "POLAR_BEAR" => EntityType.POLAR_BEAR
      case "RABBIT" => EntityType.RABBIT
      case "SHEEP" => EntityType.SHEEP
      case "SHULKER" => EntityType.SHULKER
      case "SILVERFISH" => EntityType.SILVERFISH
      case "SKELETON" => EntityType.SKELETON
      case "SKELETON_HORSE" => EntityType.SKELETON_HORSE
      case "SLIME" => EntityType.SLIME
      case "SNOWMAN" => EntityType.SNOWMAN
      case "SPIDER" => EntityType.SPIDER
      case "SQUID" => EntityType.SQUID
      case "STRAY" => EntityType.STRAY
      case "VEX" => EntityType.VEX
      case "VILLAGER" => EntityType.VILLAGER
      case "VINDICATOR" => EntityType.VINDICATOR
      case "WITCH" => EntityType.WITCH
      case "WITHER" => EntityType.WITHER
      case "WITHER_SKELETON" => EntityType.WITHER_SKELETON
      case "WOLF" => EntityType.WOLF
      case "ZOMBIE" => EntityType.ZOMBIE
      case "ZOMBIE_HORSE" => EntityType.ZOMBIE_HORSE
      case "ZOMBIE_VILLAGER" => EntityType.ZOMBIE_VILLAGER
      case _ => null
    }
  }
}