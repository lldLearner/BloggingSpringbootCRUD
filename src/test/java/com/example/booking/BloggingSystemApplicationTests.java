package com.example.booking;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import com.example.booking.domain.Blog;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BloggingSystemApplicationTests {

	@Autowired
	TestRestTemplate testRestTemplate;

	@Test
	void shouldCreateTheBlogSuccessfully() {
		Blog blog = new Blog("MyFirstBlog", "this is my test blog");
		ResponseEntity<Void> response = testRestTemplate.postForEntity("/blogging/create", blog, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		ResponseEntity<String> getResponse = testRestTemplate.getForEntity("/blogging/get/MyFirstBlog", String.class);
		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		String id = documentContext.read("$.id");
		assertThat(id).isEqualTo("MyFirstBlog");
		String content = documentContext.read("$.content");
		assertThat(content).isEqualTo("this is my test blog");
	}

	@Test
	void shouldReturnTheBlogSuccessfully() {
		ResponseEntity<String> response = testRestTemplate.getForEntity("/blogging/get/KartikTestBlog", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void shouldReturnBadRequestForCreatingBlogsMoreThanWordLimit() {
		Blog blog = new Blog("MyFirstInvalidBlog", "this is my test bloggggggggggggggggggggggggggggggggggggggg");
		ResponseEntity<Void> response = testRestTemplate.postForEntity("/blogging/create", blog, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	@DirtiesContext
	void shouldUpdateTheBlog() {
		Blog updatedBlog = new Blog(null, "this is my first test blog updated!");
		HttpEntity<Blog> request = new HttpEntity<Blog>(updatedBlog);
		ResponseEntity<Void> updateResponse = testRestTemplate.exchange("/blogging/put/KavailyaTestBlog",
				HttpMethod.PUT, request, Void.class);
		assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		ResponseEntity<String> getResponse = testRestTemplate.getForEntity("/blogging/get/KavailyaTestBlog",
				String.class);
		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		String id = documentContext.read("$.id");
		String content = documentContext.read("$.content");
		assertThat(id).isEqualTo("KavailyaTestBlog");
		assertThat(content).isEqualTo("this is my first test blog updated!");
	}
}
