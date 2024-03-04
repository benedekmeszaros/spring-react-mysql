package pmf.ris.peek.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pmf.ris.peek.exceptions.NoSuchElementException;
import pmf.ris.peek.model.Gallery;
import pmf.ris.peek.model.User;
import pmf.ris.peek.repository.GalleryRepository;

@Service
public class GalleryService {
	@Autowired
	private GalleryRepository galleryRepository;
	
	public List<Gallery> findAll(){
		return galleryRepository.findAll();
	}
	
	public List<Gallery> findAllSorted(){
		return galleryRepository.findAllSorted();
	}
	
	public List<Gallery> findAllByUser(User user){
		return galleryRepository.findByUser(user);
	}
	
	public List<Gallery> findAllByUserSorted(User user){
		return galleryRepository.findByUserSorted(user);
	}
	
	public List<Gallery> findAllGalleryCreatedAt(Instant date){
		return galleryRepository.findAllCreatedAt(date);
	}
	
	public boolean existsGalleryByTitle(String title) {
		return galleryRepository.existsByTitle(title);
	}
	
	public Gallery findGalleryById(Integer id) throws NoSuchElementException {
		return galleryRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("No gallery found with id: "+ id));
	}
	
	public int saveGallery(Gallery gallery) {
		try {		
			return galleryRepository.save(gallery).getId();
		} catch (Exception e) {
			return -1;
		}
	}
	
	public boolean deleteGallery(Gallery gallery) {
		try {
			galleryRepository.delete(gallery);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public long getScoreOfGallery(Gallery gallery) {
		Long value = galleryRepository.getScore(gallery.getId());
		if(value != null)
			return value;
		else
			return 0;
	}
	
	public long getNumberOfComments(Gallery gallery) {
		Long value = galleryRepository.getNumberOfComments(gallery.getId());
		if(value != null)
			return value;
		else
			return 0;
	}
}
