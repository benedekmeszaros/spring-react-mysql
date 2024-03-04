package pmf.ris.peek.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
	
	@Value("${persistence.file-path}")
	private String imagePath;
	
	public String saveToFileSystem(MultipartFile file, String folder, String fileName) {
		if (null != file) {
			try {
				File directory = new File(imagePath + "/images/" + folder);
			    if (! directory.exists()){
			        directory.mkdirs();
			    }
			    	    
			    String originalFilename = file.getOriginalFilename();
		        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
		        String relativePath = "/images/" + folder + "/" + fileName + extension;
			    
				File imageFile = new File(imagePath, relativePath);      
				file.transferTo(imageFile);
			
			    return relativePath;
			} catch (Exception e) {
				return null;
			}  
		}else {
			return null;
		}
    }
	
	public boolean deleteFromFileSystem(String filePath) {
		try {	
			if(filePath.contains("default"))
				return false;
			Files.deleteIfExists(Paths.get(imagePath,filePath));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
