package org.zerock.b01.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.upload.UploadFileDTO;
import org.zerock.b01.dto.upload.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2


public class UpDownController {

    @Value("${org.zerock.upload.path}")
    private String uploadPath; //파일 등록

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(
            @Parameter(description = "File to be uploaded",
                       content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            UploadFileDTO uploadFileDTO){

        log.info(uploadFileDTO);

        if(uploadFileDTO.getFiles() != null){ //업로드 된 파일이 null이 아닐때
            final List<UploadResultDTO> list = new ArrayList<>(); //UploadResultDTO를 객체를 만들고

            uploadFileDTO.getFiles().forEach(multipartFile -> {
                String orgName = multipartFile.getOriginalFilename(); //원본 파일의 이름을 얻어올 수 있음
                log.info(orgName);
                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid+"_"+orgName); //uploadPath는 업로드 경로
                //원본 파일 이름을 얻고, uuid를 생성한 후 uuid와 원본파일명을 합쳐서 업로드 경로에 넣는 savePath를 생성해준다.

                boolean image = false;

                try {
                    multipartFile.transferTo(savePath); //savePath로 이름 변경한 값을 서버에 전송해준다.
                    if(Files.probeContentType(savePath).startsWith("image")){ //savePath로 저장된 파일의 유형이 image로 시작되는 것인지 확인해준다.
                        image = true;
                        File thumFile = new File(uploadPath, "s_"+uuid+"_"+orgName); //image로 시작될 때 image의 값을 true로 바꾸고
                        //썸네일 s+ uuid + _ + 원본명으로 썸네일의 값을 지정한다.
                        Thumbnailator.createThumbnail(savePath.toFile(), thumFile, 200, 200);
                        log.info("썸네일 생성!!");

                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

                //list에는 savePath로 경로를 지정해주고 썸네일을 생성한 값이 담겨있음.

                list.add(UploadResultDTO.builder()
                              .uuid(uuid).fileName(orgName).img(image).build());
                //UploadResultDTO의 객체 생성 후 list에 넣어줌

            });

            log.info("파일 업로드 성공");
            return list;
        }
        log.info("파일 업로드 실패");
        return null;


    }


    @GetMapping("/view/{fileName}") //첨부파일 조회
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();


        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));

        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);

    }

    @DeleteMapping("/remove/{fileName}") //첨부파일 삭제
    public Map<String, Boolean> removeFile(@PathVariable String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try{
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();
            if(contentType.startsWith("image")){
                File thumbnailFile = new File(uploadPath+File.separator+"s_"+fileName);
                thumbnailFile.delete();
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);
        return resultMap;
    }








}
