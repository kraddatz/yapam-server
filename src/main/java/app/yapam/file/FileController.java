package app.yapam.file;

import app.yapam.file.model.response.FileResponse;
import app.yapam.file.model.response.SimpleFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    @Autowired private FileService fileService;

    @GetMapping(value = "api/files/{fileId}")
    public FileResponse getFileForId(@PathVariable(value = "fileId") String fileId) {
        return fileService.getFileForId(fileId);
    }

    @PostMapping(value = "api/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SimpleFileResponse postFile(@RequestParam("file") MultipartFile files) {
        return fileService.saveFile(files);
    }
}
