package com.zhitar.library.controller;

import com.zhitar.library.domain.User;
import com.zhitar.library.dto.BookDto;
import com.zhitar.library.service.BookService;
import com.zhitar.library.util.ControllerUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "name");
    private final BookService bookService;

    @GetMapping
    public String getAll(Pageable pageable, Model model, @AuthenticationPrincipal User user) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE, DEFAULT_SORT);
        Page<BookDto> page = bookService.findAll(pageRequest);
        if (page.getContent().isEmpty()) {
            model.addAttribute("error", "");
        }
        model.addAttribute("page", page);
        model.addAttribute("userId", user.getId());
        model.addAttribute("debtor", ControllerUtil.checkIsDebtor(page.getContent(), user));
        return "bookList";
    }

    @GetMapping("take/{id}")
    public String takeBook(@PathVariable Integer id, @AuthenticationPrincipal User user, Model model) {
        bookService.takeBook(id, user.getId());
        return "redirect:/books";
    }

    @GetMapping("cancel/{id}")
    public String cancel(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        bookService.cancelOrder(id, user.getId());
        return "redirect:/books";
    }
}