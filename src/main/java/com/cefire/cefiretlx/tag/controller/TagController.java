package com.cefire.cefiretlx.tag.controller;

import com.cefire.cefiretlx.tag.dto.TagDetailResponseDto;
import com.cefire.cefiretlx.tag.dto.TagRequestDto;
import com.cefire.cefiretlx.tag.dto.TagResponseDto;
import com.cefire.cefiretlx.tag.service.ITagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

  private final ITagService tagService;

  @GetMapping
  ResponseEntity<List<TagResponseDto>> getAllTags() {
    return ResponseEntity.ok(tagService.findAll());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  ResponseEntity<TagDetailResponseDto> findTagById(@PathVariable Long id) {
    return ResponseEntity.ok(tagService.findById(id));
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  ResponseEntity<TagResponseDto> saveTag(@Valid @RequestBody TagRequestDto tagRequestDto) {
    return new ResponseEntity<>(tagService.save(tagRequestDto), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  ResponseEntity<TagResponseDto> updateTag(@PathVariable Long id, @Valid @RequestBody TagRequestDto tagRequestDto) {
    return ResponseEntity.ok(tagService.update(id, tagRequestDto));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  ResponseEntity<Void> deleteTag(@PathVariable Long id) {
    tagService.deleteTag(id);
    return ResponseEntity.noContent().build();
  }

}
