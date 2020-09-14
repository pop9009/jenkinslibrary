package org.devops

def SonarScan(projectName,projectDesc,projectPath){
  withSonarQubeEnv(){
    def scannerHome= "/usr/local/sonar-scanner-4.4.0.2170-linux/"
    def sonarServer= "http://172.100.25.65:9000"
    def sonarDate= sh returnStdout: true, script: 'date +%Y%m%d%H%S'
    sonarDate = sonarDate -"\n"
    sh"""
        #${scannerHome}/bin/sonar-scanner -Dsonar.host.url=${sonarServer} \
        ${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=${projectName} \
        -Dsonar.projectName=${projectName} \
        -Dsonar.projectVersion=${sonarDate} \
        -Dsonar.login=admin \
        -Dsonar.password=admin \
        -Dsonar.ws.timeout=30 \
        -Dsonar.projectDescription=${projectDesc} \
        -Dsonar.links.homepage=http://www.baidu.com \
        -Dsonar.sources=${projectPath} \
        -Dsonar.sourceEncoding=UTF-8 \
        -Dsonar.java.binaries=target/classes \
        -Dsonar.java.test.binaries=target/test-classes \
        -Dsonar.java.surefire.report=target/surefire-reports
    """
  }
}
