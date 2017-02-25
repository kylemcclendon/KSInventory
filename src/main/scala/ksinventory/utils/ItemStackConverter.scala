package ksinventory.utils

import java.util
import java.util.UUID
import ksinventory.utils.UDTMinecraftConverter.{getColor,getFireworkEffect,getPotionData,getAsMinecraftPattern}

import collection.JavaConverters._
import ksinventory.models._
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta._

class ItemStackConverter {
  def convertItemStacksToInventory(playerId: UUID, worldName: String, inventory: List[ItemStack]): List[PlayerInventory] ={
    inventory.zipWithIndex.map {
      case(item,index)=>
        if(item == null) {
          new PlayerInventory(playerId,worldName,index,0,0,null,null)
        } else {
          new PlayerInventory(playerId,worldName,index,item.getAmount,item.getDurability.toInt, item.getType.toString, getItemMeta(item))
        }
    }
  }

  def convertItemStacksToEndInventory(playerId: UUID, worldName: String, inventory: List[ItemStack]): List[EndInventory] = {
    inventory.zipWithIndex.map {
      case(item,index)=>
        if(item == null) {
          new EndInventory(playerId,worldName,index,0,0,null,null)
        } else {
          new EndInventory(playerId,worldName,index,item.getAmount,item.getDurability.toInt, item.getType.toString, getItemMeta(item))
        }
    }
  }


  def convertInventoryToItemStacks(inventories: List[PlayerInventory]): List[ItemStack] ={
    inventories.map((inv) => {
      if(inv.material == null){
        null
      }
      else {
        val item = new ItemStack(Material.getMaterial(inv.material), inv.amount, inv.damage.toShort)
        val itemMeta = setProperItemMeta(inv.material, item.getItemMeta, inv.metaUDT)
        item.setItemMeta(itemMeta)
        item
      }
    })
  }

  def convertEndInventoryToItemStacks(inventories: List[EndInventory]): List[ItemStack] ={
    inventories.map((inv)=>{
      if(inv.material == null){
        null
      }
      else{
        val item = new ItemStack(Material.getMaterial(inv.material), inv.amount, inv.damage.toShort)
        val itemMeta = setProperItemMeta(inv.material, item.getItemMeta, inv.metaUDT)
        item.setItemMeta(itemMeta)
        item      }
    })
  }


  private def getItemMeta(item: ItemStack): MetaUDT = {
    if (item.hasItemMeta) {
      var bannerMeta: util.List[PatternUDT] = null
      var bookMeta: BookMetaUDT = null
      var eggMeta: String = null
      var internal: String = null
      var leather: ColorUDT = null
      var mapMeta: MapMetaUDT = null
      var potionMeta: PotionMetaUDT = null
      var skullMeta: String = null
      var fireworkMeta: FireworkMetaUDT = null

      val displayName = item.getItemMeta.getDisplayName
      var enchantments: scala.collection.mutable.Map[String, Integer] = null

      val lore = item.getItemMeta.getLore

      item.getType match {
        case Material.BANNER =>
          val banner = item.getItemMeta.asInstanceOf[BannerMeta]

          val patterns = banner.getPatterns.asScala.map((pattern)=>{
            new PatternUDT(pattern.getColor.toString, pattern.getPattern.toString)
          }).asJava

          bannerMeta = patterns
        case Material.BOOK_AND_QUILL =>
          //Only set pages
          val book = item.getItemMeta.asInstanceOf[BookMeta]

          bookMeta = new BookMetaUDT(null, null, book.getPages)
        case Material.WRITTEN_BOOK =>
          //Set all book meta
          val book = item.getItemMeta.asInstanceOf[BookMeta]

          bookMeta = new BookMetaUDT(book.getAuthor, book.getTitle, book.getPages)
        case Material.MONSTER_EGG =>
          eggMeta = item.getItemMeta.asInstanceOf[SpawnEggMeta].getSpawnedType.toString
        case Material.SKULL_ITEM =>
          //check for skull meta, might not have one
          if(item.hasItemMeta){
            skullMeta = item.getItemMeta.asInstanceOf[SkullMeta].getOwner
          }
        case Material.POTION | Material.LINGERING_POTION | Material.SPLASH_POTION | Material.TIPPED_ARROW =>
          val potion = item.getItemMeta.asInstanceOf[PotionMeta]

          potionMeta = new PotionMetaUDT(potion.getBasePotionData.getType.toString, potion.getBasePotionData.isUpgraded, potion.getBasePotionData.isExtended)
        case Material.LEATHER_BOOTS | Material.LEATHER_CHESTPLATE | Material.LEATHER_HELMET | Material.LEATHER_LEGGINGS =>
          val leatherColor = item.getItemMeta.asInstanceOf[LeatherArmorMeta].getColor
          leather = new ColorUDT(leatherColor.getRed, leatherColor.getGreen, leatherColor.getBlue)
        case Material.MAP =>
          val map = item.getItemMeta.asInstanceOf[MapMeta]
          val mapColor = map.getColor

          mapMeta = new MapMetaUDT(new ColorUDT(mapColor.getRed, mapColor.getGreen, mapColor.getBlue), map.getLocationName, map.isScaling)
        case Material.FIREWORK =>
          val firework = item.getItemMeta.asInstanceOf[FireworkMeta]

          val effects = firework.getEffects.asScala.map((effect)=>{
            val colors = effect.getColors.asScala.map((color)=>{
              new ColorUDT(color.getRed, color.getGreen, color.getBlue)
            })
            val fadeColors = effect.getFadeColors.asScala.map((color)=>{
              new ColorUDT(color.getRed, color.getGreen, color.getBlue)
            })

            new FireworkEffectUDT(effect.getType.toString,effect.hasFlicker,effect.hasTrail, colors.asJava, fadeColors.asJava)
          }).asJava

          fireworkMeta = new FireworkMetaUDT(firework.getPower, effects)

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
             Material.YELLOW_SHULKER_BOX =>
          //TODO set internals
        case _ => //Do nothing
      }

      if(item.getEnchantments.size > 0){
        enchantments = scala.collection.mutable.Map()
        item.getEnchantments.forEach((enchantment, power)=>{
         enchantments += (enchantment.getName -> power)
        })
      }
      new MetaUDT(bannerMeta,bookMeta,displayName,eggMeta,enchantments.asJava,fireworkMeta,internal,leather,lore,mapMeta,potionMeta,skullMeta)
    }
    else {
      null
    }
  }

