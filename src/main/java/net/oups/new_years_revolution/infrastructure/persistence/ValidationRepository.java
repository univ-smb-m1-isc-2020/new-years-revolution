package net.oups.new_years_revolution.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ValidationRepository extends JpaRepository<Validation, Long> {

    List<Validation> findAll();

    Optional<Validation> findById(Long id);

    List<Validation> findByResolutionId(Long resolutionId);

    List<Validation> findByCreatorId(Long creatorId);

    List<Validation> findByDate(Date date);

    Optional<Validation> findByCreatorIdAndResolutionIdAndDate(Long creatorId, Long resolutionId, Date date);

    List<Validation> findByResolutionIdAndDateBetween(Long resolutionId, Date now, Date sevenDaysBefore);
}
