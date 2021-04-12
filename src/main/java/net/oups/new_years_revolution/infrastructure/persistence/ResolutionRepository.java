package net.oups.new_years_revolution.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResolutionRepository extends JpaRepository<Resolution, Long> {

    Resolution findById(String id);

    List<Resolution> findAll();

    List<Resolution> findByCreatorId(Long creatorId);

    List<Resolution> findByCreatorLogin(String accountCreatorLogin);
}
