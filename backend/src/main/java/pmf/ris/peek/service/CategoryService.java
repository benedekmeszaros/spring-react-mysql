package pmf.ris.peek.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pmf.ris.peek.exceptions.NoSuchElementException;
import pmf.ris.peek.model.Category;
import pmf.ris.peek.repository.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	
	public int saveCategory(Category category) {
		if(!categoryRepository.existsByName(category.getName()))
			return categoryRepository.save(category).getId();
		else
			return -1;
	}
	
	public boolean existsByName(String name) {
		return categoryRepository.existsByName(name);
	}
	
	public Category findCategory(String name) throws NoSuchElementException{
		return categoryRepository.findByName(name)
				.orElseThrow(() -> new NoSuchElementException("No user category found with name "+ name));
	}
	
	public List<Category> findAllCategory() {
		return categoryRepository.findAll();
	}
}
