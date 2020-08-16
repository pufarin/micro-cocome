pipeline {
     environment {
          registryAG = "pufarin/ag"
          registrySR = "pufarin/sr"
          registryDR = "pufarin/dr"
          registryOPROP = "pufarin/oprop"
          registryCredential = 'dockerhub'
          dockerImageSR = ''
          dockerImageDR = ''
          dockerImageOPROP = ''
          dockerImageAG = ''
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
                sh 'mvn -f /var/lib/jenkins/workspace/orchestrate_api_gateway_1_to_1_db_distributed/orderProductsAndReceiveOrderedProducts/pom.xml clean package'
                sh 'mvn -f /var/lib/jenkins/workspace/orchestrate_api_gateway_1_to_1_db_distributed/showDeliveryReports/pom.xml clean package'
                sh 'mvn -f /var/lib/jenkins/workspace/orchestrate_api_gateway_1_to_1_db_distributed/showStockReports/pom.xml clean package'
                sh 'mvn -f /var/lib/jenkins/workspace/orchestrate_api_gateway_1_to_1_db_distributed/api-gateway/pom.xml clean package'
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
                    dockerImageOPROP = docker.build("${env.registryOPROP}:oprop-v9.0","-f ${env.WORKSPACE}/orderProductsAndReceiveOrderedProducts/dockerfile ${env.WORKSPACE}/orderProductsAndReceiveOrderedProducts")
                    dockerImageDR = docker.build("${env.registryDR}:dr-v9.0","-f ${env.WORKSPACE}/showDeliveryReports/dockerfile ${env.WORKSPACE}/showDeliveryReports")
                    dockerImageSR = docker.build("${env.registrySR}:sr-v9.0","-f ${env.WORKSPACE}/showStockReports/dockerfile ${env.WORKSPACE}/showStockReports")
                    dockerImageAG = docker.build("${env.registryAG}:ag-v9.0","-f ${env.WORKSPACE}/api-gateway/dockerfile ${env.WORKSPACE}/api-gateway")
   
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
                         dockerImageAG.push()
                    }
                }
            }
        }
        stage('Download and run Images') {
            agent any
            steps{
                sshagent(credentials : ['the-key']) {
                     sh '''ssh gabrielkovacs@swa-kovacs-vm2.cs.univie.ac.at "/snap/bin/docker container stop sr || true && /snap/bin/docker container rm sr || true &&  /snap/bin/docker pull pufarin/sr:sr-v9.0 && /snap/bin/docker run -d --name sr --restart=always  -p 8085:8085 pufarin/sr:sr-v9.0" '''
                     sh '''ssh gabrielkovacs@swa-kovacs-vm3.cs.univie.ac.at "/snap/bin/docker container stop oprop || true && /snap/bin/docker container rm oprop || true && /snap/bin/docker pull pufarin/oprop:oprop-v9.0 && /snap/bin/docker run -d --name oprop --restart=always  -p 8083:8083 pufarin/oprop:oprop-v9.0" '''
                     sh '''ssh gabrielkovacs@swa-kovacs-vm4.cs.univie.ac.at "/snap/bin/docker container stop dr || true && /snap/bin/docker container rm dr || true && /snap/bin/docker pull pufarin/dr:dr-v9.0 && /snap/bin/docker run -d --name dr --restart=always  -p 8086:8086 pufarin/dr:dr-v9.0" '''
                     sh '''ssh gabrielkovacs@swa-kovacs-vm1.cs.univie.ac.at "/snap/bin/docker container stop ag || true && /snap/bin/docker container rm ag || true && /snap/bin/docker pull pufarin/ag:ag-v9.0 && /snap/bin/docker run -d --name ag --restart=always  -p 8082:8082 pufarin/ag:ag-v9.0" ''' 

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
