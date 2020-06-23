pipeline {
     environment {
        registrySR = "pufarin/sr"
        registryCredential = 'dockerhub'
        dockerImageSR = ''
    }
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
                //sh 'mvn -f /var/lib/jenkins/workspace/docker_repo_jenkins_push/orderProductsAndReceiveOrderedProducts/pom.xml clean package'
                //sh 'mvn -f /var/lib/jenkins/workspace/docker_repo_jenkins_push/showDeliveryReports/pom.xml clean package'
                sh 'mvn -f /var/lib/jenkins/workspace/docker_repo_jenkins_push/showStockReports/pom.xml clean package'
                echo 'Jars have been created'


            }
        }
        stage('Build Image') {
           // agent {docker 'docker/compose'}
            agent any
            options { skipDefaultCheckout() }
            steps {
                echo 'Creating the Image'
                script {
                    
           
                    dockerImageSR = docker.build("${env.registrySR}:sr-v1.0","-f ${env.WORKSPACE}/showStockReports/Dockerfile ${env.WORKSPACE}/showStockReports")
   
                }

            }
        }
        stage('Deploy Image') {
            steps{
                script {
                    docker.withRegistry( '', registryCredential ) {
                    dockerImageSR.push()
                    }
                }
            }
        }
        stage('Download and run Images') {
            agent any
            steps{
                sshagent(credentials : ['the-key']) {
            sh '''ssh gabrielkovacs@swa-kovacs-vm1.cs.univie.ac.at 'docker pull pufarin/sr:sr-v1.0' ''' 

        }
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
