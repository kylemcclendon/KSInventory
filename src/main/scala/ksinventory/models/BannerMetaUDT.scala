package ksinventory.models

import java.util
import com.datastax.driver.mapping.annotations.UDT

@UDT(keyspace = "minecraft", name="banner_meta")
class BannerMetaUDT(){
  private var base_color: String = _
  private var patterns: util.List[PatternUDT] = _

  def this(bc: String, pats: util.List[PatternUDT]){
    this()
    this.base_color = bc
    this.patterns = pats
  }
}