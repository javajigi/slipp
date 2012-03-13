package supports;

public class Pager {
	private static final int DEFAULT_PAGE_NO = 1;
	static final int DEFAULT_COUNT_PER_PAGE = 20;

	private int pageNo;
	private int countPerPage = DEFAULT_COUNT_PER_PAGE;

	public Pager(int pageNo) {
		this(pageNo, DEFAULT_COUNT_PER_PAGE);
	}

	public Pager(int pageNo, int countPerPage) {
		if (pageNo < 1) {
			this.pageNo = DEFAULT_PAGE_NO;
		} else {
			this.pageNo = pageNo;
		}
		this.countPerPage = countPerPage;
	}

	public int getOffset() {
		return (pageNo - 1) * DEFAULT_COUNT_PER_PAGE;
	}

	public int getPageNo() {
		return this.pageNo;
	}

	public int getCountPerPage() {
		return this.countPerPage;
	}

	public int getPreviousPageNo() {
		return getPageNo() == 1 ? getPageNo() : getPageNo() - 1;
	}

	public int getNextPageNo(int resultCount) {
		return resultCount < DEFAULT_COUNT_PER_PAGE ? getPageNo() : getPageNo() + 1;
	}

}
