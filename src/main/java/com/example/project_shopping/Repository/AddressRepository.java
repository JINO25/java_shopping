package com.example.project_shopping.Repository;

import com.example.project_shopping.Entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Integer> {
    Optional<Address> findByUserId(Long userId);
    List<Address> findAllByUserId(Long userId);


}
