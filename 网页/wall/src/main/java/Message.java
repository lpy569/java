public class Message {
    public String from;
    public String to;
    public String message;

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
