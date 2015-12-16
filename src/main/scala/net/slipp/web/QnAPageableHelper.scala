package net.slipp.web

import org.springframework.data.domain.Sort
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort.Direction

object QnAPageableHelper {
  val DefaultPageNo = 1

  def createPageableByQuestionUpdatedDate(currentPage: Integer, pageSize: Integer) = {
    createPageable(currentPage, pageSize, "updatedDate")
  }

  def createPageableByAnswer(currentPage: Integer, pageSize: Integer) = {
    new PageRequest(revisedPage(currentPage, DefaultPageNo) -1, pageSize);
  }
  
  def createPageableById(currentPage: Integer, pageSize: Integer) = {
    createPageable(currentPage, pageSize, "id")
  }
  
  def createPageableTagId(currentPage: Integer, pageSize: Integer) = {
    createPageable(currentPage, pageSize, "tagId")
  }  
  
  def createPageable(currentPage: Integer, pageSize: Integer, sortField: String) = {
    val sort = new Sort(Direction.DESC, sortField)
    new PageRequest(revisedPage(currentPage, DefaultPageNo) - 1, pageSize, sort)
  }

  def revisedPage(page: Integer): Integer = revisedPage(page, DefaultPageNo)
  
  def revisedPage(page: Integer, defaultPageNo: Integer) = if (page == null) defaultPageNo else page
}