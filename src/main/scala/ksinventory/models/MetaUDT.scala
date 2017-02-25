package ksinventory.models

import java.util

import com.datastax.driver.mapping.annotations.{Frozen, UDT}

@UDT(keyspace="minecraft", name="metaudt")
class MetaUDT(){
  @Frozen
  var banner: util.List[PatternUDT] = _
  @Frozen
  var book: BookMetaUDT = _
  var display_name: String = _
  var egg: String = _
  @Frozen
  var enchantments: util.Map[String, Integer] = _
  @Frozen
  var firework: FireworkMetaUDT = _
  var internals: String = _
  @Frozen
  var leather: ColorUDT = _
  @Frozen
  var lore: util.List[String] = _
  @Frozen
  var map: MapMetaUDT = _
  @Frozen
  var potion: PotionMetaUDT = _
  var skull: String = _

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
