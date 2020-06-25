pipeline {
     environment {
          registrySR = "pufarin/sr"
          registryDR = "pufarin/dr"
          registryOPROP = "pufarin/oprop"
          registryCredential = 'dockerhub'
          dockerImageSR = ''
          dockerImageDR = ''
          dockerImageOPROP = ''
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
                sh 'mvn -f /var/lib/jenkins/workspace/master_one_db_distributed/common/pom.xml clean install'
                sh 'mvn -f /var/lib/jenkins/workspace/master_one_db_distributed/orderProductsAndReceiveOrderedProducts/pom.xml clean package'
                sh 'mvn -f /var/lib/jenkins/workspace/master_one_db_distributed/showDeliveryReports/pom.xml clean package'
                sh 'mvn -f /var/lib/jenkins/workspace/master_one_db_distributed/showStockReports/pom.xml clean package'
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
                    dockerImageOPROP = docker.build("${env.registryOPROP}:oprop-v2.0","-f ${env.WORKSPACE}/orderProductsAndReceiveOrderedProducts/dockerfile ${env.WORKSPACE}/orderProductsAndReceiveOrderedProducts")
                    dockerImageDR = docker.build("${env.registryDR}:dr-v2.0","-f ${env.WORKSPACE}/showDeliveryReports/dockerfile ${env.WORKSPACE}/showDeliveryReports")
                    dockerImageSR = docker.build("${env.registrySR}:sr-v2.0","-f ${env.WORKSPACE}/showStockReports/dockerfile ${env.WORKSPACE}/showStockReports")
   
                }

            }
        }
        stage('Push Image') {
            steps{
                script {
                         docker.withRegistry( '', registryCredential ) {
                         dockerImageOPROP.push()
                         dockerImageDR.push()
                         dockerImageSR.push()
                    }
                }
            }
        }
        stage('Download and run Images') {
            agent any
            steps{
                sshagent(credentials : ['the-key']) {
                     sh '''ssh gabrielkovacs@swa-kovacs-vm2.cs.univie.ac.at "/snap/bin/docker pull pufarin/sr:sr-v2.0 | /snap/bin/docker run -d --restart=always  -p 8085:8085 pufarin/sr:sr-v2.0" '''
                     sh '''ssh gabrielkovacs@swa-kovacs-vm3.cs.univie.ac.at "/snap/bin/docker pull pufarin/oprop:oprop-v2.0 | /snap/bin/docker run -d --restart=always  -p 8083:8083 pufarin/oprop:oprop-v2.0" '''
                     sh '''ssh gabrielkovacs@swa-kovacs-vm4.cs.univie.ac.at "/snap/bin/docker pull pufarin/dr:dr-v2.0 | /snap/bin/docker run -d --restart=always  -p 8086:8086 pufarin/dr:dr-v2.0" ''' 

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
