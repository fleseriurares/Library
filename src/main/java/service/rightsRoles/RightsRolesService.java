package service.rightsRoles;

import model.Role;

public interface RightsRolesService {

    void addRole(String role);

    void addRight(String right);

    Role findRoleById(Long roleId);

}
