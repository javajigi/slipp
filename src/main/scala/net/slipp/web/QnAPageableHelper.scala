package net.slipp.web

import org.springframework.data.domain.Sort
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort.Direction

object QnAPageableHelper {
  val DefaultPageNo = 1

  def createPageableByQuestionUpdatedDate(currentPage: Integer, pageSize: Integer) = {
    val sort = new Sort(Direction.DESC, "updatedDate")
    new PageRequest(currentPage - 1, pageSize, sort)
  }

  def createPageableByAnswerCreatedDate(currentPage: Integer, pageSize: Integer) = {
    val sort = new Sort(Direction.DESC, "createdDate")
    new PageRequest(currentPage - 1, pageSize, sort)
  }

  def revisedPage(page: Integer): Integer = if (page == null) DefaultPageNo else page
}