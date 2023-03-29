package com.wak.chimplanet.repository;

import com.wak.chimplanet.entity.TagObj;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagObjRepository {

    private final EntityManager em;

    public List<TagObj> findALl() {
        return em.createQuery("select t from TagObj t", TagObj.class)
                .getResultList();
    }

    public List<TagObj> findAllByName(List<String> tagNames) {
        return em.createQuery("SELECT t FROM TagObj t WHERE t.tagName IN :tagNames", TagObj.class)
                .setParameter("tagNames", tagNames)
                .getResultList();
    }


    public TagObj findById(int tagId) {
        return em.find(TagObj.class, tagId);
    }

    public void save(TagObj tagObj) {
        em.persist(tagObj);
    }

    public void deleteById (TagObj tagObj){
        //Delete Logic 확인
    }
}