package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.entity.Photo;
import web.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, UUID> {
    List<Photo> findAllByUser(User user);

    List<Photo> findAllByUserAndCreatedAtBetween(User user, LocalDateTime startOfDay, LocalDateTime endOfDay);
}