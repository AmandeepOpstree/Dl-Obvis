// Inside 'shared-lib/vars/configureRole.groovy'
package sharedlib

import com.michelin.cio.hudson.plugins.rolestrategy.AuthorizationType
import com.michelin.cio.hudson.plugins.rolestrategy.PermissionEntry
import com.michelin.cio.hudson.plugins.rolestrategy.RoleBasedAuthorizationStrategy
import com.michelin.cio.hudson.plugins.rolestrategy.Role
import com.synopsys.arc.jenkins.plugins.rolestrategy.RoleType
import hudson.security.Permission
import jenkins.model.Jenkins

@NonCPS
def configureRole(String roleType, String roleName, List<String> roleUsers) {
    // Implement the method to configure roles here
    // For example:
    def strategy = Jenkins.instance.getAuthorizationStrategy()
    if (strategy instanceof RoleBasedAuthorizationStrategy) {
        Role role = new Role(roleName, roleUsers.collect { new PermissionEntry(it, ".*", false, null) } as Set<PermissionEntry>)
        if (roleType == 'GLOBAL') {
            strategy.addRole(role, RoleType.Global)
        } else if (roleType == 'ITEM') {
            strategy.addRole(role, RoleType.Project)
        } else {
            echo "Invalid role type selected."
            error "Invalid role type selected."
        }
        strategy.save()
        echo "Role '${roleName}' with type '${roleType}' has been configured successfully."
    } else {
        echo "Role-based authorization strategy not enabled. Cannot configure roles."
        error "Role-based authorization strategy not enabled. Cannot configure roles."
    }
}
