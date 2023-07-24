def call(String userEmail) {
    def distributionList = loadDistributionList()
    if (!distributionList.contains(userEmail)) {
        distributionList.add(userEmail)
        saveDistributionList(distributionList)
        echo "User ${userEmail} added to the distribution list."
        return true
    } else {
        echo "User ${userEmail} is already in the distribution list."
        return false
    }
}

def loadDistributionList() {
    return env.DL_EMAIL_SECRET.tokenize(',') ?: []
}

def saveDistributionList(List distributionList) {
    env.DL_EMAIL_SECRET = distributionList.join(',')
    echo "DL_EMAIL_SECRET value after saving: ${env.DL_EMAIL_SECRET}"
}

