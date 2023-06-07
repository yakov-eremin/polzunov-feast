package com.example.polzunovfeastserver.user.entity;

import com.example.polzunovfeastserver.user.uitl.UserUniqueKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = UserUniqueKey.USERNAME, columnNames = "username"),
                @UniqueConstraint(name = UserUniqueKey.EMAIL, columnNames = "email"),
                @UniqueConstraint(name = UserUniqueKey.PHONE, columnNames = "phone")
        }
)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    private String username;
    private String password;

    @Column(name = "user_name")
    private String name;

    private String phone;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
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