package com.cefire.cefiretlx.blog.mapper;

import com.cefire.cefiretlx.blog.domain.Blog;
import com.cefire.cefiretlx.blog.dto.BlogCreateRequestDto;
import com.cefire.cefiretlx.blog.dto.BlogDetailResponseDto;
import com.cefire.cefiretlx.blog.dto.BlogSummaryResponseDto;
import com.cefire.cefiretlx.blog.dto.BlogUpdateRequestDto;
import com.cefire.cefiretlx.category.mapper.CategoryMapper;
import com.cefire.cefiretlx.tag.mapper.TagMapper;
import com.cefire.cefiretlx.user.mapper.UserMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, UserMapper.class, TagMapper.class})
public interface BlogMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "category", ignore = true)
  @Mapping(target = "author", ignore = true)
  @Mapping(target = "tags", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Blog toEntity(BlogCreateRequestDto dto);

  BlogDetailResponseDto toDetailResponseDto(Blog blog);

  @Mapping(target = "authorDisplayName", expression = "java(blog.getAuthor().getDisplayName())")
  BlogSummaryResponseDto toSummaryResponseDto(Blog blog);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "category", ignore = true)
  @Mapping(target = "author", ignore = true)
  @Mapping(target = "tags", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateBlogFromDto(BlogUpdateRequestDto dto, @MappingTarget Blog blog);

}
