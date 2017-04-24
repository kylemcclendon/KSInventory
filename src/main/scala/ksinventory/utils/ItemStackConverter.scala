package ksinventory.utils

import java.util
import java.util.UUID

import ksinventory.utils.UDTMinecraftConverter.{getAsMinecraftPattern, getColor, getEntityType, getFireworkEffect, getPotionData}

import scala.collection.JavaConverters._
import ksinventory.models._
import org.bukkit.Material
import org.bukkit.block.ShulkerBox
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta._

class ItemStackConverter {

  //MC -> DB
  def convertItemStacksToInventory(playerId: UUID, worldName: String, inventory: List[ItemStack]): List[PlayerInventory] ={
    inventory.zipWithIndex.map {
      case(item,index)=>
        if(item == null) {
          new PlayerInventory(playerId,worldName,index,0,0,null,null, null)
        } else {
          new PlayerInventory(playerId,worldName,index,item.getAmount,item.getDurability.toInt, item.getType.toString, getItemMeta(item), getInternalInventory(item))
        }
    }
  }

  def convertItemStacksToEndInventory(playerId: UUID, worldName: String, inventory: List[ItemStack]): List[EndInventory] = {
    inventory.zipWithIndex.map {
      case(item,index)=>
        if(item == null) {
          new EndInventory(playerId,worldName,index,0,0,null,null, null)
        } else {
          new EndInventory(playerId,worldName,index,item.getAmount,item.getDurability.toInt, item.getType.toString, getItemMeta(item), getInternalInventory(item))
        }
    }
  }

  def convertItemStacksToItemUDT(inventory: List[ItemStack]): List[ItemUDT] = {
    inventory.zipWithIndex.map{
      case(item, index)=>
        if(item == null){
          new ItemUDT(index, 0, 0, null, null)
        }
        else{
          new ItemUDT(index, item.getAmount, item.getDurability, item.getType.toString, getItemMeta(item))
        }
    }
  }

