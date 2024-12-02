package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.User;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.UpdateUserRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.UserDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import br.ufpb.dcx.apps4society.meuguiapbapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO update(UpdateUserRequestData updatedData, User user) {
        User currentUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ObjectNotFoundException("User with id " + user.getId() + " not found"));

        currentUser.setEmail(updatedData.getEmail());
        currentUser.setFirstName(updatedData.getFirstName());
        currentUser.setLastName(updatedData.getLastName());

        userRepository.save(currentUser);
        return UserDTO.builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .firstName(currentUser.getFirstName())
                .lastName(currentUser.getLastName())
                .build();
    }

    public void delete(User user) {
        User currentUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ObjectNotFoundException("User with id " + user.getId() + " not found"));

        userRepository.delete(currentUser);
    }
}
