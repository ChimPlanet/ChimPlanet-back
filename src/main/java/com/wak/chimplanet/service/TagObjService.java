package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.TagObj;
import com.wak.chimplanet.repository.TagObjRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TagObjService {
    private final TagObjRepository tagObjRepository;

    public TagObjService(TagObjRepository tagObjRepository) {
        this.tagObjRepository = tagObjRepository;
    }

    public List<TagObj> getTagList(TagObj tagObj) {

        return tagObjRepository.findAll();
    }

    @Transactional
    public List<TagObj> saveTag(TagObj tagObj) {
        tagObjRepository.save(tagObj);
        return tagObjRepository.findAll();

    }
}