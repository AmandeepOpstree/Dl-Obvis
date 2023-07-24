def call(String userEmail) {
    def distributionList = loadDistributionList()
    if (!distributionList.contains(userEmail)) {
        distributionList.add(userEmail)
        saveDistributionList(distributionList)
    }
}

def loadDistributionList() {
    return env.DISTRIBUTION_LIST ? env.DISTRIBUTION_LIST.split(',') : []
}

def saveDistributionList(List distributionList) {
    env.DISTRIBUTION_LIST = distributionList.join(',')
}
