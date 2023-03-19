package com.wak.chimplanet.repository;

import com.wak.chimplanet.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagRepository {

    private final EntityManager em;

    public List<Tag> findALl() {
        return em.createQuery("select t from Tag t", Tag.class)
                .getResultList();
    }

    public List<Tag> findAllByName(List<String> tagNames) {
        return em.createQuery("SELECT t FROM Tag t WHERE t.tagName IN :tagNames", Tag.class)
                .setParameter("tagNames", tagNames)
                .getResultList();
    }
}
