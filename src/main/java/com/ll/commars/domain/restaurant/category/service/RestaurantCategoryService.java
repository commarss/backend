package com.ll.commars.domain.restaurant.category.service;

import com.ll.commars.domain.restaurant.category.dto.RestaurantCategoryDto;
import com.ll.commars.domain.restaurant.category.entity.RestaurantCategory;
import com.ll.commars.domain.restaurant.category.repository.RestaurantCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantCategoryService {
    private final RestaurantCategoryRepository restaurantCategoryRepository;

    public RestaurantCategoryDto.RestaurantCategoryInfo writeCategory(
            RestaurantCategoryDto.RestaurantCategoryWriteRequest request
    ) {
        RestaurantCategory restaurantCategory = RestaurantCategory.builder()
                .name(request.getName())
                .build();

        restaurantCategoryRepository.save(restaurantCategory);

        return RestaurantCategoryDto.RestaurantCategoryInfo.builder()
                .id(restaurantCategory.getId())
                .name(restaurantCategory.getName())
                .build();
    }
}
