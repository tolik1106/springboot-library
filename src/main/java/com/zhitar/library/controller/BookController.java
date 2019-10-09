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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String NAME = "name";
    private static final String AUTHOR = "author";
    private static final String ATTRIBUTE = "attribute";
    public static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, NAME);

    private final BookService bookService;

    @GetMapping
    public String getAll(
            Pageable pageable,
            Model model,
            @AuthenticationPrincipal User user,
            @RequestParam(name = NAME, required = false) String name,
            @RequestParam(name = AUTHOR, required = false) String author,
            @RequestParam(name = ATTRIBUTE, required = false) String attribute,
            HttpSession session,
            @RequestParam(name = "filter", required = false) String filter
    ) {
        pageable = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE, DEFAULT_SORT);
        Page<BookDto> page = null;
        if ("clear".equalsIgnoreCase(filter)) {
            session.removeAttribute(NAME);
            session.removeAttribute(AUTHOR);
            session.removeAttribute(ATTRIBUTE);
        } else if (name == null && author == null && attribute == null) {
            name = (String) session.getAttribute(NAME);
            author = (String) session.getAttribute(AUTHOR);
            attribute = (String) session.getAttribute(ATTRIBUTE);
        }

        if (name != null) {
            removeAttributes(session, AUTHOR, ATTRIBUTE);
            session.setAttribute(NAME, name);
            page = bookService.findByNameWithAuthor(name, pageable);
        } else if (author != null) {
            removeAttributes(session, NAME, ATTRIBUTE);
            session.setAttribute(AUTHOR, author);
            page = bookService.findByAuthor(author, pageable);
        } else if (attribute != null) {
            removeAttributes(session, NAME, AUTHOR);
            session.setAttribute(ATTRIBUTE, attribute);
            page = bookService.findByAttribute(attribute, pageable);
        } else {
            page = bookService.findAll(pageable);
            if (page.getContent().isEmpty()) {
                model.addAttribute("empty", true);
            }
        }
        if (page.getContent().isEmpty()) {
            model.addAttribute("error", true);
        }
        model.addAttribute("page", page);
        model.addAttribute("userId", user.getId());
        model.addAttribute("debtor", ControllerUtil.checkIsDebtor(page.getContent(), user));
        return "bookList";
    }

    @GetMapping("take/{id}")
    public String takeBook(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        bookService.takeBook(id, user.getId());
        return "redirect:/books";
    }

    @GetMapping("cancel/{id}")
    public String cancel(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        bookService.cancelOrder(id, user.getId());
        return "redirect:/books";
    }

    private void removeAttributes(HttpSession session, String... attributes) {
        for (String attribute : attributes) {
            session.removeAttribute(attribute);
        }
    }
}