package com.ll.commars.domain.restaurant.menu.controller;

import com.ll.commars.domain.restaurant.menu.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.menu.service.RestaurantMenuService;
import com.ll.commars.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/menu", produces = APPLICATION_JSON_VALUE)
@Tag(name = "ApiV1RestaurantMenuController", description = "식당 메뉴 API")
public class ApiV1RestaurantMenuController {
    private final RestaurantMenuService restaurantMenuService;

    @PatchMapping(value = "/{menu_id}", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "식당 메뉴 수정")
    public RsData<RestaurantMenuDto.MenuWriteResponse> modifyMenu(
            @PathVariable("menu_id") @NotNull Long menuId,
            @RequestBody @Valid RestaurantMenuDto.MenuInfo request
    ){
        RestaurantMenuDto.MenuWriteResponse response = restaurantMenuService.modifyMenu(menuId, request);
        return new RsData<>("200", "식당 메뉴 수정 성공", response);
    }

    @DeleteMapping("/{menu_id}")
    @Operation(summary = "식당 메뉴 삭제")
    public RsData<String> deleteMenu(
            @PathVariable("menu_id") @NotNull Long menuId
    ){
        restaurantMenuService.deleteMenu(menuId);
        return new RsData<>("200", "식당 메뉴 삭제 성공", "메뉴 삭제 성공!");
    }

    @GetMapping("/showAllMenus")
    @Operation(summary = "모든 메뉴 조회")
    public RsData<RestaurantMenuDto.ShowAllMenusResponse> showAllReviews(
            @RequestParam("restaurant_id") @NotNull Long restaurantId
    ){
        RestaurantMenuDto.ShowAllMenusResponse response = restaurantMenuService.findByRestaurantId(restaurantId);
        return new RsData<>("200", "모든 리뷰 조회 성공", response);
    }
}
