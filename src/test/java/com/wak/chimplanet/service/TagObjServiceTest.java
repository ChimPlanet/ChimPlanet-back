package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.TagObj;
import com.wak.chimplanet.repository.TagObjRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class TagObjServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    TagObjRepository tagRepository;


    @Test
    @Rollback(value = false)
    void saveTag() {

        int prevQty = tagRepository.findAll().size();

        TagObj tagObj = TagObj.builder()
                .tagName("testTag2")
                .parentTagId("100")
                .build();

        tagRepository.save(tagObj);

        int resultQty = tagRepository.findAll().size();

        prevQty = prevQty + 1;
        assertEquals(resultQty, prevQty);
    }
}