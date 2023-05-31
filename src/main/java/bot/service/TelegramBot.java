package bot.service;

import bot.config.BotConfig;
import bot.databases.PersonDAO;
import bot.models.Wind;
import bot.models.Temperature;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@Controller
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig botConfig;
    private Temperature temperature = new Temperature();
    private Wind wind = new Wind();
    private long chatId;

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

    private void signUp(Update update,long chatId){

    }
}
