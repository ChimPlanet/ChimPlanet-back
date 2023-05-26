package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.OfficialBoardTag;
import com.wak.chimplanet.entity.TagObj;
import com.wak.chimplanet.repository.TagObjRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
        if(tagObj.getTagId() == null){
            tagObjRepository.save(tagObj);
        }else{
            //영속성 되는지 확인필요
            TagObj newTagObj = tagObjRepository.findById(tagObj.getTagId());
            newTagObj.setTagName(tagObj.getTagName());
        }

        return tagObjRepository.findAll();
    }

    public List<TagObj> deleteTag(TagObj tagObj) {

        List<OfficialBoardTag> officialBoardTag = tagObjRepository.findOffcialBoard(tagObj);



        if(officialBoardTag.size() > 0){
            tagObjRepository.delete(tagObj);
        }else{

        }

        return tagObjRepository.findAll();
    }
}