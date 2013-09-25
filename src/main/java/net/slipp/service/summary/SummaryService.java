package net.slipp.service.summary;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import net.slipp.domain.summary.SiteSummary;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public SiteSummary findOneThumbnail(String url) {
		try {
			if (StringUtils.isBlank(url)) {
				return null;
			}
			logger.info("is Image : {} {}", url, isImageDirectURL(url));
			if (isImageDirectURL(url)) {
				return new SiteSummary(FilenameUtils.getName(url), StringUtils.EMPTY, url, url);
			}
			Document doc = Jsoup.connect(url).get();
			return new SiteSummary(doc, url);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	private boolean isImageDirectURL(String url) throws MalformedURLException, IOException {
		URL u = new URL(url);
		URLConnection urlConnection = u.openConnection();
		String contentType = urlConnection.getContentType();
		logger.info("ContentType : {} ", contentType);
		if (contentType.indexOf("image") > -1) {
			return true;
		}
		return false;
	}

}