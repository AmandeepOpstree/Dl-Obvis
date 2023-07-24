def call(String userEmail) {
    def distributionList = loadDistributionList()
    if (!distributionList.contains(userEmail)) {
        distributionList.add(userEmail)
        saveDistributionList(distributionList)
        echo "User ${userEmail} added to the distribution list."
    } else {
        echo "User ${userEmail} is already in the distribution list."
    }
}

def loadDistributionList() {
    return env.DISTRIBUTION_LIST ? env.DISTRIBUTION_LIST.split(',') : []
}

def saveDistributionList(List distributionList) {
    String distributionListStr = distributionList.join(',')
    // Use Jenkins API to set the global property value
    Jenkins.instance.getGlobalNodeProperties().add(new hudson.model.EnvironmentVariablesNodeProperty(
        [new hudson.model.EnvironmentVariablesNodeProperty.Entry("DISTRIBUTION_LIST", distributionListStr)]
    ))
}
