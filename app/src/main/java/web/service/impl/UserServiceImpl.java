package web.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import web.entity.User;
import web.exception.InvalidDataException;
import web.exception.NoDataFoundException;
import web.repository.UserRepository;
import web.service.PhotoService;
import web.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PhotoService photoService;

    @Override
    public void addUser(final String id) {
        if (userRepository.existsByUserId(id)) {
            throw new InvalidDataException("Пользователь уже зарегистрирован.");
        }

        val user = User.builder()
                .userId(id)
                .build();

        userRepository.save(user);
    }

    @Override
    public void deleteUser(final String id) {
        User user = userRepository.findByUserId(id)
                .orElseThrow(() -> new NoDataFoundException("Пользователь не найден."));

        photoService.deleteAllUserPhotos(user);

        userRepository.delete(user);
    }
}
