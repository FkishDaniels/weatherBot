package bot.models;

import bot.interfaces.Weather;
import bot.json.JsonParser;

public class Wind implements Weather {
    private JsonParser jsonParser = new JsonParser();

    @Override
    public String check(String city) {
        jsonParser.init(city);
        return jsonParser.getWind();
    }

    @Override
    public String result(String city) {
        return check(city);
    }
}
