package net.slipp.service.summary;

import java.io.IOException;

import net.slipp.domain.summary.SiteSummary;

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
			if( StringUtils.isBlank(url) ){
				return null;
			}
			// Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
			Document doc = Jsoup.connect(url).get();
			return new SiteSummary(doc);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}