package com.webshop.models.requests;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountRequest {
    private String name;
    private String email;
    private String password;
}
