package ru.pxlhack.genrenatorTelegramBot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pxlhack.genrenatorTelegramBot.config.BotConfig;


@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot{
    private final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }


    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getChat().getFirstName();
            botAnswerUtils(messageText, chatId, userName);
        }
    }

    private void botAnswerUtils(String receivedMessageText, long chatId, String userName) {
        switch (receivedMessageText) {
            case "/start": {
                startCommandReceived(chatId, userName);
                break;
            }
        }
    }



    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hello, " + name + "!";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
