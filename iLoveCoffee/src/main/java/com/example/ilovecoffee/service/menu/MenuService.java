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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .orElseThrow(() -> new MenuNotFoundException(id));

        return menuMapper.toMenuResponse(menu);
    }

    // 관리자용
    @Transactional
    public AdminMenuResponse create(AdminMenuCreateRequest request) {
        Menu menu = menuMapper.toEntity(request);
        return menuMapper.toAdminMenuResponse(menuRepository.save(menu));
    }

    @Transactional
    public AdminMenuResponse update(Long id, AdminMenuUpdateRequest request) {
        Menu menu = findByIdOrThrow(id);
        menuMapper.updateEntity(menu, request);
        return menuMapper.toAdminMenuResponse(menu);
    }

    @Transactional
    public void softDelete(Long id) {
        findByIdOrThrow(id).softDelete();
    }

    @Transactional
    public void restore(Long id) {
        findByIdOrThrow(id).activate();
    }

    @Transactional
    public void permanentlyDelete(Long id) {
        Menu menu = findByIdOrThrow(id);
        if (menu.getStatus() != MenuStatus.DELETED) {
            throw new MenuNotInTrashException();
        }
        menuRepository.delete(menu);
    }

    public List<AdminMenuResponse> findTrash() {
        return menuRepository.findAllByStatus(MenuStatus.DELETED).stream()
                .map(menuMapper::toAdminMenuResponse)
                .toList();
    }

    private Menu findByIdOrThrow(Long id) {
        return menuRepository.findById(id).orElseThrow(() -> new MenuNotFoundException(id));
    }
}
