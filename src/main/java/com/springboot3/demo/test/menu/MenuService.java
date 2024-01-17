package com.springboot3.demo.test.menu;

import com.springboot3.demo.test.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public long save(Menu menu) {
        Menu savedMenu = menuRepository.save(menu);
        return savedMenu.getId();
    }




    public Optional<Menu> findById(long savedId) {
        return menuRepository.findById(savedId);
    }
}
