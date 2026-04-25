package com.cefire.cefiretlx.tag.service;

import com.cefire.cefiretlx.shared.exception.ResourceNotFoundException;
import com.cefire.cefiretlx.tag.domain.Tag;
import com.cefire.cefiretlx.tag.dto.TagDetailResponseDto;
import com.cefire.cefiretlx.tag.dto.TagRequestDto;
import com.cefire.cefiretlx.tag.dto.TagResponseDto;
import com.cefire.cefiretlx.tag.mapper.TagMapper;
import com.cefire.cefiretlx.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements ITagService {

  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  @Override
  @Transactional
  public TagResponseDto save(TagRequestDto tagRequestDto) {
    Tag tagToSave = tagMapper.toEntity(tagRequestDto);
    Tag tagSaved = tagRepository.save(tagToSave);
    return tagMapper.toResponseDto(tagSaved);
  }

  @Override
  @Transactional(readOnly = true)
  public List<TagResponseDto> findAll() {
    return tagRepository.findAll()
        .stream()
        .map(tagMapper::toResponseDto).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public TagDetailResponseDto findById(Long id) {
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
      Tag newTag = new Tag();
      newTag.setName(name);
      return tagRepository.save(newTag);
    });
  }

  @Override
  @Transactional
  public TagResponseDto update(Long id, TagRequestDto tagRequestDto) {
    Tag tagToUpdate = tagRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tag no encontrado con el id " + id));
    tagMapper.updateTagFromDto(tagRequestDto, tagToUpdate);
    return tagMapper.toResponseDto(tagRepository.save(tagToUpdate));
  }

  @Override
  @Transactional
  public void deleteTag(Long id) {
    if (!tagRepository.existsById(id)) {
      throw new ResourceNotFoundException("Tag no encontrado con el id " + id);
    }
    tagRepository.deleteById(id);
  }

}
