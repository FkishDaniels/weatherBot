package bot.service;

import bot.config.BotConfig;
import bot.databases.PersonDAO;
import bot.json.JsonParser;
import bot.models.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@Controller
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig botConfig;
    private Temperature temperature = new Temperature();
    private Wind wind = new Wind();
    private TempFeelsLike tempFeelsLike = new TempFeelsLike();
    private TextFeels textFeels = new TextFeels();

    private long chatId;
    private int state = 0;

    private PersonDAO personDAO;
    @Autowired
    public TelegramBot(BotConfig botConfig, PersonDAO personDAO) {
        this.botConfig = botConfig;
        this.personDAO = personDAO;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        chatId = getChatId(update);
        sendMessage.setChatId(chatId);
        if(!checkRegistered(chatId)) {
            signUp(update,chatId);
        }else{
            if(update.hasCallbackQuery()) {
                String command = update.getCallbackQuery().getData();
                switch (command) {
                    case "/menu" -> createMenu(update, chatId, personDAO.show(chatId));
                    case "/checkWeather" -> checkWeather(update, chatId, personDAO.show(chatId));
                    case "/changeCity" -> changeCity(update, chatId, personDAO.show(chatId));
                }
            }else{
                changeCity(update,chatId,personDAO.show(chatId));
            }
        }

    }


    private long getChatId(Update update){
        long id;
        if(update.hasCallbackQuery()){
            id = update.getCallbackQuery().getMessage().getChatId();
        }else{
            id = update.getMessage().getChatId();
        }
        return id;
    }


    private boolean checkRegistered(long chatId){
        boolean flag = true;
        if(personDAO.show(chatId) == null) flag = false;
        return flag;
    }

    private void signUp(Update update,long chatId) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if(state == 0){
            sendMessage.setText("Здравствуйте, "+update.getMessage().getFrom().getUserName()+"" +
                    "\nВы не авторизованы! Для пользования ботом введите ваш город проживания :)" +
                    "\nВ дальнейшем вы сможете изменить его!");
            state =1;
            execute(sendMessage);
        }else if(state ==1){
            Person person = new Person();
            person.setChatId(chatId);
            person.setCity(update.getMessage().getText());

            personDAO.save(person);
            sendMessage.setText("Вы установили город : "+person.getCity()+"\nВы были напрвлены в главное меню!");
            state = 0;
            execute(sendMessage);
            createMenu(update,chatId,person);
        }

    }

    private void createMenu(Update update, long chatId,Person person) throws TelegramApiException {
        if(update.hasCallbackQuery()) {
            EditMessageText messageText = new EditMessageText();
            messageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            Button button = new Button();
            messageText.setReplyMarkup(button.getMarkup("/menu"));
            messageText.setText("Привет, " + update.getCallbackQuery().getFrom().getUserName() + "" +
                    "\nТы находишься в главном меню. Выбери дальнейшее действие!" +
                    "\nУстановлен следующий город : " + person.getCity());
            messageText.setChatId(chatId);
            execute(messageText);

        }else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            Button button = new Button();
            sendMessage.setText("Привет, " + update.getMessage().getFrom().getUserName() + "" +
                    "\nТы находишься в главном меню. Выбери дальнейшее действие!" +
                    "\nУстановлен следующий город : " + person.getCity());
            sendMessage.setReplyMarkup(button.getMarkup("/menu"));
            execute(sendMessage);
        }

    }

    private void checkWeather(Update update,long chatId,Person person)throws TelegramApiException{
        Button button =new Button();
        EditMessageText messageText = new EditMessageText();
        messageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        messageText.setChatId(chatId);
        String city = person.getCity();
        String text = "В городе "+city+" следующие значения температуры:" +
                "\nТемпература : "+temperature.check(city)+
                "\nОщущается как : "+tempFeelsLike.check(city)+
                "\nСкорость ветра : "+wind.check(city)+"(км/ч)"+
                "\nКороткое описание : "+textFeels.check(city);

        messageText.setText(text);
        messageText.setReplyMarkup(button.getMarkup("/checkWeather"));
        execute(messageText);

    }
    private void changeCity(Update update,long chatId,Person person)throws TelegramApiException{

        if(state == 0){
            try {


                EditMessageText messageText = new EditMessageText();
                messageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                messageText.setChatId(chatId);
                messageText.setText("Введите название города, для которого хотите узнать погоду");
                state = 1;
                execute(messageText);
            }
            catch (NullPointerException e ){
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Вы двигаетесь на по сценарию :) Перенаправил вас в главное меню");
                execute(sendMessage);
                state = 0;
                createMenu(update,chatId,person);
            }
        } else if (state ==1) {
            try {
                String city = update.getMessage().getText();
                person.setCity(city);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                temperature.check(city);
                personDAO.update(person, chatId);
                sendMessage.setText("Вы поменяли свой город на " + city);
                state = 0;
                execute(sendMessage);
                createMenu(update, chatId, person);
            }
            catch (JSONException e){
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Вы неправильно ввели название города\n" +
                        "Напоминаем, что воизбежание проблем вводите город на английском языке!");
                execute(sendMessage);
                state = 1;
            }
        }

    }
}
