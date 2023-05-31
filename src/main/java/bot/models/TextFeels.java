package bot.models;

import bot.interfaces.Weather;
import bot.json.JsonParser;

public class TextFeels implements Weather {
    JsonParser jsonParser = new JsonParser();
    @Override
    public String check(String city) {
        jsonParser.init(city);
        return jsonParser.getText();
    }

    @Override
    public String result(String city) {
        return check(city);
    }
}
