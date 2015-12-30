package net.slipp.support.web

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.apache.commons.io.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ServletDownloadManager {
  /** 다운로드 버퍼 크기 */
  private val BUFFER_SIZE: Int = 16384
  /** 문자 인코딩 */
  private val CHARSET: String = "UTF-8"

  private def addMimeTypeHeader(response: HttpServletResponse, mimetype: String) {
    var mime: String = mimetype
    if (mimetype == null || mimetype.length == 0) {
      mime = "application/octet-stream;"
    }
    response.setContentType(mime + "; charset=" + CHARSET)
  }

  @throws(classOf[UnsupportedEncodingException])
  private def addContentDispositionHeader(request: HttpServletRequest, response: HttpServletResponse, filename: String) {
    val userAgent: String = request.getHeader("User-Agent")
    if (userAgent != null && userAgent.indexOf("MSIE 5.5") > -1) {
      response.setHeader("Content-Disposition", "filename=" + URLEncoder.encode(filename, "UTF-8") + ";")
    }
    else if (userAgent != null && userAgent.indexOf("MSIE") > -1) {
      response.setHeader("Content-Disposition", "filename=" + java.net.URLEncoder.encode(filename, "UTF-8") + ";")
    }
    else {
      response.setHeader("Content-Disposition", "filename=" + new String(filename.getBytes(CHARSET), "latin1") + ";")
    }
  }

  private def addContentLengthHeader(response: HttpServletResponse, filesize: Long) {
    response.setHeader("Content-Length", "" + filesize)
  }

  private def addExpiresHeader(response: HttpServletResponse, expires: Long) {
    if (expires == 0) {
      return
    }
    response.addDateHeader("Expires", System.currentTimeMillis + expires)
  }

  @throws(classOf[IOException])
  private def copyFileContentsToResponse(response: HttpServletResponse, is: InputStream) {
    val buffer: Array[Byte] = new Array[Byte](BUFFER_SIZE)
    var fin: BufferedInputStream = null
    var outs: BufferedOutputStream = null
    try {
      fin = new BufferedInputStream(is)
      outs = new BufferedOutputStream(response.getOutputStream)
      var read: Int = 0
      while ((({
        read = fin.read(buffer); read
      })) != -1) {
        outs.write(buffer, 0, read)
      }
    } finally {
      IOUtils.closeQuietly(outs)
      IOUtils.closeQuietly(fin)
    }
  }
}

class ServletDownloadManager {
  private var log: Logger = LoggerFactory.getLogger(classOf[ServletDownloadManager])

  /**
    * 지정된 파일을 다운로드 한다.
    *
    * @param request
    * @param response
    * @param file
	 * 다운로드할 파일
    * @param expires
	 * 캐시 유지 시간 ms. 0이면 헤더 생략.
    * @throws ServletException
    * @throws IOException
    */
  @throws(classOf[ServletException])
  @throws(classOf[IOException])
  def download(request: HttpServletRequest, response: HttpServletResponse, file: File, originalFileName: String, expires: Long) {
    log.debug("originalFileName : {}", originalFileName)
    val mimetype: String = request.getSession.getServletContext.getMimeType(originalFileName)
    if (file == null || !file.exists || file.length <= 0 || file.isDirectory) {
      log.warn(file.getAbsolutePath + " 파일이 존재하지 않음.")
      setStatusAsNotFound(response)
      return
    }
    var is: InputStream = null
    try {
      is = new FileInputStream(file)
      download(request, response, is, originalFileName, file.length, mimetype, expires)
    } finally {
      IOUtils.closeQuietly(is)
    }
  }

  @throws(classOf[IOException])
  private def setStatusAsNotFound(response: HttpServletResponse) {
    response.setStatus(HttpServletResponse.SC_NOT_FOUND)
    response.getWriter.close
  }

  /**
    * 해당 입력 스트림으로부터 오는 데이터를 다운로드 한다.
    *
    * @param request
    * @param response
    * @param is
	 * 입력 스트림
    * @param filename
	 * 파일 이름
    * @param filesize
	 * 파일 크기
    * @param mimetype
	 * MIME 타입 지정
    * @param expires
	 * 캐시 유지 시간 ms. 0이면 헤더 생략.
    * @throws ServletException
    * @throws IOException
    */
  @throws(classOf[ServletException])
  @throws(classOf[IOException])
  def download(request: HttpServletRequest, response: HttpServletResponse, is: InputStream, filename: String, filesize: Long, mimetype: String, expires: Long) {
    ServletDownloadManager.addMimeTypeHeader(response, mimetype)
    ServletDownloadManager.addExpiresHeader(response, expires)
    ServletDownloadManager.addContentDispositionHeader(request, response, filename)
    ServletDownloadManager.addContentLengthHeader(response, filesize)
    ServletDownloadManager.copyFileContentsToResponse(response, is)
  }
}
