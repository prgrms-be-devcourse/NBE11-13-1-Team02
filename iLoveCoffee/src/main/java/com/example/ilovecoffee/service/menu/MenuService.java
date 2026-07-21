package com.example.ilovecoffee.service.menu;

import com.example.ilovecoffee.domain.entity.menu.Menu;
import com.example.ilovecoffee.domain.enums.MenuStatus;
import com.example.ilovecoffee.domain.repository.MenuRepository;
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

    // 고객용
    public List<MenuResponse> findAllForCustomer() {
        return menuRepository.findAllByStatusNot(MenuStatus.DELETED).stream()
                .map(menuMapper::toMenuResponse)
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
    public AdminMenuResponse create(AdminMenuCreateRequest request) {
        Menu menu = menuMapper.toEntity(request);
        Menu saved = menuRepository.save(menu);
        log.info("메뉴 생성됨: id={}, name={}", saved.getId(), saved.getName());
        return menuMapper.toAdminMenuResponse(saved);
    }

    @Transactional
    public AdminMenuResponse update(Long id, AdminMenuUpdateRequest request) {
        Menu menu = findByIdOrThrow(id);
        menuMapper.updateEntity(menu, request);
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
        if (menu.getStatus() != MenuStatus.DELETED) {
            log.warn("휴지통에 없는 메뉴 완전삭제 시도: id={}, status={}", id, menu.getStatus());
            throw new MenuNotInTrashException();
        }
        menuRepository.delete(menu);
        log.info("메뉴 완전 삭제됨: id={}", id);
    }

    public List<AdminMenuResponse> findTrash() {
        return menuRepository.findAllByStatus(MenuStatus.DELETED).stream()
                .map(menuMapper::toAdminMenuResponse)
                .toList();
    }

    private Menu findByIdOrThrow(Long id) {
        return menuRepository.findById(id).orElseThrow(() -> {
            log.warn("존재하지 않는 메뉴: id={}", id);
            return new MenuNotFoundException();
        });
    }
}
