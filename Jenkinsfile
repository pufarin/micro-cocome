pipeline {
    agent {
        docker{
            image 'adoptopenjdk/maven-openjdk11'
        }
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                sh 'cd /var/lib/jenkins/workspace/j1/orderProductsAndReceiveOrderedProducts'
                sh 'mvn clean package'
                sh 'cd ../showDeliveryReports'
                sh 'mvn clean package'
                sh 'cd ../showStockReports'
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying Some Stuff Mda 1....'
            }
        }
    }
}
