package com.library.libraryapp.entity.user;

import com.library.libraryapp.entity.purchase.OrderEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String phone;

    @Column(unique = true)
    private String email;

    private String address;

    private String postalZip;

    private String country;

    private String password;

    private String pan;

    private String expdate;

    private String cvv;

    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> orders;

    @CollectionTable(name = "prefered_genres", joinColumns = @JoinColumn(name = "user_id"))
    @ElementCollection
    @Column(name = "genre")
    private List<String> preferedGenres;
}
