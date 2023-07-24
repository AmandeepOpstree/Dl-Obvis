@Library('Dl-Obvis@main') _

pipeline {
    agent any

    parameters {
        string(name: 'USER_EMAIL', defaultValue: '', description: 'Enter the user email to add to the distribution list')
    }
    stages {
        stage('Create distribution_list.txt') {
            steps {
                script {
                    writeFile file: "${workspace}/distribution_list.txt", text: ''
                }
            }
        }
        stage('Check User Existence in Distribution List') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'dl_email_secret', variable: 'DL_EMAIL_SECRET')]) {
                        def userAdded = storeDl(params.USER_EMAIL)
                        if (userAdded) {
                            mail (
                                subject: 'User Added to Distribution List',
                                body: "The user with email ${params.USER_EMAIL} has been added to the distribution list.",
                                to: params.USER_EMAIL
                            )
                        }
                    }
                }
            }
        }
    }
}
