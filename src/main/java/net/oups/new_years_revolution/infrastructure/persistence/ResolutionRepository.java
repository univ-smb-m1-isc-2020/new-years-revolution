package net.oups.new_years_revolution.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResolutionRepository extends JpaRepository<Resolution, Long> {

    Optional<Resolution> findById(Long id);

    List<Resolution> findAll();

    List<Resolution> findByCreatorId(Long creatorId);

    List<Resolution> findByCreatorLogin(String accountCreatorLogin);
}
