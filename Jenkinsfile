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
                echo 'Jars have been created'
                /*
                sh 'cp /var/lib/jenkins/workspace/j1/orderProductsAndReceiveOrderedProducts/target/*.jar /home/gabriel/gabriel/deploy'
                sh 'cp /var/lib/jenkins/workspace/j1/showDeliveryReports/target/*.jar /home/gabriel/gabriel/deploy'
                sh 'cp /var/lib/jenkins/workspace/j1/showStockReports/target/*.jar /home/gabriel/gabriel/deploy'
                */
                fileCopyOperation('/var/lib/jenkins/workspace/j1/orderProductsAndReceiveOrderedProducts/target/*.jar','','/home/gabriel/gabriel/deploy',false)
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
