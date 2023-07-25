import com.michelin.cio.hudson.plugins.rolestrategy.AuthorizationType
import com.michelin.cio.hudson.plugins.rolestrategy.PermissionEntry
import com.michelin.cio.hudson.plugins.rolestrategy.RoleBasedAuthorizationStrategy
import com.michelin.cio.hudson.plugins.rolestrategy.Role
import com.synopsys.arc.jenkins.plugins.rolestrategy.RoleType
import hudson.security.Permission
import jenkins.model.Jenkins

def configureRole(String roleType, String roleName, List<String> roleUsers) {
    // Define the default permission
    def DEFAULT_PERMISSION = hudson.security.Permission.READ

    // Get the Jenkins instance
    Jenkins jenkins = Jenkins.get()

    // Create a new RoleBasedAuthorizationStrategy
    def rbas = new RoleBasedAuthorizationStrategy()

    // Create the new role and assign the fixed permission (READ)
    Set<Permission> permissions = new HashSet<>();
    permissions.add(DEFAULT_PERMISSION)

    // Add the users to the new role
    for (user in roleUsers) {
        permissions.add(new PermissionEntry(AuthorizationType.USER, user.trim()))
    }

    // Convert roleType string to the corresponding RoleType enum value
    RoleType roleTypeEnum
    if (roleType == 'GLOBAL') {
        roleTypeEnum = RoleType.GLOBAL
    } else if (roleType == 'ITEM') {
        roleTypeEnum = RoleType.ITEM
    } else {
        error "Invalid role type selected. Please choose 'GLOBAL' or 'ITEM'."
    }

    // Add the new role to the appropriate role map (Global or Item)
    def roleMap
    switch (roleTypeEnum) {
        case RoleType.GLOBAL:
            roleMap = rbas.getRoleMap(RoleType.GLOBAL)
            break
        case RoleType.ITEM:
            roleMap = rbas.getRoleMap(RoleType.ITEM)
            break
        default:
            error "Invalid role type selected. Please choose 'GLOBAL' or 'ITEM'."
            break
    }

    // Create the new role
    roleMap.addRole(new Role(roleName, permissions))

    // Set the Role-Based Authorization Strategy for Jenkins
    jenkins.setAuthorizationStrategy(rbas)

    // Save the Jenkins configuration
    jenkins.save()

    // Print a message indicating successful configuration
    println("Role-Based Authorization configured successfully.")
}
