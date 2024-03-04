package pmf.ris.peek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pmf.ris.peek.dto.CategoryCreateDTO;
import pmf.ris.peek.exceptions.DuplicateEntryException;
import pmf.ris.peek.exceptions.UnsupportedOperationException;
import pmf.ris.peek.model.Category;
import pmf.ris.peek.service.CategoryService;

@RestController
@CrossOrigin(origins = "${cross.allowed-origin}", allowCredentials = "true")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("public/category/all")
	public ResponseEntity<?> getCategories() {
		return ResponseEntity.ok(categoryService.findAllCategory().stream().map(c -> c.getName()).toList());
	}

	@PostMapping("private/admin/put/category")
	public ResponseEntity<String> addCategory(@RequestBody CategoryCreateDTO create) throws UnsupportedOperationException, DuplicateEntryException {
		if(categoryService.existsByName(create.getCategory()))
			throw new DuplicateEntryException("Category already exists withthe given name.");
		Category category = Category.builder().name(create.getCategory()).build();
		if (categoryService.saveCategory(category) == -1)
			throw new UnsupportedOperationException(create.getCategory().toUpperCase() + " category already exists."); 
		else
			return ResponseEntity.ok("Category added.");
	}
}
