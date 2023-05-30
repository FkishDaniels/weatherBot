package bot.models;

public class Person {
    private long chatId;
    private String city;

    public Person(long chatId, String city) {
        this.chatId = chatId;
        this.city = city;
    }
    public Person(){}

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
