package supports;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {
	private static long ONE_SECOND = 1000L;

	private static long ONE_MINUTE = ONE_SECOND * 60;

	private static long ONE_HOUR = ONE_MINUTE * 60;

	private static long ONE_DAY = ONE_HOUR * 24;

	public static enum DateFormat {
		LONG("yyyy.MM.dd HH:mm"), SHORT("yyyy.MM.dd");
		private String formatString;

		DateFormat(String formatString) {
			this.formatString = formatString;
		}

		public String getFormatString() {
			return formatString;
		}
	}
	
	public static String articleDate(Date date, DateFormat dateFormat) {
		return articleDate(now(), date, dateFormat);
	}

	public static String articleDate(Date baseDate, Date date, DateFormat dateFormat) {
		if (date == null) {
			return null;
		}

		long baseTime = baseDate.getTime();
		long dateTime = date.getTime();

		long diff = baseTime - dateTime;

		if (diff < 0) {
			return formatArticleDate(date, dateFormat);
		} else if (diff < ONE_MINUTE) {
			return (diff / ONE_SECOND) + "초 전";
		} else if (diff < ONE_HOUR) {
			return (diff / ONE_MINUTE) + "분 전";
		} else if (diff < ONE_DAY) {
			return (diff / ONE_HOUR) + "시간 전";
		}

		return formatArticleDate(date, dateFormat);
	}
	
	private static String formatArticleDate(Date date, DateFormat dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat.getFormatString());
		return sdf.format(date);
	}
	
	public static Date now(){
		return new Date(System.currentTimeMillis() + TimeZone.getTimeZone("GMT+9").getRawOffset());
	}
}
