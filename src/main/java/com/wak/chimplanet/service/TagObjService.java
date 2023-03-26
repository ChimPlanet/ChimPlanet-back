package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.TagObj;
import com.wak.chimplanet.repository.TagObjRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TagObjService {
    @Autowired
    TagObjRepository tagObjRepository;
    public List<TagObj> getTagList(TagObj tagObj) {

        return tagObjRepository.findALl();
    }

    @Transactional
    public List<TagObj> saveTag(TagObj tagObj) {
        tagObjRepository.save(tagObj);
        return tagObjRepository.findALl();

    }
}