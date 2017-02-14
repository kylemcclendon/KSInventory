package ksinventory.utils

import java.util.UUID

import collection.JavaConverters._
import ksinventory.models._
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta._

class ItemStackConverter {
  def convertItemStacksToInventory(playerId: UUID, worldId: UUID, inventory: List[ItemStack]): List[Tuple5[Int, Int, Int, String, MetaUDT]] ={
    inventory.zipWithIndex.map {
      case(item,index)=>
        if(item == null){
          Tuple5(index,0,0,null,null)
        }
        else{
          println("isn't null")
          Tuple5(index,item.getAmount, item.getDurability.toInt, item.getType.toString, getItemMeta(item))
        }
    }
  }

  private def getItemMeta(item: ItemStack): MetaUDT = {
    if (item.hasItemMeta) {
      println("has meta")
      var bannerMeta: BannerMetaUDT = null
      var bookMeta: BookMetaUDT = null
      var eggMeta: String = null
      var internal: String = null
      var leather: String = null
      var mapMeta: MapMetaUDT = null
      var potionMeta: Int = -1
      var skullMeta: String = null
      var fireworkMeta: FireworkMetaUDT = null

      println("display name")
      var displayName = item.getItemMeta.getDisplayName
      var enchantments: scala.collection.mutable.Map[String, Int] = null

      println("lore")
      var lore = item.getItemMeta.getLore

      println("matcher")
      item.getType match {
        case Material.BANNER => {
          val banner = item.getItemMeta.asInstanceOf[BannerMeta]
          println(banner)
          println(banner.getBaseColor)
          val baseColor = banner.getBaseColor.toString
          println(baseColor)
          val patterns = banner.getPatterns.asScala.map((pattern)=>{
            PatternUDT(pattern.getColor.toString, pattern.getPattern.toString)
          }).asJava

          println(patterns)

          bannerMeta = BannerMetaUDT(baseColor, patterns)
        }
        case Material.BOOK_AND_QUILL => {
          //Only set pages
          val book = item.getItemMeta.asInstanceOf[BookMeta]

          bookMeta = BookMetaUDT(null, null, book.getPages)
        }
        case Material.WRITTEN_BOOK => {
          //Set all book meta
          val book = item.getItemMeta.asInstanceOf[BookMeta]

          bookMeta = BookMetaUDT(book.getAuthor, book.getTitle, book.getPages)
        }
        case Material.MONSTER_EGG => {
          eggMeta = item.getItemMeta.asInstanceOf[SpawnEggMeta].getSpawnedType.toString
        }
        case Material.SKULL_ITEM => {
          //check for skull meta, might not have one
          if(item.hasItemMeta){
            skullMeta = item.getItemMeta.asInstanceOf[SkullMeta].getOwner
          }
        }
        case Material.POTION | Material.LINGERING_POTION | Material.SPLASH_POTION | Material.TIPPED_ARROW => {
          //TODO get potion dv
        }
        case Material.LEATHER_BOOTS | Material.LEATHER_CHESTPLATE | Material.LEATHER_HELMET | Material.LEATHER_LEGGINGS => {
          leather = item.getItemMeta.asInstanceOf[LeatherArmorMeta].getColor.toString
        }
        case Material.MAP => {
          val map = item.getItemMeta.asInstanceOf[MapMeta]

          mapMeta = MapMetaUDT(map.getColor.toString, map.getLocationName, map.isScaling)
        }
        case Material.FIREWORK => {
          val firework = item.getItemMeta.asInstanceOf[FireworkMeta]

          val effects = firework.getEffects.asScala.map((effect)=>{
            val colors = effect.getColors.asScala.map((color)=>{
              color.toString
            })
            FireworkEffectUDT(effect.getType.toString,effect.hasFlicker,effect.hasTrail, colors.asJava)
          }).asJava

          fireworkMeta = FireworkMetaUDT(firework.getPower, effects)
        }

        case Material.BLACK_SHULKER_BOX |
             Material.BLUE_SHULKER_BOX |
             Material.BROWN_SHULKER_BOX |
             Material.CYAN_SHULKER_BOX |
             Material.GRAY_SHULKER_BOX |
             Material.GREEN_SHULKER_BOX |
             Material.LIGHT_BLUE_SHULKER_BOX |
             Material.LIME_SHULKER_BOX |
             Material.MAGENTA_SHULKER_BOX |
             Material.ORANGE_SHULKER_BOX |
             Material.PINK_SHULKER_BOX |
             Material.PURPLE_SHULKER_BOX |
             Material.RED_SHULKER_BOX |
             Material.SILVER_SHULKER_BOX |
             Material.WHITE_SHULKER_BOX |
             Material.YELLOW_SHULKER_BOX => {
          //TODO set internals
        }
        case _ => {
          //Do nothing
        }
      }

      println("pre enchantments")
      if(item.getEnchantments.size > 0){
        println("has enchantments")
        enchantments = scala.collection.mutable.Map()
        item.getEnchantments.forEach((enchantment, power)=>{
          println(enchantment.getName)
          println(power)
         enchantments += (enchantment.getName -> power)
        })
      }

      MetaUDT(bannerMeta,bookMeta,displayName,eggMeta,enchantments.asJava,fireworkMeta,internal,leather,lore,mapMeta,potionMeta,skullMeta)
    }
    else {
      null
    }
  }
}
