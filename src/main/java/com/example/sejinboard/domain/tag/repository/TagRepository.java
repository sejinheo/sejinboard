package com.example.sejinboard.domain.tag.repository;

import com.example.sejinboard.domain.tag.domain.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    List<Tag> findByNameIn(Collection<String> names);
    List<Tag> findByNameContainingIgnoreCaseOrderByNameAsc(String name, Pageable pageable);
}
