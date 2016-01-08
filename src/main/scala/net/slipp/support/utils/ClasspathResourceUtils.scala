package net.slipp.support.utils

import java.io.File
import java.io.InputStream

/**
  * Classpath 내에 있는 리소스를 로딩하는 도구 모음
  *
  * resourcePath 는 최상위 클래스 패스 일 경우에는 / 없이 파일명만 있으면 된다.
  */
object ClasspathResourceUtils {
  /**
    * 클래스패스내에 있는 파일을 읽어 File 객체로 반환한다.
    *
    * @param resourcePath
	 * 클래스패스내의 리소스 경로
    * @return
    */
  def getResource(resourcePath: String): File = {
    val loader: ClassLoader = Thread.currentThread.getContextClassLoader
    return new File(loader.getResource(resourcePath).getFile)
  }

  /**
    * 클래스패스내에 있는 파일을 읽어 InputStream으로 반환한다.
    *
    * @param resourcePath
    * @return
    */
  def getResourceAsStream(resourcePath: String): InputStream = {
    val loader: ClassLoader = Thread.currentThread.getContextClassLoader
    return loader.getResourceAsStream(resourcePath)
  }
}

