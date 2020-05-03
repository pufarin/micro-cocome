pipeline {
    agent none
    /*
    agent {
        docker{
            image 'adoptopenjdk/maven-openjdk11'
        }
    }
    */
    stages {
        stage('Build') {
            agent {docker 'adoptopenjdk/maven-openjdk11' }
            steps {
                echo 'Building..'
                sh 'mvn -f /var/lib/jenkins/workspace/message_bus_1_to_1_db/orderProductsAndReceiveOrderedProducts/pom.xml clean package'
                sh 'mvn -f /var/lib/jenkins/workspace/message_bus_1_to_1_db/showDeliveryReports/pom.xml clean package'
                sh 'mvn -f /var/lib/jenkins/workspace/message_bus_1_to_1_db/showStockReports/pom.xml clean package'
                sh 'mvn -f /var/lib/jenkins/workspace/message_bus_1_to_1_db/api-gateway/pom.xml clean package'
                echo 'Jars have been created'


            }
        }
        stage('Deploy') {
           // agent {docker 'docker/compose'}
            agent any
            options { skipDefaultCheckout() }
            steps {
                echo 'Stop the existing application'
                sh "docker-compose -f /var/lib/jenkins/workspace/message_bus_1_to_1_db/docker-compose.yml down"

                echo 'Start the application'

                sh "docker-compose -f /var/lib/jenkins/workspace/message_bus_1_to_1_db/docker-compose.yml up -d"
            }
        }

    }
    /*
                post {
        always {
            echo 'I will always say Hello again!'
             sh "docker-compose -f /var/lib/jenkins/workspace/j1/docker-compose.yml up -d"
        }
    }
    */
}