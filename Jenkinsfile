#!groovy

pipeline {
    agent any
    environment {
        registry = "kostiakorzh/demoshop-order-admin-service-dev"
        registryCredential = 'dockerhub'
        dockerImage = ''
    }
    stages {
        stage('Build for docker'){
            steps{
                script{
                    sh 'rm -rf ./build/'
                    sh 'sudo ./gradlew installDist'
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    dockerImage = docker.build registry
                }
            }
        }
        stage('Push to dockerhub') {
            steps {
                script {
                    docker.withRegistry('', registryCredential) {
                        dockerImage.push("latest")
                    }
                }
            }
        }
        stage("Deploy to docker") {
            steps {
                sh 'docker rm -f order-admin-dev-container'
                sh 'docker rmi kostiakorzh/demoshop-order-admin-service-dev'
                sh 'docker run -p 8090:8090 -d --name order-admin-dev-container -e MYSQL_HOST=demo-shop.c9pmrkdcjaav.eu-central-1.rds.amazonaws.com -e MYSQL_ROOT_PASSWORD=root1234 kostiakorzh/demoshop-order-admin-service-dev'
            }
        }
    }
}