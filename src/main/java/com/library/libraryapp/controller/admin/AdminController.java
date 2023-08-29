package com.library.libraryapp.controller.admin;

import com.library.libraryapp.domain.core.sales.SalesReportService;
import com.library.libraryapp.domain.core.user.UserService;
import com.library.libraryapp.model.request.user.CreateAdminRequest;
import com.library.libraryapp.model.response.sales.SalesReportResponse;
import com.library.libraryapp.model.response.user.UserResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.library.libraryapp.domain.constants.UserRoles.ROLE_ADMIN;
import static com.library.libraryapp.domain.constants.UserRoles.ROLE_SUPER_ADMIN;

@RestController
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
public class AdminController {
    private final SalesReportService salesReportService;
    private final UserService userService;

    @PostMapping
    @RolesAllowed(ROLE_SUPER_ADMIN)
    public ResponseEntity<UserResponse> registerAdmin(@RequestBody @Valid CreateAdminRequest createAdminRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.signUp(createAdminRequest));
    }

    @GetMapping("/sales/report")
    @RolesAllowed(ROLE_SUPER_ADMIN)
    public ResponseEntity<SalesReportResponse> getSalesReport() {
        return ResponseEntity.status(HttpStatus.OK).body(salesReportService.getSalesReport());
    }

    @GetMapping("/users/{id}")
    @RolesAllowed({ROLE_SUPER_ADMIN, ROLE_ADMIN})
    public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }
}
