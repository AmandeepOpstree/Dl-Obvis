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
    try {
        return readFile(env.WORKSPACE + '/distribution_list.txt').readLines() ?: []
    } catch (FileNotFoundException e) {
        return []
    }
}

def saveDistributionList(List distributionList) {
    writeFile(file: env.WORKSPACE + '/distribution_list.txt', text: distributionList.join('\n'))
}
