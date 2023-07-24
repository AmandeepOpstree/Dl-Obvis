def call(String userEmail) {
    def distributionList = loadDistributionList()
    echo "Current Distribution List: ${distributionList}"
    
    if (!distributionList.contains(userEmail)) {
        distributionList.add(userEmail)
        saveDistributionList(distributionList)
        echo "User ${userEmail} added to the distribution list."
    } else {
        echo "User ${userEmail} is already in the distribution list."
    }

    echo "Updated Distribution List: ${loadDistributionList()}"
}

def loadDistributionList() {
    def distributionList = env.DL_EMAIL_SECRET
    if (!distributionList) {
        distributionList = []
    } else {
        distributionList = distributionList.tokenize(',')
    }
    return distributionList
}

def saveDistributionList(List distributionList) {
    env.DL_EMAIL_SECRET = distributionList.join(',')
}
