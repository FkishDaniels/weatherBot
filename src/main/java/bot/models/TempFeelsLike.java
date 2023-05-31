package bot.models;

import bot.interfaces.Weather;
import bot.json.JsonParser;

public class TempFeelsLike implements Weather {
    private JsonParser jsonParser = new JsonParser();
    @Override
    public String check(String city) {
        jsonParser.init(city);
        return jsonParser.getFeelsLike();
    }

    @Override
    public String result(String city) {
        return check(city);
    }
}
