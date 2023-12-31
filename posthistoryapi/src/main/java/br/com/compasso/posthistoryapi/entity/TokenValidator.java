package br.com.compasso.posthistoryapi.entity;

import br.com.compasso.posthistoryapi.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tb_tokens")
@Builder
@Log4j2
public class TokenValidator {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long userId;
    private String token;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean expired;
}
