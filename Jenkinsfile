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
                sh 'mvn -f /var/lib/jenkins/workspace/j1/orderProductsAndReceiveOrderedProducts/pom.xml clean package'
                sh 'mvn -f /var/lib/jenkins/workspace/j1/showDeliveryReports/pom.xml clean package'
                sh 'mvn -f /var/lib/jenkins/workspace/j1/showStockReports/pom.xml clean package'
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
                sh 'docker-compose up'
            }
        }
    }
}
