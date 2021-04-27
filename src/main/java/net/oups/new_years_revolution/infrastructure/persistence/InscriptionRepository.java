package net.oups.new_years_revolution.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    Optional<Inscription> findById(Long id);

    List<Inscription> findAll();

    List<Inscription> findByInscritId(Long userId);

    List<Inscription> findByResolutionId(Long resolutionId);

    Optional<Inscription> findByInscritIdAndResolutionId(Long inscritId, Long resolutionId);
}
