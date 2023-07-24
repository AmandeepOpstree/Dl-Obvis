pipeline {
    agent any

    parameters {
        string(name: 'USER_EMAIL', defaultValue: '', description: 'Enter the user email to add to the distribution list')
    }

    // Define a global variable to store the list of users added to the distribution list
    environment {
        DISTRIBUTION_LIST = "amandeep.singh@opstree.com,shubham.gupta@opstree.com" 
    }

    stages {
        stage('Check User Existence in Distribution List') {
            steps {
                script {
                    // Split the comma-separated list into a Groovy list
                    def distributionList = env.DISTRIBUTION_LIST.split(',')

                    // Check if the user email parameter is empty
                    if (params.USER_EMAIL.trim() == '') {
                        echo "The user email cannot be empty. Please provide a valid email."
                        currentBuild.result = 'FAILURE'
                    } 
                    // Check if the user is already in the distribution list
                    else if (distributionList.contains(params.USER_EMAIL)) {
                        echo "User ${params.USER_EMAIL} is already in the distribution list."
                        mail (
                            subject: 'User Already Added to Distribution List',
                            body: "The user with email ${params.USER_EMAIL} is already in the distribution list.",
                            to: params.USER_EMAIL
                        )
                    } 
                    else {
                        echo "User ${params.USER_EMAIL} is not in the distribution list. Proceeding to add."

                        // Pause the pipeline and prompt the user for approval to proceed with the addition
                        def approval = input(
                            id: 'AddToListApproval',
                            message: "Do you want to add ${params.USER_EMAIL} to the distribution list?",
                            parameters: [[$class: 'BooleanParameterDefinition', defaultValue: false, description: 'Approve?', name: 'APPROVED']]
                        )

                        // Check if the approval was given
                        if (approval == true) {
                            // Add the user email to the list
                            env.DISTRIBUTION_LIST += ",${params.USER_EMAIL}"
                            echo "User ${params.USER_EMAIL} added to the distribution list."
                            mail (
                                subject: 'User Added to Distribution List',
                                body: "The user with email ${params.USER_EMAIL} has been added to the distribution list.",
                                to: params.USER_EMAIL
                            )
                        } else {
                            echo "Approval not granted. User email will not be added to the distribution list."
                            currentBuild.result = 'ABORTED' // Abort the build if approval is not granted
                        }
                    }
                }
            }
        }
    }
}
