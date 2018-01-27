package gai.mooc.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by ga on 2018/1/27.
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
