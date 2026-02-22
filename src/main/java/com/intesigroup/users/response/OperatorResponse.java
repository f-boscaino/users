package com.intesigroup.users.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OperatorResponse extends UserResponse {
    String taxCode;
}
