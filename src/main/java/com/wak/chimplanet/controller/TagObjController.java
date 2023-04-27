package com.wak.chimplanet.controller;

import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.TagObj;
import com.wak.chimplanet.service.BoardService;
import com.wak.chimplanet.service.TagObjService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.HTML;
import java.util.List;

@RestController
@RequestMapping("/api/tag")
public class TagObjController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TagObjService tagObjService;

    @GetMapping("/tagList")
    public ResponseEntity<List<TagObj>> getTagList(TagObj tagObj) {
        return ResponseEntity.ok().body(tagObjService.getTagList(tagObj));
    }

    @PostMapping("/")
    public ResponseEntity<List<TagObj>> saveTag(TagObj tagObj){
        return ResponseEntity.ok().body(tagObjService.saveTag(tagObj));
    }
}