package web;

import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MessageSender {

    private final BotCore bot;

    private final Map<String, List<Integer>> lastPhotoMessages = new ConcurrentHashMap<>();


    public SendResponse send(String chatId, String message) {
        try {
            return bot.getBot().execute(new SendMessage(chatId, message));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteMessage(String chatId, Integer response) {
        try {
            bot.getBot().execute(new DeleteMessage(chatId, response));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public SendResponse sendPhoto(String chatId, File file, String caption) {
        try {
            SendPhoto request = new SendPhoto(chatId, file).caption(caption);
            return bot.getBot().execute(request);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка отправки фото: " + e.getMessage());
        }
    }

    public void rememberLastMessageId(String chatId, int messageId) {
        lastPhotoMessages.compute(chatId, (key, messages) -> {
            if (messages == null) {
                messages = new ArrayList<>();
            }
            if (messages.size() >= 2) {
                messages.remove(0);
            }
            messages.add(messageId);
            return messages;
        });
    }

    public void deleteLastMessage(String chatId) {
        List<Integer> messageIds = lastPhotoMessages.getOrDefault(chatId, Collections.emptyList());
        for (Integer id : messageIds) {
            deleteMessage(chatId, id);
        }
        lastPhotoMessages.remove(chatId);
    }

    private final Map<String, List<Integer>> commandMessageIds = new ConcurrentHashMap<>();

    public void rememberCommandMessageId(String chatId, int messageId) {
        commandMessageIds.compute(chatId, (key, messages) -> {
            if (messages == null) {
                messages = new ArrayList<>();
            }
            messages.add(messageId);
            return messages;
        });
    }

    public void deleteCommandMessages(String chatId) {
        List<Integer> messages = commandMessageIds.remove(chatId);
        if (messages != null) {
            for (Integer id : messages) {
                deleteMessage(chatId, id);
            }
        }
    }
}
