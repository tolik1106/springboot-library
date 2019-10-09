package com.zhitar.library.controller;

import com.zhitar.library.domain.Attribute;
import com.zhitar.library.domain.Author;
import com.zhitar.library.dto.BookDto;
import com.zhitar.library.dto.UserDto;
import com.zhitar.library.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.zhitar.library.util.ControllerUtil.getErrorsMap;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private static final String REDIRECT_ADMIN_EDIT_ID = "redirect:/admin/edit/";
    private final AdminService adminService;
    @GetMapping("/delete")
    public String deleteBook(@RequestParam Integer id) {
        adminService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable Integer id, Model model) {
        BookDto book = adminService.findBook(id);
        model.addAttribute("book", book);
        return "book";
    }

    @PostMapping("/edit")
    public String editBook(@Valid BookDto bookDto, BindingResult result, ModelMap modelMap) {
        if (isErrors(bookDto, result, modelMap)) return "book";
        adminService.update(bookDto);
        return "redirect:/books";
    }

    @GetMapping("/save")
    public String saveBook(Model model) {
        model.addAttribute("book", new BookDto());
        return "book";
    }

    @PostMapping("/save")
    public String saveBook(@Valid BookDto bookDto, BindingResult result, ModelMap modelMap) {
        if (isErrors(bookDto, result, modelMap)) return "book";
        BookDto saved = adminService.save(bookDto);
        return REDIRECT_ADMIN_EDIT_ID + saved.getId();
    }

    @GetMapping("/readers")
    public String readers(Model model) {
        List<UserDto> users = adminService.findUsersWithBook();
        model.addAttribute("users", users);
        return "readers";
    }

    @PostMapping("/give")
    public String giveBook(Integer userId, Integer bookId) {
        adminService.giveBook(userId, bookId);
        return "redirect:/admin/readers";
    }

    @PostMapping("/return")
    public String returnBook(Integer userId, Integer bookId) {
        adminService.returnBook(bookId, userId);
        return "redirect:/admin/readers";
    }

    @GetMapping("/book/{bookId}/attribute/delete/{attribute}")
    public String deleteAttribute(@PathVariable Integer bookId, @PathVariable String attribute) {
        adminService.deleteAttribute(bookId, attribute);
        return REDIRECT_ADMIN_EDIT_ID + bookId;
    }

    @GetMapping("/book/{bookId}/author/delete/{author}")
    public String deleteAuthor(@PathVariable Integer bookId, @PathVariable String author) {
        adminService.deleteAuthor(bookId, author);
        return REDIRECT_ADMIN_EDIT_ID + bookId;
    }

    @PostMapping("/book/{bookId}/author/save")
    public String addAuthor(@PathVariable Integer bookId, @Valid Author author, BindingResult result, ModelMap modelMap) {
        if (result.hasErrors()) {
            Map<String, String> errorsMap = getErrorsMap(Author.class, result);
            modelMap.mergeAttributes(errorsMap);
            BookDto book = adminService.findBook(bookId);
            modelMap.addAttribute("book", book);
            return "book";
        }
        adminService.saveAuthor(bookId, author);
        return REDIRECT_ADMIN_EDIT_ID + bookId;
    }

    @PostMapping("/book/{bookId}/attribute/save")
    public String addAttribute(@PathVariable Integer bookId, @Valid Attribute attribute, BindingResult result, ModelMap modelMap) {
        if (result.hasErrors()) {
            Map<String, String> errorsMap = getErrorsMap(Attribute.class, result);
            modelMap.mergeAttributes(errorsMap);
            BookDto book = adminService.findBook(bookId);
            modelMap.addAttribute("book", book);
            return "book";
        }
        adminService.saveAttribute(bookId, attribute);
        return REDIRECT_ADMIN_EDIT_ID + bookId;
    }

    private boolean isErrors(BookDto bookDto, BindingResult result, ModelMap modelMap) {
        if (result.hasErrors()) {
            Map<String, String> errorsMap = getErrorsMap(BookDto.class, result);
            modelMap.mergeAttributes(errorsMap);
            modelMap.addAttribute("book", bookDto);
            return true;
        }
        return false;
    }
}
