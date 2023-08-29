package com.library.libraryapp.mapper.user;

import com.library.libraryapp.entity.user.UserEntity;
import com.library.libraryapp.model.request.user.CreateAdminRequest;
import com.library.libraryapp.model.request.user.CreateUserRequest;
import com.library.libraryapp.model.response.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toUserEntity(CreateUserRequest user) {
        if (user == null) return null;

        return UserEntity.builder()
                .name(user.name())
                .phone(user.phone())
                .email(user.email())
                .address(user.address())
                .postalZip(user.postalZip())
                .country(user.country())
                .pan(user.pan())
                .expdate(user.expdate())
                .cvv(user.cvv())
                .preferedGenres(user.preferences())
                .build();
    }

    public UserEntity toUserEntity(CreateAdminRequest adminUser) {
        if (adminUser == null) return null;

        return UserEntity.builder()
                .name(adminUser.name())
                .phone(adminUser.phone())
                .email(adminUser.email())
                .build();
    }

    public UserResponse toUserResponse(UserEntity user) {
        if (user == null) return null;

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getAddress(),
                user.getPostalZip(),
                user.getCountry(),
                user.getPreferedGenres(),
                user.getPan(),
                user.getExpdate(),
                user.getCvv()
        );
    }
}
