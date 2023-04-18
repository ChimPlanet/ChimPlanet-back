package com.wak.chimplanet.repository;

import com.wak.chimplanet.entity.TagObj;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TagRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    private TagObjRepository tagRepository;


    @Test
    public void findAll() {
        List<TagObj> tagRepositoryList = tagRepository.findAll();
        assertEquals(tagRepositoryList.size(), 8);
    }

    @Test
    public void findAllByName() {
    }
}