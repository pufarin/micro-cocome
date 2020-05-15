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
                sh 'mvn -f /var/lib/jenkins/workspace/api_gateway_one_db/common/pom.xml clean install'
                sh 'mvn -f /var/lib/jenkins/workspace/api_gateway_one_db/orderProductsAndReceiveOrderedProducts/pom.xml clean package'
                sh 'mvn -f /var/lib/jenkins/workspace/api_gateway_one_db/showDeliveryReports/pom.xml clean package'
                sh 'mvn -f /var/lib/jenkins/workspace/api_gateway_one_db/showStockReports/pom.xml clean package'
                sh 'mvn -f /var/lib/jenkins/workspace/api_gateway_one_db/api-gateway/pom.xml clean package'
                echo 'Jars have been created'


            }
        }
        stage('Deploy') {
           // agent {docker 'docker/compose'}
            agent any
            options { skipDefaultCheckout() }
            steps {
                echo 'Stop the existing application'
                sh "docker-compose -f /var/lib/jenkins/workspace/api_gateway_one_db/docker-compose.yml down"

                echo 'Build the images'
                sh "docker-compose  -f /var/lib/jenkins/workspace/api_gateway_one_db/docker-compose.yml build --no-cache"

                echo 'Start the application'
                sh "docker-compose  -f /var/lib/jenkins/workspace/api_gateway_one_db/docker-compose.yml up -d"
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