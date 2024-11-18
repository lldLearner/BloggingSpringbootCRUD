package com.example.booking.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.booking.domain.Blog;
import com.example.booking.repository.BlogRepository;

@RestController
@RequestMapping("/blogging")
public class BloggingCrudController {

	@Autowired
	private BlogRepository blogRepository;

	@PostMapping("/create")
	public ResponseEntity<Void> createBlog(@RequestBody Blog blog, UriComponentsBuilder uriComponentsBuilder) {
		// TODO Auto-generated method stub
		int wordLimit = 20;
		if (blog.getContent().length() > wordLimit) {
			return ResponseEntity.badRequest().build();
		}
		Blog savedBlog = blogRepository.save(blog);
		URI uri = uriComponentsBuilder.path("/blogging/create/{id}").buildAndExpand(savedBlog.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Blog> getBlog(@PathVariable String id) {

		Optional<Blog> blog = blogRepository.findById(id);
		if (blog.isPresent()) {
			return ResponseEntity.ok(blog.get());
		}

		return ResponseEntity.notFound().build();
	}

	@PutMapping("/put/{id}")
	public ResponseEntity<Void> updateBlog(@PathVariable String id, @RequestBody Blog updatedBlog) {
		Optional<Blog> blog = blogRepository.findById(id);
		int wordLimit = 20;
		if (updatedBlog.getContent().length() > wordLimit) {
			return ResponseEntity.badRequest().build();
		}
		if (blog.isPresent()) {
			Blog savedBlog = blog.get();
			Blog updateBlog = new Blog(savedBlog.getId(), updatedBlog.getContent());
			blogRepository.save(updateBlog);
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteBlog(@PathVariable String id) {
		Optional<Blog> blog = blogRepository.findById(id);

		if(blog.isPresent()) {
			blogRepository.delete(blog.get());
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.notFound().build();
	}
}
