package com.tend.acd.server.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEntity {

    @NotNull
    public String username;

    @NotNull
    public String password;

    public String name = "Admin";

    public String token;

    public List<String> roles = Arrays.asList("admin");

    public String avatar = "/static/img/admin-avatar.gif";
}