  private def getItemMeta(item: ItemStack): MetaUDT = {
    if (item.hasItemMeta) {
      var bannerMeta: util.List[PatternUDT] = null
      var bookMeta: BookMetaUDT = null
      var eggMeta: String = null
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
        case Material.ENCHANTED_BOOK =>
          val enchants = item.getItemMeta.asInstanceOf[EnchantmentStorageMeta].getStoredEnchants
          enchantments = scala.collection.mutable.Map()
          enchants.forEach((enchantment, power)=>{
            enchantments += (enchantment.getName -> power)
          })
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
        case _ => //Do nothing
      }

      if(item.getEnchantments.size > 0 && enchantments == null){
        enchantments = scala.collection.mutable.Map()
        item.getEnchantments.forEach((enchantment, power)=>{
          enchantments += (enchantment.getName -> power)
        })
      }
      new MetaUDT(bannerMeta,bookMeta,displayName,eggMeta,enchantments.asJava,fireworkMeta,leather,lore,mapMeta,potionMeta,skullMeta)
    }
    else {
      null
    }
  }

  private def getInternalInventory(item: ItemStack): util.List[ItemUDT] ={
    if(item != null && item.getType.toString.contains("SHULKER_BOX")){
      val inventory = item.getItemMeta.asInstanceOf[BlockStateMeta].getBlockState.asInstanceOf[ShulkerBox].getInventory
      if(inventory != null) {
        val contents = inventory.getContents.toList
        return convertItemStacksToItemUDT(contents).asJava
      }
    }
    null
  }

  //DB -> MC
  def convertInventoryToItemStacks(inventories: List[PlayerInventory]): List[ItemStack] ={
    inventories.map((inv) => {
      if(inv.material == null){
        null
      }
      else {
        val item = new ItemStack(Material.getMaterial(inv.material), inv.amount, inv.damage.toShort)
        val itemMeta = setProperItemMeta(inv.material, item.getItemMeta, inv.metaUDT, inv.internal_inventory)
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
        val itemMeta = setProperItemMeta(inv.material, item.getItemMeta, inv.metaUDT, inv.internal_inventory)
        item.setItemMeta(itemMeta)
        item
      }
    })
  }

  private def convertItemUDTToItemStack(itemUDT: ItemUDT): ItemStack = {
    if(itemUDT == null || itemUDT.material == null){
      return null
    }
    val item = new ItemStack(Material.getMaterial(itemUDT.material), itemUDT.amount, itemUDT.damage.toShort)
    val itemMeta = setProperItemMeta(itemUDT.material, item.getItemMeta, itemUDT.metaUDT, null)
    item.setItemMeta(itemMeta)
    item
  }

  private def setProperItemMeta(material: String, meta: ItemMeta, metaudt: MetaUDT, internalInventory: util.List[ItemUDT]): ItemMeta ={
    if(metaudt != null){
      material match {
        case "BANNER" =>
          val bannerMeta = meta.asInstanceOf[BannerMeta]
          val patterns = metaudt.banner.asScala.map((pattern) =>{
            getAsMinecraftPattern(pattern.color, pattern.pattern)
          })
          bannerMeta.setPatterns(patterns.asJava)
          setGeneralMeta(bannerMeta, metaudt)
        case "BOOK_AND_QUILL" =>
          val bookMeta = meta.asInstanceOf[BookMeta]
          bookMeta.setPages(metaudt.book.pages)
          setGeneralMeta(bookMeta, metaudt)
        case "WRITTEN_BOOK" =>
          val bookMeta = meta.asInstanceOf[BookMeta]
          bookMeta.setPages(metaudt.book.pages)
          bookMeta.setAuthor(metaudt.book.author)
          bookMeta.setTitle(metaudt.book.title)
          setGeneralMeta(bookMeta, metaudt)
        case "ENCHANTED_BOOK" =>
          val enchantedBook = meta.asInstanceOf[EnchantmentStorageMeta]
          metaudt.enchantments.asScala.foreach((enchant) => {
            enchantedBook.addStoredEnchant(Enchantment.getByName(enchant._1), enchant._2, true)
          })
          setGeneralMeta(enchantedBook, metaudt, isEnchantedBook = true)
        case "MONSTER_EGG" =>
          val eggMeta = meta.asInstanceOf[SpawnEggMeta]
          eggMeta.setSpawnedType(getEntityType(metaudt.egg))
          setGeneralMeta(eggMeta, metaudt)
        case "SKULL_ITEM" =>
          val skullMeta = meta.asInstanceOf[SkullMeta]
          skullMeta.setOwner(metaudt.skull)
          setGeneralMeta(skullMeta, metaudt)
        case "POTION" | "LINGERING_POTION" | "SPLASH_POTION" | "TIPPED_ARROW" =>
          val potionMeta = meta.asInstanceOf[PotionMeta]
          potionMeta.setBasePotionData(getPotionData(metaudt.potion.potion_type, metaudt.potion.extended, metaudt.potion.upgraded))
          setGeneralMeta(potionMeta, metaudt)
        case "LEATHER_BOOTS" | "LEATHER_LEGGINGS" | "LEATHER_CHESTPLATE" | "LEATHER_HELMET" =>
          val leatherArmorMeta = meta.asInstanceOf[LeatherArmorMeta]
          leatherArmorMeta.setColor(getColor(metaudt.leather.red,metaudt.leather.green,metaudt.leather.blue))
          setGeneralMeta(leatherArmorMeta, metaudt)
        case "MAP" =>
          val mapMeta = meta.asInstanceOf[MapMeta]
          mapMeta.setColor(getColor(metaudt.map.color.red,metaudt.map.color.green,metaudt.map.color.blue))
          mapMeta.setLocationName(metaudt.map.location)
          mapMeta.setScaling(metaudt.map.scaling)
          setGeneralMeta(mapMeta, metaudt)
        case "FIREWORK" =>
          val fireworkMeta = meta.asInstanceOf[FireworkMeta]
          val effects = metaudt.firework.effects.asScala.map((effect) => getFireworkEffect(effect.firework_type,effect.flicker,effect.trail,effect.colors,effect.fade_colors)).asJava
          fireworkMeta.setPower(metaudt.firework.power)
          fireworkMeta.addEffects(effects)
          setGeneralMeta(fireworkMeta, metaudt)
        case `material` if material.contains("SHULKER_BOX") =>
          val shulkerMeta = meta.asInstanceOf[BlockStateMeta]
          if(internalInventory != null){
            val itemStacks = internalInventory.asScala.map((itemUDT) => {
              convertItemUDTToItemStack(itemUDT)
            }).toArray
            val blockState = shulkerMeta.getBlockState
            blockState.asInstanceOf[ShulkerBox].getInventory.setContents(itemStacks)
            shulkerMeta.setBlockState(blockState)
          }
          setGeneralMeta(shulkerMeta, metaudt)
        case _ =>
          setGeneralMeta(meta, metaudt)
      }
    }else{
      null
    }
  }

  def setGeneralMeta(meta: ItemMeta, metaUDT: MetaUDT, isEnchantedBook: Boolean = false): ItemMeta = {
    val newMeta = meta
    if(metaUDT.enchantments.size() > 0 && !isEnchantedBook){
      metaUDT.enchantments.asScala.map((enchant) => {
        newMeta.addEnchant(Enchantment.getByName(enchant._1),enchant._2, false)
      })
    }
    newMeta.setDisplayName(metaUDT.display_name)
    newMeta.setLore(metaUDT.lore)
    newMeta
  }
}
