package net.slipp.support

object ChromWebDriverUtils {
  private var OS: String = System.getProperty("os.name").toLowerCase

  def getChromeWebDriverPath: String = {
    return classOf[ChromWebDriverUtils].getResource("/chromedriver/" + getChromeWebDriverFileNameForOS).getPath
  }

  private def getChromeWebDriverFileNameForOS: String = {
    if (isWindows) {
      return "chromedriver-win.exe"
    }
    if (isMac) {
      return "chromedriver-mac"
    }
    throw new RuntimeException("지원하는 OS 가 없습니다.")
  }

  private def isWindows: Boolean = {
    return (OS.indexOf("win") >= 0)
  }

  private def isMac: Boolean = {
    return (OS.indexOf("mac") >= 0)
  }
}

class ChromWebDriverUtils
