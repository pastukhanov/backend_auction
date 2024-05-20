package org.luxeloot.backend.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "logo", nullable = false,columnDefinition = "varchar(255) default 'default.png'")
    private String logo;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Auction> auctions;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_card_id", referencedColumnName = "id")
    @ToString.Exclude // Avoid recursive call in toString
    @EqualsAndHashCode.Exclude // Avoid recursive call in equals and hashCode
    private PaymentCard paymentCard;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_favorites", // Name of the join table
            joinColumns = @JoinColumn(name = "user_id"), // Column referring to User
            inverseJoinColumns = @JoinColumn(name = "auction_id") // Column referring to Auction
    )
    private List<Auction> favoriteAuctions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getShortUsername() {
        return email.split("@")[0];
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
