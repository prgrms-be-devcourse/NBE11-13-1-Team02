package com.example.ilovecoffee.service.menu;

import com.example.ilovecoffee.domain.entity.menu.Menu;
import com.example.ilovecoffee.domain.entity.menu.MenuVersion;
import com.example.ilovecoffee.domain.enums.MenuStatus;
import com.example.ilovecoffee.domain.repository.MenuRepository;
import com.example.ilovecoffee.domain.repository.MenuVersionRepository;
import com.example.ilovecoffee.dto.menu.request.AdminMenuCreateRequest;
import com.example.ilovecoffee.dto.menu.request.AdminMenuReplenishRequest;
import com.example.ilovecoffee.dto.menu.request.AdminMenuUpdateRequest;
import com.example.ilovecoffee.dto.menu.response.AdminMenuResponse;
import com.example.ilovecoffee.dto.menu.response.MenuResponse;
import com.example.ilovecoffee.exception.MenuNotFoundException;
import com.example.ilovecoffee.exception.MenuNotInTrashException;
import com.example.ilovecoffee.mapper.MenuMapper;
import com.example.ilovecoffee.service.component.ImageStorageManager;
import com.example.ilovecoffee.service.component.InventoryManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuMapper menuMapper;
    private final MenuRepository menuRepository;
    private final MenuVersionRepository menuVersionRepository;
    private final ImageStorageManager imageStorageManager;
    private final InventoryManager inventoryManager;

    // 고객용
    public List<MenuResponse> findAllForCustomer() {
        log.debug("[고객용 메뉴 전체 조회 요청]");

        List<MenuResponse> responses = menuRepository
                .findAllByStatusNot(MenuStatus.DELETED)
                .stream()
                .map(menuMapper::toMenuResponse)
                .toList();

        log.debug("[고객용 메뉴 전체 조회 완료] count={}", responses.size());

        return responses;
    }

    public MenuResponse findByIdForCustomer(Long id) {
        log.debug("[고객용 메뉴 상세 조회 요청] menuId={}", id);

        Menu menu = menuRepository
                .findByIdAndStatusNot(id, MenuStatus.DELETED)
                .orElseThrow(() -> customerMenuNotFound(id));

        log.debug(
                "[고객용 메뉴 상세 조회 완료] menuId={}, name={}, status={}",
                menu.getId(),
                menu.getName(),
                menu.getStatus()
        );

        return menuMapper.toMenuResponse(menu);
    }

    // 관리자용
    public List<AdminMenuResponse> findAllForAdmin() {
        log.debug("[관리자용 메뉴 전체 조회 요청]");

        List<AdminMenuResponse> responses = menuRepository
                .findAllByStatusNot(MenuStatus.DELETED)
                .stream()
                .map(menuMapper::toAdminMenuResponse)
                .toList();

        log.debug("[관리자용 메뉴 전체 조회 완료] count={}", responses.size());

        return responses;
    }

    public List<AdminMenuResponse> findSoldOutMenus() {
        log.debug("[품절 메뉴 조회 요청]");
        var responses = menuRepository
                .findAllByStockAndStatusNot(0, MenuStatus.DELETED)
                .stream()
                .map(menuMapper::toAdminMenuResponse)
                .toList();
        log.debug("[품절 메뉴 조회 완료] count={}", responses.size());
        return responses;
    }

    @Transactional
    public AdminMenuResponse replenish(
            Long id,
            AdminMenuReplenishRequest request
    ) {
        log.info(
                "[메뉴 재입고 요청] menuId={}, quantity={}",
                id,
                request.quantity()
        );
        Menu menu = findByIdOrThrow(id);
        inventoryManager.replenish(menu, request.quantity());
        log.info(
                "[메뉴 재입고 완료] menuId={}, stock={}",
                menu.getId(),
                menu.getStock()
        );
        return menuMapper.toAdminMenuResponse(menu);
    }


    @Transactional
    public AdminMenuResponse create(
            AdminMenuCreateRequest request,
            MultipartFile image
    ) {
        log.info(
                "[메뉴 생성 요청] name={}, price={}, stock={}, hasImage={}",
                request.name(),
                request.price(),
                request.stock(),
                image != null && !image.isEmpty()
        );

        String imageUrl = imageStorageManager.store(image);
        Menu menu = menuMapper.toEntity(request, imageUrl);
        Menu saved = menuRepository.save(menu);

        log.info(
                "[메뉴 생성 완료] menuId={}, name={}, status={}, stock={}",
                saved.getId(),
                saved.getName(),
                saved.getStatus(),
                saved.getStock()
        );

        return menuMapper.toAdminMenuResponse(saved);
    }

    @Transactional
    public AdminMenuResponse update(
            Long id,
            AdminMenuUpdateRequest request,
            MultipartFile image
    ) {
        log.info(
                "[메뉴 수정 요청] menuId={}, name={}, price={}, stock={}, hasImage={}",
                id,
                request.name(),
                request.price(),
                request.stock(),
                image != null && !image.isEmpty()
        );

        Menu menu = findByIdOrThrow(id);

        archive(menu);

        String imageUrl = menu.getImageUrl();

        if (image != null && !image.isEmpty()) {
            imageUrl = imageStorageManager.store(image);

            log.debug(
                    "[메뉴 이미지 변경] menuId={}, imageUrl={}",
                    id,
                    imageUrl
            );
        }

        menuMapper.updateEntity(menu, request, imageUrl);
        menuMapper.updateStock(menu, request.stock());

        log.info(
                "[메뉴 수정 완료] menuId={}, name={}, status={}, stock={}",
                menu.getId(),
                menu.getName(),
                menu.getStatus(),
                menu.getStock()
        );

        return menuMapper.toAdminMenuResponse(menu);
    }

    @Transactional
    public void softDelete(Long id) {
        log.info("[메뉴 소프트 삭제 요청] menuId={}", id);

        Menu menu = findByIdOrThrow(id);
        menu.softDelete();

        log.info(
                "[메뉴 소프트 삭제 완료] menuId={}, changedStatus={}",
                menu.getId(),
                menu.getStatus()
        );
    }

    @Transactional
    public void restore(Long id) {
        log.info("[메뉴 복원 요청] menuId={}", id);
        Menu menu = findByIdOrThrow(id);
        menu.restore();
        log.info(
                "[메뉴 복원 완료] menuId={}, changedStatus={}",
                menu.getId(),
                menu.getStatus()
        );
    }

    @Transactional
    public void permanentlyDelete(Long id) {
        log.info("[메뉴 완전 삭제 요청] menuId={}", id);

        Menu menu = findByIdOrThrow(id);

        log.debug(
                "[메뉴 완전 삭제 검증] menuId={}, currentStatus={}",
                menu.getId(),
                menu.getStatus()
        );

        validateDeletedMenu(menu);

        String imageUrl = menu.getImageUrl();

        archive(menu);
        imageStorageManager.delete(imageUrl);
        menuRepository.delete(menu);

        log.info(
                "[메뉴 완전 삭제 완료] menuId={}, imageUrl={}",
                id,
                imageUrl
        );
    }
    
    public List<AdminMenuResponse> findTrash() {
        log.debug("[휴지통 메뉴 조회 요청]");

        List<AdminMenuResponse> responses = menuRepository
                .findAllByStatus(MenuStatus.DELETED)
                .stream()
                .map(menuMapper::toAdminMenuResponse)
                .toList();

        log.debug("[휴지통 메뉴 조회 완료] count={}", responses.size());

        return responses;
    }

    @Transactional
    public void activate(Long id) {
        log.info("[메뉴 활성화 요청] menuId={}", id);

        Menu menu = findByIdOrThrow(id);
        menu.activate();

        log.info(
                "[메뉴 활성화 완료] menuId={}, changedStatus={}",
                menu.getId(),
                menu.getStatus()
        );
    }

    @Transactional
    public void deactivate(Long id) {
        log.info("[메뉴 비활성화 요청] menuId={}", id);

        Menu menu = findByIdOrThrow(id);
        menu.deactivate();

        log.info(
                "[메뉴 비활성화 완료] menuId={}, changedStatus={}",
                menu.getId(),
                menu.getStatus()
        );
    }

    private void archive(Menu menu) {
        log.debug(
                "[메뉴 버전 저장] menuId={}, version={}, name={}, price={}",
                menu.getId(),
                menu.getVersion(),
                menu.getName(),
                menu.getPrice()
        );

        MenuVersion menuVersion = MenuVersion.from(menu);
        menuVersionRepository.save(menuVersion);
    }

    private void validateDeletedMenu(Menu menu) {
        if (menu.getStatus() != MenuStatus.DELETED) {
            log.warn(
                    "[메뉴 완전 삭제 검증 실패] menuId={}, currentStatus={}",
                    menu.getId(),
                    menu.getStatus()
            );

            throw new MenuNotInTrashException();
        }
    }

    private Menu findByIdOrThrow(Long id) {
        log.debug("[메뉴 조회] menuId={}", id);

        return menuRepository.findById(id)
                .orElseThrow(() -> menuNotFound(id));
    }

    private MenuNotFoundException menuNotFound(Long id) {
        log.warn("[메뉴 조회 실패] menuId={}", id);
        return new MenuNotFoundException();
    }

    private MenuNotFoundException customerMenuNotFound(Long id) {
        log.warn(
                "[고객용 메뉴 조회 실패] menuId={}, reason=존재하지 않거나 삭제된 메뉴",
                id
        );

        return new MenuNotFoundException();
    }
}