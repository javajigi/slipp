package net.slipp.service.summary;

import java.io.IOException;

import net.slipp.domain.summary.SiteSummary;

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
			// Document doc =
			// Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; ko-KR; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").get();
			Document doc = Jsoup.connect(url).get();
			return new SiteSummary(doc);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}