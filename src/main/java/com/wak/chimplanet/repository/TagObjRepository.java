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

    public List<TagObj> findTagList(TagObj tagObj){
        String findQuery = "select t from TagObj as t";

        return em.createQuery(findQuery, TagObj.class)
                .getResultList();
    }

}
