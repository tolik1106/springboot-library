package com.zhitar.library.controller;

import com.zhitar.library.dto.BookDto;
import com.zhitar.library.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String editBook(@RequestBody BookDto bookDto) {
        adminService.save(bookDto);
        return "redirect:/books";
    }
}
