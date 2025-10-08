package com.example.DsCatalog.services;

import com.example.DsCatalog.Dto.*;
import com.example.DsCatalog.entities.Category;
import com.example.DsCatalog.entities.Product;
import com.example.DsCatalog.entities.Role;
import com.example.DsCatalog.entities.User;
import com.example.DsCatalog.projections.UserDetailsProjection;
import com.example.DsCatalog.repository.CategoryRepository;
import com.example.DsCatalog.repository.ProductRepository;
import com.example.DsCatalog.repository.RoleRepository;
import com.example.DsCatalog.repository.UserRepository;
import com.example.DsCatalog.services.excepetions.DatabaseException;
import com.example.DsCatalog.services.excepetions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService{
    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Transactional
    public Page<UserDTO> findAllPaged(Pageable pageable){
        Page<User> result = repository.findAll(pageable);
        Page<UserDTO> list = result.map(x->new UserDTO(x));
        return list;
    }
    @Transactional
    public UserDTO findById(Long id){
        Optional<User> obj = repository.findById(id);
        User entity = obj.orElseThrow(()-> new ResourceNotFoundException("Entidade não encontrada"));
        return new UserDTO(entity);
    }
    @Transactional
    public UserDTO insert(UserInsertDTO dto){
        User entity = new User();
        copyToDtoToEntity(dto,entity);
        entity.setPassword( passwordEncoder.encode(dto.getPassword()));
        entity = repository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        try {
            User entity = repository.getReferenceById(id);
            copyToDtoToEntity(dto,entity);
            entity = repository.save(entity);
            return new UserDTO(entity);
        }
        catch (jakarta.persistence.EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyToDtoToEntity(UserDTO dto, User entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();
        for (RoleDTO roleDto : dto.getRoles()) {
            Role role = roleRepository.getReferenceById(roleDto.getId());
            entity.getRoles().add(role);
        }
    }
}
