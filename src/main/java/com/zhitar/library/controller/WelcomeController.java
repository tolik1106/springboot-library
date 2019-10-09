package com.zhitar.library.controller;

import com.zhitar.library.dto.UserDto;
import com.zhitar.library.service.UserService;
import com.zhitar.library.util.ValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.Map;

import static com.zhitar.library.util.ControllerUtil.getErrorsMap;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class WelcomeController {

    private final UserService userService;

    @GetMapping
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("register", false);
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("register", true);
        return "login";
    }

    @PostMapping("/register")
    public String register(@Valid UserDto userDto, BindingResult result, SessionStatus status, ModelMap model) {
        if (result.hasErrors()) {
            Map<String, String> errorsMap = getErrorsMap(UserDto.class, result);
            model.mergeAttributes(errorsMap);
            model.addAttribute("register", true);
            return "login";
        }
        status.setComplete();
        userService.save(userDto);
        return "redirect:/login";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String duplicateEmail(DataIntegrityViolationException e, Model model) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg.contains("unique_user_email")) {
            int beginIndex = rootMsg.indexOf('\'');
            String duplicateEmail = rootMsg.substring(beginIndex, rootMsg.indexOf('\'', beginIndex + 1) + 1);
            model.addAttribute("duplicateemailError", duplicateEmail);
            model.addAttribute("register", true);
            model.addAttribute("userDto", new UserDto());
            return "login";
        }
        return null;
    }
}
