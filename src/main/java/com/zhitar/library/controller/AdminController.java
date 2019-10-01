package com.zhitar.library.controller;

import com.zhitar.library.dto.BookDto;
import com.zhitar.library.dto.UserDto;
import com.zhitar.library.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;
    @GetMapping("/delete")
    public String deleteBook(@RequestParam Integer id) {
        adminService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/edit")
    public String editBook(@RequestParam Integer id, Model model) {
        BookDto book = adminService.findBook(id);
        model.addAttribute("book", book);
        return "book";
    }

    @PostMapping("/edit")
    public String editBook(BookDto bookDto) {
        adminService.update(bookDto);
        return "redirect:/books";
    }

    @GetMapping("/save")
    public String saveBook(Model model) {
        model.addAttribute("book", new BookDto());
        return "book";
    }

    @PostMapping("/save")
    public String saveBook(BookDto bookDto) {
        adminService.save(bookDto);
        return "redirect:/books";
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
}