  private def setProperItemMeta(material: String, meta: ItemMeta, metaudt: MetaUDT): ItemMeta ={
    if(metaudt != null){
      material match {
        case "BANNER" =>
          val bannerMeta = meta.asInstanceOf[BannerMeta]
          val patterns = metaudt.banner.asScala.map((pattern) =>{
            getAsMinecraftPattern(pattern.color, pattern.pattern)
          })
          bannerMeta.setPatterns(patterns.asJava)
          bannerMeta.setDisplayName(metaudt.display_name)
          bannerMeta.setLore(metaudt.lore)
          bannerMeta
        case "BOOK_AND_QUILL" =>
          val bookMeta = meta.asInstanceOf[BookMeta]
          bookMeta.setPages(metaudt.book.pages)
          bookMeta.setDisplayName(metaudt.display_name)
          bookMeta.setLore(metaudt.lore)
          bookMeta
        case "WRITTEN_BOOK" =>
          val bookMeta = meta.asInstanceOf[BookMeta]
          bookMeta.setPages(metaudt.book.pages)
          bookMeta.setAuthor(metaudt.book.author)
          bookMeta.setTitle(metaudt.book.title)
          bookMeta.setDisplayName(metaudt.display_name)
          bookMeta.setLore(metaudt.lore)
          bookMeta
        case "MONSTER_EGG" =>
          val eggMeta = meta.asInstanceOf[SpawnEggMeta]
          eggMeta.setSpawnedType(getEntityType(metaudt.egg))
          eggMeta.setDisplayName(metaudt.display_name)
          eggMeta.setLore(metaudt.lore)
          eggMeta
        case "SKULL_ITEM" =>
          val skullMeta = meta.asInstanceOf[SkullMeta]
          skullMeta.setOwner(metaudt.skull)
          skullMeta.setDisplayName(metaudt.display_name)
          skullMeta.setLore(metaudt.lore)
          skullMeta
        case "POTION" | "LINGERING_POTION" | "SPLASH_POTION" | "TIPPED_ARROW" =>
          val potionMeta = meta.asInstanceOf[PotionMeta]
          potionMeta.setBasePotionData(getPotionData(metaudt.potion.potion_type, metaudt.potion.extended, metaudt.potion.upgraded))
          potionMeta.setDisplayName(metaudt.display_name)
          potionMeta.setLore(metaudt.lore)
          potionMeta
        case "LEATHER_BOOTS" | "LEATHER_LEGGINGS" | "LEATHER_CHESTPLATE" | "LEATHER_HELMET" =>
          val leatherArmorMeta = meta.asInstanceOf[LeatherArmorMeta]
          leatherArmorMeta.setColor(getColor(metaudt.leather.red,metaudt.leather.green,metaudt.leather.blue))
          leatherArmorMeta.setDisplayName(metaudt.display_name)
          leatherArmorMeta.setLore(metaudt.lore)
          leatherArmorMeta
        case "MAP" =>
          val mapMeta = meta.asInstanceOf[MapMeta]
          mapMeta.setColor(getColor(metaudt.map.color.red,metaudt.map.color.green,metaudt.map.color.blue))
          mapMeta.setLocationName(metaudt.map.location)
          mapMeta.setScaling(metaudt.map.scaling)
          mapMeta.setDisplayName(metaudt.display_name)
          mapMeta.setLore(metaudt.lore)
          mapMeta
        case "FIREWORK" =>
          val fireworkMeta = meta.asInstanceOf[FireworkMeta]
          val effects = metaudt.firework.effects.asScala.map((effect) => getFireworkEffect(effect.firework_type,effect.flicker,effect.trail,effect.colors,effect.fade_colors)).asJava
          fireworkMeta.setPower(metaudt.firework.power)
          fireworkMeta.addEffects(effects)
          fireworkMeta.setDisplayName(metaudt.display_name)
          fireworkMeta.setLore(metaudt.lore)
          fireworkMeta
        case _ =>
          if(metaudt.enchantments.size() > 0){
            metaudt.enchantments.asScala.map((enchant) => {
              meta.addEnchant(Enchantment.getByName(enchant._1),enchant._2, false)
            })
          }
          meta.setDisplayName(metaudt.display_name)
          meta.setLore(metaudt.lore)
          meta
      }
    }else{
      null
    }
  }

  private def getEntityType(entity: String): EntityType = {
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
      case "ZOMBIE_VLLAGER" => EntityType.ZOMBIE_VILLAGER
      case _ => null
    }
  }
}
