package com.wak.chimplanet.repository;

import com.wak.chimplanet.entity.Tag;
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
    private TagRepository tagRepository;


    @Test
    public void findALl() {
        List<Tag> tagRepositoryList = tagRepository.findALl();
        assertEquals(tagRepositoryList.size(), 8);
    }

    @Test
    public void findAllByName() {
    }
}