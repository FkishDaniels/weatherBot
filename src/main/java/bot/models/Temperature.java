package bot.models;

import bot.interfaces.Weather;
import bot.json.JsonParser;

public class Temperature implements Weather {
    private JsonParser jsonParser = new JsonParser();
    @Override
    public String check(String city) {
        jsonParser.init(city);
        return jsonParser.getTemp();
    }

    @Override
    public String result(String city) {
        return check(city);
    }
}
