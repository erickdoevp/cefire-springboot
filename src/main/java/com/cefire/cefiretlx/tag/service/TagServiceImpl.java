package com.cefire.cefiretlx.tag.service;

import com.cefire.cefiretlx.shared.exception.ResourceNotFoundException;
import com.cefire.cefiretlx.tag.domain.Tag;
import com.cefire.cefiretlx.tag.dto.TagDetailResponseDto;
import com.cefire.cefiretlx.tag.dto.TagRequestDto;
import com.cefire.cefiretlx.tag.dto.TagResponseDto;
import com.cefire.cefiretlx.tag.mapper.TagMapper;
import com.cefire.cefiretlx.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements ITagService {

  private static final Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);

  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  @Override
  @Transactional
  public TagResponseDto save(TagRequestDto tagRequestDto) {
    logger.info("Creando tag con nombre='{}'", tagRequestDto.getName());
    Tag tagToSave = tagMapper.toEntity(tagRequestDto);
    Tag tagSaved = tagRepository.save(tagToSave);
    logger.info("Tag creado exitosamente con id={}", tagSaved.getId());
    return tagMapper.toResponseDto(tagSaved);
  }

  @Override
  @Transactional(readOnly = true)
  public List<TagResponseDto> findAll() {
    logger.debug("Obteniendo todos los tags");
    List<TagResponseDto> result = tagRepository.findAll()
        .stream()
        .map(tagMapper::toResponseDto).toList();
    logger.debug("findAll: {} tags encontrados", result.size());
    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public TagDetailResponseDto findById(Long id) {
    logger.debug("Buscando tag por id={}", id);
    Tag tag = tagRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tag no encontrado con el id " + id));
    return tagMapper.toDetailResponseDto(tag);
  }

  @Override
  @Transactional(readOnly = true)
  public Tag findEntityById(Long id) {
    return tagRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tag no encontrado con el id " + id));
  }

  @Override
  @Transactional
  public Tag findOrCreateByName(String name) {
    return tagRepository.findByName(name).orElseGet(() -> {
      logger.debug("Tag '{}' no existe, creando uno nuevo", name);
      Tag newTag = new Tag();
      newTag.setName(name);
      Tag saved = tagRepository.save(newTag);
      logger.info("Tag '{}' creado automáticamente con id={}", name, saved.getId());
      return saved;
    });
  }

  @Override
  @Transactional
  public TagResponseDto update(Long id, TagRequestDto tagRequestDto) {
    logger.info("Actualizando tag id={}", id);
    Tag tagToUpdate = tagRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tag no encontrado con el id " + id));
    tagMapper.updateTagFromDto(tagRequestDto, tagToUpdate);
    Tag saved = tagRepository.save(tagToUpdate);
    logger.info("Tag id={} actualizado a nombre='{}'", saved.getId(), saved.getName());
    return tagMapper.toResponseDto(saved);
  }

  @Override
  @Transactional
  public void deleteTag(Long id) {
    logger.info("Eliminando tag id={}", id);
    if (!tagRepository.existsById(id)) {
      logger.warn("Intento de eliminar tag inexistente con id={}", id);
      throw new ResourceNotFoundException("Tag no encontrado con el id " + id);
    }
    tagRepository.deleteById(id);
    logger.info("Tag id={} eliminado exitosamente", id);
  }

}
