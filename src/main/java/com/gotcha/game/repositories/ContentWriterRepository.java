package com.gotcha.game.repositories;

import com.gotcha.game.model.ContentWriter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentWriterRepository extends JpaRepository<ContentWriter, Long> {
}
