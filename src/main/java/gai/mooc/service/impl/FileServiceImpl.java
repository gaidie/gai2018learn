package gai.mooc.service.impl;

import gai.mooc.service.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by ga on 2018/1/27.
 */
@Service
public class FileServiceImpl implements IFileService {

    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 文件上传
     * @param file
     * @param path
     * @return
     */
    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String saveFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        File filePath = new File(path);
        if (!filePath.exists()){
            filePath.mkdirs();
            filePath.setWritable(true);
            //可以编辑
        }
        File targetFile = new File(saveFileName);
        try {
            file.transferTo(targetFile);
            //todo 上传FTP

            //删除文件
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("文件上传过程中发生错误{}", e);
        }
        return targetFile.getName();
    }

    public static void main(String[] args) {
        String fileName = "aaaaaaaa.jpg";
        System.out.println(fileName.substring(fileName.lastIndexOf(".")+1));
    }
}
