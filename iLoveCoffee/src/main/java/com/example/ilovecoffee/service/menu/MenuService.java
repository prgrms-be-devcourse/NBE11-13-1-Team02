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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuMapper menuMapper;
    private final MenuRepository menuRepository;
    private final MenuVersionRepository menuVersionRepository;

    // 고객용
    public List<MenuResponse> findAllForCustomer() {
        return menuRepository.findAllByStatusNot(MenuStatus.DELETED).stream()
                .map(menuMapper::toMenuResponse)
                .toList();
    }

    public MenuResponse findByIdForCustomer(Long id) {
        Menu menu = menuRepository.findByIdAndStatusNot(id, MenuStatus.DELETED)
                .orElseThrow(MenuNotFoundException::new);

        return menuMapper.toMenuResponse(menu);
    }

    // 관리자용
    @Transactional
    public AdminMenuResponse create(AdminMenuCreateRequest request) {
        Menu menu = menuMapper.toEntity(request);
        Menu savedMenu = menuRepository.save(menu);

        return menuMapper.toAdminMenuResponse(savedMenu);
    }

    @Transactional
    public AdminMenuResponse update(
            Long id,
            AdminMenuUpdateRequest request
    ) {
        Menu menu = findByIdOrThrow(id);
        archive(menu);
        menuMapper.updateEntity(menu, request);
        menu.updateStock(request.stock());
        return menuMapper.toAdminMenuResponse(menu);
    }

    @Transactional
    public void softDelete(Long id) {
        Menu menu = findByIdOrThrow(id);
        menu.softDelete();
    }

    @Transactional
    public void restore(Long id) {
        Menu menu = findByIdOrThrow(id);
        menu.activate();
    }

    @Transactional
    public void permanentlyDelete(Long id) {
        Menu menu = findByIdOrThrow(id);

        validateDeletedMenu(menu);

        archive(menu);
        menuRepository.delete(menu);
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
        return menuRepository.findById(id)
                .orElseThrow(MenuNotFoundException::new);
    }
}