package com.simplilearn.spring.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.simplilearn.soap.CreateUserRequest;
import com.simplilearn.soap.CreateUserResponse;
import com.simplilearn.soap.DeleteUserRequest;
import com.simplilearn.soap.DeleteUserResponse;
import com.simplilearn.soap.GetUserRequest;
import com.simplilearn.soap.GetUserResponse;
import com.simplilearn.soap.GetUsersRequest;
import com.simplilearn.soap.GetUsersResponse;
import com.simplilearn.soap.UpdateUserRequest;
import com.simplilearn.soap.UpdateUserResponse;
import com.simplilearn.spring.jpa.User;
import com.simplilearn.spring.service.UserService;

@Endpoint
public class UserEndpoint {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String NAMESPACE_URI = "http://simplilearn.com/soap";

    @Autowired
    UserService userService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserRequest")
    @ResponsePayload
    public GetUserResponse getUser(@RequestPayload GetUserRequest request) {

        logger.debug("Getting User: {}", request.getUsername());

        GetUserResponse response = new GetUserResponse();
        response.setUser(userService.findUser(request.getUsername()));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUsersRequest")
    @ResponsePayload
    public GetUsersResponse getUser(@RequestPayload GetUsersRequest request) {

        logger.debug("Getting Users");

        GetUsersResponse response = new GetUsersResponse();
        response.getUsers().addAll(this.userService.listUsers());

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createUserRequest")
    @ResponsePayload
    public CreateUserResponse createUser(@RequestPayload CreateUserRequest request) {

        logger.debug("Creating User: {}", this.createUserBean(request));

        CreateUserResponse response = new CreateUserResponse();
        response.setUser(this.userService.createUser(this.createUserBean(request)));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateUserRequest")
    @ResponsePayload
    public UpdateUserResponse updateUser(@RequestPayload UpdateUserRequest request) {

        logger.debug("Updating User: {}", this.createUserBean(request));

        UpdateUserResponse response = new UpdateUserResponse();
        response.setUser(this.userService.updateUser(this.createUserBean(request)));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserRequest")
    @ResponsePayload
    public DeleteUserResponse deleteUser(@RequestPayload DeleteUserRequest request) {

        logger.debug("Deleting User, idUser: {}", request.getIdUser());

        this.userService.deleteUser(request.getIdUser());

        return new DeleteUserResponse();
    }

    private User createUserBean(CreateUserRequest req) {

        return new User(0,req.getUsername(),req.getPassword(), req.getFirstName(),
                            req.getLastName(), req.getBirth().toGregorianCalendar().getTime(), "A");
    }

    private User createUserBean(UpdateUserRequest req) {

        return new User(req.getIdUser(),req.getUsername(),"", req.getFirstName(),
                req.getLastName(), req.getBirth().toGregorianCalendar().getTime(), "");
    }
}
