package web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import web.api.UserApi;
import web.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public void addUser(final String id) {
        log.info("Поступил запрос на добавление пользователя. ID: {}", id);

        userService.addUser(id);
    }

    @Override
    public void deleteUser(final String id) {
        log.info("Поступил запрос на удаление пользователя. ID: {}", id);

        userService.deleteUser(id);
    }
}
