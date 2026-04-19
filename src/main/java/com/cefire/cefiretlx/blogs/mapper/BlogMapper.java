package com.cefire.cefiretlx.blogs.mapper;

import com.cefire.cefiretlx.blogs.domain.Blog;
import com.cefire.cefiretlx.blogs.dto.BlogCreateRequestDto;
import com.cefire.cefiretlx.blogs.dto.BlogDetailResponseDto;
import com.cefire.cefiretlx.blogs.dto.BlogSummaryResponseDto;
import com.cefire.cefiretlx.blogs.dto.BlogUpdateRequestDto;
import com.cefire.cefiretlx.category.mapper.CategoryMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface BlogMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "category", ignore = true)
  @Mapping(target = "author", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Blog toEntity(BlogCreateRequestDto dto);

  BlogDetailResponseDto toDetailResponseDto(Blog blog);

  @Mapping(target = "authorDisplayName", expression = "java(blog.getAuthor().getDisplayName())")
  BlogSummaryResponseDto toSummaryResponseDto(Blog blog);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "category", ignore = true)
  @Mapping(target = "author", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateBlogFromDto(BlogUpdateRequestDto dto, @MappingTarget Blog blog);

}
