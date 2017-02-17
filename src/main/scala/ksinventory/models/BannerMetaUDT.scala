package ksinventory.models

import java.util
import com.datastax.driver.mapping.annotations.{Field, UDT}

@UDT(keyspace = "minecraft", name="banner_meta")
class BannerMetaUDT(){
  private var base_color: String = null
  private var patterns: util.List[PatternUDT] = null

  def this(bc: String, pats: util.List[PatternUDT]){
    this()
    this.base_color = bc
    this.patterns = pats
  }
}