package ru.pxlhack.genrenatorTelegramBot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pxlhack.genrenatorTelegramBot.config.BotConfig;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
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

        if (update.hasInlineQuery()) {
            InlineQuery inlineQuery = update.getInlineQuery();

            String genre = GenrenatorService.getGenre();

            InlineQueryResultArticle result = new InlineQueryResultArticle();
            result.setId("1");
            result.setTitle("Genrenator");
            result.setDescription("Here is your genre:");
            result.setInputMessageContent(new InputTextMessageContent(genre));

            List<InlineQueryResult> resultList = new ArrayList<>();
            resultList.add(result);

            AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
            answerInlineQuery.setInlineQueryId(inlineQuery.getId());
            answerInlineQuery.setResults(resultList);
            answerInlineQuery.setCacheTime(0);

            try {
                execute(answerInlineQuery);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    private void botAnswerUtils(String receivedMessageText, long chatId, String userName) {
        switch (receivedMessageText) {
            case "/genre": {
                genreCommandReceived(chatId);
                break;
            }
        }
    }

    private void genreCommandReceived(long chatId) {
        String genre = GenrenatorService.getGenre();
        sendMessage(chatId, genre);
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
