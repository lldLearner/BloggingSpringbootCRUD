package com.example.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booking.domain.Blog;

public interface BlogRepository extends JpaRepository<Blog, String> {

}
