package ksinventory.models

import java.util

import com.datastax.driver.mapping.annotations.{Frozen, UDT}

@UDT(keyspace="minecraft",name="book_meta")
class BookMetaUDT(){
  var author: String = _
  var title: String = _
  @Frozen
  var pages: util.List[String] = _

  def this(a: String, t: String, p: util.List[String]){
    this()
    this.author = a
    this.title = t
    this.pages = p
  }
}