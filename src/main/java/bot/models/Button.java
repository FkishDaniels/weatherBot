package bot.models;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Button {
    public String command;

    public InlineKeyboardMarkup getMarkup(String command){
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        switch (command){
            case "/menu": {
                List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
                List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();

                InlineKeyboardButton changeCity = new InlineKeyboardButton();
                changeCity.setText("Изменить город");
                changeCity.setCallbackData("/changeCity");
                InlineKeyboardButton checkWeather = new InlineKeyboardButton();
                checkWeather.setText("Узнать погоду");
                checkWeather.setCallbackData("/checkWeather");

                rowInLine1.add(changeCity);
                rowInLine2.add(checkWeather);

                rowsInLine.add(rowInLine1);
                rowsInLine.add(rowInLine2);

                markupInLine.setKeyboard(rowsInLine);
            }
            break;
            case "/checkWeather":{
                List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
                InlineKeyboardButton menu = new InlineKeyboardButton();
                menu.setText("Вернуться назад");
                menu.setCallbackData("/menu");
                rowInLine1.add(menu);
                rowsInLine.add(rowInLine1);

                markupInLine.setKeyboard(rowsInLine);
            }
        }
        return markupInLine;
    }
}
