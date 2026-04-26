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
import com.cefire.cefiretlx.shared.exception.ForbiddenException;
import com.cefire.cefiretlx.shared.exception.ResourceNotFoundException;
import com.cefire.cefiretlx.shared.util.AuthUtils;
import com.cefire.cefiretlx.tag.domain.Tag;
import com.cefire.cefiretlx.tag.service.ITagService;
import com.cefire.cefiretlx.user.domain.User;
import com.cefire.cefiretlx.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

  private final BlogRepository blogRepository;
  private final BlogMapper blogMapper;
  private final ICategoryService categoryService;
  private final ITagService tagService;
  private final UserRepository userRepository;
  private final AuthUtils authUtils;

  @Override
  @Transactional(readOnly = true)
  public Page<BlogSummaryResponseDto> findAllPaginated(
      String title, BlogStatus status, Long categoryId,
      LocalDateTime updatedFrom, LocalDateTime updatedTo, Pageable pageable) {

    UUID authorFilter = null;
    if (authUtils.isOnlyEditor()) {
      authorFilter = authUtils.getCurrentUser().getId();
      logger.debug("Editor autenticado: filtrando blogs por authorId={}", authorFilter);
    }

    Specification<Blog> spec = BlogSpecification.withFilters(title, status, categoryId, authorFilter, updatedFrom, updatedTo);
    Page<BlogSummaryResponseDto> result = blogRepository.findAll(spec, pageable).map(blogMapper::toSummaryResponseDto);
    logger.debug("findAllPaginated: {} blogs encontrados (página {}/{})", result.getNumberOfElements(), result.getNumber(), result.getTotalPages());
    return result;
  }

  @Override
  @Transactional
  public BlogDetailResponseDto create(BlogCreateRequestDto dto) {
    logger.info("Creando blog con slug='{}'", dto.getSlug());

    if (blogRepository.existsBySlug(dto.getSlug())) {
      logger.warn("Slug duplicado al crear blog: '{}'", dto.getSlug());
      throw new IllegalArgumentException("Ya existe un blog con el slug: " + dto.getSlug());
    }

    Category category = categoryService.findEntityById(dto.getCategoryId());

    User author;
    if (authUtils.isOnlyEditor()) {
      author = authUtils.getCurrentUser();
      logger.debug("Autor asignado automáticamente al editor: '{}'", author.getUsername());
    } else {
      author = userRepository.findById(dto.getAuthorId())
          .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id: " + dto.getAuthorId()));
    }

    Blog blog = blogMapper.toEntity(dto);
    blog.setCategory(category);
    blog.setAuthor(author);

    if (dto.getTags() != null && !dto.getTags().isEmpty()) {
      List<Tag> tags = dto.getTags().stream().map(tagService::findOrCreateByName).toList();
      blog.setTags(tags);
      logger.debug("Tags asociados al blog: {}", dto.getTags());
    }

    Blog saved = blogRepository.save(blog);
    logger.info("Blog creado exitosamente con id={} slug='{}'", saved.getId(), saved.getSlug());
    return blogMapper.toDetailResponseDto(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public BlogDetailResponseDto findById(Long id) {
    logger.debug("Buscando blog por id={}", id);
    Blog blog = findEntityById(id);
    if (authUtils.isOnlyEditor()) {
      User currentUser = authUtils.getCurrentUser();
      if (!blog.getAuthor().getId().equals(currentUser.getId())) {
        logger.warn("Editor '{}' intentó acceder al blog id={} sin ser el autor", currentUser.getUsername(), id);
        throw new ForbiddenException("No tienes permiso para ver este blog");
      }
    }
    return blogMapper.toDetailResponseDto(blog);
  }

  @Override
  @Transactional(readOnly = true)
  public BlogDetailResponseDto findBySlug(String slug) {
    logger.debug("Buscando blog por slug='{}'", slug);
    Blog blog = blogRepository.findBySlug(slug)
        .orElseThrow(() -> new ResourceNotFoundException("Blog no encontrado con el slug: " + slug));
    if (authUtils.isOnlyEditor()) {
      User currentUser = authUtils.getCurrentUser();
      if (!blog.getAuthor().getId().equals(currentUser.getId())) {
        logger.warn("Editor '{}' intentó acceder al blog slug='{}' sin ser el autor", currentUser.getUsername(), slug);
        throw new ForbiddenException("No tienes permiso para ver este blog");
      }
    }
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
    logger.debug("Obteniendo todos los blogs sin paginación");
    List<BlogDetailResponseDto> result = blogRepository.findAll()
        .stream()
        .map(blogMapper::toDetailResponseDto)
        .toList();
    logger.debug("findAll: {} blogs encontrados", result.size());
    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public List<BlogDetailResponseDto> findByStatus(BlogStatus status) {
    logger.debug("Buscando blogs con status={}", status);
    List<BlogDetailResponseDto> result = blogRepository.findByStatus(status)
        .stream()
        .map(blogMapper::toDetailResponseDto)
        .toList();
    logger.debug("findByStatus({}): {} blogs encontrados", status, result.size());
    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public List<BlogDetailResponseDto> findByCategoryId(Long categoryId) {
    logger.debug("Buscando blogs por categoryId={}", categoryId);
    List<BlogDetailResponseDto> result = blogRepository.findByCategoryId(categoryId)
        .stream()
        .map(blogMapper::toDetailResponseDto)
        .toList();
    logger.debug("findByCategoryId({}): {} blogs encontrados", categoryId, result.size());
    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public List<BlogDetailResponseDto> findByAuthorId(UUID authorId) {
    logger.debug("Buscando blogs por authorId={}", authorId);
    if (authUtils.isOnlyEditor()) {
      User currentUser = authUtils.getCurrentUser();
      if (!currentUser.getId().equals(authorId)) {
        logger.warn("Editor '{}' intentó listar blogs del autor {} sin permiso", currentUser.getUsername(), authorId);
        throw new ForbiddenException("Solo puedes ver tus propios blogs");
      }
    }
    List<BlogDetailResponseDto> result = blogRepository.findByAuthorId(authorId)
        .stream()
        .map(blogMapper::toDetailResponseDto)
        .toList();
    logger.debug("findByAuthorId({}): {} blogs encontrados", authorId, result.size());
    return result;
  }

  @Override
  @Transactional
  public BlogDetailResponseDto update(Long id, BlogUpdateRequestDto dto) {
    logger.info("Actualizando blog id={}", id);
    Blog blog = findEntityById(id);

    if (authUtils.isOnlyEditor()) {
      User currentUser = authUtils.getCurrentUser();
      if (!blog.getAuthor().getId().equals(currentUser.getId())) {
        logger.warn("Editor '{}' intentó editar el blog id={} sin ser el autor", currentUser.getUsername(), id);
        throw new ForbiddenException("No tienes permiso para editar este blog");
      }
    }

    if (dto.getSlug() != null && !dto.getSlug().equals(blog.getSlug()) && blogRepository.existsBySlug(dto.getSlug())) {
      logger.warn("Slug duplicado al actualizar blog id={}: '{}'", id, dto.getSlug());
      throw new IllegalArgumentException("Ya existe un blog con el slug: " + dto.getSlug());
    }

    if (dto.getCategoryId() != null) {
      blog.setCategory(categoryService.findEntityById(dto.getCategoryId()));
    }

    if (dto.getTags() != null) {
      List<Tag> tags = dto.getTags().stream().map(tagService::findOrCreateByName).toList();
      blog.setTags(tags);
      logger.debug("Tags actualizados en blog id={}: {}", id, dto.getTags());
    }

    blogMapper.updateBlogFromDto(dto, blog);
    Blog saved = blogRepository.save(blog);
    logger.info("Blog id={} actualizado exitosamente", saved.getId());
    return blogMapper.toDetailResponseDto(saved);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    logger.info("Eliminando blog id={}", id);
    if (!blogRepository.existsById(id)) {
      logger.warn("Intento de eliminar blog inexistente con id={}", id);
      throw new ResourceNotFoundException("Blog no encontrado con el id: " + id);
    }
    blogRepository.deleteById(id);
    logger.info("Blog id={} eliminado exitosamente", id);
  }

}
