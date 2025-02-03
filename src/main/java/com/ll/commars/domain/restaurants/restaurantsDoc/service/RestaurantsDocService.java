package com.ll.commars.domain.restaurants.restaurantsDoc.service;

import com.ll.commars.domain.restaurants.restaurantsDoc.document.RestaurantsDoc;
import com.ll.commars.domain.restaurants.restaurantsDoc.repository.RestaurantsDocRepository;
import com.ll.commars.domain.reviews.reviewsDoc.document.ReviewsDoc;
import com.ll.commars.domain.reviews.reviewsDoc.repository.ReviewsDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantsDocService {
    private final RestaurantsDocRepository restaurantsDocRepository;

    public RestaurantsDoc write(String name, String details, Double averageRate) {
        RestaurantsDoc restaurantsDoc = RestaurantsDoc.builder()
                .name(name)
                .details(details)
                .averageRate(averageRate)
                .build();

        return restaurantsDocRepository.save(restaurantsDoc);
    }

    public void truncate() {
        restaurantsDocRepository.deleteAll();
    }

    public List<RestaurantsDoc> searchByKeyword(String keyword) {
        return restaurantsDocRepository.searchByKeyword(keyword);
    }

    public List<RestaurantsDoc> showSortByRate() {
        return restaurantsDocRepository.findAllByOrderByAverageRateDesc();
    }
}
