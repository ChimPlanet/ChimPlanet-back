package com.wak.chimplanet.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wak.chimplanet.entity.FileEntity;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class FileControllerTest {

    @Autowired private WebApplicationContext webApplicationContext;

    @Test
    @DisplayName("파일 업로드 테스트")
    void whenFileUploaded_thenVerifyStatus() throws Exception {

        byte[] content = Files.readAllBytes(Paths.get("src/main/resources/static/images/test.png"));

        MockMultipartFile file = new MockMultipartFile(
            "file", "test.png", MediaType.IMAGE_PNG_VALUE, content
        );

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/api/file/image").file(file))
            .andExpect(status().isOk());
    }

    /*@Test
    public void 파일이미지_변경_테스트() throws Exception {
        // given
        FileEntity fileEntity = FileEntity.builder()
            .fileName("test.png")
            .imageUri("")
            .build();
        fileEntity.setFileName("test.jpg");
        fileEntity.setFilePath("/home/user/");
        fileEntity.setFileSize(1000L);
        fileEntity.setFileType("jpg");
        fileRepository.save(fileEntity);

        FileUploadRequestDto dto = new FileUploadRequestDto();
        dto.setFileId(fileEntity.getId());
        dto.setFileName("new_test.jpg");

        // when
        mockMvc.perform(put("/updateImage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
        // then
        FileEntity updatedEntity = fileRepository.findById(fileEntity.getId()).orElse(null);
        assertNotNull(updatedEntity);
        assertEquals(dto.getFileName(), updatedEntity.getFileName());
    }*/
}