package com.zhitar.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    @NotBlank
    @Size(min = 2, max = 64)
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 5, max = 64)
    private String password;
    @NotBlank
    @Pattern(regexp = "^0(95|50|66|99|67|96|97|98|63|93|73)\\d{7}")
    private String phone;
}
