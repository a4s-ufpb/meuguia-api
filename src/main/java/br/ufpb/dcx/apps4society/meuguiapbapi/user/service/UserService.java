package br.ufpb.dcx.apps4society.meuguiapbapi.user.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.domain.User;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.UpdateUserRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User with id %d not found", id)));
    }

    public User update(UpdateUserRequestData updatedData, User user) {
        User currentUser = findUserById(user.getId());

        currentUser.setEmail(updatedData.getEmail());
        currentUser.setFirstName(updatedData.getFirstName());
        currentUser.setLastName(updatedData.getLastName());

        return userRepository.save(currentUser);
    }

    public void delete(User user) {
        User currentUser = findUserById(user.getId());
        userRepository.delete(currentUser);
    }

    public void deleteUserById(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    public User updateUserById(Long id, UpdateUserRequestData updateUserRequestData) {
        User user = findUserById(id);

        user.setEmail(updateUserRequestData.getEmail());
        user.setFirstName(updateUserRequestData.getFirstName());
        user.setLastName(updateUserRequestData.getLastName());

        return userRepository.save(user);
    }

    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
