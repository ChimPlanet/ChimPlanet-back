package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.TagObj;
import com.wak.chimplanet.repository.TagObjRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagObjService {
    @Autowired
    TagObjRepository tagObjRepository;
    public List<TagObj> getTagList(TagObj tagObj) {

        return tagObjRepository.findTagList(tagObj);
    }
}
