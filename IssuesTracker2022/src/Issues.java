
public class Issues {
	public String title;
	public String date;
	public String info;
	public String reportor;
	public String status;
	public int issueNo;
	public String whoTake;

	public Issues() {

	}

	public Issues(int issueNo, String title, String date, String info, String reportor, String status, String whoTake) {
		this.issueNo = issueNo;
		this.title = title;
		this.date = date;
		this.info = info;
		this.reportor = reportor;
		this.status = status;

		this.whoTake = whoTake;

	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getReportor() {
		return reportor;
	}

	public void setReportor(String reportor) {
		this.reportor = reportor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(int issueNo) {
		this.issueNo = issueNo;
	}

	public String getWhoTake() {
		return whoTake;
	}

	public void setWhoTake(String whoTake) {
		this.whoTake = whoTake;
	}

	@Override
	public String toString() {
		return "" + issueNo;
	}

}
