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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuMapper menuMapper;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuVersionRepository menuVersionRepository;

    @InjectMocks
    private MenuService menuService;

    @Nested
    @DisplayName("고객 메뉴 조회")
    class CustomerMenuQueryTest {

        @Test
        @DisplayName("삭제되지 않은 메뉴 전체를 조회한다")
        void findAllForCustomer() {
            // given
            Menu menu1 = mock(Menu.class);
            Menu menu2 = mock(Menu.class);

            MenuResponse response1 = mock(MenuResponse.class);
            MenuResponse response2 = mock(MenuResponse.class);

            when(menuRepository.findAllByStatusNot(MenuStatus.DELETED))
                    .thenReturn(List.of(menu1, menu2));

            when(menuMapper.toMenuResponse(menu1))
                    .thenReturn(response1);

            when(menuMapper.toMenuResponse(menu2))
                    .thenReturn(response2);

            // when
            List<MenuResponse> result = menuService.findAllForCustomer();

            // then
            assertThat(result).containsExactly(response1, response2);

            verify(menuRepository)
                    .findAllByStatusNot(MenuStatus.DELETED);

            verify(menuMapper).toMenuResponse(menu1);
            verify(menuMapper).toMenuResponse(menu2);
        }

        @Test
        @DisplayName("삭제되지 않은 메뉴를 ID로 조회한다")
        void findByIdForCustomer() {
            // given
            Long menuId = 1L;
            Menu menu = mock(Menu.class);
            MenuResponse response = mock(MenuResponse.class);

            when(menuRepository.findByIdAndStatusNot(
                    menuId,
                    MenuStatus.DELETED
            )).thenReturn(Optional.of(menu));

            when(menuMapper.toMenuResponse(menu))
                    .thenReturn(response);

            // when
            MenuResponse result =
                    menuService.findByIdForCustomer(menuId);

            // then
            assertThat(result).isSameAs(response);

            verify(menuRepository).findByIdAndStatusNot(
                    menuId,
                    MenuStatus.DELETED
            );

            verify(menuMapper).toMenuResponse(menu);
        }

        @Test
        @DisplayName("고객이 존재하지 않거나 삭제된 메뉴를 조회하면 예외가 발생한다")
        void findByIdForCustomerNotFound() {
            // given
            Long menuId = 1L;

            when(menuRepository.findByIdAndStatusNot(
                    menuId,
                    MenuStatus.DELETED
            )).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(
                    () -> menuService.findByIdForCustomer(menuId)
            ).isInstanceOf(MenuNotFoundException.class);

            verifyNoInteractions(menuMapper);
        }
    }

    @Nested
    @DisplayName("관리자 메뉴 생성")
    class CreateTest {

        @Test
        @DisplayName("메뉴를 생성한다")
        void create() {
            // given
            AdminMenuCreateRequest request =
                    mock(AdminMenuCreateRequest.class);

            Menu menu = mock(Menu.class);
            Menu savedMenu = mock(Menu.class);
            AdminMenuResponse response =
                    mock(AdminMenuResponse.class);

            when(menuMapper.toEntity(request))
                    .thenReturn(menu);

            when(menuRepository.save(menu))
                    .thenReturn(savedMenu);

            when(menuMapper.toAdminMenuResponse(savedMenu))
                    .thenReturn(response);

            // when
            AdminMenuResponse result =
                    menuService.create(request);

            // then
            assertThat(result).isSameAs(response);

            verify(menuMapper).toEntity(request);
            verify(menuRepository).save(menu);
            verify(menuMapper).toAdminMenuResponse(savedMenu);
        }
    }

    @Nested
    @DisplayName("관리자 메뉴 수정")
    class UpdateTest {

        @Test
        @DisplayName("수정 전 메뉴를 보관하고 메뉴 정보와 재고를 수정한다")
        void update() {
            // given
            Long menuId = 1L;
            int newStock = 30;

            AdminMenuUpdateRequest request =
                    mock(AdminMenuUpdateRequest.class);

            Menu menu = mock(Menu.class);
            MenuVersion menuVersion = mock(MenuVersion.class);
            AdminMenuResponse response =
                    mock(AdminMenuResponse.class);

            when(request.stock()).thenReturn(newStock);

            when(menuRepository.findById(menuId))
                    .thenReturn(Optional.of(menu));

            when(menuVersionRepository.save(menuVersion))
                    .thenReturn(menuVersion);

            when(menuMapper.toAdminMenuResponse(menu))
                    .thenReturn(response);

            try (MockedStatic<MenuVersion> mockedMenuVersion =
                         mockStatic(MenuVersion.class)) {

                mockedMenuVersion.when(() -> MenuVersion.from(menu))
                        .thenReturn(menuVersion);

                // when
                AdminMenuResponse result =
                        menuService.update(menuId, request);

                // then
                assertThat(result).isSameAs(response);

                mockedMenuVersion.verify(
                        () -> MenuVersion.from(menu)
                );
            }

            verify(menuVersionRepository).save(menuVersion);
            verify(menuMapper).updateEntity(menu, request);
            verify(menu).updateStock(newStock);
            verify(menuMapper).toAdminMenuResponse(menu);
        }

        @Test
        @DisplayName("존재하지 않는 메뉴를 수정하면 예외가 발생한다")
        void updateNotFound() {
            // given
            Long menuId = 1L;
            AdminMenuUpdateRequest request =
                    mock(AdminMenuUpdateRequest.class);

            when(menuRepository.findById(menuId))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(
                    () -> menuService.update(menuId, request)
            ).isInstanceOf(MenuNotFoundException.class);

            verifyNoInteractions(menuVersionRepository);
            verifyNoInteractions(menuMapper);
        }
    }

    @Nested
    @DisplayName("메뉴 삭제와 복구")
    class DeleteAndRestoreTest {

        @Test
        @DisplayName("메뉴를 소프트 삭제한다")
        void softDelete() {
            // given
            Long menuId = 1L;
            Menu menu = mock(Menu.class);

            when(menuRepository.findById(menuId))
                    .thenReturn(Optional.of(menu));

            // when
            menuService.softDelete(menuId);

            // then
            verify(menu).softDelete();
        }

        @Test
        @DisplayName("삭제된 메뉴를 활성화하여 복구한다")
        void restore() {
            // given
            Long menuId = 1L;
            Menu menu = mock(Menu.class);

            when(menuRepository.findById(menuId))
                    .thenReturn(Optional.of(menu));

            // when
            menuService.restore(menuId);

            // then
            verify(menu).activate();
        }

        @Test
        @DisplayName("삭제 상태인 메뉴를 보관한 뒤 영구 삭제한다")
        void permanentlyDelete() {
            // given
            Long menuId = 1L;

            Menu menu = mock(Menu.class);
            MenuVersion menuVersion = mock(MenuVersion.class);

            when(menuRepository.findById(menuId))
                    .thenReturn(Optional.of(menu));

            when(menu.getStatus())
                    .thenReturn(MenuStatus.DELETED);

            try (MockedStatic<MenuVersion> mockedMenuVersion =
                         mockStatic(MenuVersion.class)) {

                mockedMenuVersion.when(() -> MenuVersion.from(menu))
                        .thenReturn(menuVersion);

                // when
                menuService.permanentlyDelete(menuId);

                // then
                mockedMenuVersion.verify(
                        () -> MenuVersion.from(menu)
                );
            }

            verify(menuVersionRepository).save(menuVersion);
            verify(menuRepository).delete(menu);
        }

        @Test
        @DisplayName("삭제 상태가 아닌 메뉴는 영구 삭제할 수 없다")
        void permanentlyDeleteNotInTrash() {
            // given
            Long menuId = 1L;
            Menu menu = mock(Menu.class);

            when(menuRepository.findById(menuId))
                    .thenReturn(Optional.of(menu));

            when(menu.getStatus())
                    .thenReturn(MenuStatus.ACTIVE);

            // when & then
            assertThatThrownBy(
                    () -> menuService.permanentlyDelete(menuId)
            ).isInstanceOf(MenuNotInTrashException.class);

            verify(menuRepository, never()).delete(any());
            verifyNoInteractions(menuVersionRepository);
        }
    }

    @Nested
    @DisplayName("휴지통 조회")
    class TrashQueryTest {

        @Test
        @DisplayName("삭제 상태인 메뉴 목록을 조회한다")
        void findTrash() {
            // given
            Menu menu1 = mock(Menu.class);
            Menu menu2 = mock(Menu.class);

            AdminMenuResponse response1 =
                    mock(AdminMenuResponse.class);

            AdminMenuResponse response2 =
                    mock(AdminMenuResponse.class);

            when(menuRepository.findAllByStatus(MenuStatus.DELETED))
                    .thenReturn(List.of(menu1, menu2));

            when(menuMapper.toAdminMenuResponse(menu1))
                    .thenReturn(response1);

            when(menuMapper.toAdminMenuResponse(menu2))
                    .thenReturn(response2);

            // when
            List<AdminMenuResponse> result =
                    menuService.findTrash();

            // then
            assertThat(result).containsExactly(response1, response2);

            verify(menuRepository)
                    .findAllByStatus(MenuStatus.DELETED);
        }
    }
}