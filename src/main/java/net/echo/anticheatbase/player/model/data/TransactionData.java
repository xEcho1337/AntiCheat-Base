package net.echo.anticheatbase.player.model.data;

public class TransactionData {

    private final long timestamp;
    private final int id;

    public TransactionData(int id, long timestamp) {
        this.timestamp = timestamp;
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getId() {
        return id;
    }
}
