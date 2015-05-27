package net.slipp.support.utils

import java.io.File

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

/**
 * 파일 관련 툴 모음 + 기타 기능 추가. 파일명 관련 기능은 {@link FilenameUtils}를 참조한다.
 */
object SlippFileUtils extends FileUtils {
  /**
   * 현재 파일의 부모 디렉토리 File 객체 확보
   */
  def getParentDir(file: File) = {
    val parentDirPath = FilenameUtils.getFullPathNoEndSeparator(file.getAbsolutePath());
    new File(parentDirPath)
  }
  
  /**
   * 현재 파일의 부모 디렉토리를 그 전의 부모까지 합쳐서 한 번에 생성한다.
   */
  def forceMkParentDir(file: File) = {
    val parentDir = getParentDir(file)
    FileUtils.forceMkdir(parentDir)
    parentDir
  }
}