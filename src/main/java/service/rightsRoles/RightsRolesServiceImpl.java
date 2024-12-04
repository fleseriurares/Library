package service.rightsRoles;

import model.Role;
import repository.security.RightsRolesRepository;

public class RightsRolesServiceImpl implements RightsRolesService{

    private final RightsRolesRepository rightsRolesRepository;

    public RightsRolesServiceImpl(RightsRolesRepository rightsRolesRepository) {
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public void addRole(String role) {
        rightsRolesRepository.addRole(role);
    }

    @Override
    public void addRight(String right) {
        rightsRolesRepository.addRight(right);
    }

    @Override
    public Role findRoleById(Long roleId) {
        return rightsRolesRepository.findRoleById(roleId);
    }



}
