public class Message {
    public boolean IsError;
    private String text;

    public Message (boolean isError, String text){
        IsError = isError;
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
