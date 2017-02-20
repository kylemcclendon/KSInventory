package ksinventory.models

import java.util

import com.datastax.driver.mapping.annotations.{Field, Frozen, UDT}

@UDT(keyspace="minecraft",name="book_meta")
class BookMetaUDT(){
  var author: String = null
  var title: String = null
  @Frozen
  var pages: util.List[String] = null

  def this(a: String, t: String, p: util.List[String]){
    this()
    this.author = a
    this.title = t
    this.pages = p
  }
}