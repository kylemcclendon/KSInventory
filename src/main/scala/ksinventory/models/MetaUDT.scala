package ksinventory.models

import java.util

import com.datastax.driver.mapping.annotations.{Field, Frozen, UDT}

@UDT(keyspace="minecraft", name="metaudt")
class MetaUDT(){
  @Frozen
  var banner: util.List[PatternUDT] = null
  @Frozen
  var book: BookMetaUDT = null
  var display_name: String = null
  var egg: String = null
  @Frozen
  var enchantments: util.Map[String, Integer] = null
  @Frozen
  var firework: FireworkMetaUDT = null
  var internals: String = null
  @Frozen
  var leather: ColorUDT = null
  @Frozen
  var lore: util.List[String] = null
  @Frozen
  var map: MapMetaUDT = null
  @Frozen
  var potion: PotionMetaUDT = null
  var skull: String = null

  def this(b: util.List[PatternUDT], bk: BookMetaUDT, dn: String, e: String, es: util.Map[String, Integer], f: FireworkMetaUDT, i: String, le: ColorUDT, lo: util.List[String], m: MapMetaUDT, p: PotionMetaUDT, s: String){
    this()
    this.banner = b
    this.book = bk
    this.display_name = dn
    this.egg = e
    this.enchantments = es
    this.firework = f
    this.internals = i
    this.leather = le
    this.lore = lo
    this.map = m
    this.potion = p
    this.skull = s
  }
}
