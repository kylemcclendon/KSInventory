package ksinventory.models

import java.util

import com.datastax.driver.mapping.annotations.{Field, Frozen, UDT}

@UDT(keyspace="minecraft",name="book_meta")
class BookMetaUDT(){
  private var author: String = null
  private var title: String = null
  @Frozen
  private var pages: util.List[String] = null

  def this(a: String, t: String, p: util.List[String]){
    this()
    this.author = a
    this.title = t
    this.pages = p
  }
}