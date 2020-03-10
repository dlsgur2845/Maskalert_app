package hyuk.com.maskalert_app.object;

public class Notice {
    private String summary;
    private String date;

    public Notice(String summary, String date) {
        this.summary = summary;
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
