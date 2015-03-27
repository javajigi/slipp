package net.slipp.service.summary;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import net.slipp.domain.summary.SiteSummary;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {
	private final String CONTENT_TYPE_IMAGE = "image";
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private int TIMEOUT = 3 * 1000;

	@Cacheable(value = "smallTalkCache", key = "#url")
	public SiteSummary findOneThumbnail(String url) {
		try {
			if (StringUtils.isBlank(url)) {
				return null;
			}
			if (isImageDirectURL(url)) {
				return new SiteSummary(FilenameUtils.getName(url), StringUtils.EMPTY, url, url);
			}
			Document doc = Jsoup.connect(url).followRedirects(true).timeout(TIMEOUT).get();
			return new SiteSummary(doc, url);
		} catch (SocketTimeoutException te) {
			logger.warn("URL Connection TimeOut : {} - {}", url, te.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	private boolean isImageDirectURL(String url) throws MalformedURLException, IOException {
		URL u = new URL(url);
		URLConnection urlConnection = u.openConnection();
		urlConnection.setConnectTimeout(TIMEOUT);
		String contentType = urlConnection.getContentType();
		if (isContentTypeImage(contentType)) {
			return true;
		}
		return false;
	}

	private boolean isContentTypeImage(String contentType) {
		return contentType.contains(CONTENT_TYPE_IMAGE);
	}
}