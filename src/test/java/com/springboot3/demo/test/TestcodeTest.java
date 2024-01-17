package com.springboot3.demo.test;

import static org.assertj.core.api.Assertions.assertThat;


import com.springboot3.demo.test.menu.Menu;
import com.springboot3.demo.test.menu.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestcodeTest {

//    private MenuService menuservice = new MenuService();

    @Autowired
    private MenuService menuService;


//
//    @BeforeEach
//    @Autowired
//    public void beforeEach() {
//        menuservice = new MenuService(menuRepository);
//    }

    @DisplayName("새로운 메뉴를 저장함")
    @Test
    public void saveMenuTest() {

        // given 새로운 메뉴르 저장하기 위한 준비 과정
        final String name = "아메리카노";
        final int price = 2000;

        final Menu americano = new Menu(name, price);

        //when 실제로 메뉴를 저장
        final long savedId = menuService.save(americano);

        //then  메뉴가 잘 추가되었는지 검증
        final Menu savedMenu = menuService.findById(savedId).get();
        assertThat(savedMenu.getName()).isEqualTo(name);
        assertThat(savedMenu.getPrice()).isEqualTo(price);
    }

}
