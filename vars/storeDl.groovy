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
    return env.DL_EMAIL_SECRET ? env.DL_EMAIL_SECRET.split(',') : []
}

def saveDistributionList(List distributionList) {
    env.DL_EMAIL_SECRET = distributionList.join(',')
}
