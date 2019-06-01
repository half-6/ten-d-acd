package com.tend.acd.server.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEntity implements Principal {

    @JsonProperty("user_id")
    public Integer userId;

    @NotNull
    public String username;

    @NotNull
    public String password;

    @JsonProperty("display_name")
    public String displayName;

    public String token;

    public List<String> roles;

    public String avatar;

    @Override
    public String getName() {
        return this.username;
    }
}
