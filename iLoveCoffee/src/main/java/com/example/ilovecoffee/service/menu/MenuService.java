package com.example.ilovecoffee.service.menu;

import com.example.ilovecoffee.domain.entity.menu.Menu;
import com.example.ilovecoffee.domain.entity.menu.MenuVersion;
import com.example.ilovecoffee.domain.enums.MenuStatus;
import com.example.ilovecoffee.domain.repository.MenuRepository;
import com.example.ilovecoffee.domain.repository.MenuVersionRepository;
import com.example.ilovecoffee.dto.menu.request.AdminMenuCreateRequest;
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

    // 고객용
    public List<MenuResponse> findAllForCustomer() {
        return menuRepository.findAllByStatusNot(MenuStatus.DELETED).stream()
                .map(menuMapper::toMenuResponse)
                .toList();
    }
    public List<AdminMenuResponse> findAllForAdmin() {
        return menuRepository.findAllByStatusNot(MenuStatus.DELETED).stream()
                .map(menuMapper::toAdminMenuResponse)
                .toList();
    }

    public MenuResponse findByIdForCustomer(Long id) {
        Menu menu = menuRepository.findByIdAndStatusNot(id, MenuStatus.DELETED)
                .orElseThrow(() -> {
                    log.debug("고객 조회 실패 - 존재하지 않거나 삭제된 메뉴: id={}", id);
                    return new MenuNotFoundException();
                });

        return menuMapper.toMenuResponse(menu);
    }

    // 관리자용
    @Transactional
    public AdminMenuResponse create(
            AdminMenuCreateRequest request,
            MultipartFile image
    ) {
        String imageUrl = imageStorageManager.store(image);
        Menu menu = menuMapper.toEntity(request, imageUrl);
        Menu saved = menuRepository.save(menu);
        log.info("메뉴 생성됨: id={}, name={}", saved.getId(), saved.getName());
        return menuMapper.toAdminMenuResponse(saved);
    }

    @Transactional
    public AdminMenuResponse update(
            Long id,
            AdminMenuUpdateRequest request,
            MultipartFile image
    ) {
        Menu menu = findByIdOrThrow(id);
        archive(menu);

        String imageUrl = menu.getImageUrl();
        if (image != null && !image.isEmpty()) {
            imageUrl = imageStorageManager.store(image);
        }
        menuMapper.updateEntity(menu, request, imageUrl);
        menuMapper.updateStock(menu, request.stock());
        log.info("메뉴 수정됨: id={}, name={}", id, menu.getName());
        return menuMapper.toAdminMenuResponse(menu);
    }

    @Transactional
    public void softDelete(Long id) {
        findByIdOrThrow(id).softDelete();
        log.info("메뉴 소프트 삭제됨(휴지통행): id={}", id);
    }

    @Transactional
    public void restore(Long id) {
        findByIdOrThrow(id).activate();
        log.info("메뉴 복원됨: id={}", id);
    }

    @Transactional
    public void permanentlyDelete(Long id) {
        Menu menu = findByIdOrThrow(id);
        validateDeletedMenu(menu);

        String imageUrl = menu.getImageUrl();
        imageStorageManager.delete(imageUrl);

        archive(menu);
        menuRepository.delete(menu);
        log.info("메뉴 완전 삭제됨: id={}", id);
    }

    public List<AdminMenuResponse> findTrash() {
        return menuRepository.findAllByStatus(MenuStatus.DELETED).stream()
                .map(menuMapper::toAdminMenuResponse)
                .toList();
    }

    private void archive(Menu menu) {
        MenuVersion menuVersion = MenuVersion.from(menu);
        menuVersionRepository.save(menuVersion);
    }

    @Transactional
    public void activate(Long id) {
        findByIdOrThrow(id).activate();
        log.info("메뉴 활성화됨: id={}", id);
    }

    @Transactional
    public void deactivate(Long id) {
        findByIdOrThrow(id).deactivate();
        log.info("메뉴 비활성화됨: id={}", id);
    }

    private void validateDeletedMenu(Menu menu) {
        if (menu.getStatus() != MenuStatus.DELETED) {
            throw new MenuNotInTrashException();
        }
    }

    private Menu findByIdOrThrow(Long id) {
        return menuRepository.findById(id).orElseThrow(() -> {
            log.warn("존재하지 않는 메뉴: id={}", id);
            return new MenuNotFoundException();
        });
    }
}