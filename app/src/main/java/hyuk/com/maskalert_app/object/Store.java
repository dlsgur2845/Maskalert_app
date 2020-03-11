package hyuk.com.maskalert_app.object;

public class Store {
    private String code;
    private String name;
    private String addr;
    private String type;
    private double lat;
    private double lon;
    private String stock_at;
    private String remain_stat;
    private String created_at;

    public Store(String code, String name, String addr, String type, double lat, double lon, String stock_at, String remain_stat, String created_at) {
        this.code = code;
        this.name = name;
        this.addr = addr;
        this.type = type;
        this.lat = lat;
        this.lon = lon;
        this.stock_at = stock_at;
        this.remain_stat = remain_stat;
        this.created_at = created_at;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getStock_at() {
        return stock_at;
    }

    public void setStock_at(String stock_at) {
        this.stock_at = stock_at;
    }

    public String getRemain_stat() {
        return remain_stat;
    }

    public void setRemain_stat(String remain_stat) {
        this.remain_stat = remain_stat;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
