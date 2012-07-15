package net.slipp.support.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * 파일 관련 툴 모음 + 기타 기능 추가. 파일명 관련 기능은 {@link FilenameUtils}를 참조한다.
 */
public class SlippFileUtils extends FileUtils {

	/**
	 * 현재 파일의 부모 디렉토리 File 객체 확보
	 * 
	 * @param file
	 * @return
	 */
	public static File getParentDir(File file) {
		String parentDirPath = FilenameUtils.getFullPathNoEndSeparator(file.getAbsolutePath());
		File parentDir = new File(parentDirPath);
		return parentDir;
	}

	/**
	 * 현재 파일의 부모 디렉토리를 그 전의 부모까지 합쳐서 한 번에 생성한다.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static File forceMkParentDir(File file) throws IOException {
		File parentDir = getParentDir(file);
		forceMkdir(parentDir);
		return parentDir;
	}

}
