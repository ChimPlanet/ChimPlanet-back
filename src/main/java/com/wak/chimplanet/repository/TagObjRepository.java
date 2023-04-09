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

    public List<TagObj> findAll() {
        return em.createQuery("select t from TagObj t", TagObj.class)
                .getResultList();
    }

    public List<TagObj> findAllByName(List<String> tagNames) {
        return em.createQuery("SELECT t FROM TagObj t WHERE t.tagName IN :tagNames", TagObj.class)
                .setParameter("tagNames", tagNames)
                .getResultList();
    }

    public List<TagObj> findAllByChildId(List<String> childIds) {
        return em.createQuery("SELECT t FROM TagObj t WHERE t.tagName IN :childIds", TagObj.class)
                .setParameter("childIds", childIds)
                .getResultList();
    }

    public void save(TagObj tagObj) {
        if(tagObj.getTagId() == null){
            em.persist(tagObj);
        }else{
            em.merge((tagObj));
        }
    }

    public void deleteById (TagObj tagObj){
        //Delete Logic 확인
    }
}