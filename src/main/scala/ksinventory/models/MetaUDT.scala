package ksinventory.models

import java.util

import com.datastax.driver.mapping.annotations.{Field, Frozen, UDT}

@UDT(keyspace="minecraft", name="metaudt")
class MetaUDT(){
  @Frozen
  private var banner: util.List[PatternUDT] = null
  @Frozen
  private var book: BookMetaUDT = null
  private var display_name: String = null
  private var egg: String = null
  @Frozen
  private var enchantments: util.Map[String, Integer] = null
  @Frozen
  private var firework: FireworkMetaUDT = null
  private var internals: String = null
  @Frozen
  private var leather: ColorUDT = null
  @Frozen
  private var lore: util.List[String] = null
  @Frozen
  private var map: MapMetaUDT = null
  @Frozen
  private var potion: PotionMetaUDT = null
  private var skull: String = null

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
