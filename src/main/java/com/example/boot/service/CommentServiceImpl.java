package com.example.boot.service;

import com.example.boot.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
}