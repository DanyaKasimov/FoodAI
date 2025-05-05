package web;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;
import web.config.BotConfig;

@Getter
@Service
public class BotCore {

    private final TelegramBot bot;

    public BotCore(BotConfig config) {
        this.bot = new TelegramBot(config.telegramToken());
    }

    @PostConstruct
    public void registerCommands() {
        BotCommand[] commands = {
            new BotCommand("/start", "Запустить бота"),
            new BotCommand("/stop", "Завершить работу бота"),
            new BotCommand("/list", "Получить список блюд"),
        };
        bot.execute(new SetMyCommands(commands));
    }
}
