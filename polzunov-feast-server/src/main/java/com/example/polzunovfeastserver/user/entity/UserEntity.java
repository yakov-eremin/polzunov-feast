package com.example.polzunovfeastserver.user.entity;

import com.example.polzunovfeastserver.user.uitl.UserTableKeys;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = UserTableKeys.UNIQUE_EMAIL, columnNames = "email"),
                @UniqueConstraint(name = UserTableKeys.UNIQUE_PHONE, columnNames = "phone")
        }
)
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    private String password;

    @Column(name = "user_name")
    private String name;

    private String phone;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * This method needs to implemented even though our users do not have usernames, so it returns email just like {@link #getEmail()}.
     *
     * @return email of this user
     */
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}