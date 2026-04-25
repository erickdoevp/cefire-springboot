package com.cefire.cefiretlx.blog.service;

import com.cefire.cefiretlx.blog.domain.Blog;
import com.cefire.cefiretlx.blog.domain.BlogStatus;
import com.cefire.cefiretlx.blog.dto.BlogCreateRequestDto;
import com.cefire.cefiretlx.blog.dto.BlogDetailResponseDto;
import com.cefire.cefiretlx.blog.dto.BlogSummaryResponseDto;
import com.cefire.cefiretlx.blog.dto.BlogUpdateRequestDto;
import com.cefire.cefiretlx.blog.mapper.BlogMapper;
import com.cefire.cefiretlx.blog.repository.BlogRepository;
import com.cefire.cefiretlx.blog.specification.BlogSpecification;
import com.cefire.cefiretlx.category.domain.Category;
import com.cefire.cefiretlx.category.service.ICategoryService;
import com.cefire.cefiretlx.shared.exception.ResourceNotFoundException;
import com.cefire.cefiretlx.tag.domain.Tag;
import com.cefire.cefiretlx.tag.service.ITagService;
import com.cefire.cefiretlx.user.domain.User;
import com.cefire.cefiretlx.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements IBlogService {

  private final BlogRepository blogRepository;
  private final BlogMapper blogMapper;
  private final ICategoryService categoryService;
  private final ITagService tagService;
  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<BlogSummaryResponseDto> findAllPaginated(
      String title, BlogStatus status, Long categoryId,
      LocalDateTime updatedFrom, LocalDateTime updatedTo, Pageable pageable) {

    Specification<Blog> spec = BlogSpecification.withFilters(title, status, categoryId, updatedFrom, updatedTo);
    return blogRepository.findAll(spec, pageable).map(blogMapper::toSummaryResponseDto);
  }

  @Override
  @Transactional
  public BlogDetailResponseDto create(BlogCreateRequestDto dto) {
    if (blogRepository.existsBySlug(dto.getSlug())) {
      throw new IllegalArgumentException("Ya existe un blog con el slug: " + dto.getSlug());
    }

    Category category = categoryService.findEntityById(dto.getCategoryId());

    User author = userRepository.findById(dto.getAuthorId())
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id: " + dto.getAuthorId()));

    Blog blog = blogMapper.toEntity(dto);
    blog.setCategory(category);
    blog.setAuthor(author);

    if (dto.getTags() != null && !dto.getTags().isEmpty()) {
      List<Tag> tags = dto.getTags().stream().map(tagService::findOrCreateByName).toList();
      blog.setTags(tags);
    }

    return blogMapper.toDetailResponseDto(blogRepository.save(blog));
  }

  @Override
  @Transactional(readOnly = true)
  public BlogDetailResponseDto findById(Long id) {
    return blogMapper.toDetailResponseDto(findEntityById(id));
  }

  @Override
  @Transactional(readOnly = true)
  public BlogDetailResponseDto findBySlug(String slug) {
    Blog blog = blogRepository.findBySlug(slug)
        .orElseThrow(() -> new ResourceNotFoundException("Blog no encontrado con el slug: " + slug));
    return blogMapper.toDetailResponseDto(blog);
  }

  @Override
  @Transactional(readOnly = true)
  public Blog findEntityById(Long id) {
    return blogRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Blog no encontrado con el id: " + id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<BlogDetailResponseDto> findAll() {
    return blogRepository.findAll()
        .stream()
        .map(blogMapper::toDetailResponseDto)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<BlogDetailResponseDto> findByStatus(BlogStatus status) {
    return blogRepository.findByStatus(status)
        .stream()
        .map(blogMapper::toDetailResponseDto)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<BlogDetailResponseDto> findByCategoryId(Long categoryId) {
    return blogRepository.findByCategoryId(categoryId)
        .stream()
        .map(blogMapper::toDetailResponseDto)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<BlogDetailResponseDto> findByAuthorId(UUID authorId) {
    return blogRepository.findByAuthorId(authorId)
        .stream()
        .map(blogMapper::toDetailResponseDto)
        .toList();
  }

  @Override
  @Transactional
  public BlogDetailResponseDto update(Long id, BlogUpdateRequestDto dto) {
    Blog blog = findEntityById(id);

    if (dto.getSlug() != null && !dto.getSlug().equals(blog.getSlug()) && blogRepository.existsBySlug(dto.getSlug())) {
      throw new IllegalArgumentException("Ya existe un blog con el slug: " + dto.getSlug());
    }

    if (dto.getCategoryId() != null) {
      blog.setCategory(categoryService.findEntityById(dto.getCategoryId()));
    }

    if (dto.getTags() != null) {
      List<Tag> tags = dto.getTags().stream().map(tagService::findOrCreateByName).toList();
      blog.setTags(tags);
    }

    blogMapper.updateBlogFromDto(dto, blog);
    return blogMapper.toDetailResponseDto(blogRepository.save(blog));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    if (!blogRepository.existsById(id)) {
      throw new ResourceNotFoundException("Blog no encontrado con el id: " + id);
    }
    blogRepository.deleteById(id);
  }

}
