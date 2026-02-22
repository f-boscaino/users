package com.intesigroup.users.response;

import com.intesigroup.users.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminResponse extends OperatorResponse {
    List<Role> roleList;
}
