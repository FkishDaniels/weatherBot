package bot.json;

import bot.models.Person;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class JsonParser{
    private String result;
    private String temp;
    private String text;
    private String feelsLike;
    private String wind;



    public void init(String city){
        String output = getURLContent("https://api.weatherapi.com/v1/current.json?key=ecb4c36ec463451c9f4113146233105&q="+city+"&aqi=no");
        result = output;

        JSONObject jsonObject = new JSONObject(result);
        temp = String.valueOf(jsonObject.getJSONObject("current").getDouble("temp_c"));
        text = jsonObject.getJSONObject("current").getJSONObject("condition").getString("text");
        feelsLike = String.valueOf(jsonObject.getJSONObject("current").getDouble("feelslike_c"));
        wind = String.valueOf(jsonObject.getJSONObject("current").getDouble("wind_kph"));
    }

    private String getURLContent(String urlAddress){
        StringBuffer content = new StringBuffer();

        try {
            URL url = new URL(urlAddress);
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;

            while((line = bufferedReader.readLine()) != null){
                content.append(line).append("\n");
            }
            bufferedReader.close();

        } catch (IOException e) {
            System.out.println("Такой город не был найден!");
        }
        return content.toString();
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

}
