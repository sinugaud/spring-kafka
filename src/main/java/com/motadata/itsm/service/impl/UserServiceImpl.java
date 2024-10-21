package com.motadata.itsm.service.impl;

import com.motadata.itsm.entity.User;
import com.motadata.itsm.repository.AddressRepository;
import com.motadata.itsm.repository.UserRepository;
import com.motadata.itsm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private final UserRepository userRepository;


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public UserServiceImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    //saving user
    @Override
    public User saveUser(User user) {
        kafkaTemplate.send("audit-save", "User save ");
        return userRepository.save(user);
    }

    @Override
    //deleting user by id

    public String deleteUser(Long id) {
        User existingUser = userRepository.findById(id).orElseThrow();
        //first we are fetching user by id

        userRepository.deleteById(id);
        //with the help of jpa we are delete object using id
        kafkaTemplate.send("audit-delete", "User delete  ");
        //once user deleted we are pushing to kafka topic


        return "User delete successfully with id " + id;
        //returning successful message for better understanding

    }

    @Override
    //update user
    public User updateUser(User user, Long id) {

        User existingUser = userRepository.findById(id).orElseThrow();
        //First we are fetching user by id if user existed with particulate id than we update the user object

        existingUser.setName(user.getName());
        existingUser.setAge((user.getAge()));
        existingUser.setAddress(user.getAddress());

        kafkaTemplate.send("audit-update", "User update ");
        //sending update audit to topic

        return userRepository.save(existingUser);


    }
}
