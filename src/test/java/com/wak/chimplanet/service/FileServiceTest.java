package com.wak.chimplanet.service;

import com.wak.chimplanet.dto.requestDto.FileSequenceRequestDto;
import com.wak.chimplanet.entity.DeviceType;
import com.wak.chimplanet.entity.FileEntity;
import com.wak.chimplanet.entity.ImageType;
import com.wak.chimplanet.repository.FileRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FileServiceTest {

    @Autowired FileService fileService;
    @Value("${path.upload-images}")
    private String filePath;
    @Autowired
    private FileRepository fileRepository;


    @Test
    public void deleteFile_success() throws IOException {
        // Given
        long fileId = 1L;

        // When
        fileService.deleteImage(fileId);

        // Then
        assertEquals(false, Files.exists(Path.of(filePath)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteFile_fileNotExist() throws IOException {
        // Given
        long fileId = 2L;

        // When
        fileService.deleteImage(fileId);
    }

    @Test
    public void 시퀀스_변경_성공() {
        // given
        FileEntity file1 = FileEntity.createFileEntity("file1", "Y", "url1", "uri1", ImageType.MAIN, DeviceType.PC, "N", 3);
        FileEntity file2 = FileEntity.createFileEntity("file2", "Y", "url2", "uri2", ImageType.MAIN, DeviceType.PC, "N", 4);
        FileEntity file3 = FileEntity.createFileEntity("file3", "Y", "url3", "uri3", ImageType.MAIN, DeviceType.PC, "N", 5);

        fileRepository.saveAll(Arrays.asList(file1, file2, file3));

        List<FileSequenceRequestDto> sequenceList = Arrays.asList(
                new FileSequenceRequestDto(file1.getFileId(), 5),
                new FileSequenceRequestDto(file2.getFileId(), 6),
                new FileSequenceRequestDto(file3.getFileId(), 7)
        );

        // when
        fileService.updateSequence(sequenceList);

        // then
        List<FileEntity> fileList = fileRepository.findAll(Sort.by(Sort.Direction.ASC, "sequence"));
        assertThat(fileList.get(1).getFileName()).isEqualTo("file2");
        assertThat(fileList.get(1).getSequence()).isEqualTo(6);
        assertThat(fileList.get(2).getFileName()).isEqualTo("file3");
        assertThat(fileList.get(2).getSequence()).isEqualTo(7);
        assertThat(fileList.get(0).getFileName()).isEqualTo("file1");
        assertThat(fileList.get(0).getSequence()).isEqualTo(5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateSequence_fail_fileNotFound() {
        // given
        List<FileSequenceRequestDto> sequenceList = Arrays.asList(
                new FileSequenceRequestDto(100L, 3),
                new FileSequenceRequestDto(200L, 1),
                new FileSequenceRequestDto(300L, 2)
        );

        // when
        fileService.updateSequence(sequenceList);

        // then
        // 예외 발생
    }
}