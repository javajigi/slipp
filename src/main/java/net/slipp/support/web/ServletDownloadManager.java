package net.slipp.support.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletDownloadManager {

	private Logger log = LoggerFactory.getLogger(ServletDownloadManager.class);

	/** 다운로드 버퍼 크기 */
	private static final int BUFFER_SIZE = 16384; // 16kb

	/** 문자 인코딩 */
	private static final String CHARSET = "UTF-8";

	/**
	 * 지정된 파일을 다운로드 한다.
	 *
	 * @param request
	 * @param response
	 * @param file
	 *            다운로드할 파일
	 * @param expires
	 *            캐시 유지 시간 ms. 0이면 헤더 생략.
	 * @throws ServletException
	 * @throws IOException
	 */
	public void download(HttpServletRequest request, HttpServletResponse response, File file, String originalFileName, long expires)
			throws ServletException, IOException {
		log.debug("originalFileName : {}", originalFileName);
		String mimetype = request.getSession().getServletContext().getMimeType(originalFileName);

		if (file == null || !file.exists() || file.length() <= 0 || file.isDirectory()) {
			log.warn(file.getAbsolutePath() + " 파일이 존재하지 않음.");

			setStatusAsNotFound(response);
			return;
		}

		InputStream is = null;

		try {
			is = new FileInputStream(file);
			download(request, response, is, originalFileName, file.length(), mimetype, expires);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	private void setStatusAsNotFound(HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		response.getWriter().close();
	}

	/**
	 * 해당 입력 스트림으로부터 오는 데이터를 다운로드 한다.
	 *
	 * @param request
	 * @param response
	 * @param is
	 *            입력 스트림
	 * @param filename
	 *            파일 이름
	 * @param filesize
	 *            파일 크기
	 * @param mimetype
	 *            MIME 타입 지정
	 * @param expires
	 *            캐시 유지 시간 ms. 0이면 헤더 생략.
	 * @throws ServletException
	 * @throws IOException
	 */
	public void download(HttpServletRequest request, HttpServletResponse response, InputStream is, String filename,
			long filesize, String mimetype, long expires) throws ServletException, IOException {
		addMimeTypeHeader(response, mimetype);

		addExpiresHeader(response, expires);

		addContentDispositionHeader(request, response, filename);

		addContentLengthHeader(response, filesize);

		copyFileContentsToResponse(response, is);
	}

	private static void addMimeTypeHeader(HttpServletResponse response, String mimetype) {
		String mime = mimetype;

		if (mimetype == null || mimetype.length() == 0) {
			mime = "application/octet-stream;";
		}
		response.setContentType(mime + "; charset=" + CHARSET);
	}

	private static void addContentDispositionHeader(HttpServletRequest request, HttpServletResponse response,
			String filename) throws UnsupportedEncodingException {
		// 아래 부분에서 euc-kr 을 utf-8 로 바꾸거나 URLEncoding을 안하거나 등의 테스트를
		// 해서 한글이 정상적으로 다운로드 되는 것으로 지정한다.
		String userAgent = request.getHeader("User-Agent");

		if (userAgent != null && userAgent.indexOf("MSIE 5.5") > -1) { // MS IE 5.5 이하
			response.setHeader("Content-Disposition", "filename=" + URLEncoder.encode(filename, "UTF-8") + ";");
		} else if (userAgent != null && userAgent.indexOf("MSIE") > -1) { // MS IE (보통은 6.x 이상 가정)
			response.setHeader("Content-Disposition", "filename=" + java.net.URLEncoder.encode(filename, "UTF-8") + ";");
		} else { // 모질라나 오페라
			response.setHeader("Content-Disposition", "filename=" + new String(filename.getBytes(CHARSET), "latin1")
					+ ";");
		}
	}

	private static void addContentLengthHeader(HttpServletResponse response, long filesize) {
		response.setHeader("Content-Length", "" + filesize);
	}

	private static void addExpiresHeader(HttpServletResponse response, long expires) {
		if (expires == 0) {
			return;
		}

		response.addDateHeader("Expires", System.currentTimeMillis() + expires);
	}

	private static void copyFileContentsToResponse(HttpServletResponse response, InputStream is) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		BufferedInputStream fin = null;
		BufferedOutputStream outs = null;

		try {
			fin = new BufferedInputStream(is);
			outs = new BufferedOutputStream(response.getOutputStream());
			int read = 0;

			while ((read = fin.read(buffer)) != -1) {
				outs.write(buffer, 0, read);
			}
		} finally {
			IOUtils.closeQuietly(outs);
			IOUtils.closeQuietly(fin);
		}
	}
}
