package com.cefire.cefiretlx.tag.service;

import com.cefire.cefiretlx.tag.domain.Tag;
import com.cefire.cefiretlx.tag.dto.TagDetailResponseDto;
import com.cefire.cefiretlx.tag.dto.TagRequestDto;
import com.cefire.cefiretlx.tag.dto.TagResponseDto;

import java.util.List;

public interface ITagService {
  TagResponseDto save(TagRequestDto tagRequestDto);
  List<TagResponseDto> findAll();
  TagDetailResponseDto findById(Long id);
  Tag findEntityById(Long id);
  TagResponseDto update(Long id, TagRequestDto tagRequestDto);
  void deleteTag(Long id);
}
