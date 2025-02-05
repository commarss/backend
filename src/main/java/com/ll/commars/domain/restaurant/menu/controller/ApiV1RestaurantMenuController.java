package com.ll.commars.domain.restaurant.menu.controller;

import com.ll.commars.domain.restaurant.menu.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.menu.service.RestaurantMenuService;
import com.ll.commars.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menu")
public class ApiV1RestaurantMenuController {
    private final RestaurantMenuService restaurantMenuService;

    // 식당 메뉴 수정
    @PatchMapping("/{menu_id}")
    public RsData<RestaurantMenuDto.RestaurantMenuModifyResponse> modifyMenu(
            @PathVariable("menu_id") @NotNull Long menuId,
            @RequestBody @Valid RestaurantMenuDto.MenuInfo request
    ){
        RestaurantMenuDto.RestaurantMenuModifyResponse response = restaurantMenuService.modifyMenu(menuId, request);
        return new RsData<>("200", "식당 메뉴 수정 성공", response);
    }

    // 식당 메뉴 삭제
//    @DeleteMapping("/{menu_id}")
}
