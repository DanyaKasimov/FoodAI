package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.entity.Description;
import web.entity.Photo;

import java.util.UUID;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, UUID> {

    Description findByPhoto(Photo photo);
}
