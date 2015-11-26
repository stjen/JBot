package util.Log;

public class LogNode {
    private long time;
    private String item;

    public LogNode(String item) {
        this.time = System.currentTimeMillis();
        this.item = item;
    }

    public long getTime() {
        return time;
    }

    public String getItem() {
        return item;
    }

    public String toString() {
        return time + ": " + item;
    }
}
